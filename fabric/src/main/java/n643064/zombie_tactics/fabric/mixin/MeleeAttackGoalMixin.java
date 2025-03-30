package n643064.zombie_tactics.fabric.mixin;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import org.spongepowered.asm.mixin.*;

import java.util.EnumSet;

@Mixin(MeleeAttackGoal.class)
public abstract class MeleeAttackGoalMixin extends Goal {
    // Why are there too many unused private fields?
    // So, just expose them
    @Mutable @Final @Shadow private int attackIntervalTicks = 20;
    @Shadow private int cooldown = attackIntervalTicks;
    @Mutable @Final @Shadow private double speed;
    @Mutable @Final @Shadow private boolean pauseWhenMobIdle;
    @Mutable @Final @Shadow protected PathAwareEntity mob;

    public MeleeAttackGoalMixin(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
        this.mob = mob;
        this.speed = speed;
        this.pauseWhenMobIdle = pauseWhenMobIdle;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Shadow public abstract boolean canStart();

    /**
     * @author PICOPress
     * @reason Using unused private variable
     */
    @Overwrite
    public void resetCooldown() {
        this.cooldown = this.getTickCount(getCooldown());
    }

    /**
     * @author PICOPress
     * @reason same above
     */
    @Overwrite
    public int getCooldown() {
        return this.getTickCount(attackIntervalTicks);
    }
}
