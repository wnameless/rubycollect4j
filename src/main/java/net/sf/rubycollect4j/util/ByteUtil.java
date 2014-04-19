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
package net.sf.rubycollect4j.util;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static net.sf.rubycollect4j.RubyCollections.newRubyArray;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.TypeConstraintException;

import net.sf.rubycollect4j.RubyArray;

/**
 * 
 * ByteUtil provides functions to manipulate bytes or convert variety Objects
 * into bytes.
 * 
 */
public final class ByteUtil {

  private static final Logger logger = Logger.getLogger(ByteUtil.class
      .getName());

  private ByteUtil() {};

  /**
   * Converts a byte array to a RubyArray of Byte.
   * 
   * @param bytes
   *          a byte array
   * @return a RubyArray of Byte
   */
  public static RubyArray<Byte> toList(byte[] bytes) {
    RubyArray<Byte> list = newRubyArray();
    for (byte b : bytes) {
      list.add(b);
    }
    return list;
  }

  /**
   * Converts a Collection of Number to a byte array.
   * 
   * @param bytes
   *          a Collection of Number
   * @return a byte array
   */
  public static byte[] toArray(Collection<? extends Number> bytes) {
    byte[] array = new byte[bytes.size()];
    Iterator<? extends Number> iter = bytes.iterator();
    for (int i = 0; i < bytes.size(); i++) {
      array[i] = iter.next().byteValue();
    }
    return array;
  }

  /**
   * Modifies the length of a byte array by padding zero bytes to the right.
   * 
   * @param bytes
   *          a byte array
   * @param width
   *          of the new byte array
   * @return a new byte array
   */
  public static byte[] ljust(byte[] bytes, int width) {
    if (bytes.length >= width) {
      return Arrays.copyOf(bytes, width);
    } else {
      byte[] ljustied = new byte[width];
      System.arraycopy(bytes, 0, ljustied, 0, bytes.length);
      return ljustied;
    }
  }

  /**
   * Modifies the length of a byte array by padding zero bytes to the left.
   * 
   * @param bytes
   *          a byte array
   * @param width
   *          of the new byte array
   * @return a new byte array
   */
  public static byte[] rjust(byte[] bytes, int width) {
    if (bytes.length >= width) {
      return Arrays.copyOfRange(bytes, bytes.length - width, bytes.length);
    } else {
      byte[] rjustied = new byte[width];
      System.arraycopy(bytes, 0, rjustied, width - bytes.length, bytes.length);
      return rjustied;
    }
  }

  /**
   * Reverses a byte array in place.
   * 
   * @param bytes
   *          to be reversed
   */
  public static void reverse(byte[] bytes) {
    for (int i = 0; i < bytes.length / 2; i++) {
      byte temp = bytes[i];
      bytes[i] = bytes[bytes.length - 1 - i];
      bytes[bytes.length - 1 - i] = temp;
    }
  }

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
  public static byte[] toByteArray(short s, ByteOrder bo) {
    return ByteBuffer.allocate(2).order(bo).putShort(s).array();
  }

  /**
   * Converts a Short into a byte array.
   * 
   * @param s
   *          a Short
   * @return a byte array
   */
  public static byte[] toByteArray(Short s, ByteOrder bo) {
    return ByteBuffer.allocate(2).order(bo).putShort(s).array();
  }

  /**
   * Converts an int into a byte array.
   * 
   * @param i
   *          an int
   * @return a byte array
   */
  public static byte[] toByteArray(int i, ByteOrder bo) {
    return ByteBuffer.allocate(4).order(bo).putInt(i).array();
  }

  /**
   * Converts an Integer into a byte array.
   * 
   * @param i
   *          an Integer
   * @return a byte array
   */
  public static byte[] toByteArray(Integer i, ByteOrder bo) {
    return ByteBuffer.allocate(4).order(bo).putInt(i).array();
  }

  /**
   * Converts a long into a byte array.
   * 
   * @param l
   *          a long
   * @return a byte array
   */
  public static byte[] toByteArray(long l, ByteOrder bo) {
    return ByteBuffer.allocate(8).order(bo).putLong(l).array();
  }

  /**
   * Converts a Long into a byte array.
   * 
   * @param l
   *          a Long
   * @return a byte array
   */
  public static byte[] toByteArray(Long l, ByteOrder bo) {
    return ByteBuffer.allocate(8).order(bo).putLong(l).array();
  }

  /**
   * Converts a float into a byte array.
   * 
   * @param f
   *          a float
   * @return a byte array
   */
  public static byte[] toByteArray(float f, ByteOrder bo) {
    return ByteBuffer.allocate(4).order(bo).putFloat(f).array();
  }

