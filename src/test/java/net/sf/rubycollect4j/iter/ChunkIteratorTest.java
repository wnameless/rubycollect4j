package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.hp;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.block.TransformBlock;

import org.junit.Before;
import org.junit.Test;

public class ChunkIteratorTest {

  private ChunkIterator<Number, String> iter;

  @Before
  public void setUp() throws Exception {
    RubyArray<Number> nums =
        ra((Number) 1, (Number) 1.0, (Number) 1.0f, (Number) 2, (Number) 2L);
    iter =
        new ChunkIterator<Number, String>(nums.iterator(),
            new TransformBlock<Number, String>() {

              @Override
              public String yield(Number item) {
                return item.toString();
              }

            });
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof ChunkIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    iter =
        new ChunkIterator<Number, String>(null,
            new TransformBlock<Number, String>() {

              @Override
              public String yield(Number item) {
                return item.toString();
              }

            });
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    iter =
        new ChunkIterator<Number, String>(ra((Number) 1, (Number) 1.0,
            (Number) 1.0f, (Number) 2, (Number) 2L).iterator(), null);
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
    assertEquals(hp("1", ra(1)), iter.next());
    assertEquals(hp("1.0", ra((Number) 1.0, (Number) 1.0f)), iter.next());
    assertEquals(hp("2", ra((Number) 2, (Number) 2L)), iter.next());
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
