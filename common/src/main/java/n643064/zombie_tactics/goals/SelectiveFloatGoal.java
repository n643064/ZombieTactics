package n643064.zombie_tactics.goals;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;

public class SelectiveFloatGoal extends FloatGoal {
    private final Mob mob;

    public SelectiveFloatGoal(Mob mob) {
        super(mob);
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        if(mob.getTarget() == null) return super.canUse();
        return super.canUse() && !mob.getTarget().isInWater();
    }
}
