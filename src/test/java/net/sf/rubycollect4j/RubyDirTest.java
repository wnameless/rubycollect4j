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
import static net.sf.rubycollect4j.RubyLiterals.qx;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

public class RubyDirTest {

  static final String BASE_DIR = "src/test/resources/";
  static final String GLOB_DIR = "src/test/resources/glob_test/";

  @Test
  public void testOpen() {
    assertTrue(RubyDir.open(BASE_DIR) instanceof RubyDir);
  }

  @Test
  public void testOpenException1() {
    assertThrows(IllegalArgumentException.class, () -> {
      RubyDir.open(BASE_DIR + "entries_test/b");
    });
  }

  @Test
  public void testOpenException2() {
    assertThrows(IllegalArgumentException.class, () -> {
      RubyDir.open(BASE_DIR + "nonexist");
    });
  }

  @Test
  public void testDelete() {
    assertTrue(RubyDir.mkdir(BASE_DIR + "tempDir"));
    assertTrue(RubyDir.delete(BASE_DIR + "tempDir"));
  }

  @Test
  public void testEmptyʔ() throws IOException {
    assertFalse(RubyDir.emptyʔ(BASE_DIR + "empty_dir"));
    new File(BASE_DIR + "empty_dir/.keep").delete();
    assertTrue(RubyDir.emptyʔ(BASE_DIR + "empty_dir"));
    new File(BASE_DIR + "empty_dir/.keep").createNewFile();
    assertFalse(RubyDir.emptyʔ(BASE_DIR + "empty_dir"));
    assertFalse(RubyDir.emptyʔ(BASE_DIR + "ruby_file_sizeQ_test.txt"));
  }

  @Test
  public void testEntries() {
    assertEquals(ra(".", "..", "a", "b"),
        RubyDir.entries(BASE_DIR + "entries_test"));
  }

  @Test
  public void testExistʔ() {
    assertTrue(RubyDir.existʔ(BASE_DIR));
    assertFalse(RubyDir.existʔ(BASE_DIR + "entries_test/b"));
  }

  @Test
  public void testForeach() {
    assertEquals(RubyEnumerator.class,
        RubyDir.foreach(BASE_DIR + "entries_test").getClass());
    assertEquals(ra(".", "..", "a", "b"),
        RubyDir.foreach(BASE_DIR + "entries_test").toA());
  }

  @Test
  public void testGlob() {
    assertEquals(ra(), RubyDir.glob(""));
    assertEquals(
        ra("folder2-1", "file2-1", "folder2-1" + File.separator + "file2-1-1",
            "folder2-1" + File.separator + "file2-1-2",
            "folder2-1" + File.separator + "file2-1-3").sort(),
        RubyDir.glob(GLOB_DIR + "folder2/**/*").sort());
    assertEquals(ra("folder1", "folder2", "rbc4j").sort(),
        RubyDir.glob(GLOB_DIR + "*").sort());
    assertEquals(ra("folder1", "folder2", "rbc4j").sort(),
        RubyDir.glob(GLOB_DIR + "**").sort());
    assertEquals(
        ra("folder1" + File.separator, "folder2" + File.separator).sort(),
        RubyDir.glob(GLOB_DIR + "*/").sort());
    assertEquals(
        ra("folder1" + File.separator,
            "folder1" + File.separator + "folder1-1" + File.separator,
            "folder1" + File.separator + "folder1-2" + File.separator,
            "folder2" + File.separator,
            "folder2" + File.separator + "folder2-1" + File.separator).sort(),
        RubyDir.glob(GLOB_DIR + "**/").sort());
    assertEquals(ra("file1-1", "file1-2", "file1-3", "file1-4", "folder1-1",
        "folder1-2"), RubyDir.glob(GLOB_DIR + "folder1/*").sort());
    assertEquals(ra(
        "folder1" + File.separator + "folder1-1" + File.separator + "file1-1-1",
        "folder1" + File.separator + "folder1-2" + File.separator + "file1-2-1")
            .sort(),
        RubyDir.glob(GLOB_DIR + "**/*1-*-1*").sort());
    assertEquals(19, RubyDir.glob(GLOB_DIR + "**/*").count());
    assertEquals(ra("file1-2", "file1-3", "folder1-2").sort(),
        RubyDir.glob(GLOB_DIR + "folder1/*[2,3]").sort());
    assertEquals(ra(GLOB_DIR + "rbc4j"),
        RubyDir.glob("**/*c4j").select(item -> item.startsWith("src")));
    assertEquals(ra(
        "folder1" + File.separator + "folder1-1" + File.separator + "file1-1-1",
        "folder1" + File.separator + "folder1-2" + File.separator + "file1-2-1")
            .sort(),
        RubyDir.glob(GLOB_DIR + "**/{file1-?-1,file1-2-1}").sort());
  }

