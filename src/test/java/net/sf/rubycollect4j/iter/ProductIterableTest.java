package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ProductIterableTest {

  private ProductIterable<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter = new ProductIterable<Integer>(ra(1, 2, 3), ra(ra(4, 5, 6)));
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof ProductIterable);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testConstructorWithVarargs() {
    iter = new ProductIterable<Integer>(ra(1, 2, 3), ra(4, 5, 6));
    assertTrue(iter instanceof ProductIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new ProductIterable<Integer>(null, ra(ra(4, 5, 6)));
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    List<List<Integer>> others = null;
    new ProductIterable<Integer>(ra(1, 2, 3), others);
  }

  @SuppressWarnings("unchecked")
  @Test(expected = NullPointerException.class)
  public void testConstructorException3() {
    new ProductIterable<Integer>(null, ra(4, 5, 6));
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof ProductIterator);
  }

  @Test
  public void testToString() {
    assertEquals(
        "[[1, 4], [1, 5], [1, 6], [2, 4], [2, 5], [2, 6], [3, 4], [3, 5], [3, 6]]",
        iter.toString());
  }

}
