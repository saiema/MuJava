package mujava.loader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
//import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * This class is used to reload and re-link classes.
 * <p>
 * <p>
 * Features of this class includes:
 * <li>maintaining a cache of classes to be reloaded</li>
 * <li>defining a priority path to load classes first from this one</li>
 * <li>reloading and re-linking a class and all classes marked as reloadable</li>
 * <li>instead of requiring to load a class as reloadable this version allows to just mark a class as reloadable</li>
 * <li>mark all classes in a folder and set this folder as priority path</li>
 * <li>Allows to define path per class, this avoids the need to move .class files</li>
 * <p>
 * <p>
 * Main changes from previous version:
 * <li>Old Reloaders are freed to improve performance</li>
 * <li>Classes byte code are stored to avoid reloading classes that don't change</li>
 * <p>
 * 
 * @author Simon Emmanuel Gutierrez Brida
 * @version 2.5.1
 */
public class Reloader extends ClassLoader {
	protected Set<String> classpath;
	protected String priorityPath;
	protected Set<String> reloadableCache;
	protected Map<String, Boolean> reloadedClasses;
	protected List<Class<?>> reloadableClassCache;
	protected Reloader child;
	private int reloadersCreated = 0;
	private ByteCodeContainer byteCodeContainer;
	public static int MAX_RELOADERS_BEFORE_CLEANING = 150;
	
	/**
	 * This map allows to define a specific path for each class
	 */
	protected Map<String, String> specificClassPaths;

	public Reloader(List<String> classpath, ClassLoader parent) {
		super(parent);
		if (parent instanceof Reloader) {
			this.reloadersCreated = ((Reloader)parent).reloadersCreated + 1;
			this.byteCodeContainer = ((Reloader)parent).byteCodeContainer;
			initReloadedClasses(((Reloader)parent).reloadedClasses);
		} else {
			this.byteCodeContainer = new ByteCodeContainer();
			initReloadedClasses();
		}
		this.classpath = new TreeSet<String>();
		this.classpath.addAll(classpath);
		this.reloadableCache = new TreeSet<String>();
		this.reloadableClassCache = new LinkedList<Class<?>>();
		this.specificClassPaths = new TreeMap<String, String>();
		
	}
	
	private void initReloadedClasses() {
		this.reloadedClasses = new TreeMap<>();
		if (this.reloadableCache == null) return;
		for (String rc : this.reloadableCache) {
			this.reloadedClasses.put(rc, Boolean.FALSE);
		}
	}
	
	private void initReloadedClasses(Map<String, Boolean> parentsReloadedClasses) {
		this.reloadedClasses = new TreeMap<>();
		for (Entry<String, Boolean> rc : parentsReloadedClasses.entrySet()) {
			this.reloadedClasses.put(rc.getKey(), Boolean.FALSE);
		}
	}
	
	private Reloader(Set<String> classpath, ClassLoader parent, Set<String> reloadableCache, Map<String, String> specificClassPaths) {
		this(classpath, parent);
		this.reloadableCache = reloadableCache;
		this.specificClassPaths = specificClassPaths;
	}
	
	private Reloader(Set<String> classpath, ClassLoader parent) {
		super(parent);
		if (parent instanceof Reloader) {
			this.reloadersCreated = ((Reloader)parent).reloadersCreated + 1;
			this.byteCodeContainer = ((Reloader)parent).byteCodeContainer;
			initReloadedClasses(((Reloader)parent).reloadedClasses);
		} else {
			this.byteCodeContainer = new ByteCodeContainer();
			initReloadedClasses();
		}
		this.classpath = classpath;
		this.reloadableClassCache = new LinkedList<Class<?>>();
	}
	
	public void setSpecificClassPath(String className, String path) {
		this.specificClassPaths.put(className, path);
	}
	
	private Reloader cleanReloader() {
		Set<String> childReloadableCache = new TreeSet<String>();
		childReloadableCache.addAll(this.reloadableCache);
		ClassLoader firstClassloader = getFirstClassLoader();
		Reloader cleanSlate = new Reloader(this.classpath, firstClassloader, childReloadableCache, this.specificClassPaths);
		cleanSlate.reloadersCreated = 1;
		unlinkPreviousReloadersAndLinkWithFirstReloader(firstClassloader, cleanSlate);
		return cleanSlate;
	}
	
