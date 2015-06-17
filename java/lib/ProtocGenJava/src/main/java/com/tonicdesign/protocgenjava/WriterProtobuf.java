package com.tonicdesign.protocgenjava;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.Map;

public class WriterProtobuf
implements
    Writer {

    private ByteArrayOutputStream mByteOutputStream;

    public static WriterProtobuf withCapacity(int capacity) {
        return new WriterProtobuf(capacity);
    }

    public WriterProtobuf(int capacity) {
        mByteOutputStream = new ByteArrayOutputStream(capacity);
    }

    public byte[] toBuffer() {
        return mByteOutputStream.toByteArray();
    }

    public void writeTag(int tag) {
        writeVarUInt(tag);
    }

    public void writeByte(byte v) {
        mByteOutputStream.write(v);
    }

    public void writeVarInt(long v) {
        writeVarUInt(v);
    }

    public void writeVarUInt(long v) {
        long x = v;
        while (x > 0x7F) {
            writeByte((byte) (x & 0x7F | 0x80));
            x >>= 7;
        }
        writeByte((byte) x);
    }

    public void writeBool(boolean v) {
        writeByte((byte) (v ? 1 : 0));
    }

    public void writeFloat32(float v) {
        int b = Float.floatToIntBits(v);
        writeByte((byte)((b >> 0x00) & 0xFF));
        writeByte((byte)((b >> 0x08) & 0xFF));
        writeByte((byte)((b >> 0x10) & 0xFF));
        writeByte((byte)((b >> 0x18) & 0xFF));
    }

    public void writeFloat64(double v) {
        long b = Double.doubleToLongBits(v);
        writeByte((byte)((b >> 0x00) & 0xFF));
        writeByte((byte)((b >> 0x08) & 0xFF));
        writeByte((byte)((b >> 0x10) & 0xFF));
        writeByte((byte)((b >> 0x18) & 0xFF));
        writeByte((byte)((b >> 0x20) & 0xFF));
        writeByte((byte)((b >> 0x28) & 0xFF));
        writeByte((byte)((b >> 0x30) & 0xFF));
        writeByte((byte)((b >> 0x38) & 0xFF));
    }

    public void writeString(String v) {
        byte[] stringBytes = v.getBytes(Charset.forName("UTF-8"));
        writeVarInt((long)stringBytes.length);
        mByteOutputStream.write(stringBytes, 0, stringBytes.length);
    }

    public void pushTagMap(Map<Integer, TagMapValue> map) {
        // Nothing
    }

    public void popTagMap() {
        // Nothing
    }
}
