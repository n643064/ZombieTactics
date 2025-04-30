package n643064.zombie_tactics.goals;

import n643064.zombie_tactics.impl.Plane;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import org.jetbrains.annotations.NotNull;


public class SelectiveFloatGoal extends FloatGoal {
    private final Mob mob;
    private final Plane plane;
    private boolean needBreathe = false;

    public SelectiveFloatGoal(Mob mob) {
        super(mob);
        this.mob = mob;
        this.plane = (Plane)mob;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();
        if(target == null) return super.canUse();
        // selectively float
        // and, zombies want to breathe, but not want to be drowned
        if(plane.zombie_tactics$getInt(0) > calculateBreath(target)) needBreathe = true;
        return  super.canUse()
                && (!target.isInWater() || mob.getBlockY() - target.getBlockY() < 1 || needBreathe);
    }

    @Override
    public boolean canContinueToUse() {
        if(plane.zombie_tactics$getInt(0) < 20) needBreathe = false;
        return super.canUse() || needBreathe;
    }

    // calculate a cost of swimming
    private int calculateBreath(@NotNull LivingEntity target) {
        return 600 - (mob.getBlockY() - target.getBlockY()) * 50;
    }
}
