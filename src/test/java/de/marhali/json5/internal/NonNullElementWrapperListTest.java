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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Marcel Haßlinger
 */
class NonNullElementWrapperListTest {

    private static <E> NonNullElementWrapperList<E> wrap(ArrayList<E> delegate) {
        return new NonNullElementWrapperList<>(delegate);
    }

    @Test
    void constructor_nullDelegate_throwsNPE() {
        assertThrows(NullPointerException.class, () -> new NonNullElementWrapperList<>(null));
    }

    @Test
    void isRandomAccess() {
        NonNullElementWrapperList<String> list = wrap(new ArrayList<>());
        assertTrue(list instanceof RandomAccess, "Wrapper should implement RandomAccess");
    }

    @Test
    void size_get_add_nonNull_ok() {
        NonNullElementWrapperList<String> list = wrap(new ArrayList<>());
        assertEquals(0, list.size());
        list.add(0, "a");
        list.add(1, "b");
        assertEquals(2, list.size());
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
    }

    @Test
    void add_null_throwsNPE() {
        NonNullElementWrapperList<String> list = wrap(new ArrayList<>());
        NullPointerException ex = assertThrows(NullPointerException.class, () -> list.add(0, null));
        assertTrue(ex.getMessage() == null || ex.getMessage().toLowerCase().contains("non-null"));
    }

    @Test
    void set_replacesAndReturnsOld_nonNull_ok() {
        NonNullElementWrapperList<String> list = wrap(new ArrayList<>(Arrays.asList("x", "y")));
        String old = list.set(1, "z");
        assertEquals("y", old);
        assertEquals(Arrays.asList("x", "z"), new ArrayList<>(list));
    }

    @Test
    void set_null_throwsNPE() {
        NonNullElementWrapperList<String> list = wrap(new ArrayList<>(Arrays.asList("x")));
        assertThrows(NullPointerException.class, () -> list.set(0, null));
        assertEquals(Collections.singletonList("x"), new ArrayList<>(list));
    }

    @Test
    void contains_and_indexOf_withNull_doNotThrow() {
        NonNullElementWrapperList<String> list = wrap(new ArrayList<>(Arrays.asList("a", "b")));
        assertFalse(list.contains(null));
        assertEquals(-1, list.indexOf(null));
        assertEquals(-1, list.lastIndexOf(null));
    }

    @Test
    void remove_byIndex_and_byObject_behave() {
        NonNullElementWrapperList<String> list = wrap(new ArrayList<>(Arrays.asList("a", "b", "c")));
        assertEquals("b", list.remove(1));
        assertEquals(Arrays.asList("a", "c"), new ArrayList<>(list));

        // remove(Object) with null must not throw and should return false
        assertDoesNotThrow(() -> {
            boolean removed = list.remove((Object) null);
            assertFalse(removed);
        });

        // remove existing object
        assertTrue(list.remove("a"));
        assertEquals(Collections.singletonList("c"), new ArrayList<>(list));
    }

    @Test
    void clear_removeAll_retainAll_delegateCorrectly() {
        NonNullElementWrapperList<String> list = wrap(new ArrayList<>(Arrays.asList("a", "b", "c", "d")));
        assertTrue(list.removeAll(Arrays.asList("b", "x"))); // removes b
        assertEquals(Arrays.asList("a", "c", "d"), new ArrayList<>(list));

        assertTrue(list.retainAll(new HashSet<>(Arrays.asList("a", "d")))); // keeps a,d
        assertEquals(Arrays.asList("a", "d"), new ArrayList<>(list));

        list.clear();
        assertTrue(list.isEmpty());
    }

    @Test
    void toArray_variants_matchDelegate() {
        NonNullElementWrapperList<String> list = wrap(new ArrayList<>(Arrays.asList("a", "b")));
        Object[] arr = list.toArray();
        assertArrayEquals(new Object[]{"a", "b"}, arr);

        String[] into = new String[0];
        String[] arr2 = list.toArray(into);
        assertArrayEquals(new String[]{"a", "b"}, arr2);
    }

    @Test
    void equals_and_hashCode_delegateBehavior_and_symmetry() {
        ArrayList<String> base = new ArrayList<>(Arrays.asList("a", "b"));
        NonNullElementWrapperList<String> w1 = wrap(new ArrayList<>(base));
        NonNullElementWrapperList<String> w2 = wrap(new ArrayList<>(base));
        ArrayList<String> otherList = new ArrayList<>(base);

        // equals compares by elements via delegate.equals(o)
        assertEquals(w1, w2);                 // wrapper vs wrapper
        assertEquals(w1, otherList);          // wrapper vs plain List
        assertEquals(otherList, w1);          // symmetry from the other side

        assertEquals(otherList.hashCode(), w1.hashCode());
        // change elements -> not equal
        w2.add(2, "c");
        assertNotEquals(w1, w2);
    }
}
