package mod.bettermite.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Accessor {
    private static Logger logger = LogManager.getLogger("Accessor");
    
    public Accessor() {
    }
    
    public static <T> T modify(@Nonnull Field field, @Nonnull T value) {
        try {
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & -17);
            field.setAccessible(true);
            field.set(value, value);
            return (T) field.get(value);
        } catch (NoSuchFieldException var3) {
            throw new RuntimeException(var3);
        } catch (IllegalAccessException var4) {
            throw new RuntimeException(var4);
        } catch (IllegalArgumentException var5) {
            throw new RuntimeException("Accessor：输入的值" + value + "无效", var5);
        } catch (ClassCastException var6) {
            throw new RuntimeException(var6);
        }
    }
    
    public static <T> T access(@Nonnull Field field, @Nonnull T exampleValue) {
        try {
            field.setAccessible(true);
            return (T) field.get(exampleValue);
        } catch (IllegalArgumentException var3) {
            throw new RuntimeException("Accessor：你最好不是乱输的" + exampleValue, var3);
        } catch (SecurityException var4) {
            throw new RuntimeException(var4);
        } catch (IllegalAccessException var5) {
            throw new RuntimeException("Accessor：快去告诉锈铁锭他Accessor有问题", var5);
        } catch (ClassCastException var6) {
            throw new RuntimeException("Accessor：不可能，绝对不可能", var6);
        }
    }
    
    public static <T> T createInstant(@Nonnull Class<T> clazz, @Nullable Object... args) {
        try {
            Class[] parameterTypes = new Class[args.length];
            int i = 0;
            Object[] var4 = args;
            int var5 = args.length;
            
            for(int var6 = 0; var6 < var5; ++var6) {
                Object object = var4[var6];
                parameterTypes[i] = object.getClass();
                ++i;
            }
            
            Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(args);
        } catch (NoSuchMethodException var8) {
            throw new RuntimeException("Accessor：你要不检查检查你传入的参数？或者使用另一个createInstant()？", var8);
        } catch (InvocationTargetException var9) {
            throw new RuntimeException(var9);
        } catch (InstantiationException var10) {
            throw new RuntimeException(var10);
        } catch (IllegalAccessException var11) {
            throw new RuntimeException("Accessor：快去告诉锈铁锭他Accessor有问题", var11);
        }
    }
    
    public static <T> T createInstant(@Nonnull Class<T> clazz, @Nonnull Class[] types, @Nonnull Object[] args) {
        try {
            if (types.length != args.length) {
                logger.error("Accessor：参数类型数量和参数数量不一致！");
                return null;
            } else {
                Class[] parameterTypes = new Class[args.length];
                int i = 0;
                Object[] var5 = args;
                int var6 = args.length;
                
                for(int var7 = 0; var7 < var6; ++var7) {
                    Object object = var5[var7];
                    parameterTypes[i] = object.getClass();
                    ++i;
                }
                
                Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
                constructor.setAccessible(true);
                return constructor.newInstance(args);
            }
        } catch (NoSuchMethodException var9) {
            throw new RuntimeException("Accessor：你要不检查检查你传入的参数类型？", var9);
        } catch (InvocationTargetException var10) {
            throw new RuntimeException(var10);
        } catch (InstantiationException var11) {
            throw new RuntimeException(var11);
        } catch (IllegalAccessException var12) {
            throw new RuntimeException("Accessor：快去告诉锈铁锭他Accessor有问题", var12);
        }
    }
    
    public static <T, Y> Y invoke(@Nonnull Method method, @Nullable T instant, @Nullable Object... args) {
        try {
            method.setAccessible(true);
            return (Y) method.invoke(instant, args);
        } catch (InvocationTargetException var4) {
            throw new RuntimeException(var4);
        } catch (IllegalAccessException var5) {
            throw new RuntimeException(var5);
        } catch (NullPointerException var6) {
            throw new RuntimeException("Accessor：需要实例！", var6);
        }
    }
    
    public static <T> Class<T> accessClass(@Nonnull String className) {
        try {
            Class<T> clazz = (Class<T>) Class.forName(className);
            return clazz;
        } catch (ClassNotFoundException var2) {
            throw new RuntimeException("Accessor：要不再考虑清楚？", var2);
        }
    }
    
    public static <T, Y> Class<Y> accessInnerClass(@Nonnull Class<T> outerClass, @Nonnull String innerClassName) {
        try {
            Class<Y> innerClass = (Class<Y>) Class.forName(outerClass.getName() + "$" + innerClassName);
            return innerClass;
        } catch (ClassNotFoundException var3) {
            throw new RuntimeException(var3);
        }
    }
}
