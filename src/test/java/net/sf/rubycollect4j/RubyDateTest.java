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

import static net.sf.rubycollect4j.RubyCollections.range;
import static net.sf.rubycollect4j.RubyCollections.rh;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.sf.rubycollect4j.RubyDate.DateField;

public class RubyDateTest {

  RubyDate rd;
  Calendar c;

  @BeforeEach
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

  @Test
  public void testConstructorException() {
    assertThrows(NullPointerException.class, () -> {
      new RubyDate(null);
    });
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
    assertEquals(c.getTime(), rd.change(
        rh(DateField.YEAR, 1990, DateField.MONTH, 6, DateField.DAY, 6)));
  }

  @Test
  public void testYear() {
    assertEquals(2013, Ruby.Date.of(2013).year());
  }

  @Test
  public void testMonth() {
    assertEquals(7, Ruby.Date.of(2013, 7).month());
  }

  @Test
  public void testDay() {
    assertEquals(4, Ruby.Date.of(2013, 7, 4).day());
  }

  @Test
  public void testWday() {
    assertEquals(4, Ruby.Date.of(2013, 7, 4).wday());
  }

  @Test
  public void testMday() {
    assertEquals(4, Ruby.Date.of(2013, 7, 4).mday());
  }

  @Test
  public void testYday() {
    assertEquals(185, Ruby.Date.of(2013, 7, 4).yday());
  }

  @Test
  public void testWeek() {
    assertEquals(27, Ruby.Date.of(2013, 7, 4).week());
  }

  @Test
  public void testHour() {
    assertEquals(8, Ruby.Date.of(2013, 7, 4, 8).hour());
  }

  @Test
  public void testMinute() {
    assertEquals(6, Ruby.Date.of(2013, 7, 4, 8, 6).minute());
  }

  @Test
  public void testSecond() {
    assertEquals(5, Ruby.Date.of(2013, 7, 4, 8, 6, 5).second());
  }

