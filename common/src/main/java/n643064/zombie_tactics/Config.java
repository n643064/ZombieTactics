package n643064.zombie_tactics;


// just define configurations
public class Config {
    public static boolean mineBlocks = true,
            targetAnimals = true,
            zombiesClimbing = true,
            dropBlocks = false,
            sunSensitive = false,
            noMercy = false,
            attackInvisible = false;

    public static double increment = 0.2,
            maxHardness = 4.5,
            hardnessMultiplier = 5,
            climbingSpeed = 0.3,
            minDist = 0.2,
            maxDist = 32,
            healAmount = 1,
            aggressiveSpeed = 1.5,
            attackRange = 0.825,
            persistenceChance = 0;

    public static int targetAnimalsPriority = 3,
            miningPriority = 1,
            attackCooldown = 10;
}
