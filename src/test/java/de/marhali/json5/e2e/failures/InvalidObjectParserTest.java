/*
 * Copyright (C) 2025 Marcel Haßlinger
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

package de.marhali.json5.e2e.failures;

import de.marhali.json5.Json5;
import de.marhali.json5.config.DuplicateKeyStrategy;
import de.marhali.json5.config.Json5Options;
import de.marhali.json5.e2e.TestResourceHelper;
import de.marhali.json5.exception.Json5Exception;
import de.marhali.json5.stream.Json5Lexer;
import de.marhali.json5.stream.Json5Parser;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Marcel Haßlinger
 */
public class InvalidObjectParserTest {
    @Test
    void invalid_object_opening_tags_throws() {
        var reader = new StringReader("[]");
        var lexer = new Json5Lexer(reader, Json5Options.DEFAULT);

        var ex = assertThrows(Json5Exception.class, () -> Json5Parser.parseObject(lexer));

        assertEquals("A Json5Object must begin with '{' at index 0 [character 1 in line 1]", ex.getMessage());
    }

    @Test
    void invalid_object_closing_tags_throws() {
        var json5 = new Json5();

        var ex = assertThrows(Json5Exception.class, () -> json5.parse(TestResourceHelper.getTestResourceContent("e2e/failures/invalid-object-closing-tags.json5")));

        assertEquals("A Json5Object must end with '}' at index 42 [character 0 in line 4]", ex.getMessage());
    }

    @Test
    void invalid_object_missing_key_suffix_throws() {
        var json5 = new Json5();

        var ex = assertThrows(Json5Exception.class, () -> json5.parse(TestResourceHelper.getTestResourceContent("e2e/failures/invalid-object-missing-key-suffix.json5")));

        assertEquals("Expected ':' after a key, got 'f' instead at index 12 [character 11 in line 2]", ex.getMessage());
    }

    @Test
    void invalid_object_missing_comma_throws() {
        var json5 = new Json5();

        var ex = assertThrows(Json5Exception.class, () -> json5.parse(TestResourceHelper.getTestResourceContent("e2e/failures/invalid-object-missing-comma.json5")));

        assertEquals("Expected ',' or '}' after value, got 's' instead at index 22 [character 3 in line 3]", ex.getMessage());
    }
}
