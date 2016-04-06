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
package net.sf.rubycollect4j.util;

import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.Map.Entry;

/**
 * 
 * {@link ComparableEntry} is Comparable if and only if the key is Comparable.
 * 
 * @param <K>
 *          the type of the key elements
 * @param <V>
 *          the type of the value elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class ComparableEntry<K, V> implements Entry<K, V>,
    Comparable<Entry<? extends K, ? extends V>> {

  private final Entry<K, V> entry;

  /**
   * Creates a {@link ComparableEntry}.
   * 
   * @param key
   *          of the entry
   * @param value
   *          of the entry
   */
  public ComparableEntry(K key, V value) {
    entry = new SimpleEntry<K, V>(key, value);
  }

  /**
   * Creates a {@link ComparableEntry}.
   * 
   * @param entry
   *          any Entry
   */
  public ComparableEntry(Entry<? extends K, ? extends V> entry) {
    this.entry = new SimpleEntry<K, V>(entry.getKey(), entry.getValue());
  }

  @Override
  public K getKey() {
    return entry.getKey();
  }

  @Override
  public V getValue() {
    return entry.getValue();
  }

  @Override
  public V setValue(V value) {
    return entry.setValue(value);
  }

  @Override
  public boolean equals(Object o) {
    return entry.equals(o);
  }

  @Override
  public int hashCode() {
    return entry.hashCode();
  }

  @Override
  public String toString() {
    return entry.toString();
  }

  @Override
  public int compareTo(Entry<? extends K, ? extends V> o) {
    if (o == null)
      throw new IllegalArgumentException("ArgumentError: comparison of "
          + entry.getClass().getName() + " with null failed");

    Comparator<K> keyComp = new TryComparator<K>();
    Comparator<V> valueComp = new TryComparator<V>();

    int diff;
    if ((diff = keyComp.compare(entry.getKey(), o.getKey())) != 0) return diff;

    if (entry.getValue() instanceof Comparable
        && o.getValue() instanceof Comparable) {
      if ((diff = valueComp.compare(entry.getValue(), o.getValue())) != 0)
        return diff;
    }

    return 0;
  }

}
