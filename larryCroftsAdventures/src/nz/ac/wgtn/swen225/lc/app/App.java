package nz.ac.wgtn.swen225.lc.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.persistency.LoadFile;
import nz.ac.wgtn.swen225.lc.persistency.Paths;
import nz.ac.wgtn.swen225.lc.persistency.SaveFile;
import nz.ac.wgtn.swen225.lc.recorder.Recorder;
import nz.ac.wgtn.swen225.lc.renderer.Renderer;

/**
 * Main class for the game application.
 * Using JFrame to create the game window.
 *
 * @author Nagare Negishi
 * @studentID 300653779
 */
class App extends JFrame{
  private static final long serialVersionUID= 1L;

  private GameInfoPanel gameInfoPanel;
  private SidePanel sidePanel;

  private Timer gameTimer;
  private static final int MAX_TIME = 60;
  private int timeLeft = MAX_TIME;
  private int currentLevel = 1;
  private int keysCollectednum = 0;
  private Set<String> keysCollected = new HashSet<>();
  private int treasuresLeft;

  Runnable closePhase= ()->{};
  private Map<String, Runnable> actionBindings =  new HashMap<>(); // need to be passed to controller
  private Map<String, Action> actions = new HashMap<>(); // contains actions for buttons
  private GameStateController model;
  private AppNotifier notifier = getAppNotifier(); // need to be passed to model
  private Controller controller;
  private Renderer renderer;
  private Recorder recorder;
  private enum AppState {PLAY, PAUSED, GAMEOVER, VICTORY}
  private AppState state = AppState.PAUSED;

  private static int width = 800;
  private static int height = 400;
  private static final int MAX_LEVEL = 2; //its not pretty... but i need to check loadNextLevel failure
  private static boolean continueGame = false;
  
  /**
   * Functional interface for actions
   * Used to store actions in a map and showcase the use of strategy pattern
   */
  private interface Action{
    void execute();
  }

  App(){
    setTitle("Larry Croft's Adventures");
    assert SwingUtilities.isEventDispatchThread();
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    initializeModel();
    initializeUI();
    initializeActionBindings();
    initializeActions();
    initializeController();
    initializeGameTimer(); //must be after controller??

    setPreferredSize(new Dimension(width, height));
    pack();
    setVisible(true);
    addWindowListener(new WindowAdapter(){
      public void windowClosed(WindowEvent e){ 
        exitGame(false);
      }
    });
    setLevel(model);
    stopGame();
  }

  /**
   * Initialize the model for the game by loading the first level or a saved game.
   */
  private void initializeModel(){
    Optional<GameStateController> loadedGame = Optional.empty();
    if (continueGame) {
      loadedGame = LoadFile.loadSave(Paths.level1); //this will be special path 
    } else {
      loadedGame = LoadFile.loadLevel(Paths.level1);
    }
    if (loadedGame.isPresent()) {
      model = loadedGame.get();
    } else {
      handleFileError("Failed to load game", "Load Error", 
      new String[]{"Chose different file", "start level 1", "quit"}, "Chose different file",
      () -> loadGame(Paths.levelsDir, false));
    }
  }

  /**
   * Initialize the UI for the game.
   */
  private void initializeUI() {
    // Dialogs to pause the game
    GameDialogs.initializeDialogs(this);
    // game info
    gameInfoPanel = new GameInfoPanel(width/8, height);
    gameInfoPanel.setPreferredSize(new Dimension(width/8, height));
    add(gameInfoPanel, BorderLayout.EAST);
    // Side panel for menu/recorder UI
    sidePanel = new SidePanel(width/8, height, e -> handleAction(e),
    e -> handleAction(e), slider -> handleSliderChange(slider));
    add(sidePanel, BorderLayout.WEST);
    // Center panel for game rendering
    renderer = new Renderer();
    add(renderer, BorderLayout.CENTER);
    GameDialogs.START.show();
}

