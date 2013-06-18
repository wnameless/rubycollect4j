package net.sf.rubycollect4j;

import java.io.File;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RubyFileTest {

  private static final String BASE_DIR = "src/test/resources/";
  private RubyFile rf;

  @Test
  public void testDelete() {
    rf = RubyFile.open(BASE_DIR + "ruby_file_delete_test.txt", "w");
    rf.close();
    RubyFile.delete(BASE_DIR + "ruby_file_delete_test.txt");
    assertFalse(RubyFile.existʔ(BASE_DIR + "ruby_file_delete_test.txt"));
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
  public void testForeach() {
    assertEquals("abcdef",
        RubyFile.foreach(BASE_DIR + "ruby_io_read_only_mode.txt").toA().join());
    File file = new File(BASE_DIR + "ruby_io_read_only_mode.txt");
    assertEquals("abcdef", RubyFile.foreach(file).toA().join());
  }

}
