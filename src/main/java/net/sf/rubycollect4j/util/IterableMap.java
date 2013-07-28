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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ForwardingMap;

/**
 * 
 * IterableMap simply wraps any Map into an Iterable by implementing Iterable
 * interface based on the iterator() of the entrySet().
 * 
 * @param <K>
 *          the type of key elements
 * @param <V>
 *          the type of value elements
 */
public final class IterableMap<K, V> extends ForwardingMap<K, V> implements
    Iterable<Entry<K, V>> {

  private final Map<K, V> map;

  /**
   * Creates an IterableMap by given Map.
   * 
   * @param map
   *          any Map
   */
  public IterableMap(Map<K, V> map) {
    this.map = map;
  }

  @Override
  protected Map<K, V> delegate() {
    return map;
  }

  @Override
  public Iterator<Entry<K, V>> iterator() {
    return map.entrySet().iterator();
  }

}
