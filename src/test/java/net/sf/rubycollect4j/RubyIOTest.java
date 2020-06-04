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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.sf.rubycollect4j.RubyIO.Mode;

public class RubyIOTest {

  static final String BASE_DIR = "src/test/resources/";
  RubyIO io;

  @Test
  public void testConstructor() throws Exception {
    io = new RubyIO(new File(BASE_DIR + "ruby_io_read_only_mode.txt"), Mode.R);
    assertTrue(io instanceof RubyIO);
  }

  @Test
  public void testConstructorException() throws Exception {
    assertThrows(NullPointerException.class, () -> {
      new RubyIO(null, Mode.R);
    });
  }

  @Test
  public void testOpen() {
    io = RubyIO.open(BASE_DIR + "ruby_io_read_only_mode.txt");
    assertEquals(RubyIO.class, io.getClass());
    io.close();
  }

  @Test
  public void testOpenException() {
    assertThrows(RuntimeException.class, () -> {
      RubyIO.open(BASE_DIR + "no_file.txt");
    });
  }

  @Test
  public void testOpenModeWithInvalidString() {
    assertThrows(IllegalArgumentException.class, () -> {
      RubyIO.open(BASE_DIR + "ruby_io_read_only_mode.txt", "haha");
    });
  }

  @Test
  public void testOpenWithMode() {
    io = RubyIO.open(BASE_DIR + "ruby_io_read_only_mode.txt", Mode.R);
    assertEquals(RubyIO.class, io.getClass());
    io.close();
  }

  @Test
  public void testOpenWithModeException() {
    assertThrows(RuntimeException.class, () -> {
      io = RubyIO.open(BASE_DIR + "no_file.txt", Mode.R);
    });
  }

  @Test
  public void testReadOnlyMode() {
    io = RubyIO.open(BASE_DIR + "ruby_io_read_only_mode.txt");
    assertEquals(ra("a", "bc", "def"), io.eachLine().toA());
    io = RubyIO.open(BASE_DIR + "ruby_io_read_only_mode.txt", "r");
    assertEquals(ra("a", "bc", "def"), io.eachLine().toA());
  }

  @Test
  public void testReadOnlyModeException1() {
    assertThrows(IllegalStateException.class, () -> {
      RubyIO.open(BASE_DIR + "ruby_io_read_only_mode.txt").puts("test");
    });
  }

  @Test
  public void testReadOnlyModeException2() {
    assertThrows(IllegalStateException.class, () -> {
      RubyIO.open(BASE_DIR + "ruby_io_read_only_mode.txt").write("test");
    });
  }

  @Test
  public void testReadWriteMode() {
    io = RubyIO.open(BASE_DIR + "ruby_io_read_write_mode.txt", "w");
    io.close();
    io = RubyIO.open(BASE_DIR + "ruby_io_read_write_mode.txt", "r+");
    io.puts("一");
    io.puts("二三");
    io.puts("四五六");
    io.close();
    io = RubyIO.open(BASE_DIR + "ruby_io_read_write_mode.txt", "r+");
    assertEquals(ra("一", "二三", "四五六"), io.eachLine().toA());
    io.close();
    io = RubyIO.open(BASE_DIR + "ruby_io_read_write_mode.txt", "w");
    io.close();
    io = RubyIO.open(BASE_DIR + "ruby_io_read_write_mode.txt", "r+");
    io.puts("1");
    io.puts("2");
    io.puts("3");
    io.close();
    io = RubyIO.open(BASE_DIR + "ruby_io_read_write_mode.txt", "r+");
    io.puts("4");
    io.close();
    io = RubyIO.open(BASE_DIR + "ruby_io_read_write_mode.txt", "r+");
    assertEquals(ra("4", "2", "3"), io.eachLine().toA());
    io.close();
  }

  @Test
  public void testWriteOnlyMode() {
    io = RubyIO.open(BASE_DIR + "ruby_io_write_only_mode.txt", "w");
    io.puts("1");
    io.puts("2");
    io.puts("3");
    io.close();
    io = RubyIO.open(BASE_DIR + "ruby_io_write_only_mode.txt", "r");
    assertEquals(ra("1", "2", "3"), io.eachLine().toA());
    io.close();
    io = RubyIO.open(BASE_DIR + "ruby_io_write_only_mode.txt", "w");
    io.close();
    io = RubyIO.open(BASE_DIR + "ruby_io_write_only_mode.txt", "r");
    assertEquals(ra(), io.eachLine().toA());
    io.close();
  }

  @Test
  public void testWriteOnlyModeException() {
    assertThrows(IllegalStateException.class, () -> {
      RubyIO.open(BASE_DIR + "ruby_io_write_only_mode.txt", "w").read();
    });
  }

  @Test
  public void testWriteReadMode() {
    io = RubyIO.open(BASE_DIR + "ruby_io_write_read_mode.txt", "w+");
    io.write("123");
    io.write("456");
    io.seek(0);
    assertEquals("123456", io.read());
    io.close();
    io = RubyIO.open(BASE_DIR + "ruby_io_write_read_mode.txt", "r");
    assertEquals("123456", io.read());
    io.close();
  }

  @Test
  public void testAppendOnlyMode() {
    io = RubyIO.open(BASE_DIR + "ruby_io_append_only_mode.txt", "w");
    io.close();
    io = RubyIO.open(BASE_DIR + "ruby_io_append_only_mode.txt", "a");
    io.write("123");
    io.close();
    io = RubyIO.open(BASE_DIR + "ruby_io_append_only_mode.txt", "a");
    io.write("456");
    io.close();
    io = RubyIO.open(BASE_DIR + "ruby_io_append_only_mode.txt", "r");
    assertEquals("123456", io.read());
    io.close();
  }

