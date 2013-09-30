package net.sf.rubycollect4j.iter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.RandomAccessFile;

import org.junit.Before;
import org.junit.Test;

public class EachLineIterableTest {

  private static final String BASE_DIR = "src/test/resources/";
  private EachLineIterable iter;

  @Before
  public void setUp() throws Exception {
    iter =
        new EachLineIterable(new RandomAccessFile(new File(BASE_DIR
            + "ruby_io_read_only_mode.txt"), "r"));
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof EachLineIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException() {
    new EachLineIterable(null);
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof EachLineIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[a, bc, def]", iter.toString());
  }

}
