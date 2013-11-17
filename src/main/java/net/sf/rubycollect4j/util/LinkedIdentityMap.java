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
import java.util.Map;
import java.util.Set;

import net.sf.rubycollect4j.iter.OrderedEntrySetIterable;
import sun.awt.util.IdentityLinkedList;

/**
 * 
 * LinkedIdentityMap is implemented by IdentityHashMap and takes advantage of
 * LinkedList to keep the key elements ordered by their insertion sequence.
 * Unlike IdentityHashMap, LinkedIdentityMap only compares its keys by
 * identities NOT the values.
 * 
 * @param <K>
 *          the type of the key elements
 * @param <V>
 *          the type of the value elements
 */
public final class LinkedIdentityMap<K, V> implements Map<K, V> {

  private final IdentityHashMap<K, V> map = new IdentityHashMap<K, V>();
  private final IdentityLinkedList<K> list = new IdentityLinkedList<K>();

  public LinkedIdentityMap() {}

  public LinkedIdentityMap(Map<K, V> map) {
    putAll(map);
  }

  @Override
  public void clear() {
    map.clear();
    list.clear();
  }

  @Override
  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    for (V val : map.values()) {
      if (val == null && value == null)
        return true;
      else if (val != null && val.equals(value))
        return true;
    }
    return false;
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    return new OrderedEntrySetIterable<K, V>(list, map);
  }

  @Override
  public V get(Object key) {
    return map.get(key);
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public Set<K> keySet() {
    return new KeySet<K, V>(list, map);
  }

  @Override
  public V put(K key, V value) {
    if (!map.containsKey(key))
      list.add(key);
    return map.put(key, value);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    for (Entry<? extends K, ? extends V> e : m.entrySet()) {
      if (!map.containsKey(e.getKey()))
        list.add(e.getKey());
      map.put(e.getKey(), e.getValue());
    }
  }

  @Override
  public V remove(Object key) {
    list.remove(key);
    return map.remove(key);
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public Collection<V> values() {
    return new Values<K, V>(list, map);
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
    StringBuilder sb = new StringBuilder("{");
    for (int i = 0; i < list.size(); i++) {
      K key = list.get(i);
      if (i == 0)
        sb.append(key).append("=").append(map.get(key));
      else
        sb.append(", ").append(key).append("=").append(map.get(key));
    }
    sb.append("}");
    return sb.toString();
  }

  /**
   * 
   * LinkedIdentityMap::KeySet is designed to build a Set view of the
   * LinkedIdentityMap#keySet.
   * 
   * @param <E>
   *          the type of the key elements
   * @param <S>
   *          the type of the value elements
   */
  static final class KeySet<E, S> implements Set<E> {

    private final IdentityLinkedList<E> list;
    private final IdentityHashMap<E, S> map;

    /**
     * Creates a LinkedIdentityMap::KeySet.
     * 
     * @param list
     *          an IdentityLinkedList
     * @param map
     *          an IdentityHashMap
     */
    public KeySet(IdentityLinkedList<E> list, IdentityHashMap<E, S> map) {
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
      return list.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
      return new KeysIterator(list.iterator(), map);
    }

    @Override
    public Object[] toArray() {
      return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
      return list.toArray(a);
    }

    @Override
    public boolean add(E e) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
      map.remove(o);
      return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
      return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
      IdentityHashMap<Object, Object> idMap =
          new IdentityHashMap<Object, Object>();
      for (Object o : c) {
        idMap.put(o, null);
      }
      boolean isChanged = false;
      Iterator<E> iter = list.iterator();
      while (iter.hasNext()) {
        E key = iter.next();
        if (!idMap.containsKey(key)) {
          isChanged = true;
          iter.remove();
          map.remove(key);
        }
      }
      return isChanged;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
      IdentityHashMap<Object, Object> idMap =
          new IdentityHashMap<Object, Object>();
      for (Object o : c) {
        idMap.put(o, null);
      }
      boolean isChanged = false;
      Iterator<E> iter = list.iterator();
      while (iter.hasNext()) {
        E key = iter.next();
        if (idMap.containsKey(key)) {
          isChanged = true;
          iter.remove();
          map.remove(key);
        }
      }
      return isChanged;
    }

    @Override
    public void clear() {
      list.clear();
      map.clear();
    }

    @Override
    public boolean equals(Object o) {
      return map.keySet().equals(o);
    }

    @Override
    public int hashCode() {
      return map.keySet().hashCode();
    }

    @Override
    public String toString() {
      return list.toString();
    }

    private final class KeysIterator implements Iterator<E> {

      private final Iterator<E> iter;
      private final IdentityHashMap<E, S> map;
      private E element;

      public KeysIterator(Iterator<E> iter, IdentityHashMap<E, S> map) {
        this.iter = iter;
        this.map = map;
      }

      @Override
      public boolean hasNext() {
        return iter.hasNext();
      }

      @Override
      public E next() {
        element = iter.next();
        return element;
      }

      @Override
      public void remove() {
        iter.remove();
        map.remove(element);
      }

    }

  }

  /**
   * 
   * LinkedIdentityMap::Values is designed to build a Collection view of the
   * LinkedIdentityMap#values.
   * 
   * @param <S>
   *          the type of the key elements
   * @param <E>
   *          the type of the value elements
   */
  static final class Values<S, E> implements Collection<E> {

    private final IdentityLinkedList<S> list;
    private final IdentityHashMap<S, E> map;

    /**
     * Creates a LinkedIdentityMap::Values.
     * 
     * @param list
     *          an IdentityLinkedList
     * @param map
     *          an IdentityHashMap
     */
    public Values(IdentityLinkedList<S> list, IdentityHashMap<S, E> map) {
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
      for (E val : map.values()) {
        if (val == null && o == null)
          return true;
        else if (val != null && val.equals(o))
          return true;
      }
      return false;
    }

    @Override
    public Iterator<E> iterator() {
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
      List<E> values = new ArrayList<E>();
      for (S key : list) {
        values.add(map.get(key));
      }
      return values.toArray(a);
    }

    @Override
    public boolean add(E e) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
      return removeAll(Collections.singleton(o));
    }

    @Override
    public boolean containsAll(Collection<?> c) {
      Collection<Object> coll = new ArrayList<Object>(c);
      for (E val : map.values()) {
        coll.remove(val);
      }
      return coll.isEmpty();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
      boolean isChanged = false;
      Iterator<Entry<S, E>> iter = map.entrySet().iterator();
      while (iter.hasNext()) {
        Entry<S, E> entry = iter.next();
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
      Iterator<Entry<S, E>> iter = map.entrySet().iterator();
      while (iter.hasNext()) {
        Entry<S, E> entry = iter.next();
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
        S key = list.get(i);
        if (i == 0)
          sb.append(map.get(key));
        else
          sb.append(", ").append(map.get(key));
      }
      sb.append("]");
      return sb.toString();
    }

    private final class ValuesIterator implements Iterator<E> {

      private final Iterator<S> iter;
      private final IdentityHashMap<S, E> map;
      private S element;

      public ValuesIterator(Iterator<S> iter, IdentityHashMap<S, E> map) {
        this.iter = iter;
        this.map = map;
      }

      @Override
      public boolean hasNext() {
        return iter.hasNext();
      }

      @Override
      public E next() {
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

}
