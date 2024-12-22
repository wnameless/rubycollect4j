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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StepIteratorTest {

  StepIterator<Integer> iter;
  List<Integer> list;

  @BeforeEach
  public void setUp() throws Exception {
    list = ra(1, 2, 3, 4, 5);
    iter = new StepIterator<Integer>(list.iterator(), 2);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof StepIterator);
  }

  @Test
  public void testConstructorException1() {
    assertThrows(NullPointerException.class, () -> {
      new StepIterator<Integer>(null, 2);
    });
  }

  @Test
  public void testConstructorException2() {
    assertThrows(IllegalArgumentException.class, () -> {
      new StepIterator<Integer>(list.iterator(), 0);
    });
  }

  @Test
  public void testConstructorException3() {
    assertThrows(IllegalArgumentException.class, () -> {
      new StepIterator<Integer>(list.iterator(), -1);
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
    assertEquals(Integer.valueOf(3), iter.next());
    assertEquals(Integer.valueOf(5), iter.next());
    assertFalse(iter.hasNext());
    iter = new StepIterator<Integer>(new ArrayList<Integer>().iterator(), 2);
    assertFalse(iter.hasNext());
    iter = new StepIterator<Integer>(ra(1, 2, 3, 4, 5).iterator(), 6);
    assertEquals(Integer.valueOf(1), iter.next());
    assertFalse(iter.hasNext());
    iter = new StepIterator<Integer>(ra(1).iterator(), 1);
    assertEquals(Integer.valueOf(1), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test
  public void testRemove() {
    while (iter.hasNext()) {
      iter.next();
      iter.remove();
    }
    assertEquals(ra(2, 4), list);
  }

  @Test
  public void testRemoveExceotion() {
    assertThrows(IllegalStateException.class, () -> {
      iter.next();
      iter.hasNext();
      iter.remove();
    });
  }

}
