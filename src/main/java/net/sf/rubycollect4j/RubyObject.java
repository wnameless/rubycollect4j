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

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * RubyObject provides the general methods of Ruby objects.
 * 
 */
public final class RubyObject {

  private RubyObject() {}

  /**
   * Executes a method of any Object by Java reflection.
   * 
   * @param o
   *          an Object
   * @param methodName
   *          name of the method
   * @param args
   *          arguments of the method
   * @return the result of the method called
   */
  @SuppressWarnings("unchecked")
  public static <E> E send(Object o, String methodName, Object... args) {
    try {
      for (Method method : o.getClass().getMethods()) {
        if (method.getName().equals(methodName)) {
          boolean isArgsMatched = true;
          for (int i = 0; i < method.getParameterTypes().length; i++) {
            if (i >= args.length || args[i] == null)
              continue;

            if (!isConvertable(method.getParameterTypes()[i],
                args[i].getClass()))
              isArgsMatched = false;
          }
          if (isArgsMatched && method.getParameterTypes().length == args.length)
            return (E) method.invoke(o, args);
        }
      }
      throw new NoSuchMethodException();
    } catch (NoSuchMethodException ex) {
      Logger.getLogger(RubyObject.class.getName()).log(Level.SEVERE, null, ex);
      throw new IllegalArgumentException("NoMethodError: undefined method `"
          + methodName + "' for " + o + ":" + o.getClass().getName());
    } catch (Exception ex) {
      Logger.getLogger(RubyObject.class.getName()).log(Level.SEVERE, null, ex);
      throw new RuntimeException(ex);
    }
  }

  private static boolean isConvertable(Class<?> klass1, Class<?> klass2) {
    if (klass1 == byte.class)
      return klass2 == Byte.class;
    else if (klass1 == short.class)
      return klass2 == Short.class;
    else if (klass1 == int.class)
      return klass2 == Integer.class;
    else if (klass1 == long.class)
      return klass2 == Long.class;
    else if (klass1 == float.class)
      return klass2 == Float.class;
    else if (klass1 == double.class)
      return klass2 == Double.class;
    else if (klass1 == boolean.class)
      return klass2 == Boolean.class;
    else if (klass1 == char.class)
      return klass2 == Character.class;
    else
      return klass1.isAssignableFrom(klass2);
  }

  /**
   * Executes a method of any Object by Java reflection.
   * 
   * @param o
   *          an Object
   * @param methodName
   *          name of the method
   * @param arg
   *          a Byte
   * @return the result of the method called
   */
  public static <E> E send(Object o, String methodName, Byte arg) {
    return send(o, methodName, (Object) arg);
  }

  /**
   * Executes a method of any Object by Java reflection.
   * 
   * @param o
   *          an Object
   * @param methodName
   *          name of the method
   * @param arg
   *          a byte
   * @return the result of the method called
   */
  @SuppressWarnings("unchecked")
  public static <E> E send(Object o, String methodName, byte arg) {
    try {
      Method method = o.getClass().getMethod(methodName, byte.class);
      return (E) method.invoke(o, arg);
    } catch (Exception e) {
      return send(o, methodName, (Object) arg);
    }
  }

  /**
   * Executes a method of any Object by Java reflection.
   * 
   * @param o
   *          an Object
   * @param methodName
   *          name of the method
   * @param arg
   *          a Short
   * @return the result of the method called
   */
  public static <E> E send(Object o, String methodName, Short arg) {
    return send(o, methodName, (Object) arg);
  }

  /**
   * Executes a method of any Object by Java reflection.
   * 
   * @param o
   *          an Object
   * @param methodName
   *          name of the method
   * @param arg
   *          a short
   * @return the result of the method called
   */
  @SuppressWarnings("unchecked")
  public static <E> E send(Object o, String methodName, short arg) {
    try {
      Method method = o.getClass().getMethod(methodName, short.class);
      return (E) method.invoke(o, arg);
    } catch (Exception e) {
      return send(o, methodName, (Object) arg);
    }
  }

  /**
   * Executes a method of any Object by Java reflection.
   * 
   * @param o
   *          an Object
   * @param methodName
   *          name of the method
   * @param arg
   *          an Integer
   * @return the result of the method called
   */
  public static <E> E send(Object o, String methodName, Integer arg) {
    return send(o, methodName, (Object) arg);
  }

