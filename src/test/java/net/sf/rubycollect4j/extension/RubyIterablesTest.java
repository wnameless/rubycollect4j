/*
 *
 * Copyright 2016 Wei-Ming Wu
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
package net.sf.rubycollect4j.extension;

import static net.sf.rubycollect4j.RubyCollections.hp;
import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.newRubyEnumerator;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.rh;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;

import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.RubyEnumerable;
import net.sf.rubycollect4j.RubyLazyEnumerator;

public class RubyIterablesTest {

  Iterable<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter = newRubyArray(1, 2, 3, 4);
  }

  @Test
  public void testPrivateConstructor() throws Exception {
    Constructor<RubyIterables> c = RubyIterables.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c.getModifiers()));
    c.setAccessible(true);
    c.newInstance();
  }

  @Test
  public void testAllʔ() {
    assertTrue(RubyIterables.allʔ(iter));
    iter = new ArrayList<Integer>();
    assertTrue(RubyIterables.allʔ(iter));
    iter = Arrays.asList(1, 2, null);
    assertFalse(RubyIterables.allʔ(iter));
    assertFalse(RubyIterables.allʔ(Arrays.asList(false, true)));
    assertFalse(RubyIterables.allʔ(Arrays.asList(null, true)));
    assertTrue(RubyIterables.allʔ(Arrays.asList(true, true)));
  }

  @Test
  public void testAllʔWithBlock() {
    Predicate<Integer> block = item -> item % 2 == 0;
    assertFalse(RubyIterables.allʔ(iter, block));
    iter = ra(2, 4, 6, 8);
    assertTrue(RubyIterables.allʔ(iter, block));
  }

  @Test
  public void testAnyʔ() {
    assertTrue(RubyIterables.anyʔ(iter));
    iter = new ArrayList<Integer>();
    assertFalse(RubyIterables.anyʔ(iter));
    iter = Arrays.asList(1, 2, null);
    assertTrue(RubyIterables.anyʔ(iter));
    iter = Arrays.asList(null, null);
    assertFalse(RubyIterables.anyʔ(iter));
    assertFalse(RubyIterables.anyʔ(Arrays.asList(false, null)));
    assertTrue(RubyIterables.anyʔ(Arrays.asList(false, null, true)));
  }

  @Test
  public void testAnyʔWithBlock() {
    Predicate<Integer> block = item -> item % 2 == 0;
    assertTrue(RubyIterables.anyʔ(iter, block));
    iter = ra(1, 3, 5, 7);
    assertFalse(RubyIterables.anyʔ(iter, block));
  }

  @Test
  public void testChunk() {
    iter = Arrays.asList(1, 2, 2, 3);
    RubyArray<Entry<Boolean, RubyArray<Integer>>> chunk =
        (RubyArray<Entry<Boolean, RubyArray<Integer>>>) RubyIterables
            .toA(RubyIterables.chunk(iter, item -> item % 2 == 0));
    assertEquals(hp(false, newRubyArray(1)).toString(),
        chunk.get(0).toString());
    assertEquals(hp(true, newRubyArray(2, 2)).toString(),
        chunk.get(1).toString());
    assertEquals(hp(false, newRubyArray(3)).toString(),
        chunk.get(2).toString());
    assertEquals(3, chunk.size());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testChunkWhile() {
    Iterable<Integer> re =
        Arrays.asList(1, 2, 4, 9, 10, 11, 12, 15, 16, 19, 20, 21);
    assertEquals(
        ra(ra(1, 2), ra(4), ra(9, 10, 11, 12), ra(15, 16), ra(19, 20, 21)),
        RubyIterables.toA(
            RubyIterables.chunkWhile(re, (key, value) -> key + 1 == value)));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testChunkWithMethodName() {
    Iterable<String> iter = Arrays.asList("aa", "bb", "bc", "cd");
    Iterable<Entry<Character, RubyArray<String>>> chunk =
        RubyIterables.chunk(iter, item -> item.charAt(0));
    assertEquals(
        ra(hp('a', ra("aa")), hp('b', ra("bb", "bc")), hp('c', ra("cd"))),
        RubyIterables.toA(chunk));
  }

  @Test
  public void testCollectWithBlock() {
    assertEquals(ra(1.0, 2.0, 3.0, 4.0),
        RubyIterables.collect(iter, item -> Double.valueOf(item)));
  }

  @Test
  public void testCollectConcatWithBlock() {
    assertEquals(ra(1.0, 2.0, 3.0, 4.0),
        RubyIterables.collectConcat(iter, item -> ra(Double.valueOf(item))));
  }

  @Test
  public void testCount() {
    assertEquals(4, RubyIterables.count(iter));
    iter = new ArrayList<Integer>();
    assertEquals(0, RubyIterables.count(iter));
  }

  @Test
  public void testCountWithBlock() {
    assertEquals(2, RubyIterables.count(iter, item -> item % 2 == 1));
  }

  @Test
  public void testCycle() {
    RubyArray<Integer> ints = ra(1, 2, 3, 4);
    Iterator<Integer> iter2 = RubyIterables.cycle(iter).iterator();
    for (int i = 0; i < 100; i++) {
      assertEquals(ints.get(0), iter2.next());
      ints.rotateǃ();
    }
  }

  @Test
  public void testCycleWithN() {
    RubyArray<Integer> ints = newRubyArray();
    Iterator<Integer> iter2 = RubyIterables.cycle(iter, 2).iterator();
    while (iter2.hasNext()) {
      ints.push(iter2.next());
    }
    assertEquals(ra(1, 2, 3, 4, 1, 2, 3, 4), RubyIterables.toA(ints));
  }

  @Test
  public void testCycleWithNAndBlock() {
    final RubyArray<Integer> ints = newRubyArray();
    RubyIterables.cycle(iter, 2, item -> ints.push(item * 2));
    assertEquals(ra(2, 4, 6, 8, 2, 4, 6, 8), ints);
  }

  @Test(expected = IllegalStateException.class)
  public void testCycleWithBlock() {
    final RubyArray<Integer> ints = newRubyArray();
    RubyIterables.cycle(iter, item -> {
      ints.push(item);
      if (ints.size() > 1000) throw new IllegalStateException();
    });
  }

  @Test
  public void testDetectWithBlock() {
    Predicate<Integer> block = item -> item == 3;
    assertEquals(Integer.valueOf(3), RubyIterables.detect(iter, block));
    iter = ra(1, 2, 4, 5);
    assertNull(RubyIterables.detect(iter, block));
  }

  @Test
  public void testDrop() {
    assertEquals(ra(1, 2, 3, 4), RubyIterables.drop(iter, 0));
    assertEquals(ra(3, 4), RubyIterables.drop(iter, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDropException() {
    RubyIterables.drop(iter, -1);
  }

  @Test
  public void testDropWhileWithBlock() {
    iter = Arrays.asList(1, 2, 3, 1);
    assertEquals(ra(3, 1), RubyIterables.dropWhile(iter, item -> item < 3));
  }

  @Test
  public void testEachWithBlock() {
    final RubyArray<Integer> ints = ra();
    RubyIterables.each(iter, item -> ints.push(item * 2));
    assertEquals(ra(2, 4, 6, 8), ints);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEachConsException() {
    RubyIterables.eachCons(iter, 0);
  }

  @Test
  public void testEachConsWithBlock() {
    final RubyArray<List<Integer>> ra = ra();
    RubyIterables.eachCons(iter, 2, item -> ra.push(item));
    assertEquals(ra, RubyIterables.toA(RubyIterables.eachCons(iter, 2)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEachConsWithBlockException() {
    RubyIterables.eachCons(iter, 0, null);
  }

  @Test
  public void testEachEntryWithBlock() {
    final RubyArray<Integer> ints = ra();
    RubyIterables.eachEntry(iter, item -> ints.add(item * 2));
    assertEquals(ra(2, 4, 6, 8), ints);
    assertTrue(
        RubyIterables.eachEntry(iter, item -> {}) instanceof RubyEnumerable);
  }

  @Test
  public void testEachSlice() {
    @SuppressWarnings("unchecked")
    RubyArray<RubyArray<Integer>> ra = ra(ra(1, 2, 3), ra(4));
    assertEquals(ra, RubyIterables.toA(RubyIterables.eachSlice(iter, 3)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEachSliceException() {
    RubyIterables.eachSlice(iter, 0);
  }

  @Test
  public void testEachSliceWithBlock() {
    final RubyArray<Integer> ints = ra();
    RubyIterables.eachSlice(iter, 3, item -> ints.add(item.get(0)));
    assertEquals(ra(1, 4), ints);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testEachSliceWithBlockException() {
    RubyIterables.eachSlice(iter, 0, null);
  }

  @Test
  public void testEachWithIndex() {
    @SuppressWarnings("unchecked")
    RubyArray<? extends Entry<Integer, Integer>> ra =
        ra(hp(1, 0), hp(2, 1), hp(3, 2), hp(4, 3));
    assertEquals(ra, RubyIterables.toA(RubyIterables.eachWithIndex(iter)));
  }

  @Test
  public void testEachWithIndexWithBlock() {
    final RubyArray<Integer> ints = ra();
    assertTrue(RubyIterables.eachWithIndex(iter,
        (item, index) -> ints.add(item + index)) instanceof RubyEnumerable);
    assertEquals(ra(1, 3, 5, 7), ints);
  }

  @Test
  public void testEachWithObject() {
    Long obj = 0L;
    @SuppressWarnings("unchecked")
    RubyArray<? extends Entry<Integer, Long>> ra =
        ra(hp(1, obj), hp(2, obj), hp(3, obj), hp(4, obj));
    assertEquals(ra,
        RubyIterables.toA(RubyIterables.eachWithObject(iter, obj)));
  }

  @Test
  public void testEachWithObjectWithBlock() {
    Long[] obj = new Long[] { 0L };
    assertEquals(new Long[] { 10L }[0], RubyIterables.eachWithObject(iter, obj,
        (item, obj1) -> obj1[0] += item)[0]);
  }

  @Test
  public void testEntries() {
    assertEquals(ra(1, 2, 3, 4), RubyIterables.entries(iter));
  }

  @Test
  public void testFindWithBlock() {
    Predicate<Integer> block = item -> item == 3;
    assertEquals(Integer.valueOf(3), RubyIterables.find(iter, block));
    iter = ra(1, 2, 4, 5);
    assertNull(RubyIterables.find(iter, block));
  }

  @Test
  public void testFindAllWithBlock() {
    assertEquals(ra(2, 3, 4), RubyIterables.findAll(iter, item -> item >= 2));
  }

  @Test
  public void testFindIndexWithBlock() {
    Predicate<Integer> block = item -> item >= 4;
    assertEquals(Integer.valueOf(3), RubyIterables.findIndex(iter, block));
    iter = ra(0, 1, 2, 3);
    assertNull(RubyIterables.findIndex(iter, block));
  }

  @Test
  public void testFindIndexWithTarget() {
    assertEquals(Integer.valueOf(2), RubyIterables.findIndex(iter, 3));
    assertNull(RubyIterables.findIndex(iter, 0));
    iter = Arrays.asList(1, null, 3);
    assertEquals(Integer.valueOf(1),
        RubyIterables.findIndex(iter, (Integer) null));
  }

  @Test
  public void testFirst() {
    assertEquals(Integer.valueOf(1), RubyIterables.first(iter));
    iter = new ArrayList<Integer>();
    assertNull(RubyIterables.first(iter));
  }

  @Test
  public void testFirstWithN() {
    assertEquals(ra(), RubyIterables.first(iter, 0));
    assertEquals(ra(1, 2, 3), RubyIterables.first(iter, 3));
    assertEquals(ra(1, 2, 3, 4), RubyIterables.first(iter, 6));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFirstWithNException() {
    RubyIterables.first(iter, -1);
  }

  @Test
  public void testFlatMapWithBlock() {
    assertEquals(ra(1L, 2L, 3L, 4L),
        RubyIterables.flatMap(iter, item -> ra(Long.valueOf(item))));
  }

  @Test
  public void testGrep() {
    assertEquals(ra(2, 4), RubyIterables.grep(iter, "[24]"));
  }

  @Test
  public void testGrepWithBlock() {
    assertEquals(ra("2", "4"),
        RubyIterables.grep(iter, "[24]", item -> item.toString()));
  }

  @Test
  public void testGrepV() {
    assertEquals(ra(1, 3), RubyIterables.grepV(iter, "[24]"));
  }

  @Test
  public void testGrepVWithBlock() {
    assertEquals(ra("1", "3"),
        RubyIterables.grepV(iter, "[24]", item -> item.toString()));
  }

  @Test
  public void testGroupByWithBlock() {
    assertEquals(rh(1, ra(1, 4), 2, ra(2), 0, ra(3)),
        RubyIterables.groupBy(iter, item -> item % 3));
  }

  @Test
  public void testIncludeʔ() {
    assertTrue(RubyIterables.includeʔ(iter, 1));
    assertFalse(RubyIterables.includeʔ(iter, 5));
  }

  @Test
  public void testInjectWithBlock() {
    BiFunction<Integer, Integer, Integer> block = (memo, item) -> memo + item;
    assertEquals(Integer.valueOf(10), RubyIterables.inject(iter, block));
    iter = ra();
    assertNull(RubyIterables.inject(iter, block));
  }

  @Test
  public void testInjectWithInitAndBlock() {
    assertEquals(Long.valueOf(20), RubyIterables.inject(iter, Long.valueOf(10),
        (init, item) -> init + item));
  }

  @Test
  public void testLazy() {
    assertTrue(RubyIterables.lazy(iter) instanceof RubyLazyEnumerator);
  }

  @Test
  public void testMapWithBlock() {
    assertEquals(ra(1L, 2L, 3L, 4L),
        RubyIterables.map(iter, item -> Long.valueOf(item)));
  }

  @Test
  public void testMax() {
    iter = ra(3, 4, 1, 2);
    assertEquals(Integer.valueOf(4), RubyIterables.max(iter));
    iter = new ArrayList<Integer>();
    assertNull(RubyIterables.max(iter));
  }

  @Test
  public void testMaxWithComparator() {
    iter = ra(3, 4, 1, 2);
    assertEquals(Integer.valueOf(1),
        RubyIterables.max(iter, (arg0, arg1) -> arg1 - arg0));
    iter = new ArrayList<Integer>();
    assertNull(RubyIterables.max(iter, null));
  }

  @Test
  public void testMaxByWithComparatorAndBlock() {
    Iterable<String> iter = Arrays.asList("aaaa", "cc", "bbb", "d");
    assertEquals("d",
        RubyIterables.maxBy(iter, (o1, o2) -> o2 - o1, item -> item.length()));
    iter = new ArrayList<String>();
    Comparator<Integer> comp = null;
    assertNull(RubyIterables.maxBy(iter, comp, null));
  }

  @Test
  public void testMaxByWithBlock() {
    Iterable<String> iter = Arrays.asList("bbb", "aaaa", "cc", "d");
    assertEquals("aaaa", RubyIterables.maxBy(iter, item -> item.length()));
    iter = new ArrayList<String>();
    assertNull(RubyIterables.maxBy(iter, null));
  }

  @Test
  public void testMemberʔ() {
    assertTrue(RubyIterables.memberʔ(iter, 1));
    assertFalse(RubyIterables.memberʔ(iter, 5));
  }

  @Test
  public void testMin() {
    iter = ra(3, 4, 1, 2);
    assertEquals(Integer.valueOf(1), RubyIterables.min(iter));
    iter = new ArrayList<Integer>();
    assertNull(RubyIterables.min(iter));
  }

  @Test
  public void testMinWithComparator() {
    iter = ra(3, 4, 1, 2);
    assertEquals(Integer.valueOf(4),
        RubyIterables.min(iter, (arg0, arg1) -> arg1 - arg0));
    iter = new ArrayList<Integer>();
    assertNull(RubyIterables.min(iter, null));
  }

  @Test
  public void testMinByWithComparatorAndBlock() {
    Iterable<String> iter = Arrays.asList("cc", "aaaa", "bbb", "d");
    assertEquals("aaaa",
        RubyIterables.minBy(iter, (o1, o2) -> o2 - o1, item -> item.length()));
    iter = new ArrayList<String>();
    Comparator<Integer> comp = null;
    assertNull(RubyIterables.minBy(iter, comp, null));
  }

  @Test
  public void testMinByWithBlock() {
    Iterable<String> iter = Arrays.asList("bbb", "aaaa", "cc", "d");
    assertEquals("d", RubyIterables.minBy(iter, item -> item.length()));
    iter = new ArrayList<String>();
    assertNull(RubyIterables.minBy(iter, null));
  }

  @Test
  public void testMinmax() {
    iter = ra(2, 1, 4, 3);
    assertEquals(ra(1, 4), RubyIterables.minmax(iter));
    iter = Arrays.asList(1);
    assertEquals(ra(1, 1), RubyIterables.minmax(iter));
    iter = new ArrayList<Integer>();
    assertEquals(ra(null, null), RubyIterables.minmax(iter));
  }

  @Test
  public void testMinmaxWithComparator() {
    iter = ra(2, 1, 4, 3);
    Comparator<Integer> comp = (o1, o2) -> o2 - o1;
    assertEquals(ra(4, 1), RubyIterables.minmax(iter, comp));
    iter = new ArrayList<Integer>();
    assertEquals(ra(null, null), RubyIterables.minmax(iter, comp));
    iter = ra(null, null, null);
    assertEquals(ra(null, null), RubyIterables.minmax(iter, comp));
  }

  @Test
  public void testMinmaxByWithComparatorAndBlock() {
    Iterable<String> iter = Arrays.asList("bbb", "aaaa", "d", "cc");
    assertEquals(ra("aaaa", "d"), RubyIterables.minmaxBy(iter,
        (o1, o2) -> o2 - o1, item -> item.length()));
    iter = new ArrayList<String>();
    Comparator<Integer> comp = null;
    assertEquals(ra(null, null), RubyIterables.minmaxBy(iter, comp, null));
    iter = ra(null, null, null);
    assertEquals(ra(null, null),
        RubyIterables.minmaxBy(iter, comp, item -> null));
  }

  @Test
  public void testMinmaxByWithBlock() {
    Iterable<String> iter = Arrays.asList("bbb", "aaaa", "d", "cc");
    assertEquals(ra("d", "aaaa"),
        RubyIterables.minmaxBy(iter, item -> item.length()));
    iter = new ArrayList<String>();
    assertEquals(ra(null, null), RubyIterables.minmaxBy(iter, null));
  }

  @Test
  public void testNoneʔ() {
    iter = new ArrayList<Integer>();
    assertTrue(RubyIterables.noneʔ(iter));
    RubyArray<Integer> ra = ra();
    ra.add(null);
    iter = ra;
    assertTrue(RubyIterables.noneʔ(iter));
    iter = Arrays.asList(1, 2, 3, 4);
    assertFalse(RubyIterables.noneʔ(iter));
    iter = Arrays.asList(1, 2, 3, null);
    assertFalse(RubyIterables.noneʔ(iter));
    assertTrue(RubyIterables.noneʔ(Arrays.asList(false, null)));
    assertFalse(RubyIterables.noneʔ(Arrays.asList(false, null, true)));
  }

  @Test
  public void testNoneʔWithBlock() {
    assertTrue(RubyIterables.noneʔ(iter, item -> item > 5));
    assertFalse(RubyIterables.noneʔ(iter, item -> item == 3));
  }

  @Test
  public void testOneʔ() {
    iter = Arrays.asList(1);
    assertTrue(RubyIterables.oneʔ(iter));
    iter = Arrays.asList(null, null, 1);
    assertTrue(RubyIterables.oneʔ(iter));
    iter = Arrays.asList(1, 2);
    assertFalse(RubyIterables.oneʔ(iter));
    List<Integer> ints = new ArrayList<Integer>();
    iter = newRubyEnumerator(ints);
    assertFalse(RubyIterables.oneʔ(iter));
    assertFalse(RubyIterables.oneʔ(Arrays.asList(false, null)));
    assertTrue(RubyIterables.oneʔ(Arrays.asList(false, null, true)));
    assertFalse(RubyIterables.oneʔ(Arrays.asList(null, true, true)));
  }

  @Test
  public void testOneʔWithBlock() {
    assertTrue(RubyIterables.oneʔ(iter, item -> item > 3));
    assertFalse(RubyIterables.oneʔ(iter, item -> item > 2));
    iter = new ArrayList<Integer>();
    assertFalse(RubyIterables.oneʔ(iter, null));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testPartitionWithBlock() {
    assertEquals(ra(ra(1, 3), ra(2, 4)),
        RubyIterables.partition(iter, item -> item % 2 == 1));
  }

  @Test
  public void testReduceWithBlock() {
    assertEquals(Integer.valueOf(10),
        RubyIterables.reduce(iter, (memo, item) -> memo + item));
  }

  @Test
  public void testReduceWithInitAndBlock() {
    assertEquals(Long.valueOf(20), RubyIterables.reduce(iter, Long.valueOf(10),
        (init, item) -> init + item));
  }

  @Test
  public void testRejectWithBlock() {
    assertEquals(ra(2, 3, 4), RubyIterables.reject(iter, item -> item == 1));
  }

  @Test
  public void testReverseEach() {
    assertEquals(ra(4, 3, 2, 1),
        RubyIterables.toA(RubyIterables.reverseEach(iter)));
    assertEquals(ra(1, 2, 3, 4).toA(), RubyIterables.toA(iter));
  }

  @Test
  public void testReverseEachWithBlock() {
    final RubyArray<Integer> ra = ra();
    assertEquals(iter, RubyIterables.reverseEach(iter, item -> ra.push(item)));
    assertEquals(ra(4, 3, 2, 1), ra);
    assertEquals(ra(1, 2, 3, 4), RubyIterables.toA(iter));
  }

  @Test
  public void testSelectBlock() {
    assertEquals(ra(3, 4), RubyIterables.select(iter, item -> item >= 3));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSliceAfterWithBlock() {
    iter = Arrays.asList(1, 3, 3, 4);
    assertEquals(ra(ra(1), ra(3), ra(3), ra(4)), RubyIterables
        .toA(RubyIterables.sliceAfter(iter, item -> item % 2 == 1)));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSliceWhen() {
    iter = newRubyEnumerator(Arrays.asList(1, 3, 3, 4));
    assertEquals(ra(ra(1, 3, 3), ra(4)), RubyIterables.toA(
        RubyIterables.sliceWhen(iter, (item1, item2) -> item1 + 1 == item2)));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSliceAfterWithRegex() {
    iter = Arrays.asList(1, 2, 3, 3);
    assertEquals(ra(ra(1, 2, 3), ra(3)),
        RubyIterables.toA(RubyIterables.sliceAfter(iter, "3")));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSliceBeforeWithBlock() {
    iter = newRubyEnumerator(Arrays.asList(1, 3, 3, 4));
    assertEquals(ra(ra(1), ra(3), ra(3, 4)), RubyIterables
        .toA(RubyIterables.sliceBefore(iter, item -> item % 2 == 1)));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSliceBeforeWithRegex() {
    iter = Arrays.asList(1, 2, 3, 3);
    assertEquals(ra(ra(1, 2), ra(3), ra(3)),
        RubyIterables.toA(RubyIterables.sliceBefore(iter, "3")));
  }

  @Test
  public void testSort() {
    iter = newRubyEnumerator(Arrays.asList(4, 1, 2, 3, 3));
    assertEquals(ra(1, 2, 3, 3, 4), RubyIterables.sort(iter));
    assertEquals(ra("abc", "b", "cd"),
        RubyIterables.sort(Arrays.asList("b", "cd", "abc")));
    assertEquals(ra(null, null, null),
        RubyIterables.sort(ra(null, null, null)));
    iter = Arrays.asList(1);
    assertEquals(ra(1), RubyIterables.sort(iter));
  }

  @Test
  public void testSortByWithComparatorAndBlock() {
    Iterable<String> re = Arrays.asList("aaaa", "bbb", "cc", "e", "d");
    assertEquals(ra("aaaa", "bbb", "cc", "e", "d"),
        RubyIterables.sortBy(re, (o1, o2) -> o2 - o1, item -> item.length()));
  }

  @Test
  public void testSortByWith2ComparatorsAndBlock() {
    Iterable<String> re = Arrays.asList("aaaa", "bbb", "cc", "d", "e");
    assertEquals(ra("aaaa", "bbb", "cc", "e", "d"),
        RubyIterables.sortBy(re, (o1, o2) -> o2.compareTo(o1),
            (o1, o2) -> o2 - o1, item -> item.length()));
  }

  @Test
  public void testSortByWithBlock() {
    Iterable<String> re = Arrays.asList("aaaa", "bbb", "cc", "e", "d");
    assertEquals(ra("e", "d", "cc", "bbb", "aaaa"),
        RubyIterables.sortBy(re, item -> item.length()));
  }

  @Test
  public void testTake() {
    assertEquals(ra(), RubyIterables.take(iter, 0));
    assertEquals(ra(1, 2), RubyIterables.take(iter, 2));
    assertEquals(ra(1, 2, 3, 4), RubyIterables.take(iter, 5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTakeException() {
    RubyIterables.take(iter, -1);
  }

  @Test
  public void testTakeWhileWithBlock() {
    Predicate<Integer> block = item -> item != 3;
    assertEquals(ra(1, 2), RubyIterables.takeWhile(iter, block));
    iter = ra(1, 1, 1, 1);
    assertEquals(ra(1, 1, 1, 1), RubyIterables.takeWhile(iter, block));
  }

  @Test
  public void testToA() {
    assertEquals(ra(1, 2, 3, 4), RubyIterables.toA(iter));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testToAH() {
    assertEquals(rh(1, 2, 3, 4), RubyIterables.toH(ra(ra(1, 2), ra(3, 4))));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testZip() {
    iter = Arrays.asList(1, 2, 3);
    assertEquals(ra(ra(1, 4), ra(2, 5), ra(3, null)),
        RubyIterables.zip(iter, ra(4, 5)));
    assertEquals(ra(ra(1, 4, 7), ra(2, 5, 8), ra(3, 6, 9)),
        RubyIterables.zip(iter, ra(4, 5, 6), ra(7, 8, 9)));
    assertEquals(ra(ra(1, 4, 7), ra(2, 5, 8), ra(3, 6, 9)),
        RubyIterables.zip(iter, ra(ra(4, 5, 6), ra(7, 8, 9))));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testZipWithBlock() {
    iter = Arrays.asList(1, 2, 3);
    final RubyArray<Integer> ints = ra();
    RubyIterables.zip(iter, ra(ra(4, 5, 6, null), ra(7, 8, 9, null)),
        (Consumer<RubyArray<Integer>>) item -> ints
            .push(item.reduce((memo, item1) -> memo + item1)));
    assertEquals(ra(12, 15, 18), ints);
  }

}
