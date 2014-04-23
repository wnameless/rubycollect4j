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
 * {@link EachWithIndexIterator} iterates each element with its index.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class EachWithIndexIterator<E> implements
    Iterator<Entry<E, Integer>> {

  private final Iterator<? extends E> iter;
  private int index = 0;

  /**
   * Creates an {@link EachWithIndexIterator}.
   * 
   * @param iter
   *          an Iterator
   * @throws NullPointerException
   *           if iter is null
   */
  public EachWithIndexIterator(Iterator<? extends E> iter) {
    if (iter == null)
      throw new NullPointerException();

    this.iter = iter;
  }

  @Override
  public boolean hasNext() {
    return iter.hasNext();
  }

  @Override
  public Entry<E, Integer> next() {
    return new ComparableEntry<E, Integer>(iter.next(), index++);
  }

  @Override
  public void remove() {
    iter.remove();
  }

}
