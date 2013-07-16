package net.minecraft.src.nbxlite.spawners;

import java.lang.reflect.Constructor;
import java.util.*;
import net.minecraft.src.nbxlite.oldbiomes.*;
import net.minecraft.src.*;

public final class SpawnerAnimalsBeta
{
    private static Set eligibleChunksForSpawning = new HashSet();
    protected static final Class nightSpawnEntities[];
    private static Map classToStringMapping;

    public SpawnerAnimalsBeta()
    {
    }

    protected static ChunkPosition getRandomSpawningPointInChunk(World world, int i, int j)
    {
        int k = i + world.rand.nextInt(16);
        int l = world.rand.nextInt(OldSpawnerAnimals.getWorldHeight(world, i, j));
        int i1 = j + world.rand.nextInt(16);
        return new ChunkPosition(k, l, i1);
    }

    public static final int performSpawning(World var0, boolean var1, boolean var2) 
    {
        if(!var1 && !var2)
        {
            return 0;
        }
        else
        {
            eligibleChunksForSpawning.clear();

            int var3;
            int var6;
            for(var3 = 0; var3 < var0.playerEntities.size(); ++var3)
            {
                EntityPlayer var4 = (EntityPlayer)var0.playerEntities.get(var3);
                int var5 = MathHelper.floor_double(var4.posX / 16.0D);
                var6 = MathHelper.floor_double(var4.posZ / 16.0D);
                byte var7 = 8;

                for(int var8 = -var7; var8 <= var7; ++var8)
                {
                    for(int var9 = -var7; var9 <= var7; ++var9)
                    {
                        eligibleChunksForSpawning.add(new ChunkCoordIntPair(var8 + var5, var9 + var6));
                    }
                }
            }

            var3 = 0;
            ChunkCoordinates var35 = var0.getSpawnPoint();
            EnumCreatureType[] var36 = EnumCreatureType.values();
            var6 = var36.length;

            for(int var37 = 0; var37 < var6; ++var37)
            {
                EnumCreatureType var38 = var36[var37];
                if((!var38.getPeacefulCreature() || var2) && (var38.getPeacefulCreature() || var1) && var0.countEntities2(var38.getCreatureClass()) <= var38.getMaxNumberOfCreature() * eligibleChunksForSpawning.size() / 256)
                {
                    Iterator var39 = eligibleChunksForSpawning.iterator();

                    label113:
                    while(var39.hasNext())
                    {
                        ChunkCoordIntPair var10 = (ChunkCoordIntPair)var39.next();
                        OldBiomeGenBase var11 = var0.getWorldChunkManager().oldGetBiomeGenAtChunkCoord(var10);
                        List var12 = cleanSpawnList(var11.getSpawnableList(var38), var0.provider.dimensionId);
                        if(var12 != null && !var12.isEmpty())
                        {
                            int var13 = 0;

                            SpawnListEntryBeta var15;
                            for(Iterator var14 = var12.iterator(); var14.hasNext(); var13 += var15.spawnRarityRate)
                            {
                                var15 = (SpawnListEntryBeta)var14.next();
                            }

                            int var40 = var0.rand.nextInt(var13);
                            var15 = (SpawnListEntryBeta)var12.get(0);
                            Iterator var16 = var12.iterator();

                            while(var16.hasNext())
                            {
                                SpawnListEntryBeta var17 = (SpawnListEntryBeta)var16.next();
                                var40 -= var17.spawnRarityRate;
                                if(var40 < 0)
                                {
                                    var15 = var17;
                                    break;
                                }
                            }
                            EntityLivingData entitylivingdata = null;

                            ChunkPosition var41 = getRandomSpawningPointInChunk(var0, var10.chunkXPos * 16, var10.chunkZPos * 16);
                            int var42 = var41.x;
                            int var18 = var41.y;
                            int var19 = var41.z;
                            if(!var0.isBlockNormalCube(var42, var18, var19) && var0.getBlockMaterial(var42, var18, var19) == var38.getCreatureMaterial())
                            {
                                int var20 = 0;

                                for(int var21 = 0; var21 < 3; ++var21)
                                {
                                    int var22 = var42;
                                    int var23 = var18;
                                    int var24 = var19;
                                    byte var25 = 6;

                                    for(int var26 = 0; var26 < 4; ++var26)
                                    {
                                        var22 += var0.rand.nextInt(var25) - var0.rand.nextInt(var25);
                                        var23 += var0.rand.nextInt(1) - var0.rand.nextInt(1);
                                        var24 += var0.rand.nextInt(var25) - var0.rand.nextInt(var25);
                                        if(canCreatureTypeSpawnAtLocation(var38, var0, var22, var23, var24))
                                        {
                                            float var27 = (float)var22 + 0.5F;
                                            float var28 = (float)var23;
                                            float var29 = (float)var24 + 0.5F;
                                            if(var0.getClosestPlayer((double)var27, (double)var28, (double)var29, 24.0D) == null)
                                            {
                                                float var30 = var27 - (float)var35.posX;
                                                float var31 = var28 - (float)var35.posY;
                                                float var32 = var29 - (float)var35.posZ;
                                                float var33 = var30 * var30 + var31 * var31 + var32 * var32;
                                                if(var33 >= 576.0F)
                                                {
                                                    EntityLiving var43;
                                                    try
                                                    {
                                                        var43 = (EntityLiving)var15.entityClass.getConstructor(new Class[]{World.class}).newInstance(new Object[]{var0});
                                                    }
                                                    catch (Exception var34)
                                                    {
                                                        var34.printStackTrace();
                                                        return var3;
                                                    }

                                                    var43.setLocationAndAngles((double)var27, (double)var28, (double)var29, var0.rand.nextFloat() * 360.0F, 0.0F);

                                                    if(var43.getCanSpawnHere())
                                                    {
                                                        ++var20;
                                                        var0.spawnEntityInWorld(var43);
                                                        entitylivingdata = var43.func_110161_a(entitylivingdata);
                                                        if(var20 >= var43.getMaxSpawnedInChunk())
                                                        {
                                                            continue label113;
                                                        }
                                                    }

                                                    var3 += var20;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return var3;
        }
    }

    private static boolean canCreatureTypeSpawnAtLocation(EnumCreatureType enumcreaturetype, World world, int i, int j, int k)
    {
        if(enumcreaturetype.getCreatureMaterial() == Material.water)
        {
            return world.getBlockMaterial(i, j, k).isLiquid() && !world.isBlockNormalCube(i, j + 1, k);
        } else
        {
            return world.isBlockNormalCube(i, j - 1, k) && !world.isBlockNormalCube(i, j, k) && !world.getBlockMaterial(i, j, k).isLiquid() && !world.isBlockNormalCube(i, j + 1, k);
        }
    }
/*
    public static boolean performSleepSpawning(World world, List list)
    {
        int height = 256;
        boolean flag = false;
        PathFinder pathfinder = new PathFinder(world);
        Iterator iterator = list.iterator();
        do
        {
            if(!iterator.hasNext())
            {
                break;
            }
            EntityPlayer entityplayer = (EntityPlayer)iterator.next();
            Class aclass[] = nightSpawnEntities;
            if(aclass != null && aclass.length != 0)
            {
                boolean flag1 = false;
                int i = 0;
                while(i < 20 && !flag1) 
                {
                    int j = (MathHelper.floor_double(entityplayer.posX) + world.rand.nextInt(32)) - world.rand.nextInt(32);
                    int k = (MathHelper.floor_double(entityplayer.posZ) + world.rand.nextInt(32)) - world.rand.nextInt(32);
                    int l = (MathHelper.floor_double(entityplayer.posY) + world.rand.nextInt(16)) - world.rand.nextInt(16);
                    if(l < 1)
                    {
                        l = 1;
                    } else
                    if(l > height)
                    {
                        l = height;
                    }
                    int i1 = world.rand.nextInt(aclass.length);
                    int j1;
                    for(j1 = l; j1 > 2 && !world.isBlockNormalCube(j, j1 - 1, k); j1--) { }
                    for(; !canCreatureTypeSpawnAtLocation(EnumCreatureType.monster, world, j, j1, k) && j1 < l + 16 && j1 < height; j1++) { }
                    if(j1 >= l + 16 || j1 >= height)
                    {
                        j1 = l;
                    } else
                    {
                        float f = (float)j + 0.5F;
                        float f1 = j1;
                        float f2 = (float)k + 0.5F;
                        EntityLiving entityliving;
                        try
                        {
                            entityliving = (EntityLiving)aclass[i1].getConstructor(new Class[] {
                                net.minecraft.src.World.class
                            }).newInstance(new Object[] {
                                world
                            });
                        }
                        catch(Exception exception)
                        {
                            exception.printStackTrace();
                            return flag;
                        }
                        EntityLivingData entitylivingdata = null;
                        entityliving.setLocationAndAngles(f, f1, f2, world.rand.nextFloat() * 360F, 0.0F);
                        if(entityliving.getCanSpawnHere())
                        {
                            PathEntity pathentity = pathfinder.createEntityPathTo(entityliving, entityplayer, 32F);
                            if(pathentity != null && pathentity.pathLength > 1)
                            {
                                PathPoint pathpoint = pathentity.func_22328_c();
                                if(Math.abs((double)pathpoint.xCoord - entityplayer.posX) < 1.5D && Math.abs((double)pathpoint.zCoord - entityplayer.posZ) < 1.5D && Math.abs((double)pathpoint.yCoord - entityplayer.posY) < 1.5D)
                                {
                                    ChunkCoordinates chunkcoordinates = BlockBed.getNearestEmptyChunkCoordinates(world, MathHelper.floor_double(entityplayer.posX), MathHelper.floor_double(entityplayer.posY), MathHelper.floor_double(entityplayer.posZ), 1);
                                    if(chunkcoordinates == null)
                                    {
                                        chunkcoordinates = new ChunkCoordinates(j, j1 + 1, k);
                                    }
                                    entityliving.setLocationAndAngles((float)chunkcoordinates.posX + 0.5F, chunkcoordinates.posY, (float)chunkcoordinates.posZ + 0.5F, 0.0F, 0.0F);
                                    world.spawnEntityInWorld(entityliving);
                                    entitylivingdata = entityliving.func_110161_a(entitylivingdata);
                                    entityplayer.wakeUpPlayer(true, false, false);
                                    entityliving.playLivingSound();
                                    flag = true;
                                    flag1 = true;
                                }
                            }
                        }
                    }
                    i++;
                }
            }
        } while(true);
        return flag;
    }
*/

    private static List cleanSpawnList(List spawnSubclasses, int dimensionId){
        List list = new ArrayList();
        for (Object o : spawnSubclasses){
            SpawnListEntryBeta s = (SpawnListEntryBeta)o;
            String str = (String)(classToStringMapping.get(s.entityClass));
            if (EntityLiving.allow(str, dimensionId)){
                list.add(s);
            }
        }
        return list;
    }

    static 
    {
        nightSpawnEntities = (new Class[] {
            net.minecraft.src.EntitySpider.class, net.minecraft.src.EntityZombie.class, net.minecraft.src.EntitySkeleton.class
        });
        classToStringMapping = (Map)(mod_OldDays.getField(EntityList.class, null, 1));
    }
}
