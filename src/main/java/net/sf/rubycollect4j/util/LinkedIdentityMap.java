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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

/**
 * 
 * {@link LinkedIdentityMap} is implemented by IdentityHashMap and takes
 * advantage of LinkedList to keep the key elements ordered by their insertion
 * sequence. Unlike IdentityHashMap, {@link LinkedIdentityMap} only compares its
 * keys by identities NOT the values.
 * 
 * @param <K>
 *          the type of the key elements
 * @param <V>
 *          the type of the value elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class LinkedIdentityMap<K, V> implements Map<K, V> {

  private final IdentityHashMap<K, V> map = new IdentityHashMap<>();
  private final List<K> list = new LinkedList<>();

  /**
   * Creates a {@link LinkedIdentityMap}.
   */
  public LinkedIdentityMap() {}

  /**
   * Creates a {@link LinkedIdentityMap} by given Map.
   * 
   * @param map
   *          any Map
   */
  public LinkedIdentityMap(Map<? extends K, ? extends V> map) {
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
      if (val == null ? value == null : val.equals(value)) return true;
    }
    return false;
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    return new EntrySet<>(list, map);
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
    return new KeySet<>(list, map);
  }

  @Override
  public V put(K key, V value) {
    if (!map.containsKey(key)) list.add(key);
    return map.put(key, value);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    for (Entry<? extends K, ? extends V> e : m.entrySet()) {
      put(e.getKey(), e.getValue());
    }
  }

  @Override
  public V remove(Object key) {
    removeByIdentity(list, key);
    return map.remove(key);
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public Collection<V> values() {
    return new Values<>(list, map);
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
   * LinkedIdentityMap::IdentityEntry overrides the equals(). It checks if 2
   * keys have the same identities before applying the regular equality logic of
   * Entry.
   * 
   * @param <S>
   *          the type of the key elements
   * @param <U>
   *          the type of the value elements
   */
  static final class IdentityEntry<S, U> implements Entry<S, U> {

    private final Entry<S, U> entry;

    /**
     * Creates a LinkedIdentityMap::IdentityEntry.
     * 
     * @param key
     *          of the entry
     * @param value
     *          of the entry
     */
    public IdentityEntry(S key, U value) {
      entry = new SimpleEntry<>(key, value);
    }

    /**
     * Creates a LinkedIdentityMap::IdentityEntry.
     * 
     * @param entry
     *          any Entry
     */
    public IdentityEntry(Entry<S, U> entry) {
      this.entry = new SimpleEntry<>(entry.getKey(), entry.getValue());
    }

    @Override
    public S getKey() {
      return entry.getKey();
    }

    @Override
    public U getValue() {
      return entry.getValue();
    }

    @Override
    public U setValue(U value) {
      return entry.setValue(value);
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof IdentityEntry) {
        IdentityEntry<?, ?> iEntry = (IdentityEntry<?, ?>) o;
        return iEntry.getKey() == entry.getKey()
            && (iEntry.getValue() == null ? entry.getValue() == null
                : iEntry.getValue().equals(entry.getValue()));
      }

      if (o instanceof Entry) return entry.equals(o);

      return false;
    }

    @Override
    public int hashCode() {
      return entry.hashCode();
    }

    @Override
    public String toString() {
      return entry.toString();
    }

  }

  /**
   * 
   * LinkedIdentityMap::EntrySet is designed to build a Set view of the
   * LinkedIdentityMap#entrySet.
   * 
   * @param <S>
   *          the type of the key elements
   * @param <U>
   *          the type of the value elements
   */
  static final class EntrySet<S, U> implements Set<Entry<S, U>> {

    private final List<S> list;
    private final IdentityHashMap<S, U> map;

    /**
     * Creates a LinkedIdentityMap::EntrySet.
     * 
     * @param list
     *          a List
     * @param map
     *          an IdentityHashMap
     */
    public EntrySet(List<S> list, IdentityHashMap<S, U> map) {
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
      if (o instanceof Entry) {
        Entry<?, ?> entry = (Entry<?, ?>) o;
        if (map.containsKey(entry.getKey())) {
          U val = map.get(entry.getKey());
          if (val == null ? entry.getValue() == null
              : val.equals(entry.getValue()))
            return true;
        }
      }
      return false;
    }

    @Override
    public Iterator<Entry<S, U>> iterator() {
      return new EntriesIterator(list.iterator(), map);
    }

    @Override
    public Object[] toArray() {
      List<Entry<S, U>> entries = new ArrayList<>();
      for (S key : list) {
        entries.add(new SimpleEntry<>(key, map.get(key)));
      }
      return entries.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
      List<Entry<S, U>> entries = new ArrayList<>();
      for (S key : list) {
        entries.add(new SimpleEntry<>(key, map.get(key)));
      }
      return entries.toArray(a);
    }

    @Override
    public boolean add(Entry<S, U> e) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
      if (o instanceof Entry) {
        Entry<?, ?> entry = (Entry<?, ?>) o;
        if (map.containsKey(entry.getKey())) {
          U val = map.get(entry.getKey());
          if ((val == null ? entry.getValue() == null
              : val.equals(entry.getValue()))) {
            removeByIdentity(list, entry.getKey());
            map.remove(entry.getKey());
            return true;
          }
        }
      }
      return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
      boolean hasAll = true;
      for (Object o : c) {
        hasAll &= contains(o);
      }
      return hasAll;
    }

    @Override
    public boolean addAll(Collection<? extends Entry<S, U>> c) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
      Map<IdentityEntry<?, ?>, Object> hashMap = new HashMap<>();
      for (Object o : c) {
        if (o instanceof Entry) {
          Entry<?, ?> entry = (Entry<?, ?>) o;
          hashMap.put(new IdentityEntry<Object, Object>(entry.getKey(),
              entry.getValue()), null);
        }
      }

      boolean isChanged = false;
      Iterator<Entry<S, U>> iter = map.entrySet().iterator();
      while (iter.hasNext()) {
        Entry<S, U> entry = iter.next();
        if (!hashMap.containsKey(new IdentityEntry<>(entry))) {
          removeByIdentity(list, entry.getKey());
          iter.remove();
          isChanged = true;
        }
      }
      return isChanged;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
      boolean isChanged = false;
      for (Object o : c) {
        isChanged |= remove(o);
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
      return map.entrySet().equals(o);
    }

    @Override
    public int hashCode() {
      return map.entrySet().hashCode();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("[");
      for (int i = 0; i < list.size(); i++) {
        S key = list.get(i);
        if (i == 0)
          sb.append(key).append("=").append(map.get(key));
        else
          sb.append(", ").append(key).append("=").append(map.get(key));
      }
      sb.append("]");
      return sb.toString();
    }

    private final class EntriesIterator implements Iterator<Entry<S, U>> {

      private final Iterator<S> iter;
      private final IdentityHashMap<S, U> map;
      private S key;

      public EntriesIterator(Iterator<S> iter, IdentityHashMap<S, U> map) {
        this.iter = iter;
        this.map = map;
      }

      @Override
      public boolean hasNext() {
        return iter.hasNext();
      }

      @Override
      public Entry<S, U> next() {
        key = iter.next();
        return new IdentityEntry<>(key, map.get(key));
      }

      @Override
      public void remove() {
        iter.remove();
        map.remove(key);
      }

    }

  }

  /**
   * 
   * LinkedIdentityMap::KeySet is designed to build a Set view of the
   * LinkedIdentityMap#keySet.
   * 
   * @param <S>
   *          the type of the key elements
   * @param <U>
   *          the type of the value elements
   */
  static final class KeySet<S, U> implements Set<S> {

    private final List<S> list;
    private final IdentityHashMap<S, U> map;

    /**
     * Creates a LinkedIdentityMap::KeySet.
     * 
     * @param list
     *          a List
     * @param map
     *          an IdentityHashMap
     */
    public KeySet(List<S> list, IdentityHashMap<S, U> map) {
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
      return map.keySet().contains(o);
    }

    @Override
    public Iterator<S> iterator() {
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
    public boolean add(S e) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
      map.remove(o);
      return removeByIdentity(list, o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
      return map.keySet().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends S> c) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
      Map<Object, Object> idMap = new IdentityHashMap<>();
      for (Object o : c) {
        idMap.put(o, null);
      }

      boolean isChanged = false;
      Iterator<S> iter = list.iterator();
      while (iter.hasNext()) {
        S key = iter.next();
        if (!idMap.containsKey(key)) {
          map.remove(key);
          iter.remove();
          isChanged = true;
        }
      }
      return isChanged;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
      Map<Object, Object> idMap = new IdentityHashMap<>();
      for (Object o : c) {
        idMap.put(o, null);
      }

      boolean isChanged = false;
      Iterator<S> iter = list.iterator();
      while (iter.hasNext()) {
        S key = iter.next();
        if (idMap.containsKey(key)) {
          map.remove(key);
          iter.remove();
          isChanged = true;
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

    private final class KeysIterator implements Iterator<S> {

      private final Iterator<S> iter;
      private final IdentityHashMap<S, U> map;
      private S key;

      public KeysIterator(Iterator<S> iter, IdentityHashMap<S, U> map) {
        this.iter = iter;
        this.map = map;
      }

      @Override
      public boolean hasNext() {
        return iter.hasNext();
      }

      @Override
      public S next() {
        key = iter.next();
        return key;
      }

      @Override
      public void remove() {
        iter.remove();
        map.remove(key);
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
   * @param <U>
   *          the type of the value elements
   */
  static final class Values<S, U> implements Collection<U> {

    private final List<S> list;
    private final IdentityHashMap<S, U> map;

    /**
     * Creates a LinkedIdentityMap::Values.
     * 
     * @param list
     *          a List
     * @param map
     *          an IdentityHashMap
     */
    public Values(List<S> list, IdentityHashMap<S, U> map) {
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
      for (U val : map.values()) {
        if (val == null ? o == null : val.equals(o)) return true;
      }
      return false;
    }

    @Override
    public Iterator<U> iterator() {
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
      List<U> values = new ArrayList<>();
      for (S key : list) {
        values.add(map.get(key));
      }
      return values.toArray(a);
    }

    @Override
    public boolean add(U e) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
      Iterator<Entry<S, U>> iter = map.entrySet().iterator();
      while (iter.hasNext()) {
        Entry<S, U> entry = iter.next();
        if (o == null ? entry.getValue() == null : o.equals(entry.getValue())) {
          removeByIdentity(list, entry.getKey());
          iter.remove();
          return true;
        }
      }
      return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
      Collection<Object> coll = new ArrayList<>(c);
      for (U val : map.values()) {
        coll.remove(val);
      }
      return coll.isEmpty();
    }

    @Override
    public boolean addAll(Collection<? extends U> c) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
      boolean isChanged = false;
      Iterator<Entry<S, U>> iter = map.entrySet().iterator();
      while (iter.hasNext()) {
        Entry<S, U> entry = iter.next();
        if (c.contains(entry.getValue())) {
          removeByIdentity(list, entry.getKey());
          iter.remove();
          isChanged = true;
        }
      }
      return isChanged;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
      boolean isChanged = false;
      Iterator<Entry<S, U>> iter = map.entrySet().iterator();
      while (iter.hasNext()) {
        Entry<S, U> entry = iter.next();
        if (!c.contains(entry.getValue())) {
          removeByIdentity(list, entry.getKey());
          iter.remove();
          isChanged = true;
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

    private final class ValuesIterator implements Iterator<U> {

      private final Iterator<S> iter;
      private final IdentityHashMap<S, U> map;
      private S key;

      public ValuesIterator(Iterator<S> iter, IdentityHashMap<S, U> map) {
        this.iter = iter;
        this.map = map;
      }

      @Override
      public boolean hasNext() {
        return iter.hasNext();
      }

      @Override
      public U next() {
        key = iter.next();
        return map.get(key);
      }

      @Override
      public void remove() {
        iter.remove();
        map.remove(key);
      }

    }

  }

  private static boolean removeByIdentity(List<?> list, Object o) {
    ListIterator<?> li = list.listIterator();
    while (li.hasNext()) {
      Object element = li.next();
      if (element == o) {
        li.remove();
        return true;
      }
    }
    return false;
  }

}
