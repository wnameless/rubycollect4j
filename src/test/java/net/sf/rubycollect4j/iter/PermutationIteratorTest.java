package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class PermutationIteratorTest {

  private PermutationIterator<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter = new PermutationIterator<Integer>(ra(1, 2, 3), 2);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof PermutationIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException() {
    new PermutationIterator<Integer>(null, 2);
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
    assertEquals(ra(1, 3), iter.next());
    assertEquals(ra(2, 1), iter.next());
    assertEquals(ra(2, 3), iter.next());
    assertEquals(ra(3, 1), iter.next());
    assertEquals(ra(3, 2), iter.next());
    assertFalse(iter.hasNext());
    iter = new PermutationIterator<Integer>(ra(1, 2, 3), 0);
    assertEquals(ra(), iter.next());
    assertFalse(iter.hasNext());
    iter = new PermutationIterator<Integer>(new ArrayList<Integer>(), 2);
    assertFalse(iter.hasNext());
    iter = new PermutationIterator<Integer>(ra(1, 2, 3), 4);
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
