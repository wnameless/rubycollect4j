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
package net.sf.rubycollect4j;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyKernel.p;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RubyKernelTest {

  private final String lineSeparator = System.getProperty("line.separator");
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

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
  public void testPutsNothing() {
    assertNull(p());
    assertEquals("", outContent.toString());
  }

  @Test
  public void testPutsObject() {
    Object o = new Object();
    assertEquals(o, p(o));
    assertTrue(Pattern.compile("java\\.lang\\.Object")
        .matcher(outContent.toString()).find());
  }

  @Test
  public void testPutsObjectWithVarargs() {
    Object o = Integer.valueOf(1);
    assertEquals(ra(o, o), p(o, o));
    assertEquals(ra(o, o, null).join(lineSeparator), outContent.toString());
  }

  @Test
  public void testPutsString() {
    assertEquals("abc", p("abc"));
    assertEquals("abc" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPutsStringWithVarargs() {
    assertEquals(ra("abc", "def"), p("abc", "def"));
    assertEquals(ra("abc", "def", null).join(lineSeparator),
        outContent.toString());
  }

  @Test
  public void testPutsBoolean() {
    assertEquals(true, p(true));
    assertEquals("true" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPutsBooleanWithVarargs() {
    assertEquals(ra(true, false), p(true, false));
    assertEquals(ra(true, false, null).join(lineSeparator),
        outContent.toString());
  }

  @Test
  public void testPutsChar() {
    assertEquals('x', p('x'));
    assertEquals("x" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPutsCharWithVarargs() {
    assertEquals(ra('x', 'y', 'z'), p('x', 'y', 'z'));
    assertEquals(ra('x', 'y', 'z', null).join(lineSeparator),
        outContent.toString());
  }

  @Test
  public void testPutsCharArray() {
    assertArrayEquals(new char[] { 'x', 'y', 'z' }, p(new char[] { 'x', 'y',
        'z' }));
    assertEquals("xyz" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPutsCharArrayWithVarargs() {
    char[] xyz = new char[] { 'x', 'y', 'z' };
    assertEquals(ra(xyz, xyz), p(xyz, xyz));
    assertEquals("xyz" + lineSeparator + "xyz" + lineSeparator,
        outContent.toString());
  }

  @Test
  public void testPutsDouble() {
    assertEquals(1.0, p(1.0), 0.0);
    assertEquals("1.0" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPutsDoubleWithVarargs() {
    assertEquals(ra(1.0, 2.0, 3.0), p(1.0, 2.0, 3.0));
    assertEquals(ra(1.0, 2.0, 3.0, null).join(lineSeparator),
        outContent.toString());
  }

  @Test
  public void testPutsFloat() {
    assertEquals(1.0f, p(1.0f), 0.0f);
    assertEquals("1.0" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPutsFloatWithVarargs() {
    assertEquals(ra(1.0f, 2.0f, 3.0f), p(1.0f, 2.0f, 3.0f));
    assertEquals(ra(1.0f, 2.0f, 3.0f, null).join(lineSeparator),
        outContent.toString());
  }

  @Test
  public void testPutsInt() {
    assertEquals(1, p(1));
    assertEquals("1" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPutsIntWithVarargs() {
    assertEquals(ra(1, 2, 3), p(1, 2, 3));
    assertEquals(ra(1, 2, 3, null).join(lineSeparator), outContent.toString());
  }

  @Test
  public void testPutsLong() {
    assertEquals(1L, p(1L));
    assertEquals("1" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPutsLongWithVarargs() {
    assertEquals(ra(1L, 2L, 3L), p(1L, 2L, 3L));
    assertEquals(ra(1L, 2L, 3L, null).join(lineSeparator),
        outContent.toString());
  }

}
