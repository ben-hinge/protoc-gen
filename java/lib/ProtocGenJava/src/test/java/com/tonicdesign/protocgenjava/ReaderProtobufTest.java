package com.tonicdesign.protocgenjava;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class ReaderProtobufTest {

    @Test
    public void testReaderProtobufReadTag() throws Exception {
        byte testByte = 0x01 << 3 | 0x02;
        ReaderProtobuf readerProtobuf = ReaderProtobuf.fromBuffer(new byte[]{testByte});
        assertEquals(testByte, readerProtobuf.readTag());
    }

    @Test
    public void testReaderProtobufReadByte() throws Exception {
        byte testByte = 0x4F;
        ReaderProtobuf readerProtobuf = ReaderProtobuf.fromBuffer(new byte[]{ testByte });
        assertEquals(testByte, readerProtobuf.readByte());
    }

    @Test
    public void testReaderProtobufReadByteAfterLimit() throws Exception {
        byte testByte = 0x4F;
        ReaderProtobuf readerProtobuf = ReaderProtobuf.fromBuffer(new byte[]{ testByte });
        assertEquals(testByte, readerProtobuf.readByte());
        assertEquals(0, readerProtobuf.readByte());
    }

    @Test
    public void testReaderProtobufReadVarIntUnder8Bits() throws Exception {
        ReaderProtobuf readerProtobuf = ReaderProtobuf.fromBuffer(new byte[]{(byte) 0x0C});
        assertEquals(12L, readerProtobuf.readVarInt());
    }

    @Test
    public void testReaderProtobufReadVarIntUnder15Bits() throws Exception {
        ReaderProtobuf readerProtobuf = ReaderProtobuf.fromBuffer(new byte[]{(byte) 0x8C, (byte) 0x04});
        assertEquals(524L, readerProtobuf.readVarInt());
    }

    @Test
    public void testReaderProtobufReadVarInt64Bits() throws Exception {
        ReaderProtobuf readerProtobuf = ReaderProtobuf.fromBuffer(new byte[]{(byte) 0x84, (byte) 0x80, (byte) 0x81, (byte) 0x88, (byte) 0x80, (byte) 0x84, (byte) 0x80, (byte) 0x88, (byte) 0x28 });
        assertEquals(2886807498600235012L, readerProtobuf.readVarInt());
    }

    @Test
    public void testReaderProtobufReadVarUInt64Bits() throws Exception {
        ReaderProtobuf readerProtobuf = ReaderProtobuf.fromBuffer(new byte[]{(byte) 0x84, (byte) 0x80, (byte) 0x81, (byte) 0x88, (byte) 0x80, (byte) 0x84, (byte) 0x80, (byte) 0x88, (byte) 0x28 });
        assertEquals(2886807498600235012L, readerProtobuf.readVarUInt());
    }

    @Test
    public void testReaderProtobufReadBool() throws Exception {
        ReaderProtobuf readerProtobuf = ReaderProtobuf.fromBuffer(new byte[]{(byte) 0x01});
        assertEquals(true, readerProtobuf.readBool());
    }

    @Test
    public void testReaderProtobufReadFloat32() throws Exception {
        ReaderProtobuf readerProtobuf = ReaderProtobuf.fromBuffer(new byte[]{(byte) 0x0A, (byte) 0xD7, (byte) 0x43, (byte) 0x41});
        assertEquals(12.24f, readerProtobuf.readFloat32(), 0.00001);
    }

    @Test
    public void testReaderProtobufReadFloat64() throws Exception {
        ReaderProtobuf readerProtobuf = ReaderProtobuf.fromBuffer(new byte[]{(byte) 0x7B, (byte) 0x14, (byte) 0xAE, (byte) 0x47, (byte) 0xE1, (byte) 0x7A, (byte) 0x28, (byte) 0x40});
        assertEquals(12.24, readerProtobuf.readFloat64(), 0.00001);
    }

    @Test
    public void testReaderProtobufReadString() throws Exception {
        ReaderProtobuf readerProtobuf = ReaderProtobuf.fromBuffer(new byte[]{(byte) 0x06, (byte) 0x68, (byte) 0xC4, (byte) 0xA1, (byte) 0xE2, (byte) 0x88, (byte) 0x9A});
        assertEquals("h\u0121\u221A", readerProtobuf.readString());
    }

    @Test
    public void testLimit() throws Exception {
        byte testByte1 = 0x01 << 3 | 0x02;
        byte testByte2 = 0x05 << 3 | 0x04;
        ReaderProtobuf readerProtobuf = ReaderProtobuf.fromBuffer(new byte[]{testByte1, testByte2});
        assertEquals(testByte1, readerProtobuf.readByte());
        int limit = readerProtobuf.pushLimit(0);
        assertEquals(0, readerProtobuf.readByte());
        readerProtobuf.popLimit(limit);
        assertEquals(testByte2, readerProtobuf.readByte());
    }

    @Test
    public void testTagMap() throws Exception {
        byte testByte = 0x01 << 3 | 0x02;
        ReaderProtobuf readerProtobuf = ReaderProtobuf.fromBuffer(new byte[]{testByte});
        readerProtobuf.pushTagMap(new HashMap<String, Reader.TagMapValue>()); // Should be a NO OP
        assertEquals(testByte, readerProtobuf.readByte());
        readerProtobuf.popTagMap(); // Should be a NO OP
    }
}
