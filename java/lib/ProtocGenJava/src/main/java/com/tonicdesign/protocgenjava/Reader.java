package com.tonicdesign.protocgenjava;

import java.util.Map;

public interface Reader {
    int readTag();
    byte readByte();
    long readVarInt();
    long readVarUInt();
    boolean readBool();
    int readUInt32();
    long readUInt64();
    float readFloat32();
    double readFloat64();
    String readString();
    int pushLimit(int limit);
    void popLimit(int limit);
    void pushTagMap(Map<String, TagMapValue> map);
    void popTagMap();
}
