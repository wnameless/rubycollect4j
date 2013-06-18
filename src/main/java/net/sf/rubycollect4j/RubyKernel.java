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
