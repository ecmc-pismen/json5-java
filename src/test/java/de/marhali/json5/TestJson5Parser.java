/*
 * Copyright (C) 2022 Marcel Haßlinger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.marhali.json5;

import de.marhali.json5.exception.Json5Exception;
import de.marhali.json5.stream.Json5Lexer;
import de.marhali.json5.stream.Json5Parser;

import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Json5Parser}.
 *
 * @author Marcel Haßlinger
 */
class TestJson5Parser {

    @Test
    void parse_an_array_with_single_quotes() {
        String payload = "['hello',1,'two',{'key':'value'}]";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        Json5Array element = Json5Parser.parseArray(lexer);
        assertEquals(payload, element.toString(options));
        assertInstanceOf(Json5String.class, element.get(0).getAsJson5Primitive());
        assertInstanceOf(Json5Number.class, element.get(1).getAsJson5Primitive());
        assertTrue(element.get(3).isJson5Object());
    }

    @Test
    void parse_an_object_with_single_quotes() {
        String payload = "{'key':'value','array':['first','second'],'nested':{'key':'value'}}";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        Json5Object element = Json5Parser.parseObject(lexer);
        assertEquals(payload, element.toString(options));
        assertEquals("value", element.get("key").getAsString());
        assertTrue(element.get("array").isJson5Array());
    }

    @Test
    void determineArrayType() {
        String payload = "['first','second']";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        Json5Element element = Json5Parser.parse(lexer);
        assertTrue(element.isJson5Array());
        assertInstanceOf(Json5Array.class, element);
    }

    @Test
    void determineObjectType() {
        String payload = "{'key':'value'}";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        Json5Element element = Json5Parser.parse(lexer);
        assertTrue(element.isJson5Object());
        assertInstanceOf(Json5Object.class, element);
    }

    @Test
    void hexadecimal() {
        String payload = "{'key':0x100}";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        Json5Object element = Json5Parser.parseObject(lexer);
        assertEquals(payload, element.toString(options));
        assertInstanceOf(Json5Hexadecimal.class, element.getAsJson5Primitive("key"));
    }

    @Test
    void allow_mixed_single_and_double_quotes_in_arrays() {
        String payload = "[\"example\",'other']";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        Json5Array element = Json5Parser.parseArray(lexer);
        assertEquals("['example','other']", element.toString(options));
    }

    @Test
    void allow_non_quoted_properties() {
        String payload = "{ a: \"Test \\' 123\" }";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        Json5Object element = Json5Parser.parseObject(lexer);
        assertEquals("Test ' 123", element.get("a").getAsString());
    }

    @Test
    void allow_mixed_single_and_double_quotes() {
        String payload = "{ 'a': \"Test \\' 123\" }";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        Json5Object element = Json5Parser.parseObject(lexer);
        assertEquals("Test ' 123", element.get("a").getAsString());
    }

    @Test
    void recognize_escaped_characters() {
        String payload = "{ a: \"\\n\\r\\f\\b\\t\\v\\0\u12fa\\x7F\" }";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        Json5Object element = Json5Parser.parseObject(lexer);
        assertEquals("\n\r\f\b\t\u000B\0\u12fa\u007F", element.get("a").getAsString());
        assertInstanceOf(Json5String.class, element.get("a"));
    }

    @Test
    void recognize_special_numbers() {
        String payload = "[+NaN,NaN,-NaN,+Infinity,Infinity,-Infinity]";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        Json5Array element = Json5Parser.parseArray(lexer);
        assertEquals("[NaN,NaN,NaN,Infinity,Infinity,-Infinity]", element.toString(options));
        assertInstanceOf(Json5Number.class, element.get(0));
    }

    @Test
    void malformed_json_causes_a_json5exception_to_be_thrown() {
        String payload = "[10}";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        assertThrows(Json5Exception.class, () -> Json5Parser.parse(lexer));
    }

