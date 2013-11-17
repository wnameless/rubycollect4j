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
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sun.awt.util.IdentityLinkedList;

public class LinkedIdentityMapValuesTest {

  private IdentityLinkedList<String> list = new IdentityLinkedList<String>();
  private IdentityHashMap<String, Integer> map =
      new IdentityHashMap<String, Integer>();
  private LinkedIdentityMapValues<String, Integer> values =
      new LinkedIdentityMapValues<String, Integer>(list, map);

  @Before
  public void setUp() throws Exception {
    list.add("a");
    map.put(list.get(0), 1);
    list.add(new String("a"));
    map.put(list.get(1), 2);
    list.add(new String("a"));
    map.put(list.get(2), 3);
  }

  @After
  public void tearDown() throws Exception {
    list.clear();
    map.clear();
  }

  @Test
  public void testConstructor() {
    assertTrue(values instanceof LinkedIdentityMapValues);
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
    assertArrayEquals(new Integer[] { 1, 2, 3 }, values.toArray(new Integer[3]));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAdd() {
    values.add(1);
  }

  @Test
  public void testRemove() {
    assertTrue(values.remove(1));
    assertFalse(values.remove(0));
    assertEquals(2, list.size());
    assertEquals(ra(2, 3), ra(values));
  }

  @Test
  public void testContainsAll() {
    assertTrue(values.containsAll(Arrays.asList(new Integer(1), 2, 3)));
    assertFalse(values.containsAll(Arrays.asList(new Integer(2), 3, 4)));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddAll() {
    values.addAll(Arrays.asList(4, 5, 6));
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
