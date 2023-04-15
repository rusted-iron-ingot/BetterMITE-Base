package mod.bettermite.base.trans.world;

import mod.bettermite.base.BetterMITEBase;

import net.minecraft.*;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.lang.reflect.Array;
import java.util.Map;

import static mod.bettermite.base.BetterMITEBase.logger;

@Mixin(Chunk.class)
public class ChunkTrans {
    
    @Shadow
    private ChunkSection[] storageArrays;
    @Shadow
    private byte[] blockBiomeArray;
    @Shadow
    private int[] precipitationHeightMap;
    @Shadow
    private boolean[] updateSkylightColumns;
    @Shadow
    public World worldObj;
    @Shadow
    public final int[] skylight_bottom = new int[114514];
    @Shadow
    public Map chunkTileEntityMap;
    
    @Overwrite
    public void a(byte[] byArray, int n, int n2, boolean bl) {
        Object object;
        int n3;
        int n4 = 0;
        byte[] bytes = new byte[BetterMITEBase.blockIDCount];
        for(int i = 0;i < bytes.length;i++){
            bytes[i] = byArray[i];
        }
        byArray = bytes;
        logger.info(byArray.length);
        boolean bl2 = !this.worldObj.provider.hasNoSky;
        for (n3 = 0; n3 < this.storageArrays.length; ++n3) {
            if ((n & 1 << n3) != 0) {
                if (this.storageArrays[n3] == null) {
                    this.storageArrays[n3] = new ChunkSection(n3 << 4, bl2);
                }
                object = this.storageArrays[n3].getBlockLSBArray();
                logger.info(byArray.length + " " + n4 + " " + ((byte[])object).length);
                System.arraycopy(byArray, n4, object, 0, ((byte[])object).length);
                n4 += 256;
                continue;
            }
            if (!bl || this.storageArrays[n3] == null) continue;
            this.storageArrays[n3] = null;
        }
        for (n3 = 0; n3 < this.storageArrays.length; ++n3) {
            if ((n & 1 << n3) == 0 || this.storageArrays[n3] == null) continue;
            object = this.storageArrays[n3].getMetadataArray();
            logger.info(byArray.length + " " + n4 + " " + ((NibbleArray) object).data.length);
            System.arraycopy(byArray, n4, ((NibbleArray) object).data, 0, ((NibbleArray) object).data.length);
            n4 += ((NibbleArray) object).data.length;
        }
        for (n3 = 0; n3 < this.storageArrays.length; ++n3) {
            if ((n & 1 << n3) == 0 || this.storageArrays[n3] == null) continue;
            object = this.storageArrays[n3].getBlocklightArray();
            logger.info(byArray.length + " " + n4 + " " + ((NibbleArray) object).data.length);
            System.arraycopy(byArray, n4, ((NibbleArray) object).data, 0, ((NibbleArray) object).data.length);
            n4 += ((NibbleArray) object).data.length;
        }
        if (bl2) {
            for (n3 = 0; n3 < this.storageArrays.length; ++n3) {
                if ((n & 1 << n3) == 0 || this.storageArrays[n3] == null) continue;
                object = this.storageArrays[n3].getSkylightArray();
                logger.info(byArray.length + " " + n4 + " " + ((NibbleArray) object).data.length);
                System.arraycopy(byArray, n4, ((NibbleArray) object).data, 0, ((NibbleArray) object).data.length);
                n4 += ((NibbleArray) object).data.length;
            }
        }
        for (n3 = 0; n3 < this.storageArrays.length; ++n3) {
            if ((n2 & 1 << n3) != 0) {
                if (this.storageArrays[n3] == null) {
                    n4 += 2048;
                    continue;
                }
                object = this.storageArrays[n3].getBlockMSBArray();
                if (object == null) {
                    object = this.storageArrays[n3].m();
                }
                logger.info(byArray.length + " " + n4 + " " + ((NibbleArray) object).data.length);
                System.arraycopy(byArray, n4, ((NibbleArray) object).data, 0, ((NibbleArray) object).data.length);
                n4 += ((NibbleArray) object).data.length;
                continue;
            }
            if (!bl || this.storageArrays[n3] == null || this.storageArrays[n3].getBlockMSBArray() == null) continue;
            this.storageArrays[n3].h();
        }
        if (bl) {
            System.arraycopy(byArray, n4, this.blockBiomeArray, 0, this.blockBiomeArray.length);
            n4 += this.blockBiomeArray.length;
        }
        if (this.hasSkylight()) {
            for (int i = 0; i < this.skylight_bottom.length; ++i) {
                this.skylight_bottom[i] = byArray[n4 + i] & 0xFF;
            }
        }
        for (n3 = 0; n3 < this.storageArrays.length; ++n3) {
            if (this.storageArrays[n3] == null || (n & 1 << n3) == 0) continue;
            this.storageArrays[n3].removeInvalidBlocks();
        }
        this.generateHeightMap(true);
        for (Object tileEntity : this.chunkTileEntityMap.values()) {
            ((TileEntity)tileEntity).updateContainingBlockInfo();
        }
    }
    @Shadow
    private void generateHeightMap(boolean b) {
    }
    
    @Shadow
    private boolean hasSkylight() {
        return false;
    }
    
}
