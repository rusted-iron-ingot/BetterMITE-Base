package mod.bettermite.base.trans.world;

import mod.bettermite.base.BetterMITEBase;

import net.minecraft.*;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ChunkSection.class)
public class ChunkSectionTrans {
    
    @Shadow
    private byte[] blockLSBArray;
    @Shadow
    private NibbleArray blockMetadataArray;
    @Shadow
    private NibbleArray blocklightArray;
    @Shadow
    private NibbleArray skylightArray;
    
    @ModifyConstant(method = "<init>(IZ)V",constant = @Constant(intValue=4096))
    private int arrayModify(int value,int i,boolean b) {
        return BetterMITEBase.blockIDCount;
    }
    
    @ModifyConstant(method = "setExtBlockID(IIII)V", constant = @Constant(intValue = 255))
    private int setExtBlockID(int value,int n, int n2, int n3, int n4) {
        return BetterMITEBase.blockIDCount;
    }
}
