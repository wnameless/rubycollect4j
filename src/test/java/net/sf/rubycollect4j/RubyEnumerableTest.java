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

import static net.sf.rubycollect4j.RubyCollections.hp;
import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.newRubyEnumerator;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.rh;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

import org.junit.Before;
import org.junit.Test;

public class RubyEnumerableTest {

  RubyEnumerable<Integer> re;
  Iterable<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter = newRubyArray(1, 2, 3, 4);
    re = new RubyEnumerable<Integer>() {

      @Override
      protected Iterable<Integer> getIterable() {
        return iter;
      }

    };
  }

  @Test
  public void testOf() {
    re = RubyEnumerable.of(iter);
    Iterator<Integer> it = iter.iterator();
    it.next();
    it.remove();
    assertEquals(ra(2, 3, 4), re.toA());
  }

  @Test(expected = NullPointerException.class)
  public void testOfException() {
    RubyEnumerable.of(null);
  }

  @Test
  public void testCopyOf() {
    re = RubyEnumerable.copyOf(iter);
    Iterator<Integer> it = iter.iterator();
    it.next();
    it.remove();
    assertEquals(ra(1, 2, 3, 4), re.toA());
  }

  @Test(expected = NullPointerException.class)
  public void testCopyOfException() {
    RubyEnumerable.copyOf(null);
  }

  @Test
  public void testInstantiation() {
    assertTrue(re instanceof RubyEnumerable);
  }

  @Test
  public void testInterface() {
    assertTrue(re instanceof Ruby.Enumerable);
  }

  @Test
  public void testGetIterable() {
    assertSame(iter, re.getIterable());
  }

  @Test
  public void testAllʔ() {
    assertTrue(re.allʔ());
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertTrue(re.allʔ());
    re = newRubyEnumerator(Arrays.asList(1, 2, null));
    assertFalse(re.allʔ());
  }

  @Test
  public void testAllʔWithBlock() {
    BooleanBlock<Integer> block = new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item % 2 == 0;
      }

    };
    assertFalse(re.allʔ(block));
    re = ra(2, 4, 6, 8);
    assertTrue(re.allʔ(block));
  }

  @Test
  public void testAnyʔ() {
    assertTrue(re.anyʔ());
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertFalse(re.anyʔ());
    re = newRubyEnumerator(Arrays.asList(1, 2, null));
    assertTrue(re.anyʔ());
    List<Integer> ints = Arrays.asList(null, null);
    re = newRubyEnumerator(ints);
    assertFalse(re.anyʔ());
  }

  @Test
  public void testAnyʔWithBlock() {
    BooleanBlock<Integer> block = new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item % 2 == 0;
      }

    };
    assertTrue(re.anyʔ(block));
    re = ra(1, 3, 5, 7);
    assertFalse(re.anyʔ(block));
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
    assertEquals(hp(false, newRubyArray(1)).toString(), chunk.get(0).toString());
    assertEquals(hp(true, newRubyArray(2, 2)).toString(), chunk.get(1)
        .toString());
    assertEquals(hp(false, newRubyArray(3)).toString(), chunk.get(2).toString());
    assertEquals(3, chunk.size());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testChunkWithMethodName() {
    RubyEnumerator<String> re =
        newRubyEnumerator(Arrays.asList("aa", "bb", "bc", "cd"));
    RubyEnumerator<Entry<Character, RubyArray<String>>> chunk =
        re.chunk("charAt", 0);
    assertEquals(
        ra(hp('a', ra("aa")), hp('b', ra("bb", "bc")), hp('c', ra("cd"))),
        chunk.toA());
  }

  @Test
  public void testCollect() {
    assertEquals(RubyEnumerator.class, re.collect().getClass());
    assertEquals(ra(1, 2, 3, 4), re.collect().toA());
  }

  @Test
  public void testCollectWithBlock() {
    assertEquals(ra(1.0, 2.0, 3.0, 4.0),
        re.collect(new TransformBlock<Integer, Double>() {

          @Override
          public Double yield(Integer item) {
            return Double.valueOf(item);
          }

        }));
  }

  @Test
  public void testCollectWithMethodName() {
    assertEquals(ra("a", "b", "c"), ra(" a ", " b ", " c ").collect("trim"));
  }

  @Test
  public void testCollectConcat() {
    assertEquals(RubyEnumerator.class, re.collectConcat().getClass());
    assertEquals(ra(1, 2, 3, 4), re.collectConcat().toA());
  }

  @Test
  public void testCollectConcatWithBlock() {
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
    assertEquals(4, re.count());
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertEquals(0, re.count());
  }

  @Test
  public void testCountWithBlock() {
    assertEquals(2, re.count(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item % 2 == 1;
      }

    }));
  }

  @Test
  public void testCycle() {
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
    final RubyArray<Integer> ints = newRubyArray();
    re.cycle(new Block<Integer>() {

      @Override
      public void yield(Integer item) {
        ints.push(item);
        if (ints.size() > 1000)
          throw new IllegalStateException();
      }

    });
  }

  @Test
  public void testDetect() {
    assertEquals(RubyEnumerator.class, re.detect().getClass());
    assertEquals(ra(1, 2, 3, 4), re.detect().toA());
  }

  @Test
  public void testDetectWithBlock() {
    BooleanBlock<Integer> block = new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item == 3;
      }

    };
    assertEquals(Integer.valueOf(3), re.detect(block));
    re = ra(1, 2, 4, 5);
    assertNull(re.detect(block));
  }

  @Test
  public void testDrop() {
    assertEquals(ra(1, 2, 3, 4), re.drop(0));
    assertEquals(ra(3, 4), re.drop(2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDropException() {
    re.drop(-1);
  }

  @Test
  public void testDropWhile() {
    assertEquals(RubyEnumerator.class, re.dropWhile().getClass());
    assertEquals(ra(1), re.dropWhile().toA());
    List<Integer> ints = new ArrayList<Integer>();
    re = newRubyEnumerator(ints);
    assertEquals(ra(), re.dropWhile().toA());
  }

  @Test
  public void testDropWhileWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 1));
    assertEquals(ra(3, 1), re.dropWhile(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item < 3;
      }

    }));
  }

  @Test
  public void testEach() {
    assertTrue(re.each() instanceof RubyEnumerator);
    assertEquals(re.toA(), re.each().toA());
  }

  @Test
  public void testEachWithBlock() {
    final RubyArray<Integer> ints = ra();
    assertEquals(re.toA(), re.each(new Block<Integer>() {

      @Override
      public void yield(Integer item) {
        ints.push(item * 2);
      }

    }).toA());
    assertEquals(ra(2, 4, 6, 8), ints);
  }

  @Test
  public void testEachCons() {
    assertEquals(RubyEnumerator.class, re.eachCons(2).getClass());
    @SuppressWarnings("unchecked")
    RubyArray<RubyArray<Integer>> ra = ra(ra(1, 2), ra(2, 3), ra(3, 4));
    assertEquals(ra, re.eachCons(2).toA());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEachConsException() {
    re.eachCons(0);
  }

  @Test
  public void testEachConsWithBlock() {
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
    re.eachCons(0, null);
  }

  @Test
  public void testEachEntry() {
    assertEquals(RubyEnumerator.class, re.eachEntry().getClass());
    assertEquals(ra(1, 2, 3, 4), re.eachEntry().toA());
  }

  @Test
  public void testEachEntryWithBlock() {
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
    assertEquals(RubyEnumerator.class, re.eachSlice(3).getClass());
    @SuppressWarnings("unchecked")
    RubyArray<RubyArray<Integer>> ra = ra(ra(1, 2, 3), ra(4));
    assertEquals(ra, re.eachSlice(3).toA());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEachSliceException() {
    re.eachSlice(0);
  }

  @Test
  public void testEachSliceWithBlock() {
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
    re.eachSlice(0, null);
  }

  @Test
  public void testEachWithIndex() {
    assertEquals(RubyEnumerator.class, re.eachWithIndex().getClass());
    @SuppressWarnings("unchecked")
    RubyArray<? extends Entry<Integer, Integer>> ra =
        ra(hp(1, 0), hp(2, 1), hp(3, 2), hp(4, 3));
    assertEquals(ra, re.eachWithIndex().toA());
  }

  @Test
  public void testEachWithIndexWithBlock() {
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
    Long obj = 0L;
    assertEquals(RubyEnumerator.class, re.eachWithObject(obj).getClass());
    @SuppressWarnings("unchecked")
    RubyArray<? extends Entry<Integer, Long>> ra =
        ra(hp(1, obj), hp(2, obj), hp(3, obj), hp(4, obj));
    assertEquals(ra, re.eachWithObject(obj).toA());
  }

  @Test
  public void testEachWithObjectWithBlock() {
    Long[] obj = new Long[] { 0L };
    assertEquals(new Long[] { 10L }[0],
        re.eachWithObject(obj, new WithObjectBlock<Integer, Long[]>() {

          @Override
          public void yield(Integer item, Long[] obj) {
            obj[0] += item;
          }

        })[0]);
  }

  @Test
  public void testEntries() {
    assertEquals(ra(1, 2, 3, 4), re.entries());
  }

  @Test
  public void testFind() {
    assertEquals(RubyEnumerator.class, re.find().getClass());
    assertEquals(ra(1, 2, 3, 4), re.find().toA());
  }

  @Test
  public void testFindWithBlock() {
    BooleanBlock<Integer> block = new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item == 3;
      }

    };
    assertEquals(Integer.valueOf(3), re.find(block));
    re = ra(1, 2, 4, 5);
    assertNull(re.find(block));
  }

  @Test
  public void testFindAll() {
    assertEquals(RubyEnumerator.class, re.findAll().getClass());
    assertEquals(ra(1, 2, 3, 4), re.findAll().toA());
  }

  @Test
  public void testFindAllWithBlock() {
    assertEquals(ra(2, 3, 4), re.findAll(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item >= 2;
      }

    }));
  }

  @Test
  public void testFindIndex() {
    assertEquals(RubyEnumerator.class, re.findIndex().getClass());
    assertEquals(ra(1, 2, 3, 4), re.findIndex().toA());
  }

  @Test
  public void testFindIndexWithBlock() {
    BooleanBlock<Integer> block = new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item >= 4;
      }

    };
    assertEquals(Integer.valueOf(3), re.findIndex(block));
    re = ra(0, 1, 2, 3);
    assertNull(re.findIndex(block));
  }

  @Test
  public void testFindIndexWithTarget() {
    assertEquals(Integer.valueOf(2), re.findIndex(3));
    assertNull(re.findIndex(0));
    re = newRubyEnumerator(Arrays.asList(1, null, 3));
    assertEquals(Integer.valueOf(1), re.findIndex((Integer) null));
  }

  @Test
  public void testFirst() {
    assertEquals(Integer.valueOf(1), re.first());
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertNull(re.first());
  }

  @Test
  public void testFirstWithN() {
    assertEquals(ra(), re.first(0));
    assertEquals(ra(1, 2, 3), re.first(3));
    assertEquals(ra(1, 2, 3, 4), re.first(6));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFirstWithNException() {
    re.first(-1);
  }

  @Test
  public void testFlatMap() {
    assertEquals(RubyEnumerator.class, re.flatMap().getClass());
    assertEquals(ra(1, 2, 3, 4), re.flatMap().toA());
  }

  @Test
  public void testFlatMapWithBlock() {
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
    assertEquals(ra(2, 4), re.grep("[24]"));
  }

  @Test
  public void testGrepWithBlock() {
    assertEquals(ra("2", "4"),
        re.grep("[24]", new TransformBlock<Integer, String>() {

          @Override
          public String yield(Integer item) {
            return item.toString();
          }

        }));
  }

  @Test
  public void testGrepWithMethodName() {
    assertEquals(ra("2", "4"), re.grep("[24]", "toString"));
  }

  @Test
  public void testGroupBy() {
    assertEquals(RubyEnumerator.class, re.groupBy().getClass());
    assertEquals(ra(1, 2, 3, 4), re.groupBy().toA());
  }

  @Test
  public void testGroupByWithBlock() {
    assertEquals(rh(1, ra(1, 4), 2, ra(2), 0, ra(3)),
        re.groupBy(new TransformBlock<Integer, Integer>() {

          @Override
          public Integer yield(Integer item) {
            return item % 3;
          }

        }));
  }

  @Test
  public void testGroupByWithMethodName() {
    RubyEnumerator<String> re =
        newRubyEnumerator(Arrays.asList("ab", "ba", "ac", "bc"));
    assertEquals(rh('a', ra("ab", "ac"), 'b', ra("ba", "bc")),
        re.groupBy("charAt", 0));
  }

  @Test
  public void testIncludeʔ() {
    assertTrue(re.includeʔ(1));
    assertFalse(re.includeʔ(5));
  }

  @Test
  public void testInjectWithInit() {
    RubyArray<Integer> ra = ra();
    assertEquals(ra(1, 2, 3, 4), re.inject(ra, "push"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInjectWithInitException1() {
    re.inject(new RubyArray<Integer>(), "no push");
  }

  @Test
  public void testInjectWithBlock() {
    ReduceBlock<Integer> block = new ReduceBlock<Integer>() {

      @Override
      public Integer yield(Integer memo, Integer item) {
        return memo + item;
      }

    };
    assertEquals(Integer.valueOf(10), re.inject(block));
    re = ra();
    assertNull(re.inject(block));
  }

  @Test
  public void testInjectWithInitAndBlock() {
    assertEquals(Long.valueOf(20),
        re.inject(Long.valueOf(10), new WithInitBlock<Integer, Long>() {

          @Override
          public Long yield(Long init, Integer item) {
            return init + item;
          }

        }));
  }

  @Test
  public void testInjectWithMethodName() {
    RubyEnumerable<Boolean> bools =
        newRubyEnumerator(Arrays.asList(true, true, true));
    assertEquals(Boolean.TRUE, bools.inject("equals"));
    List<Boolean> list = new ArrayList<Boolean>();
    bools = newRubyEnumerator(list);
    assertNull(bools.inject("equals"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInjectException() {
    newRubyEnumerator(Arrays.asList(true, true, true)).inject("not equals");
  }

  @Test
  public void testLazy() {
    assertTrue(re.lazy() instanceof RubyLazyEnumerator);
  }

  @Test
  public void testMap() {
    assertEquals(RubyEnumerator.class, re.map().getClass());
    assertEquals(ra(1, 2, 3, 4), re.map().toA());
  }

  @Test
  public void testMapWithBlock() {
    assertEquals(ra(1L, 2L, 3L, 4L),
        re.map(new TransformBlock<Integer, Long>() {

          @Override
          public Long yield(Integer item) {
            return Long.valueOf(item);
          }

        }));
  }

  @Test
  public void testMapWithMethodName() {
    assertEquals(ra("a", "b", "c"), ra(" a ", " b ", " c ").map("trim"));
  }

  @Test
  public void testMax() {
    re = ra(3, 4, 1, 2);
    assertEquals(Integer.valueOf(4), re.max());
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertNull(re.max());
  }

  @Test
  public void testMaxWithComparator() {
    re = ra(3, 4, 1, 2);
    assertEquals(Integer.valueOf(1), re.max(new Comparator<Integer>() {

      @Override
      public int compare(Integer arg0, Integer arg1) {
        return arg1 - arg0;
      }

    }));
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertNull(re.max(null));
  }

  @Test
  public void testMaxBy() {
    assertEquals(RubyEnumerator.class, re.maxBy().getClass());
    assertEquals(ra(1, 2, 3, 4), re.maxBy().toA());
  }

  @Test
  public void testMaxByWithComparatorAndBlock() {
    RubyEnumerable<String> re =
        newRubyEnumerator(Arrays.asList("aaaa", "cc", "bbb", "d"));
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
    Comparator<Integer> comp = null;
    assertNull(re.maxBy(comp, null));
  }

  @Test
  public void testMaxByWithBlock() {
    RubyEnumerable<String> re =
        newRubyEnumerator(Arrays.asList("bbb", "aaaa", "cc", "d"));
    assertEquals("aaaa", re.maxBy(new TransformBlock<String, Integer>() {

      @Override
      public Integer yield(String item) {
        return item.length();
      }

    }));
    re = newRubyEnumerator(new ArrayList<String>());
    assertNull(re.maxBy(null));
  }

  @Test
  public void testMaxByWithMethodName() {
    RubyEnumerable<String> re =
        newRubyEnumerator(Arrays.asList("aaaa", "bbb", "cc", "d"));
    assertEquals("aaaa", re.maxBy("length"));
  }

  @Test
  public void testMemberʔ() {
    assertTrue(re.memberʔ(1));
    assertFalse(re.memberʔ(5));
  }

  @Test
  public void testMin() {
    re = ra(3, 4, 1, 2);
    assertEquals(Integer.valueOf(1), re.min());
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertNull(re.min());
  }

  @Test
  public void testMinWithComparator() {
    re = ra(3, 4, 1, 2);
    assertEquals(Integer.valueOf(4), re.min(new Comparator<Integer>() {

      @Override
      public int compare(Integer arg0, Integer arg1) {
        return arg1 - arg0;
      }

    }));
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertNull(re.min(null));
  }

  @Test
  public void testMinBy() {
    assertEquals(RubyEnumerator.class, re.minBy().getClass());
    assertEquals(ra(1, 2, 3, 4), re.minBy().toA());
  }

  @Test
  public void testMinByWithComparatorAndBlock() {
    RubyEnumerable<String> re =
        newRubyEnumerator(Arrays.asList("cc", "aaaa", "bbb", "d"));
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
    Comparator<Integer> comp = null;
    assertNull(re.minBy(comp, null));
  }

  @Test
  public void testMinByWithBlock() {
    RubyEnumerable<String> re =
        newRubyEnumerator(Arrays.asList("bbb", "aaaa", "cc", "d"));
    assertEquals("d", re.minBy(new TransformBlock<String, Integer>() {

      @Override
      public Integer yield(String item) {
        return item.length();
      }

    }));
    re = newRubyEnumerator(new ArrayList<String>());
    assertNull(re.minBy(null));
  }

  @Test
  public void testMinByWithMethodName() {
    RubyEnumerable<String> re =
        newRubyEnumerator(Arrays.asList("aaaa", "bbb", "cc", "d"));
    assertEquals("d", re.minBy("length"));
  }

  @Test
  public void testMinmax() {
    re = ra(2, 1, 4, 3);
    assertEquals(ra(1, 4), re.minmax());
    re = newRubyEnumerator(Arrays.asList(1));
    assertEquals(ra(1, 1), re.minmax());
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertEquals(ra(null, null), re.minmax());
  }

  @Test
  public void testMinmaxWithComparator() {
    re = ra(2, 1, 4, 3);
    Comparator<Integer> comp = new Comparator<Integer>() {

      @Override
      public int compare(Integer o1, Integer o2) {
        return o2 - o1;
      }

    };
    assertEquals(ra(4, 1), re.minmax(comp));
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertEquals(ra(null, null), re.minmax(comp));
    re = ra(null, null, null);
    assertEquals(ra(null, null), re.minmax(comp));
  }

  @Test
  public void testMinmaxBy() {
    assertEquals(RubyEnumerator.class, re.minmaxBy().getClass());
    assertEquals(ra(1, 2, 3, 4), re.minmaxBy().toA());
  }

  @Test
  public void testMinmaxByWithComparatorAndBlock() {
    RubyEnumerable<String> re =
        newRubyEnumerator(Arrays.asList("bbb", "aaaa", "d", "cc"));
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
    Comparator<Integer> comp = null;
    assertEquals(ra(null, null), re.minmaxBy(comp, null));
    re = ra(null, null, null);
    assertEquals(ra(null, null),
        re.minmaxBy(comp, new TransformBlock<String, Integer>() {

          @Override
          public Integer yield(String item) {
            return null;
          }

        }));
  }

  @Test
  public void testMinmaxByWithBlock() {
    RubyEnumerable<String> re =
        newRubyEnumerator(Arrays.asList("bbb", "aaaa", "d", "cc"));
    assertEquals(ra("d", "aaaa"),
        re.minmaxBy(new TransformBlock<String, Integer>() {

          @Override
          public Integer yield(String item) {
            return item.length();
          }

        }));
    re = newRubyEnumerator(new ArrayList<String>());
    assertEquals(ra(null, null), re.minmaxBy(null));
  }

  @Test
  public void testMinmaxByWithMethodName() {
    RubyEnumerable<String> re =
        newRubyEnumerator(Arrays.asList("aaaa", "bbb", "cc", "d"));
    assertEquals(ra("d", "aaaa"), re.minmaxBy("length"));
  }

  @Test
  public void testNoneʔ() {
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertTrue(re.noneʔ());
    RubyArray<Integer> ra = ra();
    ra.add(null);
    re = newRubyEnumerator(ra);
    assertTrue(re.noneʔ());
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertFalse(re.noneʔ());
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, null));
    assertFalse(re.noneʔ());
  }

  @Test
  public void testNoneʔWithBlock() {
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
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertFalse(re.oneʔ(null));
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
    RubyArray<Integer> ra = ra();
    assertEquals(ra(1, 2, 3, 4), re.reduce(ra, "push"));
  }

  @Test
  public void testReduceWithBlock() {
    assertEquals(Integer.valueOf(10), re.reduce(new ReduceBlock<Integer>() {

      @Override
      public Integer yield(Integer memo, Integer item) {
        return memo + item;
      }

    }));
  }

  @Test
  public void testReduceWithInitAndBlock() {
    assertEquals(Long.valueOf(20),
        re.reduce(Long.valueOf(10), new WithInitBlock<Integer, Long>() {

          @Override
          public Long yield(Long init, Integer item) {
            return init + item;
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
    assertEquals(RubyEnumerator.class, re.reject().getClass());
    assertEquals(ra(1, 2, 3, 4), re.reject().toA());
  }

  @Test
  public void testRejectWithBlock() {
    assertEquals(ra(2, 3, 4), re.reject(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item == 1;
      }

    }));
  }

  @Test
  public void testReverseEach() {
    assertEquals(RubyEnumerator.class, re.reverseEach().getClass());
    assertEquals(ra(4, 3, 2, 1), re.reverseEach().toA());
    assertEquals(ra(1, 2, 3, 4).toA(), re.toA());
  }

  @Test
  public void testReverseEachWithBlock() {
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
    assertEquals(RubyEnumerator.class, re.select().getClass());
    assertEquals(ra(1, 2, 3, 4), re.select().toA());
  }

  @Test
  public void testSelectBlock() {
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
    assertEquals(ra(null, null, null), newRubyEnumerator(ra(null, null, null))
        .sort());
    re = newRubyEnumerator(Arrays.asList(1));
    assertEquals(ra(1), re.sort());
  }

  // @Test
  // public void testSortWithComparator() {
  // re = newRubyEnumerator(Arrays.asList(4, 1, 2, 3, 3));
  // assertEquals(ra(4, 3, 3, 2, 1), re.sort(new Comparator<Integer>() {
  //
  // @Override
  // public int compare(Integer o1, Integer o2) {
  // return o2 - o1;
  // }
  //
  // }));
  // assertEquals(ra(1), newRubyEnumerator(Arrays.asList(1)).sort(null));
  // List<Integer> nulls = new ArrayList<Integer>();
  // nulls.add(null);
  // nulls.add(null);
  // nulls.add(null);
  // re = newRubyEnumerator(nulls);
  // assertEquals(ra(null, null, null), re.sort(null));
  // }

  @Test
  public void testSortBy() {
    assertEquals(RubyEnumerator.class, re.sortBy().getClass());
    assertEquals(ra(1, 2, 3, 4), re.sortBy().toA());
  }

  @Test
  public void testSortByWithComparatorAndBlock() {
    RubyEnumerable<String> re =
        newRubyEnumerator(Arrays.asList("aaaa", "bbb", "cc", "e", "d"));
    assertEquals(ra("aaaa", "bbb", "cc", "e", "d"),
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
  public void testSortByWith2ComparatorsAndBlock() {
    RubyEnumerable<String> re =
        newRubyEnumerator(Arrays.asList("aaaa", "bbb", "cc", "d", "e"));
    assertEquals(ra("aaaa", "bbb", "cc", "e", "d"),
        re.sortBy(new Comparator<String>() {

          @Override
          public int compare(String o1, String o2) {
            return o2.compareTo(o1);
          }

        }, new Comparator<Integer>() {

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
        newRubyEnumerator(Arrays.asList("aaaa", "bbb", "cc", "e", "d"));
    assertEquals(ra("e", "d", "cc", "bbb", "aaaa"),
        re.sortBy(new TransformBlock<String, Integer>() {

          @Override
          public Integer yield(String item) {
            return item.length();
          }

        }));
  }

  @Test
  public void testSortByWithMethodName() {
    RubyEnumerable<String> re =
        newRubyEnumerator(Arrays.asList("aaaa", "bbb", "cc", "e", "d"));
    assertEquals(ra("e", "d", "cc", "bbb", "aaaa"), re.sortBy("length"));
  }

  @Test
  public void testTake() {
    assertEquals(ra(), re.take(0));
    assertEquals(ra(1, 2), re.take(2));
    assertEquals(ra(1, 2, 3, 4), re.take(5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTakeException() {
    re.take(-1);
  }

  @Test
  public void testTakeWhile() {
    assertEquals(ra(1), re.takeWhile().toA());
    re = new RubyEnumerable<Integer>() {

      @Override
      protected Iterable<Integer> getIterable() {
        return Collections.emptyList();
      }

    };
    assertEquals(ra(), re.takeWhile().toA());
  }

  @Test
  public void testTakeWhileWithBlock() {
    BooleanBlock<Integer> block = new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item != 3;
      }

    };
    assertEquals(ra(1, 2), re.takeWhile(block));
    re = ra(1, 1, 1, 1);
    assertEquals(ra(1, 1, 1, 1), re.takeWhile(block));
  }

  @Test
  public void testToA() {
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
    re.zip(ra(ra(4, 5, 6, null), ra(7, 8, 9, null)),
        new Block<RubyArray<Integer>>() {

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
    assertTrue(re.iterator() instanceof Iterator);
  }

  @Test
  public void testToString() {
    assertEquals("RubyEnumerable{[1, 2, 3, 4]}", re.toString());
  }

}
