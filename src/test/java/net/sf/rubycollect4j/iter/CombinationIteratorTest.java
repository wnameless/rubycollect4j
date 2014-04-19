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
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class CombinationIteratorTest {

  CombinationIterator<Integer> iter;
  List<Integer> list;

  @Before
  public void setUp() throws Exception {
    list = ra(1, 2, 3);
    iter = new CombinationIterator<Integer>(list, 2);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof CombinationIterator);
  }

  @Test
  public void testConstructorWithN0() {
    iter = new CombinationIterator<Integer>(ra(1, 2, 3), 0);
    assertEquals(ra(), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test
  public void testConstructorWithNegtiveN() {
    iter = new CombinationIterator<Integer>(ra(1, 2, 3), -1);
    assertFalse(iter.hasNext());
  }

  @Test
  public void testConstructorWithExceededN() {
    iter = new CombinationIterator<Integer>(ra(1, 2, 3), 4);
    assertFalse(iter.hasNext());
  }

  @Test
  public void testConstructorWithExactN() {
    iter = new CombinationIterator<Integer>(ra(1, 2, 3), 3);
    assertEquals(ra(1, 2, 3), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException() {
    new CombinationIterator<Integer>(null, 2);
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
    assertEquals(ra(1, 3), iter.next());
    assertEquals(ra(2, 3), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test
  public void testDefensiveCopy() {
    list.clear();
    assertEquals(ra(1, 2), iter.next());
    assertEquals(ra(1, 3), iter.next());
    assertEquals(ra(2, 3), iter.next());
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
