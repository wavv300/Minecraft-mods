package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class BlockLog2 extends BlockLog
{
    public static boolean rotate = true;

    protected BlockLog2(int par1)
    {
        super(par1);
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving)
    {
        if (rotate){
            super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLiving);
            return;
        }
        int i = par1World.getBlockMetadata(par2, par3, par4) & 3;
        par1World.setBlockMetadataWithNotify(par2, par3, par4, i | 0);
    }
}
