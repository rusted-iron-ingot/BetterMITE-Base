package mod.bettermite.base.interfaces;

import net.minecraft.ServerPlayer;

public interface ICommandArguments {
    public ServerPlayer getServerPlayer();

    public String getCommand();
}
