package nz.ac.wgtn.swen225.lc.app;

import java.io.File;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.persistency.LoadFile;
import nz.ac.wgtn.swen225.lc.persistency.Paths;
import nz.ac.wgtn.swen225.lc.persistency.SaveFile;

/**
 * 
 * maybe be i want to isolate the file operation from app
 */
public class FileOperation {


    public static void saveGame(JFrame app, GameStateController model) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Game");
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON files", "json"));
        // opens into saves directory
        fileChooser.setCurrentDirectory(Paths.savesDir);
        
        int userSelection = fileChooser.showSaveDialog(app); // show dialog and wait user input
        if (userSelection == JFileChooser.APPROVE_OPTION) { // if user picked a file
            File fileToSave = fileChooser.getSelectedFile();
    
            String filename = fileToSave.getName(); // i should pass file
            boolean success = SaveFile.saveGame(filename, model); // do i need to pass model or gamestate? 2/10
            if (success) {
            JOptionPane.showMessageDialog(app, "Game Saved", "Save", JOptionPane.INFORMATION_MESSAGE);
            } else {
            /**handleFileError("Failed to save game", "Save Error", 
                new String[]{"Save Game", "start level 1", "quit"}, "Save Game",
                () -> saveGame(app, model));*/
            }
        }
    }

    /**
   * Load a file from the given directory.
   * Paths class in nz.ac.wgtn.swen225.lc.persistency.Paths contains static final Files for desired directories.
   * @param dir it should use File from Paths class in nz.ac.wgtn.swen225.lc.persistency.Paths
   */
    private Optional<GameStateController> loadFile(JFrame app, File dir) { // Added File parameter
        JFileChooser fileChooser = new JFileChooser(dir){
            /**
             * Overriding setCurrentDirectory to prevent user from changing directory
             */
            @Override
            public void setCurrentDirectory(File dir) {
            super.setCurrentDirectory(getCurrentDirectory());
            }
        };
        fileChooser.setDialogTitle("Load Game");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON Game files", "json");//expecting json file
        fileChooser.setFileFilter(filter); // set filter
        fileChooser.setAcceptAllFileFilterUsed(false); // disable "All files" option
    
        int picked = fileChooser.showOpenDialog(app);
        if (picked == JFileChooser.APPROVE_OPTION) { // if user picked a file
            File file = fileChooser.getSelectedFile();
            return LoadFile.loadSave(file);
        }
        System.err.println("this is Optional.empty() from load file");
        return Optional.empty();
        }
}