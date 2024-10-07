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
import javax.swing.filechooser.FileNameExtensionFilter;

import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.persistency.LoadFile;
import nz.ac.wgtn.swen225.lc.persistency.Paths;
import nz.ac.wgtn.swen225.lc.persistency.SaveFile;
import nz.ac.wgtn.swen225.lc.recorder.Recorder;
import nz.ac.wgtn.swen225.lc.renderer.Renderer;

/**
 * Main class for the game application.
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
  private Set<String> keysCollected;
  private int treasuresLeft;

  Runnable closePhase= ()->{};
  private Map<String, Runnable> actionBindings =  new HashMap<>(); // need to be passed to controller
  private GameStateController model;
  private AppNotifier notifier = getAppNotifier(); // need to be passed to model
  private Controller controller;
  //private JPanel renderer;
  private Renderer renderer;
  private Recorder recorder;
  public enum AppState {PLAY, PAUSED, NEWGAME, GAMEOVER, VICTORY, BETWEEN, RECORDING}
  private AppState state = AppState.NEWGAME;

  private static int width = 800;
  private static int height = 400;
  private static final int MAX_LEVEL = 2; //its not pretty... but i need to check loadNextLevel failure


  App(){
    setTitle("Larry Croft's Adventures");
    assert SwingUtilities.isEventDispatchThread();
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    initializeModel();
    initializeUI();
    initializeActionBindings();
    initializeController();
    initializeGameTimer(); //must be after controller??

    setPreferredSize(new Dimension(width, height));
    pack();
    setVisible(true);
    addWindowListener(new WindowAdapter(){
      public void windowClosed(WindowEvent e){ 
        closePhase.run();
        gameTimer.stop();
      }
    });
  }

  /**
   * Initialize the model for the game by loading the first level.
   * You "can not" replace this method with checkModel(loadFile(Paths.level1))
   */
  private void initializeModel(){
    Optional<GameStateController> loadedGame = LoadFile.loadLevel(Paths.level1);
    if (loadedGame.isPresent()) {
      model = loadedGame.get();
    } else {
      handleFileError("Failed to load game", "Load Error", 
      new String[]{"Chose different file", "start level 1", "quit"}, "Chose different file",
      this::loadGame);
    }
  }

  private void initializeUI() {
    // game info
    gameInfoPanel = new GameInfoPanel(width/8, height);
    gameInfoPanel.setPreferredSize(new Dimension(width/8, height));
    add(gameInfoPanel, BorderLayout.EAST);
    // Side panel for menu/recorder UI
    sidePanel = new SidePanel(width/8, height, e -> handleMenuAction(e),
    e -> handleRecorderAction(e), slider -> handleSliderChange(slider));
    add(sidePanel, BorderLayout.WEST);
    // Center panel for game rendering
    renderer = new Renderer();
    add(renderer, BorderLayout.CENTER);
    GameDialogs.initializeDialogs(this);
    GameDialogs.START.show();
}

