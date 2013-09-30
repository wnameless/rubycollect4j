package net.sf.rubycollect4j.iter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import net.sf.rubycollect4j.succ.IntegerSuccessor;

import org.junit.Before;
import org.junit.Test;

public class RangeIteratorTest {

  private RangeIterator<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter = new RangeIterator<Integer>(IntegerSuccessor.getInstance(), 1, 3);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof RangeIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new RangeIterator<Integer>(null, 1, 3);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new RangeIterator<Integer>(IntegerSuccessor.getInstance(), null, 3);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException3() {
    new RangeIterator<Integer>(IntegerSuccessor.getInstance(), 1, null);
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
    assertEquals(Integer.valueOf(2), iter.next());
    assertEquals(Integer.valueOf(3), iter.next());
    assertFalse(iter.hasNext());
    iter = new RangeIterator<Integer>(IntegerSuccessor.getInstance(), 1, 0);
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
