package n643064.zombie_tactics.neoforge.mining;

import static n643064.zombie_tactics.common.mining.MiningRoutines.*;
import n643064.zombie_tactics.neoforge.Config;
import n643064.zombie_tactics.neoforge.Main;
import n643064.zombie_tactics.neoforge.attachments.MiningData;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

public class ZombieMineGoal<T extends Zombie> extends Goal {
    private final T zombie;
    private final Level level;
    private double progress, hardness = Double.MAX_VALUE;

    private final MiningData mine;

    // These are constant unless a target is changed
    private double X, Y, Z;

    public ZombieMineGoal(T zombie) {
        mine = new MiningData();
        this.zombie = zombie;
        level = zombie.level();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        progress = 0;
        hardness = level.getBlockState(mine.bp).getBlock().defaultDestroyTime() * Config.hardnessMultiplier;
        mine.doMining = true;
        zombie.setData(Main.ZOMBIE_MINING, mine);
    }

    // get deltaY between me and target
    // then return a proper set of positions
    public byte[][] getCandidate(@NotNull LivingEntity liv) {
        double deltaY = liv.getY() - zombie.getY();
        if(deltaY > -2 && deltaY < 2)
            return routineFlat;
        else if(deltaY <= -2)
            return routineDown;
        else // deltaY >= 2
            return routineUp;
    }

    // is valid to mine?
    protected boolean checkBlock(BlockPos pos) {
        final BlockState state = level.getBlockState(pos);
        final Block b = state.getBlock();
        float destroying = b.defaultDestroyTime();

        // exclude unbreakable blocks
        if(!b.isPossibleToRespawnInThis(state) &&
                destroying >= 0 && destroying <= Config.maxHardness) {
            mine.doMining = true;
            mine.bp = pos;
            X = pos.getX();
            Y = pos.getY();
            Z = pos.getZ();
            return true;
        }
        return false;
    }

    @Override
    public void stop() {
        // reset all progress and find path again
        level.destroyBlockProgress(zombie.getId(), mine.bp, -1);
        mine.doMining = false;
        mine.bp = null;
        zombie.getNavigation().recomputePath();
        progress = 0;
        hardness = Double.MAX_VALUE;
    }

    @Override
    public void tick() {
        if (!mine.doMining) return;
        double dist = zombie.distanceToSqr(X, Y, Z);
        if (dist <= Config.minDist ||
            dist > Config.maxDist) {
            mine.doMining = false;
            return;
        }

        // if the target block has been broken by others
        if(level.getBlockState(mine.bp).isAir()) {
            level.destroyBlockProgress(zombie.getId(), mine.bp, -1);
            progress = 0;
            mine.doMining = false;
            return;
        }
        if (progress >= hardness) {
            level.destroyBlock(mine.bp, Config.dropBlocks, zombie);
            level.destroyBlockProgress(zombie.getId(), mine.bp, -1);
            mine.doMining = false;
        } else {
            level.destroyBlockProgress(zombie.getId(), mine.bp, (int) ((progress / hardness) * 10));
            zombie.stopInPlace();
            zombie.getLookControl().setLookAt(X, Y, Z);
            progress += Config.increment;
            zombie.swing(InteractionHand.MAIN_HAND);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return mine.doMining && zombie.distanceToSqr(mine.bp.getCenter()) <= Config.maxDist;
    }

    @Override
    public boolean canUse() {
        // a zombie should be stuck
        // check availability of the mining
        double x, y, z;
        x = zombie.getX();
        y = zombie.getY();
        z = zombie.getZ();
        if(X != x || Y != y || Z != z) {
            X = x; Y = y; Z = z;
            return false;
        }

        // found path but a zombie stuck
        LivingEntity liv = zombie.getTarget();
        PathNavigation nav = zombie.getNavigation();

        // I think that isStuck always return false
        if(zombie.isAlive() && !zombie.isNoAi() &&
                nav.isDone() && liv != null /*&& nav.isStuck()*/) {
            if(zombie.isWithinMeleeAttackRange(liv)) return false;

            // go once more
            // Issue: moveTo sometimes return false while a zombie can go to the target.
            // It can solve by using the method `hasLineOfSight` but this causes a problem
            //   about fences that have 1.5 meters tall.
            // TODO: fix this
            boolean eval = nav.moveTo(liv, zombie.getSpeed());
            byte[][] set = getCandidate(liv);

            if(eval) return false;
            else System.out.println(zombie);

            for(byte[] pos: set) {
                // checkBlock method is able to change 'zombie' variable
                // So 'temp' cannot be determined as valid object
                BlockPos temp = zombie.blockPosition().offset(pos[0], pos[1], pos[2]);
                if(checkBlock(temp)) return true;
            }
        }
        // zombie cannot escape
        return false;
    }
}
