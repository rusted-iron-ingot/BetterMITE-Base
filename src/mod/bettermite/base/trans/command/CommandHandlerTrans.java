package mod.bettermite.base.trans.command;

import mod.bettermite.base.CommandHelper;

import net.minecraft.*;
import net.minecraft.server.MinecraftServer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = {CommandHandler.class})
public class CommandHandlerTrans {
    @Inject(locals = LocalCapture.CAPTURE_FAILHARD, method = {"executeCommand"}, at = {@At(value = "INVOKE_ASSIGN", shift = At.Shift.AFTER, target = "Lnet/minecraft/EnumCommand;get(Ljava/lang/String;)Lnet/minecraft/EnumCommand;")}, cancellable = true)
    public void onCommandExecuted(ICommandListener iCommandListener, String string, boolean bl, CallbackInfoReturnable<Integer> callbackInfoReturnable, MinecraftServer minecraftServer, WorldServer worldServer, ServerPlayer serverPlayer, EnumCommand enumCommand) {
        CommandHelper.onCommandExecuted(iCommandListener,string,bl,callbackInfoReturnable,minecraftServer,worldServer,serverPlayer,enumCommand);
    }
}