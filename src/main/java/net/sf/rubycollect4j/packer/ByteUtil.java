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
package net.sf.rubycollect4j.packer;

import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.ra;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.TypeConstraintException;

import net.sf.rubycollect4j.RubyArray;

/**
 * 
 * ByteUtil provides functions to convert variety Objects into bytes.
 * 
 */
public final class ByteUtil {

  private ByteUtil() {};

  /**
   * Converts a byte into a byte array.
   * 
   * @param b
   *          a byte
   * @return a byte array
   */
  public static byte[] toByteArray(byte b) {
    return new byte[] { b };
  }

  /**
   * Converts a Byte into a byte array.
   * 
   * @param b
   *          a Byte
   * @return a byte array
   */
  public static byte[] toByteArray(Byte b) {
    return new byte[] { b };
  }

  /**
   * Converts a short into a byte array.
   * 
   * @param s
   *          a short
   * @return a byte array
   */
  public static byte[] toByteArray(short s) {
    return ByteBuffer.allocate(2).order(ByteOrder.nativeOrder()).putShort(s)
        .array();
  }

  /**
   * Converts a Short into a byte array.
   * 
   * @param s
   *          a Short
   * @return a byte array
   */
  public static byte[] toByteArray(Short s) {
    return ByteBuffer.allocate(2).order(ByteOrder.nativeOrder()).putShort(s)
        .array();
  }

  /**
   * Converts a int into a byte array.
   * 
   * @param i
   *          a int
   * @return a byte array
   */
  public static byte[] toByteArray(int i) {
    return ByteBuffer.allocate(4).order(ByteOrder.nativeOrder()).putInt(i)
        .array();
  }

  /**
   * Converts a Integer into a byte array.
   * 
   * @param i
   *          a Integer
   * @return a byte array
   */
  public static byte[] toByteArray(Integer i) {
    return ByteBuffer.allocate(4).order(ByteOrder.nativeOrder()).putInt(i)
        .array();
  }

  /**
   * Converts a long into a byte array.
   * 
   * @param l
   *          a long
   * @return a byte array
   */
  public static byte[] toByteArray(long l) {
    return ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putLong(l)
        .array();
  }

  /**
   * Converts a Long into a byte array.
   * 
   * @param l
   *          a Long
   * @return a byte array
   */
  public static byte[] toByteArray(Long l) {
    return ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putLong(l)
        .array();
  }

  /**
   * Converts a float into a byte array.
   * 
   * @param f
   *          a float
   * @return a byte array
   */
  public static byte[] toByteArray(float f) {
    return ByteBuffer.allocate(4).order(ByteOrder.nativeOrder()).putFloat(f)
        .array();
  }

  /**
   * Converts a Float into a byte array.
   * 
   * @param f
   *          a Float
   * @return a byte array
   */
  public static byte[] toByteArray(Float f) {
    return ByteBuffer.allocate(4).order(ByteOrder.nativeOrder()).putFloat(f)
        .array();
  }

  /**
   * Converts a double into a byte array.
   * 
   * @param d
   *          a double
   * @return a byte array
   */
  public static byte[] toByteArray(double d) {
    return ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putDouble(d)
        .array();
  }

  /**
   * Converts a Double into a byte array.
   * 
   * @param d
   *          a Double
   * @return a byte array
   */
  public static byte[] toByteArray(Double d) {
    return ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putDouble(d)
        .array();
  }

  /**
   * Converts a boolean into a byte array.
   * 
   * @param b
   *          a boolean
   * @return a byte array
   */
  public static byte[] toByteArray(boolean b) {
    return ByteBuffer.allocate(1).order(ByteOrder.nativeOrder())
        .put(b ? (byte) 0x01 : (byte) 0x00).array();
  }

  /**
   * Converts a Boolean into a byte array.
   * 
   * @param b
   *          a Boolean
   * @return a byte array
   */
  public static byte[] toByteArray(Boolean b) {
    return ByteBuffer.allocate(1).order(ByteOrder.nativeOrder())
        .put(b ? (byte) 0x01 : (byte) 0x00).array();
  }

  /**
   * Converts a char into a byte array.
   * 
   * @param c
   *          a char
   * @return a byte array
   */
  public static byte[] toByteArray(char c) {
    return ByteBuffer.allocate(2).order(ByteOrder.nativeOrder()).putChar(c)
        .array();
  }

