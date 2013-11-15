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

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.qr;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.xml.bind.TypeConstraintException;

import net.sf.rubycollect4j.RubyArray;

/**
 * 
 * ByteUtil provides functions to convert variety Objects into bytes.
 * 
 */
public final class ByteUtil {

  private static final Logger logger = Logger.getLogger(ByteUtil.class
      .getName());

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
   * Converts an int into a byte array.
   * 
   * @param i
   *          an int
   * @return a byte array
   */
  public static byte[] toByteArray(int i) {
    return ByteBuffer.allocate(4).order(ByteOrder.nativeOrder()).putInt(i)
        .array();
  }

  /**
   * Converts an Integer into a byte array.
   * 
   * @param i
   *          an Integer
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
    if (o instanceof Byte)
      return toByteArray((Byte) o);
    if (o instanceof Short)
      return toByteArray((Short) o);
    if (o instanceof Integer)
      return toByteArray((Integer) o);
    if (o instanceof Long)
      return toByteArray((Long) o);
    if (o instanceof Float)
      return toByteArray((Float) o);
    if (o instanceof Double)
      return toByteArray((Double) o);
    if (o instanceof Boolean)
      return toByteArray((Boolean) o);
    if (o instanceof Character)
      return toByteArray((Character) o);

    try {
      Class<?> c = o.getClass();
      Method mothod = null;
      for (Method m : c.getMethods()) {
        if (m.getReturnType() == byte[].class
            && m.getParameterTypes().length == 0)
          mothod = m;
      }
      return (byte[]) mothod.invoke(o);
    } catch (Exception e) {
      logger.log(Level.SEVERE, null, e);
      throw new TypeConstraintException("TypeError: no implicit conversion of "
          + (o == null ? null : o.getClass().getName()) + " into byte[]");
    }
  }

  /**
   * Converts bytes into an ASCII String.
   * 
   * @param bytes
   *          used to be converted
   * @param n
   *          length of ASCII String
   * @param bo
   *          the ByteOrder
   * @return an ASCII String
   */
  public static String toASCIIs(byte[] bytes, int n, ByteOrder bo) {
    RubyArray<String> ra = newRubyArray();
    if (bo == LITTLE_ENDIAN) {
      for (int i = 0; i < n; i++) {
        if (i >= bytes.length) {
          ra.push("\\x00");
          continue;
        }
        byte b = bytes[i];
        ra.push(byteToASCII(b, true));
      }
      return ra.join();
    } else {
      for (int i = bytes.length - 1; n > 0; i--) {
        if (i < 0) {
          ra.unshift("\\x00");
          n--;
          continue;
        }
        byte b = bytes[i];
        ra.unshift(byteToASCII(b, true));
        n--;
      }
      return ra.join();
    }
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
  public static String toASCIIs(byte[] bytes, int n) {
    return toASCIIs(bytes, n, ByteOrder.nativeOrder());
  }

  private static String byteToASCII(byte b, boolean hexPrefix) {
    if (b >= 32 && b <= 126) {
      return new String(new byte[] { b });
    } else if (b == 7)
      return "\\a";
    else if (b == 8)
      return "\\b";
    else if (b == 9)
      return "\\t";
    else if (b == 10)
      return "\\n";
    else if (b == 11)
      return "\\v";
    else if (b == 12)
      return "\\f";
    else if (b == 13)
      return "\\r";
    else if (b == 27)
      return "\\e";
    else
      return (hexPrefix ? "\\x" : "") + String.format("%02X", b);
  }

  /**
   * Converts bytes into an UTF String.
   * 
   * @param bytes
   *          used to be converted
   * @return an UTF String
   * @throws IllegalArgumentException
   *           if codePoint is less than 0 or greater than 0X10FFFF
   */
  public static String toUTF(byte[] bytes) {
    int codePoint = ByteBuffer.wrap(bytes).getInt();
    if (codePoint < 0 || codePoint > 0X10FFFF)
      throw new IllegalArgumentException(
          "RangeError: pack(U): value out of range");

    if (codePoint <= 126) {
      String ascii = byteToASCII((byte) codePoint, false);
      if (ascii.length() == 2 && !ascii.startsWith("\\"))
        return "\\u00" + ascii;
      else
        return ascii;
    } else if (codePoint <= 65535) {
      String utf16 = String.valueOf((char) codePoint);
      Pattern p = qr("(\\p{C})+");
      if (p.matcher(utf16).matches()) {
        ByteBuffer bb = ByteBuffer.allocate(2).putChar((char) codePoint);
        return "\\u" + String.format("%02X", bb.get(0))
            + String.format("%02X", bb.get(1));
      } else {
        return utf16;
      }
    } else {
      char[] chars = Character.toChars(codePoint);
      String utf16 = String.valueOf(chars);
      Pattern p = qr("(\\p{C})+");
      if (p.matcher(utf16).matches()) {
        ByteBuffer bb = ByteBuffer.allocate(4).putInt(codePoint);
        return "\\u{" + String.format("%X", bb.get(1))
            + String.format("%02X", bb.get(2))
            + String.format("%02X", bb.get(3)) + "}";
      } else {
        return utf16;
      }
    }
  }

}
