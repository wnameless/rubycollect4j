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
package net.sf.rubycollect4j.util;

import static java.nio.ByteOrder.BIG_ENDIAN;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static java.nio.ByteOrder.nativeOrder;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.TypeConstraintException;

import org.junit.Test;

import net.sf.rubycollect4j.RubyArray;

public class ByteUtilsTest {

  ByteOrder le = LITTLE_ENDIAN;
  ByteOrder be = BIG_ENDIAN;

  @Test
  public void testPrivateConstructor() throws Exception {
    Constructor<ByteUtils> c = ByteUtils.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c.getModifiers()));
    c.setAccessible(true);
    c.newInstance();
  }

  @Test
  public void testToList() {
    RubyArray<Byte> bytes =
        ByteUtils.toList(new byte[] { (byte) 0x00, (byte) 0x01, (byte) 0x02 });
    assertEquals(ra((byte) 0x00, (byte) 0x01, (byte) 0x02), bytes);
  }

  @Test
  public void testToArray() {
    List<Byte> bytes = ra((byte) 0x00, (byte) 0x01, (byte) 0x02);
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x01, (byte) 0x02 },
        ByteUtils.toArray(bytes));
  }

  @Test
  public void testLjust() {
    byte[] bytes =
        new byte[] { (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03 };
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x01, (byte) 0x02 },
        ByteUtils.ljust(bytes, 3));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x01, (byte) 0x02,
        (byte) 0x03, (byte) 0x00 }, ByteUtils.ljust(bytes, 5));
  }

  @Test
  public void testRjust() {
    byte[] bytes =
        new byte[] { (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03 };
    assertArrayEquals(new byte[] { (byte) 0x01, (byte) 0x02, (byte) 0x03 },
        ByteUtils.rjust(bytes, 3));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x01,
        (byte) 0x02, (byte) 0x03 }, ByteUtils.rjust(bytes, 5));
  }

  @Test
  public void testReverse() {
    byte[] bytes = new byte[] { (byte) 1, (byte) 2, (byte) 3, (byte) 4 };
    ByteUtils.reverse(bytes);
    assertArrayEquals(new byte[] { (byte) 4, (byte) 3, (byte) 2, (byte) 1 },
        bytes);
  }

  @Test
  public void testToByteArrayWithByte() {
    assertArrayEquals(new byte[] { '\1' }, ByteUtils.toByteArray((byte) '\1'));
    assertArrayEquals(new byte[] { '\1' },
        ByteUtils.toByteArray(Byte.valueOf((byte) '\1')));
  }

  @Test
  public void testToByteArrayWithShort() {
    assertArrayEquals(new byte[] { '\1', '\0' },
        ByteUtils.toByteArray((short) '\1', le));
    assertArrayEquals(new byte[] { '\1', '\0' },
        ByteUtils.toByteArray(Short.valueOf((short) '\1'), le));
    assertArrayEquals(ByteUtils.toByteArray((short) '\1', be),
        ByteUtils.toByteArray((short) '\1'));
    assertArrayEquals(ByteUtils.toByteArray((short) '\1', be),
        ByteUtils.toByteArray(Short.valueOf((short) '\1')));
  }

  @Test
  public void testToByteArrayWithInteger() {
    assertArrayEquals(new byte[] { '\1', '\0', '\0', '\0' },
        ByteUtils.toByteArray(1, le));
    assertArrayEquals(new byte[] { '\1', '\0', '\0', '\0' },
        ByteUtils.toByteArray(Integer.valueOf(1), le));
    assertArrayEquals(ByteUtils.toByteArray(1, be), ByteUtils.toByteArray(1));
    assertArrayEquals(ByteUtils.toByteArray(1, be),
        ByteUtils.toByteArray(Integer.valueOf(1)));
  }

  @Test
  public void testToByteArrayWithLong() {
    assertArrayEquals(
        new byte[] { '\1', '\0', '\0', '\0', '\0', '\0', '\0', '\0' },
        ByteUtils.toByteArray(1L, le));
    assertArrayEquals(
        new byte[] { '\1', '\0', '\0', '\0', '\0', '\0', '\0', '\0' },
        ByteUtils.toByteArray(Long.valueOf(1L), le));
    assertArrayEquals(ByteUtils.toByteArray(1L, be), ByteUtils.toByteArray(1L));
    assertArrayEquals(ByteUtils.toByteArray(Long.valueOf(1L), be),
        ByteUtils.toByteArray(Long.valueOf(1L)));
  }

  @Test
  public void testToByteArrayWithFloat() {
    assertArrayEquals(new byte[] { '\0', '\0', (byte) -128, (byte) 63 },
        ByteUtils.toByteArray(1f, le));
    assertArrayEquals(new byte[] { '\0', '\0', (byte) -128, (byte) 63 },
        ByteUtils.toByteArray(Float.valueOf(1f), le));
    assertArrayEquals(ByteUtils.toByteArray(1f, be), ByteUtils.toByteArray(1f));
    assertArrayEquals(ByteUtils.toByteArray(Float.valueOf(1f), be),
        ByteUtils.toByteArray(Float.valueOf(1f)));
  }

  @Test
  public void testToByteArrayWithDouble() {
    assertArrayEquals(new byte[] { '\0', '\0', '\0', '\0', '\0', '\0',
        (byte) -16, (byte) 63 }, ByteUtils.toByteArray(1d, le));
    assertArrayEquals(new byte[] { '\0', '\0', '\0', '\0', '\0', '\0',
        (byte) -16, (byte) 63 }, ByteUtils.toByteArray(Double.valueOf(1d), le));
    assertArrayEquals(ByteUtils.toByteArray(1d, be), ByteUtils.toByteArray(1d));
    assertArrayEquals(ByteUtils.toByteArray(Double.valueOf(1d), be),
        ByteUtils.toByteArray(Double.valueOf(1d)));
  }

  @Test
  public void testToByteArrayWithBoolean() {
    assertArrayEquals(new byte[] { '\0' }, ByteUtils.toByteArray(false));
    assertArrayEquals(new byte[] { '\1' }, ByteUtils.toByteArray(true));
    assertArrayEquals(new byte[] { '\0' },
        ByteUtils.toByteArray(Boolean.FALSE));
    assertArrayEquals(new byte[] { '\1' }, ByteUtils.toByteArray(Boolean.TRUE));
  }

  @Test
  public void testToByteArrayWithCharacter() {
    assertArrayEquals(new byte[] { '\1', '\0' },
        ByteUtils.toByteArray((char) 1, le));
    assertArrayEquals(new byte[] { '\1', '\0' },
        ByteUtils.toByteArray(Character.valueOf((char) 1), le));
    assertArrayEquals(ByteUtils.toByteArray('\1', be),
        ByteUtils.toByteArray('\1'));
    assertArrayEquals(ByteUtils.toByteArray(Character.valueOf('\1'), be),
        ByteUtils.toByteArray(Character.valueOf('\1')));
  }

  @Test
  public void testToByteArrayWithString() {
    assertArrayEquals(new byte[] { (byte) 0x41 }, ByteUtils.toByteArray("A"));
  }

  @Test
  public void testToByteArrayWithObject() {
    assertArrayEquals(new byte[] { (byte) 0x41 },
        ByteUtils.toByteArray("A", le));
    assertArrayEquals(new byte[] { (byte) 0x00 },
        ByteUtils.toByteArray(Byte.valueOf((byte) 0), le));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00 },
        ByteUtils.toByteArray(Short.valueOf((short) 0), le));
    assertArrayEquals(
        new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 },
        ByteUtils.toByteArray(Integer.valueOf(0), le));
    assertArrayEquals(
        new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 },
        ByteUtils.toByteArray(Long.valueOf(0L), le));
    assertArrayEquals(
        new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 },
        ByteUtils.toByteArray(Float.valueOf(0f), le));
    assertArrayEquals(
        new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 },
        ByteUtils.toByteArray(Double.valueOf(0d), le));
    assertArrayEquals(new byte[] { (byte) 0x00 },
        ByteUtils.toByteArray(Boolean.FALSE, le));
    assertArrayEquals(new byte[] { (byte) 0x01 },
        ByteUtils.toByteArray(Boolean.TRUE, le));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00 },
        ByteUtils.toByteArray(Character.valueOf((char) 0), le));
  }

  @Test(expected = TypeConstraintException.class)
  public void testToByteArrayWithObjectAndException1() {
    ByteUtils.toByteArray(new ArrayList<Object>(), le);
  }

  @Test(expected = TypeConstraintException.class)
  public void testToByteArrayWithObjectAndException2() {
    ByteUtils.toByteArray((Object) null, le);
  }

  @Test
  public void testToASCII8Bit() {
    for (int i = 0; i < 256; i++) {
      assertEquals(String.valueOf((char) i), ByteUtils.toASCII8Bit((byte) i));
    }
  }

  @Test
  public void testToExtendedASCIIs() {
    assertEquals("A",
        ByteUtils.toExtendedASCIIs(new byte[] { (byte) 65 }, 1, nativeOrder()));
    assertEquals("\7",
        ByteUtils.toExtendedASCIIs(new byte[] { (byte) 7 }, 1, nativeOrder()));
    assertEquals("\10",
        ByteUtils.toExtendedASCIIs(new byte[] { (byte) 8 }, 1, nativeOrder()));
    assertEquals("\11",
        ByteUtils.toExtendedASCIIs(new byte[] { (byte) 9 }, 1, nativeOrder()));
    assertEquals("\12",
        ByteUtils.toExtendedASCIIs(new byte[] { (byte) 10 }, 1, nativeOrder()));
    assertEquals("\13",
        ByteUtils.toExtendedASCIIs(new byte[] { (byte) 11 }, 1, nativeOrder()));
    assertEquals("\14",
        ByteUtils.toExtendedASCIIs(new byte[] { (byte) 12 }, 1, nativeOrder()));
    assertEquals("\15",
        ByteUtils.toExtendedASCIIs(new byte[] { (byte) 13 }, 1, nativeOrder()));
    assertEquals("\33",
        ByteUtils.toExtendedASCIIs(new byte[] { (byte) 27 }, 1, nativeOrder()));
    assertEquals("\20",
        ByteUtils.toExtendedASCIIs(new byte[] { (byte) 16 }, 1, nativeOrder()));
    assertEquals("A\0",
        ByteUtils.toExtendedASCIIs(new byte[] { (byte) 65 }, 2, LITTLE_ENDIAN));
    assertEquals("\0A",
        ByteUtils.toExtendedASCIIs(new byte[] { (byte) 65 }, 2, BIG_ENDIAN));
    assertEquals("A\177", ByteUtils
        .toExtendedASCIIs(new byte[] { (byte) 65, (byte) 127 }, 2, BIG_ENDIAN));
  }

  @Test
  public void testToUTF() {
    assertEquals("A",
        ByteUtils.toUTF(ByteBuffer.allocate(4).putInt(65).array()));
    assertEquals("\0",
        ByteUtils.toUTF(ByteBuffer.allocate(4).putInt(0).array()));
    assertEquals("\7",
        ByteUtils.toUTF(ByteBuffer.allocate(4).putInt(7).array()));
    assertEquals("〹",
        ByteUtils.toUTF(ByteBuffer.allocate(4).putInt(12345).array()));
    assertEquals("\uD903",
        ByteUtils.toUTF(ByteBuffer.allocate(4).putInt(55555).array()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToUTFException1() {
    ByteUtils.toUTF(ByteBuffer.allocate(4).putInt(-1).array());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToUTFException2() {
    ByteUtils.toUTF(ByteBuffer.allocate(4).putInt(0X10FFFF + 1).array());
  }

  @Test
  public void testToBinaryString() {
    assertEquals("01100001", ByteUtils.toBinaryString("a".getBytes(), true));
    assertEquals("10000110", ByteUtils.toBinaryString("a".getBytes(), false));
    assertEquals("111001101000100010010001",
        ByteUtils.toBinaryString("我".getBytes(), true));
    assertEquals("111001101000100010010001",
        ByteUtils.toBinaryString("我".getBytes()));
    assertEquals("011001110001000110001001",
        ByteUtils.toBinaryString("我".getBytes(), false));
  }

  @Test
  public void testToHexString() {
    assertEquals("61", ByteUtils.toHexString("a".getBytes(), true));
    assertEquals("16", ByteUtils.toHexString("a".getBytes(), false));
    assertEquals("e68891", ByteUtils.toHexString("我".getBytes(), true));
    assertEquals("e68891", ByteUtils.toHexString("我".getBytes()));
    assertEquals("6e8819", ByteUtils.toHexString("我".getBytes(), false));
  }

  @Test
  public void testFromBinaryString() {
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0xFF },
        ByteUtils.fromBinaryString("0000000011111111"));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0xFE },
        ByteUtils.fromBinaryString("000000001111111"));
    assertArrayEquals(new byte[0], ByteUtils.fromBinaryString(""));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromBinaryStringException() {
    ByteUtils.fromBinaryString("000000001X1111111");
  }

  @Test
  public void testFromHexString() {
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0xFF },
        ByteUtils.fromHexString("00Ff"));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0xF0 },
        ByteUtils.fromHexString("00F"));
    assertArrayEquals(new byte[0], ByteUtils.fromHexString(""));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromHexStringException() {
    ByteUtils.fromHexString("00XF");
  }

}
