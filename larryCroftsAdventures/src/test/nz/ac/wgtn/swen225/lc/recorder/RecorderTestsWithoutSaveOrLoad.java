package test.nz.ac.wgtn.swen225.lc.recorder;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;
import nz.ac.wgtn.swen225.lc.recorder.Recorder;
import nz.ac.wgtn.swen225.lc.recorder.Recorder.RecordingChanges;

public class RecorderTestsWithoutSaveOrLoad {
	
	private GameStateController gameModel;
	private int time;
	private Consumer<RecordingChanges> crc = (rc)->{time = rc.updatedTime(); gameModel = rc.updatedGame();};
	public Supplier<GameStateController> firstLevelSupplier(){return ()->new GameStateController(8, 8, 1, 1, 10);}
	

	@Test
	public void testingPrevStepWithNoEvents() {

		Recorder testRec = new Recorder(crc, firstLevelSupplier());

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

		Recorder testRec = new Recorder(crc, firstLevelSupplier());
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
			Recorder testRec = new Recorder(crc, firstLevelSupplier());
			testRec.ping(Direction.Left, -1);
		} catch (AssertionError ae) {
			if (ae.getMessage().equals("DirectionEvent's time cannot be below 0"))
				return;
		}
		assert false;

	}
	
	@Test
	public void testRecordingWithLargeMaze() {
		GameStateController mainGame = new GameStateController(1000, 1000, 1, 1, 10);
		Recorder testRec = new Recorder(crc, ()->new GameStateController(1000, 1000, 1, 1, 10));
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
	public void validDirectionEvent() {


		Recorder testRec = new Recorder(crc, firstLevelSupplier());
		testRec.ping(Direction.Left, 60);

	}

	@Test
	public void checkDirectionEventDirectionValid() {
		try {
			Recorder testRec = new Recorder(crc, firstLevelSupplier());
			testRec.ping(null, -1);
		} catch (AssertionError ae) {
			if (ae.getMessage().equals("DirectionEvent can't have null direction!"))
				return;
		}
		assert false;

	}

	@Test
	public void testPingAndNextStepWithMaxInteger() {
		Recorder testRec = new Recorder(crc, firstLevelSupplier());

		testRec.ping(Direction.Down, Integer.MAX_VALUE);
		testRec.nextStep();
		assert time == Integer.MAX_VALUE;
	}
	
	@Test
	public void testPingWithNextStep() {
		for (int j = 0; j < 100; j++) {
			
			GameStateController mainGame = firstLevelSupplier().get();
			Recorder testRec = new Recorder(crc, firstLevelSupplier());
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
			Recorder testRec = new Recorder(crc, firstLevelSupplier());
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
			Recorder testRec = new Recorder(crc, firstLevelSupplier());
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
		Recorder testRec = new Recorder(crc, firstLevelSupplier());

		testRec.ping(Direction.Down, 10);
		testRec.nextStep();
		testRec.nextStep();
		assert time == 10;
		assert time == 10;
	}

	@Test
	public void testPreviousStepAtStart() {
		
		Recorder testRec = new Recorder(crc, firstLevelSupplier());

		testRec.ping(Direction.Down, 10);
		testRec.previousStep();
		testRec.previousStep();
		assert time == 10;
		assert time == 10;

	}

	
	@Test
	public void testCircleDirections() {
		GameStateController mainGame = firstLevelSupplier().get();
		Recorder testRec = new Recorder(crc, firstLevelSupplier());
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
	
	@Test
	public void testPlaybackSpeedValid() {
		
		Recorder testRec = new Recorder(crc, firstLevelSupplier());
		Random r = new Random();
		for(int i = 0; i < 10000; i ++) {
			int randomNumber = Math.max(1, r.nextInt());
			assert testRec.setPlaybackSpeed(randomNumber) == randomNumber;
		}
		
	}
	
	@Test
	public void testAutoReplay() {
		for (int j = 0; j < 10; j++) {
			
			GameStateController mainGame = firstLevelSupplier().get();
			Recorder testRec = new Recorder(crc, firstLevelSupplier());
			List<Direction> dirOptions = List.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
			for (int i = 0; i < 1000; i++) {
				final Direction d = dirOptions.get(new Random().nextInt(dirOptions.size()));
				try {
					mainGame.moveChap(d);
					testRec.ping(d, 60);

				} catch (IllegalArgumentException iae) {
					continue;
				}
			}
			testRec.setPlaybackSpeed(100000);
			testRec.autoReplay();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				continue;
			}
			assert testRec.getChapPosition().equals(mainGame.getChapPosition())
					: testRec.getChapPosition() + ":" + mainGame.getChapPosition();
		}
	}
	
	@Test
	public void testNullFirstLevelSupplier() {
		for (int j = 0; j < 10; j++) {
			try {
			Recorder testRec = new Recorder(crc, null);
			} catch(AssertionError ae) {
				if(ae.getMessage().equals(
						"Null first level supplier given to record during construction!")) {
					return;
				} 
			}
			assert false;
		}
	}
	
	@Test
	public void testNullUpdateReciever() {
		for (int j = 0; j < 10; j++) {
			try {
			Recorder testRec = new Recorder(null);
			} catch(AssertionError ae) {
				if(ae.getMessage().equals(
					"Null update reciever given to record during construction!")) {
					return;
				} 
			}
			assert false;
		}
	}
	
	@Test
	public void testNullUpdateReciever2() {
		for (int j = 0; j < 10; j++) {
			try {
			Recorder testRec = new Recorder(null, firstLevelSupplier());
			} catch(AssertionError ae) {
				if(ae.getMessage().equals(
					"Null update reciever given to record during construction!")) {
					return;
				} 
			}
			assert false;
		}
	}
	
	@Test
	public void testInvalidMinSetAutoReplaySpeed() {
		try {
			Recorder testRec = new Recorder(crc, firstLevelSupplier());
			testRec.setPlaybackSpeed(0);
		} catch(AssertionError ae){
			if(ae.getMessage().equals(
				"Auto replay speed must be above zero!")) {
				return;
			} 
		}
		assert false;
	}
	
	@Test
	public void testValidMinSetAutoReplaySpeed() {
		Recorder testRec = new Recorder(crc, firstLevelSupplier());
		assert testRec.setPlaybackSpeed(1) == 1;
	}
	
	@Test
	public void testPingWithInvalidEventList() {
		Recorder testRec = new Recorder(crc, firstLevelSupplier());
		
	    try {
	    	Field eventsField = Recorder.class.getDeclaredField("events");
		    eventsField.setAccessible(true);
			eventsField.set(testRec, null);
		} catch (IllegalArgumentException | IllegalAccessException 
				| NoSuchFieldException | SecurityException e) {
			return;
		}
	    
	    try {
	    testRec.ping(Direction.Up, 1);
	    }catch(AssertionError ae) {
	    	if(ae.getMessage().equals(
					"Recorder events storage is null!")) {
				return;
			} 
	    }
	    
	    assert false;
	    
	}
	
	@Test
	public void pingStressTest() {
		Recorder testRec = new Recorder(crc, firstLevelSupplier());
	    for (int i = 0; i < Integer.MAX_VALUE; i++) {
	        testRec.ping(Direction.Up, i);
	    }
	    
	}
}
