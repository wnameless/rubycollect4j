/**
 *
 * @author Wei-Ming Wu
 *
 *
 * Copyright 2013 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package net.sf.rubycollect4j;

import static net.sf.rubycollect4j.RubyCollections.newPair;
import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.newRubyEnumerator;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.rh;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.sf.rubycollect4j.block.Block;
import net.sf.rubycollect4j.block.BooleanBlock;
import net.sf.rubycollect4j.block.ReduceBlock;
import net.sf.rubycollect4j.block.TransformBlock;
import net.sf.rubycollect4j.block.WithIndexBlock;
import net.sf.rubycollect4j.block.WithInitBlock;
import net.sf.rubycollect4j.block.WithObjectBlock;

import org.junit.Test;

public class RubyEnumerableTest {

  private RubyEnumerable<Integer> re;

  @Test
  public void testConstructor() {
    re = newRubyEnumerator(Arrays.asList(1, 2));
    assertTrue(re instanceof RubyEnumerable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException() {
    List<Integer> ints = null;
    re = newRubyEnumerator(ints);
  }

  @Test
  public void testGetIterable() {
    Iterable<Integer> iter = Arrays.asList(1, 2);
    re = newRubyEnumerator(iter);
    assertTrue(iter == re.getIterable());
  }

  @Test
  public void testAllʔ() {
    re = newRubyEnumerator(Arrays.asList(1, 2));
    assertEquals(true, re.allʔ());
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertEquals(true, re.allʔ());
    re = newRubyEnumerator(Arrays.asList(1, 2, null));
    assertEquals(false, re.allʔ());
  }

  @Test
  public void testAllʔWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3));
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
    re = newRubyEnumerator(Arrays.asList(1, 2));
    assertEquals(true, re.anyʔ());
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertEquals(false, re.anyʔ());
    re = newRubyEnumerator(Arrays.asList(1, 2, null));
    assertEquals(true, re.anyʔ());
    List<Integer> ints = Arrays.asList(null, null);
    re = newRubyEnumerator(ints);
    assertEquals(false, re.anyʔ());
  }

  @Test
  public void testAnyʔWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3));
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
    re = newRubyEnumerator(Arrays.asList(1, 2, 2, 3));
    RubyArray<Entry<Boolean, RubyArray<Integer>>> chunk =
        re.chunk(new TransformBlock<Integer, Boolean>() {

          @Override
          public Boolean yield(Integer item) {
            return item % 2 == 0;
          }

        }).toA();
    assertEquals(newPair(false, newRubyArray(1)).toString(), chunk.get(0)
        .toString());
    assertEquals(newPair(true, newRubyArray(2, 2)).toString(), chunk.get(1)
        .toString());
    assertEquals(newPair(false, newRubyArray(3)).toString(), chunk.get(2)
        .toString());
    assertEquals(3, chunk.size());
  }

  @Test
  public void testCollect() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.collect().getClass());
    assertEquals(ra(1, 2, 3, 4), re.collect().toA());
  }

  @Test
  public void testCollectWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(ra(1.0, 2.0, 3.0, 4.0),
        re.collect(new TransformBlock<Integer, Double>() {

          @Override
          public Double yield(Integer item) {
            return Double.valueOf(item);
          }

        }));
  }

  @Test
  public void testCollectConcat() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.collectConcat().getClass());
    assertEquals(ra(1, 2, 3, 4), re.collectConcat().toA());
  }

  @Test
  public void testCollectConcatWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(ra(1.0, 2.0, 3.0, 4.0),
        re.collectConcat(new TransformBlock<Integer, RubyArray<Double>>() {

          @Override
          public RubyArray<Double> yield(Integer item) {
            return ra(Double.valueOf(item));
          }

        }));
  }

  @Test
  public void testCount() {
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertEquals(0, re.count());
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(4, re.count());
  }

  @Test
  public void testCountWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(2, re.count(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item % 2 == 1;
      }

    }));
  }

  @Test
  public void testCycle() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
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
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.cycle().getClass());
    RubyArray<Integer> ints = newRubyArray();
    Iterator<Integer> iter = re.cycle(2).iterator();
    while (iter.hasNext()) {
      ints.push(iter.next());
    }
    assertEquals(ra(1, 2, 3, 4, 1, 2, 3, 4), ints);
  }

  @Test
  public void testCycleWithNAndBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    final RubyArray<Integer> ints = newRubyArray();
    re.cycle(2, new Block<Integer>() {

      @Override
      public void yield(Integer item) {
        ints.push(item * 2);
      }

    });
    assertEquals(ra(2, 4, 6, 8, 2, 4, 6, 8), ints);
  }

  @Test(expected = IllegalStateException.class)
  public void testCycleWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    final RubyArray<Integer> ints = newRubyArray();
    re.cycle(new Block<Integer>() {

      @Override
      public void yield(Integer item) {
        ints.push(item);
        if (ints.size() > 1000) {
          throw new IllegalStateException();
        }
      }

    });
  }

  @Test
  public void testDetect() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.detect().getClass());
    assertEquals(ra(1, 2, 3, 4), re.detect().toA());
  }

  @Test
  public void testDetectWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
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

  @Test
  public void testDrop() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(ra(1, 2, 3, 4), re.drop(0));
    assertEquals(ra(3, 4), re.drop(2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDropException() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    re.drop(-1);
  }

  @Test
  public void testDropWhile() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.dropWhile().getClass());
    assertEquals(ra(1), re.dropWhile().toA());
    List<Integer> ints = new ArrayList<Integer>();
    re = newRubyEnumerator(ints);
    assertEquals(ra(), re.dropWhile().toA());
  }

  @Test
  public void testDropWhileWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(ra(3, 4), re.dropWhile(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item < 3;
      }

    }));
  }

  @Test
  public void testEachCons() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.eachCons(2).getClass());
    @SuppressWarnings("unchecked")
    RubyArray<RubyArray<Integer>> ra = ra(ra(1, 2), ra(2, 3), ra(3, 4));
    assertEquals(ra, re.eachCons(2).toA());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEachConsException() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    re.eachCons(0);
  }

  @Test
  public void testEachConsWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    final RubyArray<List<Integer>> ra = ra();
    re.eachCons(2, new Block<RubyArray<Integer>>() {

      @Override
      public void yield(RubyArray<Integer> item) {
        ra.push(item);
      }

    });
    assertEquals(ra, re.eachCons(2).toA());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEachConsWithBlockException() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    re.eachCons(0, new Block<RubyArray<Integer>>() {

      @Override
      public void yield(RubyArray<Integer> item) {}

    });
  }

  @Test
  public void testEachEntry() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.eachEntry().getClass());
    assertEquals(ra(1, 2, 3, 4), re.eachEntry().toA());
  }

  @Test
  public void testEachEntryWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    final RubyArray<Integer> ints = ra();
    re.eachEntry(new Block<Integer>() {

      @Override
      public void yield(Integer item) {
        ints.add(item * 2);
      }

    });
    assertEquals(ra(2, 4, 6, 8), ints);
    assertTrue(re.eachEntry(new Block<Integer>() {

      @Override
      public void yield(Integer item) {}

    }) instanceof RubyEnumerable);
  }

  @Test
  public void testEachSlice() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.eachSlice(3).getClass());
    @SuppressWarnings("unchecked")
    RubyArray<RubyArray<Integer>> ra = ra(ra(1, 2, 3), ra(4));
    assertEquals(ra, re.eachSlice(3).toA());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEachSliceException() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    re.eachSlice(0);
  }

  @Test
  public void testEachSliceWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    final RubyArray<Integer> ints = ra();
    re.eachSlice(3, new Block<RubyArray<Integer>>() {

      @Override
      public void yield(RubyArray<Integer> item) {
        ints.add(item.get(0));
      }

    });
    assertEquals(ra(1, 4), ints);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testEachSliceWithBlockException() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    re.eachSlice(0, new Block<RubyArray<Integer>>() {

      @Override
      public void yield(RubyArray<Integer> item) {}

    });
  }

  @Test
  public void testEachWithIndex() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.eachWithIndex().getClass());
    @SuppressWarnings("unchecked")
    RubyArray<? extends Entry<Integer, Integer>> ra =
        ra(newPair(1, 0), newPair(2, 1), newPair(3, 2), newPair(4, 3));
    assertEquals(ra, re.eachWithIndex().toA());
  }

  @Test
  public void testEachWithIndexWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    final RubyArray<Integer> ints = ra();
    assertTrue(re.eachWithIndex(new WithIndexBlock<Integer>() {

      @Override
      public void yield(Integer item, int index) {
        ints.add(item + index);
      }

    }) instanceof RubyEnumerable);
    assertEquals(ra(1, 3, 5, 7), ints);
  }

  @Test
  public void testEachWithObject() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    Long obj = 0L;
    assertEquals(RubyEnumerator.class, re.eachWithObject(obj).getClass());
    @SuppressWarnings("unchecked")
    RubyArray<? extends Entry<Integer, Long>> ra =
        ra(newPair(1, obj), newPair(2, obj), newPair(3, obj), newPair(4, obj));
    assertEquals(ra, re.eachWithObject(obj).toA());
  }

  @Test
  public void testEachWithObjectWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    Long[] obj = new Long[] { 0L };
    assertEquals(new Long[] { 10L }[0],
        re.eachWithObject(obj, new WithObjectBlock<Integer, Long[]>() {

          @Override
          public void yield(Integer item, Long[] o) {
            o[0] += item;
          }

        })[0]);
  }

  @Test
  public void testEntries() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(ra(1, 2, 3, 4), re.entries());
  }

  @Test
  public void testFind() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.find().getClass());
    assertEquals(ra(1, 2, 3, 4), re.find().toA());
  }

  @Test
  public void testFindWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(Integer.valueOf(3), re.find(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item == 3;
      }

    }));
    assertNull(re.find(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item == 5;
      }

    }));
  }

  @Test
  public void testFindAll() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.findAll().getClass());
    assertEquals(ra(1, 2, 3, 4), re.findAll().toA());
  }

  @Test
  public void testFindAllWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(ra(2, 3, 4), re.findAll(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item >= 2;
      }

    }));
  }

  @Test
  public void testFindIndex() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.findIndex().getClass());
    assertEquals(ra(1, 2, 3, 4), re.findIndex().toA());
  }

  @Test
  public void testFindIndexWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(Integer.valueOf(3), re.findIndex(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item >= 4;
      }

    }));
    assertNull(re.findIndex(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item > 4;
      }

    }));
  }

  @Test
  public void testFindIndexWithTarget() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(Integer.valueOf(2), re.findIndex(3));
    assertNull(re.findIndex(0));
  }

  @Test
  public void testFirst() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(Integer.valueOf(1), re.first());
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertNull(re.first());
  }

  @Test
  public void testFirstWithN() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(ra(), re.first(0));
    assertEquals(ra(1, 2, 3), re.first(3));
    assertEquals(ra(1, 2, 3, 4), re.first(6));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFirstWithNException() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    re.first(-1);
  }

  @Test
  public void testFlatMap() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.flatMap().getClass());
    assertEquals(ra(1, 2, 3, 4), re.flatMap().toA());
  }

  @Test
  public void testFlatMapWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(ra(1L, 2L, 3L, 4L),
        re.flatMap(new TransformBlock<Integer, RubyArray<Long>>() {

          @Override
          public RubyArray<Long> yield(Integer item) {
            return ra(Long.valueOf(item));
          }

        }));
  }

  @Test
  public void testGrep() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(ra(2, 4), re.grep("[24]"));
  }

  @Test
  public void testGrepWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(ra("2", "4"),
        re.grep("[24]", new TransformBlock<Integer, String>() {

          @Override
          public String yield(Integer item) {
            return item.toString();
          }

        }));
  }

  @Test
  public void testGroupBy() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.groupBy().getClass());
    assertEquals(ra(1, 2, 3, 4), re.groupBy().toA());
  }

  @Test
  public void testGroupByWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(rh(1, ra(1, 4), 2, ra(2), 0, ra(3)),
        re.groupBy(new TransformBlock<Integer, Integer>() {

          @Override
          public Integer yield(Integer item) {
            return item % 3;
          }

        }));
  }

  @Test
  public void testIncludeʔ() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertTrue(re.includeʔ(1));
    assertFalse(re.includeʔ(5));
  }

  @Test
  public void testInjectWithInit() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    RubyArray<Integer> ra = ra();
    assertEquals(ra(1, 2, 3, 4), re.inject(ra, "push"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInjectWithInitException() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    RubyArray<Integer> ra = ra();
    re.inject(ra, "no push");
  }

  @Test
  public void testInjectWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(Integer.valueOf(10), re.inject(new ReduceBlock<Integer>() {

      @Override
      public Integer yield(Integer memo, Integer item) {
        return memo + item;
      }

    }));
  }

  @Test
  public void testInjectWithInitAndBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(Long.valueOf(20),
        re.inject(Long.valueOf(10), new WithInitBlock<Integer, Long>() {

          @Override
          public Long yield(Long memo, Integer item) {
            return memo + item;
          }

        }));
  }

  @Test
  public void testInject() {
    RubyEnumerable<Boolean> bools =
        newRubyEnumerator(Arrays.asList(true, true, true));
    assertEquals(Boolean.TRUE, bools.inject("equals"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInjectException() {
    RubyEnumerable<Boolean> bools =
        newRubyEnumerator(Arrays.asList(true, true, true));
    bools.inject("not equals");
  }

  @Test
  public void testMap() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.map().getClass());
    assertEquals(ra(1, 2, 3, 4), re.map().toA());
  }

  @Test
  public void testMapWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(ra(1L, 2L, 3L, 4L),
        re.map(new TransformBlock<Integer, Long>() {

          @Override
          public Long yield(Integer item) {
            return Long.valueOf(item);
          }

        }));
  }

  @Test
  public void testMax() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(Integer.valueOf(4), re.max());
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertNull(re.max());
  }

  @Test
  public void testMaxWithComparator() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(Integer.valueOf(1), re.max(new Comparator<Integer>() {

      @Override
      public int compare(Integer arg0, Integer arg1) {
        return arg1 - arg0;
      }

    }));
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertNull(re.max(new Comparator<Integer>() {

      @Override
      public int compare(Integer arg0, Integer arg1) {
        return arg1 - arg0;
      }

    }));
  }

  @Test
  public void testMaxBy() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.maxBy().getClass());
    assertEquals(ra(1, 2, 3, 4), re.maxBy().toA());
  }

  @Test
  public void testMaxByWithComparatorAndBlock() {
    RubyEnumerable<String> re =
        newRubyEnumerator(Arrays.asList("aaaa", "bbb", "cc", "d"));
    assertEquals("d", re.maxBy(new Comparator<Integer>() {

      @Override
      public int compare(Integer o1, Integer o2) {
        return o2 - o1;
      }

    }, new TransformBlock<String, Integer>() {

      @Override
      public Integer yield(String item) {
        return item.length();
      }

    }));
    re = newRubyEnumerator(new ArrayList<String>());
    assertNull(re.maxBy(new Comparator<Integer>() {

      @Override
      public int compare(Integer o1, Integer o2) {
        return o2 - o1;
      }

    }, new TransformBlock<String, Integer>() {

      @Override
      public Integer yield(String item) {
        return item.length();
      }

    }));
  }

  @Test
  public void testMaxByWithBlock() {
    RubyEnumerable<String> re =
        newRubyEnumerator(Arrays.asList("aaaa", "bbb", "cc", "d"));
    assertEquals("aaaa", re.maxBy(new TransformBlock<String, Integer>() {

      @Override
      public Integer yield(String item) {
        return item.length();
      }

    }));
    re = newRubyEnumerator(new ArrayList<String>());
    assertNull(re.maxBy(new TransformBlock<String, Integer>() {

      @Override
      public Integer yield(String item) {
        return item.length();
      }

    }));
  }

  @Test
  public void testMemberʔ() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertTrue(re.memberʔ(1));
    assertFalse(re.memberʔ(5));
  }

  @Test
  public void testMin() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(Integer.valueOf(1), re.min());
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertNull(re.min());
  }

  @Test
  public void testMinWithComparator() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(Integer.valueOf(4), re.min(new Comparator<Integer>() {

      @Override
      public int compare(Integer arg0, Integer arg1) {
        return arg1 - arg0;
      }

    }));
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertNull(re.min(new Comparator<Integer>() {

      @Override
      public int compare(Integer arg0, Integer arg1) {
        return arg1 - arg0;
      }

    }));
  }

  @Test
  public void testMinBy() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.minBy().getClass());
    assertEquals(ra(1, 2, 3, 4), re.minBy().toA());
  }

  @Test
  public void testMinByWithComparatorAndBlock() {
    RubyEnumerable<String> re =
        newRubyEnumerator(Arrays.asList("aaaa", "bbb", "cc", "d"));
    assertEquals("aaaa", re.minBy(new Comparator<Integer>() {

      @Override
      public int compare(Integer o1, Integer o2) {
        return o2 - o1;
      }

    }, new TransformBlock<String, Integer>() {

      @Override
      public Integer yield(String item) {
        return item.length();
      }

    }));
    re = newRubyEnumerator(new ArrayList<String>());
    assertNull(re.minBy(new Comparator<Integer>() {

      @Override
      public int compare(Integer o1, Integer o2) {
        return o2 - o1;
      }

    }, new TransformBlock<String, Integer>() {

      @Override
      public Integer yield(String item) {
        return item.length();
      }

    }));
  }

  @Test
  public void testMinByWithBlock() {
    RubyEnumerable<String> re =
        newRubyEnumerator(Arrays.asList("aaaa", "bbb", "cc", "d"));
    assertEquals("d", re.minBy(new TransformBlock<String, Integer>() {

      @Override
      public Integer yield(String item) {
        return item.length();
      }

    }));
    re = newRubyEnumerator(new ArrayList<String>());
    assertNull(re.minBy(new TransformBlock<String, Integer>() {

      @Override
      public Integer yield(String item) {
        return item.length();
      }

    }));
  }

  @Test
  public void testMinmax() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(ra(1, 4), re.minmax());
    re = newRubyEnumerator(Arrays.asList(1));
    assertEquals(ra(1, 1), re.minmax());
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertEquals(ra(null, null), re.minmax());
  }

  @Test
  public void testMinmaxWithComparator() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(ra(4, 1), re.minmax(new Comparator<Integer>() {

      @Override
      public int compare(Integer o1, Integer o2) {
        return o2 - o1;
      }

    }));
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertEquals(ra(null, null), re.minmax(new Comparator<Integer>() {

      @Override
      public int compare(Integer o1, Integer o2) {
        return o2 - o1;
      }

    }));
  }

  @Test
  public void testMinmaxBy() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.minmaxBy().getClass());
    assertEquals(ra(1, 2, 3, 4), re.minmaxBy().toA());
  }

  @Test
  public void testMinmaxByWithComparatorAndBlock() {
    RubyEnumerable<String> re =
        newRubyEnumerator(Arrays.asList("aaaa", "bbb", "cc", "d"));
    assertEquals(ra("aaaa", "d"), re.minmaxBy(new Comparator<Integer>() {

      @Override
      public int compare(Integer o1, Integer o2) {
        return o2 - o1;
      }
    }, new TransformBlock<String, Integer>() {

      @Override
      public Integer yield(String item) {
        return item.length();
      }

    }));
    re = newRubyEnumerator(new ArrayList<String>());
    assertEquals(ra(null, null), re.minmaxBy(new Comparator<Integer>() {

      @Override
      public int compare(Integer o1, Integer o2) {
        return o2 - o1;
      }
    }, new TransformBlock<String, Integer>() {

      @Override
      public Integer yield(String item) {
        return item.length();
      }

    }));
  }

  @Test
  public void testMinmaxByWithBlock() {
    RubyEnumerable<String> re =
        newRubyEnumerator(Arrays.asList("aaaa", "bbb", "cc", "d"));
    assertEquals(ra("d", "aaaa"),
        re.minmaxBy(new TransformBlock<String, Integer>() {

          @Override
          public Integer yield(String item) {
            return item.length();
          }

        }));
    re = newRubyEnumerator(new ArrayList<String>());
    assertEquals(ra(null, null),
        re.minmaxBy(new TransformBlock<String, Integer>() {

          @Override
          public Integer yield(String item) {
            return item.length();
          }

        }));
  }

  @Test
  public void testNoneʔ() {
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertTrue(re.noneʔ());
    RubyArray<Integer> ra = ra();
    ra.push(null);
    re = newRubyEnumerator(ra);
    assertTrue(re.noneʔ());
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertFalse(re.noneʔ());
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, null));
    assertFalse(re.noneʔ());
  }

  @Test
  public void testNoneʔWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertTrue(re.noneʔ(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item > 5;
      }

    }));
    assertFalse(re.noneʔ(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item == 3;
      }

    }));
  }

  @Test
  public void testOneʔ() {
    re = newRubyEnumerator(Arrays.asList(1));
    assertTrue(re.oneʔ());
    re = newRubyEnumerator(Arrays.asList(null, null, 1));
    assertTrue(re.oneʔ());
    re = newRubyEnumerator(Arrays.asList(1, 2));
    assertFalse(re.oneʔ());
    List<Integer> ints = new ArrayList<Integer>();
    re = newRubyEnumerator(ints);
    assertFalse(re.oneʔ());
  }

  @Test
  public void testOneʔWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertTrue(re.oneʔ(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item > 3;
      }

    }));
    assertFalse(re.oneʔ(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item > 2;
      }

    }));
    List<Integer> ints = new ArrayList<Integer>();
    re = newRubyEnumerator(ints);
    assertFalse(re.oneʔ(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item > 6;
      }

    }));
  }

  @Test
  public void testPartition() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.partition().getClass());
    assertEquals(ra(1, 2, 3, 4), re.partition().toA());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testPartitionWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(ra(ra(1, 3), ra(2, 4)),
        re.partition(new BooleanBlock<Integer>() {

          @Override
          public boolean yield(Integer item) {
            return item % 2 == 1;
          }

        }));
  }

  @Test
  public void testReduceWithInit() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    RubyArray<Integer> ra = ra();
    assertEquals(ra(1, 2, 3, 4), re.reduce(ra, "push"));
  }

  @Test
  public void testReduceWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(Integer.valueOf(10), re.reduce(new ReduceBlock<Integer>() {

      @Override
      public Integer yield(Integer memo, Integer item) {
        return memo + item;
      }

    }));
  }

  @Test
  public void testReduceWithInitAndBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(Long.valueOf(20),
        re.reduce(Long.valueOf(10), new WithInitBlock<Integer, Long>() {

          @Override
          public Long yield(Long memo, Integer item) {
            return memo + item;
          }

        }));
  }

  @Test
  public void testReduce() {
    RubyEnumerable<Boolean> bools =
        newRubyEnumerator(Arrays.asList(true, true, true));
    assertEquals(Boolean.TRUE, bools.reduce("equals"));
  }

  @Test
  public void testReject() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.reject().getClass());
    assertEquals(ra(1, 2, 3, 4), re.reject().toA());
  }

  @Test
  public void testRejectWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(ra(2, 3, 4), re.reject(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item == 1;
      }

    }));
  }

  @Test
  public void testReverseEach() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.reverseEach().getClass());
    assertEquals(ra(4, 3, 2, 1), re.reverseEach().toA());
    assertEquals(ra(1, 2, 3, 4).toA(), re.toA());
  }

  @Test
  public void testReverseEachWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    final RubyArray<Integer> ra = ra();
    assertEquals(re, re.reverseEach(new Block<Integer>() {

      @Override
      public void yield(Integer item) {
        ra.push(item);
      }

    }));
    assertEquals(ra(4, 3, 2, 1), ra);
    assertEquals(ra(1, 2, 3, 4), re.toA());
  }

  @Test
  public void testSelect() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.select().getClass());
    assertEquals(ra(1, 2, 3, 4), re.select().toA());
  }

  @Test
  public void testSelectBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(ra(3, 4), re.select(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item >= 3;
      }

    }));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSliceBeforeWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 3, 3, 4));
    assertEquals(ra(ra(1), ra(3), ra(3, 4)),
        re.sliceBefore(new BooleanBlock<Integer>() {

          @Override
          public boolean yield(Integer item) {
            return item % 2 == 1;
          }

        }).toA());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSliceBeforeWithRegex() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 3));
    assertEquals(ra(ra(1, 2), ra(3), ra(3)), re.sliceBefore("3").toA());
  }

  @Test
  public void testSort() {
    re = newRubyEnumerator(Arrays.asList(4, 1, 2, 3, 3));
    assertEquals(ra(1, 2, 3, 3, 4), re.sort());
    assertEquals(ra("abc", "b", "cd"),
        newRubyEnumerator(Arrays.asList("b", "cd", "abc")).sort());
  }

  @Test
  public void testSortWithComparator() {
    re = newRubyEnumerator(Arrays.asList(4, 1, 2, 3, 3));
    assertEquals(ra(4, 3, 3, 2, 1), re.sort(new Comparator<Integer>() {

      @Override
      public int compare(Integer o1, Integer o2) {
        return o2 - o1;
      }

    }));
    assertEquals(ra(1),
        newRubyEnumerator(Arrays.asList(1)).sort(new Comparator<Integer>() {

          @Override
          public int compare(Integer o1, Integer o2) {
            return o2 - o1;
          }

        }));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSortException() {
    assertEquals(
        ra(new Point(1, 2), new Point(3, 4), new Point(5, 6)),
        newRubyEnumerator(
            Arrays.asList(new Point(5, 6), new Point(1, 2), new Point(3, 4)))
            .sort());
  }

  @Test
  public void testSortBy() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.sortBy().getClass());
    assertEquals(ra(1, 2, 3, 4), re.sortBy().toA());
  }

  @Test
  public void testSortByWithComparatorAndBlock() {
    RubyEnumerable<String> re =
        newRubyEnumerator(Arrays.asList("aaaa", "bbb", "cc", "d"));
    assertEquals(ra("aaaa", "bbb", "cc", "d"),
        re.sortBy(new Comparator<Integer>() {

          @Override
          public int compare(Integer o1, Integer o2) {
            return o2 - o1;
          }
        }, new TransformBlock<String, Integer>() {

          @Override
          public Integer yield(String item) {
            return item.length();
          }

        }));
  }

  @Test
  public void testSortByWithBlock() {
    RubyEnumerable<String> re =
        newRubyEnumerator(Arrays.asList("aaaa", "bbb", "cc", "d"));
    assertEquals(ra("d", "cc", "bbb", "aaaa"),
        re.sortBy(new TransformBlock<String, Integer>() {

          @Override
          public Integer yield(String item) {
            return item.length();
          }

        }));
  }

  @Test
  public void testTake() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(ra(), re.take(0));
    assertEquals(ra(1, 2), re.take(2));
    assertEquals(ra(1, 2, 3, 4), re.take(5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTakeException() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    re.take(-1);
  }

  @Test
  public void testTakeWhile() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(ra(1), re.takeWhile().toA());
  }

  @Test
  public void testTakeWhileWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(ra(1, 2), re.takeWhile(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item != 3;
      }

    }));
    assertEquals(ra(1, 2, 3, 4), re.takeWhile(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item < 5;
      }

    }));
  }

  @Test
  public void testToA() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(ra(1, 2, 3, 4), re.toA());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testZip() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3));
    assertEquals(ra(ra(1, 4), ra(2, 5), ra(3, null)), re.zip(ra(4, 5)));
    assertEquals(ra(ra(1, 4, 7), ra(2, 5, 8), ra(3, 6, 9)),
        re.zip(ra(4, 5, 6), ra(7, 8, 9)));
    assertEquals(ra(ra(1, 4, 7), ra(2, 5, 8), ra(3, 6, 9)),
        re.zip(ra(ra(4, 5, 6), ra(7, 8, 9))));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testZipWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3));
    final RubyArray<Integer> ints = ra();
    re.zip(ra(ra(4, 5, 6), ra(7, 8, 9)), new Block<RubyArray<Integer>>() {

      @Override
      public void yield(RubyArray<Integer> item) {
        ints.push(item.reduce(new ReduceBlock<Integer>() {

          @Override
          public Integer yield(Integer memo, Integer item) {
            return memo + item;
          }

        }));
      }

    });
    assertEquals(ra(12, 15, 18), ints);
  }

  @Test
  public void testIterator() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertTrue(re.iterator() instanceof Iterator);
  }

  @Test
  public void testToString() {
    re = new RubyEnumerable<Integer>() {

      @Override
      protected Iterable<Integer> getIterable() {
        return Arrays.asList(1, 2, 3, 4);
      }
    };
    assertEquals("RubyEnumerable{[1, 2, 3, 4]}", re.toString());
  }

}
