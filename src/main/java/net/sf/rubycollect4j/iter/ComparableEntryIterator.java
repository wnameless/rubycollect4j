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
 * {@link ComparableEntryIterator} iterates an Iterator of Entry and wraps each
 * Entry into a {@link ComparableEntry}.
 * 
 * @param <K>
 *          the type of the key elements
 * @param <V>
 *          the type of the value elements
 */
public final class ComparableEntryIterator<K, V> implements
    Iterator<Entry<K, V>> {

  private final Iterator<? extends Entry<? extends K, ? extends V>> iter;

  /**
   * Creates a {@link ComparableEntryIterator}.
   * 
   * @param iter
   *          an Iterator of Entry
   */
  public ComparableEntryIterator(
      Iterator<? extends Entry<? extends K, ? extends V>> iter) {
    this.iter = iter;
  }

  @Override
  public boolean hasNext() {
    return iter.hasNext();
  }

  @Override
  public Entry<K, V> next() {
    return new ComparableEntry<K, V>(iter.next());
  }

  @Override
  public void remove() {
    iter.remove();
  }

}
