package net.minecraft.src.nbxlite.oldbiomes;

import java.util.List;
import java.util.Random;
import net.minecraft.src.nbxlite.spawners.SpawnListEntryBeta;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenTrees;
import net.minecraft.src.WorldGenerator;
import net.minecraft.src.WorldGenBigTree;
import net.minecraft.src.WorldGenHugeTrees;
import net.minecraft.src.WorldGenShrub;
import net.minecraft.src.WorldGenTrees;
import net.minecraft.src.mod_noBiomesX;

public class OldBiomeGenRainforest extends OldBiomeGenBase
{

    public OldBiomeGenRainforest()
    {
        spawnableCreatureList.add(new SpawnListEntryBeta(net.minecraft.src.EntityOcelot.class, 2));
    }

    public WorldGenerator getRandomWorldGenForTrees(Random random)
    {
        if (mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_JUNGLE){
            if (random.nextInt(10) == 0)
            {
                return new WorldGenBigTree(false);
            }
            if (random.nextInt(2) == 0)
            {
                return new WorldGenShrub(3, 0);
            }
            if (random.nextInt(3) == 0)
            {
                return new WorldGenHugeTrees(false, 10 + random.nextInt(20), 3, 3);
            }
            else
            {
                return new WorldGenTrees(false, 4 + random.nextInt(7), 3, 3, true);
            }
        }else{
            if(random.nextInt(3) == 0)
            {
                return new WorldGenBigTree(false);
            } else
            {
                return new OldWorldGenTrees(false);
            }
        }
    }
}
