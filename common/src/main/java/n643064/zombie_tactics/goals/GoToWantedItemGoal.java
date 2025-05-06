package n643064.zombie_tactics.goals;

import n643064.zombie_tactics.Config;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;


public class GoToWantedItemGoal extends Goal {
    private final Mob mob;
    private final Predicate<ItemStack> predicate;
    private ItemEntity target;
    private int delay = 0;
    private final int range;

    public GoToWantedItemGoal(Mob mob, Predicate<ItemStack> predicate) {
        this(mob, predicate, Config.pickupRange);
    }

    public GoToWantedItemGoal(Mob mob, Predicate<ItemStack> predicate, int range) {
        this.mob = mob;
        this.predicate = predicate;
        this.range = range;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        ++ delay;
        if(delay < 10) return false;
        delay = 0;
        List<ItemEntity> items = mob.level().getEntitiesOfClass(ItemEntity.class,
                new AABB(mob.getX() - range, mob.getY() - range, mob.getZ() - range,
                mob.getX() + range, mob.getY() + range, mob.getZ() + range));
        for (var item: items) {
            ItemStack stack = item.getItem();
            if (predicate.test(stack) && !(item.isInWater() || item.isInLava())) {
                mob.getNavigation().moveTo(item, 1);
                target = item;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return !(mob.getNavigation().isDone() || target.isRemoved());
    }
}
