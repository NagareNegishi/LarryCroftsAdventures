package nz.ac.wgtn.swen225.lc.renderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

/**
 * This is the class that is responsible for LOADING the Images into Cache only and finding them
 * It's important to note that if the Enum's name as a file doesn't exist in the imgs folder
 * it WILL break
 * 
 * eg. if we want a "Kourie" Img, we will NEED Kourie.png to be present, otherwise, the program won't run
 * 
 * This could defintly be optimized later
 *  
 * @author Marwan Mohamed
 * @studentID 300653693
 * 
 */




public enum ImgOld {
	FreeTile,
	LockedDoor_blue,LockedDoor_red,
    Wall_Tile,
    intro1, intro2,
    InfoTile,
    Stairs,
    
    tp1,tp2,tp3,
    
    ExitLock,ExitOpen,
    
    Treasure1,Treasure2,Treasure3,Treasure4,// can likely make this an animation of sorts
   
    //
    
    Red_key,Blue_key,
    chap1,chap2,
    Actor1,Actor2,
    Water1,Water2,Water3,Water4,Water5,Water6,Water7,
    Kourie; //Kourie is a test object, don't mind him.
	
    public final BufferedImage image;
    
    ImgOld() {
        this.image = loadImage(this.name());
    }
    
    private static Path startPath() {
        Path path=  Paths.get(System.getProperty("user.dir"), "src", "imgs");
        System.out.println("Image path: " + path.toString());
        return path;
    }

    protected static BufferedImage loadImage(String name) {
        Path p = startPath().resolve(name + ".png");
        System.out.println("Loading image from: " + p.toString());
        assert Files.exists(p) : "Image " + name + " not found. Visible files are:\n" 
                                + DirectoryStructure.of(startPath());
        try {
            BufferedImage img = ImageIO.read(p.toFile());

            // forcing image transparency, 
            BufferedImage imgWithAlpha = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            imgWithAlpha.getGraphics().drawImage(img, 0, 0, null);
            
            return imgWithAlpha;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    
    

}
