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
import static net.sf.rubycollect4j.RubyCollections.newRubyLazyEnumerator;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.rh;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class RubyLazyEnumeratorTest {

  RubyLazyEnumerator<Integer> lre;
  List<Integer> list;
  Function<Integer, Boolean> block;

  @Before
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

  @Test(expected = NullPointerException.class)
  public void testOfException() {
    RubyLazyEnumerator.of(null);
  }

  @Test
  public void testCopyOf() {
    lre = RubyLazyEnumerator.copyOf(list);
    list.remove(0);
    assertEquals(ra(1, 2, 3, 4), lre.toA());
  }

  @Test(expected = NullPointerException.class)
  public void testCopyOfException() {
    RubyLazyEnumerator.copyOf(null);
  }

  @Test
  public void testConstructor() {
    assertTrue(lre instanceof RubyLazyEnumerator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException() {
    newRubyLazyEnumerator(null);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testChunk() {
    assertTrue(lre.chunk(block) instanceof RubyLazyEnumerator);
    lre = newRubyLazyEnumerator(Arrays.asList(1, 2, 2, 3));
    assertEquals(ra(hp(false, ra(1)), hp(true, ra(2, 2)), hp(false, ra(3))),
        lre.chunk(block).toA());
  }

  @SuppressWarnings("unchecked")
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
  public void testCycle() {
    assertTrue(lre.cycle() instanceof RubyLazyEnumerator);
  }

  @Test
  public void testCycleWithN() {
    assertTrue(lre.cycle(3) instanceof RubyLazyEnumerator);
  }

  @Test
  public void testDetect() {
    assertTrue(lre.detect() instanceof RubyLazyEnumerator);
  }

  @Test
  public void testDrop() {
    assertTrue(lre.drop(1) instanceof RubyLazyEnumerator);
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
    assertTrue(lre.each() instanceof RubyLazyEnumerator);
  }

  @Test
  public void testEachWithBlock() {
    final RubyArray<Integer> ints = ra();
    lre.each(item -> ints.add(item));
    assertEquals(ra(1, 2, 3, 4), ints);
  }

  @Test
  public void testEachCons() {
    assertTrue(lre.eachCons(3) instanceof RubyLazyEnumerator);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testEachConsWithBlock() {
    final RubyArray<RubyArray<Integer>> rubyArray = ra();
    lre.eachCons(3, item -> rubyArray.add(item));
    assertEquals(ra(ra(1, 2, 3), ra(2, 3, 4)), rubyArray);
  }

  @Test
  public void testEachEntry() {
    assertTrue(lre.eachEntry() instanceof RubyLazyEnumerator);
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
  }

  @Test
  public void testEachWithIndex() {
    assertTrue(lre.eachWithIndex() instanceof RubyLazyEnumerator);
  }

  @Test
  public void testEachWithIndexWithBlock() {
    final RubyArray<Integer> ints = ra();
    assertSame(lre, lre.eachWithIndex((item, index) -> ints.push(item, index)));
    assertEquals(ra(1, 0, 2, 1, 3, 2, 4, 3), ints);
  }

  @Test
  public void testEachWithObject() {
    assertTrue(lre.eachWithObject(1L) instanceof RubyLazyEnumerator);
  }

  @Test
  public void testEntries() {
    assertEquals(ra(1, 2, 3, 4), lre.entries());
  }

  @Test
  public void testFind() {
    assertTrue(lre.find() instanceof RubyLazyEnumerator);
  }

  @Test
  public void testFindAll() {
    assertTrue(lre.findAll() instanceof RubyLazyEnumerator);
  }

  @Test
  public void testFindIndex() {
    assertTrue(lre.findIndex() instanceof RubyLazyEnumerator);
  }

  @Test
  public void testFlatMap() {
    assertTrue(lre.flatMap() instanceof RubyLazyEnumerator);
  }

  @Test
  public void testGroupBy() {
    assertTrue(lre.groupBy() instanceof RubyLazyEnumerator);
  }

  @Test
  public void testLazy() {
    assertTrue(lre.lazy() instanceof RubyLazyEnumerator);
  }

  @Test
  public void testMap() {
    assertTrue(lre.map() instanceof RubyLazyEnumerator);
  }

  @Test
  public void testMapWithBlock() {
    assertTrue(lre.map(block) instanceof RubyLazyEnumerator);
  }

  @Test
  public void testMaxBy() {
    assertTrue(lre.maxBy() instanceof RubyLazyEnumerator);
  }

  @Test
  public void testMinBy() {
    assertTrue(lre.minBy() instanceof RubyLazyEnumerator);
  }

  @Test
  public void testMinmaxBy() {
    assertTrue(lre.minmaxBy() instanceof RubyLazyEnumerator);
  }

  @Test
  public void testPartition() {
    assertTrue(lre.partition() instanceof RubyLazyEnumerator);
  }

  @Test
  public void testReject() {
    assertSame(lre, lre.reject());
  }

  @Test
  public void testReverseEach() {
    assertTrue(lre.cycle().reverseEach() instanceof RubyLazyEnumerator);
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
  }

  @Test
  public void testSelectWithBlock() {
    Predicate<Integer> block = item -> item == 3;
    assertTrue(lre.select(block) instanceof RubyLazyEnumerator);
    assertEquals(ra(3), lre.select(block).toA());
  }

  @Test
  public void testTakeWhile() {
    assertTrue(lre.takeWhile() instanceof RubyLazyEnumerator);
    assertEquals(ra(1), lre.takeWhile().toA());
    lre = new RubyLazyEnumerator<Integer>(new ArrayList<Integer>());
    assertEquals(ra(), lre.takeWhile().toA());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSliceAfter() {
    Predicate<Integer> block = item -> item == 3;
    assertTrue(lre.sliceAfter(block) instanceof RubyLazyEnumerator);
    assertEquals(ra(ra(1, 2, 3), ra(4)), lre.sliceAfter(block).toA());
  }

  @Test
  public void testSliceAfterWithRegex() {
    assertTrue(lre.sliceAfter("") instanceof RubyLazyEnumerator);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSliceBefore() {
    Predicate<Integer> block = item -> item == 3;
    assertTrue(lre.sliceBefore(block) instanceof RubyLazyEnumerator);
    assertEquals(ra(ra(1, 2), ra(3, 4)), lre.sliceBefore(block).toA());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSliceWhen() {
    BiPredicate<Integer, Integer> block = (item1, item2) -> item1 + 1 == item2;
    lre = newRubyLazyEnumerator(Arrays.asList(1, 3, 3, 4));
    assertTrue(lre.sliceWhen(block) instanceof RubyLazyEnumerator);
    assertEquals(ra(ra(1, 3, 3), ra(4)), lre.sliceWhen(block).toA());
  }

  @Test
  public void testSliceBeforeWithRegex() {
    assertTrue(lre.sliceBefore("") instanceof RubyLazyEnumerator);
  }

  @Test
  public void testSortBy() {
    assertTrue(lre.sortBy() instanceof RubyLazyEnumerator);
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

  @SuppressWarnings("serial")
  @Test
  public void testToH() {
    List<List<Integer>> pairs = new ArrayList<List<Integer>>();
    pairs.add(new ArrayList<Integer>() {
      {
        add(1);
        add(2);
      }
    });
    pairs.add(new ArrayList<Integer>() {
      {
        add(3);
        add(4);
      }
    });
    assertEquals(rh(1, 2, 3, 4), newRubyLazyEnumerator(pairs).toH());
  }

  @Test
  public void testToString() {
    assertEquals("RubyLazyEnumerator{[1, 2, 3, 4]}", lre.toString());
  }

}
