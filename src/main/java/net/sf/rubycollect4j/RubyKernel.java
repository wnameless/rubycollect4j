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

import static java.lang.System.out;

/**
 * 
 * RubyKernel mimics the useful Ruby puts method by wrapping the Java
 * System.out.println method.
 * 
 */
public final class RubyKernel {

  /**
   * Equivalent to System.out.println().
   */
  public static void p() {
    out.println();
  }

  /**
   * Equivalent to System.out.println().
   * 
   * @param x
   */
  public static void p(Object x) {
    out.println(x);
  }

  /**
   * Equivalent to System.out.println().
   * 
   * @param x
   */
  public static void p(String x) {
    out.println(x);
  }

  /**
   * Equivalent to System.out.println().
   * 
   * @param x
   */
  public static void p(boolean x) {
    out.println(x);
  }

  /**
   * Equivalent to System.out.println().
   * 
   * @param x
   */
  public static void p(char x) {
    out.println(x);
  }

  /**
   * Equivalent to System.out.println().
   * 
   * @param x
   */
  public static void p(char[] x) {
    out.println(x);
  }

  /**
   * Equivalent to System.out.println().
   * 
   * @param x
   */
  public static void p(double x) {
    out.println(x);
  }

  /**
   * Equivalent to System.out.println().
   * 
   * @param x
   */
  public static void p(float x) {
    out.println(x);
  }

  /**
   * Equivalent to System.out.println().
   * 
   * @param x
   */
  public static void p(int x) {
    out.println(x);
  }

  /**
   * Equivalent to System.out.println().
   * 
   * @param x
   */
  public static void p(long x) {
    out.println(x);
  }

}
