package net.minecraft.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class NBTBase {
    public static final String[] a = {"END", "BYTE", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE", "BYTE[]", "STRING", "LIST", "COMPOUND", "INT[]"};

    protected NBTBase() {
    }

    protected static NBTBase createTag(byte _Byte) {
        switch (_Byte) {
            case 0:
                return new NBTTagEnd();
            case 1:
                return new NBTTagByte();
            case 2:
                return new NBTTagShort();
            case 3:
                return new NBTTagInt();
            case 4:
                return new NBTTagLong();
            case 5:
                return new NBTTagFloat();
            case 6:
                return new NBTTagDouble();
            case 7:
                return new NBTTagByteArray();
            case 11:
                return new NBTTagIntArray();
            case 8:
                return new NBTTagString();
            case 9:
                return new NBTTagList();
            case 10:
                return new NBTTagCompound();
        }
        return null;
    }

    abstract void write(DataOutput _DataOutput) throws IOException;

    abstract void load(DataInput _DataInput, int _Int, NBTReadLimiter _NBTReadLimiter) throws IOException;

    public abstract String toString();

    public abstract byte getTypeId();

    public abstract NBTBase clone();

    public boolean equals(Object _Object) {
        if (!(_Object instanceof NBTBase)) {
            return false;
        }
        NBTBase localNBTBase = (NBTBase) _Object;
        if (getTypeId() != localNBTBase.getTypeId()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return getTypeId();
    }

    protected String a_() {
        return toString();
    }
}
