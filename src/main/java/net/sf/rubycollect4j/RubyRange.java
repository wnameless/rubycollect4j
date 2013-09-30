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

import static net.sf.rubycollect4j.RubyCollections.newRubyEnumerator;
import static net.sf.rubycollect4j.RubyCollections.ra;
import net.sf.rubycollect4j.block.Block;
import net.sf.rubycollect4j.iter.RangeIterable;
import net.sf.rubycollect4j.iter.StepIterable;
import net.sf.rubycollect4j.succ.Successive;

/**
 * 
 * RubyRange is inspired by the Range class of Ruby. It does not exactly follow
 * the implementation of Ruby, especially on the range of alphanumeric strings.
 * All objects can use the RubyRange to create a range of interval. As long as
 * it provides the corresponding Successive object.
 * 
 * @param <E>
 *          the type of elements
 */
public final class RubyRange<E> extends RubyEnumerable<E> {

  private final RangeIterable<E> iter;
  private final Successive<E> successive;
  private final E startPoint;
  private final E endPoint;

  @Override
  protected Iterable<E> getIterable() {
    return iter;
  }

  /**
   * Creates a RubyRange of given elements.
   * 
   * @param successive
   *          a Successive object to provide the successor of elements
   * @param startPoint
   *          where the range begins
   * @param endPoint
   *          where the range ends
   * @throws NullPointerException
   *           if successive is null
   * @throws IllegalArgumentException
   *           if startPoint or endPoint is null
   */
  public RubyRange(Successive<E> successive, E startPoint, E endPoint) {
    if (successive == null)
      throw new NullPointerException();
    if (startPoint == null || endPoint == null)
      throw new IllegalArgumentException("ArgumentError: bad value for range");

    iter = new RangeIterable<E>(successive, startPoint, endPoint);
    this.successive = successive;
    this.startPoint = startPoint;
    this.endPoint = endPoint;
  }

  /**
   * Returns the beginning of this RubyRange.
   * 
   * @return the beginning of this RubyRange
   */
  public E begin() {
    return startPoint;
  }

  /**
   * Check if an item is within this RubyRange.
   * 
   * @param item
   *          to be checked
   * @return true if item is within this range, false otherwise
   */
  public boolean coverʔ(E item) {
    return successive.compare(startPoint, item) <= 0
        && successive.compare(endPoint, item) >= 0;
  }

  /**
   * Returns a RubyEnumerator of this RubyRange.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> each() {
    return newRubyEnumerator(iter);
  }

  /**
   * Yields each element of this RubyRange to the block.
   * 
   * @param block
   *          to yield each element
   * @return this RubyRange
   */
  public RubyRange<E> each(Block<E> block) {
    eachEntry(block);
    return this;
  }

  /**
   * Returns the end of this RubyRange.
   * 
   * @return the end of this RubyRange
   */
  public E end() {
    return endPoint;
  }

  /**
   * Equivalent to equals().
   * 
   * @param o
   *          any Object
   * @return true if 2 objects are equal, false otherwise
   */
  public boolean eqlʔ(Object o) {
    return equals(o);
  }

  /**
   * Equivalent to hashCode().
   * 
   * @return a int
   */
  public int hash() {
    return hashCode();
  }

  @Override
  public boolean includeʔ(E item) {
    return coverʔ(item);
  }

  /**
   * Equivalent to toString().
   * 
   * @return a String
   */
  public String inspect() {
    return toString();
  }

  /**
   * Returns the end of this RubyRange.
   * 
   * @return the end of this RubyRange
   */
  public E last() {
    return end();
  }

  /**
   * Returns the last n elements of this RubyRange.
   * 
   * @param n
   *          number of elements
   * @return a RubyArray
   */
  public RubyArray<E> last(int n) {
    RubyArray<E> lasts = ra();
    for (E item : iter) {
      if (lasts.size() < n) {
        lasts.add(item);
      } else {
        if (lasts.size() > 0) {
          lasts.remove(0);
          lasts.add(item);
        }
      }
    }
    return lasts;
  }

  @Override
  public boolean memberʔ(E item) {
    return includeʔ(item);
  }

  /**
   * Creates a RubyEnumerator by all nth elements of the range.
   * 
   * @param n
   *          interval to step
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> step(int n) {
    return newRubyEnumerator(new StepIterable<E>(iter, n));
  }

  /**
   * Iterates over this RubyRange and yields each nth element to the block.
   * 
   * @param n
   *          interval to step
   * @param block
   *          to yield each element
   * @return this RubyRange
   */
  public RubyRange<E> step(int n, Block<E> block) {
    for (E item : step(n)) {
      block.yield(item);
    }
    return this;
  }

  /**
   * Equivalent to toString().
   * 
   * @return a String
   */
  public String toS() {
    return toString();
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof RubyRange) {
      @SuppressWarnings("rawtypes")
      RubyRange rr = (RubyRange) o;
      return successive.equals(rr.successive)
          && startPoint.equals(rr.startPoint) && endPoint.equals(rr.endPoint);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hashCode = 27;
    hashCode = 31 * hashCode + successive.hashCode();
    hashCode = 31 * hashCode + startPoint.hashCode();
    hashCode = 31 * hashCode + endPoint.hashCode();
    return hashCode;
  }

  @Override
  public String toString() {
    return startPoint + ".." + endPoint;
  }

}
