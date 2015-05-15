package com.tonicdesign.protocgenjava;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReaderProtobufTest {

    @Test
    public void testReaderProtobufReadByte() throws Exception {
        byte testByte = 0x4F;
        ReaderProtobuf ReaderProtobuf = new ReaderProtobuf(new byte[]{ testByte });
        assertEquals(testByte, ReaderProtobuf.readByte());
    }

    @Test
    public void testReaderProtobufReadVarIntUnder8Bits() throws Exception {
        ReaderProtobuf ReaderProtobuf = new ReaderProtobuf(new byte[]{(byte) 0x0C});
        assertEquals(12L, ReaderProtobuf.readVarInt());
    }

    @Test
    public void testReaderProtobufReadVarIntUnder15Bits() throws Exception {
        ReaderProtobuf ReaderProtobuf = new ReaderProtobuf(new byte[]{(byte) 0x8C, (byte) 0x04});
        assertEquals(524L, ReaderProtobuf.readVarInt());
    }

    @Test
    public void testReaderProtobufReadVarInt64Bits() throws Exception {
        ReaderProtobuf ReaderProtobuf = new ReaderProtobuf(new byte[]{(byte) 0x84, (byte) 0x80, (byte) 0x81, (byte) 0x88, (byte) 0x80, (byte) 0x84, (byte) 0x80, (byte) 0x88, (byte) 0x28 });
        assertEquals(2886807498600235012L, ReaderProtobuf.readVarInt());
    }

    @Test
    public void testReaderProtobufReadBool() throws Exception {
        ReaderProtobuf ReaderProtobuf = new ReaderProtobuf(new byte[]{(byte) 0x01});
        assertEquals(true, ReaderProtobuf.readBool());
    }

    @Test
    public void testReaderProtobufReadFloat32() throws Exception {
        ReaderProtobuf ReaderProtobuf = new ReaderProtobuf(new byte[]{(byte) 0x0A, (byte) 0xD7, (byte) 0x43, (byte) 0x41});
        assertEquals(12.24f, ReaderProtobuf.readFloat32(), 0.00001);
    }

    @Test
    public void testReaderProtobufReadFloat64() throws Exception {
        ReaderProtobuf ReaderProtobuf = new ReaderProtobuf(new byte[]{(byte) 0x7B, (byte) 0x14, (byte) 0xAE, (byte) 0x47, (byte) 0xE1, (byte) 0x7A, (byte) 0x28, (byte) 0x40});
        assertEquals(12.24, ReaderProtobuf.readFloat64(), 0.00001);
    }

    @Test
    public void testReaderProtobufReadString() throws Exception {
        ReaderProtobuf ReaderProtobuf = new ReaderProtobuf(new byte[]{(byte) 0x05, (byte) 0x68, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F});
        assertEquals("hello", ReaderProtobuf.readString());
    }
}
