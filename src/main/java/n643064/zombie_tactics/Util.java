package n643064.zombie_tactics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class Util
{
    public static BlockPos off(BlockPos a, BlockPos b)
    {
        final Vec3 s = b.getCenter().subtract(a.getCenter());
        return a.offset(Double.compare(s.x, 0), Double.compare(s.y, 0), Double.compare(s.z, 0));
    }

}
