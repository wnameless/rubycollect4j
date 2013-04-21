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

import static cleanzephyr.rubycollect4j.RubyCollections.newRubyArray;
import cleanzephyr.rubycollect4j.blocks.EntryBlock;
import cleanzephyr.rubycollect4j.blocks.EntryBooleanBlock;
import cleanzephyr.rubycollect4j.blocks.EntryInjectWithInitBlock;
import cleanzephyr.rubycollect4j.blocks.EntryMergeBlock;
import cleanzephyr.rubycollect4j.blocks.EntryToListBlock;
import cleanzephyr.rubycollect4j.blocks.EntryTransformBlock;
import cleanzephyr.rubycollect4j.blocks.InjectBlock;
import cleanzephyr.rubycollect4j.blocks.ItemBlock;
import cleanzephyr.rubycollect4j.blocks.ItemFromListBlock;
import cleanzephyr.rubycollect4j.blocks.ItemWithIndexBlock;
import cleanzephyr.rubycollect4j.blocks.ItemWithObjectBlock;
import com.google.common.collect.ArrayListMultimap;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;
import com.google.common.collect.Multimap;
import static java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class RubyLinkedHashMap<K, V> implements RubyHash<K, V> {

  private final Map<K, V> map;
  private V defaultValue;

  public RubyLinkedHashMap() {
    map = newLinkedHashMap();
  }

  public RubyLinkedHashMap(Map<K, V> map) {
    this.map = newLinkedHashMap(map);
  }

  public RubyLinkedHashMap(LinkedHashMap<K, V> map, boolean defensiveCopy) {
    if (defensiveCopy) {
      this.map = newLinkedHashMap(map);
    } else {
      this.map = map;
    }
  }

  @Override
  public RubyLinkedHashMap<K, V> put(Entry<K, V> entry) {
    map.put(entry.getKey(), entry.getValue());
    return this;
  }

  @Override
  public RubyLinkedHashMap<K, V> put(Entry<K, V>... entries) {
    for (Entry<K, V> entry : entries) {
      map.put(entry.getKey(), entry.getValue());
    }
    return this;
  }

  // Ruby Hash methods
  @Override
  public Entry<K, V> assoc(K key) {
    if (map.containsKey(key)) {
      return new SimpleEntry(key, map.get(key));
    } else {
      return null;
    }
  }

  @Override
  public RubyLinkedHashMap<K, V> compareByIdentity() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean comparedByIdentityʔ() {
    return false;
  }

  @Override
  public V setDefault(V defaultValue) {
    this.defaultValue = defaultValue;
    return this.defaultValue;
  }

  @Override
  public V getDefault() {
    return defaultValue;
  }

  @Override
  public V delete(K key) {
    V removedItem = map.remove(key);
    if (removedItem == null && defaultValue != null) {
      return defaultValue;
    } else {
      return removedItem;
    }
  }

  @Override
  public RubyLinkedHashMap<K, V> deleteIf(EntryBooleanBlock<K, V> block) {
    for (Entry<K, V> item : map.entrySet()) {
      if (block.yield(item.getKey(), item.getValue())) {
        map.remove(item.getKey());
      }
    }
    return this;
  }

  @Override
  public RubyLinkedHashMap<K, V> each(EntryBlock<K, V> block) {
    for (Entry<K, V> item : map.entrySet()) {
      block.yield(item.getKey(), item.getValue());
    }
    return this;
  }

  @Override
  public RubyArray<Entry<K, V>> eachEntry(ItemBlock<Entry<K, V>> block) {
    return RubyEnumerable.eachEntry(map.entrySet(), block);
  }

  @Override
  public RubyEnumerator<Entry<K, V>> eachEntry() {
    return RubyEnumerable.eachEntry(map.entrySet());
  }

  @Override
  public RubyLinkedHashMap<K, V> eachPair(EntryBlock<K, V> block) {
    return each(block);
  }

  @Override
  public RubyLinkedHashMap<K, V> eachKey(ItemBlock<K> block) {
    for (K item : map.keySet()) {
      block.yield(item);
    }
    return this;
  }

  @Override
  public RubyLinkedHashMap<K, V> eachValue(ItemBlock<V> block) {
    for (V item : map.values()) {
      block.yield(item);
    }
    return this;
  }

  @Override
  public boolean emptyʔ() {
    return map.isEmpty();
  }

  @Override
  public boolean eqlʔ(RubyHash other) {
    return this.equals(other);
  }

  @Override
  public V fetch(K key) {
    if (!map.containsKey(key)) {
      throw new IllegalArgumentException("key not found: " + key);
    }
    return map.get(key);
  }

  @Override
  public V fetch(K key, V defaultValue) {
    if (!map.containsKey(key)) {
      return defaultValue;
    }
    return map.get(key);
  }

  @Override
  public RubyArrayList<Entry<K, V>> flatten() {
    return new RubyArrayList(RubyEnumerable.toA(map.entrySet()));
  }

  @Override
  public boolean keyʔ(K key) {
    return map.containsKey(key);
  }

  @Override
  public boolean valueʔ(V value) {
    return map.containsValue(value);
  }

  @Override
  public int hash() {
    return map.hashCode();
  }

  @Override
  public RubyLinkedHashMap<K, V> toH() {
    return this;
  }

  @Override
  public RubyLinkedHashMap<K, V> toHash() {
    return this;
  }

  @Override
  public RubyArrayList<Entry<K, V>> toA() {
    return new RubyArrayList(RubyEnumerable.toA(map.entrySet()));
  }

  @Override
  public String toS() {
    return map.toString();
  }

  @Override
  public String inspect() {
    return map.toString();
  }

  @Override
  public RubyLinkedHashMap<V, K> invert() {
    RubyLinkedHashMap<V, K> invertHash = new RubyLinkedHashMap();
    for (Entry<K, V> item : map.entrySet()) {
      invertHash.put(item.getValue(), item.getKey());
    }
    return invertHash;
  }

  @Override
  public RubyLinkedHashMap<K, V> keepIf(EntryBooleanBlock<K, V> block) {
    for (Entry<K, V> item : map.entrySet()) {
      if (!block.yield(item.getKey(), item.getValue())) {
        map.remove(item.getKey());
      }
    }
    return this;
  }

  @Override
  public K key(V value) {
    for (Entry<K, V> item : map.entrySet()) {
      if (item.getValue().equals(value)) {
        return item.getKey();
      }
    }
    return null;
  }

  @Override
  public RubyArrayList<K> keys() {
    return new RubyArrayList(map.keySet());
  }

  @Override
  public int length() {
    return map.size();
  }

  @Override
  public RubyLinkedHashMap<K, V> merge(Map<K, V> otherHash) {
    RubyLinkedHashMap<K, V> newHash = new RubyLinkedHashMap<>();
    for (Entry<K, V> item : map.entrySet()) {
      newHash.put(item);
    }
    for (Entry<K, V> item : otherHash.entrySet()) {
      newHash.put(item);
    }
    return newHash;
  }

  @Override
  public RubyLinkedHashMap<K, V> mergeEx(Map<K, V> otherHash) {
    for (Entry<K, V> item : otherHash.entrySet()) {
      map.put(item.getKey(), item.getValue());
    }
    return this;
  }

  @Override
  public RubyLinkedHashMap<K, V> update(Map<K, V> otherHash) {
    return mergeEx(otherHash);
  }

  @Override
  public RubyLinkedHashMap<K, V> merge(Map<K, V> otherHash, EntryMergeBlock<K, V> block) {
    RubyLinkedHashMap<K, V> newHash = new RubyLinkedHashMap<>();
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

  @Override
  public RubyLinkedHashMap<K, V> mergeǃ(Map<K, V> otherHash, EntryMergeBlock<K, V> block) {
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

  @Override
  public RubyLinkedHashMap<K, V> update(Map<K, V> otherHash, EntryMergeBlock<K, V> block) {
    return mergeǃ(otherHash, block);
  }

  @Override
  public Entry<K, V> rassoc(V value) {
    for (Entry<K, V> item : map.entrySet()) {
      if (item.getValue().equals(item)) {
        return item;
      }
    }
    return null;
  }

  @Override
  public RubyLinkedHashMap<K, V> rejectǃ(EntryBooleanBlock<K, V> block) {
    int beforeSize = map.size();
    deleteIf(block);
    if (map.size() == beforeSize) {
      return null;
    } else {
      return this;
    }
  }

  @Override
  public RubyLinkedHashMap<K, V> replace(Map<K, V> otherHash) {
    map.clear();
    map.putAll(otherHash);
    return this;
  }

  @Override
  public Entry<K, V> shift() {
    if (map.isEmpty()) {
      return null;
    } else {
      return map.entrySet().iterator().next();
    }
  }

  @Override
  public V store(K key, V value) {
    map.put(key, value);
    return value;
  }

  @Override
  public RubyArrayList<V> values() {
    return new RubyArrayList(map.values());
  }

  @Override
  public RubyArrayList<V> valuesAt(K... keys) {
    List<V> values = newArrayList();
    for (K key : keys) {
      values.add(map.get(key));
    }
    return new RubyArrayList(values);
  }

  // Ruby Enumerable methods
  @Override
  public boolean allʔ() {
    return true;
  }

  @Override
  public boolean allʔ(EntryBooleanBlock<K, V> block) {
    boolean bool = true;
    for (Entry<K, V> item : map.entrySet()) {
      if (block.yield(item.getKey(), item.getValue()) == false) {
        bool = false;
      }
    }
    return bool;
  }

  @Override
  public boolean anyʔ() {
    return RubyEnumerable.anyʔ(map.entrySet());
  }

  @Override
  public boolean anyʔ(EntryBooleanBlock<K, V> block) {
    boolean bool = false;
    for (Entry<K, V> item : map.entrySet()) {
      if (block.yield(item.getKey(), item.getValue())) {
        bool = true;
      }
    }
    return bool;
  }

  @Override
  public <S> RubyEnumerator<Entry<S, RubyArrayList<Entry<K, V>>>> chunk(EntryTransformBlock<K, V, S> block) {
    Multimap<S, Entry<K, V>> multimap = ArrayListMultimap.create();
    for (Entry<K, V> item : map.entrySet()) {
      S key = block.yield(item.getKey(), item.getValue());
      multimap.put(key, item);
    }
    RubyArrayList<Entry<S, RubyArrayList<Entry<K, V>>>> list = new RubyArrayList();
    for (S key : multimap.keySet()) {
      list.add(new SimpleEntry<>(key, new RubyArrayList(multimap.get(key))));
    }
    return new RubyEnumerator<>(list);
  }

  @Override
  public <S> RubyArrayList<S> collect(EntryTransformBlock<K, V, S> block) {
    List<S> list = newArrayList();
    for (Entry<K, V> item : map.entrySet()) {
      list.add(block.yield(item.getKey(), item.getValue()));
    }
    return new RubyArrayList(list);
  }

  @Override
  public RubyEnumerator<Entry<K, V>> collect() {
    return new RubyEnumerator(map.entrySet());
  }

  @Override
  public <S> RubyArrayList<S> collectConcat(EntryToListBlock<K, V, S> block) {
    List<S> list = newArrayList();
    for (Entry<K, V> item : map.entrySet()) {
      list.addAll(block.yield(item.getKey(), item.getValue()));
    }
    return new RubyArrayList(list);
  }

  @Override
  public RubyEnumerator<Entry<K, V>> collectConcat() {
    return RubyEnumerable.collectConcat(map.entrySet());
  }

  @Override
  public int count() {
    return RubyEnumerable.count(map.entrySet());
  }

  @Override
  public int count(EntryBooleanBlock<K, V> block) {
    int count = 0;
    for (Entry<K, V> item : map.entrySet()) {
      if (block.yield(item.getKey(), item.getValue())) {
        count++;
      }
    }
    return count;
  }

  @Override
  public void cycle(EntryBlock<K, V> block) {
    while (true) {
      for (Entry<K, V> item : map.entrySet()) {
        block.yield(item.getKey(), item.getValue());
      }
    }
  }

  @Override
  public void cycle(int cycles, EntryBlock<K, V> block) {
    for (int i = 0; i < cycles; i++) {
      for (Entry<K, V> item : map.entrySet()) {
        block.yield(item.getKey(), item.getValue());
      }
    }
  }

  @Override
  public Entry<K, V> detect(EntryBooleanBlock<K, V> block) {
    for (Entry<K, V> item : map.entrySet()) {
      if (block.yield(item.getKey(), item.getValue())) {
        return item;
      }
    }
    return null;
  }

  @Override
  public RubyEnumerator<Entry<K, V>> detect() {
    return RubyEnumerable.detect(map.entrySet());
  }

  @Override
  public RubyArray<Entry<K, V>> drop(int n) {
    return RubyEnumerable.drop(map.entrySet(), n);
  }

  @Override
  public RubyArrayList<Entry<K, V>> dropWhile(EntryBooleanBlock<K, V> block) {
    List<Entry<K, V>> list = newArrayList();
    boolean cutPoint = false;
    for (Entry<K, V> item : map.entrySet()) {
      if (block.yield(item.getKey(), item.getValue()) || cutPoint) {
        cutPoint = true;
        list.add(item);
      }
    }
    return new RubyArrayList(list);
  }

  @Override
  public RubyEnumerator<Entry<K, V>> dropWhile() {
    return RubyEnumerable.dropWhile(map.entrySet());
  }

  @Override
  public void eachCons(int n, ItemFromListBlock<Entry<K, V>> block) {
    RubyEnumerable.eachCons(map.entrySet(), n, block);
  }

  @Override
  public RubyEnumerator<RubyArray<Entry<K, V>>> eachCons(int n) {
    return RubyEnumerable.eachCons(map.entrySet(), n);
  }

  @Override
  public void eachSlice(int n, ItemFromListBlock<Entry<K, V>> block) {
    RubyEnumerable.eachSlice(map.entrySet(), n, block);
  }

  @Override
  public RubyEnumerator<RubyArray<Entry<K, V>>> eachSlice(int n) {
    return RubyEnumerable.eachSlice(map.entrySet(), n);
  }

  @Override
  public RubyArray<Entry<K, V>> eachWithIndex(ItemWithIndexBlock<Entry<K, V>> block) {
    return RubyEnumerable.eachWithIndex(map.entrySet(), block);
  }

  @Override
  public RubyEnumerator<Entry<Entry<K, V>, Integer>> eachWithIndex() {
    return RubyEnumerable.eachWithIndex(map.entrySet());
  }

  @Override
  public <S> S eachWithObject(S o, ItemWithObjectBlock<Entry<K, V>, S> block) {
    return RubyEnumerable.eachWithObject(map.entrySet(), o, block);
  }

  @Override
  public <S> RubyEnumerator<Entry<Entry<K, V>, S>> eachWithObject(S o) {
    return RubyEnumerable.eachWithObject(map.entrySet(), o);
  }

  @Override
  public RubyArray<Entry<K, V>> entries() {
    return RubyEnumerable.entries(map.entrySet());
  }

  @Override
  public Entry<K, V> find(EntryBooleanBlock<K, V> block) {
    return detect(block);
  }

  @Override
  public RubyEnumerator<Entry<K, V>> find() {
    return detect();
  }

  @Override
  public RubyArrayList<Entry<K, V>> findAll(EntryBooleanBlock<K, V> block) {
    List<Entry<K, V>> list = newArrayList();
    for (Entry<K, V> item : map.entrySet()) {
      if (block.yield(item.getKey(), item.getValue())) {
        list.add(item);
      }
    }
    return new RubyArrayList(list);
  }

  @Override
  public RubyEnumerator<Entry<K, V>> findAll() {
    return RubyEnumerable.findAll(map.entrySet());
  }

  @Override
  public Entry<K, V> first() {
    return RubyEnumerable.first(map.entrySet());
  }

  @Override
  public RubyArrayList<Entry<K, V>> first(int n) {
    return new RubyArrayList(RubyEnumerable.first(map.entrySet(), n));
  }

  @Override
  public Integer findIndex(Entry<K, V> target) {
    return RubyEnumerable.findIndex(map.entrySet(), target);
  }

  @Override
  public Integer findIndex(EntryBooleanBlock<K, V> block) {
    int index = 0;
    for (Entry<K, V> item : map.entrySet()) {
      if (block.yield(item.getKey(), item.getValue())) {
        return index;
      }
      index++;
    }
    return null;
  }

  @Override
  public RubyEnumerator<Entry<K, V>> findIndex() {
    return RubyEnumerable.findIndex(map.entrySet());
  }

  @Override
  public <S> RubyArrayList<S> flatMap(EntryToListBlock<K, V, S> block) {
    return collectConcat(block);
  }

  @Override
  public RubyEnumerator<Entry<K, V>> flatMap() {
    return collectConcat();
  }

  @Override
  public <S> RubyLinkedHashMap<S, RubyArrayList<Entry<K, V>>> groupBy(EntryTransformBlock<K, V, S> block) {
    Multimap<S, Entry<K, V>> multimap = ArrayListMultimap.create();
    for (Entry<K, V> item : map.entrySet()) {
      S key = block.yield(item.getKey(), item.getValue());
      multimap.put(key, item);
    }
    RubyLinkedHashMap<S, RubyArrayList<Entry<K, V>>> hash = new RubyLinkedHashMap();
    for (S key : multimap.keySet()) {
      hash.put(key, new RubyArrayList(multimap.get(key)));
    }
    return hash;
  }

  @Override
  public RubyEnumerator<Entry<K, V>> groupBy() {
    return RubyEnumerable.groupBy(map.entrySet());
  }

  @Override
  public boolean includeʔ(K key) {
    return map.containsKey(key);
  }

  @Override
  public boolean memberʔ(K key) {
    return map.containsKey(key);
  }

  @Override
  public Entry<K, V> inject(InjectBlock<Entry<K, V>> block) {
    return RubyEnumerable.inject(map.entrySet(), block);
  }

  @Override
  public <S> S inject(S init, EntryInjectWithInitBlock<K, V, S> block) {
    for (Entry<K, V> item : map.entrySet()) {
      init = block.yield(init, item);
    }
    return init;
  }

  @Override
  public <S> RubyArrayList<S> map(EntryTransformBlock<K, V, S> block) {
    return collect(block);
  }

  @Override
  public RubyEnumerator<Entry<K, V>> map() {
    return collect();
  }

  @Override
  public Entry<K, V> max(Comparator<? super K> comp) {
    K maxKey = RubyEnumerable.max(map.keySet(), comp);
    return find((k, v) -> {
      return k.equals(maxKey);
    });
  }

  @Override
  public <S> Entry<K, V> maxBy(Comparator<? super S> comp, EntryTransformBlock<K, V, S> block) {
    List<Entry<K, V>> src = newArrayList();
    List<S> dst = newArrayList();
    for (Entry<K, V> item : map.entrySet()) {
      src.add(item);
      dst.add(block.yield(item.getKey(), item.getValue()));
    }
    S maxDst = Collections.max(dst, comp);
    return src.get(dst.indexOf(maxDst));
  }

  @Override
  public RubyEnumerator<Entry<K, V>> maxBy() {
    return RubyEnumerable.maxBy(map.entrySet());
  }

  @Override
  public Entry<K, V> min(Comparator<? super K> comp) {
    K minKey = RubyEnumerable.min(map.keySet(), comp);
    return find((k, v) -> {
      return k.equals(minKey);
    });
  }

  @Override
  public <S> Entry<K, V> minBy(Comparator<? super S> comp, EntryTransformBlock<K, V, S> block) {
    List<Entry<K, V>> src = newArrayList();
    List<S> dst = newArrayList();
    for (Entry<K, V> item : map.entrySet()) {
      src.add(item);
      dst.add(block.yield(item.getKey(), item.getValue()));
    }
    S minDst = Collections.min(dst, comp);
    return src.get(dst.indexOf(minDst));
  }

  @Override
  public RubyEnumerator<Entry<K, V>> minBy() {
    return RubyEnumerable.minBy(map.entrySet());
  }

  @Override
  public RubyArrayList<Entry<K, V>> minmax(Comparator<? super K> comp) {
    K minKey = RubyEnumerable.min(map.keySet(), comp);
    K maxKey = RubyEnumerable.max(map.keySet(), comp);
    Entry<K, V> min = new SimpleEntry(minKey, map.get(minKey));
    Entry<K, V> max = new SimpleEntry(maxKey, map.get(maxKey));
    return new RubyArrayList(min, max);
  }

  @Override
  public <S> RubyArrayList<Entry<K, V>> minmaxBy(Comparator<? super S> comp, EntryTransformBlock<K, V, S> block) {
    List<Entry<K, V>> src = newArrayList();
    List<S> dst = newArrayList();
    for (Entry<K, V> item : map.entrySet()) {
      src.add(item);
      dst.add(block.yield(item.getKey(), item.getValue()));
    }
    S minDst = Collections.min(dst, comp);
    S maxDst = Collections.max(dst, comp);
    return new RubyArrayList(src.get(dst.indexOf(minDst)), src.get(dst.indexOf(maxDst)));
  }

  @Override
  public RubyEnumerator<Entry<K, V>> minmaxBy() {
    return RubyEnumerable.minmaxBy(map.entrySet());
  }

  @Override
  public RubyArray<RubyArray<Entry<K, V>>> partition(EntryBooleanBlock<K, V> block) {
    RubyArray<Entry<K, V>> trueList = new RubyArrayList();
    RubyArray<Entry<K, V>> falseList = new RubyArrayList();
    for (Entry<K, V> item : map.entrySet()) {
      if (block.yield(item.getKey(), item.getValue())) {
        trueList.add(item);
      } else {
        falseList.add(item);
      }
    }
    return new RubyArrayList(trueList, falseList);
  }

  @Override
  public RubyEnumerator<Entry<K, V>> partition() {
    return RubyEnumerable.partition(map.entrySet());
  }

  @Override
  public boolean noneʔ() {
    return RubyEnumerable.noneʔ(map.entrySet());
  }

  @Override
  public boolean noneʔ(EntryBooleanBlock<K, V> block) {
    boolean bool = true;
    for (Entry<K, V> item : map.entrySet()) {
      if (block.yield(item.getKey(), item.getValue())) {
        bool = false;
      }
    }
    return bool;
  }

  @Override
  public boolean oneʔ() {
    return RubyEnumerable.oneʔ(map.entrySet());
  }

  @Override
  public boolean oneʔ(EntryBooleanBlock<K, V> block) {
    int count = 0;
    for (Entry<K, V> item : map.entrySet()) {
      if (block.yield(item.getKey(), item.getValue())) {
        count++;
        if (count > 1) {
          return false;
        }
      }
    }
    return count == 1;
  }

  @Override
  public Entry<K, V> reduce(InjectBlock<Entry<K, V>> block) {
    return inject(block);
  }

  @Override
  public <S> S reduce(S init, EntryInjectWithInitBlock<K, V, S> block) {
    return inject(init, block);
  }

  @Override
  public RubyLinkedHashMap<K, V> reject(EntryBooleanBlock<K, V> block) {
    RubyLinkedHashMap<K, V> hash = new RubyLinkedHashMap();
    for (Entry<K, V> item : map.entrySet()) {
      if (block.yield(item.getKey(), item.getValue())) {
        hash.put(item);
      }
    }
    return hash;
  }

  @Override
  public RubyEnumerator<Entry<K, V>> reject() {
    return RubyEnumerable.reject(map.entrySet());
  }

  @Override
  public void reverseEach(EntryBlock block) {
    List<Entry<K, V>> reversedEntries = newArrayList();
    for (Entry<K, V> entry : map.entrySet()) {
      reversedEntries.add(0, entry);
    }
    for (Entry<K, V> entry : reversedEntries) {
      block.yield(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public RubyEnumerator<Entry<K, V>> reverseEach() {
    return RubyEnumerable.reverseEach(map.entrySet());
  }

  @Override
  public RubyArrayList<Entry<K, V>> select(EntryBooleanBlock<K, V> block) {
    return findAll(block);
  }

  @Override
  public RubyEnumerator<Entry<K, V>> select() {
    return findAll();
  }

  @Override
  public RubyEnumerator<RubyArray<Entry<K, V>>> sliceBefore(String regex) {
    return RubyEnumerable.sliceBefore(map.entrySet(), regex);
  }

  @Override
  public RubyEnumerator<RubyArray<Entry<K, V>>> sliceBefore(EntryBooleanBlock<K, V> block) {
    RubyArray<RubyArray<Entry<K, V>>> rubyArray = new RubyArrayList();
    RubyArray<Entry<K, V>> group = null;
    for (Entry<K, V> item : map.entrySet()) {
      if (group == null) {
        group = new RubyArrayList();
        group.add(item);
      } else if (block.yield(item.getKey(), item.getValue())) {
        rubyArray.add(group);
        group = new RubyArrayList();
        group.add(item);
      } else {
        group.add(item);
      }
    }
    if (group != null && !group.isEmpty()) {
      rubyArray.add(group);
    }
    return new RubyEnumerator(rubyArray);
  }

  @Override
  public RubyHash<K, V> sort(Comparator<? super K> comp) {
    List<K> sortedKeys = RubyEnumerable.sort(map.keySet(), comp);
    Map<K, V> sortedMap = newLinkedHashMap();
    for (K key : sortedKeys) {
      sortedMap.put(key, map.get(key));
    }
    return new RubyLinkedHashMap(sortedMap);
  }

  @Override
  public RubyHash<K, V> sort() {
    Object[] keys = newArrayList(keySet()).toArray();
    Arrays.sort(keys);
    Map<K, V> sortedMap = newLinkedHashMap();
    for (Object key : keys) {
      sortedMap.put((K) key, map.get(key));
    }
    return new RubyLinkedHashMap(sortedMap);
  }

  @Override
  public <S> RubyHash<K, V> sortBy(EntryTransformBlock<K, V, S> block) {
    RubyArray<Entry<S, K>> references = newRubyArray();
    for (Entry<K, V> item : map.entrySet()) {
      Entry<S, K> ref = new SimpleEntry<>(block.yield(item.getKey(), item.getValue()), item.getKey());
      references.add(ref);
    }
    references = references.sortBy((entry) -> {
      return entry.getKey();
    });

    Map<K, V> sortedMap = newLinkedHashMap();
    for (Entry<S, K> ref : references) {
      sortedMap.put(ref.getValue(), map.get(ref.getValue()));
    }
    return new RubyLinkedHashMap(sortedMap);
  }

  @Override
  public <S> RubyHash<K, V> sortBy(Comparator<? super S> comp, EntryTransformBlock<K, V, S> block) {
    List<Entry<S, K>> references = newArrayList();
    for (Entry<K, V> item : map.entrySet()) {
      Entry<S, K> ref = new SimpleEntry<>(block.yield(item.getKey(), item.getValue()), item.getKey());
      references.add(ref);
    }
    references = RubyEnumerable.sortBy(references, comp, (e) -> {
      return e.getKey();
    });

    Map<K, V> sortedMap = newLinkedHashMap();
    for (Entry<S, K> ref : references) {
      sortedMap.put(ref.getValue(), map.get(ref.getValue()));
    }
    return new RubyLinkedHashMap(sortedMap);
  }

  @Override
  public RubyEnumerator<Entry<K, V>> sortBy() {
    return RubyEnumerable.sortBy(map.entrySet());
  }

  @Override
  public RubyArray<Entry<K, V>> take(int n) {
    return new RubyArrayList(RubyEnumerable.take(map.entrySet(), n));
  }

  @Override
  public RubyArray<Entry<K, V>> takeWhile(EntryBooleanBlock block) {
    List<Entry<K, V>> list = newArrayList();
    for (Entry<K, V> item : map.entrySet()) {
      if (block.yield(item.getKey(), item.getValue())) {
        list.add(item);
      } else {
        return new RubyArrayList(list);
      }
    }
    return new RubyArrayList(list);
  }

  @Override
  public RubyEnumerator<Entry<K, V>> takeWhile() {
    return RubyEnumerable.takeWhile(map.entrySet());
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
    if (defaultValue != null && !map.containsKey(key)) {
      return defaultValue;
    }
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
