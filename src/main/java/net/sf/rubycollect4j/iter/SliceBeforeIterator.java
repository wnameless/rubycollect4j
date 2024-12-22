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
import java.util.regex.Pattern;
import net.sf.rubycollect4j.Ruby;
import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.util.PeekingIterator;

/**
 * 
 * {@link SliceBeforeIterator} iterates all elements by slicing elements into different parts. It
 * performs each slicing when any element is true returned by the block or matched by the pattern.
 * 
 * @param <E> the type of the elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class SliceBeforeIterator<E> implements Iterator<RubyArray<E>> {

  private final PeekingIterator<E> pIter;
  private final Predicate<? super E> block;
  private final Pattern pattern;

  /**
   * Creates a {@link SliceBeforeIterator}.
   * 
   * @param iter an Iterable
   * @param block to check elements
   * @throws NullPointerException if iter or block is null
   */
  public SliceBeforeIterator(Iterator<? extends E> iter, Predicate<? super E> block) {
    if (iter == null || block == null) throw new NullPointerException();

    pIter = new PeekingIterator<>(iter);
    this.block = block;
    pattern = null;
  }

  /**
   * Creates a {@link SliceBeforeIterator}.
   * 
   * @param iter an Iterator
   * @param pattern to match elements
   * @throws NullPointerException if iter or pattern is null
   */
  public SliceBeforeIterator(Iterator<? extends E> iter, Pattern pattern) {
    if (iter == null || pattern == null) throw new NullPointerException();

    pIter = new PeekingIterator<>(iter);
    block = null;
    this.pattern = pattern;
  }

  private RubyArray<E> nextElement() {
    RubyArray<E> element = Ruby.Array.create();
    if (block != null) {
      do {
        element.add(pIter.next());
      } while (pIter.hasNext() && !block.test(pIter.peek()));
    } else {
      do {
        element.add(pIter.next());
      } while (pIter.hasNext() && !pattern.matcher(pIter.peek().toString()).find());
    }
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
