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

import static net.sf.rubycollect4j.RubyCollections.date;
import static net.sf.rubycollect4j.RubyCollections.rh;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import net.sf.rubycollect4j.RubyDate.DateField;

import org.junit.Before;
import org.junit.Test;

public class RubyDateTest {

  RubyDate rd;
  Calendar c;

  @Before
  public void setUp() throws Exception {
    rd = new RubyDate();
    c = Calendar.getInstance();
    c.setTime(rd);
  }

  @Test
  public void testConstructor() {
    assertTrue(rd instanceof RubyDate);
    assertTrue(rd instanceof Date);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException() {
    new RubyDate(null);
  }

  @Test
  public void testConstructorWithDate() {
    Date date = new Date();
    rd = new RubyDate(date);
    assertEquals(date, rd);
  }

  @Test
  public void testChange() {
    c.set(Calendar.YEAR, 1990);
    c.set(Calendar.MONTH, 5);
    c.set(Calendar.DAY_OF_MONTH, 6);
    assertEquals(c.getTime(), rd.change(rh(DateField.YEAR, 1990,
        DateField.MONTH, 6, DateField.DAY, 6)));
  }

  @Test
  public void testYear() {
    assertEquals(2013, date(2013).year());
  }

  @Test
  public void testMonth() {
    assertEquals(7, date(2013, 7).month());
  }

  @Test
  public void testDay() {
    assertEquals(4, date(2013, 7, 4).day());
  }

  @Test
  public void testDayOfWeek() {
    assertEquals(4, date(2013, 7, 4).dayOfWeek());
  }

  @Test
  public void testDayOfMonth() {
    assertEquals(4, date(2013, 7, 4).dayOfMonth());
  }

  @Test
  public void testDayOfYear() {
    assertEquals(185, date(2013, 7, 4).dayOfYear());
  }

  @Test
  public void testWeek() {
    assertEquals(27, date(2013, 7, 4).week());
  }

  @Test
  public void testWeekOfMonth() {
    assertEquals(1, date(2013, 7, 4).weekOfMonth());
  }

  @Test
  public void testHour() {
    assertEquals(8, date(2013, 7, 4, 8).hour());
  }

  @Test
  public void testMinute() {
    assertEquals(6, date(2013, 7, 4, 8, 6).minute());
  }

  @Test
  public void testSecond() {
    assertEquals(5, date(2013, 7, 4, 8, 6, 5).second());
  }

  @Test
  public void testMillisecond() {
    assertEquals(999, date(2013, 7, 4, 8, 6, 5, 999).millisecond());
  }

  @Test
  public void testShiftYear() {
    c.add(Calendar.YEAR, 1);
    assertEquals(c.getTime(), rd.add(1).years());
    c.add(Calendar.YEAR, -2);
    assertEquals(c.getTime(), rd.minus(1).years());
  }

  @Test
  public void testShiftMonth() {
    c.add(Calendar.MONTH, 1);
    assertEquals(c.getTime(), rd.add(1).months());
    c.add(Calendar.MONTH, -2);
    assertEquals(c.getTime(), rd.minus(1).months());
  }

  @Test
  public void testShiftWeek() {
    c.add(Calendar.DAY_OF_MONTH, 1 * 7);
    assertEquals(c.getTime(), rd.add(1).weeks());
    c.add(Calendar.DAY_OF_MONTH, -2 * 7);
    assertEquals(c.getTime(), rd.minus(1).weeks());
  }

  @Test
  public void testShiftDay() {
    c.add(Calendar.DAY_OF_MONTH, 1);
    assertEquals(c.getTime(), rd.add(1).days());
    c.add(Calendar.DAY_OF_MONTH, -2);
    assertEquals(c.getTime(), rd.minus(1).days());
  }

  @Test
  public void testShiftHour() {
    c.add(Calendar.HOUR_OF_DAY, 1);
    assertEquals(c.getTime(), rd.add(1).hours());
    c.add(Calendar.HOUR_OF_DAY, -2);
    assertEquals(c.getTime(), rd.minus(1).hours());
  }

  @Test
  public void testShiftMinute() {
    c.add(Calendar.MINUTE, 1);
    assertEquals(c.getTime(), rd.add(1).minutes());
    c.add(Calendar.MINUTE, -2);
    assertEquals(c.getTime(), rd.minus(1).minutes());
  }

  @Test
  public void testShiftSecond() {
    c.add(Calendar.SECOND, 1);
    assertEquals(c.getTime(), rd.add(1).seconds());
    c.add(Calendar.SECOND, -2);
    assertEquals(c.getTime(), rd.minus(1).seconds());
  }

  @Test
  public void testShiftMillisecond() {
    c.add(Calendar.MILLISECOND, 1);
    assertEquals(c.getTime(), rd.add(1).milliseconds());
    c.add(Calendar.MILLISECOND, -2);
    assertEquals(c.getTime(), rd.minus(1).milliseconds());
  }

  @Test
  public void testCurrent() {
    assertTrue(Math.abs(date().getTime() - RubyDate.current().getTime()) <= 1L);
  }

  @Test
  public void testToday() {
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.clear(Calendar.MINUTE);
    c.clear(Calendar.SECOND);
    c.clear(Calendar.MILLISECOND);
    assertEquals(c.getTime(), RubyDate.today());
  }

  @Test
  public void testTomorrow() {
    c.add(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.clear(Calendar.MINUTE);
    c.clear(Calendar.SECOND);
    c.clear(Calendar.MILLISECOND);
    assertEquals(c.getTime(), RubyDate.tomorrow());
  }

  @Test
  public void testYesterday() {
    c.add(Calendar.DAY_OF_MONTH, -1);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.clear(Calendar.MINUTE);
    c.clear(Calendar.SECOND);
    c.clear(Calendar.MILLISECOND);
    assertEquals(c.getTime(), RubyDate.yesterday());
  }

  @Test
  public void testBeginningOfDay() {
    assertEquals(RubyDate.today(), rd.beginningOfDay());
  }

  @Test
  public void testEndOfDay() {
    assertEquals(RubyDate.tomorrow().minus(1).milliseconds(), rd.endOfDay());
  }

  @Test
  public void testBeginningOfWeek() {
    c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.clear(Calendar.MINUTE);
    c.clear(Calendar.SECOND);
    c.clear(Calendar.MILLISECOND);
    assertEquals(c.getTime(), rd.beginningOfWeek());
    assertEquals(0, rd.beginningOfWeek().dayOfWeek());
  }

  @Test
  public void testEndOfWeek() {
    c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
    c.add(Calendar.DAY_OF_YEAR, 6);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    c.set(Calendar.MILLISECOND, 999);
    assertEquals(c.getTime(), rd.endOfWeek());
    assertEquals(6, rd.endOfWeek().dayOfWeek());
  }

  @Test
  public void testBeginningOfMonth() {
    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.clear(Calendar.MINUTE);
    c.clear(Calendar.SECOND);
    c.clear(Calendar.MILLISECOND);
    assertEquals(c.getTime(), rd.beginningOfMonth());
    assertEquals(1, rd.beginningOfMonth().dayOfMonth());
  }

  @Test
  public void testEndOfMonth() {
    c.set(Calendar.DAY_OF_MONTH,
        Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    c.set(Calendar.MILLISECOND, 999);
    assertEquals(c.getTime(), rd.endOfMonth());
    assertEquals(
        Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH), rd
            .endOfMonth().dayOfMonth());
  }

  @Test
  public void testBeginningOfQuarter() {
    assertEquals(date(2014, 1, 1), date(2014, 2, 2).beginningOfQuarter());
    assertEquals(date(2014, 4, 1), date(2014, 5, 2).beginningOfQuarter());
    assertEquals(date(2014, 7, 1), date(2014, 8, 2).beginningOfQuarter());
    assertEquals(date(2014, 10, 1), date(2014, 11, 2).beginningOfQuarter());
  }

  @Test
  public void testEndOfQuarter() {
    assertEquals(date(2014, 3, 31).endOfDay(), date(2014, 2, 2).endOfQuarter());
    assertEquals(date(2014, 6, 30).endOfDay(), date(2014, 5, 2).endOfQuarter());
    assertEquals(date(2014, 9, 30).endOfDay(), date(2014, 8, 2).endOfQuarter());
    assertEquals(date(2014, 12, 31).endOfDay(), date(2014, 11, 2)
        .endOfQuarter());
  }

  @Test
  public void testBeginningOfYear() {
    assertEquals(date(2014, 1, 1), date(2014, 7, 7).beginningOfYear());
  }

  @Test
  public void testEndOfYear() {
    assertEquals(date(2014, 12, 31).endOfDay(), date(2014, 7, 7).endOfYear());
  }

  @Test
  public void testFutureʔ() {
    assertTrue(RubyDate.current().add(1).seconds().futureʔ());
    assertFalse(RubyDate.current().minus(1).seconds().futureʔ());
  }

  @Test
  public void testPastʔ() {
    assertFalse(RubyDate.current().add(1).seconds().pastʔ());
    assertTrue(RubyDate.current().minus(1).seconds().pastʔ());
  }

  @Test
  public void testTodayʔ() {
    assertTrue(RubyDate.current().todayʔ());
    assertFalse(RubyDate.current().add(1).days().todayʔ());
    assertFalse(RubyDate.current().minus(1).days().todayʔ());
  }

}
