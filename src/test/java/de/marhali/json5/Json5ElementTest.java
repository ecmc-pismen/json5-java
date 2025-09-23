/*
 * Copyright (C) 2022 - 2025 Marcel Haßlinger
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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Marcel Haßlinger
 */
public class Json5ElementTest {
    @Test
    void getAsJson5Array_throws_on_non_object() {
        var ex = assertThrows(IllegalStateException.class, () -> Json5Primitive.fromString("anyString").getAsJson5Object());
        assertEquals("Not a Json5Object: \"anyString\"", ex.getMessage());
    }

    @Test
    void getAsJson5Object_throws_on_non_array() {
        var ex = assertThrows(IllegalStateException.class, () -> Json5Primitive.fromString("anyString").getAsJson5Array());
        assertEquals("Not a Json5Array: \"anyString\"", ex.getMessage());
    }

    @Test
    void getAsJson5Primitive_throws_on_non_primitive() {
        var ex = assertThrows(IllegalStateException.class, () -> new Json5Object().getAsJson5Primitive());
        assertEquals("Not a Json5Primitive: {\n}", ex.getMessage());
    }

    @Test
    void getAsJson5Null_throws_on_null() {
        var ex = assertThrows(IllegalStateException.class, () -> Json5Primitive.fromString("anyString").getAsJson5Null());
        assertEquals("Not a Json5Null: \"anyString\"", ex.getMessage());
    }

    @Test
    void getAsBoolean_throws_on_non_boolean() {
        var ex = assertThrows(UnsupportedOperationException.class, () -> new Json5Object().getAsBoolean());
        assertEquals("Json5Object", ex.getMessage());
    }

    @Test
    void getAsInstant_throws_on_non_boolean() {
        var ex = assertThrows(UnsupportedOperationException.class, () -> new Json5Object().getAsInstant());
        assertEquals("Json5Object", ex.getMessage());
    }

    @Test
    void getAsNumber_throws_on_non_boolean() {
        var ex = assertThrows(UnsupportedOperationException.class, () -> new Json5Object().getAsNumber());
        assertEquals("Json5Object", ex.getMessage());
    }

    @Test
    void getAsRadixNumber_throws_on_non_boolean() {
        var ex = assertThrows(UnsupportedOperationException.class, () -> new Json5Object().getAsRadixNumber());
        assertEquals("Json5Object", ex.getMessage());
    }

    @Test
    void getAsString_throws_on_non_boolean() {
        var ex = assertThrows(UnsupportedOperationException.class, () -> new Json5Object().getAsString());
        assertEquals("Json5Object", ex.getMessage());
    }

    @Test
    void getAsDouble_throws_on_non_boolean() {
        var ex = assertThrows(UnsupportedOperationException.class, () -> new Json5Object().getAsDouble());
        assertEquals("Json5Object", ex.getMessage());
    }

    @Test
    void getAsFloat_throws_on_non_boolean() {
        var ex = assertThrows(UnsupportedOperationException.class, () -> new Json5Object().getAsFloat());
        assertEquals("Json5Object", ex.getMessage());
    }

    @Test
    void getAsLong_throws_on_non_boolean() {
        var ex = assertThrows(UnsupportedOperationException.class, () -> new Json5Object().getAsLong());
        assertEquals("Json5Object", ex.getMessage());
    }

    @Test
    void getAsInt_throws_on_non_boolean() {
        var ex = assertThrows(UnsupportedOperationException.class, () -> new Json5Object().getAsInt());
        assertEquals("Json5Object", ex.getMessage());
    }

    @Test
    void getAsByte_throws_on_non_boolean() {
        var ex = assertThrows(UnsupportedOperationException.class, () -> new Json5Object().getAsByte());
        assertEquals("Json5Object", ex.getMessage());
    }

    @Test
    void getAsBigDecimal_throws_on_non_boolean() {
        var ex = assertThrows(UnsupportedOperationException.class, () -> new Json5Object().getAsBigDecimal());
        assertEquals("Json5Object", ex.getMessage());
    }

    @Test
    void getAsBigInteger_throws_on_non_boolean() {
        var ex = assertThrows(UnsupportedOperationException.class, () -> new Json5Object().getAsBigInteger());
        assertEquals("Json5Object", ex.getMessage());
    }

    @Test
    void getAsShort_throws_on_non_boolean() {
        var ex = assertThrows(UnsupportedOperationException.class, () -> new Json5Object().getAsShort());
        assertEquals("Json5Object", ex.getMessage());
    }

    @Test
    void getAsBinaryString_throws_on_non_boolean() {
        var ex = assertThrows(UnsupportedOperationException.class, () -> new Json5Object().getAsBinaryString());
        assertEquals("Json5Object", ex.getMessage());
    }

    @Test
    void getAsOctalString_throws_on_non_boolean() {
        var ex = assertThrows(UnsupportedOperationException.class, () -> new Json5Object().getAsOctalString());
        assertEquals("Json5Object", ex.getMessage());
    }

    @Test
    void getAsHexString_throws_on_non_boolean() {
        var ex = assertThrows(UnsupportedOperationException.class, () -> new Json5Object().getAsHexString());
        assertEquals("Json5Object", ex.getMessage());
    }
}
