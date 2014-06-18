package net.minecraft.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NBTTagCompound
        extends NBTBase {
    private Map<String, NBTBase> map = new HashMap();

    public NBTTagCompound() {
    }

    private static void a(String string, NBTBase _NBTBase, DataOutput _DataOutput) throws IOException {
        _DataOutput.writeByte(_NBTBase.getTypeId());
        if (_NBTBase.getTypeId() == 0) {
            return;
        }
        _DataOutput.writeUTF(string);

        _NBTBase.write(_DataOutput);
    }

    private static byte a(DataInput _DataInput, NBTReadLimiter _NBTReadLimiter) throws IOException {
        return _DataInput.readByte();
    }

    private static String b(DataInput _DataInput, NBTReadLimiter _NBTReadLimiter) throws IOException {
        return _DataInput.readUTF();
    }

    static NBTBase a(byte _Byte, String string, DataInput _DataInput, int _Int, NBTReadLimiter _NBTReadLimiter) {
        NBTBase localNBTBase = NBTBase.createTag(_Byte);
        try {
            localNBTBase.load(_DataInput, _Int, _NBTReadLimiter);
        } catch (IOException localIOException) {
            CrashReport localCrashReport = CrashReport.a(localIOException, "Loading NBT data");
            CrashReportSystemDetails localCrashReportSystemDetails = localCrashReport.a("NBT Tag");
            localCrashReportSystemDetails.a("Tag name", string);
            localCrashReportSystemDetails.a("Tag type", Byte.valueOf(_Byte));
            throw new ReportedException(localCrashReport);
        }
        return localNBTBase;
    }

    void write(DataOutput _DataOutput) throws IOException {
        for (String str : this.map.keySet()) {
            NBTBase localNBTBase = this.map.get(str);
            a(str, localNBTBase, _DataOutput);
        }
        _DataOutput.writeByte(0);
    }

    void load(DataInput _DataInput, int _Int, NBTReadLimiter _NBTReadLimiter) throws IOException {
        if (_Int > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        }
        this.map.clear();
        byte b1;
        while ((b1 = a(_DataInput, _NBTReadLimiter)) != 0) {
            String str = b(_DataInput, _NBTReadLimiter);

            _NBTReadLimiter.a(16 * str.length());
            NBTBase localNBTBase = a(b1, str, _DataInput, _Int + 1, _NBTReadLimiter);
            this.map.put(str, localNBTBase);
        }
    }

    public Set c() {
        return this.map.keySet();
    }

    public byte getTypeId() {
        return 10;
    }

    public void set(String string, NBTBase _NBTBase) {
        this.map.put(string, _NBTBase);
    }

    public void setByte(String string, byte _Byte) {
        this.map.put(string, new NBTTagByte(_Byte));
    }

    public void setShort(String string, short s) {
        this.map.put(string, new NBTTagShort(s));
    }

    public void setInt(String string, int _Int) {
        this.map.put(string, new NBTTagInt(_Int));
    }

    public void setLong(String string, long _Long) {
        this.map.put(string, new NBTTagLong(_Long));
    }

    public void setFloat(String string, float _Float) {
        this.map.put(string, new NBTTagFloat(_Float));
    }

    public void setDouble(String string, double _Double) {
        this.map.put(string, new NBTTagDouble(_Double));
    }

    public void setString(String string1, String string2) {
        this.map.put(string1, new NBTTagString(string2));
    }

    public void setByteArray(String string, byte[] _ArrayOfByte) {
        this.map.put(string, new NBTTagByteArray(_ArrayOfByte));
    }

    public void setIntArray(String string, int[] _ArrayOfInt) {
        this.map.put(string, new NBTTagIntArray(_ArrayOfInt));
    }

    public void setBoolean(String string, boolean _Boolean) {
        setByte(string, (byte) (_Boolean ? 1 : 0));
    }

    public NBTBase get(String string) {
        return (NBTBase) this.map.get(string);
    }

    public byte b(String string) {
        NBTBase localNBTBase = (NBTBase) this.map.get(string);
        if (localNBTBase != null) {
            return localNBTBase.getTypeId();
        }
        return 0;
    }

    public boolean hasKey(String string) {
        return this.map.containsKey(string);
    }

    public boolean hasKeyOfType(String string, int _Int) {
        int i = b(string);
        if (i == _Int) {
            return true;
        }
        if (_Int == 99) {
            return (i == 1) || (i == 2) || (i == 3) || (i == 4) || (i == 5) || (i == 6);
        }
        return false;
    }

    public byte getByte(String string) {
        try {
            if (!this.map.containsKey(string)) {
                return 0;
            }
            return ((NBTNumber) this.map.get(string)).f();
        } catch (ClassCastException ignored) {
        }
        return 0;
    }

    public short getShort(String string) {
        try {
            if (!this.map.containsKey(string)) {
                return 0;
            }
            return ((NBTNumber) this.map.get(string)).e();
        } catch (ClassCastException ignored) {
        }
        return 0;
    }

    public int getInt(String string) {
        try {
            if (!this.map.containsKey(string)) {
                return 0;
            }
            return ((NBTNumber) this.map.get(string)).d();
        } catch (ClassCastException ignored) {
        }
        return 0;
    }

    public long getLong(String string) {
        try {
            if (!this.map.containsKey(string)) {
                return 0L;
            }
            return ((NBTNumber) this.map.get(string)).c();
        } catch (ClassCastException ignored) {
        }
        return 0L;
    }

    public float getFloat(String string) {
        try {
            if (!this.map.containsKey(string)) {
                return 0.0F;
            }
            return ((NBTNumber) this.map.get(string)).h();
        } catch (ClassCastException ignored) {
        }
        return 0.0F;
    }

    public double getDouble(String string) {
        try {
            if (!this.map.containsKey(string)) {
                return 0.0D;
            }
            return ((NBTNumber) this.map.get(string)).g();
        } catch (ClassCastException ignored) {
        }
        return 0.0D;
    }

    public String getString(String string) {
        try {
            if (!this.map.containsKey(string)) {
                return "";
            }
            return ((NBTBase) this.map.get(string)).a_();
        } catch (ClassCastException ignored) {
        }
        return "";
    }

    public byte[] getByteArray(String string) {
        try {
            if (!this.map.containsKey(string)) {
                return new byte[0];
            }
            return ((NBTTagByteArray) this.map.get(string)).c();
        } catch (ClassCastException e) {
            throw new ReportedException(a(string, 7, e));
        }
    }

    public int[] getIntArray(String string) {
        try {
            if (!this.map.containsKey(string)) {
                return new int[0];
            }
            return ((NBTTagIntArray) this.map.get(string)).c();
        } catch (ClassCastException e) {
            throw new ReportedException(a(string, 11, e));
        }
    }

    public NBTTagCompound getCompound(String string) {
        try {
            if (!this.map.containsKey(string)) {
                return new NBTTagCompound();
            }
            return (NBTTagCompound) this.map.get(string);
        } catch (ClassCastException e) {
            throw new ReportedException(a(string, 10, e));
        }
    }

    public NBTTagList getList(String string, int _Int) {
        try {
            if (b(string) != 9) {
                return new NBTTagList();
            }
            NBTTagList localNBTTagList = (NBTTagList) this.map.get(string);
            if ((localNBTTagList.size() > 0) && (localNBTTagList.d() != _Int)) {
                return new NBTTagList();
            }
            return localNBTTagList;
        } catch (ClassCastException e) {
            throw new ReportedException(a(string, 9, e));
        }
    }

    public boolean getBoolean(String string) {
        return getByte(string) != 0;
    }

    public void remove(String string) {
        this.map.remove(string);
    }

    public String toString() {
        String str1 = "{";
        for (String str2 : this.map.keySet()) {
            str1 = str1 + str2 + ':' + this.map.get(str2) + ',';
        }
        return str1 + "}";
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    private CrashReport a(String string, int _Int, ClassCastException _ClassCastException) {
        CrashReport localCrashReport = CrashReport.a(_ClassCastException, "Reading NBT data");
        CrashReportSystemDetails localCrashReportSystemDetails = localCrashReport.a("Corrupt NBT tag", 1);

        localCrashReportSystemDetails.a("Tag type found", new CrashReportCorruptNBTTag(this, string));


        localCrashReportSystemDetails.a("Tag type expected", new CrashReportCorruptNBTTag2(this, _Int));


        localCrashReportSystemDetails.a("Tag name", string);

        return localCrashReport;
    }

    public NBTBase clone() {
        NBTTagCompound localNBTTagCompound = new NBTTagCompound();
        for (String str : this.map.keySet()) {
            localNBTTagCompound.set(str, ((NBTBase) this.map.get(str)).clone());
        }
        return localNBTTagCompound;
    }

    public boolean equals(Object _Object) {
        if (super.equals(_Object)) {
            NBTTagCompound localNBTTagCompound = (NBTTagCompound) _Object;
            return this.map.entrySet().equals(localNBTTagCompound.map.entrySet());
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode() ^ this.map.hashCode();
    }
}
