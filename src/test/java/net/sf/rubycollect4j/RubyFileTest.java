package net.sf.rubycollect4j;

import java.io.File;
import java.util.Date;

import org.junit.Test;

import static net.sf.rubycollect4j.RubyCollections.qx;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RubyFileTest {

  private static final String BASE_DIR = "src/test/resources/";
  private RubyFile rf;

  @Test(expected = RuntimeException.class)
  public void testOpenException() {
    RubyFile.open("no such file!");
  }

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
    if (System.getProperty("os.name").startsWith("Windows"))
      return;

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
    if (System.getProperty("os.name").startsWith("Windows"))
      return;

    RubyFile.chmod(0444, BASE_DIR + "ruby_file_executableQ_test.txt");
    assertFalse(RubyFile.executableʔ(BASE_DIR
        + "ruby_file_executableQ_test.txt"));
    RubyFile.chmod(0111, BASE_DIR + "ruby_file_executableQ_test.txt");
    assertTrue(RubyFile
        .executableʔ(BASE_DIR + "ruby_file_executableQ_test.txt"));
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
    assertEquals(
        qx("pwd").trim() + "/" + BASE_DIR + "ruby_file_exist_test.txt",
        RubyFile.expandPath(BASE_DIR + "ruby_file_exist_test.txt"));
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
  public void testReadableʔ() {
    if (System.getProperty("os.name").startsWith("Windows"))
      return;

    RubyFile.chmod(0222, BASE_DIR + "ruby_file_readableQ_test.txt");
    assertFalse(RubyFile.readableʔ(BASE_DIR + "ruby_file_readableQ_test.txt"));
    RubyFile.chmod(0444, BASE_DIR + "ruby_file_readableQ_test.txt");
    assertTrue(RubyFile.readableʔ(BASE_DIR + "ruby_file_readableQ_test.txt"));
    RubyFile.chmod(0644, BASE_DIR + "ruby_file_readableQ_test.txt");
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
    if (System.getProperty("os.name").startsWith("Windows"))
      return;

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
    assertEquals(BASE_DIR + "ruby_file_exist_test.txt", rf.path());
    rf.close();
  }

  @Test
  public void testSize() {
    rf = RubyFile.open(BASE_DIR + "ruby_file_exist_test.txt");
    assertEquals(0L, rf.size());
    rf.close();
  }

  @Test
  public void testToPath() {
    rf = RubyFile.open(BASE_DIR + "ruby_file_exist_test.txt");
    assertEquals(BASE_DIR + "ruby_file_exist_test.txt", rf.toPath());
    rf.close();
  }

  @Test
  public void testToString() {
    rf = RubyFile.open(BASE_DIR + "ruby_file_exist_test.txt", "r");
    assertEquals("RubyFile{path=" + BASE_DIR
        + "ruby_file_exist_test.txt, mode=" + "r" + "}", rf.toString());
    rf.close();
  }

}
