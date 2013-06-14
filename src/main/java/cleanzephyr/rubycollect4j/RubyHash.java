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

import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cleanzephyr.rubycollect4j.block.BooleanBlock;
import cleanzephyr.rubycollect4j.block.EntryBlock;
import cleanzephyr.rubycollect4j.block.EntryBooleanBlock;
import cleanzephyr.rubycollect4j.block.EntryMergeBlock;
import cleanzephyr.rubycollect4j.block.EntryToRubyArrayBlock;
import cleanzephyr.rubycollect4j.block.EntryTransformBlock;
import cleanzephyr.rubycollect4j.block.ItemBlock;
import cleanzephyr.rubycollect4j.block.ItemToRubyArrayBlock;
import cleanzephyr.rubycollect4j.block.ItemTransformBlock;

import static cleanzephyr.rubycollect4j.RubyArray.newRubyArray;
import static cleanzephyr.rubycollect4j.RubyEnumerator.newRubyEnumerator;
import static com.google.common.collect.Maps.newLinkedHashMap;

/**
 * 
 * @author WMW
 * @param <K>
 * @param <V>
 */
public final class RubyHash<K, V> extends RubyEnumerable<Entry<K, V>> implements
    Map<K, V> {

  private final LinkedHashMap<K, V> map;
  private V defaultValue;

  public static <K, V> RubyHash<K, V> newRubyHash() {
    LinkedHashMap<K, V> linkedHashMap = newLinkedHashMap();
    return new RubyHash<K, V>(linkedHashMap);
  }

  public static <K, V> RubyHash<K, V> newRubyHash(Map<K, V> map) {
    LinkedHashMap<K, V> linkedHashMap = newLinkedHashMap(map);
    return new RubyHash<K, V>(linkedHashMap);
  }

  public static <K, V> RubyHash<K, V> newRubyHash(LinkedHashMap<K, V> map,
      boolean defensiveCopy) {
    if (defensiveCopy) {
      LinkedHashMap<K, V> linkedHashMap = newLinkedHashMap(map);
      return new RubyHash<K, V>(linkedHashMap);
    }
    return new RubyHash<K, V>(map);
  }

  private RubyHash(LinkedHashMap<K, V> map) {
    super(map.entrySet());
    this.map = map;
  }

  public Entry<K, V> assoc(K key) {
    if (map.containsKey(key)) {
      return new SimpleEntry<K, V>(key, map.get(key));
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

  public V delete(K key) {
    V removedItem = remove(key);
    if (removedItem == null && defaultValue != null) {
      return defaultValue;
    } else {
      return removedItem;
    }
  }

  public RubyEnumerator<Entry<K, V>> deleteIf() {
    return newRubyEnumerator(entrySet());
  }

  public RubyHash<K, V> deleteIf(EntryBooleanBlock<K, V> block) {
    Iterator<Entry<K, V>> iter = entrySet().iterator();
    while (iter.hasNext()) {
      Entry<K, V> item = iter.next();
      if (block.yield(item.getKey(), item.getValue())) {
        iter.remove();
      }
    }
    return this;
  }

  public V setDefault(V defaultValue) {
    this.defaultValue = defaultValue;
    return this.defaultValue;
  }

  public V getDefault() {
    return defaultValue;
  }

  public RubyEnumerator<Entry<K, V>> each() {
    return newRubyEnumerator(entrySet());
  }

  public RubyHash<K, V> each(EntryBlock<K, V> block) {
    for (Entry<K, V> item : entrySet()) {
      block.yield(item.getKey(), item.getValue());
    }
    return this;
  }

  public RubyEnumerator<K> eachKey() {
    return newRubyEnumerator(keySet());
  }

  public RubyHash<K, V> eachKey(ItemBlock<K> block) {
    for (K item : keySet()) {
      block.yield(item);
    }
    return this;
  }

  public RubyEnumerator<Entry<K, V>> eachPair() {
    return newRubyEnumerator(entrySet());
  }

  public RubyHash<K, V> eachPair(EntryBlock<K, V> block) {
    return each(block);
  }

  public RubyEnumerator<V> eachValue() {
    return newRubyEnumerator(values());
  }

  public RubyHash<K, V> eachValue(ItemBlock<V> block) {
    for (V item : values()) {
      block.yield(item);
    }
    return this;
  }

  public boolean emptyʔ() {
    return isEmpty();
  }

  public boolean eqlʔ(Object other) {
    return equals(other);
  }

  public V fetch(K key) {
    if (!containsKey(key)) {
      throw new IllegalArgumentException("key not found: " + key);
    }
    return get(key);
  }

  public V fetch(K key, V defaultValue) {
    if (!containsKey(key)) {
      return defaultValue;
    }
    return get(key);
  }

  public RubyArray<Entry<K, V>> flatten() {
    return newRubyArray(entrySet());
  }

  public int hash() {
    return hashCode();
  }

  public String inspect() {
    return toString();
  }

  public RubyHash<V, K> invert() {
    RubyHash<V, K> invertHash = newRubyHash();
    for (Entry<K, V> item : entrySet()) {
      invertHash.put(item.getValue(), item.getKey());
    }
    return invertHash;
  }

  public RubyEnumerator<Entry<K, V>> keepIf() {
    return newRubyEnumerator(entrySet());
  }

  public RubyHash<K, V> keepIf(EntryBooleanBlock<K, V> block) {
    Iterator<Entry<K, V>> iter = entrySet().iterator();
    while (iter.hasNext()) {
      Entry<K, V> item = iter.next();
      if (!block.yield(item.getKey(), item.getValue())) {
        iter.remove();
      }
    }
    return this;
  }

  public K key(V value) {
    for (Entry<K, V> item : entrySet()) {
      if (item.getValue().equals(value)) {
        return item.getKey();
      }
    }
    return null;
  }

  public RubyArray<K> keys() {
    return newRubyArray(keySet());
  }

  public boolean keyʔ(K key) {
    return containsKey(key);
  }

  public int length() {
    return size();
  }

  public RubyHash<K, V> merge(Map<K, V> otherHash) {
    RubyHash<K, V> newHash = newRubyHash();
    for (Entry<K, V> item : entrySet()) {
      newHash.put(item);
    }
    for (Entry<K, V> item : otherHash.entrySet()) {
      newHash.put(item);
    }
    return newHash;
  }

  public RubyHash<K, V> merge(Map<K, V> otherHash, EntryMergeBlock<K, V> block) {
    RubyHash<K, V> newHash = newRubyHash();
    for (Entry<K, V> item : entrySet()) {
      if (containsKey(item.getKey()) && otherHash.containsKey(item.getKey())) {
        newHash.put(
            item.getKey(),
            block.yield(item.getKey(), item.getValue(),
                otherHash.get(item.getKey())));
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

  public RubyHash<K, V> mergeǃ(Map<K, V> otherHash) {
    for (Entry<K, V> item : otherHash.entrySet()) {
      put(item.getKey(), item.getValue());
    }
    return this;
  }

  public RubyHash<K, V>
      mergeǃ(Map<K, V> otherHash, EntryMergeBlock<K, V> block) {
    for (Entry<K, V> item : entrySet()) {
      if (containsKey(item.getKey()) && otherHash.containsKey(item.getKey())) {
        put(item.getKey(),
            block.yield(item.getKey(), item.getValue(),
                otherHash.get(item.getKey())));
      } else {
        put(item.getKey(), item.getValue());
      }
    }
    for (Entry<K, V> item : otherHash.entrySet()) {
      if (!containsKey(item.getKey())) {
        put(item.getKey(), item.getValue());
      }
    }
    return this;
  }

  public RubyHash<K, V> put(Entry<K, V> entry) {
    put(entry.getKey(), entry.getValue());
    return this;
  }

  public RubyHash<K, V> put(Entry<K, V> entry, Entry<K, V>... entries) {
    put(entry.getKey(), entry.getValue());
    for (Entry<K, V> e : entries) {
      put(e.getKey(), e.getValue());
    }
    return this;
  }

  public Entry<K, V> rassoc(V value) {
    for (Entry<K, V> item : entrySet()) {
      if (item.getValue().equals(value)) {
        return item;
      }
    }
    return null;
  }

  public RubyEnumerator<Entry<K, V>> rejectǃ() {
    return newRubyEnumerator(entrySet());
  }

  public RubyHash<K, V> rejectǃ(EntryBooleanBlock<K, V> block) {
    int beforeSize = size();
    deleteIf(block);
    if (size() == beforeSize) {
      return null;
    } else {
      return this;
    }
  }

  public RubyHash<K, V> replace(Map<K, V> otherHash) {
    clear();
    putAll(otherHash);
    return this;
  }

  public Entry<K, V> shift() {
    if (isEmpty()) {
      return null;
    } else {
      Iterator<Entry<K, V>> iter = entrySet().iterator();
      Entry<K, V> first = iter.next();
      iter.remove();
      return first;
    }
  }

  public V store(K key, V value) {
    put(key, value);
    return value;
  }

  public RubyHash<K, V> toH() {
    return this;
  }

  public RubyHash<K, V> toHash() {
    return this;
  }

  public String toS() {
    return toString();
  }

  public RubyHash<K, V> update(Map<K, V> otherHash) {
    return mergeǃ(otherHash);
  }

  public RubyHash<K, V>
      update(Map<K, V> otherHash, EntryMergeBlock<K, V> block) {
    return mergeǃ(otherHash, block);
  }

  // Apply entry blocks to RubyEnumerable methods
  public boolean allʔ(final EntryBooleanBlock<K, V> block) {
    return allʔ(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public boolean anyʔ(final EntryBooleanBlock<K, V> block) {
    return anyʔ(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public <S> RubyEnumerator<Entry<S, RubyArray<Entry<K, V>>>> chunk(
      final EntryTransformBlock<K, V, S> block) {
    return chunk(new ItemTransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public <S> RubyArray<S> collectConcat(
      final EntryToRubyArrayBlock<K, V, S> block) {
    return collectConcat(new ItemToRubyArrayBlock<Entry<K, V>, S>() {

      @Override
      public RubyArray<S> yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public int count(final EntryBooleanBlock<K, V> block) {
    return count(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public void cycle(int n, final EntryBlock<K, V> block) {
    cycle(n, new ItemBlock<Entry<K, V>>() {

      @Override
      public void yield(Entry<K, V> item) {
        block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public void cycle(final EntryBlock<K, V> block) {
    cycle(new ItemBlock<Entry<K, V>>() {

      @Override
      public void yield(Entry<K, V> item) {
        block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public Entry<K, V> detect(final EntryBooleanBlock<K, V> block) {
    return detect(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public RubyArray<Entry<K, V>> dropWhile(final EntryBooleanBlock<K, V> block) {
    return dropWhile(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public RubyEnumerable<Entry<K, V>> eachEntry(final EntryBlock<K, V> block) {
    return eachEntry(new ItemBlock<Entry<K, V>>() {

      @Override
      public void yield(Entry<K, V> item) {
        block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public Entry<K, V> find(final EntryBooleanBlock<K, V> block) {
    return detect(block);
  }

  public RubyArray<Entry<K, V>> findAll(final EntryBooleanBlock<K, V> block) {
    return findAll(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public Integer findIndex(final EntryBooleanBlock<K, V> block) {
    return findIndex(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public <S> RubyArray<S> flatMap(final EntryToRubyArrayBlock<K, V, S> block) {
    return collectConcat(block);
  }

  public <S> RubyArray<S> grep(String regex,
      final EntryTransformBlock<K, V, S> block) {
    return grep(regex, new ItemTransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public <S> RubyHash<S, RubyArray<Entry<K, V>>> groupBy(
      final EntryTransformBlock<K, V, S> block) {
    return groupBy(new ItemTransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public <S> RubyArray<S> map(final EntryTransformBlock<K, V, S> block) {
    return map(new ItemTransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public <S> Entry<K, V> maxBy(Comparator<? super S> comp,
      final EntryTransformBlock<K, V, S> block) {
    return maxBy(comp, new ItemTransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public <S> Entry<K, V> maxBy(final EntryTransformBlock<K, V, S> block) {
    return maxBy(new ItemTransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public <S> Entry<K, V> minBy(Comparator<? super S> comp,
      final EntryTransformBlock<K, V, S> block) {
    return minBy(comp, new ItemTransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public <S> Entry<K, V> minBy(final EntryTransformBlock<K, V, S> block) {
    return minBy(new ItemTransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public <S> RubyArray<Entry<K, V>> minmaxBy(Comparator<? super S> comp,
      final EntryTransformBlock<K, V, S> block) {
    return minmaxBy(comp, new ItemTransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public <S> RubyArray<Entry<K, V>> minmaxBy(
      final EntryTransformBlock<K, V, S> block) {
    return minmaxBy(new ItemTransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public boolean noneʔ(final EntryBooleanBlock<K, V> block) {
    return noneʔ(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public boolean oneʔ(final EntryBooleanBlock<K, V> block) {
    return oneʔ(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public RubyArray<RubyArray<Entry<K, V>>> partition(
      final EntryBooleanBlock<K, V> block) {
    return partition(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public RubyArray<Entry<K, V>> reject(final EntryBooleanBlock<K, V> block) {
    return reject(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public RubyEnumerable<Entry<K, V>> reverseEach(final EntryBlock<K, V> block) {
    return reverseEach(new ItemBlock<Entry<K, V>>() {

      @Override
      public void yield(Entry<K, V> item) {
        block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public RubyArray<Entry<K, V>> select(final EntryBooleanBlock<K, V> block) {
    return findAll(block);
  }

  public RubyEnumerator<RubyArray<Entry<K, V>>> sliceBefore(
      final EntryBooleanBlock<K, V> block) {
    return sliceBefore(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public <S> RubyArray<Entry<K, V>> sortBy(Comparator<? super S> comp,
      final EntryTransformBlock<K, V, S> block) {
    return sortBy(comp, new ItemTransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public <S> RubyArray<Entry<K, V>> sortBy(
      final EntryTransformBlock<K, V, S> block) {
    return sortBy(new ItemTransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public RubyArray<Entry<K, V>> takeWhile(final EntryBooleanBlock<K, V> block) {
    return takeWhile(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  @Override
  public RubyArray<V> values() {
    return newRubyArray(map.values());
  }

  public RubyArray<V> valuesAt(K... keys) {
    RubyArray<V> values = newRubyArray();
    for (K key : keys) {
      values.add(get(key));
    }
    return values;
  }

  public boolean valueʔ(V value) {
    return containsValue(value);
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
  public boolean equals(Object o) {
    if (!(o instanceof Map))
      return false;
    return map.equals(o);
  }

  @Override
  public int hashCode() {
    return map.hashCode();
  }

  @Override
  public String toString() {
    return map.toString();
  }

}
