package nz.ac.wgtn.swen225.lc.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout; // task 2
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel; // task 2
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.Maze;

class App extends JFrame{
  private static final long serialVersionUID= 1L;

  private JLabel timeLabel, levelLabel, keysLabel, treasuresLabel;
  private JPanel gamePanel; // placeholder for "render"
  private JPanel recorderPanel; // placeholder for "recorder UI"
  private JPanel DomainPanel; // placeholder for Jpanel i want to pass to the domain as interface keysLabel, treasuresLabel will be this
  private Timer gameTimer;
  private int timeLeft = 60; // 1 minute for level 1??
  private int currentLevel = 1;
  private int keysCollected = 0; //or List<Key> keysCollected or items??? but in that case i shouldnt involeve the process
  private int treasuresLeft = 10; // Example value
  //private int score = 0; // not requirement????

  private MenuPanel menuPanel;
  private PauseDialog pauseDialog;

  


  Runnable closePhase= ()->{};
  //Phase currentPhase;
  private Controller controller;

  /**private Game game;
  private Renderer renderer;
  private Recorder recorder;*/
  private boolean isPaused = false;



  App(){
    setTitle("Larry Croft's Adventures");//or something else


    assert SwingUtilities.isEventDispatchThread();
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


    initializeUI();
    initializeController();
    initializeGameTimer();

    setPreferredSize(new Dimension(800, 400));
    pack();
    setVisible(true);
    addWindowListener(new WindowAdapter(){
      public void windowClosed(WindowEvent e){ closePhase.run(); }
    });
  }

  



  private void initializeUI() {

    // Top panel for game info
    JPanel infoPanel = new JPanel(new GridLayout(1, 4)); //make new class for this
    timeLabel = new JLabel("Time: 60");
    levelLabel = new JLabel("Level: 1");
    keysLabel = new JLabel("Keys: 0"); // if images, i need to pass it to renderer
    treasuresLabel = new JLabel("Treasures left: 10"); // i need to pass it to domain
    infoPanel.add(timeLabel);
    infoPanel.add(levelLabel);
    infoPanel.add(keysLabel);
    infoPanel.add(treasuresLabel);
    add(infoPanel, BorderLayout.NORTH);


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
    //menuPanel.disableKeyStroke();
    add(menuPanel, BorderLayout.EAST); // temporary

    // Center panel for game rendering (placeholder)
    gamePanel = new JPanel();
    gamePanel.setBackground(Color.BLACK);
    add(gamePanel, BorderLayout.CENTER);

    pauseDialog = new PauseDialog(this);
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
      case "load" -> loadGame();
      case "help" -> showHelp();
      case "exit" -> System.exit(0);// need proper method later
    }
    assert false: "Unknown action command: " + actionCommand;
  }

  private void initializeController(){

    controller = new Controller(new Chap(2,2), new Maze(5,5), this::pauseGame, this::unpauseGame);//temp maze and chap
    addKeyListener(controller);
    setFocusable(true);//could be remove??
  }

  //1000ms = 1s
  private void initializeGameTimer() {
      gameTimer = new Timer(1000, e -> {
          timeLeft--;
          timeLabel.setText("Time: " + timeLeft);
      });
      gameTimer.start();
  }


  /**
   * maybe i need separate button?
   * key and button conbination messing conditions
   * 
   */
  private void pauseGame() {
    System.out.println("Game Paused");
    if (isPaused) return;
    isPaused = true;
    gameTimer.stop();
    pauseDialog.setVisible(true);
  }

  private void unpauseGame() {
    System.out.println("Game Unpaused");
    if (!isPaused) return;
    isPaused = false;
    assert !gameTimer.isRunning(): "Game is already running";
    gameTimer.start();
    pauseDialog.setVisible(false);
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
      //GameSaver.saveGame();
      JOptionPane.showMessageDialog(this, "Game Saved", "Save", JOptionPane.INFORMATION_MESSAGE);
  }

  private void loadGame() {
      //Game loadedGame = GameLoader.loadGame();
      //if (loadedGame != null) {
          //game = loadedGame;
          JOptionPane.showMessageDialog(this, "Game Loaded", "Load", JOptionPane.INFORMATION_MESSAGE);
      //}
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
 * likely getting model from persistency
 * and model should already have Runnable next, Runnable first
 * so this method may not be needed
 */
  private void NewPhase(){
    //setPhase(Phase.level1(()->phaseTwo(), ()->phaseZero(), keyBindings));
	setPhase(new MockPhase());
  }


/**
 * likely getting model from persistency
 * ->i can use it to make viewport (or renderer)
 * viewport should extend JPanel, so i set it up in the viewport class
 *
 */
  void setPhase(MockPhase p){
    //set up the viewport and the timer
    MockView v= new MockView();   // pass model to it (p.model());
    v.addKeyListener(controller);//or just controller? can i reuse? i guess depend on others code
    v.setFocusable(true);
    Timer timer= new Timer(34, unused->{
      assert SwingUtilities.isEventDispatchThread();

      if (!isPaused) { //isPaused may not be pretty solution..... should i use timer.running() instead??
        new MockModel().ping();//p.model().ping();
        v.repaint();
      }
    });
    closePhase.run();//close phase before adding any element of the new phase
    closePhase = ()->{ timer.stop(); remove(v); };
    add(BorderLayout.CENTER, v);//add the new phase viewport
    setPreferredSize(getSize());//to keep the current size
    pack();                     //after pack
    v.requestFocus();//need to be after pack
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