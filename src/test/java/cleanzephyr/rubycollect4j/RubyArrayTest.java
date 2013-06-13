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

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import cleanzephyr.rubycollect4j.block.Block;
import cleanzephyr.rubycollect4j.block.ItemBlock;

import static cleanzephyr.rubycollect4j.RubyArray.newRubyArray;
import static cleanzephyr.rubycollect4j.RubyCollections.ra;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RubyArrayTest {
  private RubyArray<Integer> ra;

  @Test
  public void testFactory() {
    ra = newRubyArray(Arrays.asList(1, 2));
    assertEquals(RubyArray.class, ra.getClass());
    ra = newRubyArray(new Integer[] { 1 });
    assertEquals(RubyArray.class, ra.getClass());
    ra = newRubyArray(1, 2, 3);
    assertEquals(RubyArray.class, ra.getClass());
    ra = newRubyArray(ra.iterator());
    assertEquals(RubyArray.class, ra.getClass());
    List<Integer> ints = newArrayList(1, 2, 3);
    ra = newRubyArray(ints);
    ints.set(0, 4);
    assertEquals(ra(4, 2, 3), ra);
    ints = newArrayList(1, 2, 3);
    ra = newRubyArray(ints, true);
    ints.set(0, 4);
    assertEquals(ra(1, 2, 3), ra);
  }

  @Test
  public void testAssoc() {
    @SuppressWarnings("unchecked")
    RubyArray<RubyArray<Integer>> ra = ra(ra(1, 2, 3), ra(4, 5, 6));
    assertEquals(ra(4, 5, 6), ra.assoc(4));
  }

  @Test
  public void testAt() {
    ra = ra(1, 2, 3, 4);
    assertEquals(Integer.valueOf(1), ra.at(0));
    assertEquals(Integer.valueOf(4), ra.at(-1));
    assertNull(ra.at(4));
    assertNull(ra.at(-5));
  }

  @Test
  public void testBsearch() {
    ra = ra(1, 2, 3, 4);
    assertEquals(Integer.valueOf(3), ra.bsearch(3));
    ra = ra(4, 1, 3, 2);
    assertNull(ra.bsearch(4));
  }

  @Test
  public void testBsearchWithComparator() {
    ra = ra(1, 2, 3, 4);
    assertNull(ra.bsearch(3, new Comparator<Integer>() {

      @Override
      public int compare(Integer arg0, Integer arg1) {
        return arg1 - arg0;
      }

    }));
    ra = ra(4, 3, 2, 1);
    assertEquals(Integer.valueOf(3), ra.bsearch(3, new Comparator<Integer>() {

      @Override
      public int compare(Integer arg0, Integer arg1) {
        return arg1 - arg0;
      }

    }));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testCombination() {
    ra = ra(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, ra.combination(0).getClass());
    assertEquals(ra(), ra.combination(-1).toA());
    assertEquals(ra(ra()), ra.combination(0).toA());
    assertEquals(ra(), ra.combination(5).toA());
    assertEquals(ra(ra(1), ra(2), ra(3), ra(4)), ra.combination(1).toA());
    assertEquals(
        ra(ra(1, 2), ra(1, 3), ra(1, 4), ra(2, 3), ra(2, 4), ra(3, 4)), ra
            .combination(2).toA());
    assertEquals(ra(ra(1, 2, 3), ra(1, 2, 4), ra(1, 3, 4), ra(2, 3, 4)), ra
        .combination(3).toA());
    assertEquals(ra(ra(1, 2, 3, 4)), ra.combination(4).toA());
  }

  @Test
  public void testCombinationWithBlock() {
    ra = ra(1, 2, 3, 4);
    final RubyArray<Integer> ints = ra();
    assertEquals(ra, ra.combination(3, new ItemBlock<RubyArray<Integer>>() {

      @Override
      public void yield(RubyArray<Integer> item) {
        ints.concat(item);
      }

    }));
    assertEquals(ra(1, 2, 3, 1, 2, 4, 1, 3, 4, 2, 3, 4), ints);
  }

  @Test
  public void testCompact() {
    ra = ra(1, 2, 3, null);
    ra = ra.compact();
    assertEquals(ra(1, 2, 3), ra);
  }

  @Test
  public void testCompactǃ() {
    ra = ra(1, 2, 3, null);
    ra.compactǃ();
    assertEquals(ra(1, 2, 3), ra);
    ra = ra(1, 2, 3);
    assertNull(ra.compactǃ());
  }

  @Test
  public void testConcat() {
    ra = ra(1, 2, 3);
    ra.concat(ra(4, 5, 6));
    assertEquals(ra(1, 2, 3, 4, 5, 6), ra);
  }

  @Test
  public void testCount() {
    ra = ra(1, 2, 2, 3);
    assertEquals(2, ra.count(2));
  }

  @Test
  public void testDelete() {
    ra = ra(1, 2, 3, 3);
    assertEquals(Integer.valueOf(3), ra.delete(3));
    assertNull(ra.delete(3));
    assertEquals(ra(1, 2), ra);
  }

  @Test
  public void testDeleteWithBlock() {
    ra = ra(1, 2, 3, 3);
    assertEquals(Integer.valueOf(3), ra.delete(3, new Block<Integer>() {

      @Override
      public Integer yield() {
        return 6;
      }

    }));
    assertEquals(Integer.valueOf(6), ra.delete(3, new Block<Integer>() {

      @Override
      public Integer yield() {
        return 6;
      }

    }));
    assertEquals(ra(1, 2), ra);
  }

}
