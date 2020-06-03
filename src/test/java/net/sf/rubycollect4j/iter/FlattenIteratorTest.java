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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.sf.rubycollect4j.RubyArray;

public class FlattenIteratorTest {

  FlattenIterator<Integer, Double> iter;
  List<Integer> list;
  Function<Integer, RubyArray<Double>> block;

  @BeforeEach
  public void setUp() throws Exception {
    list = ra(1, 2, 3);
    block = item -> ra(item.doubleValue(), item.doubleValue());
    iter = new FlattenIterator<Integer, Double>(list.iterator(), block);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof FlattenIterator);
  }

  @Test
  public void testConstructorException1() {
    assertThrows(NullPointerException.class, () -> {
      new FlattenIterator<Integer, Double>(null, block);
    });
  }

  @Test
  public void testConstructorException2() {
    assertThrows(NullPointerException.class, () -> {
      new FlattenIterator<Integer, Double>(list.iterator(), null);
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
    assertEquals(Double.valueOf(1.0), iter.next());
    assertEquals(Double.valueOf(1.0), iter.next());
    assertEquals(Double.valueOf(2.0), iter.next());
    assertEquals(Double.valueOf(2.0), iter.next());
    assertEquals(Double.valueOf(3.0), iter.next());
    assertEquals(Double.valueOf(3.0), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test
  public void testNextWithNullValue() {
    iter =
        new FlattenIterator<Integer, Double>(ra(1, null, 3).iterator(), block);
    assertEquals(ra(1.0, 1.0, null, 3.0, 3.0), ra(iter));
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
  public void testRemoveException() {
    assertThrows(UnsupportedOperationException.class, () -> {
      iter.remove();
    });
  }

}
