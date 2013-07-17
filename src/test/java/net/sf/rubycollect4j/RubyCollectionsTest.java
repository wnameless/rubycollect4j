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
package net.sf.rubycollect4j;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Test;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newLinkedHashSet;
import static net.sf.rubycollect4j.RubyArray.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.Hash;
import static net.sf.rubycollect4j.RubyCollections.date;
import static net.sf.rubycollect4j.RubyCollections.hp;
import static net.sf.rubycollect4j.RubyCollections.newPair;
import static net.sf.rubycollect4j.RubyCollections.qr;
import static net.sf.rubycollect4j.RubyCollections.qw;
import static net.sf.rubycollect4j.RubyCollections.qx;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.rh;
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
    assertEquals("Hello world!\n", qx("echo", "Hello world!"));
  }

  @Test(expected = RuntimeException.class)
  public void testQxException() {
    qx("lls");
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
  public void testRhWith1To20Pairs() {
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
    assertEquals(
        rh(1, 1),
        rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1));
    assertEquals(
        rh(1, 1),
        rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1));
    assertEquals(
        rh(1, 1),
        rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1));
    assertEquals(
        rh(1, 1),
        rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1));
    assertEquals(
        rh(1, 1),
        rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(
        rh(1, 1),
        rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(
        rh(1, 1),
        rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(
        rh(1, 1),
        rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(
        rh(1, 1),
        rh(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
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

  @Test
  public void testDate() {
    Date date = new Date();
    assertTrue(date(date) instanceof RubyDate);
    assertTrue(date.equals(date(date)));
    assertTrue(date(date).equals(date));
  }

  @Test
  public void testDate1() {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, 2013);
    c.set(Calendar.MONTH, 0);
    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    assertEquals(c.getTime(), date(2013));
  }

  @Test
  public void testDate2() {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, 2013);
    c.set(Calendar.MONTH, 6);
    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    assertEquals(c.getTime(), date(2013, 7));
  }

  @Test
  public void testDate3() {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, 2013);
    c.set(Calendar.MONTH, 6);
    c.set(Calendar.DAY_OF_MONTH, 7);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    assertEquals(c.getTime(), date(2013, 7, 7));
  }

  @Test
  public void testDate4() {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, 2013);
    c.set(Calendar.MONTH, 6);
    c.set(Calendar.DAY_OF_MONTH, 7);
    c.set(Calendar.HOUR_OF_DAY, 13);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    assertEquals(c.getTime(), date(2013, 7, 7, 13));
  }

  @Test
  public void testDate5() {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, 2013);
    c.set(Calendar.MONTH, 6);
    c.set(Calendar.DAY_OF_MONTH, 7);
    c.set(Calendar.HOUR_OF_DAY, 13);
    c.set(Calendar.MINUTE, 22);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    assertEquals(c.getTime(), date(2013, 7, 7, 13, 22));
  }

  @Test
  public void testDate6() {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, 2013);
    c.set(Calendar.MONTH, 6);
    c.set(Calendar.DAY_OF_MONTH, 7);
    c.set(Calendar.HOUR_OF_DAY, 11);
    c.set(Calendar.MINUTE, 22);
    c.set(Calendar.SECOND, 33);
    c.set(Calendar.MILLISECOND, 0);
    assertEquals(c.getTime(), date(2013, 7, 7, 11, 22, 33));
  }

  @Test
  public void testDate7() {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, 2013);
    c.set(Calendar.MONTH, 6);
    c.set(Calendar.DAY_OF_MONTH, 7);
    c.set(Calendar.HOUR_OF_DAY, 11);
    c.set(Calendar.MINUTE, 22);
    c.set(Calendar.SECOND, 33);
    c.set(Calendar.MILLISECOND, 999);
    assertEquals(c.getTime(), date(2013, 7, 7, 11, 22, 33, 999));
  }

}
