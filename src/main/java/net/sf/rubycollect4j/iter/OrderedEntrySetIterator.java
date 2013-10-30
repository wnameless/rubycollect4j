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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import net.sf.rubycollect4j.util.ComparableEntry;

/**
 * 
 * OrderedEntrySetIterator iterates each Entry by the key ordering of the
 * Iterator gives. It assumes Iterator contains all the keys of given Map, the
 * hasNext() and next() may not function properly if Iterator has shortage of
 * keys.
 * 
 * @param <K>
 *          the type of the key elements
 * @param <V>
 *          the type of the value elements
 */
public final class OrderedEntrySetIterator<K, V> implements
    Iterator<Entry<K, V>> {

  private final Iterator<K> iter;
  private final Map<K, V> map;

  /**
   * Creates an OrderedEntrySetIterator.
   * 
   * @param iter
   *          an Iterator
   * @param map
   *          a Map
   * @throws NullPointerException
   *           if iter or map is null
   */
  public OrderedEntrySetIterator(Iterator<K> iter, Map<K, V> map) {
    if (iter == null || map == null)
      throw new NullPointerException();

    this.iter = iter;
    this.map = map;
  }

  @Override
  public boolean hasNext() {
    return iter.hasNext();
  }

  @Override
  public Entry<K, V> next() {
    if (!hasNext())
      throw new NoSuchElementException();

    K key = iter.next();
    return new ComparableEntry<K, V>(key, map.get(key));
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
