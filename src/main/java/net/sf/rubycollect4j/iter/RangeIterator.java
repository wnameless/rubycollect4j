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

import static com.google.common.base.Preconditions.checkNotNull;

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
   * The constructor of the RangeIterator.
   * 
   * @param successive
   *          a Successive
   * @param startPoint
   *          a element
   * @param endPoint
   *          a element
   */
  public RangeIterator(Successive<E> successive, E startPoint, E endPoint) {
    this.successive = checkNotNull(successive);
    curr = checkNotNull(startPoint);
    this.endPoint = checkNotNull(endPoint);
  }

  @Override
  public boolean hasNext() {
    return successive.compare(curr, endPoint) <= 0;
  }

  @Override
  public E next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    E next = curr;
    curr = successive.succ(curr);
    return next;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
