import numpy as np
from enum import Enum
import matplotlib.pyplot as plt

FROZEN_LAKE = '../java/assignment-4/gridworld/'
LUNAR_LANDER = '../java/assignment-4/lunarlander/'

VALUE_ITERATION_PREFIX = 'vi_'
POLICY_ITERATION_PREFIX = 'pi_'
Q_LEARNING_PREFIX = 'ql_'

FROZEN_LAKE_SMALL = '11x11.csv'
FROZEN_LAKE_LARGE = '22x22.csv'
LUNAR_LANDER_SMALL = 'SMALL.csv'
LUNAR_LANDER_LARGE = 'LARGE.csv'


class Problem(Enum):
    FROZEN_LAKE_VI_SMALL = FROZEN_LAKE + VALUE_ITERATION_PREFIX + FROZEN_LAKE_SMALL
    FROZEN_LAKE_PI_SMALL = FROZEN_LAKE + POLICY_ITERATION_PREFIX + FROZEN_LAKE_SMALL
    FROZEN_LAKE_QL_SMALL = FROZEN_LAKE + Q_LEARNING_PREFIX + FROZEN_LAKE_SMALL

    FROZEN_LAKE_VI_LARGE = FROZEN_LAKE + VALUE_ITERATION_PREFIX + FROZEN_LAKE_LARGE
    FROZEN_LAKE_PI_LARGE = FROZEN_LAKE + POLICY_ITERATION_PREFIX + FROZEN_LAKE_LARGE
    FROZEN_LAKE_QL_LARGE = FROZEN_LAKE + Q_LEARNING_PREFIX + FROZEN_LAKE_LARGE

    LUNAR_LANDER_VI_SMALL = FROZEN_LAKE + VALUE_ITERATION_PREFIX + LUNAR_LANDER_SMALL
    LUNAR_LANDER_PI_SMALL = LUNAR_LANDER + POLICY_ITERATION_PREFIX + LUNAR_LANDER_SMALL
    LUNAR_LANDER_QL_SMALL = LUNAR_LANDER + Q_LEARNING_PREFIX + LUNAR_LANDER_SMALL

    LUNAR_LANDER_VI_LARGE = LUNAR_LANDER + VALUE_ITERATION_PREFIX + LUNAR_LANDER_LARGE
    LUNAR_LANDER_PI_LARGE = LUNAR_LANDER + POLICY_ITERATION_PREFIX + LUNAR_LANDER_LARGE
    LUNAR_LANDER_QL_LARGE = LUNAR_LANDER + Q_LEARNING_PREFIX + LUNAR_LANDER_LARGE


class Analysis:
    def __init__(self, convergence, delta_init, delta_max, time):
        self.convergence = convergence
        self. delta_init = delta_init
        self.delta_max = delta_max
        self.time = time


def parse_data(problem: Problem) -> Analysis:
    data = np.genfromtxt(problem.value, delimiter=',', dtype=float)
    return Analysis(data[0], data[1], data[2], data[3])


def create_plot(problem: Problem):
    analysis = parse_data(problem)
    iterations = np.arange(analysis.time.size)

    fig, (convergence_plot, delta_plot, time_plot) = plt.subplots(1, 3)

    convergence_plot.set_title('Convergence')
    convergence_plot.set_xlabel('Iteration')
    convergence_plot.set_ylabel('Reward')
    convergence_plot.plot(iterations, analysis.convergence)

    delta_plot.set_title('Delta Convergence')
    delta_plot.set_xlabel('Iteration')
    delta_plot.set_ylabel('Reward Delta')
    delta_plot.plot(iterations, analysis.delta_init, label='Initial State Delta', linestyle='dotted')
    delta_plot.plot(iterations, analysis.delta_max, label='Max Delta', linestyle='dashed')
    delta_plot.legend(loc='best')

    time_plot.set_title('Time to Converge')
    time_plot.set_xlabel('Iteration')
    time_plot.set_ylabel('Time (in ms)')
    time_plot.plot(iterations, analysis.time)
    time_plot.text(0, 0, f'Total time: {analysis.time.sum()} ms')

    plt.show()


if __name__ == '__main__':
    pass


