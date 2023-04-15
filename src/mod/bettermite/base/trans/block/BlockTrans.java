package mod.bettermite.base.trans.block;

import mod.bettermite.base.Accessor;
import mod.bettermite.base.BetterMITEBase;
import mod.bettermite.base.block.Blocks;
import net.minecraft.Block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static mod.bettermite.base.BetterMITEBase.blockIDCount;
import static mod.bettermite.base.BetterMITEBase.logger;

@Mixin(Block.class)
public class BlockTrans {
    @Shadow
    private String textureName;

    public Block setResourceLocation(String string) {
        return this.setTextureName(string);
    }

    @Shadow
    private Block setTextureName(String string) {
        return null;
    }
    
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void injected(CallbackInfo info) {
        try {
            Block[] blocksList_ = new Block[blockIDCount];
            int i = 0;
            boolean[] opaqueCubeLookup_ = new boolean[blockIDCount];
            for(boolean b : Block.opaqueCubeLookup){
                opaqueCubeLookup_[i] = b;
                i++;
            }
            Accessor.modify(Block.class.getField("opaqueCubeLookup"),opaqueCubeLookup_);
            int[] lightOpacity_ = new int[blockIDCount];
            i = 0;
            for(int integer : Block.lightOpacity){
                lightOpacity_[i] = integer;
                i++;
            }
            Accessor.modify(Block.class.getField("lightOpacity"),lightOpacity_);
            i = 0;
            boolean[] canHaveLightValue_ = new boolean[blockIDCount];
            for(boolean b : Block.canHaveLightValue){
                canHaveLightValue_[i] = b;
                i++;
            }
            i = 0;
            Accessor.modify(Block.class.getField("canHaveLightValue"),canHaveLightValue_);
            int[] lightValue_ = new int[blockIDCount];
            for(int b : Block.lightValue){
                lightValue_[i] = b;
                i++;
            }
            Accessor.modify(Block.class.getField("lightValue"),lightValue_);
            i = 0;
            boolean[] useNeighborBrightness_ = new boolean[blockIDCount];
            for(boolean b : Block.useNeighborBrightness){
                useNeighborBrightness_[i] = b;
                i++;
            }
            Accessor.modify(Block.class.getField("useNeighborBrightness"),useNeighborBrightness_);
            i = 0;
            boolean[] is_normal_cube_lookup_ = new boolean[blockIDCount];
            for(Boolean b : Block.is_normal_cube_lookup){
                is_normal_cube_lookup_[i] = b;
                i++;
            }
            Accessor.modify(Block.class.getField("is_normal_cube_lookup"),is_normal_cube_lookup_);
            i = 0;
            for(Block block : Block.blocksList){
                blocksList_[i] = block;
                i++;
            }
            Accessor.modify(Block.class.getField("blocksList"),blocksList_);
            
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        Blocks.regis();
        logger.info("BetterMITE-Base：共有" + blockIDCount + "个方块ID");
    }
    
}
