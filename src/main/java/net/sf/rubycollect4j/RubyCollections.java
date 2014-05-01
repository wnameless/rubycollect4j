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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import net.sf.rubycollect4j.succ.CharacterSuccessor;
import net.sf.rubycollect4j.succ.DateSuccessor;
import net.sf.rubycollect4j.succ.DoubleSuccessor;
import net.sf.rubycollect4j.succ.IntegerSuccessor;
import net.sf.rubycollect4j.succ.LongSuccessor;
import net.sf.rubycollect4j.succ.StringSuccessor;
import net.sf.rubycollect4j.util.ComparableEntry;

/**
 * 
 * {@link RubyCollections} provides numerous useful static methods to make the
 * RubyCollect4J easy to use.
 * 
 */
public final class RubyCollections {

  private static final Logger logger = Logger.getLogger(RubyCollections.class
      .getName());

  private RubyCollections() {}

  /**
   * Creates an empty {@link RubyArray}.
   * 
   * @param <E>
   *          the type of the elements
   * @return new {@link RubyArray}
   */
  public static <E> RubyArray<E> newRubyArray() {
    return new RubyArray<E>();
  }

  /**
   * Creates a {@link RubyArray} by given Iterable.
   * 
   * @param <E>
   *          the type of the elements
   * @param iter
   *          an Iterable
   * @return new {@link RubyArray}
   */
  public static <E> RubyArray<E> newRubyArray(Iterable<E> iter) {
    return new RubyArray<E>(iter);
  }

  /**
   * Creates a {@link RubyArray} by given Iterator.
   * 
   * @param <E>
   *          the type of the elements
   * @param iter
   *          an Iterator
   * @return new {@link RubyArray}
   */
  public static <E> RubyArray<E> newRubyArray(Iterator<E> iter) {
    RubyArray<E> rubyArray = new RubyArray<E>();
    while (iter.hasNext()) {
      rubyArray.add(iter.next());
    }
    return rubyArray;
  }

  /**
   * Creates a {@link RubyArray} by given List.
   * 
   * @param <E>
   *          the type of the elements
   * @param list
   *          a List
   * @return new {@link RubyArray}
   */
  public static <E> RubyArray<E> newRubyArray(List<E> list) {
    return new RubyArray<E>(list);
  }

  /**
   * Creates a {@link RubyArray} by given elements.
   * 
   * @param <E>
   *          the type of the elements
   * @param elements
   *          varargs
   * @return new {@link RubyArray}
   */
  @SafeVarargs
  public static <E> RubyArray<E> newRubyArray(E... elements) {
    return new RubyArray<E>(new ArrayList<E>(Arrays.asList(elements)));
  }

  /**
   * Creates an empty {@link RubyHash}.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @return new {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> newRubyHash() {
    return new RubyHash<K, V>();
  }

  /**
   * Creates a {@link RubyHash} by given Map.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @param map
   *          a Map
   * @return new {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> newRubyHash(Map<K, V> map) {
    return new RubyHash<K, V>(new LinkedHashMap<K, V>(map));
  }

  /**
   * Creates an empty {@link RubySet}.
   * 
   * @param <E>
   *          the type of the elements
   * @return new {@link RubySet}
   */
  public static <E> RubySet<E> newRubySet() {
    return new RubySet<E>();
  }

  /**
   * Creates a {@link RubySet} by given Iterable.
   * 
   * @param <E>
   *          the type of the elements
   * @param iter
   *          any Iterable
   * @return new {@link RubySet}
   */
  public static <E> RubySet<E> newRubySet(Iterable<E> iter) {
    return new RubySet<E>(iter);
  }

  /**
   * Creates a {@link RubySet} by given elements.
   * 
   * @param elements
   *          varargs
   * @return new {@link RubySet}
   */
  @SafeVarargs
  public static <E> RubySet<E> newRubySet(E... elements) {
    return new RubySet<E>(Arrays.asList(elements));
  }

