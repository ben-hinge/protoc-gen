package com.tonicdesign.protocgenjava;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

public class WriterJSONTest {

    private static final int TEST_TAG = 10;
    private static final String TEST_TAG_JSON_KEY = "testTag";
    private static final int TEST_TAG_REPEATED = 20;
    private static final String TEST_TAG_REPEATED_JSON_KEY = "testTagRepeated";

    private WriterJSON mWriter = new WriterJSON();

    @Before
    public void setUp() throws Exception {
        mWriter = new WriterJSON();
        mWriter.pushTagMap(new HashMap<Integer, Writer.TagMapValue>() {{
            put(TEST_TAG, new Writer.TagMapValue(TEST_TAG_JSON_KEY, false));
            put(TEST_TAG_REPEATED, new Writer.TagMapValue(TEST_TAG_REPEATED_JSON_KEY, false));
        }});
    }

    @Test
    public void testWriteByte() throws Exception {
        mWriter.writeTag(TEST_TAG);
        mWriter.writeByte((byte) 0xFA);
        assertTrue(Arrays.equals(new byte[]{ (byte) 0x7b, (byte) 0x22, (byte) 0x74, (byte) 0x65, (byte) 0x73, (byte) 0x74, (byte) 0x54, (byte) 0x61, (byte) 0x67, (byte) 0x22, (byte) 0x3a, (byte) 0x32, (byte) 0x35, (byte) 0x30, (byte) 0x7d }, mWriter.toBuffer()));
    }
}
