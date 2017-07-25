package mujava.app;

import java.io.PrintStream;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Flags {
	private TreeMap<Character, List<String>> optionalFlags;
	private TreeMap<Character, List<String>> requiredFlags;
	private TreeMap<Character, List<Character>> flagDependencies;
	private Set<Character> noValueFlags; //these flags doen's have a value
	private List<Character> flagsFound = new LinkedList<Character>();
	private Set<Character> overridingFlags; //these flags have no values and override the use required and optional flags
	private PrintStream errorOutput;
	private boolean overridingFlagFound = false;
	
	public Flags(PrintStream output) {
		this.optionalFlags = new TreeMap<Character, List<String>>();
		this.requiredFlags = new TreeMap<Character, List<String>>();
		this.noValueFlags = new TreeSet<Character>();
		this.flagDependencies = new TreeMap<Character, List<Character>>();
		this.overridingFlags = new TreeSet<Character>();
		this.errorOutput = output;
	}

	public boolean setOptionalFlag(char flag) {
		if (this.optionalFlags.containsKey(flag) || this.requiredFlags.containsKey(flag) || this.noValueFlags.contains(flag)) {
			return false;
		}
		this.optionalFlags.put(flag, new LinkedList<String>());
		return true;
	}
	
	public boolean setRequiredFlag(char flag) {
		if (this.requiredFlags.containsKey(flag) || this.optionalFlags.containsKey(flag) || this.noValueFlags.contains(flag)) {
			return false;
		}
		this.requiredFlags.put(flag, new LinkedList<String>());
		return true;
	}
	
	public boolean setOverridingFlag(char flag) {
		if (this.overridingFlags.contains(flag)) {
			return false;
		}
		this.overridingFlags.add(flag);
		return true;
	}
	
	public boolean setNoValueFlag(char flag) {
		if (this.requiredFlags.containsKey(flag) || this.optionalFlags.containsKey(flag) || this.noValueFlags.contains(flag)) {
			return false;
		}
		this.noValueFlags.add(flag);
		return true;
	}
	
	public boolean setDependence(Character dependent, Character dependence) {
		if (!this.flagExist(dependent)||!this.flagExist(dependence)) {
			return false;
		}
		if (this.flagDependencies.containsKey(dependent)) {
			List<Character> dependencies = this.flagDependencies.get(dependent);
			if (dependencies.contains(dependence)) {
				return false;
			}
			dependencies.add(dependence);
		}
		List<Character> nl = new LinkedList<Character>();
		this.flagDependencies.put(dependent, nl);
		return true;
	}
	
	public List<String> getFlagValues(char flag) {
		if (this.optionalFlags.containsKey(flag)) {
			return this.optionalFlags.get(flag);
		}
		if (this.requiredFlags.containsKey(flag)) {
			return this.requiredFlags.get(flag);
		}
		return null;
	}
	
	public boolean validateInput(String[] input) {
		Character currentFlag = null;
		boolean valuesAdded = false;
		int requiredFlagsScanned = 0;
		for (String value : input) {
			if (value.startsWith("-")) {
				if (value.length() == 2) {
					if (currentFlag != null) {
						if (!valuesAdded) {
							this.errorOutput.println("A new flag detected ("+value+") but was expecting values for previous flag\n");
						}
						valuesAdded = false;
					}
					currentFlag = value.charAt(1);
					if (this.noValueFlags.contains(currentFlag)) {
						flagsFound.add(currentFlag);
						currentFlag = null;
						continue;
					}
					if (this.overridingFlags.contains(value.charAt(1))) {
						if (input.length != 1) {
							this.errorOutput.println("Can't use an overriding flag ("+value+") with values o more flags\n");
						}
						this.overridingFlagFound = true;
					}
					if (isRequired(currentFlag)) requiredFlagsScanned++;
					flagsFound.add(currentFlag);
					if (!validFlag(currentFlag)) {
						this.errorOutput.println("Unsupported flag ("+value+")\n");
					}
				} else {
					this.errorOutput.println("Flag has more than one letter : ("+ value +")\n");
				}
			} else {
				if (currentFlag == null) {
					this.errorOutput.println("Expected a flag got ("+value+") instead\n");
				}
				List<String> fargs;
				if (isRequired(currentFlag)) {
					if (this.requiredFlags.get(currentFlag) == null) {
						fargs = new LinkedList<String>();
					} else {
						fargs = this.requiredFlags.get(currentFlag);
					}
					fargs.add(value);
					this.requiredFlags.put(currentFlag, fargs);
				} else {
					if (this.optionalFlags.get(currentFlag) == null) {
						fargs = new LinkedList<String>();
					} else {
						fargs = this.optionalFlags.get(currentFlag);
					}
					fargs.add(value);
					this.optionalFlags.put(currentFlag, fargs);
				}
				valuesAdded = true;
			}
		}
		if (!this.overridingFlagFound && requiredFlagsScanned < this.requiredFlags.size()) {
			this.errorOutput.println("Not all required flags are present\n" +
					"Required flags are : " + this.requiredFlags.keySet().toString()+'\n');
			return false;
		}
		if (currentFlag != null && (valuesAdded == false && !this.overridingFlagFound)) {
			this.errorOutput.println("Missing values for flag("+'-'+currentFlag+")\n");
			return false;
		}
		return checkDependencies(flagsFound);
	}
	
	public Set<Character> getRequiredFlags() {
		return this.requiredFlags.keySet();
	}
	
	public Set<Character> getOptionalFlags() {
		return this.optionalFlags.keySet();
	}
	
	public Set<Character> getOverridingFlags() {
		return this.overridingFlags;
	}
	
	public Set<Character> getNoValueFlags() {
		return this.noValueFlags;
	}
	
	public boolean flagExist(char flag) {
		return this.flagsFound.contains(flag);
	}
	
	private boolean validFlag(char flag) {
		return this.optionalFlags.containsKey(flag) || this.requiredFlags.containsKey(flag) || this.overridingFlags.contains(flag) || this.noValueFlags.contains(flag);
	}
	
	
	public boolean isRequired(char flag) {
		return this.requiredFlags.containsKey(flag);
	}
	
	public boolean overridingFlagFound() {
		return this.overridingFlagFound;
	}
	
	private boolean checkDependencies(List<Character> flags) {
		for (Character c1 : flags) {
			if (this.flagDependencies.containsKey(c1)) {
				for (Character c2 : this.flagDependencies.get(c1)) {
					if (!flags.contains(c2)) {
						this.errorOutput.println("Dependency not met, have ("+c1+") need ("+c2+")\n");
						return false;
					}
				}
			}
		}
		return true;
	}

}
