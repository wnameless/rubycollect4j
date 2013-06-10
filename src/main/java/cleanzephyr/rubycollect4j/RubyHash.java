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

import cleanzephyr.rubycollect4j.block.EntryBlock;
import cleanzephyr.rubycollect4j.block.EntryBooleanBlock;
import cleanzephyr.rubycollect4j.block.EntryMergeBlock;
import cleanzephyr.rubycollect4j.block.ItemBlock;
import static com.google.common.collect.Lists.newArrayList;
import static cleanzephyr.rubycollect4j.RubyArray.newRubyArray;
import static com.google.common.collect.Maps.newLinkedHashMap;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author WMW
 * @param <K>
 * @param <V>
 */
public class RubyHash<K, V> extends RubyEnumerable<Entry<K, V>> implements Map<K, V> {

  private final LinkedHashMap<K, V> map;
  private V defaultValue;

  public static <K, V> RubyHash<K, V> newRubyHash() {
    return new RubyHash(newLinkedHashMap());
  }

  public static <K, V> RubyHash<K, V> newRubyHash(Map<K, V> map) {
    return new RubyHash(newLinkedHashMap(map));
  }

  public static <K, V> RubyHash<K, V> newRubyHash(LinkedHashMap<K, V> map, boolean defensiveCopy) {
    if (defensiveCopy) {
      return new RubyHash(newLinkedHashMap(map));
    }
    return new RubyHash(map);
  }

  private RubyHash(LinkedHashMap<K, V> map) {
    super(map.entrySet());
    this.map = map;
  }

  public RubyHash<K, V> put(Entry<K, V> entry) {
    map.put(entry.getKey(), entry.getValue());
    return this;
  }

  public RubyHash<K, V> put(Entry<K, V>... entries) {
    for (Entry<K, V> entry : entries) {
      map.put(entry.getKey(), entry.getValue());
    }
    return this;
  }

  public Entry<K, V> assoc(K key) {
    if (map.containsKey(key)) {
      return new AbstractMap.SimpleEntry(key, map.get(key));
    } else {
      return null;
    }
  }

  public RubyHash<K, V> compareByIdentity() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public boolean comparedByIdentityʔ() {
    return false;
  }

  public V setDefault(V defaultValue) {
    this.defaultValue = defaultValue;
    return this.defaultValue;
  }

  public V getDefault() {
    return defaultValue;
  }

  public V delete(K key) {
    V removedItem = map.remove(key);
    if (removedItem == null && defaultValue != null) {
      return defaultValue;
    } else {
      return removedItem;
    }
  }

  public RubyHash<K, V> deleteIf(EntryBooleanBlock<K, V> block) {
    for (Entry<K, V> item : map.entrySet()) {
      if (block.yield(item.getKey(), item.getValue())) {
        map.remove(item.getKey());
      }
    }
    return this;
  }

  public RubyHash<K, V> each(EntryBlock<K, V> block) {
    for (Entry<K, V> item : map.entrySet()) {
      block.yield(item.getKey(), item.getValue());
    }
    return this;
  }

  public RubyHash<K, V> eachPair(EntryBlock<K, V> block) {
    return each(block);
  }

  public RubyHash<K, V> eachKey(ItemBlock<K> block) {
    for (K item : map.keySet()) {
      block.yield(item);
    }
    return this;
  }

  public RubyHash<K, V> eachValue(ItemBlock<V> block) {
    for (V item : map.values()) {
      block.yield(item);
    }
    return this;
  }

  public boolean emptyʔ() {
    return map.isEmpty();
  }

  public boolean eqlʔ(RubyHash other) {
    return this.equals(other);
  }

  public V fetch(K key) {
    if (!map.containsKey(key)) {
      throw new IllegalArgumentException("key not found: " + key);
    }
    return map.get(key);
  }

  public V fetch(K key, V defaultValue) {
    if (!map.containsKey(key)) {
      return defaultValue;
    }
    return map.get(key);
  }

  public RubyArray<Entry<K, V>> flatten() {
    return newRubyArray(new RubyEnumerable(map.entrySet()).toA());
  }

  public boolean keyʔ(K key) {
    return map.containsKey(key);
  }

  public boolean valueʔ(V value) {
    return map.containsValue(value);
  }

  public int hash() {
    return map.hashCode();
  }

  public RubyHash<K, V> toH() {
    return this;
  }

  public RubyHash<K, V> toHash() {
    return this;
  }

  public String toS() {
    return map.toString();
  }

  public String inspect() {
    return map.toString();
  }

