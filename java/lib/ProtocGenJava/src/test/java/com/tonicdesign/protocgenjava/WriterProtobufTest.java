package com.tonicdesign.protocgenjava;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

public class WriterProtobufTest {

    @Test
    public void testWriterProtobufWriteTag() throws Exception {
        byte testByte = 0x01 << 3 | 0x02;
        WriterProtobuf writerProtobuf = WriterProtobuf.withCapacity(1);
        writerProtobuf.writeTag(testByte);
        assertTrue(Arrays.equals(new byte[]{ testByte }, writerProtobuf.toBuffer()));
    }

    @Test
    public void testWriterProtobufWriteByte() throws Exception {
        WriterProtobuf writerProtobuf = WriterProtobuf.withCapacity(1);
        writerProtobuf.writeByte((byte) 0xFF);
        assertTrue(Arrays.equals(new byte[]{(byte) 0xFF}, writerProtobuf.toBuffer()));
    }

    @Test
    public void testWriterProtobufWriteVarIntUnder8Bits() throws Exception {
        WriterProtobuf writerProtobuf = WriterProtobuf.withCapacity(1);
        writerProtobuf.writeVarInt(12L);
        assertTrue(Arrays.equals(new byte[]{(byte) 0x0C}, writerProtobuf.toBuffer()));
    }

    @Test
    public void testWriterProtobufWriteVarIntUnder15Bits() throws Exception {
        WriterProtobuf writerProtobuf = WriterProtobuf.withCapacity(2);
        writerProtobuf.writeVarInt(524L);
        assertTrue(Arrays.equals(new byte[]{(byte) 0x8C, (byte) 0x04}, writerProtobuf.toBuffer()));
    }

    @Test
    public void testWriterProtobufWriteVarInt64Bits() throws Exception {
        WriterProtobuf writerProtobuf = WriterProtobuf.withCapacity(9);
        writerProtobuf.writeVarInt(2886807498600235012L);
        assertTrue(Arrays.equals(new byte[]{(byte) 0x84, (byte) 0x80, (byte) 0x81, (byte) 0x88, (byte) 0x80, (byte) 0x84, (byte) 0x80, (byte) 0x88, (byte) 0x28 }, writerProtobuf.toBuffer()));
    }

    @Test
    public void testWriterProtobufWriteVarUInt64Bits() throws Exception {
        WriterProtobuf writerProtobuf = WriterProtobuf.withCapacity(9);
        writerProtobuf.writeVarUInt(2886807498600235012L);
        assertTrue(Arrays.equals(new byte[]{(byte) 0x84, (byte) 0x80, (byte) 0x81, (byte) 0x88, (byte) 0x80, (byte) 0x84, (byte) 0x80, (byte) 0x88, (byte) 0x28 }, writerProtobuf.toBuffer()));
    }

    @Test
    public void testWriterProtobufWriteBool() throws Exception {
        WriterProtobuf writerProtobuf = WriterProtobuf.withCapacity(1);
        writerProtobuf.writeBool(true);
        assertTrue(Arrays.equals(new byte[]{(byte) 0x01}, writerProtobuf.toBuffer()));
    }

    @Test
    public void testWriterProtobufWriteFloat32() throws Exception {
        WriterProtobuf writerProtobuf = WriterProtobuf.withCapacity(4);
        writerProtobuf.writeFloat32(12.24f);
        assertTrue(Arrays.equals(new byte[]{(byte) 0x0A, (byte) 0xD7, (byte) 0x43, (byte) 0x41 }, writerProtobuf.toBuffer()));
    }

    @Test
    public void testWriterProtobufWriteFloat64() throws Exception {
        WriterProtobuf writerProtobuf = WriterProtobuf.withCapacity(8);
        writerProtobuf.writeFloat64(12.24);
        assertTrue(Arrays.equals(new byte[]{(byte) 0x7B, (byte) 0x14, (byte) 0xAE, (byte) 0x47, (byte) 0xE1, (byte) 0x7A, (byte) 0x28, (byte) 0x40 }, writerProtobuf.toBuffer()));
    }

    @Test
    public void testWriterProtobufWriteString() throws Exception {
        WriterProtobuf writerProtobuf = WriterProtobuf.withCapacity(6);
        writerProtobuf.writeString("h\u0121\u221A");
        assertTrue(Arrays.equals(new byte[]{(byte) 0x06, (byte) 0x68, (byte) 0xC4, (byte) 0xA1, (byte) 0xE2, (byte) 0x88, (byte) 0x9A}, writerProtobuf.toBuffer()));
    }

    @Test
    public void testTagMap() throws Exception {
        byte testByte = 0x01 << 3 | 0x02;
        WriterProtobuf writerProtobuf = WriterProtobuf.withCapacity(1);
        writerProtobuf.pushTagMap(new HashMap<Integer, Writer.TagMapValue>()); // Should be a NO OP
        writerProtobuf.writeByte(testByte);
        writerProtobuf.popTagMap(); // Should be a NO OP
        assertTrue(Arrays.equals(new byte[]{testByte}, writerProtobuf.toBuffer()));
    }
}
