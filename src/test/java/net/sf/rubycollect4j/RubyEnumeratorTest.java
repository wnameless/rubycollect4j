/*
 *
 * Copyright 2013-2015 Wei-Ming Wu
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
package net.sf.rubycollect4j;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.sf.rubycollect4j.block.Block;

public class RubyEnumeratorTest {

  RubyEnumerator<Integer> re;
  List<Integer> list;

  @Before
  public void setUp() throws Exception {
    list = new ArrayList<Integer>(Arrays.asList(1, 2, 3));
    re = new RubyEnumerator<Integer>(list);
  }

  @Test
  public void testOf() {
    re = RubyEnumerator.of(list);
    list.remove(0);
    assertEquals(ra(2, 3), re.toA());
  }

  @Test(expected = NullPointerException.class)
  public void testOfException() {
    RubyEnumerator.of(null);
  }

  @Test
  public void testCopyOf() {
    re = RubyEnumerator.copyOf(list);
    list.remove(0);
    assertEquals(ra(1, 2, 3), re.toA());
  }

  @Test(expected = NullPointerException.class)
  public void testCopyOfException() {
    RubyEnumerator.copyOf(null);
  }

  @Test
  public void testConstructor() {
    assertTrue(re instanceof RubyEnumerator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException() {
    new RubyEnumerator<Integer>((Iterable<Integer>) null);
  }

  @Test
  public void testEach() {
    assertEquals(re.toA(), re.each().toA());
  }

  @Test
  public void testEachWithBlock() {
    final List<Integer> ints = new ArrayList<Integer>();
    assertSame(re, re.each(new Block<Integer>() {

      @Override
      public void yield(Integer item) {
        ints.add(item);
      }

    }));
    assertEquals(Arrays.asList(1, 2, 3), ints);
  }

  @Test
  public void testRewind() {
    while (re.hasNext()) {
      re.next();
    }
    re.rewind();
    assertTrue(re.hasNext());
  }

  @Test
  public void testPeek() {
    while (re.hasNext()) {
      Integer peeking = re.peek();
      assertEquals(peeking, re.next());
    }
  }

  @Test
  public void testIterator() {
    assertTrue(re.iterator() instanceof Iterator);
  }

  @Test
  public void testHasNext() {
    Iterator<Integer> listIt = list.iterator();
    while (re.hasNext()) {
      assertTrue(listIt.hasNext());
      re.next();
      listIt.next();
    }
    assertFalse(listIt.hasNext());
  }

  @Test
  public void testNext() {
    Iterator<Integer> listIt = list.iterator();
    while (re.hasNext()) {
      assertEquals(listIt.next(), re.next());
    }
    assertFalse(listIt.hasNext());
  }

  @Test
  public void testRemove() {
    List<Integer> ints = new ArrayList<Integer>(list);
    Iterator<Integer> intsIt = ints.iterator();
    re.next();
    re.remove();
    intsIt.next();
    intsIt.remove();
    assertEquals(ints, list);
  }

  @Test
  public void testToString() {
    assertEquals("RubyEnumerator{[1, 2, 3]}", re.toString());
  }

}
