package n643064.zombie_tactics.mixin;

import n643064.zombie_tactics.goals.FindAllTargetsGoal;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;


@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level {
    @Unique private static int zombie_tactics$duration = 0;

    protected ServerLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
        super(levelData, dimension, registryAccess, dimensionTypeRegistration, profiler, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
    }


    // garbage
    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        ++ zombie_tactics$duration;
        if(zombie_tactics$duration > 10) {
            zombie_tactics$duration = 0;
            while(true) {
                boolean mark = true;
                int idx = 0;
                for(var cp: FindAllTargetsGoal.cache_path) {
                    if(!cp.getA().isAlive()) {
                        mark = false;
                        break;
                    }
                    ++ idx;
                }
                if(mark) break;
                FindAllTargetsGoal.cache_path.remove(idx);
            }
        }
    }
}
