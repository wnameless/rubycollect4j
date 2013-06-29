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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static net.sf.rubycollect4j.RubyArray.newRubyArray;
import static net.sf.rubycollect4j.RubyHash.newRubyHash;
import static net.sf.rubycollect4j.RubyRange.newRubyRange;

/**
 * 
 * Provides numerous useful static methods to make rubycollect4j easy to use.
 * 
 */
public final class RubyCollections {

  private RubyCollections() {}

  /**
   * Creates a regular expression Pattern.
   * 
   * @param regex
   *          regular expression
   * @return a Pattern
   */
  public static Pattern qr(String regex) {
    return Pattern.compile(regex);
  }

  /**
   * Creates a RubyArray of Strings.
   * 
   * @param str
   *          words separated by spaces
   * @return a RubyArray
   */
  public static RubyArray<String> qw(String str) {
    return newRubyArray(str.trim().split("\\s+"));
  }

  /**
   * Executes a system command and returns its result.
   * 
   * @param cmd
   *          to be executed
   * @return a String
   */
  public static String qx(String... cmd) {
    StringBuilder sb = new StringBuilder();

    try {
      Process proc = Runtime.getRuntime().exec(cmd);
      BufferedReader stdInput =
          new BufferedReader(new InputStreamReader(proc.getInputStream()));
      BufferedReader stdError =
          new BufferedReader(new InputStreamReader(proc.getErrorStream()));

      String s;
      while ((s = stdInput.readLine()) != null) {
        sb.append(s).append("\n");
      }

      while ((s = stdError.readLine()) != null) {
        sb.append(s).append("\n");
      }
    } catch (IOException ex) {
      Logger.getLogger(RubyCollections.class.getName()).log(Level.SEVERE, null,
          ex);
      sb.append(ex.getMessage());
    }

    return sb.toString();
  }

  /**
   * Creates an empty RubyArray
   * 
   * @param <E>
   *          the type of the elements
   * @return a RubyArray
   */
  public static <E> RubyArray<E> ra() {
    return newRubyArray();
  }

  /**
   * Creates a RubyArray by given elements.
   * 
   * @param <E>
   *          the type of the elements
   * @param args
   *          elements
   * @return a RubyArray
   */
  public static <E> RubyArray<E> ra(E... args) {
    return newRubyArray(args);
  }

  /**
   * Creates a RubyArray by given Iterable.
   * 
   * @param <E>
   *          the type of the elements
   * @param iter
   *          an Iterable
   * @return a RubyArray
   */
  public static <E> RubyArray<E> ra(Iterable<E> iter) {
    return newRubyArray(iter);
  }

  /**
   * Creates a RubyArray by given Iterator.
   * 
   * @param <E>
   *          the type of the elements
   * @param iter
   *          an Iterator
   * @return a RubyArray
   */
  public static <E> RubyArray<E> ra(Iterator<E> iter) {
    return newRubyArray(iter);
  }

  /**
   * Creates a RubyArray by given List.
   * 
   * @param <E>
   *          the type of the elements
   * @param list
   *          a List
   * @return a RubyArray
   */
  public static <E> RubyArray<E> ra(List<E> list) {
    return newRubyArray(list);
  }

  /**
   * Creates a RubyArray which contains the given RubyArray.
   * 
   * @param <E>
   *          the type of the elements
   * @param rubyArray
   *          a RubyArray
   * @return a RubyArray of RubyArray
   */
  public static <E> RubyArray<RubyArray<E>> ra(RubyArray<E> rubyArray) {
    RubyArray<RubyArray<E>> ra = newRubyArray();
    return ra.push(rubyArray);
  }

  /**
   * Creates a empty RubyHash.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @return a RubyHaah
   */
  public static <K, V> RubyHash<K, V> rh() {
    return newRubyHash();
  }

  /**
   * Creates a empty RubyHash by given Map.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @param map
   *          any Map
   * @return a RubyHash
   */
  public static <K, V> RubyHash<K, V> rh(Map<K, V> map) {
    return newRubyHash(map);
  }

  /**
   * Creates a RubyHash by given key-value pair.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @param key
   *          of entry
   * @param value
   *          of entry
   * @return a RubyHash
   */
  public static <K, V> RubyHash<K, V> rh(K key, V value) {
    RubyHash<K, V> rh = newRubyHash();
    rh.put(key, value);
    return rh;
  }

