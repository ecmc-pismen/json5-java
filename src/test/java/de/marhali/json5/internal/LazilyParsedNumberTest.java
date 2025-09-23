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

package de.marhali.json5.internal;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Marcel Haßlinger
 */
class LazilyParsedNumberTest {

    @Test
    void toString_returnsOriginal() {
        assertEquals("123", new LazilyParsedNumber("123").toString());
        assertEquals("1.50", new LazilyParsedNumber("1.50").toString());
    }

    @Test
    void int_long_parse_directInteger() {
        LazilyParsedNumber n = new LazilyParsedNumber("123");
        assertEquals(123, n.intValue());
        assertEquals(123L, n.longValue());
    }

    @Test
    void float_double_parse_decimal() {
        LazilyParsedNumber n = new LazilyParsedNumber("1.5");
        assertEquals(1.5f, n.floatValue(), 0.000001f);
        assertEquals(1.5, n.doubleValue(), 0.0000000001);
        // Integer/Long parse fails -> BigDecimal.intValue()
        assertEquals(new BigDecimal("1.5").intValue(), n.intValue());
    }

    @Test
    void intValue_fallsBackToLongAndNarrows() {
        // 2147483648 = Integer.MAX_VALUE + 1
        String s = "2147483648";
        LazilyParsedNumber n = new LazilyParsedNumber(s);

        // Long.parseLong works, intValue uses (int) long → Overflows to MIN_VALUE
        long expectedLong = Long.parseLong(s);
        int expectedInt = (int) expectedLong;

        assertEquals(expectedLong, n.longValue());
        assertEquals(expectedInt, n.intValue());
    }

    @Test
    void longAndInt_fallBackToBigDecimalWhenTooLargeForLong() {
        // Long.MAX_VALUE + 1 -> Long.parseLong causes NFE, BigDecimal will be used
        String s = "9223372036854775808";
        LazilyParsedNumber n = new LazilyParsedNumber(s);

        long expectedLong = new BigDecimal(s).longValue();
        int expectedInt = new BigDecimal(s).intValue();

        assertEquals(expectedLong, n.longValue());
        assertEquals(expectedInt, n.intValue());
        assertEquals(Double.parseDouble(s), n.doubleValue(), 0.0);
    }

    @Test
    void equals_and_hashCode_behave() {
        LazilyParsedNumber a1 = new LazilyParsedNumber("42");
        LazilyParsedNumber a2 = new LazilyParsedNumber("42");
        LazilyParsedNumber b = new LazilyParsedNumber("043");

        assertEquals(a1, a1);              // reflexive
        assertEquals(a1, a2);              // same string content
        assertNotEquals(a1, b);            // other content
        assertNotEquals(a1, new Object()); // other type

        assertEquals("42".hashCode(), a1.hashCode());
    }

    @Test
    void serialization_usesWriteReplace_returnsBigDecimalOnRead() throws Exception {
        LazilyParsedNumber n = new LazilyParsedNumber("123.45");
        Object roundTripped = roundTrip(n);

        assertTrue(roundTripped instanceof BigDecimal, "Deserialize should return BigDecimal");
        assertEquals(new BigDecimal("123.45"), roundTripped);
    }

    private static Object roundTrip(Object o) throws IOException, ClassNotFoundException {
        byte[] bytes;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(o);
            oos.flush();
            bytes = baos.toByteArray();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return ois.readObject();
        }
    }
}
