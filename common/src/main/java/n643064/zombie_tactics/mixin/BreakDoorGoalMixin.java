package n643064.zombie_tactics.mixin;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.minecraft.world.entity.ai.goal.DoorInteractGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(BreakDoorGoal.class)
public abstract class BreakDoorGoalMixin extends DoorInteractGoal {
    @Shadow protected int breakTime;

    @Shadow protected abstract boolean isValidDifficulty(Difficulty difficulty);
    @Shadow protected abstract int getDoorBreakTime();

    public BreakDoorGoalMixin(Mob mob) {
        super(mob);
    }

    /**
     * distance limit: 2 -> 5
     * @author PICOPress
     * @reason keep from interrupting when climbing
     */
    @Overwrite
    public boolean canContinueToUse() {
        return this.breakTime <= this.getDoorBreakTime() && !this.isOpen() &&
                this.doorPos.closerToCenterThan(this.mob.position(), 5) &&
                this.isValidDifficulty(this.mob.level().getDifficulty());
    }
}
