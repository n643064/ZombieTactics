package n643064.zombie_tactics.mixin;

import n643064.zombie_tactics.Config;
import n643064.zombie_tactics.ZombieMineGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public class ZombieMixin extends Monster
{
    protected ZombieMixin(EntityType<? extends Monster> entityType, Level level)
    {
        super(entityType, level);
    }

    @Inject(method = "addBehaviourGoals", at = @At("TAIL"))
    private void addGoals(CallbackInfo ci)
    {
        if (!Config.affectPiglins && getType() == EntityType.ZOMBIFIED_PIGLIN)
            return;
        if (Config.targetAnimals)
            this.targetSelector.addGoal(Config.targetAnimalsPriority, new NearestAttackableTargetGoal<>(this, Animal.class, Config.targetAnimalsVisibility));
        if (Config.mineBlocks)
            this.goalSelector.addGoal(Config.miningPriority, new ZombieMineGoal((Zombie) (Object) this));
    }


}
