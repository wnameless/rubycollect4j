package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class EachIndexIterableTest {

  private EachIndexIterable<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter = new EachIndexIterable<Integer>(ra(1, 2, 3));
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof EachIndexIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException() {
    new EachIndexIterable<Integer>(null);
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof EachIndexIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[0, 1, 2]", iter.toString());
  }

}
