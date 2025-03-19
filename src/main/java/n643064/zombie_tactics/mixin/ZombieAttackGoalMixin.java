package n643064.zombie_tactics.mixin;

import n643064.zombie_tactics.Config;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;

import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Refactoring ZombieAttackGoal
// I just want to make attack speed faster
@Mixin(ZombieAttackGoal.class)
public class ZombieAttackGoalMixin extends MeleeAttackGoal
{
    @Final @Shadow private Zombie zombie;
    public ZombieAttackGoalMixin(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen)
    {
        super(mob, speedModifier, followingTargetEvenIfNotSeen);
    }

    @Override
    @Unique
    protected int getAttackInterval()
    {
        return this.adjustedTickDelay(Config.attackCooldown);
    }
    /*
    // not test
    // change target to the nearest
    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci)
    {
        for(Class<? extends LivingEntity> entity: Config.AttackableTypes)
        {
            LivingEntity imposter = zombie.level().getNearestEntity(entity, TargetingConditions.DEFAULT, zombie,
                    zombie.getX(), zombie.getY(), zombie.getZ(),
                    AABB.ofSize(zombie.getUpVector(0), 16, 16, 16));
            if(imposter == null) continue;
            if(zombie.isWithinMeleeAttackRange(imposter))
            {
                zombie.setTarget(imposter);
                break;
            }
        }
    }
     */
}
