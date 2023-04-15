package mod.bettermite.base;

import mod.bettermite.base.command.CommandMan;
import mod.bettermite.base.trans.TransMarker;

import net.xiaoyu233.fml.FishModLoader;
import net.xiaoyu233.fml.classloading.Mod;
import net.xiaoyu233.fml.config.InjectionConfig;
import net.xiaoyu233.fml.reload.event.MITEEvents;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.spongepowered.asm.mixin.MixinEnvironment;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Mod
public class BetterMITEBase extends AbstractModWithSubscriber {

    public static Integer blockIDCount = 4096;
    public static final Boolean isServer = FishModLoader.isServer();
    public static final Boolean isITFLoaded = FishModLoader.hasMod("我不到啊");
    public static final Boolean isITELoaded = FishModLoader.hasMod("MITE-ITE");
    public static final Logger logger = LogManager.getLogger("BetterMITE");

    public void preInit() {
    
    }

    public void postInit() {
        List<CommandMan.ManDocument.ManPage> pages = new ArrayList<>();
        List<String> page1 = new ArrayList<>();
        page1.add("这是base的man文档的第一页，也是最后一页");
        page1.add("BetterMITE由 (Bilibili)锈铁锭和whscqngsd 共同创造");
        page1.add("我是锈铁锭，我现在正在告诉你关于BetterMITE的一切");
        page1.add("BetterMITE由whscqngsd提出，现在我来写代码他来画贴图");
        page1.add("他画的大饼我认为有点不切实际，所以BetterMITE目前正在进行客户端/服务端方面的优化");
        page1.add("sudo是BetterMITE项目发布的第一个有服务端插件性质的模组（也是第一批模组）");
        page1.add("main将会在不久后完成（应该）");
        page1.add("我没有多大能力，我希望能有更多的人参与进MITE的模组开发");
        page1.add("而base，就是我准备的代码库，所有人都可以使用");
        pages.add(new CommandMan.ManDocument.ManPage(page1));
        CommandMan.manDocumentList.add(new CommandMan.ManDocument("base", pages, 0));
        logger.info("已注册base的man文档");
        
        MITEEvents.MITE_EVENT_BUS.register(new Test());
    }

    @Nonnull
    public InjectionConfig getInjectionConfig() {
        return InjectionConfig.Builder.of("BetterMITE-Base", TransMarker.class.getPackage(), MixinEnvironment.Phase.PREINIT).build();
    }

    public String modId() {
        return "BetterMITEBase";
    }

    public int modVerNum() {
        return 005;
    }

    public String modVerStr() {
        return "0.0.5-b2";
    }

    @Override
    public void injectCustomSubscriber() {

    }
    
    @Override
    public void checkForCustomSubscriber(Class clazz) {
    
    }
    
    @Override
    public String getTransPackage() {
        return "mod.bettermite.base.trans";
    }

}

