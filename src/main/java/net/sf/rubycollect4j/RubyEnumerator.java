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
package net.sf.rubycollect4j;

import java.util.Iterator;

import net.sf.rubycollect4j.block.Block;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import static com.google.common.collect.Lists.newArrayList;

/**
 * 
 * RubyEnumerator implements most of the methods refer to the Enumerator of Ruby
 * language. RubyEnumerator is both Iterable and Iterator and it's also a
 * peeking iterator.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class RubyEnumerator<E> extends RubyEnumerable<E> implements
    Iterable<E>, Iterator<E> {

  private PeekingIterator<E> pIterator;

  /**
   * Creates a RubyEnumerator by given Iterable.
   * 
   * @param iter
   *          an Iterable
   * @return a new RubyEnumerator
   */
  public static <E> RubyEnumerator<E> newRubyEnumerator(Iterable<E> iter) {
    return new RubyEnumerator<E>(iter);
  }

  /**
   * Creates a RubyEnumerator by given Iterator.
   * 
   * @param iter
   *          an Iterator
   * @return a new RubyEnumerator
   */
  public static <E> RubyEnumerator<E> newRubyEnumerator(Iterator<E> iter) {
    return new RubyEnumerator<E>(iter);
  }

  /**
   * Creates a RubyEnumerator by given Iterable.
   * 
   * @param iter
   *          an Iterable
   */
  public RubyEnumerator(Iterable<E> iter) {
    super(iter);
    pIterator = Iterators.peekingIterator(super.iterator());
  }

  /**
   * Creates a RubyEnumerator by given Iterator.
   * 
   * @param iter
   *          an Iterator
   */
  public RubyEnumerator(Iterator<E> iter) {
    super(newArrayList(iter));
    pIterator = Iterators.peekingIterator(super.iterator());
  }

  /**
   * Returns a RubyEnumerator which is self.
   * 
   * @return this RubyEnumerator
   */
  public RubyEnumerator<E> each() {
    return this;
  }

  /**
   * Yields each element to the block.
   * 
   * @param block
   *          to yield each element
   * @return this RubyEnumerator
   */
  public RubyEnumerator<E> each(Block<E> block) {
    for (E item : iter) {
      block.yield(item);
    }
    return this;
  }

  /**
   * Resets the iterator of this RubyEnumerator to the beginning.
   * 
   * @return this RubyEnumerator
   */
  public RubyEnumerator<E> rewind() {
    pIterator = Iterators.peekingIterator(super.iterator());
    return this;
  }

  /**
   * Returns the next element without advancing the iteration.
   * 
   * @return an element
   */
  public E peek() {
    return pIterator.peek();
  }

  @Override
  public Iterator<E> iterator() {
    return iter.iterator();
  }

  @Override
  public boolean hasNext() {
    return pIterator.hasNext();
  }

  @Override
  public E next() {
    return pIterator.next();
  }

  @Override
  public void remove() {
    pIterator.remove();
  }

}
