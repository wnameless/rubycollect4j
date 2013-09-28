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
import static net.sf.rubycollect4j.RubyCollections.newRubyArray;

/**
 * 
 * RubyKernel mimics the useful Ruby p method by wrapping the Java
 * System.out.println method.
 * 
 */
public final class RubyKernel {

  private RubyKernel() {}

  /**
   * Returns null.
   */
  public static Object p() {
    return null;
  }

  /**
   * Calls System.out.println() and returns the argument.
   * 
   * @param x
   *          any Object
   * @return the Object
   */
  public static Object p(Object x) {
    out.println(x);
    return x;
  }

  /**
   * Calls System.out.println() and returns arguments.
   * 
   * @param xs
   *          an array of Object
   * @return a RubyArray of Object
   */
  public static RubyArray<Object> p(Object... xs) {
    for (Object x : xs) {
      out.println(x);
    }
    return newRubyArray(xs);
  }

  /**
   * Calls System.out.println() and returns the argument.
   * 
   * @param x
   *          any String
   * @return the String
   */
  public static String p(String x) {
    out.println(x);
    return x;
  }

  /**
   * Calls System.out.println() and returns arguments.
   * 
   * @param xs
   *          an array of String
   * @return a RubyArray of String
   */
  public static RubyArray<String> p(String... xs) {
    for (String x : xs) {
      out.println(x);
    }
    return newRubyArray(xs);
  }

  /**
   * Calls System.out.println() and returns the argument.
   * 
   * @param x
   *          any boolean
   * @return the boolean
   */
  public static boolean p(boolean x) {
    out.println(x);
    return x;
  }

  /**
   * Calls System.out.println() and returns arguments.
   * 
   * @param xs
   *          an array of Boolean
   * @return a RubyArray of Boolean
   */
  public static RubyArray<Boolean> p(Boolean... xs) {
    RubyArray<Boolean> rubyArray = newRubyArray();
    for (boolean x : xs) {
      out.println(x);
      rubyArray.add(x);
    }
    return rubyArray;
  }

  /**
   * Calls System.out.println() and returns the argument.
   * 
   * @param x
   *          any char
   * @return the char
   */
  public static char p(char x) {
    out.println(x);
    return x;
  }

  /**
   * Calls System.out.println() and returns arguments.
   * 
   * @param xs
   *          an array of Character
   * @return a RubyArray of Character
   */
  public static RubyArray<Character> p(Character... xs) {
    RubyArray<Character> rubyArray = newRubyArray();
    for (Character x : xs) {
      out.println(x);
      rubyArray.add(x);
    }
    return rubyArray;
  }

  /**
   * Calls System.out.println() and returns the argument.
   * 
   * @param x
   *          any char[]
   * @return the char[]
   */
  public static char[] p(char[] x) {
    out.println(x);
    return x;
  }

  /**
   * Calls System.out.println() and returns arguments.
   * 
   * @param xs
   *          an array of char[]
   * @return a RubyArray of char[]
   */
  public static RubyArray<char[]> p(char[]... xs) {
    RubyArray<char[]> rubyArray = newRubyArray();
    for (char[] x : xs) {
      out.println(x);
      rubyArray.add(x);
    }
    return rubyArray;
  }

  /**
   * Calls System.out.println() and returns the argument.
   * 
   * @param x
   *          any double
   * @return the double
   */
  public static double p(double x) {
    out.println(x);
    return x;
  }

  /**
   * Calls System.out.println() and returns arguments.
   * 
   * @param xs
   *          an array of Double
   * @return a RubyArray of Double
   */
  public static RubyArray<Double> p(Double... xs) {
    RubyArray<Double> rubyArray = newRubyArray();
    for (Double x : xs) {
      out.println(x);
      rubyArray.add(x);
    }
    return rubyArray;
  }

  /**
   * Calls System.out.println() and returns the argument.
   * 
   * @param x
   *          any float
   * @return the float
   */
  public static float p(float x) {
    out.println(x);
    return x;
  }

  /**
   * Calls System.out.println() and returns arguments.
   * 
   * @param xs
   *          an array of Float
   * @return a RubyArray of Float
   */
  public static RubyArray<Float> p(Float... xs) {
    RubyArray<Float> rubyArray = newRubyArray();
    for (Float x : xs) {
      out.println(x);
      rubyArray.add(x);
    }
    return rubyArray;
  }

  /**
   * Calls System.out.println() and returns the argument.
   * 
   * @param x
   *          any int
   * @return the int
   */
  public static int p(int x) {
    out.println(x);
    return x;
  }

  /**
   * Calls System.out.println() and returns arguments.
   * 
   * @param xs
   *          an array of Integer
   * @return a RubyArray of Integer
   */
  public static RubyArray<Integer> p(Integer... xs) {
    RubyArray<Integer> rubyArray = newRubyArray();
    for (Integer x : xs) {
      out.println(x);
      rubyArray.add(x);
    }
    return rubyArray;
  }

  /**
   * Calls System.out.println() and returns the argument.
   * 
   * @param x
   *          any long
   * @return the long
   */
  public static long p(long x) {
    out.println(x);
    return x;
  }

  /**
   * Calls System.out.println() and returns arguments.
   * 
   * @param xs
   *          an array of Long
   * @return a RubyArray of Long
   */
  public static RubyArray<Long> p(Long... xs) {
    RubyArray<Long> rubyArray = newRubyArray();
    for (Long x : xs) {
      out.println(x);
      rubyArray.add(x);
    }
    return rubyArray;
  }

}
