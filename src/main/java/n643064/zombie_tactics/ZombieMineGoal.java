package n643064.zombie_tactics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ZombieMineGoal extends Goal
{
    final Zombie zombie;
    final Level level;
    BlockPos target;
    double progress, hardness = Double.MAX_VALUE;

    final byte[][] offsets = new byte[][]
            {
                    {0, 0, 1},
                    {0, 0, -1},
                    {1, 0, 0},
                    {-1, 0, 0},
                    {0, -1, 0},
                    {0, 2, 0},

                    {1, 0, 1},
                    {1, 0, -1},
                    {-1, 0, 1},
                    {-1, 0, -1}
            };

    public ZombieMineGoal(Zombie zombie)
    {
        this.zombie = zombie;
        level = zombie.level();
    }

    @Override
    public boolean requiresUpdateEveryTick()
    {
        return true;
    }

    @Override
    public void start()
    {
        progress = 0;
        hardness = level.getBlockState(target).getBlock().defaultDestroyTime() * Config.hardnessMult;
    }

    boolean scanColumn(BlockPos bp)
    {
        //System.out.println("scan " + bp);
        int diff = Integer.compare(zombie.getBlockY() - bp.getY(), 0);

        if (!checkBlock(bp.offset(0, diff, 0)))
            if (!checkBlock(bp))
                return checkBlock(bp.offset(0, -diff, 0));
        return true;
    }

    boolean checkBlock(BlockPos pos)
    {
        final BlockState state = level.getBlockState(pos);
        final Block b = state.getBlock();
        //System.out.println("check " + pos);
        if (!b.isPossibleToRespawnInThis(state) && b.defaultDestroyTime() <= Config.maxHardness)
        {
            target = pos;
            return true;
        }
        return false;
    }

    @Override
    public void stop()
    {
        if (target != null)
        {
            zombie.level().destroyBlockProgress(zombie.getId(), target, -1);
            target = null;
        }
        zombie.getNavigation().recomputePath();
        progress = 0;
        hardness = Double.MAX_VALUE;
    }

    @Override
    public void tick()
    {
        if (zombie.getTarget() == null)
            target = null;
        if (target == null) return;
        final double d = zombie.distanceToSqr(zombie.getTarget());

        if (level.getBlockState(target).isAir() || d <= Config.minDist || d > Config.maxDist)
        {
            target = null;
            return;
        }
        if (progress >= hardness)
        {
            level.destroyBlock(target, Config.dropBlocks, zombie);
            zombie.level().destroyBlockProgress(zombie.getId(), target, -1);
            target = null;
        } else
        {
            level.destroyBlockProgress(zombie.getId(), target, (int) ((progress / hardness) * 10));
            zombie.stopInPlace();
            zombie.getLookControl().setLookAt(target.getX(), target.getY(), target.getZ());
            progress += Config.increment;
            zombie.swing(InteractionHand.MAIN_HAND);
        }
    }

    @Override
    public boolean canContinueToUse()
    {
        return target != null && zombie.distanceToSqr(target.getCenter()) <= 9;
    }

    @Override
    public boolean canUse()
    {
        if(zombie.isAlive() && !zombie.isNoAi() && (zombie.getNavigation().isStuck() || zombie.getNavigation().isDone()) && zombie.getTarget() != null)
        {
            //zombie.getNavigation().recomputePath();
            BlockPos bp = Util.off(zombie.blockPosition(), zombie.getTarget().blockPosition());
            final double dttsqr = zombie.distanceToSqr(zombie.getTarget());
            if (dttsqr * 1.2 >= zombie.distanceToSqr(bp.getCenter()) && !scanColumn(bp.above()))
                if(zombie.getNavigation().isStuck() && !scanColumn(bp))
                    for (byte[] o : offsets)
                        scanColumn(zombie.blockPosition().offset(o[0], o[1], o[2]));
            /*
            else if (!scanColumn(bp))
                if (zombie.getNavigation().isStuck())
                    bp = zombie.blockPosition();
                    for (byte[] o : offsets)
                        if (scanColumn(bp.offset(o[0], o[1], o[2])))
                            break;
                else
                    target = null;

             */
        }
        return target != null;
    }
}
