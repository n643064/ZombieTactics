package n643064.zombie_tactics.neoforge.mixin;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

import org.spongepowered.asm.mixin.*;

import java.util.EnumSet;

@Mixin(MeleeAttackGoal.class)
public abstract class MeleeAttackGoalMixin extends Goal {
    // Why are there too many unused private fields?
    // So, just expose them
    @Mutable @Final @Shadow private int attackInterval = 20;
    @Shadow private int ticksUntilNextAttack = attackInterval;
    @Mutable @Final @Shadow private double speedModifier;
    @Mutable @Final @Shadow private boolean followingTargetEvenIfNotSeen;
    @Mutable @Final @Shadow protected PathfinderMob mob;

    public MeleeAttackGoalMixin(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.followingTargetEvenIfNotSeen = followingTargetEvenIfNotSeen;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Shadow public abstract boolean canUse();

    /**
     * @author PICOPress
     * @reason Using unused private variable
     */
    @Overwrite
    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(getAttackInterval());
    }

    /**
     * @author PICOPress
     * @reason same above
     */
    @Overwrite
    public int getAttackInterval() {
        return this.adjustedTickDelay(attackInterval);
    }
}
