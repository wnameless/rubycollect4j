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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReverseEachIteratorTest {

  ReverseEachIterator<Integer> iter;

  @BeforeEach
  public void setUp() throws Exception {
    iter = new ReverseEachIterator<Integer>(ra(1, 2, 3, 4, 5).iterator());
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof ReverseEachIterator);
  }

  @Test
  public void testConstructorException() {
    assertThrows(NullPointerException.class, () -> {
      new ReverseEachIterator<Integer>(null);
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
  public void testHasNextLaziness() {
    iter = new ReverseEachIterator<Integer>(ra(1, 2, 3, 4, 5).lazy().cycle());
    assertTrue(iter.hasNext());
  }

  @Test
  public void testNext() {
    assertEquals(Integer.valueOf(5), iter.next());
    assertEquals(Integer.valueOf(4), iter.next());
    assertEquals(Integer.valueOf(3), iter.next());
    assertEquals(Integer.valueOf(2), iter.next());
    assertEquals(Integer.valueOf(1), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test
  public void testRemoveException() {
    assertThrows(UnsupportedOperationException.class, () -> {
      iter.remove();
    });
  }

}
