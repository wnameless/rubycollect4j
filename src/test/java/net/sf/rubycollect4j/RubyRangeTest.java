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
package net.sf.rubycollect4j;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.range;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;

import org.junit.Test;

import net.sf.rubycollect4j.succ.DoubleSuccessor;
import net.sf.rubycollect4j.succ.StringSuccessor;

public class RubyRangeTest {

  @Test
  public void testInterfaces() {
    assertTrue(range("A", "Z") instanceof RubyEnumerable);
    assertTrue(range("A", "Z") instanceof Serializable);
  }

  @Test
  public void testConstructor() {
    RubyRange<String> range =
        new RubyRange<String>(StringSuccessor.getInstance(), "a", "z");
    assertTrue(range instanceof RubyRange);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorWithNullSuccessive() {
    new RubyRange<String>(null, "a", "z");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNullStartPoint() {
    new RubyRange<String>(StringSuccessor.getInstance(), null, "z");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNullEndPoint() {
    new RubyRange<String>(StringSuccessor.getInstance(), "a", null);
  }

  @Test
  public void testRangeWithString() {
    assertEquals(ra("abcd", "abce"), range("abcd", "abce").toA());
    assertEquals(ra("THX1138", "THX1139"), range("THX1138", "THX1139").toA());
    assertEquals(ra("<<koala>>", "<<koalb>>"),
        range("<<koala>>", "<<koalb>>").toA());
    assertEquals(ra("1999zzz", "2000aaa"), range("1999zzz", "2000aaa").toA());
    assertEquals(ra("zzz9999", "aaaa0000"), range("zzz9999", "aaaa0000").toA());
    assertEquals(ra("***", "**+"), range("***", "**+").toA());
    assertEquals(ra("a", "b", "c", "d", "e"), range("a", "e").toA());
    assertEquals(ra("ay", "az", "ba"), range("ay", "ba").toA());
    assertEquals(ra("aY", "aZ", "bA"), range("aY", "bA").toA());
    assertEquals(ra("999--", "1000--", "1001--"),
        range("999--", "1001--").toA());
    assertEquals(ra("999", "1000", "1001"), range("999", "1001").toA());
    assertEquals(ra("1.2", "1.3", "1.4", "1.5"), range("1.2", "1.5").toA());
    assertEquals(ra("1.49", "1.50", "1.51"), range("1.49", "1.51").toA());
    assertEquals(ra(), range("zzz", "aaa").toA());
    assertEquals(ra("Z", "AA", "AB"), range("Z", "AB").toA());
  }

  @Test
  public void testRangeWithInteger() {
    assertEquals(ra(-1, 0, 1, 2, 3, 4, 5, 6), range(-1, 6).toA());
  }

  @Test
  public void testRangeWithLong() {
    assertEquals(ra(-1L, 0L, 1L, 2L, 3L, 4L, 5L, 6L), range(-1L, 6L).toA());
    assertEquals(ra(), range(-1L, -2L).toA());
  }

  @Test
  public void testRangeWithDouble() {
    assertEquals(ra(1.08, 1.09, 1.10, 1.11), range(1.08, 1.11).toA());
    assertEquals(ra(), range(-1.0, -2.0).toA());
  }

  @Test
  public void testRangeWithDate() {
    assertEquals(
        ra(Ruby.Date.of(2013, 7, 2), Ruby.Date.of(2013, 7, 3),
            Ruby.Date.of(2013, 7, 4)),
        range(Ruby.Date.of(2013, 7, 2), Ruby.Date.of(2013, 7, 4)).toA());
    assertEquals(ra(),
        range(Ruby.Date.of(2013, 7, 4), Ruby.Date.of(2013, 7, 2)).toA());
  }

  @Test
  public void testBegin() {
    assertEquals(Integer.valueOf(1), range(1, 1000).begin());
  }

  @Test
  public void testCoverʔ() {
    assertTrue(range(1, 100).coverʔ(50));
    assertTrue(range(1, 100).coverʔ(1));
    assertTrue(range(1, 100).coverʔ(100));
    assertFalse(range(1, 100).coverʔ(101));
    assertFalse(range(1, 100).coverʔ(-1));
  }

  @Test
  public void testEach() {
    assertTrue(range(1, 10).each() instanceof RubyEnumerator);
    assertEquals(ra(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), range(1, 10).each().toA());
  }

  @Test
  public void testEachWithBlock() {
    final RubyArray<Integer> ints = ra();
    assertEquals(RubyRange.class,
        range(1, 10).each(item -> ints.push(item)).getClass());
    assertEquals(ra(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), ints);
  }

  @Test
  public void testEnd() {
    assertEquals(Integer.valueOf(1000), range(1, 1000).end());
  }

  @Test
  public void testEqlʔ() {
    assertTrue(range(1, 1000).eqlʔ(range(1, 1000)));
    assertFalse(range(1, 1000).eqlʔ(range(1, 1001)));
    assertFalse(range(1, 1000).eqlʔ(range(2, 1000)));
    assertFalse(range(1, 1000).eqlʔ(null));
    assertFalse(new RubyRange<Double>(new DoubleSuccessor(1), 1.0, 2.0)
        .eqlʔ(new RubyRange<Double>(new DoubleSuccessor(2), 1.0, 2.0)));
  }

  @Test
  public void testHash() {
    assertEquals(range(1, 100).hashCode(), range(1, 100).hash());
    assertNotSame(range(1, 101).hashCode(), range(1, 100).hash());
  }

  @Test
  public void testIncludeʔ() {
    assertTrue(range(1, 100).includeʔ(50));
    assertTrue(range(1, 100).includeʔ(1));
    assertTrue(range(1, 100).includeʔ(100));
    assertFalse(range(1, 100).includeʔ(101));
    assertFalse(range(1, 100).coverʔ(-1));
  }

  @Test
  public void testInspect() {
    assertEquals(range(1, 100).toString(), range(1, 100).inspect());
    assertEquals("1..100", range(1, 100).inspect());
  }

  @Test
  public void testLast() {
    assertEquals(Integer.valueOf(1000), range(1, 1000).last());
  }

  @Test
  public void testLastWithN() {
    assertEquals(ra(96, 97, 98, 99, 100), range(1, 100).last(5));
    assertEquals(ra(), range(1, 100).last(-1));
  }

  @Test
  public void testMemeberʔ() {
    assertTrue(range(1, 100).memberʔ(50));
    assertTrue(range(1, 100).memberʔ(1));
    assertTrue(range(1, 100).memberʔ(100));
    assertFalse(range(1, 100).memberʔ(101));
  }

  @Test
  public void testStep() {
    assertTrue(range(1, 10).step(3) instanceof RubyEnumerator);
    assertEquals(ra(1, 4, 7, 10), range(1, 10).step(3).toA());
  }

  @Test
  public void testStepWithBlock() {
    final RubyArray<Integer> ints = ra();
    assertTrue(range(1, 10).step(3) instanceof RubyEnumerator);
    assertEquals(RubyRange.class,
        range(1, 10).step(3, item -> ints.push(item)).getClass());
    assertEquals(ra(1, 4, 7, 10), ints);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStepException1() {
    range(1, 10).step(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStepException2() {
    range(1, 10).step(-1);
  }

  @Test
  public void testEquals() {
    assertTrue(range(1, 1000).equals(range(1, 1000)));
    assertFalse(range(1, 1000).equals(range(1, 1001)));
    assertFalse(range(1, 1000).equals(range(2, 1000)));
    assertFalse(range(1, 1000).equals(null));
    assertFalse(new RubyRange<Double>(new DoubleSuccessor(1), 1.0, 2.0)
        .equals(new RubyRange<Double>(new DoubleSuccessor(2), 1.0, 2.0)));
  }

  @Test
  public void testToS() {
    assertEquals(range(1, 100).toString(), range(1, 100).toS());
    assertEquals("1..100", range(1, 100).toS());
  }

}
