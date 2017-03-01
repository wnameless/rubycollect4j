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

import static net.sf.rubycollect4j.RubyKernel.p;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.junit.Test;

public class RubyTest {

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
  }

  @Test
  public void testArray() {
    assertEquals(new RubyArray<>(), Ruby.Array.create());
    assertEquals(new RubyArray<>(Arrays.asList(1, 2, 3)),
        Ruby.Array.of(Arrays.asList(1, 2, 3)));
    assertEquals(new RubyArray<>(Arrays.asList(1, 2, 3)),
        Ruby.Array.of(1, 2, 3));
    assertEquals(new RubyArray<>(Arrays.asList(1, 2, 3)),
        Ruby.Array.copyOf((Iterable<Integer>) Arrays.asList(1, 2, 3)));
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
  public void test() {
    RubyHash<Iterable<String>, Iterator<Character>> rh =
        Ruby.Hash.create(Ruby.Array.of(Ruby.Entry.of(Ruby.Array.of("a", "b"),
            Ruby.Array.of('a', 'b').iterator())));
    p(rh);
  }

}
