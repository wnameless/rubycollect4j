package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class StepIteratorTest {

  private StepIterator<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter = new StepIterator<Integer>(ra(1, 2, 3, 4, 5).iterator(), 2);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof StepIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    iter = new StepIterator<Integer>(null, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException2() {
    iter = new StepIterator<Integer>(ra(1, 2, 3, 4, 5).iterator(), 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException3() {
    iter = new StepIterator<Integer>(ra(1, 2, 3, 4, 5).iterator(), -1);
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
    assertEquals(Integer.valueOf(1), iter.next());
    assertEquals(Integer.valueOf(3), iter.next());
    assertEquals(Integer.valueOf(5), iter.next());
    assertFalse(iter.hasNext());
    iter = new StepIterator<Integer>(new ArrayList<Integer>().iterator(), 2);
    assertFalse(iter.hasNext());
    iter = new StepIterator<Integer>(ra(1, 2, 3, 4, 5).iterator(), 6);
    assertEquals(Integer.valueOf(1), iter.next());
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
