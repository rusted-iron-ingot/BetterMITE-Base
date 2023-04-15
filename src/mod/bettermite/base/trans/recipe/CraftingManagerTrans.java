package mod.bettermite.base.trans.recipe;

import net.minecraft.*;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;


@Mixin(value = {CraftingManager.class})
public class CraftingManagerTrans {
    @Shadow
    private ShapedRecipes addRecipe(ItemStack itemStack, boolean bl, Object[] objectArray) {
        return null;
    }

    @Shadow
    private ShapelessRecipes addShapelessRecipe(ItemStack itemStack, boolean bl, Object[] objectArray) {
        return null;
    }

    @Redirect(method={"<init>"}, at=@At(value="INVOKE", target="Lnet/minecraft/RecipesMITE;addCraftingRecipes(Lnet/minecraft/CraftingManager;)V"))
    private void injectRegisters(CraftingManager craftingManager) {
    //    for (int i = 0; i < BetterMITERecipesManager.recipesList.size(); ++i) {
    //        BetterMITERecipesManager.recipesList.get(i).recipes(craftingManager);
    //        System.out.println("BetterMITE-Base：已应用" + BetterMITERecipesManager.recipesList.get(i).toString() + "中的合成表");
    //    }
        RecipesMITE.addCraftingRecipes(craftingManager);
    }
    
    public ShapedRecipes addRecipeP(ItemStack itemStack, boolean bl, Object... objectArray) {
        return this.addRecipe(itemStack, bl, objectArray);
    }

    public ShapelessRecipes addShapelessRecipeP(ItemStack itemStack, boolean bl, Object... objectArray) {
        return this.addShapelessRecipe(itemStack, bl, objectArray);
    }
}