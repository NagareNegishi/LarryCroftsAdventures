package nz.ac.wgtn.swen225.lc.renderer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * For Directory Structure, alot of it was "inspired" from Assigment 1.
 * But I did analyze it and gave my comments on what the parts actually do
 * To whoever originally wrote this, (Probably Marco on a good evening)
 * This is really cool code to read
 * 
 * @author Marwan Mohamed
 * @studentID 300653693
 */ 

class DirectoryStructure {
	
    public static String of(Path startPath) {
        try (var paths = Files.walk(startPath)) {//look into all the files
            return paths
                .filter(pi -> !pi.equals(startPath))
                .map(pi -> startPath.relativize(pi))
                .mapMulti(DirectoryStructure::formatEntry)
                .collect(Collectors.joining());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void formatEntry(Path rel, Consumer<String> consumer) {
        int depth = rel.getNameCount() - 1;
        consumer.accept("--|".repeat(depth));
        consumer.accept(rel.getFileName().toString());
        consumer.accept("  //Path.of(\"");
        consumer.accept(formatPath(rel));
        consumer.accept("\")\n");
    }

    private static String formatPath(Path rel) {
        return StreamSupport.stream(rel.spliterator(), false)
            .map(Path::toString)
            .collect(Collectors.joining("\", \""));
    }
}

