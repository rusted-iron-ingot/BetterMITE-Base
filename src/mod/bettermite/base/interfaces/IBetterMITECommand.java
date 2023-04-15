package mod.bettermite.base.interfaces;

import com.sun.istack.internal.NotNull;
import mod.bettermite.base.annotation.Info;
import net.minecraft.ChatMessage;
import net.minecraft.ICommandListener;

import javax.annotation.Nullable;
@Info("指令")
public interface IBetterMITECommand extends IBetterMITESubscriber{
    void executeCommand(ICommandArguments arg);

    /*
     *args只能为Integer和String型的变量，有返回值长度取决于你要分析的指令有多少个空格
     */
    default Object[] analyzeCommand(@NotNull ICommandListener iCommandListener, @NotNull String command, @Nullable Object... args) {
        int i1 = 0;
        int i2 = 0;
        try {
            System.out.println("正在解析参数： " + command);
            for (int i = 0; i < command.length(); i++) {
                if (command.substring(i, i + 1).equals(" ")) {
                    if (args[i1] instanceof Integer) {
                        args[i1] = Integer.valueOf(command.substring(i2, i));
                    } else {
                        args[i1] = command.substring(i2, i);
                    }
                    i2 = i;
                    i1++;
                    System.out.println(args[i1]);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            iCommandListener.sendChatToPlayer(ChatMessage.createFromText("无法解析指令： " + command));
            throw new RuntimeException("无法解析参数： " + command, e);
        }
        return args;
    }
}