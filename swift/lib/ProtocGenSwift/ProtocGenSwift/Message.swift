import Foundation

public protocol MessageInit:class
{
    init()
}

public protocol Message:class,MessageInit
{
    public let sizeInBytes: Int32
    
    public func toWriter(w: Writer)
    public class func fromReader(r: Reader) -> Self
    public class func builder() -> MessageBuilder
}

public protocol MessageBuilder: class
{
    public func clear() -> Self
    public func build() -> Message
}