    @Test
    void an_empty_array_is_not_an_object() {
        String payload = "[]";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        assertThrows(Json5Exception.class, () -> Json5Parser.parseObject(lexer));
    }

    @Test
    void incomplete_json_causes_a_json5exception_to_be_thrown() {
        String payload = "{";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        assertThrows(Json5Exception.class, () -> Json5Parser.parseObject(lexer));
    }

    @Test
    void an_empty_object_is_not_an_array() {
        String payload = "{}";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        assertThrows(Json5Exception.class, () -> Json5Parser.parseArray(lexer));
    }

    @Test
    void incomplete_array_throws_a_json5exception() {
        String payload = "[";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        assertThrows(Json5Exception.class, () -> Json5Parser.parseArray(lexer));
    }

    @Test
    void duplicate_object_keys_throws_a_json5exception() {
        String payload = "{'key':'value','key':'value'}";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        assertThrows(Json5Exception.class, () -> Json5Parser.parseObject(lexer));
    }

    @Test
    void missing_key_value_divider_throws_a_json5exception() {
        String payload = "{'key''value'}";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        assertThrows(Json5Exception.class, () -> Json5Parser.parseObject(lexer));
    }

    @Test
    void missing_comma_between_keys_throws_a_json5exception() {
        String payload = "{'key':'value''otherKey':'value'}";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        assertThrows(Json5Exception.class, () -> Json5Parser.parseObject(lexer));
    }

    @Test
    void unknown_control_characters_throws_a_json5exception() {
        String payload = "|";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        assertThrows(Json5Exception.class, () -> Json5Parser.parse(lexer));
    }

    @Test
    void empty_payload_returns_a_null_object() {
        String payload = "";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        assertNull(Json5Parser.parse(lexer));
    }

    @Test
    void utf_16_is_alloewd_as_key_names() {
        String payload = "{ $Lorem\\u0041_Ipsum123指事字: 0 }";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        Json5Object element = Json5Parser.parseObject(lexer);
        assertTrue(element.has("$LoremA_Ipsum123指事字"));
    }

    @Test
    void multi_lines_comments_are_allowed() {
        String payload = "/**/{/**/a/**/:/**/'b'/**/}/**/";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        Json5Object element = Json5Parser.parseObject(lexer);
        assertTrue(element.has("a"));
    }

    @Test
    void single_line_comments_are_allowed() {
        String payload = "// test\n{ // lorem ipsum\n a: 'b'\n// test\n}// test";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        Json5Object element = Json5Parser.parseObject(lexer);
        assertTrue(element.has("a"));
    }

    @Test
    void boolean_values_are_parsed() {
        String payload = "[true,false]";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        Json5Array element = Json5Parser.parseArray(lexer);
        assertEquals(payload, element.toString(options));
        assertInstanceOf(Json5Boolean.class, element.get(0));
    }

    @Test
    void number_formats_are_parsed_correctly() {
        String payload = "[123e+45,-123e45,123]";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        Json5Array element = Json5Parser.parseArray(lexer);
        assertEquals(123e+45, element.get(0).getAsNumber().doubleValue());
        assertEquals(-123e45, element.get(1).getAsNumber().doubleValue());
        assertEquals(123, element.get(2).getAsNumber().doubleValue());
        assertTrue(element.get(0).isJson5Primitive());
        assertTrue(element.get(0).getAsJson5Primitive().isNumber());
        assertInstanceOf(Json5Number.class, element.get(2));
    }

    @Test
    void nullLiteral() {
        String payload = "[null,{'key':null}]";
        Json5Options options = new Json5Options(true, true, false, 0);
        Json5Lexer lexer = new Json5Lexer(new StringReader(payload), options);
        Json5Array element = Json5Parser.parseArray(lexer);
        assertEquals(payload, element.toString(options));
        assertTrue(element.get(0).isJson5Null());
        assertInstanceOf(Json5Null.class, element.get(0));
    }
}
