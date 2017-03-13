/*
 *
 * Copyright 2016 Wei-Ming Wu
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
import java.util.function.BiPredicate;

import net.sf.rubycollect4j.Ruby;
import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.util.PeekingIterator;

/**
 * 
 * {@link ChunkWhileIterator} processes elements with given block first, and
 * then puts the original elements together if elements which are next to each
 * others have the true returned value. Chuncked elements are put into
 * {@link RubyArray}s.
 * 
 * @param <E>
 *          the type of the elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class ChunkWhileIterator<E> implements Iterator<RubyArray<E>> {

  private final PeekingIterator<E> pIter;
  private final BiPredicate<? super E, ? super E> block;

  /**
   * Creates a {@link ChunkWhileIterator}.
   * 
   * @param iter
   *          an Iterator
   * @param block
   *          to define which elements to be chunked
   * @throws NullPointerException
   *           if iterator or block is null
   */
  public ChunkWhileIterator(Iterator<? extends E> iter,
      BiPredicate<? super E, ? super E> block) {
    if (iter == null || block == null) throw new NullPointerException();

    pIter = new PeekingIterator<E>(iter);
    this.block = block;
  }

  private RubyArray<E> nextElement() {
    RubyArray<E> bucket = Ruby.Array.create();
    E left = pIter.next();
    bucket.add(left);
    if (pIter.hasNext()) {
      E right = pIter.peek();
      while (pIter.hasNext() && block.test(left, right)) {
        bucket.add(right);
        left = pIter.next();
        if (pIter.hasNext()) right = pIter.peek();
      }
    }
    return bucket;
  }

  @Override
  public boolean hasNext() {
    return pIter.hasNext();
  }

  @Override
  public RubyArray<E> next() {
    return nextElement();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
