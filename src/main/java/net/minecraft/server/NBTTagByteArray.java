package net.minecraft.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NBTTagByteArray
        extends NBTBase {
    private byte[] data;

    NBTTagByteArray() {
    }

    public NBTTagByteArray(byte[] _ArrayOfByte) {
        this.data = _ArrayOfByte;
    }

    void write(DataOutput _DataOutput) throws IOException {
        _DataOutput.writeInt(this.data.length);
        _DataOutput.write(this.data);
    }

    void load(DataInput _DataInput, int _Int, NBTReadLimiter _NBTReadLimiter) throws IOException {
        int i = _DataInput.readInt();
        _NBTReadLimiter.a(8 * i);
        this.data = new byte[i];
        _DataInput.readFully(this.data);

    }

    public byte getTypeId() {
        return 7;
    }

    public String toString() {
        return "[" + this.data.length + " bytes]";
    }

    public NBTBase clone() {
        byte[] arrayOfByte = new byte[this.data.length];
        System.arraycopy(this.data, 0, arrayOfByte, 0, this.data.length);
        return new NBTTagByteArray(arrayOfByte);
    }

    public boolean equals(Object _Object) {
        if (super.equals(_Object)) {
            return Arrays.equals(this.data, ((NBTTagByteArray) _Object).data);
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.data);
    }

    public byte[] c() {
        return this.data;
    }
}