  /**
   * Creates a {@link RubyLazyEnumerator} by given Iterable.
   * 
   * @param <E>
   *          the type of the elements
   * @param iter
   *          an Iterable
   * @return new {@link RubyLazyEnumerator}
   */
  public static <E> RubyLazyEnumerator<E>
      newRubyLazyEnumerator(Iterable<E> iter) {
    return new RubyLazyEnumerator<E>(iter);
  }

  /**
   * Creates a {@link RubyEnumerator} by given Iterable.
   * 
   * @param <E>
   *          the type of the elements
   * @param iter
   *          an Iterable
   * @return new {@link RubyEnumerator}
   */
  public static <E> RubyEnumerator<E> newRubyEnumerator(Iterable<E> iter) {
    return new RubyEnumerator<E>(iter);
  }

  /**
   * Creates a {@link RubyEnumerator} by given Iterator.
   * 
   * @param <E>
   *          the type of the elements
   * @param iter
   *          an Iterator
   * @return new {@link RubyEnumerator}
   */
  public static <E> RubyEnumerator<E> newRubyEnumerator(Iterator<E> iter) {
    return new RubyEnumerator<E>(iter);
  }

  /**
   * Creates a {@link RubyString}.
   * 
   * @return {@link RubyString}
   */
  public static RubyString rs() {
    return new RubyString();
  }

  /**
   * Creates a {@link RubyString} by given Object.
   * 
   * @return {@link RubyString}
   */
  public static RubyString rs(Object o) {
    return new RubyString(o);
  }

  /**
   * Creates a regular expression Pattern.
   * 
   * @param regex
   *          regular expression
   * @return Pattern
   */
  public static Pattern qr(String regex) {
    return Pattern.compile(regex);
  }

  /**
   * Creates a {@link RubyArray} of Strings.
   * 
   * @param str
   *          words separated by spaces
   * @return {@link RubyArray}
   */
  public static RubyArray<String> qw(String str) {
    return newRubyArray(str.trim().split("\\s+"));
  }

  /**
   * Executes a system command and returns its result.
   * 
   * @param cmd
   *          to be executed
   * @return String
   * @throws RuntimeException
   *           if command not found
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
        sb.append(s).append(System.getProperty("line.separator"));
      }
      while ((s = stdError.readLine()) != null) {
        sb.append(s).append(System.getProperty("line.separator"));
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, null, e);
      throw new RuntimeException(e);
    }

    return sb.toString();
  }

  /**
   * Creates an empty {@link RubyArray}.
   * 
   * @param <E>
   *          the type of the elements
   * @return {@link RubyArray}
   */
  public static <E> RubyArray<E> ra() {
    return newRubyArray();
  }

  /**
   * Creates a {@link RubyArray} by given elements.
   * 
   * @param <E>
   *          the type of the elements
   * @param args
   *          elements
   * @return {@link RubyArray}
   */
  @SafeVarargs
  public static <E> RubyArray<E> ra(E... args) {
    return newRubyArray(args);
  }

  /**
   * Creates a {@link RubyArray} by given Iterable.
   * 
   * @param <E>
   *          the type of the elements
   * @param iter
   *          an Iterable
   * @return {@link RubyArray}
   */
  public static <E> RubyArray<E> ra(Iterable<E> iter) {
    return newRubyArray(iter);
  }

  /**
   * Creates a {@link RubyArray} by given Iterator.
   * 
   * @param <E>
   *          the type of the elements
   * @param iter
   *          an Iterator
   * @return {@link RubyArray}
   */
  public static <E> RubyArray<E> ra(Iterator<E> iter) {
    return newRubyArray(iter);
  }

  /**
   * Creates a {@link RubyArray} by given List.
   * 
   * @param <E>
   *          the type of the elements
   * @param list
   *          a List
   * @return {@link RubyArray}
   */
  public static <E> RubyArray<E> ra(List<E> list) {
    return newRubyArray(list);
  }

