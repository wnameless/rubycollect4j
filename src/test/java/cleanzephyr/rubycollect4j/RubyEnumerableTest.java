package cleanzephyr.rubycollect4j;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import cleanzephyr.rubycollect4j.block.BooleanBlock;
import cleanzephyr.rubycollect4j.block.ItemBlock;
import cleanzephyr.rubycollect4j.block.ItemToRubyArrayBlock;
import cleanzephyr.rubycollect4j.block.ItemTransformBlock;
import cleanzephyr.rubycollect4j.block.ItemWithIndexBlock;
import cleanzephyr.rubycollect4j.block.ItemWithObjectBlock;
import cleanzephyr.rubycollect4j.block.ListBlock;

import static cleanzephyr.rubycollect4j.RubyArray.newRubyArray;
import static cleanzephyr.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        re.collectConcat(new ItemToRubyArrayBlock<Integer, Double>() {

          @Override
          public RubyArray<Double> yield(Integer item) {
            return ra(Double.valueOf(item));
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

  @Test
  public void testCycle() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.cycle().getClass());
    RubyArray<Integer> ints = ra(1, 2, 3, 4);
    Iterator<Integer> iter = re.cycle().iterator();
    for (int i = 0; i < 100; i++) {
      assertEquals(ints.get(0), iter.next());
      ints.rotateǃ();
    }
  }

  @Test
  public void testCycleWithN() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.cycle().getClass());
    RubyArray<Integer> ints = newRubyArray();
    Iterator<Integer> iter = re.cycle(2).iterator();
    while (iter.hasNext()) {
      ints.push(iter.next());
    }
    assertEquals(ra(1, 2, 3, 4, 1, 2, 3, 4), ints);
  }

  @Test
  public void testCycleWithBlock() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    final RubyArray<Integer> ints = newRubyArray();
    re.cycle(2, new ItemBlock<Integer>() {

      @Override
      public void yield(Integer item) {
        ints.push(item * 2);
      }

    });
    assertEquals(ra(2, 4, 6, 8, 2, 4, 6, 8), ints);
  }

  @Test
  public void testDetect() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.detect().getClass());
    assertEquals(ra(1, 2, 3, 4), re.detect().toA());
  }

  @Test
  public void testDetectWithBlock() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    assertEquals(Integer.valueOf(3), re.detect(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item == 3;
      }

    }));
    assertNull(re.detect(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item == 5;
      }

    }));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDrop() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    assertEquals(ra(3, 4), re.drop(2));
    re.drop(-1);
  }

  @Test
  public void testDropWhile() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.dropWhile().getClass());
    assertEquals(ra(1), re.dropWhile().toA());
  }

  @Test
  public void testDropWhileWithBlock() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    assertEquals(ra(3, 4), re.dropWhile(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item < 3;
      }

    }));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEachCons() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.eachCons(2).getClass());
    @SuppressWarnings("unchecked")
    RubyArray<RubyArray<Integer>> ra = ra(ra(1, 2), ra(2, 3), ra(3, 4));
    assertEquals(ra, re.eachCons(2).toA());
    re.eachCons(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEachConsWithBlock() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    final RubyArray<List<Integer>> ra = ra();
    re.eachCons(2, new ListBlock<Integer>() {

      @Override
      public void yield(List<Integer> item) {
        ra.push(item);
      }

    });
    assertEquals(ra, re.eachCons(2).toA());
    re.eachCons(0, new ListBlock<Integer>() {

      @Override
      public void yield(List<Integer> item) {}

    });
  }

  @Test
  public void testEachEntry() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.eachEntry().getClass());
    assertEquals(ra(1, 2, 3, 4), re.eachEntry().toA());
  }

  @Test
  public void testEachEntryWithBlock() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    final RubyArray<Integer> ints = ra();
    re.eachEntry(new ItemBlock<Integer>() {

      @Override
      public void yield(Integer item) {
        ints.add(item * 2);
      }

    });
    assertEquals(ra(2, 4, 6, 8), ints);
    assertEquals(RubyEnumerable.class, re.eachEntry(new ItemBlock<Integer>() {

      @Override
      public void yield(Integer item) {}

    }).getClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEachSlice() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.eachSlice(3).getClass());
    @SuppressWarnings("unchecked")
    RubyArray<RubyArray<Integer>> ra = ra(ra(1, 2, 3), ra(4));
    assertEquals(ra, re.eachSlice(3).toA());
    re.eachSlice(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEachSliceWithBlock() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    final RubyArray<Integer> ints = ra();
    re.eachSlice(3, new ListBlock<Integer>() {

      @Override
      public void yield(List<Integer> item) {
        ints.add(item.get(0));
      }

    });
    assertEquals(ra(1, 4), ints);
    re.eachSlice(0);
  }

  @Test
  public void testEachWithIndex() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.eachWithIndex().getClass());
    @SuppressWarnings("unchecked")
    RubyArray<SimpleEntry<Integer, Integer>> ra =
        ra(new SimpleEntry<Integer, Integer>(1, 0),
            new SimpleEntry<Integer, Integer>(2, 1),
            new SimpleEntry<Integer, Integer>(3, 2),
            new SimpleEntry<Integer, Integer>(4, 3));
    assertEquals(ra, re.eachWithIndex().toA());
  }

  @Test
  public void testEachWithIndexWithBlock() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    final RubyArray<Integer> ints = ra();
    assertEquals(RubyEnumerable.class,
        re.eachWithIndex(new ItemWithIndexBlock<Integer>() {

          @Override
          public void yield(Integer item, int index) {
            ints.add(item + index);
          }

        }).getClass());
    assertEquals(ra(1, 3, 5, 7), ints);
  }

  @Test
  public void testEachWithObject() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    Long obj = 0L;
    assertEquals(RubyEnumerator.class, re.eachWithObject(obj).getClass());
    @SuppressWarnings("unchecked")
    RubyArray<SimpleEntry<Integer, Long>> ra =
        ra(new SimpleEntry<Integer, Long>(1, obj),
            new SimpleEntry<Integer, Long>(2, obj),
            new SimpleEntry<Integer, Long>(3, obj),
            new SimpleEntry<Integer, Long>(4, obj));
    assertEquals(ra, re.eachWithObject(obj).toA());
  }

  @Test
  public void testEachWithObjectWithBlock() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    Long[] obj = new Long[] { 0L };
    assertEquals(new Long[] { 10L }[0],
        re.eachWithObject(obj, new ItemWithObjectBlock<Integer, Long[]>() {

          @Override
          public void yield(Integer item, Long[] o) {
            o[0] += item;
          }

        })[0]);
  }

  @Test
  public void testEntries() {
    re = new RubyEnumerable<Integer>(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3, 4), re.entries());
  }

}