  /**
   * Converts a Float into a byte array.
   * 
   * @param f
   *          a Float
   * @return a byte array
   */
  public static byte[] toByteArray(Float f, ByteOrder bo) {
    return ByteBuffer.allocate(4).order(bo).putFloat(f).array();
  }

  /**
   * Converts a double into a byte array.
   * 
   * @param d
   *          a double
   * @return a byte array
   */
  public static byte[] toByteArray(double d, ByteOrder bo) {
    return ByteBuffer.allocate(8).order(bo).putDouble(d).array();
  }

  /**
   * Converts a Double into a byte array.
   * 
   * @param d
   *          a Double
   * @return a byte array
   */
  public static byte[] toByteArray(Double d, ByteOrder bo) {
    return ByteBuffer.allocate(8).order(bo).putDouble(d).array();
  }

  /**
   * Converts a boolean into a byte array.
   * 
   * @param b
   *          a boolean
   * @return a byte array
   */
  public static byte[] toByteArray(boolean b) {
    return ByteBuffer.allocate(1).put(b ? (byte) 0x01 : (byte) 0x00).array();
  }

  /**
   * Converts a Boolean into a byte array.
   * 
   * @param b
   *          a Boolean
   * @return a byte array
   */
  public static byte[] toByteArray(Boolean b) {
    return ByteBuffer.allocate(1).put(b ? (byte) 0x01 : (byte) 0x00).array();
  }

  /**
   * Converts a char into a byte array.
   * 
   * @param c
   *          a char
   * @return a byte array
   */
  public static byte[] toByteArray(char c, ByteOrder bo) {
    return ByteBuffer.allocate(2).order(bo).putChar(c).array();
  }

  /**
   * Converts a Character into a byte array.
   * 
   * @param c
   *          a Character
   * @return a byte array
   */
  public static byte[] toByteArray(Character c, ByteOrder bo) {
    return ByteBuffer.allocate(2).order(bo).putChar(c).array();
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
  public static byte[] toByteArray(Object o, ByteOrder bo) {
    if (o instanceof Byte)
      return toByteArray((Byte) o);
    if (o instanceof Short)
      return toByteArray((Short) o, bo);
    if (o instanceof Integer)
      return toByteArray((Integer) o, bo);
    if (o instanceof Long)
      return toByteArray((Long) o, bo);
    if (o instanceof Float)
      return toByteArray((Float) o, bo);
    if (o instanceof Double)
      return toByteArray((Double) o, bo);
    if (o instanceof Boolean)
      return toByteArray((Boolean) o);
    if (o instanceof Character)
      return toByteArray((Character) o, bo);

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
   * Encodes a byte to ASCII-8Bit String character.
   * 
   * @param b
   *          any byte
   * @return a ISO-8859-1 String character
   */
  public static String toASCII8Bit(byte b) {
    return new String(new byte[] { b }, Charset.forName("ISO-8859-1"));
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
  public static String toExtendedASCIIs(byte[] bytes, int n, ByteOrder bo) {
    RubyArray<String> ra = newRubyArray();
    if (bo == LITTLE_ENDIAN) {
      for (int i = 0; i < n; i++) {
        if (i >= bytes.length) {
          ra.push("\0");
          continue;
        }
        byte b = bytes[i];
        ra.push(toASCII8Bit(b));
      }
      return ra.join();
    } else {
      for (int i = bytes.length - 1; n > 0; i--) {
        if (i < 0) {
          ra.unshift("\0");
          n--;
          continue;
        }
        byte b = bytes[i];
        ra.unshift(toASCII8Bit(b));
        n--;
      }
      return ra.join();
    }
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

    return String.valueOf((char) codePoint);
  }

  /**
   * Converts byte array to a binary String.
   * 
   * @param bytes
   *          array of byte
   * @param isMSB
   *          true if MSB, false if LSB
   * @return a binary String
   */
  public static String toBinaryString(byte[] bytes, boolean isMSB) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      String binary =
          String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ',
              '0');
      if (isMSB)
        sb.append(binary);
      else
        sb.append(new StringBuilder(binary).reverse());
    }
    return sb.toString();
  }

  /**
   * Converts byte array to a hex String.
   * 
   * @param bytes
   *          array of byte
   * @param isHNF
   *          true if HNF(high nibble first), false if LNF(low nibble first)
   * @return a hex String
   */
  public static String toHexString(byte[] bytes, boolean isHNF) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      String hex =
          String.format("%2s", Integer.toHexString(b & 0xFF)).replace(' ', '0');
      if (isHNF)
        sb.append(hex);
      else
        sb.append(new StringBuilder(hex).reverse());
    }
    return sb.toString();
  }

}
