/*
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

import net.sf.rubycollect4j.block.BooleanBlock;
import net.sf.rubycollect4j.util.PeekingIterator;

/**
 * 
 * {@link TakeWhileIterator} iterates over the elements until the returned value
 * by the block is false.
 * 
 * @param <E>
 *          the type of the elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class TakeWhileIterator<E> implements Iterator<E> {

  private final PeekingIterator<E> pIter;
  private final BooleanBlock<? super E> block;

  /**
   * Creates a {@link TakeWhileIterator}.
   * 
   * @param iter
   *          an Iterable
   * @param block
   *          to check elements
   * @throws NullPointerException
   *           if iter or block is null
   */
  public TakeWhileIterator(Iterator<? extends E> iter,
      BooleanBlock<? super E> block) {
    if (iter == null || block == null) throw new NullPointerException();

    pIter = new PeekingIterator<E>(iter);
    this.block = block;
  }

  @Override
  public boolean hasNext() {
    return pIter.hasNext() && block.yield(pIter.peek());
  }

  @Override
  public E next() {
    if (!hasNext()) throw new NoSuchElementException();

    return pIter.next();
  }

  @Override
  public void remove() {
    pIter.remove();
  }

}
