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
        bp = bp.above();
        int diff = zombie.getBlockY() - bp.getY();
        final BlockPos bp2 = bp.offset(0, diff, 0);

        if (checkBlock(bp2))
            target = bp2;
        else if (checkBlock(bp))
            target = bp;
        else if (checkBlock(bp.below()))
            target = bp.below();
        else
            return false;
        return true;
    }

    boolean checkBlock(BlockPos pos)
    {
        final BlockState state = level.getBlockState(pos);
        final Block b = state.getBlock();
        System.out.println("check " + pos);
        return !b.isPossibleToRespawnInThis(state) && b.defaultDestroyTime() <= Config.hardnessMult;
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
        return target != null && zombie.getEyePosition().distanceToSqr(target.getCenter()) <= 9;
    }

    @Override
    public boolean canUse()
    {
        if(zombie.isAlive() && !zombie.isNoAi() && (zombie.getNavigation().isStuck() || zombie.getNavigation().isDone()) && zombie.getTarget() != null)
        {
            //System.out.println("can use check passed");
            final BlockPos bp = Util.off(zombie.blockPosition(), zombie.getTarget().blockPosition());
            final double dsqr = zombie.distanceToSqr(zombie.getTarget());
            if (checkBlock(bp))
                target = bp;
            else if (dsqr < 2 || dsqr > 5 || !scanColumn(bp))
                target = null;
        }
        return target != null;
    }



}
