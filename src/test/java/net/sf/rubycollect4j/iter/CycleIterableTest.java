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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CycleIterableTest {

  CycleIterable<Integer> iter;

  @BeforeEach
  public void setUp() throws Exception {
    iter = new CycleIterable<Integer>(ra(1, 2, 3));
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof CycleIterable);
    iter = new CycleIterable<Integer>(ra(1, 2, 3), 2);
    assertTrue(iter instanceof CycleIterable);
  }

  @Test
  public void testConstructorException1() {
    assertThrows(NullPointerException.class, () -> {
      new CycleIterable<Integer>(null);
    });
  }

  @Test
  public void testConstructorException2() {
    assertThrows(NullPointerException.class, () -> {
      new CycleIterable<Integer>(null, 2);
    });
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof CycleIterator);
    iter = new CycleIterable<Integer>(ra(1, 2, 3), 2);
    assertTrue(iter.iterator() instanceof CycleIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[1, 2, 3...]", iter.toString());
    iter = new CycleIterable<Integer>(ra(1, 2, 3), 2);
    assertEquals("[1, 2, 3, 1, 2, 3]", iter.toString());
  }

}
