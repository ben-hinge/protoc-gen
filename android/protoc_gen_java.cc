#include <google/protobuf/compiler/plugin.h>

#include <code_generator.h>

int main(int argc, char *argv[]) {
  google::protobuf::compiler::js::CodeGenerator generator("android_plugin");
  return google::protobuf::compiler::PluginMain(argc, argv, &generator);
}
