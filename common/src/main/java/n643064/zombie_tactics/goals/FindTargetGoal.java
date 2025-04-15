package n643064.zombie_tactics.goals;

import n643064.zombie_tactics.Config;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.ArrayList;
import java.util.List;


// for testing
@SuppressWarnings("unused")
public class FindTargetGoal extends TargetGoal {
    private final List<Class<? extends LivingEntity>> list;
    private TargetingConditions targetingConditions;
    private int delay;

    public FindTargetGoal( List<Class<? extends LivingEntity>> targets, Mob mob, boolean mustSee) {
        super(mob, mustSee);
        list = targets;
        targetingConditions = TargetingConditions.forCombat();
        if(Config.attackInvisible) targetingConditions = targetingConditions.ignoreLineOfSight();
    }

    @Override
    public boolean canUse() {
        return mob.getTarget() == null;
    }

    @Override
    public void start() {
        delay = 0;
    }

    @Override
    public void stop() {

    }

    @Override
    public void tick() {
        ++ delay;
        if(delay > 20) {
            delay = 0;
            var imposters = new ArrayList<LivingEntity>();
            double follow = getFollowDistance();
            int minimumCost = Integer.MAX_VALUE;

            // query targets
            for(var sus: list) {
                var imposter2 = mob.level().getNearbyEntities(sus, targetingConditions, mob, mob.getBoundingBox().inflate(follow, follow, follow));
                for(var imposter: imposter2) {
                    if(imposter != null) imposters.add(imposter);
                }
            }

            // calculate the cost for each of imposters
            for(var amogus : imposters) {
                // TODO because of bed time
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }
}
