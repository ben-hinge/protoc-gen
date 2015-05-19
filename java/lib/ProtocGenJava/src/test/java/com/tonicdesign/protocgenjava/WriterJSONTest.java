package com.tonicdesign.protocgenjava;

import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
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
        assertTrue(Arrays.equals(bytesFromJSON("{\"testTag\":250}"), mWriter.toBuffer()));
    }

    @Test
    public void testWriteVarInt64Bits() throws Exception {
        mWriter.writeTag(TEST_TAG);
        mWriter.writeVarInt(2886807498600235012L);
        assertTrue(Arrays.equals(bytesFromJSON("{\"testTag\":2886807498600235012}"), mWriter.toBuffer()));
    }

    private byte[] bytesFromJSON(String json) {
        return json.getBytes(Charset.forName("UTF-8"));
    }
}
