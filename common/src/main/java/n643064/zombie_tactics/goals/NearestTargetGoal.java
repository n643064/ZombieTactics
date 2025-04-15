package n643064.zombie_tactics.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.NotNull;


// allow zombies to attack invisible entities
public class NearestTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private int delay = 0;
    public NearestTargetGoal(Mob mob, Class<T> targetType, boolean mustSee) {
        super(mob, targetType, mustSee);
        this.targetConditions = targetConditions.ignoreLineOfSight();
    }

    // reset target to the other
    @Override
    public boolean canContinueToUse() {
        boolean ret = super.canContinueToUse();
        ++ delay;
        if(delay > 20) {
            delay = 0;
            LivingEntity prev = mob.getTarget();
            findTarget();
            if(target != null && prev != null && prev.distanceTo(target) < 1) {
                mob.setTarget(prev);
                target = prev;
            } else mob.setTarget(target);
        }
        return ret;
    }

    // search also for Y
    @Override
    protected @NotNull AABB getTargetSearchArea(double targetDistance) {
        return mob.getBoundingBox().inflate(targetDistance, targetDistance, targetDistance);
    }
}
