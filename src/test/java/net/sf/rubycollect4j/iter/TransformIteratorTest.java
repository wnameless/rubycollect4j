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
package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.sf.rubycollect4j.block.TransformBlock;

public class TransformIteratorTest {

  TransformIterator<Integer, Double> iter;
  TransformBlock<Integer, Double> block;
  List<Integer> list;

  @Before
  public void setUp() throws Exception {
    list = ra(1, 2, 3, 4);
    block = new TransformBlock<Integer, Double>() {

      @Override
      public Double yield(Integer item) {
        return item.doubleValue();
      }

    };
    iter = new TransformIterator<Integer, Double>(list.iterator(), block);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof TransformIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new TransformIterator<Integer, Double>(null, block);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new TransformIterator<Integer, Double>(list.iterator(), null);
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
    assertEquals(Double.valueOf(1.0), iter.next());
    assertEquals(Double.valueOf(2.0), iter.next());
    assertEquals(Double.valueOf(3.0), iter.next());
    assertEquals(Double.valueOf(4.0), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test
  public void testRemove() {
    iter.next();
    iter.remove();
    assertEquals(ra(2.0, 3.0, 4.0), ra(iter));
  }

}
