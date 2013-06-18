package cleanzephyr.rubycollect4j;

import java.io.File;
import java.util.NoSuchElementException;

import org.junit.Test;

import static cleanzephyr.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;

public class RubyIOTest {

  private static final String BASE_DIR = "src/test/resources/";
  private RubyFile rf;

  @Test
  public void testFactory() {
    rf = RubyFile.open(BASE_DIR + "ruby_io_read_only_mode.txt");
    assertEquals(RubyFile.class, rf.getClass());
    rf.close();
    File file = new File(BASE_DIR + "ruby_io_read_only_mode.txt");
    rf = RubyFile.open(file);
    assertEquals(RubyFile.class, rf.getClass());
    rf.close();
  }

  @Test(expected = NoSuchElementException.class)
  public void testOpenModeWithInvalidString() {
    rf = RubyFile.open(BASE_DIR + "ruby_io_read_only_mode.txt", "haha");
  }

  @Test
  public void testReadOnlyMode() {
    rf = RubyFile.open(BASE_DIR + "ruby_io_read_only_mode.txt");
    assertEquals(ra("a", "bc", "def"), rf.eachLine().toA());
    rf = RubyFile.open(BASE_DIR + "ruby_io_read_only_mode.txt", "r");
    assertEquals(ra("a", "bc", "def"), rf.eachLine().toA());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testReadOnlyModeException1() {
    RubyFile rf = RubyFile.open(BASE_DIR + "ruby_io_read_only_mode.txt");
    rf.puts("test");
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testReadOnlyModeException2() {
    RubyFile rf = RubyFile.open(BASE_DIR + "ruby_io_read_only_mode.txt");
    rf.write("test");
  }

  @Test
  public void testReadWriteMode() {
    rf = RubyFile.open(BASE_DIR + "ruby_io_read_write_mode.txt", "w");
    rf.close();
    rf = RubyFile.open(BASE_DIR + "ruby_io_read_write_mode.txt", "r+");
    rf.puts("1");
    rf.puts("2");
    rf.puts("3");
    rf.close();
    rf = RubyFile.open(BASE_DIR + "ruby_io_read_write_mode.txt", "r+");
    assertEquals(ra("1", "2", "3"), rf.eachLine().toA());
    rf.close();
    rf = RubyFile.open(BASE_DIR + "ruby_io_read_write_mode.txt", "r+");
    rf.puts("4");
    rf.close();
    rf = RubyFile.open(BASE_DIR + "ruby_io_read_write_mode.txt", "r+");
    assertEquals(ra("4", "2", "3"), rf.eachLine().toA());
    rf.close();
  }

  @Test
  public void testWriteOnlyMode() {
    rf = RubyFile.open(BASE_DIR + "ruby_io_write_only_mode.txt", "w");
    rf.puts("1");
    rf.puts("2");
    rf.puts("3");
    rf.close();
    rf = RubyFile.open(BASE_DIR + "ruby_io_write_only_mode.txt", "r");
    assertEquals(ra("1", "2", "3"), rf.eachLine().toA());
    rf.close();
    rf = RubyFile.open(BASE_DIR + "ruby_io_write_only_mode.txt", "w");
    rf.close();
    rf = RubyFile.open(BASE_DIR + "ruby_io_write_only_mode.txt", "r");
    assertEquals(ra(), rf.eachLine().toA());
    rf.close();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testWriteOnlyModeException() {
    rf = RubyFile.open(BASE_DIR + "ruby_io_write_only_mode.txt", "w");
    rf.read();
  }

  @Test
  public void testWriteReadMode() {
    rf = RubyFile.open(BASE_DIR + "ruby_io_write_read_mode.txt", "w+");
    rf.write("123");
    rf.write("456");
    rf.seek(0);
    assertEquals("123456\n", rf.read());
    rf.close();
    rf = RubyFile.open(BASE_DIR + "ruby_io_write_read_mode.txt", "r");
    assertEquals("123456\n", rf.read());
    rf.close();
  }

  @Test
  public void testAppendOnlyMode() {
    rf = RubyFile.open(BASE_DIR + "ruby_io_append_only_mode.txt", "w");
    rf.close();
    rf = RubyFile.open(BASE_DIR + "ruby_io_append_only_mode.txt", "a");
    rf.write("123");
    rf.close();
    rf = RubyFile.open(BASE_DIR + "ruby_io_append_only_mode.txt", "a");
    rf.write("456");
    rf.close();
    rf = RubyFile.open(BASE_DIR + "ruby_io_append_only_mode.txt", "r");
    assertEquals("123456\n", rf.read());
    rf.close();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAppendOnlyModeException() {
    rf = RubyFile.open(BASE_DIR + "ruby_io_append_only_mode.txt", "a");
    rf.read();
  }

  @Test
  public void testAppendReadMode() {
    rf = RubyFile.open(BASE_DIR + "ruby_io_append_read_mode.txt", "w");
    rf.close();
    rf = RubyFile.open(BASE_DIR + "ruby_io_append_read_mode.txt", "a+");
    rf.write("123");
    rf.close();
    rf = RubyFile.open(BASE_DIR + "ruby_io_append_read_mode.txt", "a+");
    rf.write("456");
    rf.seek(0);
    assertEquals("123456\n", rf.read());
    rf.close();
  }

  @Test
  public void testForeach() {
    assertEquals("abcdef",
        RubyFile.foreach(BASE_DIR + "ruby_io_read_only_mode.txt").toA().join());
    File file = new File(BASE_DIR + "ruby_io_read_only_mode.txt");
    assertEquals("abcdef", RubyFile.foreach(file).toA().join());
  }
}
