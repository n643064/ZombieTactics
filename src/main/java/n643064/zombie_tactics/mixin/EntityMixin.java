package n643064.zombie_tactics.mixin;

import n643064.zombie_tactics.Config;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin
{

    @Shadow public boolean horizontalCollision;
    @Shadow public abstract Vec3 getDeltaMovement();
    @Shadow public abstract void setDeltaMovement(double x, double y, double z);

    @Inject(method = "push(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"))
    private void pushAwayFrom(Entity entity, CallbackInfo ci)
    {
        if (Config.zombiesClimbing && entity instanceof Zombie && getClass().isAssignableFrom(Zombie.class) && entity.onGround() && horizontalCollision)
        {
            final Vec3 v = getDeltaMovement();
            setDeltaMovement(v.x, Config.climbingSpeed, v.z);
        }
    }
}
