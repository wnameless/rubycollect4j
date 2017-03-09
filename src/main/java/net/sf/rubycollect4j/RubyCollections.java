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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.rubycollect4j.RubyRange.Interval;
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
 * @author Wei-Ming Wu
 * 
 */
public final class RubyCollections {

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
    iter.forEachRemaining(e -> rubyArray.add(e));
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
  public static <E> RubyLazyEnumerator<E> newRubyLazyEnumerator(
      Iterable<E> iter) {
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
   * @param o
   *          any object
   * @return {@link RubyString}
   */
  public static RubyString rs(Object o) {
    return new RubyString(o);
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
      K key7, V value7, K key8, V value8, K key9, V value9, K key10,
      V value10) {
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
   * @param key21
   *          of entry 21
   * @param value21
   *          of entry 21
   * @return {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
      K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10,
      K key11, V value11, K key12, V value12, K key13, V value13, K key14,
      V value14, K key15, V value15, K key16, V value16, K key17, V value17,
      K key18, V value18, K key19, V value19, K key20, V value20, K key21,
      V value21) {
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
    rh.put(key21, value21);
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
   * @param key21
   *          of entry 21
   * @param value21
   *          of entry 21
   * @param key22
   *          of entry 22
   * @param value22
   *          of entry 22
   * @return {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
      K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10,
      K key11, V value11, K key12, V value12, K key13, V value13, K key14,
      V value14, K key15, V value15, K key16, V value16, K key17, V value17,
      K key18, V value18, K key19, V value19, K key20, V value20, K key21,
      V value21, K key22, V value22) {
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
    rh.put(key21, value21);
    rh.put(key22, value22);
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
   * @param key21
   *          of entry 21
   * @param value21
   *          of entry 21
   * @param key22
   *          of entry 22
   * @param value22
   *          of entry 22
   * @param key23
   *          of entry 23
   * @param value23
   *          of entry 23
   * @return {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
      K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10,
      K key11, V value11, K key12, V value12, K key13, V value13, K key14,
      V value14, K key15, V value15, K key16, V value16, K key17, V value17,
      K key18, V value18, K key19, V value19, K key20, V value20, K key21,
      V value21, K key22, V value22, K key23, V value23) {
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
    rh.put(key21, value21);
    rh.put(key22, value22);
    rh.put(key23, value23);
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
   * @param key21
   *          of entry 21
   * @param value21
   *          of entry 21
   * @param key22
   *          of entry 22
   * @param value22
   *          of entry 22
   * @param key23
   *          of entry 23
   * @param value23
   *          of entry 23
   * @param key24
   *          of entry 24
   * @param value24
   *          of entry 24
   * @return {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
      K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10,
      K key11, V value11, K key12, V value12, K key13, V value13, K key14,
      V value14, K key15, V value15, K key16, V value16, K key17, V value17,
      K key18, V value18, K key19, V value19, K key20, V value20, K key21,
      V value21, K key22, V value22, K key23, V value23, K key24, V value24) {
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
    rh.put(key21, value21);
    rh.put(key22, value22);
    rh.put(key23, value23);
    rh.put(key24, value24);
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
   * @param key21
   *          of entry 21
   * @param value21
   *          of entry 21
   * @param key22
   *          of entry 22
   * @param value22
   *          of entry 22
   * @param key23
   *          of entry 23
   * @param value23
   *          of entry 23
   * @param key24
   *          of entry 24
   * @param value24
   *          of entry 24
   * @param key25
   *          of entry 25
   * @param value25
   *          of entry 25
   * @return {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
      K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10,
      K key11, V value11, K key12, V value12, K key13, V value13, K key14,
      V value14, K key15, V value15, K key16, V value16, K key17, V value17,
      K key18, V value18, K key19, V value19, K key20, V value20, K key21,
      V value21, K key22, V value22, K key23, V value23, K key24, V value24,
      K key25, V value25) {
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
    rh.put(key21, value21);
    rh.put(key22, value22);
    rh.put(key23, value23);
    rh.put(key24, value24);
    rh.put(key25, value25);
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
   * @param key21
   *          of entry 21
   * @param value21
   *          of entry 21
   * @param key22
   *          of entry 22
   * @param value22
   *          of entry 22
   * @param key23
   *          of entry 23
   * @param value23
   *          of entry 23
   * @param key24
   *          of entry 24
   * @param value24
   *          of entry 24
   * @param key25
   *          of entry 25
   * @param value25
   *          of entry 25
   * @param key26
   *          of entry 26
   * @param value26
   *          of entry 26
   * @return {@link RubyHash}
   */
  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2,
      K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
      K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10,
      K key11, V value11, K key12, V value12, K key13, V value13, K key14,
      V value14, K key15, V value15, K key16, V value16, K key17, V value17,
      K key18, V value18, K key19, V value19, K key20, V value20, K key21,
      V value21, K key22, V value22, K key23, V value23, K key24, V value24,
      K key25, V value25, K key26, V value26) {
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
    rh.put(key21, value21);
    rh.put(key22, value22);
    rh.put(key23, value23);
    rh.put(key24, value24);
    rh.put(key25, value25);
    rh.put(key26, value26);
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
   * Turns a {@link Collection} of Lists into a {@link RubyHash}.
   * 
   * @param <E>
   *          the type of the elements
   * @param cols
   *          a {@link Collection} of Collections
   * @return {@link RubyHash}
   */
  public static <E> RubyHash<E, E> Hash(
      Collection<? extends Collection<? extends E>> cols) {
    RubyHash<E, E> rubyHash = newRubyHash();
    for (Collection<? extends E> col : cols) {
      if (col.size() < 1 || col.size() > 2) throw new IllegalArgumentException(
          "ArgumentError: invalid number of elements (" + col.size()
              + " for 1..2)");
      Iterator<? extends E> iter = col.iterator();
      rubyHash.put(iter.next(), iter.hasNext() ? iter.next() : null);
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
    return new RubyRange<String>(StringSuccessor.getInstance(), start, end,
        Interval.CLOSED);
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
        end, Interval.CLOSED);
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
    return new RubyRange<Integer>(IntegerSuccessor.getInstance(), start, end,
        Interval.CLOSED);
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
    return new RubyRange<Long>(LongSuccessor.getInstance(), start, end,
        Interval.CLOSED);
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
    return new RubyRange<Double>(
        new DoubleSuccessor(Math.max(startPrecision, endPrecision)), start, end,
        Interval.CLOSED);
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
    return new RubyRange<Date>(DateSuccessor.getInstance(), start, end,
        Interval.CLOSED);
  }

}
