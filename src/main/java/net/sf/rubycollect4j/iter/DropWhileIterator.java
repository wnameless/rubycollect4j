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

import net.sf.rubycollect4j.block.BooleanBlock;
import net.sf.rubycollect4j.util.PeekingIterator;

/**
 * 
 * DropWhileIterator drops elements until the returned value of the block is
 * false before iterating elements.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class DropWhileIterator<E> implements Iterator<E> {

  private final PeekingIterator<E> pIter;
  private boolean hasFirstCall = false;

  /**
   * Creates a DropWhileIterator.
   * 
   * @param iter
   *          an Iterator
   * @param block
   *          to check elements
   * @throws NullPointerException
   *           if iter or block is null
   */
  public DropWhileIterator(Iterator<E> iter, BooleanBlock<E> block) {
    if (iter == null || block == null)
      throw new NullPointerException();

    pIter = new PeekingIterator<E>(iter);
    while (pIter.hasNext() && block.yield(pIter.peek())) {
      pIter.next();
    }
  }

  @Override
  public boolean hasNext() {
    return pIter.hasNext();
  }

  @Override
  public E next() {
    hasFirstCall = true;
    return pIter.next();
  }

  @Override
  public void remove() {
    if (!hasFirstCall)
      throw new IllegalStateException();

    pIter.remove();
  }

}
