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
package cleanzephyr.rubycollect4j;

import static cleanzephyr.rubycollect4j.RubyCollections.ra;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import org.junit.Test;

public class RubyArrayListTests {

  public RubyArrayListTests() {
  }

  /**
   * Test of and method, of class RubyArrayList.
   */
  @Test
  public void testIntersect() {
    assertEquals(ra(3), ra(1, 2, 3).intersection(ra(3, 4, 5)));
  }

  /**
   * Test of multiply method, of class RubyArrayList.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMultiply() {
    assertEquals(ra(1, 2, 1, 2), ra(1, 2).multiply(2));
    assertEquals("1, 2", ra(1, 2).multiply(", "));
    assertEquals(ra(), ra(1, 2).multiply(0));
    ra(1, 2).multiply(-1);
  }

  /**
   * Test of add method, of class RubyArrayList.
   */
  @Test
  public void testAdd() {
    assertEquals(ra(1, 2, 3), ra(1).add(ra(2, 3)));
  }

  /**
   * Test of minus method, of class RubyArrayList.
   */
  @Test
  public void testMinus() {
    assertEquals(ra(1), ra(1).minus(ra(2, 3)));
    assertEquals(ra(1), ra(1, 2, 3).minus(ra(2, 3)));
    assertEquals(ra(), ra(1).minus(ra(1, 2, 3)));
  }

  /**
   * Test of assoc method, of class RubyArrayList.
   */
  @Test
  public void testAssoc() {
    assertNull(ra(1, 2, 3).assoc(1));
    assertNull(ra(ra(2, 3)).assoc(1));
    assertEquals(ra(4, 5, 6), ra(ra(1, 2, 3), ra(4, 5, 6)).assoc(4));
  }

  /**
   * Test of at method, of class RubyArrayList.
   */
  @Test
  public void testAt() {
    assertNull(ra("a", "b", "c").at(3));
    assertNull(ra("a", "b", "c").at(-4));
    assertEquals("c", ra("a", "b", "c").at(2));
    assertEquals("a", ra("a", "b", "c").at(-3));
  }

  /**
   * Test of bsearch method, of class RubyArrayList.
   */
  @Test
  public void testBsearch() {
    assertEquals(new Integer(15), ra(45, 2, 15, 8, 3).bsearch(new Integer(15)));
    assertNull(ra(45, 2, 15, 8, 3).bsearch(new Integer(16)));
  }

  /**
   * Test of bsearch method, of class RubyArrayList.
   */
  @Test
  public void testBsearchWithComparator() {
    assertEquals(new Integer(15), ra(45, 2, 15, 8, 3).bsearch(new Integer(15), (i1, i2) -> {
      return i1.compareTo(i2);
    }));
    assertNull(ra(45, 2, 15, 8, 3).bsearch(new Integer(16), (i1, i2) -> {
      return i1.compareTo(i2);
    }));
  }

  /**
   * Test of combination method, of class RubyArrayList.
   */
  @Test
  public void testCombination() {
    assertEquals(ra(), ra(1, 2, 3).combination(-1));
    assertEquals(ra(), ra(1, 2, 3).combination(4));
    assertEquals(ra(ra()), ra(1, 2, 3).combination(0));
    assertEquals(ra(ra(1), ra(2), ra(3)), ra(1, 2, 3).combination(1));
    assertEquals(ra(ra(1, 2), ra(1, 3), ra(2, 3)), ra(1, 2, 3).combination(2));
    assertEquals(ra(ra(1, 2, 3)), ra(1, 2, 3).combination(3));
  }

  /**
   * Test of combination method, of class RubyArrayList.
   */
  @Test
  public void testCombinationWithBlock() {
    final int[] i = new int[1];
    ra(1, 2, 3).combination(2, (c) -> {
      if (i[0] == 0) {
        assertEquals(ra(1, 2), c);
      }
      if (i[0] == 1) {
        assertEquals(ra(1, 3), c);
      }
      if (i[0] == 2) {
        assertEquals(ra(2, 3), c);
      }
      i[0]++;
    });
  }

  /**
   * Test of compact method, of class RubyArrayList.
   */
  @Test
  public void testCompact() {
    assertEquals(ra(1, 3), ra(1, null, 3, null).compact());
  }

  /**
   * Test of compactǃ method, of class RubyArrayList.
   */
  @Test
  public void testCompactǃ() {
    RubyArray<Integer> rubyArray = ra(1, null, 3, null);
    rubyArray.compactǃ();
    assertEquals(ra(1, 3), rubyArray);
  }

  /**
   * Test of concat method, of class RubyArrayList.
   */
  @Test
  public void testConcat() {
    assertEquals(ra(1, 2, 3, 4, 5, 6), ra(1, 2, 3).concat(ra(4, 5, 6)));
  }

  /**
   * Test of repeatedCombination method, of class RubyArrayList.
   */
  @Test
  public void testRepeatedCombination() {
    assertEquals(ra(), ra(1, 2).repeatedCombination(-1));
    assertEquals(ra(ra(1, 1, 1), ra(1, 1, 2), ra(1, 2, 2), ra(2, 2, 2)), ra(1, 2).repeatedCombination(3));
    assertEquals(ra(ra()), ra(1, 2, 3).repeatedCombination(0));
    assertEquals(ra(ra(1), ra(2), ra(3)), ra(1, 2, 3).repeatedCombination(1));
    assertEquals(ra(ra(1, 1), ra(1, 2), ra(1, 3), ra(2, 2), ra(2, 3), ra(3, 3)), ra(1, 2, 3).repeatedCombination(2));
    assertEquals(ra(ra(1, 1), ra(1, 2), ra(2, 2)), ra(1, 2).repeatedCombination(2));
  }

  /**
   * Test of repeatedCombination method, of class RubyArrayList.
   */
  @Test
  public void testRepeatedCombinationWithBlock() {
    final int[] i = new int[1];
    ra(1, 2).repeatedCombination(2, (c) -> {
      if (i[0] == 0) {
        assertEquals(ra(1, 1), c);
      }
      if (i[0] == 1) {
        assertEquals(ra(1, 2), c);
      }
      if (i[0] == 2) {
        assertEquals(ra(2, 2), c);
      }
      i[0]++;
    });
  }
}
