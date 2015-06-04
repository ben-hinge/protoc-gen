import Foundation


public typealias ReadByte = (Int) -> UInt8

func readByteFromData(data: NSData) -> ReadByte {
    return { offset in
        return UnsafePointer<UInt8>(data.bytes)[offset]
    }
}

func readByteFromInputStream(inputStream: NSInputStream) -> ReadByte {
    return { offset in
        var b: UInt8 = 0
        let bytesRead = inputStream.read(&b, maxLength: 1)
        if bytesRead == 1 {
            return b
        } else {
            return 0
        }
    }
}