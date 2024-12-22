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
package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TakeWhileIteratorTest {

  TakeWhileIterator<Integer> iter;
  List<Integer> list;
  Predicate<Integer> block;

  @BeforeEach
  public void setUp() throws Exception {
    list = ra(1, 2, 3, 4, 5);
    block = item -> item < 3;
    iter = new TakeWhileIterator<Integer>(list.iterator(), block);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof TakeWhileIterator);
  }

  @Test
  public void testConstructorException1() {
    assertThrows(NullPointerException.class, () -> {
      new TakeWhileIterator<Integer>(list.iterator(), null);
    });
  }

  @Test
  public void testConstructorException2() {
    assertThrows(NullPointerException.class, () -> {
      new TakeWhileIterator<Integer>(null, block);
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
    assertEquals(Integer.valueOf(1), iter.next());
    assertEquals(Integer.valueOf(2), iter.next());
    assertFalse(iter.hasNext());
    iter = new TakeWhileIterator<Integer>(new ArrayList<Integer>().iterator(), block);
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
    iter.next();
    iter.remove();
    assertEquals(ra(2, 3, 4, 5), list);
  }

}
