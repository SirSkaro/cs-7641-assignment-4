Code is located at https://github.com/SirSkaro/cs-7641-assignment-4

## Setting up an envrionment - Java
1) Clone the repository
2) Install JDK 17 (11 is probably fine too)
3) Install the latest version of Maven
4) Configure the project to use JDK17 (JDK8 or 11 will probably work too)

## Setting up an envrionment - Python
1) Clone the repository
2) Setup and activate a virtual environment with Python 3.10.
    * If using Anaconda/miniconda, you can simply use a command like "conda create -n bchurchill6_assignment1 python=3.10".
    * Otherwise, download a distribution of Python 3.10 manually and use it to run "python -m venv assignment4"
3) Install requirements. With the base of the project as the working directory, you can run the command "pip install -r requirements.txt"
4) Import the project into your favorite IDE

## Running the MDPs - Java
To run an MDP problem, use the `exec:java` goal in Maven. You will also need to specify the main class and commandline arguments. This project uses the `exec-maven-plugin` 
tool. Please refer to its documentation for additional information.

### Frozen Lake
Execute FrozenLakeExperiment.java via the following command:
```
mvn compile exec:java -D exec.mainClass="edu.gatech.churchill.cs7641.frozenlake.FrozenLakeExperiment" -D exec.args="VI 11 11"
```
Three arguments are required: the algorithm (VI, PI, or QL) and the world dimensions (width, height). A world will be randomly generated and solved. Two GUIs will pop up
when finished: a GUI for the policy and a GUI showing an episode of running the policy.

### Lunar Lander
Execute LunarLander.java via the following command:
```
mvn compile exec:java -D exec.mainClass="edu.gatech.churchill.cs7641.lunarlander.LunarLanderExperiment" -D exec.args="VI small"
```
Two arguments are required: the algorithm (VI, PI, or QL) and the world size.

No other configuration is possible from the commandline. Any other modification will require code changes.

Running an algorithm over a problem outputs a `.csv` file (in the working directory) that the Python script uses to generate plots.

## Running the Graphs - Python
First, copy any CSV files created from the Java code into the root directory of the Python code.

Using an interactive interpreter (with your virtual environments), run the following code:
```python
from graph import Problem
import graph

# graph results for 150-Queens
graph.graph_problem(Problem.N_QUEENS)

# graph results for Four Peaks
graph.graph_problem(Problem.FOUR_PEAKS)

# graph results for Traveling Salesman
graph.graph_problem(Problem.TRAVELING_SALESMAN)

#graph results for Neural Network
graph.graph_nn()
```