	private void unlinkPreviousReloadersAndLinkWithFirstReloader(ClassLoader until, Reloader newReloader) {
		ClassLoader current = this; //this reloader is the only one to be left unchanged
		while (current instanceof Reloader && current != until) {
			if (current != this) {
				((Reloader)current).child = null;
			}
			ClassLoader parent = current.getParent();
			Field[] fields = ClassLoader.class.getDeclaredFields();
			Field parentField = null;
			for (Field f : fields) {
				if (f.getName().compareTo("parent") == 0) {
					parentField = f;
					break;
				}
			}
			if (parentField != null) {
				parentField.setAccessible(true);
				try {
					parentField.set(current, (ClassLoader)null);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
				parentField.setAccessible(false);
			}
			current = parent;			
		}
		if (until != null && until instanceof Reloader) {
			((Reloader)until).child = newReloader;
		}
	}
	
	private ClassLoader getFirstClassLoader() {
		ClassLoader fcl = this;
		while (fcl.getParent() instanceof Reloader) {
			fcl = fcl.getParent();
		}
		return fcl;
	}

	@Override
	public Class<?> loadClass(String s) throws ClassNotFoundException {
		Class<?> clazz = this.reloadableCache.contains(s)?retrieveFromCache(s):null;
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
		if (this.reloadableCache.contains(s)) {
			addToCache(clazz);
			if (this.reloadedClasses.containsKey(s)) {
				this.reloadedClasses.put(s, Boolean.TRUE);
			}
		}
		return clazz;
	}
	
	public void markClassAsReloadable(String s) {
		this.reloadableCache.add(s);
		this.reloadedClasses.put(s, Boolean.FALSE);
	}

	public Class<?> loadClassAsReloadable(String s) throws ClassNotFoundException {
		Class<?> clazz = loadClass(s);
		if (clazz != null) {
			addToCache(clazz);
			markClassAsReloadable(s);
		}
		return clazz;
	}

	public Class<?> rloadClass(String s, boolean reload) throws ClassNotFoundException {
		Class<?> clazz = null;
		if (reload) {
			this.byteCodeContainer.eliminateClass(s);
			clazz = reload(s);
		}
		if (clazz == null) {
			clazz = loadClass(s);
		}
		if (clazz != null && reload) {
			addToCache(clazz);
			markClassAsReloadable(s);
		}
		return clazz;
	}
	
	public void setPathAsPriority(String path) {
		this.priorityPath = path;
	}
	
	public void markEveryClassInFolderAsReloadable(String folder) {
		markEveryClassInFolderAsReloadable(folder, null);
	}
	
	public void markEveryClassInFolderAsReloadable(String folder, Set<String> allowedPackages) {
		cleanIfRescaning(folder);
		File pathFile = new File(folder);
		if (pathFile.exists() && pathFile.isDirectory()) {
			setPathAsPriority(folder);
			crawlAndMark(pathFile, "", allowedPackages);
		}
	}
	
	private void crawlAndMark(File dir, String pkg, Set<String> allowedPackages) {
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.getName().startsWith(".")) {
				continue;
			} else if (file.isFile() && file.getName().endsWith(".class")) {
				if (allowedPackages != null && !allowedPackages.contains(pkg)) {
					continue;
				}
				this.markClassAsReloadable(this.getClassName(file, pkg));
			} else if (file.isDirectory()) {
				String newPkg;
				if (!pkg.isEmpty()) {
					newPkg = pkg + "." + file.getName();
				} else {
					newPkg = file.getName();
				}
				crawlAndMark(file, newPkg, allowedPackages);
			}
		}
	}
	
	private String getClassName(File file, String pkg) {
		String className;
		String classSimpleName = file.getName();
		int lastDotIdx = classSimpleName.lastIndexOf('.');
		className = classSimpleName.substring(0, lastDotIdx);
		if (!pkg.isEmpty()) {
			className = pkg + "." + className;
		}
		return className;
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

	protected Class<?> reload(String s) throws ClassNotFoundException {
		Class<?> clazz = null;
		Reloader r = newReloader();
		for (String c : this.reloadableCache) {
			if (c.compareTo(s) != 0) {
				Class<?> newClass = null;
				if (r.reloadedClasses.containsKey(c) && r.reloadedClasses.get(c).booleanValue()) {
					newClass = r.loadClass(c);
				} else {
					newClass = r.loadAgain(c);
				}
				r.addToCache(newClass);
				//Class<?> newClass = r.loadAgain(c);
				//r.addToCache(newClass);
			}
		}
		clazz = r.loadAgain(s);
		this.child = r;
		r.addToCache(clazz);
		return clazz;
	}
	
	private Reloader newReloader() {
		if (this.reloadersCreated == Reloader.MAX_RELOADERS_BEFORE_CLEANING) {
			return this.cleanReloader();
		} else {
			Set<String> childReloadableCache = new TreeSet<String>();
			childReloadableCache.addAll(this.reloadableCache);
			return new Reloader(this.classpath, this, childReloadableCache, this.specificClassPaths);
		}
	}

	protected Class<?> retrieveFromCache(String s) {
		Class<?> clazz = null;
		for (Class<?> c : this.reloadableClassCache) {
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

	protected byte[] loadClassData(String className) throws IOException {
		boolean found = false;
		File f = null;
		if (this.specificClassPaths.containsKey(className)) {
			String specificPath = this.specificClassPaths.get(className);
			f = new File(specificPath + className.replaceAll("\\.", File.separator) + ".class");
			if (f != null && f.exists()) {
				found = true;
			}
		}
		if (!found) {
			if (this.priorityPath != null) f = new File(this.priorityPath + className.replaceAll("\\.", File.separator) + ".class");
			if (f != null && f.exists()) {
				found = true;
			}
		}
		if (!found) {
			for (String cp : this.classpath) {
				f = new File(cp + className.replaceAll("\\.", File.separator) + ".class");
				found = f.exists();
				if (found) break;
			}
		}
		if (!found) {
			throw new IOException("File " + className + " doesn't exist\n");
		}
		byte[] classDef = this.byteCodeContainer.loadByteCodeFile(f, className);
		return classDef;
	}

	private void addToCache(Class<?> clazz) {
		boolean found = false;
		int i = 0;
		for (Class<?> c : this.reloadableClassCache) {
			found = c.getName().compareTo(clazz.getName()) == 0;
			if (found) break;
			i++;
		}
		if (found) this.reloadableClassCache.remove(i);
		this.reloadableClassCache.add(clazz);
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
			found = this.byteCodeContainer.byteCodeExist(f);
			if (found) break;
		}
		return found;
	}
	
	private void cleanIfRescaning(String folder) {
		File pathFile = new File(folder);
		if (pathFile.exists() && pathFile.isDirectory()) {
			if (this.priorityPath != null && this.priorityPath.compareTo(folder) == 0) {
				cleanDeletedClasses();
			} else if (this.classpath.contains(folder)) {
				cleanDeletedClasses();
			} else if (this.specificClassPaths.containsKey(folder)) {
				this.reloadableCache.remove(this.specificClassPaths.get(folder));
				this.specificClassPaths.remove(folder);
			}
		}
	}

	private void cleanDeletedClasses() {
		Set<String> cleanedClasses = new TreeSet<>();
		for (String c : this.reloadableCache) {
			if (classExists(c)) {
				cleanedClasses.add(c);
			} else {
				this.byteCodeContainer.eliminateClass(c);
			}
		}
		this.reloadableCache = cleanedClasses;
	}
	
	private boolean classExists(String className) {
		boolean found = false;
		File f = null;
		for (String cp : classpath) {
			f = new File(cp + className.replaceAll("\\.", File.separator) + ".class");
			found = f.exists() && f.isFile();
			if (found) break;
		}
		return found;
	}

}
