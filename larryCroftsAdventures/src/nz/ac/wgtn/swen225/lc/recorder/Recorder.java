package nz.ac.wgtn.swen225.lc.recorder;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;
import nz.ac.wgtn.swen225.lc.domain.GameStateController;

/**
 * Class used by the App module to generate a Recorder object. 
 */
public class Recorder { 
	
	// Serializes/de-serializes objects.
	private ObjectMapper eventMapper = new ObjectMapper();
	
	// Entry to modify model of game for recording purposes.
	private GameStateController recordingGame;
	
	// Holds Chap's recorded movements and when they happened.
	private List<DirectionEvent> events = new ArrayList<>();
	
	// Index of event in list that replay is up to. 
	private int currentEventIndex = -1;
	
	// Rate events are shown per second during auto-replay.
	private Double autoReplaySpeed = 1.0;

	/**
	 * Wraps an actor's movement into an object to read at a later point.
	 * @param direction The direction actor is moving.
	 * @param time Time when user inputed to move actor.
	 */
	private record DirectionEvent(Direction direction, int time) {
		public DirectionEvent {
			assert direction != null : "DirectionEvent can't have null direction!";
			assert time >= 0 : "DirectionEvent's time cannot be below 0";
		}
	}
	
	/**
	 * Wrapper allowing file operations to be given as 
	 * arguments without worrying about dealing with exception first. 
	 */
	interface FileOperation {
	    void execute(RecordingFileChooser rfc) throws IOException;
	}

	/**
	 * Handles common logic for file operations such as load or save.
	 * Shows a message to the user if the operation succeeds.
	 */
	private void handleRecording(String operationType, FileOperation fileOperation) {
	    RecordingFileChooser rfc = new RecordingFileChooser(operationType);
	    try {
	        fileOperation.execute(rfc);
	        JOptionPane.showMessageDialog(rfc.fileChooser, rfc.success);
	    } catch (IOException ioe) {
	        ioe.printStackTrace();
	    }
	}
	
	/**
	 * Reads JSON file, interpreting data into
	 * a list of DirectionEvent objects.
	 */
	public void loadRecording() {
	    handleRecording("load", rfc -> {
	        events = eventMapper.readValue(rfc.recordingLoc,
	                eventMapper.getTypeFactory().constructCollectionType(List.class, DirectionEvent.class));
	    });
	}
	
	/**
	 * Writes JSON file, by serialising 
	 * a list of DirectionEvent objects.
	 */
	public void saveRecording() {
	    handleRecording("save", rfc -> {
	        eventMapper.writeValue(rfc.recordingLoc, events);
	    });
	}

	/**
	 * Used by App module to inform Recorder 
	 * when chap is moving and what direction.
	 * @param direction The direction chap is moving.
	 * @param currentTime Time when user inputed to move chap.
	 */
	public void ping(Direction direction, int currentTime) {
		events.add(new DirectionEvent(direction, currentTime));
	}

	/**
	 * Used by App module to just set 
	 * the automatic replay speed.
	 * @param eventsPerSecond Rate events play for auto replay
	 */
	public void setPlaybackSpeed(Double eventsPerSecond) {
		autoReplaySpeed = eventsPerSecond;
	}

	/** OUTDATED WILL BE CHANGED TO PING PERSISTENCY
	 * Used by App module to manually
	 * step the recording back by one event.
	 * @param controllerOfLevelOnStart Controls newly loaded model of game.
	 * @return The amount of time left when event originally occurred.
	 */
	public int previousStep(GameStateController controllerOfLevelOnStart) {
		assert controllerOfLevelOnStart != null : "Cannot call previousStep on Recorder object with null GameStateController!";
		assert !events.isEmpty() : "Recording is empty!";
		recordingGame = controllerOfLevelOnStart;
		if (currentEventIndex <= 0) return events.get(0).time();
		currentEventIndex--;
		
		for (int i = 0; i <= currentEventIndex; i++) {
			DirectionEvent curEvent = events.get(i);
			recordingGame.moveChap(curEvent.direction());
		}
		return events.get(currentEventIndex).time();
	}

	/**
	 * Used by App module to manually
	 * step the recording forward by one event.
	 * @return The amount of time left when event originally occurred.
	 */
	public int nextStep() {
		assert !events.isEmpty() : "Recording is empty!";
		if (currentEventIndex >= events.size() - 1) {
			return events.get(events.size() - 1).time();
		}
		currentEventIndex = Math.min(currentEventIndex + 1, events.size() - 1);
		DirectionEvent curEvent = events.get(currentEventIndex);
		recordingGame.moveChap(curEvent.direction());
		return curEvent.time();
	}

	/** OUTDATED WILL BE CHANGED TO PING PERSISTENCY
	 * Creates recorder object so long as 
	 * gameController actually controls a game.
	 * @param gameController Controls model of game with newly loaded level.
	 */
	public Recorder(GameStateController gameController) {
		assert gameController != null : "Cannot create Recorder object with null GameStateController!";
		recordingGame = gameController;
	}
	
	/**
	 * Intended for making testing easier.
	 * @return Chap's position in game model recording can view.
	 */
	public String getChapPosition() {
		return recordingGame.getChapPosition();
	}
}

/**
 * Provides a custom file chooser UI for 
 * selecting and operating on JSON recording files.
 */
class RecordingFileChooser {

	JFileChooser fileChooser;
	File recordingLoc;
	String success;

	RecordingFileChooser(String operationType) {

		fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Select A File in order to " + operationType + " Recording!");
		FileFilter filter = new FileNameExtensionFilter("JSON file", "JSON");
		fileChooser.setFileFilter(filter);
		File currentDirectiory = new File(System.getProperty("user.dir"));
		fileChooser.setCurrentDirectory(currentDirectiory);

		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			recordingLoc = fileChooser.getSelectedFile();
			this.success = "Was able to " + operationType + " successfully: " + this.recordingLoc.getAbsolutePath();
		} else
			throw new AssertionError("Invalid file chosen!");
	}
}
