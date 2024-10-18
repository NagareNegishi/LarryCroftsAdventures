package nz.ac.wgtn.swen225.lc.renderer;

import javax.sound.sampled.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * This class is responsible for loading audio files into memory.
 * It ensures all audio files corresponding to enum names and are loaded on start-up.
 * If the audio file is missing, the program will fail.
 * 
 * Can only use Wav files unfortunately with a very specific formatting.
 *
 * Here's an example usage:
 * Audio.Thud.play();
 * 
 * This will work anywhere
 * 
 * @author Marwan Mohamed
 * @studentID 300653693
 * 
 */


public enum AudioP {
    Thud,
    TreasureCollected,
    death;

    public final Clip clip;
    public static float master;
    AudioP() {
        this.clip = loadAudio(this.name());
    }
    
    static {
        for (AudioP audio : AudioP.values()) {
            // Eagerly load all audio clips at initialization.
            audio.clip.start();
            audio.clip.stop();
            audio.clip.setFramePosition(0);  // Reset the clip after loading.
        }
    }

    	private static Path startPath() {
        Path path = Paths.get(System.getProperty("user.dir"), "src", "audio");
        System.out.println("Audio path: " + path.toString());
        return path;
    }

    	/**
    	 * 
    	 * Load Audio Tries to convert the Audio into a supported format if it isn't already supported.
    	 * it takes a String containing the Path for the audio file itself
    	 * 
    	 * @param name
    	 * @return Clip
    	 */
    	private static Clip loadAudio(String name) {
    	    Path p = startPath().resolve(name + ".wav");
    	    System.out.println("Loading audio from: " + p.toString());
    	    assert Files.exists(p) : "Audio file " + name + " not found. Visible files are:\n" 
    	                            + DirectoryStructure.of(startPath());

    	    try {
    	        AudioInputStream audioStream = AudioSystem.getAudioInputStream(p.toFile());
    	        AudioFormat originalFormat = audioStream.getFormat();

    	        // Check if the format is supported, otherwise convert
    	        AudioFormat targetFormat = new AudioFormat(
    	            AudioFormat.Encoding.PCM_SIGNED,
    	            originalFormat.getSampleRate(),
    	            16, // Convert to 16-bit
    	            originalFormat.getChannels(),
    	            originalFormat.getChannels() * 2, // 2 bytes per frame (16-bit stereo)
    	            originalFormat.getSampleRate(),
    	            false // little-endian
    	        );

    	        // If the original format is not PCM_SIGNED 16-bit, convert the stream
    	        AudioInputStream convertedStream;
    	        if (!AudioSystem.isConversionSupported(targetFormat, originalFormat)) {
    	            convertedStream = AudioSystem.getAudioInputStream(targetFormat, audioStream);
    	        } else {
    	            convertedStream = audioStream; // Use original stream if already supported
    	        }

    	        Clip clip = AudioSystem.getClip();
    	        clip.open(convertedStream);
    	        return clip;

    	    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
    	        throw new RuntimeException("Failed to load audio file: " + name, e);
    	    }
    	}
    
    
    public void setVolume(float p) {
    	 if (clip != null) {
			FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		    master = volume.getValue();
		    volume.setValue(p);
    	 }
    }
    	
    public void setNVolume() {
        if (clip != null) {
        	FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        	setVolume(volume.getMinimum());
        }
    }
    

    // Play the audio
    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // Rewind to the start of the clip
            clip.start();             // Play the audio clip
        }
    }

    //You should only Use Stop to end the Audio Clip that's running abruptly. 
    //You don't need this for the music to stop, it stops when the file reaches it's end
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    // Method to loop the audio continuously
    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY); 
        }
    }

    // Method to check if the audio is playing
    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }

   
}
