/*
 *
 * Copyright 2013 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package net.sf.rubycollect4j.util;

import static net.sf.rubycollect4j.RubyCollections.hp;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.rh;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.IdentityHashMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.sf.rubycollect4j.RubyArray;

public class LinkedIdentityMapTest {

  LinkedIdentityMap<String, Integer> map = new LinkedIdentityMap<String, Integer>();

  @BeforeEach
  public void setUp() throws Exception {
    map.put("a", 1);
    map.put(new String("a"), 1);
  }

  @AfterEach
  public void tearDown() throws Exception {
    map.clear();
  }

  @Test
  public void testConstructor() {
    assertTrue(map instanceof LinkedIdentityMap);
    map = new LinkedIdentityMap<String, Integer>(rh("a", 1));
    assertTrue(map instanceof LinkedIdentityMap);
  }

  @Test
  public void testClear() {
    assertEquals(2, map.size());
    assertEquals(2, map.keySet().size());
    assertEquals(2, map.values().size());
    map.clear();
    assertEquals(0, map.size());
    assertEquals(0, map.keySet().size());
    assertEquals(0, map.values().size());
  }

  @Test
  public void testContainsKey() {
    assertFalse(map.containsKey(new String("a")));
    String key = "b";
    map.put(key, 2);
    assertTrue(map.containsKey(key));
    assertFalse(map.containsKey(new String("b")));
  }

  @Test
  public void testContainsValue() {
    map.put(new String("a"), null);
    assertTrue(map.containsValue(Integer.valueOf(1)));
    assertFalse(map.containsValue(2));
    assertTrue(map.containsValue(null));
  }

  @Test
  public void testEntrySet() {
    map.clear();
    String key1 = "a";
    String key2 = new String("a");
    String key3 = new String("a");
    map.put(key1, 1);
    map.put(key2, 2);
    map.put(key3, 3);
    assertEquals(ra(hp(key1, 1), hp(key2, 2), hp(key3, 3)), ra(map.entrySet()));
    assertSame(key1, ra(map.entrySet()).get(0).getKey());
    assertSame(key2, ra(map.entrySet()).get(1).getKey());
    assertSame(key3, ra(map.entrySet()).get(2).getKey());
  }

  @Test
  public void testGet() {
    map.clear();
    String key1 = "a";
    map.put(key1, 1);
    assertEquals(Integer.valueOf(1), map.get(key1));
    assertNull(map.get(new String("a")));
  }

  @Test
  public void testIsEmpty() {
    assertFalse(map.isEmpty());
    map.clear();
    assertTrue(map.isEmpty());
  }

  @Test
  public void testKeySet() {
    map.clear();
    assertTrue(ra(map.keySet()).isEmpty());
    String key1 = "a";
    String key2 = new String("a");
    String key3 = new String("a");
    map.put(key1, 1);
    map.put(key2, 2);
    map.put(key3, 3);
    RubyArray<String> ra = ra(map.keySet());
    assertSame(key1, ra.get(0));
    assertSame(key2, ra.get(1));
    assertSame(key3, ra.get(2));
  }

  @Test
  public void testPut() {
    map.clear();
    String key1 = "a";
    assertNull(map.put(key1, 1));
    assertEquals(Integer.valueOf(1), map.put(key1, 2));
    assertEquals(Integer.valueOf(2), map.get(key1));
    assertEquals(1, map.size());
    String key2 = new String("a");
    assertNull(map.put(key2, 3));
    assertEquals(Integer.valueOf(3), map.put(key2, 4));
    assertEquals(Integer.valueOf(4), map.get(key2));
    assertEquals(2, map.size());
  }

  @Test
  public void testPutAll() {
    map.clear();
    String key1 = "a";
    String key2 = new String("a");
    String key3 = new String("a");
    map.put(key1, 1);
    map.putAll(rh(key1, 2));
    map.putAll(rh(key2, 3));
    map.putAll(rh(key3, 4));
    assertEquals(ra(hp(key1, 2), hp(key2, 3), hp(key3, 4)), ra(map.entrySet()));
    assertSame(key1, ra(map.entrySet()).get(0).getKey());
    assertEquals(Integer.valueOf(2), ra(map.entrySet()).get(0).getValue());
    assertSame(key2, ra(map.entrySet()).get(1).getKey());
    assertEquals(Integer.valueOf(3), ra(map.entrySet()).get(1).getValue());
    assertSame(key3, ra(map.entrySet()).get(2).getKey());
    assertEquals(Integer.valueOf(4), ra(map.entrySet()).get(2).getValue());
    assertEquals(3, map.size());
  }

  @Test
  public void testRemove() {
    map.clear();
    String key1 = "a";
    map.put(key1, 1);
    String key2 = new String("a");
    map.put(key2, 2);
    assertEquals(Integer.valueOf(2), map.remove(key2));
    assertEquals(1, map.size());
    assertEquals(Integer.valueOf(1), map.remove(key1));
    assertTrue(map.isEmpty());
    assertTrue(ra(map.keySet()).isEmpty());
    assertNull(map.remove(key1));
  }

  @Test
  public void testSize() {
    assertEquals(2, map.size());
    map.clear();
    assertEquals(0, map.size());
    map.put("a", 1);
    assertEquals(1, map.size());
  }

  @Test
  public void testValues() {
    assertEquals(ra(1, 1), ra(map.values()));
    map.clear();
    String key1 = "a";
    String key2 = new String("a");
    String key3 = new String("a");
    map.put(key1, 3);
    map.put(key2, 2);
    map.put(key3, 1);
    assertEquals(ra(3, 2, 1), ra(map.values()));
  }

  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void testEquals() {
    map.clear();
    String key1 = new String("a");
    String key2 = new String("b");
    map.put(key1, 1);
    map.put(key2, 2);
    assertTrue(map.equals(rh(key1, 1, key2, 2)));
    assertTrue(rh(key1, 1, key2, 2).equals(map));
    assertFalse(map.equals(rh(new String("a"), 1, new String("b"), 2)));
    assertFalse(rh(new String("a"), 1, new String("b"), 2).equals(map));
  }

  @Test
  public void testHashCode() {
    map.clear();
    String key1 = "a";
    String key2 = "b";
    map.put(key1, 1);
    map.put(key2, 2);
    IdentityHashMap<String, Integer> idMap = new IdentityHashMap<String, Integer>();
    idMap.put(key1, 1);
    idMap.put(key2, 2);
    assertEquals(idMap.hashCode(), map.hashCode());
  }

  @Test
  public void testToString() {
    map.put("b", 2);
    map.put("c", 3);
    assertEquals("{a=1, a=1, b=2, c=3}", map.toString());
  }

}
