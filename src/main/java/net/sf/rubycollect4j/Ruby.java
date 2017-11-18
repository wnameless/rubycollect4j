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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import net.sf.rubycollect4j.RubyRange.Interval;
import net.sf.rubycollect4j.succ.CharacterSuccessor;
import net.sf.rubycollect4j.succ.DateSuccessor;
import net.sf.rubycollect4j.succ.DoubleSuccessor;
import net.sf.rubycollect4j.succ.IntegerSuccessor;
import net.sf.rubycollect4j.succ.LocalDateTimeSuccessor;
import net.sf.rubycollect4j.succ.LongSuccessor;
import net.sf.rubycollect4j.succ.StringSuccessor;
import net.sf.rubycollect4j.util.ComparableEntry;

/**
 * 
 * {@link Ruby} provides numerous useful static classes and methods to make the
 * RubyCollect4J easy to use.
 * 
 * @author Wei-Ming Wu
 *
 */
public final class Ruby {

  private Ruby() {}

  /**
   * 
   * {@link Array} provides numerous useful static methods to make the
   * {@link RubyArray} easy to use.
   * 
   * @author Wei-Ming Wu
   *
   */
  public static final class Array {

    private Array() {}

    /**
     * Creates an empty {@link RubyArray}.
     * 
     * @param <E>
     *          the type of the elements
     * @return new {@link RubyArray}
     */
    public static <E> RubyArray<E> create() {
      return new RubyArray<>();
    }

    /**
     * Creates a {@link RubyArray} by given List. It's a wrapper implementation.
     * No defensive copy has been made.
     * 
     * @param <E>
     *          the type of the elements
     * @param list
     *          a List
     * @return new {@link RubyArray}
     */
    public static <E> RubyArray<E> of(List<E> list) {
      return new RubyArray<>(list);
    }

    /**
     * Creates a {@link RubyArray} by given elements.
     * 
     * @param first
     *          the first element
     * @param others
     *          the other elements
     * @return new {@link RubyArray}
     */
    @SafeVarargs
    public static <E> RubyArray<E> of(E first, E... others) {
      RubyArray<E> rubyArray = new RubyArray<>();
      rubyArray.add(first);
      Arrays.asList(others).forEach(rubyArray::add);
      return rubyArray;
    }

    /**
     * Creates a {@link RubyArray} which contains the given {@link RubyArray}.
     * 
     * @param rubyArray
     *          a {@link RubyArray}
     * @return {@link RubyArray} of {@link RubyArray}
     */
    public static <E> RubyArray<RubyArray<E>> of(RubyArray<E> rubyArray) {
      RubyArray<RubyArray<E>> ra = new RubyArray<>();
      return ra.push(rubyArray);
    }

    /**
     * Creates a {@link RubyArray} by given elements.
     * 
     * @param elements
     *          an array of elements
     * @return new {@link RubyArray}
     */
    public static <E> RubyArray<E> copyOf(E[] elements) {
      RubyArray<E> rubyArray = new RubyArray<>();
      Arrays.asList(elements).forEach(rubyArray::add);
      return rubyArray;
    }

