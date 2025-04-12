package n643064.zombie_tactics.mining;


public class MiningRoutines {
    /*  Y
        /\   Z
        |   /
        |  /
        | /
        |/________ X
        O
     */
    // The sequence of these array is important because
    //      it determines how a zombie reaches its target.
    // For example, the target could be placed above, below,
    //      in front of, or behind the zombie.
    // The order in which blocks are mined
    //      affects the zombie's movement path.
    public static final byte[][] routineUp = new byte[][] {
            // Y = 1
            // The blocks in front, behind, to the left and the right of eye level
            {0, 1, 1},
            {-1, 1, 0},
            {1, 1, 0},
            {0, 1, -1},

            // Y = 2
            {0, 2, 0},
            {0, 2, 1},
            {0, 2, -1},
            {1, 2, 0},
            {-1, 2, 0},

            // Y = 0
            {0, 0, 1},
            {-1, 0, 0},
            {1, 0, 0},
            {0, 0, -1},

            // Y = -1
            //{0, -1, 0},
    };

    public static final byte[][] routineDown = new byte[][] {
            // Y = 1
            {0, 1, 1},
            {-1, 1, 0},
            {1, 1, 0},
            {0, 1, -1},

            // Y = 0
            {0, 0, 1},
            {-1, 0, 0},
            {1, 0, 0},
            {0, 0, -1},

            // Y = 2
            {0, 2, 0},

            // Y = -1
            {0, -1, 0},
            {0, -1, 1},
            {-1, -1, 0},
            {1, -1, 0},
            {0, -1, -1},
    };
    public static final byte[][] routineFlat = new byte[][] {
            // Y = 1
            {0, 1, 1},
            {-1, 1, 0},
            {1, 1, 0},
            {0, 1, -1},

            // Y = 0
            {0, 0, 1},
            {-1, 0, 0},
            {1, 0, 0},
            {0, 0, -1},

            // Y = 2
            {0, 2, 0},

            // Y = -1
            {0, -1, 0},
    };
}
