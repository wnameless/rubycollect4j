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
package cleanzephyr.rubycollect4j;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;

import cleanzephyr.rubycollect4j.block.BooleanBlock;
import cleanzephyr.rubycollect4j.block.InjectBlock;
import cleanzephyr.rubycollect4j.block.InjectWithInitBlock;
import cleanzephyr.rubycollect4j.block.ItemBlock;
import cleanzephyr.rubycollect4j.block.ItemToRubyArrayBlock;
import cleanzephyr.rubycollect4j.block.ItemTransformBlock;
import cleanzephyr.rubycollect4j.block.ItemWithIndexBlock;
import cleanzephyr.rubycollect4j.block.ItemWithObjectBlock;
import cleanzephyr.rubycollect4j.block.ListBlock;

import static cleanzephyr.rebycollect4j.util.Pair.newPair;
import static cleanzephyr.rubycollect4j.RubyArray.newRubyArray;
import static cleanzephyr.rubycollect4j.RubyCollections.ra;
import static cleanzephyr.rubycollect4j.RubyCollections.rh;
import static cleanzephyr.rubycollect4j.RubyEnumerable.newRubyEnumerable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RubyEnumerableTest {
  private RubyEnumerable<Integer> re;

  @Test
  public void testConstructor() {
    re = newRubyEnumerable(Arrays.asList(1, 2));
    assertEquals(RubyEnumerable.class, re.getClass());
    re = newRubyEnumerable(new Integer[] { 1 });
    assertEquals(RubyEnumerable.class, re.getClass());
    re = newRubyEnumerable(1, 2, 3);
    assertEquals(RubyEnumerable.class, re.getClass());
  }

  @Test
  public void testAllʔ() {
    re = newRubyEnumerable(1, 2);
    assertEquals(true, re.allʔ());
    re = newRubyEnumerable(new Integer[0]);
    assertEquals(true, re.allʔ());
    re = newRubyEnumerable(1, 2, null);
    assertEquals(false, re.allʔ());
  }

  @Test
  public void testAllʔWithBlock() {
    re = newRubyEnumerable(1, 2, 3);
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
    re = newRubyEnumerable(1, 2);
    assertEquals(true, re.anyʔ());
    re = newRubyEnumerable(new Integer[0]);
    assertEquals(false, re.anyʔ());
    re = newRubyEnumerable(1, 2, null);
    assertEquals(true, re.anyʔ());
  }

  @Test
  public void testAnyʔWithBlock() {
    re = newRubyEnumerable(1, 2, 3);
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
    re = newRubyEnumerable(1, 2, 2, 3);
    RubyArray<Entry<Boolean, RubyArray<Integer>>> chunk =
        re.chunk(new ItemTransformBlock<Integer, Boolean>() {

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
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.collect().getClass());
    assertEquals(ra(1, 2, 3, 4), re.collect().toA());
  }

  @Test
  public void testCollectWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
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
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.collectConcat().getClass());
    assertEquals(ra(1, 2, 3, 4), re.collectConcat().toA());
  }

  @Test
  public void testCollectConcatWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
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
    re = newRubyEnumerable();
    assertEquals(0, re.count());
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(4, re.count());
  }

  @Test
  public void testCountWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(2, re.count(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item % 2 == 1;
      }

    }));
  }

  @Test
  public void testCycle() {
    re = newRubyEnumerable(1, 2, 3, 4);
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
    re = newRubyEnumerable(1, 2, 3, 4);
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
    re = newRubyEnumerable(1, 2, 3, 4);
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
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.detect().getClass());
    assertEquals(ra(1, 2, 3, 4), re.detect().toA());
  }

  @Test
  public void testDetectWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
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
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(ra(3, 4), re.drop(2));
    re.drop(-1);
  }

  @Test
  public void testDropWhile() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.dropWhile().getClass());
    assertEquals(ra(1), re.dropWhile().toA());
  }

  @Test
  public void testDropWhileWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(ra(3, 4), re.dropWhile(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item < 3;
      }

    }));
  }

  @Test
  public void testEachCons() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.eachCons(2).getClass());
    @SuppressWarnings("unchecked")
    RubyArray<RubyArray<Integer>> ra = ra(ra(1, 2), ra(2, 3), ra(3, 4));
    assertEquals(ra, re.eachCons(2).toA());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEachConsException() {
    re = newRubyEnumerable(1, 2, 3, 4);
    re.eachCons(0);
  }

  @Test
  public void testEachConsWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
    final RubyArray<List<Integer>> ra = ra();
    re.eachCons(2, new ListBlock<Integer>() {

      @Override
      public void yield(List<Integer> item) {
        ra.push(item);
      }

    });
    assertEquals(ra, re.eachCons(2).toA());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEachConsWithBlockException() {
    re = newRubyEnumerable(1, 2, 3, 4);
    re.eachCons(0, new ListBlock<Integer>() {

      @Override
      public void yield(List<Integer> item) {}

    });
  }

  @Test
  public void testEachEntry() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.eachEntry().getClass());
    assertEquals(ra(1, 2, 3, 4), re.eachEntry().toA());
  }

  @Test
  public void testEachEntryWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
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

  @Test
  public void testEachSlice() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.eachSlice(3).getClass());
    @SuppressWarnings("unchecked")
    RubyArray<RubyArray<Integer>> ra = ra(ra(1, 2, 3), ra(4));
    assertEquals(ra, re.eachSlice(3).toA());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEachSliceException() {
    re = newRubyEnumerable(1, 2, 3, 4);
    re.eachSlice(0);
  }

  @Test
  public void testEachSliceWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
    final RubyArray<Integer> ints = ra();
    re.eachSlice(3, new ListBlock<Integer>() {

      @Override
      public void yield(List<Integer> item) {
        ints.add(item.get(0));
      }

    });
    assertEquals(ra(1, 4), ints);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testEachSliceWithBlockException() {
    re = newRubyEnumerable(1, 2, 3, 4);
    re.eachSlice(0, new ListBlock<Integer>() {

      @Override
      public void yield(List<Integer> item) {}

    });
  }

  @Test
  public void testEachWithIndex() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.eachWithIndex().getClass());
    @SuppressWarnings("unchecked")
    RubyArray<? extends Entry<Integer, Integer>> ra =
        ra(newPair(1, 0), newPair(2, 1), newPair(3, 2), newPair(4, 3));
    assertEquals(ra, re.eachWithIndex().toA());
  }

  @Test
  public void testEachWithIndexWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
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
    re = newRubyEnumerable(1, 2, 3, 4);
    Long obj = 0L;
    assertEquals(RubyEnumerator.class, re.eachWithObject(obj).getClass());
    @SuppressWarnings("unchecked")
    RubyArray<? extends Entry<Integer, Long>> ra =
        ra(newPair(1, obj), newPair(2, obj), newPair(3, obj), newPair(4, obj));
    assertEquals(ra, re.eachWithObject(obj).toA());
  }

  @Test
  public void testEachWithObjectWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
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
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3, 4), re.entries());
  }

  @Test
  public void testFind() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.find().getClass());
    assertEquals(ra(1, 2, 3, 4), re.find().toA());
  }

  @Test
  public void testFindWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
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
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.findAll().getClass());
    assertEquals(ra(1, 2, 3, 4), re.findAll().toA());
  }

  @Test
  public void testFindAllWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(ra(2, 3, 4), re.findAll(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item >= 2;
      }

    }));
  }

  @Test
  public void testFindIndex() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.findIndex().getClass());
    assertEquals(ra(1, 2, 3, 4), re.findIndex().toA());
  }

  @Test
  public void testFindIndexWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
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
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(Integer.valueOf(2), re.findIndex(3));
    assertNull(re.findIndex(0));
  }

  @Test
  public void testFirst() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(Integer.valueOf(1), re.first());
    re = newRubyEnumerable();
    assertNull(re.first());
  }

  @Test
  public void testFirstWithN() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3), re.first(3));
    assertEquals(ra(1, 2, 3, 4), re.first(6));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFirstWithNException() {
    re = newRubyEnumerable(1, 2, 3, 4);
    re.first(-1);
  }

  @Test
  public void testFlatMap() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.flatMap().getClass());
    assertEquals(ra(1, 2, 3, 4), re.flatMap().toA());
  }

  @Test
  public void testFlatMapWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(ra(1L, 2L, 3L, 4L),
        re.flatMap(new ItemToRubyArrayBlock<Integer, Long>() {

          @Override
          public RubyArray<Long> yield(Integer item) {
            return ra(Long.valueOf(item));
          }

        }));
  }

  @Test
  public void testGrep() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(ra(2, 4), re.grep("[24]"));
  }

  @Test
  public void testGrepWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(ra("2", "4"),
        re.grep("[24]", new ItemTransformBlock<Integer, String>() {

          @Override
          public String yield(Integer item) {
            return item.toString();
          }

        }));
  }

  @Test
  public void testGroupBy() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.groupBy().getClass());
    assertEquals(ra(1, 2, 3, 4), re.groupBy().toA());
  }

  @Test
  public void testGroupByWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(rh(1, ra(1, 4), 2, ra(2), 0, ra(3)),
        re.groupBy(new ItemTransformBlock<Integer, Integer>() {

          @Override
          public Integer yield(Integer item) {
            return item % 3;
          }

        }));
  }

  @Test
  public void testIncludeʔ() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertTrue(re.includeʔ(1));
    assertFalse(re.includeʔ(5));
  }

  @Test
  public void testInjectWithInit() {
    re = newRubyEnumerable(1, 2, 3, 4);
    RubyArray<Integer> ra = ra();
    assertEquals(ra(1, 2, 3, 4), re.inject(ra, "push"));
  }

  @Test
  public void testInjectWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(Integer.valueOf(10), re.inject(new InjectBlock<Integer>() {

      @Override
      public Integer yield(Integer memo, Integer item) {
        return memo + item;
      }

    }));
  }

  @Test
  public void testInjectWithInitAndBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(Long.valueOf(20),
        re.inject(Long.valueOf(10), new InjectWithInitBlock<Integer, Long>() {

          @Override
          public Long yield(Long memo, Integer item) {
            return memo + item;
          }

        }));
  }

  @Test
  public void testInject() {
    RubyEnumerable<Boolean> bools = newRubyEnumerable(true, true, true);
    assertEquals(Boolean.TRUE, bools.inject("equals"));
  }

  @Test
  public void testMap() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.map().getClass());
    assertEquals(ra(1, 2, 3, 4), re.map().toA());
  }

  @Test
  public void testMapWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(ra(1L, 2L, 3L, 4L),
        re.map(new ItemTransformBlock<Integer, Long>() {

          @Override
          public Long yield(Integer item) {
            return Long.valueOf(item);
          }

        }));
  }

  @Test
  public void testMax() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(Integer.valueOf(4), re.max());
    re = newRubyEnumerable();
    assertNull(re.max());
  }

  @Test
  public void testMaxWithComparator() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(Integer.valueOf(1), re.max(new Comparator<Integer>() {

      @Override
      public int compare(Integer arg0, Integer arg1) {
        return arg1 - arg0;
      }

    }));
  }

  @Test
  public void testMaxBy() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.maxBy().getClass());
    assertEquals(ra(1, 2, 3, 4), re.maxBy().toA());
  }

  @Test
  public void testMaxByWithComparatorAndBlock() {
    RubyEnumerable<String> re = newRubyEnumerable("aaaa", "bbb", "cc", "d");
    assertEquals("d", re.maxBy(new Comparator<Integer>() {

      @Override
      public int compare(Integer o1, Integer o2) {
        return o2 - o1;
      }
    }, new ItemTransformBlock<String, Integer>() {

      @Override
      public Integer yield(String item) {
        return item.length();
      }

    }));
  }

  @Test
  public void testMaxByWithBlock() {
    RubyEnumerable<String> re = newRubyEnumerable("aaaa", "bbb", "cc", "d");
    assertEquals("aaaa", re.maxBy(new ItemTransformBlock<String, Integer>() {

      @Override
      public Integer yield(String item) {
        return item.length();
      }

    }));
  }

  @Test
  public void testMemberʔ() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertTrue(re.memberʔ(1));
    assertFalse(re.memberʔ(5));
  }

  @Test
  public void testMin() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(Integer.valueOf(1), re.min());
    re = newRubyEnumerable();
    assertNull(re.min());
  }

  @Test
  public void testMinWithComparator() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(Integer.valueOf(4), re.min(new Comparator<Integer>() {

      @Override
      public int compare(Integer arg0, Integer arg1) {
        return arg1 - arg0;
      }

    }));
  }

  @Test
  public void testMinBy() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.minBy().getClass());
    assertEquals(ra(1, 2, 3, 4), re.minBy().toA());
  }

  @Test
  public void testMinByWithComparatorAndBlock() {
    RubyEnumerable<String> re = newRubyEnumerable("aaaa", "bbb", "cc", "d");
    assertEquals("aaaa", re.minBy(new Comparator<Integer>() {

      @Override
      public int compare(Integer o1, Integer o2) {
        return o2 - o1;
      }
    }, new ItemTransformBlock<String, Integer>() {

      @Override
      public Integer yield(String item) {
        return item.length();
      }

    }));
  }

  @Test
  public void testMinByWithBlock() {
    RubyEnumerable<String> re = newRubyEnumerable("aaaa", "bbb", "cc", "d");
    assertEquals("d", re.minBy(new ItemTransformBlock<String, Integer>() {

      @Override
      public Integer yield(String item) {
        return item.length();
      }

    }));
  }

  @Test
  public void testMinmax() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(ra(1, 4), re.minmax());
    re = newRubyEnumerable(1);
    assertEquals(ra(1, 1), re.minmax());
    re = newRubyEnumerable();
    assertEquals(ra(null, null), re.minmax());
  }

  @Test
  public void testMinmaxWithComparator() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(ra(4, 1), re.minmax(new Comparator<Integer>() {

      @Override
      public int compare(Integer o1, Integer o2) {
        return o2 - o1;
      }

    }));
  }

  @Test
  public void testMinmaxBy() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.minmaxBy().getClass());
    assertEquals(ra(1, 2, 3, 4), re.minmaxBy().toA());
  }

  @Test
  public void testMinmaxByWithComparatorAndBlock() {
    RubyEnumerable<String> re = newRubyEnumerable("aaaa", "bbb", "cc", "d");
    assertEquals(ra("aaaa", "d"), re.minmaxBy(new Comparator<Integer>() {

      @Override
      public int compare(Integer o1, Integer o2) {
        return o2 - o1;
      }
    }, new ItemTransformBlock<String, Integer>() {

      @Override
      public Integer yield(String item) {
        return item.length();
      }

    }));
  }

  @Test
  public void testMinmaxByWithBlock() {
    RubyEnumerable<String> re = newRubyEnumerable("aaaa", "bbb", "cc", "d");
    assertEquals(ra("d", "aaaa"),
        re.minmaxBy(new ItemTransformBlock<String, Integer>() {

          @Override
          public Integer yield(String item) {
            return item.length();
          }

        }));
  }

  @Test
  public void testNoneʔ() {
    re = newRubyEnumerable();
    assertTrue(re.noneʔ());
    RubyArray<Integer> ra = ra();
    ra.push(null);
    re = newRubyEnumerable(ra);
    assertTrue(re.noneʔ());
    re = newRubyEnumerable(1, 2, 3, 4);
    assertFalse(re.noneʔ());
    re = newRubyEnumerable(1, 2, 3, null);
    assertFalse(re.noneʔ());
  }

  @Test
  public void testNoneʔWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
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
    re = newRubyEnumerable(1);
    assertTrue(re.oneʔ());
    re = newRubyEnumerable(1, null);
    assertTrue(re.oneʔ());
    re = newRubyEnumerable(1, 2);
    assertFalse(re.oneʔ());
  }

  @Test
  public void testOneʔWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
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
  }

  @Test
  public void testPartition() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.partition().getClass());
    assertEquals(ra(1, 2, 3, 4), re.partition().toA());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testPartitionWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
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
    re = newRubyEnumerable(1, 2, 3, 4);
    RubyArray<Integer> ra = ra();
    assertEquals(ra(1, 2, 3, 4), re.reduce(ra, "push"));
  }

  @Test
  public void testReduceWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(Integer.valueOf(10), re.reduce(new InjectBlock<Integer>() {

      @Override
      public Integer yield(Integer memo, Integer item) {
        return memo + item;
      }

    }));
  }

  @Test
  public void testReduceWithInitAndBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(Long.valueOf(20),
        re.reduce(Long.valueOf(10), new InjectWithInitBlock<Integer, Long>() {

          @Override
          public Long yield(Long memo, Integer item) {
            return memo + item;
          }

        }));
  }

  @Test
  public void testReduce() {
    RubyEnumerable<Boolean> bools = newRubyEnumerable(true, true, true);
    assertEquals(Boolean.TRUE, bools.reduce("equals"));
  }

  @Test
  public void testReject() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.reject().getClass());
    assertEquals(ra(1, 2, 3, 4), re.reject().toA());
  }

  @Test
  public void testRejectWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(ra(2, 3, 4), re.reject(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item == 1;
      }

    }));
  }

  @Test
  public void testReverseEach() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.reverseEach().getClass());
    assertEquals(ra(4, 3, 2, 1), re.reverseEach().toA());
  }

  @Test
  public void testReverseEachWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
    final RubyArray<Integer> ra = ra();
    assertEquals(re, re.reverseEach(new ItemBlock<Integer>() {

      @Override
      public void yield(Integer item) {
        ra.push(item);
      }

    }));
    assertEquals(ra(4, 3, 2, 1), ra);
  }

  @Test
  public void testSelect() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.select().getClass());
    assertEquals(ra(1, 2, 3, 4), re.select().toA());
  }

  @Test
  public void testSelectBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
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
    re = newRubyEnumerable(1, 3, 3, 4);
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
    re = newRubyEnumerable(1, 2, 3, 3);
    assertEquals(ra(ra(1, 2), ra(3), ra(3)), re.sliceBefore("3").toA());
  }

  @Test
  public void testSort() {
    re = newRubyEnumerable(4, 1, 2, 3, 3);
    assertEquals(ra(1, 2, 3, 3, 4), re.sort());
    assertEquals(ra("abc", "b", "cd"), newRubyEnumerable("b", "cd", "abc")
        .sort());
  }

  @Test
  public void testSortBy() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, re.sortBy().getClass());
    assertEquals(ra(1, 2, 3, 4), re.sortBy().toA());
  }

  @Test
  public void testSortByWithComparatorAndBlock() {
    RubyEnumerable<String> re = newRubyEnumerable("aaaa", "bbb", "cc", "d");
    assertEquals(ra("aaaa", "bbb", "cc", "d"),
        re.sortBy(new Comparator<Integer>() {

          @Override
          public int compare(Integer o1, Integer o2) {
            return o2 - o1;
          }
        }, new ItemTransformBlock<String, Integer>() {

          @Override
          public Integer yield(String item) {
            return item.length();
          }

        }));
  }

  @Test
  public void testSortByWithBlock() {
    RubyEnumerable<String> re = newRubyEnumerable("aaaa", "bbb", "cc", "d");
    assertEquals(ra("d", "cc", "bbb", "aaaa"),
        re.sortBy(new ItemTransformBlock<String, Integer>() {

          @Override
          public Integer yield(String item) {
            return item.length();
          }

        }));
  }

  @Test
  public void testTake() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(ra(1, 2), re.take(2));
    assertEquals(ra(1, 2, 3, 4), re.take(5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTakeException() {
    re = newRubyEnumerable(1, 2, 3, 4);
    re.take(-1);
  }

  @Test
  public void testTakeWhile() {
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(ra(1), re.takeWhile().toA());
  }

  @Test
  public void testTakeWhileWithBlock() {
    re = newRubyEnumerable(1, 2, 3, 4);
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
    re = newRubyEnumerable(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3, 4), re.toA());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testZip() {
    re = newRubyEnumerable(1, 2, 3);
    assertEquals(ra(ra(1, 4), ra(2, 5), ra(3, null)), re.zip(ra(4, 5)));
    assertEquals(ra(ra(1, 4, 7), ra(2, 5, 8), ra(3, 6, 9)),
        re.zip(ra(4, 5, 6), ra(7, 8, 9)));
    assertEquals(ra(ra(1, 4, 7), ra(2, 5, 8), ra(3, 6, 9)),
        re.zip(ra(ra(4, 5, 6), ra(7, 8, 9))));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testZipWithBlock() {
    re = newRubyEnumerable(1, 2, 3);
    final RubyArray<Integer> ints = ra();
    re.zip(ra(ra(4, 5, 6), ra(7, 8, 9)), new ItemBlock<RubyArray<Integer>>() {

      @Override
      public void yield(RubyArray<Integer> item) {
        ints.push(item.reduce(new InjectBlock<Integer>() {

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
    re = newRubyEnumerable(1, 2, 3, 4);
    assertTrue(re.iterator() instanceof Iterator);
  }

}
