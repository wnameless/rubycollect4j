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

import static net.sf.rubycollect4j.RubyCollections.newRubySet;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.rh;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import net.sf.rubycollect4j.block.Block;
import net.sf.rubycollect4j.block.BooleanBlock;
import net.sf.rubycollect4j.block.TransformBlock;

public class RubySetTest {

  RubySet<Integer> rs;

  @Before
  public void setUp() throws Exception {
    rs = new RubySet<Integer>(Arrays.asList(1, 2, 3, 3));
  }

  @Test
  public void testInterfaces() {
    assertTrue(rs instanceof RubyEnumerable);
    assertTrue(rs instanceof Set);
    assertTrue(rs instanceof Comparable);
    assertTrue(rs instanceof Serializable);
  }

  @Test
  public void testOf() {
    LinkedHashSet<Integer> ints =
        new LinkedHashSet<Integer>(Arrays.asList(1, 2, 3));
    rs = RubySet.of(ints);
    ints.remove(1);
    assertEquals(ints, rs);
  }

  @Test(expected = NullPointerException.class)
  public void testOfException() {
    RubySet.of(null);
  }

  @Test
  public void testCopyOf() {
    LinkedHashSet<Integer> ints =
        new LinkedHashSet<Integer>(Arrays.asList(1, 2, 3));
    rs = RubySet.copyOf(ints);
    ints.remove(1);
    assertEquals(new LinkedHashSet<Integer>(Arrays.asList(1, 2, 3)), rs);
  }

  @Test(expected = NullPointerException.class)
  public void testCopyOfException() {
    RubySet.copyOf(null);
  }

  @Test
  public void testConstructor() {
    assertTrue(rs instanceof RubySet);
    assertTrue(new RubySet<Integer>() instanceof RubySet);
    assertTrue(
        new RubySet<Integer>(new LinkedHashSet<Integer>()) instanceof RubySet);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructor1() {
    new RubySet<Integer>((LinkedHashSet<Integer>) null);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructor2() {
    new RubySet<Integer>((Iterable<Integer>) null);
  }

  @Test
  public void testAddʔ() {
    assertSame(rs, rs.addʔ(4));
    assertEquals(newRubySet(1, 2, 3, 4), rs);
    assertNull(rs.addʔ(4));
  }

  @Test
  public void testClassify() {
    RubyHash<Boolean, RubySet<Integer>> classes =
        newRubySet(1, 2, 3, 4, 5, 6, 7, 7)
            .classify(new TransformBlock<Integer, Boolean>() {

              @Override
              public Boolean yield(Integer item) {
                return item % 2 == 0;
              }

            });
    assertEquals(rh(false, newRubySet(1, 3, 5, 7), true, newRubySet(2, 4, 6)),
        classes);
  }

  @Test
  public void testCollectǃ() {
    assertSame(rs, rs.collectǃ(new TransformBlock<Integer, Integer>() {

      @Override
      public Integer yield(Integer item) {
        return item + 1;
      }

    }));
    assertEquals(newRubySet(2, 3, 4), rs);
  }

  @Test
  public void testCollectǃWithMethodName() {
    assertSame(rs, rs.collectǃ("intValue"));
    assertEquals(newRubySet(1, 2, 3), rs);
  }

  @Test
  public void testDelete() {
    assertSame(rs, rs.delete(1));
    assertEquals(newRubySet(2, 3), rs);
  }

  @Test
  public void testDeleteʔ() {
    assertSame(rs, rs.deleteʔ(1));
    assertEquals(newRubySet(2, 3), rs);
    assertNull(rs.deleteʔ(1));
  }

  @Test
  public void testDeleteIf() {
    assertSame(rs, rs.deleteIf(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item == 1;
      }

    }));
    assertEquals(newRubySet(2, 3), rs);
  }

  @Test
  public void testDifference() {
    assertEquals(newRubySet(1), rs.difference(ra(2, 2, 3)));
  }

