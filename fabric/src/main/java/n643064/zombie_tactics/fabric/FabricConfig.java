package n643064.zombie_tactics.fabric;

import n643064.zombie_tactics.Config;
import n643064.zombie_tactics.attachments.FindTargetType;

import eu.midnightdust.lib.config.MidnightConfig;


// I think MidnightConfig is even better than cloth-config :(
// Anyway, it causes the duplication of language assets
public class FabricConfig extends MidnightConfig {
    public static final String MINING = "Mining";
    public static final String CLIMBING = "Climbing";
    public static final String SPAWN = "Spawn";
    public static final String TARGETING = "Targeting";
    public static final String GENERAL = "General";
    public static final String FLYING = "Flying";
    public static final String OPTIMIZE = "Optimize";
    public static final String DEBUG = "Debug";

    // category shuffled because I sorted it to the types
    // which is better?
    @Entry(category=MINING) public static boolean do_mine = Config.mineBlocks;
    @Entry(category=MINING, min=0) public static double mining_speed = Config.break_speed;
    @Entry(category=MINING, min=0) public static double max_hardness = Config.maxHardness;
    @Entry(category=MINING) public static boolean drop_blocks = Config.dropBlocks;
    @Entry(category=MINING, min=0) public static double min_mine_dist = Config.minDist;
    @Entry(category=MINING, min=0) public static double max_mine_dist = Config.maxDist;
    @Entry(category=MINING, min=0)  public static double hardness_multiplier = Config.hardnessMultiplier;

    @Entry(category=CLIMBING) public static boolean do_climb = Config.zombiesClimbing;
    @Entry(category=CLIMBING, min=1, max=Integer.MAX_VALUE) public static int climb_limit_ticks = Config.climbLimitTicks;
    @Entry(category=CLIMBING, min=0) public static double climb_speed = Config.climbingSpeed;
    @Entry(category=CLIMBING) public static boolean hyper_climbing = Config.hyperClimbing;

    @Entry(category=SPAWN) public static boolean spawn_under_sun = Config.spawnUnderSun;
    @Entry(category=SPAWN, min=0, max=Integer.MAX_VALUE) public static int max_threshold = Config.maxThreshold;
    @Entry(category=SPAWN, min=0, max=1) public static double persistence_chance = Config.persistenceChance;

    @Entry(category=TARGETING) public static boolean do_hurt_animals = Config.targetAnimals;
    @Entry(category=TARGETING) public static FindTargetType find_target_type = Config.findTargetType;
    @Entry(category=TARGETING, min=1, max=65536) public static int block_cost = Config.blockCost;
    @Entry(category=TARGETING, min=1, max=128) public static int follow_range = Config.followRange;
    @Entry(category=TARGETING, min=0.25, max=127) public static double attack_range = Config.attackRange;
    @Entry(category=TARGETING) public static boolean attack_invisible = Config.attackInvisible;

    @Entry(category=OPTIMIZE, min=0, max=16) public static int path_accuracy = Config.accuracy;

    @Entry(category=FLYING) public static boolean can_fly = Config.canFly;
    @Entry(category=FLYING, min=0, max=32) public static double fly_speed = Config.flySpeed;

    @Entry(category=GENERAL, min=0, max=1024) public static double heal_amount = Config.healAmount;
    @Entry(category=GENERAL, min=0.01, max=128) public static double aggressive_speed = Config.aggressiveSpeed;
    @Entry(category=GENERAL, min=1, max=1000) public static int attack_cooldown = Config.attackCooldown;
    @Entry(category=GENERAL) public static boolean sun_sensitive = Config.sunSensitive;
    @Entry(category=GENERAL) public static boolean no_mercy = Config.noMercy;
    @Entry(category=GENERAL) public static boolean can_float = Config.canFloat;
    @Entry(category=GENERAL) public static boolean jump_block = Config.jumpBlock;
    @Entry(category=GENERAL, min=0, max=128) public static double jump_acceleration = Config.jumpAcceleration;
    @Entry(category=GENERAL, min=0, max=128) public static int pickup_range = Config.pickupRange;


    // debugging
    @Entry(category=DEBUG) public static boolean show_nodes = Config.showNodes;

    // fabric fields do nothing without the update of config
    public static void updateConfig() {
        Config.mineBlocks = do_mine;
        Config.targetAnimals = do_hurt_animals;
        Config.attackInvisible = attack_invisible;
        Config.zombiesClimbing = do_climb;
        Config.dropBlocks = drop_blocks;
        Config.sunSensitive = sun_sensitive;
        Config.noMercy = no_mercy;
        Config.canFloat = can_float;
        Config.hyperClimbing = hyper_climbing;
        Config.jumpBlock = jump_block;
        Config.spawnUnderSun = spawn_under_sun;
        Config.canFly = can_fly;
        Config.break_speed = mining_speed;
        Config.maxHardness = max_hardness;
        Config.hardnessMultiplier = hardness_multiplier;
        Config.climbingSpeed = climb_speed;
        Config.minDist = min_mine_dist;
        Config.maxDist = max_mine_dist;
        Config.healAmount = heal_amount;
        Config.aggressiveSpeed = aggressive_speed;
        Config.attackRange = attack_range;
        Config.persistenceChance = persistence_chance;
        Config.jumpAcceleration = jump_acceleration;
        Config.flySpeed = fly_speed;
        Config.attackCooldown = attack_cooldown;
        Config.maxThreshold = max_threshold;
        Config.blockCost = block_cost;
        Config.climbLimitTicks = climb_limit_ticks;
        Config.followRange = follow_range;
        Config.findTargetType = find_target_type;
        Config.showNodes = show_nodes;
        Config.accuracy = path_accuracy;
        Config.pickupRange = pickup_range;
    }

    @Override
    public void writeChanges(String mod_id) {
        super.writeChanges(mod_id);
        updateConfig();
    }
}
