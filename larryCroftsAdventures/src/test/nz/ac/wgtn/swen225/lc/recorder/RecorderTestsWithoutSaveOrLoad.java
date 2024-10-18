package test.nz.ac.wgtn.swen225.lc.recorder;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.domain.Maze;
import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;
import nz.ac.wgtn.swen225.lc.persistency.LoadFile;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.recorder.Recorder;
import nz.ac.wgtn.swen225.lc.recorder.Recorder.RecordingChanges;
import nz.ac.wgtn.swen225.lc.app.AppNotifier;
import nz.ac.wgtn.swen225.lc.persistency.Paths;
public class RecorderTestsWithoutSaveOrLoad {
	
	private GameStateController gameModel;
	private GameStateController gameModelRecorder;
    private int time = 60;
    
    private Consumer<RecordingChanges> crc;
    private Supplier<GameState> mockGameStateSupplier;
    private Recorder testRec;
    private AppNotifier mockAppNotifier = new AppNotifier(){
		
		public void onGameWin() {}

		
		public void onGameLose() {}

		
		public void onKeyPickup(String keyName) {}

		public void onTreasurePickup(int treasureCount) {}}; 
    @BeforeEach
    public void setup() {
        Supplier<Maze> mockMazeSupplier = ()->Maze.createBasicMaze(8, 8);
        Supplier<Chap> mockChapSupplier = ()->new Chap(1, 1, new ArrayList<>()); 
        mockGameStateSupplier = ()->new GameState(mockMazeSupplier.get(), mockChapSupplier.get(),
        		10, 0, Map.of(), time, mockAppNotifier, new ArrayList<>(), 1);
        gameModel = new GameStateController(mockGameStateSupplier.get());
        crc = (rc) -> {
            time = rc.updatedTime();
            gameModelRecorder = rc.updatedGame();
        };
        testRec = new Recorder(crc, ()->new GameStateController(mockGameStateSupplier.get()));
    }
	

    @Test
    public void testPreviousStepWithNoEvents() {
        testRec.previousStep();
        assert time == 60;
    }

    @Test
    public void testNextStepWithNoEvents() {
        testRec.nextStep();
        assert time == 60;
    }

    @Test
    public void testPingWithValidInput() {
        testRec.ping(Direction.Left, 60);
    }
	

	@Test
	public void checkDirectionEventTimeAboveZero() {
		try {
			testRec.ping(Direction.Left, -1);
		} catch (AssertionError ae) {
			if (ae.getMessage().equals("Event time must be positive!"))
				return;
		}
		assert false;

	}
	
	@Test
	public void testRecordingWithLargeMaze() {
		Supplier<GameStateController> largeGameGiver = ()->new GameStateController(
				new GameState(Maze.createBasicMaze(1000, 1000), new Chap(1, 1, new ArrayList<>()), 10, 0, 
						Map.of(), time, mockAppNotifier, new ArrayList<>(), 1)); 
		
		GameStateController mainGame = largeGameGiver.get();
		Recorder testRec = new Recorder(crc, largeGameGiver);
		
		List<Direction> dirOptions = List.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
		Random random = new Random();

		for (int i = 0; i < 1000; i++) {
			Direction d = dirOptions.get(random.nextInt(dirOptions.size()));
			try {
				mainGame.moveChap(d);
				testRec.ping(d, i);
				testRec.nextStep();
			} catch (IllegalArgumentException iae) {
				continue;
			}
		}

		checkChapPosMatch();
	}
	

	@Test
	public void checkDirectionEventDirectionValid() {
		try {
			testRec.ping(null, -1);
		} catch (AssertionError ae) {
			if (ae.getMessage().equals("ChapEvent can't have null direction!"))
				return;
		}
		assert false;

	}

	@Test
	public void testPingAndNextStepWithMaxInteger() {
		testRec.ping(Direction.Down, Integer.MAX_VALUE);
		testRec.nextStep();
		assert time == Integer.MAX_VALUE;
	}



