package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class EachIndexIteratorTest {

  private EachIndexIterator<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter = new EachIndexIterator<Integer>(ra(1, 2, 3).iterator());
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof EachIndexIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException() {
    new EachIndexIterator<Integer>(null);
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
    assertEquals(Integer.valueOf(0), iter.next());
    assertEquals(Integer.valueOf(1), iter.next());
    assertEquals(Integer.valueOf(2), iter.next());
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
