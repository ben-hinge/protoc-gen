import UIKit
import XCTest
import ProtocGenSwift

class ProtocGenSwiftTests: XCTestCase {
    
    var nestedMessage: DemoMessage.NestedMessage!
    var demoMessage: DemoMessage!
    
    override func setUp() {
        nestedMessage = DemoMessage.NestedMessage.builder()
            .setNestedString("nested string")
            .setNestedInt32(42)
            .build()
        
        demoMessage = DemoMessage.builder()
            .setDemoDouble(5.0)
            .setDemoInt32(42)
            .setDemoInt64(123)
            .setDemoBool(true)
            .setDemoString("demo string")
            .setNestedMessage(nestedMessage)
            .setDemoRepeated(["Hello", "World", "Foo", "Bar"])
            .build()
    }
    
    func testJSON() {
        let writer = JSONWriter.withCapacity(demoMessage.sizeInBytes)!
        demoMessage.toWriter(writer)
        let buffer = writer.toBuffer()
        var jsonString = NSString(data: buffer, encoding: NSUTF8StringEncoding)
        assert(jsonString == "{\"demoInt32\":42,\"demoRepeated\":[\"Hello\",\"World\",\"Foo\",\"Bar\"],\"demoDouble\":5,\"demoString\":\"demo string\",\"nestedMessage\":{\"nestedString\":\"nested string\",\"nestedInt32\":42},\"demoBool\":true,\"demoInt64\":123}")
        
        let reader = JSONReader.fromBuffer(buffer)!
        let readDemoMessage = DemoMessage.fromReader(reader)
        assert(demoMessage.demoDouble == readDemoMessage.demoDouble, "written value for message double should match read double value")
        assert(demoMessage.demoInt32 == readDemoMessage.demoInt32, "written value for message int32 should match read int32 value")
        assert(demoMessage.demoInt64 == readDemoMessage.demoInt64, "written value for message int64 should match read int64 value")
        assert(demoMessage.demoBool == readDemoMessage.demoBool, "written value for message bool should match read bool value")
        assert(demoMessage.demoString == readDemoMessage.demoString, "written value for message string should match read string value")
        assert(demoMessage.demoRepeated == readDemoMessage.demoRepeated, "written value for repeated value should match read repeated value")
        // assert(t == t2, "read object to match written object") // TO DO: Add Swift Equality
    }
    
    func testProtobuf() {
        let writer = ProtobufWriter.withCapacity(demoMessage.sizeInBytes)!
        demoMessage.toWriter(writer)
        let buffer = writer.toBuffer()
        let reader = ProtobufReader.fromBuffer(buffer)!
        let readDemoMessage = DemoMessage.fromReader(reader)
        assert(demoMessage.demoDouble == readDemoMessage.demoDouble, "written value for message double should match read double value")
        assert(demoMessage.demoInt32 == readDemoMessage.demoInt32, "written value for message int32 should match read int32 value")
        assert(demoMessage.demoInt64 == readDemoMessage.demoInt64, "written value for message int64 should match read int64 value")
        assert(demoMessage.demoBool == readDemoMessage.demoBool, "written value for message bool should match read bool value")
        assert(demoMessage.demoString == readDemoMessage.demoString, "written value for message string should match read string value")
        assert(demoMessage.demoRepeated == readDemoMessage.demoRepeated, "written value for repeated value should match read repeated value")
        // assert(t == t2, "read object to match written object") // TO DO: Add Swift Equality
    }
}
