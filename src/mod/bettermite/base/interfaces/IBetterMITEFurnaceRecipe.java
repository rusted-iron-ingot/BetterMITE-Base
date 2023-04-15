package mod.bettermite.base.interfaces;

import mod.bettermite.base.annotation.Info;
import net.minecraft.RecipesFurnace;
@Info("熔炉配方")
public interface IBetterMITEFurnaceRecipe extends IBetterMITESubscriber{
    void furnaceRecipeRegister(RecipesFurnace recipesFurnace);
    
}
