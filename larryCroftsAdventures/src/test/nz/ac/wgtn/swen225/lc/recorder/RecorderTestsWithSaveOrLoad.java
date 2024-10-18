package test.nz.ac.wgtn.swen225.lc.recorder;

import nz.ac.wgtn.swen225.lc.domain.Maze;
import nz.ac.wgtn.swen225.lc.domain.GameStateController; 
import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.recorder.Recorder;
import nz.ac.wgtn.swen225.lc.recorder.Recorder.RecordingChanges;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import nz.ac.wgtn.swen225.lc.app.AppNotifier;

public class RecorderTestsWithSaveOrLoad {
    private GameStateController gameModel;
	private GameStateController gameModelRecorder;
    private int time = 60;
    private Consumer<RecordingChanges> crc;
    private Supplier<GameState> mockGameStateSupplier;
    private Recorder testRec;
    private AppNotifier mockAppNotifier = new AppNotifier() {
        public void onGameWin() {}
        public void onGameLose() {}
        public void onKeyPickup(String keyName) {}
        public void onTreasurePickup(int treasureCount) {}
    };

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
    public void testSaveLoadWithLargeNumberOfEvents() {
        GameStateController mainGame = new GameStateController(mockGameStateSupplier.get());
        List<Direction> dirOptions = List.of(Direction.Up, Direction.Down, Direction.Left, Direction.Right);
        Random random = new Random();

        for (int i = 0; i < 10000; i++) {
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

        for (int i = 0; i < 10000; i++) {
            try {
                testRec.nextStep();
                assert time < 10000;
            } catch (IllegalArgumentException iae) {
                continue;
            }
        }

		assert testRec.getChap().getRow() == mainGame.getChap().getRow()
				: testRec.getChap().getRow() + ":Row:" + mainGame.getChap().getRow();
		assert testRec.getChap().getCol() == mainGame.getChap().getCol()
				: testRec.getChap().getCol() + ":Col:" + mainGame.getChap().getCol();
    }

    @Test
    public void testMultipleSaveAndLoad() {
        GameStateController mainGame = new GameStateController(mockGameStateSupplier.get());
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

		assert testRec.getChap().getRow() == mainGame.getChap().getRow()
				: testRec.getChap().getRow() + ":Row:" + mainGame.getChap().getRow();
		assert testRec.getChap().getCol() == mainGame.getChap().getCol()
				: testRec.getChap().getCol() + ":Col:" + mainGame.getChap().getCol();
    }
}