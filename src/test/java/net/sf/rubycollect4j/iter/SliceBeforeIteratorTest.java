package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.qr;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import net.sf.rubycollect4j.block.BooleanBlock;

import org.junit.Before;
import org.junit.Test;

public class SliceBeforeIteratorTest {

  private SliceBeforeIterator<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter =
        new SliceBeforeIterator<Integer>(ra(1, 2, 3, 4, 5).iterator(),
            new BooleanBlock<Integer>() {

              @Override
              public boolean yield(Integer item) {
                return item % 3 == 0;
              }

            });
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof SliceBeforeIterator);
    iter =
        new SliceBeforeIterator<Integer>(ra(1, 2, 3, 4, 5).iterator(), qr("3"));
    assertTrue(iter instanceof SliceBeforeIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new SliceBeforeIterator<Integer>(null, new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item % 2 == 0;
      }

    });
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    BooleanBlock<Integer> block = null;
    new SliceBeforeIterator<Integer>(ra(1, 2, 3, 4, 5).iterator(), block);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException3() {
    new SliceBeforeIterator<Integer>(null, qr("3"));
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException4() {
    Pattern pattern = null;
    new SliceBeforeIterator<Integer>(ra(1, 2, 3, 4, 5).iterator(), pattern);
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
    assertEquals(ra(1, 2), iter.next());
    assertEquals(ra(3, 4, 5), iter.next());
    assertFalse(iter.hasNext());
    iter =
        new SliceBeforeIterator<Integer>(ra(1, 2, 3, 4, 5).iterator(), qr("3"));
    assertEquals(ra(1, 2), iter.next());
    assertEquals(ra(3, 4, 5), iter.next());
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
