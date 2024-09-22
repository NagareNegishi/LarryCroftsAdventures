package test.nz.ac.wgtn.swen225.lc.recorder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.junit.jupiter.api.Test;
import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;
import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.recorder.Recorder;

import nz.ac.wgtn.swen225.lc.recorder.Recorder.*;

 

public class RecorderTests {
	public Supplier<GameStateController> firstLevelSupplier(){return ()->new GameStateController(8, 8, 1, 1, 10);}
	@Test
	public void testPingWithNextStep() {
		for (int j = 0; j < 100; j++) {
			
			GameStateController mainGame = firstLevelSupplier().get();
			Recorder testRec = new Recorder(firstLevelSupplier());
			List<Direction> dirOptions = List.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
			for (int i = 0; i < 1000; i++) {
				final Direction d = dirOptions.get(new Random().nextInt(dirOptions.size()));
				try {
					mainGame.moveChap(d);
					testRec.ping(d, 60);
					testRec.nextStep();
				} catch (IllegalArgumentException iae) {
					continue;
				}
			}
			assert testRec.getChapPosition().equals(mainGame.getChapPosition())
					: testRec.getChapPosition() + ":" + mainGame.getChapPosition();
		}
	}

	@Test
	public void testPingWithNextAndPrev() {
		for (int j = 0; j < 100; j++) {
			GameStateController mainGame = firstLevelSupplier().get();
			Recorder testRec = new Recorder(firstLevelSupplier());
			int successfulMainMoves = 0;
			int successfulRecordMoves = 0;
			List<Direction> dirOptions = List.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
			for (int i = 0; i < 1000; i++) {
				final Direction d = dirOptions.get(new Random().nextInt(dirOptions.size()));
				try {
					if (i < 500) {
						mainGame.moveChap(d);
						successfulMainMoves++;
					}
					testRec.ping(d, 60);
					successfulRecordMoves++;
					testRec.nextStep();
				} catch (IllegalArgumentException iae) {
					continue;
				}

			}
			for (int i = 0; i < 1000 - (successfulRecordMoves - successfulMainMoves); i++) {
				try {
					testRec.previousStep();
				} catch (IllegalArgumentException iae) {
					continue;
				}

			}
			assert testRec.getChapPosition().equals(mainGame.getChapPosition())
					: testRec.getChapPosition() + ":" + mainGame.getChapPosition();
		}

	}

	@Test
	public void testPingWithMultipleNextAndPrev() {
		for (int j = 0; j < 50; j++) {
			GameStateController mainGame = firstLevelSupplier().get();
			Recorder testRec = new Recorder(firstLevelSupplier());
			List<Direction> dirOptions = List.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
			Random random = new Random();

			for (int i = 0; i < 500; i++) {
				Direction d = dirOptions.get(random.nextInt(dirOptions.size()));
				try {
					mainGame.moveChap(d);
					testRec.ping(d, i);
					testRec.nextStep();
				} catch (IllegalArgumentException iae) {
					continue;
				}
			}

			for (int i = 0; i < 250; i++) {
				try {
					testRec.previousStep();
				} catch (IllegalArgumentException iae) {
					continue;
				}
			}

			for (int i = 0; i < 250; i++) {
				try {
					testRec.nextStep();
				} catch (IllegalArgumentException iae) {
					continue;
				}
			}

			assert testRec.getChapPosition().equals(mainGame.getChapPosition())
					: testRec.getChapPosition() + ":" + mainGame.getChapPosition();
		}
	}

	@Test
	public void testPingAndNextStepWithTenTime() {
		Recorder testRec = new Recorder(firstLevelSupplier());

		testRec.ping(Direction.Down, 10);
		assert testRec.nextStep().updatedTime() == 10;
		assert testRec.nextStep().updatedTime() == 10;
	}

	@Test
	public void testPreviousStepAtStart() {
		
		Recorder testRec = new Recorder(firstLevelSupplier());

		testRec.ping(Direction.Down, 10);
		assert testRec.previousStep().updatedTime() == 10;
		assert testRec.previousStep().updatedTime() == 10;

	}

