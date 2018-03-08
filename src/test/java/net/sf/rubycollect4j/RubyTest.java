/*
 *
 * Copyright 2017 Wei-Ming Wu
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

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import org.junit.Test;

import net.sf.rubycollect4j.RubyIO.Mode;
import net.sf.rubycollect4j.util.ComparableEntry;

public class RubyTest {

  static final String BASE_DIR = "src/test/resources/";

  @Test
  public void testPrivateConstructor() throws Exception {
    Constructor<Ruby> c = Ruby.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c.getModifiers()));
    c.setAccessible(true);
    c.newInstance();

    Constructor<Ruby.Array> c1 = Ruby.Array.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c1.getModifiers()));
    c1.setAccessible(true);
    c1.newInstance();

    Constructor<Ruby.Hash> c2 = Ruby.Hash.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c2.getModifiers()));
    c2.setAccessible(true);
    c2.newInstance();

    Constructor<Ruby.Entry> c3 = Ruby.Entry.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c3.getModifiers()));
    c3.setAccessible(true);
    c3.newInstance();

    Constructor<Ruby.Set> c4 = Ruby.Set.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c4.getModifiers()));
    c4.setAccessible(true);
    c4.newInstance();

    Constructor<Ruby.Enumerator> c5 =
        Ruby.Enumerator.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c5.getModifiers()));
    c5.setAccessible(true);
    c5.newInstance();

    Constructor<Ruby.LazyEnumerator> c6 =
        Ruby.LazyEnumerator.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c6.getModifiers()));
    c6.setAccessible(true);
    c6.newInstance();

    Constructor<Ruby.String> c7 = Ruby.String.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c7.getModifiers()));
    c7.setAccessible(true);
    c7.newInstance();

    Constructor<Ruby.Range> c8 = Ruby.Range.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c8.getModifiers()));
    c8.setAccessible(true);
    c8.newInstance();

    Constructor<Ruby.Date> c9 = Ruby.Date.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c9.getModifiers()));
    c9.setAccessible(true);
    c9.newInstance();

    Constructor<Ruby.IO> c10 =
        Ruby.IO.class.getDeclaredConstructor(java.io.File.class, Mode.class);
    assertTrue(Modifier.isPrivate(c10.getModifiers()));
    c10.setAccessible(true);
    c10.newInstance(new File(BASE_DIR + "ruby_io_read_only_mode.txt"), Mode.R);

    Constructor<Ruby.File> c11 =
        Ruby.File.class.getDeclaredConstructor(java.io.File.class, Mode.class);
    assertTrue(Modifier.isPrivate(c11.getModifiers()));
    c11.setAccessible(true);
    c11.newInstance(new File(BASE_DIR + "ruby_io_read_only_mode.txt"), Mode.R);

    Constructor<Ruby.Dir> c12 =
        Ruby.Dir.class.getDeclaredConstructor(java.io.File.class);
    assertTrue(Modifier.isPrivate(c12.getModifiers()));
    c12.setAccessible(true);
    c12.newInstance(new File(BASE_DIR));

    Constructor<Ruby.Literals> c13 =
        Ruby.Literals.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c13.getModifiers()));
    c13.setAccessible(true);
    c13.newInstance();

    Constructor<Ruby.Kernel> c14 = Ruby.Kernel.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c14.getModifiers()));
    c14.setAccessible(true);
    c14.newInstance();

    Constructor<Ruby.Object> c15 = Ruby.Object.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c15.getModifiers()));
    c15.setAccessible(true);
    c15.newInstance();
  }

  @Test
  public void testArray() {
    assertEquals(new RubyArray<>(), Ruby.Array.create());
    assertEquals(new RubyArray<>(Arrays.asList(1, 2, 3)),
        Ruby.Array.of(Arrays.asList(1, 2, 3)));
    assertEquals(new RubyArray<>(Arrays.asList(1, 2, 3)),
        Ruby.Array.of(1, 2, 3));
    assertEquals(
        new RubyArray<RubyArray<Integer>>()
            .push(new RubyArray<>(Arrays.asList(1, 2, 3))),
        Ruby.Array.of(Ruby.Array.of(1, 2, 3)));
    assertEquals(new RubyArray<>(Arrays.asList(1, 2, 3)),
        Ruby.Array.copyOf(new Integer[] { 1, 2, 3 }));
    assertEquals(new RubyArray<>(Arrays.asList(1, 2, 3)),
        Ruby.Array.copyOf(Arrays.asList(1, 2, 3)));
    assertEquals(new RubyArray<>(Arrays.asList(1, 2, 3)),
        Ruby.Array.copyOf((Iterable<Integer>) Arrays.asList(1, 2, 3)));
    assertEquals(new RubyArray<>(Arrays.asList(1, 2, 3)),
        Ruby.Array.copyOf(Arrays.asList(1, 2, 3).iterator()));
  }

  @Test
  public void testHash() {
    assertEquals(new RubyHash<>(), Ruby.Hash.create());
    LinkedHashMap<Integer, Integer> lhm = new LinkedHashMap<>();
    RubyHash<Integer, Integer> rh = Ruby.Hash.of(lhm);
    lhm.put(1, 2);
    assertEquals(lhm, rh);
    rh = Ruby.Hash.copyOf(lhm);
    lhm.clear();
    assertNotEquals(lhm, rh);
    assertEquals(Ruby.Hash.of(1, 2),
        Ruby.Hash.create(Ruby.Array.of(Ruby.Entry.of(1, 2))));
  }

  @Test
  public void testHashWith1To26Pairs() {
    assertEquals(Ruby.Hash.of(1, 1), Ruby.Hash.of(1, 1));
    assertEquals(Ruby.Hash.of(1, 1), Ruby.Hash.of(1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1), Ruby.Hash.of(1, 1, 1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1), Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1),
        Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1),
        Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1),
        Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1),
        Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1),
        Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1), Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1), Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1), Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1), Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1), Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1), Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1), Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1),
        Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1),
        Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1),
        Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1),
        Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1),
        Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1),
        Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1));
    assertEquals(Ruby.Hash.of(1, 1),
        Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1),
        Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1),
        Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1));
    assertEquals(Ruby.Hash.of(1, 1),
        Ruby.Hash.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1));
  }

  @Test
  public void testEntry() {
    assertEquals(new ComparableEntry<Integer, Integer>(1, 2),
        Ruby.Entry.of(1, 2));
  }

  @Test
  public void testSet() {
    assertEquals(new RubySet<>(), Ruby.Set.create());
    LinkedHashSet<Integer> lhs = new LinkedHashSet<>();
    RubySet<Integer> rs = Ruby.Set.of(lhs);
    lhs.add(1);
    lhs.add(2);
    lhs.add(3);
    assertEquals(lhs, rs);
    assertEquals(lhs, Ruby.Set.of(1, 2, 3));
    assertEquals(lhs, Ruby.Set.copyOf(Arrays.asList(1, 2, 3)));
    assertEquals(lhs, Ruby.Set.copyOf(Arrays.asList(1, 2, 3).iterator()));
    assertEquals(lhs, Ruby.Set.copyOf(new Integer[] { 1, 2, 3 }));
  }

  @Test
  public void testEnumerator() {
    List<Integer> ints = Ruby.Array.of(1, 2, 3);
    RubyEnumerator<Integer> re = Ruby.Enumerator.of(ints);
    ints.remove(0);
    assertEquals(ints, re.toA());
    ints = Ruby.Array.of(1, 2, 3);
    re = Ruby.Enumerator.copyOf(ints);
    ints.remove(0);
    assertNotEquals(ints, re.toA());
  }

  @Test
  public void testLazyEnumerator() {
    List<Integer> ints = Ruby.Array.of(1, 2, 3);
    RubyLazyEnumerator<Integer> re = Ruby.LazyEnumerator.of(ints);
    ints.remove(0);
    assertEquals(ints, re.toA());
    ints = Ruby.Array.of(1, 2, 3);
    re = Ruby.LazyEnumerator.copyOf(ints);
    ints.remove(0);
    assertNotEquals(ints, re.toA());
  }

  @Test
  public void testString() {
    assertEquals("", Ruby.String.create().toS());
    assertEquals("abc", Ruby.String.of("abc").toS());
  }

  @Test
  public void testStringRange() {
    assertTrue(Ruby.Range.of("A", "Z") instanceof RubyRange);
  }

  @Test
  public void testCharacterRange() {
    assertTrue(Ruby.Range.of('A', 'Z') instanceof RubyRange);
  }

  @Test
  public void testIntegerRange() {
    assertTrue(Ruby.Range.of(1, 100) instanceof RubyRange);
  }

  @Test
  public void testLongRange() {
    assertTrue(Ruby.Range.of(1L, 100L) instanceof RubyRange);
  }

  @Test
  public void testDoubleRange() {
    assertTrue(Ruby.Range.of(1.0, 100.0) instanceof RubyRange);
    assertEquals(ra(1.48, 1.49, 1.50), Ruby.Range.of(1.48, 1.5).toA());
    assertEquals(ra(1.50, 1.51, 1.52), Ruby.Range.of(1.5, 1.52).toA());
  }

  @Test
  public void testDateRange() {
    assertTrue(Ruby.Range.of(RubyDate.yesterday(),
        RubyDate.tomorrow()) instanceof RubyRange);
  }

  @Test
  public void testLocalDateTimeRange() {
    assertTrue(Ruby.Range.of(LocalDateTime.now(),
        LocalDateTime.now().plusDays(1)) instanceof RubyRange);
  }

  @Test
  public void testTemporalRange() {
    RubyRange<LocalDate> range1 = Ruby.Range.of(LocalDate.now(),
        LocalDate.now().plusDays(10), ChronoUnit.DAYS);
    assertEquals(11, range1.count());
    assertTrue(range1.includeʔ(LocalDate.now().plusDays(5)));
    assertFalse(range1.includeʔ(LocalDate.now().plusDays(11)));

    RubyRange<LocalDateTime> range2 = Ruby.Range.of(LocalDateTime.now(),
        LocalDateTime.now().plusSeconds(10), ChronoUnit.SECONDS);
    assertEquals(11, range2.count());
    assertTrue(range2.includeʔ(LocalDateTime.now().plusSeconds(5)));
    assertFalse(range2.includeʔ(LocalDateTime.now().plusSeconds(11)));
  }

  @Test
  public void testDate() {
    assertTrue(Ruby.Date.current() instanceof RubyDate);
    assertEquals(Ruby.Date.of(new Date()).year(), Ruby.Date.current().year());
    assertEquals(Ruby.Date.of(new Date()).month(), Ruby.Date.current().month());
    assertEquals(Ruby.Date.of(new Date()).day(), Ruby.Date.current().day());
    assertEquals(Ruby.Date.of(new Date()).hour(), Ruby.Date.current().hour());
    assertEquals(Ruby.Date.of(new Date()).minute(),
        Ruby.Date.current().minute());
  }

  @Test
  public void testDateWithInit() {
    Date date = new Date();
    assertTrue(Ruby.Date.of(date) instanceof RubyDate);
    assertTrue(date.equals(Ruby.Date.of(date)));
    assertTrue(Ruby.Date.of(date).equals(date));
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
    assertEquals(c.getTime(), Ruby.Date.of(2013));
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
    assertEquals(c.getTime(), Ruby.Date.of(2013, 7));
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
    assertEquals(c.getTime(), Ruby.Date.of(2013, 7, 7));
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
    assertEquals(c.getTime(), Ruby.Date.of(2013, 7, 7, 13));
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
    assertEquals(c.getTime(), Ruby.Date.of(2013, 7, 7, 13, 22));
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
    assertEquals(c.getTime(), Ruby.Date.of(2013, 7, 7, 11, 22, 33));
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
    assertEquals(c.getTime(), Ruby.Date.of(2013, 7, 7, 11, 22, 33, 999));
  }

  @Test
  public void testIO() throws IOException {
    assertEquals(
        new RubyIO(new File(BASE_DIR + "ruby_io_read_only_mode.txt"), Mode.R)
            .toString(),
        Ruby.IO.of(new File(BASE_DIR + "ruby_io_read_only_mode.txt"))
            .toString());
    assertEquals(
        new RubyIO(new File(BASE_DIR + "ruby_io_read_only_mode.txt"), Mode.R)
            .toString(),
        Ruby.IO.of(new File(BASE_DIR + "ruby_io_read_only_mode.txt"), Mode.R)
            .toString());
  }

  @Test
  public void testFile() throws IOException {
    assertEquals(
        new RubyFile(new File(BASE_DIR + "ruby_io_read_only_mode.txt"), Mode.R)
            .toString(),
        Ruby.File.of(new File(BASE_DIR + "ruby_io_read_only_mode.txt"))
            .toString());
    assertEquals(
        new RubyFile(new File(BASE_DIR + "ruby_io_read_only_mode.txt"), Mode.R)
            .toString(),
        Ruby.File.of(new File(BASE_DIR + "ruby_io_read_only_mode.txt"), Mode.R)
            .toString());
  }

  @Test
  public void testDir() {
    assertEquals(new RubyDir(new File(BASE_DIR)).toString(),
        Ruby.Dir.of(new File(BASE_DIR)).toString());
  }

}
