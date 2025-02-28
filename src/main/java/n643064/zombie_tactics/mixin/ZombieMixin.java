package n643064.zombie_tactics.mixin;

import n643064.zombie_tactics.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Zombie.class)
public abstract class ZombieMixin extends Monster implements IMarkerFollower
{
    @Shadow public abstract boolean canBreakDoors();
    @Unique private MarkerEntity zombieTactics$marker = null;

    @Override @Nullable
    public MarkerEntity zombieTactics$getTargetMarker()
    {
        return zombieTactics$marker;
    }

    @Override
    public void zombieTactics$setTargetMarker(@Nullable MarkerEntity marker)
    {
        zombieTactics$marker = marker;
    }
    protected ZombieMixin(EntityType<? extends Zombie> entityType, Level level)
    {
        super(entityType, level);
    }

    /**
     * Force Object casting is required to load Mixin correctly, but linter warns those.
     * By using SuppressWarnings, highlights can be disabled.
     *
     */
    @SuppressWarnings("all")
    @Overwrite
    protected void addBehaviourGoals()
    {
        this.goalSelector.addGoal(1, new ZombieAttackGoal((Zombie)(Object)this,
                1.0, true));

        if (Config.targetAnimals)
            this.targetSelector.addGoal(Config.targetAnimalsPriority,
                    new NearestAttackableTargetGoal<>(this, Animal.class,
                            Config.targetAnimalsVisibility));

        if (Config.mineBlocks)
            this.goalSelector.addGoal(Config.miningPriority,
                    new ZombieMineGoal<>((Zombie & IMarkerFollower) (Object)this));

        if (Config.enableMarkers)
        {
            this.goalSelector.addGoal(2,
                    new RemoveMarkerGoal<>((Zombie & IMarkerFollower)(Object)this));

            this.goalSelector.addGoal(Config.markerPathingPriority,
                    new MoveTowardsMarkerGoal<>((Zombie & IMarkerFollower)(Object)this));
        }

        this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this,
                1.0, true, 4, this::canBreakDoors));

        this.goalSelector.addGoal(7,
                new WaterAvoidingRandomStrollGoal(this, 1.0));

        this.targetSelector.addGoal(1,
                (new HurtByTargetGoal(this)).setAlertOthers(ZombifiedPiglin.class));

        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this,
                Player.class, false));

        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this,
                AbstractVillager.class, false));

        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this,
                IronGolem.class, true));

        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this,
                Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

}
