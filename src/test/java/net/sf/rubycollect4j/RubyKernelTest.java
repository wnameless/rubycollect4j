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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static net.sf.rubycollect4j.RubyKernel.p;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RubyKernelTest {

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
    p();
    if (System.getProperty("os.name").startsWith("Windows")) {
      assertEquals("\r\n", outContent.toString());
    } else {
      assertEquals("\n", outContent.toString());
    }
  }

  @Test
  public void testPutsString() {
    p("");
    if (System.getProperty("os.name").startsWith("Windows")) {
      assertEquals("\r\n", outContent.toString());
    } else {
      assertEquals("\n", outContent.toString());
    }
  }

  @Test
  public void testPutsObject() {
    p(new Object());
    assertTrue(Pattern.compile("java\\.lang\\.Object")
        .matcher(outContent.toString()).find());
  }

  @Test
  public void testPutsBoolean() {
    p(true);
    if (System.getProperty("os.name").startsWith("Windows")) {
      assertEquals("true\r\n", outContent.toString());
    } else {
      assertEquals("true\n", outContent.toString());
    }
  }

  @Test
  public void testPutsChar() {
    p('x');
    if (System.getProperty("os.name").startsWith("Windows")) {
      assertEquals("x\r\n", outContent.toString());
    } else {
      assertEquals("x\n", outContent.toString());
    }
  }

  @Test
  public void testPutsCharArray() {
    p(new char[] { 'x', 'y', 'z' });
    if (System.getProperty("os.name").startsWith("Windows")) {
      assertEquals("xyz\r\n", outContent.toString());
    } else {
      assertEquals("xyz\n", outContent.toString());
    }
  }

  @Test
  public void testPutsDouble() {
    p(1.0);
    if (System.getProperty("os.name").startsWith("Windows")) {
      assertEquals("1.0\r\n", outContent.toString());
    } else {
      assertEquals("1.0\n", outContent.toString());
    }
  }

  @Test
  public void testPutsFloat() {
    p(1.0f);
    if (System.getProperty("os.name").startsWith("Windows")) {
      assertEquals("1.0\r\n", outContent.toString());
    } else {
      assertEquals("1.0\n", outContent.toString());
    }
  }

  @Test
  public void testPutsInt() {
    p(1);
    if (System.getProperty("os.name").startsWith("Windows")) {
      assertEquals("1\r\n", outContent.toString());
    } else {
      assertEquals("1\n", outContent.toString());
    }
  }

  @Test
  public void testPutsLong() {
    p(1L);
    if (System.getProperty("os.name").startsWith("Windows")) {
      assertEquals("1\r\n", outContent.toString());
    } else {
      assertEquals("1\n", outContent.toString());
    }
  }

}
