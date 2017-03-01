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
package net.sf.rubycollect4j;

import static net.sf.rubycollect4j.RubyCollections.Hash;
import static net.sf.rubycollect4j.RubyCollections.hp;
import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.newRubyEnumerator;
import static net.sf.rubycollect4j.RubyCollections.newRubyHash;
import static net.sf.rubycollect4j.RubyCollections.newRubyLazyEnumerator;
import static net.sf.rubycollect4j.RubyCollections.newRubySet;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.range;
import static net.sf.rubycollect4j.RubyCollections.rh;
import static net.sf.rubycollect4j.RubyCollections.rs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import net.sf.rubycollect4j.util.ComparableEntry;

public class RubyCollectionsTest {

  @Test
  public void testPrivateConstructor() throws Exception {
    Constructor<RubyCollections> c =
        RubyCollections.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c.getModifiers()));
    c.setAccessible(true);
    c.newInstance();
  }

  @Test
  public void testNewRubyArray() {
    RubyArray<Integer> ra = newRubyArray(Arrays.asList(1, 2));
    assertTrue(ra instanceof RubyArray);
    ra = newRubyArray(new Integer[] { 1 });
    assertTrue(ra instanceof RubyArray);
    ra = newRubyArray(1, 2, 3);
    assertTrue(ra instanceof RubyArray);
    ra = newRubyArray(ra.iterator());
    assertTrue(ra instanceof RubyArray);
    List<Integer> ints = Arrays.asList(1, 2, 3);
    ra = newRubyArray(ints);
    ints.set(0, 4);
    assertEquals(ra(4, 2, 3), ra);
  }

  @Test
  public void testNewRubyHash() {
    RubyHash<Integer, Integer> rh = newRubyHash();
    assertTrue(rh instanceof RubyHash);
    Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    rh = newRubyHash(map);
    assertTrue(rh instanceof RubyHash);
  }

  @Test
  public void testNewRubySet() {
    assertTrue(newRubySet() instanceof RubySet);
    assertTrue(newRubySet(Arrays.asList(1, 2, 3)) instanceof RubySet);
    assertTrue(newRubySet(1, 2, 3, 3) instanceof RubySet);
  }

  @Test
  public void testNewRubyLazyEnumerator() {
    RubyLazyEnumerator<Integer> lre =
        newRubyLazyEnumerator(Arrays.asList(1, 2, 3));
    assertTrue(lre instanceof RubyLazyEnumerator);
  }

  @Test
  public void testNewRubyEnumerator() {
    RubyEnumerator<Integer> re = newRubyEnumerator(Arrays.asList(1, 2, 3));
    assertTrue(re instanceof RubyEnumerator);
  }

  @Test
  public void testRs() {
    assertTrue(rs() instanceof RubyString);
    assertTrue(rs("str") instanceof RubyString);
  }

  @Test
  public void testRa() {
    assertTrue(ra() instanceof RubyArray);
    assertEquals(Collections.EMPTY_LIST, ra());
  }

  @Test
  public void testRaWithIterable() {
    Set<Integer> set = new LinkedHashSet<Integer>(Arrays.asList(1, 2, 3));
    assertEquals(ra(1, 2, 3), ra(set));
  }

  @Test
  public void testRaWithIterator() {
    Set<Integer> set = new LinkedHashSet<Integer>(Arrays.asList(1, 2, 3));
    assertEquals(ra(1, 2, 3), ra(set.iterator()));
  }

  @Test
  public void testRaWithList() {
    List<Integer> list = Arrays.asList(1, 2, 3);
    assertEquals(ra(1, 2, 3), ra(list));
  }

  @Test
  public void testRaWithRubyArray() {
    RubyArray<Integer> ra = newRubyArray(1, 2, 3);
    assertEquals(ra(ra(1, 2, 3)), ra(ra));
  }

  @Test
  public void testRh() {
    assertTrue(rh() instanceof RubyHash);
    assertEquals(Collections.EMPTY_MAP, rh());
  }

  @Test
  public void testRhWithMap() {
    Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    map.put(1, 2);
    RubyHash<Integer, Integer> rh = rh(map);
    assertEquals(rh(1, 2), rh);
    map.put(1, 3);
    assertEquals(rh(1, 2), rh);
  }

  @Test
  public void testRhWith1To26Pairs() {
    assertEquals(rh(1, 1), rh(1, 1));
    assertEquals(rh(1, 1), rh(1, 1, 1, 1));
    assertEquals(rh(1, 1), rh(1, 1, 1, 1, 1, 1));
    assertEquals(rh(1, 1), rh(1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(rh(1, 1), rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(rh(1, 1), rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(rh(1, 1), rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(rh(1, 1), rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(rh(1, 1),
        rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(rh(1, 1),
        rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(rh(1, 1),
        rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(rh(1, 1), rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1));
    assertEquals(rh(1, 1), rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(rh(1, 1), rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(rh(1, 1), rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(rh(1, 1), rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(rh(1, 1), rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(rh(1, 1), rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(rh(1, 1), rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(rh(1, 1), rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(rh(1, 1),
        rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(rh(1, 1),
        rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(rh(1, 1),
        rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1));
    assertEquals(rh(1, 1),
        rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1));
    assertEquals(rh(1, 1),
        rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1));
    assertEquals(rh(1, 1),
        rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1));
  }

  @Test
  public void testHp() {
    assertTrue(hp(1, 1) instanceof Entry);
    assertEquals(new ComparableEntry<Integer, Integer>(1, 1), hp(1, 1));
  }

  @Test
  public void testHash() {
    assertEquals(rh(1, 2, 3, 4, 5, 6), Hash(ra(ra(1, 2), ra(3, 4), ra(5, 6))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHashException1() {
    Hash(ra(ra(1, 2), ra(3, 4), ra(5, 6, 7)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHashException2() {
    Hash(ra(ra(1, 2), ra(3, 4), new RubyArray<Integer>()));
  }

  @Test
  public void testStringRange() {
    assertTrue(range("A", "Z") instanceof RubyRange);
  }

  @Test
  public void testCharacterRange() {
    assertTrue(range('A', 'Z') instanceof RubyRange);
  }

  @Test
  public void testIntegerRange() {
    assertTrue(range(1, 100) instanceof RubyRange);
  }

  @Test
  public void testLongRange() {
    assertTrue(range(1L, 100L) instanceof RubyRange);
  }

  @Test
  public void testDoubleRange() {
    assertTrue(range(1.0, 100.0) instanceof RubyRange);
    assertEquals(ra(1.48, 1.49, 1.50), range(1.48, 1.5).toA());
    assertEquals(ra(1.50, 1.51, 1.52), range(1.5, 1.52).toA());
  }

  @Test
  public void testDateRange() {
    assertTrue(
        range(RubyDate.yesterday(), RubyDate.tomorrow()) instanceof RubyRange);
  }

}
