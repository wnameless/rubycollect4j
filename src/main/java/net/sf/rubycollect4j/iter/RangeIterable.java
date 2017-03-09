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

import net.sf.rubycollect4j.RubyRange.Interval;
import net.sf.rubycollect4j.succ.Successive;

/**
 * 
 * {@link RangeIterable} iterates each element within a range derived by 2
 * objects.
 * 
 * @param <E>
 *          the type of the elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class RangeIterable<E> implements Iterable<E> {

  private final Successive<E> successive;
  private final E startPoint;
  private final E endPoint;
  private final Interval interval;

  /**
   * Creates a {@link RangeIterable}.
   * 
   * @param successive
   *          a Successive
   * @param startPoint
   *          an element
   * @param endPoint
   *          an element
   * @param interval
   *          an {@link Interval}
   * @throws NullPointerException
   *           if successive or startPoint or endPoint is null
   */
  public RangeIterable(Successive<E> successive, E startPoint, E endPoint,
      Interval interval) {
    if (successive == null || startPoint == null || endPoint == null
        || interval == null)
      throw new NullPointerException();

    this.successive = successive;
    this.startPoint = startPoint;
    this.endPoint = endPoint;
    this.interval = interval;
  }

  @Override
  public Iterator<E> iterator() {
    return new RangeIterator<E>(successive, startPoint, endPoint, interval);
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
