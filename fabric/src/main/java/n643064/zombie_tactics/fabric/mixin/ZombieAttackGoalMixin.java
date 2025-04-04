package n643064.zombie_tactics.fabric.mixin;

import n643064.zombie_tactics.fabric.Main;

import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.entity.mob.ZombieEntity;

import org.spongepowered.asm.mixin.Mixin;

// Refactoring ZombieAttackGoal
// I just want to make attack speed faster
@Mixin(ZombieAttackGoal.class)
public abstract class ZombieAttackGoalMixin extends MeleeAttackGoal {
    public ZombieAttackGoalMixin(ZombieEntity zombie, double speedModifier, boolean pauseWenMobIdle) {
        super(zombie, speedModifier, pauseWenMobIdle);
    }

    @Override
    protected int getMaxCooldown() {
        return this.getTickCount(Main.config.attackCooldown);
    }
}
