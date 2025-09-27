package n643064.zombie_tactics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

public record Config(
        boolean affectPiglins,
        boolean targetAnimals,
        int targetAnimalsPriority,
        boolean targetAnimalsVisibilityCheck,
        boolean mineBlocks,
        double miningMinDistance,
        double miningMaxDistance,
        boolean dropBrokenBlocks,
        int miningPriority,
        boolean zombieClimbing,
        double climbingSpeed,
        double miningIncrement,
        double maxHardness,
        double hardnessMultiplier,
        boolean enableMarkers,
        int markerLifetime,
        double markerSpawnChance,
        double markerRange,
        boolean markersSpawnOnDamage,
        boolean markersSpawnOnItemUse,
        boolean markersSpawnOnProjectileHit,
        int markerNavigationPriority,
        List<String> markerSpawningEntities
)
{

    public static Config CONFIG = new Config(
        true,
            true,
            3,
            false,
            true,
            0.2,
            32,
            true,
            1,
            true,
            0.3,
            0.1,
            12,
            5,
            true,
            20,
            0.5,
            96,
            true,
            true,
            true,
            2,
            List.of(
                    "minecraft:player",
                    "minecraft:sheep",
                    "minecraft:chicken",
                    "minecraft:cow",
                    "minecraft:pig",
                    "minecraft:villager"
            )

    );


    public static final HashSet<EntityType<? extends Entity>> MARKER_SPAWNING_ENTITIES = new HashSet<>();

    static void onLoad()
    {
       MARKER_SPAWNING_ENTITIES.clear();
        for (String s : CONFIG.markerSpawningEntities)
        {
            MARKER_SPAWNING_ENTITIES.add(BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(s)));
        }
    }

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();
    static final String CONFIG_PATH = "config" + File.separator + "zombie_tactics.json";

    public static void create() throws IOException
    {
        Path p = Path.of("config");
        if (Files.exists(p))
        {
            if (Files.isDirectory(p))
            {
                FileWriter writer = new FileWriter(CONFIG_PATH);
                writer.write(GSON.toJson(CONFIG));
                writer.flush();
                writer.close();
            }
        } else
        {
            Files.createDirectory(p);
            create();
        }
    }

    public static <T> T read(String path, Class<T> clazz) throws IOException
    {
        final FileReader reader = new FileReader(path);
        T t = GSON.fromJson(reader, clazz);
        reader.close();
        return t;
    }

    public static void setup()
    {
        try
        {
            if (Files.exists(Path.of(CONFIG_PATH)))
            {
                CONFIG = read(CONFIG_PATH, Config.class);
            } else
            {
                create();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
