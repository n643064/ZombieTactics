package n643064.zombie_tactics.neoforge;

import n643064.zombie_tactics.Config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import org.apache.commons.lang3.tuple.Pair;


@EventBusSubscriber(modid = Main.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NeoForgeConfig {
    private static final Pair<build, ModConfigSpec> BUILDER = new ModConfigSpec.Builder().configure(build::new);
    private static ModConfigSpec.BooleanValue TARGET_ANIMALS;
    private static ModConfigSpec.IntValue TARGET_ANIMALS_PRIORITY;
    private static ModConfigSpec.BooleanValue ATTACK_INVISIBLE;
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
    private static ModConfigSpec.DoubleValue ATTACK_RANGE;
    private static ModConfigSpec.DoubleValue PERSISTENCE_CHANCE;

    static final ModConfigSpec SPEC = BUILDER.getRight();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent ignored) {
        Config.mineBlocks = MINE_BLOCKS.get();
        Config.minDist = MIN_DISTANCE.get();
        Config.maxDist = MAX_DISTANCE.get();
        Config.dropBlocks = DROP_BROKEN_BLOCKS.get();
        Config.miningPriority = MINING_PRIORITY.get();
        Config.targetAnimals = TARGET_ANIMALS.get();
        Config.targetAnimalsPriority = TARGET_ANIMALS_PRIORITY.get();
        Config.attackInvisible = ATTACK_INVISIBLE.get();
        Config.increment = MINING_SPEED.get();
        Config.maxHardness = MAX_HARDNESS.get();
        Config.hardnessMultiplier = HARDNESS_MULTIPLIER.get();
        Config.zombiesClimbing = ZOMBIE_CLIMBING.get();
        Config.climbingSpeed = CLIMBING_SPEED.get();
        Config.healAmount = HEAL_AMOUNT.get();
        Config.attackCooldown = ATTACK_COOLDOWN.get();
        Config.aggressiveSpeed = AGGRESSIVE_SPEED.get();
        Config.sunSensitive = SUN_SENSITIVE.get();
        Config.noMercy = NO_MERCY.get();
        Config.attackRange = ATTACK_RANGE.get();
        Config.persistenceChance = PERSISTENCE_CHANCE.get();
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
            ATTACK_COOLDOWN = b.comment("Interval ticks to attack").translation(MOD_CFG + "General.attack_cooldown").defineInRange("attackCooldown", 20, 1, 1000);
            AGGRESSIVE_SPEED = b.comment("Walk speed when a zombie is mad").translation(MOD_CFG + "General.aggressive_speed").defineInRange("aggressiveSpeed", 1.5, 0.01, 128);
            SUN_SENSITIVE = b.comment("Zombie is sensitive to the sun").translation(MOD_CFG + "General.sun_sensitive").define("sunSensitive", true);
            NO_MERCY = b.comment("Target entity").translation(MOD_CFG + "General.no_mercy").define("noMercy", false);
            ATTACK_RANGE = b.comment("Zombie attack range").translation(MOD_CFG + "General.attack_range").defineInRange("", Math.sqrt(2.04) - 0.6, 0.25, 127.);
            ATTACK_INVISIBLE = b.comment("Does animal targeting require line of sight").translation(MOD_CFG + "General.attack_invisible").define("targetVisibilityCheck", false);
            b.pop();
            b.push("Spawn");
            PERSISTENCE_CHANCE = b.translation(MOD_CFG + "Spawn.persistence_chance").defineInRange("persistenceChance", 0.0, 0, 1);
            b.pop();
        }
    }
}
