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
import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.domain.GameStateControllerInterface;
import nz.ac.wgtn.swen225.lc.domain.Maze;
import nz.ac.wgtn.swen225.lc.persistency.LoadFile;
import nz.ac.wgtn.swen225.lc.persistency.SaveFile;
import nz.ac.wgtn.swen225.lc.renderer.Renderer;



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

  /**
  private Recorder recorder;*/
  public enum AppState {PLAY, PAUSED, NEWGAME, GAMEOVER, VICTORY, RECORDING}
  private AppState state = AppState.NEWGAME;

  private GameStateController model;
  private int width = 800;
  private int height = 400;



//////////////////////////////


  App(){
    setTitle("Larry Croft's Adventures");//or something else


    assert SwingUtilities.isEventDispatchThread();
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


    initializeUI();
    initializeController();
    initializeGameTimer();

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
    startDialog = new PauseDialog(this, "Press Space to start", Color.BLUE, Color.YELLOW, 0.75);
    gameoverDialog = new PauseDialog(this, "Game Over\n Press Space to retry", Color.RED, Color.BLACK, 0.75);
    victoryDialog = new PauseDialog(this, "Victory\nPress Space to play again", Color.GREEN, Color.ORANGE, 0.75);
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


  private void handleRecorderAction(String actionCommand){
    switch(actionCommand){
     /** case "step" -> step();
      case "autoReplay" -> toggleAutoReplay();
      case "loadRecording" -> loadRecording();
      case "toggleRecording" -> toggleRecording();*/
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
    //do something with the value
    System.out.println("speed is :" + value);
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
          gameInfoPanel.setTime(timeLeft);
          if (timeLeft == 0) {
              gameTimer.stop();
              gameoverDialog.setVisible(true);
          }
      });
      gameTimer.start();
  }



  private void pauseGame() {
    if (state != AppState.PLAY) return;
    state = AppState.PAUSED;
    // renderer.setFocusable(false); i probably want it
    gameTimer.stop();
    pauseDialog.setVisible(true);
    startDialog.setVisible(false);// should optimize this
  }

  private void unpauseGame() {
    if (state == AppState.PLAY) return;
/**
 * i can use swich here too
 * but do it tomorrow my brain is dead.
 * 
  if (state == AppState.PLAY) return;
  switch(state){
    case AppState.PLAY -> return;
    case AppState.PAUSED -> state = AppState.PLAY;
    case AppState.NEWGAME -> loadNextLevel();
    case AppState.GAMEOVER -> loadLevel("level" + currentLevel);
    case AppState.VICTORY -> loadLevel("level" + 1);
    case AppState.RECORDING -> state = AppState.PLAY;//???
  }
 */



    state = AppState.PLAY;
    renderer.setFocusable(true);
    renderer.requestFocus();
    assert !gameTimer.isRunning(): "Game is already running";
    gameTimer.start();
    pauseDialog.setVisible(false);
    gameoverDialog.setVisible(false);// should optimize this
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
        JOptionPane.showMessageDialog(this, "Failed to save game", "Save Error", JOptionPane.ERROR_MESSAGE);
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
    } else {
      JOptionPane.showMessageDialog(this, "Failed to load game", "Load Error", JOptionPane.ERROR_MESSAGE);
    }

  }
  
  private void exitGameWithoutSaving() {
    gameTimer.stop();
    System.exit(0);
  }

  private void exitGameAndSave() {
    saveGame();
    gameTimer.stop();
    System.exit(0);
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
    renderer.addKeyListener(controller);//likely i need to make new controller each level as controller contains maze
    renderer.setFocusable(true);
    Timer timer= new Timer(34, unused->{
      assert SwingUtilities.isEventDispatchThread();

      if (state == AppState.PLAY) {
        new MockModel().ping();//p.model().ping();

        //model.ping() or update() or something

        /**
         * its not pretty solution
         * but i can take keys/ treasure info from the model and update the gameInfoPanel here
         */

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


/**
//for prototype, i can assume max level is 2 to simplify the process
private void loadNextLevel() {

int nextlevel = currentLevel++;
String levelName = "level" + nextlevel;
  Optional<GameStateControllerInterface> loadedGame = LoadFile.loadLevel(levelName);
  if (loadedGame.isPresent()) {
    model = (GameStateController)loadedGame.get();
    setLevel(model);
  } else {
    JOptionPane.showMessageDialog(this, "Failed to load game", "Load Error", JOptionPane.ERROR_MESSAGE);
    //or victory screen
  }
}


private void onLevelComplete() {
    loadNextLevel();
}

private void onGameOver() {
    showGameOverScreen();
}
//or????
private void onGameOver() {
  restartCurrentLevel();
}

private void restartCurrentLevel() {
  Level level = persistency.restartCurrentLevel(this::onLevelComplete, this::onGameOver);
  if (level == null) {
      // This should not happen, but just in case
      showGameOverScreen();
  } else {
      setPhase(level);
  }
}
*/
}