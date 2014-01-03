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

/**
 * 
 * DropIterator drops first n elements before iterating elements.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class DropIterator<E> implements Iterator<E> {

  private final Iterator<E> iter;
  private boolean hasFirstCall = false;

  /**
   * Creates a DropIterator.
   * 
   * @param iter
   *          an Iterator
   * @param drop
   *          number of elements to drop
   * @throws NullPointerException
   *           if iter is null
   * @throws IllegalArgumentException
   *           if step is less than or equal to 0
   */
  public DropIterator(Iterator<E> iter, int drop) {
    if (iter == null)
      throw new NullPointerException();
    if (drop < 0)
      throw new IllegalArgumentException(
          "ArgumentError: attempt to drop negative size");

    while (iter.hasNext() && drop > 0) {
      iter.next();
      drop--;
    }
    this.iter = iter;
  }

  @Override
  public boolean hasNext() {
    return iter.hasNext();
  }

  @Override
  public E next() {
    hasFirstCall = true;
    return iter.next();
  }

  @Override
  public void remove() {
    if (!hasFirstCall)
      throw new IllegalStateException();

    iter.remove();
  }

}
