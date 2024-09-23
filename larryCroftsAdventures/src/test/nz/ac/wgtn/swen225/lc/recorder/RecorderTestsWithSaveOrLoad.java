package test.nz.ac.wgtn.swen225.lc.recorder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.junit.jupiter.api.Test;
import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;
import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.recorder.Recorder;
import nz.ac.wgtn.swen225.lc.recorder.Recorder.RecordingChanges;
import nz.ac.wgtn.swen225.lc.recorder.Recorder.*;

 

public class RecorderTestsWithSaveOrLoad {
	private GameStateController gameModel;
	private int time;
	private Consumer<RecordingChanges> crc = (rc)->{time = rc.updatedTime(); gameModel = rc.updatedGame();};
	public Supplier<GameStateController> firstLevelSupplier(){return ()->new GameStateController(8, 8, 1, 1, 10);}
	
	@Test
	public void saveAndLoadRecording1() {
		GameStateController mainGame = firstLevelSupplier().get();
		Recorder testRec = new Recorder(crc, firstLevelSupplier());
		List<Direction> dirOptions = List.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
		for (int i = 0; i < 1000; i++) {
			final Direction d = dirOptions.get(new Random().nextInt(dirOptions.size()));
			try {
				mainGame.moveChap(d);
				testRec.ping(d, i);
			} catch (IllegalArgumentException iae) {
				continue;
			}
		}
		testRec.saveRecording();
		testRec.loadRecording();

		for (int i = 0; i < 10000; i++) {
			try {
				testRec.nextStep();
				assert time < 1000;
			} catch (IllegalArgumentException iae) {
				continue;
			}

		}
		assert testRec.getChapPosition().equals(mainGame.getChapPosition())
				: testRec.getChapPosition() + ":" + mainGame.getChapPosition();
	}

	@Test
	public void testSaveLoadWithLargeNumberOfEvents() {
		GameStateController mainGame = firstLevelSupplier().get();
		Recorder testRec = new Recorder(crc, firstLevelSupplier());
		List<Direction> dirOptions = List.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
		Random random = new Random();

		for (int i = 0; i < 1000000; i++) {
			Direction d = dirOptions.get(random.nextInt(dirOptions.size()));
			try {
				mainGame.moveChap(d);
				testRec.ping(d, i);
			} catch (IllegalArgumentException iae) {
				continue;
			}
		}

		testRec.saveRecording();
		testRec.loadRecording();

		for (int i = 0; i < 1000000; i++) {
			try {
				testRec.nextStep();
				assert time < 1000000;
			} catch (IllegalArgumentException iae) {
				continue;
			}
		}

		assert testRec.getChapPosition().equals(mainGame.getChapPosition())
				: testRec.getChapPosition() + ":" + mainGame.getChapPosition();
	}

	@Test
	public void testMultipleSaveAndLoad() {
		GameStateController mainGame = firstLevelSupplier().get();
		Recorder testRec = new Recorder(crc, firstLevelSupplier());
		List<Direction> dirOptions = List.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
		Random random = new Random();

		for (int i = 0; i < 100; i++) {
			Direction d = dirOptions.get(random.nextInt(dirOptions.size()));
			try {
				mainGame.moveChap(d);
				testRec.ping(d, i);
			} catch (IllegalArgumentException iae) {
				continue;
			}
			
		}

		testRec.saveRecording();
		testRec.loadRecording();
		testRec.saveRecording();
		testRec.loadRecording();

		for (int i = 0; i < 100; i++) {
			try {
				testRec.nextStep();
				assert time < 100;
			} catch (IllegalArgumentException iae) {
				continue;
			}
		}

		assert testRec.getChapPosition().equals(mainGame.getChapPosition())
				: testRec.getChapPosition() + ":" + mainGame.getChapPosition();
	}
	
	
	
	
}
