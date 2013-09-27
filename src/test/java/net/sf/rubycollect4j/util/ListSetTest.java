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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ListSetTest {

  private List<Integer> list = new ArrayList<Integer>();
  private Set<Integer> set = new ListSet<Integer>(list);

  @Before
  public void setUp() {
    list.addAll(Arrays.asList(1, 2, 3));
  }

  @After
  public void tearDown() {
    list.clear();
  }

  @Test
  public void testConstructor() {
    assertEquals(ra(set), ra(list));
  }

  @Test
  public void testAdd() {
    list.add(4);
    assertEquals(4, ra(set).count());
    assertEquals(Integer.valueOf(4), ra(set).last());
    set.add(5);
    assertEquals(5, ra(set).count());
    assertEquals(Integer.valueOf(5), ra(set).last());
    set.add(5);
    assertEquals(6, ra(set).count());
    assertEquals(Integer.valueOf(5), ra(set).last());
  }

  @Test
  public void testAddAll() {
    set.addAll(Arrays.asList(4, 5, 6));
    assertEquals(ra(1, 2, 3, 4, 5, 6), ra(set));
  }

  @Test
  public void testClear() {
    set.clear();
    assertTrue(list.isEmpty());
    assertTrue(set.isEmpty());
    assertEquals(ra(), ra(set));
  }

  @Test
  public void testContains() {
    assertTrue(set.contains(1));
    assertFalse(set.contains(0));
  }

  @Test
  public void testContainsAll() {
    set.add(3);
    assertTrue(set.containsAll(Arrays.asList(1, 2, 3)));
    assertTrue(set.containsAll(Arrays.asList(1, 2)));
    assertFalse(set.containsAll(Arrays.asList(1, 2, 3, 4)));
  }

  @Test
  public void testIterator() {
    set.add(3);
    assertEquals(ra(1, 2, 3, 3), ra(set.iterator()));
  }

  @Test
  public void testRemove() {
    set.addAll(Arrays.asList(1, 2, 3));
    set.remove(3);
    assertEquals(ra(1, 2, 1, 2, 3), ra(set));
    set.remove(3);
    assertEquals(ra(1, 2, 1, 2), ra(set));
    set.remove(3);
    assertEquals(ra(1, 2, 1, 2), ra(set));
  }

  @Test
  public void testRemoveAll() {
    set.addAll(Arrays.asList(1, 2, 3));
    set.removeAll(Arrays.asList(3));
    assertEquals(ra(1, 2, 1, 2), ra(set));
    set.removeAll(Arrays.asList(3));
    assertEquals(ra(1, 2, 1, 2), ra(set));
  }

  @Test
  public void testRetainAll() {
    set.retainAll(Arrays.asList(1));
    assertEquals(ra(1), ra(set));
    set.addAll(Arrays.asList(1, 2, 3));
    set.retainAll(Arrays.asList(1));
    assertEquals(ra(1, 1), ra(set));
  }

  @Test
  public void testSize() {
    assertEquals(3, set.size());
    set.remove(1);
    assertEquals(2, set.size());
    set.add(1);
    assertEquals(3, set.size());
  }

  @Test
  public void testToArray() {
    assertTrue(Arrays.equals(
        new Integer[] { Integer.valueOf(1), Integer.valueOf(2),
            Integer.valueOf(3) }, set.toArray()));
    assertFalse(Arrays.equals(new Integer[] { Integer.valueOf(1) },
        set.toArray()));
    assertTrue(Arrays.equals(
        new Integer[] { Integer.valueOf(1), Integer.valueOf(2),
            Integer.valueOf(3) }, set.toArray(new Integer[3])));
    assertFalse(Arrays.equals(new Integer[] { Integer.valueOf(1) },
        set.toArray(new Integer[3])));
  }

  @Test
  public void testEquals() {
    ListSet<Integer> ls = new ListSet<Integer>(Arrays.asList(1, 2, 3));
    assertTrue(set.equals(ls));
    ls = new ListSet<Integer>(Arrays.asList(1, 2));
    assertFalse(set.equals(ls));
    ls = new ListSet<Integer>(Arrays.asList(1, 2, 3));
    assertTrue(set.equals(ls));
    assertFalse(set.equals(null));
    Set<Integer> hs = new HashSet<Integer>(Arrays.asList(1, 2, 3));
    assertTrue(hs.equals(set));
    assertTrue(set.equals(hs));
    ListSet<Integer> ls1 = new ListSet<Integer>(Arrays.asList(1, 2, 3, 3));
    ListSet<Integer> ls2 = new ListSet<Integer>(Arrays.asList(3, 2, 1, 3));
    assertEquals(ls1, ls2);
    assertNotEquals(ls1, hs);
    assertNotEquals(ls2, hs);
  }

  @Test
  public void testHashCode() {
    ListSet<Integer> ls1 =
        new ListSet<Integer>(new ArrayList<Integer>(Arrays.asList(1, 2, 3)));
    ListSet<Integer> ls2 =
        new ListSet<Integer>(new ArrayList<Integer>(Arrays.asList(3, 2, 1)));
    Set<Integer> hs = new HashSet<Integer>(Arrays.asList(1, 2, 3));
    assertEquals(ls1.hashCode(), ls2.hashCode());
    assertEquals(ls2.hashCode(), hs.hashCode());
    ls1.add(3);
    ls2.add(3);
    hs.add(3);
    assertEquals(ls1.hashCode(), ls2.hashCode());
    assertNotEquals(ls2.hashCode(), hs.hashCode());
  }

  @Test
  public void testToString() {
    assertEquals("[1, 2, 3]", set.toString());
  }

}
