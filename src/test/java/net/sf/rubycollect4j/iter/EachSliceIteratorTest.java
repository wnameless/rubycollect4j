package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class EachSliceIteratorTest {

  private EachSliceIterator<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter = new EachSliceIterator<Integer>(ra(1, 2, 3, 4, 5).iterator(), 2);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof EachSliceIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new EachSliceIterator<Integer>(null, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException2() {
    new EachSliceIterator<Integer>(ra(1, 2, 3, 4, 5).iterator(), 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException3() {
    new EachSliceIterator<Integer>(ra(1, 2, 3, 4, 5).iterator(), -1);
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
    assertEquals(ra(3, 4), iter.next());
    assertEquals(ra(5), iter.next());
    assertFalse(iter.hasNext());
    iter = new EachSliceIterator<Integer>(ra(1, 2, 3, 4, 5).iterator(), 6);
    assertEquals(ra(1, 2, 3, 4, 5), iter.next());
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