  /**
   * Creates a {@link RubyArray} which contains the given {@link RubyArray}.
   * 
   * @param <E>
   *          the type of the elements
   * @param rubyArray
   *          a {@link RubyArray}
   * @return {@link RubyArray} of {@link RubyArray}
   */
  public static <E> RubyArray<RubyArray<E>> ra(RubyArray<E> rubyArray) {
    RubyArray<RubyArray<E>> ra = newRubyArray();
    return ra.push(rubyArray);
  }

  /**
   * Creates an empty {@link RubyHash}.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @return {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> rh() {
    return newRubyHash();
  }

  /**
   * Creates a {@link RubyHash} by given Map.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @param map
   *          any Map
   * @return {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> rh(Map<K, V> map) {
    return newRubyHash(map);
  }

  /**
   * Creates a {@link RubyHash} by given key-value pair.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @param key
   *          of entry
   * @param value
   *          of entry
   * @return {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> rh(K key, V value) {
    RubyHash<K, V> rh = newRubyHash();
    rh.put(key, value);
    return rh;
  }

  /**
   * Creates a {@link RubyHash} by given key-value pairs.
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
   * @return {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2) {
    RubyHash<K, V> rh = newRubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    return rh;
  }

  /**
   * Creates a {@link RubyHash} by given key-value pairs.
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
   * @return {@link RubyHash}
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
   * Creates a {@link RubyHash} by given key-value pairs.
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
   * @return {@link RubyHash}
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
   * Creates a {@link RubyHash} by given key-value pairs.
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
   * @return {@link RubyHash}
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
   * Creates a {@link RubyHash} by given key-value pairs.
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
   * @return {@link RubyHash}
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
   * Creates a {@link RubyHash} by given key-value pairs.
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
   * @return {@link RubyHash}
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
   * Creates a {@link RubyHash} by given key-value pairs.
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
   * @return {@link RubyHash}
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
   * Creates a {@link RubyHash} by given key-value pairs.
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
   * @return {@link RubyHash}
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
   * Creates a {@link RubyHash} by given key-value pairs.
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
   * @return {@link RubyHash}
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
   * Creates a {@link RubyHash} by given key-value pairs.
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
   * @param key11
   *          of entry 11
   * @param value11
   *          of entry 11
   * @return {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
      K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10,
      K key11, V value11) {
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
    rh.put(key11, value11);
    return rh;
  }

  /**
   * Creates a {@link RubyHash} by given key-value pairs.
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
   * @param key11
   *          of entry 11
   * @param value11
   *          of entry 11
   * @param key12
   *          of entry 12
   * @param value12
   *          of entry 12
   * @return {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
      K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10,
      K key11, V value11, K key12, V value12) {
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
    rh.put(key11, value11);
    rh.put(key12, value12);
    return rh;
  }

  /**
   * Creates a {@link RubyHash} by given key-value pairs.
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
   * @param key11
   *          of entry 11
   * @param value11
   *          of entry 11
   * @param key12
   *          of entry 12
   * @param value12
   *          of entry 12
   * @param key13
   *          of entry 13
   * @param value13
   *          of entry 13
   * @return {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
      K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10,
      K key11, V value11, K key12, V value12, K key13, V value13) {
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
    rh.put(key11, value11);
    rh.put(key12, value12);
    rh.put(key13, value13);
    return rh;
  }

  /**
   * Creates a {@link RubyHash} by given key-value pairs.
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
   * @param key11
   *          of entry 11
   * @param value11
   *          of entry 11
   * @param key12
   *          of entry 12
   * @param value12
   *          of entry 12
   * @param key13
   *          of entry 13
   * @param value13
   *          of entry 13
   * @param key14
   *          of entry 14
   * @param value14
   *          of entry 14
   * @return {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
      K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10,
      K key11, V value11, K key12, V value12, K key13, V value13, K key14,
      V value14) {
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
    rh.put(key11, value11);
    rh.put(key12, value12);
    rh.put(key13, value13);
    rh.put(key14, value14);
    return rh;
  }

  /**
   * Creates a {@link RubyHash} by given key-value pairs.
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
   * @param key11
   *          of entry 11
   * @param value11
   *          of entry 11
   * @param key12
   *          of entry 12
   * @param value12
   *          of entry 12
   * @param key13
   *          of entry 13
   * @param value13
   *          of entry 13
   * @param key14
   *          of entry 14
   * @param value14
   *          of entry 14
   * @param key15
   *          of entry 15
   * @param value15
   *          of entry 15
   * @return {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
      K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10,
      K key11, V value11, K key12, V value12, K key13, V value13, K key14,
      V value14, K key15, V value15) {
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
    rh.put(key11, value11);
    rh.put(key12, value12);
    rh.put(key13, value13);
    rh.put(key14, value14);
    rh.put(key15, value15);
    return rh;
  }

  /**
   * Creates a {@link RubyHash} by given key-value pairs.
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
   * @param key11
   *          of entry 11
   * @param value11
   *          of entry 11
   * @param key12
   *          of entry 12
   * @param value12
   *          of entry 12
   * @param key13
   *          of entry 13
   * @param value13
   *          of entry 13
   * @param key14
   *          of entry 14
   * @param value14
   *          of entry 14
   * @param key15
   *          of entry 15
   * @param value15
   *          of entry 15
   * @param key16
   *          of entry 16
   * @param value16
   *          of entry 16
   * @return {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
      K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10,
      K key11, V value11, K key12, V value12, K key13, V value13, K key14,
      V value14, K key15, V value15, K key16, V value16) {
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
    rh.put(key11, value11);
    rh.put(key12, value12);
    rh.put(key13, value13);
    rh.put(key14, value14);
    rh.put(key15, value15);
    rh.put(key16, value16);
    return rh;
  }

  /**
   * Creates a {@link RubyHash} by given key-value pairs.
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
   * @param key11
   *          of entry 11
   * @param value11
   *          of entry 11
   * @param key12
   *          of entry 12
   * @param value12
   *          of entry 12
   * @param key13
   *          of entry 13
   * @param value13
   *          of entry 13
   * @param key14
   *          of entry 14
   * @param value14
   *          of entry 14
   * @param key15
   *          of entry 15
   * @param value15
   *          of entry 15
   * @param key16
   *          of entry 16
   * @param value16
   *          of entry 16
   * @param key17
   *          of entry 17
   * @param value17
   *          of entry 17
   * @return {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
      K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10,
      K key11, V value11, K key12, V value12, K key13, V value13, K key14,
      V value14, K key15, V value15, K key16, V value16, K key17, V value17) {
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
    rh.put(key11, value11);
    rh.put(key12, value12);
    rh.put(key13, value13);
    rh.put(key14, value14);
    rh.put(key15, value15);
    rh.put(key16, value16);
    rh.put(key17, value17);
    return rh;
  }

  /**
   * Creates a {@link RubyHash} by given key-value pairs.
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
   * @param key11
   *          of entry 11
   * @param value11
   *          of entry 11
   * @param key12
   *          of entry 12
   * @param value12
   *          of entry 12
   * @param key13
   *          of entry 13
   * @param value13
   *          of entry 13
   * @param key14
   *          of entry 14
   * @param value14
   *          of entry 14
   * @param key15
   *          of entry 15
   * @param value15
   *          of entry 15
   * @param key16
   *          of entry 16
   * @param value16
   *          of entry 16
   * @param key17
   *          of entry 17
   * @param value17
   *          of entry 17
   * @param key18
   *          of entry 18
   * @param value18
   *          of entry 18
   * @return {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
      K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10,
      K key11, V value11, K key12, V value12, K key13, V value13, K key14,
      V value14, K key15, V value15, K key16, V value16, K key17, V value17,
      K key18, V value18) {
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
    rh.put(key11, value11);
    rh.put(key12, value12);
    rh.put(key13, value13);
    rh.put(key14, value14);
    rh.put(key15, value15);
    rh.put(key16, value16);
    rh.put(key17, value17);
    rh.put(key18, value18);
    return rh;
  }

  /**
   * Creates a {@link RubyHash} by given key-value pairs.
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
   * @param key11
   *          of entry 11
   * @param value11
   *          of entry 11
   * @param key12
   *          of entry 12
   * @param value12
   *          of entry 12
   * @param key13
   *          of entry 13
   * @param value13
   *          of entry 13
   * @param key14
   *          of entry 14
   * @param value14
   *          of entry 14
   * @param key15
   *          of entry 15
   * @param value15
   *          of entry 15
   * @param key16
   *          of entry 16
   * @param value16
   *          of entry 16
   * @param key17
   *          of entry 17
   * @param value17
   *          of entry 17
   * @param key18
   *          of entry 18
   * @param value18
   *          of entry 18
   * @param key19
   *          of entry 19
   * @param value19
   *          of entry 19
   * @return {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
      K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10,
      K key11, V value11, K key12, V value12, K key13, V value13, K key14,
      V value14, K key15, V value15, K key16, V value16, K key17, V value17,
      K key18, V value18, K key19, V value19) {
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
    rh.put(key11, value11);
    rh.put(key12, value12);
    rh.put(key13, value13);
    rh.put(key14, value14);
    rh.put(key15, value15);
    rh.put(key16, value16);
    rh.put(key17, value17);
    rh.put(key18, value18);
    rh.put(key19, value19);
    return rh;
  }

  /**
   * Creates a {@link RubyHash} by given key-value pairs.
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
   * @param key11
   *          of entry 11
   * @param value11
   *          of entry 11
   * @param key12
   *          of entry 12
   * @param value12
   *          of entry 12
   * @param key13
   *          of entry 13
   * @param value13
   *          of entry 13
   * @param key14
   *          of entry 14
   * @param value14
   *          of entry 14
   * @param key15
   *          of entry 15
   * @param value15
   *          of entry 15
   * @param key16
   *          of entry 16
   * @param value16
   *          of entry 16
   * @param key17
   *          of entry 17
   * @param value17
   *          of entry 17
   * @param key18
   *          of entry 18
   * @param value18
   *          of entry 18
   * @param key19
   *          of entry 19
   * @param value19
   *          of entry 19
   * @param key20
   *          of entry 20
   * @param value20
   *          of entry 20
   * @return {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
      K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10,
      K key11, V value11, K key12, V value12, K key13, V value13, K key14,
      V value14, K key15, V value15, K key16, V value16, K key17, V value17,
      K key18, V value18, K key19, V value19, K key20, V value20) {
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
    rh.put(key11, value11);
    rh.put(key12, value12);
    rh.put(key13, value13);
    rh.put(key14, value14);
    rh.put(key15, value15);
    rh.put(key16, value16);
    rh.put(key17, value17);
    rh.put(key18, value18);
    rh.put(key19, value19);
    rh.put(key20, value20);
    return rh;
  }

  /**
   * Creates a hash pair by the {@link ComparableEntry}.
   * 
   * @param <K>
   *          the type of the key
   * @param <V>
   *          the type of the value
   * @param key
   *          of entry
   * @param value
   *          of entry
   * @return Entry
   */
  public static <K, V> Entry<K, V> hp(K key, V value) {
    return new ComparableEntry<K, V>(key, value);
  }

