package n643064.zombie_tactics.mixin;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import java.util.EnumSet;


@Mixin(MeleeAttackGoal.class)
public abstract class MeleeAttackGoalMixin extends Goal {
    // Why are there too many unused private fields?
    // So, just expose them
    @Shadow private int attackInterval = 20;
    @Mutable @Final @Shadow private double speedModifier;
    @Mutable @Final @Shadow private boolean followingTargetEvenIfNotSeen;
    @Mutable @Final @Shadow protected PathfinderMob mob;

    @Shadow protected abstract int getAttackInterval();

    public MeleeAttackGoalMixin(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.followingTargetEvenIfNotSeen = followingTargetEvenIfNotSeen;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @ModifyExpressionValue(method = "resetAttackCooldown", at =@At(value="CONSTANT", args="intValue=20"))
    public int resetAttackCooldown(int original) {
        return getAttackInterval();
    }

    @ModifyExpressionValue(method = "getAttackInterval", at =@At(value="CONSTANT", args="intValue=20"))
    public int g(int original) {
        return attackInterval;
    }
}
