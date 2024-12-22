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

import static net.sf.rubycollect4j.RubyCollections.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import net.sf.rubycollect4j.util.ComparableEntry;

public class EachWithObjectIteratorTest {

  EachWithObjectIterator<Integer, List<Integer>> iter;
  List<Integer> list;
  List<Integer> emptyList;

  @BeforeEach
  public void setUp() throws Exception {
    emptyList = Collections.emptyList();
    list = ra(1, 2, 3);
    iter = new EachWithObjectIterator<Integer, List<Integer>>(list.iterator(), emptyList);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof EachWithObjectIterator);
  }

  @Test
  public void testConstructorException1() {
    assertThrows(NullPointerException.class, () -> {
      new EachWithObjectIterator<Integer, List<Integer>>(null, emptyList);
    });
  }

  @Test
  public void testConstructorException2() {
    assertThrows(NullPointerException.class, () -> {
      new EachWithObjectIterator<Integer, List<Integer>>(list.iterator(), null);
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
    assertEquals(hp(1, emptyList), iter.next());
    assertEquals(hp(2, emptyList), iter.next());
    assertEquals(hp(3, emptyList), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test
  public void testComparableEntry() {
    assertTrue(iter.next() instanceof ComparableEntry);
  }

  @Test
  public void testRemove() {
    iter.next();
    iter.next();
    iter.remove();
    assertEquals(ra(1, 3), list);
  }

}
