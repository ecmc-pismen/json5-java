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
import de.marhali.json5.e2e.TestResourceHelper;
import de.marhali.json5.exception.Json5Exception;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Marcel Haßlinger
 */
public class MissingElementClosingTest {
    @Test
    void missing_object_closing_throws() {
        var json5 = Json5.builder(builder -> builder.duplicateKeyStrategy(DuplicateKeyStrategy.UNIQUE).build());

        var ex = assertThrows(Json5Exception.class, () -> json5.parse(TestResourceHelper.getTestResourceContent("e2e/failures/invalid-closing-object.json5")));

        assertEquals("A Json5Object must end with '}' at index 42 [character 0 in line 4]", ex.getMessage());
    }

    @Test
    void missing_array_closing_throws() {
        var json5 = Json5.builder(builder -> builder.duplicateKeyStrategy(DuplicateKeyStrategy.UNIQUE).build());

        var ex = assertThrows(Json5Exception.class, () -> json5.parse(TestResourceHelper.getTestResourceContent("e2e/failures/invalid-closing-array.json5")));

        assertEquals("A Json5Array must end with ']' at index 17 [character 0 in line 3]", ex.getMessage());
    }
}
