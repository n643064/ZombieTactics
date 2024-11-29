package n643064.zombie_tactics;

import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class MoveTowardsMarkerGoal<T extends Zombie & IMarkerFollower> extends WaterAvoidingRandomStrollGoal
{
    private final T zombie;
    public MoveTowardsMarkerGoal(T zombie)
    {
        super(zombie, 1d);
        this.zombie = zombie;
    }

    @Override
    protected @Nullable Vec3 getPosition()
    {
        final MarkerEntity m = zombie.zombieTactics$getTargetMarker();
        if (m == null) return null;
        if (m.isRemoved())
        {
            zombie.zombieTactics$setTargetMarker(null);
            return null;
        }
        return m.getPosition(0);
    }

    @Override
    public void stop()
    {
        super.stop();
        zombie.zombieTactics$setTargetMarker(null);
    }
}
