package com.tonicdesign.protocgenjava;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Stack;

public class WriterJSON
implements
    Writer {

    private Stack<Map<Integer, TagMapValue>> mTagMapStack = new Stack<Map<Integer, TagMapValue>>();
    private Map<Integer, TagMapValue> mTagMap;
    private Stack<JSONObject> mObjectStack = new Stack<JSONObject>();
    private JSONObject mObject;
    private Integer mTag;

    public byte[] toBuffer() {
        return mObject.toString().getBytes(Charset.forName("UTF-8"));
    }

    public void writeTag(int tag) {
        mTag = tag;
    }

    public void writeByte(byte v) {
        writeValue((int) v & 0xFF);
    }

    public void writeVarInt(long v) {
        writeValue(v);
    }

    public void writeVarUInt(long v) {
        writeValue(v);
    }

    public void writeBool(boolean v) {
        writeValue(v);
    }

    public void writeFloat32(float v) {
        writeValue(v);
    }

    public void writeFloat64(double v) {
        writeValue(v);
    }

    public void writeString(String v) {
        writeValue(v);
    }

    private void writeValue(Object v) {
        writeValue(v, mObject);
    }

    private void writeValue(Object v, JSONObject object) {
        TagMapValue tagMapValue = mTagMap.get(mTag);
        if (null != tagMapValue) {
            if (tagMapValue.isRepeated) {
                // Repeated
            } else {
                try {
                    object.put(tagMapValue.key, v);
                } catch (JSONException e) {
                    // error
                }
            }
        }else {
            // error
        }
    }

    public void pushTagMap(Map<Integer, TagMapValue> map) {
        JSONObject parentObject = mObject;
        mObject = new JSONObject();
        if (mTag != null) {
            writeValue(mObject, parentObject);
        }

        if (mTagMap != null) {
            mTagMapStack.push(mTagMap);
            mObjectStack.push(parentObject);
        }
        mTagMap = map;
    }

    public void popTagMap() {

    }
}
