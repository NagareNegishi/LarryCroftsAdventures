package nz.ac.wgtn.swen225.lc.recorder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;
import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.persistency.LoadFile;
import nz.ac.wgtn.swen225.lc.persistency.Paths;

/**
 * Class used by the App module to generate a Recorder object. Handles recording
 * and playback of game events.
 * 
 * @author Joshua Neylan
 * @studentID 300654087
 */

public class Recorder {
	// Map of level numbers to their corresponding file paths.
	private Map<Integer, File> levelPaths = Map.of(1, Paths.level1, 2, Paths.level2);

	// Serializes/de-serializes objects.
	private ObjectMapper eventMapper = new ObjectMapper();

	// Holds Chap's recorded movements and when they happened.
	private List<Event> events = new ArrayList<>();

	// Limit to prevent memory issues
	// Probably not an issue unless trying to stress test game :/
	private final int EventsLimit = 10000000;

	// Index of event in list that replay is up to.
	private int currentEventIndex = -1;

	// Keeps track of where the end of event list should be.
	private int headEventIndex = currentEventIndex;

	// Rate events are shown per second during auto-replay.
	private int autoReplaySpeed = 1;

	// Used during auto replay to decide direction to traverse events.
	private Runnable lastStepMethodUsed = this::nextStep;

	// Provides controllable model of game at start of first level.
	private Supplier<GameStateController> firstLevelSupplier;

	// Reference to the controllable game model.
	private GameStateController recordingGame;

	// Thread used for auto replays.
	private Thread replayThread = null;

	// An object to receive changes for App.
	private Consumer<RecordingChanges> updateReciever;

	/**
	 * Interface representing an event in the game.
	 */
	private interface Event {
		int time();

		default void run(GameStateController gsc) {
			gsc.moveActor();
		}
	}

	/**
	 * Wraps Chap's movement into an object to be read at a later point.
	 * 
	 * @param direction The direction actor is moving.
	 * @param time      Time when user inputed to move actor.
	 */
	private record ChapEvent(Direction direction, int time) implements Event {
		public ChapEvent {
			assert direction != null : "ChapEvent can't have null direction!";
			assert time >= 0 : "ChapEvent's time cannot be below 0";
		}

		@Override
		public void run(GameStateController gsc) {
			gsc.moveChap(direction);
		}
	}

