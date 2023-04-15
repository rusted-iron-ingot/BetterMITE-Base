package mod.bettermite.base.block;

import net.minecraft.Block;
import net.minecraft.BlockStone;

public class Blocks{
    public static Block block1280;
    public static void regis(){
        block1280 = new BlockStone(1280);
        block1280.setResourceLocation("stone");
        block1280.setUnlocalizedName("block1280");
    }
}
