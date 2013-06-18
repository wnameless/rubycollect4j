package net.sf.rubycollect4j;

import java.io.File;

import org.junit.Test;

import static net.sf.rubycollect4j.RubyCollections.qx;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RubyFileTest {

  private static final String BASE_DIR = "src/test/resources/";
  private RubyFile rf;

  @Test
  public void testAbsolutePath() {
    assertEquals(qx("pwd").trim(), RubyFile.absolutePath(""));
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
    RubyFile.chmod(0444, BASE_DIR + "ruby_file_executable?_test.txt");
    assertFalse(RubyFile.executableʔ(BASE_DIR
        + "ruby_file_executable?_test.txt"));
    RubyFile.chmod(0111, BASE_DIR + "ruby_file_executable?_test.txt");
    assertTrue(RubyFile
        .executableʔ(BASE_DIR + "ruby_file_executable?_test.txt"));
    RubyFile.chmod(0644, BASE_DIR + "ruby_file_executable?_test.txt");
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
  public void testFileʔ() {
    assertTrue(RubyFile.fileʔ(BASE_DIR + "ruby_file_exist_test.txt"));
    assertFalse(RubyFile.fileʔ(BASE_DIR));
  }

  @Test
  public void testReadableʔ() {
    RubyFile.chmod(0222, BASE_DIR + "ruby_file_readable?_test.txt");
    assertFalse(RubyFile.readableʔ(BASE_DIR + "ruby_file_readable?_test.txt"));
    RubyFile.chmod(0444, BASE_DIR + "ruby_file_readable?_test.txt");
    assertTrue(RubyFile.readableʔ(BASE_DIR + "ruby_file_readable?_test.txt"));
    RubyFile.chmod(0644, BASE_DIR + "ruby_file_readable?_test.txt");
  }

  @Test
  public void testWritableʔ() {
    RubyFile.chmod(0444, BASE_DIR + "ruby_file_writable?_test.txt");
    assertFalse(RubyFile.writableʔ(BASE_DIR + "ruby_file_writable?_test.txt"));
    RubyFile.chmod(0222, BASE_DIR + "ruby_file_writable?_test.txt");
    assertTrue(RubyFile.writableʔ(BASE_DIR + "ruby_file_writable?_test.txt"));
    RubyFile.chmod(0644, BASE_DIR + "ruby_file_writable?_test.txt");
  }

}
