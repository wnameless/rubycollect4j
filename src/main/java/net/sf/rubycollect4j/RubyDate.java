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

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import net.sf.rubycollect4j.RubyRange.Interval;
import net.sf.rubycollect4j.succ.DateReverseSuccessor;

/**
 * 
 * {@link RubyDate} simply extends Java Date and adds few useful methods which
 * are inspired by Ruby on Rails.
 * 
 * @author Wei-Ming Wu
 * 
 */
public class RubyDate extends Date {

  private static final long serialVersionUID = 1L;

  /**
   * 
   * {@link DateField} is designed for {@link RubyDate#change(Map)} to use.
   *
   */
  public enum DateField {
    YEAR, MONTH, DAY;
  }

  /**
   * Creates a {@link RubyDate} of current time.
   * 
   * @return {@link RubyDate}
   */
  public static RubyDate current() {
    return new RubyDate();
  }

  /**
   * Creates a {@link RubyDate} of beginning of today.
   * 
   * @return {@link RubyDate}
   */
  public static RubyDate today() {
    return new RubyDate().beginningOfDay();
  }

  /**
   * Creates a {@link RubyDate} of tomorrow.
   * 
   * @return {@link RubyDate}
   */
  public static RubyDate tomorrow() {
    return today().add(1).days();
  }

  /**
   * Creates a {@link RubyDate} of yesterday.
   * 
   * @return {@link RubyDate}
   */
  public static RubyDate yesterday() {
    return today().minus(1).days();
  }

  /**
   * Returns a {@link RubyDate} of now.
   */
  public RubyDate() {}

  /**
   * Creates a {@link RubyDate} of given Date.
   * 
   * @param date
   *          a Date
   * @throws NullPointerException
   *           if date is null
   */
  public RubyDate(Date date) {
    Objects.requireNonNull(date);

    setTime(date.getTime());
  }

  /**
   * Returns a new {@link RubyDate} where one or more of the elements have been
   * changed according to the options parameter.
   * 
   * @param options
   *          used to describe the coming change of this {@link RubyDate}
   * @return new {@link RubyDate}
   */
  public RubyDate change(Map<DateField, Integer> options) {
    Calendar c = Calendar.getInstance();
    c.setTime(this);
    for (DateField field : options.keySet()) {
      switch (field) {
        case YEAR:
          c.set(Calendar.YEAR, options.get(field));
          break;
        case MONTH:
          c.set(Calendar.MONTH, options.get(field) - 1);
          break;
        default: // DAY
          c.set(Calendar.DAY_OF_MONTH, options.get(field));
          break;
      }
    }
    return new RubyDate(c.getTime());
  }

  /**
   * Increases an interval of time to a date by the {@link DateShifter}.
   * 
   * @param interval
   *          of time to be shifted
   * @return {@link DateShifter}
   */
  public DateShifter add(int interval) {
    return new DateShifter(this, interval);
  }

  /**
   * Decreases an interval of time to a date by the {@link DateShifter}.
   * 
   * @param interval
   *          of time to be shifted
   * @return {@link DateShifter}
   */
  public DateShifter minus(int interval) {
    return new DateShifter(this, -interval);
  }

  /**
   * Returns the day(0-6) of week of this {@link RubyDate}.<br>
   * 
   * Sun : 0<br>
   * Mon : 1<br>
   * Tue : 2<br>
   * Wed : 3<br>
   * Thur: 4<br>
   * Fri : 5<br>
   * Sat : 6
   * 
   * @return the day of week
   */
  public int wday() {
    Calendar c = Calendar.getInstance();
    c.setTime(this);
    return c.get(Calendar.DAY_OF_WEEK) - 1;
  }

