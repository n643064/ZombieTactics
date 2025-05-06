package n643064.zombie_tactics.neoforge;

import n643064.zombie_tactics.Config;
import n643064.zombie_tactics.attachments.FindTargetType;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import org.apache.commons.lang3.tuple.Pair;


@EventBusSubscriber(modid=Main.MOD_ID, bus=EventBusSubscriber.Bus.MOD)
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
    private static ModConfigSpec.BooleanValue HYPER_CLIMBING;
    private static ModConfigSpec.BooleanValue JUMP_BLOCK;
    private static ModConfigSpec.IntValue FOLLOW_RANGE;
    private static ModConfigSpec.EnumValue<FindTargetType> TARGET_TYPE;
    private static ModConfigSpec.BooleanValue SPAWN_UNDER_SUN;
    private static ModConfigSpec.BooleanValue CAN_FLY;
    private static ModConfigSpec.DoubleValue FLY_SPEED;
    private static ModConfigSpec.IntValue PATH_ACCURACY;
    private static ModConfigSpec.IntValue PICKUP_RANGE;

    private static ModConfigSpec.BooleanValue SHOW_NODES;

    static final ModConfigSpec SPEC = BUILDER.getRight();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent ignored) {
        Config.mineBlocks = MINE_BLOCKS.get();
        Config.minDist = MIN_DISTANCE.get();
        Config.maxDist = MAX_DISTANCE.get();
        Config.dropBlocks = DROP_BROKEN_BLOCKS.get();
        Config.targetAnimals = TARGET_ANIMALS.get();
        Config.attackInvisible = ATTACK_INVISIBLE.get();
        Config.break_speed = MINING_SPEED.get();
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
        Config.hyperClimbing = HYPER_CLIMBING.get();
        Config.jumpBlock = JUMP_BLOCK.get();
        Config.followRange = FOLLOW_RANGE.get();
        Config.findTargetType = TARGET_TYPE.get();
        Config.spawnUnderSun = SPAWN_UNDER_SUN.get();
        Config.canFly = CAN_FLY.get();
        Config.flySpeed = FLY_SPEED.get();
        Config.showNodes = SHOW_NODES.get();
        Config.accuracy = PATH_ACCURACY.get();
        Config.pickupRange = PICKUP_RANGE.get();
    }

    /*
        I super hard coded.
        Translation!!
     */
    public static class build {
        static final String MOD_CFG = Main.MOD_ID + ".midnightconfig.";
        public build(ModConfigSpec.Builder b) {
            b.push("Mining");
            MINE_BLOCKS = b.comment("Should zombies attempt to break blocks in their way").translation(MOD_CFG + "do_mine").define("zombiesMineBlocks", Config.mineBlocks);
            MINING_SPEED = b.comment("The amount of mining progress made per tick").translation(MOD_CFG + "mining_speed").defineInRange("miningSpeed", Config.break_speed, 0, Double.MAX_VALUE);
            MIN_DISTANCE = b.comment("Min distance for mining").translation(MOD_CFG + "min_mine_dist").defineInRange("minDistForMining", Config.minDist, 0, Double.MAX_VALUE);
            MAX_DISTANCE = b.comment("Max distance for mining").translation(MOD_CFG + "max_mine_dist").defineInRange("maxDistForMining", Config.maxDist, 0, Double.MAX_VALUE);
            MAX_HARDNESS = b.comment("The maximum hardness of targeted blocks. For example, Iron block is 5").translation(MOD_CFG + "max_hardness").defineInRange("maxHardness", Config.maxHardness, 0, Double.MAX_VALUE);
            DROP_BROKEN_BLOCKS = b.comment("Should broken blocks be dropped").translation(MOD_CFG + "drop_blocks").define("dropBrokenBlocks", Config.dropBlocks);
            HARDNESS_MULTIPLIER = b.comment("Target block hardness multiplier, and doesn't affect block selection. Mining progress = hardnessMultiplier * block hardness").translation(MOD_CFG + "hardness_multiplier").defineInRange("hardnessMultiplier", Config.hardnessMultiplier, 0, Double.MAX_VALUE);
            b.pop();
            b.push("Climbing");
            ZOMBIE_CLIMBING = b.comment("Should zombies climb each other on collision").translation(MOD_CFG + "do_climb").define("zombiesClimb", Config.zombiesClimbing);
            CLIMBING_SPEED = b.comment("Zombie climbing speed").translation(MOD_CFG + "climb_speed").defineInRange("zombieClimbingSpeed", Config.climbingSpeed, 0, Double.MAX_VALUE);
            CLIMB_LIMIT_TICKS = b.comment("Zombie climbing limit ticks").translation(MOD_CFG + "climb_limit_ticks").defineInRange("climbLimitTicks", Config.climbLimitTicks, 1, Integer.MAX_VALUE);
            HYPER_CLIMBING = b.translation(MOD_CFG + "hyper_climbing").define("hyperClimbing", Config.hyperClimbing);
            b.pop();
            b.push("Spawn");
            PERSISTENCE_CHANCE = b.translation(MOD_CFG + "persistence_chance").defineInRange("persistenceChance", Config.persistenceChance, 0, 1);
            MAX_THRESHOLD = b.translation(MOD_CFG + "max_threshold").defineInRange("maxThreshold", Config.maxThreshold, 0, Integer.MAX_VALUE);
            SPAWN_UNDER_SUN = b.translation(MOD_CFG + "spawn_under_sun").define("spawnUnderSun", Config.spawnUnderSun);
            b.pop();
            b.push("Targeting");
            TARGET_ANIMALS = b.comment("Should zombies target animals").translation(MOD_CFG + "do_hurt_animals").define("zombiesTargetAnimals", Config.targetAnimals);
            BLOCK_COST = b.translation(MOD_CFG + "block_cost").defineInRange("blockCost", Config.blockCost, 1, 65536);
            FOLLOW_RANGE = b.translation(MOD_CFG + "follow_range").defineInRange("followRange", Config.followRange, 1, 128);
            TARGET_TYPE = b.translation(MOD_CFG + "find_target_type").defineEnum("findTargetType", Config.findTargetType);
            ATTACK_RANGE = b.comment("Zombie attack range").translation(MOD_CFG + "attack_range").defineInRange("", Config.attackRange, 0.25, 127.);
            ATTACK_INVISIBLE = b.comment("Does animal targeting require line of sight").translation(MOD_CFG + "attack_invisible").define("targetVisibilityCheck", Config.attackInvisible);
            b.pop();
            b.push("Optimize");
            PATH_ACCURACY = b.translation(MOD_CFG + "accuracy").defineInRange("pathAccuracy", Config.accuracy, 0, 95);
            b.pop();
            b.push("Flying");
            CAN_FLY = b.translation(MOD_CFG + "can_fly").define("canFly", Config.canFly);
            FLY_SPEED = b.translation(MOD_CFG + "fly_speed").defineInRange("flySpeed", Config.flySpeed, 0, 32);
            b.pop();
            b.push("General");
            HEAL_AMOUNT = b.comment("The amount of heal when a zombie attacks somewhat").translation(MOD_CFG + "heal_amount").defineInRange("healAmount", Config.healAmount, 0, 1024);
            ATTACK_COOLDOWN = b.comment("Interval ticks to attack").translation(MOD_CFG + "attack_cooldown").defineInRange("attackCooldown", Config.attackCooldown, 1, 1000);
            AGGRESSIVE_SPEED = b.comment("Walk speed when a zombie is mad").translation(MOD_CFG + "aggressive_speed").defineInRange("aggressiveSpeed", Config.aggressiveSpeed, 0.01, 128);
            SUN_SENSITIVE = b.comment("Zombie is sensitive to the sun").translation(MOD_CFG + "sun_sensitive").define("sunSensitive", Config.sunSensitive);
            NO_MERCY = b.comment("Target entity").translation(MOD_CFG + "no_mercy").define("noMercy", Config.noMercy);
            CAN_FLOAT = b.translation(MOD_CFG + "can_float").define("canFloat", Config.canFloat);
            JUMP_ACCELERATION = b.translation(MOD_CFG + "jump_acceleration").defineInRange("jumpAcceleration", Config.jumpAcceleration, 0, 128);
            JUMP_BLOCK = b.translation(MOD_CFG + "jump_block").define("jumpBlock", Config.jumpBlock);
            PICKUP_RANGE = b.translation(MOD_CFG + "pickup_range").defineInRange("pickupRange", Config.pickupRange, 0, 128);
            b.pop();
            b.push("Debug");
            SHOW_NODES = b.define("showNodes", Config.showNodes);
            b.pop();
        }
    }
}
