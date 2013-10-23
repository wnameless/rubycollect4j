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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import net.sf.rubycollect4j.RubyIO.Mode;
import net.sf.rubycollect4j.block.Block;

import org.junit.Test;

public class RubyIOTest {

  private static final String BASE_DIR = "src/test/resources/";
  private RubyIO io;

  @Test
  public void testConstructor() throws Exception {
    io = new RubyIO(new File(BASE_DIR + "ruby_io_read_only_mode.txt"), Mode.R);
    assertTrue(io instanceof RubyIO);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException() throws Exception {
    new RubyIO(null, Mode.R);
  }

  @Test
  public void testOpen() {
    io = RubyIO.open(BASE_DIR + "ruby_io_read_only_mode.txt");
    assertEquals(RubyIO.class, io.getClass());
    io.close();
  }

  @Test(expected = RuntimeException.class)
  public void testOpenException() {
    RubyIO.open(BASE_DIR + "no_file.txt");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOpenModeWithInvalidString() {
    RubyIO.open(BASE_DIR + "ruby_io_read_only_mode.txt", "haha");
  }

  @Test
  public void testReadOnlyMode() {
    io = RubyIO.open(BASE_DIR + "ruby_io_read_only_mode.txt");
    assertEquals(ra("a", "bc", "def"), io.eachLine().toA());
    io = RubyIO.open(BASE_DIR + "ruby_io_read_only_mode.txt", "r");
    assertEquals(ra("a", "bc", "def"), io.eachLine().toA());
  }

  @Test(expected = IllegalStateException.class)
  public void testReadOnlyModeException1() {
    RubyIO io = RubyIO.open(BASE_DIR + "ruby_io_read_only_mode.txt");
    io.puts("test");
  }

  @Test(expected = IllegalStateException.class)
  public void testReadOnlyModeException2() {
    RubyIO io = RubyIO.open(BASE_DIR + "ruby_io_read_only_mode.txt");
    io.write("test");
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

  @Test(expected = IllegalStateException.class)
  public void testWriteOnlyModeException() {
    io = RubyIO.open(BASE_DIR + "ruby_io_write_only_mode.txt", "w");
    io.read();
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

  @Test(expected = IllegalStateException.class)
  public void testAppendOnlyModeException() {
    io = RubyIO.open(BASE_DIR + "ruby_io_append_only_mode.txt", "a");
    io.read();
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
    assertTrue(RubyFile.foreach(file) instanceof RubyEnumerator);
    assertEquals(ra("a", "bc", "def"), RubyFile.foreach(file).toA());
  }

  @Test
  public void testForeachWithBlock() {
    final RubyArray<String> ra = ra();
    RubyIO.foreach(BASE_DIR + "ruby_io_read_only_mode.txt",
        new Block<String>() {

          @Override
          public void yield(String item) {
            ra.add(item);
          }

        });
    assertEquals("a" + "bc" + "def", ra.join());
  }

  @Test(expected = RuntimeException.class)
  public void testForeachException() {
    RubyIO.foreach("no such file!", null);
  }

  @Test
  public void testEachLine() {
    io = RubyIO.open(BASE_DIR + "ruby_io_read_only_mode.txt");
    assertTrue(io.eachLine() instanceof RubyEnumerator);
    assertEquals("a", io.eachLine().first());
    assertEquals("a", io.eachLine().first());
    assertEquals(ra("a", "bc", "def"), io.eachLine().toA());
  }

  @Test(expected = IllegalStateException.class)
  public void testEachLineException() {
    io = RubyIO.open(BASE_DIR + "ruby_io_write_only_mode.txt", "w");
    io.eachLine();
  }

  @Test
  public void testToString() throws IOException {
    RubyIO rIO =
        new RubyIO(new File(BASE_DIR + "ruby_io_read_only_mode.txt"),
            RubyIO.Mode.R);
    if (System.getProperty("os.name").startsWith("Windows")) {
      assertEquals(
          ("RubyIO{path=" + BASE_DIR + "ruby_io_read_only_mode.txt, mode="
              + "r" + "}").replaceAll("/", "\\\\"), rIO.toString());
    } else {
      assertEquals("RubyIO{path=" + BASE_DIR
          + "ruby_io_read_only_mode.txt, mode=" + "r" + "}", rIO.toString());
    }
    rIO.close();
  }

}