	@Test
	public void testPingWithNextAndPrev() {
		for (int j = 0; j < 100; j++) {
			setup();
			List<Direction> dirOptions = List.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
			for (int i = 0;  i < 50; i++) {
				
				Direction d = dirOptions.get(new Random().nextInt(dirOptions.size()));
				
				try{
					gameModel.moveChap(d);
		            testRec.ping(d, i);
		            testRec.nextStep();
				} catch(IllegalArgumentException iae) {
					i--;
				}
	           
	        }

	        for (int i = 0; i < 25; i++) {
	            testRec.previousStep();
	        }

	        for (int i = 0; i < 25; i++) {
	            testRec.nextStep();
	        }
			
	        checkChapPosMatch();
		}

	}
	
	
	@Test
	public void testPingAndNextStepWithTenTime() {
	    testRec.ping(Direction.Down, 10);
	    testRec.nextStep();
	    testRec.nextStep();
	    assert time == 10;
	    assert time == 10;
	}
	
	@Test
	public void testPreviousStepAtStart() {
	    testRec.ping(Direction.Down, 10);
	    testRec.previousStep();
	    testRec.previousStep();
	    assert time == 10;
	    assert time == 10;
	}
	
	@Test
	public void testCircleDirections() {
	   
	    List<Direction> dirOptions = List.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
	
	    for (int i = 0; i < 1000; i++) {
	        Direction d = dirOptions.get(i % 4);
	        try {
	        	gameModel.moveChap(d);
	            testRec.ping(d, i);
	            testRec.nextStep();
	        } catch (IllegalArgumentException iae) {
	            continue;
	        }
	    }
	
	    checkChapPosMatch();
	}
	
	@Test
	public void testPlaybackSpeedValid() {
	    Random r = new Random();
	    for(int i = 0; i < 10000; i ++) {
	        int randomNumber = Math.max(1, r.nextInt());
	        assert testRec.setPlaybackSpeed(randomNumber) == randomNumber;
	    }
	}
	
	@Test
	public void testAutoReplayForwards() {
	   
        testRec = new Recorder(crc, ()->new GameStateController(mockGameStateSupplier.get()));
        List<Direction> dirOptions = List.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
        for (int i = 0; i < 100; i++) {
            Direction d = dirOptions.get(new Random().nextInt(dirOptions.size()));
            try {
                gameModel.moveChap(d);
                testRec.ping(d, 60);
            } catch (IllegalArgumentException iae) {
                continue;
            }
        }
        testRec.setPlaybackSpeed(100000);
        testRec.autoReplay();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {}
        checkChapPosMatch();
	    
	}
	
	@Test
	public void testAutoReplayBackwards() {
		GameStateController throwAwayGameModel = new GameStateController(mockGameStateSupplier.get());
	    testRec = new Recorder(crc, ()->new GameStateController(mockGameStateSupplier.get()));
	    List<Direction> dirOptions = List.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
	    for (int i = 0; i < 1000; i++) {
	        Direction d = dirOptions.get(new Random().nextInt(dirOptions.size()));
	        try {
	        	throwAwayGameModel.moveChap(d);
	        	
	        	if(gameModel.getChap().getCol() == 1 &&
	        			gameModel.getChap().getRow() == 1)gameModel.moveChap(d);
	        	
	            testRec.ping(d, 60);
	        } catch (IllegalArgumentException iae) {
	            continue;
	        }
	    }
	    
	    for (int i = 0; i < 100; i++) {
	        testRec.nextStep();
	    }
	    
	  
	    testRec.previousStep();
	    testRec.setPlaybackSpeed(109000);
	    testRec.autoReplay();
	    try {
	        Thread.sleep(1000);
	    } catch (InterruptedException e) {}
	    
	    checkChapPosMatch();
	}
	
