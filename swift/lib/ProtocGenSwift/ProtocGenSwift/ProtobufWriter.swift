import Foundation

public class ProtobufWriter : Writer {
    let data: NSMutableData
    
    init(data: NSMutableData) {
        self.data = data
    }
    
    public class func withCapacity(capacity: Int) -> Writer? {
        if let data = NSMutableData(capacity: capacity) {
            return ProtobufWriter(data: data)
        }
        return nil
    }
    
    public func toBuffer() -> NSData {
        return data
    }
    
    public func writeByte(v: UInt8) {
        let b = [v]
        data.appendBytes(b, length: 1)
    }
    
    public func writeTag(v: Int) {
        writeVarUInt(UInt(bitPattern: v))
    }
    
    public func writeVarInt(v: Int) {
        writeVarUInt(UInt(bitPattern: v))
    }
    
    public func writeVarUInt(v: UInt) {
        var x = v
        while (x > 0x7F) {
            writeByte(UInt8(x & 0x7F) | 0x80)
            x >>= 7
        }
        writeByte(UInt8(x))
    }
    
    public func writeBool(v: Bool) {
        writeByte(v ? 1 : 0)
    }
    
    public func writeFloat32(v: Float32) {
        let b = UInt32(v._toBitPattern())
        writeByte(UInt8((b >> 0x00) & 0xFF))
        writeByte(UInt8((b >> 0x08) & 0xFF))
        writeByte(UInt8((b >> 0x10) & 0xFF))
        writeByte(UInt8((b >> 0x18) & 0xFF))
    }
    
    public func writeFloat64(v: Float64) {
        let b = UInt64(v._toBitPattern())
        writeByte(UInt8((b >> 0x00) & 0xFF))
        writeByte(UInt8((b >> 0x08) & 0xFF))
        writeByte(UInt8((b >> 0x10) & 0xFF))
        writeByte(UInt8((b >> 0x18) & 0xFF))
        writeByte(UInt8((b >> 0x20) & 0xFF))
        writeByte(UInt8((b >> 0x28) & 0xFF))
        writeByte(UInt8((b >> 0x30) & 0xFF))
        writeByte(UInt8((b >> 0x38) & 0xFF))
    }
    
    public func writeString(v: String) {
        let b = v.dataUsingEncoding(NSUTF8StringEncoding, allowLossyConversion: false)!
        writeVarInt(b.length)
        data.appendData(b)
    }
    
    public func pushTagMap(map: [Int:(String, Bool)]){
        // NO OP
    }
    
    public func popTagMap(){
        // NO OP
    }
}