  /**
   * Initialize the actions for buttons in the UI
   */
  private void initializeActions(){
    actions.put("pause", () -> {
      pauseGame();
      sidePanel.setPauseButtonText("Unpause");
    });
    actions.put("unpause", () -> {
      unpauseGame();
      sidePanel.setPauseButtonText("Pause");
    });
    actions.put("save", this::saveGame);
    actions.put("load", () -> loadGame(Paths.savesDir, false));
    actions.put("help", () -> showHelp(MenuPanel.HELP));
    actions.put("exit", () -> exitGame(false));
    actions.put("toggleMenu", () -> {
      sidePanel.togglePanel();
      controller.setRecorderMode(true);
      GameDialogs.hideAll(); //# 1
      stopGame();
    });
    actions.put("step", () -> recorder.nextStep());
    actions.put("back", () -> recorder.previousStep());
    actions.put("autoReplay", () -> recorder.autoReplay());
    actions.put("loadRecording", () -> recorder.loadRecording());
    actions.put("saveRecording", () -> recorder.saveRecording());
    actions.put("helpRecorder", () -> showHelp(RecorderPanel.HELP));
    actions.put("toggleRecorder", () -> {
      sidePanel.togglePanel();
      controller.setRecorderMode(false);
      unpauseGame();
    });
  }

  /**
   * Handle the action command from the buttons in the UI
   * @param actionCommand
   */
  private void handleAction(String actionCommand) {
    actions.getOrDefault(actionCommand, () -> {
      throw new IllegalArgumentException("Unknown action command: " + actionCommand);
    }).execute();
  }

  /**
   * Method to handle the change in the slider value in the recorder panel
   * @param value the value of the slider
   */
  private void handleSliderChange(int value){
    recorder.setPlaybackSpeed(value);
  }

  /**
   * Initialize the action bindings (Map of actions to Runnable) for the controller
   */
  private void initializeActionBindings() {
    actionBindings.put("exitWithoutSaving", () -> exitGame(false));
    actionBindings.put("exitAndSave", () -> exitGame(true));
    actionBindings.put("resumeSavedGame", () -> loadGame(Paths.savesDir,false));
    actionBindings.put("startNewGame1", () -> loadGame(Paths.level1, true));
    actionBindings.put("startNewGame2", () -> loadGame(Paths.level1, true));/////////////////////// need to change to level2
    actionBindings.put("pause", this::pauseGame);
    actionBindings.put("unpause", this::unpauseGame);
  }

  /**
   * Initialize the controller for the game
   */
  private void initializeController() {
    controller = new Controller(model, actionBindings, MAX_TIME); //initialize with level 1
    addKeyListener(controller);
    setFocusable(true);//could be remove??
  }

  /**
   * Initialize the game timer to count down the time left in the game.
   */
  private void initializeGameTimer() {
      gameTimer = new Timer(1000, e -> {
          timeLeft--;
          controller.updatetime(timeLeft);
          gameInfoPanel.setTime(timeLeft);
          if (timeLeft == 0) {
              gameOver();
          }
      });
      gameTimer.stop();
  }

  /**
   * Stop the game without showing any dialog.
   */
  private void stopGame(){
    state = AppState.PAUSED;
    controller.pause(true);
    // renderer.setFocusable(false); i probably want it
    gameTimer.stop();
  }

  /**
   * Pause the game and show the pause dialog.
   */
  private void pauseGame() {
    if (state != AppState.PLAY) return;
    stopGame();
    GameDialogs.PAUSE.show();
  }

  /**
   * Depending on the current state, either start a new game or unpause the game.
   */
  private void unpauseGame() {
    boolean unpause = switch (state) {
      case PLAY -> false; // Already playing
      case PAUSED -> true;
      case GAMEOVER -> {
      checkModel(LoadFile.loadLevel("level" + currentLevel));
      yield true;
      }
      case VICTORY -> {
        currentLevel = 1; // reset level to 1///////////////////this could go
        checkModel(LoadFile.loadLevel(Paths.level1));
        yield true;
      }
    };
    if(!unpause) return;
    gameRun();
  }

  /**
   * unpause the game and start the timer
   */
  private void gameRun(){
    GameDialogs.hideAll(); // a bit wasteful to hide all dialogs, but I chose safety and compact code here
    state = AppState.PLAY;
    controller.pause(false);
    renderer.setFocusable(true);
    renderer.requestFocus();
    assert !gameTimer.isRunning(): "Game is already running";
    gameTimer.start();
  }