  /**
   * Creates a RubyHash by given key-value pairs.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @param key1
   *          of entry 1
   * @param value1
   *          of entry 1
   * @param key2
   *          of entry 2
   * @param value2
   *          of entry 2
   * @return a RubyHash
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2) {
    RubyHash<K, V> rh = newRubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    return rh;
  }

  /**
   * Creates a RubyHash by given key-value pairs.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @param key1
   *          of entry 1
   * @param value1
   *          of entry 1
   * @param key2
   *          of entry 2
   * @param value2
   *          of entry 2
   * @param key3
   *          of entry 3
   * @param value3
   *          of entry 3
   * @return a RubyHash
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3) {
    RubyHash<K, V> rh = newRubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    return rh;
  }

  /**
   * Creates a RubyHash by given key-value pairs.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @param key1
   *          of entry 1
   * @param value1
   *          of entry 1
   * @param key2
   *          of entry 2
   * @param value2
   *          of entry 2
   * @param key3
   *          of entry 3
   * @param value3
   *          of entry 3
   * @param key4
   *          of entry 4
   * @param value4
   *          of entry 4
   * @return a RubyHash
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4) {
    RubyHash<K, V> rh = newRubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    rh.put(key4, value4);
    return rh;
  }

  /**
   * Creates a RubyHash by given key-value pairs.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @param key1
   *          of entry 1
   * @param value1
   *          of entry 1
   * @param key2
   *          of entry 2
   * @param value2
   *          of entry 2
   * @param key3
   *          of entry 3
   * @param value3
   *          of entry 3
   * @param key4
   *          of entry 4
   * @param value4
   *          of entry 4
   * @param key5
   *          of entry 5
   * @param value5
   *          of entry 5
   * @return a RubyHash
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5) {
    RubyHash<K, V> rh = newRubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    rh.put(key4, value4);
    rh.put(key5, value5);
    return rh;
  }

  /**
   * Creates a RubyHash by given key-value pairs.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @param key1
   *          of entry 1
   * @param value1
   *          of entry 1
   * @param key2
   *          of entry 2
   * @param value2
   *          of entry 2
   * @param key3
   *          of entry 3
   * @param value3
   *          of entry 3
   * @param key4
   *          of entry 4
   * @param value4
   *          of entry 4
   * @param key5
   *          of entry 5
   * @param value5
   *          of entry 5
   * @param key6
   *          of entry 6
   * @param value6
   *          of entry 6
   * @return a RubyHash
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6) {
    RubyHash<K, V> rh = newRubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    rh.put(key4, value4);
    rh.put(key5, value5);
    rh.put(key6, value6);
    return rh;
  }

  /**
   * Creates a RubyHash by given key-value pairs.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @param key1
   *          of entry 1
   * @param value1
   *          of entry 1
   * @param key2
   *          of entry 2
   * @param value2
   *          of entry 2
   * @param key3
   *          of entry 3
   * @param value3
   *          of entry 3
   * @param key4
   *          of entry 4
   * @param value4
   *          of entry 4
   * @param key5
   *          of entry 5
   * @param value5
   *          of entry 5
   * @param key6
   *          of entry 6
   * @param value6
   *          of entry 6
   * @param key7
   *          of entry 7
   * @param value7
   *          of entry 7
   * @return a RubyHash
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
      K key7, V value7) {
    RubyHash<K, V> rh = newRubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    rh.put(key4, value4);
    rh.put(key5, value5);
    rh.put(key6, value6);
    rh.put(key7, value7);
    return rh;
  }

  /**
   * Creates a RubyHash by given key-value pairs.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @param key1
   *          of entry 1
   * @param value1
   *          of entry 1
   * @param key2
   *          of entry 2
   * @param value2
   *          of entry 2
   * @param key3
   *          of entry 3
   * @param value3
   *          of entry 3
   * @param key4
   *          of entry 4
   * @param value4
   *          of entry 4
   * @param key5
   *          of entry 5
   * @param value5
   *          of entry 5
   * @param key6
   *          of entry 6
   * @param value6
   *          of entry 6
   * @param key7
   *          of entry 7
   * @param value7
   *          of entry 7
   * @param key8
   *          of entry 8
   * @param value8
   *          of entry 8
   * @return a RubyHash
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
      K key7, V value7, K key8, V value8) {
    RubyHash<K, V> rh = newRubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    rh.put(key4, value4);
    rh.put(key5, value5);
    rh.put(key6, value6);
    rh.put(key7, value7);
    rh.put(key8, value8);
    return rh;
  }

  /**
   * Creates a RubyHash by given key-value pairs.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @param key1
   *          of entry 1
   * @param value1
   *          of entry 1
   * @param key2
   *          of entry 2
   * @param value2
   *          of entry 2
   * @param key3
   *          of entry 3
   * @param value3
   *          of entry 3
   * @param key4
   *          of entry 4
   * @param value4
   *          of entry 4
   * @param key5
   *          of entry 5
   * @param value5
   *          of entry 5
   * @param key6
   *          of entry 6
   * @param value6
   *          of entry 6
   * @param key7
   *          of entry 7
   * @param value7
   *          of entry 7
   * @param key8
   *          of entry 8
   * @param value8
   *          of entry 8
   * @param key9
   *          of entry 9
   * @param value9
   *          of entry 9
   * @return a RubyHash
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
      K key7, V value7, K key8, V value8, K key9, V value9) {
    RubyHash<K, V> rh = newRubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    rh.put(key4, value4);
    rh.put(key5, value5);
    rh.put(key6, value6);
    rh.put(key7, value7);
    rh.put(key8, value8);
    rh.put(key9, value9);
    return rh;
  }

  /**
   * Creates a RubyHash by given key-value pairs.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @param key1
   *          of entry 1
   * @param value1
   *          of entry 1
   * @param key2
   *          of entry 2
   * @param value2
   *          of entry 2
   * @param key3
   *          of entry 3
   * @param value3
   *          of entry 3
   * @param key4
   *          of entry 4
   * @param value4
   *          of entry 4
   * @param key5
   *          of entry 5
   * @param value5
   *          of entry 5
   * @param key6
   *          of entry 6
   * @param value6
   *          of entry 6
   * @param key7
   *          of entry 7
   * @param value7
   *          of entry 7
   * @param key8
   *          of entry 8
   * @param value8
   *          of entry 8
   * @param key9
   *          of entry 9
   * @param value9
   *          of entry 9
   * @param key10
   *          of entry 10
   * @param value10
   *          of entry 10
   * @return a RubyHash
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
      K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10) {
    RubyHash<K, V> rh = newRubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    rh.put(key4, value4);
    rh.put(key5, value5);
    rh.put(key6, value6);
    rh.put(key7, value7);
    rh.put(key8, value8);
    rh.put(key9, value9);
    rh.put(key10, value10);
    return rh;
  }

  /**
   * Creates a hash pair by the SimpleEntry.
   * 
   * @param <K>
   *          the type of the key
   * @param <V>
   *          the type of the value
   * @param key
   *          of entry
   * @param value
   *          of entry
   * @return a SimpleEntry
   */
  public static <K, V> Entry<K, V> newPair(K key, V value) {
    return new SimpleEntry<K, V>(key, value);
  }

