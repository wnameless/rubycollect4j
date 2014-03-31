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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import javax.xml.bind.TypeConstraintException;

import org.junit.Test;

public class ByteUtilTest {

  @Test
  public void testPrivateConstructor() throws Exception {
    Constructor<ByteUtil> c = ByteUtil.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c.getModifiers()));
    c.setAccessible(true);
    c.newInstance();
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
        ByteUtil.toByteArray((short) 0));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray(Short.valueOf((short) 0)));
  }

  @Test
  public void testToByteArrayWithInteger() {
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x0, (byte) 0x00,
        (byte) 0x00 }, ByteUtil.toByteArray(0));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00 }, ByteUtil.toByteArray(Integer.valueOf(0)));
  }

  @Test
  public void testToByteArrayWithLong() {
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x0, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray(0L));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray(Long.valueOf(0L)));
  }

  @Test
  public void testToByteArrayWithFloat() {
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x0, (byte) 0x00,
        (byte) 0x00 }, ByteUtil.toByteArray((float) 0));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00 }, ByteUtil.toByteArray(Float.valueOf((float) 0)));
  }

  @Test
  public void testToByteArrayWithDouble() {
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x0, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray((double) 0));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray(Double.valueOf((double) 0)));
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
        ByteUtil.toByteArray((char) 0));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray(Character.valueOf((char) 0)));
  }

  @Test
  public void testToByteArrayWithString() {
    assertArrayEquals(new byte[] { (byte) 0x41 }, ByteUtil.toByteArray("A"));
  }

  @Test
  public void testToByteArrayWithObject() {
    assertArrayEquals(new byte[] { (byte) 0x41 },
        ByteUtil.toByteArray((Object) "A"));
    assertArrayEquals(new byte[] { (byte) 0x00 },
        ByteUtil.toByteArray((Object) Byte.valueOf((byte) 0)));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray((Object) Short.valueOf((short) 0)));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00 }, ByteUtil.toByteArray((Object) Integer.valueOf((int) 0)));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray((Object) Long.valueOf((long) 0)));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00 }, ByteUtil.toByteArray((Object) Float.valueOf((float) 0)));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray((Object) Double.valueOf((double) 0)));
    assertArrayEquals(new byte[] { (byte) 0x00 },
        ByteUtil.toByteArray((Object) Boolean.FALSE));
    assertArrayEquals(new byte[] { (byte) 0x00, (byte) 0x00 },
        ByteUtil.toByteArray((Object) Character.valueOf((char) 0)));
  }

  @Test(expected = TypeConstraintException.class)
  public void testToByteArrayWithObjectAndException1() {
    ByteUtil.toByteArray(new ArrayList<Object>());
  }

  @Test(expected = TypeConstraintException.class)
  public void testToByteArrayWithObjectAndException2() {
    ByteUtil.toByteArray((Object) null);
  }

  @Test
  public void testToASCIIs() {
    assertEquals("A", ByteUtil.toASCIIs(new byte[] { (byte) 65 }, 1));
    assertEquals("\7", ByteUtil.toASCIIs(new byte[] { (byte) 7 }, 1));
    assertEquals("\10", ByteUtil.toASCIIs(new byte[] { (byte) 8 }, 1));
    assertEquals("\11", ByteUtil.toASCIIs(new byte[] { (byte) 9 }, 1));
    assertEquals("\12", ByteUtil.toASCIIs(new byte[] { (byte) 10 }, 1));
    assertEquals("\13", ByteUtil.toASCIIs(new byte[] { (byte) 11 }, 1));
    assertEquals("\14", ByteUtil.toASCIIs(new byte[] { (byte) 12 }, 1));
    assertEquals("\15", ByteUtil.toASCIIs(new byte[] { (byte) 13 }, 1));
    assertEquals("\33", ByteUtil.toASCIIs(new byte[] { (byte) 27 }, 1));
    assertEquals("\20", ByteUtil.toASCIIs(new byte[] { (byte) 16 }, 1));
    assertEquals("A\0",
        ByteUtil.toASCIIs(new byte[] { (byte) 65 }, 2, ByteOrder.LITTLE_ENDIAN));
    assertEquals("\0A",
        ByteUtil.toASCIIs(new byte[] { (byte) 65 }, 2, ByteOrder.BIG_ENDIAN));
    assertEquals("A\177", ByteUtil.toASCIIs(
        new byte[] { (byte) 65, (byte) 127 }, 2, ByteOrder.BIG_ENDIAN));
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
