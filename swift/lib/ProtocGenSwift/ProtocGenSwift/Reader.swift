import Foundation

public protocol Reader {
    static func from(data: NSData) throws -> Reader?
    static func from(inputStream: NSInputStream) throws -> Reader?
    func readTag() -> Int
    func readByte() -> UInt8
    func readVarInt() -> Int
    func readVarUInt() -> UInt
    func readVarUInt64() -> UInt64
    func readBool() -> Bool
    func readData() -> NSData
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
