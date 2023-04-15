package mod.bettermite.base;

import mod.bettermite.base.interfaces.IBetterMITECommand;
import mod.bettermite.base.interfaces.ICommandArguments;

import net.minecraft.*;
import net.minecraft.server.MinecraftServer;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Constructor;

import static mod.bettermite.base.BetterMITEBase.logger;
import static mod.bettermite.base.Subscriber.commands;

public class CommandHelper {
    public static void onCommandExecuted(ICommandListener iCommandListener, String string, boolean bl, CallbackInfoReturnable<Integer> callbackInfoReturnable, MinecraftServer minecraftServer, WorldServer worldServer, ServerPlayer serverPlayer, EnumCommand enumCommand) {
        try {
            for (int i = 0; i < commands.size(); i++) {
                if (string.startsWith(commands.get(i).string)) {
                    logger.info("BetterMITE：玩家" + Accessor.access(serverPlayer.getClass().getDeclaredField("username"), "") + "正在调用指令" + commands.get(i).string);
                    logger.info(commands.get(i).clazz);
                    Constructor constructor = commands.get(i).clazz.getDeclaredConstructor();
                    logger.info(constructor);
                    constructor.setAccessible(true);
                    IBetterMITECommand command = (IBetterMITECommand) constructor.newInstance(new Object[0]);
                    logger.info(command);
                    command.executeCommand(new ICommandArguments() {
                        @Override
                        public ServerPlayer getServerPlayer() {
                            return serverPlayer;
                        }
                        
                        @Override
                        public String getCommand() {
                            return string;
                        }
                    });
                    callbackInfoReturnable.setReturnValue(1);
                }
            }
        } catch (Throwable e) {
            logger.warn("执行BetterMITE命令失败！");
            e.printStackTrace();
        }
        if (string.equals("listCommands")) {
            for (int i = 0; i < commands.size(); i++) {
                iCommandListener.sendChatToPlayer(ChatMessage.createFromText(commands.get(i).string));
            }
            callbackInfoReturnable.setReturnValue(1);
        }
    }
}
