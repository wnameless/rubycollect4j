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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

  @Override
  protected Iterable<E> getIterable() {
    return iter;
  }

  /**
   * Creates a {@link RubyEnumerator} by given Iterable.
   * 
   * @param iterable
   *          an Iterable
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
   * Creates a {@link RubyEnumerator} by given Iterator. This Iterator will be
   * converted into an Iterable. In other words, a copy will be made.
   * 
   * @param iterator
   *          an Iterator
   * @throws NullPointerException
   *           if iterator is null
   */
  public RubyEnumerator(Iterator<E> iterator) {
    if (iterator == null)
      throw new NullPointerException();

    List<E> list = new ArrayList<E>();
    while (iterator.hasNext()) {
      list.add(iterator.next());
    }
    iter = list;
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
