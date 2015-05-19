package com.tonicdesign.protocgenjava;

import java.util.Map;

public class ReaderProtobuf
implements
    Reader {

    private final byte[] mBytes;
    private int mLimit;
    private int mOffset;

    public static ReaderProtobuf fromBuffer(byte[] bytes) {
        return new ReaderProtobuf(bytes);
    }

    public ReaderProtobuf(byte[] bytes) {
        mBytes = bytes;
        mLimit = bytes.length;
    }

    public int readTag() {
        return (int) readVarInt();
    }

    public byte readByte() {
        if (mOffset == mLimit) {
            return 0;
        }
        return mBytes[mOffset++];
    }

    public long readVarInt() {
        return readVarUInt();
    }

    public long readVarUInt() {
        long v = 0;
        long s = 0;
        while (true) {
            byte b = readByte();
            v |= ((long) (b & 0x7F) << s);
            s += 7;
            if (0 == (b & 0x80)) {
                break;
            }
        }
        return v;
    }

    public boolean readBool() {
        return readVarInt() != 0;
    }

    public int readUInt32() {
        int v = readByte() & 0xFF;
        v |= (readByte() & 0xFF) << 0x08;
        v |= (readByte() & 0xFF) << 0x10;
        v |= (readByte() & 0xFF) << 0x18;
        return v;
    }

    public long readUInt64() {
        long v = readByte() & 0xFF;
        v |= (long) (readByte() & 0xFF) << 0x08;
        v |= (long) (readByte() & 0xFF) << 0x10;
        v |= (long) (readByte() & 0xFF) << 0x18;
        v |= (long) (readByte() & 0xFF) << 0x20;
        v |= (long) (readByte() & 0xFF) << 0x28;
        v |= (long) (readByte() & 0xFF) << 0x30;
        v |= (long) (readByte() & 0xFF) << 0x38;
        return v;
    }

    public float readFloat32() {
        return Float.intBitsToFloat(readUInt32());
    }

    public double readFloat64() {
        return Double.longBitsToDouble(readUInt64());
    }

    public String readString() {
        StringBuilder s = new StringBuilder();
        long l = readVarInt();
        while (l > 0) {
            int c = readByte() & 0xFF;
            int v = c >> 4;

            if (v > 0x0D) {
                c = (((c & 0x0F) << 12) | ((readByte() & 0x3F) << 6) | (readByte() & 0x3F));
                l -= 3;
            } else if (v > 0x08) {
                c = (((c & 0x1F) << 6) | (readByte() & 0x3F));
                l -= 2;
            } else {
                l -= 1;
            }

            s.append((char) c);
        }
        return s.toString();
    }

    public int pushLimit(int limit) {
        int oldLimit = mLimit;
        mLimit = mOffset + limit;
        return oldLimit;
    }

    public void popLimit(int limit) {
        mLimit = limit;
    }

    public void pushTagMap(Map<String, TagMapValue> map) {
        // Nothing
    }

    public void popTagMap() {
        // Nothing
    }
}