	@Test
	public void testNullFirstLevelSupplier() {
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
	
	
	@Test
	public void testInvalidMinSetAutoReplaySpeed() {
	    try {
	        testRec.setPlaybackSpeed(0);
	    } catch(AssertionError ae){
	        if(ae.getMessage().equals("Playback speed must be positive.")) {
	            return;
	        } 
	    }
	    assert false;
	}
	
	@Test
	public void testValidMinSetAutoReplaySpeed() {
	    assert testRec.setPlaybackSpeed(1) == 1;
	}
	
	@Test
	public void testOnGameLoseWithEmptyEvents() {
	    testRec.onGameLose();
	    testRec.nextStep();
	    assert time == 60;
	}
	
	@Test
	public void testOnGameLoseWithEvents() {
	    for(int i = 0; i < 100; i++) {
	    	for(int j = 0; j < new Random().nextInt(100); j++) {
	    		 if(new Random().nextBoolean()) testRec.ping(i);
	    		 else {
	    			 List<Direction> dirOptions = List.of(
	    					 Direction.Up, Direction.Down, Direction.Left, Direction.Right);
	    			 Direction d = dirOptions.get(new Random().nextInt(dirOptions.size()));
	    			 testRec.ping(d, i);
	    		 }
	    		 try {
	 	        	testRec.nextStep();
	 	        } catch (IllegalArgumentException iae) {
	 	            continue;
	 	        }
	    		 
	    	}
	    	testRec.onGameLose();
	    }
	}
	
	@Test
	public void pingStressTest() {
	    for (int i = 0; i < 1000000; i++) {
	        testRec.ping(Direction.Up, i);
	    }
	}
	
	@Test
	public void testsCreatingNonTestingConstructor() {
	    testRec = new Recorder(1, crc);
	    testRec = new Recorder(2, crc);
	    try {
	    	for(int i = -100000; i < 100000; i++) {
	    		if (i != 1 && i != 2) testRec = new Recorder(3, crc);
	    	}
	    } catch(IllegalArgumentException iae) {
	    	return;
	    }
	    assert false;
	}
	
	@Test
	public void testsUsingNonTestingConstructor() {
	    testRec = new Recorder(1, crc);
	    GameStateController throwAwayGameModel =  LoadFile.loadLevel(Paths.level1).get();
	    gameModel = LoadFile.loadLevel(Paths.level1).get();
	    List<Direction> dirOptions = List.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
	    for (int i = 0; i < 100; i++) {
	        Direction d = dirOptions.get(new Random().nextInt(dirOptions.size()));
	        try {
	        	throwAwayGameModel.moveChap(d);
	        	
	        	if(gameModel.getChap().getCol() == 9 &&
	        			gameModel.getChap().getRow() == 13)gameModel.moveChap(d);
	        	
	            testRec.ping(d, 60);
	        } catch (IllegalArgumentException iae) {
	            continue;
	        }
	    }
	    
	    for (int i = 0; i < 100; i++) {
	        testRec.nextStep();
	    }
	    
	    testRec.previousStep();
	    testRec.setPlaybackSpeed(100000);
	    testRec.autoReplay();
	    try {
	        Thread.sleep(5000);
	    } catch (InterruptedException e) {}
	    
	    checkChapPosMatch();
	}
	
	@Test
	public void testsUsingNonTestingConstructorAndActorPing() {
	    testRec = new Recorder(1, crc);
	    gameModel = LoadFile.loadLevel(Paths.level1).get();
	    int successfulMoves = 0;
	    List<Direction> dirOptions = List.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
	    for (int i = 0; i < 100; i++) {
	        Direction d = dirOptions.get(new Random().nextInt(dirOptions.size()));
	        try {
	        	if(successfulMoves < 50) gameModel.moveChap(d); 
	            testRec.ping(d, 60);
	            successfulMoves++;
	            for(int j = 0; j < 100; j++) testRec.ping(j);
	        } catch (IllegalArgumentException iae) {
	            continue;
	        }
	    }
	    
	    for (int i = 0; i < 50; i++) {
	        testRec.nextStep();
	    }
	    
	    checkChapPosMatch();
	}
	    
	public void checkChapPosMatch() {
		assert testRec.getChap().getRow() == gameModel.getChap().getRow()
		        : testRec.getChap().getRow() + ":Row:" + gameModel.getChap().getRow();
	    assert testRec.getChap().getCol() == gameModel.getChap().getCol()
	        : testRec.getChap().getCol() + ":Col:" + gameModel.getChap().getCol();
	}
}