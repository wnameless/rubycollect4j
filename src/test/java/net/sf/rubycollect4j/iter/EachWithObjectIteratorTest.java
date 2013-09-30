package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.hp;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class EachWithObjectIteratorTest {

  private EachWithObjectIterator<Integer, List<Integer>> iter;

  @Before
  public void setUp() throws Exception {
    iter =
        new EachWithObjectIterator<Integer, List<Integer>>(ra(1, 2, 3)
            .iterator(), new ArrayList<Integer>());
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof EachWithObjectIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new EachWithObjectIterator<Integer, List<Integer>>(null,
        new ArrayList<Integer>());
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new EachWithObjectIterator<Integer, List<Integer>>(ra(1, 2, 3).iterator(),
        null);
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
    assertEquals(hp(1, new ArrayList<Integer>()), iter.next());
    assertEquals(hp(2, new ArrayList<Integer>()), iter.next());
    assertEquals(hp(3, new ArrayList<Integer>()), iter.next());
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