  /**
   * Returns the day(1-31) of month of this {@link RubyDate}.
   * 
   * @return the day of month
   */
  public int mday() {
    Calendar c = Calendar.getInstance();
    c.setTime(this);
    return c.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Returns the day of year of this {@link RubyDate}.
   * 
   * @return the day of year
   */
  public int yday() {
    Calendar c = Calendar.getInstance();
    c.setTime(this);
    return c.get(Calendar.DAY_OF_YEAR);
  }

  /**
   * Returns the year of this {@link RubyDate}.
   * 
   * @return the year
   */
  public int year() {
    Calendar c = Calendar.getInstance();
    c.setTime(this);
    return c.get(Calendar.YEAR);
  }

  /**
   * Returns the month(1-12) of this {@link RubyDate}.
   * 
   * @return the month
   */
  public int month() {
    Calendar c = Calendar.getInstance();
    c.setTime(this);
    return c.get(Calendar.MONTH) + 1;
  }

  /**
   * Returns the day(1-31) of this {@link RubyDate}.
   * 
   * @return the day
   */
  public int day() {
    Calendar c = Calendar.getInstance();
    c.setTime(this);
    return c.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Returns the week of year of this {@link RubyDate}.
   * 
   * @return the week of year
   */
  public int week() {
    Calendar c = Calendar.getInstance();
    c.setTime(this);
    return c.get(Calendar.WEEK_OF_YEAR);
  }

  /**
   * Returns the hour(0-23) of this {@link RubyDate}.
   * 
   * @return the hour
   */
  public int hour() {
    Calendar c = Calendar.getInstance();
    c.setTime(this);
    return c.get(Calendar.HOUR_OF_DAY);
  }

  /**
   * Returns the minute of this {@link RubyDate}.
   * 
   * @return the minute
   */
  public int minute() {
    Calendar c = Calendar.getInstance();
    c.setTime(this);
    return c.get(Calendar.MINUTE);
  }

  /**
   * Returns the second of this {@link RubyDate}.
   * 
   * @return the second
   */
  public int second() {
    Calendar c = Calendar.getInstance();
    c.setTime(this);
    return c.get(Calendar.SECOND);
  }

  /**
   * Returns the millisecond of this {@link RubyDate}.
   * 
   * @return the millisecond
   */
  public int millisecond() {
    Calendar c = Calendar.getInstance();
    c.setTime(this);
    return c.get(Calendar.MILLISECOND);
  }

  /**
   * Returns a {@link RubyRange} from beginning of week to end of week.
   * 
   * @return {@link RubyRange}
   */
  public RubyRange<Date> allWeek() {
    return Ruby.Range.of(beginningOfWeek(), endOfWeek());
  }

  /**
   * Returns a {@link RubyRange} from beginning of month to end of month.
   * 
   * @return {@link RubyRange}
   */
  public RubyRange<Date> allMonth() {
    return Ruby.Range.of(beginningOfMonth(), endOfMonth());
  }

  /**
   * Returns a {@link RubyRange} from beginning of quarter to end of quarter.
   * 
   * @return {@link RubyRange}
   */
  public RubyRange<Date> allQuarter() {
    return Ruby.Range.of(beginningOfQuarter(), endOfQuarter());
  }

  /**
   * Returns a {@link RubyRange} from beginning of year to end of year.
   * 
   * @return {@link RubyRange}
   */
  public RubyRange<Date> allYear() {
    return Ruby.Range.of(beginningOfYear(), endOfYear());
  }

  /**
   * Creates a {@link RubyEnumerator} from this {@link RubyDate} to the given
   * max {@link Date}.
   * 
   * @param max
   *          a {@link Date}
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<Date> upto(Date max) {
    return step(max, 1).each();
  }

  /**
   * Iterates from this {@link RubyDate} to the given max {@link Date}.
   * 
   * @param max
   *          a {@link Date}
   * @param block
   *          to yield each date
   * @return this {@link RubyDate}
   */
  public RubyDate upto(Date max, Consumer<Date> block) {
    step(max, 1).each(block);
    return this;
  }

  /**
   * Creates a {@link RubyEnumerator} from this {@link RubyDate} to the given
   * min {@link Date}.
   * 
   * @param min
   *          a {@link Date}
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<Date> downto(Date min) {
    return step(min, -1).each();
  }

  /**
   * Iterates from this {@link RubyDate} to the given min {@link Date}.
   * 
   * @param min
   *          a {@link Date}
   * @param block
   *          to yield each date
   * @return this {@link RubyDate}
   */
  public RubyDate downto(Date min, Consumer<Date> block) {
    step(min, -1).each(block);
    return this;
  }

  /**
   * Creates a {@link RubyEnumerator} from this {@link RubyDate} to the given
   * limit {@link Date}.
   * 
   * @param limit
   *          a {@link Date}
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<Date> step(Date limit) {
    return step(limit, 1).each();
  }

  /**
   * Iterates from this {@link RubyDate} to the given limit {@link Date}.
   * 
   * @param limit
   *          a {@link Date}
   * @param block
   *          to yield each date
   * @return this {@link RubyDate}
   */
  public RubyDate step(Date limit, Consumer<Date> block) {
    step(limit, 1).each(block);
    return this;
  }

  /**
   * Creates a {@link RubyEnumerator} from this {@link RubyDate} to the given
   * limit {@link Date} by stepping certain number of dates.
   * 
   * @param limit
   *          a {@link Date}
   * @param step
   *          number of dates to step
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<Date> step(Date limit, int step) {
    if (step > 0) {
      return Ruby.Range.of(this, limit).step(step).each();
    } else {
      return new RubyRange<>(DateReverseSuccessor.getInstance(), this, limit,
          Interval.CLOSED).step(-step).each();
    }
  }

  /**
   * Iterates from this {@link RubyDate} to the given limit {@link Date} by
   * stepping certain number of dates.
   * 
   * @param limit
   *          a {@link Date}
   * @param step
   *          number of dates to step
   * @param block
   *          to yield each date
   * @return this {@link RubyDate}
   */
  public RubyDate step(Date limit, int step, Consumer<Date> block) {
    step(limit, step).each(block);
    return this;
  }

  /**
   * Returns a 1-day advanced {@link RubyDate}.
   * 
   * @return new {@link RubyDate}
   */
  public RubyDate nextDay() {
    return add(1).days();
  }

  /**
   * Returns a 1-week advanced {@link RubyDate}.
   * 
   * @return new {@link RubyDate}
   */
  public RubyDate nextWeek() {
    return add(1).weeks();
  }

  /**
   * Returns a 1-month advanced {@link RubyDate}.
   * 
   * @return new {@link RubyDate}
   */
  public RubyDate nextMonth() {
    return add(1).months();
  }

  /**
   * Returns a 1-quarter advanced {@link RubyDate}.
   * 
   * @return new {@link RubyDate}
   */
  public RubyDate nextQuarter() {
    return add(3).months();
  }

  /**
   * Returns a 1-year advanced {@link RubyDate}.
   * 
   * @return new {@link RubyDate}
   */
  public RubyDate nextYear() {
    return add(1).years();
  }

  /**
   * Returns a 1-day backward {@link RubyDate}.
   * 
   * @return new {@link RubyDate}
   */
  public RubyDate prevDay() {
    return minus(1).days();
  }

  /**
   * Returns a 1-week backward {@link RubyDate}.
   * 
   * @return new {@link RubyDate}
   */
  public RubyDate prevWeek() {
    return this.minus(1).weeks();
  }

  /**
   * Returns a 1-month backward {@link RubyDate}.
   * 
   * @return new {@link RubyDate}
   */
  public RubyDate prevMonth() {
    return this.minus(1).months();
  }

  /**
   * Returns a 1-quarter backward {@link RubyDate}.
   * 
   * @return new {@link RubyDate}
   */
  public RubyDate prevQuarter() {
    return minus(3).months();
  }

  /**
   * Returns a 1-year backward {@link RubyDate}.
   * 
   * @return new {@link RubyDate}
   */
  public RubyDate prevYear() {
    return this.minus(1).years();
  }

  /**
   * Returns a new {@link RubyDate} with time set to the beginning of day based
   * on this {@link RubyDate}.
   * 
   * @return new {@link RubyDate}
   */
  public RubyDate beginningOfDay() {
    Calendar c = Calendar.getInstance();
    c.setTime(this);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.clear(Calendar.MINUTE);
    c.clear(Calendar.SECOND);
    c.clear(Calendar.MILLISECOND);
    return new RubyDate(c.getTime());
  }

  /**
   * Returns a new {@link RubyDate} with time set to the end of day based on
   * this {@link RubyDate}.
   * 
   * @return new {@link RubyDate}
   */
  public RubyDate endOfDay() {
    Calendar c = Calendar.getInstance();
    c.setTime(this);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    c.set(Calendar.MILLISECOND, 999);
    return new RubyDate(c.getTime());
  }

  /**
   * Returns a new {@link RubyDate} with time set to the beginning of week based
   * on this {@link RubyDate}. The beginning of week is Sunday.
   * 
   * @return new {@link RubyDate}
   */
  public RubyDate beginningOfWeek() {
    Calendar c = Calendar.getInstance();
    c.setTime(this);
    c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.clear(Calendar.MINUTE);
    c.clear(Calendar.SECOND);
    c.clear(Calendar.MILLISECOND);
    return new RubyDate(c.getTime());
  }

  /**
   * Returns a new {@link RubyDate} with time set to the end of week based on
   * this {@link RubyDate}. The end of week is Saturday.
   * 
   * @return new {@link RubyDate}
   */
  public RubyDate endOfWeek() {
    return beginningOfWeek().add(6).days().endOfDay();
  }

  /**
   * Returns a new {@link RubyDate} with time set to the beginning of month
   * based on this {@link RubyDate}.
   * 
   * @return new {@link RubyDate}
   */
  public RubyDate beginningOfMonth() {
    return change(Ruby.Hash.of(DateField.DAY, 1)).beginningOfDay();
  }

  /**
   * Returns a new {@link RubyDate} with time set to the end of month based on
   * this {@link RubyDate}.
   * 
   * @return new {@link RubyDate}
   */
  public RubyDate endOfMonth() {
    Calendar c = Calendar.getInstance();
    c.setTime(this);
    c.set(Calendar.DAY_OF_MONTH,
        Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    c.set(Calendar.MILLISECOND, 999);
    return new RubyDate(c.getTime());
  }

  /**
   * Returns a new {@link RubyDate} representing the start of the quarter (1st
   * of january, april, july, october).
   * 
   * @return new {@link RubyDate}
   */
  public RubyDate beginningOfQuarter() {
    switch (month()) {
      case 1:
      case 2:
      case 3:
        return change(Ruby.Hash.of(DateField.MONTH, 1, DateField.DAY, 1))
            .beginningOfDay();
      case 4:
      case 5:
      case 6:
        return change(Ruby.Hash.of(DateField.MONTH, 4, DateField.DAY, 1))
            .beginningOfDay();
      case 7:
      case 8:
      case 9:
        return change(Ruby.Hash.of(DateField.MONTH, 7, DateField.DAY, 1))
            .beginningOfDay();
      default:
        return change(Ruby.Hash.of(DateField.MONTH, 10, DateField.DAY, 1))
            .beginningOfDay();
    }
  }

  /**
   * Returns a new {@link RubyDate} representing the end of the quarter (last
   * day of march, june, september, december).
   * 
   * @return new {@link RubyDate}
   */
  public RubyDate endOfQuarter() {
    switch (month()) {
      case 1:
      case 2:
      case 3:
        return change(Ruby.Hash.of(DateField.MONTH, 3, DateField.DAY, 31))
            .endOfDay();
      case 4:
      case 5:
      case 6:
        return change(Ruby.Hash.of(DateField.MONTH, 6, DateField.DAY, 30))
            .endOfDay();
      case 7:
      case 8:
      case 9:
        return change(Ruby.Hash.of(DateField.MONTH, 9, DateField.DAY, 30))
            .endOfDay();
      default:
        return change(Ruby.Hash.of(DateField.MONTH, 12, DateField.DAY, 31))
            .endOfDay();
    }
  }

  /**
   * Returns a new {@link RubyDate} representing the beginning of the year.
   * 
   * @return new {@link RubyDate}
   */
  public RubyDate beginningOfYear() {
    return change(Ruby.Hash.of(DateField.MONTH, 1, DateField.DAY, 1))
        .beginningOfDay();
  }

  /**
   * Returns a new {@link RubyDate} representing the end of the year.
   * 
   * @return new {@link RubyDate}
   */
  public RubyDate endOfYear() {
    return change(Ruby.Hash.of(DateField.MONTH, 12, DateField.DAY, 31))
        .endOfDay();
  }

  /**
   * Checks if this {@RubyDate} is Monday.
   * 
   * @return true if the date is Monday, false otherwise
   */
  public boolean mondayʔ() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(this);
    return cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
  }

  /**
   * Checks if this {@RubyDate} is Tuesday.
   * 
   * @return true if the date is Tuesday, false otherwise
   */
  public boolean tuesdayʔ() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(this);
    return cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY;
  }

  /**
   * Checks if this {@RubyDate} is Wednesday.
   * 
   * @return true if the date is Wednesday, false otherwise
   */
  public boolean wednesdayʔ() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(this);
    return cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY;
  }

