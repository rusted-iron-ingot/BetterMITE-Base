package mod.bettermite.base.trans.recipe;

import mod.bettermite.base.Accessor;
import mod.bettermite.base.Subscriber;

import net.minecraft.RecipesFurnace;
import net.minecraft.RecipesMITE;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static mod.bettermite.base.BetterMITEBase.logger;

@Mixin(RecipesMITE.class)
public class RecipesMITETrans {
    private static Boolean b = true;
    @Inject(method = "addFurnaceRecipes", at = @At(value = "INVOKE"))
    private static void addBetterMITEFurnaceRecipes(RecipesFurnace recipesFurnace, CallbackInfo callbackInfo){
        if (b){
            logger.info("BetterMITE-Base：开始注册熔炉配方");
            b = false;
        }
        for(int i = 0; i < Subscriber.furnaceRecipes.size(); i++){
            try {
                //Subscriber.furnaceRecipes.get(i).getMethod("furnaceRecipeRegister").invoke(Accessor.createInstant(Subscriber.furnaceRecipes.get(i),recipesFurnace));
            } catch (Throwable e) {
                logger.error("BetterMITE-Base：处理" + Subscriber.furnaceRecipes.get(i).getName() + "中的熔炉配方失败");
            }
        }
    }
}
