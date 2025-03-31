package n643064.zombie_tactics.fabric;

import com.google.common.collect.Maps;

import n643064.zombie_tactics.common.IMain;
import n643064.zombie_tactics.common.attachments.MiningData;
import n643064.zombie_tactics.fabric.commands.CommandSumZ;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

// What can I do...
// I don't know that AttachmentType corresponds to the NeoForge's one
// And It shows warning!
public class Main implements ModInitializer, IMain {
    static public HashMap<Integer, MiningData<BlockPos>> ZOMBIE_MINING = Maps.newHashMap();

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT
                .register((dispatcher,
                           access,
                           env) ->
                        CommandSumZ.register(dispatcher));
    }
}
