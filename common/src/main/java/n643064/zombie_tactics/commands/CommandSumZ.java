package n643064.zombie_tactics.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.Zombie;


public class CommandSumZ {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("sumz")
                .then(Commands.argument("spawnCount", IntegerArgumentType.integer(1, 1024))
                        .executes(CommandSumZ::command)));
    }
    // ex) /sumz 32 ==> summon 32 zombies
    public static int command(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        ServerLevel world = src.getLevel();
        Component chat;
        int count = ctx.getArgument("spawnCount", Integer.class);

        for(int i = 0; i < count; ++ i) {
            Zombie z = new Zombie(world);
            z.setPos(src.getPosition());
            world.addFreshEntity(z);
        }
        chat = Component.literal(count + " zombies spawned");
        src.sendSystemMessage(chat);
        return 0;
    }
}
