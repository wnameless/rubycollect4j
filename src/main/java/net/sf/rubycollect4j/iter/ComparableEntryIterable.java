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
import java.util.Map.Entry;

import net.sf.rubycollect4j.util.ComparableEntry;

/**
 * 
 * ComparableEntryIterable iterates an Iterable of Entry and wraps each Entry
 * into a ComparableEntry.
 * 
 * @param <K>
 *          the type of the key elements
 * @param <V>
 *          the type of the value elements
 */
public final class ComparableEntryIterable<K, V> implements
    Iterable<ComparableEntry<K, V>> {

  private final Iterable<Entry<K, V>> iter;

  /**
   * Creates a ComparableEntryIterable.
   * 
   * @param iter
   *          an Iterable of Entry
   */
  public ComparableEntryIterable(Iterable<Entry<K, V>> iter) {
    this.iter = iter;
  }

  @Override
  public Iterator<ComparableEntry<K, V>> iterator() {
    return new ComparableEntryIterator<K, V>(iter.iterator());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    int index = 0;
    for (ComparableEntry<K, V> item : this) {
      if (index == 0)
        sb.append(item);
      else
        sb.append(", " + item);
      index++;
    }
    sb.append("]");
    return sb.toString();
  }

}
