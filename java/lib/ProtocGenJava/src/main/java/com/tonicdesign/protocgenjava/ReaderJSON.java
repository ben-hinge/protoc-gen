package com.tonicdesign.protocgenjava;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

public class ReaderJSON
implements
    Reader {

    class RepeatedObject {

        private final int mKey;
        private final JSONArray mJSONArray;
        private int mArrayIndex;

        public RepeatedObject(int key, JSONArray jsonArray) {
            mKey = key;
            mJSONArray = jsonArray;
            mArrayIndex = 0;
        }

        public int getKey() {
            return mKey;
        }

        public boolean hasNext() {
            return mArrayIndex < mJSONArray.length();
        }

        public Object next() throws JSONException {
            return mJSONArray.get(mArrayIndex++);
        }
    }

    private Stack<Iterator<String>> mKeyIteratorStack = new Stack<Iterator<String>>();
    private Iterator<String> mKeyIterator;
    private Stack<JSONObject> mJSONObjectStack = new Stack<JSONObject>();
    private JSONObject mJSONObject;
    private Stack<Map<String, Reader.TagMapValue>> mTagMapStack = new Stack<Map<String, TagMapValue>>();
    private Map<String, Reader.TagMapValue> mTagMap;
    private Object mObject;
    private Stack<RepeatedObject> mRepeatedObjectStack = new Stack<RepeatedObject>();
    private RepeatedObject mRepeatedObject;

    public static ReaderJSON fromBuffer(byte[] bytes) {
        String json = new String(bytes, Charset.forName("UTF-8"));
        try {
            return new ReaderJSON(new JSONObject(json));
        } catch (JSONException e) {
            // Nothing
        }
        return null;
    }

    public ReaderJSON(JSONObject jsonObject) {
        mJSONObject = jsonObject;
        mKeyIterator = jsonObject.keys();
    }

    public int readTag() {
        try {
            if (null != mRepeatedObject && mRepeatedObject.hasNext()) {
                mObject = mRepeatedObject.next();
                return mRepeatedObject.getKey();
            }

            if (mKeyIterator.hasNext()) {
                String key = mKeyIterator.next();
                TagMapValue tagMapValue = mTagMap.get(key);
                if (tagMapValue.isRepeated) {
                    JSONArray jsonArray = mJSONObject.getJSONArray(key);
                    mRepeatedObject = new RepeatedObject(tagMapValue.key, jsonArray);
                    mObject = mRepeatedObject.next();
                } else {
                    mObject = mJSONObject.get(key);
                }
                return tagMapValue.key;
            }
        } catch (JSONException e) {

        }

        return 0;
    }

    public byte readByte() {
        if (mObject instanceof Integer) {
            int readByte = (Integer) mObject;
            return (byte) (readByte & 0xFF);
        }
        return 0;
    }

    public long readVarInt() {
        return readVarUInt();
    }

    public long readVarUInt() {
        if (mObject instanceof Long) {
            return (Long) mObject;
        }
        return 0;
    }

    public boolean readBool() {
        if (mObject instanceof Boolean) {
            return (Boolean) mObject;
        }
        return false;
    }

    public int readUInt32() {
        if (mObject instanceof Integer) {
            return (Integer) mObject;
        }
        return 0;
    }

    public long readUInt64() {
        if (mObject instanceof Long) {
            return (Long) mObject;
        } else if (mObject instanceof Integer) {
            return (Integer) mObject;
        }
        return 0;
    }

    public float readFloat32() {
        return (float) readFloat64();
    }

    public double readFloat64() {
        if (mObject instanceof Double) {
            return (Double) mObject;
        }
        return 0;
    }

    public String readString() {
        if (mObject instanceof String) {
            return (String) mObject;
        }
        return "";
    }

    public int pushLimit(int limit) {
        return 0; // Nothing
    }

    public void popLimit(int limit) {
        // Nothing
    }

    public void pushTagMap(Map<String, TagMapValue> map) {
        if (null  != mTagMap) {
            mTagMapStack.push(mTagMap);
            mJSONObjectStack.push(mJSONObject);
            mJSONObject = (JSONObject) mObject;
            mKeyIteratorStack.push(mKeyIterator);
            mKeyIterator = mJSONObject.keys();
            mRepeatedObjectStack.push(mRepeatedObject);
            mRepeatedObject = null;
        }
        mTagMap = map;
    }

    public void popTagMap() {
        if (mTagMapStack.size() > 0) {
            mTagMap = mTagMapStack.pop();
            mJSONObject = mJSONObjectStack.pop();
            mKeyIterator = mKeyIteratorStack.pop();
            mRepeatedObject = mRepeatedObjectStack.pop();
        }
    }
}