  /**
   * Turns a List of Entries into a {@link RubyHash}.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @param list
   *          a List of Entries
   * @return {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> Hash(List<? extends Entry<K, V>> list) {
    RubyHash<K, V> rubyHash = newRubyHash();
    for (Entry<K, V> entry : list) {
      rubyHash.put(entry);
    }
    return rubyHash;
  }

  /**
   * Turns a {@link RubyArray} of Lists into a {@link RubyHash}.
   * 
   * @param <E>
   *          the type of the elements
   * @param lists
   *          a {@link RubyArray} of List
   * @return {@link RubyHash}
   */
  public static <E> RubyHash<E, E> Hash(RubyArray<? extends List<E>> lists) {
    RubyHash<E, E> rubyHash = newRubyHash();
    for (List<E> list : lists) {
      if (list.size() < 1 || list.size() > 2)
        throw new IllegalArgumentException(
            "ArgumentError: invalid number of elements (" + list.size()
                + " for 1..2)");
      rubyHash.put(list.get(0), ra(list).at(1));
    }
    return rubyHash;
  }

  /**
   * Creates a {@link RubyRange} by given strings.
   * 
   * @param start
   *          of the range
   * @param end
   *          of the range
   * @return {@link RubyRange}
   */
  public static RubyRange<String> range(String start, String end) {
    return new RubyRange<String>(StringSuccessor.getInstance(), start, end);
  }

