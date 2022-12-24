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
package net.sf.rubycollect4j.iter;

import java.util.Iterator;
import java.util.function.Predicate;

import net.sf.rubycollect4j.util.PeekingIterator;

/**
 * 
 * {@link RejectIterator} filters all elements which are true returned by the block.
 * 
 * @param <E> the type of the elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class RejectIterator<E> implements Iterator<E> {

  private final PeekingIterator<E> pIter;
  private final Predicate<? super E> block;

  /**
   * Creates a {@link RejectIterator}.
   * 
   * @param iter an Iterator
   * @param block to check elements
   * @throws NullPointerException if iter or block is null
   */
  public RejectIterator(Iterator<? extends E> iter, Predicate<? super E> block) {
    if (iter == null || block == null) throw new NullPointerException();

    pIter = new PeekingIterator<>(iter);
    this.block = block;
  }

  private void advanceCursor() {
    while (pIter.hasNext() && block.test(pIter.peek())) {
      pIter.next();
    }
  }

  @Override
  public boolean hasNext() {
    advanceCursor();
    return pIter.hasNext();
  }

  @Override
  public E next() {
    advanceCursor();
    return pIter.next();
  }

  @Override
  public void remove() {
    pIter.remove();
  }

}
