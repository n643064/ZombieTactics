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
            canFloat = true,
            hyperClimbing = false,
            jumpBlock = true,
            spawnUnderSun = false,
            canFly = false; // overpowered

    public static double break_speed = 0.2,
            maxHardness = 4.5,
            hardnessMultiplier = 10,
            climbingSpeed = 0.3,
            minDist = 0.2,
            maxDist = 8,
            healAmount = 0,
            aggressiveSpeed = 1,
            attackRange = Math.sqrt(2.04) - 0.6, // from BoundingBox code
            persistenceChance = 0,
            jumpAcceleration = 0.3,
            flySpeed = 0.10;

    public static int attackCooldown = 20,
            maxThreshold = 384,
            blockCost = 5,
            climbLimitTicks = 120,
            followRange = 40,
            accuracy = 1,
            pickupRange = 8;

    // debugging
    public static boolean showNodes = false;

    public static FindTargetType findTargetType = FindTargetType.SIMPLE;
}
