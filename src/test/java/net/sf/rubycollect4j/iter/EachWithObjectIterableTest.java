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

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class EachWithObjectIterableTest {

  private EachWithObjectIterable<Integer, List<Integer>> iter;
  private List<Integer> list;

  @Before
  public void setUp() throws Exception {
    list = ra(1, 2, 3);
    iter =
        new EachWithObjectIterable<Integer, List<Integer>>(list,
            new ArrayList<Integer>());
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof EachWithObjectIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new EachWithObjectIterable<Integer, List<Integer>>(null,
        new ArrayList<Integer>());
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    new EachWithObjectIterable<Integer, List<Integer>>(list, null);
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof EachWithObjectIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[1=[], 2=[], 3=[]]", iter.toString());
  }

}
