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
package net.sf.rubycollect4j.succ;

/**
 * 
 * {@link DoubleSuccessor} generates a successor of any given Double. It requires a precision to
 * determine what is the number to increase on every successor.
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class DoubleSuccessor implements Successive<Double> {

  private final int precision;

  /**
   * Returns a {@link DoubleSuccessor}.
   * 
   * @param precision of each successor
   */
  public DoubleSuccessor(int precision) {
    if (precision < 0) throw new IllegalArgumentException("negative precision");

    this.precision = precision;
  }

  @Override
  public Double succ(Double curr) {
    return curr + 1.0 / Math.pow(10, precision);
  }

  @Override
  public int compare(Double arg0, Double arg1) {
    return arg0.compareTo(arg1);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof DoubleSuccessor) {
      DoubleSuccessor ds = (DoubleSuccessor) o;
      return precision == ds.precision;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return precision;
  }

  @Override
  public String toString() {
    return "DoubleSuccessor{precision=" + precision + "}";
  }

}
