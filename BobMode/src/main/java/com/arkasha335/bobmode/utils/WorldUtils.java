package com.Arkasha335.bobmode.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class WorldUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    /**
     * Checks if a solid block exists at the position directly below the player.
     * This is crucial for reactive bridging logic.
     * @return true if a non-air block is under the player's feet.
     */
    public static boolean isBlockPlacedUnderPlayer() {
        if (mc.thePlayer == null || mc.theWorld == null) {
            return false;
        }
        BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        BlockPos blockBelow = playerPos.down();
        return mc.theWorld.getBlockState(blockBelow).getBlock() != Blocks.air;
    }
}