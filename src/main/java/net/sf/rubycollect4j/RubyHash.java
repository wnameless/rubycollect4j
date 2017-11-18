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
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import net.sf.rubycollect4j.function.TriFunction;
import net.sf.rubycollect4j.iter.ComparableEntryIterator;
import net.sf.rubycollect4j.util.ComparableEntry;
import net.sf.rubycollect4j.util.LinkedIdentityMap;

/**
 * 
 * {@link RubyHash} implements all methods refer to the Hash class of Ruby
 * language.
 * <p>
 * {@link RubyHash} is also a Java Map and a {@link RubyBase.Enumerable}.
 * 
 * @param <K>
 *          the type of the key elements
 * @param <V>
 *          the type of the value elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class RubyHash<K, V>
    implements RubyEnumerable<Entry<K, V>>, Map<K, V>, Serializable {

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
    Objects.requireNonNull(map);

    return new RubyHash<>(map);
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
    Objects.requireNonNull(map);

    return new RubyHash<>(map);
  }

  /**
   * Creates a {@link RubyHash}.
   */
  public RubyHash() {
    map = new LinkedHashMap<>();
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
    Objects.requireNonNull(map);

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
    Objects.requireNonNull(map);

    this.map = new LinkedHashMap<>(map);
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
      return new ComparableEntry<>(key, map.get(key));
    else
      return null;
  }

  /**
   * Returns a new {@link RubyHash} with the null values/key pairs removed.
   * 
   * @return new {@link RubyHash}
   */
  public RubyHash<K, V> compact() {
    RubyHash<K, V> rubyHash = Ruby.Hash.create();
    map.entrySet().forEach(entry -> {
      if (entry.getValue() != null) rubyHash.put(entry);
    });
    return rubyHash;
  }

  /**
   * Removes all null values from the {@link RubyHash}.
   * 
   * @return this {@link RubyHash}
   */
  public RubyHash<K, V> compactǃ() {
    return deleteIf((k, v) -> v == null);
  }

  /**
   * Turns this {@link RubyHash} to compare each elements by their identities
   * instead of their equalities.
   * 
   * @return this {@link RubyHash}
   */
  public RubyHash<K, V> compareByIdentity() {
    map = new LinkedIdentityMap<>(map);
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
    return Ruby.Enumerator.of(this);
  }

  /**
   * Deletes entries which the result are true returned by the block.
   * 
   * @param block
   *          to filter elements
   * @return this {@link RubyHash}
   */
  public RubyHash<K, V> deleteIf(BiPredicate<? super K, ? super V> block) {
    Iterator<Entry<K, V>> iter = map.entrySet().iterator();
    while (iter.hasNext()) {
      Entry<K, V> item = iter.next();
      if (block.test(item.getKey(), item.getValue())) iter.remove();
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
   * @see RubyEnumerable#each(Consumer)
   */
  public RubyHash<K, V> each(BiConsumer<? super K, ? super V> block) {
    for (Entry<K, V> item : map.entrySet()) {
      block.accept(item.getKey(), item.getValue());
    }
    return this;
  }

  /**
   * Returns a {@link RubyEnumerator} of keys of this {@link RubyHash}.
   * 
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<K> eachKey() {
    return Ruby.Enumerator.of(map.keySet());
  }

  /**
   * Yields each key to given block.
   * 
   * @param block
   *          to yield each key
   * @return this {@link RubyHash}
   */
  public RubyHash<K, V> eachKey(Consumer<? super K> block) {
    for (K item : map.keySet()) {
      block.accept(item);
    }
    return this;
  }

  /**
   * Returns a {@link RubyEnumerator} of entries this {@link RubyHash}.
   * 
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<Entry<K, V>> eachPair() {
    return Ruby.Enumerator.of(this);
  }

  /**
   * Equivalent to {@link #each(BiComsumer)}.
   * 
   * @param block
   *          to yield each entry
   * @return this {@link RubyHash}
   */
  public RubyHash<K, V> eachPair(BiConsumer<? super K, ? super V> block) {
    return each(block);
  }

  /**
   * Returns a {@link RubyEnumerator} of values of this {@link RubyHash}.
   * 
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<V> eachValue() {
    return Ruby.Enumerator.of(values());
  }

  /**
   * Yields each value to given block.
   * 
   * @param block
   *          to yield each value
   * @return this {@link RubyHash}
   */
  public RubyHash<K, V> eachValue(Consumer<? super V> block) {
    for (V item : values()) {
      block.accept(item);
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
   * Finds all the values by given keys.
   * 
   * @param keys
   *          used to retrieve values
   * @return {@link RubyArray}
   * @throws IllegalArgumentException
   *           if any key cannot be found
   */
  public RubyArray<V> fetchValues(Object... keys) {
    return fetchValues(Arrays.asList(keys));
  }

  /**
   * Finds all the values by given keys.
   * 
   * @param keys
   *          used to retrieve values
   * @return {@link RubyArray}
   * @throws IllegalArgumentException
   *           if any key cannot be found
   */
  public RubyArray<V> fetchValues(Iterable<?> keys) {
    RubyArray<V> values = Ruby.Array.create();
    keys.forEach(key -> {
      if (map.containsKey(key)) {
        values.add(map.get(key));
      } else {
        throw new NoSuchElementException("KeyError: key not found: " + key);
      }
    });
    return values;
  }

  /**
   * Returns a {@link RubyEnumerator} of entries of this {@link RubyHash}.
   * 
   * @return {@link RubyEnumerator}
   */
  public RubyArray<Entry<K, V>> flatten() {
    return Ruby.Array.copyOf(this);
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
    RubyHash<V, K> invertHash = Ruby.Hash.create();
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
    return Ruby.Enumerator.of(this);
  }

  /**
   * Keeps elements which the result are true returned by the block.
   * 
   * @param block
   *          to filter elements
   * @return this {@link RubyHash}
   */
  public RubyHash<K, V> keepIf(BiPredicate<? super K, ? super V> block) {
    Iterator<Entry<K, V>> iter = map.entrySet().iterator();
    while (iter.hasNext()) {
      Entry<K, V> item = iter.next();
      if (!block.test(item.getKey(), item.getValue())) iter.remove();
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
    return Ruby.Array.copyOf(map.keySet());
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
    RubyHash<K, V> rubyHash = Ruby.Hash.create();
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
      TriFunction<? super K, V, V, V> block) {
    RubyHash<K, V> rubyHash = Ruby.Hash.copyOf(map);
    for (Entry<K, V> item : otherHash.entrySet()) {
      if (rubyHash.containsKey(item.getKey()))
        rubyHash.put(item.getKey(), block.apply(item.getKey(),
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
      TriFunction<? super K, V, V, V> block) {
    for (Entry<? extends K, ? extends V> item : otherHash.entrySet()) {
      if (containsKey(item.getKey()))
        map.put(item.getKey(), block.apply(item.getKey(),
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
  @SafeVarargs
  public final RubyHash<K, V> put(Entry<? extends K, ? extends V>... entries) {
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
        return new ComparableEntry<>(item);
    }
    return null;
  }

  /**
   * Returns a {@link RubyEnumerator} of entries of this {@link RubyHash}.
   * 
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<Entry<K, V>> rejectǃ() {
    return Ruby.Enumerator.of(this);
  }

  /**
   * Deletes entries which are true returned by the block. Returns null if
   * nothing is deleted.
   * 
   * @param block
   *          to filter elements
   * @return this {@link RubyHash} or null
   */
  public RubyHash<K, V> rejectǃ(BiPredicate<? super K, ? super V> block) {
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
      return new ComparableEntry<>(first);
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
  public RubyHash<K, V> toHash() {
    return this;
  }

  /**
   * Puts all entries into a {@link Map}.
   * 
   * @return a {@link Map}
   */
  public Map<K, V> toMap() {
    return new LinkedHashMap<>(this);
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
   * Returns a {@link RubyEnumerator} of values of this {@link RubyHash}.
   * 
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<V> transformValues() {
    return Ruby.Enumerator.of(map.values());
  }

  /**
   * Transforms every value with the results of running block.
   * 
   * @param block
   *          to transform values
   * @return new {@link RubyHash}
   */
  public RubyHash<K, V> transformValues(
      Function<? super V, ? extends V> block) {
    RubyHash<K, V> rubyHash = Ruby.Hash.create();
    map.entrySet().forEach(entry -> {
      rubyHash.put(entry.getKey(), block.apply(entry.getValue()));
    });
    return rubyHash;
  }

  /**
   * Returns a {@link RubyEnumerator} of values of this {@link RubyHash}.
   * 
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<V> transformValuesǃ() {
    return Ruby.Enumerator.of(map.values());
  }

  /**
   * Transforms every value with the results of running block.
   * 
   * @param block
   *          to transform values
   * @return this {@link RubyHash}
   */
  public RubyHash<K, V> transformValuesǃ(
      Function<? super V, ? extends V> block) {
    map.keySet().forEach(key -> map.replace(key, block.apply(map.get(key))));
    return this;
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
   * Equivalent to {@link #mergeǃ(Map, TriFunction)}.
   * 
   * @param otherHash
   *          any Map
   * @param block
   *          to determine which value is used when key is conflict
   * @return new RubyHash
   */
  public RubyHash<K, V> update(Map<? extends K, ? extends V> otherHash,
      TriFunction<? super K, V, V, V> block) {
    return mergeǃ(otherHash, block);
  }

  // Apply entry blocks to RubyEnumerable methods
  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#allʔ(Predicate)
   */
  public boolean allʔ(BiPredicate<? super K, ? super V> block) {
    return allʔ((Predicate<Entry<K, V>>) item -> block.test(item.getKey(),
        item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#anyʔ(Predicate)
   */
  public boolean anyʔ(BiPredicate<? super K, ? super V> block) {
    return anyʔ((Predicate<Entry<K, V>>) item -> block.test(item.getKey(),
        item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#chunk(Function)
   */
  public <S> RubyEnumerator<Entry<S, RubyArray<Entry<K, V>>>> chunk(
      BiFunction<? super K, ? super V, ? extends S> block) {
    return chunk((Function<Entry<K, V>, S>) item -> block.apply(item.getKey(),
        item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#collect(Function)
   */
  public <S> RubyArray<S> collect(
      BiFunction<? super K, ? super V, ? extends S> block) {
    return collect((Function<Entry<K, V>, S>) item -> block.apply(item.getKey(),
        item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#collectConcat(Function)
   */
  public <S> RubyArray<S> collectConcat(
      BiFunction<? super K, ? super V, ? extends List<S>> block) {
    return collectConcat(
        (Function<Entry<K, V>, RubyArray<S>>) item -> Ruby.Array
            .of(block.apply(item.getKey(), item.getValue())));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#count(Predicate)
   */
  public int count(BiPredicate<? super K, ? super V> block) {
    return count((Predicate<Entry<K, V>>) item -> block.test(item.getKey(),
        item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#cycle(Consumer)
   */
  public void cycle(BiConsumer<? super K, ? super V> block) {
    cycle(item -> block.accept(item.getKey(), item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#cycle(int, Consumer)
   */
  public void cycle(int n, BiConsumer<? super K, ? super V> block) {
    cycle(n, item -> block.accept(item.getKey(), item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#detect(Predicate)
   */
  public Entry<K, V> detect(BiPredicate<? super K, ? super V> block) {
    return detect((Predicate<Entry<K, V>>) item -> block.test(item.getKey(),
        item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#dropWhile(Predicate)
   */
  public RubyArray<Entry<K, V>> dropWhile(
      BiPredicate<? super K, ? super V> block) {
    return dropWhile((Predicate<Entry<K, V>>) item -> block.test(item.getKey(),
        item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#eachEntry(Consumer)
   */
  public RubyEnumerable<Entry<K, V>> eachEntry(
      BiConsumer<? super K, ? super V> block) {
    return eachEntry((Consumer<Entry<K, V>>) item -> block.accept(item.getKey(),
        item.getValue()));

  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#find(Predicate)
   */
  public Entry<K, V> find(BiPredicate<? super K, ? super V> block) {
    return detect(block);
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#findAll(Predicate)
   */
  public RubyArray<Entry<K, V>> findAll(
      BiPredicate<? super K, ? super V> block) {
    return findAll((Predicate<Entry<K, V>>) item -> block.test(item.getKey(),
        item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#findIndex(Predicate)
   */
  public Integer findIndex(BiPredicate<? super K, ? super V> block) {
    return findIndex((Predicate<Entry<K, V>>) item -> block.test(item.getKey(),
        item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#flatMap(Function)
   */
  public <S> RubyArray<S> flatMap(
      BiFunction<? super K, ? super V, ? extends List<S>> block) {
    return collectConcat(block);
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#grep(String, Function)
   */
  public <S> RubyArray<S> grep(String regex,
      BiFunction<? super K, ? super V, ? extends S> block) {
    return grep(regex, (Function<Entry<K, V>, S>) item -> block
        .apply(item.getKey(), item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#groupBy(Function)
   */
  public <S> RubyHash<S, RubyArray<Entry<K, V>>> groupBy(
      BiFunction<? super K, ? super V, ? extends S> block) {
    return groupBy((Function<Entry<K, V>, S>) item -> block.apply(item.getKey(),
        item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#map(Function)
   */
  public <S> RubyArray<S> map(
      BiFunction<? super K, ? super V, ? extends S> block) {
    return map((Function<Entry<K, V>, S>) item -> block.apply(item.getKey(),
        item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#maxBy(Comparator, Function)
   */
  public <S> Entry<K, V> maxBy(Comparator<? super S> comp,
      BiFunction<? super K, ? super V, ? extends S> block) {
    return maxBy(comp, (Function<Entry<K, V>, S>) item -> block
        .apply(item.getKey(), item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#minBy(Comparator, Function)
   */
  public <S> Entry<K, V> minBy(Comparator<? super S> comp,
      BiFunction<? super K, ? super V, ? extends S> block) {
    return minBy(comp, (Function<Entry<K, V>, S>) item -> block
        .apply(item.getKey(), item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#minBy(Function)
   */
  public <S> Entry<K, V> minBy(
      BiFunction<? super K, ? super V, ? extends S> block) {
    return minBy((Function<Entry<K, V>, S>) item -> block.apply(item.getKey(),
        item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#minmaxBy(Comparator, Function)
   */
  public <S> RubyArray<Entry<K, V>> minmaxBy(Comparator<? super S> comp,
      BiFunction<? super K, ? super V, ? extends S> block) {
    return minmaxBy(comp, (Function<Entry<K, V>, S>) item -> block
        .apply(item.getKey(), item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#minmaxBy(Function)
   */
  public <S> RubyArray<Entry<K, V>> minmaxBy(
      BiFunction<? super K, ? super V, ? extends S> block) {
    return minmaxBy((Function<Entry<K, V>, S>) item -> block
        .apply(item.getKey(), item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#maxBy(Function)
   */
  public <S> Entry<K, V> maxBy(
      BiFunction<? super K, ? super V, ? extends S> block) {
    return maxBy((Function<Entry<K, V>, S>) item -> block.apply(item.getKey(),
        item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#noneʔ(Predicate)
   */
  public boolean noneʔ(BiPredicate<? super K, ? super V> block) {
    return noneʔ((Predicate<Entry<K, V>>) item -> block.test(item.getKey(),
        item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#oneʔ(Predicate)
   */
  public boolean oneʔ(BiPredicate<? super K, ? super V> block) {
    return oneʔ((Predicate<Entry<K, V>>) item -> block.test(item.getKey(),
        item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#partition(Predicate)
   */
  public RubyArray<RubyArray<Entry<K, V>>> partition(
      BiPredicate<? super K, ? super V> block) {
    return partition((Predicate<Entry<K, V>>) item -> block.test(item.getKey(),
        item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#reject(Predicate)
   */
  public RubyArray<Entry<K, V>> reject(
      BiPredicate<? super K, ? super V> block) {
    return reject((Predicate<Entry<K, V>>) item -> block.test(item.getKey(),
        item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#reverseEach(Consumer)
   */
  public RubyEnumerable<Entry<K, V>> reverseEach(
      BiConsumer<? super K, ? super V> block) {
    return reverseEach((Consumer<Entry<K, V>>) item -> block
        .accept(item.getKey(), item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#select(Predicate)
   */
  public RubyArray<Entry<K, V>> select(
      BiPredicate<? super K, ? super V> block) {
    return findAll(block);
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#sliceBefore(Predicate)
   */
  public RubyEnumerator<RubyArray<Entry<K, V>>> sliceBefore(
      BiPredicate<? super K, ? super V> block) {
    return sliceBefore((Predicate<Entry<K, V>>) item -> block
        .test(item.getKey(), item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#sortBy(Comparator, Function)
   */
  public <S> RubyArray<Entry<K, V>> sortBy(Comparator<? super S> comp,
      BiFunction<? super K, ? super V, ? extends S> block) {
    return sortBy(comp, (Function<Entry<K, V>, S>) item -> block
        .apply(item.getKey(), item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#sortBy(Function)
   */
  public <S> RubyArray<Entry<K, V>> sortBy(
      BiFunction<? super K, ? super V, ? extends S> block) {
    return sortBy((Function<Entry<K, V>, S>) item -> block.apply(item.getKey(),
        item.getValue()));
  }

  /**
   * An adapter method.
   * 
   * @see RubyEnumerable#takeWhile(Predicate)
   */
  public RubyArray<Entry<K, V>> takeWhile(
      BiPredicate<? super K, ? super V> block) {
    return takeWhile((Predicate<Entry<K, V>>) item -> block.test(item.getKey(),
        item.getValue()));
  }

  /**
   * Returns a {@link RubyArray} of values of this {@link RubyHash}.
   * 
   * @return {@link RubyArray}
   */
  @Override
  public RubyArray<V> values() {
    return Ruby.Array.copyOf(map.values());
  }

  /**
   * Finds all the values by given keys.
   * 
   * @param keys
   *          used to retrieve values
   * @return {@link RubyArray}
   */
  public RubyArray<V> valuesAt(Object... keys) {
    return valuesAt(Arrays.asList(keys));
  }

  /**
   * Finds all the values by given keys.
   * 
   * @param keys
   *          used to retrieve values
   * @return {@link RubyArray}
   */
  public RubyArray<V> valuesAt(Iterable<?> keys) {
    RubyArray<V> rubyArray = Ruby.Array.create();
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
  public Iterator<Entry<K, V>> iterator() {
    return new ComparableEntryIterator<>(map.entrySet().iterator());
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
