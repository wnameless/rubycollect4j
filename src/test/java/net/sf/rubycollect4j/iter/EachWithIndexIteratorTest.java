package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.hp;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class EachWithIndexIteratorTest {

  private EachWithIndexIterator<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter = new EachWithIndexIterator<Integer>(ra(1, 2, 3).iterator());
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof EachWithIndexIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException() {
    new EachWithIndexIterator<Integer>(null);
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
    assertEquals(hp(1, 0), iter.next());
    assertEquals(hp(2, 1), iter.next());
    assertEquals(hp(3, 2), iter.next());
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
