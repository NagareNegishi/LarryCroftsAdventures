package nz.ac.wgtn.swen225.lc.renderer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

/**
 * This class is responsible for loading audio files into memory.
 * It ensures all audio files corresponding to enum names are loaded on start-up.
 * If the audio file is missing, the program will fail.
 * 
 * Can only use Wav files unfortunatly.
 *
 * Example usage:
 * Audio.JumpSound.play();
 * 
 * @author Marwan Mohamed
 * @studentID 300653693
 */
public enum AudioP {
    aThud,
    TreasureCollected,
    death;

    public final Clip clip;

    AudioP() {
        this.clip = loadAudio(this.name());
    }

    	private static Path startPath() {
        // Adjust this path to your audio folder
        Path path = Paths.get(System.getProperty("user.dir"), "src", "audio");
        System.out.println("Audio path: " + path.toString());
        return path;
    }

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


    // Method to play the audio
    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // Rewind to the start of the clip
            clip.start();              // Play the audio clip
        }
    }

    // Method to stop the audio
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop(); // Stop the audio
        }
    }

    // Method to loop the audio continuously
    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the audio continuously
        }
    }

    // Method to check if the audio is playing
    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }

    /**
     * For Directory Structure: similar to your image loader, used for debugging and listing files.
     */
    static class DirectoryStructure {
        public static String of(Path startPath) {
            try (var paths = Files.walk(startPath)) {
                return paths
                        .filter(pi -> !pi.equals(startPath))
                        .map(pi -> startPath.relativize(pi))
                        .mapMulti(DirectoryStructure::formatEntry)
                        .collect(Collectors.joining());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read directory structure.", e);
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
