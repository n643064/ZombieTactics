package n643064.zombie_tactics.goals;

import static n643064.zombie_tactics.util.Tactics.*;
import n643064.zombie_tactics.Config;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;
import java.util.Optional;


// for attacking and following
public class ZombieGoal extends ZombieAttackGoal {
    public ZombieGoal(Zombie zombie, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(zombie, speedModifier, followingTargetEvenIfNotSeen);
    }

    @Override
    protected int getAttackInterval() {
        return this.adjustedTickDelay(Config.attackCooldown);
    }

    @Override
    public void tick() {
        super.tick();

        // jump a block
        if(Config.jumpBlock && !mob.isWithinMeleeAttackRange(mob.getTarget())) {
            Optional<BlockPos> bp = mob.mainSupportingBlockPos;
            if(bp.isPresent()) {
                BlockPos pos = bp.get().mutable().offset(UNIT_FRONT.rotate(getRelativeRotation(mob))).above().above();
                boolean airs = true;
                /* do not jump in an inadequate situation
                    zombie      target
                    ______    ______
                    |    |    |    |
                    |    |    |    |
                    |    |____|    |
                 */
                for(int i = 0; i < 5; ++ i) {
                    if(!mob.level().isEmptyBlock(pos)) {
                        airs = false;
                        break;
                    }
                    if(i != 4) pos = pos.below();
                }
                // this algorithm should be improved
                // for now, it cannot cover all cases
                if(airs) {
                    mob.getJumpControl().jump();
                    // target must not be null
                    Vec3 v = Objects.requireNonNull(mob.getTarget()).position().subtract(mob.position());
                    mob.setDeltaMovement(v.scale(Config.jumpAcceleration / v.length()));
                }
            }
        }
    }
}
