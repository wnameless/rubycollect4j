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
package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.block.TransformBlock;

import org.junit.Before;
import org.junit.Test;

public class ChunkIterableTest {

  private ChunkIterable<Number, String> iter;
  private RubyArray<Number> nums;
  private TransformBlock<Number, String> block;

  @Before
  public void setUp() throws Exception {
    block = new TransformBlock<Number, String>() {

      @Override
      public String yield(Number item) {
        return item.toString();
      }

    };
    nums = ra((Number) 1, (Number) 1.0, (Number) 1.0f, (Number) 2, (Number) 2L);
    iter = new ChunkIterable<Number, String>(nums, block);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof ChunkIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new ChunkIterable<Number, String>(null, block);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new ChunkIterable<Number, String>(nums, null);
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof ChunkIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[1=[1], 1.0=[1.0, 1.0], 2=[2, 2]]", iter.toString());
  }

}
