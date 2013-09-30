package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class RepeatedPermutationIteratorTest {

  private RepeatedPermutationIterator<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter = new RepeatedPermutationIterator<Integer>(ra(1, 2, 3), 2);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof RepeatedPermutationIterator);
  }

  @Test
  public void testConstructorWithN0() {
    iter = new RepeatedPermutationIterator<Integer>(ra(1, 2, 3), 0);
    assertEquals(ra(), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test
  public void testConstructorWithNegtiveN() {
    iter = new RepeatedPermutationIterator<Integer>(ra(1, 2, 3), -1);
    assertFalse(iter.hasNext());
  }

  @Test
  public void testConstructorWithExceededN() {
    iter = new RepeatedPermutationIterator<Integer>(ra(1, 2), 3);
    assertEquals(ra(1, 1, 1), iter.next());
    assertEquals(ra(1, 1, 2), iter.next());
    assertEquals(ra(1, 2, 1), iter.next());
    assertEquals(ra(1, 2, 2), iter.next());
    assertEquals(ra(2, 1, 1), iter.next());
    assertEquals(ra(2, 1, 2), iter.next());
    assertEquals(ra(2, 2, 1), iter.next());
    assertEquals(ra(2, 2, 2), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test
  public void testConstructorWithExactN() {
    iter = new RepeatedPermutationIterator<Integer>(ra(1, 2), 2);
    assertEquals(ra(1, 1), iter.next());
    assertEquals(ra(1, 2), iter.next());
    assertEquals(ra(2, 1), iter.next());
    assertEquals(ra(2, 2), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException() {
    new RepeatedPermutationIterator<Integer>(null, 2);
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
    assertEquals(ra(1, 1), iter.next());
    assertEquals(ra(1, 2), iter.next());
    assertEquals(ra(1, 3), iter.next());
    assertEquals(ra(2, 1), iter.next());
    assertEquals(ra(2, 2), iter.next());
    assertEquals(ra(2, 3), iter.next());
    assertEquals(ra(3, 1), iter.next());
    assertEquals(ra(3, 2), iter.next());
    assertEquals(ra(3, 3), iter.next());
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