  public RubyHash<V, K> invert() {
    RubyHash<V, K> invertHash = newRubyHash();
    for (Entry<K, V> item : map.entrySet()) {
      invertHash.put(item.getValue(), item.getKey());
    }
    return invertHash;
  }

  public RubyHash<K, V> keepIf(EntryBooleanBlock<K, V> block) {
    for (Entry<K, V> item : map.entrySet()) {
      if (!block.yield(item.getKey(), item.getValue())) {
        map.remove(item.getKey());
      }
    }
    return this;
  }

  public K key(V value) {
    for (Entry<K, V> item : map.entrySet()) {
      if (item.getValue().equals(value)) {
        return item.getKey();
      }
    }
    return null;
  }

  public RubyArray<K> keys() {
    return newRubyArray(map.keySet());
  }

  public int length() {
    return map.size();
  }

  public RubyHash<K, V> merge(Map<K, V> otherHash) {
    RubyHash<K, V> newHash = newRubyHash();
    for (Entry<K, V> item : map.entrySet()) {
      newHash.put(item);
    }
    for (Entry<K, V> item : otherHash.entrySet()) {
      newHash.put(item);
    }
    return newHash;
  }

  public RubyHash<K, V> mergeEx(Map<K, V> otherHash) {
    for (Entry<K, V> item : otherHash.entrySet()) {
      map.put(item.getKey(), item.getValue());
    }
    return this;
  }

  public RubyHash<K, V> update(Map<K, V> otherHash) {
    return mergeEx(otherHash);
  }

  public RubyHash<K, V> merge(Map<K, V> otherHash, EntryMergeBlock<K, V> block) {
    RubyHash<K, V> newHash = newRubyHash();
    for (Entry<K, V> item : map.entrySet()) {
      if (map.containsKey(item.getKey()) && otherHash.containsKey(item.getKey())) {
        newHash.put(item.getKey(), block.yield(item.getKey(), item.getValue(), otherHash.get(item.getKey())));
      } else {
        newHash.put(item);
      }
    }
    for (Entry<K, V> item : otherHash.entrySet()) {
      if (!newHash.containsKey(item.getKey())) {
        newHash.put(item);
      }
    }
    return newHash;
  }

  public RubyHash<K, V> mergeǃ(Map<K, V> otherHash, EntryMergeBlock<K, V> block) {
    for (Entry<K, V> item : map.entrySet()) {
      if (map.containsKey(item.getKey()) && otherHash.containsKey(item.getKey())) {
        map.put(item.getKey(), block.yield(item.getKey(), item.getValue(), otherHash.get(item.getKey())));
      } else {
        map.put(item.getKey(), item.getValue());
      }
    }
    for (Entry<K, V> item : otherHash.entrySet()) {
      if (!map.containsKey(item.getKey())) {
        map.put(item.getKey(), item.getValue());
      }
    }
    return this;
  }

  public RubyHash<K, V> update(Map<K, V> otherHash, EntryMergeBlock<K, V> block) {
    return mergeǃ(otherHash, block);
  }

  public Entry<K, V> rassoc(V value) {
    for (Entry<K, V> item : map.entrySet()) {
      if (item.getValue().equals(item)) {
        return item;
      }
    }
    return null;
  }

  public RubyHash<K, V> rejectǃ(EntryBooleanBlock<K, V> block) {
    int beforeSize = map.size();
    deleteIf(block);
    if (map.size() == beforeSize) {
      return null;
    } else {
      return this;
    }
  }

  public RubyHash<K, V> replace(Map<K, V> otherHash) {
    map.clear();
    map.putAll(otherHash);
    return this;
  }

  public Entry<K, V> shift() {
    if (map.isEmpty()) {
      return null;
    } else {
      return map.entrySet().iterator().next();
    }
  }

  public V store(K key, V value) {
    map.put(key, value);
    return value;
  }

  @Override
  public RubyArray<V> values() {
    return newRubyArray(map.values());
  }

  public RubyArray<V> valuesAt(K... keys) {
    RubyArray<V> values = newRubyArray();
    for (K key : keys) {
      values.add(map.get(key));
    }
    return values;
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return map.containsValue(value);
  }

  @Override
  public V get(Object key) {
    return map.get(key);
  }

  @Override
  public V put(K key, V value) {
    return map.put(key, value);
  }

  @Override
  public V remove(Object key) {
    return map.remove(key);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    map.putAll(m);
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public Set<K> keySet() {
    return map.keySet();
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    return map.entrySet();
  }

  @Override
  public String toString() {
    return map.toString();
  }

}
