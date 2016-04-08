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

import static net.sf.rubycollect4j.RubyCollections.qr;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.regex.Pattern;

import net.sf.rubycollect4j.block.BooleanBlock;

import org.junit.Before;
import org.junit.Test;

public class SliceAfterIterableTest {

  SliceAfterIterable<Integer> iter;
  List<Integer> list;
  BooleanBlock<Integer> block;
  Pattern pattern;

  @Before
  public void setUp() throws Exception {
    list = ra(1, 2, 3, 4, 5);
    block = new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item % 2 == 0;
      }

    };
    pattern = qr("3");
    iter = new SliceAfterIterable<Integer>(list, block);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof SliceAfterIterable);
    iter = new SliceAfterIterable<Integer>(list, pattern);
    assertTrue(iter instanceof SliceAfterIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new SliceAfterIterable<Integer>(null, block);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new SliceAfterIterable<Integer>(list, (BooleanBlock<Integer>) null);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException3() {
    new SliceAfterIterable<Integer>(null, pattern);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException4() {
    new SliceAfterIterable<Integer>(list, (Pattern) null);
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof SliceAfterIterator);
    iter = new SliceAfterIterable<Integer>(list, pattern);
    assertTrue(iter.iterator() instanceof SliceAfterIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[[1, 2], [3, 4], [5]]", iter.toString());
  }

}