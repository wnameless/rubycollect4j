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
package cleanzephyr.util.rubycollections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public final class RubyCollections {

  public static <E> RubyArray<E> newRubyArray() {
    return new RubyArrayList<>();
  }

  public static <E> RubyArray<E> newRubyArray(Collection<E> coll) {
    return new RubyArrayList(new ArrayList<>(coll));
  }

  public static <E> RubyArray<E> newRubyArray(Iterator<E> iter) {
    return new RubyArrayList(iter);
  }

  public static <E> RubyArray<E> newRubyArray(E... args) {
    return new RubyArrayList(Arrays.asList(args), true);
  }

  public static <E> RubyArray<E> ra() {
    return new RubyArrayList<>();
  }

  public static <E> RubyArray<E> ra(Collection<E> coll) {
    return new RubyArrayList(coll);
  }

  public static <E> RubyArray<E> ra(Iterator<E> iter) {
    return new RubyArrayList(iter);
  }

  public static <E> RubyArray<E> ra(E... args) {
    return new RubyArrayList(Arrays.asList(args), true);
  }

  public static <K, V> RubyHash<K, V> newRubyHash() {
    return new RubyLinkedHashMap();
  }

  public static <K, V> RubyHash<K, V> newRubyHash(Map<K, V> map) {
    return new RubyLinkedHashMap(map);
  }

  public static <K, V> RubyHash<K, V> newRubyHash(LinkedHashMap<K, V> map, boolean defensiveCopy) {
    return new RubyLinkedHashMap(map, defensiveCopy);
  }

  public static <K, V> RubyHash<K, V> rh() {
    return new RubyLinkedHashMap();
  }

  public static <K, V> RubyHash<K, V> rh(Map<K, V> map) {
    return new RubyLinkedHashMap(map);
  }

  public static <K, V> RubyHash<K, V> rh(LinkedHashMap<K, V> map, boolean defensiveCopy) {
    return new RubyLinkedHashMap(map, defensiveCopy);
  }

  public static <K, V> RubyHash<K, V> rh(K key, V value) {
    RubyLinkedHashMap<K, V> rh = new RubyLinkedHashMap();
    rh.put(key, value);
    return rh;
  }

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2) {
    RubyLinkedHashMap<K, V> rh = new RubyLinkedHashMap();
    rh.put(key1, value1);
    rh.put(key2, value2);
    return rh;
  }

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2, K key3, V value3) {
    RubyLinkedHashMap<K, V> rh = new RubyLinkedHashMap();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    return rh;
  }

  public static <K, V> RubyLinkedHashMap<K, V> rh(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
    RubyLinkedHashMap<K, V> rh = new RubyLinkedHashMap();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    rh.put(key4, value4);
    return rh;
  }

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
    RubyLinkedHashMap<K, V> rh = new RubyLinkedHashMap();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    rh.put(key4, value4);
    rh.put(key5, value5);
    return rh;
  }

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6) {
    RubyLinkedHashMap<K, V> rh = new RubyLinkedHashMap();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    rh.put(key4, value4);
    rh.put(key5, value5);
    rh.put(key6, value6);
    return rh;
  }

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7) {
    RubyLinkedHashMap<K, V> rh = new RubyLinkedHashMap();
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
    RubyLinkedHashMap<K, V> rh = new RubyLinkedHashMap();
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
    RubyLinkedHashMap<K, V> rh = new RubyLinkedHashMap();
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
    RubyLinkedHashMap<K, V> rh = new RubyLinkedHashMap();
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

    System.out.println(rh("c", 1, "b", 2, "aa", 3).sortBy((k, v) -> {
      return k.length();
    }));
  }
}
