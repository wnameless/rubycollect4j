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

import net.sf.rubycollect4j.iter.RangeIterable;
import net.sf.rubycollect4j.range.DoubleSuccessor;
import net.sf.rubycollect4j.range.IntegerSuccessor;
import net.sf.rubycollect4j.range.LongSuccessor;
import net.sf.rubycollect4j.range.StringSuccessor;
import net.sf.rubycollect4j.range.Successive;

/**
 * 
 * RubyRange is inspired by the Range class of Ruby. It does not exactly follow
 * the implementation of Ruby, especially on the range of alphanumeric strings.
 * All Comparable objects can use the RubyRange to create a range of interval.
 * As long as it provides the corresponding Successive object.
 * 
 * @param <E>
 *          the type of elements
 */
public final class RubyRange<E extends Comparable<E>> extends RubyEnumerable<E> {

  /**
   * Creates a RubyRange of given integers.
   * 
   * @param startPoint
   *          where the range begins
   * @param endPoint
   *          where the range ends
   * @return a RubyRange
   */
  public static RubyRange<Integer> range(int startPoint, int endPoint) {
    return new RubyRange<Integer>(IntegerSuccessor.getInstance(), startPoint,
        endPoint);
  }

  /**
   * Creates a RubyRange of given strings.
   * 
   * @param startPoint
   *          where the range begins
   * @param endPoint
   *          where the range ends
   * @return a RubyRange
   */
  public static RubyRange<String> range(String startPoint, String endPoint) {
    return new RubyRange<String>(StringSuccessor.getInstance(), startPoint,
        endPoint);
  }

  /**
   * Creates a RubyRange of given longs.
   * 
   * @param startPoint
   *          where the range begins
   * @param endPoint
   *          where the range ends
   * @return a RubyRange
   */
  public static RubyRange<Long> range(long startPoint, long endPoint) {
    return new RubyRange<Long>(LongSuccessor.getInstance(), startPoint,
        endPoint);
  }

  /**
   * Creates a RubyRange of given doubles.
   * 
   * @param startPoint
   *          where the range begins
   * @param endPoint
   *          where the range ends
   * @return a RubyRange
   */
  public static RubyRange<Double> range(double startPoint, double endPoint) {
    return new RubyRange<Double>(DoubleSuccessor.getInstance(), startPoint,
        endPoint);
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
   */
  public RubyRange(Successive<E> successive, E startPoint, E endPoint) {
    super(new RangeIterable<E>(successive, startPoint, endPoint));
  }

}
