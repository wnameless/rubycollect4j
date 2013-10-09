package net.sf.rubycollect4j.util;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public final class ComparableEntry<K, V> implements Entry<K, V>,
    Comparable<Entry<K, V>> {

  private final Entry<K, V> entry;

  public ComparableEntry(K key, V value) {
    entry = new SimpleEntry<K, V>(key, value);
  }

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
    if (!(entry.getKey() instanceof Comparable))
      throw new IllegalArgumentException("ArgumentError: comparison of "
          + (entry.getKey() == null ? "null" : entry.getKey().getClass()
              .getName()) + " with "
          + (o.getKey() == null ? "null" : o.getKey().getClass().getName())
          + " failed");

    int diff;
    diff = ((Comparable) entry.getKey()).compareTo(o.getKey());
    if (diff != 0)
      return diff;

    if (entry.getValue() instanceof Comparable) {
      diff = ((Comparable) entry.getValue()).compareTo(o.getValue());
      if (diff != 0)
        return diff;
    }

    return 0;
  }

}
