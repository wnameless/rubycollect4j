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
 * CycleIterator iterates an Iterable n times. If n is not given, it iterates
 * the Iterable forever.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class CycleIterator<E> implements Iterator<E> {

  private final Iterable<? extends E> iter;
  private Integer n;
  private Iterator<? extends E> it;

  /**
   * Creates a CycleIterator.
   * 
   * @param iter
   *          an Iterable
   * @throws NullPointerException
   *           if iter is null
   */
  public CycleIterator(Iterable<? extends E> iter) {
    if (iter == null)
      throw new NullPointerException();

    this.iter = iter;
    n = null;
    it = iter.iterator();
  }

  /**
   * Creates a CycleIterator.
   * 
   * @param iter
   *          an Iterable
   * @param n
   *          times to iterate
   * @throws NullPointerException
   *           if iter is null
   */
  public CycleIterator(Iterable<? extends E> iter, int n) {
    if (iter == null)
      throw new NullPointerException();

    this.iter = iter;
    this.n = n;
    it = iter.iterator();
  }

  private E nextElement() {
    if (!it.hasNext()) {
      it = iter.iterator();
      if (n != null)
        n--;
    }
    return it.next();
  }

  @Override
  public boolean hasNext() {
    boolean hasAny = iter.iterator().hasNext();
    if (n == null) {
      return hasAny;
    } else {
      if (!hasAny || n <= 0)
        return false;
      else
        return it.hasNext() || n > 1;
    }
  }

  @Override
  public E next() {
    if (!hasNext())
      throw new NoSuchElementException();

    return nextElement();
  }

  @Override
  public void remove() {
    it.remove();
  }

}
