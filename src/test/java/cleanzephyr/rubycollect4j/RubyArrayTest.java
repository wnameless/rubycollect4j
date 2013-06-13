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
import cleanzephyr.rubycollect4j.block.BooleanBlock;
import cleanzephyr.rubycollect4j.block.IndexBlock;
import cleanzephyr.rubycollect4j.block.IndexWithReturnBlock;
import cleanzephyr.rubycollect4j.block.ItemBlock;

import static cleanzephyr.rubycollect4j.RubyArray.newRubyArray;
import static cleanzephyr.rubycollect4j.RubyCollections.ra;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

  @Test
  public void testDeteleAt() {
    ra = ra(1, 2, 3, 4);
    assertNull(ra.deleteAt(4));
    assertNull(ra.deleteAt(-5));
    assertEquals(Integer.valueOf(1), ra.deleteAt(0));
    assertEquals(Integer.valueOf(4), ra.deleteAt(-1));
    assertEquals(ra(2, 3), ra);
    ra = ra();
    assertNull(ra.deleteAt(0));
  }

  @Test
  public void testDeleteIf() {
    ra = ra(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, ra.deleteIf().getClass());
    assertEquals(ra(1, 2, 3, 4), ra.deleteIf().toA());
  }

  @Test
  public void testDeleteIfWithBlock() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3, 4), ra.deleteIf(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item > 5;
      }

    }));
    assertEquals(ra(1, 2, 3, 4), ra);
    assertEquals(ra(3, 4), ra.deleteIf(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item < 3;
      }

    }));
    assertEquals(ra(3, 4), ra);
  }

  @Test
  public void testEach() {
    ra = ra(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, ra.each().getClass());
    assertEquals(ra(1, 2, 3, 4), ra.each().toA());
  }

  @Test
  public void testEachWithBlock() {
    ra = ra(1, 2, 3, 4);
    final RubyArray<Integer> ints = ra();
    assertEquals(ra, ra.each(new ItemBlock<Integer>() {

      @Override
      public void yield(Integer item) {
        ints.push(item * 2);
      }

    }));
    assertEquals(ra(2, 4, 6, 8), ints);
  }

  @Test
  public void testEachIndex() {
    ra = ra(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, ra.eachIndex().getClass());
    assertEquals(ra(0, 1, 2, 3), ra.eachIndex().toA());
  }

  @Test
  public void testEachIndexWithBlock() {
    ra = ra(1, 2, 3, 4);
    final RubyArray<Integer> ints = ra();
    assertEquals(ra, ra.eachIndex(new IndexBlock() {

      @Override
      public void yield(Integer index) {
        ints.push(index * 2);
      }

    }));
    assertEquals(ra(0, 2, 4, 6), ints);
  }

  @Test
  public void testEmptyʔ() {
    ra = ra();
    assertTrue(ra.emptyʔ());
    ra = ra(1, 2, 3, 4);
    assertFalse(ra.emptyʔ());
  }

  @Test
  public void testEqlʔ() {
    ra = ra(1, 2, 3);
    assertTrue(ra.eqlʔ(ra(1, 2, 3)));
    assertFalse(ra.eqlʔ(ra(3, 2, 1)));
    List<Integer> list = newArrayList(1, 2, 3);
    assertTrue(ra.eqlʔ(list));
    assertTrue(ra().eqlʔ(newArrayList()));
  }

  @Test
  public void testFetch() {
    ra = ra(1, 2, 3, 4);
    assertEquals(Integer.valueOf(3), ra.fetch(2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFetchException() {
    ra = ra(1, 2, 3, 4);
    try {
      ra.fetch(4);
      fail();
    } catch (Exception e) {}
    ra.fetch(-5);
  }

  @Test
  public void testFetchWithDefaultValue() {
    ra = ra(1, 2, 3, 4);
    assertEquals(Integer.valueOf(3), ra.fetch(2, 10));
    assertEquals(Integer.valueOf(10), ra.fetch(4, 10));
    assertEquals(Integer.valueOf(10), ra.fetch(-5, 10));
  }

  @Test
  public void testFetchWithBlock() {
    ra = ra(1, 2, 3, 4);
    final RubyArray<Integer> ints = ra();
    assertEquals(Integer.valueOf(3), ra.fetch(2, new IndexBlock() {

      @Override
      public void yield(Integer index) {
        ints.push(index);
      }

    }));
    assertEquals(null, ra.fetch(4, new IndexBlock() {

      @Override
      public void yield(Integer index) {
        ints.push(index);
      }

    }));
    assertEquals(ra(4), ints);
    ints.clear();
    assertEquals(null, ra.fetch(-5, new IndexBlock() {

      @Override
      public void yield(Integer index) {
        ints.push(index);
      }

    }));
    assertEquals(ra(-5), ints);
  }

  @Test
  public void testFill() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(5, 5, 5, 5), ra.fill(5));
    assertEquals(ra(5, 5, 5, 5), ra);
  }

  @Test
  public void testFillWithStart() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 5, 5), ra.fill(5, 2));
    assertEquals(ra(1, 2, 5, 5), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 5, 5), ra.fill(5, -2));
    assertEquals(ra(1, 2, 5, 5), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(5, 5, 5, 5), ra.fill(5, -4));
    assertEquals(ra(5, 5, 5, 5), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3, 4), ra.fill(5, 4));
    assertEquals(ra(1, 2, 3, 4), ra);
  }

  @Test
  public void testFillWithStartAndLength() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 5, 4), ra.fill(5, 2, 1));
    assertEquals(ra(1, 2, 5, 4), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 5, 4), ra.fill(5, -2, 1));
    assertEquals(ra(1, 2, 5, 4), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(5, 2, 3, 4), ra.fill(5, -7, 1));
    assertEquals(ra(5, 2, 3, 4), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3, 4, null, null, 5, 5), ra.fill(5, 6, 2));
    assertEquals(ra(1, 2, 3, 4, null, null, 5, 5), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(5, 5, 5, 5, 5, 5, 5), ra.fill(5, 0, 7));
    assertEquals(ra(5, 5, 5, 5, 5, 5, 5), ra);
  }

  @Test
  public void testFillWithBlock() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(0, 1, 2, 3), ra.fill(new IndexWithReturnBlock<Integer>() {

      @Override
      public Integer yield(Integer index) {
        return index;
      }

    }));
    assertEquals(ra(0, 1, 2, 3), ra);
  }

  @Test
  public void testFillWithBlockAndStart() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 2, 3),
        ra.fill(2, new IndexWithReturnBlock<Integer>() {

          @Override
          public Integer yield(Integer index) {
            return index;
          }

        }));
    assertEquals(ra(1, 2, 2, 3), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 2, 3),
        ra.fill(-2, new IndexWithReturnBlock<Integer>() {

          @Override
          public Integer yield(Integer index) {
            return index;
          }

        }));
    assertEquals(ra(1, 2, 2, 3), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(0, 1, 2, 3),
        ra.fill(-4, new IndexWithReturnBlock<Integer>() {

          @Override
          public Integer yield(Integer index) {
            return index;
          }

        }));
    assertEquals(ra(0, 1, 2, 3), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3, 4),
        ra.fill(4, new IndexWithReturnBlock<Integer>() {

          @Override
          public Integer yield(Integer index) {
            return index;
          }

        }));
    assertEquals(ra(1, 2, 3, 4), ra);
  }

  @Test
  public void testFillWithBlockAndStartAndLength() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 2, 4),
        ra.fill(2, 1, new IndexWithReturnBlock<Integer>() {

          @Override
          public Integer yield(Integer index) {
            return index;
          }

        }));
    assertEquals(ra(1, 2, 2, 4), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 2, 4),
        ra.fill(-2, 1, new IndexWithReturnBlock<Integer>() {

          @Override
          public Integer yield(Integer index) {
            return index;
          }

        }));
    assertEquals(ra(1, 2, 2, 4), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(0, 2, 3, 4),
        ra.fill(-7, 1, new IndexWithReturnBlock<Integer>() {

          @Override
          public Integer yield(Integer index) {
            return index;
          }

        }));
    assertEquals(ra(0, 2, 3, 4), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3, 4, null, null, 6, 7),
        ra.fill(6, 2, new IndexWithReturnBlock<Integer>() {

          @Override
          public Integer yield(Integer index) {
            return index;
          }

        }));
    assertEquals(ra(1, 2, 3, 4, null, null, 6, 7), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(0, 1, 2, 3, 4, 5, 6),
        ra.fill(0, 7, new IndexWithReturnBlock<Integer>() {

          @Override
          public Integer yield(Integer index) {
            return index;
          }

        }));
    assertEquals(ra(0, 1, 2, 3, 4, 5, 6), ra);
  }

  @Test
  public void testFlatten() {
    @SuppressWarnings("unchecked")
    RubyArray<RubyArray<Integer>> layer2 = ra(ra(1), ra(2, 3), ra(4, 5, 6));
    assertEquals(ra(1, 2, 3, 4, 5, 6), layer2.flatten());
    @SuppressWarnings("unchecked")
    RubyArray<RubyArray<RubyArray<Integer>>> layer3 =
        ra(ra(ra(1), ra(2, 3)), ra(ra(4, 5, 6)));
    assertEquals(ra(1, 2, 3, 4, 5, 6), layer3.flatten());
  }

  @Test
  public void testIndexWithBlock() {
    ra = ra(1, 2, 3, 4);
    assertEquals(Integer.valueOf(1), ra.index(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item >= 2;
      }

    }));
    assertNull(ra.index(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item > 4;
      }

    }));
  }

  @Test
  public void testIndex() {
    ra = ra(1, 2, 3, 4);
    assertEquals(Integer.valueOf(2), ra.index(3));
    assertNull(ra.index(5));
  }

  @Test
  public void testInsert() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3, 5, 6, 4), ra.insert(-2, 5, 6));
    assertEquals(ra(1, 2, 3, 5, 6, 4), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 5, 6, 3, 4), ra.insert(2, 5, 6));
    assertEquals(ra(1, 2, 5, 6, 3, 4), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3, 4, null, null, null, 5, 6), ra.insert(7, 5, 6));
    assertEquals(ra(1, 2, 3, 4, null, null, null, 5, 6), ra);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInsertException() {
    ra = ra(1, 2, 3, 4);
    ra.insert(-5, 0);
  }

  @Test
  public void testInspect() {
    ra = ra(1, 2, 3, 4);
    assertEquals("[1, 2, 3, 4]", ra.inspect());
    assertEquals(ra.toString(), ra.inspect());
  }

  @Test
  public void testJoin() {
    ra = ra(1, 2, 3, 4);
    assertEquals("1234", ra.join());
    ra.push(null);
    assertEquals("1234", ra.join());
  }

  @Test
  public void testJoinWithSeparator() {
    ra = ra(1, 2, 3, 4);
    assertEquals("1,2,3,4", ra.join(","));
    ra.push(null);
    assertEquals("1\t2\t3\t4\t", ra.join("\t"));
  }

  @Test
  public void testKeepIf() {
    ra = ra(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, ra.keepIf().getClass());
    assertEquals(ra(1, 2, 3, 4), ra.keepIf().toA());
  }

  @Test
  public void testKeepIfWithBlock() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 3), ra.keepIf(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item % 2 == 1;
      }

    }));
    assertEquals(ra(1, 3), ra);
  }

  @Test
  public void testLast() {
    ra = ra(1, 2, 3, 4);
    assertEquals(Integer.valueOf(4), ra.last());
    ra = ra();
    assertNull(ra.last());
  }

  @Test
  public void testLastWithN() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(2, 3, 4), ra.last(3));
    assertEquals(ra(1, 2, 3, 4), ra.last(6));
    ra = ra();
    assertEquals(ra(), ra.last(2));
  }

  @Test
  public void testLength() {
    ra = ra(1, 2, 3, 4);
    assertEquals(4, ra.length());
    assertEquals(ra.size(), ra.length());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testPermutaion() {
    ra = ra(1, 2, 3);
    assertEquals(RubyEnumerator.class, ra.permutation().getClass());
    assertEquals(
        ra(ra(1, 2, 3), ra(1, 3, 2), ra(2, 1, 3), ra(2, 3, 1), ra(3, 1, 2),
            ra(3, 2, 1)), ra.permutation().toA());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testPermutaionWithN() {
    ra = ra(1, 2, 3);
    assertEquals(RubyEnumerator.class, ra.permutation(0).getClass());
    assertEquals(ra(), ra.permutation(-1).toA());
    assertEquals(ra(ra()), ra.permutation(0).toA());
    assertEquals(ra(ra(1), ra(2), ra(3)), ra.permutation(1).toA());
    assertEquals(
        ra(ra(1, 2), ra(1, 3), ra(2, 1), ra(2, 3), ra(3, 1), ra(3, 2)), ra
            .permutation(2).toA());
    assertEquals(
        ra(ra(1, 2, 3), ra(1, 3, 2), ra(2, 1, 3), ra(2, 3, 1), ra(3, 1, 2),
            ra(3, 2, 1)), ra.permutation(3).toA());
    assertEquals(ra(), ra.permutation(4).toA());
  }

  @Test
  public void testPermutaionWithNAndBlock() {
    ra = ra(1, 2, 3);
    final RubyArray<Integer> ints = ra();
    assertEquals(ra, ra.permutation(1, new ItemBlock<RubyArray<Integer>>() {

      @Override
      public void yield(RubyArray<Integer> item) {
        ints.concat(item);
      }

    }));
    assertEquals(ra(1, 2, 3), ints);
  }

  @Test
  public void testPermutaionWithBlock() {
    ra = ra(1, 2, 3);
    final RubyArray<Integer> ints = ra();
    assertEquals(ra, ra.permutation(new ItemBlock<RubyArray<Integer>>() {

      @Override
      public void yield(RubyArray<Integer> item) {
        ints.concat(item);
      }

    }));
    assertEquals(ra(1, 2, 3, 1, 3, 2, 2, 1, 3, 2, 3, 1, 3, 1, 2, 3, 2, 1), ints);
  }

  @Test
  public void testPop() {
    ra = ra();
    assertNull(ra.pop());
    ra = ra(1, 2, 3, 4);
    assertEquals(Integer.valueOf(4), ra.pop());
  }

  @Test
  public void testPopWithN() {
    ra = ra();
    assertEquals(ra(), ra.pop(3));
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(), ra.pop(0));
    assertEquals(ra(3, 4), ra.pop(2));
    assertEquals(ra(1, 2), ra);
    assertEquals(ra(1, 2), ra.pop(3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPopException() {
    ra = ra(1, 2, 3, 4);
    ra.pop(-1);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testProduct() {
    ra = ra(1, 2);
    assertEquals(ra(), ra().product());
    assertEquals(ra(ra(1), ra(2)), ra.product());
    assertEquals(ra(ra(1, 3), ra(1, 4), ra(2, 3), ra(2, 4)),
        ra.product(ra(3, 4)));
    assertEquals(ra(ra(1, 3, 5), ra(1, 4, 5), ra(2, 3, 5), ra(2, 4, 5)),
        ra.product(ra(3, 4), ra(5)));
    assertEquals(ra(ra(1, 3, 5), ra(1, 4, 5), ra(2, 3, 5), ra(2, 4, 5)),
        ra.product(ra(ra(3, 4), ra(5))));
  }

  @Test
  public void testProductWithBlock() {
    ra = ra(1, 2);
    final RubyArray<Integer> ints = ra();
    assertEquals(ra,
        ra.product(ra(ra(3, 4)), new ItemBlock<RubyArray<Integer>>() {

          @Override
          public void yield(RubyArray<Integer> item) {
            ints.concat(item);
          }

        }));
    assertEquals(ra(1, 3, 1, 4, 2, 3, 2, 4), ints);
  }

  @Test
  public void testPush() {
    ra = ra();
    assertEquals(ra(1), ra.push(1));
    assertEquals(ra(1), ra);
    assertEquals(ra(1, 2), ra.push(2));
    assertEquals(ra(1, 2), ra);
  }

  @Test
  public void testRassoc() {
    @SuppressWarnings("unchecked")
    RubyArray<RubyArray<Integer>> ra =
        ra(ra(1, 2), ra(3, 4), ra(4, 4), ra(6, 7));
    assertEquals(ra(3, 4), ra.rassoc(4));
    assertNull(ra.rassoc(6));
  }

  @Test
  public void testReject() {
    ra = ra(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, ra.reject().getClass());
    assertEquals(ra(1, 2, 3, 4), ra.reject().toA());
  }

  @Test
  public void testRejectWithBlock() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2), ra.reject(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item > 2;
      }

    }));
    assertEquals(ra(1, 2, 3, 4), ra);
  }

  @Test
  public void testRejectǃ() {
    ra = ra(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, ra.rejectǃ().getClass());
    assertEquals(ra(1, 2, 3, 4), ra.rejectǃ().toA());
  }

  @Test
  public void testRejectǃWithBlock() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2), ra.rejectǃ(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item > 2;
      }

    }));
    assertEquals(ra(1, 2), ra);
    assertNull(ra.rejectǃ(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item > 2;
      }

    }));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testRepeatedCombination() {
    ra = ra(1, 2);
    assertEquals(RubyEnumerator.class, ra.repeatedCombination(0).getClass());
    assertEquals(ra(), ra.repeatedCombination(-1).toA());
    assertEquals(ra(ra()), ra.repeatedCombination(0).toA());
    assertEquals(ra(ra(1), ra(2)), ra.repeatedCombination(1).toA());
    assertEquals(ra(ra(1, 1), ra(1, 2), ra(2, 2)), ra.repeatedCombination(2)
        .toA());
    assertEquals(ra(ra(1, 1, 1), ra(1, 1, 2), ra(1, 2, 2), ra(2, 2, 2)), ra
        .repeatedCombination(3).toA());
  }

  @Test
  public void testRepeatedCombinationWithBlock() {
    ra = ra(1, 2);
    final RubyArray<Integer> ints = ra();
    assertEquals(ra,
        ra.repeatedCombination(2, new ItemBlock<RubyArray<Integer>>() {

          @Override
          public void yield(RubyArray<Integer> item) {
            ints.concat(item);
          }

        }));
    assertEquals(ra(1, 1, 1, 2, 2, 2), ints);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testRepeatedPermutaion() {
    ra = ra(1, 2);
    assertEquals(RubyEnumerator.class, ra.repeatedPermutation(0).getClass());
    assertEquals(ra(), ra.repeatedPermutation(-1).toA());
    assertEquals(ra(ra()), ra.repeatedPermutation(0).toA());
    assertEquals(ra(ra(1), ra(2)), ra.repeatedPermutation(1).toA());
    assertEquals(ra(ra(1, 1), ra(1, 2), ra(2, 1), ra(2, 2)), ra
        .repeatedPermutation(2).toA());
    assertEquals(
        ra(ra(1, 1, 1), ra(1, 1, 2), ra(1, 2, 1), ra(1, 2, 2), ra(2, 1, 1),
            ra(2, 1, 2), ra(2, 2, 1), ra(2, 2, 2)), ra.repeatedPermutation(3)
            .toA());
  }

  @Test
  public void testRepeatedPermutaionWithBlock() {
    ra = ra(1, 2);
    final RubyArray<Integer> ints = ra();
    assertEquals(ra,
        ra.repeatedPermutation(2, new ItemBlock<RubyArray<Integer>>() {

          @Override
          public void yield(RubyArray<Integer> item) {
            ints.concat(item);
          }

        }));
    assertEquals(ra(1, 1, 1, 2, 2, 1, 2, 2), ints);
  }

  @Test
  public void testReplace() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(4, 5, 6, 7), ra.replace(ra(4, 5, 6, 7)));
    assertEquals(ra(4, 5, 6, 7), ra);
  }

  @Test
  public void testReverse() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(4, 3, 2, 1), ra.reverse());
    assertEquals(ra(1, 2, 3, 4), ra);
  }

  @Test
  public void testReverseǃ() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(4, 3, 2, 1), ra.reverseǃ());
    assertEquals(ra(4, 3, 2, 1), ra);
    ra = ra();
    assertEquals(ra(), ra.reverseǃ());
  }

  @Test
  public void testRindex() {
    ra = ra(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, ra.rindex().getClass());
    assertEquals(ra(4, 3, 2, 1), ra.rindex().toA());
  }

  @Test
  public void testRindexWithBlock() {
    ra = ra(1, 2, 3, 4);
    assertEquals(Integer.valueOf(3), ra.rindex(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item > 1;
      }

    }));
    assertNull(ra.rindex(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item < 1;
      }

    }));
  }

  @Test
  public void testRindexWithTarget() {
    ra = ra(1, 2, 4, 4);
    assertEquals(Integer.valueOf(3), ra.rindex(4));
    assertNull(ra.rindex(5));
  }

}
