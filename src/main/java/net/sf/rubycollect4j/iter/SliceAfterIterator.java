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
import java.util.regex.Pattern;

import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.block.BooleanBlock;
import net.sf.rubycollect4j.util.PeekingIterator;

/**
 * 
 * {@link SliceAfterIterator} iterates all elements by slicing elements into
 * different parts. It ends each slice when any element is true returned by the
 * block or matched by the pattern.
 * 
 * @param <E>
 *          the type of the elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class SliceAfterIterator<E> implements Iterator<RubyArray<E>> {

  private final PeekingIterator<E> pIter;
  private final BooleanBlock<? super E> block;
  private final Pattern pattern;

  /**
   * Creates a {@link SliceAfterIterator}.
   * 
   * @param iter
   *          an Iterable
   * @param block
   *          to check elements
   * @throws NullPointerException
   *           if iter or block is null
   */
  public SliceAfterIterator(Iterator<? extends E> iter,
      BooleanBlock<? super E> block) {
    if (iter == null || block == null) throw new NullPointerException();

    pIter = new PeekingIterator<E>(iter);
    this.block = block;
    pattern = null;
  }

  /**
   * Creates a {@link SliceAfterIterator}.
   * 
   * @param iter
   *          an Iterator
   * @param pattern
   *          to match elements
   * @throws NullPointerException
   *           if iter or pattern is null
   */
  public SliceAfterIterator(Iterator<? extends E> iter, Pattern pattern) {
    if (iter == null || pattern == null) throw new NullPointerException();

    pIter = new PeekingIterator<E>(iter);
    block = null;
    this.pattern = pattern;
  }

  private RubyArray<E> nextElement() {
    RubyArray<E> element = newRubyArray();
    if (block != null) {
      do {
        element.add(pIter.next());
        if (block.yield(element.last())) return element;
      } while (pIter.hasNext() && !block.yield(pIter.peek()));
    } else {
      do {
        element.add(pIter.next());
        if (pattern.matcher(element.last().toString()).find()) return element;
      } while (pIter.hasNext()
          && !pattern.matcher(pIter.peek().toString()).find());
    }
    if (pIter.hasNext()) element.add(pIter.next());
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