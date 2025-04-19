package n643064.zombie_tactics.goals;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;

public class SelectiveFloatGoal extends FloatGoal {
    private final Mob mob;
    private boolean need_air;

    public SelectiveFloatGoal(Mob mob) {
        super(mob);
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        if(mob.getTarget() == null) return super.canUse();
        // zombies want to breathe
        if(mob.getAirSupply() < 10) need_air = true;
        if(need_air && mob.getAirSupply() == 300) {
            need_air = false;
        }
        return super.canUse() && !mob.getTarget().isInWater() || need_air;
    }
}
