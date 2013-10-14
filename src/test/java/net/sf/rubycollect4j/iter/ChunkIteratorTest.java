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

import static net.sf.rubycollect4j.RubyCollections.hp;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.block.TransformBlock;

import org.junit.Before;
import org.junit.Test;

public class ChunkIteratorTest {

  private ChunkIterator<Number, String> iter;
  private TransformBlock<Number, String> block;

  @Before
  public void setUp() throws Exception {
    block = new TransformBlock<Number, String>() {

      @Override
      public String yield(Number item) {
        return item.toString();
      }

    };
    RubyArray<Number> nums =
        ra((Number) 1, (Number) 1.0, (Number) 1.0f, (Number) 2, (Number) 2L);
    iter = new ChunkIterator<Number, String>(nums.iterator(), block);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof ChunkIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    iter = new ChunkIterator<Number, String>(null, block);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    iter =
        new ChunkIterator<Number, String>(ra((Number) 1, (Number) 1.0,
            (Number) 1.0f, (Number) 2, (Number) 2L).iterator(), null);
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
    assertEquals(hp("1", ra(1)), iter.next());
    assertEquals(hp("1.0", ra((Number) 1.0, (Number) 1.0f)), iter.next());
    assertEquals(hp("2", ra((Number) 2, (Number) 2L)), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test(expected = NoSuchElementException.class)
  public void testNextException() {
    while (iter.hasNext()) {
      iter.next();
    }
    iter.next();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemove() {
    iter.remove();
  }

}
