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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sf.rubycollect4j.block.Block;
import net.sf.rubycollect4j.block.BooleanBlock;
import net.sf.rubycollect4j.block.TransformBlock;
import net.sf.rubycollect4j.block.WithIndexBlock;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class RubyLazyEnumeratorTest {

  RubyLazyEnumerator<Integer> lre;
  List<Integer> list;
  TransformBlock<Integer, Boolean> block;

  @Before
  public void setUp() throws Exception {
    list = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    lre = new RubyLazyEnumerator<Integer>(list);
    block = new TransformBlock<Integer, Boolean>() {

      @Override
      public Boolean yield(Integer item) {
        return item % 2 == 0;
      }

    };
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
  public void testChunkWithMethodName() {
    lre = newRubyLazyEnumerator(Arrays.asList(1, 2, 2, 3));
    assertTrue(lre.chunk("toString") instanceof RubyLazyEnumerator);
    assertEquals(ra(hp("1", ra(1)), hp("2", ra(2, 2)), hp("3", ra(3))), lre
        .chunk("toString").toA());
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
  public void testCollectWithMethodName() {
    assertEquals(ra("1", "2", "3", "4"), lre.collect("toString").toA());
  }

  @Test
  public void testCollectConcat() {
    assertSame(lre, lre.collectConcat());
  }

  @Test
  public void testCollectConcatWithBlock() {
    TransformBlock<Integer, RubyArray<Integer>> block =
        new TransformBlock<Integer, RubyArray<Integer>>() {

          @Override
          public RubyArray<Integer> yield(Integer item) {
            return ra(item, item);
          }

        };
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
    BooleanBlock<Integer> block = new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item < 3;
      }

    };
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
    lre.each(new Block<Integer>() {

      @Override
      public void yield(Integer item) {
        ints.add(item);
      }

    });
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
    lre.eachCons(3, new Block<RubyArray<Integer>>() {

      @Override
      public void yield(RubyArray<Integer> item) {
        rubyArray.add(item);
      }

    });
    assertEquals(ra(ra(1, 2, 3), ra(2, 3, 4)), rubyArray);
  }

  @Test
  public void testEachEntry() {
    assertTrue(lre.eachEntry() instanceof RubyLazyEnumerator);
  }

  @Test
  public void testEachEntryWithBlock() {
    final RubyArray<Integer> ints = ra();
    assertTrue(lre.eachEntry(new Block<Integer>() {

      @Override
      public void yield(Integer item) {
        ints.add(item);
      }

    }) instanceof RubyLazyEnumerator);
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
    assertSame(lre, lre.eachWithIndex(new WithIndexBlock<Integer>() {

      @Override
      public void yield(Integer item, int index) {
        ints.push(item, index);
      }

    }));
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
  public void testGrepWithMethodName() {
    assertTrue(lre.grep("2|3", "toString") instanceof RubyLazyEnumerator);
    assertEquals(ra("2", "3"), lre.grep("2|3", "toString").toA());
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
  public void testMapWithMethodName() {
    assertTrue(lre.map("toString") instanceof RubyLazyEnumerator);
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
    assertTrue(lre.reverseEach(new Block<Integer>() {

      @Override
      public void yield(Integer item) {
        ints.add(item);
      }

    }) instanceof RubyLazyEnumerator);
    assertEquals(ra(4, 3, 2, 1), ints);
  }

  @Test
  public void testSelect() {
    assertSame(lre, lre.select());
  }

  @Test
  public void testSelectWithBlock() {
    BooleanBlock<Integer> block = new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item == 3;
      }

    };
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
  public void testSliceBefore() {
    BooleanBlock<Integer> block = new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item == 3;
      }

    };
    assertTrue(lre.sliceBefore(block) instanceof RubyLazyEnumerator);
    assertEquals(ra(ra(1, 2), ra(3, 4)), lre.sliceBefore(block).toA());
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

  @Test
  public void testToString() {
    assertEquals("RubyLazyEnumerator{[1, 2, 3, 4]}", lre.toString());
  }

}
