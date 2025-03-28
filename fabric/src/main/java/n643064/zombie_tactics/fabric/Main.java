package n643064.zombie_tactics.fabric;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;

public class Main implements ModInitializer {
    public static final String MOD_ID = "zombie_tactics";
    public static final ComponentType<Boolean> ZOMBIE_MINING = Registry
            .register(Registries.DATA_COMPONENT_TYPE, Identifier,
                    ComponentType.<Boolean>builder().build());

    @Override
    public void onInitialize() {
    }
}
