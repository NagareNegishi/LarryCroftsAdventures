package nz.ac.wgtn.swen225.lc.renderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.imageio.ImageIO;

/**
 * This is the class that is responsible for LOADING the Images into Cache only and finding them
 * It's important to note that if the Enum's name as a file doesn't exist in the imgs folder
 * it WILL break
 * 
 * eg. if we want a Kourie Img, we will NEED Kourie.png to be present, otherwise, the program won't run
 * 
 *  
 * @author Marwan Mohamed
 * @studentID 300653693
 * 
 */




public enum Img {
	FreeTile,
	LockedDoor_blue,
    Wall_Tile,
    Treasure,
    Blue_key,
    chap,
    Actor,
    Kourie; //Kourie is a test object, don't mind him.
    public final BufferedImage image;
    
    Img() {
        this.image = loadImage(this.name());
    }


    private static Path startPath() {
        Path path=  Paths.get(System.getProperty("user.dir"), "src", "imgs");
        System.out.println("Image path: " + path.toString());
        return path;
    }

    private static BufferedImage loadImage(String name) {
        Path p = startPath().resolve(name + ".png");
        System.out.println("Loading image from: " + p.toString());
        assert Files.exists(p) : "Image " + name + " not found. Visible files are:\n" 
                                + DirectoryStructure.of(startPath());
        try {
            BufferedImage img = ImageIO.read(p.toFile());

            // forcing image transparency, but still doesn't work
            BufferedImage imgWithAlpha = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            imgWithAlpha.getGraphics().drawImage(img, 0, 0, null);
            
            return imgWithAlpha;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }




/**
 * For Directory Structure, alot of it was "inspired" from Assigment 1.
 * But I did analyze it and gave my comments on what thiese parts actually do
 * To whoever originally wrote this, (Probably Marco on a good evening)
 * This is really cool code to read
 * Though, it's probably possible to read just the code to fix itself if it does find the image
 * why not just fix the image path whilst we're at it, buuuut. It's defintly on me as a
 * developer to have my pathing done right the first time so let's leave it as is =D
 * 
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
}
