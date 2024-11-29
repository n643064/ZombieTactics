package n643064.zombie_tactics;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    private static final ModConfigSpec.BooleanValue AFFECT_PIGLINS = BUILDER.comment("Should the changes be applied to zombified piglins").define("affectZombifiedPiglins", false);
    private static final ModConfigSpec.BooleanValue TARGET_ANIMALS = BUILDER.comment("Should zombies target animals").define("zombiesTargetAnimals", true);
    private static final ModConfigSpec.IntValue TARGET_ANIMALS_PRIORITY = BUILDER.comment("Animal targeting priority (lower values mean higher priority)").defineInRange("targetAnimalsPriority", 3, 0, Integer.MAX_VALUE);
    private static final ModConfigSpec.BooleanValue TARGET_ANIMALS_VISIBILITY = BUILDER.comment("Does animal targeting require line of sight").define("targetAnimalsVisibilityCheck", false);
    private static final ModConfigSpec.BooleanValue MINE_BLOCKS = BUILDER.comment("Should zombies attempt to break blocks in their way").define("zombiesMineBlocks", true);
    private static final ModConfigSpec.DoubleValue MIN_DISTANCE = BUILDER.comment("Min distance to target entity for mining").defineInRange("minDistForMining", 0.2, 0, Double.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue MAX_DISTANCE = BUILDER.comment("Max distance to target entity for mining").defineInRange("maxDistForMining", 32, 0, Double.MAX_VALUE);
    private static final ModConfigSpec.BooleanValue DROP_BROKEN_BLOCKS = BUILDER.comment("Should broken blocks be dropped").define("dropBrokenBlocks", true);
    private static final ModConfigSpec.IntValue MINING_PRIORITY = BUILDER.comment("Block breaking goal priority").defineInRange("minePriority", 1, 0, Integer.MAX_VALUE);
    private static final ModConfigSpec.BooleanValue ZOMBIE_CLIMBING = BUILDER.comment("Should zombies climb each other on collision").define("zombiesClimb", true);
    private static final ModConfigSpec.DoubleValue CLIMBING_SPEED = BUILDER.comment("Zombie climbing speed").defineInRange("zombieClimbingSpeed", 0.3, 0, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue MINING_INCREMENT = BUILDER.comment("The amount of mining progress made per tick").defineInRange("miningIncrement", 0.1, 0, Double.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue MAX_HARDNESS = BUILDER.comment("The maximal hardness of targeted blocks").defineInRange("maxHardness", 12, 0, Double.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue HARDNESS_MULTIPLIER = BUILDER.comment("Target block hardness multiplier, doesn't affect block selection").defineInRange("hardnessMultiplier", 5, 0, Double.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean affectPiglins, mineBlocks, targetAnimals, targetAnimalsVisibility, zombiesClimbing, dropBlocks;
    public static double increment, maxHardness, hardnessMult, climbingSpeed, minDist, maxDist;
    public static int targetAnimalsPriority, miningPriority;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        affectPiglins = AFFECT_PIGLINS.get();
        mineBlocks = MINE_BLOCKS.get();
        minDist = MIN_DISTANCE.get();
        maxDist = MAX_DISTANCE.get();
        dropBlocks = DROP_BROKEN_BLOCKS.get();
        miningPriority = MINING_PRIORITY.get();
        targetAnimals = TARGET_ANIMALS.get();
        targetAnimalsPriority = TARGET_ANIMALS_PRIORITY.get();
        targetAnimalsVisibility = TARGET_ANIMALS_VISIBILITY.get();
        increment = MINING_INCREMENT.get();
        maxHardness = MAX_HARDNESS.get();
        hardnessMult = HARDNESS_MULTIPLIER.get();
        zombiesClimbing = ZOMBIE_CLIMBING.get();
        climbingSpeed = CLIMBING_SPEED.get();
    }
}
