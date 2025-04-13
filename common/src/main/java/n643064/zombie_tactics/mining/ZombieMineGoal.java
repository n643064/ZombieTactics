package n643064.zombie_tactics.mining;

import static n643064.zombie_tactics.mining.MiningRoutines.*;
import n643064.zombie_tactics.attachments.MiningData;
import n643064.zombie_tactics.Config;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;

import org.jetbrains.annotations.NotNull;


public class ZombieMineGoal<T extends Monster> extends Goal {
    private final T zombie;
    private final Level level;
    private double progress, hardness = Double.MAX_VALUE;

    // These are constant unless a target is changed
    private double X, Y, Z;

    public final MiningData mine;

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

    private Rotation getRelativeRotation() {
        Vec3i norm = zombie.getNearestViewDirection().getNormal();
        int x = norm.getX(), z = norm.getZ();
        if(x == 0 && z == 1) return Rotation.NONE;
        else if(x == 0 && z == -1) return Rotation.CLOCKWISE_180;
        else if(x == 1 && z == 0) return Rotation.CLOCKWISE_90;
        else return Rotation.COUNTERCLOCKWISE_90; // x == -1, z = 0
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
        if(zombie.isNoAi() || !zombie.isAlive()) return false;
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
        if(nav.isDone() && liv != null /*&& nav.isStuck()*/) {
            if(zombie.isWithinMeleeAttackRange(liv) && zombie.hasLineOfSight(liv)) return false;

            // why is the path null even though it can reach a target?
            // the sucks
            Path p = nav.createPath(liv, 0);
            if(p == null) {
                System.out.println(nav + ", " + zombie.getNearestViewDirection().getNormal());
            }

            // go once more
            // Issue: moveTo sometimes return false while a zombie can go to the target.
            // It can solve by using the method `hasLineOfSight` but this causes a problem
            //   about fences that have 1.5 meters tall.
            boolean eval = nav.moveTo(liv, zombie.getSpeed());
            if(eval) return false;
            BlockPos[] set = getCandidate(liv);
            int airStack = 0;
            for(BlockPos pos: set) {
                // checkBlock method is able to change 'zombie' variable
                // So 'temp' cannot be determined as valid object
                // select relative block position
                BlockPos temp = zombie.blockPosition().offset(pos.rotate(getRelativeRotation()));
                // prevent that they are not stuck but zombie digs under their foot
                // It may fix the described issue in specific cases
                if(level.getBlockState(temp).isAir()) ++ airStack;
                if(airStack == set.length - 1) return false;
                if(checkBlock(temp)) return true;
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
