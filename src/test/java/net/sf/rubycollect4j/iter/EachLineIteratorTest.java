package net.sf.rubycollect4j.iter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class EachLineIteratorTest {

  private static final String BASE_DIR = "src/test/resources/";
  private EachLineIterator iter;

  @Before
  public void setUp() throws Exception {
    iter =
        new EachLineIterator(new RandomAccessFile(new File(BASE_DIR
            + "ruby_io_read_only_mode.txt"), "r"));
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof EachLineIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new EachLineIterator(null);
  }

  @Test
  public void testHasNext() {
    assertTrue(iter.hasNext());
    while (iter.hasNext()) {
      iter.next();
    }
    assertFalse(iter.hasNext());
  }

  @Test
  public void testNext() {
    assertEquals("a", iter.next());
    assertEquals("bc", iter.next());
    assertEquals("def", iter.next());
    assertFalse(iter.hasNext());
  }

  @Test(expected = NoSuchElementException.class)
  public void testNextException() {
    while (iter.hasNext()) {
      iter.next();
    }
    iter.next();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemove() {
    iter.remove();
  }

}
