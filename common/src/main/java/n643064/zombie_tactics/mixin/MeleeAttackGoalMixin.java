package n643064.zombie_tactics.mixin;

import n643064.zombie_tactics.impl.Plane;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Zombie;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;


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
    }

    @ModifyExpressionValue(method="resetAttackCooldown", at=@At(value="CONSTANT", args="intValue=20"))
    public int reset(int original) {
        return getAttackInterval();
    }

    @ModifyExpressionValue(method="getAttackInterval", at=@At(value="CONSTANT", args="intValue=20"))
    public int attack(int original) {
        return attackInterval;
    }

    @WrapWithCondition(method="tick", at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/ai/control/LookControl;setLookAt(Lnet/minecraft/world/entity/Entity;FF)V"))
    public boolean modifyLookControl(LookControl instance, Entity entity, float deltaYaw, float deltaPitch) {
        // if I am not a zombie which is mining
        return !(mob instanceof Zombie z && ((Plane)z).zombie_tactics$getBool(0));
    }
}
