package n643064.zombie_tactics;

import n643064.zombie_tactics.attachments.MiningData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Main.MOD_ID)
public class Main
{
    public static final String MOD_ID = "zombie_tactics";
    public static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.
            create(BuiltInRegistries.ENTITY_TYPE,"zombie_tactics");
    public static AttachmentType<MiningData> ZOMBIE_MINING = AttachmentType.
            builder(MiningData::new).build();

    public Main(IEventBus modEventBus, ModContainer modContainer)
    {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        ENTITIES.register(modEventBus);
    }
}
