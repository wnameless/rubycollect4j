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
package net.sf.rubycollect4j.range;

/**
 * 
 * DoubleSuccessor generates a successor of any given Double. It's a singleton
 * object.
 * 
 */
public final class DoubleSuccessor implements Successive<Double> {

  private static volatile DoubleSuccessor INSTANCE;

  /**
   * Returns the singleton DoubleSuccessor object.
   * 
   * @return a DoubleSuccessor
   */
  public static DoubleSuccessor getInstance() {
    if (INSTANCE == null) {
      synchronized (LongSuccessor.class) {
        if (INSTANCE == null) {
          INSTANCE = new DoubleSuccessor();
        }
      }
    }
    return INSTANCE;
  }

  private DoubleSuccessor() {}

  @Override
  public Double succ(Double curr) {
    String doubleStr = curr.toString();
    int precision = doubleStr.lastIndexOf('.');
    return curr + 1.0 / Math.pow(10, doubleStr.length() - precision - 1);
  }

  @Override
  public int compare(Double arg0, Double arg1) {
    return arg0.compareTo(arg1);
  }

}