package mujava.util.packageScanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class ClassScanner {
    private final List<Path> paths;
    private final Path pathToScan;

    public ClassScanner(String path) {
        this(Paths.get(path));
    }

    public ClassScanner(Path path) {
        this.pathToScan = path;
        this.paths = new LinkedList<>();
    }

    public void scan() throws IllegalStateException, IOException {
        this.paths.clear();
        File folderOrZipToScan = this.pathToScan.toFile();
        if (!folderOrZipToScan.exists()) {
            throw new IllegalStateException("ClassScanner was constructed with a path to a file or folder that doesn't exist");
        } else if (folderOrZipToScan.isFile() && !folderOrZipToScan.getName().endsWith(".zip")) {
            throw new IllegalStateException("ClassScanner was constructed to a path that refers to a file that is not a zip");
        } else if (folderOrZipToScan.isFile()) {
            throw new IOException("Unsupported path to scan: " + this.pathToScan.toString());
        } else {
            FileVisitor fileVisitor = new FileVisitor("**.class");
            Files.walkFileTree(this.pathToScan, fileVisitor);
            this.paths.addAll(fileVisitor.getMatchedPaths());
        }
    }

    public List<Path> getPaths() {
        return this.paths;
    }
}
