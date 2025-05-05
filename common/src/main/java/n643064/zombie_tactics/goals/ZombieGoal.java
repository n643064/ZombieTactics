package n643064.zombie_tactics.goals;

import static n643064.zombie_tactics.util.Tactics.*;
import n643064.zombie_tactics.Config;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;


// for attacking and following
public class ZombieGoal extends ZombieAttackGoal {
    private Vec3 delta;
    private boolean jumping = false;
    Zombie mob;

    public ZombieGoal(Zombie zombie, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(zombie, speedModifier, followingTargetEvenIfNotSeen);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
        mob = zombie;
    }

    @Override
    protected int getAttackInterval() {
        return this.adjustedTickDelay(Config.attackCooldown);
    }

    @Override
    public void tick() {
        super.tick();

        // for debugging
        if(Config.showNodes) {
            Path path = this.mob.getNavigation().getPath();
            if(path != null && mob.getServer() != null) {
                ServerLevel server = mob.getServer().getLevel(mob.level().dimension());
                if(server != null)
                    for(int i = 0; i < path.getNodeCount(); ++ i) {
                        BlockPos pos = path.getNode(i).asBlockPos();
                        // add particles at the node
                        server.sendParticles(ParticleTypes.FLAME, pos.getX(), pos.getY(), pos.getZ(),
                                0, 0, 0, 0.1, 0.1);
                }
            }
        }

        // keeping delta movement when jumping except delta y(gravity)
        if(mob.onGround()) jumping = false;
        if(jumping) mob.setDeltaMovement(delta.x, mob.getDeltaMovement().y, delta.z);

        // jump a block
        if(Config.jumpBlock && !mob.isWithinMeleeAttackRange(mob.getTarget()) && mob.getNavigation().isDone()) {
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
                    jumping = true;
                    mob.getJumpControl().jump();
                    // target must not be null in here
                    delta = Objects.requireNonNull(mob.getTarget()).position().subtract(mob.position());
                    delta = delta.scale(Config.jumpAcceleration / delta.length());
                    mob.addDeltaMovement(delta);
                }
            }
        }
    }
}
