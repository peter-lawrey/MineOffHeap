package net.minecraft.server;

public class NibbleArray {
    public final byte[] a;
    private final int b;
    private final int c;

    public NibbleArray(int _Int1, int _Int2) {
        this.a = new byte[_Int1 >> 1];
        this.b = _Int2;
        this.c = (_Int2 + 4);
    }

    public NibbleArray(byte[] _ArrayOfByte, int _Int) {
        this.a = _ArrayOfByte;
        this.b = _Int;
        this.c = (_Int + 4);
    }

    public int a(int _Int1, int _Int2, int _Int3) {
        int i = _Int2 << this.c | _Int3 << this.b | _Int1;
        int j = i >> 1;
        int k = i & 0x1;
        if (k == 0) {
            return this.a[j] & 0xF;
        }
        return this.a[j] >> 4 & 0xF;
    }

    public void a(int _Int1, int _Int2, int _Int3, int _Int4) {
        int i = _Int2 << this.c | _Int3 << this.b | _Int1;

        int j = i >> 1;
        int k = i & 0x1;
        if (k == 0) {
            this.a[j] = ((byte) (this.a[j] & 0xF0 | _Int4 & 0xF));
        } else {
            this.a[j] = ((byte) (this.a[j] & 0xF | (_Int4 & 0xF) << 4));
        }
    }
}
