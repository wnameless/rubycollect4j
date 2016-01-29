/*
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

/**
 * 
 * {@link DropIterable} drops first n elements before iterating elements.
 * 
 * @param <E>
 *          the type of the elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class DropIterable<E> implements Iterable<E> {

  private final Iterable<? extends E> iter;
  private final int drop;

  /**
   * Creates a {@link DropIterable}.
   * 
   * @param iter
   *          an Iterable
   * @param drop
   *          number of elements to drop
   * @throws NullPointerException
   *           if iter is null
   * @throws IllegalArgumentException
   *           if step is less than or equal to 0
   */
  public DropIterable(Iterable<? extends E> iter, int drop) {
    if (iter == null) throw new NullPointerException();
    if (drop < 0) throw new IllegalArgumentException(
        "ArgumentError: attempt to drop negative size");

    this.iter = iter;
    this.drop = drop;
  }

  @Override
  public Iterator<E> iterator() {
    return new DropIterator<E>(iter.iterator(), drop);
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
