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
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import net.sf.rubycollect4j.iter.OrderedEntrySetIterable;

import static java.util.Collections.unmodifiableList;

/**
 * 
 * LinkedIdentityMap is implemented by IdentityHashMap and takes advantage of
 * LinkedList to keep the key elements ordered by their insertion sequence.
 * 
 * @param <K>
 *          the type of the key elements
 * @param <V>
 *          the type of the value elements
 */
public final class LinkedIdentityMap<K, V> implements Map<K, V> {

  private final IdentityHashMap<K, V> map = new IdentityHashMap<K, V>();
  private final LinkedList<K> list = new LinkedList<K>();

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
    return map.containsValue(value);
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
    if (map.containsKey(key)) {
      ListIterator<K> li = list.listIterator();
      while (li.hasNext()) {
        K k = li.next();
        if (k == key) {
          li.remove();
          break;
        }
      }
    }
    return map.remove(key);
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public Collection<V> values() {
    List<V> values = new ArrayList<V>();
    for (K key : list) {
      values.add(map.get(key));
    }
    return values;
  }

}
