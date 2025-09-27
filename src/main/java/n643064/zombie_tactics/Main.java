package n643064.zombie_tactics;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class Main implements ModInitializer
{
    public static final String MODID = "zombie_tactics";
    public static EntityType<Entity> MARKER;

    @Override
    public void onInitialize()
    {
        Config.setup();

        ServerWorldEvents.LOAD.register((server, level) -> Config.onLoad());
        MARKER = Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MODID, "marker"),
                EntityType.Builder.of(MarkerEntity::new, MobCategory.MISC)
                        .noSave()
                        .updateInterval(100)
                        .sized(0.1f, 0.1f)
                        .canSpawnFarFromPlayer()
                        .build("zombie_tactics:marker"));


    }
}
