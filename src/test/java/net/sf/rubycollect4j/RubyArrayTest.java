/*
 *
 * Copyright 2013 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package net.sf.rubycollect4j;

import static net.sf.rubycollect4j.RubyCollections.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RubyArrayTest {

  RubyArray<Integer> ra;
  RubyArray<Integer> frozenRa;
  Function<Integer, Integer> block;
  Comparator<Integer> comp;

  @BeforeEach
  public void setUp() throws Exception {
    ra = ra(1, 2, 3, 4);
    frozenRa = ra(1, 2, 3, 4).freeze();
    block = item -> item * 2;
    comp = (o1, o2) -> o2 - o1;
  }

  @Test
  public void testInterfaces() {
    assertTrue(ra instanceof RubyEnumerable);
    assertTrue(ra instanceof List);
    assertTrue(ra instanceof Comparable);
    assertTrue(ra instanceof Serializable);
  }

  @Test
  public void testStaticFactoryMethodOf() {
    List<Integer> list = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    RubyArray<Integer> ints = RubyArray.of(list);
    list.remove(0);
    assertEquals(list, ints);
  }

  @Test
  public void testStaticFactoryMethodOfException() {
    assertThrows(NullPointerException.class, () -> {
      RubyArray.of(null);
    });
  }

  @Test
  public void testStaticFactoryMethodCopyOf() {
    List<Integer> list = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    RubyArray<Integer> ints = RubyArray.copyOf(list);
    list.remove(0);
    assertNotEquals(list, ints);
  }

  @Test
  public void testStaticFactoryMethodCopyOfException() {
    assertThrows(NullPointerException.class, () -> {
      RubyArray.copyOf(null);
    });
  }

  @Test
  public void testConstructor() {
    assertTrue(ra instanceof RubyArray);
    ra = new RubyArray<Integer>(new ArrayList<Integer>());
    assertTrue(ra instanceof RubyArray);
    Iterable<Integer> iter = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = new RubyArray<Integer>(iter);
    assertEquals(ra(1, 2, 3, 4), ra);
  }

  @Test
  public void testConstructorException1() {
    assertThrows(NullPointerException.class, () -> {
      new RubyArray<Integer>((List<Integer>) null);
    });
  }

  @Test
  public void testConstructorException2() {
    assertThrows(NullPointerException.class, () -> {
      new RubyArray<Integer>((Iterable<Integer>) null);
    });
  }

  @Test
  public void testAssoc() {
    RubyArray<? extends List<Integer>> ra = ra(ra(1, 2, 3), ra(4, 5, 6));
    assertEquals(ra(4, 5, 6), ra.assoc(4));
    assertNull(ra.assoc(7));
    List<Integer> ints = new ArrayList<Integer>();
    ra = ra(null, ints, ra(1, 2, 3), ra(4, 5, 6));
    assertEquals(ra(1, 2, 3), ra.assoc(1));
    ra = ra(ra(1, 2, 3), ra(null, 5, 6), ra(7, 8, 9));
    assertEquals(ra(null, 5, 6), ra.assoc(null));
    assertEquals(ra(7, 8, 9), ra.assoc(7));
  }

  @Test
  public void testAt() {
    assertEquals(Integer.valueOf(1), ra.at(0));
    assertEquals(Integer.valueOf(4), ra.at(-1));
    assertNull(ra.at(4));
    assertNull(ra.at(-5));
    assertNull(ra().at(0));
  }

  @Test
  public void testBsearch() {
    assertEquals(Integer.valueOf(3), ra.bsearch(3));
    ra = ra(4, 1, 3, 2);
    assertNull(ra.bsearch(4));
    assertNull(ra().bsearch(1));
  }

  @Test
  public void testBsearchWithComparator() {
    Comparator<Integer> comp = (arg0, arg1) -> arg1 - arg0;
    assertNull(ra.bsearch(3, comp));
    ra = ra(4, 3, 2, 1);
    assertEquals(Integer.valueOf(3), ra.bsearch(3, comp));
  }

  @Test
  public void testBsearchWithBlock() {
    ra = ra(1, 2, 3, 4, 5, 6, 7);
    assertEquals(Integer.valueOf(6), ra.bsearch(item -> item - 6));
    assertEquals(Integer.valueOf(3), ra.bsearch(item -> item - 3));
    assertNull(ra.bsearch(item -> item - 0));
  }

  @Test
  public void testCollectǃ() {
    assertSame(ra, ra.collectǃ(block));
    assertEquals(ra(2, 4, 6, 8), ra);
  }

  @Test
  public void testCombination() {
    assertEquals(RubyEnumerator.class, ra.combination(0).getClass());
    assertEquals(ra(), ra.combination(-1).toA());
    assertEquals(ra(ra()), ra.combination(0).toA());
    assertEquals(ra(), ra.combination(5).toA());
    assertEquals(ra(ra(1), ra(2), ra(3), ra(4)), ra.combination(1).toA());
    assertEquals(ra(ra(1, 2), ra(1, 3), ra(1, 4), ra(2, 3), ra(2, 4), ra(3, 4)),
        ra.combination(2).toA());
    assertEquals(ra(ra(1, 2, 3), ra(1, 2, 4), ra(1, 3, 4), ra(2, 3, 4)), ra.combination(3).toA());
    assertEquals(ra(ra(1, 2, 3, 4)), ra.combination(4).toA());
  }

  @Test
  public void testCombinationWithBlock() {
    final RubyArray<Integer> ints = ra();
    assertEquals(ra, ra.combination(3, item -> ints.concat(item)));
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
    ra.concat(ra(5, 6, 7, 8));
    assertEquals(ra(1, 2, 3, 4, 5, 6, 7, 8), ra);
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
    assertEquals(Integer.valueOf(3), ra.delete(3, block));
    assertEquals(ra(1, 2), ra);
    assertEquals(Integer.valueOf(27), ra.delete(9, item -> item * 3));
    assertEquals(ra(1, 2), ra);
    ra = ra(1, 2, null, null);
    assertNull(ra.delete(null, block));
    assertEquals(ra(1, 2), ra);
  }

  @Test
  public void testDeteleAt() {
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
    assertEquals(RubyEnumerator.class, ra.deleteIf().getClass());
    assertEquals(ra(1, 2, 3, 4), ra.deleteIf().toA());
  }

  @Test
  public void testDeleteIfWithBlock() {
    assertEquals(ra(1, 2, 3, 4), ra.deleteIf(item -> item > 5));
    assertEquals(ra(1, 2, 3, 4), ra);
    assertEquals(ra(3, 4), ra.deleteIf(item -> item < 3));
    assertEquals(ra(3, 4), ra);
  }

  @Test
  public void testDifference() {
    assertEquals(ra(1, 2, 2), ra(1, 2, 2, 3, 4).difference(ra(4, 3)));
  }

  @Test
  public void testEach() {
    assertEquals(RubyEnumerator.class, ra.each().getClass());
    assertEquals(ra(1, 2, 3, 4), ra.each().toA());
  }

  @Test
  public void testEachWithBlock() {
    final RubyArray<Integer> ints = ra();
    assertEquals(ra, ra.each(item -> ints.push(item * 2)));
    assertEquals(ra(2, 4, 6, 8), ints);
  }

  @Test
  public void testEachIndex() {
    assertEquals(RubyEnumerator.class, ra.eachIndex().getClass());
    assertEquals(ra(0, 1, 2, 3), ra.eachIndex().toA());
  }

  @Test
  public void testEachIndexWithBlock() {
    final RubyArray<Integer> ints = ra();
    assertEquals(ra, ra.eachIndex(index -> ints.push(index * 2)));
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
    assertTrue(ra.eqlʔ(ra(1, 2, 3, 4)));
    assertFalse(ra.eqlʔ(ra(4, 3, 2, 1)));
    List<Integer> list = Arrays.asList(1, 2, 3, 4);
    assertTrue(ra.eqlʔ(list));
    assertTrue(ra().eqlʔ(new ArrayList<Object>()));
    assertFalse(ra.eqlʔ(new Object()));
  }

  @Test
  public void testFetch() {
    assertEquals(Integer.valueOf(3), ra.fetch(2));
  }

  @Test
  public void testFetchException() {
    assertThrows(IndexOutOfBoundsException.class, () -> {
      try {
        ra.fetch(4);
        fail();
      } catch (IndexOutOfBoundsException e) {}
      ra.fetch(-5);
    });
  }

  @Test
  public void testFetchWithDefaultValue() {
    assertEquals(Integer.valueOf(3), ra.fetch(2, 10));
    assertEquals(Integer.valueOf(10), ra.fetch(4, 10));
    assertEquals(Integer.valueOf(10), ra.fetch(-5, 10));
  }

  @Test
  public void testFetchWithBlock() {
    final RubyArray<Integer> ints = ra();
    Consumer<Integer> block = index -> ints.push(index);
    assertEquals(Integer.valueOf(3), ra.fetch(2, block));
    assertEquals(null, ra.fetch(4, block));
    assertEquals(ra(4), ints);
    ints.clear();
    assertEquals(null, ra.fetch(-5, index -> ints.push(index)));
    assertEquals(ra(-5), ints);
  }

  @Test
  public void testFill() {
    assertEquals(ra(5, 5, 5, 5), ra.fill(5));
    assertEquals(ra(5, 5, 5, 5), ra);
  }

  @Test
  public void testFillWithStart() {
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
    assertEquals(ra(0, 1, 2, 3), ra.fill(index -> index));
    assertEquals(ra(0, 1, 2, 3), ra);
  }

  @Test
  public void testFillWithBlockAndStart() {
    assertEquals(ra(1, 2, 2, 3), ra.fill(2, index -> index));
    assertEquals(ra(1, 2, 2, 3), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 2, 3), ra.fill(-2, index -> index));
    assertEquals(ra(1, 2, 2, 3), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(0, 1, 2, 3), ra.fill(-4, index -> index));
    assertEquals(ra(0, 1, 2, 3), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3, 4), ra.fill(4, block));
    assertEquals(ra(1, 2, 3, 4), ra);
  }

  @Test
  public void testFillWithBlockAndStartAndLength() {
    assertEquals(ra(1, 2, 2, 4), ra.fill(2, 1, index -> index));
    assertEquals(ra(1, 2, 2, 4), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 2, 4), ra.fill(-2, 1, index -> index));
    assertEquals(ra(1, 2, 2, 4), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(0, 2, 3, 4), ra.fill(-7, 1, index -> index));
    assertEquals(ra(0, 2, 3, 4), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3, 4, null, null, 6, 7), ra.fill(6, 2, index -> index));
    assertEquals(ra(1, 2, 3, 4, null, null, 6, 7), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(0, 1, 2, 3, 4, 5, 6), ra.fill(0, 7, index -> index));
    assertEquals(ra(0, 1, 2, 3, 4, 5, 6), ra);
  }

  @Test
  public void testFlatten() {
    RubyArray<RubyArray<Integer>> layer2 = ra(ra(1), ra(2, 3), ra(4, 5, 6));
    assertEquals(ra(1, 2, 3, 4, 5, 6), layer2.flatten());
    RubyArray<RubyArray<RubyArray<Integer>>> layer3 = ra(ra(ra(1), ra(2, 3)), ra(ra(4, 5, 6)));
    assertEquals(ra(1, 2, 3, 4, 5, 6), layer3.flatten());
    RubyArray<RubyArray<Integer>> ints = ra();
    RubyArray<RubyArray<RubyArray<Integer>>> layer3WithNull =
        ra(null, ints, ra(ra(null, 1), ra(2, 3)), ra(ra(4, 5, 6)));
    assertEquals(ra(null, null, 1, 2, 3, 4, 5, 6), layer3WithNull.flatten());
    RubyArray<?> orderTest = ra(1, null, ra(3, 4), 5, ra(ra(6)));
    assertEquals(ra(1, null, 3, 4, 5, 6), orderTest.flatten());
  }

  @Test
  public void testFlattenWitnN() {
    assertEquals(ra(0, ra(1, 2, 3)), ra(ra(0, ra(1, 2, 3))).flatten(1));
    assertEquals(ra(ra(0, ra(1, 2, 3))), ra(ra(0, ra(1, 2, 3))).flatten(-1));
  }

  @Test
  public void testFreeze() {
    ra.freeze();
    assertSame(ra, ra.freeze());
    assertTrue(ra.frozenʔ());
    frozenRa.freeze();
    assertSame(frozenRa, frozenRa.freeze());
    assertTrue(frozenRa.frozenʔ());
  }

  @Test
  public void testFrozenʔ() {
    assertFalse(ra.frozenʔ());
    assertTrue(frozenRa.frozenʔ());
  }

  @Test
  public void testFrozenException() {
    assertThrows(UnsupportedOperationException.class, () -> {
      frozenRa.shift();
    });
  }

  @Test
  public void testHash() {
    assertEquals(ra.hashCode(), ra.hash());
  }

  @Test
  public void testIndexWithBlock() {
    assertEquals(Integer.valueOf(1), ra.index(item -> item >= 2));
    assertNull(ra.index(item -> item > 4));
  }

  @Test
  public void testIndex() {
    assertEquals(Integer.valueOf(2), ra.index(3));
    assertNull(ra.index(5));
  }

  @Test
  public void testInsert() {
    assertEquals(ra(1, 2, 3, 5, 6, 4), ra.insert(-2, 5, 6));
    assertEquals(ra(1, 2, 3, 5, 6, 4), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 5, 6, 3, 4), ra.insert(2, 5, 6));
    assertEquals(ra(1, 2, 5, 6, 3, 4), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(1, 2, 3, 4, null, null, null, 5, 6), ra.insert(7, 5, 6));
    assertEquals(ra(1, 2, 3, 4, null, null, null, 5, 6), ra);
    ra = ra(1, 2, 3, 4);
    assertEquals(ra(-1, 0, 1, 2, 3, 4), ra.insert(-5, -1, 0));
  }

  @Test
  public void testInsertException() {
    assertThrows(IndexOutOfBoundsException.class, () -> {
      ra.insert(-6, 0);
    });
  }

  @Test
  public void testInspect() {
    assertEquals("[1, 2, 3, 4]", ra.inspect());
    assertEquals(ra.toString(), ra.inspect());
  }

  @Test
  public void testIntersection() {
    ra = ra(1, 2, 2, 3);
    assertEquals(ra(2, 3), ra.intersection(ra(-1, 2, 3, 6)));
  }

  @Test
  public void testJoin() {
    assertEquals("1234", ra.join());
    ra.add(null);
    assertEquals("1234", ra.join());
  }

  @Test
  public void testJoinWithSeparator() {
    assertEquals("1,2,3,4", ra.join(","));
    ra.add(null);
    assertEquals("1\t2\t3\t4\t", ra.join("\t"));
    ra.clear();
    assertEquals("", ra.join(":"));
  }

  @Test
  public void testKeepIf() {
    assertEquals(RubyEnumerator.class, ra.keepIf().getClass());
    assertEquals(ra(1, 2, 3, 4), ra.keepIf().toA());
  }

  @Test
  public void testKeepIfWithBlock() {
    assertEquals(ra(1, 3), ra.keepIf(item -> item % 2 == 1));
    assertEquals(ra(1, 3), ra);
  }

  @Test
  public void testLast() {
    assertEquals(Integer.valueOf(4), ra.last());
    ra = ra();
    assertNull(ra.last());
  }

  @Test
  public void testLastWithN() {
    assertEquals(ra(2, 3, 4), ra.last(3));
    assertEquals(ra(1, 2, 3, 4), ra.last(6));
    ra = ra();
    assertEquals(ra(), ra.last(2));
  }

  @Test
  public void testLastWithNException() {
    assertThrows(IllegalArgumentException.class, () -> {
      ra.last(-1);
    });
  }

  @Test
  public void testLength() {
    assertEquals(4, ra.length());
    assertEquals(ra.size(), ra.length());
  }

  @Test
  public void testMapǃ() {
    assertSame(ra, ra.mapǃ(item -> item * 2));
    assertEquals(ra(2, 4, 6, 8), ra);
  }

  @Test
  public void testMinus() {
    assertEquals(ra(1, 3), ra.minus(ra(2, 4)));
  }

  @Test
  public void testMultiply() {
    assertEquals(ra(), ra.multiply(0));
    assertEquals(ra(1, 2, 3, 4, 1, 2, 3, 4), ra.multiply(2));
  }

  @Test
  public void testMultiplyException() {
    assertThrows(IllegalArgumentException.class, () -> {
      ra.multiply(-1);
    });
  }

  @Test
  public void testMultiplyWithString() {
    assertEquals("1,2,3,4", ra.multiply(","));
  }

  @Test
  public void testPack() {
    assertEquals("a  b  c  ", ra("a", "b", "c").pack("A3A3A3"));
    assertEquals("a\0\0b\0\0c\0\0", ra("a", "b", "c").pack("a3a3a3"));
    assertEquals("ABC", ra(65, 66, 67).pack("ccc"));
  }

  @Test
  public void testPermutaion() {
    ra = ra(1, 2, 3);
    assertEquals(RubyEnumerator.class, ra.permutation().getClass());
    assertEquals(ra(ra(1, 2, 3), ra(1, 3, 2), ra(2, 1, 3), ra(2, 3, 1), ra(3, 1, 2), ra(3, 2, 1)),
        ra.permutation().toA());
  }

  @Test
  public void testPermutaionWithN() {
    ra = ra(1, 2, 3);
    assertEquals(RubyEnumerator.class, ra.permutation(0).getClass());
    assertEquals(ra(), ra.permutation(-1).toA());
    assertEquals(ra(ra()), ra.permutation(0).toA());
    assertEquals(ra(ra(1), ra(2), ra(3)), ra.permutation(1).toA());
    assertEquals(ra(ra(1, 2), ra(1, 3), ra(2, 1), ra(2, 3), ra(3, 1), ra(3, 2)),
        ra.permutation(2).toA());
    assertEquals(ra(ra(1, 2, 3), ra(1, 3, 2), ra(2, 1, 3), ra(2, 3, 1), ra(3, 1, 2), ra(3, 2, 1)),
        ra.permutation(3).toA());
    assertEquals(ra(), ra.permutation(4).toA());
  }

  @Test
  public void testPermutaionWithNAndBlock() {
    ra = ra(1, 2, 3);
    final RubyArray<Integer> ints = ra();
    assertEquals(ra, ra.permutation(1, item -> ints.concat(item)));
    assertEquals(ra(1, 2, 3), ints);
  }

  @Test
  public void testPermutaionWithBlock() {
    ra = ra(1, 2, 3);
    final RubyArray<Integer> ints = ra();
    assertEquals(ra, ra.permutation(item -> ints.concat(item)));
    assertEquals(ra(1, 2, 3, 1, 3, 2, 2, 1, 3, 2, 3, 1, 3, 1, 2, 3, 2, 1), ints);
  }

  @Test
  public void testPlus() {
    assertEquals(ra(1, 2, 3, 4, 5, 6), ra.plus(ra(5, 6)));
  }

  @Test
  public void testPop() {
    assertEquals(Integer.valueOf(4), ra.pop());
    ra = ra();
    assertNull(ra.pop());
  }

  @Test
  public void testPopWithN() {
    assertEquals(ra(), ra.pop(0));
    assertEquals(ra(3, 4), ra.pop(2));
    assertEquals(ra(1, 2), ra);
    assertEquals(ra(1, 2), ra.pop(3));
    ra = ra();
    assertEquals(ra(), ra.pop(3));
  }

  @Test
  public void testPopException() {
    assertThrows(IllegalArgumentException.class, () -> {
      ra.pop(-1);
    });
  }

  @Test
  public void testPrepend() {
    assertEquals(ra(0, 1, 2, 3, 4), ra.prepend(0));
    assertEquals(ra(-2, -1, 0, 1, 2, 3, 4), ra.prepend(-2, -1));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testProduct() {
    ra = ra(1, 2);
    assertEquals(ra(), ra().product());
    assertEquals(ra(ra(1), ra(2)), ra.product());
    assertEquals(ra(ra(1, 3), ra(1, 4), ra(2, 3), ra(2, 4)), ra.product(ra(3, 4)));
    assertEquals(ra(ra(1, 3, 5), ra(1, 4, 5), ra(2, 3, 5), ra(2, 4, 5)),
        ra.product(ra(3, 4), ra(5)));
    assertEquals(ra(ra(1, 3, 5), ra(1, 4, 5), ra(2, 3, 5), ra(2, 4, 5)),
        ra.product(ra(ra(3, 4), ra(5))));
  }

  @Test
  public void testProductWithBlock() {
    ra = ra(1, 2);
    final RubyArray<Integer> ints = ra();
    assertEquals(ra, ra.product(ra(ra(3, 4)), item -> ints.concat(item)));
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

  @Test
  public void testRassoc() {
    RubyArray<? extends List<Integer>> ra = ra(ra(1, 2), ra(3, 4), ra(4, 4), ra(6, 7));
    assertEquals(ra(3, 4), ra.rassoc(4));
    assertNull(ra.rassoc(6));
    List<Integer> ints = new ArrayList<Integer>();
    ra = ra(null, ints, ra(1, 2, 3), ra(4, 5, 6));
    assertEquals(ra(1, 2, 3), ra.rassoc(3));
    ra = ra(ra(1, 2, 3), ra(4, 5, null), ra(7, 8, 9));
    assertEquals(ra(4, 5, null), ra.rassoc(null));
    assertEquals(ra(7, 8, 9), ra.rassoc(9));
  }

  @Test
  public void testRejectǃ() {
    assertEquals(RubyEnumerator.class, ra.rejectǃ().getClass());
    assertEquals(ra(1, 2, 3, 4), ra.rejectǃ().toA());
  }

  @Test
  public void testRejectǃWithBlock() {
    Predicate<Integer> block = item -> item > 2;
    assertEquals(ra(1, 2), ra.rejectǃ(block));
    assertEquals(ra(1, 2), ra);
    assertNull(ra.rejectǃ(block));
  }

  @Test
  public void testRepeatedCombination() {
    ra = ra(1, 2);
    assertEquals(RubyEnumerator.class, ra.repeatedCombination(0).getClass());
    assertEquals(ra(), ra.repeatedCombination(-1).toA());
    assertEquals(ra(ra()), ra.repeatedCombination(0).toA());
    assertEquals(ra(ra(1), ra(2)), ra.repeatedCombination(1).toA());
    assertEquals(ra(ra(1, 1), ra(1, 2), ra(2, 2)), ra.repeatedCombination(2).toA());
    assertEquals(ra(ra(1, 1, 1), ra(1, 1, 2), ra(1, 2, 2), ra(2, 2, 2)),
        ra.repeatedCombination(3).toA());
  }

  @Test
  public void testRepeatedCombinationWithBlock() {
    ra = ra(1, 2);
    final RubyArray<Integer> ints = ra();
    assertEquals(ra, ra.repeatedCombination(2, item -> ints.concat(item)));
    assertEquals(ra(1, 1, 1, 2, 2, 2), ints);
  }

  @Test
  public void testRepeatedPermutaion() {
    ra = ra(1, 2);
    assertEquals(RubyEnumerator.class, ra.repeatedPermutation(0).getClass());
    assertEquals(ra(), ra.repeatedPermutation(-1).toA());
    assertEquals(ra(ra()), ra.repeatedPermutation(0).toA());
    assertEquals(ra(ra(1), ra(2)), ra.repeatedPermutation(1).toA());
    assertEquals(ra(ra(1, 1), ra(1, 2), ra(2, 1), ra(2, 2)), ra.repeatedPermutation(2).toA());
    assertEquals(ra(ra(1, 1, 1), ra(1, 1, 2), ra(1, 2, 1), ra(1, 2, 2), ra(2, 1, 1), ra(2, 1, 2),
        ra(2, 2, 1), ra(2, 2, 2)), ra.repeatedPermutation(3).toA());
  }

  @Test
  public void testRepeatedPermutaionWithBlock() {
    ra = ra(1, 2);
    final RubyArray<Integer> ints = ra();
    assertEquals(ra, ra.repeatedPermutation(2, item -> ints.concat(item)));
    assertEquals(ra(1, 1, 1, 2, 2, 1, 2, 2), ints);
  }

  @Test
  public void testReplace() {
    assertEquals(ra(4, 5, 6, 7), ra.replace(ra(4, 5, 6, 7)));
    assertEquals(ra(4, 5, 6, 7), ra);
  }

  @Test
  public void testReverse() {
    assertEquals(ra(4, 3, 2, 1), ra.reverse());
    assertEquals(ra(1, 2, 3, 4), ra);
  }

  @Test
  public void testReverseǃ() {
    assertEquals(ra(4, 3, 2, 1), ra.reverseǃ());
    assertEquals(ra(4, 3, 2, 1), ra);
  }

  @Test
  public void testRindex() {
    assertEquals(RubyEnumerator.class, ra.rindex().getClass());
    assertEquals(ra(4, 3, 2, 1), ra.rindex().toA());
  }

  @Test
  public void testRindexWithBlock() {
    Predicate<Integer> block = item -> item > 1;
    assertEquals(Integer.valueOf(3), ra.rindex(block));
    ra = ra(0, -1, -2, -3);
    assertNull(ra.rindex(block));
  }

  @Test
  public void testRindexWithTarget() {
    ra = ra(1, 2, 4, 4);
    assertEquals(Integer.valueOf(3), ra.rindex(4));
    assertNull(ra.rindex(5));
  }

  @Test
  public void testRotate() {
    assertEquals(ra(2, 3, 4, 1), ra.rotate());
    assertEquals(ra(1, 2, 3, 4), ra);
    assertEquals(ra(), ra().rotate());
  }

  @Test
  public void testRotateWithN() {
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
    assertTrue(ra.includeʔ(ra.sample()));
    assertNull(ra().sample());
    final Set<Integer> set = newRubySet();
    range(0, 1000).each(item -> set.add(ra.sample()));
    assertEquals(ra.size(), set.size());
  }

  @Test
  public void testSampleWithN() {
    RubyArray<Integer> samples = ra.sample(3);
    assertEquals(3, samples.uniq().count());
    assertEquals(4, ra.sample(5).uniq().count());
    assertEquals(ra(), ra.sample(0));
    final Set<Integer> set = newRubySet();
    range(0, 1000).each(item -> set.addAll(ra.sample(2)));
    assertEquals(ra.size(), set.size());
  }

  @Test
  public void testSampleException() {
    assertThrows(IllegalArgumentException.class, () -> {
      ra.sample(-1);
    });
  }

  @Test
  public void testSelectǃ() {
    assertEquals(RubyEnumerator.class, ra.selectǃ().getClass());
    assertEquals(ra(1, 2, 3, 4), ra.selectǃ().toA());
  }

  @Test
  public void testSelectǃWithBlock() {
    Predicate<Integer> block = item -> item % 2 == 1;
    assertEquals(ra(1, 3), ra.selectǃ(block));
    assertEquals(ra(1, 3), ra);
    assertNull(ra.selectǃ(block));
  }

  @Test
  public void testShift() {
    assertEquals(Integer.valueOf(1), ra.shift());
    assertEquals(ra(2, 3, 4), ra);
    ra = ra();
    assertNull(ra.shift());
  }

  @Test
  public void testShiftWithN() {
    assertEquals(ra(1, 2), ra.shift(2));
    assertEquals(ra(3, 4), ra);
    assertEquals(ra(3, 4), ra.shift(5));
    assertEquals(ra(), ra);
  }

  @Test
  public void testShiftException() {
    assertThrows(IllegalArgumentException.class, () -> {
      ra.shift(-1);
    });
  }

  @Test
  public void testShuffle() {
    ra = ra(0).fill(0, 1000, index -> index);
    assertFalse(ra.equals(ra.shuffle()));
    assertEquals(ra(0).fill(0, 1000, index -> index), ra);
  }

  @Test
  public void testShuffleǃ() {
    ra = ra(0).fill(0, 1000, index -> index);
    ra.shuffleǃ();
    assertFalse(ra(0).fill(0, 1000, index -> index).equals(ra));
  }

  @Test
  public void testSlice() {
    assertEquals(Integer.valueOf(1), ra.slice(0));
    assertEquals(Integer.valueOf(4), ra.slice(-1));
    assertNull(ra.slice(4));
    assertNull(ra.slice(-5));
  }

  @Test
  public void testSliceWithLength() {
    assertEquals(ra(1, 2, 3, 4), ra.slice(0, 5));
    assertEquals(ra(3), ra.slice(-2, 1));
    assertNull(ra.slice(4, 2));
    assertNull(ra.slice(-5, 3));
  }

  @Test
  public void testSliceǃ() {
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
    assertEquals(ra(null, null, null), ra(null, null, null).sortǃ());
  }

  @Test
  public void testSortǃWithComparator() {
    ra = ra(4, 1, 2, 3, 3);
    assertEquals(ra(4, 3, 3, 2, 1), ra.sortǃ(comp));
    assertEquals(ra(4, 3, 3, 2, 1), ra);
    assertEquals(ra(1), ra(1).sortǃ(comp));
    assertEquals(ra(null, null, null), ra(null, null, null).sortǃ(null));
  }

  @Test
  public void testSortByǃ() {
    assertEquals(RubyEnumerator.class, ra.sortByǃ().getClass());
    assertEquals(ra(1, 2, 3, 4), ra.sortByǃ().toA());
  }

  @Test
  public void testSortByǃWithComparatorAndBlock() {
    RubyArray<String> ra = ra("aaaa", "bbb", "ff", "cc", "d");
    assertEquals(ra("aaaa", "bbb", "ff", "cc", "d"), ra.sortByǃ(comp, item -> item.length()));
    assertEquals(ra("aaaa", "bbb", "ff", "cc", "d"), ra);
  }

  @Test
  public void testSortByǃWith2ComparatorAndBlock() {
    RubyArray<String> ra = ra("aaaa", "bbb", "cc", "ff", "d");
    assertEquals(ra("aaaa", "bbb", "ff", "cc", "d"),
        ra.sortByǃ((o1, o2) -> o2.compareTo(o1), comp, item -> item.length()));
    assertEquals(ra("aaaa", "bbb", "ff", "cc", "d"), ra);
  }

  @Test
  public void testSortByǃWithBlock() {
    RubyArray<String> ra = ra("aaaa", "bbb", "ff", "cc", "d");
    assertEquals(ra("d", "ff", "cc", "bbb", "aaaa"), ra.sortByǃ(item -> item.length()));
    assertEquals(ra("d", "ff", "cc", "bbb", "aaaa"), ra);
  }

  @Test
  public void testSubtract() {
    ra = ra(1, 2, 3, 4, 4);
    assertEquals(ra(1, 2), ra.minus(ra(3, 4, 5)));
  }

  @Test
  public void testSum() {
    assertEquals(new BigDecimal(10), ra.sum());
  }

  @Test
  public void testSumException() {
    assertThrows(ClassCastException.class, () -> {
      ra("a", "b", "c").sum();
    });
  }

  @Test
  public void testToS() {
    assertEquals("[1, 2, 3, 4]", ra.toS());
    assertEquals(ra.toString(), ra.toS());
  }

  @Test
  public void testTranspose() {
    assertEquals(ra(), ra().transpose());
    RubyArray<RubyArray<Integer>> ra = ra(ra(1, 2, 3), ra(4, 5, 6));
    assertEquals(ra(ra(1, 4), ra(2, 5), ra(3, 6)), ra.transpose());
  }

  @Test
  public void testTransposeException1() {
    assertThrows(ClassCastException.class, () -> {
      ra.transpose();
    });
  }

  @Test
  public void testTransposeException2() {
    assertThrows(IndexOutOfBoundsException.class, () -> {
      ra(ra(1, 2, 3), ra(4, 5)).transpose();
    });
  }

  @Test
  public void testUnion() {
    ra = ra(1, 2, 3, 4, 4);
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
    assertEquals(ra("aa", "ccc", "f"), ra.uniq(item -> item.length()));
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
    assertEquals(ra("aa", "ccc", "f"), ra.uniqǃ(item -> item.length()));
    assertEquals(ra("aa", "ccc", "f"), ra);
    ra = ra("a", "bb", "ccc", "dddd");
    assertNull(ra.uniqǃ(item -> item.length()));
  }

  @Test
  public void testUnshift() {
    assertEquals(ra(0, 1, 2, 3, 4), ra.unshift(0));
    assertEquals(ra(-2, -1, 0, 1, 2, 3, 4), ra.unshift(-2, -1));
  }

  @Test
  public void testValuesAt() {
    assertEquals(ra(4, 1, null, null), ra.valuesAt(-1, 0, 5, -6));
    assertEquals(ra(4, 1, null, null), ra.valuesAt(Arrays.asList(-1, 0, 5, -6)));
  }

  @Test
  public void testSize() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    assertEquals(ints.size(), ra.size());
    ints.clear();
    ra.clear();
    assertEquals(ints.size(), ra.size());
  }

  @Test
  public void testIsEmpty() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    assertEquals(ints.isEmpty(), ra.isEmpty());
    ints.clear();
    ra.clear();
    assertEquals(ints.size(), ra.size());
  }

  @Test
  public void testContains() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    assertEquals(ints.contains(1), ra.contains(1));
  }

  @Test
  public void testIterator() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    assertEquals(ra(ints.iterator()), ra(ra.iterator()));
  }

  @Test
  public void testToArray() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    assertArrayEquals(ints.toArray(), ra.toArray());
  }

  @Test
  public void testToArrayWithArgument() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    assertArrayEquals(ints.toArray(new Integer[4]), ra.toArray(new Integer[4]));
  }

  @Test
  public void testAdd() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    ints.add(5);
    ra.add(5);
    assertEquals(ints, ra);
  }

  @Test
  public void testRemove() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    ints.remove(Integer.valueOf(4));
    ra.remove(Integer.valueOf(4));
    assertEquals(ints, ra);
  }

  @Test
  public void testContainsAll() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    List<Integer> one = Collections.singletonList(1);
    assertEquals(ints.containsAll(one), ra.containsAll(one));
  }

  @Test
  public void testAddAll() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    List<Integer> one = Collections.singletonList(1);
    ints.addAll(one);
    ra.addAll(one);
    assertEquals(ints, ra);
  }

  @Test
  public void testAddAllWithIndex() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    List<Integer> one = Collections.singletonList(1);
    ints.addAll(0, one);
    ra.addAll(0, one);
    assertEquals(ints, ra);
  }

  @Test
  public void testRemoveAll() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    List<Integer> one = Collections.singletonList(1);
    ints.removeAll(one);
    ra.removeAll(one);
    assertEquals(ints, ra);
  }

  @Test
  public void testRetainAll() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    List<Integer> one = Collections.singletonList(1);
    ints.retainAll(one);
    ra.retainAll(one);
    assertEquals(ints, ra);
  }

  @Test
  public void testClear() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    ints.clear();
    ra.clear();
    assertEquals(ints, ra);
  }

  @Test
  public void testGet() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    assertEquals(ints.get(1), ra.get(1));
  }

  @Test
  public void testSet() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    ints.set(0, 0);
    ra.set(0, 0);
    assertEquals(ints, ra);
  }

  @Test
  public void testAddWithIndex() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    ints.add(0, 0);
    ra.add(0, 0);
    assertEquals(ints, ra);
  }

  @Test
  public void testRemoveWithIndex() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    ints.remove(0);
    ra.remove(0);
    assertEquals(ints, ra);
  }

  @Test
  public void testIndexOf() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    assertEquals(ints.indexOf(1), ra.indexOf(1));
  }

  @Test
  public void testLastIndexOf() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    assertEquals(ints.lastIndexOf(1), ra.lastIndexOf(1));
  }

  @Test
  public void testListIterator() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    assertEquals(ra(ints.listIterator()), ra(ra.listIterator()));
  }

  @Test
  public void testListIteratorWithIndex() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    assertEquals(ra(ints.listIterator(1)), ra(ra.listIterator(1)));
  }

  @Test
  public void testSubList() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    assertEquals(ints.subList(0, 2), ra.subList(0, 2));
  }

  @Test
  public void testEquals() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    assertEquals(ints.equals(Arrays.asList(1, 2, 3, 4)), ra.equals(Arrays.asList(1, 2, 3, 4)));
  }

  @Test
  public void testHashCode() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    assertEquals(ints.hashCode(), ra.hashCode());
  }

  @Test
  public void testToString() {
    List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    ra = RubyArray.copyOf(ints);
    assertEquals(ints.toString(), ra.toString());
  }

  @Test
  public void testCompareTo() {
    assertEquals(ra(ra(), ra(), ra(1), ra(1), ra(1, 1), ra(1, 1), ra(2, 3), ra(4, 5)),
        ra(ra(4, 5), ra(1), ra(), ra(1, 1), ra(), ra(1), ra(1, 1), ra(2, 3)).sort());
  }

  @Test
  public void testCompareToException() {
    assertThrows(IllegalArgumentException.class, () -> {
      ra(4, 5).compareTo(null);
    });
  }

}