  /**
   * Creates a {@link RubyRange} by given chars.
   * 
   * @param start
   *          of the range
   * @param end
   *          of the range
   * @return {@link RubyRange}
   */
  public static RubyRange<Character> range(char start, char end) {
    return new RubyRange<Character>(CharacterSuccessor.getInstance(), start,
        end);
  }

  /**
   * Creates a {@link RubyRange} by given integers.
   * 
   * @param start
   *          of the range
   * @param end
   *          of the range
   * @return {@link RubyRange}
   */
  public static RubyRange<Integer> range(int start, int end) {
    return new RubyRange<Integer>(IntegerSuccessor.getInstance(), start, end);
  }

  /**
   * Creates a {@link RubyRange} by given longs.
   * 
   * @param start
   *          of the range
   * @param end
   *          of the range
   * @return {@link RubyRange}
   */
  public static RubyRange<Long> range(long start, long end) {
    return new RubyRange<Long>(LongSuccessor.getInstance(), start, end);
  }

  /**
   * Creates a {@link RubyRange} by given doubles.
   * 
   * @param start
   *          of the range
   * @param end
   *          of the range
   * @return {@link RubyRange}
   */
  public static RubyRange<Double> range(double start, double end) {
    String startStr = String.valueOf(start);
    String endStr = String.valueOf(end);
    int startPrecision = startStr.length() - startStr.lastIndexOf('.') - 1;
    int endPrecision = endStr.length() - endStr.lastIndexOf('.') - 1;
    return new RubyRange<Double>(new DoubleSuccessor(Math.max(startPrecision,
        endPrecision)), start, end);
  }

