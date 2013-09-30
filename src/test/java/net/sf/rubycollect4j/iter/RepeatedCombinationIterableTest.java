package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RepeatedCombinationIterableTest {

  private RepeatedCombinationIterable<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter = new RepeatedCombinationIterable<Integer>(ra(1, 2, 3), 2);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof RepeatedCombinationIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException() {
    new RepeatedCombinationIterable<Integer>(null, 2);
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof RepeatedCombinationIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[[1, 1], [1, 2], [1, 3], [2, 2], [2, 3], [3, 3]]",
        iter.toString());
  }

}
