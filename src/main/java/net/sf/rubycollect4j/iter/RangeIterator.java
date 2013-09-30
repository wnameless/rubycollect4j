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

import net.sf.rubycollect4j.succ.Successive;

/**
 * 
 * RangeIterator iterates each element within a range derived by 2 objects.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class RangeIterator<E> implements Iterator<E> {

  private final Successive<E> successive;
  private final E endPoint;
  private E curr;

  /**
   * Creates an RangeIterator.
   * 
   * @param successive
   *          a Successive
   * @param startPoint
   *          an element
   * @param endPoint
   *          an element
   * @throws NullPointerException
   *           if successive or startPoint or endPoint is null
   */
  public RangeIterator(Successive<E> successive, E startPoint, E endPoint) {
    if (successive == null || startPoint == null || endPoint == null)
      throw new NullPointerException();

    this.successive = successive;
    curr = startPoint;
    this.endPoint = endPoint;
  }

  @Override
  public boolean hasNext() {
    return successive.compare(curr, endPoint) <= 0;
  }

  @Override
  public E next() {
    if (!hasNext())
      throw new NoSuchElementException();

    E next = curr;
    curr = successive.succ(curr);
    return next;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
