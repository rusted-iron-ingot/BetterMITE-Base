package mod.bettermite.base.trans;

import mod.bettermite.base.AbstractModWithSubscriber;
import mod.bettermite.base.Subscriber;

import net.minecraft.MinecraftEncryption;
import net.minecraft.client.main.Main;
import net.minecraft.server.MinecraftServer;
import net.xiaoyu233.fml.FishModLoader;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static mod.bettermite.base.AbstractModWithSubscriber.list;
import static mod.bettermite.base.BetterMITEBase.logger;

@Mixin({Main.class, MinecraftServer.class})
public class MainTrans {
    
    @Inject(method="main",at=@At("INVOKE"))
    private static void inject(CallbackInfo callbackInfo){
        if(Subscriber.commands.size() <= 0){
            Map modsMap_ = FishModLoader.getModsMap();
            for(int i =0;i < modsMap_.size();i++){
                if(modsMap_.get(i) != null){
                    if(modsMap_.get(i) instanceof AbstractModWithSubscriber){
                        list.add((AbstractModWithSubscriber) modsMap_.get(i));
                    }
                }
            }
            try {
                for (int i = 0; i < list.size(); i++) {
                    logger.info("BetterMITE-Base：正在搜索模组" + list.get(i).modId() + "中的Subscriber");
                    Subscriber.searchForSubscribers(list.get(i).getPackageName(), list.get(i).getTransPackage(),list.get(i));
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("BetterMITE-Base：搜索Subscriber失败！", e);
            }
        }
    }
}
