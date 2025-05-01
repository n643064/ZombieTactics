package n643064.zombie_tactics.goals;

import n643064.zombie_tactics.attachments.FindTargetType;
import n643064.zombie_tactics.Config;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;

import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;


// the new improved target finding goal
public class FindAllTargetsGoal extends TargetGoal {
    public static final List<Pair<LivingEntity, Path>> cache_path = new ArrayList<>();
    private final List<Class<? extends LivingEntity>> list;
    private final List<LivingEntity> imposters = new ArrayList<>();
    private final int[] priorities;
    private final AABB boundary;
    private TargetingConditions targetingConditions;
    private int delay;
    private int idx;
    private boolean section;

    /**
     * @param priorities specify mobs' priority respectively. its length must be equal or larger than to the target list size.
     */
    public FindAllTargetsGoal(Set<Class<? extends LivingEntity>> targets, Mob mob, int[] priorities, boolean mustSee) {
        super(mob, mustSee);
        setFlags(EnumSet.of(Flag.TARGET));
        list = targets.stream().toList();
        this.priorities = priorities;
        boundary = mob.getBoundingBox().inflate(getFollowDistance() * getFollowDistance());
        targetingConditions = TargetingConditions.forCombat().range(Config.followRange).selector(null);
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
        if(Config.findTargetType == FindTargetType.SIMPLE && delay > 3) {
            delay = 0;
            // simple target finding
            LivingEntity target;
            var clazz = list.get(idx);
            if (clazz != Player.class && clazz != ServerPlayer.class) {
                target = mob.level().getNearestEntity(clazz, targetingConditions, mob, mob.getX(), mob.getEyeY(), mob.getZ(), boundary);
            } else {
                 target = mob.level().getNearestPlayer(targetingConditions, mob, mob.getX(), mob.getEyeY(), mob.getZ());
            }
            if(target != null && mob.getTarget() != null && mob.distanceToSqr(target) < mob.distanceToSqr(mob.getTarget()) || mob.getTarget() == null) {
                mob.setTarget(target);
            }
            ++ idx;
            idx %= list.size();
        } else if(delay > 5) {
            delay = 0;
            section = true;

            // query targets
            // and fucking slow
            for(var sus: list) {
                List<? extends LivingEntity> imposter2;
                if(sus == Player.class || sus == ServerPlayer.class) {
                    imposter2 = mob.level().getNearbyPlayers(targetingConditions, mob, boundary); // players
                } else imposter2 = mob.level().getNearbyEntities(sus, targetingConditions, mob, boundary); // just mobs
                for(var imposter: imposter2) {
                    if(imposter != null) imposters.add(imposter);
                }
            }
            // distribute the loads
        } else if(section) {
            BlockPos me = mob.blockPosition();
            LivingEntity target = null;
            int minimumCost = Integer.MAX_VALUE;

            section = false;
            // calculate the cost for each of imposters
            for(var amogus: imposters) {
                BlockPos delta = me.subtract(amogus.blockPosition());
                int score = 0;
                int idx = 0;

                if(Config.findTargetType == FindTargetType.INTENSIVE) {
                    boolean found = false;
                    Path path = null;
                    for(var p: cache_path) {
                        if(p.getA() == amogus) {
                            path = p.getB();
                            found = true;
                            break;
                        }
                    }
                    if(!found) {
                        // use cache to prevent overloading
                        path = mob.getNavigation().createPath(amogus, 0);
                        cache_path.add(new Pair<>(amogus, path));
                    }
                    if(path != null) {
                        score += path.getNodeCount();
                        if(!path.canReach()) score *= 128;
                    }
                } else if(Config.findTargetType == FindTargetType.LINEAR) {
                    // using linear function
                    double len = mob.distanceToSqr(amogus);
                    int xx = mob.getBlockX();
                    int yy = mob.getBlockY();
                    int zz = mob.getBlockZ();
                    for(int i = 0; i <= len; ++ i) {
                        if(!mob.level().getBlockState(new BlockPos((int)(xx + delta.getX() * i / len),
                                (int)(yy + delta.getY() * i / len), (int)(zz + delta.getZ() * i / len))).isAir())
                            score += Config.blockCost;
                        else ++ score;
                    }
                } else if(Config.findTargetType == FindTargetType.OVERLOAD) { // no one can endure this overload
                    Path path = mob.getNavigation().createPath(amogus, 0);
                    if(path != null) {
                        score += path.getNodeCount();
                        if(!path.canReach()) score *= 128;
                    }
                }

                // apply priority
                for(var p: list) {
                    if(p.isAssignableFrom(amogus.getClass())) {
                        break;
                    }
                    ++ idx;
                }
                // idx must match the target list unless priorities are invalid
                score *= priorities[idx];

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
            //System.out.println(mob.getId() + ": " + mob.canPickUpLoot());
            // set target
            mob.setTarget(target);
            imposters.clear();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return canUse() || super.canContinueToUse();
    }
}
