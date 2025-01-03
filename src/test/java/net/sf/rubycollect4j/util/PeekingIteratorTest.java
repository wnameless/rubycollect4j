/*
 *
 * Copyright 2013 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package net.sf.rubycollect4j.util;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PeekingIteratorTest {

  PeekingIterator<Integer> pIterater;
  PeekingIterator<Integer> emptyIterater;

  @BeforeEach
  public void setUp() {
    pIterater = new PeekingIterator<Integer>(ra(1, 2, 3, 4).iterator());
    emptyIterater = new PeekingIterator<Integer>(new ArrayList<Integer>().iterator());
  }

  @Test
  public void testIterface() {
    assertTrue(pIterater instanceof Iterator);
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
    assertFalse(emptyIterater.hasNext());
  }

  @Test
  public void testNext() {
    assertEquals(Integer.valueOf(1), pIterater.next());
    pIterater.next();
    pIterater.next();
    assertEquals(Integer.valueOf(4), pIterater.next());
  }

  @Test
  public void testNextException() {
    assertThrows(NoSuchElementException.class, () -> {
      emptyIterater.next();
    });
  }

  @Test
  public void testPeek() {
    assertEquals(Integer.valueOf(1), pIterater.peek());
    pIterater.next();
    assertEquals(Integer.valueOf(2), pIterater.peek());
  }

  @Test
  public void testPeekException() {
    assertThrows(NoSuchElementException.class, () -> {
      emptyIterater.peek();
    });
  }

  @Test
  public void testRemoveException() {
    assertThrows(IllegalStateException.class, () -> {
      pIterater.peek();
      pIterater.remove();
    });
  }

}
