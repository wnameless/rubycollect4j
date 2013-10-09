package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.rh;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ComparableEntryIterableTest {

  private ComparableEntryIterable<Integer, Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter =
        new ComparableEntryIterable<Integer, Integer>(rh(1, 2, 3, 4).entrySet());
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof ComparableEntryIterable);
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof ComparableEntryIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[1=2, 3=4]", iter.toString());
  }

}
