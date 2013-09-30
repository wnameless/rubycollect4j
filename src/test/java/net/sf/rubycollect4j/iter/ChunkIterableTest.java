package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.block.TransformBlock;

import org.junit.Before;
import org.junit.Test;

public class ChunkIterableTest {

  private ChunkIterable<Number, String> iter;

  @Before
  public void setUp() throws Exception {
    RubyArray<Number> nums =
        ra((Number) 1, (Number) 1.0, (Number) 1.0f, (Number) 2, (Number) 2L);
    iter =
        new ChunkIterable<Number, String>(nums,
            new TransformBlock<Number, String>() {

              @Override
              public String yield(Number item) {
                return item.toString();
              }

            });
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof ChunkIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    iter =
        new ChunkIterable<Number, String>(null,
            new TransformBlock<Number, String>() {

              @Override
              public String yield(Number item) {
                return item.toString();
              }

            });
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    iter =
        new ChunkIterable<Number, String>(ra((Number) 1, (Number) 1.0,
            (Number) 1.0f, (Number) 2, (Number) 2L), null);
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof ChunkIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[1=[1], 1.0=[1.0, 1.0], 2=[2, 2]]", iter.toString());
  }

}