  /**
   * Executes a method of any Object by Java reflection.
   * 
   * @param o
   *          an Object
   * @param methodName
   *          name of the method
   * @param arg
   *          an int
   * @return the result of the method called
   */
  @SuppressWarnings("unchecked")
  public static <E> E send(Object o, String methodName, int arg) {
    try {
      Method method = o.getClass().getMethod(methodName, int.class);
      return (E) method.invoke(o, arg);
    } catch (Exception e) {
      return send(o, methodName, (Object) arg);
    }
  }

  /**
   * Executes a method of any Object by Java reflection.
   * 
   * @param o
   *          an Object
   * @param methodName
   *          name of the method
   * @param arg
   *          a Long
   * @return the result of the method called
   */
  public static <E> E send(Object o, String methodName, Long arg) {
    return send(o, methodName, (Object) arg);
  }

  /**
   * Executes a method of any Object by Java reflection.
   * 
   * @param o
   *          an Object
   * @param methodName
   *          name of the method
   * @param arg
   *          a long
   * @return the result of the method called
   */
  @SuppressWarnings("unchecked")
  public static <E> E send(Object o, String methodName, long arg) {
    try {
      Method method = o.getClass().getMethod(methodName, long.class);
      return (E) method.invoke(o, arg);
    } catch (Exception e) {
      return send(o, methodName, (Object) arg);
    }
  }

  /**
   * Executes a method of any Object by Java reflection.
   * 
   * @param o
   *          an Object
   * @param methodName
   *          name of the method
   * @param arg
   *          a Float
   * @return the result of the method called
   */
  public static <E> E send(Object o, String methodName, Float arg) {
    return send(o, methodName, (Object) arg);
  }

  /**
   * Executes a method of any Object by Java reflection.
   * 
   * @param o
   *          an Object
   * @param methodName
   *          name of the method
   * @param arg
   *          a float
   * @return the result of the method called
   */
  @SuppressWarnings("unchecked")
  public static <E> E send(Object o, String methodName, float arg) {
    try {
      Method method = o.getClass().getMethod(methodName, float.class);
      return (E) method.invoke(o, arg);
    } catch (Exception e) {
      return send(o, methodName, (Object) arg);
    }
  }

  /**
   * Executes a method of any Object by Java reflection.
   * 
   * @param o
   *          an Object
   * @param methodName
   *          name of the method
   * @param arg
   *          a Double
   * @return the result of the method called
   */
  public static <E> E send(Object o, String methodName, Double arg) {
    return send(o, methodName, (Object) arg);
  }

  /**
   * Executes a method of any Object by Java reflection.
   * 
   * @param o
   *          an Object
   * @param methodName
   *          name of the method
   * @param arg
   *          a double
   * @return the result of the method called
   */
  @SuppressWarnings("unchecked")
  public static <E> E send(Object o, String methodName, double arg) {
    try {
      Method method = o.getClass().getMethod(methodName, double.class);
      return (E) method.invoke(o, arg);
    } catch (Exception e) {
      return send(o, methodName, (Object) arg);
    }
  }

  /**
   * Executes a method of any Object by Java reflection.
   * 
   * @param o
   *          an Object
   * @param methodName
   *          name of the method
   * @param arg
   *          a Boolean
   * @return the result of the method called
   */
  public static <E> E send(Object o, String methodName, Boolean arg) {
    return send(o, methodName, (Object) arg);
  }

  /**
   * Executes a method of any Object by Java reflection.
   * 
   * @param o
   *          an Object
   * @param methodName
   *          name of the method
   * @param arg
   *          a boolean
   * @return the result of the method called
   */
  @SuppressWarnings("unchecked")
  public static <E> E send(Object o, String methodName, boolean arg) {
    try {
      Method method = o.getClass().getMethod(methodName, boolean.class);
      return (E) method.invoke(o, arg);
    } catch (Exception e) {
      return send(o, methodName, (Object) arg);
    }
  }

  /**
   * Executes a method of any Object by Java reflection.
   * 
   * @param o
   *          an Object
   * @param methodName
   *          name of the method
   * @param arg
   *          a Character
   * @return the result of the method called
   */
  public static <E> E send(Object o, String methodName, Character arg) {
    return send(o, methodName, (Object) arg);
  }

  /**
   * Executes a method of any Object by Java reflection.
   * 
   * @param o
   *          an Object
   * @param methodName
   *          name of the method
   * @param arg
   *          a char
   * @return the result of the method called
   */
  @SuppressWarnings("unchecked")
  public static <E> E send(Object o, String methodName, char arg) {
    try {
      Method method = o.getClass().getMethod(methodName, char.class);
      return (E) method.invoke(o, arg);
    } catch (Exception e) {
      return send(o, methodName, (Object) arg);
    }
  }

}
