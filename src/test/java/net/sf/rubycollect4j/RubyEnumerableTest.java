/*
 *
 * Copyright 2013 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package net.sf.rubycollect4j;

import static net.sf.rubycollect4j.RubyCollections.*;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RubyEnumerableTest {

  RubyEnumerable<Integer> re;
  Iterable<Integer> iter;

  @BeforeEach
  public void setUp() throws Exception {
    iter = newRubyArray(1, 2, 3, 4);
    re = new RubyEnumerable<Integer>() {

      @Override
      public Iterator<Integer> iterator() {
        return iter.iterator();
      }

    };
  }

  @Test
  public void testInstantiation() {
    assertTrue(re instanceof RubyEnumerable);
  }

  @Test
  public void testInterface() {
    assertTrue(re instanceof RubyBase.Enumerable);
  }

  @Test
  public void testAllʔ() {
    assertTrue(re.allʔ());
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertTrue(re.allʔ());
    re = newRubyEnumerator(Arrays.asList(1, 2, null));
    assertFalse(re.allʔ());
    assertFalse(newRubyEnumerator(Arrays.asList(false, true)).allʔ());
    assertFalse(newRubyEnumerator(Arrays.asList(null, true)).allʔ());
    assertTrue(newRubyEnumerator(Arrays.asList(true, true)).allʔ());
  }

  @Test
  public void testAllʔWithBlock() {
    Predicate<Integer> block = item -> item % 2 == 0;
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
    assertFalse(newRubyEnumerator(Arrays.asList(false, null)).anyʔ());
    assertTrue(newRubyEnumerator(Arrays.asList(false, null, true)).anyʔ());
  }

  @Test
  public void testAnyʔWithBlock() {
    Predicate<Integer> block = item -> item % 2 == 0;
    assertTrue(re.anyʔ(block));
    re = ra(1, 3, 5, 7);
    assertFalse(re.anyʔ(block));
  }

  @Test
  public void testChunk() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 2, 3));
    RubyArray<Entry<Boolean, RubyArray<Integer>>> chunk = re.chunk(item -> item % 2 == 0).toA();
    assertEquals(hp(false, newRubyArray(1)).toString(), chunk.get(0).toString());
    assertEquals(hp(true, newRubyArray(2, 2)).toString(), chunk.get(1).toString());
    assertEquals(hp(false, newRubyArray(3)).toString(), chunk.get(2).toString());
    assertEquals(3, chunk.size());
  }

  @Test
  public void testChunkWhile() {
    RubyEnumerator<Integer> re =
        newRubyEnumerator(Arrays.asList(1, 2, 4, 9, 10, 11, 12, 15, 16, 19, 20, 21));
    assertEquals(ra(ra(1, 2), ra(4), ra(9, 10, 11, 12), ra(15, 16), ra(19, 20, 21)),
        re.chunkWhile((key, value) -> key + 1 == value).toA());
  }

  @Test
  public void testCollect() {
    assertEquals(RubyEnumerator.class, re.collect().getClass());
    assertEquals(ra(1, 2, 3, 4), re.collect().toA());
  }

  @Test
  public void testCollectWithBlock() {
    assertEquals(ra(1.0, 2.0, 3.0, 4.0), re.collect(item -> Double.valueOf(item)));
  }

  @Test
  public void testCollectConcat() {
    assertEquals(RubyEnumerator.class, re.collectConcat().getClass());
    assertEquals(ra(1, 2, 3, 4), re.collectConcat().toA());
  }

  @Test
  public void testCollectConcatWithBlock() {
    assertEquals(ra(1.0, 2.0, 3.0, 4.0), re.collectConcat(item -> ra(Double.valueOf(item))));
  }

  @Test
  public void testCount() {
    assertEquals(4, re.count());
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertEquals(0, re.count());
  }

  @Test
  public void testCountWithBlock() {
    assertEquals(2, re.count(item -> item % 2 == 1));
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
    re.cycle(2, item -> ints.push(item * 2));
    assertEquals(ra(2, 4, 6, 8, 2, 4, 6, 8), ints);
  }

  @Test
  public void testCycleWithBlock() {
    final RubyArray<Integer> ints = newRubyArray();
    assertThrows(IllegalStateException.class, () -> {
      re.cycle(item -> {
        ints.push(item);
        if (ints.size() > 1000) throw new IllegalStateException();
      });
    });
  }

  @Test
  public void testDetect() {
    assertTrue(re.detect() instanceof RubyEnumerator);
    assertEquals(ra(1, 2, 3, 4), re.detect().toA());
  }

  @Test
  public void testDetectWithBlock() {
    Predicate<Integer> block = item -> item == 3;
    assertEquals(Integer.valueOf(3), re.detect(block));
    re = ra(1, 2, 4, 5);
    assertNull(re.detect(block));
  }

  @Test
  public void testDrop() {
    assertEquals(ra(1, 2, 3, 4), re.drop(0));
    assertEquals(ra(3, 4), re.drop(2));
  }

  @Test
  public void testDropException() {
    assertThrows(IllegalArgumentException.class, () -> {
      re.drop(-1);
    });
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
    assertEquals(ra(3, 1), re.dropWhile(item -> item < 3));
  }

  @Test
  public void testEach() {
    assertTrue(re.each() instanceof RubyEnumerator);
    assertEquals(re.toA(), re.each().toA());
  }

  @Test
  public void testEachWithBlock() {
    final RubyArray<Integer> ints = ra();
    assertEquals(re.toA(), re.each(item -> ints.push(item * 2)).toA());
    assertEquals(ra(2, 4, 6, 8), ints);
  }

  @Test
  public void testEachCons() {
    assertEquals(RubyEnumerator.class, re.eachCons(2).getClass());
    RubyArray<RubyArray<Integer>> ra = ra(ra(1, 2), ra(2, 3), ra(3, 4));
    assertEquals(ra, re.eachCons(2).toA());
  }

  @Test
  public void testEachConsException() {
    assertThrows(IllegalArgumentException.class, () -> {
      re.eachCons(0);
    });
  }

  @Test
  public void testEachConsWithBlock() {
    final RubyArray<List<Integer>> ra = ra();
    re.eachCons(2, item -> ra.push(item));
    assertEquals(ra, re.eachCons(2).toA());
  }

  @Test
  public void testEachConsWithBlockException() {
    assertThrows(IllegalArgumentException.class, () -> {
      re.eachCons(0, null);
    });
  }

  @Test
  public void testEachEntry() {
    assertEquals(RubyEnumerator.class, re.eachEntry().getClass());
    assertEquals(ra(1, 2, 3, 4), re.eachEntry().toA());
  }

  @Test
  public void testEachEntryWithBlock() {
    final RubyArray<Integer> ints = ra();
    re.eachEntry(item -> ints.add(item * 2));
    assertEquals(ra(2, 4, 6, 8), ints);
    assertTrue(re.eachEntry(item -> {}) instanceof RubyEnumerable);
  }

  @Test
  public void testEachSlice() {
    assertEquals(RubyEnumerator.class, re.eachSlice(3).getClass());
    RubyArray<RubyArray<Integer>> ra = ra(ra(1, 2, 3), ra(4));
    assertEquals(ra, re.eachSlice(3).toA());
  }

  @Test
  public void testEachSliceException() {
    assertThrows(IllegalArgumentException.class, () -> {
      re.eachSlice(0);
    });
  }

  @Test
  public void testEachSliceWithBlock() {
    final RubyArray<Integer> ints = ra();
    re.eachSlice(3, item -> ints.add(item.get(0)));
    assertEquals(ra(1, 4), ints);
  }

  @Test
  public void testEachSliceWithBlockException() {
    assertThrows(IllegalArgumentException.class, () -> {
      re.eachSlice(0, null);
    });
  }

  @Test
  public void testEachWithIndex() {
    assertEquals(RubyEnumerator.class, re.eachWithIndex().getClass());
    RubyArray<? extends Entry<Integer, Integer>> ra = ra(hp(1, 0), hp(2, 1), hp(3, 2), hp(4, 3));
    assertEquals(ra, re.eachWithIndex().toA());
  }

  @Test
  public void testEachWithIndexWithBlock() {
    final RubyArray<Integer> ints = ra();
    assertTrue(re.eachWithIndex((item, index) -> ints.add(item + index)) instanceof RubyEnumerable);
    assertEquals(ra(1, 3, 5, 7), ints);
  }

  @Test
  public void testEachWithObject() {
    Long obj = 0L;
    assertEquals(RubyEnumerator.class, re.eachWithObject(obj).getClass());
    RubyArray<? extends Entry<Integer, Long>> ra =
        ra(hp(1, obj), hp(2, obj), hp(3, obj), hp(4, obj));
    assertEquals(ra, re.eachWithObject(obj).toA());
  }

  @Test
  public void testEachWithObjectWithBlock() {
    Long[] obj = new Long[] {0L};
    assertEquals(new Long[] {10L}[0], re.eachWithObject(obj, (item, obj1) -> obj1[0] += item)[0]);
  }

  @Test
  public void testEntries() {
    assertEquals(ra(1, 2, 3, 4), re.entries());
  }

  @Test
  public void testFilter() {
    assertEquals(RubyEnumerator.class, re.filter().getClass());
    assertEquals(ra(1, 2, 3, 4), re.filter().toA());
  }

  @Test
  public void testFilterWithBlock() {
    assertEquals(ra(3, 4), re.filter(item -> item >= 3));
  }

  @Test
  public void testFind() {
    assertEquals(RubyEnumerator.class, re.find().getClass());
    assertEquals(ra(1, 2, 3, 4), re.find().toA());
  }

  @Test
  public void testFindWithBlock() {
    Predicate<Integer> block = item -> item == 3;
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
    assertEquals(ra(2, 3, 4), re.findAll(item -> item >= 2));
  }

  @Test
  public void testFindIndex() {
    assertEquals(RubyEnumerator.class, re.findIndex().getClass());
    assertEquals(ra(1, 2, 3, 4), re.findIndex().toA());
  }

  @Test
  public void testFindIndexWithBlock() {
    Predicate<Integer> block = item -> item >= 4;
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

  @Test
  public void testFirstWithNException() {
    assertThrows(IllegalArgumentException.class, () -> {
      re.first(-1);
    });
  }

  @Test
  public void testFlatMap() {
    assertEquals(RubyEnumerator.class, re.flatMap().getClass());
    assertEquals(ra(1, 2, 3, 4), re.flatMap().toA());
  }

  @Test
  public void testFlatMapWithBlock() {
    assertEquals(ra(1L, 2L, 3L, 4L), re.flatMap(item -> ra(Long.valueOf(item))));
  }

  @Test
  public void testGrep() {
    assertEquals(ra(2, 4), re.grep("[24]"));
  }

  @Test
  public void testGrepWithBlock() {
    assertEquals(ra("2", "4"), re.grep("[24]", item -> item.toString()));
  }

  @Test
  public void testGrepV() {
    assertEquals(ra(1, 3), re.grepV("[24]"));
  }

  @Test
  public void testGrepVWithBlock() {
    assertEquals(ra("1", "3"), re.grepV("[24]", item -> item.toString()));
  }

  @Test
  public void testGroupBy() {
    assertEquals(RubyEnumerator.class, re.groupBy().getClass());
    assertEquals(ra(1, 2, 3, 4), re.groupBy().toA());
  }

  @Test
  public void testGroupByWithBlock() {
    assertEquals(rh(1, ra(1, 4), 2, ra(2), 0, ra(3)), re.groupBy(item -> item % 3));
  }

  @Test
  public void testIncludeʔ() {
    assertTrue(re.includeʔ(1));
    assertFalse(re.includeʔ(5));
  }

  @Test
  public void testInjectWithInit() {
    RubyArray<Integer> ra = ra();
    assertEquals(ra(1, 2, 3, 4), re.inject(ra, RubyArray::push));
  }

  @Test
  public void testInjectWithBlock() {
    BiFunction<Integer, Integer, Integer> block = (memo, item) -> memo + item;
    assertEquals(Integer.valueOf(10), re.inject(block));
    re = ra();
    assertNull(re.inject(block));
  }

  @Test
  public void testInjectWithInitAndBlock() {
    assertEquals(Long.valueOf(20), re.inject(Long.valueOf(10), (init, item) -> init + item));
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
    assertEquals(ra(1L, 2L, 3L, 4L), re.map(item -> Long.valueOf(item)));
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
    assertEquals(Integer.valueOf(1), re.max((arg0, arg1) -> arg1 - arg0));
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
    RubyEnumerable<String> re = newRubyEnumerator(Arrays.asList("aaaa", "cc", "bbb", "d"));
    assertEquals("d", re.maxBy((o1, o2) -> o2 - o1, item -> item.length()));
    re = newRubyEnumerator(new ArrayList<String>());
    Comparator<Integer> comp = null;
    assertNull(re.maxBy(comp, null));
  }

  @Test
  public void testMaxByWithBlock() {
    RubyEnumerable<String> re = newRubyEnumerator(Arrays.asList("bbb", "aaaa", "cc", "d"));
    assertEquals("aaaa", re.maxBy(item -> item.length()));
    re = newRubyEnumerator(new ArrayList<String>());
    assertNull(re.maxBy(null));
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
    assertEquals(Integer.valueOf(4), re.min((arg0, arg1) -> arg1 - arg0));
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
    RubyEnumerable<String> re = newRubyEnumerator(Arrays.asList("cc", "aaaa", "bbb", "d"));
    assertEquals("aaaa", re.minBy((o1, o2) -> o2 - o1, item -> item.length()));
    re = newRubyEnumerator(new ArrayList<String>());
    Comparator<Integer> comp = null;
    assertNull(re.minBy(comp, null));
  }

  @Test
  public void testMinByWithBlock() {
    RubyEnumerable<String> re = newRubyEnumerator(Arrays.asList("bbb", "aaaa", "cc", "d"));
    assertEquals("d", re.minBy(item -> item.length()));
    re = newRubyEnumerator(new ArrayList<String>());
    assertNull(re.minBy(null));
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
    Comparator<Integer> comp = (o1, o2) -> o2 - o1;
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
    RubyEnumerable<String> re = newRubyEnumerator(Arrays.asList("bbb", "aaaa", "d", "cc"));
    assertEquals(ra("aaaa", "d"), re.minmaxBy((o1, o2) -> o2 - o1, item -> item.length()));
    re = newRubyEnumerator(new ArrayList<String>());
    Comparator<Integer> comp = null;
    assertEquals(ra(null, null), re.minmaxBy(comp, null));
    re = ra(null, null, null);
    assertEquals(ra(null, null), re.minmaxBy(comp, item -> null));
  }

  @Test
  public void testMinmaxByWithBlock() {
    RubyEnumerable<String> re = newRubyEnumerator(Arrays.asList("bbb", "aaaa", "d", "cc"));
    assertEquals(ra("d", "aaaa"), re.minmaxBy(item -> item.length()));
    re = newRubyEnumerator(new ArrayList<String>());
    assertEquals(ra(null, null), re.minmaxBy(null));
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
    assertTrue(newRubyEnumerator(Arrays.asList(false, null)).noneʔ());
    assertFalse(newRubyEnumerator(Arrays.asList(false, null, true)).noneʔ());
  }

  @Test
  public void testNoneʔWithBlock() {
    assertTrue(re.noneʔ(item -> item > 5));
    assertFalse(re.noneʔ(item -> item == 3));
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
    assertFalse(newRubyEnumerator(Arrays.asList(false, null)).oneʔ());
    assertTrue(newRubyEnumerator(Arrays.asList(false, null, true)).oneʔ());
    assertFalse(newRubyEnumerator(Arrays.asList(null, true, true)).oneʔ());
  }

  @Test
  public void testOneʔWithBlock() {
    assertTrue(re.oneʔ(item -> item > 3));
    assertFalse(re.oneʔ(item -> item > 2));
    re = newRubyEnumerator(new ArrayList<Integer>());
    assertFalse(re.oneʔ(null));
  }

  @Test
  public void testPartition() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertEquals(RubyEnumerator.class, re.partition().getClass());
    assertEquals(ra(1, 2, 3, 4), re.partition().toA());
  }

  @Test
  public void testPartitionWithBlock() {
    assertEquals(ra(ra(1, 3), ra(2, 4)), re.partition(item -> item % 2 == 1));
  }

  @Test
  public void testReduceWithInit() {
    RubyArray<Integer> ra = ra();
    assertEquals(ra(1, 2, 3, 4), re.reduce(ra, RubyArray::push));
  }

  @Test
  public void testReduceWithBlock() {
    assertEquals(Integer.valueOf(10), re.reduce((memo, item) -> memo + item));
  }

  @Test
  public void testReduceWithInitAndBlock() {
    assertEquals(Long.valueOf(20), re.reduce(Long.valueOf(10), (init, item) -> init + item));
  }

  @Test
  public void testReduce() {
    RubyEnumerable<Boolean> bools = newRubyEnumerator(Arrays.asList(true, true, true));
    assertEquals(Boolean.TRUE, bools.reduce(Boolean::equals));
  }

  @Test
  public void testReject() {
    assertEquals(RubyEnumerator.class, re.reject().getClass());
    assertEquals(ra(1, 2, 3, 4), re.reject().toA());
  }

  @Test
  public void testRejectWithBlock() {
    assertEquals(ra(2, 3, 4), re.reject(item -> item == 1));
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
    assertEquals(re, re.reverseEach(item -> ra.push(item)));
    assertEquals(ra(4, 3, 2, 1), ra);
    assertEquals(ra(1, 2, 3, 4), re.toA());
  }

  @Test
  public void testSelect() {
    assertEquals(RubyEnumerator.class, re.select().getClass());
    assertEquals(ra(1, 2, 3, 4), re.select().toA());
  }

  @Test
  public void testSelectWithBlock() {
    assertEquals(ra(3, 4), re.select(item -> item >= 3));
  }

  @Test
  public void testSliceAfterWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 3, 4, 7));
    assertEquals(ra(ra(1), ra(3), ra(4, 7)), re.sliceAfter(item -> item % 2 == 1).toA());
  }

  @Test
  public void testSliceAfterWithRegex() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 3));
    assertEquals(ra(ra(1, 2, 3), ra(3)), re.sliceAfter("3").toA());
  }

  @Test
  public void testSliceBeforeWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 3, 3, 4));
    assertEquals(ra(ra(1), ra(3), ra(3, 4)), re.sliceBefore(item -> item % 2 == 1).toA());
  }

  @Test
  public void testSliceBeforeWithRegex() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3, 3));
    assertEquals(ra(ra(1, 2), ra(3), ra(3)), re.sliceBefore("3").toA());
  }

  @Test
  public void testSliceWhen() {
    re = newRubyEnumerator(Arrays.asList(1, 3, 3, 4));
    assertEquals(ra(ra(1, 3, 3), ra(4)), re.sliceWhen((item1, item2) -> item1 + 1 == item2).toA());
  }

  @Test
  public void testSort() {
    re = newRubyEnumerator(Arrays.asList(4, 1, 2, 3, 3));
    assertEquals(ra(1, 2, 3, 3, 4), re.sort());
    assertEquals(ra("abc", "b", "cd"), newRubyEnumerator(Arrays.asList("b", "cd", "abc")).sort());
    assertEquals(ra(null, null, null), newRubyEnumerator(ra(null, null, null)).sort());
    re = newRubyEnumerator(Arrays.asList(1));
    assertEquals(ra(1), re.sort());
  }

  @Test
  public void testSortBy() {
    assertEquals(RubyEnumerator.class, re.sortBy().getClass());
    assertEquals(ra(1, 2, 3, 4), re.sortBy().toA());
  }

  @Test
  public void testSortByWithComparatorAndBlock() {
    RubyEnumerable<String> re = newRubyEnumerator(Arrays.asList("aaaa", "bbb", "cc", "e", "d"));
    assertEquals(ra("aaaa", "bbb", "cc", "e", "d"),
        re.sortBy((o1, o2) -> o2 - o1, item -> item.length()));
  }

  @Test
  public void testSortByWith2ComparatorsAndBlock() {
    RubyEnumerable<String> re = newRubyEnumerator(Arrays.asList("aaaa", "bbb", "cc", "d", "e"));
    assertEquals(ra("aaaa", "bbb", "cc", "e", "d"),
        re.sortBy((o1, o2) -> o2.compareTo(o1), (o1, o2) -> o2 - o1, item -> item.length()));
  }

  @Test
  public void testSortByWithBlock() {
    RubyEnumerable<String> re = newRubyEnumerator(Arrays.asList("aaaa", "bbb", "cc", "e", "d"));
    assertEquals(ra("e", "d", "cc", "bbb", "aaaa"), re.sortBy(item -> item.length()));
  }

  @Test
  public void testSum() {
    assertEquals(new BigDecimal(10), newRubyEnumerator(Arrays.asList(1, 2, 3, 4)).sum());
  }

  @Test
  public void testSumException() {
    assertThrows(ClassCastException.class, () -> {
      newRubyEnumerator(Arrays.asList("a", "b", "c")).sum();
    });
  }

  @Test
  public void testSumWithBlock() {
    assertEquals(new BigDecimal("20.8"),
        newRubyEnumerator(Arrays.asList(1.1, 2.1, 3.1, 4.1)).sum(n -> n.doubleValue() * 2));
  }

  @Test
  public void testSumWithInit() {
    assertEquals(new BigDecimal("11.4"),
        newRubyEnumerator(Arrays.asList(1.1, 2.1, 3.1, 4.1)).sum(1L));
  }

  @Test
  public void testSumWithInitAndBlock() {
    assertEquals(new BigDecimal("21.8"),
        newRubyEnumerator(Arrays.asList(1.1, 2.1, 3.1, 4.1)).sum(1L, n -> n.doubleValue() * 2));
  }

  @Test
  public void testTake() {
    assertEquals(ra(), re.take(0));
    assertEquals(ra(1, 2), re.take(2));
    assertEquals(ra(1, 2, 3, 4), re.take(5));
  }

  @Test
  public void testTakeException() {
    assertThrows(IllegalArgumentException.class, () -> {
      re.take(-1);
    });
  }

  @Test
  public void testTakeWhile() {
    assertEquals(ra(1), re.takeWhile().toA());
    re = new RubyEnumerable<Integer>() {

      @Override
      public Iterator<Integer> iterator() {
        return new ArrayList<Integer>().iterator();
      }

    };
    assertEquals(ra(), re.takeWhile().toA());
  }

  @Test
  public void testTakeWhileWithBlock() {
    Predicate<Integer> block = item -> item != 3;
    assertEquals(ra(1, 2), re.takeWhile(block));
    re = ra(1, 1, 1, 1);
    assertEquals(ra(1, 1, 1, 1), re.takeWhile(block));
  }

  @Test
  public void testToA() {
    assertEquals(ra(1, 2, 3, 4), re.toA());
  }

  @Test
  public void testToList() {
    assertEquals(Arrays.asList(1, 2, 3, 4), re.toList());
    assertTrue(re.toList() instanceof ArrayList);
  }

  @Test
  public void testToSet() {
    assertEquals(new LinkedHashSet<>(Arrays.asList(1, 2, 3, 4)), re.toSet());
    iter = newRubyArray(1, 2, 2, 3, 3, 3);
    assertEquals(new LinkedHashSet<>(Arrays.asList(1, 2, 3)), re.toSet());
    assertTrue(re.toSet() instanceof LinkedHashSet);
  }

  @Test
  public void testToH() {
    assertEquals(rh(1, 2, 3, 4), new RubyEnumerable<List<Integer>>() {

      @Override
      public Iterator<List<Integer>> iterator() {
        List<Integer> l1 = ra(1, 2);
        List<Integer> l2 = ra(3, 4);
        return Arrays.asList(l1, l2).iterator();
      }

    }.toH(ary -> hp(ary.get(0), ary.get(1))));

    assertEquals(rh(1, 2, 3, 4), new RubyEnumerable<Integer>() {

      @Override
      public Iterator<Integer> iterator() {
        return Arrays.asList(1, 2, 3, 4).iterator();
      }

    }.toH((k, v) -> hp(k, v)));
  }

  @Test
  public void testZip() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3));
    assertEquals(ra(ra(1, 4), ra(2, 5), ra(3, null)), re.zip(ra(ra(4, 5))));
    assertEquals(ra(ra(1, 4, 7), ra(2, 5, 8), ra(3, 6, 9)), re.zip(ra(ra(4, 5, 6), ra(7, 8, 9))));
  }

  @Test
  public void testZipWithBlock() {
    re = newRubyEnumerator(Arrays.asList(1, 2, 3));
    final RubyArray<Integer> ints = ra();
    re.zip(ra(ra(4, 5, 6, null), ra(7, 8, 9, null)), (Consumer<RubyArray<Integer>>) item -> ints
        .push(item.reduce((memo, item1) -> memo + item1)));
    assertEquals(ra(12, 15, 18), ints);
  }

  @Test
  public void testIterator() {
    assertTrue(re.iterator() instanceof Iterator);
  }

}
