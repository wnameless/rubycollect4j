package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class EachWithObjectIterableTest {

  private EachWithObjectIterable<Integer, List<Integer>> iter;

  @Before
  public void setUp() throws Exception {
    iter =
        new EachWithObjectIterable<Integer, List<Integer>>(ra(1, 2, 3),
            new ArrayList<Integer>());
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof EachWithObjectIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new EachWithObjectIterable<Integer, List<Integer>>(null,
        new ArrayList<Integer>());
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new EachWithObjectIterable<Integer, List<Integer>>(ra(1, 2, 3), null);
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof EachWithObjectIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[1=[], 2=[], 3=[]]", iter.toString());
  }

}
