package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class EachWithIndexIterableTest {

  private EachWithIndexIterable<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter = new EachWithIndexIterable<Integer>(ra(1, 2, 3));
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof EachWithIndexIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException() {
    new EachWithIndexIterable<Integer>(null);
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof EachWithIndexIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[1=0, 2=1, 3=2]", iter.toString());
  }

}
