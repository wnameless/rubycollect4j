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

import java.util.Calendar;
import java.util.Date;

/**
 * 
 * {@link RubyDate} simply extends Java Date and adds few useful methods which
 * are inspired by Ruby on Rails.
 * 
 */
public final class RubyDate extends Date {

  private static final long serialVersionUID = 6634492638579024503L;

  /**
   * Returns a {@link RubyDate} of now.
   */
  public RubyDate() {}

  /**
   * Returns a {@link RubyDate} of given Date.
   * 
   * @param date
   *          a Date
   * @throws NullPointerException
   *           if date is null
   */
  public RubyDate(Date date) {
    if (date == null)
      throw new NullPointerException();

    setTime(date.getTime());
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
  public int dayOfWeek() {
    Calendar c = Calendar.getInstance();
    c.setTime(this);
    return c.get(Calendar.DAY_OF_WEEK) - 1;
  }

  /**
   * Returns the day of year of this {@link RubyDate}.
   * 
   * @return the day of year
   */
  public int dayOfYear() {
    Calendar c = Calendar.getInstance();
    c.setTime(this);
    return c.get(Calendar.DAY_OF_YEAR);
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
   * Returns the week of month of this {@link RubyDate}.
   * 
   * @return the week of month
   */
  public int weekOfMonth() {
    Calendar c = Calendar.getInstance();
    c.setTime(this);
    return c.get(Calendar.WEEK_OF_MONTH);
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
   * Creates a {@link RubyDate} of today.
   * 
   * @return {@link RubyDate}
   */
  public static RubyDate today() {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.clear(Calendar.MINUTE);
    c.clear(Calendar.SECOND);
    c.clear(Calendar.MILLISECOND);
    return new RubyDate(c.getTime());
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
