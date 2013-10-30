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
package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.hp;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.rh;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class OrderedEntrySetIterableTest {

  private OrderedEntrySetIterable<String, Integer> setIter;
  private List<String> list;

  @Before
  public void setUp() throws Exception {
    list = ra("a", "b", "c");
    setIter =
        new OrderedEntrySetIterable<String, Integer>(list, rh("c", 3, "b", 2,
            "a", 1));
  }

  @Test
  public void testConstructor() {
    assertTrue(setIter instanceof OrderedEntrySetIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new OrderedEntrySetIterable<String, Integer>(null, rh("c", 3, "b", 2, "a",
        1));
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new OrderedEntrySetIterable<String, Integer>(list, null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAdd() {
    setIter.add(hp("d", 4));
  }

  @SuppressWarnings("unchecked")
  @Test(expected = UnsupportedOperationException.class)
  public void testAddAll() {
    setIter.addAll(Arrays.asList(hp("d", 4)));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testClear() {
    setIter.clear();
  }

  @Test
  public void testContains() {
    assertTrue(setIter.contains(hp("a", 1)));
    assertFalse(setIter.contains(hp("d", 4)));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testContainsAll() {
    assertTrue(setIter.containsAll(Arrays.asList(hp("a", 1), hp("b", 2))));
    assertFalse(setIter.containsAll(Arrays.asList(hp("c", 3), hp("d", 4))));
  }

  @Test
  public void testIsEmpty() {
    assertFalse(setIter.isEmpty());
    setIter =
        new OrderedEntrySetIterable<String, Integer>(new ArrayList<String>(),
            new HashMap<String, Integer>());
    assertTrue(setIter.isEmpty());
  }

  @Test
  public void testIterator() {
    assertTrue(setIter.iterator() instanceof Iterator);
    assertTrue(setIter.iterator() instanceof OrderedEntrySetIterator);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemove() {
    setIter.remove(hp("a", 1));
  }

  @SuppressWarnings("unchecked")
  @Test(expected = UnsupportedOperationException.class)
  public void testRemoveAll() {
    setIter.removeAll(Arrays.asList(hp("a", 1), hp("b", 2)));
  }

  @SuppressWarnings("unchecked")
  @Test(expected = UnsupportedOperationException.class)
  public void testRetainAll() {
    setIter.retainAll(Arrays.asList(hp("a", 1), hp("b", 2)));
  }

  @Test
  public void testSize() {
    assertEquals(3, setIter.size());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testToArray() {
    assertTrue(Arrays.equals(ra(hp("a", 1), hp("b", 2), hp("c", 3)).toArray(),
        setIter.toArray()));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testToArrayWithGeneric() {
    assertTrue(Arrays.equals(
        ra(hp("a", 1), hp("b", 2), hp("c", 3)).toArray(new Entry[3]),
        setIter.toArray(new Entry[3])));
  }

  @Test
  public void testEquals() {
    Set<Entry<String, Integer>> set = new HashSet<Entry<String, Integer>>();
    set.add(new SimpleEntry<String, Integer>("a", 1));
    set.add(new SimpleEntry<String, Integer>("b", 2));
    set.add(new SimpleEntry<String, Integer>("c", 3));
    assertTrue(setIter.equals(set));
    assertTrue(set.equals(setIter));
    set.add(new SimpleEntry<String, Integer>("d", 4));
    assertFalse(setIter.equals(set));
    assertFalse(set.equals(setIter));
  }

  @Test
  public void testHashCode() {
    Set<Entry<String, Integer>> set = new HashSet<Entry<String, Integer>>();
    set.add(new SimpleEntry<String, Integer>("a", 1));
    set.add(new SimpleEntry<String, Integer>("b", 2));
    set.add(new SimpleEntry<String, Integer>("c", 3));
    assertEquals(set.hashCode(), setIter.hashCode());
    set.add(new SimpleEntry<String, Integer>("d", 4));
    assertNotEquals(set.hashCode(), setIter.hashCode());
  }

  @Test
  public void testToString() {
    assertEquals("[a=1, b=2, c=3]", setIter.toString());
  }

}
