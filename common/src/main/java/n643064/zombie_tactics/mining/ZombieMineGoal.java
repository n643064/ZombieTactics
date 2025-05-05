package n643064.zombie_tactics.mining;

import static n643064.zombie_tactics.mining.MiningRoutines.*;
import static n643064.zombie_tactics.util.Tactics.getRelativeRotation;
import n643064.zombie_tactics.attachments.MiningData;
import n643064.zombie_tactics.Config;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;


public class ZombieMineGoal<T extends Monster> extends Goal {
    private final T zombie;
    private final Level level;
    private double progress, hardness = Double.MAX_VALUE;

    // These are constant unless a target is changed
    private Vec3 where;

    public final MiningData mine;

    public ZombieMineGoal(T zombie) {
        mine = new MiningData();
        this.zombie = zombie;
        level = zombie.level();
        setFlags(EnumSet.of(Flag.LOOK));
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
    }

    // get deltaY between me and target
    // then return a proper set of positions
    public BlockPos[] getCandidate(@NotNull LivingEntity liv) {
        double deltaY = liv.getY() - zombie.getY();
        if(deltaY > -2 && deltaY < 2)
            return routineFlat;
        else if(deltaY <= -2)
            return routineDown;
        else // deltaY >= 2
            return routineUp;
    }

    // is valid to mine?
    private boolean checkBlock(BlockPos pos) {
        final BlockState state = level.getBlockState(pos);
        final Block b = state.getBlock();
        float destroying = b.defaultDestroyTime();

        // exclude unbreakable blocks
        if(!b.isPossibleToRespawnInThis(state) && destroying >= 0 && destroying <= Config.maxHardness) {
            mine.doMining = true;
            mine.bp = pos;
            mine.bp_vec3 = pos.getCenter();
            where = pos.getCenter();
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
        double dist = zombie.distanceToSqr(mine.bp_vec3);
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
            zombie.getLookControl().setLookAt(mine.bp_vec3);
            progress += Config.increment;
            zombie.swing(InteractionHand.MAIN_HAND);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return mine.doMining && zombie.distanceToSqr(where) <= Config.maxDist;
    }

    @Override
    public boolean canUse() {
        if(zombie.isNoAi() || !zombie.isAlive()) return false;
        // a zombie should be stuck
        // check availability of the mining
        Vec3 ord = zombie.position();
        if(!ord.equals(where)) {
            where = ord;
            // relaxed for flying zombies
            if(!Config.canFly) return false;
        }

        // found path but a zombie stuck
        LivingEntity liv = zombie.getTarget();
        PathNavigation nav = zombie.getNavigation();

        if(nav.isDone() && liv != null && nav.getPath() != null && !nav.getPath().canReach()) {
            if(zombie.isWithinMeleeAttackRange(liv) && zombie.hasLineOfSight(liv)) return false;

            // why is the path null even though it can reach a target?
            // the sucks

            // go once more
            // Issue: moveTo sometimes return false while a zombie can go to the target.
            // It can solve by using the method `hasLineOfSight` but this causes a problem
            // about fences that have 1.5 meters tall.
            if(nav.moveTo(liv, zombie.getSpeed())) return false;
            final BlockPos[] set = getCandidate(liv);
            int airStack = 0;

            for(BlockPos pos: set) {
                // checkBlock method is able to change 'zombie' variable
                // So 'temp' cannot be determined as valid object
                // select relative block position
                BlockPos temp = zombie.blockPosition().offset(pos.rotate(getRelativeRotation(zombie)));

                // prevent that they are not stuck but zombie digs under their foot
                // It may fix the described issue in specific cases
                if(level.getBlockState(temp).isAir()) ++ airStack;
                if(airStack == set.length - 1) break;
                if(checkBlock(temp))
                    return true;
            }

            // zombie is in the wall
        } else if(zombie.isInWall()) {
            for(BlockPos p: routineWall) {
                BlockPos temp = zombie.blockPosition().offset(p);
                if(checkBlock(temp)) return true;
            }
        }
        // zombie cannot escape
        return false;
    }
}
