package nz.ac.wgtn.swen225.lc.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel; // task 2
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.domain.Maze;
import nz.ac.wgtn.swen225.lc.persistency.LoadFile;
import nz.ac.wgtn.swen225.lc.persistency.SaveFile;
import nz.ac.wgtn.swen225.lc.recorder.Recorder;
import nz.ac.wgtn.swen225.lc.renderer.Renderer;


/**
 * Main class for the game application.
 */
class App extends JFrame{
  private static final long serialVersionUID= 1L;

  private GameInfoPanel gameInfoPanel;
  private JPanel sidePanel; // switch menu/ recorder panel
  private MenuPanel menuPanel;
  private RecorderPanel recorderPanel; // placeholder for "recorder UI"
  private PauseDialog pauseDialog;
  private PauseDialog startDialog;
  private PauseDialog gameoverDialog;
  private PauseDialog victoryDialog;

  private Timer gameTimer;
  private int timeLeft = 60; // 1 minute for level 1??
  private int currentLevel = 0;
  private int keysCollected = 0; //or List<Key> keysCollected or items??? but in that case i shouldnt involeve the process
  private int treasuresLeft = 10; // Example value


  private JPanel renderer;


  Runnable closePhase= ()->{};
  //Phase currentPhase;
  private Controller controller;

  
  private Recorder recorder;
  public enum AppState {PLAY, PAUSED, NEWGAME, GAMEOVER, VICTORY, BETWEEN, RECORDING}
  private AppState state = AppState.NEWGAME;

  private GameStateController model;
  private static int width = 800;
  private static int height = 400;

  private static final int MAX_LEVEL = 2; //its not pretty... but i need to check loadNextLevel failure




