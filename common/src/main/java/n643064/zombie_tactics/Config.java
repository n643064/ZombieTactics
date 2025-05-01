package n643064.zombie_tactics;

import n643064.zombie_tactics.attachments.FindTargetType;

// just define configurations
// balance patch
public class Config {
    public static boolean mineBlocks = true,
            targetAnimals = true,
            zombiesClimbing = true,
            dropBlocks = false,
            sunSensitive = true,
            noMercy = false,
            attackInvisible = false,
            canFloat = false,
            hyperClimbing = false,
            jumpBlock = true;

    public static double increment = 0.2,
            maxHardness = 4.5,
            hardnessMultiplier = 5,
            climbingSpeed = 0.3,
            minDist = 0.2,
            maxDist = 32,
            healAmount = 0,
            aggressiveSpeed = 1,
            attackRange = Math.sqrt(2.04) - 0.6, // from BoundingBox code
            persistenceChance = 0,
            jumpAcceleration = 0.3;

    public static int attackCooldown = 20,
            maxThreshold = 384,
            blockCost = 5,
            climbLimitTicks = 120,
            followRange = 42;

    public static FindTargetType findTargetType = FindTargetType.SIMPLE;
}
