package nz.ac.wgtn.swen225.lc.app;

import java.util.HashMap;
import java.util.Map;

import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;
import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.domain.Maze;
import nz.ac.wgtn.swen225.lc.domain.Tile;

/**
 * Mock Controller for fuzz testing.
 * Initially created by Fuzz developer, and I asked him to move it to App package.
 * As App shouldn't allow other class to modify the controller,
 * but Fuzz need to test the game logic.
 *
 * @author Nagare Negishi
 * @studentID 300653779
 */
public class MockController extends Controller{
	public GameStateController stateController;

	/**
	 * Constructor for MockController class
	 * @param stateController contains the methods defined in GameStateController class
	 */
	public MockController(GameStateController stateController){
		super(stateController, createMockActionBindings(),60);
		this.stateController = stateController;
	}

	/**
	 * Mocking the action bindings
	 * @return a map of action bindings
	 */
	private static Map<String, Runnable> createMockActionBindings() {
		Map<String, Runnable> mockBindings = new HashMap<>();
        mockBindings.put("exitWithoutSaving", () -> {});
        mockBindings.put("exitAndSave", () -> {});
        mockBindings.put("resumeSavedGame", () -> {});
        mockBindings.put("startNewGame1", () -> {});
        mockBindings.put("startNewGame2", () -> {});
        mockBindings.put("pause", () -> {});
        mockBindings.put("unpause", () -> {});
        return mockBindings;
	}
	public void moveChap(Direction direction) {stateController.moveChap(direction);}
	public void moveActor() {stateController.moveActor();}
	public Maze getMaze() {return stateController.getMaze();}
	public Chap getChap() {return stateController.getChap();}
	public Tile getTileAtChapPosition() { return stateController.getMaze().getTile(stateController.getChap().getRow(), stateController.getChap().getCol());}	 

}
