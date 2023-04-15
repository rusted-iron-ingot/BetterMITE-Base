package mod.bettermite.base;

import net.xiaoyu233.fml.AbstractMod;

import java.util.ArrayList;
import java.util.List;

import static mod.bettermite.base.BetterMITEBase.logger;

public abstract class AbstractModWithSubscriber extends AbstractMod {
    public static List<AbstractModWithSubscriber> list = new ArrayList<>();
    protected AbstractModWithSubscriber() {
        try{
            List<AbstractModWithSubscriber> list = Accessor.access(this.getClass().getField("list"),new ArrayList<>());
            list.add(this);
            logger.info("BetterMITE-Base：已注册" + this.getClass().getName());
        }catch (Throwable e){
            e.printStackTrace();
        }
        injectCustomSubscriber();
    }

    public String getPackageName() {
        String name = this.getClass().getName();
        String packageName = name.substring(0, name.lastIndexOf("."));
        return packageName;
    }

    /*
      关于自定义Subscriber的实现：
          接口继承IBetterMITESubscriber并加上@Info注解
          注解加上@Info注解
          然后添加进Subscriber的List里面
          如：
              Subscriber.subscriberList.add(XXX.class);               //接口
              Subscriber.subscriberAnnotationList.add(XXX.class);     //注解
     */
    public abstract void injectCustomSubscriber();
    
    public abstract void checkForCustomSubscriber(Class clazz);

    public abstract String getTransPackage();

    protected static class Entry {
        Integer i;
        String s;

        private Entry(Integer i, String s) {
            this.i = i;
            this.s = s;
        }
    }
}
