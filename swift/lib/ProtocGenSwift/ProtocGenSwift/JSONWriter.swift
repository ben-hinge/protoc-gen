import Foundation

public class JSONWriter : Writer {
    var tagMapStack: [[Int:(String, Bool)]] = []
    var tagMap: [Int:(String, Bool)]! = nil
    var objectStack: [NSMutableDictionary] = []
    var object: NSMutableDictionary! = nil
    var tag: Int! = nil
    var isRepeatedStack: [Bool] = []
    
    public class func withCapacity(capacity: Int) -> Writer {
        return JSONWriter()
    }
    
    public func toBuffer() throws -> NSData {
        return try NSJSONSerialization.dataWithJSONObject(object, options: [])
    }
    
    public func writeTag(tag: Int) {
        self.tag = tag
    }
    
    public func writeByte(v: UInt8) {
        self.writeValue(Int(v))
    }
    
    public func writeVarInt(v: Int) {
        if 0 == tag & 0x02 {
            self.writeValue(v)
        }
    }
    
    public func writeVarUInt(v: UInt) {
        self.writeValue(v)
    }
    
    public func writeVarUInt64(v: UInt64) {
        self.writeValue(Int(v))
    }
    
    public func writeBool(v: Bool) {
        self.writeValue(v)
    }
    
    public func writeData(v: NSData) {
        NSException(name:"Unsupported Operation", reason:"", userInfo: nil).raise()
    }
    
    public func writeFloat32(v: Float32) {
        self.writeValue(v)
    }
    
    public func writeFloat64(v: Float64) {
        self.writeValue(v)
    }
    
    public func writeString(v: String) {
        self.writeValue(v)
    }
    
    private func writeValue(v: AnyObject) {
        self.writeValue(v, object: object)
    }
    
    private func writeValue(v: AnyObject, object: NSMutableDictionary) {
        if let (key, required) = tagMap[tag] {
            if required {
                if let array = object[key] as? NSMutableArray {
                    array.addObject(v)
                } else {
                    object[key] = NSMutableArray(objects: v)
                }
            } else {
                object[key] = v
            }
        } else {
            // error
        }
    }
    
    public func pushTagMap(map: [Int:(String, Bool)]) {
        let parentObject = object
        object = [:]
        if tag != nil {
            self.writeValue(object, object: parentObject)
        }
        
        if nil != tagMap {
            tagMapStack.append(tagMap)
            objectStack.append(parentObject)
        }
        tagMap = map
    }
    
    public func popTagMap() {
        if tagMapStack.count > 0 {
            tagMap = tagMapStack.removeLast()
            object = objectStack.removeLast()
        }
    }
}