  App(){
    setTitle("Larry Croft's Adventures");//or something else


    assert SwingUtilities.isEventDispatchThread();
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


    initializeUI();
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

  



  private void initializeUI() {

    // game info
    gameInfoPanel = new GameInfoPanel(width/8, height);
    gameInfoPanel.setPreferredSize(new Dimension(width/8, height));
    add(gameInfoPanel, BorderLayout.EAST);

    // Side panel for menu/recorder UI
    sidePanel = new JPanel(new BorderLayout());
    sidePanel.setPreferredSize(new Dimension(width/8, height));
    add(sidePanel, BorderLayout.WEST);

/**
 * note:
 * Jbutton need action listener
 * action listener is functional interface
 * Im implementing public void actionPerformed(ActionEvent e) here
 * in MenuPanel, i set action command for each button/ action event
 * so i can use getActionCommand() to get the action command
 * action command is string discribe the action
 * so i can use switch case to handle the action
 */
    menuPanel = new MenuPanel(e -> handleMenuAction(e.getActionCommand()));
    sidePanel.add(menuPanel);

    recorderPanel = new RecorderPanel(e -> handleRecorderAction(e.getActionCommand())
    ,slider -> handleSliderChange(slider));



    // Center panel for game rendering
    renderer = new Renderer();
    add(renderer, BorderLayout.CENTER);


    pauseDialog = new PauseDialog(this,"Game is paused", Color.BLACK, new Color(150, 150, 0), 0.75);
    startDialog = new PauseDialog(this, "Press Escape to start", Color.BLUE, Color.YELLOW, 0.75);
    gameoverDialog = new PauseDialog(this, "Game Over\n Press Escape to retry", Color.RED, Color.BLACK, 0.75);
    victoryDialog = new PauseDialog(this, "Victory\nPress Escape to play again", Color.GREEN, Color.ORANGE, 0.75);
    startDialog.setVisible(true);

}

/**
 * Handle menu actions
 * @param actionCommand
 */
  private void handleMenuAction(String actionCommand) {
    switch(actionCommand){
      case "pause" -> {
        pauseGame();
        menuPanel.setPauseButton("Unpause");
      }
      case "unpause" -> {
        unpauseGame();
        menuPanel.setPauseButton("Pause");
      }
      case "save" -> saveGame();
      case "load" -> loadFile();
      case "help" -> showHelp();
      case "exit" -> System.exit(0);// need proper method later
      case "toggle" -> toggleSidePanel();
    }
    assert false: "Unknown action command: " + actionCommand;
  }


  /**
   * connect to recorder method when recorder is implemented
   */
  private void handleRecorderAction(String actionCommand){
    switch(actionCommand){
      case "step" -> recorder.nextStep();
      //case "autoReplay" -> toggleAutoReplay();
      case "loadRecording" -> recorder.loadRecording();
      case "saveRecording" -> recorder.saveRecording();
      case "toggle" -> toggleSidePanel();
    }
    assert false: "Unknown action command: " + actionCommand;
  }

  /**
   * for slider
   * i want to take the int value of the slider
   * and i want to pass it to app
   * so what is this?
   * runnables are not needed?.....oh, its consumer???
   */
  private void handleSliderChange(int value){
    recorder.setPlaybackSpeed(value); //it will complite after recorder is updated
  }


  private void toggleSidePanel() {
    if (sidePanel.getComponent(0) == menuPanel) {
      sidePanel.remove(menuPanel);
      sidePanel.add(recorderPanel);
    } else {
      sidePanel.remove(recorderPanel);
      sidePanel.add(menuPanel);
    }
    sidePanel.revalidate();
    sidePanel.repaint();
  }


  /**
   * Initialize the controller for the game
   * make map of key bindings and pass it to the controller
   */
  private void initializeController(){
    Map<String, Runnable> actionBindings =  new HashMap<>();
    actionBindings.put("exitWithoutSaving", this::exitGameWithoutSaving);
    actionBindings.put("exitAndSave", this::exitGameAndSave);
    actionBindings.put("resumeSavedGame", this::loadGame);
    actionBindings.put("startNewGame1", () -> LoadFile.loadLevel("level1"));
    actionBindings.put("startNewGame2", () -> LoadFile.loadLevel("level2"));
    actionBindings.put("pause", this::pauseGame);
    actionBindings.put("unpause", this::unpauseGame);


    controller = new Controller(new Chap(2,2), new Maze(5,5), actionBindings);//temp maze and chap
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
              gameTimer.stop();
              gameoverDialog.setVisible(true);
          }
      });
      gameTimer.stop();
  }

  /**
   * I want to pass it to loader
   * for final submission
   * so loader can pluge those method to the model
   * either file of runable or abstract method
   * field may not work since 2 of method takes int....
   */
  private AppNotifier getAppNotifier(){
    return new AppNotifier(){
      public void onGameWin(){
        state = AppState.BETWEEN;
        gameTimer.stop();
        loadNextLevel();
      }
      public void onGameLose(){
        state = AppState.GAMEOVER;
        gameTimer.stop();
        gameoverDialog.setVisible(true);
      }
      public void onKeyPickup(int keyCount){
        keysCollected = keyCount;
        gameInfoPanel.setKeys(keysCollected);
      }
      public void onTreasurePickup(int treasureCount){
        treasuresLeft = treasureCount;
        gameInfoPanel.setTreasures(treasuresLeft);
      }
    };
  }



  private void pauseGame() {
    if (state != AppState.PLAY) return;
    state = AppState.PAUSED;
    // renderer.setFocusable(false); i probably want it
    gameTimer.stop();
    pauseDialog.setVisible(true);
  }

  private void unpauseGame() {
    boolean unpause = switch (state) {
      case PLAY -> false; // Already playing
      case PAUSED -> true;
      case NEWGAME -> {setfakeLevel(); startDialog.setVisible(false);  yield true; }//{ loadNextLevel(); startDialog.setVisible(false); yield true; } !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
      case GAMEOVER -> { LoadFile.loadLevel("level" + currentLevel); yield true; }
      case VICTORY -> { LoadFile.loadLevel("level1"); yield true; }
      case RECORDING, BETWEEN -> false; // need to think about this
    };

    if(!unpause) return;

    state = AppState.PLAY;
    renderer.setFocusable(true);
    renderer.requestFocus();
    assert !gameTimer.isRunning(): "Game is already running";
    
    pauseDialog.setVisible(false);
    gameTimer.start();
  }


  


  private void showHelp() {
      JOptionPane.showMessageDialog(this, "Help:\n" +
              "Use the arrow keys to move the hero.\n" +
              "Collect all treasures to complete the level.\n" +
              "Collect keys to unlock doors.\n" +
              "Avoid enemies.\n" +
              "Press Ctrl + X to exit without saving.\n" +
              "Press Ctrl + S to save the game.\n" +
              "Press Ctrl + R to resume a saved game.\n" +
              "Press Ctrl + 1 to start a new game at level 1.\n" +
              "Press Ctrl + 2 to start a new game at level 2.\n" +
              "Press Space to pause the game.\n" +
              "Press Esc to resume the game.\n", "Help", JOptionPane.INFORMATION_MESSAGE);
  }

  private void saveGame() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save Game");
    fileChooser.setFileFilter(new FileNameExtensionFilter("JSON files", "json"));

    int userSelection = fileChooser.showSaveDialog(this); // show dialog and wait user input
    if (userSelection == JFileChooser.APPROVE_OPTION) { // if user picked a file
      File fileToSave = fileChooser.getSelectedFile();

      String filename = fileToSave.getName(); // i should pass file
      boolean success = SaveFile.saveGame(filename, model);
      if (success) {
        JOptionPane.showMessageDialog(this, "Game Saved", "Save", JOptionPane.INFORMATION_MESSAGE);
      } else {
        handleFileError("Failed to save game", "Save Error", 
      new String[]{"Save Game", "start level 1", "quit"}, "Save Game",
      this::saveGame);
      }
    }
  }

  private Optional<GameStateController> loadFile() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Load Game");
    FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON Game files", "json");//expecting json file
    fileChooser.setFileFilter(filter); // set filter
    int picked = fileChooser.showOpenDialog(this);
    if (picked == JFileChooser.APPROVE_OPTION) { // if user picked a file
      File file = fileChooser.getSelectedFile();

      String filename = file.getName(); // i should pass file
      return LoadFile.loadSave(filename);
    }
    return Optional.empty();
  }


  private void loadGame(){//(int level, Runnable onWin, Runnable onLose) {
    Optional<GameStateController> loadedGame = loadFile();
    if (loadedGame.isPresent()) {
      model = loadedGame.get();
      setLevel(model);
      state = AppState.PLAY;
    } else {
      handleFileError("Failed to load game", "Load Error", 
      new String[]{"Chose different file", "start level 1", "quit"}, "Chose different file",
      this::loadGame);
    }
  }


  private void handleFileError(String message, String title, String[] options, String defult, Runnable action){
    int choice = JOptionPane.showOptionDialog(this, message, title,
      JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null,
      options, defult);
      switch(choice){
        case 0 -> action.run();
        case 1 -> LoadFile.loadLevel("level1");
        case 2 -> exitGameWithoutSaving();
      }
  }
  

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


