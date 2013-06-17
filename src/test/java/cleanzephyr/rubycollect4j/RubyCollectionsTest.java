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
package cleanzephyr.rubycollect4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Test;

import static cleanzephyr.rubycollect4j.RubyArray.newRubyArray;
import static cleanzephyr.rubycollect4j.RubyCollections.Hash;
import static cleanzephyr.rubycollect4j.RubyCollections.hp;
import static cleanzephyr.rubycollect4j.RubyCollections.newPair;
import static cleanzephyr.rubycollect4j.RubyCollections.qr;
import static cleanzephyr.rubycollect4j.RubyCollections.qw;
import static cleanzephyr.rubycollect4j.RubyCollections.qx;
import static cleanzephyr.rubycollect4j.RubyCollections.ra;
import static cleanzephyr.rubycollect4j.RubyCollections.rh;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RubyCollectionsTest {

  @Test
  public void testQr() {
    assertTrue(qr("\\d+") instanceof Pattern);
    assertTrue(qr("\\d+").matcher("asf324ds").find());
  }

  @Test
  public void testQw() {
    assertTrue(qw("a b c") instanceof RubyArray);
    assertEquals(ra("a", "b", "c"), qw("a b c"));
  }

  @Test
  public void testQx() {
    assertEquals("Hello world!\n", qx("echo Hello world!"));
  }

  @Test
  public void testRa() {
    assertTrue(ra() instanceof RubyArray);
    assertEquals(Collections.EMPTY_LIST, ra());
  }

  @Test
  public void testRaWithIterable() {
    Set<Integer> set = newLinkedHashSet(Arrays.asList(1, 2, 3));
    assertEquals(ra(1, 2, 3), ra(set));
  }

  @Test
  public void testRaWithIterator() {
    Set<Integer> set = newLinkedHashSet(Arrays.asList(1, 2, 3));
    assertEquals(ra(1, 2, 3), ra(set.iterator()));
  }

  @Test
  public void testRaWithList() {
    List<Integer> list = newArrayList(1, 2, 3);
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
    Map<Integer, Integer> map = newHashMap();
    map.put(1, 2);
    RubyHash<Integer, Integer> rh = rh(map);
    assertEquals(rh(1, 2), rh);
    map.put(1, 3);
    assertEquals(rh(1, 2), rh);
  }

  @Test
  public void testRhWith1To10Pairs() {
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
  }

  @Test
  public void testHp() {
    assertTrue(hp(1, 1) instanceof Entry);
    assertEquals(newPair(1, 1), hp(1, 1));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testHash() {
    assertEquals(rh(1, 2, 3, 4, 5, 6), Hash(ra(hp(1, 2), hp(3, 4), hp(5, 6))));
  }

}
