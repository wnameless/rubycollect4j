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
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.block.TransformBlock;

public class FlattenIterableTest {

  FlattenIterable<Integer, Double> iter;
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
    iter = new FlattenIterable<Integer, Double>(list, block);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof FlattenIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new FlattenIterable<Integer, Double>(null, block);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new FlattenIterable<Integer, Double>(list, null);
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof FlattenIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[1.0, 1.0, 2.0, 2.0, 3.0, 3.0]", iter.toString());
  }

}
