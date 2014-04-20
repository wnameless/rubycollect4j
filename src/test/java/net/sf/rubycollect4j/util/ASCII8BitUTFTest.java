/**
 *
 * @author Wei-Ming Wu
 *
 *
 * Copyright 2014 Wei-Ming Wu
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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ASCII8BitUTFTest {

  ASCII8BitUTF a8u;
  ASCII8BitUTF emptyA8u;

  @Before
  public void setUp() throws Exception {
    a8u = new ASCII8BitUTF("我\377abc");
    emptyA8u = new ASCII8BitUTF("");
  }

  @Test
  public void testConstructor() {
    assertTrue(a8u instanceof ASCII8BitUTF);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException() {
    new ASCII8BitUTF(null);
  }

  @Test
  public void testTotalByteNumber() {
    assertEquals(7, a8u.totalByteNumber());
  }

  @Test
  public void testRemainingByteNumber() {
    a8u.nextByte();
    assertEquals(6, a8u.remainingByteNumber());
    a8u.reset();
    a8u.nextChar();
    assertEquals(4, a8u.remainingByteNumber());
  }

  @Test
  public void testReset() {
    a8u.nextByte(100);
    assertEquals(0, a8u.remainingByteNumber());
    a8u.reset();
    assertEquals(7, a8u.remainingByteNumber());
  }

  @Test
  public void testHasChar() {
    a8u = new ASCII8BitUTF("\377我");
    assertTrue(a8u.hasChar());
    a8u.nextChar();
    assertTrue(a8u.hasChar());
    a8u.nextByte();
    assertFalse(a8u.hasChar());
  }

  @Test
  public void testHasByte() {
    assertTrue(a8u.hasByte());
    a8u.nextByte(6);
    assertTrue(a8u.hasByte());
    a8u.nextByte();
    assertFalse(a8u.hasByte());
  }

  @Test
  public void testNextChar() {
    a8u.nextByte();
    assertEquals("\377", a8u.nextChar());
    assertEquals("a", a8u.nextChar());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextCharException() {
    emptyA8u.nextChar();
  }

  @Test
  public void testNextByte() {
    assertEquals((byte) -26, a8u.nextByte());
    assertEquals((byte) -120, a8u.nextByte());
    assertEquals((byte) -111, a8u.nextByte());
    assertEquals((byte) -1, a8u.nextByte());
    assertEquals((byte) 97, a8u.nextByte());
    assertEquals((byte) 98, a8u.nextByte());
    assertEquals((byte) 99, a8u.nextByte());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextByteException() {
    emptyA8u.nextByte();
  }

  @Test
  public void testNextByteWithNumber() {
    a8u = new ASCII8BitUTF("abc");
    assertArrayEquals(new byte[] { (byte) 97, (byte) 98 }, a8u.nextByte(2));
    assertArrayEquals(new byte[] { (byte) 99 }, a8u.nextByte(2));
  }

  @Test
  public void testEquals() {
    assertTrue(a8u.equals(a8u));
    assertTrue(a8u.equals(new ASCII8BitUTF("我\377abc")));
    assertFalse(a8u.equals(null));
  }

  @Test
  public void testHashCode() {
    assertEquals(a8u.hashCode(), new ASCII8BitUTF("我\377abc").hashCode());
    assertNotEquals(a8u.hashCode(), new ASCII8BitUTF("\377abc").hashCode());
  }

  @Test
  public void testToString() {
    assertEquals("ASCII8BitUTF{我\377abc}", a8u.toString());
  }

}
