/*
 *
 * Copyright 2016 Wei-Ming Wu
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
import static net.sf.rubycollect4j.RubyLiterals.qr;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

public class SliceAfterIteratorTest {

  SliceAfterIterator<Integer> iter;
  List<Integer> list;
  Predicate<Integer> block;
  Pattern pattern;

  @Before
  public void setUp() throws Exception {
    list = ra(1, 2, 3, 4, 5);
    block = item -> item % 3 == 0;
    pattern = qr("3");
    iter = new SliceAfterIterator<Integer>(list.iterator(), block);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof SliceAfterIterator);
    iter = new SliceAfterIterator<Integer>(list.iterator(), pattern);
    assertTrue(iter instanceof SliceAfterIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new SliceAfterIterator<Integer>(null, block);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new SliceAfterIterator<Integer>(list.iterator(), (Predicate<Integer>) null);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException3() {
    new SliceAfterIterator<Integer>(null, pattern);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException4() {
    new SliceAfterIterator<Integer>(list.iterator(), (Pattern) null);
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
    assertEquals(ra(1, 2, 3), iter.next());
    assertEquals(ra(4, 5), iter.next());
    assertFalse(iter.hasNext());
    iter = new SliceAfterIterator<Integer>(list.iterator(), pattern);
    assertEquals(ra(1, 2, 3), iter.next());
    assertEquals(ra(4, 5), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemove() {
    iter.remove();
  }

}
