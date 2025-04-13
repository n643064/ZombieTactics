package n643064.zombie_tactics.mining;

import net.minecraft.core.BlockPos;


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
    public static final BlockPos[] routineUp = new BlockPos[] {
            // Y = 1
            // The blocks in front, behind, to the left and the right of eye level
            new BlockPos(0, 1, 1),
            new BlockPos(-1, 1, 0),
            new BlockPos(1, 1, 0),
            new BlockPos(0, 1, -1),

            // Y = 2
            new BlockPos(0, 2, 0),
            new BlockPos(0, 2, 1),
            new BlockPos(0, 2, -1),
            new BlockPos(1, 2, 0),
            new BlockPos(-1, 2, 0),

            // Y = 0
            new BlockPos(0, 0, 1),
            new BlockPos(-1, 0, 0),
            new BlockPos(1, 0, 0),
            new BlockPos(0, 0, -1),

            // Y = -1
            //new BlockPos(0, -1, 0),
    };

    public static final BlockPos[] routineDown = new BlockPos[] {
            // Y = 1
            new BlockPos(0, 1, 1),
            new BlockPos(-1, 1, 0),
            new BlockPos(1, 1, 0),
            new BlockPos(0, 1, -1),

            // Y = 0
            new BlockPos(0, 0, 1),
            new BlockPos(-1, 0, 0),
            new BlockPos(1, 0, 0),
            new BlockPos(0, 0, -1),

            // Y = 2
            new BlockPos(0, 2, 0),

            // Y = -1
            new BlockPos(0, -1, 0),
            new BlockPos(0, -1, 1),
            new BlockPos(-1, -1, 0),
            new BlockPos(1, -1, 0),
            new BlockPos(0, -1, -1),
    };

    public static final BlockPos[] routineFlat = new BlockPos[] {
            // Y = 1
            new BlockPos(0, 1, 1),
            new BlockPos(-1, 1, 0),
            new BlockPos(1, 1, 0),
            new BlockPos(0, 1, -1),

            // Y = 0
            new BlockPos(0, 0, 1),
            new BlockPos(-1, 0, 0),
            new BlockPos(1, 0, 0),
            new BlockPos(0, 0, -1),

            // Y = 2
            new BlockPos(0, 2, 0),

            // Y = -1
            new BlockPos(0, -1, 0),
    };

    // isInWall() == true
    public static final BlockPos[] routineWall = new BlockPos[] {
            new BlockPos(0, 1, 0),
            new BlockPos(0, 0, 0),
    };
}