  /**
   * Creates a {@link RubyRange} by given Dates.
   * 
   * @param start
   *          of the range
   * @param end
   *          of the range
   * @return {@link RubyRange}
   */
  public static RubyRange<Date> range(Date start, Date end) {
    return new RubyRange<Date>(DateSuccessor.getInstance(), start, end);
  }

  /**
   * Creates a {@link RubyDate} by current Date.
   * 
   * @return {@link RubyDate}
   */
  public static RubyDate date() {
    return new RubyDate(new Date());
  }

  /**
   * Creates a {@link RubyDate} by given Date.
   * 
   * @param date
   *          a Date
   * @return {@link RubyDate}
   */
  public static RubyDate date(Date date) {
    return new RubyDate(date);
  }

  /**
   * Creates a {@link RubyDate} by given year.
   * 
   * @param year
   *          of a date
   * @return {@link RubyDate}
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
   * Creates a {@link RubyDate} by given year and month.
   * 
   * @param year
   *          of a date
   * @param month
   *          of a date
   * @return {@link RubyDate}
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
   * Creates a {@link RubyDate} by given year, month and day.
   * 
   * @param year
   *          of a date
   * @param month
   *          of a date
   * @param day
   *          of a date
   * @return {@link RubyDate}
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
   * Creates a {@link RubyDate} by given year, month, day and hour.
   * 
   * @param year
   *          of a date
   * @param month
   *          of a date
   * @param day
   *          of a date
   * @param hour
   *          of a date
   * @return {@link RubyDate}
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
   * Creates a {@link RubyDate} by given year, month, day, hour and minute.
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
   * @return {@link RubyDate}
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
   * Creates a {@link RubyDate} by given year, month, day, hour, minute and
   * second.
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
   * @return {@link RubyDate}
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
   * Creates a {@link RubyDate} by given year, month, day, hour, minute, second
   * and millisecond.
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
   * @return {@link RubyDate}
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

  /**
   * Checks if a String is null or blank(only white-spaces).
   * 
   * @param str
   *          any String
   * @return true if given String is null or blank, false otherwise
   */
  public static boolean isBlank(String str) {
    return str == null || str.trim().isEmpty();
  }

