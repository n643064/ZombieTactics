package n643064.zombie_tactics.fabric;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.KeybindTranslations;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

public class ModConfig implements ConfigData {
    public boolean mineBlocks = true;
    public boolean targetAnimals = true;
    public boolean targetAnimalsVisibility = false;
    public boolean zombiesClimbing = true;
    public boolean dropBlocks = false;
    public boolean sunSensitive = false;
    public boolean noMercy = true;

    public double increment = 0.2;
    public double maxHardness = 4.5;
    public double hardnessMultiplier = 5;
    public double climbingSpeed = 0.2;
    public double minDist = 0.2;
    public double maxDist = 32;
    public double healAmount = 1;
    public double aggressiveSpeed = 1.8;
    public double attackRange = 0.825;

    public int targetAnimalsPriority = 3;
    public int miningPriority = 1;
    public int attackCooldown = 10;


    void init() {
        final String MOD_CFG = Main.MOD_ID + ".configuration.";
        ConfigBuilder b = ConfigBuilder.create();
        ConfigEntryBuilder entry = b.entryBuilder();
        ConfigCategory animals = b.getOrCreateCategory((Text)new TranslatableTextContent(MOD_CFG + "Animals", "", null));

        animals.addEntry(entry.startBooleanToggle((Text)new TranslatableTextContent(MOD_CFG + "Animals.do_hurt_animals", "", null), true).build());
        TARGET_ANIMALS_PRIORITY = b.comment("Animal targeting priority (lower values mean higher priority). Do not change if you don't know what it is").translation(MOD_CFG + "Animals.hurt_animal_priority").defineInRange("targetAnimalsPriority", 3, 0, Integer.MAX_VALUE);
        TARGET_ANIMALS_VISIBILITY = b.comment("Does animal targeting require line of sight").translation(MOD_CFG + "Animals.hurt_visible_animal").define("targetAnimalsVisibilityCheck", false);

        MINE_BLOCKS = b.comment("Should zombies attempt to break blocks in their way").translation(MOD_CFG + "Mining.do_mine").define("zombiesMineBlocks", true);
        MIN_DISTANCE = b.comment("Min distance for mining").translation(MOD_CFG + "Mining.min_mine_dist").defineInRange("minDistForMining", 0.2, 0, Double.MAX_VALUE);
        MAX_DISTANCE = b.comment("Max distance for mining").translation(MOD_CFG + "Mining.max_mine_dist").defineInRange("maxDistForMining", 32, 0, Double.MAX_VALUE);
        DROP_BROKEN_BLOCKS = b.comment("Should broken blocks be dropped").translation(MOD_CFG + "Mining.drop_blocks").define("dropBrokenBlocks", true);
        MINING_PRIORITY = b.comment("Block breaking goal priority. Do not change if you don't know what it is").translation(MOD_CFG + "Mining.mine_priority").defineInRange("minePriority", 1, 0, Integer.MAX_VALUE);
        MINING_SPEED = b.comment("The amount of mining progress made per tick").translation(MOD_CFG + "Mining.mining_speed").defineInRange("miningSpeed", 0.2, 0, Double.MAX_VALUE);
        MAX_HARDNESS = b.comment("The maximum hardness of targeted blocks. For example, Iron block is 5").translation(MOD_CFG + "Mining.max_hardness").defineInRange("maxHardness", 4.5, 0, Double.MAX_VALUE);
        HARDNESS_MULTIPLIER = b.comment("Target block hardness multiplier, and doesn't affect block selection. Mining progress = hardnessMultiplier * block hardness").translation(MOD_CFG + "Mining.hardness_multiplier").defineInRange("hardnessMultiplier", 5, 0, Double.MAX_VALUE);

        ZOMBIE_CLIMBING = b.comment("Should zombies climb each other on collision").translation(MOD_CFG + "Climbing.do_climb").define("zombiesClimb", true);
        CLIMBING_SPEED = b.comment("Zombie climbing speed").translation(MOD_CFG + "Climbing.climb_speed").defineInRange("zombieClimbingSpeed", 0.3, 0, Double.MAX_VALUE);


        HEAL_AMOUNT = b.comment("The amount of heal when a zombie attacks somewhat").translation(MOD_CFG + "General.heal_amount").defineInRange("healAmount", 1.0, 0, 1024);
        ATTACK_COOLDOWN = b.comment("Interval ticks to attack").translation(MOD_CFG + "General.attack_cooldown").defineInRange("attackCooldown", 10, 1, 1000);
        AGGRESSIVE_SPEED = b.comment("Walk speed when a zombie is mad").translation(MOD_CFG + "General.aggressive_speed").defineInRange("aggressiveSpeed", 1.5, 0.01, 128);
        SUN_SENSITIVE = b.comment("Zombie is sensitive to the sun").translation(MOD_CFG + "General.sun_sensitive").define("sunSensitive", false);
        NO_MERCY = b.comment("Target entity").translation(MOD_CFG + "General.no_mercy").define("noMercy", false);
        ATTACK_RANGE = b.comment("Zombie attack range").translation(MOD_CFG + "General.attack_range").defineInRange("", Math.sqrt(2.04) - 0.6, 0.25, 127.);
    }
}
