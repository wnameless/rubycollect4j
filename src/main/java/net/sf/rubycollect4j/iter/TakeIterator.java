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
import java.util.NoSuchElementException;

/**
 * 
 * {@link TakeIterator} drops first n elements before iterating elements.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class TakeIterator<E> implements Iterator<E> {

  private final Iterator<? extends E> iter;
  private int n;

  /**
   * Creates a {@link TakeIterator}.
   * 
   * @param iter
   *          an Iterator
   * @param n
   *          number of elements to take
   * @throws NullPointerException
   *           if iter is null
   * @throws IllegalArgumentException
   *           if n is less than 0
   */
  public TakeIterator(Iterator<? extends E> iter, int n) {
    if (iter == null)
      throw new NullPointerException();
    if (n < 0)
      throw new IllegalArgumentException(
          "ArgumentError: attempt to take negative size");

    this.iter = iter;
    this.n = n;
  }

  @Override
  public boolean hasNext() {
    return iter.hasNext() && n > 0;
  }

  @Override
  public E next() {
    if (!hasNext())
      throw new NoSuchElementException();

    n--;
    return iter.next();
  }

  @Override
  public void remove() {
    iter.remove();
  }

}