  /**
   * Creates a hash pair by the SimpleEntry.
   * 
   * @param <K>
   *          the type of the key
   * @param <V>
   *          the type of the value
   * @param key
   *          of entry
   * @param value
   *          of entry
   * @return a SimpleEntry
   */
  public static <K, V> Entry<K, V> hp(K key, V value) {
    return newPair(key, value);
  }

  /**
   * Turns a List of Entry into a RubyHash.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @param list
   *          a List of Entry
   * @return a RubyHash
   */
  public static <K, V> RubyHash<K, V> Hash(List<? extends Entry<K, V>> list) {
    RubyHash<K, V> rubyHash = rh();
    for (Entry<K, V> entry : list) {
      rubyHash.put(entry);
    }
    return rubyHash;
  }

  /**
   * Creates a RubyRange by given strings.
   * 
   * @param start
   *          of the range
   * @param end
   *          of the range
   * @return a RubyRange
   */
  public static RubyRange<String> range(String start, String end) {
    return newRubyRange(start, end);
  }

  /**
   * Creates a RubyRange by given integers.
   * 
   * @param start
   *          of the range
   * @param end
   *          of the range
   * @return a RubyRange
   */
  public static RubyRange<Integer> range(int start, int end) {
    return newRubyRange(start, end);
  }

  /**
   * Creates a RubyRange by given longs.
   * 
   * @param start
   *          of the range
   * @param end
   *          of the range
   * @return a RubyRange
   */
  public static RubyRange<Long> range(long start, long end) {
    return newRubyRange(start, end);
  }

