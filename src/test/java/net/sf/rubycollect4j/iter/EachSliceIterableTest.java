package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class EachSliceIterableTest {

  private EachSliceIterable<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter = new EachSliceIterable<Integer>(ra(1, 2, 3, 4, 5), 2);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof EachSliceIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new EachSliceIterable<Integer>(null, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException2() {
    new EachSliceIterable<Integer>(ra(1, 2, 3, 4, 5), 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException3() {
    new EachSliceIterable<Integer>(ra(1, 2, 3, 4, 5), -1);
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof EachSliceIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[[1, 2], [3, 4], [5]]", iter.toString());
  }

}
