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
import java.util.regex.Pattern;

import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.block.BooleanBlock;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import static com.google.common.base.Preconditions.checkNotNull;
import static net.sf.rubycollect4j.RubyCollections.newRubyArray;

/**
 * 
 * SliceBeforeIterator iterates all elements by slicing elements into different
 * parts. It performs each slicing when any element is true returned by the
 * block or matched by the pattern.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class SliceBeforeIterator<E> implements Iterator<RubyArray<E>> {

  private final PeekingIterator<E> pIter;
  private final BooleanBlock<E> block;
  private final Pattern pattern;

  /**
   * The constructor of the SliceBeforeIterator.
   * 
   * @param iter
   *          an Iterable
   * @param block
   *          to check elements
   */
  public SliceBeforeIterator(Iterator<E> iter, BooleanBlock<E> block) {
    pIter = Iterators.peekingIterator(checkNotNull(iter));
    this.block = checkNotNull(block);
    pattern = null;
  }

  /**
   * The constructor of the SliceBeforeIterator.
   * 
   * @param iter
   *          an Iterator
   * @param pattern
   *          to match elements
   */
  public SliceBeforeIterator(Iterator<E> iter, Pattern pattern) {
    pIter = Iterators.peekingIterator(checkNotNull(iter));
    block = null;
    this.pattern = checkNotNull(pattern);
  }

  private RubyArray<E> nextElement() {
    RubyArray<E> element = newRubyArray();
    if (block != null) {
      do {
        element.add(pIter.next());
      } while (pIter.hasNext() && !block.yield(pIter.peek()));
    } else {
      do {
        element.add(pIter.next());
      } while (pIter.hasNext()
          && !pattern.matcher(pIter.peek().toString()).find());
    }
    return element;
  }

  @Override
  public boolean hasNext() {
    return pIter.hasNext();
  }

  @Override
  public RubyArray<E> next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return nextElement();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
