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

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.nio.ByteOrder;

import javax.xml.bind.TypeConstraintException;

import org.junit.Test;

public class ByteUtilTest {

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
  }

  @Test(expected = TypeConstraintException.class)
  public void testToByteArrayWithObjectAndException() {
    ByteUtil.toByteArray(newArrayList());
  }

  @Test
  public void testToASCII() {
    assertEquals("A", ByteUtil.toASCII(new byte[] { (byte) 65 }, 1));
    assertEquals("\\a", ByteUtil.toASCII(new byte[] { (byte) 7 }, 1));
    assertEquals("\\b", ByteUtil.toASCII(new byte[] { (byte) 8 }, 1));
    assertEquals("\\t", ByteUtil.toASCII(new byte[] { (byte) 9 }, 1));
    assertEquals("\\n", ByteUtil.toASCII(new byte[] { (byte) 10 }, 1));
    assertEquals("\\v", ByteUtil.toASCII(new byte[] { (byte) 11 }, 1));
    assertEquals("\\f", ByteUtil.toASCII(new byte[] { (byte) 12 }, 1));
    assertEquals("\\r", ByteUtil.toASCII(new byte[] { (byte) 13 }, 1));
    assertEquals("\\e", ByteUtil.toASCII(new byte[] { (byte) 27 }, 1));
    assertEquals("\\x10", ByteUtil.toASCII(new byte[] { (byte) 16 }, 1));
    assertEquals(ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN ? "\\x00A"
        : "A\\x00", ByteUtil.toASCII(new byte[] { (byte) 65 }, 2));
  }

  @Test
  public void testToBytesArray() {
    assertArrayEquals(new byte[][] { new byte[] { (byte) 65 } },
        ByteUtil.toBytesArray(((byte) 65)));
    assertArrayEquals(new byte[][] { new byte[] { (byte) 0, (byte) 0 } },
        ByteUtil.toBytesArray(((short) 0)));
    assertArrayEquals(new byte[][] { new byte[] { (byte) 0, (byte) 0, (byte) 0,
        (byte) 0 } }, ByteUtil.toBytesArray((0)));
    assertArrayEquals(new byte[][] { new byte[] { (byte) 0, (byte) 0, (byte) 0,
        (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 } },
        ByteUtil.toBytesArray((0L)));
    assertArrayEquals(new byte[][] { new byte[] { (byte) 0, (byte) 0, (byte) 0,
        (byte) 0 } }, ByteUtil.toBytesArray(((float) 0)));
    assertArrayEquals(new byte[][] { new byte[] { (byte) 0, (byte) 0, (byte) 0,
        (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 } },
        ByteUtil.toBytesArray((0.0)));
    assertArrayEquals(new byte[][] { new byte[] { (byte) 0 } },
        ByteUtil.toBytesArray((false)));
    assertArrayEquals(new byte[][] { new byte[] { (byte) 0, (byte) 0 } },
        ByteUtil.toBytesArray(((char) 0)));
    assertArrayEquals(new byte[][] { new byte[] { (byte) 65 } },
        ByteUtil.toBytesArray("A"));
    assertArrayEquals(new byte[][] { new byte[] { (byte) 0 } },
        ByteUtil.toBytesArray(BigInteger.valueOf(0)));
  }

}