  /**
   * Converts a Character into a byte array.
   * 
   * @param c
   *          a Character
   * @return a byte array
   */
  public static byte[] toByteArray(Character c) {
    return ByteBuffer.allocate(2).order(ByteOrder.nativeOrder()).putChar(c)
        .array();
  }

  /**
   * Converts a String into a byte array.
   * 
   * @param s
   *          a String
   * @return a byte array
   */
  public static byte[] toByteArray(String s) {
    return s.getBytes();
  }

  /**
   * Converts an Object into a byte array.
   * 
   * @param o
   *          an Object
   * @return a byte array
   * @throws TypeConstraintException
   *           if the Object can't be converted into bytes
   */
  public static byte[] toByteArray(Object o) {
    Class<?> c = o.getClass();
    for (Method m : c.getDeclaredMethods()) {
      if (m.getReturnType() == byte[].class
          && m.getParameterTypes().length == 0) {
        try {
          return (byte[]) m.invoke(o);
        } catch (Exception e) {
          Logger.getLogger(ByteUtil.class.getName()).log(Level.INFO, null,
              e.getMessage());
        }
      }
    }
    throw new TypeConstraintException("TypeError: no implicit conversion of "
        + c.getName() + " into byte[]");
  }

  /**
   * Converts bytes into an ASCII String.
   * 
   * @param bytes
   *          used to be converted
   * @param n
   *          length of ASCII String
   * @return an ASCII String
   */
  public static String toASCII(byte[] bytes, int n) {
    RubyArray<String> ra = newRubyArray();
    for (int i = n - 1; i >= 0; i--) {
      if (i >= bytes.length) {
        ra.add("\\x00");
        continue;
      }
      byte b = bytes[i];
      if (b >= 32 && b <= 126)
        ra.add(new String(new byte[] { b }));
      else if (b == 7)
        ra.add("\\a");
      else if (b == 8)
        ra.add("\\b");
      else if (b == 9)
        ra.add("\\t");
      else if (b == 10)
        ra.add("\\n");
      else if (b == 11)
        ra.add("\\v");
      else if (b == 12)
        ra.add("\\f");
      else if (b == 13)
        ra.add("\\r");
      else if (b == 27)
        ra.add("\\e");
      else
        ra.add("\\x" + String.format("%02X", b));
    }
    return ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN ? ra.join() : ra
        .reverse().join();
  }

  /**
   * Converts a List of Object into an array of bytes.
   * 
   * @param objs
   *          a List of Object
   * @return an array of bytes
   */
  public static byte[][] toBytesArray(List<Object> objs) {
    RubyArray<Object> objects = ra(objs).compact();
    byte[][] bytes = new byte[objects.size()][];
    for (int i = 0; i < objects.size(); i++) {
      Object o = objects.get(i);
      if (o instanceof Byte) {
        bytes[i] = ByteUtil.toByteArray((Byte) o);
      } else if (o instanceof Short) {
        bytes[i] = ByteUtil.toByteArray((Short) o);
      } else if (o instanceof Integer) {
        bytes[i] = ByteUtil.toByteArray((Integer) o);
      } else if (o instanceof Long) {
        bytes[i] = ByteUtil.toByteArray((Long) o);
      } else if (o instanceof Float) {
        bytes[i] = ByteUtil.toByteArray((Float) o);
      } else if (o instanceof Double) {
        bytes[i] = ByteUtil.toByteArray((Double) o);
      } else if (o instanceof Boolean) {
        bytes[i] = ByteUtil.toByteArray((Boolean) o);
      } else if (o instanceof Character) {
        bytes[i] = ByteUtil.toByteArray((Character) o);
      } else if (o instanceof String) {
        bytes[i] = ((String) o).getBytes();
      } else {
        bytes[i] = ByteUtil.toByteArray(o);
      }
    }
    return bytes;
  }

  /**
   * Converts an array of Object into an array of bytes.
   * 
   * @param objs
   *          an array of Object
   * @return an array of bytes
   */
  public static byte[][] toBytesArray(Object... objs) {
    return toBytesArray(Arrays.asList(objs));
  }

}
