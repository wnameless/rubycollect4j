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

import net.sf.rubycollect4j.RubyArray;

import org.junit.Test;

public class ByteUtilTest {

  ByteOrder bo = LITTLE_ENDIAN;

  @Test
  public void testPrivateConstructor() throws Exception {
    Constructor<ByteUtil> c = ByteUtil.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c.getModifiers()));
    c.setAccessible(true);
    c.newInstance();
  }

  @Test
  public void testToList() {
    RubyArray<Byte> bytes =
        ByteUtil.toList(new byte[] { (byte) 0x00, (byte) 0x01, (byte) 0x02 });
    assertEquals(ra((byte) 0x00, (byte) 0x01, (byte) 0x02), bytes);
  }

  @Test
  public void testToArray() {
    List<Byte> bytes = ra((byte) 0x00, (byte) 0x01, (byte) 0x02);
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x01, (byte) 0x02 },
        ByteUtil.toArray(bytes));
  }

  @Test
  public void testLjust() {
    byte[] bytes =
        new byte[] { (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03 };
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x01, (byte) 0x02 },
        ByteUtil.ljust(bytes, 3));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x01, (byte) 0x02,
        (byte) 0x03, (byte) 0x00 }, ByteUtil.ljust(bytes, 5));
  }

  @Test
  public void testRjust() {
    byte[] bytes =
        new byte[] { (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03 };
    assertArrayEquals(new byte[] { (byte) 0x01, (byte) 0x02, (byte) 0x03 },
        ByteUtil.rjust(bytes, 3));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x01,
        (byte) 0x02, (byte) 0x03 }, ByteUtil.rjust(bytes, 5));
  }

  @Test
  public void testReverse() {
    byte[] bytes = new byte[] { (byte) 1, (byte) 2, (byte) 3, (byte) 4 };
    ByteUtil.reverse(bytes);
    assertArrayEquals(new byte[] { (byte) 4, (byte) 3, (byte) 2, (byte) 1 },
        bytes);
  }

  @Test
  public void testToByteArrayWithByte() {
    assertArrayEquals(new byte[] { (byte) 0x00 },
        ByteUtil.toByteArray((byte) 0x00));
    assertArrayEquals(new byte[] { (byte) 0x00 },
        ByteUtil.toByteArray(Byte.valueOf((byte) 0x00)));
  }

  @Test
  public void testToByteArrayWithShort() {
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray((short) 0, bo));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray(Short.valueOf((short) 0), bo));
  }

  @Test
  public void testToByteArrayWithInteger() {
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x0, (byte) 0x00,
        (byte) 0x00 }, ByteUtil.toByteArray(0, bo));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00 }, ByteUtil.toByteArray(Integer.valueOf(0), bo));
  }

  @Test
  public void testToByteArrayWithLong() {
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x0, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray(0L, bo));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray(Long.valueOf(0L), bo));
  }

  @Test
  public void testToByteArrayWithFloat() {
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x0, (byte) 0x00,
        (byte) 0x00 }, ByteUtil.toByteArray(0f, bo));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00 }, ByteUtil.toByteArray(Float.valueOf(0f), bo));
  }

  @Test
  public void testToByteArrayWithDouble() {
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x0, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray(0d, bo));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray(Double.valueOf(0d), bo));
  }

  @Test
  public void testToByteArrayWithBoolean() {
    assertArrayEquals(new byte[] { (byte) 0x00 }, ByteUtil.toByteArray(false));
    assertArrayEquals(new byte[] { (byte) 0x01 }, ByteUtil.toByteArray(true));
    assertArrayEquals(new byte[] { (byte) 0x00 },
        ByteUtil.toByteArray(Boolean.FALSE));
    assertArrayEquals(new byte[] { (byte) 0x01 },
        ByteUtil.toByteArray(Boolean.TRUE));
  }

  @Test
  public void testToByteArrayWithCharacter() {
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray((char) 0, bo));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray(Character.valueOf((char) 0), bo));
  }

  @Test
  public void testToByteArrayWithString() {
    assertArrayEquals(new byte[] { (byte) 0x41 }, ByteUtil.toByteArray("A"));
  }

  @Test
  public void testToByteArrayWithObject() {
    assertArrayEquals(new byte[] { (byte) 0x41 },
        ByteUtil.toByteArray((Object) "A", bo));
    assertArrayEquals(new byte[] { (byte) 0x00 },
        ByteUtil.toByteArray((Object) Byte.valueOf((byte) 0), bo));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray((Object) Short.valueOf((short) 0), bo));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00 }, ByteUtil.toByteArray((Object) Integer.valueOf(0), bo));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray((Object) Long.valueOf(0L), bo));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00 }, ByteUtil.toByteArray((Object) Float.valueOf(0f), bo));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray((Object) Double.valueOf(0d), bo));
    assertArrayEquals(new byte[] { (byte) 0x00 },
        ByteUtil.toByteArray((Object) Boolean.FALSE, bo));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray((Object) Character.valueOf((char) 0), bo));
  }

  @Test(expected = TypeConstraintException.class)
  public void testToByteArrayWithObjectAndException1() {
    ByteUtil.toByteArray(new ArrayList<Object>(), bo);
  }

  @Test(expected = TypeConstraintException.class)
  public void testToByteArrayWithObjectAndException2() {
    ByteUtil.toByteArray((Object) null, bo);
  }

  @Test
  public void testToASCII8Bit() {
    for (int i = 0; i < 256; i++) {
      assertEquals(String.valueOf((char) i), ByteUtil.toASCII8Bit((byte) i));
    }
  }

  @Test
  public void testToExtendedASCIIs() {
    assertEquals("A",
        ByteUtil.toExtendedASCIIs(new byte[] { (byte) 65 }, 1, nativeOrder()));
    assertEquals("\7",
        ByteUtil.toExtendedASCIIs(new byte[] { (byte) 7 }, 1, nativeOrder()));
    assertEquals("\10",
        ByteUtil.toExtendedASCIIs(new byte[] { (byte) 8 }, 1, nativeOrder()));
    assertEquals("\11",
        ByteUtil.toExtendedASCIIs(new byte[] { (byte) 9 }, 1, nativeOrder()));
    assertEquals("\12",
        ByteUtil.toExtendedASCIIs(new byte[] { (byte) 10 }, 1, nativeOrder()));
    assertEquals("\13",
        ByteUtil.toExtendedASCIIs(new byte[] { (byte) 11 }, 1, nativeOrder()));
    assertEquals("\14",
        ByteUtil.toExtendedASCIIs(new byte[] { (byte) 12 }, 1, nativeOrder()));
    assertEquals("\15",
        ByteUtil.toExtendedASCIIs(new byte[] { (byte) 13 }, 1, nativeOrder()));
    assertEquals("\33",
        ByteUtil.toExtendedASCIIs(new byte[] { (byte) 27 }, 1, nativeOrder()));
    assertEquals("\20",
        ByteUtil.toExtendedASCIIs(new byte[] { (byte) 16 }, 1, nativeOrder()));
    assertEquals("A\0",
        ByteUtil.toExtendedASCIIs(new byte[] { (byte) 65 }, 2, LITTLE_ENDIAN));
    assertEquals("\0A",
        ByteUtil.toExtendedASCIIs(new byte[] { (byte) 65 }, 2, BIG_ENDIAN));
    assertEquals("A\177", ByteUtil.toExtendedASCIIs(new byte[] { (byte) 65,
        (byte) 127 }, 2, BIG_ENDIAN));
  }

  @Test
  public void testToUTF() {
    assertEquals("A", ByteUtil.toUTF(ByteBuffer.allocate(4).putInt(65).array()));
    assertEquals("\0", ByteUtil.toUTF(ByteBuffer.allocate(4).putInt(0).array()));
    assertEquals("\7", ByteUtil.toUTF(ByteBuffer.allocate(4).putInt(7).array()));
    assertEquals("〹",
        ByteUtil.toUTF(ByteBuffer.allocate(4).putInt(12345).array()));
    assertEquals("\uD903",
        ByteUtil.toUTF(ByteBuffer.allocate(4).putInt(55555).array()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToUTFException1() {
    ByteUtil.toUTF(ByteBuffer.allocate(4).putInt(-1).array());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToUTFException2() {
    ByteUtil.toUTF(ByteBuffer.allocate(4).putInt(0X10FFFF + 1).array());
  }

  @Test
  public void testToBinaryString() {
    assertEquals("01100001", ByteUtil.toBinaryString("a".getBytes(), true));
    assertEquals("10000110", ByteUtil.toBinaryString("a".getBytes(), false));
    assertEquals("111001101000100010010001",
        ByteUtil.toBinaryString("我".getBytes(), true));
    assertEquals("011001110001000110001001",
        ByteUtil.toBinaryString("我".getBytes(), false));
  }

  @Test
  public void testToHexString() {
    assertEquals("61", ByteUtil.toHexString("a".getBytes(), true));
    assertEquals("16", ByteUtil.toHexString("a".getBytes(), false));
    assertEquals("e68891", ByteUtil.toHexString("我".getBytes(), true));
    assertEquals("6e8819", ByteUtil.toHexString("我".getBytes(), false));
  }

}
