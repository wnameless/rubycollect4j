package net.sf.rubycollect4j.iter;

import java.util.Iterator;
import java.util.Map.Entry;

import net.sf.rubycollect4j.util.ComparableEntry;

public final class ComparableEntryIterator<K, V> implements
    Iterator<ComparableEntry<K, V>> {

  private final Iterator<Entry<K, V>> iter;

  public ComparableEntryIterator(Iterator<Entry<K, V>> iter) {
    this.iter = iter;
  }

  @Override
  public boolean hasNext() {
    return iter.hasNext();
  }

  @Override
  public ComparableEntry<K, V> next() {
    return new ComparableEntry<K, V>(iter.next());
  }

  @Override
  public void remove() {
    iter.remove();
  }

}
