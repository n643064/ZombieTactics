package n643064.zombie_tactics.goals;

import n643064.zombie_tactics.Config;

import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.monster.Zombie;

public class CustomZombieAttackGoal extends ZombieAttackGoal {
    public CustomZombieAttackGoal(Zombie zombie, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(zombie, speedModifier, followingTargetEvenIfNotSeen);
    }

    @Override
    protected int getAttackInterval() {
        return this.adjustedTickDelay(Config.attackCooldown);
    }
}
