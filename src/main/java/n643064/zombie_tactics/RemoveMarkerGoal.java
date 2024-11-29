package n643064.zombie_tactics;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Zombie;

public class RemoveMarkerGoal<T extends Zombie & IMarkerFollower> extends Goal
{
    private final T zombie;
    public RemoveMarkerGoal(T zombie)
    {
        this.zombie = zombie;
    }

    @Override
    public boolean canContinueToUse()
    {
        return false;
    }

    @Override
    public void start()
    {
        zombie.zombieTactics$setTargetMarker(null);
    }

    @Override
    public boolean canUse()
    {
        final MarkerEntity m = zombie.zombieTactics$getTargetMarker();
        if (m == null) return false;
        return m.isRemoved() || zombie.distanceToSqr(m) <= 1.3;
    }
}
