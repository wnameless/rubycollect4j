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
package net.sf.rubycollect4j;

import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.TypeConstraintException;

import net.sf.rubycollect4j.block.Block;
import net.sf.rubycollect4j.block.BooleanBlock;
import net.sf.rubycollect4j.block.TransformBlock;

import org.junit.Test;

public class RubyArrayTest {

  private RubyArray<Integer> ra;

  @Test
  public void testConstructor() {
    ra = new RubyArray<Integer>();
    assertTrue(ra instanceof RubyArray);
    ra = new RubyArray<Integer>(new ArrayList<Integer>());
    assertTrue(ra instanceof RubyArray);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException() {
    ra = new RubyArray<Integer>(null);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testAssoc() {
    RubyArray<? extends List<Integer>> ra = ra(ra(1, 2, 3), ra(4, 5, 6));
    assertEquals(ra(4, 5, 6), ra.assoc(4));
    assertNull(ra.assoc(7));
    List<Integer> ints = new ArrayList<Integer>();
    ra = ra(null, ints, ra(1, 2, 3), ra(4, 5, 6));
    assertEquals(ra(1, 2, 3), ra.assoc(1));
    ra = ra(ra(null, 2, 3), ra(4, 5, 6));
    assertEquals(ra(null, 2, 3), ra.assoc(null));
    assertEquals(ra(4, 5, 6), ra.assoc(4));
  }

  @Test
  public void testAt() {
    ra = ra(1, 2, 3, 4);
    assertEquals(Integer.valueOf(1), ra.at(0));
    assertEquals(Integer.valueOf(4), ra.at(-1));
    assertNull(ra.at(4));
    assertNull(ra.at(-5));
    assertNull(ra().at(0));
  }

  @Test
  public void testBsearch() {
    ra = ra(1, 2, 3, 4);
    assertEquals(Integer.valueOf(3), ra.bsearch(3));
    ra = ra(4, 1, 3, 2);
    assertNull(ra.bsearch(4));
    assertNull(ra().bsearch(1));
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

  @Test
  public void testBsearchWithBlock() {
    ra = ra(1, 2, 3, 4, 5, 6, 7);
    assertEquals(Integer.valueOf(6),
        ra.bsearch(new TransformBlock<Integer, Integer>() {

          @Override
          public Integer yield(Integer item) {
            return item - 6;
          }

        }));
    assertEquals(Integer.valueOf(3),
        ra.bsearch(new TransformBlock<Integer, Integer>() {

          @Override
          public Integer yield(Integer item) {
            return item - 3;
          }

        }));
    assertNull(ra.bsearch(new TransformBlock<Integer, Integer>() {

      @Override
      public Integer yield(Integer item) {
        return item - 0;
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
    assertEquals(ra, ra.combination(3, new Block<RubyArray<Integer>>() {

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
    ra = ra(1, 2, null, null);
    assertNull(ra.delete(null));
    assertEquals(ra(1, 2), ra);
  }

  @Test
  public void testDeleteWithBlock() {
    ra = ra(1, 2, 3, 3);
    assertEquals(Integer.valueOf(3),
        ra.delete(3, new TransformBlock<Integer, Integer>() {

          @Override
          public Integer yield(Integer item) {
            return item * 3;
          }

        }));
    assertEquals(ra(1, 2), ra);
    assertEquals(Integer.valueOf(27),
        ra.delete(9, new TransformBlock<Integer, Integer>() {

          @Override
          public Integer yield(Integer item) {
            return item * 3;
          }

        }));
    assertEquals(ra(1, 2), ra);
    ra = ra(1, 2, null, null);
    assertNull(ra.delete(null, new TransformBlock<Integer, Integer>() {

      @Override
      public Integer yield(Integer item) {
        return item * 3;
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
    assertEquals(ra, ra.each(new Block<Integer>() {

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
    assertEquals(ra, ra.eachIndex(new Block<Integer>() {

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
    List<Integer> list = Arrays.asList(1, 2, 3);
    assertTrue(ra.eqlʔ(list));
    assertTrue(ra().eqlʔ(new ArrayList<Object>()));
    assertFalse(ra.eqlʔ(new Object()));
  }

  @Test
  public void testFetch() {
    ra = ra(1, 2, 3, 4);
    assertEquals(Integer.valueOf(3), ra.fetch(2));
  }

  @Test(expected = IndexOutOfBoundsException.class)
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
    assertEquals(Integer.valueOf(3), ra.fetch(2, new Block<Integer>() {

      @Override
      public void yield(Integer index) {
        ints.push(index);
      }

    }));
    assertEquals(null, ra.fetch(4, new Block<Integer>() {

      @Override
      public void yield(Integer index) {
        ints.push(index);
      }

    }));
    assertEquals(ra(4), ints);
    ints.clear();
    assertEquals(null, ra.fetch(-5, new Block<Integer>() {

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
    assertEquals(ra(0, 1, 2, 3),
        ra.fill(new TransformBlock<Integer, Integer>() {

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
        ra.fill(2, new TransformBlock<Integer, Integer>() {

          @Override
          public Integer yield(Integer index) {
            return index;
          }

        }));
    assertEquals(ra(1, 2, 2, 3), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 2, 3),
        ra.fill(-2, new TransformBlock<Integer, Integer>() {

          @Override
          public Integer yield(Integer index) {
            return index;
          }

        }));
    assertEquals(ra(1, 2, 2, 3), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(0, 1, 2, 3),
        ra.fill(-4, new TransformBlock<Integer, Integer>() {

          @Override
          public Integer yield(Integer index) {
            return index;
          }

        }));
    assertEquals(ra(0, 1, 2, 3), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3, 4),
        ra.fill(4, new TransformBlock<Integer, Integer>() {

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
        ra.fill(2, 1, new TransformBlock<Integer, Integer>() {

          @Override
          public Integer yield(Integer index) {
            return index;
          }

        }));
    assertEquals(ra(1, 2, 2, 4), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 2, 4),
        ra.fill(-2, 1, new TransformBlock<Integer, Integer>() {

          @Override
          public Integer yield(Integer index) {
            return index;
          }

        }));
    assertEquals(ra(1, 2, 2, 4), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(0, 2, 3, 4),
        ra.fill(-7, 1, new TransformBlock<Integer, Integer>() {

          @Override
          public Integer yield(Integer index) {
            return index;
          }

        }));
    assertEquals(ra(0, 2, 3, 4), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3, 4, null, null, 6, 7),
        ra.fill(6, 2, new TransformBlock<Integer, Integer>() {

          @Override
          public Integer yield(Integer index) {
            return index;
          }

        }));
    assertEquals(ra(1, 2, 3, 4, null, null, 6, 7), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(0, 1, 2, 3, 4, 5, 6),
        ra.fill(0, 7, new TransformBlock<Integer, Integer>() {

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
    RubyArray<RubyArray<Integer>> ints = ra();
    @SuppressWarnings("unchecked")
    RubyArray<RubyArray<RubyArray<Integer>>> layer3WithNull =
        ra(null, ints, ra(ra(null, 1), ra(2, 3)), ra(ra(4, 5, 6)));
    assertEquals(ra(null, null, 1, 2, 3, 4, 5, 6), layer3WithNull.flatten());
  }

  @Test
  public void testHash() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra.hashCode(), ra.hash());
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

  @Test(expected = IndexOutOfBoundsException.class)
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
  public void testIntersection() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(2, 3), ra.intersection(ra(-1, 2, 3, 6)));
  }

  @Test
  public void testJoin() {
    ra = ra(1, 2, 3, 4);
    assertEquals("1234", ra.join());
    ra.add(null);
    assertEquals("1234", ra.join());
  }

  @Test
  public void testJoinWithSeparator() {
    ra = ra(1, 2, 3, 4);
    assertEquals("1,2,3,4", ra.join(","));
    ra.add(null);
    assertEquals("1\t2\t3\t4\t", ra.join("\t"));
    ra.clear();
    assertEquals("", ra.join(":"));
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

  @Test
  public void testMinus() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 3), ra.minus(ra(2, 4)));
  }

  @Test
  public void testMultiply() {
    ra = ra(1, 2, 3);
    assertEquals(ra(), ra.multiply(0));
    assertEquals(ra(1, 2, 3, 1, 2, 3), ra.multiply(2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultiplyException() {
    ra = ra(1, 2, 3);
    ra.multiply(-1);
  }

  @Test
  public void testMultiplyWithString() {
    ra = ra(1, 2, 3);
    assertEquals("1,2,3", ra.multiply(","));
  }

  @Test
  public void testPack() {
    assertEquals("a  b  c  ", ra("a", "b", "c").pack("A3A3A3"));
    assertEquals("a\\x00\\x00b\\x00\\x00c\\x00\\x00",
        ra("a", "b", "c").pack("a3a3a3"));
    assertEquals("ABC", ra(65, 66, 67).pack("ccc"));
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
    assertEquals(ra, ra.permutation(1, new Block<RubyArray<Integer>>() {

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
    assertEquals(ra, ra.permutation(new Block<RubyArray<Integer>>() {

      @Override
      public void yield(RubyArray<Integer> item) {
        ints.concat(item);
      }

    }));
    assertEquals(ra(1, 2, 3, 1, 3, 2, 2, 1, 3, 2, 3, 1, 3, 1, 2, 3, 2, 1), ints);
  }

  @Test
  public void testPlus() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3, 4, 5, 6), ra.plus(ra(5, 6)));
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
    assertEquals(ra, ra.product(ra(ra(3, 4)), new Block<RubyArray<Integer>>() {

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
  public void testPushWithVarargs() {
    ra = ra();
    assertEquals(ra(1, 2, 3), ra.push(1, 2, 3));
    assertEquals(ra(1, 2, 3), ra);
    assertEquals(ra(1, 2, 3, 4, null, 6), ra.push(4, null, 6));
    assertEquals(ra(1, 2, 3, 4, null, 6), ra);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testRassoc() {
    RubyArray<? extends List<Integer>> ra =
        ra(ra(1, 2), ra(3, 4), ra(4, 4), ra(6, 7));
    assertEquals(ra(3, 4), ra.rassoc(4));
    assertNull(ra.rassoc(6));
    List<Integer> ints = new ArrayList<Integer>();
    ra = ra(null, ints, ra(1, 2, 3), ra(4, 5, 6));
    assertEquals(ra(1, 2, 3), ra.rassoc(3));
    ra = ra(ra(1, 2, null), ra(4, 5, 6));
    assertEquals(ra(1, 2, null), ra.rassoc(null));
    assertEquals(ra(4, 5, 6), ra.rassoc(6));
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
    assertEquals(ra, ra.repeatedCombination(2, new Block<RubyArray<Integer>>() {

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
    assertEquals(ra, ra.repeatedPermutation(2, new Block<RubyArray<Integer>>() {

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

  @Test
  public void testRotate() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(2, 3, 4, 1), ra.rotate());
    assertEquals(ra(1, 2, 3, 4), ra);
    assertEquals(ra(), ra().rotate());
  }

  @Test
  public void testRotateWithN() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3, 4), ra.rotate(0));
    assertEquals(ra(2, 3, 4, 1), ra.rotate(1));
    assertEquals(ra(4, 1, 2, 3), ra.rotate(-1));
    assertEquals(ra(2, 3, 4, 1), ra.rotate(5));
    assertEquals(ra(3, 4, 1, 2), ra.rotate(-6));
    assertEquals(ra(1), ra(1).rotate(-6));
  }

  @Test
  public void testRotateǃ() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(2, 3, 4, 1), ra.rotateǃ());
    assertEquals(ra(2, 3, 4, 1), ra);
    assertEquals(ra(), ra().rotateǃ());
  }

  @Test
  public void testRotateǃWithN() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3, 4), ra.rotateǃ(0));
    assertEquals(ra(1, 2, 3, 4), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(2, 3, 4, 1), ra.rotateǃ(1));
    assertEquals(ra(2, 3, 4, 1), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(4, 1, 2, 3), ra.rotateǃ(-1));
    assertEquals(ra(4, 1, 2, 3), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(2, 3, 4, 1), ra.rotateǃ(5));
    assertEquals(ra(2, 3, 4, 1), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(3, 4, 1, 2), ra.rotateǃ(-6));
    assertEquals(ra(3, 4, 1, 2), ra);
    assertEquals(ra(1), ra(1).rotateǃ(-6));
  }

  @Test
  public void testSample() {
    ra = ra(1, 2, 3, 4);
    assertTrue(ra.includeʔ(ra.sample()));
    assertNull(ra().sample());
  }

  @Test
  public void testSampleWithN() {
    ra = ra(1, 2, 3, 4);
    RubyArray<Integer> samples = ra.sample(3);
    assertEquals(3, samples.uniq().count());
    assertEquals(4, ra.sample(5).uniq().count());
    assertEquals(ra(), ra.sample(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSampleException() {
    ra = ra(1, 2, 3, 4);
    ra.sample(-1);
  }

  @Test
  public void testSelectǃ() {
    ra = ra(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, ra.selectǃ().getClass());
    assertEquals(ra(1, 2, 3, 4), ra.selectǃ().toA());
  }

  @Test
  public void testSelectǃWithBlock() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 3), ra.selectǃ(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item % 2 == 1;
      }

    }));
    assertEquals(ra(1, 3), ra);
    assertNull(ra.selectǃ(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item % 2 == 1;
      }

    }));
  }

  @Test
  public void testShift() {
    ra = ra(1, 2, 3, 4);
    assertEquals(Integer.valueOf(1), ra.shift());
    assertEquals(ra(2, 3, 4), ra);
    ra = ra();
    assertNull(ra.shift());
  }

  @Test
  public void testShiftWithN() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2), ra.shift(2));
    assertEquals(ra(3, 4), ra);
    assertEquals(ra(3, 4), ra.shift(5));
    assertEquals(ra(), ra);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testShiftException() {
    ra = ra(1, 2, 3, 4);
    ra.shift(-1);
  }

  @Test
  public void testShuffle() {
    ra = ra(0).fill(0, 1000, new TransformBlock<Integer, Integer>() {

      @Override
      public Integer yield(Integer index) {
        return index;
      }

    });
    assertFalse(ra.equals(ra.shuffle()));
    assertEquals(ra(0).fill(0, 1000, new TransformBlock<Integer, Integer>() {

      @Override
      public Integer yield(Integer index) {
        return index;
      }

    }), ra);
  }

  @Test
  public void testShuffleǃ() {
    ra = ra(0).fill(0, 1000, new TransformBlock<Integer, Integer>() {

      @Override
      public Integer yield(Integer index) {
        return index;
      }

    });
    ra.shuffleǃ();
    assertFalse(ra(0).fill(0, 1000, new TransformBlock<Integer, Integer>() {

      @Override
      public Integer yield(Integer index) {
        return index;
      }

    }).equals(ra));
  }

  @Test
  public void testSlice() {
    ra = ra(1, 2, 3, 4);
    assertEquals(Integer.valueOf(1), ra.slice(0));
    assertEquals(Integer.valueOf(4), ra.slice(-1));
    assertNull(ra.slice(4));
    assertNull(ra.slice(-5));
  }

  @Test
  public void testSliceWithLength() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3, 4), ra.slice(0, 5));
    assertEquals(ra(3), ra.slice(-2, 1));
    assertNull(ra.slice(4, 2));
    assertNull(ra.slice(-5, 3));
  }

  @Test
  public void testSliceǃ() {
    ra = ra(1, 2, 3, 4);
    assertEquals(Integer.valueOf(1), ra.sliceǃ(0));
    assertEquals(ra(2, 3, 4), ra);
    assertEquals(Integer.valueOf(4), ra.sliceǃ(-1));
    assertEquals(ra(2, 3), ra);
    assertNull(ra.sliceǃ(4));
    assertNull(ra.sliceǃ(-5));
    assertNull(ra().sliceǃ(0));
  }

  @Test
  public void testSliceǃWithLength() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3, 4), ra.sliceǃ(0, 5));
    assertEquals(ra(), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(3), ra.sliceǃ(-2, 1));
    assertEquals(ra(1, 2, 4), ra);
    assertNull(ra.sliceǃ(4, 2));
    assertNull(ra.sliceǃ(-5, 3));
  }

  @Test
  public void testSortǃ() {
    ra = ra(2, 1, 4, 3);
    assertEquals(ra(1, 2, 3, 4), ra.sortǃ());
    assertEquals(ra(1, 2, 3, 4), ra);
    ra = ra(1);
    assertEquals(ra(1), ra.sortǃ());
  }

  @Test
  public void testSortǃWithComparator() {
    ra = ra(4, 1, 2, 3, 3);
    assertEquals(ra(4, 3, 3, 2, 1), ra.sortǃ(new Comparator<Integer>() {

      @Override
      public int compare(Integer o1, Integer o2) {
        return o2 - o1;
      }

    }));
    assertEquals(ra(4, 3, 3, 2, 1), ra);
    assertEquals(ra(1), ra(1).sortǃ(new Comparator<Integer>() {

      @Override
      public int compare(Integer o1, Integer o2) {
        return o2 - o1;
      }

    }));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSortǃException() {
    RubyArray<Point> ra = ra(new Point(1, 2), new Point(3, 4));
    ra.sortǃ();
  }

  @Test
  public void testSortByǃ() {
    ra = ra(1, 2, 3, 4);
    assertEquals(RubyEnumerator.class, ra.sortByǃ().getClass());
    assertEquals(ra(1, 2, 3, 4), ra.sortByǃ().toA());
  }

  @Test
  public void testSortByǃWithComparatorAndBlock() {
    RubyArray<String> ra = ra("aaaa", "bbb", "cc", "d");
    assertEquals(ra("aaaa", "bbb", "cc", "d"),
        ra.sortByǃ(new Comparator<Integer>() {

          @Override
          public int compare(Integer o1, Integer o2) {
            return o2 - o1;
          }
        }, new TransformBlock<String, Integer>() {

          @Override
          public Integer yield(String item) {
            return item.length();
          }

        }));
    assertEquals(ra("aaaa", "bbb", "cc", "d"), ra);
  }

  @Test
  public void testSortByǃWithBlock() {
    RubyArray<String> ra = ra("aaaa", "bbb", "cc", "d");
    assertEquals(ra("d", "cc", "bbb", "aaaa"),
        ra.sortByǃ(new TransformBlock<String, Integer>() {

          @Override
          public Integer yield(String item) {
            return item.length();
          }

        }));
    assertEquals(ra("d", "cc", "bbb", "aaaa"), ra);
  }

  @Test
  public void testSubtract() {
    ra = ra(1, 2, 3, 4, 4);
    assertEquals(ra(1, 2), ra.minus(ra(3, 4, 5)));
  }

  @Test
  public void testToS() {
    ra = ra(1, 2, 3, 4);
    assertEquals("[1, 2, 3, 4]", ra.toS());
    assertEquals(ra.toString(), ra.toS());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testTranspose() {
    assertEquals(ra(), ra().transpose());
    RubyArray<RubyArray<Integer>> ra = ra(ra(1, 2, 3), ra(4, 5, 6));
    assertEquals(ra(ra(1, 4), ra(2, 5), ra(3, 6)), ra.transpose());
  }

  @Test(expected = TypeConstraintException.class)
  public void testTransposeException1() {
    RubyArray<Integer> ra = ra(1, 2, 3);
    ra.transpose();
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testTransposeException2() {
    @SuppressWarnings("unchecked")
    RubyArray<RubyArray<Integer>> ra = ra(ra(1, 2, 3), ra(4, 5));
    ra.transpose();
  }

  @Test
  public void testUnion() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3, 4, 6, 7), ra.union(ra(2, 3, 3, 6, 7)));
  }

  @Test
  public void testUniq() {
    ra = ra(1, 1, 2, 2, 3, 3, 4, 4);
    assertEquals(ra(1, 2, 3, 4), ra.uniq());
  }

  @Test
  public void testUniqWithBlock() {
    RubyArray<String> ra = ra("aa", "bb", "ccc", "ddd", "f");
    assertEquals(ra("aa", "ccc", "f"),
        ra.uniq(new TransformBlock<String, Integer>() {

          @Override
          public Integer yield(String item) {
            return item.length();
          }

        }));
  }

  @Test
  public void testUniqǃ() {
    ra = ra(1, 1, 2, 2, 3, 3, 4, 4);
    assertEquals(ra(1, 2, 3, 4), ra.uniqǃ());
    assertEquals(ra(1, 2, 3, 4), ra);
    ra = ra(1, 2, 3, 4);
    assertNull(ra.uniqǃ());
  }

  @Test
  public void testUniqǃWithBlock() {
    RubyArray<String> ra = ra("aa", "bb", "ccc", "ddd", "f");
    assertEquals(ra("aa", "ccc", "f"),
        ra.uniqǃ(new TransformBlock<String, Integer>() {

          @Override
          public Integer yield(String item) {
            return item.length();
          }

        }));
    assertEquals(ra("aa", "ccc", "f"), ra);
    ra = ra("a", "bb", "ccc", "dddd");
    assertNull(ra.uniqǃ(new TransformBlock<String, Integer>() {

      @Override
      public Integer yield(String item) {
        return item.length();
      }

    }));
  }

  @Test
  public void testUnshift() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(0, 1, 2, 3, 4), ra.unshift(0));
  }

  @Test
  public void testValuesAt() {
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(4, 1, null, null), ra.valuesAt(-1, 0, 5, -6));
  }

  @Test
  public void testSize() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    assertEquals(ints.size(), ra.size());
    ints.clear();
    ra.clear();
    assertEquals(ints.size(), ra.size());
  }

  @Test
  public void testIsEmpty() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    assertEquals(ints.isEmpty(), ra.isEmpty());
    ints.clear();
    ra.clear();
    assertEquals(ints.size(), ra.size());
  }

  @Test
  public void testContains() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    assertEquals(ints.contains(1), ra.contains(1));
  }

  @Test
  public void testIterator() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    assertEquals(ra(ints.iterator()), ra(ra.iterator()));
  }

  @Test
  public void testToArray() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    assertArrayEquals(ints.toArray(), ra.toArray());
  }

  @Test
  public void testToArrayWithArgument() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    assertArrayEquals(ints.toArray(new Integer[4]), ra.toArray(new Integer[4]));
  }

  @Test
  public void testAdd() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    ints.add(5);
    ra.add(5);
    assertEquals(ints, ra);
  }

  @Test
  public void testRemove() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    ints.remove(Integer.valueOf(4));
    ra.remove(Integer.valueOf(4));
    assertEquals(ints, ra);
  }

  @Test
  public void testContainsAll() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    List<Integer> one = Collections.singletonList(1);
    assertEquals(ints.containsAll(one), ra.containsAll(one));
  }

  @Test
  public void testAddAll() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    List<Integer> one = Collections.singletonList(1);
    ints.addAll(one);
    ra.addAll(one);
    assertEquals(ints, ra);
  }

