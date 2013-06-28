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

import net.sf.rubycollect4j.range.Successive;

/**
 * 
 * RangeIterable iterates each element within a range defined by 2 Comparable
 * elements.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class RangeIterable<E extends Comparable<E>> implements
    Iterable<E> {

  private final Successive<E> successive;
  private final E startPoint;
  private final E endPoint;

  /**
   * The constructor of the RangeIterable.
   * 
   * @param successive
   *          a Successive
   * @param startPoint
   *          a element
   * @param endPoint
   *          a element
   */
  public RangeIterable(Successive<E> successive, E startPoint, E endPoint) {
    this.successive = successive;
    this.startPoint = startPoint;
    this.endPoint = endPoint;
  }

  @Override
  public Iterator<E> iterator() {
    return new RangeIterator<E>(successive, startPoint, endPoint);
  }

}
