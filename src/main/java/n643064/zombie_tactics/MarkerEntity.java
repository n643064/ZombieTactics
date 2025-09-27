package n643064.zombie_tactics;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import static n643064.zombie_tactics.Config.CONFIG;


public class MarkerEntity extends Entity
{
    private int timeToLive;
    public MarkerEntity(EntityType<?> entityType, Level level)
    {
        super(entityType, level);
        timeToLive = CONFIG.markerLifetime() * 20;

        for (Entity e : level.getEntities(this, this.getBoundingBox().inflate(10 * CONFIG.markerRange()), e -> (e instanceof IMarkerFollower)))
        {
            ((IMarkerFollower) (e)).zombieTactics$setTargetMarker(this);
        }
    }

    public MarkerEntity(Level level)
    {
        this(Main.MARKER, level);
    }


    @Override
    public void baseTick()
    {
        timeToLive--;
        if (timeToLive <= 0)
            this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder)
    {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag)
    {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag)
    {

    }
}