/**
 * Handle menu actions
 * @param actionCommand
 */
  private void handleMenuAction(String actionCommand) {
    switch(actionCommand){
      case "pause" -> {
        pauseGame();
        sidePanel.setPauseButtonText("Unpause");
      }
      case "unpause" -> {
        unpauseGame();
        sidePanel.setPauseButtonText("Pause");
      }
      case "save" -> saveGame();
      /**
       *  Currently never called. I would like different buttons for levels and saves
       *  but if we cannot, we could just use the single levels folder - AdamT
       * 
       * -> I believe loading from saved game is only requirement. need to be confirmed
       */
      case "load" -> {
        checkModel(loadFile(Paths.savesDir));
        gameRun();
      }
      case "help" -> showHelp(MenuPanel.HELP);
      case "exit" -> exitGameWithoutSaving();
      case "toggle" -> {
        sidePanel.togglePanel();
        pauseGame();
      }
    }
    assert false: "Unknown action command: " + actionCommand;
  }

  /**
   * connect to recorder method when recorder is implemented
   */
  private void handleRecorderAction(String actionCommand){
    switch(actionCommand){
      case "step" -> recorder.nextStep();
      case "back" -> recorder.previousStep();
      case "autoReplay" -> recorder.autoReplay();
      case "loadRecording" -> recorder.loadRecording();
      case "saveRecording" -> recorder.saveRecording();
      case "toggle" -> {
        sidePanel.togglePanel();
        unpauseGame();
      }
    }
    assert false: "Unknown action command: " + actionCommand;
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
    actionBindings.put("exitWithoutSaving", this::exitGameWithoutSaving);
    actionBindings.put("exitAndSave", this::exitGameAndSave);
    actionBindings.put("resumeSavedGame", this::loadGame);
    actionBindings.put("startNewGame1", () -> {
      //resetGame();
      currentLevel = 1;
      checkModel(LoadFile.loadLevel(Paths.level1));
      gameRun();
    });
    actionBindings.put("startNewGame2", () -> {
      //resetGame();
      currentLevel = 2;
      checkModel(LoadFile.loadLevel("level2"));
      gameRun();
    });
    actionBindings.put("pause", this::pauseGame);
    actionBindings.put("unpause", this::unpauseGame);
  }

  /**
   * Initialize the controller for the game
   */
  private void initializeController() {
    controller = new Controller(model, actionBindings); //initialize with level 1
    addKeyListener(controller);
    setFocusable(true);//could be remove??
  }

  //1000ms = 1s
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



  private AppNotifier getAppNotifier(){
    return new AppNotifier(){
      public void onGameWin(){
        state = AppState.BETWEEN;
        gameTimer.stop();
        loadNextLevel();
      }
      public void onGameLose(){
        gameOver();
        System.out.println("Game Over is called");
      }
      public void onKeyPickup(int keyCount){
        keysCollectednum = keyCount;
        gameInfoPanel.setKeys(keysCollectednum);
      }
      public void onTreasurePickup(int treasureCount){
        treasuresLeft = treasureCount;
        gameInfoPanel.setTreasures(treasuresLeft);
      }
    };
  }

  private void stopGame(){
    controller.pause(true);
    // renderer.setFocusable(false); i probably want it
    gameTimer.stop();
  }

  private void resetGame(){
    state = AppState.PAUSED;
    stopGame();
    timeLeft = MAX_TIME;
    keysCollectednum = 0;
    treasuresLeft = model.getTotalTreasures();
  }

  private void pauseGame() {
    if (state != AppState.PLAY) return;
    state = AppState.PAUSED;
    stopGame();
    GameDialogs.PAUSE.show();
  }

  private void unpauseGame() {
    boolean unpause = switch (state) {
      case PLAY -> false; // Already playing
      case PAUSED -> true;
      case NEWGAME -> {
        setLevel(model);
        yield true;}
      case GAMEOVER -> {
      timeLeft = MAX_TIME;
      checkModel(LoadFile.loadLevel("level" + currentLevel));
      yield true;
      }
      case VICTORY -> {
        currentLevel = 1; // reset level to 1
        checkModel(LoadFile.loadLevel(Paths.level1));
        yield true; }
      case RECORDING, BETWEEN -> false; // need to think about this
    };
    if(!unpause) return;
    gameRun();
  }

  private void gameRun(){
    GameDialogs.hideAll(); // a bit wasteful to hide all dialogs, but I chose safety and compact code here
    state = AppState.PLAY;
    controller.pause(false);
    renderer.setFocusable(true);
    renderer.requestFocus();
    assert !gameTimer.isRunning(): "Game is already running";
    gameTimer.start();
  }

  private void showHelp(String text) {
      JOptionPane.showMessageDialog(this, text, "Help", JOptionPane.INFORMATION_MESSAGE);
  }

  public void gameOver(){
    state = AppState.GAMEOVER;
    gameTimer.stop();
    GameDialogs.GAMEOVER.show();
    controller.pause(true);
  }


////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////Save and Load related methods here /////////////////////////////////////////////////
///////////////// saveGame() and loadFile() are useing JFileChooser to get file from user/////////////////////
///////////////// So probably Persistency want to modify that File to something it requires/////////////////