/**
 *  Still need to address Runnable next, Runnable first
 */

  void setLevel(GameStateController level){


    /**
     * Phase was model + controller and model need rannable to be complete
     * app has controller, so if i can reuse it, just do so,
     * if not get chap from model and make new controller
     * pass it to the renderer
     * 
     * app shouldnt check the model,
     * but for the prototype, i can check win/lose condition here
     * to final submission, the model should be able to handle it
     */





    //set up the viewport and the timer
    //MockView v= new MockView();   // pass model to it (p.model());

    /**
     * i dont need to make new renderer each time
     * if renderer has setmethod to update the model(gamestatecontroller)
     * i can just pass the model to the renderer
     * so i dont need to risk breaking the jframe
     */



    model = level;

    /**
     * after the merge it should be
     * recorder = new Recorder((rc)-> {gameInfoPanel.setTime(rc.updateTime());
     *                                model = re.updatedGame();
     *                                });
     */


    recorder = new Recorder(model);
    /**
    * likely i need to make new controller or set it
    */
    renderer.addKeyListener(controller);//likely i need to make new controller each level as controller contains maze
    renderer.setFocusable(true);
    Timer timer= new Timer(34, unused->{
      assert SwingUtilities.isEventDispatchThread();

      if (state == AppState.PLAY) {

        // should work without it for level 1
        //new MockModel().ping();//p.model().ping();

        //model.ping() or update() or something

        /**
         * its not pretty solution
         * but i can take keys/ treasure info from the model and update the gameInfoPanel here
         */

        updateGameInfo(model); // this need to be gone
        
        renderer.repaint();
      }
    });
    closePhase.run();//close phase before adding any element of the new phase
    closePhase = ()->{ timer.stop();}; //remove(renderer); };
/**
 * I should be able to remove this part
 *  
    add(BorderLayout.CENTER, renderer);//add the new phase viewport
    setPreferredSize(getSize());//to keep the current size
    pack();                     //after pack
*/

    renderer.requestFocus();//need to be after pack
    timer.start();
  }

  //for prototype, i can assume max level is 2 to simplify the process
  private void loadNextLevel() {
    int nextlevel = currentLevel++;
    if (nextlevel > MAX_LEVEL) {
      state = AppState.VICTORY;
      victoryDialog.setVisible(true);
      return;
    }
    String levelName = "level" + nextlevel;
      Optional<GameStateController> loadedGame = LoadFile.loadLevel(levelName);
      if (loadedGame.isPresent()) {
        model = loadedGame.get();
        setLevel(model);
      } else {
        handleFileError("Failed to load game", "Load Error", 
        new String[]{"Chose different file", "start level 1", "quit"}, "Chose different file",
        this::loadGame);
      }
  }


  /**
   * this is soooo unpretty solution
   * definitely need to be changed
   */
  private void updateGameInfo(GameStateController level) {
    keysCollected = level.getKeysCollected().size();
    treasuresLeft = level.getTotalTreasures() - level.getTreasuresCollected();
    gameInfoPanel.setKeys(keysCollected);
    gameInfoPanel.setTreasures(treasuresLeft);
  }



  public void setfakeLevel(){
    System.out.println("fake level");
    Maze maze = Maze.createBasicMaze(10, 10);
    Chap chap = new Chap(2,2);
    GameState gameState = new GameState(maze, chap, 10);
    model = new GameStateController(maze, chap, gameState);
    recorder = new Recorder(model);
    controller.setChap(chap);
    controller.setMaze(maze);
    controller.setRecorder(recorder);
    /**
    * likely i need to make new controller or set it
    */
    renderer.addKeyListener(controller);//likely i need to make new controller each level as controller contains maze
    renderer.setFocusable(true);
    Timer timer= new Timer(200, unused->{
      assert SwingUtilities.isEventDispatchThread();
      if (state == AppState.PLAY) {
        updateGameInfo(model); // this need to be gone
        renderer.repaint();
        //System.out.println("game is running");
      }
    });
    closePhase.run();//close phase before adding any element of the new phase
    closePhase = ()->{ timer.stop();}; //remove(renderer); };

    renderer.requestFocus();//need to be after pack
    timer.start();
  }









}