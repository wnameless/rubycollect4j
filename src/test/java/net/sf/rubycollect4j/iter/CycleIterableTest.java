package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CycleIterableTest {

  private CycleIterable<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter = new CycleIterable<Integer>(ra(1, 2, 3));
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof CycleIterable);
    iter = new CycleIterable<Integer>(ra(1, 2, 3), 2);
    assertTrue(iter instanceof CycleIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    iter = new CycleIterable<Integer>(null);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    iter = new CycleIterable<Integer>(null, 2);
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof CycleIterator);
    iter = new CycleIterable<Integer>(ra(1, 2, 3), 2);
    assertTrue(iter.iterator() instanceof CycleIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[1, 2, 3...]", iter.toString());
    iter = new CycleIterable<Integer>(ra(1, 2, 3), 2);
    assertEquals("[1, 2, 3, 1, 2, 3]", iter.toString());
  }

}
