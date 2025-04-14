package n643064.zombie_tactics.mixin;

import n643064.zombie_tactics.impl.Plane;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.minecraft.world.entity.ai.goal.DoorInteractGoal;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(BreakDoorGoal.class)
public abstract class BreakDoorGoalMixin extends DoorInteractGoal implements Plane {
    // to prevent that the zombies climb when breaking a door
    @Unique public boolean zombie_tactics$isBreaking;

    @Unique
    @Override
    public boolean zombie_tactics$getBool() {
        return zombie_tactics$isBreaking;
    }

    public BreakDoorGoalMixin(Mob mob) {
        super(mob);
    }
    /**
     * distance limit: 2 -> 4
     * @return 4.0
     */
    @ModifyExpressionValue(method = "canContinueToUse", at = @At(value="CONSTANT", args="doubleValue=2.0"))
    public double doorRange(double original) {
        return 4;
    }

    @Inject(method = "start", at = @At("TAIL"))
    public void start(CallbackInfo ci) {
        zombie_tactics$isBreaking = true;
    }

    @Inject(method = "stop", at = @At("TAIL"))
    public void stop(CallbackInfo ci) {
        zombie_tactics$isBreaking = false;
    }

    // breaking door sound and the swinging arms are continued while the door was broken
    @Inject(method = "canContinueToUse", at = @At("RETURN"), cancellable = true)
    public void canContinueToUse(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() && !mob.level().getBlockState(this.doorPos).isAir());
    }
}
