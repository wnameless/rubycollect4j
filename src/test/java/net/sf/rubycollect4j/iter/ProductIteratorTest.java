package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import net.sf.rubycollect4j.RubyArray;

import org.junit.Before;
import org.junit.Test;

public class ProductIteratorTest {

  private ProductIterator<Integer> iter;

  @Before
  public void setUp() throws Exception {
    RubyArray<List<Integer>> lists = ra();
    iter =
        new ProductIterator<Integer>(lists.push(ra(1, 2, 3)).push(ra(4, 5, 6)));
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof ProductIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException() {
    new ProductIterator<Integer>(null);
  }

  @Test
  public void testHasNext() {
    assertTrue(iter.hasNext());
    while (iter.hasNext()) {
      iter.next();
    }
    assertFalse(iter.hasNext());
  }

  @Test
  public void testNext() {
    assertEquals(ra(1, 4), iter.next());
    assertEquals(ra(1, 5), iter.next());
    assertEquals(ra(1, 6), iter.next());
    assertEquals(ra(2, 4), iter.next());
    assertEquals(ra(2, 5), iter.next());
    assertEquals(ra(2, 6), iter.next());
    assertEquals(ra(3, 4), iter.next());
    assertEquals(ra(3, 5), iter.next());
    assertEquals(ra(3, 6), iter.next());
    assertFalse(iter.hasNext());
    iter = new ProductIterator<Integer>(ra(ra(1, 2, 3)));
    assertEquals(ra(1), iter.next());
    assertEquals(ra(2), iter.next());
    assertEquals(ra(3), iter.next());
    assertFalse(iter.hasNext());
    RubyArray<List<Integer>> lists = ra();
    iter =
        new ProductIterator<Integer>(lists.push(ra(1, 2, 3)).push(
            new ArrayList<Integer>()));
    assertFalse(iter.hasNext());
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
