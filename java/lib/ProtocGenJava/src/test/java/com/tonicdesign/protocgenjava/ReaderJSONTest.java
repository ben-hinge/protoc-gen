package com.tonicdesign.protocgenjava;

import org.junit.Test;

import java.nio.charset.Charset;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class ReaderJSONTest {

    private static final int TEST_TAG = 10;
    private static final String TEST_TAG_JSON_KEY = "testTag";
    private static final int TEST_TAG_2 = 15;
    private static final String TEST_TAG_2_JSON_KEY = "testTag2";
    private static final int TEST_TAG_REPEATED = 20;
    private static final String TEST_TAG_REPEATED_JSON_KEY = "testTagRepeated";
    private static final int TEST_TAG_NESTED = 30;
    private static final String TEST_TAG_NESTED_JSON_KEY = "testTagNested";

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

    @Test
    public void testReadRepeatedString() throws Exception {
        mReader = ReaderJSON.fromBuffer(bytesFromJSON("{\"testTagRepeated\":[\"string1\",\"string2\",\"string3\",\"string4\"]}"));
        mReader.pushTagMap(new HashMap<String, Reader.TagMapValue>() {{
            put(TEST_TAG_REPEATED_JSON_KEY, new Reader.TagMapValue(TEST_TAG_REPEATED, true));
        }});
        assertEquals(TEST_TAG_REPEATED, mReader.readTag());
        assertEquals("string1", mReader.readString());
        assertEquals(TEST_TAG_REPEATED, mReader.readTag());
        assertEquals("string2", mReader.readString());
        assertEquals(TEST_TAG_REPEATED, mReader.readTag());
        assertEquals("string3", mReader.readString());
        assertEquals(TEST_TAG_REPEATED, mReader.readTag());
        assertEquals("string4", mReader.readString());
    }

    @Test
    public void testNestedObject() throws Exception {
        mReader = ReaderJSON.fromBuffer(bytesFromJSON("{\"testTag\":{\"testTagNested\":true},\"testTag2\":\"hello\"}"));
        mReader.pushTagMap(new HashMap<String, Reader.TagMapValue>() {{
            put(TEST_TAG_JSON_KEY, new Reader.TagMapValue(TEST_TAG, false));
            put(TEST_TAG_2_JSON_KEY, new Reader.TagMapValue(TEST_TAG_2, false));
        }});
        assertEquals(TEST_TAG, mReader.readTag());
        mReader.pushTagMap(new HashMap<String, Reader.TagMapValue>() {{
            put(TEST_TAG_NESTED_JSON_KEY, new Reader.TagMapValue(TEST_TAG_NESTED, false));
        }});
        assertEquals(TEST_TAG_NESTED, mReader.readTag());
        assertEquals(true, mReader.readBool());
        mReader.popTagMap();
        assertEquals(TEST_TAG_2, mReader.readTag());
        assertEquals("hello", mReader.readString());
    }

    private byte[] bytesFromJSON(String json) {
        return json.getBytes(Charset.forName("UTF-8"));
    }
}
