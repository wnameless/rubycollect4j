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
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import net.sf.rubycollect4j.block.BooleanBlock;

import org.junit.Before;
import org.junit.Test;

public class SliceBeforeIterableTest {

  private SliceBeforeIterable<Integer> iter;

  @Before
  public void setUp() throws Exception {
    iter =
        new SliceBeforeIterable<Integer>(ra(1, 2, 3, 4, 5),
            new BooleanBlock<Integer>() {

              @Override
              public boolean yield(Integer item) {
                return item % 2 == 0;
              }

            });
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof SliceBeforeIterable);
    iter = new SliceBeforeIterable<Integer>(ra(1, 2, 3, 4, 5), qr("3"));
    assertTrue(iter instanceof SliceBeforeIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new SliceBeforeIterable<Integer>(null, new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item % 2 == 0;
      }

    });
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    BooleanBlock<Integer> block = null;
    new SliceBeforeIterable<Integer>(ra(1, 2, 3, 4, 5), block);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException3() {
    iter = new SliceBeforeIterable<Integer>(null, qr("3"));
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException4() {
    Pattern pattern = null;
    iter = new SliceBeforeIterable<Integer>(ra(1, 2, 3, 4, 5), pattern);
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof SliceBeforeIterator);
    iter = new SliceBeforeIterable<Integer>(ra(1, 2, 3, 4, 5), qr("3"));
    assertTrue(iter.iterator() instanceof SliceBeforeIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[[1], [2, 3], [4, 5]]", iter.toString());
  }

}
