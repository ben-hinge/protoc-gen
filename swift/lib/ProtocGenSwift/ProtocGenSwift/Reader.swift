import Foundation

public protocol Reader {
    static func fromBuffer(data: NSData) -> Reader?
    func readTag() -> Int
    func readByte() -> UInt8
    func readVarInt() -> Int
    func readVarUInt() -> UInt
    func readBool() -> Bool
    func readFloat32() -> Float32
    func readFloat64() -> Float64
    func readUInt32() -> UInt32
    func readUInt64() -> UInt64
    func readString() -> String
    func pushLimit(limit: Int) -> Int
    func popLimit(limit: Int)
    func pushTagMap(map: [String:(Int, Bool)])
    func popTagMap()
}