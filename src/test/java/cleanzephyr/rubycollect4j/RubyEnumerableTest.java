package cleanzephyr.rubycollect4j;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import cleanzephyr.rubycollect4j.block.BooleanBlock;
import cleanzephyr.rubycollect4j.block.ItemToListBlock;
import cleanzephyr.rubycollect4j.block.ItemTransformBlock;

import static cleanzephyr.rubycollect4j.RubyArray.newRubyArray;
import static cleanzephyr.rubycollect4j.RubyCollections.ra;
import static junit.framework.Assert.assertEquals;

public class RubyEnumerableTest {
  private RubyEnumerable<Integer> re;

  @Before
  public void setUp() throws Exception {}

  @Test
  public void testConstructor() {
    re = new RubyEnumerable<Integer>(Arrays.asList(1, 2));
    assertEquals(RubyEnumerable.class, re.getClass());
    re = new RubyEnumerable<Integer>(new Integer[] { 1 });
    assertEquals(RubyEnumerable.class, re.getClass());
    re = new RubyEnumerable<Integer>(1, 2, 3);
    assertEquals(RubyEnumerable.class, re.getClass());
  }

  @Test
  public void testAllʔ() {
    re = new RubyEnumerable<Integer>(1, 2);
    assertEquals(true, re.allʔ());
    re = new RubyEnumerable<Integer>(new Integer[0]);
    assertEquals(true, re.allʔ());
    re = new RubyEnumerable<Integer>(1, 2, null);
    assertEquals(false, re.allʔ());
  }

  @Test
  public void testAllʔWithBlock() {
    re = new RubyEnumerable<Integer>(1, 2, 3);
    assertEquals(false, re.allʔ(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item % 2 == 0;
      }

    }));
    assertEquals(true, re.allʔ(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item < 5;
      }

    }));
  }

  @Test
  public void testAnyʔ() {
    re = new RubyEnumerable<Integer>(1, 2);
    assertEquals(true, re.anyʔ());
    re = new RubyEnumerable<Integer>(new Integer[0]);
    assertEquals(false, re.anyʔ());
    re = new RubyEnumerable<Integer>(1, 2, null);
    assertEquals(true, re.anyʔ());
  }

  @Test
  public void testAnyʔWithBlock() {
    re = new RubyEnumerable<Integer>(1, 2, 3);
    assertEquals(true, re.anyʔ(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item % 2 == 0;
      }

    }));
    assertEquals(false, re.anyʔ(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item > 5;
      }

    }));
  }

  @Test
  public void testChunk() {
    re = new RubyEnumerable<Integer>(1, 2, 2, 3);
    RubyArray<Entry<Boolean, RubyArray<Integer>>> chunk =
        re.chunk(new ItemTransformBlock<Integer, Boolean>() {

          @Override
          public Boolean yield(Integer item) {
            return item % 2 == 0;
          }

        }).toA();
    assertEquals(new SimpleEntry<Boolean, RubyArray<Integer>>(false,
        newRubyArray(1)).toString(), chunk.get(0).toString());
    assertEquals(new SimpleEntry<Boolean, RubyArray<Integer>>(true,
        newRubyArray(2, 2)).toString(), chunk.get(1).toString());
    assertEquals(new SimpleEntry<Boolean, RubyArray<Integer>>(false,
        newRubyArray(3)).toString(), chunk.get(2).toString());
    assertEquals(3, chunk.size());
  }

  @Test
  public void testCollect() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.collect().getClass());
    assertEquals(ra(1, 2, 3, 4), re.collect().toA());
  }

  @Test
  public void testCollectWithBlock() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    assertEquals(ra(1.0, 2.0, 3.0, 4.0),
        re.collect(new ItemTransformBlock<Integer, Double>() {

          @Override
          public Double yield(Integer item) {
            return Double.valueOf(item);
          }

        }));
  }

  @Test
  public void testCollectConcat() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.collectConcat().getClass());
    assertEquals(ra(1, 2, 3, 4), re.collectConcat().toA());
  }

  @Test
  public void testCollectConcatWithBlock() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    assertEquals(ra(1.0, 2.0, 3.0, 4.0),
        re.collectConcat(new ItemToListBlock<Integer, Double>() {

          @Override
          public List<Double> yield(Integer item) {
            return Arrays.asList(Double.valueOf(item));
          }

        }));
  }

  @Test
  public void testCount() {
    re = new RubyEnumerable<Integer>();
    assertEquals(0, re.count());
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    assertEquals(4, re.count());
  }

  @Test
  public void testCountWithBlock() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    assertEquals(2, re.count(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item % 2 == 1;
      }

    }));
  }

}
