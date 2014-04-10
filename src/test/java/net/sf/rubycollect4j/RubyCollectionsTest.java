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

import static net.sf.rubycollect4j.RubyCollections.Hash;
import static net.sf.rubycollect4j.RubyCollections.date;
import static net.sf.rubycollect4j.RubyCollections.hp;
import static net.sf.rubycollect4j.RubyCollections.isBlank;
import static net.sf.rubycollect4j.RubyCollections.isNotBlank;
import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.newRubyEnumerator;
import static net.sf.rubycollect4j.RubyCollections.newRubyHash;
import static net.sf.rubycollect4j.RubyCollections.newRubyLazyEnumerator;
import static net.sf.rubycollect4j.RubyCollections.newRubyRange;
import static net.sf.rubycollect4j.RubyCollections.newRubyString;
import static net.sf.rubycollect4j.RubyCollections.qr;
import static net.sf.rubycollect4j.RubyCollections.qw;
import static net.sf.rubycollect4j.RubyCollections.qx;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.range;
import static net.sf.rubycollect4j.RubyCollections.rh;
import static net.sf.rubycollect4j.RubyCollections.rs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import net.sf.rubycollect4j.util.ComparableEntry;

import org.junit.Test;

public class RubyCollectionsTest {

  @Test
  public void testPrivateConstructor() throws Exception {
    Constructor<RubyCollections> c =
        RubyCollections.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c.getModifiers()));
    c.setAccessible(true);
    c.newInstance();
  }

  @SuppressWarnings("deprecation")
  @Test
  public void testNewRubyArray() {
    RubyArray<Integer> ra;
    ra = newRubyArray(Arrays.asList(1, 2));
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
    ints = Arrays.asList(1, 2, 3);
    ra = newRubyArray(ints, true);
    ints.set(0, 4);
    assertEquals(ra(1, 2, 3), ra);
    ints = Arrays.asList(1, 2, 3);
    ra = newRubyArray(ints, false);
    ints.set(0, 4);
    assertEquals(ra(4, 2, 3), ra);
  }

  @SuppressWarnings("deprecation")
  @Test
  public void testNewRubyHash() {
    RubyHash<Integer, Integer> rh;
    rh = newRubyHash();
    assertTrue(rh instanceof RubyHash);
    Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    rh = newRubyHash(map);
    assertTrue(rh instanceof RubyHash);
    LinkedHashMap<Integer, Integer> lhm = new LinkedHashMap<Integer, Integer>();
    rh = newRubyHash(lhm, true);
    assertTrue(rh instanceof RubyHash);
    rh = newRubyHash(lhm, false);
    assertTrue(rh instanceof RubyHash);
  }

  @Test
  public void testNewRubyLazyEnumerator() {
    RubyLazyEnumerator<Integer> lre;
    lre = newRubyLazyEnumerator(Arrays.asList(1, 2, 3));
    assertTrue(lre instanceof RubyLazyEnumerator);
  }

  @Test
  public void testNewRubyEnumerator() {
    RubyEnumerator<Integer> re;
    re = newRubyEnumerator(Arrays.asList(1, 2, 3));
    assertTrue(re instanceof RubyEnumerator);
    re = newRubyEnumerator(Arrays.asList(0, 1).iterator());
    assertTrue(re instanceof RubyEnumerator);
  }

  @Test
  public void testNewRubyRange() {
    assertTrue(newRubyRange("a", "z") instanceof RubyRange);
    assertTrue(newRubyRange('a', 'z') instanceof RubyRange);
    assertTrue(newRubyRange(1, 9) instanceof RubyRange);
    assertTrue(newRubyRange(1L, 9L) instanceof RubyRange);
    assertTrue(newRubyRange(1.0, 9.0) instanceof RubyRange);
    assertTrue(newRubyRange(RubyDate.today(), RubyDate.today().add(9).days()) instanceof RubyRange);
  }

  @Test
  public void testNewRubyString() {
    assertTrue(newRubyString() instanceof RubyString);
    assertTrue(newRubyString("str") instanceof RubyString);
  }

  @Test
  public void testRs() {
    assertTrue(rs() instanceof RubyString);
    assertTrue(rs("str") instanceof RubyString);
  }

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
    if (System.getProperty("os.name").startsWith("Windows")) {
      assertEquals("Hello world!" + System.getProperty("line.separator"),
          qx("cmd", "/C", "echo Hello world!"));
    } else {
      assertEquals("Hello world!" + System.getProperty("line.separator"),
          qx("echo", "Hello world!"));
      assertEquals("Hello world!" + System.getProperty("line.separator"),
          qx(new String[] { "sh", "-c", "echo Hello world! 1>&2" }));
    }
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
    assertEquals(new ComparableEntry<Integer, Integer>(1, 1), hp(1, 1));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testHash() {
    assertEquals(rh(1, 2, 3, 4, 5, 6), Hash(ra(hp(1, 2), hp(3, 4), hp(5, 6))));
    assertEquals(rh(1, 2, 3, 4, 5, 6), Hash(ra(ra(1, 2), ra(3, 4), ra(5, 6))));
  }

  @SuppressWarnings("unchecked")
  @Test(expected = IllegalArgumentException.class)
  public void testHashException1() {
    Hash(ra(ra(1, 2), ra(3, 4), ra(5, 6, 7)));
  }

  @SuppressWarnings("unchecked")
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
    assertTrue(range(RubyDate.yesterday(), RubyDate.tomorrow()) instanceof RubyRange);
  }

  @Test
  public void testDate() {
    assertTrue(date() instanceof RubyDate);
    assertEquals(date(new Date()).year(), date().year());
    assertEquals(date(new Date()).month(), date().month());
    assertEquals(date(new Date()).day(), date().day());
    assertEquals(date(new Date()).hour(), date().hour());
    assertEquals(date(new Date()).minute(), date().minute());
  }

  @Test
  public void testDateWithInit() {
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

  @Test
  public void testIsBlank() {
    assertTrue(isBlank(""));
    assertTrue(isBlank("   "));
    assertTrue(isBlank((String) null));
    assertFalse(isBlank("?"));
    assertTrue(isBlank(ra()));
    assertTrue(isBlank((Iterable<?>) null));
    assertFalse(isBlank(ra(1, 2, 3)));
    assertTrue(isBlank(new HashMap<Integer, String>()));
    assertTrue(isBlank((Map<?, ?>) null));
    assertFalse(isBlank(new HashMap<Integer, String>() {

      private static final long serialVersionUID = 1L;

      {
        put(1, "a");
        put(2, "b");
      }

    }));
    assertTrue(isBlank(false));
    assertTrue(isBlank((Boolean) null));
    assertFalse(isBlank(true));
    assertTrue(isBlank((Integer) null));
    assertFalse(isBlank(1));
    assertTrue(isBlank(rh()));
    assertTrue(isBlank((RubyHash<?, ?>) null));
    assertFalse(isBlank(rh(1, "a", 2, "b")));
  }

  @Test
  public void testIsNotBlank() {
    assertFalse(isNotBlank(""));
    assertFalse(isNotBlank("   "));
    assertFalse(isNotBlank((String) null));
    assertTrue(isNotBlank("?"));
    assertFalse(isNotBlank(ra()));
    assertFalse(isNotBlank((Iterable<?>) null));
    assertTrue(isNotBlank(ra(1, 2, 3)));
    assertFalse(isNotBlank(new HashMap<Integer, String>()));
    assertFalse(isNotBlank((Map<?, ?>) null));
    assertTrue(isNotBlank(new HashMap<Integer, String>() {

      private static final long serialVersionUID = 1L;

      {
        put(1, "a");
        put(2, "b");
      }

    }));
    assertFalse(isNotBlank(false));
    assertFalse(isNotBlank((Boolean) null));
    assertTrue(isNotBlank(true));
    assertFalse(isNotBlank((Integer) null));
    assertTrue(isNotBlank(1));
    assertFalse(isNotBlank(rh()));
    assertFalse(isNotBlank((RubyHash<?, ?>) null));
    assertTrue(isNotBlank(rh(1, "a", 2, "b")));
  }

}
