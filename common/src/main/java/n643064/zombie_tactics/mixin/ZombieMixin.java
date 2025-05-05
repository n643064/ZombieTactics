package n643064.zombie_tactics.mixin;

import n643064.zombie_tactics.Config;
import n643064.zombie_tactics.goals.ZombieGoal;
import n643064.zombie_tactics.goals.FindAllTargetsGoal;
import n643064.zombie_tactics.goals.SelectiveFloatGoal;
import n643064.zombie_tactics.impl.Plane;
import n643064.zombie_tactics.mining.ZombieMineGoal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import org.jetbrains.annotations.NotNull;

import oshi.util.tuples.Pair;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.HashSet;
import java.util.Set;

@Mixin(Zombie.class)
public abstract class ZombieMixin extends Monster implements Plane {
    @Unique private static final Set<Pair<Class<? extends LivingEntity>, Integer>> zombie_tactics$target_set = new HashSet<>();
    @Unique private static int zombie_tactics$threshold = 0;
    @Unique private ZombieMineGoal<? extends Monster> zombie_tactics$mine_goal;
    @Unique private BreakDoorGoal zombie_tactics$bdg;
    @Unique private int zombieTactics$climbedCount = 0;
    @Unique private boolean zombieTactics$isClimbing = false;
    @Unique private boolean zombie_tactics$persistence;

    @Final @Shadow private static Predicate<Difficulty> DOOR_BREAKING_PREDICATE;
    @Shadow private int inWaterTime;
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

    @Override
    protected float getFlyingSpeed() {
        return (float)this.getAttributeValue(Attributes.FLYING_SPEED);
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
    public int getMaxSpawnClusterSize() {
        return 64; // I think, the bigger the number is the better
    }

    // not to despawn
    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        if(zombie_tactics$persistence) return false;
        else return super.removeWhenFarAway(distanceToClosestPlayer);
    }

    @Override
    public int zombie_tactics$getInt(int id) {
        // inWaterTime
        if(id == 0) {
            return inWaterTime;
        }
        // nothing else
        return 0;
    }

    @Override
    public boolean zombie_tactics$getBool(int id) {
        if(id == 0) {
            if(zombie_tactics$mine_goal == null) return false;
            return zombie_tactics$mine_goal.mine.doMining;
        }
        return false;
    }

    @Override
    public double getAttributeValue(Holder<Attribute> attribute) {
        // change follow range
        if(attribute == Attributes.FOLLOW_RANGE) return Config.followRange;
        return super.getAttributeValue(attribute);
    }

    @Override
    public boolean wantsToPickUp(@NotNull ItemStack stack) {
        Item item = stack.getItem();
        if(item instanceof SwordItem s) {
            if(s.getTier().getAttackDamageBonus() > this.getMainHandItem().getDamageValue()) return true;
        } else if(item instanceof ArmorItem armor) {
            if(armor.getDefense() > this.getArmorValue()) return true;
        }
        return super.wantsToPickUp(stack);
    }


    @Override
    public boolean isPersistenceRequired() {
        return zombie_tactics$persistence || super.isPersistenceRequired();
    }

    // For climbing
    @Override
    public void push(@NotNull Entity entity) {
        if(zombie_tactics$bdg != null && Config.zombiesClimbing && entity instanceof Zombie &&
                (horizontalCollision || Config.hyperClimbing) && !((Plane)zombie_tactics$bdg).zombie_tactics$getBool(0)) {
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
        if(zombie_tactics$mine_goal != null && zombie_tactics$mine_goal.mine.doMining)
            this.level().destroyBlockProgress(this.getId(), zombie_tactics$mine_goal.mine.bp, -1);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        // unlock darkness
        return Config.spawnUnderSun? 0: super.getWalkTargetValue(pos, level);
    }

    @Inject(method="createAttributes", at=@At("RETURN"), cancellable=true)
    private static void createAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        // if a zombie cannot fly, it is just nothing
        cir.setReturnValue(cir.getReturnValue().add(Attributes.FLYING_SPEED, Config.flySpeed));
    }

    @Inject(method="hurt", at=@At("HEAD"))
    public void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity who = source.getEntity();
        // new blacklist
        if(who instanceof PathfinderMob mob && !(who instanceof Monster)) {
            zombie_tactics$target_set.add(new Pair<>(mob.getClass(), 3));
        }
    }

    @Inject(method="<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at=@At("TAIL"))
    public void constructor(EntityType<? extends Zombie> entityType, Level level, CallbackInfo ci) {
        double tmp = this.level().random.nextDouble();
        zombie_tactics$persistence = tmp <= Config.persistenceChance;
        if(zombie_tactics$persistence && zombie_tactics$threshold < Config.maxThreshold) {
            ++ zombie_tactics$threshold;
        } else zombie_tactics$persistence = false;
        if(zombie_tactics$persistence) this.setPersistenceRequired();
        if(Config.canFly) {
            this.moveControl = new FlyingMoveControl(this, 360, true);
            this.navigation = new FlyingPathNavigation(this, level);
            Objects.requireNonNull(this.getAttribute(Attributes.FLYING_SPEED)).setBaseValue(Config.flySpeed);
        }
    }

    @Inject(method="tick", at=@At("TAIL"))
    public void tick(CallbackInfo ci) {
        if(!this.canPickUpLoot()) this.setCanPickUpLoot(true);
        if(Config.canFly) this.fallDistance = 0;
    }

    // fixes that doing both mining and attacking
    @Inject(method="doHurtTarget", at=@At("HEAD"))
    public void doHurtTargetHead(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if(zombie_tactics$mine_goal != null) zombie_tactics$mine_goal.mine.doMining = false;
    }

    // Healing zombie
    @Inject(method="doHurtTarget", at=@At("TAIL"))
    public void doHurtTargetTail(Entity ent, CallbackInfoReturnable<Boolean> ci) {
        if(ent instanceof LivingEntity) {
            if(this.getHealth() <= this.getMaxHealth())
                this.heal((float)Config.healAmount);
        }
        // reset invulnerable time
        if(Config.noMercy) ent.invulnerableTime = 0;
    }

    // I do not want to see that zombies burn
    @Inject(method="isSunSensitive", at=@At("RETURN"), cancellable=true)
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
        if(Config.targetAnimals) zombie_tactics$target_set.add(new Pair<>(Animal.class, 5));
        if(Config.mineBlocks) this.goalSelector.addGoal(1, zombie_tactics$mine_goal = new ZombieMineGoal<>(this));
        if(Config.canFloat) this.goalSelector.addGoal(5, new SelectiveFloatGoal(this));
        if(Config.canFly) this.goalSelector.addGoal(7, new WaterAvoidingRandomFlyingGoal(this, 1.0));
        else this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));

        this.targetSelector.addGoal(3, new FindAllTargetsGoal(zombie_tactics$target_set, this, false));
        this.goalSelector.addGoal(1, new ZombieGoal((Zombie)(Object)this, Config.aggressiveSpeed, true));
        this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0, false, 4, this::canBreakDoors));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(ZombifiedPiglin.class));
        this.goalSelector.addGoal(1, zombie_tactics$bdg = new BreakDoorGoal(this, DOOR_BREAKING_PREDICATE));
    }

    static {
        zombie_tactics$target_set.add(new Pair<>(Player.class, 2));
        zombie_tactics$target_set.add(new Pair<>(AbstractVillager.class, 3));
        zombie_tactics$target_set.add(new Pair<>(IronGolem.class, 3));
        zombie_tactics$target_set.add(new Pair<>(Turtle.class, 3));
    }
}
