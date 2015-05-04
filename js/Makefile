
PROTO_FILES := int64_encoding.proto javascript_package.proto
SOURCE_FILES := code_generator.cc protoc_gen_js.cc
PROTO_GEN_FILES_CC := $(patsubst %.proto, %.pb.cc, $(PROTO_FILES))
PROTOBUF := /usr/local/opt/protobuf/include

protoc-gen-js: $(SOURCE_FILES) $(PROTO_GEN_FILES_CC)
	g++ $(SOURCE_FILES) $(PROTO_GEN_FILES_CC) \
		-I . \
		-I $(PROTOBUF) \
		-L /usr/local/lib \
		-lprotobuf \
		-lprotoc \
		-o $@

js/%.pb.cc: js/%.proto
	protoc --cpp_out=./js -I$(PROTOBUF) --proto_path=js $<

clean:
	rm -fr *.pb.*
	rm -fr protoc-gen-js
