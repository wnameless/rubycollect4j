package net.sf.rubycollect4j.iter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.sf.rubycollect4j.succ.IntegerSuccessor;

import org.junit.Before;
import org.junit.Test;

public class RangeIterableTest {

  private RangeIterable<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter = new RangeIterable<Integer>(IntegerSuccessor.getInstance(), 1, 3);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof RangeIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new RangeIterable<Integer>(null, 1, 3);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new RangeIterable<Integer>(IntegerSuccessor.getInstance(), null, 3);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException3() {
    new RangeIterable<Integer>(IntegerSuccessor.getInstance(), 1, null);
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof RangeIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[1, 2, 3]", iter.toString());
  }

}
