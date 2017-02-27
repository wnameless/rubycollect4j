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
package net.sf.rubycollect4j;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyKernel.p;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RubyKernelTest {

  final String lineSeparator = System.getProperty("line.separator");
  final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @After
  public void cleanUpStreams() {
    System.setOut(null);
    System.setErr(null);
  }

  @Test
  public void testPrivateConstructor() throws Exception {
    Constructor<RubyKernel> c = RubyKernel.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c.getModifiers()));
    c.setAccessible(true);
    c.newInstance();
  }

  @Test
  public void testPNothing() {
    assertNull(p());
    assertEquals("", outContent.toString());
  }

  @Test
  public void testPObject() {
    Object o = new Object();
    assertEquals(o, p(o));
    assertTrue(Pattern.compile("java\\.lang\\.Object")
        .matcher(outContent.toString()).find());
  }

  @Test
  public void testPObjectWithVarargs() {
    Object o = Integer.valueOf(1);
    assertEquals(ra(o, o), p(o, o));
    assertEquals(ra(o, o, null).join(lineSeparator), outContent.toString());
  }

  @Test
  public void testPString() {
    assertEquals("abc", p("abc"));
    assertEquals("abc" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPStringWithVarargs() {
    assertEquals(ra("abc", "def"), p("abc", "def"));
    assertEquals(ra("abc", "def", null).join(lineSeparator),
        outContent.toString());
  }

  @Test
  public void testPBoolean() {
    assertTrue(p(true));
    assertEquals("true" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPBooleanWithVarargs() {
    assertEquals(ra(true, false), p(true, false));
    assertEquals(ra(true, false, null).join(lineSeparator),
        outContent.toString());
  }

  @Test
  public void testPChar() {
    assertEquals('x', p('x'));
    assertEquals("x" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPCharWithVarargs() {
    assertEquals(ra('x', 'y', 'z'), p('x', 'y', 'z'));
    assertEquals(ra('x', 'y', 'z', null).join(lineSeparator),
        outContent.toString());
  }

  @Test
  public void testPCharArray() {
    assertArrayEquals(new char[] { 'x', 'y', 'z' },
        p(new char[] { 'x', 'y', 'z' }));
    assertEquals("xyz" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPCharArrayWithVarargs() {
    char[] xyz = new char[] { 'x', 'y', 'z' };
    assertEquals(ra(xyz, xyz), p(xyz, xyz));
    assertEquals("xyz" + lineSeparator + "xyz" + lineSeparator,
        outContent.toString());
  }

  @Test
  public void testPDouble() {
    assertEquals(1.0, p(1.0), 0.0);
    assertEquals("1.0" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPDoubleWithVarargs() {
    assertEquals(ra(1.0, 2.0, 3.0), p(1.0, 2.0, 3.0));
    assertEquals(ra(1.0, 2.0, 3.0, null).join(lineSeparator),
        outContent.toString());
  }

  @Test
  public void testPFloat() {
    assertEquals(1.0f, p(1.0f), 0.0f);
    assertEquals("1.0" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPFloatWithVarargs() {
    assertEquals(ra(1.0f, 2.0f, 3.0f), p(1.0f, 2.0f, 3.0f));
    assertEquals(ra(1.0f, 2.0f, 3.0f, null).join(lineSeparator),
        outContent.toString());
  }

  @Test
  public void testPInt() {
    assertEquals(1, p(1));
    assertEquals("1" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPIntWithVarargs() {
    assertEquals(ra(1, 2, 3), p(1, 2, 3));
    assertEquals(ra(1, 2, 3, null).join(lineSeparator), outContent.toString());
  }

  @Test
  public void testPLong() {
    assertEquals(1L, p(1L));
    assertEquals("1" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPLongWithVarargs() {
    assertEquals(ra(1L, 2L, 3L), p(1L, 2L, 3L));
    assertEquals(ra(1L, 2L, 3L, null).join(lineSeparator),
        outContent.toString());
  }

  @Test
  public void testPByteArray() {
    assertArrayEquals(new byte[] { (byte) 0xFF, (byte) 0x00 },
        p(new byte[] { (byte) 0xFF, (byte) 0x00 }));
    assertEquals(ra(-1, 0) + lineSeparator, outContent.toString());
  }

  @Test
  public void testPShortArray() {
    assertArrayEquals(new short[] { (short) 1, (short) 0 },
        p(new short[] { (short) 1, (short) 0 }));
    assertEquals(ra(1, 0) + lineSeparator, outContent.toString());
  }

  @Test
  public void testPIntArray() {
    assertArrayEquals(new int[] { 1, 0 }, p(new int[] { 1, 0 }));
    assertEquals(ra(1, 0) + lineSeparator, outContent.toString());
  }

  @Test
  public void testPLongArray() {
    assertArrayEquals(new long[] { 1L, 0L }, p(new long[] { 1L, 0L }));
    assertEquals(ra(1L, 0L) + lineSeparator, outContent.toString());
  }

  @Test
  public void testPFloatArray() {
    assertArrayEquals(new float[] { 1, 0 }, p(new float[] { 1, 0 }), 0);
    assertEquals(ra((float) 1, (float) 0) + lineSeparator,
        outContent.toString());
  }

  @Test
  public void testPDoubleArray() {
    assertArrayEquals(new double[] { 1.0, 0.0 }, p(new double[] { 1.0, 0.0 }),
        0.0);
    assertEquals(ra(1.0, 0.0) + lineSeparator, outContent.toString());
  }

  @Test
  public void testPBooleanArray() {
    boolean[] bools = p(new boolean[] { true, false });
    assertEquals(2, bools.length);
    assertEquals(true, bools[0]);
    assertEquals(false, bools[1]);
    assertEquals(ra(true, false) + lineSeparator, outContent.toString());
  }

}
