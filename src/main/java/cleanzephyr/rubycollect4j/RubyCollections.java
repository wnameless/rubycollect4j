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
package cleanzephyr.rubycollect4j;

import static cleanzephyr.rubycollect4j.RubyIO.puts;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public final class RubyCollections {

  public static RubyArray<String> w(String str) {
    return new RubyArray<>(str.trim().split("\\s+"));
  }

  public static <E> RubyArray<E> newRubyArray() {
    return new RubyArray<>();
  }

  public static <E> RubyArray<E> newRubyArray(Collection<E> coll) {
    return new RubyArray(coll);
  }

  public static <E> RubyArray<E> newRubyArray(Iterator<E> iter) {
    return new RubyArray(iter);
  }

  public static <E> RubyArray<E> newRubyArray(E... args) {
    return new RubyArray(Arrays.asList(args), true);
  }

  public static <E> RubyArray<E> ra() {
    return newRubyArray();
  }

  public static <E> RubyArray<E> ra(Collection<E> coll) {
    if (coll instanceof RubyArray) {
      return new RubyArray(Arrays.asList(coll), false);
    } else {
      return newRubyArray(coll);
    }
  }

  public static <E> RubyArray<E> ra(Iterator<E> iter) {
    return newRubyArray(iter);
  }

  public static <E> RubyArray<E> ra(E... args) {
    return newRubyArray(args);
  }

  public static <K, V> RubyHash<K, V> newRubyHash() {
    return new RubyHash();
  }

  public static <K, V> RubyHash<K, V> newRubyHash(Map<K, V> map) {
    return new RubyHash(map);
  }

  public static <K, V> RubyHash<K, V> newRubyHash(LinkedHashMap<K, V> map, boolean defensiveCopy) {
    return new RubyHash(map, defensiveCopy);
  }

  public static <K, V> RubyHash<K, V> rh() {
    return new RubyHash();
  }

  public static <K, V> RubyHash<K, V> rh(Map<K, V> map) {
    return new RubyHash(map);
  }

  public static <K, V> RubyHash<K, V> rh(LinkedHashMap<K, V> map, boolean defensiveCopy) {
    return new RubyHash(map, defensiveCopy);
  }

  public static <K, V> RubyHash<K, V> rh(K key, V value) {
    RubyHash<K, V> rh = new RubyHash();
    rh.put(key, value);
    return rh;
  }

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2) {
    RubyHash<K, V> rh = new RubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    return rh;
  }

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2, K key3, V value3) {
    RubyHash<K, V> rh = new RubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    return rh;
  }

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
    RubyHash<K, V> rh = new RubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    rh.put(key4, value4);
    return rh;
  }

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
    RubyHash<K, V> rh = new RubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    rh.put(key4, value4);
    rh.put(key5, value5);
    return rh;
  }

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6) {
    RubyHash<K, V> rh = new RubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    rh.put(key4, value4);
    rh.put(key5, value5);
    rh.put(key6, value6);
    return rh;
  }

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7) {
    RubyHash<K, V> rh = new RubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    rh.put(key4, value4);
    rh.put(key5, value5);
    rh.put(key6, value6);
    rh.put(key7, value7);
    return rh;
  }

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8) {
    RubyHash<K, V> rh = new RubyHash();
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

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8, K key9, V value9) {
    RubyHash<K, V> rh = new RubyHash();
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

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10) {
    RubyHash<K, V> rh = new RubyHash();
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

  public static void main(String[] args) {
    ra(1, 3, 4, 5, 7).chunk((i) -> {
      return i / 5;
    }).each((e) -> {
      puts(e.getKey());
    });
    puts(ra(1, 2, 3).add(ra(2, 3, 4)));
  }
}