  @Test
  public void testHome() {
    if (System.getProperty("os.name").startsWith("Windows")) {
      assertEquals(System.getProperty("user.home"), RubyDir.home());
    } else {
      assertEquals(qx("sh", "-c", "echo ~").trim(), RubyDir.home());
    }
  }

  @Test
  public void testMkdir() {
    assertTrue(RubyDir.mkdir(BASE_DIR + "tempDir"));
    assertTrue(RubyDir.delete(BASE_DIR + "tempDir"));
  }

  @Test
  public void testPwd() {
    if (System.getProperty("os.name").startsWith("Windows")) {
      assertEquals(System.getProperty("user.dir"), RubyDir.pwd());
    } else {
      assertEquals(qx("pwd").trim(), RubyDir.pwd());
    }
  }

  @Test
  public void testEach() {
    assertEquals(RubyEnumerator.class,
        RubyDir.open(BASE_DIR + "glob_test/folder2").each().getClass());
    assertEquals(ra(".", "..", "folder2-1", "file2-1").sort(),
        RubyDir.open(BASE_DIR + "glob_test/folder2").each().toA().sort());
  }

  @Test
  public void testEachWithBlock() {
    final RubyArray<String> entries = ra();
    RubyDir.open(BASE_DIR + "glob_test/folder2")
        .each(item -> entries.push(item));
    assertEquals(ra(".", "..", "folder2-1", "file2-1").sort(), entries);
  }

  @Test
  public void testPath() {
    assertEquals(normalizePath(BASE_DIR),
        RubyDir.open(BASE_DIR).path() + File.separator);
  }

  @Test
  public void testPos() {
    RubyDir dir = RubyDir.open(BASE_DIR + "glob_test/folder2");
    assertEquals(0, dir.pos());
    dir.read();
    assertEquals(1, dir.pos());
    dir.read();
    assertEquals(2, dir.pos());
    dir.read();
    assertEquals(3, dir.pos());
    dir.read();
    assertEquals(4, dir.pos());
    dir.read();
    assertEquals(4, dir.pos());
    dir.pos(-1);
    assertEquals(-1, dir.pos());
    dir.read();
    assertEquals(-1, dir.pos());
    dir.pos(0);
    assertEquals(0, dir.pos());
    dir.read();
    assertEquals(1, dir.pos());
  }

  @Test
  public void testRead() {
    RubyDir dir = RubyDir.open(BASE_DIR + "glob_test/folder2");
    assertEquals(".", dir.read());
    assertEquals("..", dir.read());
    assertEquals("file2-1", dir.read());
    assertEquals("folder2-1", dir.read());
    assertNull(dir.read());
  }

  @Test
  public void testRewind() {
    RubyDir dir = RubyDir.open(BASE_DIR + "glob_test/folder2");
    assertEquals(0, dir.pos());
    dir.read();
    assertEquals(1, dir.pos());
    dir.rewind();
    assertEquals(0, dir.pos());
  }

  @Test
  public void testSeek() {
    RubyDir dir = RubyDir.open(BASE_DIR + "glob_test/folder2");
    assertTrue(dir.seek(1) instanceof RubyDir);
    assertEquals("..", dir.seek(1).read());
    assertNull(dir.seek(-1).read());
    assertEquals(".", dir.read());
  }

  @Test
  public void testTell() {
    RubyDir dir = RubyDir.open(BASE_DIR + "glob_test/folder2");
    assertEquals(0, dir.tell());
    dir.read();
    assertEquals(1, dir.tell());
    dir.read();
    assertEquals(2, dir.tell());
    dir.read();
    assertEquals(3, dir.tell());
    dir.read();
    assertEquals(4, dir.tell());
    dir.read();
    assertEquals(4, dir.tell());
  }

  @Test
  public void testToString() {
    assertEquals(
        normalizePath("RubyDir{path="
            + BASE_DIR.substring(0, BASE_DIR.length() - 1) + "}"),
        RubyDir.open(BASE_DIR).toString());
  }

  private String normalizePath(String path) {
    String os = System.getProperty("os.name");
    if (os.startsWith("Windows")) {
      return path.replaceAll("/", "\\\\");
    } else {
      return path;
    }
  }

}
