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
import java.util.NoSuchElementException;

/**
 * 
 * PeekingIterator is an Iterator which provides a peek() method for user to
 * inspect an element advanced.
 * 
 * @param <E>
 *          the type of elements
 */
public final class PeekingIterator<E> implements Iterator<E> {

  private final Iterator<E> iterator;
  private E peek;
  private boolean hasPeek;

  /**
   * Creates a PeekingIterator.
   * 
   * @param iterator
   *          an Iterator
   */
  public PeekingIterator(Iterator<E> iterator) {
    this.iterator = iterator;
    if (this.iterator.hasNext()) {
      peek = this.iterator.next();
      hasPeek = true;
    } else {
      hasPeek = false;
    }
  }

  @Override
  public boolean hasNext() {
    return hasPeek;
  }

  @Override
  public E next() {
    E result = peek;
    if (!hasNext())
      throw new NoSuchElementException();

    if (iterator.hasNext()) {
      peek = iterator.next();
      hasPeek = true;
    } else {
      hasPeek = false;
    }
    return result;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

  /**
   * Checks an element advanced.
   * 
   * @return an element
   */
  public E peek() {
    if (!hasPeek)
      throw new NoSuchElementException();

    return peek;
  }

}
