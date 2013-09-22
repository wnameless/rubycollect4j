package net.sf.rubycollect4j.packer;

import static net.sf.rubycollect4j.RubyCollections.ra;

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
    return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(s)
        .array();
  }

  public static byte[] toByteArray(Short s) {
    return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(s)
        .array();
  }

  public static byte[] toByteArray(int i) {
    return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(i)
        .array();
  }

  public static byte[] toByteArray(Integer i) {
    return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(i)
        .array();
  }

  public static byte[] toByteArray(long l) {
    return ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(l)
        .array();
  }

  public static byte[] toByteArray(Long l) {
    return ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(l)
        .array();
  }

  public static byte[] toByteArray(float f) {
    return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(f)
        .array();
  }

  public static byte[] toByteArray(Float f) {
    return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(f)
        .array();
  }

  public static byte[] toByteArray(double d) {
    return ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(d)
        .array();
  }

  public static byte[] toByteArray(Double d) {
    return ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(d)
        .array();
  }

  public static byte[] toByteArray(boolean b) {
    return ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN)
        .putInt(b ? 1 : 0).array();
  }

  public static byte[] toByteArray(Boolean b) {
    return ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN)
        .putInt(b ? 1 : 0).array();
  }

  public static byte[] toByteArray(char c) {
    return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putChar(c)
        .array();
  }

  public static byte[] toByteArray(Character c) {
    return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putChar(c)
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
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < n; i++) {
      if (i >= bytes.length) {
        sb.append("\\x00");
        continue;
      }
      byte b = bytes[i];
      if (b >= 32 && b <= 126)
        sb.append(new String(new byte[] { b }));
      else if (b == 7)
        sb.append("\\a");
      else if (b == 8)
        sb.append("\\b");
      else if (b == 9)
        sb.append("\\t");
      else if (b == 10)
        sb.append("\\n");
      else if (b == 11)
        sb.append("\\v");
      else if (b == 12)
        sb.append("\\f");
      else if (b == 13)
        sb.append("\\r");
      else if (b == 27)
        sb.append("\\e");
      else
        sb.append("\\x" + String.format("%02X", b));
    }
    return sb.toString();
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

  public static void main(String[] args) {
    System.out.println(toASCII(
        ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(15)
            .array(), 6));
  }

}
