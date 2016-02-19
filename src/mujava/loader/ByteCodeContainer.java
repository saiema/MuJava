package mujava.loader;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import mujava.util.JustCodeDigest;

public class ByteCodeContainer {
	private Map<String, byte[]> classByteCodeMap;
	private Map<String, String> filePerClass;
	private Set<String> classesToReload;
	private boolean verifyFileChanges;
	public static boolean reuseByteCode = true;
	
	private ByteCodeContainer(Map<String, byte[]> classByteCodeMap, Set<String> classesToReload, boolean verifyFileChanges) {
		this.classByteCodeMap = classByteCodeMap;
		this.classesToReload = classesToReload;
		this.verifyFileChanges = verifyFileChanges;
		this.filePerClass = new TreeMap<String, String>();
	}
	
	public ByteCodeContainer() {
		this(new TreeMap<String, byte[]>(), new TreeSet<String>(), false);
	}
	
	public ByteCodeContainer(Set<String> classesToReload) {
		this(new TreeMap<String, byte[]>(), classesToReload, false);
	}
	
	public ByteCodeContainer(boolean verifyFileChanges) {
		this(new TreeMap<String, byte[]>(), new TreeSet<String>(), verifyFileChanges);
	}
	
	public void eliminateClass(String className) {
		String classFilePath = this.filePerClass.remove(className);
		if (classFilePath != null) this.classByteCodeMap.remove(classFilePath);
	}
	
	public byte[] loadByteCodeFile(File file) throws IOException {
		return loadByteCodeFile(file, null);
	}
	
	public byte[] loadByteCodeFile(File file, String asClass) throws IOException {
		if (asClass != null && this.filePerClass.containsKey(asClass)) {
			String associatedPath = this.filePerClass.get(asClass);
			if (associatedPath.compareTo(file.getAbsolutePath()) != 0) {
				this.classByteCodeMap.remove(associatedPath);
				this.filePerClass.put(asClass, file.getAbsolutePath());
			}
		} else if (asClass != null) {
			this.filePerClass.put(asClass, file.getAbsolutePath());
		}
		String key = file.getAbsolutePath();
		if (ByteCodeContainer.reuseByteCode && this.classByteCodeMap.containsKey(key)) {
			if (this.classesToReload.contains(key)) {
				byte buff[] = loadFile(file);
				this.classByteCodeMap.put(key, buff);
				return buff;
			} else if (this.verifyFileChanges) {
				byte lastBuff[] = this.classByteCodeMap.get(key);
				byte currentBuff[] = JustCodeDigest.digest(file, false);
				if (Arrays.equals(lastBuff, currentBuff)) {
					return lastBuff;
				} else {
					this.classByteCodeMap.put(key, currentBuff);
					return currentBuff;
				}
			} else {
				return this.classByteCodeMap.get(key);
			}
		} else {
			byte buff[] = loadFile(file);
			this.classByteCodeMap.put(key, buff);
			return buff;
		}
	}
	
	public boolean byteCodeExist(File file) {
		if (this.classByteCodeMap.containsKey(file.getAbsolutePath())) {
			return true;
		} else {
			return file.exists();
		}
	}
	
	private byte[] loadFile(File file) throws IOException {
		int size = (int) file.length();
		byte buff[] = new byte[size];
		FileInputStream fis = new FileInputStream(file);
		DataInputStream dis = new DataInputStream(fis);
		dis.readFully(buff);
		dis.close();
		return buff;
	}
	
	
	public String containerStatusAsString() {
		return "Container has " + this.classByteCodeMap.size() + " values";
	}
	
	@Override
	public String toString() {
		String res = "";
		res += "Reuse bytecode      : " + ByteCodeContainer.reuseByteCode + "\n";
		res += "Verify file changes : " + this.verifyFileChanges + "\n";
		res += "Classes to reload   : \n" + classesToReloadAsString(true) + "\n";
		res += "File per class      : \n" + filePerClassAsString(true) + "\n";
		res += "Bytecode per class  : \n" + byteCodePerClass(true, false) + "\n";
		return res;
	}
	
	public String classesToReloadAsString(boolean indent) {
		String res = "";
		Iterator<String> it = this.classesToReload.iterator();
		while (it.hasNext()) {
			res += indentation(indent) + it.next();
			if (it.hasNext()) res += "\n"; 
		}
		return res;
	}
	
	public String filePerClassAsString(boolean indent) {
		String res = "";
		Iterator<Entry<String, String>> it = this.filePerClass.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			res += indentation(indent) + entry.getKey() + " : " + entry.getValue();
			if (it.hasNext()) res += "\n";
		}
		return res;
	}
	
	public String byteCodePerClass(boolean indent, boolean fullByteCode) {
		String res = "";
		Iterator<Entry<String, byte[]>> it = this.classByteCodeMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, byte[]> entry = it.next();
			String byteCodeAsString = fullByteCode?Arrays.toString(entry.getValue()):entry.getValue().toString();
			res += indentation(indent) + entry.getKey() + " : " + byteCodeAsString;
			if (it.hasNext()) res += "\n";
		}
		return res;
	}
	
	private String indentation(boolean indent) {
		if (indent) return "    ";
		else return "";
	}
	
}
