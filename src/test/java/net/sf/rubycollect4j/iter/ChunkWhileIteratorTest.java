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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.BiPredicate;

import org.junit.Before;
import org.junit.Test;

import net.sf.rubycollect4j.RubyArray;

public class ChunkWhileIteratorTest {

  ChunkWhileIterator<Integer> iter;
  RubyArray<Integer> nums;
  BiPredicate<Integer, Integer> block;

  @Before
  public void setUp() throws Exception {
    block = (item1, item2) -> item1 + 1 == item2;
    nums = ra(1, 2, 4, 9, 10, 11, 12, 15, 16, 19, 20, 21);
    iter = new ChunkWhileIterator<Integer>(nums.iterator(), block);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof ChunkWhileIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new ChunkWhileIterator<Integer>(null, block);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new ChunkWhileIterator<Integer>(nums.iterator(), null);
  }

  @Test
  public void testHasNext() {
    assertTrue(iter.hasNext());
    while (iter.hasNext()) {
      iter.next();
    }
    assertFalse(iter.hasNext());
  }

  @Test
  public void testNext() {
    assertEquals(ra(1),
        new ChunkWhileIterator<Integer>(ra(1).iterator(), block).next());
    assertEquals(ra(1, 2), iter.next());
    assertEquals(ra(4), iter.next());
    assertEquals(ra(9, 10, 11, 12), iter.next());
    assertEquals(ra(15, 16), iter.next());
    assertEquals(ra(19, 20, 21), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test
  public void testComparableEntry() {
    assertTrue(iter.next() instanceof RubyArray);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemove() {
    iter.remove();
  }

}
