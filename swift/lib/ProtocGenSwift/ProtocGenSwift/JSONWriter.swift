import Foundation

public class JSONWriter : Writer {
    var tagMapStack: [[Int:(String, Bool)]] = []
    var tagMap: [Int:(String, Bool)]! = nil
    var objectStack: [NSMutableDictionary] = []
    var object: NSMutableDictionary! = nil
    var key: String! = nil
    var wireType: UInt8! = 0
    var isRepeated: Bool = false
    var isRepeatedStack: [Bool] = []
    
    public class func withCapacity(capacity: Int) -> Writer? {
        return JSONWriter()
    }
    
    public func toBuffer() -> NSData {
        return NSJSONSerialization.dataWithJSONObject(object, options: nil, error: nil)!
    }
    
    public func writeTag(tag: Int) {
        if let info = tagMap[tag] {
            wireType = UInt8(tag & 0x07)
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
        if 0 == wireType & 0x02 {
            self.writeValue(v)
        }
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
        let parentIsRepeated = isRepeated
        if nil != tagMap {
            tagMapStack.append(tagMap)
            objectStack.append(object)
            isRepeatedStack.append(isRepeated)
        }
        tagMap = map
        object = [:]
        isRepeated = false
        if key != nil {
            if parentIsRepeated {
                if let array = parentObject[key] as? NSMutableArray {
                    array.addObject(object)
                } else {
                    parentObject[key] = NSMutableArray(objects: object)
                }
            } else {
                parentObject[key] = object
            }
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
