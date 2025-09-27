package n643064.zombie_tactics.mixin;

import n643064.zombie_tactics.MarkerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static n643064.zombie_tactics.Config.CONFIG;

@Mixin(Projectile.class)
public abstract class ProjectileMixin extends Entity
{
    public ProjectileMixin(EntityType<?> entityType, Level level)
    {
        super(entityType, level);
    }

    @Inject(method = "onHit", at = @At("TAIL"))
    private void onHit(HitResult result, CallbackInfo ci)
    {
        if (result.getType() != HitResult.Type.MISS && CONFIG.enableMarkers() && CONFIG.markersSpawnOnProjectileHit() && random.nextDouble() <= CONFIG.markerSpawnChance())
        {
            final MarkerEntity m = new MarkerEntity(level());
            m.setPos(result.getLocation());
            level().addFreshEntity(m);
        }
    }
}