////other methods related to save and load are also in this section, but they are not directly related to JFileChooser

  private void saveGame() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save Game");
    fileChooser.setFileFilter(new FileNameExtensionFilter("JSON files", "json"));
    // opens into saves directory
    fileChooser.setCurrentDirectory(Paths.savesDir);
    
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
   * String-based methods expect filenames without the ".json" extension, as they automatically append it.
   * File-based methods expect complete filenames including the ".json" extension, as they use the File object as-is.
   * ->
   * Moving to use File based methods for all file operations.
   * Paths class in nz.ac.wgtn.swen225.lc.persistency.Paths contains static final Files for desired directories.
   *
   * @param dir it should use File from Paths class in nz.ac.wgtn.swen225.lc.persistency.Paths
   */
  private Optional<GameStateController> loadFile(File dir) { // Added File parameter
    JFileChooser fileChooser = new JFileChooser(dir);
    fileChooser.setDialogTitle("Load Game");
    FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON Game files", "json");//expecting json file
    fileChooser.setFileFilter(filter); // set filter
    fileChooser.setAcceptAllFileFilterUsed(false); // disable "All files" option

    int picked = fileChooser.showOpenDialog(this);
    if (picked == JFileChooser.APPROVE_OPTION) { // if user picked a file
      File file = fileChooser.getSelectedFile();
      return LoadFile.loadSave(file);
    }
    System.err.println("this is Optional.empty() from load file");
    return Optional.empty();
  }
  
  

  /**
   * currently simply Calling LoadFile()
   */
  private void loadGame(){//(int level, Runnable onWin, Runnable onLose) {
    checkModel(loadFile(Paths.levelsDir));
  }

  /**
   * Optional check here
   */
  private void checkModel(Optional<GameStateController> opm) { // may need variant for save
    opm.ifPresentOrElse(this::setLevel, ()->{
      handleFileError("Failed to load game", "Load Error", 
      new String[]{"Chose different file", "start level 1", "quit"}, "Chose different file",
      this::loadGame);
    });
  }

  //for prototype, i can assume max level is 2 to simplify the process
  /**
   * this should work fine, as LoadFile.loadLevel(String) is currently working
   */
  private void loadNextLevel() {
    int nextlevel = currentLevel++;
    if (nextlevel > MAX_LEVEL) {
      state = AppState.VICTORY;
      GameDialogs.VICTORY.show();
      return;
    }
    // This still works. I've converted everything else to Files, but not sure how to convert this atm -AdamT
    checkModel(LoadFile.loadLevel("level" + nextlevel));
  }

  /**
   * Handling load error
   * choice is:
   * 0: try again loadFile or saveGame
   * 1: start level 1
   * 2: quit game
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
        case 2 -> exitGameWithoutSaving();
      }
  }
///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////


  private void exitGameWithoutSaving() {
    closePhase.run();
    gameTimer.stop();
    dispose();
    System.exit(0);
  }

  private void exitGameAndSave() {
    saveGame();
    exitGameWithoutSaving();
  }



  void setLevel(GameStateController level){
    resetGame();

    model = level;

    treasuresLeft = model.getTotalTreasures();
    keysCollectednum = model.getKeysCollected().size();
    keysCollected = new HashSet<>(model.getKeysCollected().values());
    System.out.println(keysCollected);//////////////////////////////////////

    GameState gamestate = model.getGameState();
    gamestate.setAppNotifier(notifier);
    controller = new Controller(model, actionBindings);

    recorder = new Recorder((rc)-> { 
      gameInfoPanel.setTime(rc.updatedTime());
      model = rc.updatedGame();
      renderer.gameConsumer(model.getGameState());
    });
    controller.setRecorder(recorder);

/////////////////////////delete this part
    System.out.println("recorder is set in setLevel");
/////////////////////////////////

    renderer.gameConsumer(gamestate);
    renderer.addKeyListener(controller);
    renderer.setFocusable(true);
    Timer timer= new Timer(34, unused->{
      assert SwingUtilities.isEventDispatchThread();

      if (state == AppState.PLAY) {
        //need some sort of update method here for domain and recorder for level 2!!!!!!!!!!!!!!!!!
        //System.out.println(gamestate.chapPosition());
        /**
         * its not pretty solution, but i can take keys/ treasure info from the model and update the gameInfoPanel here
         */
        updateGameInfo(model); // this need to be gone
        //renderer.updateCanvas();
        renderer.repaint();
      }
    });
    closePhase.run();//close phase before adding any element of the new phase
    closePhase = ()->{ timer.stop();};
    renderer.requestFocus();
    timer.start();
  }


  /**
   * this is soooo unpretty solution definitely need to be changed
   */
  private void updateGameInfo(GameStateController level) {
    keysCollectednum = level.getKeysCollected().size();
    treasuresLeft = level.getTotalTreasures() - level.getTreasuresCollected();
    gameInfoPanel.setKeys(keysCollectednum);
    gameInfoPanel.setTreasures(treasuresLeft);
  }

}