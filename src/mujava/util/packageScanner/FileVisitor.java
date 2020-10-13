package mujava.util.packageScanner;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileVisitor extends SimpleFileVisitor<Path> {
    private final PathMatcher matcher;
    private final List<Path> matchedPaths = new ArrayList<>();

    FileVisitor(String pattern) {
        this.matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
    }

    public int getTotalMatches() {
        return this.matchedPaths.size();
    }

    public Collection<Path> getMatchedPaths() {
        return this.matchedPaths;
    }

    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        this.match(file);
        return FileVisitResult.CONTINUE;
    }

    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        this.match(dir);
        return FileVisitResult.CONTINUE;
    }

    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        exc.printStackTrace();
        return FileVisitResult.CONTINUE;
    }

    private void match(Path file) {
        if (file == null)
            return;
        Path name = file.toAbsolutePath();
        if (this.matcher.matches(name)) {
            this.matchedPaths.add(name);
        }

    }
}
