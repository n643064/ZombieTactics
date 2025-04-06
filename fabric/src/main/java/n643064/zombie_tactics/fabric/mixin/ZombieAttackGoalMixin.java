package n643064.zombie_tactics.fabric.mixin;

import n643064.zombie_tactics.fabric.ModConfig;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.entity.mob.ZombieEntity;

import org.spongepowered.asm.mixin.Mixin;

// Refactoring ZombieAttackGoal
// I just want to make attack speed faster
// But Idk why it doesn't work on Fabric...
@Mixin(ZombieAttackGoal.class)
public abstract class ZombieAttackGoalMixin extends MeleeAttackGoal {
    public ZombieAttackGoalMixin(ZombieEntity zombie, double speedModifier, boolean pauseWenMobIdle) {
        super(zombie, speedModifier, pauseWenMobIdle);
    }

    @Override
    protected int getMaxCooldown() {
        return this.getTickCount(ModConfig.attackCooldown);
    }
}