  @Test
  public void testAppendOnlyModeException() {
    assertThrows(IllegalStateException.class, () -> {
      RubyIO.open(BASE_DIR + "ruby_io_append_only_mode.txt", "a").read();
    });
  }

  @Test
  public void testAppendReadMode() {
    io = RubyIO.open(BASE_DIR + "ruby_io_append_read_mode.txt", "w");
    io.close();
    io = RubyIO.open(BASE_DIR + "ruby_io_append_read_mode.txt", "a+");
    io.write("123");
    io.close();
    io = RubyIO.open(BASE_DIR + "ruby_io_append_read_mode.txt", "a+");
    io.write("456");
    io.seek(0);
    assertEquals("123456", io.read());
    io.close();
  }

  @Test
  public void testForeach() {
    String file = BASE_DIR + "ruby_io_read_only_mode.txt";
    assertTrue(RubyIO.foreach(file) instanceof RubyEnumerator);
    assertEquals(ra("a", "bc", "def"), RubyIO.foreach(file).toA());
  }

  @Test
  public void testForeachKeepNewLine() {
    String file = BASE_DIR + "ruby_io_read_only_mode.txt";
    assertTrue(RubyIO.foreach(file, true) instanceof RubyEnumerator);
    assertEquals(ra("a\n", "bc\n", "def\n"), RubyIO.foreach(file, true).toA());
  }

  @Test
  public void testForeachWithFile() {
    String file = BASE_DIR + "ruby_io_read_only_mode.txt";
    assertTrue(RubyIO.foreach(new File(file)) instanceof RubyEnumerator);
    assertEquals(ra("a", "bc", "def"), RubyIO.foreach(new File(file)).toA());
  }

  @Test
  public void testForeachWithFileKeepNewLine() {
    String file = BASE_DIR + "ruby_io_read_only_mode.txt";
    assertTrue(RubyIO.foreach(new File(file), true) instanceof RubyEnumerator);
    assertEquals(ra("a\n", "bc\n", "def\n"),
        RubyIO.foreach(new File(file), true).toA());
  }

  @Test
  public void testForeachWithInputStream() throws Exception {
    String file = BASE_DIR + "ruby_io_read_only_mode.txt";
    assertTrue(RubyIO.foreach(
        new FileInputStream(new File(file))) instanceof RubyEnumerator);
    assertEquals(ra("a", "bc", "def"),
        RubyIO.foreach(new FileInputStream(new File(file))).toA());
  }

  @Test
  public void testForeachWithInputStreamKeepNewLine() throws Exception {
    String file = BASE_DIR + "ruby_io_read_only_mode.txt";
    assertTrue(RubyIO.foreach(new FileInputStream(new File(file)),
        true) instanceof RubyEnumerator);
    assertEquals(ra("a\n", "bc\n", "def\n"),
        RubyIO.foreach(new FileInputStream(new File(file)), true).toA());
  }

  @Test
  public void testForeachWithBlock() {
    final RubyArray<String> ra = ra();
    RubyIO.foreach(BASE_DIR + "ruby_io_read_only_mode.txt",
        item -> ra.add(item));
    assertEquals("a" + "bc" + "def", ra.join());
  }

  @Test
  public void testForeachWithBlockKeepNewLine() {
    final RubyArray<String> ra = ra();
    RubyIO.foreach(BASE_DIR + "ruby_io_read_only_mode.txt",
        item -> ra.add(item), true);
    assertEquals("a\n" + "bc\n" + "def\n", ra.join());
  }

  @Test
  public void testForeachException() {
    assertThrows(RuntimeException.class, () -> {
      RubyIO.foreach("no such file!", null);
    });
  }

  @Test
  public void testEachLine() {
    io = RubyIO.open(BASE_DIR + "ruby_io_read_only_mode.txt");
    assertTrue(io.eachLine() instanceof RubyEnumerator);
    assertEquals("a", io.eachLine().first());
    assertEquals("a", io.eachLine().first());
    assertEquals(ra("a", "bc", "def"), io.eachLine().toA());
  }

  @Test
  public void testEachLineKeepNewLine() {
    io = RubyIO.open(BASE_DIR + "ruby_io_read_only_mode.txt");
    assertTrue(io.eachLine(true) instanceof RubyEnumerator);
    assertEquals("a\n", io.eachLine(true).first());
    assertEquals("a\n", io.eachLine(true).first());
    assertEquals(ra("a\n", "bc\n", "def\n"), io.eachLine(true).toA());
  }

  @Test
  public void testEachLineException() {
    assertThrows(IllegalStateException.class, () -> {
      RubyIO.open(BASE_DIR + "ruby_io_write_only_mode.txt", "w").eachLine();
    });
  }

  @Test
  public void testEachLineKeepNewLineException() {
    assertThrows(IllegalStateException.class, () -> {
      RubyIO.open(BASE_DIR + "ruby_io_write_only_mode.txt", "w").eachLine(true);
    });
  }

  @Test
  public void testToString() throws IOException {
    RubyIO rIO = new RubyIO(new File(BASE_DIR + "ruby_io_read_only_mode.txt"),
        RubyIO.Mode.R);
    if (System.getProperty("os.name").startsWith("Windows")) {
      assertEquals(
          ("RubyIO{path=" + BASE_DIR + "ruby_io_read_only_mode.txt, mode=" + "r"
              + "}").replaceAll("/", "\\\\"),
          rIO.toString());
    } else {
      assertEquals("RubyIO{path=" + BASE_DIR
          + "ruby_io_read_only_mode.txt, mode=" + "r" + "}", rIO.toString());
    }
    rIO.close();
  }

}