  /**
   * Checks if a String is not null or blank(only white-spaces).
   * 
   * 
   * @param str
   *          any String
   * @return true if given String is not null or blank, false otherwise
   */
  public static boolean isNotBlank(String str) {
    return !isBlank(str);
  }

  /**
   * Checks if an Iterable is null or empty.
   * 
   * @param iter
   *          any Iterable
   * @return true if given Iterable is null or empty, false otherwise
   */
  public static boolean isBlank(Iterable<?> iter) {
    return iter == null || !iter.iterator().hasNext();
  }

  /**
   * Checks if an Iterable is not null or empty.
   * 
   * @param iter
   *          any Iterable
   * @return true if given Iterable is not null or empty, false otherwise
   */
  public static boolean isNotBlank(Iterable<?> iter) {
    return !isBlank(iter);
  }

  /**
   * Checks if a Map is null or empty.
   * 
   * @param map
   *          any Map
   * @return true if given Map is null or empty, false otherwise
   */
  public static boolean isBlank(Map<?, ?> map) {
    return map == null || map.isEmpty();
  }

  /**
   * Checks if a Map is not null or empty.
   * 
   * @param map
   *          any Map
   * @return true if given Map is not null or empty, false otherwise
   */
  public static boolean isNotBlank(Map<?, ?> map) {
    return !isBlank(map);
  }

  /**
   * Checks if a {@link RubyHash} is null or empty.
   * 
   * @param rubyHash
   *          any {@link RubyHash}
   * @return true if given {@link RubyHash} is null or empty, false otherwise
   */
  public static boolean isBlank(RubyHash<?, ?> rubyHash) {
    return rubyHash == null || rubyHash.isEmpty();
  }

  /**
   * Checks if a {@link RubyHash} is not null or empty.
   * 
   * @param rubyHash
   *          any {@link RubyHash}
   * @return true if given {@link RubyHash} is not null or empty, false
   *         otherwise
   */
  public static boolean isNotBlank(RubyHash<?, ?> rubyHash) {
    return !isBlank(rubyHash);
  }

  /**
   * Checks if a Boolean is null or false.
   * 
   * @param bool
   *          any Boolean
   * @return true if given Boolean is null or False, false otherwise
   */
  public static boolean isBlank(Boolean bool) {
    return bool == null || bool.equals(Boolean.FALSE);
  }

  /**
   * Checks if a Boolean is not null or false.
   * 
   * @param bool
   *          any Boolean
   * @return true if given Boolean is not null or False, false otherwise
   */
  public static boolean isNotBlank(Boolean bool) {
    return !isBlank(bool);
  }

  /**
   * Checks if an Object is null.
   * 
   * @param o
   *          any Object
   * @return true if given Object is null, false otherwise
   */
  public static boolean isBlank(Object o) {
    return o == null;
  }

  /**
   * Checks if an Object is not null.
   * 
   * @param o
   *          any Object
   * @return true if given Object is not null, false otherwise
   */
  public static boolean isNotBlank(Object o) {
    return o != null;
  }

}