    /**
     * Creates a {@link RubyArray} by given List.
     * 
     * @param list
     *          any List
     * @return new {@link RubyArray}
     */
    public static <E> RubyArray<E> copyOf(List<E> list) {
      return RubyArray.copyOf(list);
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
    public static <E> RubyArray<E> copyOf(Iterable<E> iter) {
      return new RubyArray<>(iter);
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
    public static <E> RubyArray<E> copyOf(Iterator<E> iter) {
      RubyArray<E> rubyArray = new RubyArray<>();
      iter.forEachRemaining(rubyArray::add);
      return rubyArray;
    }

  }

  /**
   * 
   * {@link Hash} provides numerous useful static methods to make the
   * {@link RubyHash} easy to use.
   * 
   * @author Wei-Ming Wu
   *
   */
  public static final class Hash {

    private Hash() {}

    /**
     * Creates an empty {@link RubyHash}.
     * 
     * @param <K>
     *          the type of the key elements
     * @param <V>
     *          the type of the value elements
     * @return new {@link RubyHash}
     */
    public static <K, V> RubyHash<K, V> create() {
      return new RubyHash<>();
    }

    /**
     * Creates a {@link RubyHash} which wraps the given Map.
     * 
     * @param map
     *          any Map
     * @return {@link RubyHash}
     * @throws NullPointerException
     *           if map is null
     */
    public static <K, V> RubyHash<K, V> of(LinkedHashMap<K, V> map) {
      return RubyHash.of(map);
    }

    /**
     * Creates a {@link RubyHash} which copies the elements of given Map.
     * 
     * @param map
     *          any Map
     * @return {@link RubyHash}
     * @throws NullPointerException
     *           if map is null
     */
    public static <K, V> RubyHash<K, V> copyOf(Map<K, V> map) {
      return RubyHash.copyOf(map);
    }

    /**
     * Turns a List of Entries into a {@link RubyHash}.
     * 
     * @param <K>
     *          the type of the key elements
     * @param <V>
     *          the type of the value elements
     * @param entries
     *          an Iterable of Entries
     * @return {@link RubyHash}
     */
    public static <K, V> RubyHash<K, V> create(
        Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> entries) {
      RubyHash<K, V> rubyHash = new RubyHash<>();
      entries.forEach(rubyHash::put);
      return rubyHash;
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
    public static <K, V> RubyHash<K, V> of(K key, V value) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5, K key6,
        V value6) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
        K key7, V value7) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
        K key7, V value7, K key8, V value8) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
        K key7, V value7, K key8, V value8, K key9, V value9) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
        K key7, V value7, K key8, V value8, K key9, V value9, K key10,
        V value10) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
        K key7, V value7, K key8, V value8, K key9, V value9, K key10,
        V value10, K key11, V value11) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
        K key7, V value7, K key8, V value8, K key9, V value9, K key10,
        V value10, K key11, V value11, K key12, V value12) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
        K key7, V value7, K key8, V value8, K key9, V value9, K key10,
        V value10, K key11, V value11, K key12, V value12, K key13, V value13) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
        K key7, V value7, K key8, V value8, K key9, V value9, K key10,
        V value10, K key11, V value11, K key12, V value12, K key13, V value13,
        K key14, V value14) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
        K key7, V value7, K key8, V value8, K key9, V value9, K key10,
        V value10, K key11, V value11, K key12, V value12, K key13, V value13,
        K key14, V value14, K key15, V value15) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
        K key7, V value7, K key8, V value8, K key9, V value9, K key10,
        V value10, K key11, V value11, K key12, V value12, K key13, V value13,
        K key14, V value14, K key15, V value15, K key16, V value16) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
        K key7, V value7, K key8, V value8, K key9, V value9, K key10,
        V value10, K key11, V value11, K key12, V value12, K key13, V value13,
        K key14, V value14, K key15, V value15, K key16, V value16, K key17,
        V value17) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
        K key7, V value7, K key8, V value8, K key9, V value9, K key10,
        V value10, K key11, V value11, K key12, V value12, K key13, V value13,
        K key14, V value14, K key15, V value15, K key16, V value16, K key17,
        V value17, K key18, V value18) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
        K key7, V value7, K key8, V value8, K key9, V value9, K key10,
        V value10, K key11, V value11, K key12, V value12, K key13, V value13,
        K key14, V value14, K key15, V value15, K key16, V value16, K key17,
        V value17, K key18, V value18, K key19, V value19) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
        K key7, V value7, K key8, V value8, K key9, V value9, K key10,
        V value10, K key11, V value11, K key12, V value12, K key13, V value13,
        K key14, V value14, K key15, V value15, K key16, V value16, K key17,
        V value17, K key18, V value18, K key19, V value19, K key20, V value20) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
        K key7, V value7, K key8, V value8, K key9, V value9, K key10,
        V value10, K key11, V value11, K key12, V value12, K key13, V value13,
        K key14, V value14, K key15, V value15, K key16, V value16, K key17,
        V value17, K key18, V value18, K key19, V value19, K key20, V value20,
        K key21, V value21) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
        K key7, V value7, K key8, V value8, K key9, V value9, K key10,
        V value10, K key11, V value11, K key12, V value12, K key13, V value13,
        K key14, V value14, K key15, V value15, K key16, V value16, K key17,
        V value17, K key18, V value18, K key19, V value19, K key20, V value20,
        K key21, V value21, K key22, V value22) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
        K key7, V value7, K key8, V value8, K key9, V value9, K key10,
        V value10, K key11, V value11, K key12, V value12, K key13, V value13,
        K key14, V value14, K key15, V value15, K key16, V value16, K key17,
        V value17, K key18, V value18, K key19, V value19, K key20, V value20,
        K key21, V value21, K key22, V value22, K key23, V value23) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
        K key7, V value7, K key8, V value8, K key9, V value9, K key10,
        V value10, K key11, V value11, K key12, V value12, K key13, V value13,
        K key14, V value14, K key15, V value15, K key16, V value16, K key17,
        V value17, K key18, V value18, K key19, V value19, K key20, V value20,
        K key21, V value21, K key22, V value22, K key23, V value23, K key24,
        V value24) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
        K key7, V value7, K key8, V value8, K key9, V value9, K key10,
        V value10, K key11, V value11, K key12, V value12, K key13, V value13,
        K key14, V value14, K key15, V value15, K key16, V value16, K key17,
        V value17, K key18, V value18, K key19, V value19, K key20, V value20,
        K key21, V value21, K key22, V value22, K key23, V value23, K key24,
        V value24, K key25, V value25) {
      RubyHash<K, V> rh = new RubyHash<>();
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
    public static <K, V> RubyHash<K, V> of(K key1, V value1, K key2, V value2,
        K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6,
        K key7, V value7, K key8, V value8, K key9, V value9, K key10,
        V value10, K key11, V value11, K key12, V value12, K key13, V value13,
        K key14, V value14, K key15, V value15, K key16, V value16, K key17,
        V value17, K key18, V value18, K key19, V value19, K key20, V value20,
        K key21, V value21, K key22, V value22, K key23, V value23, K key24,
        V value24, K key25, V value25, K key26, V value26) {
      RubyHash<K, V> rh = new RubyHash<>();
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

  }

  /**
   * 
   * {@link Entry} provides numerous useful static methods to make the
   * {@link ComparableEntry} easy to use.
   * 
   * @author Wei-Ming Wu
   *
   */
  public static final class Entry {

    private Entry() {}

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
    public static <K, V> java.util.Map.Entry<K, V> of(K key, V value) {
      return new ComparableEntry<>(key, value);
    }

  }

  /**
   * 
   * {@link Set} provides numerous useful static methods to make the
   * {@link RubySet} easy to use.
   * 
   * @author Wei-Ming Wu
   *
   */
  public static final class Set {

    private Set() {}

    /**
     * Creates an empty {@link RubySet}.
     * 
     * @return {@link RubySet}
     */
    public static <E> RubySet<E> create() {
      return new RubySet<>();
    }

    /**
     * Creates a {@link RubySet} which wraps the given LinkedHashSet.
     * 
     * @param set
     *          any LinkedHashSet
     * @return {@link RubySet}
     */
    public static <E> RubySet<E> of(LinkedHashSet<E> set) {
      return RubySet.of(set);
    }

    /**
     * Creates a {@link RubySet} by given elements.
     * 
     * @param first
     *          the first element
     * @param others
     *          the other elements
     * @return {@link RubySet}
     */
    @SafeVarargs
    public static <E> RubySet<E> of(E first, E... others) {
      RubySet<E> rs = new RubySet<>();
      rs.add(first);
      rs.addAll(Arrays.asList(others));
      return rs;
    }

    /**
     * Creates a {@link RubySet} which copies the elements of given Iterable.
     * 
     * @param iter
     *          any Iterable
     * @return {@link RubySet}
     */
    public static <E> RubySet<E> copyOf(Iterable<E> iter) {
      return RubySet.copyOf(iter);
    }

    /**
     * Creates a {@link RubySet} which copies the elements of given Iterator.
     * 
     * @param iter
     *          any Iterator
     * @return {@link RubySet}
     */
    public static <E> RubySet<E> copyOf(Iterator<E> iter) {
      RubySet<E> rs = new RubySet<>();
      iter.forEachRemaining(rs::add);
      return rs;
    }

    /**
     * Creates a {@link RubySet} by given elements.
     * 
     * @param elements
     *          an array
     * @return new {@link RubySet}
     */
    public static <E> RubySet<E> copyOf(E[] elements) {
      return new RubySet<>(Arrays.asList(elements));
    }

  }

  /**
   * 
   * {@link Enumerator} provides numerous useful static methods to make the
   * {@link RubyEnumerator} easy to use.
   * 
   * @author Wei-Ming Wu
   *
   */
  public static final class Enumerator {

    private Enumerator() {}

    /**
     * @see RubyEnumerator#of(Iterable)
     */
    public static <E> RubyEnumerator<E> of(Iterable<E> iter) {
      return RubyEnumerator.of(iter);
    }

    /**
     * @see RubyEnumerator#copyOf(Iterable)
     */
    public static <E> RubyEnumerator<E> copyOf(Iterable<E> iter) {
      return RubyEnumerator.copyOf(iter);
    }

  }

  /**
   * 
   * {@link LazyEnumerator} provides numerous useful static methods to make the
   * {@link RubyLazyEnumerator} easy to use.
   * 
   * @author Wei-Ming Wu
   *
   */
  public static final class LazyEnumerator {

    private LazyEnumerator() {}

    /**
     * @see RubyLazyEnumerator#of(Iterable)
     */
    public static <E> RubyLazyEnumerator<E> of(Iterable<E> iter) {
      return RubyLazyEnumerator.of(iter);
    }

    /**
     * @see RubyLazyEnumerator#copyOf(Iterable)
     */
    public static <E> RubyLazyEnumerator<E> copyOf(Iterable<E> iter) {
      return RubyLazyEnumerator.copyOf(iter);
    }

  }

  /**
   * 
   * {@link String} provides numerous useful static methods to make the
   * {@link RubyString} easy to use.
   * 
   * @author Wei-Ming Wu
   *
   */
  public static final class String {

    private String() {}

    /**
     * Creates a {@link RubyString}.
     * 
     * @return {@link RubyString}
     */
    public static RubyString create() {
      return new RubyString();
    }

    /**
     * Creates a {@link RubyString} by given Object.
     * 
     * @param o
     *          any object
     * @return {@link RubyString}
     */
    public static RubyString of(java.lang.Object o) {
      return new RubyString(o);
    }

  }

  /**
   * 
   * {@link Range} provides numerous useful static methods to make the
   * {@link RubyRange} easy to use.
   * 
   * @author Wei-Ming Wu
   *
   */
  public static final class Range {

    private Range() {}

    /**
     * Creates a {@link RubyRange} by given strings.
     * 
     * @param start
     *          of the range
     * @param end
     *          of the range
     * @return {@link RubyRange}
     */
    public static RubyRange<java.lang.String> of(java.lang.String start,
        java.lang.String end) {
      return new RubyRange<>(StringSuccessor.getInstance(), start, end,
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
    public static RubyRange<Character> of(char start, char end) {
      return new RubyRange<>(CharacterSuccessor.getInstance(), start, end,
          Interval.CLOSED);
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
    public static RubyRange<Integer> of(int start, int end) {
      return new RubyRange<>(IntegerSuccessor.getInstance(), start, end,
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
    public static RubyRange<Long> of(long start, long end) {
      return new RubyRange<>(LongSuccessor.getInstance(), start, end,
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
    public static RubyRange<Double> of(double start, double end) {
      java.lang.String startStr = java.lang.String.valueOf(start);
      java.lang.String endStr = java.lang.String.valueOf(end);
      int startPrecision = startStr.length() - startStr.lastIndexOf('.') - 1;
      int endPrecision = endStr.length() - endStr.lastIndexOf('.') - 1;
      return new RubyRange<>(
          new DoubleSuccessor(Math.max(startPrecision, endPrecision)), start,
          end, Interval.CLOSED);
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
    public static RubyRange<java.util.Date> of(java.util.Date start,
        java.util.Date end) {
      return new RubyRange<>(DateSuccessor.getInstance(), start, end,
          Interval.CLOSED);
    }

    /**
     * Creates a {@link RubyRange} by given {@link LocalDateTime}s.
     * 
     * @param start
     *          of the range
     * @param end
     *          of the range
     * @return {@link RubyRange}
     */
    public static RubyRange<LocalDateTime> of(LocalDateTime start,
        LocalDateTime end) {
      return new RubyRange<>(LocalDateTimeSuccessor.getInstance(), start, end,
          Interval.CLOSED);
    }

  }

  /**
   * 
   * {@link Date} provides numerous useful static methods to make the
   * {@link RubyDate} easy to use.
   * 
   * @author Wei-Ming Wu
   *
   */
  public static final class Date extends RubyDate {

    private static final long serialVersionUID = 1L;

    private Date() {}

    /**
     * Creates a {@link RubyDate} by given Date.
     * 
     * @param date
     *          a Date
     * @return {@link RubyDate}
     */
    public static RubyDate of(java.util.Date date) {
      return new RubyDate(date);
    }

    /**
     * Creates a {@link RubyDate} by given year.
     * 
     * @param year
     *          of a date
     * @return {@link RubyDate}
     */
    public static RubyDate of(int year) {
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
    public static RubyDate of(int year, int month) {
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
    public static RubyDate of(int year, int month, int day) {
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
    public static RubyDate of(int year, int month, int day, int hour) {
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
    public static RubyDate of(int year, int month, int day, int hour, int min) {
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
    public static RubyDate of(int year, int month, int day, int hour, int min,
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
     * Creates a {@link RubyDate} by given year, month, day, hour, minute,
     * second and millisecond.
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
    public static RubyDate of(int year, int month, int day, int hour, int min,
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

  /**
   * 
   * {@link IO} provides numerous useful static methods to make the
   * {@link RubyIO} easy to use.
   * 
   * @author Wei-Ming Wu
   *
   */
  public static final class IO extends RubyIO {

    private IO(java.io.File file, Mode mode) throws IOException {
      super(file, mode);
    }

    /**
     * Creates a {@link RubyIO} by given File with read-only mode.
     * 
     * @param file
     *          any File
     * @return {@link RubyIO}
     * @throws IOException
     *           while file cannot be accessed
     */
    public static RubyIO of(java.io.File file) throws IOException {
      return new RubyIO(file, Mode.R);
    }

    /**
     * Creates a {@link RubyIO} by given File and {@link Mode}.
     * 
     * @param file
     *          any File
     * @param mode
     *          a {@link Mode}
     * @return {@link RubyIO}
     * @throws IOException
     *           while file cannot be accessed
     */
    public static RubyIO of(java.io.File file, Mode mode) throws IOException {
      return new RubyIO(file, mode);
    }

  }

  /**
   * 
   * {@link File} provides numerous useful static methods to make the
   * {@link RubyFile} easy to use.
   * 
   * @author Wei-Ming Wu
   *
   */
  public static final class File extends RubyFile {

    private File(java.io.File file, Mode mode)
        throws FileNotFoundException, IOException {
      super(file, mode);
    }

    /**
     * Creates a {@link RubyFile} by given File with read-only mode.
     * 
     * @param file
     *          any File
     * @return {@link RubyFile}
     * @throws FileNotFoundException
     *           while file cannot be found
     * @throws IOException
     *           while file cannot be accessed
     */
    public static RubyFile of(java.io.File file)
        throws FileNotFoundException, IOException {
      return new RubyFile(file, Mode.R);
    }

    /**
     * Creates a {@link RubyFile} by given File with read-only mode.
     * 
     * @param file
     *          any File
     * @return {@link RubyFile}
     * @param mode
     *          a {@link Mode}
     * @return {@link RubyFile}
     * @throws FileNotFoundException
     *           while file cannot be found
     * @throws IOException
     *           while file cannot be accessed
     */
    public static RubyFile of(java.io.File file, Mode mode)
        throws FileNotFoundException, IOException {
      return new RubyFile(file, mode);
    }

  }

  /**
   * 
   * {@link Dir} provides numerous useful static methods to make the
   * {@link RubyDir} easy to use.
   * 
   * @author Wei-Ming Wu
   *
   */
  public static final class Dir extends RubyDir {

    private Dir(java.io.File directory) {
      super(directory);
    }

    /**
     * Creates a {@link RubyDir} by given File.
     * 
     * @param dir
     *          a File
     * @return {@link RubyDir}
     */
    public static RubyDir of(java.io.File dir) {
      return new RubyDir(dir);
    }

  }

  /**
   * 
   * {@link Literals} provides numerous useful static methods to make the
   * {@link RubyLiterals} easy to use.
   * 
   * @author Wei-Ming Wu
   *
   */
  public static class Literals extends RubyLiterals {

    private Literals() {}

  }

  /**
   * 
   * {@link Kernel} provides numerous useful static methods to make the
   * {@link RubyKernel} easy to use.
   * 
   * @author Wei-Ming Wu
   *
   */
  public static final class Kernel extends RubyKernel {

    private Kernel() {}

  }

  /**
   * 
   * {@link Object} provides numerous useful static methods to make the
   * {@link RubyObject} easy to use.
   * 
   * @author Wei-Ming Wu
   *
   */
  public static final class Object extends RubyObject {

    private Object() {}

  }

}
