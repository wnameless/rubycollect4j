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

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import net.sf.rubycollect4j.block.Block;
import net.sf.rubycollect4j.block.BooleanBlock;
import net.sf.rubycollect4j.block.EntryBlock;
import net.sf.rubycollect4j.block.EntryBooleanBlock;
import net.sf.rubycollect4j.block.EntryMergeBlock;
import net.sf.rubycollect4j.block.EntryTransformBlock;
import net.sf.rubycollect4j.block.TransformBlock;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static net.sf.rubycollect4j.RubyArray.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.newPair;
import static net.sf.rubycollect4j.RubyEnumerator.newRubyEnumerator;

/**
 * RubyHash implements all methods refer to the Hash of Ruby language. RubyHash
 * is also a Java Map.
 * 
 * @param <K>
 *          the type of the key elements
 * @param <V>
 *          the type of the value elements
 */
public final class RubyHash<K, V> extends RubyEnumerable<Entry<K, V>> implements
    Map<K, V> {

  private final LinkedHashMap<K, V> map;
  private V defaultValue;

  /**
   * Build up an empty RubyHash.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @return a new RubyHash
   */
  public static <K, V> RubyHash<K, V> newRubyHash() {
    LinkedHashMap<K, V> linkedHashMap = newLinkedHashMap();
    return new RubyHash<K, V>(linkedHashMap);
  }

  /**
   * Build up an empty RubyHash by given Map.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @param map
   *          a Map
   * @return a new RubyHash
   */
  public static <K, V> RubyHash<K, V> newRubyHash(Map<K, V> map) {
    LinkedHashMap<K, V> linkedHashMap = newLinkedHashMap(map);
    return new RubyHash<K, V>(linkedHashMap);
  }

  /**
   * Build up an empty RubyHash by given LinkedHashMap.
   * 
   * @param <K>
   *          the type of the key elements
   * @param <V>
   *          the type of the value elements
   * @param map
   *          a Map
   * @param defensiveCopy
   *          it makes a defensive copy if specified
   * @return a new RubyHash
   */
  public static <K, V> RubyHash<K, V> newRubyHash(LinkedHashMap<K, V> map,
      boolean defensiveCopy) {
    if (defensiveCopy) {
      LinkedHashMap<K, V> linkedHashMap = newLinkedHashMap(map);
      return new RubyHash<K, V>(linkedHashMap);
    }
    return new RubyHash<K, V>(map);
  }

  /**
   * Private constructor to enhance static factory methods and prevent from
   * inheritance.
   * 
   * @param map
   *          a Map
   */
  private RubyHash(LinkedHashMap<K, V> map) {
    super(map.entrySet());
    this.map = map;
  }

  /**
   * Find an Entry by given key.
   * 
   * @param key
   *          to be found
   * @return an Entry or null
   */
  public Entry<K, V> assoc(K key) {
    if (map.containsKey(key)) {
      return newPair(key, map.get(key));
    } else {
      return null;
    }
  }

  /**
   * Not supported yet!
   * 
   * @return a RubyHash
   * @throws UnsupportedOperationException
   */
  public RubyHash<K, V> compareByIdentity() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * Return false because compareByIdentity() is not supported yet.
   * 
   * @return false
   */
  public boolean comparedByIdentityʔ() {
    return false;
  }

  /**
   * Delete an Entry by given key and return the deleted value. If key is not
   * found and default value is not set, return null, default value otherwise.
   * 
   * @param key
   *          to be deleted
   * @return deleted value or null
   */
  public V delete(K key) {
    V removedItem = remove(key);
    if (removedItem == null && defaultValue != null) {
      return defaultValue;
    } else {
      return removedItem;
    }
  }

  /**
   * Return a RubyEnumerator of entries this RubyHash.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<Entry<K, V>> deleteIf() {
    return newRubyEnumerator(entrySet());
  }

  /**
   * Delete entries which the result returned by the block are true.
   * 
   * @param block
   *          to filter elements
   * @return this RubyHash
   */
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

  /**
   * Set the default value of this RubyHash.
   * 
   * @param defaultValue
   *          default value if key is not found
   * @return default value
   */
  public V setDefault(V defaultValue) {
    this.defaultValue = defaultValue;
    return this.defaultValue;
  }

  /**
   * Return the default value of this RubyHash.
   * 
   * @return default value
   */
  public V getDefault() {
    return defaultValue;
  }

  /**
   * Return a RubyEnumerator of entries of this RubyHash.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<Entry<K, V>> each() {
    return newRubyEnumerator(entrySet());
  }

  /**
   * Yield each element to given block.
   * 
   * @param block
   *          to yield each entry
   * @return this RubyHash
   */
  public RubyHash<K, V> each(EntryBlock<K, V> block) {
    for (Entry<K, V> item : entrySet()) {
      block.yield(item.getKey(), item.getValue());
    }
    return this;
  }

  /**
   * Return a RubyEnumerator of keys of this RubyHash.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<K> eachKey() {
    return newRubyEnumerator(keySet());
  }

  /**
   * Yield each key to given block.
   * 
   * @param block
   *          to yield each key
   * @return this RubyHash
   */
  public RubyHash<K, V> eachKey(Block<K> block) {
    for (K item : keySet()) {
      block.yield(item);
    }
    return this;
  }

  /**
   * Return a RubyEnumerator of entries this RubyHash.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<Entry<K, V>> eachPair() {
    return newRubyEnumerator(entrySet());
  }

  /**
   * Equivalent to each().
   * 
   * @param block
   *          to yield each entry
   * @return this RubyHash
   */
  public RubyHash<K, V> eachPair(EntryBlock<K, V> block) {
    return each(block);
  }

  /**
   * Return a RubyEnumerator of values of this RubyHash.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<V> eachValue() {
    return newRubyEnumerator(values());
  }

  /**
   * Yield each value to given block.
   * 
   * @param block
   *          to yield each value
   * @return this RubyHash
   */
  public RubyHash<K, V> eachValue(Block<V> block) {
    for (V item : values()) {
      block.yield(item);
    }
    return this;
  }

  /**
   * Check if this RubyHash is empty.
   * 
   * @return true if this RubyHash is empty, false otherwise
   */
  public boolean emptyʔ() {
    return isEmpty();
  }

  /**
   * Equivalent to equals().
   * 
   * @param other
   *          any Object
   * @return true if 2 objects are equaled, false otherwise
   */
  public boolean eqlʔ(Object other) {
    return equals(other);
  }

  /**
   * Fetch the value of the key.
   * 
   * @param key
   *          to be found
   * @return value of the key
   * @throws NoSuchElementException
   *           if key is not found
   */
  public V fetch(K key) {
    if (!containsKey(key)) {
      throw new NoSuchElementException("key not found: " + key);
    }
    return get(key);
  }

  /**
   * Fetch the value of the key.
   * 
   * @param key
   *          to be found
   * @param defaultValue
   *          when key is not found
   * @return value of the key or defaultValue
   */
  public V fetch(K key, V defaultValue) {
    if (!containsKey(key)) {
      return defaultValue;
    }
    return get(key);
  }

  /**
   * Return a RubyEnumerator of entries of this RubyHash.
   * 
   * @return a RubyEnumerator
   */
  public RubyArray<Entry<K, V>> flatten() {
    return newRubyArray(entrySet());
  }

  /**
   * Equivalent to hashCode().
   * 
   * @return an int
   */
  public int hash() {
    return hashCode();
  }

  /**
   * Equivalent to toString().
   * 
   * @return a String
   */
  public String inspect() {
    return toString();
  }

  /**
   * Create an inverted RubyHash which gets the reversed value-key entries based
   * on this RubyHash.
   * 
   * @return a new RubyHash
   */
  public RubyHash<V, K> invert() {
    RubyHash<V, K> invertHash = newRubyHash();
    for (Entry<K, V> item : entrySet()) {
      invertHash.put(item.getValue(), item.getKey());
    }
    return invertHash;
  }

  /**
   * Return a RubyEnumerator of entries of this RubyHash.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<Entry<K, V>> keepIf() {
    return newRubyEnumerator(entrySet());
  }

  /**
   * Keep elements which the result returned by the block are false.
   * 
   * @param block
   *          to filter elements
   * @return this RubyHash
   */
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

  /**
   * Find the key related to given value.
   * 
   * @param value
   *          to be found
   * @return key of the given value or null
   */
  public K key(V value) {
    for (Entry<K, V> item : entrySet()) {
      if (item.getValue().equals(value)) {
        return item.getKey();
      }
    }
    return null;
  }

  /**
   * Return a RubyEnumerator of keys of this RubyHash.
   * 
   * @return a RubyEnumerator
   */
  public RubyArray<K> keys() {
    return newRubyArray(keySet());
  }

  /**
   * Equivalent to containsKey().
   * 
   * @param key
   *          to be found
   * @return true if key is found, false otherwise
   */
  public boolean keyʔ(K key) {
    return containsKey(key);
  }

  /**
   * Equivalent to size().
   * 
   * @return an int
   */
  public int length() {
    return size();
  }

  /**
   * Merge any Map with self and store into a new RubyHash.
   * 
   * @param otherHash
   *          any Map
   * @return a new RubyHash
   */
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

  /**
   * Merge any Map with self and store into a new RubyHash. Resolve the key
   * conflicts by given block.
   * 
   * @param otherHash
   *          any Map
   * @param block
   *          to determine which value is used when key is conflict
   * @return a new RubyHash
   */
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

  /**
   * Merge any Map within self.
   * 
   * @param otherHash
   *          any Map
   * @return this RubyHash
   */
  public RubyHash<K, V> mergeǃ(Map<K, V> otherHash) {
    for (Entry<K, V> item : otherHash.entrySet()) {
      put(item.getKey(), item.getValue());
    }
    return this;
  }

  /**
   * Merge any Map within self. Resolve the key conflicts by given block.
   * 
   * @param otherHash
   *          any Map
   * @param block
   *          to determine which value is used when key is conflict
   * @return a new RubyHash
   */
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

  /**
   * Put an entry into this RubyHash directly.
   * 
   * @param entry
   *          an Entry
   * @return this RubyHash
   */
  public RubyHash<K, V> put(Entry<K, V> entry) {
    put(entry.getKey(), entry.getValue());
    return this;
  }

  /**
   * Put an entries into this RubyHash directly.
   * 
   * @param entry
   *          an Entry
   * @param entries
   *          array of entries
   * @return this RubyHash
   */
  public RubyHash<K, V> put(Entry<K, V> entry, Entry<K, V>... entries) {
    put(entry.getKey(), entry.getValue());
    for (Entry<K, V> e : entries) {
      put(e.getKey(), e.getValue());
    }
    return this;
  }

  /**
   * Find an Entry by given value.
   * 
   * @param value
   *          to be found
   * @return an Entry or null
   */
  public Entry<K, V> rassoc(V value) {
    for (Entry<K, V> item : entrySet()) {
      if (item.getValue().equals(value)) {
        return item;
      }
    }
    return null;
  }

  /**
   * Return a RubyEnumerator of entries of this RubyHash.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<Entry<K, V>> rejectǃ() {
    return newRubyEnumerator(entrySet());
  }

  /**
   * Delete entries which the result returned by the block are true. Return null
   * if nothing is deleted.
   * 
   * @param block
   *          to filter elements
   * @return this RubyHash or null
   */
  public RubyHash<K, V> rejectǃ(EntryBooleanBlock<K, V> block) {
    int beforeSize = size();
    deleteIf(block);
    if (size() == beforeSize) {
      return null;
    } else {
      return this;
    }
  }

  /**
   * Replace all entries with the entries of given Map.
   * 
   * @param otherHash
   *          any Map
   * @return this RubyHash
   */
  public RubyHash<K, V> replace(Map<K, V> otherHash) {
    clear();
    putAll(otherHash);
    return this;
  }

  /**
   * Remove the first Entry and return it.
   * 
   * @return an Entry
   */
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

  /**
   * Store a key-value pair and return the value.
   * 
   * @param key
   *          of Entry
   * @param value
   *          of Entry
   * @return value
   */
  public V store(K key, V value) {
    put(key, value);
    return value;
  }

  /**
   * Return this RubyHash.
   * 
   * @return this RubyHash
   */
  public RubyHash<K, V> toH() {
    return this;
  }

  /**
   * Return this RubyHash.
   * 
   * @return this RubyHash
   */
  public RubyHash<K, V> toHash() {
    return this;
  }

  /**
   * Equivalent to toString().
   * 
   * @return a String
   */
  public String toS() {
    return toString();
  }

  /**
   * Equivalent to mergeǃ().
   * 
   * @param otherHash
   *          any Map
   * @return this RubyHash
   */
  public RubyHash<K, V> update(Map<K, V> otherHash) {
    return mergeǃ(otherHash);
  }

  /**
   * Equivalent to mergeǃ().
   * 
   * @param otherHash
   *          any Map
   * @param block
   *          to determine which value is used when key is conflict
   * @return a new RubyHash
   */
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

  /**
   * @param <S>
   *          the type of transformed elements
   */
  public <S> RubyEnumerator<Entry<S, RubyArray<Entry<K, V>>>> chunk(
      final EntryTransformBlock<K, V, S> block) {
    return chunk(new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * @param <S>
   *          the type of transformed elements
   */
  public <S> RubyArray<S> collect(final EntryTransformBlock<K, V, S> block) {
    return collect(new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * @param <S>
   *          the type of transformed elements
   */
  public <S> RubyArray<S> collectConcat(
      final EntryTransformBlock<K, V, RubyArray<S>> block) {
    return collectConcat(new TransformBlock<Entry<K, V>, RubyArray<S>>() {

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

  public void cycle(final EntryBlock<K, V> block) {
    cycle(new Block<Entry<K, V>>() {

      @Override
      public void yield(Entry<K, V> item) {
        block.yield(item.getKey(), item.getValue());
      }

    });
  }

  public void cycle(int n, final EntryBlock<K, V> block) {
    cycle(n, new Block<Entry<K, V>>() {

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
    return eachEntry(new Block<Entry<K, V>>() {

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

  /**
   * @param <S>
   *          the type of transformed elements
   */
  public <S> RubyArray<S> flatMap(
      final EntryTransformBlock<K, V, RubyArray<S>> block) {
    return collectConcat(block);
  }

  /**
   * @param <S>
   *          the type of transformed elements
   */
  public <S> RubyArray<S> grep(String regex,
      final EntryTransformBlock<K, V, S> block) {
    return grep(regex, new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * @param <S>
   *          the type of transformed elements
   */
  public <S> RubyHash<S, RubyArray<Entry<K, V>>> groupBy(
      final EntryTransformBlock<K, V, S> block) {
    return groupBy(new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * @param <S>
   *          the type of transformed elements
   */
  public <S> RubyArray<S> map(final EntryTransformBlock<K, V, S> block) {
    return map(new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * @param <S>
   *          the type of transformed elements
   */
  public <S> Entry<K, V> maxBy(Comparator<? super S> comp,
      final EntryTransformBlock<K, V, S> block) {
    return maxBy(comp, new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * @param <S>
   *          the type of transformed elements
   */
  public <S> Entry<K, V> maxBy(final EntryTransformBlock<K, V, S> block) {
    return maxBy(new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * @param <S>
   *          the type of transformed elements
   */
  public <S> Entry<K, V> minBy(Comparator<? super S> comp,
      final EntryTransformBlock<K, V, S> block) {
    return minBy(comp, new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * @param <S>
   *          the type of transformed elements
   */
  public <S> Entry<K, V> minBy(final EntryTransformBlock<K, V, S> block) {
    return minBy(new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * @param <S>
   *          the type of transformed elements
   */
  public <S> RubyArray<Entry<K, V>> minmaxBy(Comparator<? super S> comp,
      final EntryTransformBlock<K, V, S> block) {
    return minmaxBy(comp, new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * @param <S>
   *          the type of transformed elements
   */
  public <S> RubyArray<Entry<K, V>> minmaxBy(
      final EntryTransformBlock<K, V, S> block) {
    return minmaxBy(new TransformBlock<Entry<K, V>, S>() {

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
    return reverseEach(new Block<Entry<K, V>>() {

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

  /**
   * @param <S>
   *          the type of transformed elements
   */
  public <S> RubyArray<Entry<K, V>> sortBy(Comparator<? super S> comp,
      final EntryTransformBlock<K, V, S> block) {
    return sortBy(comp, new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * @param <S>
   *          the type of transformed elements
   */
  public <S> RubyArray<Entry<K, V>> sortBy(
      final EntryTransformBlock<K, V, S> block) {
    return sortBy(new TransformBlock<Entry<K, V>, S>() {

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
