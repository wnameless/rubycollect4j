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
package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.block.EntryBooleanBlock;

import org.junit.Before;
import org.junit.Test;

public class ChunkWhileIterableTest {

  ChunkWhileIterable<Integer> iter;
  RubyArray<Integer> nums;
  EntryBooleanBlock<Integer, Integer> block;

  @Before
  public void setUp() throws Exception {
    block = new EntryBooleanBlock<Integer, Integer>() {

      @Override
      public boolean yield(Integer item1, Integer item2) {
        return item1 + 1 == item2;
      }

    };
    nums = ra(1, 2, 4, 9, 10, 11, 12, 15, 16, 19, 20, 21);
    iter = new ChunkWhileIterable<Integer>(nums, block);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof ChunkWhileIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new ChunkWhileIterable<Integer>(null, block);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new ChunkWhileIterable<Number>(nums, null);
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof ChunkWhileIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[[1, 2], [4], [9, 10, 11, 12], [15, 16], [19, 20, 21]]",
        iter.toString());
  }

}
