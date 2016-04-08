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

import java.util.List;

import net.sf.rubycollect4j.block.EntryBooleanBlock;

import org.junit.Before;
import org.junit.Test;

public class SliceWhenIteratorTest {

  SliceWhenIterator<Integer> iter;
  List<Integer> list;
  EntryBooleanBlock<Integer, Integer> block;

  @Before
  public void setUp() throws Exception {
    list = ra(1, 2, 4, 9, 10, 11, 12, 15, 16, 19, 20, 21);
    block = new EntryBooleanBlock<Integer, Integer>() {

      @Override
      public boolean yield(Integer item1, Integer item2) {
        return item1 + 1 != item2;
      }

    };
    iter = new SliceWhenIterator<Integer>(list.iterator(), block);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof SliceWhenIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new SliceWhenIterator<Integer>(null, block);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new SliceWhenIterator<Integer>(list.iterator(),
        (EntryBooleanBlock<Integer, Integer>) null);
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
    assertEquals(ra(1, 2), iter.next());
    assertEquals(ra(4), iter.next());
    assertEquals(ra(9, 10, 11, 12), iter.next());
    assertEquals(ra(15, 16), iter.next());
    assertEquals(ra(19, 20, 21), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemove() {
    iter.remove();
  }

}