package n643064.zombie_tactics;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import org.apache.commons.lang3.tuple.Pair;

@EventBusSubscriber(modid = Main.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final Pair<build, ModConfigSpec> BUILDER = new ModConfigSpec.Builder().configure(build::new);
    private static ModConfigSpec.BooleanValue TARGET_ANIMALS;
    private static ModConfigSpec.IntValue TARGET_ANIMALS_PRIORITY;
    private static ModConfigSpec.BooleanValue TARGET_ANIMALS_VISIBILITY;
    private static ModConfigSpec.BooleanValue MINE_BLOCKS;
    private static ModConfigSpec.DoubleValue MIN_DISTANCE;
    private static ModConfigSpec.DoubleValue MAX_DISTANCE;
    private static ModConfigSpec.BooleanValue DROP_BROKEN_BLOCKS;
    private static ModConfigSpec.IntValue MINING_PRIORITY;
    private static ModConfigSpec.BooleanValue ZOMBIE_CLIMBING;
    private static ModConfigSpec.DoubleValue CLIMBING_SPEED;
    private static ModConfigSpec.DoubleValue MINING_SPEED;
    private static ModConfigSpec.DoubleValue MAX_HARDNESS;
    private static ModConfigSpec.DoubleValue HARDNESS_MULTIPLIER;
    private static ModConfigSpec.DoubleValue HEAL_AMOUNT;
    private static ModConfigSpec.IntValue ATTACK_COOLDOWN;
    private static ModConfigSpec.DoubleValue AGGRESSIVE_SPEED;
    private static ModConfigSpec.BooleanValue SUN_SENSITIVE;
    private static ModConfigSpec.BooleanValue NO_MERCY;

    static final ModConfigSpec SPEC = BUILDER.getRight();

    public static boolean mineBlocks,
            targetAnimals,
            targetAnimalsVisibility,
            zombiesClimbing,
            dropBlocks,
            sunSensitive,
            noMercy;

    public static double increment,
            maxHardness,
            hardnessMultiplier,
            climbingSpeed,
            minDist,
            maxDist,
            healAmount,
            aggressiveSpeed;

    public static int targetAnimalsPriority,
            miningPriority,
            attackCooldown;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent ignored) {
        mineBlocks = MINE_BLOCKS.get();
        minDist = MIN_DISTANCE.get();
        maxDist = MAX_DISTANCE.get();
        dropBlocks = DROP_BROKEN_BLOCKS.get();
        miningPriority = MINING_PRIORITY.get();
        targetAnimals = TARGET_ANIMALS.get();
        targetAnimalsPriority = TARGET_ANIMALS_PRIORITY.get();
        targetAnimalsVisibility = TARGET_ANIMALS_VISIBILITY.get();
        increment = MINING_SPEED.get();
        maxHardness = MAX_HARDNESS.get();
        hardnessMultiplier = HARDNESS_MULTIPLIER.get();
        zombiesClimbing = ZOMBIE_CLIMBING.get();
        climbingSpeed = CLIMBING_SPEED.get();
        healAmount = HEAL_AMOUNT.get();
        attackCooldown = ATTACK_COOLDOWN.get();
        aggressiveSpeed = AGGRESSIVE_SPEED.get();
        sunSensitive = SUN_SENSITIVE.get();
        noMercy = NO_MERCY.get();
    }

    /*
        I super hard coded.
        Translation!!
     */
    public static class build {
        static final String MOD_CFG = Main.MOD_ID + ".configuration.";
        public build(ModConfigSpec.Builder b) {
            b.push("Animals");
            TARGET_ANIMALS = b.comment("Should zombies target animals").translation(MOD_CFG + "Animals.do_hurt_animals").define("zombiesTargetAnimals", true);
            TARGET_ANIMALS_PRIORITY = b.comment("Animal targeting priority (lower values mean higher priority). Do not change if you don't know what it is").translation(MOD_CFG + "Animals.hurt_animal_priority").defineInRange("targetAnimalsPriority", 3, 0, Integer.MAX_VALUE);
            TARGET_ANIMALS_VISIBILITY = b.comment("Does animal targeting require line of sight").translation(MOD_CFG + "Animals.hurt_visible_animal").define("targetAnimalsVisibilityCheck", false);
            b.pop();
            b.push("Mining");
            MINE_BLOCKS = b.comment("Should zombies attempt to break blocks in their way").translation(MOD_CFG + "Mining.do_mine").define("zombiesMineBlocks", true);
            MIN_DISTANCE = b.comment("Min distance for mining").translation(MOD_CFG + "Mining.min_mine_dist").defineInRange("minDistForMining", 0.2, 0, Double.MAX_VALUE);
            MAX_DISTANCE = b.comment("Max distance for mining").translation(MOD_CFG + "Mining.max_mine_dist").defineInRange("maxDistForMining", 32, 0, Double.MAX_VALUE);
            DROP_BROKEN_BLOCKS = b.comment("Should broken blocks be dropped").translation(MOD_CFG + "Mining.drop_blocks").define("dropBrokenBlocks", true);
            MINING_PRIORITY = b.comment("Block breaking goal priority. Do not change if you don't know what it is").translation(MOD_CFG + "Mining.mine_priority").defineInRange("minePriority", 1, 0, Integer.MAX_VALUE);
            MINING_SPEED = b.comment("The amount of mining progress made per tick").translation(MOD_CFG + "Mining.mining_speed").defineInRange("miningSpeed", 0.2, 0, Double.MAX_VALUE);
            MAX_HARDNESS = b.comment("The maximum hardness of targeted blocks. For example, Iron block is 5").translation(MOD_CFG + "Mining.max_hardness").defineInRange("maxHardness", 4.5, 0, Double.MAX_VALUE);
            HARDNESS_MULTIPLIER = b.comment("Target block hardness multiplier, and doesn't affect block selection. Mining progress = hardnessMultiplier * block hardness").translation(MOD_CFG + "Mining.hardness_multiplier").defineInRange("hardnessMultiplier", 5, 0, Double.MAX_VALUE);
            b.pop();
            b.push("Climbing");
            ZOMBIE_CLIMBING = b.comment("Should zombies climb each other on collision").translation(MOD_CFG + "Climbing.do_climb").define("zombiesClimb", true);
            CLIMBING_SPEED = b.comment("Zombie climbing speed").translation(MOD_CFG + "Climbing.climb_speed").defineInRange("zombieClimbingSpeed", 0.3, 0, Double.MAX_VALUE);
            b.pop();
            b.push("General");
            HEAL_AMOUNT = b.comment("The amount of heal when a zombie attacks somewhat").translation(MOD_CFG + "General.heal_amount").defineInRange("healAmount", 1.0, 0, 1024);
            ATTACK_COOLDOWN = b.comment("Interval ticks to attack").translation(MOD_CFG + "General.attack_cooldown").defineInRange("attackCooldown", 10, 1, 1000);
            AGGRESSIVE_SPEED = b.comment("Walk speed when a zombie is mad").translation(MOD_CFG + "General.aggressive_speed").defineInRange("aggressiveSpeed", 1.5, 0.01, 128);
            SUN_SENSITIVE = b.comment("Zombie is sensitive to the sun").translation(MOD_CFG + "General.sun_sensitive").define("sunSensitive", false);
            NO_MERCY = b.comment("Target entity").translation(MOD_CFG + "General.no_mercy").define("noMercy", false);
            b.pop();
        }
    }
}
