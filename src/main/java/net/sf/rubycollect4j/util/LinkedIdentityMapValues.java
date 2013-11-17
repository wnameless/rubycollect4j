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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import sun.awt.util.IdentityLinkedList;

/**
 * 
 * LinkedIdentityMapValues is designed to build a Collection view of the values
 * of a LinkedIdentityMap.
 * 
 * @param <K>
 *          the type of the key elements
 * @param <V>
 *          the type of the value elements
 */
public final class LinkedIdentityMapValues<K, V> implements Collection<V> {

  private final IdentityLinkedList<K> list;
  private final IdentityHashMap<K, V> map;

  /**
   * Creates a LinkedIdentityMapValues.
   * 
   * @param list
   *          an IdentityLinkedList
   * @param map
   *          an IdentityHashMap
   */
  public LinkedIdentityMapValues(IdentityLinkedList<K> list,
      IdentityHashMap<K, V> map) {
    this.list = list;
    this.map = map;
  }

  @Override
  public int size() {
    return list.size();
  }

  @Override
  public boolean isEmpty() {
    return list.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    for (V val : map.values()) {
      if (val == null && o == null)
        return true;
      else if (val != null && val.equals(o))
        return true;
    }
    return false;
  }

  @Override
  public Iterator<V> iterator() {
    return new ValuesIterator(list.iterator(), map);
  }

  @Override
  public Object[] toArray() {
    Object[] objs = new Object[list.size()];
    for (int i = 0; i < list.size(); i++) {
      objs[i] = map.get(list.get(i));
    }
    return objs;
  }

  @Override
  public <T> T[] toArray(T[] a) {
    List<V> values = new ArrayList<V>();
    for (K key : list) {
      values.add(map.get(key));
    }
    return values.toArray(a);
  }

  @Override
  public boolean add(V e) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean remove(Object o) {
    return removeAll(Collections.singleton(o));
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    Collection<Object> coll = new ArrayList<Object>(c);
    for (V val : map.values()) {
      coll.remove(val);
    }
    return coll.isEmpty();
  }

  @Override
  public boolean addAll(Collection<? extends V> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    boolean isChanged = false;
    Iterator<Entry<K, V>> iter = map.entrySet().iterator();
    while (iter.hasNext()) {
      Entry<K, V> entry = iter.next();
      if (c.contains(entry.getValue())) {
        isChanged = true;
        list.remove(entry.getKey());
        iter.remove();
      }
    }
    return isChanged;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    boolean changed = false;
    Iterator<Entry<K, V>> iter = map.entrySet().iterator();
    while (iter.hasNext()) {
      Entry<K, V> entry = iter.next();
      if (!c.contains(entry.getValue())) {
        changed = true;
        list.remove(entry.getKey());
        iter.remove();
      }
    }
    return changed;
  }

  @Override
  public void clear() {
    list.clear();
    map.values().clear();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    for (int i = 0; i < list.size(); i++) {
      K key = list.get(i);
      if (i == 0)
        sb.append(map.get(key));
      else
        sb.append(", ").append(map.get(key));
    }
    sb.append("]");
    return sb.toString();
  }

  private final class ValuesIterator implements Iterator<V> {

    private final Iterator<K> iter;
    private final IdentityHashMap<K, V> map;
    private K element;

    public ValuesIterator(Iterator<K> iter, IdentityHashMap<K, V> map) {
      this.iter = iter;
      this.map = map;
    }

    @Override
    public boolean hasNext() {
      return iter.hasNext();
    }

    @Override
    public V next() {
      element = iter.next();
      return map.get(element);
    }

    @Override
    public void remove() {
      iter.remove();
      map.remove(element);
    }

  }

}
