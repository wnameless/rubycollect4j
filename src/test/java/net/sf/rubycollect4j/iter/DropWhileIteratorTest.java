package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import net.sf.rubycollect4j.block.BooleanBlock;

import org.junit.Before;
import org.junit.Test;

public class DropWhileIteratorTest {

  private DropWhileIterator<Integer> iter;
  private List<Integer> list;
  private BooleanBlock<Integer> block;

  @Before
  public void setUp() throws Exception {
    list = ra(1, 2, 3, 4, 5);
    block = new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item < 3;
      }

    };
    iter = new DropWhileIterator<Integer>(list.iterator(), block);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof DropWhileIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new DropWhileIterator<Integer>(list.iterator(), null);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new DropWhileIterator<Integer>(null, block);
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
    assertEquals(Integer.valueOf(3), iter.next());
    assertEquals(Integer.valueOf(4), iter.next());
    assertEquals(Integer.valueOf(5), iter.next());
    assertFalse(iter.hasNext());
    iter =
        new DropWhileIterator<Integer>(new ArrayList<Integer>().iterator(),
            block);
    assertFalse(iter.hasNext());
  }

  @Test
  public void testRemove() {
    iter.next();
    iter.remove();
    assertEquals(ra(1, 2, 4, 5), list);
  }

  @Test(expected = IllegalStateException.class)
  public void testRemoveException() {
    iter.remove();
  }

}
