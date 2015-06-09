import Foundation

public protocol Writer {
    static func withCapacity(capacity: Int) -> Writer?
    func toBuffer() -> NSData
    func writeTag(tag: Int)
    func writeByte(v: UInt8)
    func writeVarInt(v: Int)
    func writeVarUInt(v: UInt)
    func writeVarUInt64(v: UInt64)
    func writeData(v: NSData)
    func writeBool(v: Bool)
    func writeFloat32(v: Float32)
    func writeFloat64(v: Float64)
    func writeString(v: String)
    func pushTagMap(map: [Int:(String, Bool)])
    func popTagMap()
}