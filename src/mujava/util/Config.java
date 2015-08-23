package mujava.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.lang.enums.EnumUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import main.api.DependencyScanner;
import mujava.api.Mutant;
import mujava.app.Core;
import mujava.app.MutatorsInfo;

public class Config {
	//basic folders
	private String originalSrcDir;
	private String originalBinDir;
	private String mutantsOutputFolder;
	//basic folders
	
	//basic mutation values
	private String classToMutate;
	private Set<String> methodsToMutate;
	//basic mutation values
	
	//mutation operators
	private Set<Mutant> operators;
	//mutation operators
	
	//advanced mutation options
	private boolean fullVerboseMode;
	private boolean allowClassMutations;
	private boolean allowFieldMutations;
	private Set<String> bannedFields;
	private Set<String> bannedMethods;
	private boolean ignoreMutGenLimit;
	private Set<String> allowedPackagesToReload;
	private boolean allowNumericLiteralVariations;
	private boolean disablePrimitiveToObjectAssignments;
	private boolean wrapPrimitiveToObjectAssignments;
	private boolean applyRefinedPRVOInMethodCallStatements;
	//advanced mutation options
	
	//mutation score
	private boolean runMutationScore;
	private String testsBinDir;
	private Set<String> testClasses;
	//mutation score
	
	//mutation score advanced options
	private boolean quickDeath;
	//mutation score advanced options
	
	//auxiliary values
	private List<Method> methodsInClassToMutate;
	private List<String> classesInOriginalBinDir;
	private List<String> packagesInOriginalBinDir;
	private List<String> testClassesInTestsBinDir;
	//auxiliary values
	

	public Config(String originalSourceDir, String originalBinDir, String mutantsOutputFolder) {
		this.originalSrcDir = addTrailingSeparator(originalSourceDir);
		this.originalBinDir = addTrailingSeparator(originalBinDir);
		this.mutantsOutputFolder = addTrailingSeparator(mutantsOutputFolder);
		clearMethodsToMutate();
		clearOperators();
		fullVerboseMode(false);
		allowClassMutations(false);
		allowFieldMutations(false);
		clearMethodsInsideClassToMutate();
		clearClassesInOriginalBinDir();
		clearPackagesInOriginalBinDir();
		clearBannedMethods();
		clearBannedFields();
		clearPackagesToReload();
		clearTestClassesInTestsBinDir();
		clearTestClasses();
	}
	
	public void classToMutate(String classToMutate) {
		this.classToMutate = classToMutate;
		clearMethodsToMutate();
		clearMethodsInsideClassToMutate();
	}
	
	public String classToMutate() {
		return this.classToMutate;
	}
	
	public String originalSourceDir() {
		return this.originalSrcDir;
	}
	
	public String originalBinDir() {
		return this.originalBinDir;
	}
	
	public String mutantsOutputFolder() {
		return this.mutantsOutputFolder;
	}
	
	public void addMethodToMutate(String method) {
		if (!this.methodsToMutate.contains(method)) this.methodsToMutate.add(method);
	}
	
	public void removeMethodToMutate(String method) {
		if (this.methodsToMutate.contains(method)) this.methodsToMutate.remove(method);
	}
	
	public void clearMethodsToMutate() {
		if (this.methodsToMutate == null) {
			this.methodsToMutate = new TreeSet<String>();
		} else {
			this.methodsToMutate.clear();
		}
	}
	
	public Set<String> methodToMutate() {
		return this.methodsToMutate;
	}
	
	public boolean addOperator(String op) {
		if (EnumUtils.getEnum(Mutant.class, op) != null) {
			this.operators.add(Mutant.valueOf(op));
			return true;
		} else {
			return false;
		}
	}
	
	public boolean removeOperator(String op) {
		if (EnumUtils.getEnum(Mutant.class, op) != null) {
			this.operators.remove(Mutant.valueOf(op));
			return true;
		} else {
			return false;
		}
	}
	
