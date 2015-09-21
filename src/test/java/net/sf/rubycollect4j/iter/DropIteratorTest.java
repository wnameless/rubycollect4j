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

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DropIteratorTest {

  DropIterator<Integer> iter;
  List<Integer> list;

  @Before
  public void setUp() throws Exception {
    list = ra(1, 2, 3, 4, 5);
    iter = new DropIterator<Integer>(list.iterator(), 2);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof DropIterator);
  }

  @Test
  public void testConstructorWithExceedN() {
    assertEquals(ra(), ra(new DropIterator<Integer>(list.iterator(), 6)));
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new DropIterator<Integer>(null, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException2() {
    new DropIterator<Integer>(list.iterator(), -1);
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
    assertEquals(Integer.valueOf(3), iter.next());
    assertEquals(Integer.valueOf(4), iter.next());
    assertEquals(Integer.valueOf(5), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test
  public void testRemove() {
    iter.next();
    iter.remove();
    assertEquals(ra(1, 2, 4, 5), list);
  }

  @Test(expected = IllegalStateException.class)
  public void testRemoveException() {
    iter.remove();
  }

}
