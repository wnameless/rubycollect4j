/*
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
import static net.sf.rubycollect4j.RubyCollections.newRubyLazyEnumerator;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.rh;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RubyLazyEnumeratorTest {

  RubyLazyEnumerator<Integer> lre;
  List<Integer> list;
  Function<Integer, Boolean> block;

  @BeforeEach
  public void setUp() throws Exception {
    list = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    lre = new RubyLazyEnumerator<Integer>(list);
    block = item -> item % 2 == 0;
  }

  @Test
  public void testOf() {
    lre = RubyLazyEnumerator.of(list);
    list.remove(0);
    assertEquals(ra(2, 3, 4), lre.toA());
  }

  @Test
  public void testOfException() {
    assertThrows(NullPointerException.class, () -> {
      RubyLazyEnumerator.of(null);
    });
  }

  @Test
  public void testCopyOf() {
    lre = RubyLazyEnumerator.copyOf(list);
    list.remove(0);
    assertEquals(ra(1, 2, 3, 4), lre.toA());
  }

  @Test
  public void testCopyOfException() {
    assertThrows(NullPointerException.class, () -> {
      RubyLazyEnumerator.copyOf(null);
    });
  }

  @Test
  public void testConstructor() {
    assertTrue(lre instanceof RubyLazyEnumerator);
  }

  @Test
  public void testConstructorException() {
    assertThrows(NullPointerException.class, () -> {
      newRubyLazyEnumerator(null);
    });
  }

  @Test
  public void testAllʔ() {
    assertTrue(lre.allʔ());
    lre = newRubyLazyEnumerator(new ArrayList<Integer>());
    assertTrue(lre.allʔ());
    lre = newRubyLazyEnumerator(Arrays.asList(1, 2, null));
    assertFalse(lre.allʔ());
    assertFalse(newRubyLazyEnumerator(Arrays.asList(false, true)).allʔ());
    assertFalse(newRubyLazyEnumerator(Arrays.asList(null, true)).allʔ());
    assertTrue(newRubyLazyEnumerator(Arrays.asList(true, true)).allʔ());
  }

  @Test
  public void testAllʔWithBlock() {
    Predicate<Integer> block = item -> item % 2 == 0;
    assertFalse(lre.allʔ(block));
    lre = ra(2, 4, 6, 8).lazy();
    assertTrue(lre.allʔ(block));
  }

  @Test
  public void testAnyʔ() {
    assertTrue(lre.anyʔ());
    lre = newRubyLazyEnumerator(new ArrayList<Integer>());
    assertFalse(lre.anyʔ());
    lre = newRubyLazyEnumerator(Arrays.asList(1, 2, null));
    assertTrue(lre.anyʔ());
    List<Integer> ints = Arrays.asList(null, null);
    lre = newRubyLazyEnumerator(ints);
    assertFalse(lre.anyʔ());
    assertFalse(newRubyLazyEnumerator(Arrays.asList(false, null)).anyʔ());
    assertTrue(newRubyLazyEnumerator(Arrays.asList(false, null, true)).anyʔ());
  }

  @Test
  public void testAnyʔWithBlock() {
    Predicate<Integer> block = item -> item % 2 == 0;
    assertTrue(lre.anyʔ(block));
    lre = ra(1, 3, 5, 7).lazy();
    assertFalse(lre.anyʔ(block));
  }

  @Test
  public void testChunk() {
    assertTrue(lre.chunk(block) instanceof RubyLazyEnumerator);
    lre = newRubyLazyEnumerator(Arrays.asList(1, 2, 2, 3));
    assertEquals(ra(hp(false, ra(1)), hp(true, ra(2, 2)), hp(false, ra(3))),
        lre.chunk(block).toA());
  }

  @Test
  public void testChunkWhile() {
    lre = newRubyLazyEnumerator(Arrays.asList(1, 2, 2, 3));
    BiPredicate<Integer, Integer> block = (key, value) -> value - key == 1;
    assertTrue(lre.chunkWhile(block) instanceof RubyLazyEnumerator);
    assertEquals(ra(ra(1, 2), ra(2, 3)), lre.chunkWhile(block).toA());
  }

  @Test
  public void testCollect() {
    assertSame(lre, lre.collect());
  }

  @Test
  public void testCollectWithBlock() {
    assertTrue(lre.collect(block) instanceof RubyLazyEnumerator);
    assertEquals(ra(1.0, 2.0, 3.0, 4.0),
        lre.collect(item -> Double.valueOf(item)).toA());
  }

  @Test
  public void testCollectConcat() {
    assertSame(lre, lre.collectConcat());
  }

  @Test
  public void testCollectConcatWithBlock() {
    Function<Integer, RubyArray<Integer>> block = item -> ra(item, item);
    assertTrue(lre.collectConcat(block) instanceof RubyLazyEnumerator);
    assertEquals(ra(1, 1, 2, 2, 3, 3, 4, 4), lre.collectConcat(block).toA());
  }

  @Test
  public void testCount() {
    assertEquals(4, lre.count());
    lre = newRubyLazyEnumerator(new ArrayList<Integer>());
    assertEquals(0, lre.count());
  }

  @Test
  public void testCountWithBlock() {
    assertEquals(2, lre.count(item -> item % 2 == 1));
  }

  @Test
  public void testCycle() {
    assertTrue(lre.cycle() instanceof RubyLazyEnumerator);
    RubyArray<Integer> ints = ra(1, 2, 3, 4);
    Iterator<Integer> iter = lre.cycle().iterator();
    for (int i = 0; i < 100; i++) {
      assertEquals(ints.get(0), iter.next());
      ints.rotateǃ();
    }
  }

  @Test
  public void testCycleWithN() {
    assertTrue(lre.cycle(3) instanceof RubyLazyEnumerator);
    RubyArray<Integer> ints = newRubyArray();
    Iterator<Integer> iter = lre.cycle(2).iterator();
    while (iter.hasNext()) {
      ints.push(iter.next());
    }
    assertEquals(ra(1, 2, 3, 4, 1, 2, 3, 4), ints);
  }

  @Test
  public void testCycleWithNAndBlock() {
    final RubyArray<Integer> ints = newRubyArray();
    lre.cycle(2, item -> ints.push(item * 2));
    assertEquals(ra(2, 4, 6, 8, 2, 4, 6, 8), ints);
  }

  @Test
  public void testCycleWithBlock() {
    final RubyArray<Integer> ints = newRubyArray();
    assertThrows(IllegalStateException.class, () -> {
      lre.cycle(item -> {
        ints.push(item);
        if (ints.size() > 1000) throw new IllegalStateException();
      });
    });
  }

  @Test
  public void testDetect() {
    assertSame(lre, lre.detect());
  }

  @Test
  public void testDetectWithBlock() {
    Predicate<Integer> block = item -> item == 3;
    assertEquals(Integer.valueOf(3), lre.detect(block));
    lre = ra(1, 2, 4, 5).lazy();
    assertNull(lre.detect(block));
  }

  @Test
  public void testDrop() {
    assertTrue(lre.drop(1) instanceof RubyLazyEnumerator);
    assertEquals(ra(1, 2, 3, 4), lre.drop(0).toA());
    assertEquals(ra(3, 4), lre.drop(2).toA());
  }

  @Test
  public void testDropException() {
    assertThrows(IllegalArgumentException.class, () -> {
      lre.drop(-1);
    });
  }

  @Test
  public void testDropWhile() {
    assertEquals(ra(1), lre.dropWhile().toA());
    lre = new RubyLazyEnumerator<Integer>(new ArrayList<Integer>());
    assertEquals(ra(), lre.dropWhile().toA());
  }

  @Test
  public void testDropWhileWithBlock() {
    Predicate<Integer> block = item -> item < 3;
    assertTrue(lre.dropWhile(block) instanceof RubyLazyEnumerator);
    assertEquals(ra(3, 4), lre.dropWhile(block).toA());
  }

  @Test
  public void testEach() {
    assertSame(lre, lre.each());
  }

  @Test
  public void testEachWithBlock() {
    final RubyArray<Integer> ints = ra();
    lre.each(item -> ints.add(item));
    assertEquals(ra(1, 2, 3, 4), ints);
  }

  @Test
  public void testEachCons() {
    assertTrue(lre.eachCons(2) instanceof RubyLazyEnumerator);
    RubyArray<RubyArray<Integer>> ra = ra(ra(1, 2), ra(2, 3), ra(3, 4));
    assertEquals(ra, lre.eachCons(2).toA());
  }

  @Test
  public void testEachConsWithBlock() {
    final RubyArray<RubyArray<Integer>> rubyArray = ra();
    lre.eachCons(3, item -> rubyArray.add(item));
    assertEquals(ra(ra(1, 2, 3), ra(2, 3, 4)), rubyArray);
  }

  @Test
  public void testEachEntry() {
    assertSame(lre, lre.eachEntry());
  }

  @Test
  public void testEachEntryWithBlock() {
    final RubyArray<Integer> ints = ra();
    assertTrue(
        lre.eachEntry(item -> ints.add(item)) instanceof RubyLazyEnumerator);
    assertEquals(ra(1, 2, 3, 4), ints);
  }

  @Test
  public void testEachSlice() {
    assertTrue(lre.eachSlice(3) instanceof RubyLazyEnumerator);
    RubyArray<RubyArray<Integer>> ra = ra(ra(1, 2, 3), ra(4));
    assertEquals(ra, lre.eachSlice(3).toA());
  }

  @Test
  public void testEachSliceException() {
    assertThrows(IllegalArgumentException.class, () -> {
      lre.eachSlice(0);
    });
  }

  @Test
  public void testEachSliceWithBlock() {
    final RubyArray<Integer> ints = ra();
    lre.eachSlice(3, item -> ints.add(item.get(0)));
    assertEquals(ra(1, 4), ints);
  }

  @Test
  public void testEachSliceWithBlockException() {
    assertThrows(IllegalArgumentException.class, () -> {
      lre.eachSlice(0, null);
    });
  }

  @Test
  public void testEachWithIndex() {
    assertTrue(lre.eachWithIndex() instanceof RubyLazyEnumerator);
    RubyArray<? extends Entry<Integer, Integer>> ra =
        ra(hp(1, 0), hp(2, 1), hp(3, 2), hp(4, 3));
    assertEquals(ra, lre.eachWithIndex().toA());
  }

  @Test
  public void testEachWithIndexWithBlock() {
    final RubyArray<Integer> ints = ra();
    assertSame(lre, lre.eachWithIndex((item, index) -> ints.push(item, index)));
    assertEquals(ra(1, 0, 2, 1, 3, 2, 4, 3), ints);
  }

  @Test
  public void testEachWithObject() {
    Long obj = 0L;
    assertTrue(lre.eachWithObject(obj) instanceof RubyLazyEnumerator);
    RubyArray<? extends Entry<Integer, Long>> ra =
        ra(hp(1, obj), hp(2, obj), hp(3, obj), hp(4, obj));
    assertEquals(ra, lre.eachWithObject(obj).toA());
  }

  @Test
  public void testEachWithObjectWithBlock() {
    Long[] obj = new Long[] { 0L };
    assertEquals(new Long[] { 10L }[0],
        lre.eachWithObject(obj, (item, obj1) -> obj1[0] += item)[0]);
  }

  @Test
  public void testEntries() {
    assertEquals(ra(1, 2, 3, 4), lre.entries());
  }

  @Test
  public void testFind() {
    assertSame(lre, lre.find());
    assertEquals(ra(1, 2, 3, 4), lre.find().toA());
  }

  @Test
  public void testFindWithBlock() {
    Predicate<Integer> block = item -> item == 3;
    assertEquals(Integer.valueOf(3), lre.find(block));
    lre = ra(1, 2, 4, 5).lazy();
    assertNull(lre.find(block));
  }

  @Test
  public void testFindAll() {
    assertSame(lre, lre.findAll());
    assertEquals(ra(1, 2, 3, 4), lre.findAll().toA());
  }

  @Test
  public void testFindAllWithBlock() {
    assertEquals(ra(2, 3, 4), lre.findAll(item -> item >= 2).toA());
  }

  @Test
  public void testFindIndex() {
    assertSame(lre, lre.findIndex());
    assertEquals(ra(1, 2, 3, 4), lre.findIndex().toA());
  }

  @Test
  public void testFindIndexWithBlock() {
    Predicate<Integer> block = item -> item >= 4;
    assertEquals(Integer.valueOf(3), lre.findIndex(block));
    lre = ra(0, 1, 2, 3).lazy();
    assertNull(lre.findIndex(block));
  }

  @Test
  public void testFindIndexWithTarget() {
    assertEquals(Integer.valueOf(2), lre.findIndex(3));
    assertNull(lre.findIndex(0));
    lre = newRubyLazyEnumerator(Arrays.asList(1, null, 3));
    assertEquals(Integer.valueOf(1), lre.findIndex((Integer) null));
  }

  @Test
  public void testFirst() {
    assertEquals(Integer.valueOf(1), lre.first());
    lre = newRubyLazyEnumerator(new ArrayList<Integer>());
    assertNull(lre.first());
  }

  @Test
  public void testFirstWithN() {
    assertEquals(ra(), lre.first(0));
    assertEquals(ra(1, 2, 3), lre.first(3));
    assertEquals(ra(1, 2, 3, 4), lre.first(6));
  }

  @Test
  public void testFirstWithNException() {
    assertThrows(IllegalArgumentException.class, () -> {
      lre.first(-1);
    });
  }

  @Test
  public void testFlatMap() {
    assertSame(lre, lre.flatMap());
    assertEquals(ra(1, 2, 3, 4), lre.flatMap().toA());
  }

  @Test
  public void testFlatMapWithBlock() {
    assertTrue(lre
        .flatMap(item -> ra(Long.valueOf(item))) instanceof RubyLazyEnumerator);
    assertEquals(ra(1L, 2L, 3L, 4L),
        lre.flatMap(item -> ra(Long.valueOf(item))).toA());
  }

  @Test
  public void testGrep() {
    assertTrue(lre.grep("[24]") instanceof RubyLazyEnumerator);
    assertEquals(ra(2, 4), lre.grep("[24]").toA());
  }

  @Test
  public void testGrepWithBlock() {
    assertTrue(lre.grep("[24]",
        item -> item.toString()) instanceof RubyLazyEnumerator);
    assertEquals(ra("2", "4"), lre.grep("[24]", item -> item.toString()).toA());
  }

  @Test
  public void testGrepV() {
    assertTrue(lre.grepV("[24]") instanceof RubyLazyEnumerator);
    assertEquals(ra(1, 3), lre.grepV("[24]").toA());
  }

  @Test
  public void testGrepVWithBlock() {
    assertTrue(lre.grepV("[24]",
        item -> item.toString()) instanceof RubyLazyEnumerator);
    assertEquals(ra("1", "3"),
        lre.grepV("[24]", item -> item.toString()).toA());
  }

  @Test
  public void testGroupBy() {
    assertSame(lre, lre.groupBy());
    assertEquals(ra(1, 2, 3, 4), lre.groupBy().toA());
  }

  @Test
  public void testGroupByWithBlock() {
    assertEquals(rh(1, ra(1, 4), 2, ra(2), 0, ra(3)),
        lre.groupBy(item -> item % 3));
  }

  @Test
  public void testIncludeʔ() {
    assertTrue(lre.includeʔ(1));
    assertFalse(lre.includeʔ(5));
  }

  @Test
  public void testInjectWithInit() {
    RubyArray<Integer> ra = ra();
    assertEquals(ra(1, 2, 3, 4), lre.inject(ra, RubyArray::push));
  }

  @Test
  public void testInjectWithBlock() {
    BiFunction<Integer, Integer, Integer> block = (memo, item) -> memo + item;
    assertEquals(Integer.valueOf(10), lre.inject(block));
    lre = new RubyArray<Integer>().lazy();
    assertNull(lre.inject(block));
  }

  @Test
  public void testInjectWithInitAndBlock() {
    assertEquals(Long.valueOf(20),
        lre.inject(Long.valueOf(10), (init, item) -> init + item));
  }

  @Test
  public void testLazy() {
    assertTrue(lre.lazy() instanceof RubyLazyEnumerator);
  }

  @Test
  public void testMap() {
    assertSame(lre, lre.map());
    assertEquals(ra(1, 2, 3, 4), lre.map().toA());
  }

  @Test
  public void testMapWithBlock() {
    assertTrue(lre.map(block) instanceof RubyLazyEnumerator);
    assertEquals(ra(1L, 2L, 3L, 4L), lre.map(item -> Long.valueOf(item)).toA());
  }

  @Test
  public void testMax() {
    lre = ra(3, 4, 1, 2).lazy();
    assertEquals(Integer.valueOf(4), lre.max());
    lre = newRubyLazyEnumerator(new ArrayList<Integer>());
    assertNull(lre.max());
  }

  @Test
  public void testMaxWithComparator() {
    lre = ra(3, 4, 1, 2).lazy();
    assertEquals(Integer.valueOf(1), lre.max((arg0, arg1) -> arg1 - arg0));
    lre = newRubyLazyEnumerator(new ArrayList<Integer>());
    assertNull(lre.max(null));
  }

  @Test
  public void testMaxBy() {
    assertSame(lre, lre.maxBy());
    assertEquals(ra(1, 2, 3, 4), lre.maxBy().toA());
  }

  @Test
  public void testMaxByWithComparatorAndBlock() {
    RubyLazyEnumerator<String> lre =
        newRubyLazyEnumerator(Arrays.asList("aaaa", "cc", "bbb", "d"));
    assertEquals("d", lre.maxBy((o1, o2) -> o2 - o1, item -> item.length()));
    lre = newRubyLazyEnumerator(new ArrayList<String>());
    Comparator<Integer> comp = null;
    assertNull(lre.maxBy(comp, null));
  }

  @Test
  public void testMaxByWithBlock() {
    RubyLazyEnumerator<String> lre =
        newRubyLazyEnumerator(Arrays.asList("bbb", "aaaa", "cc", "d"));
    assertEquals("aaaa", lre.maxBy(item -> item.length()));
    lre = newRubyLazyEnumerator(new ArrayList<String>());
    assertNull(lre.maxBy(null));
  }

  @Test
  public void testMemberʔ() {
    assertTrue(lre.memberʔ(1));
    assertFalse(lre.memberʔ(5));
  }

  @Test
  public void testMin() {
    lre = ra(3, 4, 1, 2).lazy();
    assertEquals(Integer.valueOf(1), lre.min());
    lre = newRubyLazyEnumerator(new ArrayList<Integer>());
    assertNull(lre.min());
  }

  @Test
  public void testMinWithComparator() {
    lre = ra(3, 4, 1, 2).lazy();
    assertEquals(Integer.valueOf(4), lre.min((arg0, arg1) -> arg1 - arg0));
    lre = newRubyLazyEnumerator(new ArrayList<Integer>());
    assertNull(lre.min(null));
  }

  @Test
  public void testMinBy() {
    assertSame(lre, lre.minBy());
    assertEquals(ra(1, 2, 3, 4), lre.minBy().toA());
  }

  @Test
  public void testMinByWithComparatorAndBlock() {
    RubyLazyEnumerator<String> lre =
        newRubyLazyEnumerator(Arrays.asList("cc", "aaaa", "bbb", "d"));
    assertEquals("aaaa", lre.minBy((o1, o2) -> o2 - o1, item -> item.length()));
    lre = newRubyLazyEnumerator(new ArrayList<String>());
    Comparator<Integer> comp = null;
    assertNull(lre.minBy(comp, null));
  }

  @Test
  public void testMinByWithBlock() {
    RubyLazyEnumerator<String> lre =
        newRubyLazyEnumerator(Arrays.asList("bbb", "aaaa", "cc", "d"));
    assertEquals("d", lre.minBy(item -> item.length()));
    lre = newRubyLazyEnumerator(new ArrayList<String>());
    assertNull(lre.minBy(null));
  }

  @Test
  public void testMinmax() {
    lre = ra(2, 1, 4, 3).lazy();
    assertEquals(ra(1, 4), lre.minmax());
    lre = newRubyLazyEnumerator(Arrays.asList(1));
    assertEquals(ra(1, 1), lre.minmax());
    lre = newRubyLazyEnumerator(new ArrayList<Integer>());
    assertEquals(ra(null, null), lre.minmax());
  }

  @Test
  public void testMinmaxWithComparator() {
    lre = ra(2, 1, 4, 3).lazy();
    Comparator<Integer> comp = (o1, o2) -> o2 - o1;
    assertEquals(ra(4, 1), lre.minmax(comp));
    lre = newRubyLazyEnumerator(new ArrayList<Integer>());
    assertEquals(ra(null, null), lre.minmax(comp));
    lre = ra((Integer) null, null, null).lazy();
    assertEquals(ra(null, null), lre.minmax(comp));
  }

  @Test
  public void testMinmaxBy() {
    assertTrue(lre.minmaxBy() instanceof RubyLazyEnumerator);
    assertEquals(ra(1, 2, 3, 4), lre.minmaxBy().toA());
  }

  @Test
  public void testMinmaxByWithComparatorAndBlock() {
    RubyLazyEnumerator<String> lre =
        newRubyLazyEnumerator(Arrays.asList("bbb", "aaaa", "d", "cc"));
    assertEquals(ra("aaaa", "d"),
        lre.minmaxBy((o1, o2) -> o2 - o1, item -> item.length()));
    lre = newRubyLazyEnumerator(new ArrayList<String>());
    Comparator<Integer> comp = null;
    assertEquals(ra(null, null), lre.minmaxBy(comp, null));
    lre = ra((String) null, null, null).lazy();
    assertEquals(ra(null, null), lre.minmaxBy(comp, item -> null));
  }

  @Test
  public void testMinmaxByWithBlock() {
    RubyLazyEnumerator<String> lre =
        newRubyLazyEnumerator(Arrays.asList("bbb", "aaaa", "d", "cc"));
    assertEquals(ra("d", "aaaa"), lre.minmaxBy(item -> item.length()));
    lre = newRubyLazyEnumerator(new ArrayList<String>());
    assertEquals(ra(null, null), lre.minmaxBy(null));
  }

  @Test
  public void testNoneʔ() {
    lre = newRubyLazyEnumerator(new ArrayList<Integer>());
    assertTrue(lre.noneʔ());
    RubyArray<Integer> ra = ra();
    ra.add(null);
    lre = newRubyLazyEnumerator(ra);
    assertTrue(lre.noneʔ());
    lre = newRubyLazyEnumerator(Arrays.asList(1, 2, 3, 4));
    assertFalse(lre.noneʔ());
    lre = newRubyLazyEnumerator(Arrays.asList(1, 2, 3, null));
    assertFalse(lre.noneʔ());
    assertTrue(newRubyEnumerator(Arrays.asList(false, null)).noneʔ());
    assertFalse(newRubyEnumerator(Arrays.asList(false, null, true)).noneʔ());
  }

  @Test
  public void testNoneʔWithBlock() {
    assertTrue(lre.noneʔ(item -> item > 5));
    assertFalse(lre.noneʔ(item -> item == 3));
  }

  @Test
  public void testOneʔ() {
    lre = newRubyLazyEnumerator(Arrays.asList(1));
    assertTrue(lre.oneʔ());
    lre = newRubyLazyEnumerator(Arrays.asList(null, null, 1));
    assertTrue(lre.oneʔ());
    lre = newRubyLazyEnumerator(Arrays.asList(1, 2));
    assertFalse(lre.oneʔ());
    List<Integer> ints = new ArrayList<Integer>();
    lre = newRubyLazyEnumerator(ints);
    assertFalse(lre.oneʔ());
    assertFalse(newRubyEnumerator(Arrays.asList(false, null)).oneʔ());
    assertTrue(newRubyEnumerator(Arrays.asList(false, null, true)).oneʔ());
    assertFalse(newRubyEnumerator(Arrays.asList(null, true, true)).oneʔ());
  }

  @Test
  public void testOneʔWithBlock() {
    assertTrue(lre.oneʔ(item -> item > 3));
    assertFalse(lre.oneʔ(item -> item > 2));
    lre = newRubyLazyEnumerator(new ArrayList<Integer>());
    assertFalse(lre.oneʔ(null));
  }

  @Test
  public void testPartition() {
    assertSame(lre, lre.partition());
    assertEquals(ra(1, 2, 3, 4), lre.partition().toA());
  }

  @Test
  public void testPartitionWithBlock() {
    assertEquals(ra(ra(1, 3), ra(2, 4)), lre.partition(item -> item % 2 == 1));
  }

  @Test
  public void testReduceWithInit() {
    RubyArray<Integer> ra = ra();
    assertEquals(ra(1, 2, 3, 4), lre.reduce(ra, RubyArray::push));
  }

  @Test
  public void testReduceWithBlock() {
    assertEquals(Integer.valueOf(10), lre.reduce((memo, item) -> memo + item));
  }

  @Test
  public void testReduceWithInitAndBlock() {
    assertEquals(Long.valueOf(20),
        lre.reduce(Long.valueOf(10), (init, item) -> init + item));
  }

  @Test
  public void testReduce() {
    RubyLazyEnumerator<Boolean> bools =
        newRubyLazyEnumerator(Arrays.asList(true, true, true));
    assertEquals(Boolean.TRUE, bools.reduce(Boolean::equals));
  }

  @Test
  public void testReject() {
    assertSame(lre, lre.reject());
    assertEquals(ra(1, 2, 3, 4), lre.reject().toA());
  }

  @Test
  public void testRejectWithBlock() {
    assertTrue(lre.reject(item -> item == 1) instanceof RubyLazyEnumerator);
    assertEquals(ra(2, 3, 4), lre.reject(item -> item == 1).toA());
  }

  @Test
  public void testReverseEach() {
    assertTrue(lre.cycle().reverseEach() instanceof RubyLazyEnumerator);
    assertEquals(ra(4, 3, 2, 1), lre.reverseEach().toA());
    assertEquals(ra(1, 2, 3, 4).toA(), lre.toA());
  }

  @Test
  public void testReverseEachWithBlock() {
    final RubyArray<Integer> ints = ra();
    assertTrue(
        lre.reverseEach(item -> ints.add(item)) instanceof RubyLazyEnumerator);
    assertEquals(ra(4, 3, 2, 1), ints);
  }

  @Test
  public void testSelect() {
    assertSame(lre, lre.select());
    assertEquals(ra(1, 2, 3, 4), lre.select().toA());
  }

  @Test
  public void testSelectWithBlock() {
    Predicate<Integer> block = item -> item == 3;
    assertTrue(lre.select(block) instanceof RubyLazyEnumerator);
    assertEquals(ra(3), lre.select(block).toA());
  }

  @Test
  public void testSliceAfter() {
    Predicate<Integer> block = item -> item == 3;
    assertTrue(lre.sliceAfter(block) instanceof RubyLazyEnumerator);
    assertEquals(ra(ra(1, 2, 3), ra(4)), lre.sliceAfter(block).toA());
  }

  @Test
  public void testSliceAfterWithBlock() {
    lre = newRubyLazyEnumerator(Arrays.asList(1, 3, 4, 7));
    assertEquals(ra(ra(1), ra(3), ra(4, 7)),
        lre.sliceAfter(item -> item % 2 == 1).toA());
  }

  @Test
  public void testSliceAfterWithRegex() {
    assertTrue(lre.sliceAfter("") instanceof RubyLazyEnumerator);
    lre = newRubyLazyEnumerator(Arrays.asList(1, 2, 3, 3));
    assertEquals(ra(ra(1, 2, 3), ra(3)), lre.sliceAfter("3").toA());
  }

  @Test
  public void testSliceBefore() {
    Predicate<Integer> block = item -> item == 3;
    assertTrue(lre.sliceBefore(block) instanceof RubyLazyEnumerator);
    assertEquals(ra(ra(1, 2), ra(3, 4)), lre.sliceBefore(block).toA());
  }

  @Test
  public void testSliceBeforeWithBlock() {
    lre = newRubyLazyEnumerator(Arrays.asList(1, 3, 3, 4));
    assertEquals(ra(ra(1), ra(3), ra(3, 4)),
        lre.sliceBefore(item -> item % 2 == 1).toA());
  }

  @Test
  public void testSliceBeforeWithRegex() {
    assertTrue(lre.sliceBefore("") instanceof RubyLazyEnumerator);
    lre = newRubyLazyEnumerator(Arrays.asList(1, 2, 3, 3));
    assertEquals(ra(ra(1, 2), ra(3), ra(3)), lre.sliceBefore("3").toA());
  }

  @Test
  public void testSliceWhen() {
    BiPredicate<Integer, Integer> block = (item1, item2) -> item1 + 1 == item2;
    lre = newRubyLazyEnumerator(Arrays.asList(1, 3, 3, 4));
    assertTrue(lre.sliceWhen(block) instanceof RubyLazyEnumerator);
    assertEquals(ra(ra(1, 3, 3), ra(4)), lre.sliceWhen(block).toA());
  }

  @Test
  public void testSort() {
    lre = newRubyLazyEnumerator(Arrays.asList(4, 1, 2, 3, 3));
    assertEquals(ra(1, 2, 3, 3, 4), lre.sort());
    assertEquals(ra("abc", "b", "cd"),
        newRubyEnumerator(Arrays.asList("b", "cd", "abc")).sort());
    assertEquals(ra(null, null, null),
        newRubyEnumerator(ra(null, null, null)).sort());
    lre = newRubyLazyEnumerator(Arrays.asList(1));
    assertEquals(ra(1), lre.sort());
  }

  @Test
  public void testSortBy() {
    assertTrue(lre.sortBy() instanceof RubyLazyEnumerator);
    assertEquals(ra(1, 2, 3, 4), lre.sortBy().toA());
  }

  @Test
  public void testSortByWithComparatorAndBlock() {
    RubyLazyEnumerator<String> lre =
        newRubyLazyEnumerator(Arrays.asList("aaaa", "bbb", "cc", "e", "d"));
    assertEquals(ra("aaaa", "bbb", "cc", "e", "d"),
        lre.sortBy((o1, o2) -> o2 - o1, item -> item.length()));
  }

  @Test
  public void testSortByWith2ComparatorsAndBlock() {
    RubyLazyEnumerator<String> lre =
        newRubyLazyEnumerator(Arrays.asList("aaaa", "bbb", "cc", "d", "e"));
    assertEquals(ra("aaaa", "bbb", "cc", "e", "d"),
        lre.sortBy((o1, o2) -> o2.compareTo(o1), (o1, o2) -> o2 - o1,
            item -> item.length()));
  }

  @Test
  public void testSortByWithBlock() {
    RubyLazyEnumerator<String> lre =
        newRubyLazyEnumerator(Arrays.asList("aaaa", "bbb", "cc", "e", "d"));
    assertEquals(ra("e", "d", "cc", "bbb", "aaaa"),
        lre.sortBy(item -> item.length()));
  }

  @Test
  public void testSum() {
    assertEquals(new BigDecimal(10), lre.sum());
  }

  @Test
  public void testSumException() {
    assertThrows(ClassCastException.class, () -> {
      Ruby.LazyEnumerator.of(ra("a", "b", "c")).sum();
    });
  }

  @Test
  public void testSumWithBlock() {
    RubyLazyEnumerator<Double> lre =
        Ruby.LazyEnumerator.of(Arrays.asList(1.1, 2.1, 3.1, 4.1));
    assertEquals(new BigDecimal("20.8"), lre.sum(n -> n.doubleValue() * 2));
  }

  @Test
  public void testSumWithInit() {
    RubyLazyEnumerator<Double> lre =
        Ruby.LazyEnumerator.of(Arrays.asList(1.1, 2.1, 3.1, 4.1));
    assertEquals(new BigDecimal("11.4"), lre.sum(1L));
  }

  @Test
  public void testSumWithInitAndBlock() {
    RubyLazyEnumerator<Double> lre =
        Ruby.LazyEnumerator.of(Arrays.asList(1.1, 2.1, 3.1, 4.1));
    assertEquals(new BigDecimal("21.8"), lre.sum(1L, n -> n.doubleValue() * 2));
  }

  @Test
  public void testTake() {
    assertTrue(lre.take(0) instanceof RubyLazyEnumerator);
    assertEquals(ra(), lre.take(0).toA());
    assertEquals(ra(1, 2), lre.take(2).toA());
    assertEquals(ra(1, 2, 3, 4), lre.take(5).toA());
  }

  @Test
  public void testTakeException() {
    assertThrows(IllegalArgumentException.class, () -> {
      lre.take(-1);
    });
  }

  @Test
  public void testTakeWhile() {
    assertTrue(lre.takeWhile() instanceof RubyLazyEnumerator);
    assertEquals(ra(1), lre.takeWhile().toA());
    lre = new RubyLazyEnumerator<Integer>(new ArrayList<Integer>());
    assertEquals(ra(), lre.takeWhile().toA());
  }

  @Test
  public void testTakeWhileWithBlock() {
    Predicate<Integer> block = item -> item != 3;
    assertTrue(lre.takeWhile(block) instanceof RubyLazyEnumerator);
    assertEquals(ra(1, 2), lre.takeWhile(block).toA());
    lre = ra(1, 1, 1, 1).lazy();
    assertEquals(ra(1, 1, 1, 1), lre.takeWhile(block).toA());
  }

  @Test
  public void testToA() {
    assertEquals(ra(1, 2, 3, 4), lre.toA());
  }

  @Test
  public void testToList() {
    assertEquals(Arrays.asList(1, 2, 3, 4), lre.toList());
    assertTrue(lre.toList() instanceof ArrayList);
  }

  @Test
  public void testToSet() {
    assertEquals(new LinkedHashSet<>(Arrays.asList(1, 2, 3, 4)), lre.toSet());
    list = new ArrayList<Integer>(Arrays.asList(1, 2, 2, 3, 3, 3));
    lre = new RubyLazyEnumerator<Integer>(list);
    assertEquals(new LinkedHashSet<>(Arrays.asList(1, 2, 3)), lre.toSet());
    assertTrue(lre.toSet() instanceof LinkedHashSet);
  }

  @Test
  public void testToH() {
    List<List<Integer>> pairs = new ArrayList<List<Integer>>();
    pairs.add(new ArrayList<Integer>() {

      private static final long serialVersionUID = 1L;

      {
        add(1);
        add(2);
      }
    });
    pairs.add(new ArrayList<Integer>() {

      private static final long serialVersionUID = 1L;

      {
        add(3);
        add(4);
      }
    });
    assertEquals(rh(1, 2, 3, 4),
        newRubyLazyEnumerator(pairs).toH(ary -> hp(ary.get(0), ary.get(1))));
    assertEquals(Ruby.Hash.of(ra(1, 2), ra(3, 4)),
        newRubyLazyEnumerator(pairs).toH((e1, e2) -> hp(e1, e2)));
  }

  @Test
  public void testZip() {
    lre = newRubyLazyEnumerator(Arrays.asList(1, 2, 3));
    assertTrue(lre.zip(ra(ra(4, 5))) instanceof RubyLazyEnumerator);
    assertEquals(ra(ra(1, 4), ra(2, 5), ra(3, null)),
        lre.zip(ra(ra(4, 5))).toA());
    assertEquals(ra(ra(1, 4, 7), ra(2, 5, 8), ra(3, 6, 9)),
        lre.zip(ra(ra(4, 5, 6), ra(7, 8, 9))).toA());
  }

  @Test
  public void testZipWithBlock() {
    lre = newRubyLazyEnumerator(Arrays.asList(1, 2, 3));
    final RubyArray<Integer> ints = ra();
    lre.zip(ra(ra(4, 5, 6, null), ra(7, 8, 9, null)),
        (Consumer<RubyArray<Integer>>) item -> ints
            .push(item.reduce((memo, item1) -> memo + item1)));
    assertEquals(ra(12, 15, 18), ints);
  }

  @Test
  public void testRewind() {
    lre.rewind();
    lre = new RubyLazyEnumerator<Integer>(list);
    while (lre.hasNext()) {
      lre.next();
    }
    lre.rewind();
    assertTrue(lre.hasNext());
  }

  @Test
  public void testPeek() {
    while (lre.hasNext()) {
      Integer peeking = lre.peek();
      assertEquals(peeking, lre.next());
    }
    lre = new RubyLazyEnumerator<Integer>(list);
    assertEquals(Integer.valueOf(1), lre.peek());
  }

  @Test
  public void testRemove() {
    List<Integer> ints = new ArrayList<Integer>(list);
    Iterator<Integer> intsIt = ints.iterator();
    lre.next();
    lre.remove();
    intsIt.next();
    intsIt.remove();
    assertEquals(ints, list);
  }

  @Test
  public void testToString() {
    assertEquals("RubyLazyEnumerator{[1, 2, 3, 4]}", lre.toString());
  }

}