	public void clearOperators() {
		if (this.operators == null) {
			this.operators = new TreeSet<Mutant>();
		} else {
			this.operators.clear();
		}
	}
	
	public Set<Mutant> operators() {
		return this.operators;
	}
	
	public void fullVerboseMode(boolean enable) {
		this.fullVerboseMode = enable;
	}
	
	public boolean fullVerboseMode() {
		return this.fullVerboseMode;
	}
	
	public void allowClassMutations(boolean allow) {
		this.allowClassMutations = allow;
	}
	
	public boolean allowClassMutations() {
		return this.allowClassMutations;
	}
	
	public void allowFieldMutations(boolean allow) {
		this.allowFieldMutations = allow;
	}
	
	public boolean allowFieldMutations() {
		return this.allowFieldMutations;
	}
	
	public void addBannedMethod(String m) {
		this.bannedMethods.add(m);
	}
	
	public void delBannedMethod(String m) {
		this.bannedMethods.remove(m);
	}
	
	public void clearBannedMethods() {
		if (this.bannedMethods == null) {
			this.bannedMethods = new TreeSet<String>();
		} else {
			this.bannedMethods.clear();
		}
	}
	
	public Set<String> bannedMethods() {
		return this.bannedMethods;
	}
	
	public void addBannedField(String f) {
		this.bannedFields.add(f);
	}
	
	public void delBannedField(String f) {
		this.bannedFields.remove(f);
	}
	
	public void clearBannedFields() {
		if (this.bannedFields == null) {
			this.bannedFields = new TreeSet<String>();
		} else {
			this.bannedFields.clear();
		}
	}
	
	public boolean ignoreMutGenLimit() {
		return this.ignoreMutGenLimit;
	}
	
	public void ignoreMutGenLimit(boolean ignore) {
		this.ignoreMutGenLimit = ignore;
	}
	
	public void addPackageToReload(String p) {
		this.allowedPackagesToReload.add(p);
	}
	
	public void delPackageToReload(String p) {
		this.allowedPackagesToReload.remove(p);
	}
	
	public void clearPackagesToReload() {
		if (this.allowedPackagesToReload == null) {
			this.allowedPackagesToReload = new TreeSet<String>();
		} else {
			this.allowedPackagesToReload.clear();
		}
	}
	
	public Set<String> allowedPackagesToReload() {
		return this.allowedPackagesToReload;
	}
	
	public void allowNumericLiteralVariations(boolean allow) {
		this.allowNumericLiteralVariations = allow;
	}
	
	public boolean allowNumericLiteralVariations() {
		return this.allowNumericLiteralVariations;
	}
	
	public void runMutationScore(boolean run) {
		this.runMutationScore = run;
	}
	
	public boolean runMutationScore() {
		return this.runMutationScore;
	}
	
	public String testsBinDir() {
		return this.testsBinDir;
	}
	
	public void testsBinDir(String dir) {
		this.testsBinDir = addTrailingSeparator(dir);
	}
	
	public void addTestClass(String c) {
		this.testClasses.add(c);
	}
	
	public void delTestClass(String c) {
		this.testClasses.remove(c);
	}
	
	public void clearTestClasses() {
		if (this.testClasses == null) {
			this.testClasses = new TreeSet<String>();
		} else {
			this.testClasses.clear();
		}
	}
	
	public void quickDeath(boolean enable) {
		this.quickDeath = enable;
	}
	
	public boolean quickDeath() {
		return this.quickDeath;
	}
	
	public boolean allowPrimitiveToObjectAssignments() {
		return this.disablePrimitiveToObjectAssignments;
	}
	
	public void allowPrimitiveToObjectAssignments(boolean b) {
		this.disablePrimitiveToObjectAssignments = b;
	}
	
	public boolean wrapPrimitiveToObjectAssignments() {
		return this.wrapPrimitiveToObjectAssignments;
	}
	
	public void wrapPrimitiveToObjectAssignments(boolean b) {
		this.wrapPrimitiveToObjectAssignments = b;
	}
	
