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
import java.util.Objects;
import net.sf.rubycollect4j.RubyArray;

/**
 * 
 * {@link EachSliceIterable} iterates each element by a window of size n. It returns a
 * {@link RubyArray} which includes n consecutive elements within this window, then it moves the
 * position to the very next element behind the window and so on.
 * 
 * @param <E> the type of the elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class EachSliceIterable<E> implements Iterable<RubyArray<E>> {

  private final Iterable<? extends E> iter;
  private final int size;

  /**
   * Creates an {@link EachSliceIterable}.
   * 
   * @param iter an Iterable
   * @param size of the window
   * @throws NullPointerException if iter is null
   * @throws IllegalArgumentException if size is less than or equal to 0
   */
  public EachSliceIterable(Iterable<? extends E> iter, int size) {
    Objects.requireNonNull(iter);
    if (size <= 0) throw new IllegalArgumentException("ArgumentError: invalid slice size");

    this.iter = iter;
    this.size = size;
  }

  @Override
  public Iterator<RubyArray<E>> iterator() {
    return new EachSliceIterator<>(iter.iterator(), size);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    int index = 0;
    for (RubyArray<E> item : this) {
      if (index == 0)
        sb.append(item);
      else
        sb.append(", " + item);
      index++;
    }
    sb.append("]");
    return sb.toString();
  }

}
