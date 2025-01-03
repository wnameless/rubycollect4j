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

import static org.junit.jupiter.api.Assertions.*;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import net.sf.rubycollect4j.Ruby;
import net.sf.rubycollect4j.RubyRange.Interval;
import net.sf.rubycollect4j.succ.IntegerSuccessor;

public class RangeIteratorTest {

  RangeIterator<Integer> iter;

  @BeforeEach
  public void setUp() throws Exception {
    iter = new RangeIterator<Integer>(IntegerSuccessor.getInstance(), 1, 3, Interval.CLOSED);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof RangeIterator);
  }

  @Test
  public void testConstructorException1() {
    assertThrows(NullPointerException.class, () -> {
      new RangeIterator<Integer>(null, 1, 3, Interval.CLOSED);
    });
  }

  @Test
  public void testConstructorException2() {
    assertThrows(NullPointerException.class, () -> {
      new RangeIterator<Integer>(IntegerSuccessor.getInstance(), null, 3, Interval.CLOSED);
    });
  }

  @Test
  public void testConstructorException3() {
    assertThrows(NullPointerException.class, () -> {
      new RangeIterator<Integer>(IntegerSuccessor.getInstance(), 1, null, Interval.CLOSED);
    });
  }

  @Test
  public void testConstructorException4() {
    assertThrows(NullPointerException.class, () -> {
      new RangeIterator<Integer>(IntegerSuccessor.getInstance(), 1, 3, null);
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
    assertEquals(Integer.valueOf(3), iter.next());
    assertFalse(iter.hasNext());
    iter = new RangeIterator<Integer>(IntegerSuccessor.getInstance(), 1, 0, Interval.CLOSED);
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

  @Test
  public void testClosedOpen() {
    iter = new RangeIterator<Integer>(IntegerSuccessor.getInstance(), 1, 3, Interval.CLOSED_OPEN);
    assertEquals(Ruby.Array.of(1, 2), Ruby.Array.copyOf(iter));
  }

  @Test
  public void testOpen() {
    iter = new RangeIterator<Integer>(IntegerSuccessor.getInstance(), 1, 3, Interval.OPEN);
    assertEquals(Ruby.Array.of(2), Ruby.Array.copyOf(iter));
    iter = new RangeIterator<Integer>(IntegerSuccessor.getInstance(), 3, 1, Interval.OPEN);
    assertEquals(Ruby.Array.create(), Ruby.Array.copyOf(iter));
  }

  @Test
  public void testOpenClosed() {
    iter = new RangeIterator<Integer>(IntegerSuccessor.getInstance(), 1, 3, Interval.OPEN_CLOSED);
    assertEquals(Ruby.Array.of(2, 3), Ruby.Array.copyOf(iter));
    iter = new RangeIterator<Integer>(IntegerSuccessor.getInstance(), 3, 1, Interval.OPEN_CLOSED);
    assertEquals(Ruby.Array.create(), Ruby.Array.copyOf(iter));
  }

}
