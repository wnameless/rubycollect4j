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

import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.newRubyEnumerator;
import static net.sf.rubycollect4j.RubyCollections.newRubyHash;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
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
import net.sf.rubycollect4j.iter.ComparableEntryIterable;
import net.sf.rubycollect4j.util.ComparableEntry;
import net.sf.rubycollect4j.util.LinkedIdentityMap;

/**
 * 
 * {@link RubyHash} implements all methods refer to the Hash class of Ruby
 * language.
 * <p>
 * {@link RubyHash} is also a Java Map and a {@link Ruby.Enumerable}.
 * 
 * @param <K>
 *          the type of the key elements
 * @param <V>
 *          the type of the value elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class RubyHash<K, V> extends RubyEnumerable<Entry<K, V>>
    implements Map<K, V>, Serializable {

  private static final long serialVersionUID = 1L;

  private Map<K, V> map;
  private V defaultValue;
  private boolean isFrozen = false;

  /**
   * Returns a {@link RubyHash} which wraps the given Map.
   * 
   * @param map
   *          any Map
   * @return {@link RubyHash}
   * @throws NullPointerException
   *           if map is null
   */
  public static <K, V> RubyHash<K, V> of(LinkedHashMap<K, V> map) {
    if (map == null) throw new NullPointerException();

    return new RubyHash<K, V>(map);
  }

  /**
   * Returns a {@link RubyHash} which copies the elements of given Map.
   * 
   * @param map
   *          any Map
   * @return {@link RubyHash}
   * @throws NullPointerException
   *           if map is null
   */
  public static <K, V> RubyHash<K, V> copyOf(Map<K, V> map) {
    if (map == null) throw new NullPointerException();

    return new RubyHash<K, V>(map);
  }

  @Override
  protected Iterable<Entry<K, V>> getIterable() {
    return new ComparableEntryIterable<K, V>(map.entrySet());
  }

  /**
   * Creates a {@link RubyHash}.
   */
  public RubyHash() {
    map = new LinkedHashMap<K, V>();
  }

  /**
   * Creates a {@link RubyHash} by given LinkedHashMap. It's a wrapper
   * implementation. No defensive copy has been made.
   * 
   * @param map
   *          a LinkedHashMap
   * @throws NullPointerException
   *           if map is null
   */
  public RubyHash(LinkedHashMap<K, V> map) {
    if (map == null) throw new NullPointerException();

    this.map = map;
  }

  /**
   * Creates a {@link RubyHash} by given Map.
   * 
   * @param map
   *          any Map
   * @throws NullPointerException
   *           if map is null
   */
  public RubyHash(Map<K, V> map) {
    if (map == null) throw new NullPointerException();

    this.map = new LinkedHashMap<K, V>(map);
  }

  /**
   * Finds an Entry by given key.
   * 
   * @param key
   *          to be found
   * @return Entry or null
   */
  public Entry<K, V> assoc(K key) {
    if (map.containsKey(key))
      return new ComparableEntry<K, V>(key, map.get(key));
    else
      return null;
  }

  /**
   * Turns this {@link RubyHash} to compare each elements by their identities
   * instead of their equalities.
   * 
   * @return this {@link RubyHash}
   */
  public RubyHash<K, V> compareByIdentity() {
    map = new LinkedIdentityMap<K, V>(map);
    return this;
  }

  /**
   * Returns whether this {@link RubyHash} is compared by identity.
   * 
   * @return true if this {@link RubyHash} is compared by identity, false
   *         otherwise
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
   * Returns a {@link RubyEnumerator} of entries this {@link RubyHash}.
   * 
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<Entry<K, V>> deleteIf() {
    return newRubyEnumerator(new ComparableEntryIterable<K, V>(map.entrySet()));
  }

  /**
   * Deletes entries which the result are true returned by the block.
   * 
   * @param block
   *          to filter elements
   * @return this {@link RubyHash}
   */
  public RubyHash<K, V> deleteIf(
      EntryBooleanBlock<? super K, ? super V> block) {
    Iterator<Entry<K, V>> iter = map.entrySet().iterator();
    while (iter.hasNext()) {
      Entry<K, V> item = iter.next();
      if (block.yield(item.getKey(), item.getValue())) iter.remove();
    }
    return this;
  }

  /**
   * Sets the default value of this {@link RubyHash}.
   * 
   * @param defaultValue
   *          default value if key is not found
   * @return default value
   */
  public V setDefault(V defaultValue) {
    this.defaultValue = defaultValue;
    return defaultValue;
  }

  /**
   * Returns the default value of this {@link RubyHash}.
   * 
   * @return default value
   */
  public V getDefault() {
    return defaultValue;
  }

  /**
   * @return this {@link RubyHash}
   * @see RubyEnumerable#each(Block)
   */
  public RubyHash<K, V> each(EntryBlock<? super K, ? super V> block) {
    for (Entry<K, V> item : map.entrySet()) {
      block.yield(item.getKey(), item.getValue());
    }
    return this;
  }

  /**
   * Returns a {@link RubyEnumerator} of keys of this {@link RubyHash}.
   * 
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<K> eachKey() {
    return newRubyEnumerator(map.keySet());
  }

  /**
   * Yields each key to given block.
   * 
   * @param block
   *          to yield each key
   * @return this {@link RubyHash}
   */
  public RubyHash<K, V> eachKey(Block<? super K> block) {
    for (K item : map.keySet()) {
      block.yield(item);
    }
    return this;
  }

  /**
   * Returns a {@link RubyEnumerator} of entries this {@link RubyHash}.
   * 
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<Entry<K, V>> eachPair() {
    return newRubyEnumerator(new ComparableEntryIterable<K, V>(map.entrySet()));
  }

  /**
   * Equivalent to {@link #each(EntryBlock)}.
   * 
   * @param block
   *          to yield each entry
   * @return this {@link RubyHash}
   */
  public RubyHash<K, V> eachPair(EntryBlock<? super K, ? super V> block) {
    return each(block);
  }

  /**
   * Returns a {@link RubyEnumerator} of values of this {@link RubyHash}.
   * 
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<V> eachValue() {
    return newRubyEnumerator(values());
  }

  /**
   * Yields each value to given block.
   * 
   * @param block
   *          to yield each value
   * @return this {@link RubyHash}
   */
  public RubyHash<K, V> eachValue(Block<? super V> block) {
    for (V item : values()) {
      block.yield(item);
    }
    return this;
  }

  /**
   * Checks if this {@link RubyHash} is empty.
   * 
   * @return true if this {@link RubyHash} is empty, false otherwise
   */
  public boolean emptyʔ() {
    return map.isEmpty();
  }

  /**
   * Equivalent to {@link #equals(Object)}.
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
      throw new NoSuchElementException("KeyError: key not found: " + key);

    return map.get(key);
  }

  /**
   * Fetches the value of the key.
   * 
   * @param key
   *          to be found
   * @param defaultValue
   *          when key is not found
   * @return value of the key or defaultValue
   */
  public V fetch(K key, V defaultValue) {
    if (!map.containsKey(key)) return defaultValue;

    return map.get(key);
  }

  /**
   * Returns a {@link RubyEnumerator} of entries of this {@link RubyHash}.
   * 
   * @return {@link RubyEnumerator}
   */
  public RubyArray<Entry<K, V>> flatten() {
    return newRubyArray(new ComparableEntryIterable<K, V>(map.entrySet()));
  }

  /**
   * Freezes this {@link RubyHash}.
   * 
   * @return this {@link RubyHash}
   */
  public RubyHash<K, V> freeze() {
    if (!isFrozen) {
      map = Collections.unmodifiableMap(map);
      isFrozen = true;
    }
    return this;
  }

  /**
   * Checks if this {@link RubyHash} is frozen.
   * 
   * @return true if this {@link RubyHash} is frozen, false otherwise
   */
  public boolean frozenʔ() {
    return isFrozen;
  }

  /**
   * Equivalent to {@link #hashCode()}.
   * 
   * @return the hash code
   */
  public int hash() {
    return hashCode();
  }

  /**
   * Equivalent to {@link #toString()}.
   * 
   * @return String
   */
  public String inspect() {
    return toString();
  }

  /**
   * Creates an inverted {@link RubyHash} which gets the reversed value-key
   * entries based on this {@link RubyHash}.
   * 
   * @return new {@link RubyHash}
   */
  public RubyHash<V, K> invert() {
    RubyHash<V, K> invertHash = newRubyHash();
    for (Entry<K, V> item : map.entrySet()) {
      invertHash.put(item.getValue(), item.getKey());
    }
    return invertHash;
  }

  /**
   * Returns a {@link RubyEnumerator} of entries of this {@link RubyHash}.
   * 
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<Entry<K, V>> keepIf() {
    return newRubyEnumerator(new ComparableEntryIterable<K, V>(map.entrySet()));
  }

  /**
   * Keeps elements which the result are true returned by the block.
   * 
   * @param block
   *          to filter elements
   * @return this {@link RubyHash}
   */
  public RubyHash<K, V> keepIf(EntryBooleanBlock<? super K, ? super V> block) {
    Iterator<Entry<K, V>> iter = map.entrySet().iterator();
    while (iter.hasNext()) {
      Entry<K, V> item = iter.next();
      if (!block.yield(item.getKey(), item.getValue())) iter.remove();
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
      if (value == null ? item.getValue() == null
          : value.equals(item.getValue()))
        return item.getKey();
    }
    return null;
  }

  /**
   * Returns a {@link RubyArray} of keys of this {@link RubyHash}.
   * 
   * @return {@link RubyArray}
   */
  public RubyArray<K> keys() {
    return newRubyArray(map.keySet());
  }

  /**
   * Equivalent to {@link #containsKey(Object)}.
   * 
   * @param key
   *          to be found
   * @return true if key is found, false otherwise
   */
  public boolean keyʔ(K key) {
    return map.containsKey(key);
  }

  /**
   * Equivalent to {@link #size()}.
   * 
   * @return size of this {@link RubyHash}
   */
  public int length() {
    return map.size();
  }

  /**
   * Merges any Map with self and puts into a new {@link RubyHash}.
   * 
   * @param otherHash
   *          any Map
   * @return new {@link RubyHash}
   */
  public RubyHash<K, V> merge(Map<? extends K, ? extends V> otherHash) {
    RubyHash<K, V> rubyHash = newRubyHash();
    for (Entry<K, V> item : map.entrySet()) {
      rubyHash.put(item);
    }
    for (Entry<? extends K, ? extends V> item : otherHash.entrySet()) {
      rubyHash.put(item);
    }
    return rubyHash;
  }

  /**
   * Merges any Map with self and puts into a new {@link RubyHash}. Resolves the
   * final value of the key which is conflicted by given block.
   * 
   * @param otherHash
   *          any Map
   * @param block
   *          to determine which value is used when key is conflict
   * @return new {@link RubyHash}
   */
  public RubyHash<K, V> merge(Map<K, V> otherHash,
      EntryMergeBlock<? super K, V> block) {
    RubyHash<K, V> rubyHash = newRubyHash(map);
    for (Entry<K, V> item : otherHash.entrySet()) {
      if (rubyHash.containsKey(item.getKey()))
        rubyHash.put(item.getKey(), block.yield(item.getKey(),
            map.get(item.getKey()), item.getValue()));
      else
        rubyHash.put(item);
    }
    return rubyHash;
  }

  /**
   * Merges any Map into self.
   * 
   * @param otherHash
   *          any Map
   * @return this {@link RubyHash}
   */
  public RubyHash<K, V> mergeǃ(Map<? extends K, ? extends V> otherHash) {
    for (Entry<? extends K, ? extends V> item : otherHash.entrySet()) {
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
   * @return new {@link RubyHash}
   */
  public RubyHash<K, V> mergeǃ(Map<? extends K, ? extends V> otherHash,
      EntryMergeBlock<? super K, V> block) {
    for (Entry<? extends K, ? extends V> item : otherHash.entrySet()) {
      if (containsKey(item.getKey()))
        map.put(item.getKey(), block.yield(item.getKey(),
            map.get(item.getKey()), item.getValue()));
      else
        put(item);
    }
    return this;
  }

  /**
   * Puts an Entry into this {@link RubyHash} directly.
   * 
   * @param entry
   *          an Entry
   * @return this {@link RubyHash}
   */
  public RubyHash<K, V> put(Entry<? extends K, ? extends V> entry) {
    map.put(entry.getKey(), entry.getValue());
    return this;
  }

  /**
   * Puts entries into this {@link RubyHash} directly.
   * 
   * @param entries
   *          an array of entries
   * @return this {@link RubyHash}
   */
  public RubyHash<K, V> put(Entry<? extends K, ? extends V>... entries) {
    for (Entry<? extends K, ? extends V> e : entries) {
      map.put(e.getKey(), e.getValue());
    }
    return this;
  }

  /**
   * Finds an Entry by given value.
   * 
   * @param value
   *          to be found
   * @return Entry or null
   */
  public Entry<K, V> rassoc(V value) {
    for (Entry<K, V> item : map.entrySet()) {
      if (value == null ? item.getValue() == null
          : value.equals(item.getValue()))
        return new ComparableEntry<K, V>(item);
    }
    return null;
  }

  /**
   * Returns a {@link RubyEnumerator} of entries of this {@link RubyHash}.
   * 
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<Entry<K, V>> rejectǃ() {
    return newRubyEnumerator(new ComparableEntryIterable<K, V>(map.entrySet()));
  }

  /**
   * Deletes entries which are true returned by the block. Returns null if
   * nothing is deleted.
   * 
   * @param block
   *          to filter elements
   * @return this {@link RubyHash} or null
   */
  public RubyHash<K, V> rejectǃ(EntryBooleanBlock<? super K, ? super V> block) {
    int beforeSize = size();
    deleteIf(block);
    return map.size() == beforeSize ? null : this;
  }

  /**
   * Replaces all entries with the entries of given Map.
   * 
   * @param otherHash
   *          any Map
   * @return this {@link RubyHash}
   */
  public RubyHash<K, V> replace(Map<? extends K, ? extends V> otherHash) {
    map.clear();
    map.putAll(otherHash);
    return this;
  }

  /**
   * Removes the first Entry and returns it.
   * 
   * @return Entry
   */
  public Entry<K, V> shift() {
    if (map.isEmpty()) {
      return null;
    } else {
      Iterator<Entry<K, V>> iter = map.entrySet().iterator();
      Entry<K, V> first = iter.next();
      iter.remove();
      return new ComparableEntry<K, V>(first);
    }
  }

  /**
   * Puts a key-value pair and returns the value.
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
   * Returns this {@link RubyHash}.
   * 
   * @return this {@link RubyHash}
   */
  public RubyHash<K, V> toH() {
    return this;
  }

  /**
   * Returns this {@link RubyHash}.
   * 
   * @return this {@link RubyHash}
   */
  public RubyHash<K, V> toHash() {
    return this;
  }

  /**
   * Equivalent to {@link #toString()}.
   * 
   * @return String
   */
  public String toS() {
    return map.toString();
  }

  /**
   * Equivalent to {@link #mergeǃ(Map)}.
   * 
   * @param otherHash
   *          any Map
   * @return this RubyHash
   */
  public RubyHash<K, V> update(Map<? extends K, ? extends V> otherHash) {
    return mergeǃ(otherHash);
  }

  /**
   * Equivalent to {@link #mergeǃ(Map, EntryMergeBlock)}.
   * 
   * @param otherHash
   *          any Map
   * @param block
   *          to determine which value is used when key is conflict
   * @return new RubyHash
   */
  public RubyHash<K, V> update(Map<? extends K, ? extends V> otherHash,
      EntryMergeBlock<? super K, V> block) {
    return mergeǃ(otherHash, block);
  }

  // Apply entry blocks to RubyEnumerable methods
  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#allʔ(BooleanBlock)
   */
  public boolean allʔ(final EntryBooleanBlock<? super K, ? super V> block) {
    return allʔ(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#anyʔ(BooleanBlock)
   */
  public boolean anyʔ(final EntryBooleanBlock<? super K, ? super V> block) {
    return anyʔ(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#chunk(TransformBlock)
   */
  public <S> RubyEnumerator<Entry<S, RubyArray<Entry<K, V>>>> chunk(
      final EntryTransformBlock<? super K, ? super V, ? extends S> block) {
    return chunk(new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#collect(TransformBlock)
   */
  public <S> RubyArray<S> collect(
      final EntryTransformBlock<? super K, ? super V, ? extends S> block) {
    return collect(new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#collectConcat(TransformBlock)
   */
  public <S> RubyArray<S> collectConcat(
      final EntryTransformBlock<? super K, ? super V, ? extends List<S>> block) {
    return collectConcat(new TransformBlock<Entry<K, V>, RubyArray<S>>() {

      @Override
      public RubyArray<S> yield(Entry<K, V> item) {
        return newRubyArray(block.yield(item.getKey(), item.getValue()));
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#count(BooleanBlock)
   */
  public int count(final EntryBooleanBlock<? super K, ? super V> block) {
    return count(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#cycle(Block)
   */
  public void cycle(final EntryBlock<? super K, ? super V> block) {
    cycle(new Block<Entry<K, V>>() {

      @Override
      public void yield(Entry<K, V> item) {
        block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#cycle(int, Block)
   */
  public void cycle(int n, final EntryBlock<? super K, ? super V> block) {
    cycle(n, new Block<Entry<K, V>>() {

      @Override
      public void yield(Entry<K, V> item) {
        block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#detect(BooleanBlock)
   */
  public Entry<K, V> detect(
      final EntryBooleanBlock<? super K, ? super V> block) {
    return detect(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#dropWhile(BooleanBlock)
   */
  public RubyArray<Entry<K, V>> dropWhile(
      final EntryBooleanBlock<? super K, ? super V> block) {
    return dropWhile(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#eachEntry(Block)
   */
  public RubyEnumerable<Entry<K, V>> eachEntry(
      final EntryBlock<? super K, ? super V> block) {
    return eachEntry(new Block<Entry<K, V>>() {

      @Override
      public void yield(Entry<K, V> item) {
        block.yield(item.getKey(), item.getValue());
      }

    });

  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#find(BooleanBlock)
   */
  public Entry<K, V> find(final EntryBooleanBlock<? super K, ? super V> block) {
    return detect(block);
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#findAll(BooleanBlock)
   */
  public RubyArray<Entry<K, V>> findAll(
      final EntryBooleanBlock<? super K, ? super V> block) {
    return findAll(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#findIndex(BooleanBlock)
   */
  public Integer findIndex(
      final EntryBooleanBlock<? super K, ? super V> block) {
    return findIndex(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#flatMap(TransformBlock)
   */
  public <S> RubyArray<S> flatMap(
      final EntryTransformBlock<? super K, ? super V, ? extends List<S>> block) {
    return collectConcat(block);
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#grep(String, TransformBlock)
   */
  public <S> RubyArray<S> grep(String regex,
      final EntryTransformBlock<? super K, ? super V, ? extends S> block) {
    return grep(regex, new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#groupBy(TransformBlock)
   */
  public <S> RubyHash<S, RubyArray<Entry<K, V>>> groupBy(
      final EntryTransformBlock<? super K, ? super V, ? extends S> block) {
    return groupBy(new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#map(TransformBlock)
   */
  public <S> RubyArray<S> map(
      final EntryTransformBlock<? super K, ? super V, ? extends S> block) {
    return map(new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#maxBy(Comparator, TransformBlock)
   */
  public <S> Entry<K, V> maxBy(Comparator<? super S> comp,
      final EntryTransformBlock<? super K, ? super V, ? extends S> block) {
    return maxBy(comp, new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#maxBy(TransformBlock)
   */
  public <S> Entry<K, V> maxBy(
      final EntryTransformBlock<? super K, ? super V, ? extends S> block) {
    return maxBy(new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#minBy(Comparator, TransformBlock)
   */
  public <S> Entry<K, V> minBy(Comparator<? super S> comp,
      final EntryTransformBlock<? super K, ? super V, ? extends S> block) {
    return minBy(comp, new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#minBy(TransformBlock)
   */
  public <S> Entry<K, V> minBy(
      final EntryTransformBlock<? super K, ? super V, ? extends S> block) {
    return minBy(new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#minmaxBy(Comparator, TransformBlock)
   */
  public <S> RubyArray<Entry<K, V>> minmaxBy(Comparator<? super S> comp,
      final EntryTransformBlock<? super K, ? super V, ? extends S> block) {
    return minmaxBy(comp, new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#minmaxBy(TransformBlock)
   */
  public <S> RubyArray<Entry<K, V>> minmaxBy(
      final EntryTransformBlock<? super K, ? super V, ? extends S> block) {
    return minmaxBy(new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#noneʔ(BooleanBlock)
   */
  public boolean noneʔ(final EntryBooleanBlock<? super K, ? super V> block) {
    return noneʔ(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#oneʔ(BooleanBlock)
   */
  public boolean oneʔ(final EntryBooleanBlock<? super K, ? super V> block) {
    return oneʔ(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#partition(BooleanBlock)
   */
  public RubyArray<RubyArray<Entry<K, V>>> partition(
      final EntryBooleanBlock<? super K, ? super V> block) {
    return partition(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#reject(BooleanBlock)
   */
  public RubyArray<Entry<K, V>> reject(
      final EntryBooleanBlock<? super K, ? super V> block) {
    return reject(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#reverseEach(Block)
   */
  public RubyEnumerable<Entry<K, V>> reverseEach(
      final EntryBlock<? super K, ? super V> block) {
    return reverseEach(new Block<Entry<K, V>>() {

      @Override
      public void yield(Entry<K, V> item) {
        block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#select(BooleanBlock)
   */
  public RubyArray<Entry<K, V>> select(
      final EntryBooleanBlock<? super K, ? super V> block) {
    return findAll(block);
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#sliceBefore(BooleanBlock)
   */
  public RubyEnumerator<RubyArray<Entry<K, V>>> sliceBefore(
      final EntryBooleanBlock<? super K, ? super V> block) {
    return sliceBefore(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#sortBy(Comparator, TransformBlock)
   */
  public <S> RubyArray<Entry<K, V>> sortBy(Comparator<? super S> comp,
      final EntryTransformBlock<? super K, ? super V, ? extends S> block) {
    return sortBy(comp, new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#sortBy(TransformBlock)
   */
  public <S> RubyArray<Entry<K, V>> sortBy(
      final EntryTransformBlock<? super K, ? super V, ? extends S> block) {
    return sortBy(new TransformBlock<Entry<K, V>, S>() {

      @Override
      public S yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#takeWhile(BooleanBlock)
   */
  public RubyArray<Entry<K, V>> takeWhile(
      final EntryBooleanBlock<? super K, ? super V> block) {
    return takeWhile(new BooleanBlock<Entry<K, V>>() {

      @Override
      public boolean yield(Entry<K, V> item) {
        return block.yield(item.getKey(), item.getValue());
      }

    });
  }

  /**
   * Returns a {@link RubyArray} of values of this {@link RubyHash}.
   * 
   * @return {@link RubyArray}
   */
  @Override
  public RubyArray<V> values() {
    return newRubyArray(map.values());
  }

  /**
   * Finds all the values by given keys.
   * 
   * @param keys
   *          to retrieve values
   * @return {@link RubyArray}
   */
  public RubyArray<V> valuesAt(Object... keys) {
    return valuesAt(Arrays.asList(keys));
  }

  /**
   * Finds all the values by given keys.
   * 
   * @param keys
   *          to retrieve values
   * @return {@link RubyArray}
   */
  public RubyArray<V> valuesAt(Iterable<?> keys) {
    RubyArray<V> rubyArray = newRubyArray();
    for (Object key : keys) {
      rubyArray.add(map.get(key));
    }
    return rubyArray;
  }

  /**
   * Checks if the value included.
   * 
   * @param value
   *          to be checked
   * @return true if value included, false otherwise
   */
  public boolean valueʔ(Object value) {
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
