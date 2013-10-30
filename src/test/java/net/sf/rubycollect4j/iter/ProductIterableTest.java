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
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ProductIterableTest {

  private ProductIterable<Integer> iter;
  private List<Integer> list1;
  private List<Integer> list2;
  private List<? extends List<Integer>> lists;

  @Before
  public void setUp() throws Exception {
    list1 = ra(1, 2, 3);
    list2 = ra(4, 5, 6);
    lists = ra(ra(4, 5, 6));
    iter = new ProductIterable<Integer>(list1, lists);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof ProductIterable);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testConstructorWithVarargs() {
    iter = new ProductIterable<Integer>(list1, list2);
    assertTrue(iter instanceof ProductIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new ProductIterable<Integer>(null, lists);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new ProductIterable<Integer>(list1, (List<List<Integer>>) null);
  }

  @SuppressWarnings("unchecked")
  @Test(expected = NullPointerException.class)
  public void testConstructorException3() {
    new ProductIterable<Integer>(null, list2);
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof ProductIterator);
  }

  @Test
  public void testToString() {
    assertEquals(
        "[[1, 4], [1, 5], [1, 6], [2, 4], [2, 5], [2, 6], [3, 4], [3, 5], [3, 6]]",
        iter.toString());
  }

}
