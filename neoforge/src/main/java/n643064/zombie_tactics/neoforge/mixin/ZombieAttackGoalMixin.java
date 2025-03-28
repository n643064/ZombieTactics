package n643064.zombie_tactics.neoforge.mixin;

import n643064.zombie_tactics.neoforge.Config;
import net.minecraft.world.entity.PathfinderMob;

import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

// Refactoring ZombieAttackGoal
// I just want to make attack speed faster
@Mixin(ZombieAttackGoal.class)
public class ZombieAttackGoalMixin extends MeleeAttackGoal {
    public ZombieAttackGoalMixin(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(mob, speedModifier, followingTargetEvenIfNotSeen);
    }

    @Override
    @Unique
    protected int getAttackInterval() {
        return this.adjustedTickDelay(Config.attackCooldown);
    }
}
