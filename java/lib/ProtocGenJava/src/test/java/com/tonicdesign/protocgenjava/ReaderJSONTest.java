package com.tonicdesign.protocgenjava;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class ReaderJSONTest {

    private static final int TEST_TAG = 10;
    private static final String TEST_TAG_JSON_KEY = "testTag";

    private ReaderJSON mReader;

    @Test
    public void testReadVarInt64Bits() throws Exception {
        mReader = ReaderJSON.fromBuffer(bytesFromJSON("{\"testTag\": 2886807498600235000}"));
        mReader.pushTagMap(new HashMap<String, Reader.TagMapValue>() {{
            put(TEST_TAG_JSON_KEY, new Reader.TagMapValue(TEST_TAG, false));
        }});
        assertEquals(TEST_TAG, mReader.readTag());
        assertEquals(2886807498600235000L, mReader.readVarInt());
    }

    @Test
    public void testReadVarUInt64Bits() throws Exception {
        mReader = ReaderJSON.fromBuffer(bytesFromJSON("{\"testTag\": 2886807498600235000}"));
        mReader.pushTagMap(new HashMap<String, Reader.TagMapValue>() {{
            put(TEST_TAG_JSON_KEY, new Reader.TagMapValue(TEST_TAG, false));
        }});
        assertEquals(TEST_TAG, mReader.readTag());
        assertEquals(2886807498600235000L, mReader.readVarUInt());
    }

    @Test
    public void testReadBool() throws Exception {
        mReader = ReaderJSON.fromBuffer(bytesFromJSON("{\"testTag\": true}"));
        mReader.pushTagMap(new HashMap<String, Reader.TagMapValue>() {{
            put(TEST_TAG_JSON_KEY, new Reader.TagMapValue(TEST_TAG, false));
        }});
        assertEquals(TEST_TAG, mReader.readTag());
        assertEquals(true, mReader.readBool());
    }

    @Test
    public void testReadUInt32() throws Exception {
        mReader = ReaderJSON.fromBuffer(bytesFromJSON("{\"testTag\": 524}"));
        mReader.pushTagMap(new HashMap<String, Reader.TagMapValue>() {{
            put(TEST_TAG_JSON_KEY, new Reader.TagMapValue(TEST_TAG, false));
        }});
        assertEquals(TEST_TAG, mReader.readTag());
        assertEquals(524, mReader.readUInt32());
    }

    @Test
    public void testReadUInt64Under32Bits() throws Exception {
        mReader = ReaderJSON.fromBuffer(bytesFromJSON("{\"testTag\": 524}"));
        mReader.pushTagMap(new HashMap<String, Reader.TagMapValue>() {{
            put(TEST_TAG_JSON_KEY, new Reader.TagMapValue(TEST_TAG, false));
        }});
        assertEquals(TEST_TAG, mReader.readTag());
        assertEquals(524, mReader.readUInt64());
    }

    @Test
    public void testReadUInt64() throws Exception {
        mReader = ReaderJSON.fromBuffer(bytesFromJSON("{\"testTag\": 2886807498600235000}"));
        mReader.pushTagMap(new HashMap<String, Reader.TagMapValue>() {{
            put(TEST_TAG_JSON_KEY, new Reader.TagMapValue(TEST_TAG, false));
        }});
        assertEquals(TEST_TAG, mReader.readTag());
        assertEquals(2886807498600235000L, mReader.readUInt64());
    }

    @Test
    public void testReadFloat32() throws Exception {
        mReader = ReaderJSON.fromBuffer(bytesFromJSON("{\"testTag\": 12.24}"));
        mReader.pushTagMap(new HashMap<String, Reader.TagMapValue>() {{
            put(TEST_TAG_JSON_KEY, new Reader.TagMapValue(TEST_TAG, false));
        }});
        assertEquals(TEST_TAG, mReader.readTag());
        assertEquals(12.24, mReader.readFloat32(), 0.001);
    }

    @Test
    public void testReadFloat64() throws Exception {
        mReader = ReaderJSON.fromBuffer(bytesFromJSON("{\"testTag\": 12.24}"));
        mReader.pushTagMap(new HashMap<String, Reader.TagMapValue>() {{
            put(TEST_TAG_JSON_KEY, new Reader.TagMapValue(TEST_TAG, false));
        }});
        assertEquals(TEST_TAG, mReader.readTag());
        assertEquals(12.24, mReader.readFloat64(), 0.001);
    }

    @Test
    public void testReadString() throws Exception {
        mReader = ReaderJSON.fromBuffer(bytesFromJSON("{\"testTag\": \"h\u0121\u221A\"}"));
        mReader.pushTagMap(new HashMap<String, Reader.TagMapValue>() {{
            put(TEST_TAG_JSON_KEY, new Reader.TagMapValue(TEST_TAG, false));
        }});
        assertEquals(TEST_TAG, mReader.readTag());
        assertEquals("h\u0121\u221A", mReader.readString());
    }

    private byte[] bytesFromJSON(String json) {
        return json.getBytes(Charset.forName("UTF-8"));
    }
}
