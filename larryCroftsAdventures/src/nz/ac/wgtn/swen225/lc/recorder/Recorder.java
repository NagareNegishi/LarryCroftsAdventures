package nz.ac.wgtn.swen225.lc.recorder;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;
import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.persistency.LoadFile;

/**
 * Class used by the App module to generate a Recorder object.
 */
public class Recorder {

	// Serializes/de-serializes objects.
	private ObjectMapper eventMapper = new ObjectMapper();

	// Holds Chap's recorded movements and when they happened.
	private List<DirectionEvent> events = new ArrayList<>();

	// Limit to prevent memory issues 
	// Probably not an issue unless trying to stress test game :/
	private final int EventsLimit = 10000000;
	
	// Index of event in list that replay is up to.
	private int currentEventIndex = -1;

	// Rate events are shown per second during auto-replay.
	private int autoReplaySpeed = 1;

	// Provides controllable model of game at start of first level
	private Supplier<GameStateController> firstLevelSupplier;

	// Reference to the controllable game model
	private GameStateController recordingGame;

	// Thread used for auto replays
	private Thread replayThread = null;

	// An object to receive changes for App
	private Consumer<RecordingChanges> updateReciever;

	/**
	 * Wraps an actor's movement into an object to read at a later point.
	 * 
	 * @param direction The direction actor is moving.
	 * @param time      Time when user inputed to move actor.
	 */
	private record DirectionEvent(Direction direction, int time) {
		public DirectionEvent {
			assert direction != null : "DirectionEvent can't have null direction!";
			assert time >= 0 : "DirectionEvent's time cannot be below 0";
		}
	}

	/**
	 * Wraps an actor's movement into an object to read at a later point.
	 * 
	 * @param direction The direction actor is moving.
	 * @param time      Time when user inputed to move actor.
	 */
	public record RecordingChanges(GameStateController updatedGame, int updatedTime) {
		public RecordingChanges {
			assert updatedGame != null : "RecordingChanges can't have null GameStateController!";
			assert updatedTime >= 0 : "DirectionEvent's time cannot be below 0";
		}
	}

	/**
	 * Wrapper allowing file operations to be given as arguments without worrying
	 * about dealing with exception first.
	 */
	private interface FileOperation {
		void execute(RecordingFileChooser rfc) throws IOException;
	}

	/**
	 * Constructor intended to be used by App for creating recorder object.
	 */
	public Recorder(Consumer<RecordingChanges> updateReciever) {
		assert updateReciever != null : "Null update reciever given to record during construction!";
		this.updateReciever = updateReciever;
		firstLevelSupplier = () -> {
			assert LoadFile.loadLevel("level1").isPresent()
					: "Exception occured when attempting to load first level for recorder!";
			return LoadFile.loadLevel("level1").get();
		};
		recordingGame = firstLevelSupplier.get();
	}

	/**
	 * Constructor allowing recorder to work When first level is not "level1".
	 * Intended for testing
	 * 
	 * @param firstLevelSupplier Gives recorder modifiable game at first level.
	 */
	public Recorder(Consumer<RecordingChanges> updateReciever, Supplier<GameStateController> firstLevelSupplier) {
		assert updateReciever != null : "Null update reciever given to record during construction!";
		assert firstLevelSupplier != null : "Null first level supplier given to record during construction!";
		this.updateReciever = updateReciever;
		this.firstLevelSupplier = firstLevelSupplier;
		recordingGame = firstLevelSupplier.get();
	}

	/**
	 * Handles common logic for file operations such as load or save. Shows a
	 * message to the user if the operation succeeds.
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
	 * Used by App module.
	 * Reads JSON file, interpreting data into a list of DirectionEvent objects.
	 */
	public void loadRecording() {
		handleRecording("load", rfc -> {
			events = eventMapper.readValue(rfc.recordingLoc,
					eventMapper.getTypeFactory().constructCollectionType(List.class, DirectionEvent.class));
		});
	}

	/**
	 * Used by App module.
	 * Writes JSON file, by serializing a list of DirectionEvent objects.
	 */
	public void saveRecording() {
		handleRecording("save", rfc -> {
			eventMapper.writeValue(rfc.recordingLoc, events);
		});
	}

	/**
	 * Used by App module to inform Recorder when chap is moving and what direction.
	 * 
	 * @param direction   The direction chap is moving.
	 * @param currentTime Time when user inputed to move chap.
	 */
	public void ping(Direction direction, int currentTime) {
		assert events != null : "Recorder events storage is null!";
		if(events.size() == EventsLimit) return;
		events.add(new DirectionEvent(direction, currentTime));
	}

	/**
	 * Used by App module to just set the automatic replay speed.
	 * 
	 * @param eventsPerSecond Rate events play for auto replay
	 * @return Rate that autoReplaySpeed was set to.
	 */
	public int setPlaybackSpeed(int eventsPerSecond) {
		assert eventsPerSecond > 0 : "Auto replay speed must be above zero!";
		return (autoReplaySpeed = eventsPerSecond);
	}

	/**
	 * Used by App module to manually step the recording back by one event.
	 */
	public void previousStep() {

		assert !events.isEmpty() : "Recording is empty!";
		recordingGame = firstLevelSupplier.get();
		if (currentEventIndex <= 0) {
			updateReciever.accept(new RecordingChanges(recordingGame, events.get(0).time()));
			return;
		}

		currentEventIndex--;

		for (int i = 0; i <= currentEventIndex; i++) {
			DirectionEvent curEvent = events.get(i);
			recordingGame.moveChap(curEvent.direction());
		}
		updateReciever.accept(new RecordingChanges(recordingGame, events.get(currentEventIndex).time()));
	}

	/**
	 * Used by App module to manually step the recording forward by one event.
	 */
	public void nextStep() {
		assert recordingGame != null : "Recording doesn't have level to reference!";
		assert !events.isEmpty() : "Recording is empty!";
		if (currentEventIndex >= events.size() - 1) {
			updateReciever.accept(new RecordingChanges(recordingGame, events.get(events.size() - 1).time()));
			return;
		}
		currentEventIndex = Math.min(currentEventIndex + 1, events.size() - 1);
		DirectionEvent curEvent = events.get(currentEventIndex);
		recordingGame.moveChap(curEvent.direction());
		updateReciever.accept(new RecordingChanges(recordingGame, curEvent.time()));
	}

	/**
	 * Used by App module to automatically step forward recording.
	 * Can be toggled on and off.
	 */
	public void autoReplay() {
		if (replayThread != null && replayThread.isAlive()) {
			replayThread.interrupt();
			return;
		}
		replayThread = new Thread(() -> {
			try {
				while (currentEventIndex < events.size()) {
					Thread.sleep(1000 / autoReplaySpeed);
					nextStep();
				}
			} catch (InterruptedException e) {
				return;
			}
		});

		replayThread.start();
	}

	/**
	 * Intended for making testing easier.
	 * 
	 * @return Chap's position in game model recording can view.
	 */
	public String getChapPosition() {
		return recordingGame.getChapPosition();
	}
}

/**
 * Provides a custom file chooser UI for selecting and operating on JSON
 * recording files.
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
