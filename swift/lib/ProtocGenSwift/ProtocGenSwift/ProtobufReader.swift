import Foundation

public enum ProtobufWireFormat:Int32
{
    case WireFormatVarint = 0
    case WireFormatFixed64 = 1
    case WireFormatLengthDelimited = 2
    case WireFormatStartGroup = 3
    case WireFormatEndGroup = 4
    case WireFormatFixed32 = 5
    case WireFormatTagTypeMask = 7
}

public class ProtobufReader : Reader {
    var offset = 0
    var limit: Int
    let _readByte: ReadByte
    
    init(limit: Int = -1, readByte: ReadByte) {
        self._readByte = readByte
        self.limit = limit
    }
    
    public class func from(data: NSData) -> Reader? {
        return ProtobufReader(limit: data.length, readByte: readByteFromData(data))
    }
    
    public class func from(inputStream: NSInputStream) -> Reader? {
        return ProtobufReader(readByte: readByteFromInputStream(inputStream))
    }
    
    public func readTag() -> Int {
        return Int(readVarUInt())
    }
    
    public func readByte() -> UInt8 {
        if limit != -1 && offset == limit {
            return 0
        }
        return _readByte(offset++)
    }
    
    public func readVarInt() -> Int {
        return Int(readVarUInt())
    }
    
    public func readVarUInt() -> UInt {
        var v: UInt = 0
        var s: UInt = 0
        while true {
            var b = readByte()
            v |= (UInt(b & 0x7F) << s)
            s += 7
            if 0 == (b & 0x80) {
                break
            }
        }
        return v
    }
    
    public func readVarUInt64() -> UInt64 {
        return UInt64(readVarUInt())
    }
    
    public func readBool() -> Bool {
        return readVarInt() != 0
    }
    
    public func readData() -> NSData {
        let numberOfBytes = self.readVarInt()
        let data = NSMutableData(capacity: numberOfBytes)!
        let ptr = UnsafeMutablePointer<UInt8>(data.bytes)
        for var i = 0; i < numberOfBytes; i++ {
            ptr[i] = self.readByte()
        }
        return data
    }
    
    public func readUInt32() -> UInt32 {
        var v = UInt32(readByte())
        v |= UInt32(readByte()) << 0x08
        v |= UInt32(readByte()) << 0x10
        v |= UInt32(readByte()) << 0x18
        return v
    }
    
    public func readUInt64() -> UInt64 {
        var v = UInt64(readByte())
        v |= UInt64(readByte()) << 0x08
        v |= UInt64(readByte()) << 0x10
        v |= UInt64(readByte()) << 0x18
        v |= UInt64(readByte()) << 0x20
        v |= UInt64(readByte()) << 0x28
        v |= UInt64(readByte()) << 0x30
        v |= UInt64(readByte()) << 0x38
        return v
    }
    
    public func readFloat32() -> Float32 {
        return Float32._fromBitPattern(readUInt32())
    }
    
    public func readFloat64() -> Float64 {
        return Float64._fromBitPattern(readUInt64())
    }
    
    public func readString() -> String {
        var s = ""
        var l = readVarInt()
        while (l > 0) {
            var c = UInt32(readByte())
            var v = c >> 4
            if (v > 13) {
                c = (((c & 0x0F) << 12) | ((UInt32(readByte()) & 0x3F) << 6) | (UInt32(readByte()) & 0x3F))
                l -= 3
            } else if (v > 8) {
                c = (((c & 0x1F) << 6) | (UInt32(readByte()) & 0x3F))
                l -= 2
            } else {
                l -= 1
            }
            s = s + String(UnicodeScalar(c))
        }
        return s
    }
    
    public func pushLimit(limit: Int) -> Int {
        var oldLimit = self.limit
        self.limit = self.offset + limit
        return oldLimit
    }
    
    public func popLimit(limit: Int) {
        self.limit = limit
    }
    
    public func pushTagMap(map: [String:(Int, Bool)]) {
        // NO OP
    }
    
    public func popTagMap() {
        // NO OP
    }
}