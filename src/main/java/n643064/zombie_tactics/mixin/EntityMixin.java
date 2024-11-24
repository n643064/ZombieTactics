package n643064.apocalypse.mixin;

import n643064.apocalypse.Apocalypse;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin
{
    @Shadow public abstract Vec3d getVelocity();
    @Shadow public abstract void setVelocity(double x, double y, double z);

    @Shadow public boolean horizontalCollision;

    @Inject(method = "pushAwayFrom", at = @At("HEAD"))
    private void pushAwayFrom(Entity entity, CallbackInfo ci)
    {
        if (Apocalypse.config.zombie.climbEachOther && entity instanceof ZombieEntity && getClass().isAssignableFrom(ZombieEntity.class) && entity.isOnGround() && horizontalCollision)
        {
            final Vec3d v = getVelocity();
            setVelocity(v.x, Apocalypse.config.zombie.climbingVelocity, v.z);
        }
    }
}
