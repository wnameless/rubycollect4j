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

import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ZipIteratorTest {

  ZipIterator<Integer> iter;
  List<Integer> list;
  List<? extends Iterator<Integer>> others;

  @BeforeEach
  public void setUp() throws Exception {
    list = ra(1, 2, 3);
    others = ra(ra(4, 5).iterator(), ra(6).iterator());
    iter = new ZipIterator<Integer>(list.iterator(), others);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof ZipIterator);
  }

  @Test
  public void testConstructorException1() {
    assertThrows(NullPointerException.class, () -> {
      new ZipIterator<Integer>(null, others);
    });
  }

  @Test
  public void testConstructorException2() {
    assertThrows(NullPointerException.class, () -> {
      new ZipIterator<Integer>(list.iterator(), null);
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
    assertEquals(ra(1, 4, 6), iter.next());
    assertEquals(ra(2, 5, null), iter.next());
    assertEquals(ra(3, null, null), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test
  public void testDefensiveCopy() {
    others.clear();
    assertEquals(ra(1, 4, 6), iter.next());
    assertEquals(ra(2, 5, null), iter.next());
    assertEquals(ra(3, null, null), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test
  public void testRemove() {
    assertThrows(UnsupportedOperationException.class, () -> {
      iter.remove();
    });
  }

}
