package n643064.zombie_tactics.mixin;

import n643064.zombie_tactics.Config;
import n643064.zombie_tactics.MarkerEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static n643064.zombie_tactics.Config.CONFIG;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
    public LivingEntityMixin(EntityType<?> entityType, Level level)
    {
        super(entityType, level);
    }

    @Inject(method = "hurt", at = @At("TAIL"))
    private void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
    {
        if (CONFIG.enableMarkers() && CONFIG.markersSpawnOnDamage() && Config.MARKER_SPAWNING_ENTITIES.contains(getType()) && amount > 0.5f  && random.nextDouble() <= CONFIG.markerSpawnChance())
        {
            final MarkerEntity m = new MarkerEntity(level());
            m.setPos(this.getPosition(0));
            level().addFreshEntity(m);
        }
    }

    @Inject(method = "completeUsingItem", at = @At("TAIL"))
    private void onUseItem(CallbackInfo ci)
    {
        if (CONFIG.enableMarkers() && CONFIG.markersSpawnOnItemUse() && Config.MARKER_SPAWNING_ENTITIES.contains(getType()) && random.nextDouble() <= CONFIG.markerSpawnChance())
        {
            final MarkerEntity m = new MarkerEntity(level());
            m.setPos(this.getPosition(0));
            level().addFreshEntity(m);
        }
    }
}
