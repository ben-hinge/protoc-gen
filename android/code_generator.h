// Copyright (c) 2010 SameGoal LLC.
// All Rights Reserved.
// Author: Andy Hochhaus <ahochhaus@samegoal.com>

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

#ifndef PROTOBUF_JS_CODE_GENERATOR_H_
#define PROTOBUF_JS_CODE_GENERATOR_H_

#include <string>

#include "google/protobuf/compiler/code_generator.h"
#include "google/protobuf/descriptor.h"
#include "google/protobuf/io/printer.h"

namespace google {
namespace protobuf {
namespace compiler {
namespace js {

class CodeGenerator : public ::google::protobuf::compiler::CodeGenerator {
 public:
  explicit CodeGenerator(const std::string& name);
  virtual ~CodeGenerator();

  virtual bool Generate(
      const google::protobuf::FileDescriptor *file,
      const std::string &parameter,
      google::protobuf::compiler::OutputDirectory *output_directory,
      std::string *error) const;

 private:
  std::string name_;

  static std::string JsFullName(
      const google::protobuf::FileDescriptor *file,
      const std::string &full_name);

  static void GenDescriptor(
      const google::protobuf::Descriptor *message,
      google::protobuf::io::Printer *printer);

  static void GenEnum(
      const google::protobuf::EnumDescriptor *enum_desc,
      google::protobuf::io::Printer *printer);

  static void GenMessage_equality(
      const google::protobuf::Descriptor *message,
      google::protobuf::io::Printer *printer);
  
  static void GenMessage_fromReader(
      const google::protobuf::Descriptor *message,
      google::protobuf::io::Printer *printer);

  static void GenMessage_builder(
      const google::protobuf::Descriptor *message,
      google::protobuf::io::Printer *printer);

  static void GenMessageBuilder(
      const google::protobuf::Descriptor *message,
      google::protobuf::io::Printer *printer);

  static void GenMessage_toWriter(
      const google::protobuf::Descriptor *message,
      google::protobuf::io::Printer *printer);

  static void GenMessage_sizeOf(
      const google::protobuf::Descriptor *message,
      google::protobuf::io::Printer *printer);
};

}  // namespace js
}  // namespace compiler
}  // namespace protobuf
}  // namespace google

#endif  // PROTOBUF_JS_CODE_GENERATOR_H_
