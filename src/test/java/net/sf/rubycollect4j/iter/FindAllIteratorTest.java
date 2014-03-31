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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.sf.rubycollect4j.block.BooleanBlock;

import org.junit.Before;
import org.junit.Test;

public class FindAllIteratorTest {

  private FindAllIterator<Integer> iter;
  private List<Integer> list;
  private BooleanBlock<Integer> block;

  @Before
  public void setUp() throws Exception {
    list = ra(1, 2, 3);
    block = new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item % 2 == 1;
      }

    };
    iter = new FindAllIterator<Integer>(list.iterator(), block);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof FindAllIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new FindAllIterator<Integer>(null, block);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new FindAllIterator<Integer>(list.iterator(), null);
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
    assertEquals(Integer.valueOf(1), iter.next());
    assertEquals(Integer.valueOf(3), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test
  public void testRemove() {
    iter.next();
    iter.remove();
    assertEquals(ra(2, 3), list);
  }

}