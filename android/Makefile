
SOURCE_FILES := code_generator.cc protoc_gen_android.cc
PROTOBUF := /usr/local/opt/protobuf/include

protoc-gen-android: $(SOURCE_FILES)
	g++ $(SOURCE_FILES) \
		-I . \
		-I $(PROTOBUF) \
		-L /usr/local/lib \
		-lprotobuf \
		-lprotoc \
		-o $@

%.pb.cc: %.proto
	protoc --cpp_out=. -I$(PROTOBUF) --proto_path=. $<

clean:
	rm -fr *.pb.*
	rm -fr protoc-gen-android
