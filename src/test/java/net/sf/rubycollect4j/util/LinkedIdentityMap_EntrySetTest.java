/**
 *
 * @author Wei-Ming Wu
 *
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

import static net.sf.rubycollect4j.RubyCollections.hp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sun.awt.util.IdentityLinkedList;

public class LinkedIdentityMap_EntrySetTest {

  private IdentityLinkedList<String> list = new IdentityLinkedList<String>();
  private IdentityHashMap<String, Integer> map =
      new IdentityHashMap<String, Integer>();
  private LinkedIdentityMap.EntrySet<String, Integer> entrySet =
      new LinkedIdentityMap.EntrySet<String, Integer>(list, map);

  private String key1 = "a";
  private String key2 = new String("a");
  private String key3 = new String("a");

  @Before
  public void setUp() throws Exception {
    list.add(key1);
    map.put(key1, 1);
    list.add(key2);
    map.put(key2, 2);
    list.add(key3);
    map.put(key3, 3);
  }

  @After
  public void tearDown() throws Exception {
    list.clear();
    map.clear();
  }

  @Test
  public void testConstructor() {
    assertTrue(entrySet instanceof LinkedIdentityMap.EntrySet);
  }

  @Test
  public void testSize() {
    assertEquals(3, entrySet.size());
    entrySet.clear();
    assertEquals(0, entrySet.size());
  }

  @Test
  public void testIsEmpty() {
    assertFalse(entrySet.isEmpty());
    entrySet.clear();
    assertTrue(entrySet.isEmpty());
  }

  @Test
  public void testContains() {
    list.add(null);
    map.put(null, 4);
    assertTrue(entrySet.contains(hp(key1, 1)));
    assertFalse(entrySet.contains(hp(new String("a"), 1)));
    assertFalse(entrySet.contains(null));
    assertFalse(entrySet.contains(hp(null, 5)));
    map.put(null, null);
    assertTrue(entrySet.contains(hp(null, null)));
    assertFalse(entrySet.contains(hp(null, 5)));
  }

  @Test
  public void testIterator() {
    Iterator<Entry<String, Integer>> iter = entrySet.iterator();
    assertTrue(iter instanceof Iterator);
    assertSame(key1, iter.next().getKey());
    assertSame(key2, iter.next().getKey());
    iter.remove();
    assertSame(key3, iter.next().getKey());
    assertFalse(iter.hasNext());
    assertEquals(2, list.size());
    assertSame(key1, entrySet.toArray(new Entry[2])[0].getKey());
    assertSame(key3, entrySet.toArray(new Entry[2])[1].getKey());
  }

  @Test
  public void testToArray() {
    assertEquals(3, entrySet.toArray().length);
    assertSame(key1, ((Entry<?, ?>) entrySet.toArray()[0]).getKey());
    assertSame(key2, ((Entry<?, ?>) entrySet.toArray()[1]).getKey());
    assertSame(key3, ((Entry<?, ?>) entrySet.toArray()[2]).getKey());
  }

  @Test
  public void testToArrayWithGeneric() {
    assertEquals(3, entrySet.toArray().length);
    assertSame(key1, ((Entry<?, ?>) entrySet.toArray(new Entry[3])[0]).getKey());
    assertSame(key2, ((Entry<?, ?>) entrySet.toArray(new Entry[3])[1]).getKey());
    assertSame(key3, ((Entry<?, ?>) entrySet.toArray(new Entry[3])[2]).getKey());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAdd() {
    entrySet.add(null);
  }

  @Test
  public void testRemove() {
    list.add(null);
    map.put(null, 4);
    assertFalse(entrySet.remove(hp(new String("a"), 1)));
    assertTrue(entrySet.remove(hp(key1, 1)));
    assertEquals(3, list.size());
    assertSame(key2, list.get(0));
    assertSame(key3, list.get(1));
    assertSame(null, list.get(2));
    assertFalse(entrySet.remove(null));
    assertFalse(entrySet.remove(hp(null, 5)));
    map.put(null, null);
    assertFalse(entrySet.remove(hp(null, 5)));
    assertTrue(entrySet.remove(hp(null, null)));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testContainsAll() {
    assertTrue(entrySet.containsAll(Arrays.asList(hp(key1, 1), hp(key2, 2))));
    assertFalse(entrySet.containsAll(Arrays.asList(hp(key1, 1),
        hp(new String("a"), 2))));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddAll() {
    entrySet.addAll(null);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testRetainAll() {
    assertTrue(entrySet
        .retainAll(Arrays.asList(hp(key1, 1), hp(key2, 2), null)));
    assertEquals(2, list.size());
    assertSame(key1, list.get(0));
    assertSame(key2, list.get(1));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testRemoveAll() {
    assertTrue(entrySet.removeAll(Arrays.asList(hp(key1, 1), hp(key2, 2))));
    assertEquals(1, list.size());
    assertSame(key3, list.get(0));
  }

  @Test
  public void testClear() {
    assertFalse(entrySet.isEmpty());
    entrySet.clear();
    assertTrue(entrySet.isEmpty());
  }

  @Test
  public void testEquals() {
    Map<Object, Object> idMap = new IdentityHashMap<Object, Object>();
    idMap.put(key1, 1);
    idMap.put(key2, 2);
    idMap.put(key3, 3);
    assertTrue(entrySet.equals(idMap.entrySet()));
    idMap.put(new String("a"), 4);
    assertFalse(entrySet.equals(idMap.entrySet()));
  }

  @Test
  public void testHashCode() {
    Map<Object, Object> idMap = new IdentityHashMap<Object, Object>();
    idMap.put(key1, 1);
    idMap.put(key2, 2);
    idMap.put(key3, 3);
    assertEquals(idMap.entrySet().hashCode(), entrySet.hashCode());
  }

  @Test
  public void testToString() {
    assertEquals("[a=1, a=2, a=3]", entrySet.toString());
  }

}
