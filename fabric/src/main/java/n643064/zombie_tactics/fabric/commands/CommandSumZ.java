package n643064.zombie_tactics.fabric.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.text.Text;

public class CommandSumZ {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("sumz")
                .then(CommandManager.argument("spawnCount", IntegerArgumentType.integer(1, 1024))
                        .executes(CommandSumZ::command)));
    }

    // ex) /sumz 32 ==> summon 32 zombies
    public static int command(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource src = ctx.getSource();
        ServerWorld world = src.getWorld();
        Text chat;
        int count = ctx.getArgument("spawnCount", Integer.class);

        for(int i = 0; i < count; ++ i) {
            ZombieEntity z = new ZombieEntity(world);
            z.setPosition(src.getPosition());
            world.spawnEntity(z);
        }
        chat = Text.literal(count + " zombies spawned");
        src.sendMessage(chat);
        return 0;
    }
}
