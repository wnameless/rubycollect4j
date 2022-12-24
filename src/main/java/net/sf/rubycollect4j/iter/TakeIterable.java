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

/**
 * 
 * {@link TakeIterable} iterates over the first n elements.
 * 
 * @param <E> the type of the elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class TakeIterable<E> implements Iterable<E> {

  private final Iterable<? extends E> iter;
  private final int n;

  /**
   * Creates a {@link TakeIterable}.
   * 
   * @param iter an Iterable
   * @param n number of elements to take
   * @throws NullPointerException if iter is null
   * @throws IllegalArgumentException if n is less than 0
   */
  public TakeIterable(Iterable<? extends E> iter, int n) {
    Objects.requireNonNull(iter);
    if (n < 0) throw new IllegalArgumentException("ArgumentError: attempt to take negative size");

    this.iter = iter;
    this.n = n;
  }

  @Override
  public Iterator<E> iterator() {
    return new TakeIterator<>(iter.iterator(), n);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    int index = 0;
    for (E item : this) {
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
