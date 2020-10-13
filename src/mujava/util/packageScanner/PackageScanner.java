package mujava.util.packageScanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class PackageScanner {

    public static Set<String> scanPackagesIn(Path root) throws IOException {
        if (root == null)
            throw new IllegalArgumentException("root path is null");
        File rootAsFile = root.toFile();
        if (!rootAsFile.exists() || !rootAsFile.isDirectory() || !rootAsFile.canRead()) {
            return new HashSet<>();
        }
        Set<String> packages = new HashSet<>();
        ClassScanner classScanner = new ClassScanner(root);
        classScanner.scan();
        for (Path clazz : classScanner.getPaths()) {
            String packageRaw = clazz.toAbsolutePath().subpath(root.getNameCount(), clazz.toAbsolutePath().getNameCount() - 1).toString();
            packageRaw = packageRaw.replaceAll(File.separator, ".");
            String pkg = packageRaw.startsWith(File.separator)?packageRaw.substring(1):packageRaw;
            packages.add(pkg);
        }
        return packages;
    }

    public static Set<String> scanClassesIn(Path root) throws IOException {
        if (root == null)
            throw new IllegalArgumentException("root path is null");
        File rootAsFile = root.toFile();
        if (!rootAsFile.exists() || !rootAsFile.isDirectory() || !rootAsFile.canRead()) {
            return new HashSet<>();
        }
        Set<String> classes = new HashSet<>();
        ClassScanner classScanner = new ClassScanner(root);
        classScanner.scan();
        for (Path clazz : classScanner.getPaths()) {
            String packageRaw = clazz.toAbsolutePath().subpath(root.toAbsolutePath().getNameCount(), clazz.toAbsolutePath().getNameCount() - 1).toString();
            packageRaw = packageRaw.replaceAll(File.separator, ".");
            String pkg = packageRaw.startsWith(File.separator)?packageRaw.substring(1):packageRaw;
            String fileName = clazz.getFileName().toString().replaceAll("\\.class", "");
            classes.add(pkg.isEmpty()?fileName:(pkg + "." + fileName));
        }
        return classes;
    }

}
