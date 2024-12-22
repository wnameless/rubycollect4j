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
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EachSliceIteratorTest {

  EachSliceIterator<Integer> iter;
  List<Integer> list;

  @BeforeEach
  public void setUp() throws Exception {
    list = ra(1, 2, 3, 4, 5);
    iter = new EachSliceIterator<Integer>(list.iterator(), 2);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof EachSliceIterator);
  }

  @Test
  public void testConstructorException1() {
    assertThrows(NullPointerException.class, () -> {
      new EachSliceIterator<Integer>(null, 2);
    });
  }

  @Test
  public void testConstructorException2() {
    assertThrows(IllegalArgumentException.class, () -> {
      new EachSliceIterator<Integer>(list.iterator(), 0);
    });
  }

  @Test
  public void testConstructorException3() {
    assertThrows(IllegalArgumentException.class, () -> {
      new EachSliceIterator<Integer>(list.iterator(), -1);
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
    assertEquals(ra(3, 4), iter.next());
    assertEquals(ra(5), iter.next());
    assertFalse(iter.hasNext());
    iter = new EachSliceIterator<Integer>(ra(1, 2, 3, 4, 5).iterator(), 6);
    assertEquals(ra(1, 2, 3, 4, 5), iter.next());
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
