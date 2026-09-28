# Traffic Simulator

A Java implementation of a traffic simulator with both console and graphical user interfaces. The project models vehicles, roads, junctions, pollution, and weather conditions, processing events from JSON files. It follows the Model-View-Controller (MVC) pattern and uses multithreading to keep the GUI responsive during simulation.

>As this is a **discrete-event simulator**, the system advances in fixed steps (ticks), resolving interactions between vehicles and infrastructure according to predefined rules.

### GUI Preview
*Example of a running simulation. The GUI displays vehicles moving through a road network linked by a junction.*
<p align="center">
<img width="1917" height="1018" alt="Traffic Simulator GUI running a simulation" src="https://github.com/user-attachments/assets/a7b0e612-9c6d-40ff-89f2-a3865c3ed4f4" /> 
</p>


### Context
This project was originally developed for *Programming Technology II* subject at UCM. While this documentation is in English, the internal code comments are in **Spanish**.

### Key Features

#### 1. Simulation Objects
The application manages three fundamental types of simulation objects:
* **Vehicles:** Single `Vehicle` class with an itinerary (list of junctions), maximum speed, current speed, status (`PENDING`, `TRAVELING`, `WAITING`, `ARRIVED`), current road and location, contamination class (0–10), total CO₂ emitted, and total distance traveled.
* **Roads:** Base `Road` class with two subclasses: `InterCityRoad` and `CityRoad`. They manage speed limits, weather conditions, total contamination, and the list of vehicles currently circulating.
* **Junctions:** Regulate traffic flow using `LightSwitchingStrategy` (e.g., `RoundRobinStrategy`, `MostCrowdedStrategy`) and `DequeuingStrategy` (e.g., `MoveFirstStrategy`, `MoveAllStrategy`). Each junction maintains incoming road queues and controls which road has a green light.

#### 2. Object-Oriented Design & Design Patterns
* **Inheritance and Polymorphism:** A clear class hierarchy allows different road types and strategies to share common interfaces while overriding specific behaviors.
* **Design Patterns:** The implementation leverages several design patterns:
  * **Strategy** for light switching and dequeuing policies.
  * **Factory / Builder** for creating events and strategies from JSON.
  * **Observer** for the MVC architecture (model notifies views).
* **MVC Architecture:** The graphical interface is separated from the simulation engine, allowing the same core logic to be driven from either a console mode or a Swing-based GUI.

#### 3. Simulation Engine
* **Discrete Steps (Ticks):** The simulation advances in steps. Each tick processes pending events, advances junctions, then advances roads.
* **Configurable Inputs:** Simulations are defined by JSON files containing a list of events (new junctions, roads, vehicles, weather changes, contamination class changes).
* **Console and GUI Modes:** Run a single simulation with console output, or launch the interactive Swing interface.

#### 4. Graphical User Interface & Multithreading
* **Main Window:** Includes a control panel, status bar, tables (Events, Vehicles, Roads, Junctions), and two map views (`MapComponent` and `MapByRoadComponent`).
* **Control Panel:** Buttons to load events, change CO₂ class, change weather, run/stop simulation, and exit. A spinner sets the number of ticks and a delay between steps.
* **Multithreading:** The simulation runs in a separate thread, allowing the GUI to remain responsive.

### Tech Stack
* **Language:** Java
* **Libraries:** Java Swing (GUI), JSON library (provided in `lib`).
* **Input/Output:** JSON files for simulation input and output.

### How to Run
1. Clone the repository and ensure you have a Java Development Kit (JDK) installed.
2. Add the JAR libraries from `lib/` to the build path.
3. Compile the source files.
4. Run the `Main` class. The application accepts several command-line options:
   * `-i <file>`: Input JSON events file.
   * `-o <file>`: Output JSON file for simulation results (console mode only). If omitted, the report is printed to stdout.
   * `-t <number>`: Number of simulation ticks to execute (default is 10).
   * `-m <gui|console>`: Launch in GUI mode or console mode. Default is `gui`.
   * `-h`: Show all available commands.

**Notes:**
* In GUI mode, the `-i` option is optional (the simulator can start without loading events), and both `-o` and `-t` are ignored.
* Additional example JSON input files can be found in `resources/examples`.
