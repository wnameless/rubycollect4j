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
import java.util.NoSuchElementException;
import java.util.Objects;
import net.sf.rubycollect4j.Ruby;
import net.sf.rubycollect4j.RubyArray;

/**
 * 
 * {@link EachConsIterator} iterates each element by a window of size n. It returns a
 * {@link RubyArray} which includes n consecutive elements within this window.
 * 
 * @param <E> the type of the elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class EachConsIterator<E> implements Iterator<RubyArray<E>> {

  private final Iterator<? extends E> iter;
  private final int size;
  private final RubyArray<E> bucket = Ruby.Array.create();

  /**
   * Creates an {@link EachConsIterator}.
   * 
   * @param iter an Iterator
   * @param size of the window
   * @throws NullPointerException if iter is null
   * @throws IllegalArgumentException if size is less than or equal to 0
   */
  public EachConsIterator(Iterator<? extends E> iter, int size) {
    Objects.requireNonNull(iter);
    if (size <= 0) throw new IllegalArgumentException("ArgumentError: invalid size");

    this.iter = iter;
    this.size = size;
    fillBucket();
  }

  private void fillBucket() {
    while (iter.hasNext() && bucket.size() < size) {
      bucket.add(iter.next());
    }
  }

  private void updateBucket() {
    bucket.deleteAt(0);
    if (iter.hasNext()) bucket.add(iter.next());
  }

  private RubyArray<E> nextElement() {
    RubyArray<E> element = RubyArray.copyOf(bucket);
    updateBucket();
    return element;
  }

  @Override
  public boolean hasNext() {
    return bucket.size() == size;
  }

  @Override
  public RubyArray<E> next() {
    if (!hasNext()) throw new NoSuchElementException();

    return nextElement();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
