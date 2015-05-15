package com.tonicdesign.protocgenjava;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class WriterProtobufTest {

    @Test
    public void testWriterProtobufWriteByte() throws Exception {
        WriterProtobuf WriterProtobuf = new WriterProtobuf(1);
        WriterProtobuf.writeByte((byte) 0xFF);
        assertTrue(Arrays.equals(new byte[]{(byte) 0xFF}, WriterProtobuf.toBuffer()));
    }

    @Test
    public void testWriterProtobufWriteVarIntUnder8Bits() throws Exception {
        WriterProtobuf WriterProtobuf = new WriterProtobuf(1);
        WriterProtobuf.writeVarInt(12L);
        assertTrue(Arrays.equals(new byte[]{(byte) 0x0C}, WriterProtobuf.toBuffer()));
    }

    @Test
    public void testWriterProtobufWriteVarIntUnder15Bits() throws Exception {
        WriterProtobuf WriterProtobuf = new WriterProtobuf(2);
        WriterProtobuf.writeVarInt(524L);
        assertTrue(Arrays.equals(new byte[]{(byte) 0x8C, (byte) 0x04}, WriterProtobuf.toBuffer()));
    }

    @Test
    public void testWriterProtobufWriteVarInt64Bits() throws Exception {
        WriterProtobuf WriterProtobuf = new WriterProtobuf(9);
        WriterProtobuf.writeVarInt(2886807498600235012L);
        assertTrue(Arrays.equals(new byte[]{(byte) 0x84, (byte) 0x80, (byte) 0x81, (byte) 0x88, (byte) 0x80, (byte) 0x84, (byte) 0x80, (byte) 0x88, (byte) 0x28 }, WriterProtobuf.toBuffer()));
    }

    @Test
    public void testWriterProtobufWriteBool() throws Exception {
        WriterProtobuf WriterProtobuf = new WriterProtobuf(1);
        WriterProtobuf.writeBool(true);
        assertTrue(Arrays.equals(new byte[]{(byte) 0x01}, WriterProtobuf.toBuffer()));
    }

    @Test
    public void testWriterProtobufWriteFloat32() throws Exception {
        WriterProtobuf WriterProtobuf = new WriterProtobuf(4);
        WriterProtobuf.writeFloat32(12.24f);
        assertTrue(Arrays.equals(new byte[]{(byte) 0x0A, (byte) 0xD7, (byte) 0x43, (byte) 0x41 }, WriterProtobuf.toBuffer()));
    }

    @Test
    public void testWriterProtobufWriteFloat64() throws Exception {
        WriterProtobuf WriterProtobuf = new WriterProtobuf(8);
        WriterProtobuf.writeFloat64(12.24);
        assertTrue(Arrays.equals(new byte[]{(byte) 0x7B, (byte) 0x14, (byte) 0xAE, (byte) 0x47, (byte) 0xE1, (byte) 0x7A, (byte) 0x28, (byte) 0x40 }, WriterProtobuf.toBuffer()));
    }

    @Test
    public void testWriterProtobufWriteString() throws Exception {
        WriterProtobuf WriterProtobuf = new WriterProtobuf(6);
        WriterProtobuf.writeString("hello");
        assertTrue(Arrays.equals(new byte[]{(byte) 0x05, (byte) 0x68, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F }, WriterProtobuf.toBuffer()));
    }
}
