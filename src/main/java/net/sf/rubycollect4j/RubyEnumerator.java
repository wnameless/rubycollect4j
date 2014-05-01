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
import net.sf.rubycollect4j.util.PeekingIterator;

/**
 * 
 * {@link RubyEnumerator} implements most of the methods refer to the Enumerator
 * class of Ruby language.
 * <p>
 * {@link RubyEnumerator} is both Iterable and Iterator and it's also a peeking
 * iterator and a {@link Ruby.Enumerable}.
 * 
 * @param <E>
 *          the type of the elements
 */
public class RubyEnumerator<E> extends RubyEnumerable<E> implements
    Ruby.Enumerator<E> {

  private final Iterable<E> iter;
  private PeekingIterator<E> pIterator;

  /**
   * Returns a {@link RubyEnumerator} which wraps the given Iterable.
   * 
   * @param iter
   *          any Iterable
   * @return {@link RubyEnumerator}
   * @throws NullPointerException
   *           if iter is null
   */
  public static <E> RubyEnumerator<E> of(Iterable<E> iter) {
    if (iter == null)
      throw new NullPointerException();

    return new RubyEnumerator<E>(iter);
  }

  /**
   * Returns a {@link RubyEnumerator} which copies the elements of given
   * Iterable.
   * 
   * @param iter
   *          any Iterable
   * @return {@link RubyEnumerator}
   * @throws NullPointerException
   *           if iter is null
   */
  public static <E> RubyEnumerator<E> copyOf(Iterable<E> iter) {
    if (iter == null)
      throw new NullPointerException();

    return new RubyEnumerator<E>(RubyArray.copyOf(iter));
  }

  @Override
  protected Iterable<E> getIterable() {
    return iter;
  }

  /**
   * Creates a {@link RubyEnumerator} by given Iterable. It's a wrapper
   * implementation. No defensive copy has been made.
   * 
   * @param iterable
   *          any Iterable
   * @throws NullPointerException
   *           if iterable is null
   */
  public RubyEnumerator(Iterable<E> iterable) {
    if (iterable == null)
      throw new NullPointerException();

    iter = iterable;
    pIterator = new PeekingIterator<E>(iter.iterator());
  }

  /**
   * {@inheritDoc}
   * 
   * @return this {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> each(Block<? super E> block) {
    for (E item : iter) {
      block.yield(item);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   * 
   * @return this {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> rewind() {
    pIterator = new PeekingIterator<E>(iter.iterator());
    return this;
  }

  @Override
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

  @Override
  public String toString() {
    return "RubyEnumerator{" + iter + "}";
  }

}
