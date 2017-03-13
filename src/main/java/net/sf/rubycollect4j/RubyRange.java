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
package net.sf.rubycollect4j;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

import net.sf.rubycollect4j.iter.RangeIterable;
import net.sf.rubycollect4j.iter.StepIterable;
import net.sf.rubycollect4j.succ.Successive;

/**
 * 
 * {@link RubyRange} is inspired by the Range class of Ruby. It does not exactly
 * follow the implementation of Ruby, especially on the range of alphanumeric
 * strings. All objects can use the {@link RubyRange} to create a range of
 * interval. As long as it provides the corresponding Successive object.
 * <p>
 * {@link RubyRange} is also a {@link RubyBase.Enumerable}.
 * 
 * @param <E>
 *          the type of elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class RubyRange<E> implements RubyEnumerable<E>, Serializable {

  public enum Interval {

    CLOSED, OPEN, CLOSED_OPEN, OPEN_CLOSED;

  }

  private static final long serialVersionUID = 1L;

  private final RangeIterable<E> iter;
  private final Successive<E> successive;
  private final E startPoint;
  private final E endPoint;
  private final Interval interval;

  /**
   * Creates a {@link RubyRange} of given elements.
   * 
   * @param successive
   *          a Successive object to provide the successor of elements
   * @param startPoint
   *          where the range begins
   * @param endPoint
   *          where the range ends
   * @param interval
   *          an {@link Interval}
   * @throws NullPointerException
   *           if successive or interval is null
   * @throws IllegalArgumentException
   *           if startPoint or endPoint is null
   */
  public RubyRange(Successive<E> successive, E startPoint, E endPoint,
      Interval interval) {
    Objects.requireNonNull(successive);
    Objects.requireNonNull(interval);
    if (startPoint == null || endPoint == null)
      throw new IllegalArgumentException("ArgumentError: bad value for range");

    iter = new RangeIterable<>(successive, startPoint, endPoint, interval);
    this.successive = successive;
    this.startPoint = startPoint;
    this.endPoint = endPoint;
    this.interval = interval;
  }

  /**
   * Creates a {@link RubyRange} with closed interval of the same start and end
   * points.
   * 
   * @return new {@link RubyRange}
   */
  public RubyRange<E> closed() {
    return new RubyRange<>(successive, startPoint, endPoint, Interval.CLOSED);
  }

  /**
   * Returns a {@link RubyRange} with closed-open interval of the same start and
   * end points.
   * 
   * @return new {@link RubyRange}
   */
  public RubyRange<E> closedOpen() {
    return new RubyRange<>(successive, startPoint, endPoint,
        Interval.CLOSED_OPEN);
  }

  /**
   * Returns a {@link RubyRange} with open interval of the same start and end
   * points.
   * 
   * @return new {@link RubyRange}
   */
  public RubyRange<E> open() {
    return new RubyRange<>(successive, startPoint, endPoint, Interval.OPEN);
  }

  /**
   * Returns a {@link RubyRange} with open-closed interval of the same start and
   * end points.
   * 
   * @return new {@link RubyRange}
   */
  public RubyRange<E> openClosed() {
    return new RubyRange<>(successive, startPoint, endPoint,
        Interval.OPEN_CLOSED);
  }

  /**
   * Returns the beginning of this {@link RubyRange}.
   * 
   * @return the beginning of this {@link RubyRange}
   */
  public E begin() {
    return startPoint;
  }

  /**
   * Check if an item is within this {@link RubyRange}.
   * 
   * @param item
   *          to be checked
   * @return true if item is within this range, false otherwise
   */
  public boolean coverʔ(E item) {
    switch (interval) {
      case CLOSED_OPEN:
        return successive.compare(startPoint, item) <= 0
            && successive.compare(endPoint, item) > 0;
      case OPEN_CLOSED:
        return successive.compare(startPoint, item) < 0
            && successive.compare(endPoint, item) >= 0;
      case OPEN:
        return successive.compare(startPoint, item) < 0
            && successive.compare(endPoint, item) > 0;
      default: // CLOSED
        return successive.compare(startPoint, item) <= 0
            && successive.compare(endPoint, item) >= 0;
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @return this {@link RubyRange}
   */
  @Override
  public RubyRange<E> each(Consumer<? super E> block) {
    eachEntry(block);
    return this;
  }

  /**
   * Returns the end of this {@link RubyRange}.
   * 
   * @return the end of this {@link RubyRange}
   */
  public E end() {
    return endPoint;
  }

  /**
   * Equivalent to {@link #equals(Object)}.
   * 
   * @param o
   *          any Object
   * @return true if 2 objects are equal, false otherwise
   */
  public boolean eqlʔ(Object o) {
    return equals(o);
  }

  /**
   * Equivalent to {@link #hashCode()}.
   * 
   * @return the hash code
   */
  public int hash() {
    return hashCode();
  }

  @Override
  public boolean includeʔ(E item) {
    return coverʔ(item);
  }

  /**
   * Equivalent to {@link #toString()}.
   * 
   * @return a String
   */
  public String inspect() {
    return toString();
  }

  /**
   * Returns the end of this {@link RubyRange}.
   * 
   * @return the end of this {@link RubyRange}
   */
  public E last() {
    return end();
  }

  /**
   * Returns the last n elements of this {@link RubyRange}.
   * 
   * @param n
   *          number of elements
   * @return a {@link RubyArray}
   */
  public RubyArray<E> last(int n) {
    RubyArray<E> lasts = Ruby.Array.create();
    for (E item : iter) {
      if (lasts.size() < n) {
        lasts.add(item);
      } else if (lasts.size() > 0) {
        lasts.remove(0);
        lasts.add(item);
      }
    }
    return lasts;
  }

  @Override
  public boolean memberʔ(E item) {
    return includeʔ(item);
  }

  /**
   * Creates a {@link RubyEnumerator} by all nth elements of the range.
   * 
   * @param n
   *          interval to step
   * @return a {@link RubyEnumerator}
   */
  public RubyEnumerator<E> step(int n) {
    return Ruby.Enumerator.of(new StepIterable<>(iter, n));
  }

  /**
   * Iterates over this {@link RubyRange} and yields each nth element to the
   * block.
   * 
   * @param n
   *          interval to step
   * @param block
   *          to yield each element
   * @return this {@link RubyRange}
   */
  public RubyRange<E> step(int n, Consumer<E> block) {
    for (E item : step(n)) {
      block.accept(item);
    }
    return this;
  }

  /**
   * Equivalent to {@link #toString()}.
   * 
   * @return a String
   */
  public String toS() {
    return toString();
  }

  @Override
  public Iterator<E> iterator() {
    return iter.iterator();
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof RubyRange) {
      @SuppressWarnings("rawtypes")
      RubyRange rr = (RubyRange) o;
      return successive.equals(rr.successive)
          && startPoint.equals(rr.startPoint) && endPoint.equals(rr.endPoint)
          && interval.equals(rr.interval);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hashCode = 27;
    hashCode = 31 * hashCode + successive.hashCode();
    hashCode = 31 * hashCode + startPoint.hashCode();
    hashCode = 31 * hashCode + endPoint.hashCode();
    hashCode = 31 * hashCode + interval.hashCode();
    return hashCode;
  }

  @Override
  public String toString() {
    return "RubyRange{startPoint=" + startPoint + ", endPoint=" + endPoint
        + ", interval=" + interval + "}";
  }

}
