package n643064.zombie_tactics.fabric.mixin;

import n643064.zombie_tactics.fabric.Config;

import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.ZombieAttackGoal;

import net.minecraft.entity.mob.ZombieEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

// Refactoring ZombieAttackGoal
// I just want to make attack speed faster
@Mixin(ZombieAttackGoal.class)
public class ZombieAttackGoalMixin extends MeleeAttackGoal {
    public ZombieAttackGoalMixin(ZombieEntity zombie, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(zombie, speedModifier, followingTargetEvenIfNotSeen);
    }

    @Override
    @Unique
    protected int getCooldown() {
        return this.getTickCount(Config.attackCooldown);
    }
}
