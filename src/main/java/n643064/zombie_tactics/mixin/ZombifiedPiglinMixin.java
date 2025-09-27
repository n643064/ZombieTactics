package n643064.zombie_tactics.mixin;

import n643064.zombie_tactics.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static n643064.zombie_tactics.Config.CONFIG;

@Mixin(ZombifiedPiglin.class)
public abstract class ZombifiedPiglinMixin extends Zombie implements NeutralMob
{
    public ZombifiedPiglinMixin(EntityType<? extends Zombie> entityType, Level level) {super(entityType, level);}

    /**
     * @author me
     * @reason no :3
     */
    @Overwrite
    public void addBehaviourGoals()
    {
        if (CONFIG.affectPiglins())
        {
            this.goalSelector.addGoal(1, new ZombieAttackGoal(this, 1.0, true));
            if (CONFIG.targetAnimals())
                this.targetSelector.addGoal(CONFIG.targetAnimalsPriority(), new NearestAttackableTargetGoal<>(this, Animal.class, CONFIG.targetAnimalsVisibilityCheck()));
            if (CONFIG.mineBlocks())
                this.goalSelector.addGoal(CONFIG.miningPriority(), new ZombieMineGoal<>((Zombie & IMarkerFollower) this));
            if (CONFIG.enableMarkers())
            {
                this.goalSelector.addGoal(2, new RemoveMarkerGoal<>((Zombie & IMarkerFollower) this));
                this.goalSelector.addGoal(CONFIG.markerNavigationPriority(), new MoveTowardsMarkerGoal<>((Zombie & IMarkerFollower) this));
            }
        } else
            this.goalSelector.addGoal(2, new ZombieAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, true));
    }
}
