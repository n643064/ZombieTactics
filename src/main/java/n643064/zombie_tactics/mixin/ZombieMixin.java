package n643064.zombie_tactics.mixin;

import n643064.zombie_tactics.*;

import n643064.zombie_tactics.attachments.MiningData;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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

import net.tslat.smartbrainlib.api.SmartBrainOwner;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(Zombie.class)
public abstract class ZombieMixin extends Monster implements SmartBrainOwner<ZombieMixin>
{
    @Final @Shadow private static Predicate<Difficulty> DOOR_BREAKING_PREDICATE;

    protected ZombieMixin(EntityType<? extends Zombie> entityType, Level level)
    {
        super(entityType, level);
    }

    @Shadow
    public abstract boolean canBreakDoors();

    // Healing zombie
    @Inject(method = "doHurtTarget", at = @At("HEAD"))
    public void doHurtTarget(Entity ent, CallbackInfoReturnable<Boolean> ci) {
        if(ent instanceof LivingEntity && this.getHealth() < this.getMaxHealth()) {
            this.heal((float)Config.healAmount);
        }
    }

    @Override
    public void die(@NotNull DamageSource source)
    {
        super.die(source);
        MiningData md = this.getData(Main.ZOMBIE_MINING);
        if(md.doMining)
            this.level().destroyBlockProgress(this.getId(), md.bp, - 1);
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
                Config.aggressiveSpeed, true));

        if (Config.targetAnimals)
        {
            this.targetSelector.addGoal(Config.targetAnimalsPriority,
                    new NearestAttackableTargetGoal<>(this, Animal.class,
                            Config.targetAnimalsVisibility));
            Config.AttackableTypes.add(Animal.class);
        }
        if (Config.mineBlocks)
            this.goalSelector.addGoal(Config.miningPriority,
                    new ZombieMineGoal<>((Zombie) (Object)this));

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
                Turtle.class, 10, true, false,
                Turtle.BABY_ON_LAND_SELECTOR));

        this.goalSelector.addGoal(1, new BreakDoorGoal(this, DOOR_BREAKING_PREDICATE));

        Config.AttackableTypes.add(AbstractVillager.class);
        Config.AttackableTypes.add(IronGolem.class);
        Config.AttackableTypes.add(Turtle.class);
        Config.AttackableTypes.add(Player.class);
    }
}
