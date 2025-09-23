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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Marcel Haßlinger
 */
public class InvalidArrayParserTest {
    @Test
    void invalid_array_opening_tags_throws() {
        var reader = new StringReader("{}");
        var lexer = new Json5Lexer(reader, Json5Options.DEFAULT);

        var ex = assertThrows(Json5Exception.class, () -> Json5Parser.parseArray(lexer));

        assertEquals("A Json5Array must begin with '[' at index 0 [character 1 in line 1]", ex.getMessage());
    }

    @Test
    void invalid_array_closing_tags_throws() {
        var json5 = new Json5();

        var ex = assertThrows(Json5Exception.class, () -> json5.parse(TestResourceHelper.getTestResourceContent("e2e/failures/invalid-array-closing-tags.json5")));

        assertEquals("A Json5Array must end with ']' at index 17 [character 0 in line 3]", ex.getMessage());
    }

    @Test
    void invalid_array_missing_comma_throws() {
        var json5 = new Json5();

        var ex = assertThrows(Json5Exception.class, () -> json5.parse(TestResourceHelper.getTestResourceContent("e2e/failures/invalid-array-missing-comma.json5")));

        assertEquals("Expected ',' or ']' after value, got '[' instead at index 9 [character 3 in line 3]", ex.getMessage());
    }
}
