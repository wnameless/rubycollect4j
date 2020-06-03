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

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LinkedIdentityMap_ValuesTest {

  List<String> list = new LinkedList<String>();
  IdentityHashMap<String, Integer> map = new IdentityHashMap<String, Integer>();
  LinkedIdentityMap.Values<String, Integer> values =
      new LinkedIdentityMap.Values<String, Integer>(list, map);

  @BeforeEach
  public void setUp() throws Exception {
    list.add("a");
    map.put(list.get(0), 1);
    list.add(new String("a"));
    map.put(list.get(1), 2);
    list.add(new String("a"));
    map.put(list.get(2), 3);
  }

  @AfterEach
  public void tearDown() throws Exception {
    list.clear();
    map.clear();
  }

  @Test
  public void testConstructor() {
    assertTrue(values instanceof LinkedIdentityMap.Values);
  }

  @Test
  public void testSize() {
    assertEquals(3, values.size());
    values.clear();
    assertEquals(0, values.size());
  }

  @Test
  public void testIsEmpty() {
    assertFalse(values.isEmpty());
    values.clear();
    assertTrue(values.isEmpty());
  }

  @Test
  public void testContains() {
    list.add(new String("a"));
    map.put(list.get(3), null);
    assertFalse(values.contains(0));
    assertTrue(values.contains(1));
    assertTrue(values.contains(null));
  }

  @Test
  public void testIterator() {
    Iterator<Integer> iter = values.iterator();
    assertTrue(iter instanceof Iterator);
    assertEquals(Integer.valueOf(1), iter.next());
    assertEquals(Integer.valueOf(2), iter.next());
    iter.remove();
    assertEquals(Integer.valueOf(3), iter.next());
    assertFalse(iter.hasNext());
    assertEquals(2, list.size());
    assertEquals(ra(1, 3), ra(values));
  }

  @Test
  public void testToArray() {
    assertArrayEquals(new Integer[] { 1, 2, 3 }, values.toArray());
  }

  @Test
  public void testToArrayWithGeneric() {
    assertArrayEquals(new Integer[] { 1, 2, 3 },
        values.toArray(new Integer[3]));
  }

  @Test
  public void testAdd() {
    assertThrows(UnsupportedOperationException.class, () -> {
      values.add(null);
    });
  }

  @Test
  public void testRemove() {
    assertFalse(values.remove(0));
    assertTrue(values.remove(1));
    assertEquals(2, list.size());
    assertEquals(ra(2, 3), ra(values));
    list.add(new String("a"));
    map.put(list.get(list.size() - 1), 3);
    assertTrue(values.remove(3));
    assertEquals(ra(2, 3), ra(values));
    assertFalse(values.remove(null));
    list.add(new String("a"));
    map.put(list.get(list.size() - 1), null);
    assertTrue(values.remove(null));
  }

  @Test
  public void testContainsAll() {
    assertTrue(values.containsAll(Arrays.asList(Integer.valueOf(1), 2, 3)));
    assertFalse(values.containsAll(Arrays.asList(Integer.valueOf(2), 3, 4)));
  }

  @Test
  public void testAddAll() {
    assertThrows(UnsupportedOperationException.class, () -> {
      values.addAll(null);
    });
  }

  @Test
  public void testRemoveAll() {
    assertTrue(values.removeAll(Arrays.asList(1, 2)));
    assertEquals(1, list.size());
    assertEquals(ra(3), ra(values));
  }

  @Test
  public void testRetainAll() {
    assertTrue(values.retainAll(Arrays.asList(0, 1, 2)));
    assertEquals(2, list.size());
    assertEquals(ra(1, 2), ra(values));
  }

  @Test
  public void testClear() {
    assertFalse(values.isEmpty());
    values.clear();
    assertTrue(values.isEmpty());
  }

  @Test
  public void testToString() {
    assertEquals("[1, 2, 3]", values.toString());
  }

}
