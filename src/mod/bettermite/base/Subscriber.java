package mod.bettermite.base;

import mod.bettermite.base.annotation.SubscribeCommand;
import mod.bettermite.base.annotation.SubscribeFurnaceRecipe;
import mod.bettermite.base.annotation.SubscribeRecipe;
import mod.bettermite.base.interfaces.IBetterMITECommand;
import mod.bettermite.base.interfaces.IBetterMITEFurnaceRecipe;
import mod.bettermite.base.interfaces.IBetterMITERecipes;
import mod.bettermite.base.interfaces.IBetterMITESubscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Unfinished
/*
  @author 锈铁锭
  @since 0.0.5-b3
  内部类不能是Subscriber
 */
public class Subscriber {

    public static final List<Entry> commands = new ArrayList<>();
    public static final List<Class> recipes = new ArrayList<>();
    public static final List<Class> furnaceRecipes = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger("Subscriber");
    static final List<Class<? extends IBetterMITESubscriber>> subscriberList = new ArrayList<>();
    static final List<Class<? extends Annotation>> subscriberAnnotationList = new ArrayList<>();

    public synchronized static void searchForSubscribers(@Nonnull String packageName, String transPackage, AbstractModWithSubscriber mod) throws ClassNotFoundException {
        if (packageName == null) {
            logger.warn("Subscriber：输入的包为null！");
            return;
        }
        logger.info("Subscriber：正在查找包" + packageName);
        List<String> list = getClassName(packageName, true, transPackage);
        for (int i = 0; i < list.size(); i++) {
            String className = list.get(i);
            if(!className.contains(transPackage)){
                if (!className.contains("$")) {
                    logger.info("Subscriber：正在检测类" + className);
                    addSubscriber(className,mod);
                } else {
                    logger.info("Subscriber：已忽略内部类" + className);
                }
            }else {
                logger.info("Subscriber：已忽略Mixin类" + className);
            }
        }
        logger.info("Subscriber：已查找包" + packageName);
        logger.info("Subscriber：现有" + commands.size() + "个指令类");
        logger.info("Subscriber：现有" + recipes.size() + "个合成表类");
        logger.info("Subscriber：现有" + furnaceRecipes.size() + "个熔炉配方类");

    }

    public synchronized static List<String> getClassName(String packageName, boolean childPackage, @Nonnull String transPackage) {
        List<String> fileNames = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");
        URL url = loader.getResource(packagePath);
        if (url != null) {
            String type = url.getProtocol();
            if (type.equals("file")) {
                fileNames = getClassNameByFile(url.getPath(), null, childPackage, transPackage);
            } else if (type.equals("jar")) {
                fileNames = getClassNameByJar(url.getPath(), childPackage);
            }
        } else {
            fileNames = getClassNameByJars(((URLClassLoader) loader).getURLs(), packagePath, childPackage);
        }
        return fileNames;
    }

    private synchronized static List<String> getClassNameByFile(String filePath, List<String> className, boolean childPackage, @Nonnull String transPackage) {
        List<String> myClassName = new ArrayList<>();
        File file = new File(filePath);
        File[] files = file.listFiles();
        List<File> childFiles = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            childFiles.add(files[i]);
        }
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                if (childPackage) {
                    myClassName.addAll(getClassNameByFile(childFile.getPath(), myClassName, childPackage, transPackage));
                }
            } else {
                String childFilePath = childFile.getPath();
                if (childFilePath.endsWith(".class")) {
                    childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9,
                            childFilePath.lastIndexOf("."));
                    childFilePath = childFilePath.replace("\\", ".");
                    myClassName.add(childFilePath);
                }
            }
        }
        for(int i = 0;i < myClassName.size();i++){
            if(myClassName.get(i).contains(transPackage)){
                myClassName.remove(i);
            }
        }
        return myClassName;
    }

    private synchronized static List<String> getClassNameByJar(String jarPath, boolean childPackage) {
        List<String> myClassName = new ArrayList<>();
        String[] jarInfo = jarPath.split("!");
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
        String packagePath = jarInfo[1].substring(1);
        try {
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {
                    if (childPackage) {
                        if (entryName.startsWith(packagePath)) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    } else {
                        int index = entryName.lastIndexOf("/");
                        String myPackagePath;
                        if (index != -1) {
                            myPackagePath = entryName.substring(0, index);
                        } else {
                            myPackagePath = entryName;
                        }
                        if (myPackagePath.equals(packagePath)) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myClassName;
    }

    private synchronized static List<String> getClassNameByJars(URL[] urls, String packagePath, boolean childPackage) {
        List<String> myClassName = new ArrayList<>();
        if (urls != null) {
            for (int i = 0; i < urls.length; i++) {
                URL url = urls[i];
                String urlPath = url.getPath();
                // 不必搜索classes文件夹
                if (urlPath.endsWith("classes/")) {
                    continue;
                }
                String jarPath = urlPath + "!/" + packagePath;
                myClassName.addAll(getClassNameByJar(jarPath, childPackage));
            }
        }
        return myClassName;
    }

    private synchronized static void addSubscriber(String string,AbstractModWithSubscriber mod){
        try {
            Class clazz = ClassLoader.getSystemClassLoader().loadClass(string);
            clazz = Class.forName(string);
            mod.checkForCustomSubscriber(clazz);
            checkCustomSubscriber(clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Entry {
        public String string;
        public Class clazz;

        Entry(String string, Class clazz) {
            this.string = string;
            this.clazz = clazz;
        }
    }
    
    static {
        subscriberList.add(IBetterMITECommand.class);
        subscriberList.add(IBetterMITEFurnaceRecipe.class);
        subscriberList.add(IBetterMITERecipes.class);
        subscriberAnnotationList.add(SubscribeCommand.class);
        subscriberAnnotationList.add(SubscribeRecipe.class);
        subscriberAnnotationList.add(SubscribeFurnaceRecipe.class);
    }
    
    private static void checkCustomSubscriber(Class clazz){
        for(int i = 0;i < clazz.getInterfaces().length;i++){
            if(clazz.getInterfaces()[i] == IBetterMITECommand.class){
                commands.add(new Entry(((SubscribeCommand)clazz.getAnnotation(SubscribeCommand.class)).command(),clazz));
                logger.info("已注册指令类" + clazz.getName());
            }
            if(clazz.getInterfaces()[i] == IBetterMITEFurnaceRecipe.class){
                furnaceRecipes.add(clazz);
                logger.info("已注册熔炉配方类" + clazz.getName());
            }
            if(clazz.getInterfaces()[i] == IBetterMITERecipes.class){
                recipes.add(clazz);
                logger.info("已注册合成表类" + clazz.getName());
            }
        }
    }
}
