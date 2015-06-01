import Foundation

public protocol MessageInit:class
{
    init()
}

public protocol Message: class, MessageInit
{
    func serializedSize() -> Int
    func toWriter(w: Writer)
    static func builder() -> MessageBuilder
}

public protocol MessageBuilder: class, MessageInit
{
    func clear() -> Self
    func build() -> AbstractMessage
}

public class AbstractMessage: Message {
    
    required public init() {
        
    }
    
    public func serializedSize() -> Int {
        return 0
    }
    
    public func toWriter(w: Writer) {
        NSException(name:"Override", reason:"", userInfo: nil).raise()
    }
    
    public static func builder() -> MessageBuilder {
        NSException(name:"Override", reason:"", userInfo: nil).raise()
        return AbstractMessageBuilder()
    }
}

public class AbstractMessageBuilder: MessageBuilder {
    
    required public init() {
        
    }
    
    public func clear() -> Self {
        return self
    }
    
    public func build() -> AbstractMessage {
        return AbstractMessage()
    }
}