  /**
   * Creates a RubyRange by given doubles.
   * 
   * @param start
   *          of the range
   * @param end
   *          of the range
   * @return a RubyRange
   */
  public static RubyRange<Double> range(double start, double end) {
    return newRubyRange(start, end);
  }

  /**
   * Creates a RubyRange by given Dates.
   * 
   * @param start
   *          of the range
   * @param end
   *          of the range
   * @return a RubyRange
   */
  public static RubyRange<Date> range(Date start, Date end) {
    return newRubyRange(start, end);
  }

  /**
   * Creates a RubyDate by given Date.
   * 
   * @param date
   *          a Date
   * @return a RubyDate
   */
  public static RubyDate date(Date date) {
    return new RubyDate(date);
  }

  /**
   * Creates a RubyDate by given year.
   * 
   * @param year
   *          of a date
   * @return a RubyDate
   */
  public static RubyDate date(int year) {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, 0);
    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    return new RubyDate(c.getTime());
  }

  /**
   * Creates a RubyDate by given year and month.
   * 
   * @param year
   *          of a date
   * @param month
   *          of a date
   * @return a RubyDate
   */
  public static RubyDate date(int year, int month) {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, month - 1);
    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    return new RubyDate(c.getTime());
  }

  /**
   * Creates a RubyDate by given year, month and day.
   * 
   * @param year
   *          of a date
   * @param month
   *          of a date
   * @param day
   *          of a date
   * @return a RubyDate
   */
  public static RubyDate date(int year, int month, int day) {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, month - 1);
    c.set(Calendar.DAY_OF_MONTH, day);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    return new RubyDate(c.getTime());
  }

  /**
   * Creates a RubyDate by given year, month, day and hour.
   * 
   * @param year
   *          of a date
   * @param month
   *          of a date
   * @param day
   *          of a date
   * @param hour
   *          of a date
   * @return a RubyDate
   */
  public static RubyDate date(int year, int month, int day, int hour) {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, month - 1);
    c.set(Calendar.DAY_OF_MONTH, day);
    c.set(Calendar.HOUR_OF_DAY, hour);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    return new RubyDate(c.getTime());
  }

  /**
   * Creates a RubyDate by given year, month, day, hour and minute.
   * 
   * @param year
   *          of a date
   * @param month
   *          of a date
   * @param day
   *          of a date
   * @param hour
   *          of a date
   * @param min
   *          of a date
   * @return a RubyDate
   */
  public static RubyDate date(int year, int month, int day, int hour, int min) {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, month - 1);
    c.set(Calendar.DAY_OF_MONTH, day);
    c.set(Calendar.HOUR_OF_DAY, hour);
    c.set(Calendar.MINUTE, min);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    return new RubyDate(c.getTime());
  }

  /**
   * Creates a RubyDate by given year, month, day, hour, minute and second.
   * 
   * @param year
   *          of a date
   * @param month
   *          of a date
   * @param day
   *          of a date
   * @param hour
   *          of a date
   * @param min
   *          of a date
   * @param sec
   *          of a date
   * @return a RubyDate
   */
  public static RubyDate date(int year, int month, int day, int hour, int min,
      int sec) {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, month - 1);
    c.set(Calendar.DAY_OF_MONTH, day);
    c.set(Calendar.HOUR_OF_DAY, hour);
    c.set(Calendar.MINUTE, min);
    c.set(Calendar.SECOND, sec);
    c.set(Calendar.MILLISECOND, 0);
    return new RubyDate(c.getTime());
  }

  /**
   * Creates a RubyDate by given year, month, day, hour, minute, second and
   * millisecond.
   * 
   * @param year
   *          of a date
   * @param month
   *          of a date
   * @param day
   *          of a date
   * @param hour
   *          of a date
   * @param min
   *          of a date
   * @param sec
   *          of a date
   * @param millisec
   *          of a date
   * @return a RubyDate
   */
  public static RubyDate date(int year, int month, int day, int hour, int min,
      int sec, int millisec) {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, month - 1);
    c.set(Calendar.DAY_OF_MONTH, day);
    c.set(Calendar.HOUR_OF_DAY, hour);
    c.set(Calendar.MINUTE, min);
    c.set(Calendar.SECOND, sec);
    c.set(Calendar.MILLISECOND, millisec);
    return new RubyDate(c.getTime());
  }

}
