package n643064.zombie_tactics.neoforge;

import n643064.zombie_tactics.impl.IMain;
import n643064.zombie_tactics.commands.CommandSumZ;

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


@EventBusSubscriber(modid=Main.MOD_ID)
@Mod(Main.MOD_ID)
public class Main implements IMain {
    // not using for now
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister
            .create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);

    public Main(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, NeoForgeConfig.SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        ATTACHMENTS.register(modEventBus);
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandSumZ.register(event.getDispatcher());
    }
}
