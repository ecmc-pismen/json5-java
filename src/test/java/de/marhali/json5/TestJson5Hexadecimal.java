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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Json5Hexadecimal} primitive data type.
 *
 * @author Marcel Haßlinger
 */
class TestJson5Hexadecimal {

    @Test
    void json5primitive_handles_neutral_hex_numbers() {
        String hex = "0x100";
        Json5Primitive primitive = new Json5Hexadecimal(hex);
        assertEquals(256, primitive.getAsInt());
        assertEquals(hex, primitive.getAsString());
    }

    @Test
    void json5primitive_handles_positive_hex_numbers() {
        String hex = "+0x100";
        Json5Primitive primitive = new Json5Hexadecimal(hex);
        assertEquals(256, primitive.getAsInt());
        assertEquals("0x100", primitive.getAsString()); // We cut the '+' by default since we cannot track this state
    }

    @Test
    void json5hexadecimal_serialize_positive_prefixed_hex() {
        String hex = "0x100";
        Json5Primitive primitive = new Json5Hexadecimal(hex);
        assertEquals("+0x100", Json5Hexadecimal.serializeHexString(primitive.getAsBigInteger(), true));
    }

    @Test
    void json5hexadecimal_serialize_negative_prefixed_hex() {
        String hex = "-0x100";
        Json5Primitive primitive = new Json5Hexadecimal(hex);
        assertEquals(primitive.getAsInt(), -256);
        assertEquals(hex, primitive.getAsString());
    }

    @Test
    void json5primitive_of_a_hex_string_becomes_an_instance_of_json5hexadecimal() {
        assertTrue(Json5Primitive.of("0x100", true) instanceof Json5Hexadecimal);
        assertFalse(Json5Primitive.of("0x100", false) instanceof Json5Hexadecimal);
    }
}
