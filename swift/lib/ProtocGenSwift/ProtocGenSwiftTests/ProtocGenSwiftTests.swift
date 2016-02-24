import XCTest
import ProtocGenSwift

class ProtocGenSwiftTests: XCTestCase {
    
    var demoNestedMessage: DemoMessage.DemoNestedMessage!
    var demoMessage: DemoMessage!
    var demoMessageWithEmptyNestedArray: DemoMessage!
    
    override func setUp() {
        demoNestedMessage = DemoMessage.DemoNestedMessage.builder()
            .setNestedString("nested string")
            .setNestedInt32(42)
            .build()
        
        demoMessage = DemoMessage.builder()
            .setDemoDouble(5.0)
            .setDemoInt32(42)
            .setDemoInt64(123)
            .setDemoBool(true)
            .setDemoString("demo string")
            .setDemoNestedMessage(demoNestedMessage)
            .setDemoRepeated(["Hello", "World", "Foo", "Bar"])
            .setDemoRepeatedNestedMessage([demoNestedMessage, demoNestedMessage])
            .build()
        
        let emptyNestedMessage = DemoMessage.DemoNestedMessage.builder().build()
        
        demoMessageWithEmptyNestedArray = DemoMessage.builder()
            .setDemoRepeatedNestedMessage([emptyNestedMessage])
            .build()
    }
    
    func testJSON() {
        do {
            let writer = try JSONWriter.withCapacity(demoMessage.sizeInBytes)
            demoMessage.toWriter(writer)
            let buffer = try writer.toBuffer()
            let jsonString = NSString(data: buffer, encoding: NSUTF8StringEncoding)
            assert(jsonString == "{\"demoInt32\":42,\"demoNestedMessage\":{\"nestedString\":\"nested string\",\"nestedInt32\":42},\"demoRepeated\":[\"Hello\",\"World\",\"Foo\",\"Bar\"],\"demoDouble\":5,\"demoString\":\"demo string\",\"demoBool\":true,\"demoRepeatedNestedMessage\":[{\"nestedString\":\"nested string\",\"nestedInt32\":42},{\"nestedString\":\"nested string\",\"nestedInt32\":42}],\"demoInt64\":123}")
            
            let reader = try JSONReader.from(buffer)!
            let readDemoMessage = DemoMessage.fromReader(reader)
            assert(demoMessage == readDemoMessage, "read object to match written object")
            assert(demoMessage.demoNestedMessage == readDemoMessage.demoNestedMessage, "read nested object to match written nested object")
            assert(demoMessage.demoDouble == readDemoMessage.demoDouble, "written value for message double should match read double value")
            assert(demoMessage.demoInt32 == readDemoMessage.demoInt32, "written value for message int32 should match read int32 value")
            assert(demoMessage.demoInt64 == readDemoMessage.demoInt64, "written value for message int64 should match read int64 value")
            assert(demoMessage.demoBool == readDemoMessage.demoBool, "written value for message bool should match read bool value")
            assert(demoMessage.demoString == readDemoMessage.demoString, "written value for message string should match read string value")
            assert(demoMessage.demoRepeated == readDemoMessage.demoRepeated, "written value for repeated value should match read repeated value")
        }catch {
            XCTFail()
        }
    }
    
    func testJSONReadArray() {
        do {
            let writer = try JSONWriter.withCapacity(demoMessageWithEmptyNestedArray.sizeInBytes)
            demoMessageWithEmptyNestedArray.toWriter(writer)
            let jsonString = "{\"demoRepeatedNestedMessage\":[]}"
            let jsonData = jsonString.dataUsingEncoding(NSUTF8StringEncoding)!
            
            let reader = try JSONReader.from(jsonData)!
            let readDemoMessage = DemoMessage.fromReader(reader)
            XCTAssertTrue(demoMessageWithEmptyNestedArray.demoRepeated == readDemoMessage.demoRepeated, "written value for repeated value should match read repeated value")
        }catch {
            XCTFail()
        }
    }
    
    func testProtobuf() {
        do {
            let writer = try ProtobufWriter.withCapacity(demoMessage.sizeInBytes)
            demoMessage.toWriter(writer)
            let buffer = try writer.toBuffer()
            let reader = ProtobufReader.from(buffer)!
            let readDemoMessage = DemoMessage.fromReader(reader)
            assert(demoMessage == readDemoMessage, "read object to match written object")
            assert(demoMessage.demoNestedMessage == readDemoMessage.demoNestedMessage, "read nested object to match written nested object")
            assert(demoMessage.demoDouble == readDemoMessage.demoDouble, "written value for message double should match read double value")
            assert(demoMessage.demoInt32 == readDemoMessage.demoInt32, "written value for message int32 should match read int32 value")
            assert(demoMessage.demoInt64 == readDemoMessage.demoInt64, "written value for message int64 should match read int64 value")
            assert(demoMessage.demoBool == readDemoMessage.demoBool, "written value for message bool should match read bool value")
            assert(demoMessage.demoString == readDemoMessage.demoString, "written value for message string should match read string value")
            assert(demoMessage.demoRepeated == readDemoMessage.demoRepeated, "written value for repeated value should match read repeated value")
        } catch {
            XCTFail()
        }
    }
    
    func testProtoGen() {
        
        do {
            let jsonString = "{\"name\":\"test\"}"
            let jsonData = jsonString.dataUsingEncoding(NSUTF8StringEncoding)!
            
            let reader = try JSONReader.from(jsonData)!
            let proto: TestProto = TestProto.fromReader(reader)
            
            assert(proto.name != nil)
        } catch {
            XCTFail()
        }
    }
}
