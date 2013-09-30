package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class CycleIteratorTest {

  private CycleIterator<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter = new CycleIterator<Integer>(ra(1, 2, 3), 2);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof CycleIterator);
    iter = new CycleIterator<Integer>(ra(1, 2, 3));
    assertTrue(iter instanceof CycleIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    iter = new CycleIterator<Integer>(null);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    iter = new CycleIterator<Integer>(null, 2);
  }

  @Test
  public void testHasNext() {
    assertTrue(iter.hasNext());
    while (iter.hasNext()) {
      iter.next();
    }
    assertFalse(iter.hasNext());
    iter = new CycleIterator<Integer>(new ArrayList<Integer>());
    assertFalse(iter.hasNext());
    iter = new CycleIterator<Integer>(new ArrayList<Integer>(), 2);
    assertFalse(iter.hasNext());
    iter = new CycleIterator<Integer>(ra(1, 2, 3), 0);
    assertFalse(iter.hasNext());
    iter = new CycleIterator<Integer>(ra(1, 2, 3), -1);
    assertFalse(iter.hasNext());
    iter = new CycleIterator<Integer>(ra(1, 2, 3));
    for (int i = 0; i <= 10000; i++) {
      iter.next();
      if (i == 10000)
        return;
    }
    fail();
  }

  @Test
  public void testNext() {
    assertEquals(Integer.valueOf(1), iter.next());
    assertEquals(Integer.valueOf(2), iter.next());
    assertEquals(Integer.valueOf(3), iter.next());
    assertTrue(iter.hasNext());
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