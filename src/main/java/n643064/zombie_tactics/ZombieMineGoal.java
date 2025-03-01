package n643064.zombie_tactics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ZombieMineGoal<T extends Zombie & IMarkerFollower> extends Goal
{
    final T zombie;
    final Level level;
    BlockPos target;
    double progress, hardness = Double.MAX_VALUE;
    boolean doMining;

    /* Z
        /\   Y
        |   /
        |  /
        | /
        |/________ X
        O
     */
    final byte[][] offsets = new byte[][] {
            // Y = 1
            // The blocks in front, behind, to the left and the right of eye level
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

    public ZombieMineGoal(T zombie)
    {
        doMining = true;
        this.zombie = zombie;
        level = zombie.level();
    }

    @Override
    public boolean requiresUpdateEveryTick()
    {
        return true;
    }

    @Override
    public void start() {
        doMining = true;
        progress = 0;
        hardness = level.getBlockState(target).getBlock().defaultDestroyTime() * Config.hardnessMult;
    }

    boolean checkBlock(BlockPos pos)
    {
        final BlockState state = level.getBlockState(pos);
        final Block b = state.getBlock();
        //System.out.println("check " + pos);
        if (!b.isPossibleToRespawnInThis(state) &&
                b.defaultDestroyTime() <= Config.maxHardness &&
                b.defaultDestroyTime() != -1) // exclude unbreakable blocks
        {
            doMining = true;
            target = pos;
            return true;
        }
        return false;
    }

    @Override
    public void stop()
    {
        zombie.level().destroyBlockProgress(zombie.getId(), target, -1);
        target = null;
        doMining = false;
        zombie.getNavigation().recomputePath();
        progress = 0;
        hardness = Double.MAX_VALUE;
    }

    @Override
    public void tick()
    {
        if (!doMining) return;
        double d = 0, d2 = 0;
        int check = 0;
        final MarkerEntity m = zombie.zombieTactics$getTargetMarker();
        final LivingEntity t = zombie.getTarget();

        if(t != null)
        {
            d = zombie.distanceToSqr(t);
            check += 1;
        }
        if(m != null)
        {
            d2 = zombie.distanceToSqr(m);
            check += 2;
        }
        switch(check)
        {
            case 0:
                doMining = false;
                return;

            case 1: break; // pass
            case 2: d = d2; break;
            case 3:
                if(d > d2) d = d2;
                break;
        }

        if (d <= Config.minDist || d > Config.maxDist)
        {
            doMining = false;
            return;
        }
        if(level.getBlockState(target).isAir())
        {
            // if the target has been broken by others
            level.destroyBlockProgress(zombie.getId(), target, -1);
            progress = 0;
            doMining = false;
            return;
        }
        if (progress >= hardness)
        {
            level.destroyBlock(target, Config.dropBlocks, zombie);
            zombie.level().destroyBlockProgress(zombie.getId(), target, -1);
            doMining = false;
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
        return doMining && zombie.distanceToSqr(target.getCenter()) <= 9;
    }

    @Override
    public boolean canUse()
    {
        // check availability of the mining
        // found path but a zombie stuck
        PathNavigation nav = zombie.getNavigation();
        if(zombie.isAlive() && !zombie.isNoAi() &&
                (nav.isStuck() || nav.isDone()))
        {
            /*
            final MarkerEntity mark = zombie.zombieTactics$getTargetMarker();
            final LivingEntity living = zombie.getTarget();
            double d = 0, d2 = 0;
            int check = 0;

            if(mark != null)
            {
                d = zombie.distanceToSqr(mark);
                check += 1;
            }
            if(living != null)
            {
                d2 = zombie.distanceToSqr(living);
                check += 2;
            }

            switch(check)
            {
                case 0: return false;
                case 1: break; // pass
                case 2: d = d2; break;
                case 3:
                    if(d > d2) d = d2;
                    break;
            }

             */

            for(byte[] options: offsets)
            {
                // checkBlock method is able to change 'zombie' variable
                // So 'temp' cannot be determined as valid object
                BlockPos temp = zombie.blockPosition().offset(options[0], options[1], options[2]);
                if(checkBlock(temp))
                {
                    return true;
                }
            }
        }
        // zombie cannot escape
        return false;
    }
}