	public boolean applyRefinedPRVOInMethodCallStatements() {
		return this.applyRefinedPRVOInMethodCallStatements;
	}
	
	public void applyRefinedPRVOInMethodCallStatements(boolean b) {
		this.applyRefinedPRVOInMethodCallStatements = b;
	}
	
	public List<Method> getClassMethods() {
		if (this.methodsInClassToMutate != null && !this.methodsInClassToMutate.isEmpty()) return this.methodsInClassToMutate;
		List<Method> classMethods = new LinkedList<Method>();
		if (verifyDirectory(this.originalBinDir) && verifyFile(sumPaths(this.originalBinDir, classNameAsPath(this.classToMutate))+".class")) {
			Class<?> classToMutate = loadClass(this.classToMutate, this.originalBinDir);
			if (classToMutate != null) {
				for (Method m : classToMutate.getDeclaredMethods()) {
					classMethods.add(m);
				}
			}
		}
		this.methodsInClassToMutate.addAll(classMethods);
		return classMethods;
	}
	
	public List<String> getClassesInOriginalBinDir() {
		if (this.classesInOriginalBinDir != null && !this.classesInOriginalBinDir.isEmpty()) return this.classesInOriginalBinDir;
		List<String> classesInOriginalBinDir = new LinkedList<String>();
		try {
			DependencyScanner depScanner = new DependencyScanner(toPath(addTrailingSeparator(this.originalBinDir)));
			classesInOriginalBinDir.addAll(depScanner.getDependencyMap().getClasses());
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.classesInOriginalBinDir = classesInOriginalBinDir;
		return classesInOriginalBinDir;
	}
	
	public List<String> getTestClassesInTestsBinDir() {
		if (this.testClassesInTestsBinDir != null && !this.testClassesInTestsBinDir.isEmpty()) return this.testClassesInTestsBinDir;
		List<String> testClassesInTestsBinDir = new LinkedList<String>();
		try {
			DependencyScanner depScanner = new DependencyScanner(toPath(addTrailingSeparator(this.testsBinDir)));
			for (String c : depScanner.getDependencyMap().getClasses()) {
				if (isTestClass(loadClass(c, this.testsBinDir))) {
					testClassesInTestsBinDir.add(c);
				}
			}
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.testClassesInTestsBinDir = testClassesInTestsBinDir;
		return testClassesInTestsBinDir;
	}
	
	public List<String> getpackagesInOriginalBinDir() {
		if (this.packagesInOriginalBinDir != null && !this.packagesInOriginalBinDir.isEmpty()) return this.packagesInOriginalBinDir;
		List<String> packagesInOriginalBinDir = new LinkedList<String>();
		try {
			DependencyScanner depScanner = new DependencyScanner(toPath(addTrailingSeparator(this.originalBinDir)));
			packagesInOriginalBinDir.addAll(depScanner.getScannedPackages());
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.packagesInOriginalBinDir = packagesInOriginalBinDir;
		return packagesInOriginalBinDir;
	}
	
	//TODO: if validated return null, else return error message
	public String validate() {
		//verify folders
		if (!verifyDirectory(addTrailingSeparator(this.originalSrcDir))) return "Invalid directory (Original source folder) : " + this.originalSrcDir;
		if (!verifyDirectory(addTrailingSeparator(this.originalBinDir))) return "Invalid directory (Original binary folder) : " + this.originalBinDir;
		if (this.testsBinDir != null && !verifyDirectory(addTrailingSeparator(this.testsBinDir))) return "Invalid directory (Tests binary folder) : " + this.testsBinDir;
		if (this.testsBinDir == null && this.runMutationScore) return "Mutation score is enabled but no tests binary folder has been selected";
		if (!getClassesInOriginalBinDir().contains(this.classToMutate)) return "Class " + this.classToMutate + " can't be found inside " + this.originalBinDir;
		if (this.runMutationScore && (this.testClasses == null || this.testClasses.isEmpty())) return "Mutation score is enabled but no test classes has been selected";
		if (this.runMutationScore) {
			for (String t : this.testClasses) {
				if (!getTestClassesInTestsBinDir().contains(t)) {
					return "Class " + t + " can't be found inside " + this.testsBinDir;
				}
				if (!isTestClass(loadClass(t, addTrailingSeparator(this.testsBinDir)))) {
					return "Class " + t + " is not a valid test class";
				}
			}
		}
		for (Mutant m : this.operators) {
			if (!MutatorsInfo.getInstance().isSupported(m)) return "Operator " + m.toString() + " is not supported";
		}
		for (String method : this.methodsToMutate) {
			boolean found = false;
			for (Method m : this.methodsInClassToMutate) {
				if (m.getName().compareTo(method) == 0) {
					found = true;
				}
				if (!found) return "Method " + method + " doesn't belong to class " + this.classToMutate;
			}
		}
		for (String apr : this.allowedPackagesToReload) {
			if (!this.packagesInOriginalBinDir.contains(apr)) return "Package " + apr + " is not present in " + this.originalBinDir;
		}
		if (!this.quickDeath && this.runMutationScore) return "Quick death option is enabled but mutation score is not";
		return null;
	}
	
	private Path toPath(String p) {
		File path = new File(p);
		return path.toPath();
	}
	
	private String classNameAsPath(String cname) {
		return cname.replaceAll("\\.", Core.SEPARATOR);
	}
	
	private String addTrailingSeparator(String path) {
		if (path.endsWith(Core.SEPARATOR)) {
			return path;
		} else {
			return path + Core.SEPARATOR;
		}
	}
	
	private String sumPaths(String p1, String p2) {
		return addTrailingSeparator(p1) + p2;
	}
	
	private boolean verifyDirectory(String dir) {
		File directory = new File(dir);
		return directory.exists() && directory.isDirectory();
	}
	
	private boolean verifyFile(String f) {
		File file = new File(f);
		return file.exists() && file.isFile();
	}
	
	private Class<?> loadClass(String className, String fromPath) {
		File file = new File(fromPath);
		Class<?> clazz = null;
		String classToLoad = className;
		try {
		    URL url = file.toURI().toURL();
		    URL[] urls = new URL[]{url};
		    
		    URLClassLoader cl = new URLClassLoader(urls);

		    clazz = cl.loadClass(classToLoad);
		    cl.close();
		    
		} catch (MalformedURLException e) {
			
		} catch (ClassNotFoundException e) {
			
		} catch (IOException e) {
			
		}
		return clazz;
	}
	
	private void clearMethodsInsideClassToMutate() {
		if (this.methodsInClassToMutate == null) {
			this.methodsInClassToMutate = new LinkedList<Method>();
		} else {
			this.methodsInClassToMutate.clear();
		}
	}
	
	private void clearClassesInOriginalBinDir() {
		if (this.classesInOriginalBinDir == null) {
			this.classesInOriginalBinDir = new LinkedList<String>();
		} else {
			this.classesInOriginalBinDir.clear();
		}
	}
	
	private void clearPackagesInOriginalBinDir() {
		if (this.packagesInOriginalBinDir == null) {
			this.packagesInOriginalBinDir = new LinkedList<String>();
		} else {
			this.packagesInOriginalBinDir.clear();
		}
	}
	
	private void clearTestClassesInTestsBinDir() {
		if (this.testClassesInTestsBinDir == null) {
			this.testClassesInTestsBinDir = new LinkedList<String>();
		} else {
			this.testClassesInTestsBinDir.clear();
		}
	}
	
	private boolean isTestClass(Class<?> c) {
		if (c == null) return false;
		RunWith runWithAnnotation = c.getAnnotation(RunWith.class);
		if (runWithAnnotation != null) {
			return true;
		} else if (TestCase.class.isAssignableFrom(c)) {
			return true;
		} else if (TestSuite.class.isAssignableFrom(c)) {
			return true;
		} else if (c.getAnnotation(Test.class) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
}
