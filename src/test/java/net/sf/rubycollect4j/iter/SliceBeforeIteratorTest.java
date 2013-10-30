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

import static net.sf.rubycollect4j.RubyCollections.qr;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import net.sf.rubycollect4j.block.BooleanBlock;

import org.junit.Before;
import org.junit.Test;

public class SliceBeforeIteratorTest {

  private SliceBeforeIterator<Integer> iter;
  private List<Integer> list;
  private BooleanBlock<Integer> block;
  private Pattern pattern;

  @Before
  public void setUp() throws Exception {
    list = ra(1, 2, 3, 4, 5);
    block = new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item % 3 == 0;
      }

    };
    pattern = qr("3");
    iter = new SliceBeforeIterator<Integer>(list.iterator(), block);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof SliceBeforeIterator);
    iter = new SliceBeforeIterator<Integer>(list.iterator(), pattern);
    assertTrue(iter instanceof SliceBeforeIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new SliceBeforeIterator<Integer>(null, block);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new SliceBeforeIterator<Integer>(list.iterator(),
        (BooleanBlock<Integer>) null);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException3() {
    new SliceBeforeIterator<Integer>(null, pattern);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException4() {
    new SliceBeforeIterator<Integer>(list.iterator(), (Pattern) null);
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
    assertEquals(ra(1, 2), iter.next());
    assertEquals(ra(3, 4, 5), iter.next());
    assertFalse(iter.hasNext());
    iter = new SliceBeforeIterator<Integer>(list.iterator(), pattern);
    assertEquals(ra(1, 2), iter.next());
    assertEquals(ra(3, 4, 5), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test(expected = NoSuchElementException.class)
  public void testNextException() {
    while (iter.hasNext()) {
      iter.next();
    }
    iter.next();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemove() {
    iter.remove();
  }

}
