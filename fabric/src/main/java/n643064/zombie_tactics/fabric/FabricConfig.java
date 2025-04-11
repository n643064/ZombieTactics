package n643064.zombie_tactics.fabric;

import n643064.zombie_tactics.Config;

import eu.midnightdust.lib.config.MidnightConfig;

// I think MidnightConfig is even better than cloth-config :(
// Anyway, it causes the duplication of language assets
public class FabricConfig extends MidnightConfig {
    public static final String ANIMALS = "Animals";
    public static final String MINING = "Mining";
    public static final String CLIMBING = "Climbing";
    public static final String GENERAL = "General";

    // category shuffled because I sorted it to the types
    // which is better?
    @Entry(category = MINING) public static boolean mineBlocks = true;
    @Entry(category = ANIMALS) public static boolean targetAnimals = true;
    @Entry(category = ANIMALS) public static boolean targetAnimalsVisibility = false;
    @Entry(category = CLIMBING) public static boolean zombiesClimbing = true;
    @Entry(category = MINING) public static boolean dropBlocks = false;
    @Entry(category = GENERAL) public static boolean sunSensitive = false;
    @Entry(category = GENERAL) public static boolean noMercy = Config.noMercy;

    @Entry(category = MINING, min = 0) public static double increment = 0.2;
    @Entry(category = MINING, min = 0) public static double maxHardness = 4.5;
    @Entry(category = MINING, min = 0)  public static double hardnessMultiplier = 5;
    @Entry(category = CLIMBING, min = 0) public static double climbingSpeed = 0.3;
    @Entry(category = MINING, min = 0) public static double minDist = 0.2;
    @Entry(category = MINING, min = 0) public static double maxDist = 32;
    @Entry(category = GENERAL, min = 0, max = 1024) public static double healAmount = 1;
    @Entry(category = GENERAL, min = 0.01, max = 128) public static double aggressiveSpeed = 1.5;
    @Entry(category = GENERAL, min = 0.25, max = 127) public static double attackRange = 0.825;

    @Entry(category = ANIMALS, min = 0) public static int targetAnimalsPriority = 3;
    @Entry(category = MINING, min = 0) public static int miningPriority = 1;
    @Entry(category = GENERAL, min = 1, max = 1000) public static int attackCooldown = 10;

    // fabric fields do nothing without the update of config
    public static void updateConfig() {
        Config.mineBlocks = mineBlocks;
        Config.targetAnimals = targetAnimals;
        Config.targetAnimalsVisibility = targetAnimalsVisibility;
        Config.zombiesClimbing = zombiesClimbing;
        Config.dropBlocks = dropBlocks;
        Config.sunSensitive = sunSensitive;
        Config.noMercy = noMercy;

        Config.increment = increment;
        Config.maxHardness = maxHardness;
        Config.hardnessMultiplier = hardnessMultiplier;
        Config.climbingSpeed = climbingSpeed;
        Config.minDist = minDist;
        Config.maxDist = maxDist;
        Config.healAmount = healAmount;
        Config.aggressiveSpeed = aggressiveSpeed;
        Config.attackRange = attackRange;

        Config.targetAnimalsPriority = targetAnimalsPriority;
        Config.miningPriority = miningPriority;
        Config.attackCooldown = attackCooldown;
    }

    @Override
    public void writeChanges(String modid) {
        super.writeChanges(modid);
        updateConfig();
    }
}
