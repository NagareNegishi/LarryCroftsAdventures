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
	@Test
	public void testPingWithNextStep() {
		for (int j = 0; j < 100; j++) {
			GameStateController mainGame = new GameStateController(8, 8, 1, 1, 10);
			GameStateController recordGame = new GameStateController(8, 8, 1, 1, 10);
			Recorder testRec = new Recorder(recordGame);
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
			Supplier<GameStateController> gameSupplier = () -> {
				return new GameStateController(8, 8, 1, 1, 10);
			};
			GameStateController mainGame = gameSupplier.get();
			GameStateController recordGame = gameSupplier.get();
			Recorder testRec = new Recorder(recordGame);
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
					testRec.previousStep(gameSupplier.get());
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
			GameStateController mainGame = new GameStateController(8, 8, 1, 1, 10);
			GameStateController recordGame = new GameStateController(8, 8, 1, 1, 10);
			Recorder testRec = new Recorder(recordGame);
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
					testRec.previousStep(new GameStateController(8, 8, 1, 1, 10));
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
		GameStateController recordGame = new GameStateController(8, 8, 1, 1, 10);
		Recorder testRec = new Recorder(recordGame);

		testRec.ping(Direction.Down, 10);
		assert testRec.nextStep() == 10;
		assert testRec.nextStep() == 10;
	}

	@Test
	public void testPreviousStepAtStart() {
		GameStateController recordGame = new GameStateController(8, 8, 1, 1, 10);
		Recorder testRec = new Recorder(recordGame);

		testRec.ping(Direction.Down, 10);
		assert testRec.previousStep(new GameStateController(8, 8, 1, 1, 10)) == 10;
		assert testRec.previousStep(new GameStateController(8, 8, 1, 1, 10)) == 10;

	}

	@Test
	public void saveAndLoadRecording1() {
		GameStateController mainGame = new GameStateController(8, 8, 1, 1, 10);
		GameStateController recordGame = new GameStateController(8, 8, 1, 1, 10);
		Recorder testRec = new Recorder(recordGame);
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
				assert testRec.nextStep() < 1000;
			} catch (IllegalArgumentException iae) {
				continue;
			}

		}
		assert testRec.getChapPosition().equals(mainGame.getChapPosition())
				: testRec.getChapPosition() + ":" + mainGame.getChapPosition();
	}

	@Test
	public void testSaveLoadWithLargeNumberOfEvents() {
		GameStateController mainGame = new GameStateController(10, 10, 1, 1, 20);
		GameStateController recordGame = new GameStateController(10, 10, 1, 1, 20);
		Recorder testRec = new Recorder(recordGame);
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
				assert testRec.nextStep() < 1000000;
			} catch (IllegalArgumentException iae) {
				continue;
			}
		}

		assert testRec.getChapPosition().equals(mainGame.getChapPosition())
				: testRec.getChapPosition() + ":" + mainGame.getChapPosition();
	}

	@Test
	public void testingPrevStepWithNoEvents() {

		GameStateController recordGame = new GameStateController(8, 8, 1, 1, 10);
		Recorder testRec = new Recorder(recordGame);

		try {
			testRec.previousStep(recordGame);
		} catch (AssertionError ae) {
			if (ae.getMessage().equals("Recording is empty!"))
				return;
		}
		assert false;

	}

	@Test
	public void testingNextStepWithNoEvents() {

		GameStateController recordGame = new GameStateController(8, 8, 1, 1, 10);
		Recorder testRec = new Recorder(recordGame);
		try {
			testRec.nextStep();
		} catch (AssertionError ae) {
			if (ae.getMessage().equals("Recording is empty!"))
				return;
		}
		assert false;

	}

	@Test
	public void checkNoInitialisationWithNull() {

		GameStateController recordGame = null;

		try {
			Recorder testRec = new Recorder(recordGame);
		} catch (AssertionError ae) {
			if (ae.getMessage().equals("Cannot create Recorder object with null GameStateController!"))
				return;
		}
		assert false;

	}

	@Test
	public void checkNoPrevStepWithNull() {

		GameStateController recordGame = new GameStateController(8, 8, 1, 1, 10);

		try {
			Recorder testRec = new Recorder(recordGame);
			testRec.previousStep(null);
		} catch (AssertionError ae) {
			if (ae.getMessage().equals("Cannot call previousStep on Recorder object with null GameStateController!"))
				return;
		}
		assert false;

	}

	@Test
	public void checkDirectionEventTimeAboveZero() {

		GameStateController recordGame = new GameStateController(8, 8, 1, 1, 10);

		try {
			Recorder testRec = new Recorder(recordGame);
			testRec.ping(Direction.Left, -1);
		} catch (AssertionError ae) {
			if (ae.getMessage().equals("DirectionEvent's time cannot be below 0"))
				return;
		}
		assert false;

	}

	@Test
	public void validDirectionEvent() {

		GameStateController recordGame = new GameStateController(8, 8, 1, 1, 10);

		Recorder testRec = new Recorder(recordGame);
		testRec.ping(Direction.Left, 60);

	}

	@Test
	public void checkDirectionEventDirectionValid() {

		GameStateController recordGame = new GameStateController(8, 8, 1, 1, 10);

		try {
			Recorder testRec = new Recorder(recordGame);
			testRec.ping(null, -1);
		} catch (AssertionError ae) {
			if (ae.getMessage().equals("DirectionEvent can't have null direction!"))
				return;
		}
		assert false;

	}

	@Test
	public void testPingAndNextStepWithMaxInteger() {
		GameStateController recordGame = new GameStateController(8, 8, 1, 1, 10);
		Recorder testRec = new Recorder(recordGame);

		testRec.ping(Direction.Down, Integer.MAX_VALUE);
		assert testRec.nextStep() == Integer.MAX_VALUE;
	}

	@Test
	public void testRecordingWithLargeMaze() {
		GameStateController mainGame = new GameStateController(1000, 1000, 1, 1, 10);
		GameStateController recordGame = new GameStateController(1000, 1000, 1, 1, 10);
		Recorder testRec = new Recorder(recordGame);
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
		GameStateController mainGame = new GameStateController(8, 8, 1, 1, 10);
		Recorder testRec = new Recorder(mainGame);
		List<Direction> dirOptions = List.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
		Random random = new Random();

		for (int i = 0; i < 100; i++) {
			Direction d = dirOptions.get(random.nextInt(dirOptions.size()));
			testRec.ping(d, i);
		}

		testRec.saveRecording();
		testRec.loadRecording();
		testRec.saveRecording();
		testRec.loadRecording();

		for (int i = 0; i < 100; i++) {
			try {
				assert testRec.nextStep() < 100;
			} catch (IllegalArgumentException iae) {
				continue;
			}
		}

		assert testRec.getChapPosition().equals(mainGame.getChapPosition())
				: testRec.getChapPosition() + ":" + mainGame.getChapPosition();
	}

	@Test
	public void testCircleDirections() {
		GameStateController mainGame = new GameStateController(8, 8, 1, 1, 10);
		GameStateController recordGame = new GameStateController(8, 8, 1, 1, 10);
		Recorder testRec = new Recorder(recordGame);
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
