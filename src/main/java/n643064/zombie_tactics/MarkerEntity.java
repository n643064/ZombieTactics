package n643064.zombie_tactics;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

public class MarkerEntity extends Entity
{
    private int timeToLive;
    public MarkerEntity(EntityType<?> entityType, Level level)
    {
        super(entityType, level);
        timeToLive = Config.markerLifeTime * 20;

        for (Entity e : level.getEntities(this,
                this.getBoundingBox().inflate(10 * Config.markerRange),
                e -> (e instanceof IMarkerFollower)))
        {
            //System.out.println(e);
            ((IMarkerFollower) (e)).zombieTactics$setTargetMarker(this);
        }
    }

    public MarkerEntity(Level level)
    {
        this(Main.MARKER.get(), level);
    }

    @Override
    public void baseTick()
    {

        -- timeToLive;
        if (timeToLive <= 0)
        {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override @ParametersAreNonnullByDefault
    protected void defineSynchedData(SynchedEntityData.Builder builder)
    {

    }

    @Override @ParametersAreNonnullByDefault
    protected void readAdditionalSaveData(CompoundTag compoundTag)
    {

    }

    @Override @ParametersAreNonnullByDefault
    protected void addAdditionalSaveData(CompoundTag compoundTag)
    {

    }
}
