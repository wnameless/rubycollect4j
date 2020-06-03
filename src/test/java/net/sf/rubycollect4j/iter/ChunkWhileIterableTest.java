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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.BiPredicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.sf.rubycollect4j.RubyArray;

public class ChunkWhileIterableTest {

  ChunkWhileIterable<Integer> iter;
  RubyArray<Integer> nums;
  BiPredicate<Integer, Integer> block;

  @BeforeEach
  public void setUp() throws Exception {
    block = (item1, item2) -> item1 + 1 == item2;
    nums = ra(1, 2, 4, 9, 10, 11, 12, 15, 16, 19, 20, 21);
    iter = new ChunkWhileIterable<Integer>(nums, block);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof ChunkWhileIterable);
  }

  @Test
  public void testConstructorException1() {
    assertThrows(NullPointerException.class, () -> {
      new ChunkWhileIterable<Integer>(null, block);
    });
  }

  @Test
  public void testConstructorException2() {
    assertThrows(NullPointerException.class, () -> {
      new ChunkWhileIterable<Number>(nums, null);
    });
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
