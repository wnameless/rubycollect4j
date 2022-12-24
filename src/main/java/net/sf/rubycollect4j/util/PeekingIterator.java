/*
 *
 * Copyright 2013 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package net.sf.rubycollect4j.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * {@link PeekingIterator} is an Iterator which provides a peek() method for user to peek an element
 * advanced.
 * 
 * @param <E> the type of elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class PeekingIterator<E> implements Iterator<E> {

  private final Iterator<? extends E> iterator;
  private E peek;
  private boolean hasPeek = false;

  /**
   * Creates a {@link PeekingIterator}.
   * 
   * @param iterator an Iterator
   */
  public PeekingIterator(Iterator<? extends E> iterator) {
    this.iterator = iterator;
  }

  private void peeking() {
    peek = iterator.next();
    hasPeek = true;
  }

  @Override
  public boolean hasNext() {
    return hasPeek || iterator.hasNext();
  }

  @Override
  public E next() {
    if (!hasNext()) throw new NoSuchElementException();

    if (hasPeek) {
      hasPeek = false;
      return peek;
    } else {
      peeking();
      return next();
    }
  }

  @Override
  public void remove() {
    if (hasPeek) throw new IllegalStateException();

    iterator.remove();
  }

  /**
   * Peeks an element advanced. Warning: remove() is temporarily out of function after a peek()
   * until a next() is called.
   * 
   * @return element
   */
  public E peek() {
    if (!hasPeek && hasNext()) peeking();
    if (!hasPeek) throw new NoSuchElementException();

    return peek;
  }

}
