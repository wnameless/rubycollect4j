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

import static java.lang.System.out;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * {@link RubyKernel} mimics the useful Ruby p method by wrapping the Java
 * System.out.println method.
 * 
 * @author Wei-Ming Wu
 * 
 */
public class RubyKernel {

  RubyKernel() {}

  /**
   * Prints an empty line and returns null.
   * 
   * @return null
   */
  public static Void p() {
    out.println();
    return null;
  }

  /**
   * Prints a human-readable representation of given Object.
   * 
   * @param o
   *          any Object
   * @return the Object
   */
  public static <T> T p(T o) {
    return p(o, true);
  }

  /**
   * Prints a human-readable representation of given Objects.
   * 
   * @param first
   *          first Object
   * @param others
   *          other Object
   * @return a {@link RubyArray} of given Objects
   */
  @SafeVarargs
  public static <T> RubyArray<T> p(T first, T... others) {
    RubyArray<T> ra = new RubyArray<>();
    out.print("[");
    ra.add(p(first, false));
    Arrays.asList(others).forEach(item -> {
      out.print(", ");
      ra.add(p(item, false));
    });
    out.print("]");
    out.println();
    return ra;
  }

  private static <T> T p(T o, boolean newLine) {
    if (o instanceof CharSequence) {
      out.print("\"");
      out.print(o);
      out.print("\"");
    } else if (o instanceof Character) {
      out.print("'");
      out.print(o);
      out.print("'");
    } else if (o instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) o;
      out.print("{");
      Ruby.Enumerator.of(map.entrySet()).eachWithIndex((entry, i) -> {
        if (i != 0) out.print(", ");
        p(entry.getKey(), false);
        out.print("=");
        p(entry.getValue(), false);
      });
      out.print("}");
    } else if (o instanceof Iterable) {
      Iterable<?> iter = (Iterable<?>) o;
      out.print("[");
      Ruby.Enumerator.of(iter).eachWithIndex((item, i) -> {
        if (i != 0) out.print(", ");
        p(item, false);
      });
      out.print("]");
    } else if (o instanceof Iterator) {
      Iterator<?> iter = (Iterator<?>) o;
      out.print("[");
      int i = 0;
      while (iter.hasNext()) {
        if (i != 0) out.print(", ");
        p(iter.next(), false);
        i++;
      }
      out.print("]");
    } else if (o instanceof byte[]) {
      out.print(Arrays.toString((byte[]) o));
    } else if (o instanceof short[]) {
      out.print(Arrays.toString((short[]) o));
    } else if (o instanceof int[]) {
      out.print(Arrays.toString((int[]) o));
    } else if (o instanceof long[]) {
      out.print(Arrays.toString((long[]) o));
    } else if (o instanceof float[]) {
      out.print(Arrays.toString((float[]) o));
    } else if (o instanceof double[]) {
      out.print(Arrays.toString((double[]) o));
    } else if (o instanceof boolean[]) {
      out.print(Arrays.toString((boolean[]) o));
    } else if (o instanceof char[]) {
      out.print("[");
      int i = 0;
      for (char c : ((char[]) o)) {
        if (i != 0) out.print(", ");
        out.print("'");
        out.print(c);
        out.print("'");
        i++;
      }
      out.print("]");
    } else if (o instanceof Object[]) {
      Object[] array = (Object[]) o;
      out.print("[");
      Ruby.Enumerator.of(Arrays.asList(array)).eachWithIndex((item, i) -> {
        if (i != 0) out.print(", ");
        p(item, false);
      });
      out.print("]");
    } else {
      out.print(o);
    }
    if (newLine) out.println();
    return o;
  }

}
