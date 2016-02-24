import Foundation

public class JSONReader : Reader {
    typealias RepeatedObject = (key: Int, generator: NSArray.Generator)
    
    var generatorStack: [NSDictionary.Generator] = []
    var generator: NSDictionary.Generator
    var tagMapStack: [[String:(Int, Bool)]] = []
    var tagMap: [String:(Int, Bool)]! = nil
    var object: AnyObject! = nil
    var repeatedObjectStack: [RepeatedObject?] = []
    var repeatedObject: RepeatedObject? = nil
    
    init(dictionary: NSDictionary) {
        self.generator = dictionary.generate()
    }
    
    public class func from(data: NSData) throws -> Reader? {
        let dict: NSDictionary = try NSJSONSerialization.JSONObjectWithData(data, options: []) as! NSDictionary
        return JSONReader(dictionary: dict)
    }
    
    public class func from(inputStream: NSInputStream) throws -> Reader? {
        let dict: NSDictionary = try NSJSONSerialization.JSONObjectWithStream(inputStream, options: []) as! NSDictionary
        return JSONReader(dictionary: dict)
    }
    
    public func readTag() -> Int {
        if let value: AnyObject = repeatedObject?.generator.next() {
            object = value
            return repeatedObject!.key
        }
        
        while let (key, value) = generator.next() {
            if let (tag, repeated) = tagMap[key as! String] {
                if repeated {
                    if let array = value as? NSArray where array.count > 0 {
                        repeatedObject = (key: tag, generator: array.generate())
                        object = repeatedObject?.generator.next()
                    } else {
                        continue
                    }
                } else {
                    object = value
                }
                return tag
            } else {
                return -1 // Unknown
            }
        }
        return 0
    }
    
    public func readByte() -> UInt8 {
        return 0 // NO OP
    }
    
    public func readVarInt() -> Int {
        if let v = (object as? NSNumber)?.integerValue {
            return v
        }
        return 0
    }
    
    public func readVarUInt() -> UInt {
        if let v = (object as? NSNumber)?.unsignedIntegerValue {
            return UInt(v)
        }
        return 0
    }
    
    public func readVarUInt64() -> UInt64 {
        if let v = (object as? NSNumber)?.unsignedLongLongValue {
            return UInt64(v)
        }
        return 0
    }
    
    public func readBool() -> Bool {
        if let v = (object as? NSNumber)?.boolValue {
            return v
        }
        return false
    }
    
    public func readData() -> NSData {
        NSException(name:"Unsupported Operation", reason:"", userInfo: nil).raise()
        return NSData()
    }
    
    public func readUInt32() -> UInt32 {
        if let v = (object as? NSNumber)?.unsignedIntegerValue {
            return UInt32(v)
        }
        return 0
    }
    
    public func readUInt64() -> UInt64 {
        if let v = (object as? NSNumber)?.unsignedIntegerValue {
            return UInt64(v)
        }
        return 0
    }
    
    public func readFloat32() -> Float32 {
        if let v = (object as? NSNumber)?.floatValue {
            return Float32(v)
        }
        return 0
    }
    
    public func readFloat64() -> Float64 {
        if let v = (object as? NSNumber)?.doubleValue {
            return Float64(v)
        }
        return 0
    }
    
    public func readString() -> String {
        if let value = object as? String {
            return value
        }
        return ""
    }
    
    public func pushLimit(limit: Int) -> Int {
        return 0 // NO OP
    }
    
    public func popLimit(limit: Int) {
        // NO OP
    }
    
    public func pushTagMap(map: [String:(Int, Bool)]) {
        if nil != self.tagMap && nil != object {
            tagMapStack.append(self.tagMap)
            generatorStack.append(generator)
            generator = (object as! NSDictionary).generate()
            repeatedObjectStack.append(repeatedObject)
            repeatedObject = nil
        }
        self.tagMap = map
    }
    
    public func popTagMap() {
        if tagMapStack.count > 0 {
            tagMap = tagMapStack.removeLast()
            generator = generatorStack.removeLast()
            repeatedObject = repeatedObjectStack.removeLast()
        }
    }
}