  @Test
  public void testAddAllWithIndex() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    List<Integer> one = Collections.singletonList(1);
    ints.addAll(0, one);
    ra.addAll(0, one);
    assertEquals(ints, ra);
  }

  @Test
  public void testRemoveAll() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    List<Integer> one = Collections.singletonList(1);
    ints.removeAll(one);
    ra.removeAll(one);
    assertEquals(ints, ra);
  }

  @Test
  public void testRetainAll() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    List<Integer> one = Collections.singletonList(1);
    ints.retainAll(one);
    ra.retainAll(one);
    assertEquals(ints, ra);
  }

  @Test
  public void testClear() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    ints.clear();
    ra.clear();
    assertEquals(ints, ra);
  }

  @Test
  public void testGet() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    assertEquals(ints.get(1), ra.get(1));
  }

  @Test
  public void testSet() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    ints.set(0, 0);
    ra.set(0, 0);
    assertEquals(ints, ra);
  }

  @Test
  public void testAddWithIndex() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    ints.add(0, 0);
    ra.add(0, 0);
    assertEquals(ints, ra);
  }

  @Test
  public void testRemoveWithIndex() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    ints.remove(0);
    ra.remove(0);
    assertEquals(ints, ra);
  }

  @Test
  public void testIndexOf() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    assertEquals(ints.indexOf(1), ra.indexOf(1));
  }

  @Test
  public void testLastIndexOf() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    assertEquals(ints.lastIndexOf(1), ra.lastIndexOf(1));
  }

  @Test
  public void testListIterator() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    assertEquals(ra(ints.listIterator()), ra(ra.listIterator()));
  }

  @Test
  public void testListIteratorWithIndex() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    assertEquals(ra(ints.listIterator(1)), ra(ra.listIterator(1)));
  }

  @Test
  public void testSubList() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    assertEquals(ints.subList(0, 2), ra.subList(0, 2));
  }

  @Test
  public void testEquals() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    assertEquals(ints.equals(Arrays.asList(1, 2, 3, 4)),
        ra.equals(Arrays.asList(1, 2, 3, 4)));
  }

  @Test
  public void testHashCode() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    assertEquals(ints.hashCode(), ra.hashCode());
  }

  @Test
  public void testToString() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = newRubyArray(ints, true);
    assertEquals(ints.toString(), ra.toString());
  }

}
