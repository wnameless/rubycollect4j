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

import org.junit.Test;

import static net.sf.rubycollect4j.RubyCollections.qx;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RubyDirTest {

  private static final String BASE_DIR = "src/test/resources/";
  private static final String GLOB_DIR = "src/test/resources/glob_test/";

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
    assertEquals(ra("pom.xml", "README.md", "src", "target").sort(), RubyDir
        .glob("*").sort());
    assertEquals(ra("pom.xml", "README.md", "src", "target").sort(), RubyDir
        .glob("**").sort());
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

}
