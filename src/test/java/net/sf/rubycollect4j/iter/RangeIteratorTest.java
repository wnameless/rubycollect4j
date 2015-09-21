/*
 *
 * Copyright 2013-2015 Wei-Ming Wu
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import net.sf.rubycollect4j.succ.IntegerSuccessor;

public class RangeIteratorTest {

  RangeIterator<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter = new RangeIterator<Integer>(IntegerSuccessor.getInstance(), 1, 3);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof RangeIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new RangeIterator<Integer>(null, 1, 3);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new RangeIterator<Integer>(IntegerSuccessor.getInstance(), null, 3);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException3() {
    new RangeIterator<Integer>(IntegerSuccessor.getInstance(), 1, null);
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
    assertEquals(Integer.valueOf(2), iter.next());
    assertEquals(Integer.valueOf(3), iter.next());
    assertFalse(iter.hasNext());
    iter = new RangeIterator<Integer>(IntegerSuccessor.getInstance(), 1, 0);
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
