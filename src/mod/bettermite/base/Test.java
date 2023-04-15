package mod.bettermite.base;

import com.google.common.eventbus.Subscribe;
import net.minecraft.*;
import net.xiaoyu233.fml.reload.event.PlayerLoggedInEvent;

import static mod.bettermite.base.block.Blocks.block1280;

public class Test {
    @Subscribe
    public void onPlayerLoggedIn(PlayerLoggedInEvent playerLoggedInEvent) {
        playerLoggedInEvent.getPlayer().worldObj.getAsWorldServer().spawnEntityInWorld(new EntityItem(playerLoggedInEvent.getPlayer().worldObj,playerLoggedInEvent.getPlayer().posX,playerLoggedInEvent.getPlayer().posY,playerLoggedInEvent.getPlayer().posZ,new ItemStack(new ItemBlock(block1280),1)));
    }
}
