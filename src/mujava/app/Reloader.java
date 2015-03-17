package mujava.app;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is used to reload and re-link classes.
 * <p>
 * <p>
 * Features of this class includes:
 * <li>maintaining a cache of classes to be reloaded</li>
 * <li>loading a class from a specific location</li>
 * <li>reloading and re-linking a class and all classes stored in the cache</li>
 * <p>
 * <p>
 * Main changes from previous version:
 * <li>only classes that are meant to be reloaded are stored in the cache, the rest will only be loaded once</li>
 * <li>the same class won't be store more than once in the cache</li>
 * <p>
 * 
 * @author Simon Emmanuel Gutierrez Brida
 * @version 2.5
 */
public class Reloader extends ClassLoader {
	protected List<String> classpath;
	protected List<Class<?>> reloadableCache;
	protected Reloader child;

	public Reloader() {
		String classpath = System.getProperty("java.class.path");
		String[] classpathEntries = classpath.split(File.pathSeparator);
		this.classpath = new LinkedList<String>(Arrays.asList(classpathEntries));
		this.reloadableCache = new LinkedList<Class<?>>();
	}

	public Reloader(ClassLoader parent) {
		super(parent);
		String classpath = System.getProperty("java.class.path");
		String[] classpathEntries = classpath.split(File.pathSeparator);
		this.classpath = new LinkedList<String>(Arrays.asList(classpathEntries));
		this.reloadableCache = new LinkedList<Class<?>>();
	}

	public Reloader(List<String> classpath, ClassLoader parent) {
		super(parent);
		this.classpath = new LinkedList<String>(classpath);
		this.reloadableCache = new LinkedList<Class<?>>();
	}

	@Override
	public Class<?> loadClass(String s) throws ClassNotFoundException {
		Class<?> clazz = retrieveFromCache(s);
		if (clazz == null) {
			if (this.getParent() != null) {
				try {
					clazz = this.getParent().loadClass(s);
				} catch (ClassNotFoundException e) {}
			}
			if (clazz == null) {
				clazz = findClass(s);
			}
		}
		return clazz;
	}

	public Class<?> loadClassFrom(String s, String classpath) throws ClassNotFoundException {
		Class<?> clazz = null;
		if (this.getParent() != null) {
			try {
				clazz = this.getParent().loadClass(s);
			} catch (ClassNotFoundException e) {}
		}
		if (clazz != null) {
			clazz = loadAgainFrom(s, classpath);
		} else {
			clazz = findClassFrom(s, classpath);
		}
		if (clazz != null) addToCache(clazz);
		return clazz;
	}

	public Class<?> loadClassAsReloadable(String s) throws ClassNotFoundException {
		Class<?> clazz = loadClass(s);
		if (clazz != null) addToCache(clazz);
		return clazz;
	}

	public Class<?> rloadClass(String s, boolean reload) throws ClassNotFoundException {
		Class<?> clazz = null;
		if (reload) {
			clazz = reload(s);
		}
		if (clazz == null) {
			clazz = loadClass(s);
		}
		if (clazz != null && reload) addToCache(clazz);
		return clazz;
	}

	public Class<?> rloadClassFrom(String s, String classpath) throws ClassNotFoundException {
		Class<?> clazz = reloadFrom(s, classpath);
		return clazz;
	}

	protected Class<?> loadAgain(String s) throws ClassNotFoundException {
		Class<?> clazz = null;
		if (classExist(s, this.classpath.toArray(new String[this.classpath.size()]))) {
			clazz = findClass(s);
		} else {
			clazz = loadClassAsReloadable(s);
		}
		return clazz;
	}
	
	protected Class<?> loadAgainFrom(String s, String classpath) throws ClassNotFoundException {
		Class<?> clazz = null;
		if (classExist(s, new String[]{classpath})) {
			clazz = findClassFrom(s, classpath);
		} else {
			clazz = loadClassAsReloadable(s);
		}
		return clazz;
	}

	protected Class<?> reload(String s) throws ClassNotFoundException {
		Class<?> clazz = null;
		Reloader r = new Reloader(this.classpath, this);
		for (Class<?> c : this.reloadableCache) {
			if (c.getName().compareTo(s) != 0) {
				Class<?> newClass = r.loadAgain(c.getName());
				r.addToCache(newClass);
			}
		}
		clazz = r.loadAgain(s);
		this.child = r;
		r.addToCache(clazz);
		return clazz;
	}

