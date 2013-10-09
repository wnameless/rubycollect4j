package net.sf.rubycollect4j.iter;

import java.util.Iterator;
import java.util.Map.Entry;

import net.sf.rubycollect4j.util.ComparableEntry;

public final class ComparableEntryIterable<K, V> implements
    Iterable<ComparableEntry<K, V>> {

  private final Iterable<Entry<K, V>> iter;

  public ComparableEntryIterable(Iterable<Entry<K, V>> iter) {
    this.iter = iter;
  }

  @Override
  public Iterator<ComparableEntry<K, V>> iterator() {
    return new ComparableEntryIterator<K, V>(iter.iterator());
  }

}
