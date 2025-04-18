package n643064.zombie_tactics.goals;

import n643064.zombie_tactics.Config;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.phys.Vec3;

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
        Optional<BlockPos> bp = mob.mainSupportingBlockPos;
        if(bp.isPresent()) {
            BlockPos pos = bp.get().mutable();
            float rot = mob.getViewXRot(1);
            if(rot >= -45 && rot <= 45) {
                pos = pos.offset(1, 0, 0);
            } else if(rot > 45 && rot < 135) {
                pos = pos.offset(0, 0, 1);
            } else if(rot >= 135 || rot <= -135) {
                pos = pos.offset(-1, 0, 0);
            } else if(rot < -45) {
                pos = pos.offset(0, 0, -1);
            }
            //System.out.println(mob.level().getBlockState(pos).getBlock());
            if(mob.level().getBlockState(pos).isAir()) {
                mob.getJumpControl().jump();
                LivingEntity liv = mob.getTarget();
                if(liv != null) {
                    Vec3 v = liv.position().subtract(mob.position());
                    mob.getLookControl().setLookAt(liv);
                    mob.setDeltaMovement(v.scale(Config.jumpAcceleration / v.length()));
                }
            }

        }
    }
}
