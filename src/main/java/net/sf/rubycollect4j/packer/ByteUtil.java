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

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.rubycollect4j.RubyArray;

public final class ByteUtil {

  private ByteUtil() {};

  public static byte[] toByteArray(byte b) {
    return new byte[] { b };
  }

  public static byte[] toByteArray(Byte b) {
    return new byte[] { b };
  }

  public static byte[] toByteArray(short s) {
    return ByteBuffer.allocate(2).order(ByteOrder.nativeOrder()).putShort(s)
        .array();
  }

  public static byte[] toByteArray(Short s) {
    return ByteBuffer.allocate(2).order(ByteOrder.nativeOrder()).putShort(s)
        .array();
  }

  public static byte[] toByteArray(int i) {
    return ByteBuffer.allocate(4).order(ByteOrder.nativeOrder()).putInt(i)
        .array();
  }

  public static byte[] toByteArray(Integer i) {
    return ByteBuffer.allocate(4).order(ByteOrder.nativeOrder()).putInt(i)
        .array();
  }

  public static byte[] toByteArray(long l) {
    return ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putLong(l)
        .array();
  }

  public static byte[] toByteArray(Long l) {
    return ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putLong(l)
        .array();
  }

  public static byte[] toByteArray(float f) {
    return ByteBuffer.allocate(4).order(ByteOrder.nativeOrder()).putFloat(f)
        .array();
  }

  public static byte[] toByteArray(Float f) {
    return ByteBuffer.allocate(4).order(ByteOrder.nativeOrder()).putFloat(f)
        .array();
  }

  public static byte[] toByteArray(double d) {
    return ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putDouble(d)
        .array();
  }

  public static byte[] toByteArray(Double d) {
    return ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putDouble(d)
        .array();
  }

  public static byte[] toByteArray(boolean b) {
    return ByteBuffer.allocate(1).order(ByteOrder.nativeOrder())
        .putInt(b ? 1 : 0).array();
  }

  public static byte[] toByteArray(Boolean b) {
    return ByteBuffer.allocate(1).order(ByteOrder.nativeOrder())
        .putInt(b ? 1 : 0).array();
  }

  public static byte[] toByteArray(char c) {
    return ByteBuffer.allocate(2).order(ByteOrder.nativeOrder()).putChar(c)
        .array();
  }

  public static byte[] toByteArray(Character c) {
    return ByteBuffer.allocate(2).order(ByteOrder.nativeOrder()).putChar(c)
        .array();
  }

  public static byte[] toByteArray(Object o) {
    Class<?> c = o.getClass();
    try {
      Method m = c.getMethod("getBytes");
      return (byte[]) m.invoke(o);
    } catch (Exception e) {
      e.printStackTrace();
      Logger.getLogger(ByteUtil.class.getName()).log(Level.INFO, null,
          e.getMessage());
    }
    try {
      Method m = c.getMethod("toByteArray");
      return (byte[]) m.invoke(o);
    } catch (Exception e) {
      Logger.getLogger(ByteUtil.class.getName()).log(Level.INFO, null,
          e.getMessage());
    }
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
    throw new UnsupportedOperationException();
  }

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
    System.out.println(ByteOrder.nativeOrder());
    return ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN ? ra.join() : ra
        .reverse().join();
  }

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

  public static byte[][] toBytesArray(Object... objs) {
    return toBytesArray(Arrays.asList(objs));
  }

  public static void main(String[] args) throws UnsupportedEncodingException {
    System.out.println(new String(ByteBuffer.allocate(4)
        .order(ByteOrder.BIG_ENDIAN).putInt(123456).array(), "UTF-8"));
  }

}
