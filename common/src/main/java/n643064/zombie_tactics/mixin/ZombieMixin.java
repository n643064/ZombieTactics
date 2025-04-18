package n643064.zombie_tactics.mixin;

import n643064.zombie_tactics.Config;
import n643064.zombie_tactics.goals.ZombieGoal;
import n643064.zombie_tactics.goals.FindAllTargetsGoal;
import n643064.zombie_tactics.goals.SelectiveFloatGoal;
import n643064.zombie_tactics.impl.Plane;
import n643064.zombie_tactics.mining.ZombieMineGoal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


@Mixin(Zombie.class)
public abstract class ZombieMixin extends Monster {
    @Unique private int zombieTactics$climbedCount = 0;
    @Unique private boolean zombieTactics$isClimbing = false;
    @Unique private boolean zombie_tactics$persistence;
    @Unique private static int zombie_tactics$threshold = 0;
    @Unique private ZombieMineGoal<? extends Monster> zombie_tactics$mine_goal;
    @Unique private BreakDoorGoal zombie_tactics$bdg;
    @Unique private static final List<Class<? extends LivingEntity>> zombie_tactics$target_list = new ArrayList<>();

    @Final @Shadow private static Predicate<Difficulty> DOOR_BREAKING_PREDICATE;
    @Shadow public abstract boolean canBreakDoors(); // This just makes path finding

    public ZombieMixin(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }

    // zombie doesn't take fall damage when climbing
    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
        if(zombieTactics$isClimbing && onGround) {
            fallDistance = 0;
            zombieTactics$isClimbing = false;
            zombieTactics$climbedCount = 0;
        }
        super.checkFallDamage(y, onGround, state, pos);
    }

    // Modifying Attack range
    @Override
    protected @NotNull AABB getAttackBoundingBox() {
        Entity entity = this.getVehicle();
        AABB aabb;
        if (entity != null) {
            AABB aabb1 = entity.getBoundingBox();
            AABB aabb2 = this.getBoundingBox();
            aabb = new AABB(Math.min(aabb2.minX, aabb1.minX),
                    aabb2.minY,
                    Math.min(aabb2.minZ, aabb1.minZ),
                    Math.max(aabb2.maxX, aabb1.maxX),
                    aabb2.maxY,
                    Math.max(aabb2.maxZ, aabb1.maxZ));
        } else {
            aabb = this.getBoundingBox();
        }
        return aabb.inflate(Config.attackRange, Config.attackRange, Config.attackRange);
    }

    @Override
    public boolean isPersistenceRequired() {
        return zombie_tactics$persistence || super.isPersistenceRequired();
    }

    // For climbing
    @Override
    public void push(@NotNull Entity entity) {
        if(zombie_tactics$bdg != null && Config.zombiesClimbing && entity instanceof Zombie &&
                horizontalCollision && !((Plane)zombie_tactics$bdg).zombie_tactics$getBool()) {
            if(zombieTactics$climbedCount < 120) {
                final Vec3 v = getDeltaMovement();
                setDeltaMovement(v.x, Config.climbingSpeed, v.z);
                zombieTactics$isClimbing = true;
                ++ zombieTactics$climbedCount;
            }
        }
        super.push(entity);
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        -- zombie_tactics$threshold; // decrease
    }

    // reset crack progress if a zombie died when mining
    @Override
    public void die(@NotNull DamageSource source) {
        super.die(source);
        -- zombie_tactics$threshold;
        if(zombie_tactics$mine_goal != null && zombie_tactics$mine_goal.mine.doMining)
            this.level().destroyBlockProgress(this.getId(), zombie_tactics$mine_goal.mine.bp, -1);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At("TAIL"))
    public void constructor(EntityType<? extends Zombie> entityType, Level level, CallbackInfo ci) {
        double tmp = this.level().random.nextDouble();
        zombie_tactics$persistence = tmp <= Config.persistenceChance;
        if(zombie_tactics$persistence && zombie_tactics$threshold < Config.maxThreshold) {
            ++ zombie_tactics$threshold;
        } else zombie_tactics$persistence = false;
        if(zombie_tactics$persistence) this.setPersistenceRequired();
    }

    // fixes that doing both mining and attacking
    @Inject(method = "doHurtTarget", at = @At("HEAD"))
    public void doHurtTargetHead(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if(zombie_tactics$mine_goal != null) zombie_tactics$mine_goal.mine.doMining = false;
    }

    // Healing zombie
    @Inject(method = "doHurtTarget", at = @At("TAIL"))
    public void doHurtTargetTail(Entity ent, CallbackInfoReturnable<Boolean> ci) {
        if(ent instanceof LivingEntity) {
            if(this.getHealth() <= this.getMaxHealth())
                this.heal((float)Config.healAmount);
        }
        // reset invulnerable time
        if(Config.noMercy) ent.invulnerableTime = 0;
    }

    // I do not want to see that zombies burn
    @Inject(method = "isSunSensitive", at = @At("RETURN"), cancellable = true)
    public void isSunSensitive(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(Config.sunSensitive);
    }

    /**
     * ZombieMineGoal doesn't use zombie-exclusive things
     * @author PICOPress
     * @reason it's very hard to inject for each parameter
     */
    @Overwrite
    public void addBehaviourGoals() {
        if(Config.targetAnimals) zombie_tactics$target_list.add(Animal.class);
        if(Config.mineBlocks) this.goalSelector.addGoal(1, zombie_tactics$mine_goal = new ZombieMineGoal<>(this));
        if(Config.canFloat) this.goalSelector.addGoal(5, new SelectiveFloatGoal(this));

        this.targetSelector.addGoal(3, new FindAllTargetsGoal(zombie_tactics$target_list, this, new int[] {2, 3, 3, 3, 5}, false));
        this.goalSelector.addGoal(1, new ZombieGoal((Zombie)(Object)this, Config.aggressiveSpeed, true));
        this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0, false, 4, this::canBreakDoors));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(ZombifiedPiglin.class));
        this.goalSelector.addGoal(1, zombie_tactics$bdg = new BreakDoorGoal(this, DOOR_BREAKING_PREDICATE));
    }

    static {
        zombie_tactics$target_list.add(Player.class);
        zombie_tactics$target_list.add(AbstractVillager.class);
        zombie_tactics$target_list.add(IronGolem.class);
        zombie_tactics$target_list.add(Turtle.class);
    }
}
