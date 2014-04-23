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

import static java.nio.ByteOrder.BIG_ENDIAN;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.ra;

import java.lang.reflect.Method;
import java.math.BigInteger;
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
 * {@link ByteUtil} provides functions to manipulate bytes or to convert variety
 * Objects into bytes.
 * 
 */
public final class ByteUtil {

  private static final Logger logger = Logger.getLogger(ByteUtil.class
      .getName());

  private ByteUtil() {};

  /**
   * Converts a byte array to a {@link RubyArray} of Byte.
   * 
   * @param bytes
   *          a byte array
   * @return a {@link RubyArray} of Byte
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
   * Converts a short into a byte array by big-endian byte order.
   * 
   * @param s
   *          a short
   * @return a byte array
   */
  public static byte[] toByteArray(short s) {
    return ByteBuffer.allocate(2).order(BIG_ENDIAN).putShort(s).array();
  }

  /**
   * Converts a short into a byte array.
   * 
   * @param s
   *          a short
   * @param bo
   *          a ByteOrder
   * @return a byte array
   */
  public static byte[] toByteArray(short s, ByteOrder bo) {
    return ByteBuffer.allocate(2).order(bo).putShort(s).array();
  }

  /**
   * Converts an int into a byte array by big-endian byte order.
   * 
   * @param i
   *          an int
   * @return a byte array
   */
  public static byte[] toByteArray(int i) {
    return ByteBuffer.allocate(4).order(BIG_ENDIAN).putInt(i).array();
  }

  /**
   * Converts an int into a byte array.
   * 
   * @param i
   *          an int
   * @param bo
   *          a ByteOrder
   * @return a byte array
   */
  public static byte[] toByteArray(int i, ByteOrder bo) {
    return ByteBuffer.allocate(4).order(bo).putInt(i).array();
  }

  /**
   * Converts a long into a byte array by big-endian byte order.
   * 
   * @param l
   *          a long
   * @return a byte array
   */
  public static byte[] toByteArray(long l) {
    return ByteBuffer.allocate(8).order(BIG_ENDIAN).putLong(l).array();
  }

  /**
   * Converts a long into a byte array.
   * 
   * @param l
   *          a long
   * @param bo
   *          a ByteOrder
   * @return a byte array
   */
  public static byte[] toByteArray(long l, ByteOrder bo) {
    return ByteBuffer.allocate(8).order(bo).putLong(l).array();
  }

  /**
   * Converts a float into a byte array by big-endian byte order.
   * 
   * @param f
   *          a float
   * @return a byte array
   */
  public static byte[] toByteArray(float f) {
    return ByteBuffer.allocate(4).order(BIG_ENDIAN).putFloat(f).array();
  }

  /**
   * Converts a float into a byte array.
   * 
   * @param f
   *          a float
   * @param bo
   *          a ByteOrder
   * @return a byte array
   */
  public static byte[] toByteArray(float f, ByteOrder bo) {
    return ByteBuffer.allocate(4).order(bo).putFloat(f).array();
  }

  /**
   * Converts a double into a byte array by big-endian byte order.
   * 
   * @param d
   *          a double
   * @return a byte array
   */
  public static byte[] toByteArray(double d) {
    return ByteBuffer.allocate(8).order(BIG_ENDIAN).putDouble(d).array();
  }