  @Test
  public void testDisjointʔ() {
    assertTrue(rs.disjointʔ(ra(4, 5, 6)));
    assertFalse(rs.disjointʔ(ra(3, 4, 5)));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testDivide() {
    assertEquals(newRubySet(newRubySet(1, 3), newRubySet(2)),
        rs.divide(new TransformBlock<Integer, Boolean>() {

          @Override
          public Boolean yield(Integer item) {
            return item % 2 == 0;
          }

        }));
  }

  @Test
  public void testEach() {
    final RubyArray<Integer> ra = ra();
    assertSame(rs, rs.each(new Block<Integer>() {

      @Override
      public void yield(Integer item) {
        ra.add(item);
      }

    }));
    assertEquals(ra, rs.toA());
  }

  @Test
  public void testEmptyʔ() {
    assertFalse(rs.emptyʔ());
    assertTrue(newRubySet().emptyʔ());
  }

  @Test
  public void testExclusive() {
    assertEquals(newRubySet(1, 2, 4, 5), rs.exclusive(ra(3, 4, 5)));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testFlatten() {
    assertEquals(newRubySet(1, 2, 3, 4, 5),
        newRubySet(newRubySet(1, 2, newRubySet(3)), 4,
            newRubySet(newRubySet(5))).flatten());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testFreeze() {
    rs.freeze().freeze().add(null);

  }

  @Test
  public void testFrozenʔ() {
    assertFalse(rs.frozenʔ());
    assertTrue(rs.freeze().frozenʔ());
  }

  @Test
  public void testInspect() {
    assertEquals(rs.toString(), rs.inspect());
  }

  @Test
  public void testIntersectʔ() {
    assertTrue(rs.intersectʔ(ra(3, 4, 5)));
    assertFalse(rs.intersectʔ(ra(4, 5, 6)));
  }

  @Test
  public void testIntersection() {
    assertEquals(newRubySet(3), rs.intersection(ra(3, 4, 5)));
  }

  @Test
  public void testKeepIf() {
    assertSame(rs, rs.keepIf(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item % 2 == 0;
      }

    }));
    assertEquals(ra(2), rs.toA());
  }

  @Test
  public void testLength() {
    assertEquals(rs.size(), rs.length());
  }

  @Test
  public void testMapǃ() {
    assertSame(rs, rs.mapǃ(new TransformBlock<Integer, Integer>() {

      @Override
      public Integer yield(Integer item) {
        return item + 1;
      }

    }));
    assertEquals(newRubySet(2, 3, 4), rs);
  }

  @Test
  public void testMapǃWithMethodName() {
    assertSame(rs, rs.mapǃ("intValue"));
    assertEquals(newRubySet(1, 2, 3), rs);
  }

  @Test
  public void testMerge() {
    assertSame(rs, rs.merge(ra(3, 4, 5)));
    assertEquals(ra(1, 2, 3, 4, 5), rs.toA());
  }

  @Test
  public void testProperSubsetʔ() {
    assertFalse(rs.properSubsetʔ(rs));
    assertFalse(rs.properSubsetʔ(newRubySet(4, 5)));
    assertTrue(rs.properSubsetʔ(RubySet.copyOf(rs).delete(1)));
  }

  @Test
  public void testProperSupersetʔ() {
    assertFalse(rs.properSupersetʔ(rs));
    assertFalse(rs.properSupersetʔ(newRubySet(1, 3, 4, 5)));
    assertTrue(rs.properSupersetʔ(RubySet.copyOf(rs).addʔ(4)));
  }

  @Test
  public void testRejectǃ() {
    BooleanBlock<Integer> block = new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item == 1;
      }

    };
    assertSame(rs, rs.rejectǃ(block));
    assertEquals(ra(2, 3), rs.toA());
    assertNull(rs.rejectǃ(block));
  }

  @Test
  public void testReplace() {
    assertSame(rs, rs.replace(ra(4, 5, 6, 6)));
    assertEquals(ra(4, 5, 6), rs.toA());
  }

  @Test
  public void testSelectǃ() {
    BooleanBlock<Integer> block = new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item == 1;
      }

    };
    assertSame(rs, rs.selectǃ(block));
    assertEquals(ra(1), rs.toA());
    assertNull(rs.selectǃ(block));
  }

  @Test
  public void testSubsetʔ() {
    assertTrue(rs.subsetʔ(rs));
    assertFalse(rs.subsetʔ(newRubySet(1, 2, 3, 4)));
    assertFalse(rs.subsetʔ(newRubySet(4, 5)));
    assertTrue(rs.subsetʔ(RubySet.copyOf(rs).delete(1)));
  }

  @Test
  public void testSubtract() {
    assertEquals(ra(2), rs.subtract(ra(1, 3, 4)).toA());
  }

  @Test
  public void testSupersetʔ() {
    assertTrue(rs.supersetʔ(rs));
    assertFalse(rs.supersetʔ(newRubySet(1, 3, 4, 5)));
    assertFalse(rs.supersetʔ(newRubySet(4, 5)));
    assertTrue(rs.supersetʔ(RubySet.copyOf(rs).addʔ(4)));
  }

  @Test
  public void testUnion() {
    assertEquals(ra(1, 2, 3, 4, 5), rs.union(ra(2, 3, 4, 5)).toA());
  }

  @Test
  public void testAdd() {
    assertTrue(rs.add(4));
    assertFalse(rs.add(4));
  }

  @Test
  public void testAddAll() {
    rs.addAll(ra(2, 3, 4, 5));
    assertEquals(ra(1, 2, 3, 4, 5), rs.toA());
  }

  @Test
  public void testClear() {
    rs.clear();
    assertTrue(rs.isEmpty());
  }

  @Test
  public void testContains() {
    assertTrue(rs.contains(1));
    assertFalse(rs.contains(4));
  }

  @Test
  public void testContainsAll() {
    assertTrue(rs.containsAll(ra(1, 2, 3)));
    assertFalse(rs.containsAll(ra(2, 3, 4)));
  }

  @Test
  public void testIsEmpty() {
    assertFalse(rs.isEmpty());
    rs.clear();
    assertTrue(rs.isEmpty());
  }

  @Test
  public void testRemove() {
    assertTrue(rs.remove(1));
    assertFalse(rs.remove(1));
  }

  @Test
  public void testRemoveAll() {
    assertTrue(rs.removeAll(ra(1, 2)));
    assertFalse(rs.removeAll(ra(1, 2)));
  }

  @Test
  public void testRetainAll() {
    assertTrue(rs.retainAll(ra(1, 2)));
    assertFalse(rs.retainAll(ra(1, 2)));
  }

  @Test
  public void testSize() {
    assertEquals(3, rs.size());
  }

  @Test
  public void testToArray() {
    assertArrayEquals(new Object[] { 1, 2, 3 }, rs.toArray());
  }

  @Test
  public void testToArrayWithArgument() {
    assertArrayEquals(new Integer[] { 1, 2, 3 }, rs.toArray(new Integer[3]));
  }

  @Test
  public void testEquals() {
    assertTrue(rs.equals(new HashSet<Integer>(Arrays.asList(1, 2, 3))));
    assertFalse(rs.equals(new HashSet<Integer>(Arrays.asList(2, 3, 4))));
  }

  @Test
  public void testHashCode() {
    assertEquals(new HashSet<Integer>(Arrays.asList(1, 2, 3)).hashCode(),
        rs.hashCode());
    assertNotEquals(new HashSet<Integer>(Arrays.asList(2, 3, 4)).hashCode(),
        rs.hashCode());
  }

  @Test
  public void testToString() {
    assertEquals("[1, 2, 3]", rs.toString());
  }

  @Test
  public void testCompareTo() {
    assertEquals(ra(1, 2, 3).compareTo(ra(-1, 3, 4)), newRubySet(1, 2, 3)
        .compareTo(new LinkedHashSet<Integer>(Arrays.asList(2, -1, 4))));
  }

}
