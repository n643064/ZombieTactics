package n643064.zombie_tactics;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
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
    private static final ModConfigSpec.DoubleValue HEAL_AMOUNT = BUILDER.comment("The amount of heal when a zombie attacks somewhat").defineInRange("healAmount", 1.0, 0, 1024);
    private static final ModConfigSpec.IntValue ATTACK_COOLDOWN = BUILDER.comment("Interval ticks to attack").defineInRange("attackCooldown", 10, 1, 1000);
    private static final ModConfigSpec.DoubleValue AGGRESSIVE_SPEED = BUILDER.comment("Walk speed when a zombie is mad").defineInRange("aggressiveSpeed", 1.5, 0.01, 128);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean
            mineBlocks,
            targetAnimals,
            targetAnimalsVisibility,
            zombiesClimbing,
            dropBlocks;

    public static double
            increment,
            maxHardness,
            hardnessMult,
            climbingSpeed,
            minDist,
            maxDist,
            healAmount,
            aggressiveSpeed;

    public static int
            targetAnimalsPriority,
            miningPriority,
            attackCooldown;

    public static List<Class<? extends LivingEntity>> AttackableTypes;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent ignored)
    {
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
        healAmount = HEAL_AMOUNT.get();
        attackCooldown = ATTACK_COOLDOWN.get();
        aggressiveSpeed = AGGRESSIVE_SPEED.get();

        AttackableTypes = new ArrayList<>();
    }
}
