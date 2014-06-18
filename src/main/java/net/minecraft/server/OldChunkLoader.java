package net.minecraft.server;

public class OldChunkLoader {
    public static OldChunk a(NBTTagCompound _NBTTagCompound) {
        int i = _NBTTagCompound.getInt("xPos");
        int j = _NBTTagCompound.getInt("zPos");

        OldChunk localOldChunk = new OldChunk(i, j);
        localOldChunk.g = _NBTTagCompound.getByteArray("Blocks");
        localOldChunk.f = new OldNibbleArray(_NBTTagCompound.getByteArray("Data"), 7);
        localOldChunk.e = new OldNibbleArray(_NBTTagCompound.getByteArray("SkyLight"), 7);
        localOldChunk.d = new OldNibbleArray(_NBTTagCompound.getByteArray("BlockLight"), 7);
        localOldChunk.c = _NBTTagCompound.getByteArray("HeightMap");
        localOldChunk.b = _NBTTagCompound.getBoolean("TerrainPopulated");
        localOldChunk.h = _NBTTagCompound.getList("Entities", 10);
        localOldChunk.i = _NBTTagCompound.getList("TileEntities", 10);
        localOldChunk.j = _NBTTagCompound.getList("TileTicks", 10);
        try {
            localOldChunk.a = _NBTTagCompound.getLong("LastUpdate");
        } catch (ClassCastException localClassCastException) {
            localOldChunk.a = _NBTTagCompound.getInt("LastUpdate");
        }
        return localOldChunk;
    }

    public static void a(OldChunk _OldChunk, NBTTagCompound _NBTTagCompound, WorldChunkManager _WorldChunkManager) {
        _NBTTagCompound.setInt("xPos", _OldChunk.k);
        _NBTTagCompound.setInt("zPos", _OldChunk.l);
        _NBTTagCompound.setLong("LastUpdate", _OldChunk.a);
        int[] arrayOfInt = new int[_OldChunk.c.length];
        for (int i = 0; i < _OldChunk.c.length; i++) {
            arrayOfInt[i] = _OldChunk.c[i];
        }
        _NBTTagCompound.setIntArray("HeightMap", arrayOfInt);
        _NBTTagCompound.setBoolean("TerrainPopulated", _OldChunk.b);

        NBTTagList localNBTTagList = new NBTTagList();
        for (int j = 0; j < 8; j++) {
            int k = 1;
            int i4;
            for (int m = 0; (m < 16) && (k != 0); m++) {
                for (int i1 = 0; (i1 < 16) && (k != 0); i1++) {
                    for (int i2 = 0; i2 < 16; i2++) {
                        int i3 = m << 11 | i2 << 7 | i1 + (j << 4);
                        i4 = _OldChunk.g[i3];
                        if (i4 != 0) {
                            k = 0;
                            break;
                        }
                    }
                }
            }
            if (k == 0) {
                byte[] arrayOfByte2 = new byte['က'];
                NibbleArray localNibbleArray1 = new NibbleArray(arrayOfByte2.length, 4);
                NibbleArray localNibbleArray2 = new NibbleArray(arrayOfByte2.length, 4);
                NibbleArray localNibbleArray3 = new NibbleArray(arrayOfByte2.length, 4);
                for (i4 = 0; i4 < 16; i4++) {
                    for (int i5 = 0; i5 < 16; i5++) {
                        for (int i6 = 0; i6 < 16; i6++) {
                            int i7 = i4 << 11 | i6 << 7 | i5 + (j << 4);
                            int i8 = _OldChunk.g[i7];

                            arrayOfByte2[(i5 << 8 | i6 << 4 | i4)] = ((byte) (i8 & 0xFF));
                            localNibbleArray1.a(i4, i5, i6, _OldChunk.f.a(i4, i5 + (j << 4), i6));
                            localNibbleArray2.a(i4, i5, i6, _OldChunk.e.a(i4, i5 + (j << 4), i6));
                            localNibbleArray3.a(i4, i5, i6, _OldChunk.d.a(i4, i5 + (j << 4), i6));
                        }
                    }
                }
                NBTTagCompound localNBTTagCompound = new NBTTagCompound();

                localNBTTagCompound.setByte("Y", (byte) (j & 0xFF));
                localNBTTagCompound.setByteArray("Blocks", arrayOfByte2);
                localNBTTagCompound.setByteArray("Data", localNibbleArray1.a);
                localNBTTagCompound.setByteArray("SkyLight", localNibbleArray2.a);
                localNBTTagCompound.setByteArray("BlockLight", localNibbleArray3.a);

                localNBTTagList.add(localNBTTagCompound);
            }
        }
        _NBTTagCompound.set("Sections", localNBTTagList);


        byte[] arrayOfByte1 = new byte[256];
        for (int k = 0; k < 16; k++) {
            for (int n = 0; n < 16; n++) {
                arrayOfByte1[(n << 4 | k)] = ((byte) (_WorldChunkManager.getBiome(_OldChunk.k << 4 | k, _OldChunk.l << 4 | n).id & 0xFF));
            }
        }
        _NBTTagCompound.setByteArray("Biomes", arrayOfByte1);
        _NBTTagCompound.set("Entities", _OldChunk.h);
        _NBTTagCompound.set("TileEntities", _OldChunk.i);
        if (_OldChunk.j != null) {
            _NBTTagCompound.set("TileTicks", _OldChunk.j);
        }
    }
}
