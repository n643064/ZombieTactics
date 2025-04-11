package n643064.zombie_tactics.mixin;

import n643064.zombie_tactics.Config;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;

import org.spongepowered.asm.mixin.Mixin;

// Refactoring ZombieAttackGoal
// I just want to make attack speed faster
@Mixin(ZombieAttackGoal.class)
public abstract class ZombieAttackGoalMixin extends MeleeAttackGoal {
    public ZombieAttackGoalMixin(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(mob, speedModifier, followingTargetEvenIfNotSeen);
    }

    @Override
    protected int getAttackInterval() {
        return this.adjustedTickDelay(Config.attackCooldown);
    }
}
