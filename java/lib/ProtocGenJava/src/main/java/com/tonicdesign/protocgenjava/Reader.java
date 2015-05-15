package com.tonicdesign.protocgenjava;

public interface Reader {
    byte readByte();
    long readVarInt();
    boolean readBool();
    int readUInt32();
    long readUInt64();
    float readFloat32();
    double readFloat64();
    String readString();
    int pushLimit(int limit);
    void popLimit(int limit);
}
