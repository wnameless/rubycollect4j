/**
 *
 * @author Wei-Ming Wu
 *
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ZipIteratorTest {

  private ZipIterator<Integer> iter;
  private List<Integer> list;
  private List<? extends Iterator<Integer>> others;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() throws Exception {
    list = ra(1, 2, 3);
    others = ra(ra(4, 5).iterator(), ra(6).iterator());
    iter = new ZipIterator<Integer>(ra(1, 2, 3).iterator(), others);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof ZipIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new ZipIterator<Integer>(null, others);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new ZipIterator<Integer>(list.iterator(), null);
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
    list.clear();
    others.clear();
    assertEquals(ra(1, 4, 6), iter.next());
    assertEquals(ra(2, 5, null), iter.next());
    assertEquals(ra(3, null, null), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemove() {
    iter.remove();
  }

}
