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
package net.sf.rubycollect4j.util;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class PeekingIteratorTest {

  private PeekingIterator<Integer> pIterater;

  @Before
  public void setUp() {
    pIterater = new PeekingIterator<Integer>(ra(1, 2, 3, 4).iterator());
  }

  @Test
  public void testIterface() {
    assertTrue(pIterater instanceof Iterator);
  }

  @Test(expected = NoSuchElementException.class)
  public void testConstructorWithEmptyIterator1() {
    pIterater =
        new PeekingIterator<Integer>(new ArrayList<Integer>().iterator());
    assertFalse(pIterater.hasNext());
    pIterater.next();
  }

  @Test(expected = NoSuchElementException.class)
  public void testConstructorWithEmptyIterator2() {
    pIterater =
        new PeekingIterator<Integer>(new ArrayList<Integer>().iterator());
    assertFalse(pIterater.hasNext());
    pIterater.peek();
  }

  @Test
  public void testRemove() {
    List<Integer> list = ra(1, 2, 3, 4);
    pIterater = new PeekingIterator<Integer>(list.iterator());
    pIterater.next();
    pIterater.remove();
    assertEquals(ra(2, 3, 4), list);
  }

  @Test
  public void testHasNext() {
    assertTrue(pIterater.hasNext());
  }

  @Test
  public void testNext() {
    assertEquals(Integer.valueOf(1), pIterater.next());
    pIterater.next();
    pIterater.next();
    assertEquals(Integer.valueOf(4), pIterater.next());
  }

  @Test
  public void testPeek() {
    assertEquals(Integer.valueOf(1), pIterater.peek());
    pIterater.next();
    assertEquals(Integer.valueOf(2), pIterater.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testPeekException1() {
    pIterater.peek();
    pIterater.remove();
  }

  @Test(expected = IllegalStateException.class)
  public void testPeekException2() {
    pIterater.next();
    pIterater.remove();
    pIterater.remove();
  }

}