  @Test
  public void testMillisecond() {
    assertEquals(999, Ruby.Date.of(2013, 7, 4, 8, 6, 5, 999).millisecond());
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
  public void testAllWeek() {
    assertEquals(range(rd.beginningOfWeek(), rd.endOfWeek()), rd.allWeek());
  }

  @Test
  public void testAllMonth() {
    assertEquals(range(rd.beginningOfMonth(), rd.endOfMonth()), rd.allMonth());
  }

  @Test
  public void testAllQuarter() {
    assertEquals(range(rd.beginningOfQuarter(), rd.endOfQuarter()),
        rd.allQuarter());
  }

  @Test
  public void testAllYear() {
    assertEquals(range(rd.beginningOfYear(), rd.endOfYear()), rd.allYear());
  }

  @Test
  public void testUpto() {
    rd = Ruby.Date.today();
    assertEquals(range(rd, rd.add(7).days()).toA(),
        rd.upto(Ruby.Date.today().nextWeek()).toA());
  }

  @Test
  public void testUptoWithBlock() {
    rd = Ruby.Date.today();
    RubyArray<Date> dates = Ruby.Array.create();
    rd.upto(Ruby.Date.today().nextWeek(), d -> dates.add(d));
    assertEquals(range(rd, rd.add(7).days()).toA(), dates);
  }

  @Test
  public void testDownto() {
    rd = Ruby.Date.today();
    assertEquals(range(rd.minus(7).days(), rd).toA().reverse(),
        rd.downto(Ruby.Date.today().prevWeek()).toA());
  }

  @Test
  public void testDowntoWithBlock() {
    rd = Ruby.Date.today();
    RubyArray<Date> dates = Ruby.Array.create();
    rd.downto(Ruby.Date.today().prevWeek(), d -> dates.add(d));
    assertEquals(range(rd.prevWeek(), rd).toA().reverse(), dates);
  }

  @Test
  public void testStep() {
    rd = Ruby.Date.today();
    assertEquals(range(rd, rd.add(7).days()).toA(),
        rd.step(Ruby.Date.today().nextWeek()).toA());
  }

  @Test
  public void testStepWithBlock() {
    rd = Ruby.Date.today();
    RubyArray<Date> dates = Ruby.Array.create();
    rd.step(Ruby.Date.today().nextWeek(), d -> dates.add(d));
    assertEquals(range(rd, rd.add(7).days()).toA(), dates);
  }

  @Test
  public void testStepWithNum() {
    rd = Ruby.Date.today();
    assertEquals(range(rd, rd.add(7).days()).step(2).toA(),
        rd.step(Ruby.Date.today().nextWeek(), 2).toA());
    rd = Ruby.Date.today();
    assertEquals(range(rd.minus(28).days(), rd).step(2).toA().reverse(), rd
        .step(Ruby.Date.today().prevWeek().prevWeek().prevWeek().prevWeek(), -2)
        .toA());
  }

  @Test
  public void testStepWithNumAndBlock() {
    rd = Ruby.Date.today();
    RubyArray<Date> dates = Ruby.Array.create();
    rd.step(Ruby.Date.today().nextWeek(), 2, d -> dates.add(d));
    assertEquals(range(rd, rd.add(7).days()).step(2).toA(), dates);
  }

  @Test
  public void testNextDay() {
    assertEquals(rd.add(1).days(), rd.nextDay());
  }

  @Test
  public void testNextWeek() {
    assertEquals(rd.add(1).weeks(), rd.nextWeek());
  }

  @Test
  public void testNextMonth() {
    assertEquals(rd.add(1).months(), rd.nextMonth());
  }

  @Test
  public void testNextQuarter() {
    assertEquals(rd.add(3).months(), rd.nextQuarter());
  }

  @Test
  public void testNextYear() {
    assertEquals(rd.add(1).years(), rd.nextYear());
  }

  @Test
  public void testPrevDay() {
    assertEquals(rd.minus(1).days(), rd.prevDay());
  }

  @Test
  public void testPrevWeek() {
    assertEquals(rd.minus(1).weeks(), rd.prevWeek());
  }

  @Test
  public void testPrevMonth() {
    assertEquals(rd.minus(1).months(), rd.prevMonth());
  }

  @Test
  public void testPrevQuarter() {
    assertEquals(rd.minus(3).months(), rd.prevQuarter());
  }

  @Test
  public void testPrevYear() {
    assertEquals(rd.minus(1).years(), rd.prevYear());
  }

  @Test
  public void testDayʔCheck() {
    rd.allYear().each(date -> {
      switch (Ruby.Date.of(date).wday()) {
        case 0:
          assertTrue(Ruby.Date.of(date).sundayʔ());
          assertFalse(Ruby.Date.of(date).onWeekdayʔ());
          assertTrue(Ruby.Date.of(date).onWeekendʔ());
          break;
        case 1:
          assertTrue(Ruby.Date.of(date).mondayʔ());
          assertTrue(Ruby.Date.of(date).onWeekdayʔ());
          assertFalse(Ruby.Date.of(date).onWeekendʔ());
          break;
        case 2:
          assertTrue(Ruby.Date.of(date).tuesdayʔ());
          assertTrue(Ruby.Date.of(date).onWeekdayʔ());
          assertFalse(Ruby.Date.of(date).onWeekendʔ());
          break;
        case 3:
          assertTrue(Ruby.Date.of(date).wednesdayʔ());
          assertTrue(Ruby.Date.of(date).onWeekdayʔ());
          assertFalse(Ruby.Date.of(date).onWeekendʔ());
          break;
        case 4:
          assertTrue(Ruby.Date.of(date).thursdayʔ());
          assertTrue(Ruby.Date.of(date).onWeekdayʔ());
          assertFalse(Ruby.Date.of(date).onWeekendʔ());
          break;
        case 5:
          assertTrue(Ruby.Date.of(date).fridayʔ());
          assertTrue(Ruby.Date.of(date).onWeekdayʔ());
          assertFalse(Ruby.Date.of(date).onWeekendʔ());
          break;
        case 6:
          assertTrue(Ruby.Date.of(date).saturdayʔ());
          assertFalse(Ruby.Date.of(date).onWeekdayʔ());
          assertTrue(Ruby.Date.of(date).onWeekendʔ());
          break;
      }
    });
  }

  @Test
  public void testCurrent() {
    assertTrue(
        Math.abs(new Date().getTime() - RubyDate.current().getTime()) <= 10L);
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
    assertEquals(0, rd.beginningOfWeek().wday());
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
    assertEquals(6, rd.endOfWeek().wday());
  }

  @Test
  public void testBeginningOfMonth() {
    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.clear(Calendar.MINUTE);
    c.clear(Calendar.SECOND);
    c.clear(Calendar.MILLISECOND);
    assertEquals(c.getTime(), rd.beginningOfMonth());
    assertEquals(1, rd.beginningOfMonth().mday());
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
    assertEquals(Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH),
        rd.endOfMonth().mday());
  }

  @Test
  public void testBeginningOfQuarter() {
    assertEquals(Ruby.Date.of(2014, 1, 1),
        Ruby.Date.of(2014, 2, 2).beginningOfQuarter());
    assertEquals(Ruby.Date.of(2014, 4, 1),
        Ruby.Date.of(2014, 5, 2).beginningOfQuarter());
    assertEquals(Ruby.Date.of(2014, 7, 1),
        Ruby.Date.of(2014, 8, 2).beginningOfQuarter());
    assertEquals(Ruby.Date.of(2014, 10, 1),
        Ruby.Date.of(2014, 11, 2).beginningOfQuarter());
  }

  @Test
  public void testEndOfQuarter() {
    assertEquals(Ruby.Date.of(2014, 3, 31).endOfDay(),
        Ruby.Date.of(2014, 2, 2).endOfQuarter());
    assertEquals(Ruby.Date.of(2014, 6, 30).endOfDay(),
        Ruby.Date.of(2014, 5, 2).endOfQuarter());
    assertEquals(Ruby.Date.of(2014, 9, 30).endOfDay(),
        Ruby.Date.of(2014, 8, 2).endOfQuarter());
    assertEquals(Ruby.Date.of(2014, 12, 31).endOfDay(),
        Ruby.Date.of(2014, 11, 2).endOfQuarter());
  }

  @Test
  public void testBeginningOfYear() {
    assertEquals(Ruby.Date.of(2014, 1, 1),
        Ruby.Date.of(2014, 7, 7).beginningOfYear());
  }

  @Test
  public void testEndOfYear() {
    assertEquals(Ruby.Date.of(2014, 12, 31).endOfDay(),
        Ruby.Date.of(2014, 7, 7).endOfYear());
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

  @Test
  public void testToDate() {
    assertEquals(new Date(rd.getTime()), rd.toDate());
  }

}
