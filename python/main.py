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


def parse_data(problem: Problem):
    return np.genfromtxt(problem.value, delimiter=',', dtype=float)



if __name__ == '__main__':
    pass


