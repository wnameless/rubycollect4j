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

import static net.sf.rubycollect4j.RubyCollections.newRubyArray;

import java.util.Iterator;
import java.util.function.BiPredicate;

import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.util.PeekingIterator;

/**
 * 
 * {@link SliceWhenIterator} iterates all elements by slicing elements into
 * different parts. It performs each slicing when any element is true returned
 * by the block.
 * 
 * @param <E>
 *          the type of the elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class SliceWhenIterator<E> implements Iterator<RubyArray<E>> {

  private final PeekingIterator<E> pIter;
  private final BiPredicate<? super E, ? super E> block;

  /**
   * Creates a {@link SliceWhenIterator}.
   * 
   * @param iter
   *          an Iterable
   * @param block
   *          to check elements
   * @throws NullPointerException
   *           if iter or block is null
   */
  public SliceWhenIterator(Iterator<? extends E> iter,
      BiPredicate<? super E, ? super E> block) {
    if (iter == null || block == null) throw new NullPointerException();

    pIter = new PeekingIterator<E>(iter);
    this.block = block;
  }

  private RubyArray<E> nextElement() {
    RubyArray<E> element = newRubyArray();
    do {
      element.add(pIter.next());
    } while (pIter.hasNext() && !block.test(element.last(), pIter.peek()));
    return element;
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
