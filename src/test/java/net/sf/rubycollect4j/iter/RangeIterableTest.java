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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import net.sf.rubycollect4j.succ.IntegerSuccessor;

public class RangeIterableTest {

  RangeIterable<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter = new RangeIterable<Integer>(IntegerSuccessor.getInstance(), 1, 3);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof RangeIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new RangeIterable<Integer>(null, 1, 3);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new RangeIterable<Integer>(IntegerSuccessor.getInstance(), null, 3);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException3() {
    new RangeIterable<Integer>(IntegerSuccessor.getInstance(), 1, null);
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof RangeIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[1, 2, 3]", iter.toString());
  }

}