  /**
   * Checks if this {@RubyDate} is Thursday.
   * 
   * @return true if the date is Thursday, false otherwise
   */
  public boolean thursdayʔ() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(this);
    return cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY;
  }

  /**
   * Checks if this {@RubyDate} is Friday.
   * 
   * @return true if the date is Friday, false otherwise
   */
  public boolean fridayʔ() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(this);
    return cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;
  }

  /**
   * Checks if this {@RubyDate} is Saturday.
   * 
   * @return true if the date is Saturday, false otherwise
   */
  public boolean saturdayʔ() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(this);
    return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
  }

  /**
   * Checks if this {@RubyDate} is Sunday.
   * 
   * @return true if the date is Sunday, false otherwise
   */
  public boolean sundayʔ() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(this);
    return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
  }

  /**
   * Checks if this {@RubyDate} is on weekday.
   * 
   * @return true if the date is on weekday, false otherwise
   */
  public boolean onWeekdayʔ() {
    return mondayʔ() || tuesdayʔ() || wednesdayʔ() || thursdayʔ() || fridayʔ();
  }

  /**
   * Checks if this {@RubyDate} is on weekend.
   * 
   * @return true if the date is on weekend, false otherwise
   */
  public boolean onWeekendʔ() {
    return saturdayʔ() || sundayʔ();
  }

  /**
   * Checks if this {@link RubyDate} represents a future time.
   * 
   * @return true if this {@link RubyDate} represents a future time, false
   *         otherwise
   */
  public boolean futureʔ() {
    return new RubyDate().compareTo(this) < 0;
  }

  /**
   * Checks if this {@link RubyDate} represents a past time.
   * 
   * @return true if this {@link RubyDate} represents a past time, false
   *         otherwise
   */
  public boolean pastʔ() {
    return new RubyDate().compareTo(this) > 0;
  }

  /**
   * Checks if this {@link RubyDate} is a time of today.
   * 
   * @return true if this {@link RubyDate} is a time of today, false otherwise
   */
  public boolean todayʔ() {
    return RubyDate.today().equals(beginningOfDay());
  }

  /**
   * Converts this {@link RubyDate} to {@link Date}.
   * 
   * @return {@link Date}
   */
  public Date toDate() {
    return new Date(getTime());
  }

  /**
   * 
   * {@link DateShifter} shifts a Date by an interval of time and creates a new
   * {@link RubyDate} from it.
   * 
   */
  public final class DateShifter {

    private final Date date;
    private final int interval;

    /**
     * Returns a {@link DateShifter}.
     * 
     * @param date
     *          a Date
     * @param interval
     *          of time to be shifted
     */
    public DateShifter(Date date, int interval) {
      this.date = date;
      this.interval = interval;
    }

    /**
     * Shifts the time by milliseconds.
     * 
     * @return {@link RubyDate}
     */
    public RubyDate milliseconds() {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(Calendar.MILLISECOND, interval);
      return new RubyDate(c.getTime());
    }

    /**
     * Shifts the time by seconds.
     * 
     * @return {@link RubyDate}
     */
    public RubyDate seconds() {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(Calendar.SECOND, interval);
      return new RubyDate(c.getTime());
    }

    /**
     * Shifts the time by minutes.
     * 
     * @return {@link RubyDate}
     */
    public RubyDate minutes() {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(Calendar.MINUTE, interval);
      return new RubyDate(c.getTime());
    }

    /**
     * Shifts the time by hours.
     * 
     * @return {@link RubyDate}
     */
    public RubyDate hours() {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(Calendar.HOUR_OF_DAY, interval);
      return new RubyDate(c.getTime());
    }

    /**
     * Shifts the time by weeks.
     * 
     * @return {@link RubyDate}
     */
    public RubyDate weeks() {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(Calendar.DAY_OF_MONTH, interval * 7);
      return new RubyDate(c.getTime());
    }

    /**
     * Shifts the time by days.
     * 
     * @return {@link RubyDate}
     */
    public RubyDate days() {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(Calendar.DAY_OF_MONTH, interval);
      return new RubyDate(c.getTime());
    }

    /**
     * Shifts the time by months.
     * 
     * @return {@link RubyDate}
     */
    public RubyDate months() {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(Calendar.MONTH, interval);
      return new RubyDate(c.getTime());
    }

    /**
     * Shifts the time by years.
     * 
     * @return {@link RubyDate}
     */
    public RubyDate years() {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(Calendar.YEAR, interval);
      return new RubyDate(c.getTime());
    }

  }

}