  /**
   * Converts a double into a byte array.
   * 
   * @param d
   *          a double
   * @param bo
   *          a ByteOrder
   * @return a byte array
   */
  public static byte[] toByteArray(double d, ByteOrder bo) {
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
   * Converts a char into a byte array by big-endian byte order.
   * 
   * @param c
   *          a char
   * @return a byte array
   */
  public static byte[] toByteArray(char c) {
    return ByteBuffer.allocate(2).order(BIG_ENDIAN).putChar(c).array();
  }

  /**
   * Converts a char into a byte array.
   * 
   * @param c
   *          a char
   * @param bo
   *          a ByteOrder
   * @return a byte array
   */
  public static byte[] toByteArray(char c, ByteOrder bo) {
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
   * Converts an Object into a byte array by big-endian byte order. ByteOrder
   * only affects on Java Number. If this Object is not a Java primitive wrapper
   * Object, a reflection will be performed to search any method which returns a
   * byte[] and call it directly. Under this circumstance, the ByteOrder won't
   * take effect too.
   * 
   * @param o
   *          any Object
   * @return a byte array
   */
  public static byte[] toByteArray(Object o) {
    return toByteArray(o, BIG_ENDIAN);
  }

  /**
   * Converts an Object into a byte array. ByteOrder only affects on Java
   * Number. If this Object is not a Java primitive wrapper Object, a reflection
   * will be performed to search any method which returns a byte[] and call it
   * directly. Under this circumstance, the ByteOrder won't take effect too.
   * 
   * @param o
   *          an Object
   * @param bo
   *          a ByteOrder
   * @return a byte array
   * @throws TypeConstraintException
   *           if the Object can't be converted into bytes
   */
  public static byte[] toByteArray(Object o, ByteOrder bo) {
    if (o instanceof Byte)
      return new byte[] { (Byte) o };
    if (o instanceof Short)
      return ByteBuffer.allocate(2).order(bo).putShort((Short) o).array();
    if (o instanceof Integer)
      return ByteBuffer.allocate(4).order(bo).putInt((Integer) o).array();
    if (o instanceof Long)
      return ByteBuffer.allocate(8).order(bo).putLong((Long) o).array();
    if (o instanceof Float)
      return ByteBuffer.allocate(4).order(bo).putFloat((Float) o).array();
    if (o instanceof Double)
      return ByteBuffer.allocate(8).order(bo).putDouble((Double) o).array();
    if (o instanceof Boolean)
      return (Boolean) o ? new byte[] { '\1' } : new byte[] { '\0' };
    if (o instanceof Character)
      return ByteBuffer.allocate(2).order(bo).putChar((Character) o).array();

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
   * Converts a byte array into an ASCII String.
   * 
   * @param bytes
   *          used to be converted
   * @param n
   *          length of ASCII String
   * @param bo
   *          a ByteOrder
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
   * Converts a byte array into an UTF String.
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
   * Converts byte array to a binary String by MSB order.
   * 
   * @param bytes
   *          a byte array
   * @return a binary String
   */
  public static String toBinaryString(byte[] bytes) {
    return toBinaryString(bytes, true);
  }

  /**
   * Converts a byte array to a binary String.
   * 
   * @param bytes
   *          a byte array
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
   * Converts a byte array to a hex String by HNF order.
   * 
   * @param bytes
   *          a byte array
   * @return a hex String
   */
  public static String toHexString(byte[] bytes) {
    return toHexString(bytes, true);
  }

  /**
   * Converts a byte array to a hex String.
   * 
   * @param bytes
   *          a byte array
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

  /**
   * Converts a binary string into byte array.
   * 
   * @param binaryStr
   *          a binary string
   * @return a byte array
   * @throws IllegalArgumentException
   *           if binary string is invalid
   */
  public static byte[] fromBinaryString(String binaryStr) {
    if (!binaryStr.matches("^[01]*$"))
      throw new IllegalArgumentException("Invalid binary string");
    if (binaryStr.isEmpty())
      return new byte[0];

    int complementary = binaryStr.length() % 8;
    if (complementary != 0)
      binaryStr += ra("0").multiply(8 - complementary).join();
    return rjust(new BigInteger(binaryStr, 2).toByteArray(),
        binaryStr.length() / 8);
  }

  /**
   * Converts a hexadecimal string into byte array.
   * 
   * @param hexStr
   *          a hexadecimal string
   * @return a byte array
   * @throws IllegalArgumentException
   *           if hexadecimal string is invalid
   */
  public static byte[] fromHexString(String hexStr) {
    if (!hexStr.matches("^[0-9A-Fa-f]*$"))
      throw new IllegalArgumentException("Invalid hexadecimal string");
    if (hexStr.isEmpty())
      return new byte[0];

    int complementary = hexStr.length() % 2;
    if (complementary != 0)
      hexStr += "0";
    return rjust(new BigInteger(hexStr, 16).toByteArray(), hexStr.length() / 2);
  }

}