	@Test
	public void saveAndLoadRecording1() {
		GameStateController mainGame = firstLevelSupplier().get();
		Recorder testRec = new Recorder(firstLevelSupplier());
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
				assert testRec.nextStep().updatedTime() < 1000;
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
		Recorder testRec = new Recorder(firstLevelSupplier());
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
				assert testRec.nextStep().updatedTime() < 1000000;
			} catch (IllegalArgumentException iae) {
				continue;
			}
		}

		assert testRec.getChapPosition().equals(mainGame.getChapPosition())
				: testRec.getChapPosition() + ":" + mainGame.getChapPosition();
	}

	@Test
	public void testingPrevStepWithNoEvents() {

		Recorder testRec = new Recorder(firstLevelSupplier());

		try {
			testRec.previousStep();
		} catch (AssertionError ae) {
			if (ae.getMessage().equals("Recording is empty!"))
				return;
		}
		assert false;

	}

	@Test
	public void testingNextStepWithNoEvents() {

		Recorder testRec = new Recorder(firstLevelSupplier());
		try {
			testRec.nextStep();
		} catch (AssertionError ae) {
			if (ae.getMessage().equals("Recording is empty!"))
				return;
		}
		assert false;

	}

	

	@Test
	public void checkDirectionEventTimeAboveZero() {


		try {
			Recorder testRec = new Recorder(firstLevelSupplier());
			testRec.ping(Direction.Left, -1);
		} catch (AssertionError ae) {
			if (ae.getMessage().equals("DirectionEvent's time cannot be below 0"))
				return;
		}
		assert false;

	}

	@Test
	public void validDirectionEvent() {


		Recorder testRec = new Recorder(firstLevelSupplier());
		testRec.ping(Direction.Left, 60);

	}

	@Test
	public void checkDirectionEventDirectionValid() {
		try {
			Recorder testRec = new Recorder(firstLevelSupplier());
			testRec.ping(null, -1);
		} catch (AssertionError ae) {
			if (ae.getMessage().equals("DirectionEvent can't have null direction!"))
				return;
		}
		assert false;

	}

	@Test
	public void testPingAndNextStepWithMaxInteger() {
		Recorder testRec = new Recorder(firstLevelSupplier());

		testRec.ping(Direction.Down, Integer.MAX_VALUE);
		assert testRec.nextStep().updatedTime() == Integer.MAX_VALUE;
	}

	@Test
	public void testRecordingWithLargeMaze() {
		GameStateController mainGame = new GameStateController(1000, 1000, 1, 1, 10);
		Recorder testRec = new Recorder(()->new GameStateController(1000, 1000, 1, 1, 10));
		List<Direction> dirOptions = List.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
		Random random = new Random();

		for (int i = 0; i < 10000; i++) {
			Direction d = dirOptions.get(random.nextInt(dirOptions.size()));
			try {
				mainGame.moveChap(d);
				testRec.ping(d, i);
				testRec.nextStep();
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
		Recorder testRec = new Recorder(firstLevelSupplier());
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
				assert testRec.nextStep().updatedTime() < 100;
			} catch (IllegalArgumentException iae) {
				continue;
			}
		}

		assert testRec.getChapPosition().equals(mainGame.getChapPosition())
				: testRec.getChapPosition() + ":" + mainGame.getChapPosition();
	}

	@Test
	public void testCircleDirections() {
		GameStateController mainGame = firstLevelSupplier().get();
		Recorder testRec = new Recorder(firstLevelSupplier());
		List<Direction> dirOptions = List.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);

		for (int i = 0; i < 1000; i++) {
			Direction d = dirOptions.get(i % 4);
			try {
				mainGame.moveChap(d);
				testRec.ping(d, i);
				testRec.nextStep();
			} catch (IllegalArgumentException iae) {
				continue;
			}
		}

		assert testRec.getChapPosition().equals(mainGame.getChapPosition())
				: testRec.getChapPosition() + ":" + mainGame.getChapPosition();
	}

}
