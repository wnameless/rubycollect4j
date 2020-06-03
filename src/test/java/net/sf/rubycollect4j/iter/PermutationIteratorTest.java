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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PermutationIteratorTest {

  PermutationIterator<Integer> iter;
  List<Integer> list;

  @BeforeEach
  public void setUp() throws Exception {
    list = ra(1, 2, 3);
    iter = new PermutationIterator<Integer>(list, 2);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof PermutationIterator);
  }

  @Test
  public void testConstructorException() {
    assertThrows(NullPointerException.class, () -> {
      new PermutationIterator<Integer>(null, 2);
    });
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
    assertEquals(ra(2, 1), iter.next());
    assertEquals(ra(2, 3), iter.next());
    assertEquals(ra(3, 1), iter.next());
    assertEquals(ra(3, 2), iter.next());
    assertFalse(iter.hasNext());
    iter = new PermutationIterator<Integer>(ra(1, 2, 3), 0);
    assertEquals(ra(), iter.next());
    assertFalse(iter.hasNext());
    iter = new PermutationIterator<Integer>(new ArrayList<Integer>(), 2);
    assertFalse(iter.hasNext());
    iter = new PermutationIterator<Integer>(ra(1, 2, 3), 4);
    assertFalse(iter.hasNext());
  }

  @Test
  public void testDefensiveCopy() {
    list.clear();
    assertEquals(ra(1, 2), iter.next());
    assertEquals(ra(1, 3), iter.next());
    assertEquals(ra(2, 1), iter.next());
    assertEquals(ra(2, 3), iter.next());
    assertEquals(ra(3, 1), iter.next());
    assertEquals(ra(3, 2), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test
  public void testNextException() {
    assertThrows(NoSuchElementException.class, () -> {
      while (iter.hasNext()) {
        iter.next();
      }
      iter.next();
    });
  }

  @Test
  public void testRemove() {
    assertThrows(UnsupportedOperationException.class, () -> {
      iter.remove();
    });
  }

}
