/*
 *
 * Copyright 2013 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package net.sf.rubycollect4j;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyKernel.p;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RubyKernelTest {

  final String lineSeparator = System.getProperty("line.separator");
  final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

  @BeforeEach
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @AfterEach
  public void cleanUpStreams() {
    System.setOut(null);
    System.setErr(null);
  }

  @Test
  public void testP() {
    assertEquals(null, p());
    assertEquals(lineSeparator, outContent.toString());
  }

  @Test
  public void testPObject() {
    Object o = new Object();
    assertEquals(o, p(o));
    assertTrue(Pattern.compile("java\\.lang\\.Object").matcher(outContent.toString()).find());
  }

  @Test
  public void testPObjectWithVarargs() {
    Object o = Integer.valueOf(1);
    assertEquals(ra(o, o), p(o, o));
    assertEquals(ra(o, o).toString() + lineSeparator, outContent.toString());
  }

  @Test
  public void testPString() {
    assertEquals("abc", p("abc"));
    assertEquals("\"abc\"" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPStringWithVarargs() {
    assertEquals(ra("abc", "def"), p("abc", "def"));
    assertEquals("[\"abc\", \"def\"]" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPBoolean() {
    assertTrue(p(true));
    assertEquals("true" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPBooleanWithVarargs() {
    assertEquals(ra(true, false), p(true, false));
    assertEquals(ra(true, false).toString() + lineSeparator, outContent.toString());
  }

  @Test
  public void testPChar() {
    assertEquals('x', (char) p('x'));
    assertEquals("'x'" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPCharWithVarargs() {
    assertEquals(ra('x', 'y', 'z'), p('x', 'y', 'z'));
    assertEquals("['x', 'y', 'z']" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPCharArray() {
    assertArrayEquals(new char[] {'x', 'y', 'z'}, p(new char[] {'x', 'y', 'z'}));
    assertEquals("['x', 'y', 'z']" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPCharArrayWithVarargs() {
    char[] xyz = new char[] {'x', 'y', 'z'};
    assertEquals(ra(xyz, xyz), p(xyz, xyz));
    assertEquals("[['x', 'y', 'z'], ['x', 'y', 'z']]" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPDouble() {
    assertEquals(1.0, p(1.0), 0.0);
    assertEquals("1.0" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPDoubleWithVarargs() {
    assertEquals(ra(1.0, 2.0, 3.0), p(1.0, 2.0, 3.0));
    assertEquals(ra(1.0, 2.0, 3.0).toString() + lineSeparator, outContent.toString());
  }

  @Test
  public void testPFloat() {
    assertEquals(1.0f, p(1.0f), 0.0f);
    assertEquals("1.0" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPFloatWithVarargs() {
    assertEquals(ra(1.0f, 2.0f, 3.0f), p(1.0f, 2.0f, 3.0f));
    assertEquals(ra(1.0f, 2.0f, 3.0f).toString() + lineSeparator, outContent.toString());
  }

  @Test
  public void testPInt() {
    assertEquals(1, (int) p(1));
    assertEquals("1" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPIntWithVarargs() {
    assertEquals(ra(1, 2, 3), p(1, 2, 3));
    assertEquals(ra(1, 2, 3).toString() + lineSeparator, outContent.toString());
  }

  @Test
  public void testPLong() {
    assertEquals(1L, (long) p(1L));
    assertEquals("1" + lineSeparator, outContent.toString());
  }

  @Test
  public void testPLongWithVarargs() {
    assertEquals(ra(1L, 2L, 3L), p(1L, 2L, 3L));
    assertEquals(ra(1L, 2L, 3L).toString() + lineSeparator, outContent.toString());
  }

  @Test
  public void testPByteArray() {
    assertArrayEquals(new byte[] {(byte) 0xFF, (byte) 0x00},
        p(new byte[] {(byte) 0xFF, (byte) 0x00}));
    assertEquals(ra(-1, 0) + lineSeparator, outContent.toString());
  }

  @Test
  public void testPShortArray() {
    assertArrayEquals(new short[] {(short) 1, (short) 0}, p(new short[] {(short) 1, (short) 0}));
    assertEquals(ra(1, 0) + lineSeparator, outContent.toString());
  }

  @Test
  public void testPIntArray() {
    assertArrayEquals(new int[] {1, 0}, p(new int[] {1, 0}));
    assertEquals(ra(1, 0) + lineSeparator, outContent.toString());
  }

  @Test
  public void testPLongArray() {
    assertArrayEquals(new long[] {1L, 0L}, p(new long[] {1L, 0L}));
    assertEquals(ra(1L, 0L) + lineSeparator, outContent.toString());
  }

  @Test
  public void testPFloatArray() {
    assertArrayEquals(new float[] {1, 0}, p(new float[] {1, 0}), 0);
    assertEquals(ra((float) 1, (float) 0) + lineSeparator, outContent.toString());
  }

  @Test
  public void testPDoubleArray() {
    assertArrayEquals(new double[] {1.0, 0.0}, p(new double[] {1.0, 0.0}), 0.0);
    assertEquals(ra(1.0, 0.0) + lineSeparator, outContent.toString());
  }

  @Test
  public void testPBooleanArray() {
    boolean[] bools = p(new boolean[] {true, false});
    assertEquals(2, bools.length);
    assertEquals(true, bools[0]);
    assertEquals(false, bools[1]);
    assertEquals(ra(true, false) + lineSeparator, outContent.toString());
  }

  @Test
  public void testPMapWithIterableKeysAndIteratorValues() {
    RubyHash<Iterable<String>, Iterator<Character>> rh = Ruby.Hash.create(
        Ruby.Array.of(Ruby.Entry.of(Ruby.Array.of("a", "b"), Ruby.Array.of('a', 'b').iterator())));
    assertSame(rh, p(rh));
    assertEquals("{[\"a\", \"b\"]=['a', 'b']}" + lineSeparator, outContent.toString());
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Test
  public void testPObjectArray() {
    @SuppressWarnings("serial")
    List[] listArray = new List[] {new ArrayList() {
      {
        add(1);
        add(2);
      }
    }, new ArrayList() {
      {
        add('a');
        add('b');
      }
    }};
    assertSame(listArray, p(listArray));
    assertEquals("[[1, 2], ['a', 'b']]" + lineSeparator, outContent.toString());
  }

}
