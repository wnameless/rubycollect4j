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
 * peek an element advanced.
 * 
 * @param <E>
 *          the type of elements
 */
public final class PeekingIterator<E> implements Iterator<E> {

  private final Iterator<E> iterator;
  private E peek;
  private boolean hasPeek;
  private boolean removable = false;

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
    return hasPeek || iterator.hasNext();
  }

  @Override
  public E next() {
    if (!hasNext())
      throw new NoSuchElementException();

    if (hasPeek) {
      hasPeek = false;
      removable = true;
      return peek;
    } else {
      if (iterator.hasNext()) {
        peek = iterator.next();
        hasPeek = true;
      }
      return next();
    }
  }

  @Override
  public void remove() {
    if (!removable)
      throw new IllegalStateException();

    iterator.remove();
    removable = false;
  }

  /**
   * Peeks an element advanced. Warning: remove() is temporarily out of function
   * after a peek() until a next() is called.
   * 
   * @return an element
   */
  public E peek() {
    if (!hasPeek && iterator.hasNext()) {
      peek = iterator.next();
      hasPeek = true;
      removable = false;
    }
    if (!hasPeek)
      throw new NoSuchElementException();

    return peek;
  }

}
