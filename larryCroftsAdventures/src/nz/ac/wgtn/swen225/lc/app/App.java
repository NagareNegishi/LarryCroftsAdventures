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

  private Timer gameTimer;
  private int timeLeft = 60; // 1 minute for level 1??
  private int currentLevel = 1;
  private int keysCollected = 0; //or List<Key> keysCollected or items??? but in that case i shouldnt involeve the process
  private int treasuresLeft = 10; // Example value


  private JPanel renderer;
  
  private JPanel DomainPanel; // placeholder for Jpanel i want to pass to the domain as interface keysLabel, treasuresLabel will be this
  //currently directly making the parameter from domain

  //private int score = 0; // not requirement????

  Runnable closePhase= ()->{};
  //Phase currentPhase;
  private Controller controller;

  /**private Game game;
  private Renderer renderer;
  private Recorder recorder;*/
  private boolean isPaused = false;

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

    // Top panel for game info
    gameInfoPanel = new GameInfoPanel(width/8, height);
    gameInfoPanel.setPreferredSize(new Dimension(width/8, height));
    add(gameInfoPanel, BorderLayout.EAST);


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
    ,slider -> handleSliderChange(slider));//yes! slider value change -. then call handleSliderChange i hope it works
    //recorderPanel.setVisible(false); // initially hidden


    // Center panel for game rendering (placeholder)
    renderer = new Renderer();
    //renderer.setPreferredSize(new Dimension(800, 400)); everythingelse is for the renderer
    add(renderer, BorderLayout.CENTER);

    pauseDialog = new PauseDialog(this,"Game is paused", Color.BLACK, new Color(150, 150, 0), 0.75);
    startDialog = new PauseDialog(this, "Press Space to start", Color.GREEN, Color.YELLOW, 0.75);
    gameoverDialog = new PauseDialog(this, "Game Over", Color.RED, Color.BLACK, 0.75);
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
   * so unlike i want to take the int value of the slider
   * and i want to pass it to the recorder panel
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
    if (isPaused) return;
    isPaused = true;
    // renderer.setFocusable(false); i probably want it
    gameTimer.stop();
    pauseDialog.setVisible(true);
    startDialog.setVisible(false);// should optimize this
  }

  private void unpauseGame() {
    if (!isPaused) return;
    isPaused = false;
    // renderer.setFocusable(true); i probably want it
    // renderer.requestFocus();
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

  private Optional<GameStateControllerInterface> loadFile() {
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
    Optional<GameStateControllerInterface> loadedGame = loadFile();
    if (loadedGame.isPresent()) {
      model = (GameStateController)loadedGame.get();
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
    private void saveGame() {
      GameSaver.saveGame();// i want to pass 2 runnable and int for level
      JOptionPane.showMessageDialog(this, "Game Saved", "Save", JOptionPane.INFORMATION_MESSAGE);
  }

  private void loadGame() {
  // i think i need to pop up file chooser
  // and get json file
  // then pass it to persistency

  JsonFile jsonFile = new JsonFile();?????
//do i need to check it here? or in persistency??
      Game loadedGame = GameLoader.loadGame(jsonFile);
      if (loadedGame != null) {
          game = loadedGame;
          renderer.updateGame(game);
          updateInfoLabels();
          JOptionPane.showMessageDialog(this, "Game Loaded", "Load", JOptionPane.INFORMATION_MESSAGE);
      }
  }*/



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
    renderer.addKeyListener(controller);//or just controller? can i reuse? i guess depend on others code
    renderer.setFocusable(true);
    Timer timer= new Timer(34, unused->{
      assert SwingUtilities.isEventDispatchThread();

      if (!isPaused) { //isPaused may not be pretty solution..... should i use timer.running() instead??
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
  private void startGame() {
    loadNextLevel();
}

private void loadNextLevel() {
    Level level = persistency.load(this::onLevelComplete, this::onGameOver);
    if (Level == null) {
        showVictoryScreen();
    } else {
        setPhase(level);
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


  /**
   * We dont need start menu for this project but I will keep it for future reference



  private void phaseZero() {
    var panel = new JPanel(new GridLayout(0, 2));
    var welcome= new JLabel("     Welcome to Compact. A compact Java game!");
    var start= new JButton("     Start!");
    panel.add(new JLabel("     Customize your controller (press keys to assign):")); // taks 2
    panel.add(new JLabel()); // task 2 adjust the grid layout
    closePhase.run();
    closePhase = ()->{ getContentPane().removeAll(); }; // task 2 remove all addtional components
    add(BorderLayout.NORTH, welcome);
    add(BorderLayout.SOUTH, start);

    // task 2 add the key bindings to the panel
    for (String action : actions) {
        panel.add(new JLabel("       " + action + ":")); // trying to make it prettier but "compact"
        JTextField keyField = new JTextField(KeyEvent.getKeyText(keyBindings.get(action)));
        keyField.setEditable(false); // only accept key press, not text input
        keyField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if ((keyCode >= KeyEvent.VK_NUMPAD0 && keyCode <= KeyEvent.VK_NUMPAD9) ||
                    (keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z)) {
                    keyBindings.put(action, keyCode);
                    keyField.setText(KeyEvent.getKeyText(keyCode));
                }
            }
        });
        panel.add(keyField);
    }

    add(BorderLayout.CENTER, panel);
    start.addActionListener(e -> phaseOne());
    setPreferredSize(new Dimension(800, 400));
    pack();
  }*/



  /**
  I believe we dont need this for this project but I will keep it for future reference
  
  private void victory(){
    closePhase.run();
    closePhase = ()->{}; // could be removed but I think good practice to keep it
    add(BorderLayout.CENTER, new JLabel("     Victory!!"));
    pack();
  }

  private void phaseOne(){
    setPhase(Phase.level1(()->phaseTwo(), ()->phaseZero(), keyBindings));
  }

  private void phaseTwo(){
    setPhase(Phase.level2(()->victory(), ()->phaseZero(), keyBindings));
  }*/
















}