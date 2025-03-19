package n643064.zombie_tactics.mixin;

import n643064.zombie_tactics.Config;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Climbing
@Mixin(Entity.class)
public abstract class EntityMixin
{
    @Unique private boolean zombieTactics$isClimbing = false;
    @Shadow public boolean horizontalCollision;
    @Shadow public abstract Vec3 getDeltaMovement();
    @Shadow public abstract void setDeltaMovement(double x, double y, double z);

    @Shadow public float fallDistance;

    @Inject(method = "push(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"))
    private void pushAwayFrom(Entity entity, CallbackInfo ignoredCI)
    {
        if (Config.zombiesClimbing && entity instanceof Zombie &&
                getClass().isAssignableFrom(Zombie.class) &&
                horizontalCollision)
        {
            final Vec3 v = getDeltaMovement();
            setDeltaMovement(v.x, Config.climbingSpeed, v.z);
            zombieTactics$isClimbing = true;
        }
    }

    // a zombie doesn't take fall damage while it is climbing
    @Inject(method = "checkFallDamage", at = @At("HEAD"))
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos, CallbackInfo ci)
    {
        if(zombieTactics$isClimbing)
        {
            fallDistance = 0;
            zombieTactics$isClimbing = false;
        }
    }
}
