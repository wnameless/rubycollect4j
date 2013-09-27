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
package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * OrderedEntrySetIterable iterates each Entry by the key ordering of the
 * Iterable gives. It assumes Iterable contains all the keys of given Map, the
 * iterator() and toString() may not function properly if Iterable has shortage
 * of keys.
 * 
 * @param <K>
 *          the type of the key elements
 * @param <V>
 *          the type of the value elements
 */
public final class OrderedEntrySetIterable<K, V> implements
    Iterable<Entry<K, V>>, Set<Entry<K, V>> {

  private final Iterable<K> iter;
  private final Map<K, V> map;

  /**
   * The constructor of the OrderedEntrySetIterable.
   * 
   * @param iter
   *          an Iterable
   * @param map
   *          a Map
   * @throws NullPointerException
   *           if iter is null
   * @throws NullPointerException
   *           if map is null
   */
  public OrderedEntrySetIterable(Iterable<K> iter, Map<K, V> map) {
    if (iter == null || map == null)
      throw new NullPointerException();

    this.iter = iter;
    this.map = map;
  }

  @Override
  public boolean add(Entry<K, V> arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean addAll(Collection<? extends Entry<K, V>> arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean contains(Object arg0) {
    return map.entrySet().contains(arg0);
  }

  @Override
  public boolean containsAll(Collection<?> arg0) {
    return map.entrySet().containsAll(arg0);
  }

  @Override
  public boolean isEmpty() {
    return map.entrySet().isEmpty();
  }

  @Override
  public Iterator<Entry<K, V>> iterator() {
    return new OrderedEntrySetIterator<K, V>(iter.iterator(), map);
  }

  @Override
  public boolean remove(Object arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeAll(Collection<?> arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean retainAll(Collection<?> arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int size() {
    return map.entrySet().size();
  }

  @Override
  public Object[] toArray() {
    return ra(iterator()).toArray();
  }

  @Override
  public <T> T[] toArray(T[] arg0) {
    return ra(iterator()).toArray(arg0);
  }

  @Override
  public boolean equals(Object o) {
    return map.entrySet().equals(o);
  }

  @Override
  public int hashCode() {
    return map.entrySet().hashCode();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    int index = 0;
    for (K key : iter) {
      if (index == 0) {
        sb.append(key).append("=").append(map.get(key));
      } else {
        sb.append(", ").append(key).append("=").append(map.get(key));
      }
      index++;
    }
    sb.append("]");
    return sb.toString();
  }

}
