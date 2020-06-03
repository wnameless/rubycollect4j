/*
 *
 * Copyright 2013 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package net.sf.rubycollect4j.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ComparableEntryTest {

  ComparableEntry<Integer, Integer> entry;

  @BeforeEach
  public void setUp() throws Exception {
    entry = new ComparableEntry<Integer, Integer>(0, 1);
  }

  @Test
  public void testConstructor() {
    assertTrue(entry instanceof ComparableEntry);
    entry = new ComparableEntry<Integer, Integer>(
        new SimpleEntry<Integer, Integer>(0, 1));
    assertTrue(entry instanceof ComparableEntry);
  }

  @Test
  public void testInterfaces() {
    assertTrue(entry instanceof Entry);
    assertTrue(entry instanceof Comparable);
  }

  @Test
  public void testGetKey() {
    assertEquals(Integer.valueOf(0), entry.getKey());
  }

  @Test
  public void testGetValue() {
    assertEquals(Integer.valueOf(1), entry.getValue());
  }

  @Test
  public void testSetValue() {
    assertEquals(Integer.valueOf(1), entry.setValue(2));
    assertEquals(Integer.valueOf(2), entry.getValue());
  }

  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void testEquals() {
    assertTrue(entry.equals(new SimpleEntry<Integer, Integer>(0, 1)));
    assertFalse(entry.equals(null));
  }

  @Test
  public void testHashCode() {
    assertEquals(new SimpleEntry<Integer, Integer>(0, 1).hashCode(),
        entry.hashCode());
  }

  @Test
  public void testToString() {
    assertEquals(new SimpleEntry<Integer, Integer>(0, 1).toString(),
        entry.toString());
  }

  @Test
  public void testCompareTo() {
    assertEquals(0, entry.compareTo(new SimpleEntry<Integer, Integer>(0, 1)));
    assertEquals(-1, entry.compareTo(new SimpleEntry<Integer, Integer>(2, 3)));
    assertEquals(1, entry.compareTo(new SimpleEntry<Integer, Integer>(0, 0)));
    assertEquals(0,
        entry.compareTo(new SimpleEntry<Integer, Integer>(0, null)));
    assertEquals(0,
        new ComparableEntry<Integer, Entry<Integer, Integer>>(0,
            new SimpleEntry<Integer, Integer>(1, 1)).compareTo(
                new ComparableEntry<Integer, Entry<Integer, Integer>>(0,
                    new SimpleEntry<Integer, Integer>(1, 1))));
  }

  @Test
  public void testCompareToException1() {
    assertThrows(IllegalArgumentException.class, () -> {
      entry.compareTo(null);
    });
  }

}
