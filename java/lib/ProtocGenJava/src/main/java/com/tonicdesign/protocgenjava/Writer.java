package com.tonicdesign.protocgenjava;

import java.util.Map;

public interface Writer {
    byte[] toBuffer();
    void writeTag(int tag);
    void writeByte(byte v);
    void writeVarInt(long v);
    void writeVarUInt(long v);
    void writeBool(boolean v);
    void writeFloat32(float v);
    void writeFloat64(double v);
    void writeString(String v);
    void pushTagMap(Map<Integer, TagMapValue> map);
    void popTagMap();
}