	protected Class<?> reloadFrom(String s, String classpath) throws ClassNotFoundException {
		Class<?> clazz = null;
		Reloader r = new Reloader(this.classpath, this);
		for (Class<?> c : this.reloadableCache) {
			if (c.getName().compareTo(s) != 0) {
				r.setPathAsPriority(classpath);
				r.loadAgain(c.getName());
			}
		}
		clazz = r.loadClassFrom(s, classpath);
		this.child = r;
		return clazz;
	}

	protected Class<?> retrieveFromCache(String s) {
		Class<?> clazz = null;
		for (Class<?> c : this.reloadableCache) {
			if (c.getName().compareTo(s)==0) {
				clazz = c;
				break;
			}
		}
		return clazz;
	}

	@Override
	public Class<?> findClass(String s) throws ClassNotFoundException {
		Class<?> clazz = null;
		try {
			byte[] bytes = loadClassData(s);
			clazz = this.defineClass(s, bytes, 0, bytes.length);
			resolveClass(clazz);
			return clazz;
		} catch (IOException ioe) {
			throw new ClassNotFoundException("unable to find class " + s, ioe);
		}
	}

	public Class<?> findClassFrom(String s, String classpath) throws ClassNotFoundException {
		Class<?> clazz = null;
		try {
			byte[] bytes = loadClassDataFrom(s, classpath);
			clazz = this.defineClass(s, bytes, 0, bytes.length);
			resolveClass(clazz);
			return clazz;
		} catch (IOException ioe) {
			throw new ClassNotFoundException("unable to find class " + s + " in " + classpath, ioe);
		}
	}

	protected byte[] loadClassData(String className) throws IOException {
		boolean found = false;
		File f = null;
		for (String cp : this.classpath) {
			f = new File(cp + className.replaceAll("\\.", File.separator) + ".class");
			found = f.exists();
			if (found) break;
		}
		if (!found) {
			throw new IOException("File " + className + " doesn't exist\n");
		}
		int size = (int) f.length();
		byte buff[] = new byte[size];
		FileInputStream fis = new FileInputStream(f);
		DataInputStream dis = new DataInputStream(fis);
		dis.readFully(buff);
		dis.close();
		return buff;
	}

	protected byte[] loadClassDataFrom(String className, String classpath) throws IOException {
		File f = new File(classpath + className.replaceAll("\\.", File.separator) + ".class");
		if (!f.exists()) throw new IOException("File " + className + " doesn't exist\n");
		int size = (int) f.length();
		byte buff[] = new byte[size];
		FileInputStream fis = new FileInputStream(f);
		DataInputStream dis = new DataInputStream(fis);
		dis.readFully(buff);
		dis.close();
		return buff;
	}

	private void addToCache(Class<?> clazz) {
		boolean found = false;
		int i = 0;
		for (Class<?> c : this.reloadableCache) {
			found = c.getName().compareTo(clazz.getName()) == 0;
			if (found) break;
			i++;
		}
		if (found) this.reloadableCache.remove(i);
		this.reloadableCache.add(clazz);
	}

	public Reloader getChild() {
		return this.child;
	}

	public Reloader getLastChild() {
		Reloader lastChild = this;
		while (lastChild.child != null) {
			lastChild = lastChild.child;
		}
		return lastChild;
	}

	private boolean classExist(String s, String[] classpath) {
		boolean found = false;
		File f = null;
		for (String cp : classpath) {
			f = new File(cp + s.replaceAll("\\.", File.separator) + ".class");
			found = f.exists();
			if (found) break;
		}
		return found;
	}
	
	private void setPathAsPriority(String path) {
		if (!this.classpath.isEmpty() && this.classpath.get(0).compareTo(path) == 0) return; //path is already at the beginning
		boolean found = false;
		int i = 1;
		while (i < this.classpath.size() && !found) {
			found = this.classpath.get(i).compareTo(path) == 0;
			if (!found) i++;
		}
		if (found) {
			this.classpath.remove(i);
		}
		this.classpath.add(path);
	}

}
