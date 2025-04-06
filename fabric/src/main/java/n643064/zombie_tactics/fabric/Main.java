package n643064.zombie_tactics.fabric;

import n643064.zombie_tactics.common.IMain;
import n643064.zombie_tactics.fabric.attachments.MiningData;
import n643064.zombie_tactics.fabric.commands.CommandSumZ;

import net.minecraft.util.Identifier;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import eu.midnightdust.lib.config.MidnightConfig;

// What can I do...
// I don't know that AttachmentType corresponds to the NeoForge's one
// And It shows warning!
public class Main implements ModInitializer, IMain {
    public static AttachmentType<MiningData> ZOMBIE_MINING = AttachmentRegistry
            .create(Identifier.of(MOD_ID, "zombie_mining"));

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, access, env) -> CommandSumZ.register(dispatcher)
        );
        MidnightConfig.init(MOD_ID, ModConfig.class);
    }
}
