package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.hp;
import static net.sf.rubycollect4j.RubyCollections.rh;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.sf.rubycollect4j.RubyHash;

import org.junit.Before;
import org.junit.Test;

public class ComparableEntryIteratorTest {

  private ComparableEntryIterator<Integer, Integer> iter;
  private RubyHash<Integer, Integer> rubyHash;

  @Before
  public void setUp() throws Exception {
    rubyHash = rh(1, 2, 3, 4);
    iter =
        new ComparableEntryIterator<Integer, Integer>(rubyHash.entrySet()
            .iterator());
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof ComparableEntryIterator);
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
    assertEquals(hp(1, 2), iter.next());
    assertEquals(hp(3, 4), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test
  public void testRemove() {
    iter.next();
    iter.remove();
    assertEquals(rh(3, 4), rubyHash);
  }

}
