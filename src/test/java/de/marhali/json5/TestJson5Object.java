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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Json5Object}
 *
 * @author Marcel Haßlinger
 */
class TestJson5Object {

    @Test
    void add_and_remove_properties_in_a_json_object() {
        Json5Object element = new Json5Object();
        element.addProperty("str", "Lorem ipsum");
        element.remove("str");
        assertEquals(0, element.size());
        assertFalse(element.has("str"));
    }

    @Test
    void a_json_array_is_not_an_json_object() {
        Json5Element element = new Json5Array();
        assertThrows(IllegalStateException.class, element::getAsJson5Object);
    }

    @Test
    void make_a_deep_copy_of_a_json_object() {
        Json5Object element = new Json5Object();
        element.addProperty("str", "Lorem ipsum");
        element.addProperty("bool", true);

        Json5Object target = element.deepCopy();

        element.remove("str");
        element.remove("bool");
        assertEquals(0, element.size());

        assertEquals(2, target.size());
        assertInstanceOf(Json5String.class, target.getAsJson5Primitive("str"));
        assertInstanceOf(Json5Boolean.class, target.getAsJson5Primitive("bool"));
    }

    @Test
    void add_a_property_to_a_json_object() {
        Json5Object object = new Json5Object();
        object.addProperty("char", 'c');
        object.add("obj", new Json5Object());
        object.add("array", new Json5Array());

        assertInstanceOf(Json5String.class, object.get("char"));
        assertEquals(new Json5Object(), object.getAsJson5Object("obj"));
        assertEquals(Set.of("char", "obj", "array"), object.keySet());
        assertInstanceOf(Json5Array.class, object.getAsJson5Array("array"));
        assertEquals(0, object.getAsJson5Array("array").size());
    }
}
