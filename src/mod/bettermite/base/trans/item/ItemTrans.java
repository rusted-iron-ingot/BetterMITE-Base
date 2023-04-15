package mod.bettermite.base.trans.item;

import net.minecraft.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Item.class)
public class ItemTrans {
    public void setResourceLocation(String string) {
        this.setTextureName(string);
    }

    @Shadow
    public Item setTextureName(String string) {
        return null;
    }
    
}
