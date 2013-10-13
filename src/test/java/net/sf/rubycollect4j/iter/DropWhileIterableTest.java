package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.sf.rubycollect4j.block.BooleanBlock;

import org.junit.Before;
import org.junit.Test;

public class DropWhileIterableTest {

  private DropWhileIterable<Integer> iter;
  private BooleanBlock<Integer> block;

  @Before
  public void setUp() throws Exception {
    block = new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item < 3;
      }

    };
    iter = new DropWhileIterable<Integer>(ra(1, 2, 3, 4, 5), block);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof DropWhileIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new DropWhileIterable<Integer>(null, block);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException2() {
    new DropWhileIterable<Integer>(ra(1, 2, 3, 4, 5), null);
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof DropWhileIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[3, 4, 5]", iter.toString());
  }

}
