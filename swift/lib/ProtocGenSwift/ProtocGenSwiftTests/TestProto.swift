// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: TestProto.proto

/**
 * @fileoverview Generated Protocol Buffer code for file TestProto.proto.
 */

import ProtocGenSwift


public func ==(a: TestProto, b: TestProto) -> Bool {
    return (
        a.sizeInBytes == b.sizeInBytes
            && a.name == b.name
            && a.nested == b.nested
            && a.object == b.object
    )
}

public func ==(a: TestProto.TestNested, b: TestProto.TestNested) -> Bool {
    return (
        a.sizeInBytes == b.sizeInBytes
            && a.id == b.id
    )
}

public class TestProto: Equatable {
    public let sizeInBytes: Int
    public let name: String?
    public let nested: [TestNested]!
    public let object: TestNested?
    
    public required init(sizeInBytes: Int, name: String?, nested: [TestNested]!, object: TestNested?) {
        self.sizeInBytes = sizeInBytes
        self.name = name
        self.nested = nested
        self.object = object
    }
    
    static let writeTagMap: [Int:(String, Bool)] = [
        10 : ("name", false),
        18 : ("nested", true),
        26 : ("object", false)
    ]
    
    public func toWriter(w: Writer) {
        w.pushTagMap(TestProto.writeTagMap)
        
        if let v = self.name {
            w.writeTag(10)
            w.writeString(v)
        }
        
        for v in self.nested {
            w.writeTag(18)
            w.writeVarInt(v.sizeInBytes)
            v.toWriter(w)
        }
        
        if let v = self.object {
            w.writeTag(26)
            w.writeVarInt(v.sizeInBytes)
            v.toWriter(w)
        }
        
        w.popTagMap()
    }
    
    static let readTagMap: [String:(Int, Bool)] = [
        "name" : (10, false),
        "nested" : (18, true),
        "object" : (26, false)
    ]
    
    public class func fromReader(r: Reader) -> Self {
        r.pushTagMap(readTagMap)
        
        var name: String? = nil
        var nested: [TestNested]! = []
        var object: TestNested? = nil
        
        loop: while true {
            switch r.readTag() {
            case 10:
                name = r.readString()
            case 18:
                let limit = r.pushLimit(r.readVarInt())
                nested.append(TestProto.TestNested.fromReader(r))
                r.popLimit(limit)
            case 26:
                let limit = r.pushLimit(r.readVarInt())
                object = TestProto.TestNested.fromReader(r)
                r.popLimit(limit)
            case 0:
                break loop
            default:
                continue
            }
        }
        
        r.popTagMap()
        
        let sizeInBytes = TestProto.sizeOf(name, nested: nested, object: object)
        return self.init(sizeInBytes: sizeInBytes, name: name, nested: nested, object: object)
    }
    
    class func sizeOf(name: String?, nested: [TestNested]!, object: TestNested?) -> Int {
        var n = 0
        
        if let v = name {
            n += 1 + sizeOfString(v)
        }
        for v in nested {
            n += 1 + sizeOfVarInt(v.sizeInBytes) + v.sizeInBytes
        }
        if let v = object {
            n += 1 + sizeOfVarInt(v.sizeInBytes) + v.sizeInBytes
        }
        
        return n
    }
    
    public class func builder() -> TestProtoBuilder {
        return TestProtoBuilder()
    }
    
    public class TestNested: Equatable {
        public let sizeInBytes: Int
        public let id: String?
        
        public required init(sizeInBytes: Int, id: String?) {
            self.sizeInBytes = sizeInBytes
            self.id = id
        }
        
        static let writeTagMap: [Int:(String, Bool)] = [
            10 : ("id", false)
        ]
        
        public func toWriter(w: Writer) {
            w.pushTagMap(TestNested.writeTagMap)
            
            if let v = self.id {
                w.writeTag(10)
                w.writeString(v)
            }
            
            w.popTagMap()
        }
        
        static let readTagMap: [String:(Int, Bool)] = [
            "id" : (10, false)
        ]
        
        public class func fromReader(r: Reader) -> Self {
            r.pushTagMap(readTagMap)
            
            var id: String? = nil
            
            loop: while true {
                switch r.readTag() {
                case 10:
                    id = r.readString()
                case 0:
                    break loop
                default:
                    continue
                }
            }
            
            r.popTagMap()
            
            let sizeInBytes = TestNested.sizeOf(id)
            return self.init(sizeInBytes: sizeInBytes, id: id)
        }
        
        class func sizeOf(id: String?) -> Int {
            var n = 0
            
            if let v = id {
                n += 1 + sizeOfString(v)
            }
            
            return n
        }
        
        public class func builder() -> TestNestedBuilder {
            return TestNestedBuilder()
        }
        
    }
    
    public class TestNestedBuilder {
        var id: String? = nil
        
        public func clear() -> Self {
            self.id = nil
            return self
        }
        
        public func setId(v: String?) -> Self {
            self.id = v
            return self
        }
        
        public func clearId() -> Self {
            self.id = nil
            return self
        }
        
        public func build() -> TestNested {
            let sizeInBytes = TestNested.sizeOf(id)
            return TestNested(sizeInBytes: sizeInBytes, id: id)
        }
    }
    
    
}

public class TestProtoBuilder {
    var name: String? = nil
    var nested: [TestProto.TestNested]! = []
    var object: TestProto.TestNested? = nil
    
    public func clear() -> Self {
        self.name = nil
        self.nested = []
        self.object = nil
        return self
    }
    
    public func setName(v: String?) -> Self {
        self.name = v
        return self
    }
    
    public func clearName() -> Self {
        self.name = nil
        return self
    }
    
    public func setNested(v: [TestProto.TestNested]!) -> Self {
        self.nested = v
        return self
    }
    
    public func clearNested() -> Self {
        self.nested = []
        return self
    }
    
    public func setObject(v: TestProto.TestNested?) -> Self {
        self.object = v
        return self
    }
    
    public func clearObject() -> Self {
        self.object = nil
        return self
    }
    
    public func build() -> TestProto {
        let sizeInBytes = TestProto.sizeOf(name, nested: nested, object: object)
        return TestProto(sizeInBytes: sizeInBytes, name: name, nested: nested, object: object)
    }
}

private func sizeOfVarInt(v: Int) -> Int {
    var n = 0
    var x = v
    repeat {
        x = x >> 7
        n++
    } while (x > 0)
    return n
}

private func sizeOfString(s: String) -> Int {
    let b = s.utf8.count
    return sizeOfVarInt(b) + b
}

