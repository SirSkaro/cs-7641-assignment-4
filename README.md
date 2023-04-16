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

## Running the MDPs - Java
To run an MDP problem, use the `exec:java` goal in Maven. You will also need to specify the main class and commandline arguments. This project uses the `exec-maven-plugin` 
tool. Please refer to its documentation for additional information.

### Frozen Lake
Execute `FrozenLakeExperiment.java` via the following command:
```
mvn compile exec:java -D exec.mainClass="edu.gatech.churchill.cs7641.frozenlake.FrozenLakeExperiment" -D exec.args="VI 11 11"
```
Three arguments are required: the algorithm (VI, PI, or QL) and the world dimensions (width, height). A world will be randomly generated and solved. Two GUIs will pop up
when finished: a GUI for the policy and a GUI showing an episode of running the policy.

### Lunar Lander
Execute `LunarLander.java` via the following command:
```
mvn compile exec:java -D exec.mainClass="edu.gatech.churchill.cs7641.lunarlander.LunarLanderExperiment" -D exec.args="VI small"
```
Two arguments are required: the algorithm (VI, PI, or QL) and the world size (small or large). 

No other configuration is possible from the commandline. Any other modification will require code changes.

Running an algorithm over a problem outputs a `.csv` file (in the working directory) that the Python script uses to generate plots.

## Creating the Plots - Python
Make sure some `.csv` files were created. This script assumes they are located at the root of the Java code.

To create convergence, delta convergence, and time/iteration plots, use the `create_plot` function using one of the `Problem` enums. There is an enum instance
for each combination of problem and world size. Note the enum is hard-coded for Frozen Lake sizes of 11x11 and 22x22.

Example using an interactive interpreter (with your virtual environments):
```python
from main import Problem, create_plot
create_plot(Problem.FROZEN_LAKE_QL_SMALL)
```