	/**
	 * Represents changes made by recorder to the game to be consumed by App
	 * 
	 * @param updatedGame A controllable GameState that reflects changes done by
	 *                    recorder.
	 * @param updatedTime Time that most recent event occurred at.
	 */
	public record RecordingChanges(GameStateController updatedGame, int updatedTime) {
		public RecordingChanges {
			assert updatedGame != null : "RecordingChanges can't have null GameStateController!";
			assert updatedTime >= 0 : "ChapEvent's time cannot be below 0";
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
	 * 
	 * @param currentLevel   The current level number.
	 * @param updateReciever Takes in recording changes so App can adapt to them.
	 */
	public Recorder(int currentLevel, Consumer<RecordingChanges> updateReciever) {
		assert updateReciever != null : "Null update reciever given to record during construction!";
		this.updateReciever = updateReciever;
		firstLevelSupplier = () -> {
			assert LoadFile.loadLevel(levelPaths.get(currentLevel)).isPresent()
					: "Exception occured when attempting to load first level for recorder!";
			return LoadFile.loadLevel(levelPaths.get(currentLevel)).get();
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
	 * Used by App module. Reads JSON file, interpreting data into a list of
	 * ChapEvent objects.
	 */
	public void loadRecording() {
		currentEventIndex = -1;
		handleRecording("load", rfc -> {
			events = eventMapper.readValue(rfc.recordingLoc,
					eventMapper.getTypeFactory().constructCollectionType(List.class, ChapEvent.class));
		});
		updateReciever.accept(new RecordingChanges(firstLevelSupplier.get(), events.get(0).time()));
	}

	/**
	 * Used by App module. Writes JSON file, by serializing a list of ChapEvent
	 * objects.
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
		assert direction != null : "ChapEvent can't have null direction!";
		ping(currentTime);
		events.set(events.size() - 1, new ChapEvent(direction, currentTime));
	}

	/**
	 * Used by App module to inform Recorder of an unspecified event In current
	 * state the only unspecified event that you would use this without the other
	 * ping method is for actor.
	 * 
	 * @param currentTime Time when user inputed to move chap.
	 */
	public void ping(int currentTime) {
		assert events != null : "Recorder events storage is null!";
		assert currentTime >= 0 : "Event time must be positive!";
		if (events.size() == EventsLimit)
			return;
		if (headEventIndex < events.size() - 1) {
			events = new ArrayList<>(events.subList(0, headEventIndex + 1));

		}
		headEventIndex++;
		events.add(() -> {
			return currentTime;
		});
	}

	/**
	 * Used by App module to just set the automatic replay speed.
	 * 
	 * @param eventsPerSecond Rate events play for auto replay
	 * @return Rate that autoReplaySpeed was set to.
	 */
	public int setPlaybackSpeed(int eventsPerSecond) {
		assert eventsPerSecond > 0 : "Playback speed must be positive.";
		return (autoReplaySpeed = eventsPerSecond);
	}

	/**
	 * Used by App module to go back to the previous chap movement.
	 */
	public void previousStep() {
		if (checkInvalidStepState()) return;
		lastStepMethodUsed = this::previousStep;
		findPreviousChapEvent();
		step();
	}

	/**
	 * Used by App module to go forward to the next chap movement.
	 */
	public void nextStep() {
		if (checkInvalidStepState()) return;
		lastStepMethodUsed = this::nextStep;

		if (currentEventIndex == -1) {
			currentEventIndex = 0;
			step();
			return;
		}
		findNextChapEvent();
		step();
	}

	/**
	 * Used to step recording forward to appropriate position from beginning.
	 * updates App using its receiver after doing so.
	 */
	private void step() {
		
		headEventIndex = (currentEventIndex = Math.max(currentEventIndex, 0));
		recordingGame = firstLevelSupplier.get();
		
		for (int i = 0; i <= currentEventIndex; i++) {
			// This line is needed to initialize App notifier for sound
			if(i == currentEventIndex) updateReciever.accept(new RecordingChanges(recordingGame, 0));
			events.get(i).run(recordingGame);
			
		}

		updateReciever.accept(new RecordingChanges(recordingGame, events.get(currentEventIndex).time()));
	}

	/**
	 * Provides information to tell if stepping should be occur.
	 * 
	 * @return boolean which is true if stepping should occur.
	 */
	private boolean checkInvalidStepState() {
		return events.isEmpty() || recordingGame == null;
	}

	/**
	 * Finds where the last chap event is in event list from current event's index.
	 * returns to start if there isn't a chap event to go back to.
	 */
	private void findPreviousChapEvent() {
		while (currentEventIndex > 1 && !(events.get(currentEventIndex - 1) instanceof ChapEvent)) {
			currentEventIndex--;
		}
		currentEventIndex--;
	}

	/**
	 * Finds where the next chap event is in event list from current event's index.
	 * goes to end if there isn't a chap event to go forward to.
	 */
	private void findNextChapEvent() {
		while (currentEventIndex < events.size() - 2 && !(events.get(currentEventIndex + 1) instanceof ChapEvent)) {
			currentEventIndex++;
		}
		currentEventIndex = Math.min(currentEventIndex + 1, events.size() - 1);
	}

	/**
	 * Used by App module to automatically step forward or backwards through
	 * recording. Can be toggled on and off.
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
					lastStepMethodUsed.run();
				}
			} catch (InterruptedException e) {
				return;
			}
		});

		replayThread.start();
	}

	/**
	 * Removes the most recent chap event added to event list.
	 * (Additionally any others in the way!)
	 */
	public void onGameLose() {

		if (events.isEmpty()) return;

		for (Event e = events.getLast(); !(e instanceof ChapEvent) && events.size() > 1; e = events.removeLast()) {
		}

		if (events.getLast() instanceof ChapEvent) events.removeLast();
	}

	/**
	 * Intended for making testing easier.
	 * 
	 * @return Chap's position in game model recording can view.
	 */
	public Chap getChap() {
		return recordingGame.getChap();
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
