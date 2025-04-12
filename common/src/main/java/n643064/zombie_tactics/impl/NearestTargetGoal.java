package n643064.zombie_tactics.impl;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;


// allow zombies to attack invisible entities
public class NearestTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    public NearestTargetGoal(Mob mob, Class<T> targetType, boolean mustSee) {
        super(mob, targetType, mustSee);
        this.targetConditions = TargetingConditions.forCombat().ignoreLineOfSight();
    }
}
