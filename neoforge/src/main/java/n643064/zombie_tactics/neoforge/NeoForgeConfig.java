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
    private static ModConfigSpec.BooleanValue ATTACK_INVISIBLE;
    private static ModConfigSpec.BooleanValue MINE_BLOCKS;
    private static ModConfigSpec.DoubleValue MIN_DISTANCE;
    private static ModConfigSpec.DoubleValue MAX_DISTANCE;
    private static ModConfigSpec.BooleanValue DROP_BROKEN_BLOCKS;
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
    private static ModConfigSpec.IntValue MAX_THRESHOLD;
    private static ModConfigSpec.IntValue BLOCK_COST;
    private static ModConfigSpec.BooleanValue CAN_FLOAT;
    private static ModConfigSpec.IntValue CLIMB_LIMIT_TICKS;
    private static ModConfigSpec.DoubleValue JUMP_ACCELERATION;

    static final ModConfigSpec SPEC = BUILDER.getRight();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent ignored) {
        Config.mineBlocks = MINE_BLOCKS.get();
        Config.minDist = MIN_DISTANCE.get();
        Config.maxDist = MAX_DISTANCE.get();
        Config.dropBlocks = DROP_BROKEN_BLOCKS.get();
        Config.targetAnimals = TARGET_ANIMALS.get();
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
        Config.maxThreshold = MAX_THRESHOLD.get();
        Config.blockCost = BLOCK_COST.get();
        Config.canFloat = CAN_FLOAT.get();
        Config.climbLimitTicks = CLIMB_LIMIT_TICKS.get();
        Config.jumpAcceleration = JUMP_ACCELERATION.get();
    }

    /*
        I super hard coded.
        Translation!!
     */
    public static class build {
        static final String MOD_CFG = Main.MOD_ID + ".midnightconfig.";
        public build(ModConfigSpec.Builder b) {
            b.push("Animals");
            TARGET_ANIMALS = b.comment("Should zombies target animals").translation(MOD_CFG + "do_hurt_animals").define("zombiesTargetAnimals", Config.targetAnimals);
            b.pop();
            b.push("Mining");
            MINE_BLOCKS = b.comment("Should zombies attempt to break blocks in their way").translation(MOD_CFG + "do_mine").define("zombiesMineBlocks", Config.mineBlocks);
            MIN_DISTANCE = b.comment("Min distance for mining").translation(MOD_CFG + "min_mine_dist").defineInRange("minDistForMining", Config.minDist, 0, Double.MAX_VALUE);
            MAX_DISTANCE = b.comment("Max distance for mining").translation(MOD_CFG + "max_mine_dist").defineInRange("maxDistForMining", Config.maxDist, 0, Double.MAX_VALUE);
            DROP_BROKEN_BLOCKS = b.comment("Should broken blocks be dropped").translation(MOD_CFG + "drop_blocks").define("dropBrokenBlocks", Config.dropBlocks);
            MINING_SPEED = b.comment("The amount of mining progress made per tick").translation(MOD_CFG + "mining_speed").defineInRange("miningSpeed", Config.increment, 0, Double.MAX_VALUE);
            MAX_HARDNESS = b.comment("The maximum hardness of targeted blocks. For example, Iron block is 5").translation(MOD_CFG + "max_hardness").defineInRange("maxHardness", Config.maxHardness, 0, Double.MAX_VALUE);
            HARDNESS_MULTIPLIER = b.comment("Target block hardness multiplier, and doesn't affect block selection. Mining progress = hardnessMultiplier * block hardness").translation(MOD_CFG + "hardness_multiplier").defineInRange("hardnessMultiplier", Config.hardnessMultiplier, 0, Double.MAX_VALUE);
            b.pop();
            b.push("Climbing");
            ZOMBIE_CLIMBING = b.comment("Should zombies climb each other on collision").translation(MOD_CFG + "do_climb").define("zombiesClimb", Config.zombiesClimbing);
            CLIMBING_SPEED = b.comment("Zombie climbing speed").translation(MOD_CFG + "climb_speed").defineInRange("zombieClimbingSpeed", Config.climbingSpeed, 0, Double.MAX_VALUE);
            CLIMB_LIMIT_TICKS = b.comment("Zombie climbing limit ticks").translation(MOD_CFG + "climb_limit_ticks").defineInRange("climbLimitTicks", Config.climbLimitTicks, 1, Integer.MAX_VALUE);
            b.pop();
            b.push("Spawn");
            PERSISTENCE_CHANCE = b.translation(MOD_CFG + "persistence_chance").defineInRange("persistenceChance", Config.persistenceChance, 0, 1);
            MAX_THRESHOLD = b.translation(MOD_CFG + "max_threshold").defineInRange("maxThreshold", Config.maxThreshold, 0, Integer.MAX_VALUE);
            b.pop();
            b.push("Targeting");
            BLOCK_COST = b.translation(MOD_CFG + "block_cost").defineInRange("blockCost", Config.blockCost, 1, 65536);
            JUMP_ACCELERATION = b.translation(MOD_CFG + "jump_acceleration").defineInRange("jumpAcceleration", Config.jumpAcceleration, 0, 128);
            b.pop();
            b.push("General");
            HEAL_AMOUNT = b.comment("The amount of heal when a zombie attacks somewhat").translation(MOD_CFG + "heal_amount").defineInRange("healAmount", Config.healAmount, 0, 1024);
            ATTACK_COOLDOWN = b.comment("Interval ticks to attack").translation(MOD_CFG + "attack_cooldown").defineInRange("attackCooldown", Config.attackCooldown, 1, 1000);
            AGGRESSIVE_SPEED = b.comment("Walk speed when a zombie is mad").translation(MOD_CFG + "aggressive_speed").defineInRange("aggressiveSpeed", Config.aggressiveSpeed, 0.01, 128);
            SUN_SENSITIVE = b.comment("Zombie is sensitive to the sun").translation(MOD_CFG + "sun_sensitive").define("sunSensitive", Config.sunSensitive);
            NO_MERCY = b.comment("Target entity").translation(MOD_CFG + "no_mercy").define("noMercy", Config.noMercy);
            ATTACK_RANGE = b.comment("Zombie attack range").translation(MOD_CFG + "attack_range").defineInRange("", Config.attackRange, 0.25, 127.);
            ATTACK_INVISIBLE = b.comment("Does animal targeting require line of sight").translation(MOD_CFG + "attack_invisible").define("targetVisibilityCheck", Config.attackInvisible);
            CAN_FLOAT = b.translation(MOD_CFG + "can_float").define("canFloat", Config.canFloat);
            b.pop();
        }
    }
}
