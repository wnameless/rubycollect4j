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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import net.sf.rubycollect4j.RubyArray;

public class ProductIteratorTest {

  ProductIterator<Integer> iter;
  RubyArray<List<Integer>> lists;

  @BeforeEach
  public void setUp() throws Exception {
    lists = ra();
    lists.push(ra(1, 2, 3)).push(ra(4, 5, 6));
    iter = new ProductIterator<Integer>(lists);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof ProductIterator);
  }

  @Test
  public void testConstructorException() {
    assertThrows(NullPointerException.class, () -> {
      new ProductIterator<Integer>(null);
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
    assertEquals(ra(1, 4), iter.next());
    assertEquals(ra(1, 5), iter.next());
    assertEquals(ra(1, 6), iter.next());
    assertEquals(ra(2, 4), iter.next());
    assertEquals(ra(2, 5), iter.next());
    assertEquals(ra(2, 6), iter.next());
    assertEquals(ra(3, 4), iter.next());
    assertEquals(ra(3, 5), iter.next());
    assertEquals(ra(3, 6), iter.next());
    assertFalse(iter.hasNext());
    iter = new ProductIterator<Integer>(ra(ra(1, 2, 3)));
    assertEquals(ra(1), iter.next());
    assertEquals(ra(2), iter.next());
    assertEquals(ra(3), iter.next());
    assertFalse(iter.hasNext());
    RubyArray<List<Integer>> lists = ra();
    iter = new ProductIterator<Integer>(lists.push(ra(1, 2, 3)).push(new ArrayList<Integer>()));
    assertFalse(iter.hasNext());
  }

  @Test
  public void testDefensiveCopy() {
    lists.clear();
    assertEquals(ra(1, 4), iter.next());
    assertEquals(ra(1, 5), iter.next());
    assertEquals(ra(1, 6), iter.next());
    assertEquals(ra(2, 4), iter.next());
    assertEquals(ra(2, 5), iter.next());
    assertEquals(ra(2, 6), iter.next());
    assertEquals(ra(3, 4), iter.next());
    assertEquals(ra(3, 5), iter.next());
    assertEquals(ra(3, 6), iter.next());
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
