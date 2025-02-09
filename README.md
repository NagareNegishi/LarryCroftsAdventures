# Larry Croft's Adventures

## Project Overview
Larry Croft's Adventures is a Java-based maze game developed using modern software design patterns and modular architecture. Originally created as part of a team project at Victoria University of Wellington, this game demonstrates the practical application of software engineering principles.

### My Role: App Module Lead Developer
As the developer responsible for the App module, I:
- Implemented core application architecture using multiple design patterns
- Developed the central communication system between all game modules
- Created a flexible UI component system using Factory and Strategy patterns
- Managed game state and user interactions

## Technical Implementation
### Design Patterns Used
- **Factory Method Pattern**: Implemented in `ComponentFactory` for UI components
- **Observer Pattern**: Used in `AppNotifier` for module communication
- **Command Pattern**: Applied for key bindings and game actions
- **Strategy Pattern**: Used for dynamic UI panel switching
- **Singleton Pattern**: Implemented in `GameDialogs` for dialog management

## How to Play

### Getting Started
To start the game, run `nz.ac.wgtn.swen225.lc.app.Main` from Eclipse.

### Game Controls
#### Gameplay Controls
- **Arrow Keys**: Move Chap (Up, Down, Left, Right)
- **Space**: Pause the game
- **Esc**: Unpause the game

#### Special Commands
- **Ctrl + X**: Exit without saving
- **Ctrl + S**: Save and Exit
- **Ctrl + R**: Resume saved game
- **Ctrl + 1**: Start new game (Level 1)
- **Ctrl + 2**: Start new game (Level 2)

### Game Mechanics
- Collect keys to open corresponding doors
- Gather all treasures to unlock the Exit
- Complete levels before time runs out
- Avoid obstacles and enemies

### Level 2 Special Elements
- **Water**: Touching water kills Chap
- **Enemy**: Contact with enemies is fatal
- **Teleport Tiles**: Transport Chap to corresponding locations

## User Interface
The game features two interface modes:

### Game Mode
- Pause/Unpause: Temporarily halt or resume the game
- Save: Store your current progress
- Load: Retrieve a previously saved game
- Help: Display game instructions and tips
- Exit: Quit the game without save
- Show Recorder: Open the game recording interface

### Recorder Interface
- Step Forward/Backward: Move forward or backward one step at a time
- Auto Play: Automatically play through the recorded game. In this mode, all other UI elements except speed slider are disabled. This button can be toggled on/off
- Speed: Adjust the playback speed of the recording for Auto Play
- Load Recording: Open a previously saved game recording
- Save Recording: Store the current game session as a recording
- Help: View information about the recorder features
- Show Menu: Return to the main game menu

## Technical Architecture
- **Modular Design**: Six distinct modules handling different aspects of the game
- **MVC Pattern**: Clear separation between game logic, visualization, and control
- **Event System**: Robust communication between modules
- **State Management**: Thread-safe game state handling

## Development Highlights
- Implementation of multiple design patterns for maintainable, extensible code
- Creation of a flexible UI component system
- Development of robust error handling and state management
- Integration of multiple modules into a cohesive application

## Setup and Installation
1. Clone the repository
2. Open project in Eclipse IDE
3. Run `nz.ac.wgtn.swen225.lc.app.Main`
4. Use the controls described above to play

## About the Project
This project was developed as part of a team software engineering course at Victoria University of Wellington (2024). Beyond the technical implementation, we utilized modern project management practices through GitLab's built-in tools:

### Project Management Approach
- **Issue Tracking**: Used GitLab issues for task management and bug tracking
- **Merge Requests**: Implemented code review practices through merge request workflows
- **CI/CD Pipeline**: Utilized automated testing and deployment pipelines
- **Sprint Planning**: Managed development cycles using GitLab milestones
- **Team Collaboration**: Coordinated six developers using GitLab's collaboration features

All commits, issues, and project management artifacts were originally managed in the university's GitLab instance, providing experience with industry-standard project management tools and practices.