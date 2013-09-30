package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.hp;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.rh;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class OrderedEntrySetIteratorTest {

  private OrderedEntrySetIterator<String, Integer> setIter;

  @Before
  public void setUp() {
    setIter =
        new OrderedEntrySetIterator<String, Integer>(ra("a", "b", "c")
            .iterator(), rh("c", 3, "b", 2, "a", 1));
  }

  @Test
  public void testConstructor() {
    assertTrue(setIter instanceof OrderedEntrySetIterator);
  }

  @Test
  public void testHasNext() {
    assertTrue(setIter.hasNext());
    while (setIter.hasNext()) {
      setIter.next();
    }
    assertFalse(setIter.hasNext());
  }

  @Test
  public void testNext() {
    assertEquals(hp("a", 1), setIter.next());
    assertEquals(hp("b", 2), setIter.next());
    assertEquals(hp("c", 3), setIter.next());
    assertFalse(setIter.hasNext());
  }

  @Test(expected = NoSuchElementException.class)
  public void testNextException() {
    while (setIter.hasNext()) {
      setIter.next();
    }
    setIter.next();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemove() {
    setIter.remove();
  }

}
