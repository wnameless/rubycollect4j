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
 * RubyDate isn't exact like the Date class of Ruby. It simply extends Java Date
 * and adds few useful methods which are inspired by Ruby on Rails.
 * 
 */
public final class RubyDate extends Date {

  private static final long serialVersionUID = -8002198927043898170L;

  /**
   * The constructor of the RubyDate.
   */
  public RubyDate() {}

  /**
   * The constructor of the RubyDate.
   * 
   * @param date
   *          a Date
   */
  public RubyDate(Date date) {
    setTime(date.getTime());
  }

  /**
   * Increases an interval of time to a date by the DateShifter.
   * 
   * @param interval
   *          of time to be shifted
   * @return a DateShifter
   */
  public DateShifter add(int interval) {
    return new DateShifter(this, interval);
  }

  /**
   * Decreases an interval of time to a date by the DateShifter.
   * 
   * @param interval
   *          of time to be shifted
   * @return a DateShifter
   */
  public DateShifter minus(int interval) {
    return new DateShifter(this, -interval);
  }

  /**
   * Returns a new RubyDate with time set to the beginning of day based on this
   * RubyDate.
   * 
   * @return a new RubyDate
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
   * Returns a new RubyDate with time set to the end of day based on this
   * RubyDate.
   * 
   * @return a new RubyDate
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
   * Returns a new RubyDate with time set to the beginning of week based on this
   * RubyDate. The beginning of week is Sunday.
   * 
   * @return a new RubyDate
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
   * Creates a RubyDate of today.
   * 
   * @return a RubyDate
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
   * Creates a RubyDate of tomorrow.
   * 
   * @return a RubyDate
   */
  public static RubyDate tomorrow() {
    return today().add(1).days();
  }

  /**
   * Creates a RubyDate of yesterday.
   * 
   * @return a RubyDate
   */
  public static RubyDate yesterday() {
    return today().minus(1).days();
  }

  /**
   * 
   * DateShifter shifts a Date by an interval of time and creates a new RubyDate
   * from it.
   * 
   */
  public final class DateShifter {

    private final Date date;
    private final int interval;

    /**
     * The constructor of the DateShifter.
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
     * @return a RubyDate
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
     * @return a RubyDate
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
     * @return a RubyDate
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
     * @return a RubyDate
     */
    public RubyDate hours() {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(Calendar.HOUR_OF_DAY, interval);
      return new RubyDate(c.getTime());
    }

    /**
     * Shifts the time by days.
     * 
     * @return a RubyDate
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
     * @return a RubyDate
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
     * @return a RubyDate
     */
    public RubyDate years() {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(Calendar.YEAR, interval);
      return new RubyDate(c.getTime());
    }

  }

}
