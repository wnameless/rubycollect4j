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

import static java.util.Collections.unmodifiableList;

import java.util.Collection;
import java.util.IdentityHashMap;
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
    return new ListSet<K>(unmodifiableList(list));
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
    return new LinkedIdentityMapValues<K, V>(list, map);
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

}
