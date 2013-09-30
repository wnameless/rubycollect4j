package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class StepIterableTest {

  private StepIterable<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter = new StepIterable<Integer>(ra(1, 2, 3, 4, 5), 2);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof StepIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new StepIterable<Integer>(null, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException2() {
    new StepIterable<Integer>(ra(1, 2, 3, 4, 5), 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException3() {
    new StepIterable<Integer>(ra(1, 2, 3, 4, 5), -1);
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof StepIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[1, 3, 5]", iter.toString());
  }

}
