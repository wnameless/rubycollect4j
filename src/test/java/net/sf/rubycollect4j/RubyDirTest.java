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

import net.sf.rubycollect4j.block.Block;

import org.junit.Test;

import static net.sf.rubycollect4j.RubyCollections.qx;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RubyDirTest {

  private static final String BASE_DIR = "src/test/resources/";
  private static final String GLOB_DIR = "src/test/resources/glob_test/";

  @Test
  public void testOpen() {
    assertTrue(RubyDir.open(BASE_DIR) instanceof RubyDir);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOpenException1() {
    RubyDir.open(BASE_DIR + "entries_test/b");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOpenException2() {
    RubyDir.open(BASE_DIR + "nonexist");
  }

  @Test
  public void testDelete() {
    assertTrue(RubyDir.mkdir(BASE_DIR + "tempDir"));
    assertTrue(RubyDir.delete(BASE_DIR + "tempDir"));
  }

  @Test
  public void testEntries() {
    assertEquals(ra(".", "..", "a", "b"),
        RubyDir.entries(BASE_DIR + "entries_test"));
  }

  @Test
  public void testExistsʔ() {
    assertTrue(RubyDir.existsʔ(BASE_DIR));
    assertFalse(RubyDir.existsʔ(BASE_DIR + "nonexist"));
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
    assertEquals(ra("folder1", "folder2").sort(), RubyDir.glob(GLOB_DIR + "*")
        .sort());
    assertEquals(ra("folder1", "folder2").sort(), RubyDir.glob(GLOB_DIR + "**")
        .sort());
    assertEquals(ra("src/", "target/").sort(), RubyDir.glob("*/").sort());
    assertEquals(
        ra("folder1/", "folder1/folder1-1/", "folder1/folder1-2/", "folder2/",
            "folder2/folder2-1/").sort(), RubyDir.glob(GLOB_DIR + "**/").sort());
    assertEquals(ra("folder2-1", "file2-1").sort(),
        RubyDir.glob(GLOB_DIR + "folder2/*").sort());
    assertEquals(
        ra("folder1/folder1-1/file1-1-1", "folder1/folder1-2/file1-2-1").sort(),
        RubyDir.glob(GLOB_DIR + "**/*1-*-1*").sort());
    assertEquals(18, RubyDir.glob(GLOB_DIR + "**/*").count());
    assertEquals(ra("file1-2", "file1-3", "folder1-2").sort(),
        RubyDir.glob(GLOB_DIR + "folder1/*[2,3]").sort());
  }

  @Test
  public void testHome() {
    assertEquals(qx("sh", "-c", "echo ~").trim(), RubyDir.home());
  }

  @Test
  public void testMkdir() {
    assertTrue(RubyDir.mkdir(BASE_DIR + "tempDir"));
    assertTrue(RubyDir.delete(BASE_DIR + "tempDir"));
  }

  @Test
  public void testPwd() {
    assertEquals(qx("pwd").trim(), RubyDir.pwd());
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
    RubyDir.open(BASE_DIR + "glob_test/folder2").each(new Block<String>() {

      @Override
      public void yield(String item) {
        entries.push(item);
      }

    });
    assertEquals(ra(".", "..", "folder2-1", "file2-1").sort(), entries);
  }

  @Test
  public void testPath() {
    assertEquals(BASE_DIR, RubyDir.open(BASE_DIR).path() + "/");
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

}