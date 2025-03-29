package n643064.zombie_tactics.fabric;

import n643064.zombie_tactics.fabric.attachments.MiningData;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.util.Identifier;

// What can I do...
// I don't know that AttachmentType corresponds to the NeoForge's one
// And It shows warning!
public class Main implements ModInitializer {
    public static final String MOD_ID = "zombie_tactics";
    public static final AttachmentType<MiningData> ZOMBIE_MINING = AttachmentRegistry
            .create(Identifier.<MiningData>of(""));
    @Override
    public void onInitialize() {
    }
}