  /**
   * Show help dialog with given text
   * @param text
   */
  private void showHelp(String text) {
      JOptionPane.showMessageDialog(this, text, "Help", JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Game over, show game over dialog and pause the game.
   */
  public void gameOver(){
    state = AppState.GAMEOVER;
    gameTimer.stop();
    GameDialogs.GAMEOVER.show();
    controller.pause(true);
  }


////////////////////////////////////////////////////////////////////////////////////////////
  /**
   * Save the game to a file in the saves directory.
   */
  private void saveGame() {
    model.setTime(timeLeft);

    /////set level too??///////////////////////////

    JFileChooser fileChooser = ComponentFactory.customFileChooser(Paths.savesDir, "Save Game", "JSON files");
    int userSelection = fileChooser.showSaveDialog(this); // show dialog and wait user input
    if (userSelection == JFileChooser.APPROVE_OPTION) { // if user picked a file
      File fileToSave = fileChooser.getSelectedFile();
      String filename = fileToSave.getName(); // i should pass file
      boolean success = SaveFile.saveGame(filename, model); // do i need to pass model or gamestate? 2/10
      if (success) {
        JOptionPane.showMessageDialog(this, "Game Saved", "Save", JOptionPane.INFORMATION_MESSAGE);
      } else {
        handleFileError("Failed to save game", "Save Error", 
      new String[]{"Save Game", "start level 1", "quit"}, "Save Game",
      this::saveGame);
      }
    }
  }

  /**
   * Load a file from the given directory.
   * Paths class in nz.ac.wgtn.swen225.lc.persistency.Paths contains static final Files for desired directories.
   * @param dir it should use File from Paths class in nz.ac.wgtn.swen225.lc.persistency.Paths
   */
  private Optional<GameStateController> loadFile(File dir) { // Added File parameter
    JFileChooser fileChooser = ComponentFactory.customFileChooser(dir, "Load Game", "JSON Game files");
    int picked = fileChooser.showOpenDialog(this);
    if (picked == JFileChooser.APPROVE_OPTION) { // if user picked a file
      File file = fileChooser.getSelectedFile();
      return LoadFile.loadSave(file);
    }
    System.err.println("this is Optional.empty() from load file");
    return Optional.empty();
  }

  /**
   * Load a game from the given directory.
   * If quickLoad is true, load the file without showing the file chooser dialog.
   * @param dir it should use File from Paths class in nz.ac.wgtn.swen225.lc.persistency.Paths
   * @param quickLoad whether to load the file without showing the file chooser dialog
   */
  private void loadGame(File dir, boolean quickLoad) {
    stopGame();
    if (quickLoad) {
      checkModel(LoadFile.loadLevel(dir));
    } else {
      checkModel(loadFile(dir));
    }
    gameRun();
  }

  /**
   * Check presence of model, if present set the level, if not show error dialog.
   * @param opm Optional of GameStateController
   */
  private void checkModel(Optional<GameStateController> opm) { // may need variant for save
    opm.ifPresentOrElse(this::setLevel, ()->{
      handleFileError("Failed to load game", "Load Error", 
      new String[]{"Chose different file", "start level 1", "quit"}, "Chose different file",
      () -> loadGame(Paths.levelsDir, false));
    });
  }

  /**
   * Load the next level of the game.
   * If the next level is not found, the game is won.
   */
  private void loadNextLevel() {
    int nextlevel = currentLevel++;
    if (nextlevel > MAX_LEVEL) {
      state = AppState.VICTORY;
      GameDialogs.VICTORY.show();
      return;
    }
    // This still works. I've converted everything else to Files, but not sure how to convert this atm -AdamT
    stopGame();
    checkModel(LoadFile.loadLevel("level" + nextlevel));
    gameRun();
  }

  /**
   * Handling load error. the choices are:
   * 0: try again loadFile or saveGame
   * 1: start level 1
   * 2: quit game
   * @param message error message
   * @param title error title
   * @param options options for user
   * @param defult default option
   * @param action action to be taken for the first option
   */
  private void handleFileError(String message, String title, String[] options, String defult, Runnable action){
    int choice = JOptionPane.showOptionDialog(this, message, title,
      JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, options, defult);
      switch(choice){
        case 0 -> action.run();
        case 1 -> {
          checkModel(LoadFile.loadLevel(Paths.level1)); // it should loop unless model is set
          GameDialogs.hideAll();
        }
        case 2 -> exitGame(false);
      }
  }
///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Exit the game.
   * @param save whether to save the game before exiting
   */
  private void exitGame(boolean save) {
    if (save) {
      saveGame();////////////////////probably need to directly call special slot
      continueGame = true;
    } else {
      continueGame = false;
    }
    closePhase.run();
    gameTimer.stop();
    dispose(); // release resources
    System.exit(0);
  }

  /**
   * Set the model of the game, and all the necessary components for the game to run.
   * Once timer starts, the game will run.
   * @param level model of the game (GameStateController)
   */
  void setLevel(GameStateController level){
    model = level;
    timeLeft = model.getTime();
    treasuresLeft = model.getTotalTreasures();
    System.out.println("total treasures: " + treasuresLeft);/////////////////
    keysCollectednum = model.getKeysCollected().size();
    keysCollected.clear();
    keysCollected.addAll(model.getKeysCollected().values());
    gameInfoPanel.setKeys(keysCollectednum);
    gameInfoPanel.setTreasures(treasuresLeft);

    GameState gamestate = model.getGameState();
    gamestate.setAppNotifier(notifier);
    controller = new Controller(model, actionBindings, timeLeft);

    recorder = new Recorder((rc)-> {
      timeLeft = rc.updatedTime();
      model = rc.updatedGame();
      gameInfoPanel.setTime(timeLeft);
      initializeGameTimer();
      controller.setGameStateController(model);
      renderer.gameConsumer(model.getGameState());
      updateGameInfo(model);
    });
    controller.setRecorder(recorder);

    renderer.gameConsumer(gamestate);
    renderer.addKeyListener(controller);
    renderer.setFocusable(true);
    Timer timer= new Timer(34, unused->{
      assert SwingUtilities.isEventDispatchThread();
      if (state == AppState.PLAY) {
        //model.moveActor();
        //recorder.ping();
        renderer.updateCanvas();
      }
    });
    closePhase.run();//close phase before adding any element of the new phase
    closePhase = ()->{ timer.stop();};
    renderer.requestFocus();
    timer.start();
  }

  /**
   * Update the game info panel with the current keys and treasures left.
   * Used by recorder to update the game info.
   * @param level model of the game (GameStateController)
   */
  private void updateGameInfo(GameStateController level) {
    keysCollectednum = level.getKeysCollected().size();
    treasuresLeft = level.getTotalTreasures() - level.getTreasuresCollected();
    gameInfoPanel.setKeys(keysCollectednum);
    gameInfoPanel.setTreasures(treasuresLeft);
  }

  /**
   * Get the AppNotifier for the game.
   * @return AppNotifier
   */
  private AppNotifier getAppNotifier(){
    return new AppNotifier(){
      public void onGameWin(){
        stopGame();
        loadNextLevel();
      }
      public void onGameLose(){
        gameOver();
        System.out.println("Game Over is called");
      }
      public void onKeyPickup(int keyCount){
        assert keyCount >= 0: "keyCount is negative";
        keysCollectednum = keyCount;
        gameInfoPanel.setKeys(keysCollectednum);
      }
      public void onTreasurePickup(int treasureCount){
        assert treasureCount >= 0: "treasureCount is negative";
        treasuresLeft = treasureCount;
        gameInfoPanel.setTreasures(treasuresLeft);
      }
    };
  }

}

/**
 * better if we let the player to pick up where they left off
 *
 * recorder = new Recorder((rc)-> {
        
      timeLeft = rc.updatedTime();
      model = rc.updatedGame();
      gameInfoPanel.setTime(timeLeft);
      initializeGameTimer();
      controller.setGameStateController(model);
      renderer.gameConsumer(model.getGameState());
      
    });

 */