package mod.bettermite.base.interfaces;

import mod.bettermite.base.annotation.Info;
import net.minecraft.CraftingManager;
@Info("合成表")
public interface IBetterMITERecipes extends IBetterMITESubscriber{
    void recipes(CraftingManager craftingManager);
    
}