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
package net.sf.rubycollect4j.util;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

/**
 * 
 * ComparableEntry is comparable if and only if the key is comparable.
 * 
 * @param <K>
 *          the type of the key elements
 * @param <V>
 *          the type of the value elements
 */
public final class ComparableEntry<K, V> implements Entry<K, V>,
    Comparable<Entry<K, V>> {

  private final Entry<K, V> entry;

  /**
   * Creates a ComparableEntry.
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
   * Creates a ComparableEntry.
   * 
   * @param entry
   *          any Entry
   */
  public ComparableEntry(Entry<K, V> entry) {
    this.entry = entry;
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

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public int compareTo(Entry<K, V> o) {
    if (o == null)
      throw new IllegalArgumentException("ArgumentError: comparison of "
          + entry.getClass().getName() + " with null failed");
    if (!(entry.getKey() instanceof Comparable)
        || !(o.getKey() instanceof Comparable))
      throw new IllegalArgumentException("ArgumentError: comparison of "
          + (entry.getKey() == null ? "null" : entry.getKey().getClass()
              .getName()) + " with "
          + (o.getKey() == null ? "null" : o.getKey().getClass().getName())
          + " failed");

    int diff;
    diff = ((Comparable) entry.getKey()).compareTo(o.getKey());
    if (diff != 0)
      return diff;

    if (entry.getValue() instanceof Comparable
        && o.getValue() instanceof Comparable) {
      diff = ((Comparable) entry.getValue()).compareTo(o.getValue());
      if (diff != 0)
        return diff;
    }

    return 0;
  }
}
