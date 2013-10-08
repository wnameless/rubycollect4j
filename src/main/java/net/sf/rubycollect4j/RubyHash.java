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

import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.newRubyEnumerator;
import static net.sf.rubycollect4j.RubyCollections.newRubyHash;

import java.util.AbstractMap.SimpleEntry;
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
import net.sf.rubycollect4j.util.LinkedIdentityMap;

/**
 * 
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

  private Map<K, V> map;
  private V defaultValue;

  @Override
  protected Iterable<Entry<K, V>> getIterable() {
    return map.entrySet();
  }

  /**
   * Creates a RubyHash.
   */
  public RubyHash() {
    map = new LinkedHashMap<K, V>();
  }

  /**
   * Creates a RubyHash by given LinkedHashMap.
   * 
   * @param map
   *          a LinkedHashMap
   * @throws NullPointerException
   *           if map is null
   */
  public RubyHash(LinkedHashMap<K, V> map) {
    if (map == null)
      throw new NullPointerException();

    this.map = map;
  }

  /**
   * Finds an Entry by given key.
   * 
   * @param key
   *          to be found
   * @return an Entry or null
   */
  public Entry<K, V> assoc(K key) {
    if (map.containsKey(key))
      return new SimpleEntry<K, V>(key, map.get(key));
    else
      return null;
  }

  /**
   * Turns this RubyHash to compare each elements by their identities instead of
   * their equalities.
   * 
   * @return this RubyHash
   */
  public RubyHash<K, V> compareByIdentity() {
    map = new LinkedIdentityMap<K, V>(map);
    return this;
  }

  /**
   * Returns whether this RubyHash is compared by identity.
   * 
   * @return true if this RubyHash is compared by identity, false otherwise
   */
  public boolean comparedByIdentityʔ() {
    return map instanceof LinkedIdentityMap;
  }

  /**
   * Deletes an Entry by given key and returns the deleted value.Returns null if
   * key is not found and default value is not set. Returns default value
   * otherwise.
   * 
   * @param key
   *          to be deleted
   * @return deleted value or null
   */
  public V delete(K key) {
    V removedItem = map.remove(key);
    if (removedItem == null && defaultValue != null)
      return defaultValue;
    else
      return removedItem;
  }

  /**
   * Returns a RubyEnumerator of entries this RubyHash.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<Entry<K, V>> deleteIf() {
    return newRubyEnumerator(map.entrySet());
  }

  /**
   * Deletes entries which the result are true returned by the block.
   * 
   * @param block
   *          to filter elements
   * @return this RubyHash
   */
  public RubyHash<K, V> deleteIf(EntryBooleanBlock<K, V> block) {
    Iterator<Entry<K, V>> iter = map.entrySet().iterator();
    while (iter.hasNext()) {
      Entry<K, V> item = iter.next();
      if (block.yield(item.getKey(), item.getValue()))
        iter.remove();
    }
    return this;
  }

  /**
   * Sets the default value of this RubyHash.
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
   * Returns the default value of this RubyHash.
   * 
   * @return default value
   */
  public V getDefault() {
    return defaultValue;
  }

  /**
   * Returns a RubyEnumerator of entries of this RubyHash.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<Entry<K, V>> each() {
    return newRubyEnumerator(map.entrySet());
  }

  /**
   * Yields each element to given block.
   * 
   * @param block
   *          to yield each entry
   * @return this RubyHash
   */
  public RubyHash<K, V> each(EntryBlock<K, V> block) {
    for (Entry<K, V> item : map.entrySet()) {
      block.yield(item.getKey(), item.getValue());
    }
    return this;
  }

  /**
   * Returns a RubyEnumerator of keys of this RubyHash.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<K> eachKey() {
    return newRubyEnumerator(map.keySet());
  }

  /**
   * Yields each key to given block.
   * 
   * @param block
   *          to yield each key
   * @return this RubyHash
   */
  public RubyHash<K, V> eachKey(Block<K> block) {
    for (K item : map.keySet()) {
      block.yield(item);
    }
    return this;
  }

  /**
   * Returns a RubyEnumerator of entries this RubyHash.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<Entry<K, V>> eachPair() {
    return newRubyEnumerator(map.entrySet());
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
   * Returns a RubyEnumerator of values of this RubyHash.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<V> eachValue() {
    return newRubyEnumerator(values());
  }

  /**
   * Yields each value to given block.
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
   * Checks if this RubyHash is empty.
   * 
   * @return true if this RubyHash is empty, false otherwise
   */
  public boolean emptyʔ() {
    return map.isEmpty();
  }

  /**
   * Equivalent to equals().
   * 
   * @param other
   *          any Object
   * @return true if 2 objects are equal, false otherwise
   */
  public boolean eqlʔ(Object other) {
    return map.equals(other);
  }

  /**
   * Fetchs the value of the key.
   * 
   * @param key
   *          to be found
   * @return value of the key
   * @throws NoSuchElementException
   *           if key is not found
   */
  public V fetch(K key) {
    if (!map.containsKey(key))
      throw new NoSuchElementException("key not found: " + key);

    return map.get(key);
  }

  /**
   * Fetchs the value of the key.
   * 
   * @param key
   *          to be found
   * @param defaultValue
   *          when key is not found
   * @return value of the key or defaultValue
   */
  public V fetch(K key, V defaultValue) {
    if (!map.containsKey(key))
      return defaultValue;

    return map.get(key);
  }

  /**
   * Returns a RubyEnumerator of entries of this RubyHash.
   * 
   * @return a RubyEnumerator
   */
  public RubyArray<Entry<K, V>> flatten() {
    return newRubyArray(map.entrySet());
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
   * Creates an inverted RubyHash which gets the reversed value-key entries
   * based on this RubyHash.
   * 
   * @return a new RubyHash
   */
  public RubyHash<V, K> invert() {
    RubyHash<V, K> invertHash = newRubyHash();
    for (Entry<K, V> item : map.entrySet()) {
      invertHash.put(item.getValue(), item.getKey());
    }
    return invertHash;
  }

  /**
   * Returns a RubyEnumerator of entries of this RubyHash.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<Entry<K, V>> keepIf() {
    return newRubyEnumerator(map.entrySet());
  }

  /**
   * Keeps elements which the result are true returned by the block.
   * 
   * @param block
   *          to filter elements
   * @return this RubyHash
   */
  public RubyHash<K, V> keepIf(EntryBooleanBlock<K, V> block) {
    Iterator<Entry<K, V>> iter = map.entrySet().iterator();
    while (iter.hasNext()) {
      Entry<K, V> item = iter.next();
      if (!block.yield(item.getKey(), item.getValue()))
        iter.remove();
    }
    return this;
  }

  /**
   * Finds the key related to given value.
   * 
   * @param value
   *          to be found
   * @return key of the given value or null
   */
  public K key(V value) {
    for (Entry<K, V> item : map.entrySet()) {
      if (value == null) {
        if (item.getValue() == null)
          return item.getKey();
      } else {
        if (value.equals(item.getValue()))
          return item.getKey();
      }
    }
    return null;
  }

  /**
   * Returns a RubyEnumerator of keys of this RubyHash.
   * 
   * @return a RubyEnumerator
   */
  public RubyArray<K> keys() {
    return newRubyArray(map.keySet());
  }

  /**
   * Equivalent to containsKey().
   * 
   * @param key
   *          to be found
   * @return true if key is found, false otherwise
   */
  public boolean keyʔ(K key) {
    return map.containsKey(key);
  }

  /**
   * Equivalent to size().
   * 
   * @return an int
   */
  public int length() {
    return map.size();
  }

  /**
   * Merges any Map with self and stores into a new RubyHash.
   * 
   * @param otherHash
   *          any Map
   * @return a new RubyHash
   */
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

  /**
   * Merges any Map with self and stores into a new RubyHash. Resolves the final
   * value of the key which is conflicted by given block.
   * 
   * @param otherHash
   *          any Map
   * @param block
   *          to determine which value is used when key is conflict
   * @return a new RubyHash
   */
  public RubyHash<K, V> merge(Map<K, V> otherHash, EntryMergeBlock<K, V> block) {
    RubyHash<K, V> newHash = newRubyHash(map);
    for (Entry<K, V> item : otherHash.entrySet()) {
      if (newHash.containsKey(item.getKey()))
        newHash
            .put(
                item.getKey(),
                block.yield(item.getKey(), map.get(item.getKey()),
                    item.getValue()));
      else
        newHash.put(item);
    }
    return newHash;
  }

  /**
   * Merges any Map into self.
   * 
   * @param otherHash
   *          any Map
   * @return this RubyHash
   */
  public RubyHash<K, V> mergeǃ(Map<K, V> otherHash) {
    for (Entry<K, V> item : otherHash.entrySet()) {
      map.put(item.getKey(), item.getValue());
    }
    return this;
  }

  /**
   * Merges any Map into self. Resolves the final value of the key which is
   * conflicted by given block.
   * 
   * @param otherHash
   *          any Map
   * @param block
   *          to determine which value is used when key is conflict
   * @return a new RubyHash
   */
  public RubyHash<K, V>
      mergeǃ(Map<K, V> otherHash, EntryMergeBlock<K, V> block) {
    for (Entry<K, V> item : otherHash.entrySet()) {
      if (containsKey(item.getKey()))
        map.put(item.getKey(),
            block.yield(item.getKey(), map.get(item.getKey()), item.getValue()));
      else
        put(item);
    }
    return this;
  }

  /**
   * Puts an Entry into this RubyHash directly.
   * 
   * @param entry
   *          an Entry
   * @return this RubyHash
   */
  public RubyHash<K, V> put(Entry<K, V> entry) {
    map.put(entry.getKey(), entry.getValue());
    return this;
  }

  /**
   * Puts entries into this RubyHash directly.
   * 
   * @param entries
   *          an array of entries
   * @return this RubyHash
   */
  public RubyHash<K, V> put(Entry<K, V>... entries) {
    for (Entry<K, V> e : entries) {
      map.put(e.getKey(), e.getValue());
    }
    return this;
  }

  /**
   * Finds an Entry by given value.
   * 
   * @param value
   *          to be found
   * @return an Entry or null
   */
  public Entry<K, V> rassoc(V value) {
    for (Entry<K, V> item : map.entrySet()) {
      if (value == null) {
        if (item.getValue() == null)
          return item;
      } else {
        if (value.equals(item.getValue()))
          return item;
      }
    }
    return null;
  }

  /**
   * Returns a RubyEnumerator of entries of this RubyHash.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<Entry<K, V>> rejectǃ() {
    return newRubyEnumerator(map.entrySet());
  }

  /**
   * Deletes entries which are true returned by the block. Returns null if
   * nothing is deleted.
   * 
   * @param block
   *          to filter elements
   * @return this RubyHash or null
   */
  public RubyHash<K, V> rejectǃ(EntryBooleanBlock<K, V> block) {
    int beforeSize = size();
    deleteIf(block);
    return map.size() == beforeSize ? null : this;
  }

  /**
   * Replaces all entries with the entries of given Map.
   * 
   * @param otherHash
   *          any Map
   * @return this RubyHash
   */
  public RubyHash<K, V> replace(Map<K, V> otherHash) {
    map.clear();
    map.putAll(otherHash);
    return this;
  }

  /**
   * Removes the first Entry and returns it.
   * 
   * @return an Entry
   */
  public Entry<K, V> shift() {
    if (map.isEmpty()) {
      return null;
    } else {
      Iterator<Entry<K, V>> iter = map.entrySet().iterator();
      Entry<K, V> first = iter.next();
      iter.remove();
      return first;
    }
  }

  /**
   * Stores a key-value pair and returns the value.
   * 
   * @param key
   *          of Entry
   * @param value
   *          of Entry
   * @return value
   */
  public V store(K key, V value) {
    map.put(key, value);
    return value;
  }

  /**
   * Returns this RubyHash.
   * 
   * @return this RubyHash
   */
  public RubyHash<K, V> toH() {
    return this;
  }

  /**
   * Returns this RubyHash.
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
    return map.toString();
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
  /**
   * Checks if any result returned by the block is false.
   * 
   * @param block
   *          to check elements
   * @return true if all result are true, false otherwise
   */
  public boolean allʔ(final EntryBooleanBlock<K, V> block) {
    return allʔ(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * Checks if any not-null object included.
   * 
   * @param block
   *          to check elements
   * @return true if not-null object is found, false otherwise
   */
  public boolean anyʔ(final EntryBooleanBlock<K, V> block) {
    return anyʔ(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * Chunks elements to entries. Keys of entries are the result returned by the
   * block. Values of entries are RubyArrays of elements which get the same
   * result returned by the block and aside to each other.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to chunk elements
   * @return a RubyEnumerator
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
   * Stores elements which are transformed by the block into a RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a RubyArray
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
   * Turns each element into a RubyArray and then flattens it.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to take element and generate a RubyArray
   * @return a RubyArray
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

  /**
   * Counts the elements which are true returned by the block.
   * 
   * @param block
   *          to define elements to be counted
   * @return a int
   */
  public int count(final EntryBooleanBlock<K, V> block) {
    return count(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * Generates a sequence from start element to end element and so on
   * infinitely.
   * 
   * @param block
   *          to yield each element
   */
  public void cycle(final EntryBlock<K, V> block) {
    cycle(new Block<Entry<K, V>>() {

      @Override
      public void yield(Entry<K, V> item) {
        block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * Generates a sequence from start element to end element, repeat n times.
   * Yields each element to the block.
   * 
   * @param n
   *          times to repeat
   * @param block
   *          to yield each element
   */
  public void cycle(int n, final EntryBlock<K, V> block) {
    cycle(n, new Block<Entry<K, V>>() {

      @Override
      public void yield(Entry<K, V> item) {
        block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * Finds the first element which gets true returned by the block. Returns null
   * if element is not found.
   * 
   * @param block
   *          to filter elements
   * @return an element or null
   */
  public Entry<K, V> detect(final EntryBooleanBlock<K, V> block) {
    return detect(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * Drops the first n elements until a element gets false returned by the
   * block.
   * 
   * @param block
   *          to define which elements to be dropped
   * @return a RubyArray
   */
  public RubyArray<Entry<K, V>> dropWhile(final EntryBooleanBlock<K, V> block) {
    return dropWhile(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * Yields each elements to the block.
   * 
   * @param block
   *          to yield each element
   * @return this RubyEnumerable
   */
  public RubyEnumerable<Entry<K, V>> eachEntry(final EntryBlock<K, V> block) {
    return eachEntry(new Block<Entry<K, V>>() {

      @Override
      public void yield(Entry<K, V> item) {
        block.yield(item.getKey(), item.getValue());
      }

    });

  }

  /**
   * Equivalent to detect().
   * 
   * @param block
   *          to filter elements
   * @return an element or null
   */
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

  /**
   * Stores elements which are true returned by the block into a RubyArray.
   * 
   * @param block
   *          to filter elements
   * @return a RubyArray
   */
  public Integer findIndex(final EntryBooleanBlock<K, V> block) {
    return findIndex(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * Equivalent to collectConcat().
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to take element and generate a RubyArray
   * @return a RubyArray
   */
  public <S> RubyArray<S> flatMap(
      final EntryTransformBlock<K, V, RubyArray<S>> block) {
    return collectConcat(block);
  }

  /**
   * Stores elements which are matched by regex transformed by the block into a
   * RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param regex
   *          regular expression
   * @param block
   *          to transform elements
   * @return a RubyArray
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
   * Puts elements with the same result S returned by the block into a
   * Entry&#60;S, RubyArray&#60;E&#62;&#62;y of a RubyHash.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to group each element
   * @return a RubyHash
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
   * Equivalent to collect().
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a RubyArray
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
   * Finds the element which is the max element induced by the Comparator
   * transformed by the block of this RubyEnumerable. Returns null if this
   * RubyEnumerable is empty.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param comp
   *          a Comparator
   * @param block
   *          to transform elements
   * @return an element or null
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
   * Finds the element which is the max element transformed by the block of this
   * RubyEnumerable. Returns null if this RubyEnumerable is empty.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return an element or null
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
   * Finds the element which is the min element induced by the Comparator
   * transformed by the block of this RubyEnumerable. Returns null if this
   * RubyEnumerable is empty.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param comp
   *          a Comparator
   * @param block
   *          to transform elements
   * @return an element or null
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
   * Finds the element which is the min element transformed by the block of this
   * RubyEnumerable. Returns null if this RubyEnumerable is empty.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return an element or null
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
   * Finds elements which are the min and max elements induced by the Comparator
   * transformed by the block of this RubyEnumerable and stores them into a
   * RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param comp
   *          a Comparator
   * @param block
   *          to transform elements
   * @return a RubyArray
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
   * Finds elements which is the min and max elements transformed by the block
   * of this RubyEnumerable and stores them into a RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a RubyArray
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

  /**
   * Checks if this RubyEnumerable contains only elements which are false
   * returned by the block.
   * 
   * @param block
   *          to check elements
   * @return true if all results of block are false, false otherwise
   */
  public boolean noneʔ(final EntryBooleanBlock<K, V> block) {
    return noneʔ(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * Checks if this RubyEnumerable contains only one element which are true
   * returned by the block.
   * 
   * @param block
   *          to check elements
   * @return true if only one result of block is true, false otherwise
   */
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

  /**
   * Divides elements into 2 groups by the given block.
   * 
   * @param block
   *          to part elements
   * @return a RubyArray of 2 RubyArrays
   */
  public RubyArray<Entry<K, V>> reject(final EntryBooleanBlock<K, V> block) {
    return reject(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * Iterates each element reversed by given block.
   * 
   * @param block
   *          to yield each element
   * @return this RubyEnumerable
   */
  public RubyEnumerable<Entry<K, V>> reverseEach(final EntryBlock<K, V> block) {
    return reverseEach(new Block<Entry<K, V>>() {

      @Override
      public void yield(Entry<K, V> item) {
        block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * Equivalent to findAll().
   * 
   * @param block
   *          to filter elements
   * @return a RubyArray
   */
  public RubyArray<Entry<K, V>> select(final EntryBooleanBlock<K, V> block) {
    return findAll(block);
  }

  /**
   * Groups elements into RubyArrays and the first element of each RubyArray
   * should get true returned by the block.
   * 
   * @param block
   *          to check where to do slice
   * @return a RubyEnumerator
   */
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
   * Sorts elements of this RubyEnumerable by the ordering of elements
   * transformed by the block induced by the Comparator and stores them into a
   * RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param comp
   *          a Comparator
   * @param block
   *          to transform elements
   * @return a RubyArray
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
   * Sorts elements of this RubyEnumerable by the ordering of elements
   * transformed by the block and stores them into a RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a RubyArray
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

  /**
   * Stores element into a RubyArray from beginning until the result returned by
   * the block is false.
   * 
   * @param block
   *          to filter elements
   * @return a RubyArray
   */
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

  /**
   * Finds all the values by given keys.
   * 
   * @param keys
   *          to access values
   * @return a RubyArray
   */
  public RubyArray<V> valuesAt(K... keys) {
    RubyArray<V> values = newRubyArray();
    for (K key : keys) {
      values.add(map.get(key));
    }
    return values;
  }

  /**
   * Check if the value included.
   * 
   * @param value
   *          to be checked
   * @return true if value included, false otherwise
   */
  public boolean valueʔ(V value) {
    return map.containsValue(value);
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
