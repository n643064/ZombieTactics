package n643064.zombie_tactics;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Main.MODID)
public class Main
{
    public static final String MODID = "zombie_tactics";
    public static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, "zombie_tactics");
    public static DeferredHolder<EntityType<? extends Entity>, EntityType<Entity>> MARKER = ENTITIES.register("marker", () -> EntityType.Builder.of(MarkerEntity::new, MobCategory.MISC)
            .noSave()
            .updateInterval(100)
            .sized(0.1f, 0.1f)
            .canSpawnFarFromPlayer()
            .build("zombie_tactics:marker"));


    public Main(IEventBus modEventBus, ModContainer modContainer)
    {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        ENTITIES.register(modEventBus);
        modEventBus.addListener(Main::registerRenderers);
    }


    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        //System.out.println("register renderers");
        event.registerEntityRenderer(MARKER.value(), MarkerRenderer::new);
    }
}
