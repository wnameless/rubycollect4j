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
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.block.TransformBlock;

public class FlattenIteratorTest {

  FlattenIterator<Integer, Double> iter;
  List<Integer> list;
  TransformBlock<Integer, RubyArray<Double>> block;

  @Before
  public void setUp() throws Exception {
    list = ra(1, 2, 3);
    block = new TransformBlock<Integer, RubyArray<Double>>() {

      @Override
      public RubyArray<Double> yield(Integer item) {
        return ra(item.doubleValue(), item.doubleValue());
      }

    };
    iter = new FlattenIterator<Integer, Double>(list.iterator(), block);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof FlattenIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new FlattenIterator<Integer, Double>(null, block);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new FlattenIterator<Integer, Double>(list.iterator(), null);
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
    assertEquals(Double.valueOf(1.0), iter.next());
    assertEquals(Double.valueOf(2.0), iter.next());
    assertEquals(Double.valueOf(2.0), iter.next());
    assertEquals(Double.valueOf(3.0), iter.next());
    assertEquals(Double.valueOf(3.0), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test
  public void testNextWithNullValue() {
    iter =
        new FlattenIterator<Integer, Double>(ra(1, null, 3).iterator(), block);
    assertEquals(ra(1.0, 1.0, null, 3.0, 3.0), ra(iter));
  }

  @Test(expected = NoSuchElementException.class)
  public void testNextException() {
    while (iter.hasNext()) {
      iter.next();
    }
    iter.next();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemoveException() {
    iter.remove();
  }

}
