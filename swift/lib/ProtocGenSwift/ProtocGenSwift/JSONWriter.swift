import Foundation

public class JSONWriter : Writer {
    var tagMapStack: [[Int:(String, Bool)]] = []
    var tagMap: [Int:(String, Bool)]! = nil
    var objectStack: [NSMutableDictionary] = []
    var object: NSMutableDictionary! = nil
    var key: String! = nil
    var isRepeated: Bool = false
    
    public class func withCapacity(capacity: Int) -> Writer? {
        return JSONWriter()
    }
    
    public func toBuffer() -> NSData {
        return NSJSONSerialization.dataWithJSONObject(object, options: nil, error: nil)!
    }
    
    public func writeTag(tag: Int) {
        if let info = tagMap[tag] {
            key = info.0
            isRepeated = info.1
        } else {
            // error
        }
    }
    
    public func writeByte(v: UInt8) {
        self.writeValue(Int(v))
    }
    
    public func writeVarInt(v: Int) {
        self.writeValue(v)
    }
    
    public func writeVarUInt(v: UInt) {
        self.writeValue(v)
    }
    
    public func writeBool(v: Bool) {
        self.writeValue(v)
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
        if isRepeated {
            if let array = object[key] as? NSMutableArray {
                array.addObject(v)
            } else {
                object[key] = NSMutableArray(objects: v)
            }
        } else {
            object[key] = v
        }
    }
    
    public func pushTagMap(map: [Int:(String, Bool)]) {
        let parentObject = object
        if nil != tagMap {
            tagMapStack.append(tagMap)
            objectStack.append(object)
        }
        tagMap = map
        object = [:]
        if key != nil {
            parentObject[key] = object
            key = nil
        }
    }
    
    public func popTagMap() {
        if tagMapStack.count > 0 {
            tagMap = tagMapStack.removeLast()
            object = objectStack.removeLast()
        }
    }
}
