package n643064.zombie_tactics.neoforge;

import n643064.zombie_tactics.common.attachments.MiningData;
import n643064.zombie_tactics.common.commands.CommandSumZ;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

@EventBusSubscriber(modid = Main.MOD_ID)
@Mod(Main.MOD_ID)
public class Main {
    public static final String MOD_ID = "zombie_tactics";
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister
            .create(BuiltInRegistries.ENTITY_TYPE, MOD_ID);
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister
            .create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);

    public static final Supplier<AttachmentType<MiningData>> ZOMBIE_MINING = ATTACHMENTS
            .register("zombie_mining", () -> AttachmentType.builder(MiningData::new).build());

    public Main(IEventBus modEventBus, ModContainer modContainer) {
        ENTITIES.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        ATTACHMENTS.register(modEventBus);
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandSumZ.register(event.getDispatcher());
    }
}
