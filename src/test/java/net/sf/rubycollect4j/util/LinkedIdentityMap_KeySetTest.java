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

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LinkedIdentityMap_KeySetTest {

  private List<String> list = new LinkedList<String>();
  private IdentityHashMap<String, Integer> map =
      new IdentityHashMap<String, Integer>();
  private LinkedIdentityMap.KeySet<String, Integer> keySet =
      new LinkedIdentityMap.KeySet<String, Integer>(list, map);

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
    assertTrue(keySet instanceof LinkedIdentityMap.KeySet);
  }

  @Test
  public void testSize() {
    assertEquals(3, keySet.size());
    keySet.clear();
    assertEquals(0, keySet.size());
  }

  @Test
  public void testIsEmpty() {
    assertFalse(keySet.isEmpty());
    keySet.clear();
    assertTrue(keySet.isEmpty());
  }

  @Test
  public void testContains() {
    list.add(null);
    map.put(null, 4);
    assertFalse(keySet.contains(new String("a")));
    assertTrue(keySet.contains(key1));
    assertTrue(keySet.contains(null));
  }

  @Test
  public void testIterator() {
    Iterator<String> iter = keySet.iterator();
    assertTrue(iter instanceof Iterator);
    assertSame(key1, iter.next());
    assertSame(key2, iter.next());
    iter.remove();
    assertSame(key3, iter.next());
    assertFalse(iter.hasNext());
    assertEquals(2, list.size());
    assertSame(key1, keySet.toArray()[0]);
    assertSame(key3, keySet.toArray()[1]);
  }

  @Test
  public void testToArray() {
    assertEquals(3, keySet.toArray().length);
    assertSame(key1, keySet.toArray()[0]);
    assertSame(key2, keySet.toArray()[1]);
    assertSame(key3, keySet.toArray()[2]);
  }

  @Test
  public void testToArrayWithGeneric() {
    assertSame(key1, keySet.toArray(new String[3])[0]);
    assertSame(key2, keySet.toArray(new String[3])[1]);
    assertSame(key3, keySet.toArray(new String[3])[2]);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAdd() {
    keySet.add(null);
  }

  @Test
  public void testRemove() {
    assertFalse(keySet.remove(new String("a")));
    assertTrue(keySet.remove(key1));
    assertEquals(2, list.size());
    assertEquals(ra(2, 3), ra(map.get(list.get(0)), map.get(list.get(1))));
  }

  @Test
  public void testContainsAll() {
    assertTrue(keySet.containsAll(Arrays.asList(key1, key2, key3)));
    assertFalse(keySet.containsAll(Arrays.asList(new String("a"), key2, key3)));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddAll() {
    keySet.addAll(null);
  }

  @Test
  public void testRetainAll() {
    assertTrue(keySet.retainAll(Arrays.asList(new String("a"), key2, key3)));
    assertEquals(2, list.size());
    assertEquals(ra(2, 3), ra(map.get(list.get(0)), map.get(list.get(1))));
  }

  @Test
  public void testRemoveAll() {
    assertTrue(keySet.removeAll(Arrays.asList(new String("a"), key2, key3)));
    assertEquals(1, list.size());
    assertEquals(ra(1), ra(map.get(list.get(0))));
  }

  @Test
  public void testClear() {
    assertFalse(keySet.isEmpty());
    keySet.clear();
    assertTrue(keySet.isEmpty());
  }

  @Test
  public void testEquals() {
    Map<Object, Object> idMap = new IdentityHashMap<Object, Object>();
    idMap.put(key1, 1);
    idMap.put(key2, 2);
    idMap.put(key3, 3);
    assertTrue(keySet.equals(idMap.keySet()));
    idMap.put(new String("a"), 4);
    assertFalse(keySet.equals(idMap.keySet()));
  }

  @Test
  public void testHashCode() {
    Map<Object, Object> idMap = new IdentityHashMap<Object, Object>();
    idMap.put(key1, 1);
    idMap.put(key2, 2);
    idMap.put(key3, 3);
    assertEquals(idMap.keySet().hashCode(), keySet.hashCode());
  }

  @Test
  public void testToString() {
    assertEquals("[a, a, a]", keySet.toString());
  }

}