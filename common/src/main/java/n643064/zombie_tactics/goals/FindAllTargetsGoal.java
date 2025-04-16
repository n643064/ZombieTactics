package n643064.zombie_tactics.goals;

import n643064.zombie_tactics.Config;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;


// the new improved target finding goal
public class FindAllTargetsGoal extends TargetGoal {
    private final List<Class<? extends LivingEntity>> list;
    private final List<LivingEntity> imposters = new ArrayList<>();
    private TargetingConditions targetingConditions;
    private final int[] priorities;
    private int delay;
    private boolean section;

    /**
     * @param priorities specify mobs' priority respectively. its length must be equal to the target size.
     */
    public FindAllTargetsGoal(List<Class<? extends LivingEntity>> targets, Mob mob, int[] priorities, boolean mustSee) {
        super(mob, mustSee);
        list = targets;
        this.priorities = priorities;
        setFlags(EnumSet.of(Flag.TARGET));
        targetingConditions = TargetingConditions.forCombat().range(getFollowDistance()).selector(null);
        if(Config.attackInvisible) targetingConditions = targetingConditions.ignoreLineOfSight();
    }

    @Override
    public boolean canUse() {
        return mob.getTarget() == null;
    }

    @Override
    public void start() {
        delay = 0;
        section = false;
    }

    @Override
    public void tick() {
        ++ delay;
        if(delay > 5) {
            delay = 0;
            section = true;
            double follow = getFollowDistance(); follow *= follow;
            AABB boundary = mob.getBoundingBox().inflate(follow);

            // query targets
            for(var sus: list) {
                if(sus == Player.class || sus == ServerPlayer.class){

                    continue;
                }
                var imposter2 = mob.level().getNearbyEntities(sus,targetingConditions, mob, boundary);
                for(var imposter: imposter2) {
                    if(imposter != null) imposters.add(imposter);
                }
            }
            // distribute the loads
        } else if(section) {
            section = false;
            BlockPos me = mob.blockPosition();
            int minimumCost = Integer.MAX_VALUE;LivingEntity target = null;
            int xx = mob.getBlockX();
            int yy = mob.getBlockY();
            int zz = mob.getBlockZ();

            // calculate the cost for each of imposters
            for(var amogus : imposters) {
                BlockPos delta = me.subtract(amogus.blockPosition());
                int score = 0;

                // using linear function
                double len = mob.distanceToSqr(amogus);
                for(int i = 0; i <= len; ++ i) {
                    if(! mob.level().getBlockState(new BlockPos((int)(xx + delta.getX() * i / len),
                            (int)(yy + delta.getY() * i / len), (int)(zz + delta.getZ() * i / len))).isAir())
                        score += Config.blockCost;
                    else ++ score;
                }
                // apply priority
                score *= priorities[list.indexOf(amogus.getClass())];

                // getting insane
                if(mob.hasLineOfSight(amogus)) score /= 2;
                if(delta.getY() >= -2) score /= 2;

                // for debug
                //amogus.setCustomName(Component.literal("" + score));

                // select minimum score
                if(score < minimumCost) {
                    minimumCost = score;
                    target = amogus;
                }
            }
            mob.setTarget(target);
            imposters.clear();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return canUse() || super.canContinueToUse();
    }
}
