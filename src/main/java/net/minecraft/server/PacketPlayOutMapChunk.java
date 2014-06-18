package net.minecraft.server;

import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class PacketPlayOutMapChunk
        extends Packet {
    private static byte[] buildBuffer = new byte[196864];
    private int a;
    private int b;
    private int c;
    private int d;
    private byte[] e;
    private byte[] buffer;
    private boolean inflatedBuffer;
    private int size;

    public PacketPlayOutMapChunk() {
    }

    public PacketPlayOutMapChunk(Chunk _Chunk, boolean _Boolean, int _Int) {
        this.a = _Chunk.locX;
        this.b = _Chunk.locZ;
        this.inflatedBuffer = _Boolean;

        ChunkMap localChunkMap = a(_Chunk, _Boolean, _Int);
        Deflater localDeflater = new Deflater(-1);
        this.d = localChunkMap.c;
        this.c = localChunkMap.b;
        try {
            this.buffer = localChunkMap.a;
            localDeflater.setInput(localChunkMap.a, 0, localChunkMap.a.length);
            localDeflater.finish();
            this.e = new byte[localChunkMap.a.length];
            this.size = localDeflater.deflate(this.e);
        } finally {
            localDeflater.end();
        }
    }

    public static int c() {
        return 196864;
    }

    public static ChunkMap a(Chunk _Chunk, boolean _Boolean, int _Int) {
        int i = 0;
        ChunkSection[] arrayOfChunkSection = _Chunk.i();
        int j = 0;
        ChunkMap localChunkMap = new ChunkMap();
        byte[] arrayOfByte1 = buildBuffer;
        if (_Boolean) {
            _Chunk.q = true;
        }
        for (int k = 0; k < arrayOfChunkSection.length; k++) {
            if ((arrayOfChunkSection[k] != null) && ((!_Boolean) || (!arrayOfChunkSection[k].isEmpty())) && ((_Int & 1 << k) != 0)) {
                localChunkMap.b |= 1 << k;
                if (arrayOfChunkSection[k].getExtendedIdArray() != null) {
                    localChunkMap.c |= 1 << k;
                    j++;
                }
            }
        }
        for (int k = 0; k < arrayOfChunkSection.length; k++) {
            if ((arrayOfChunkSection[k] != null) && ((!_Boolean) || (!arrayOfChunkSection[k].isEmpty())) && ((_Int & 1 << k) != 0)) {
                byte[] localObject = arrayOfChunkSection[k].getIdArray();
                System.arraycopy(localObject, 0, arrayOfByte1, i, localObject.length);
                i += localObject.length;
            }
        }

        for (int k = 0; k < arrayOfChunkSection.length; k++) {
            if ((arrayOfChunkSection[k] != null) && ((!_Boolean) || (!arrayOfChunkSection[k].isEmpty())) && ((_Int & 1 << k) != 0)) {
                NibbleArray localObject = arrayOfChunkSection[k].getDataArray();
                System.arraycopy(((NibbleArray) localObject).a, 0, arrayOfByte1, i, ((NibbleArray) localObject).a.length);
                i += ((NibbleArray) localObject).a.length;
            }
        }
        for (int k = 0; k < arrayOfChunkSection.length; k++) {
            if ((arrayOfChunkSection[k] != null) && ((!_Boolean) || (!arrayOfChunkSection[k].isEmpty())) && ((_Int & 1 << k) != 0)) {
                NibbleArray localObject = arrayOfChunkSection[k].getEmittedLightArray();
                System.arraycopy(((NibbleArray) localObject).a, 0, arrayOfByte1, i, ((NibbleArray) localObject).a.length);
                i += ((NibbleArray) localObject).a.length;
            }
        }
        if (!_Chunk.world.worldProvider.g) {
            for (int k = 0; k < arrayOfChunkSection.length; k++) {
                if ((arrayOfChunkSection[k] != null) && ((!_Boolean) || (!arrayOfChunkSection[k].isEmpty())) && ((_Int & 1 << k) != 0)) {
                    NibbleArray localObject = arrayOfChunkSection[k].getSkyLightArray();
                    System.arraycopy(((NibbleArray) localObject).a, 0, arrayOfByte1, i, ((NibbleArray) localObject).a.length);
                    i += ((NibbleArray) localObject).a.length;
                }
            }
        }
        if (j > 0) {
            for (int k = 0; k < arrayOfChunkSection.length; k++) {
                if ((arrayOfChunkSection[k] != null) && ((!_Boolean) || (!arrayOfChunkSection[k].isEmpty())) && (arrayOfChunkSection[k].getExtendedIdArray() != null) && ((_Int & 1 << k) != 0)) {
                    NibbleArray localObject = arrayOfChunkSection[k].getExtendedIdArray();
                    System.arraycopy(((NibbleArray) localObject).a, 0, arrayOfByte1, i, ((NibbleArray) localObject).a.length);
                    i += ((NibbleArray) localObject).a.length;
                }
            }
        }
        if (_Boolean) {
            byte[] arrayOfByte2 = _Chunk.m();
            System.arraycopy(arrayOfByte2, 0, arrayOfByte1, i, arrayOfByte2.length);
            i += arrayOfByte2.length;
        }
        localChunkMap.a = new byte[i];
        System.arraycopy(arrayOfByte1, 0, localChunkMap.a, 0, i);

        return localChunkMap;
    }

    public void a(PacketDataSerializer _PacketDataSerializer) throws IOException {
        this.a = _PacketDataSerializer.readInt();
        this.b = _PacketDataSerializer.readInt();
        this.inflatedBuffer = _PacketDataSerializer.readBoolean();
        this.c = _PacketDataSerializer.readShort();
        this.d = _PacketDataSerializer.readShort();

        this.size = _PacketDataSerializer.readInt();
        if (buildBuffer.length < this.size) {
            buildBuffer = new byte[this.size];
        }
        _PacketDataSerializer.readBytes(buildBuffer, 0, this.size);

        int i = 0;
        for (int j = 0; j < 16; j++) {
            i += (this.c >> j & 0x1);
        }
        int j = 12288 * i;
        if (this.inflatedBuffer) {
            j += 256;
        }
        this.buffer = new byte[j];

        Inflater localInflater = new Inflater();
        localInflater.setInput(buildBuffer, 0, this.size);
        try {
            localInflater.inflate(this.buffer);
        } catch (DataFormatException localDataFormatException) {
            throw new IOException("Bad compressed data format");
        } finally {
            localInflater.end();
        }
    }

    public void b(PacketDataSerializer _PacketDataSerializer) {
        _PacketDataSerializer.writeInt(this.a);
        _PacketDataSerializer.writeInt(this.b);
        _PacketDataSerializer.writeBoolean(this.inflatedBuffer);
        _PacketDataSerializer.writeShort((short) (this.c & 0xFFFF));
        _PacketDataSerializer.writeShort((short) (this.d & 0xFFFF));

        _PacketDataSerializer.writeInt(this.size);
        _PacketDataSerializer.writeBytes(this.e, 0, this.size);
    }

    public void a(PacketPlayOutListener _PacketPlayOutListener) {
        _PacketPlayOutListener.a(this);
    }

    public String b() {
        return String.format("x=%d, z=%d, full=%b, sects=%d, add=%d, size=%d", new Object[]{Integer.valueOf(this.a), Integer.valueOf(this.b), Boolean.valueOf(this.inflatedBuffer), Integer.valueOf(this.c), Integer.valueOf(this.d), Integer.valueOf(this.size)});
    }

    public void handle(PacketListener packetlistener) {
        this.a((PacketPlayOutListener) packetlistener);
    }
}
