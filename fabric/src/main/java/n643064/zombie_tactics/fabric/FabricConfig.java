package n643064.zombie_tactics.fabric;

import n643064.zombie_tactics.Config;

import eu.midnightdust.lib.config.MidnightConfig;


// I think MidnightConfig is even better than cloth-config :(
// Anyway, it causes the duplication of language assets
public class FabricConfig extends MidnightConfig {
    public static final String ANIMALS = "Animals";
    public static final String MINING = "Mining";
    public static final String CLIMBING = "Climbing";
    public static final String SPAWN = "Spawn";
    public static final String TARGETING = "Targeting";
    public static final String GENERAL = "General";

    // category shuffled because I sorted it to the types
    // which is better?
    @Entry(category=MINING) public static boolean do_mine = Config.mineBlocks;
    @Entry(category=ANIMALS) public static boolean do_hurt_animals = Config.targetAnimals;
    @Entry(category=GENERAL) public static boolean attack_invisible = Config.attackInvisible;
    @Entry(category=CLIMBING) public static boolean do_climb = Config.zombiesClimbing;
    @Entry(category=MINING) public static boolean drop_blocks = Config.dropBlocks;
    @Entry(category=GENERAL) public static boolean sun_sensitive = Config.sunSensitive;
    @Entry(category=GENERAL) public static boolean no_mercy = Config.noMercy;
    @Entry(category=GENERAL) public static boolean can_float = Config.canFloat;
    @Entry(category=CLIMBING) public static boolean jump_block = Config.jumpBlock;
    @Entry(category=CLIMBING) public static boolean hyper_climbing = Config.hyperClimbing;

    @Entry(category=MINING, min=0) public static double mining_speed = Config.increment;
    @Entry(category=MINING, min=0) public static double max_hardness = Config.maxHardness;
    @Entry(category=MINING, min=0)  public static double hardness_multiplier = Config.hardnessMultiplier;
    @Entry(category=CLIMBING, min=0) public static double climb_speed = Config.climbingSpeed;
    @Entry(category=MINING, min=0) public static double min_mine_dist = Config.minDist;
    @Entry(category=MINING, min=0) public static double max_mine_dist = Config.maxDist;
    @Entry(category=GENERAL, min=0, max=1024) public static double heal_amount = Config.healAmount;
    @Entry(category=GENERAL, min=0.01, max=128) public static double aggressive_speed = Config.aggressiveSpeed;
    @Entry(category=GENERAL, min=0.25, max=127) public static double attack_range = Config.attackRange;
    @Entry(category=SPAWN, min=0, max=1) public static double persistence_chance = Config.persistenceChance;
    @Entry(category=GENERAL, min=0, max=128) public static double jump_acceleration = Config.jumpAcceleration;

    @Entry(category=GENERAL, min=1, max=1000) public static int attack_cooldown = Config.attackCooldown;
    @Entry(category=SPAWN, min=0, max=Integer.MAX_VALUE) public static int max_threshold = Config.maxThreshold;
    @Entry(category=TARGETING, min=1, max=65536) public static int block_cost = Config.blockCost;
    @Entry(category=CLIMBING, min=1, max=Integer.MAX_VALUE) public static int climb_limit_ticks = Config.climbLimitTicks;
    @Entry(category=TARGETING, min=1, max=Integer.MAX_VALUE) public static int follow_distance = Config.followRange;

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

        Config.increment = mining_speed;
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

        Config.attackCooldown = attack_cooldown;
        Config.maxThreshold = max_threshold;
        Config.blockCost = block_cost;
        Config.climbLimitTicks = climb_limit_ticks;
        Config.followRange = follow_distance;
    }

    @Override
    public void writeChanges(String modid) {
        super.writeChanges(modid);
        updateConfig();
    }
}
