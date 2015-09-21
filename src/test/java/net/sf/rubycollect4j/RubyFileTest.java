/*
 *
 * Copyright 2013-2015 Wei-Ming Wu
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

import static net.sf.rubycollect4j.RubyCollections.qx;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Date;

import org.junit.Test;

public class RubyFileTest {

  static final String BASE_DIR = "src/test/resources/";
  RubyFile rf;

  @Test(expected = RuntimeException.class)
  public void testOpenException() {
    RubyFile.open("no such file!");
  }

  @Test
  public void testAbsolutePath() {
    if (System.getProperty("os.name").startsWith("Windows")) {
      assertEquals(
          (qx("cmd", "/C", "echo %cd%").trim()).replaceAll("/", "\\\\"),
          RubyFile.absolutePath(""));
    } else {
      assertEquals(qx("pwd").trim(), RubyFile.absolutePath(""));
    }
  }

  @Test
  public void testBasename() {
    assertEquals("ruby_file_exist_test.txt",
        RubyFile.basename(BASE_DIR + "ruby_file_exist_test.txt"));
    assertEquals("ruby_file_exist_test",
        RubyFile.basename(BASE_DIR + "ruby_file_exist_test.txt", ".txt"));
    assertEquals("ruby_file_exist_test.txt",
        RubyFile.basename(BASE_DIR + "ruby_file_exist_test.txt", ".ppt"));
  }

  @Test
  public void testChmod() {
    if (System.getProperty("os.name").startsWith("Windows")) return;

    RubyFile.chmod(0000, BASE_DIR + "ruby_file_chmod_test.txt");
    assertFalse(RubyFile.readableʔ(BASE_DIR + "ruby_file_chmod_test.txt"));
    assertFalse(RubyFile.writableʔ(BASE_DIR + "ruby_file_chmod_test.txt"));
    assertFalse(RubyFile.executableʔ(BASE_DIR + "ruby_file_chmod_test.txt"));
    RubyFile.chmod(0111, BASE_DIR + "ruby_file_chmod_test.txt");
    assertFalse(RubyFile.readableʔ(BASE_DIR + "ruby_file_chmod_test.txt"));
    assertFalse(RubyFile.writableʔ(BASE_DIR + "ruby_file_chmod_test.txt"));
    assertTrue(RubyFile.executableʔ(BASE_DIR + "ruby_file_chmod_test.txt"));
    RubyFile.chmod(0222, BASE_DIR + "ruby_file_chmod_test.txt");
    assertFalse(RubyFile.readableʔ(BASE_DIR + "ruby_file_chmod_test.txt"));
    assertTrue(RubyFile.writableʔ(BASE_DIR + "ruby_file_chmod_test.txt"));
    assertFalse(RubyFile.executableʔ(BASE_DIR + "ruby_file_chmod_test.txt"));
    RubyFile.chmod(0444, BASE_DIR + "ruby_file_chmod_test.txt");
    assertTrue(RubyFile.readableʔ(BASE_DIR + "ruby_file_chmod_test.txt"));
    assertFalse(RubyFile.writableʔ(BASE_DIR + "ruby_file_chmod_test.txt"));
    assertFalse(RubyFile.executableʔ(BASE_DIR + "ruby_file_chmod_test.txt"));
    RubyFile.chmod(0644, BASE_DIR + "ruby_file_chmod_test.txt");
  }

  @Test
  public void testDelete() {
    rf = RubyFile.open(BASE_DIR + "ruby_file_delete_test.txt", "w");
    rf.close();
    RubyFile.delete(BASE_DIR + "ruby_file_delete_test.txt");
    assertFalse(RubyFile.existʔ(BASE_DIR + "ruby_file_delete_test.txt"));
  }

  @Test
  public void testDirectoryʔ() {
    assertTrue(RubyFile.directoryʔ(BASE_DIR));
    assertFalse(RubyFile.directoryʔ(BASE_DIR + "ruby_file_exist_test.txt"));
  }

  @Test
  public void testDirname() {
    assertEquals(new File(BASE_DIR).getPath(),
        RubyFile.dirname(BASE_DIR + "ruby_file_exist_test.txt"));
  }

  @Test
  public void testExecutableʔ() {
    if (System.getProperty("os.name").startsWith("Windows")) return;

    RubyFile.chmod(0444, BASE_DIR + "ruby_file_executableQ_test.txt");
    assertFalse(
        RubyFile.executableʔ(BASE_DIR + "ruby_file_executableQ_test.txt"));
    RubyFile.chmod(0111, BASE_DIR + "ruby_file_executableQ_test.txt");
    assertTrue(
        RubyFile.executableʔ(BASE_DIR + "ruby_file_executableQ_test.txt"));
    RubyFile.chmod(0644, BASE_DIR + "ruby_file_executableQ_test.txt");
  }

  @Test
  public void testExistʔ() {
    assertTrue(RubyFile.existʔ(BASE_DIR + "ruby_file_exist_test.txt"));
    assertFalse(RubyFile.existʔ(BASE_DIR + "ruby_file_nonexist_test.txt"));
  }

  @Test
  public void testExistsʔ() {
    assertTrue(RubyFile.existsʔ(BASE_DIR + "ruby_file_exist_test.txt"));
    assertFalse(RubyFile.existsʔ(BASE_DIR + "ruby_file_nonexist_test.txt"));
  }

  @Test
  public void testExpandPath() {
    if (System.getProperty("os.name").startsWith("Windows")) {
      assertEquals(
          (qx("cmd", "/C", "echo %cd%").trim() + "/" + BASE_DIR
              + "ruby_file_exist_test.txt").replaceAll("/", "\\\\"),
          RubyFile.expandPath(BASE_DIR + "ruby_file_exist_test.txt"));
      assertEquals(
          (qx("cmd", "/C", "echo %cd%").trim() + "/" + BASE_DIR
              + "ruby_file_exist_test.txt").replaceAll("/", "\\\\"),
          RubyFile.expandPath("ruby_file_exist_test.txt", BASE_DIR));
    } else {
      assertEquals(
          qx("pwd").trim() + "/" + BASE_DIR + "ruby_file_exist_test.txt",
          RubyFile.expandPath(BASE_DIR + "ruby_file_exist_test.txt"));
      assertEquals(
          qx("pwd").trim() + "/" + BASE_DIR + "ruby_file_exist_test.txt",
          RubyFile.expandPath("ruby_file_exist_test.txt", BASE_DIR));
    }
  }

  @Test
  public void testExtname() {
    assertEquals(".txt",
        RubyFile.extname(BASE_DIR + "ruby_file_exist_test.txt"));
    assertEquals("", RubyFile.extname(BASE_DIR));
  }

  @Test
  public void testFileʔ() {
    assertTrue(RubyFile.fileʔ(BASE_DIR + "ruby_file_exist_test.txt"));
    assertFalse(RubyFile.fileʔ(BASE_DIR));
  }

  @Test
  public void testJoin() {
    assertEquals("", RubyFile.join());
    assertEquals(
        File.separator + "ab" + File.separator + "c" + File.separator + "def"
            + File.separator,
        RubyFile.join(
            File.separator + "ab" + File.separator + "c" + File.separator,
            File.separator + "def" + File.separator));
    assertEquals(
        File.separator + "home" + File.separator + "ruby" + File.separator
            + "collect",
        RubyFile.join(File.separator + "home" + File.separator,
            File.separator + "ruby", "collect"));
  }

  @Test
  public void testReadableʔ() {
    if (System.getProperty("os.name").startsWith("Windows")) return;

    RubyFile.chmod(0222, BASE_DIR + "ruby_file_readableQ_test.txt");
    assertFalse(RubyFile.readableʔ(BASE_DIR + "ruby_file_readableQ_test.txt"));
    RubyFile.chmod(0444, BASE_DIR + "ruby_file_readableQ_test.txt");
    assertTrue(RubyFile.readableʔ(BASE_DIR + "ruby_file_readableQ_test.txt"));
    RubyFile.chmod(0644, BASE_DIR + "ruby_file_readableQ_test.txt");
  }

  @Test
  public void testSize1() {
    RubyFile.size(BASE_DIR + "ruby_file_exist_test.txt");
    assertEquals(0L, RubyFile.size(BASE_DIR + "ruby_file_exist_test.txt"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSizeException() {
    RubyFile.size(BASE_DIR + "no such file!");
  }

  @Test
  public void testSizeʔ() {
    assertNull(RubyFile.sizeʔ(BASE_DIR + "nonexist"));
    assertNull(RubyFile.sizeʔ(BASE_DIR + "ruby_file_exist_test.txt"));
    assertEquals(Long.valueOf(6L),
        RubyFile.sizeʔ(BASE_DIR + "ruby_file_sizeQ_test.txt"));
  }

  @Test
  public void testWritableʔ() {
    if (System.getProperty("os.name").startsWith("Windows")) return;

    RubyFile.chmod(0444, BASE_DIR + "ruby_file_writableQ_test.txt");
    assertFalse(RubyFile.writableʔ(BASE_DIR + "ruby_file_writableQ_test.txt"));
    RubyFile.chmod(0222, BASE_DIR + "ruby_file_writableQ_test.txt");
    assertTrue(RubyFile.writableʔ(BASE_DIR + "ruby_file_writableQ_test.txt"));
    RubyFile.chmod(0644, BASE_DIR + "ruby_file_writableQ_test.txt");
  }

  @Test
  public void testZeroʔ() {
    assertTrue(RubyFile.zeroʔ(BASE_DIR + "ruby_file_exist_test.txt"));
    assertFalse(RubyFile.zeroʔ(BASE_DIR + "nonexist"));
    assertFalse(RubyFile.zeroʔ(BASE_DIR + "ruby_file_sizeQ_test.txt"));
  }

  @Test
  public void testMdate() {
    rf = RubyFile.open(BASE_DIR + "ruby_file_exist_test.txt", "w");
    long modifiedTime = rf.mtime().getTime();
    assertTrue(modifiedTime <= new Date().getTime());
    rf.close();
  }

  @Test
  public void testPath() {
    rf = RubyFile.open(BASE_DIR + "ruby_file_exist_test.txt");
    if (System.getProperty("os.name").startsWith("Windows")) {
      assertEquals(
          (BASE_DIR + "ruby_file_exist_test.txt").replaceAll("/", "\\\\"),
          rf.path());
    } else {
      assertEquals(BASE_DIR + "ruby_file_exist_test.txt", rf.path());
    }
    rf.close();
  }

  @Test
  public void testSize2() {
    rf = RubyFile.open(BASE_DIR + "ruby_file_exist_test.txt");
    assertEquals(0L, rf.size());
    rf.close();
  }

  @Test
  public void testToPath() {
    rf = RubyFile.open(BASE_DIR + "ruby_file_exist_test.txt");
    if (System.getProperty("os.name").startsWith("Windows")) {
      assertEquals(
          (BASE_DIR + "ruby_file_exist_test.txt").replaceAll("/", "\\\\"),
          rf.toPath());
    } else {
      assertEquals(BASE_DIR + "ruby_file_exist_test.txt", rf.toPath());
    }
    rf.close();
  }

  @Test
  public void testToString() {
    rf = RubyFile.open(BASE_DIR + "ruby_file_exist_test.txt", "r");
    if (System.getProperty("os.name").startsWith("Windows")) {
      assertEquals(
          ("RubyFile{path=" + BASE_DIR + "ruby_file_exist_test.txt, mode=" + "r"
              + "}").replaceAll("/", "\\\\"),
          rf.toString());
    } else {
      assertEquals("RubyFile{path=" + BASE_DIR
          + "ruby_file_exist_test.txt, mode=" + "r" + "}", rf.toString());
    }
  }

}
