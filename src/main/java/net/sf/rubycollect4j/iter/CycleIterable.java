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

import java.util.Iterator;

/**
 * 
 * CycleIterable iterates an Iterable n times. If n is not given, it iterates
 * the Iterable forever.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class CycleIterable<E> implements Iterable<E> {

  private final Iterable<E> iter;
  private final Integer n;

  /**
   * Creates a CycleIterable.
   * 
   * @param iter
   *          an Iterable
   * @throws NullPointerException
   *           if iter is null
   */
  public CycleIterable(Iterable<E> iter) {
    if (iter == null)
      throw new NullPointerException();

    this.iter = iter;
    n = null;
  }

  /**
   * Creates a CycleIterable.
   * 
   * @param iter
   *          an Iterable
   * @param n
   *          times to iterate
   * @throws NullPointerException
   *           if iter is null
   */
  public CycleIterable(Iterable<E> iter, int n) {
    if (iter == null)
      throw new NullPointerException();

    this.iter = iter;
    this.n = n;
  }

  @Override
  public Iterator<E> iterator() {
    if (n == null)
      return new CycleIterator<E>(iter);
    else
      return new CycleIterator<E>(iter, n);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    if (n != null) {
      int index = 0;
      for (E item : this) {
        if (index == 0)
          sb.append(item);
        else
          sb.append(", " + item);
        index++;
      }
      sb.append("]");
    } else {
      int index = 0;
      for (E item : iter) {
        if (index == 0)
          sb.append(item);
        else
          sb.append(", " + item);
        index++;
      }
      sb.append("...]");
    }
    return sb.toString();
  }

}
