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

import static net.sf.rubycollect4j.RubyCollections.newRubyArray;

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.sf.rubycollect4j.RubyArray;

/**
 * 
 * EachSliceIterator iterates each element by a window of size n. It returns a
 * RubyArray which includes n consecutive elements within this window, then it
 * moves the position to the very next element behind the window and so on.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class EachSliceIterator<E> implements Iterator<RubyArray<E>> {

  private final Iterator<E> iter;
  private final int size;

  /**
   * The constructor of the EachSliceIterator.
   * 
   * @param iter
   *          an Iterabel
   * @param size
   *          of the window
   * @throws NullPointerException
   *           if iter is null
   * @throws IllegalArgumentException
   *           if size less than or equal to 0
   */
  public EachSliceIterator(Iterator<E> iter, int size) {
    if (iter == null)
      throw new NullPointerException();
    if (size <= 0)
      throw new IllegalArgumentException("ArgumentError: invalid slice size");

    this.iter = iter;
    this.size = size;
  }

  private RubyArray<E> nextElement() {
    RubyArray<E> bucket = newRubyArray();
    while (iter.hasNext() && bucket.size() < size) {
      bucket.add(iter.next());
    }
    return bucket;
  }

  @Override
  public boolean hasNext() {
    return iter.hasNext();
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
