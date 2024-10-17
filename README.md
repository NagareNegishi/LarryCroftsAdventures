# Larry Croft's Adventures

## Getting Started

To start the game, run `nz.ac.wgtn.swen225.lc.app.Main` from Eclipse.

## How to Play

- At the start of the game, or when paused, game over, or victory is achieved, the game stops and a corresponding dialog shows up. Press Esc or the Press UI button "Pause" twice (first press makes it "Unpause") to start or resume the game.
- Use the arrow keys to move Chap around the maze.
- Chap cannot go through walls or locked doors.
- Collect keys to open corresponding doors.
- Collect all treasures to unlock the Exit.
- Complete each level before the time runs out.
- If time runs out, it's game over, and the game state is reset to the beginning of the current level

### Level 2 Special Elements

Level 2 introduces three new elements:
- **Water**: Touching water kills Chap.
- **Enemy**: Coming into contact with an enemy kills Chap.
- **Teleport Tiles**: Stepping on a teleport tile instantly moves Chap to the corresponding teleport tile elsewhere on the map.

## Controls

### Gameplay Controls

- **Arrow Keys**: Move Chap (Up, Down, Left, Right)
- **Space**: Pause the game
- **Esc**: Unpause the game

### Special Commands

- **Ctrl + X**: Exit without saving
- **Ctrl + S**: Save and Exit
- **Ctrl + R**: Resume saved game
- **Ctrl + 1**: Start new game (Level 1)
- **Ctrl + 2**: Start new game (Level 2)

## User Interface (left side panel)

User Interface can be toggled between two modes:

### Game Mode

- **Pause/Unpause**: Temporarily halt or resume the game
- **Save**: Store your current progress
- **Load**: Retrieve a previously saved game
- **Help**: Display game instructions and tips
- **Exit**: Quit the game without save
- **Show Recorder**: Open the game recording interface

### Recorder Interface

- **Step Forward/Backward**: Move forward or backward one step at a time
- **Auto Play**: Automatically play through the recorded game. In this mode, all other UI elements except speed slider are disabled. This button can be toggled on/off
- **Speed**: Adjust the playback speed of the recording for Auto Play
- **Load Recording**: Open a previously saved game recording
- **Save Recording**: Store the current game session as a recording
- **Help**: View information about the recorder features
- **Show Menu**: Return to the main game menu

