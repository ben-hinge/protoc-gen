package com.tonicdesign.protocgenjava;

import org.junit.Test;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ReaderJSONTest {

    private static final int TEST_TAG = 10;
    private static final String TEST_TAG_JSON_KEY = "testTag";
    private static final int TEST_TAG_2 = 15;
    private static final String TEST_TAG_2_JSON_KEY = "testTag2";
    private static final int TEST_TAG_REPEATED = 20;
    private static final String TEST_TAG_REPEATED_JSON_KEY = "testTagRepeated";
    private static final int TEST_TAG_NESTED = 30;
    private static final String TEST_TAG_NESTED_JSON_KEY = "testTagNested";

    private static final Map<String, Reader.TagMapValue> TAG_MAP;
    private static final Map<String, Reader.TagMapValue> TAG_MAP_REPEATED;

    static {
        TAG_MAP = new HashMap<String, Reader.TagMapValue>();
        TAG_MAP.put(TEST_TAG_JSON_KEY, new Reader.TagMapValue(TEST_TAG, false));
        TAG_MAP_REPEATED = new HashMap<String, Reader.TagMapValue>();
        TAG_MAP_REPEATED.put(TEST_TAG_REPEATED_JSON_KEY, new Reader.TagMapValue(TEST_TAG_REPEATED, true));
    }

    @Test
    public void testInvalidJSON() throws Exception {
        Reader reader = ReaderJSON.fromBuffer(bytesFromJSON("sdasdasdadsdasd"));
        assertNull(reader);
    }

    @Test
    public void testReadByte() throws Exception {
        Reader reader = getReader("{\"testTag\": 250}", TAG_MAP);
        assertEquals(TEST_TAG, reader.readTag());
        assertEquals((byte) 0xFA, reader.readByte());
    }

    @Test
    public void testReadVarInt64Bits() throws Exception {
        Reader reader = getReader("{\"testTag\": 2886807498600235000}", TAG_MAP);
        assertEquals(TEST_TAG, reader.readTag());
        assertEquals(2886807498600235000L, reader.readVarInt());
    }

    @Test
    public void testReadVarUInt64Bits() throws Exception {
        Reader reader = getReader("{\"testTag\": 2886807498600235000}", TAG_MAP);
        assertEquals(TEST_TAG, reader.readTag());
        assertEquals(2886807498600235000L, reader.readVarUInt());
    }

    @Test
    public void testReadBool() throws Exception {
        Reader reader = getReader("{\"testTag\": true}", TAG_MAP);
        assertEquals(TEST_TAG, reader.readTag());
        assertEquals(true, reader.readBool());
    }

    @Test
    public void testReadUInt32() throws Exception {
        Reader reader = getReader("{\"testTag\": 524}", TAG_MAP);
        assertEquals(TEST_TAG, reader.readTag());
        assertEquals(524, reader.readUInt32());
    }

    @Test
    public void testReadUInt64Under32Bits() throws Exception {
        Reader reader = getReader("{\"testTag\": 524}", TAG_MAP);
        assertEquals(TEST_TAG, reader.readTag());
        assertEquals(524, reader.readUInt64());
    }

    @Test
    public void testReadUInt64() throws Exception {
        Reader reader = getReader("{\"testTag\": 2886807498600235000}", TAG_MAP);
        assertEquals(TEST_TAG, reader.readTag());
        assertEquals(2886807498600235000L, reader.readUInt64());
    }

    @Test
    public void testReadFloat32() throws Exception {
        Reader reader = getReader("{\"testTag\": 12.24}", TAG_MAP);
        assertEquals(TEST_TAG, reader.readTag());
        assertEquals(12.24, reader.readFloat32(), 0.001);
    }

    @Test
    public void testReadFloat64() throws Exception {
        Reader reader = getReader("{\"testTag\": 12.24}", TAG_MAP);
        assertEquals(TEST_TAG, reader.readTag());
        assertEquals(12.24, reader.readFloat64(), 0.001);
    }

    @Test
    public void testReadString() throws Exception {
        Reader reader = getReader("{\"testTag\": \"h\u0121\u221A\"}", TAG_MAP);
        assertEquals(TEST_TAG, reader.readTag());
        assertEquals("h\u0121\u221A", reader.readString());
    }

    @Test
    public void testReadRepeatedString() throws Exception {
        Reader reader = getReader("{\"testTagRepeated\":[\"string1\",\"string2\",\"string3\",\"string4\"]}", TAG_MAP_REPEATED);
        assertEquals(TEST_TAG_REPEATED, reader.readTag());
        assertEquals("string1", reader.readString());
        assertEquals(TEST_TAG_REPEATED, reader.readTag());
        assertEquals("string2", reader.readString());
        assertEquals(TEST_TAG_REPEATED, reader.readTag());
        assertEquals("string3", reader.readString());
        assertEquals(TEST_TAG_REPEATED, reader.readTag());
        assertEquals("string4", reader.readString());
    }

    @Test
    public void testNestedObject() throws Exception {
        Reader reader = getReader("{\"testTag\":{\"testTagNested\":true},\"testTag2\":\"hello\"}", new HashMap<String, Reader.TagMapValue>() {{
            put(TEST_TAG_JSON_KEY, new Reader.TagMapValue(TEST_TAG, false));
            put(TEST_TAG_2_JSON_KEY, new Reader.TagMapValue(TEST_TAG_2, false));
        }});
        assertEquals(TEST_TAG, reader.readTag());
        reader.pushTagMap(new HashMap<String, Reader.TagMapValue>() {{
            put(TEST_TAG_NESTED_JSON_KEY, new Reader.TagMapValue(TEST_TAG_NESTED, false));
        }});
        assertEquals(TEST_TAG_NESTED, reader.readTag());
        assertEquals(true, reader.readBool());
        reader.popTagMap();
        assertEquals(TEST_TAG_2, reader.readTag());
        assertEquals("hello", reader.readString());
    }

    @Test
    public void testLimit() throws Exception {
        Reader reader = getReader("{\"testTag\": 12.24}", TAG_MAP);
        int limit = reader.pushLimit(0); // Should be a NO OP
        assertEquals(TEST_TAG, reader.readTag());
        assertEquals(12.24, reader.readFloat64(), 0.001);
        reader.popLimit(limit); // Should be a NO OP
    }

    @Test
    public void testIncorrectTag() throws Exception {
        Reader reader = getReader("{\"unknownTag\": true}", TAG_MAP);
        assertEquals(0, reader.readTag());
    }

    @Test
    public void testIncorrectByteValue() throws Exception {
        Reader reader = getReader("{\"testTag\": true}", TAG_MAP);
        assertEquals(TEST_TAG, reader.readTag());
        assertEquals(0, reader.readByte());
    }

    @Test
    public void testIncorrectVarIntValue() throws Exception {
        Reader reader = getReader("{\"testTag\": true}", TAG_MAP);
        assertEquals(TEST_TAG, reader.readTag());
        assertEquals(0, reader.readVarInt());
    }

    @Test
    public void testIncorrectVarUIntValue() throws Exception {
        Reader reader = getReader("{\"testTag\": true}", TAG_MAP);
        assertEquals(TEST_TAG, reader.readTag());
        assertEquals(0, reader.readVarUInt());
    }

    @Test
    public void testIncorrectBoolValue() throws Exception {
        Reader reader = getReader("{\"testTag\": \"not a bool\"}", TAG_MAP);
        assertEquals(TEST_TAG, reader.readTag());
        assertEquals(false, reader.readBool());
    }

    @Test
    public void testIncorrectUInt32Value() throws Exception {
        Reader reader = getReader("{\"testTag\": true}", TAG_MAP);
        assertEquals(TEST_TAG, reader.readTag());
        assertEquals(0, reader.readUInt32());
    }

    @Test
    public void testIncorrectUInt64Value() throws Exception {
        Reader reader = getReader("{\"testTag\": true}", TAG_MAP);
        assertEquals(TEST_TAG, reader.readTag());
        assertEquals(0, reader.readUInt64());
    }

    @Test
    public void testIncorrectFloat32Value() throws Exception {
        Reader reader = getReader("{\"testTag\": true}", TAG_MAP);
        assertEquals(TEST_TAG, reader.readTag());
        assertEquals(0, reader.readFloat32(), 0.001);
    }

    @Test
    public void testIncorrectFloat64Value() throws Exception {
        Reader reader = getReader("{\"testTag\": true}", TAG_MAP);
        assertEquals(TEST_TAG, reader.readTag());
        assertEquals(0, reader.readFloat64(), 0.001);
    }

    @Test
    public void testIncorrectStringValue() throws Exception {
        Reader reader = getReader("{\"testTag\": true}", TAG_MAP);
        assertEquals(TEST_TAG, reader.readTag());
        assertEquals("", reader.readString());
    }

    @Test
    public void testIncorrectRepeatedValue() throws Exception {
        Reader reader = getReader("{\"testTagRepeated\": true}", TAG_MAP_REPEATED);
        assertEquals(0, reader.readTag());
    }

    private Reader getReader(String json, Map<String, Reader.TagMapValue> map) {
        Reader reader = ReaderJSON.fromBuffer(bytesFromJSON(json));
        reader.pushTagMap(map);
        return reader;
    }

    private byte[] bytesFromJSON(String json) {
        return json.getBytes(Charset.forName("UTF-8"));
    }
}
