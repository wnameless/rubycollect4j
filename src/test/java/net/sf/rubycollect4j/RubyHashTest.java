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

import static net.sf.rubycollect4j.RubyCollections.hp;
import static net.sf.rubycollect4j.RubyCollections.newRubyHash;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.rh;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.sf.rubycollect4j.util.ComparableEntry;

public class RubyHashTest {

  RubyHash<Integer, Integer> rh;
  RubyHash<Integer, Integer> frozenRh;

  @BeforeEach
  public void setUp() {
    rh = new RubyHash<Integer, Integer>();
    rh.put(1, 2);
    rh.put(3, 4);
    rh.put(5, 6);
    frozenRh = rh(1, 2, 3, 4, 5, 6).freeze();
  }

  @Test
  public void testInterfaces() {
    assertTrue(rh instanceof RubyEnumerable);
    assertTrue(rh instanceof Map);
    assertTrue(rh instanceof Serializable);
  }

  @Test
  public void testStaticFactoryMethodOf() {
    LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
    map.put(1, "a");
    map.put(2, "b");
    map.put(3, "c");
    RubyHash<Integer, String> intStr = RubyHash.of(map);
    map.remove(1);
    assertEquals(map, intStr);
  }

  @Test
  public void testStaticFactoryMethodOfException() {
    assertThrows(NullPointerException.class, () -> {
      RubyHash.of((LinkedHashMap<?, ?>) null);
    });
  }

  @Test
  public void testStaticFactoryMethodCopyOf() {
    LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
    map.put(1, "a");
    map.put(2, "b");
    map.put(3, "c");
    RubyHash<Integer, String> intStr = RubyHash.copyOf(map);
    map.remove(1);
    assertNotEquals(map, intStr);
  }

  @Test
  public void testStaticFactoryMethodCopyOfException() {
    assertThrows(NullPointerException.class, () -> {
      RubyHash.copyOf((Map<?, ?>) null);
    });
  }

  @Test
  public void testConstructor() {
    assertTrue(rh instanceof RubyHash);
    rh = new RubyHash<Integer, Integer>(new LinkedHashMap<Integer, Integer>());
    assertTrue(rh instanceof RubyHash);
    rh = new RubyHash<Integer, Integer>(new HashMap<Integer, Integer>());
    assertTrue(rh instanceof RubyHash);
  }

  @Test
  public void testConstructorException1() {
    assertThrows(NullPointerException.class, () -> {
      new RubyHash<Integer, Integer>((LinkedHashMap<Integer, Integer>) null);
    });
  }

  @Test
  public void testConstructorException2() {
    assertThrows(NullPointerException.class, () -> {
      new RubyHash<Integer, Integer>((Map<Integer, Integer>) null);
    });
  }

  @Test
  public void testAssoc() {
    assertEquals(hp(3, 4), rh.assoc(3));
    assertNull(rh.assoc(7));
    rh = rh(1, 2, null, 4);
    assertEquals(hp(null, 4), rh.assoc(null));
  }

  @Test
  public void testCompact() {
    rh.put(7, null);
    assertEquals(Ruby.Hash.of(1, 2, 3, 4, 5, 6), rh.compact());
    assertEquals(Ruby.Hash.of(1, 2, 3, 4, 5, 6, 7, null), rh);
  }

  @Test
  public void testCompactǃ() {
    rh.put(7, null);
    assertEquals(Ruby.Hash.of(1, 2, 3, 4, 5, 6), rh.compactǃ());
    assertSame(rh, rh.compactǃ());
  }

  @Test
  public void testCompareByIdentity() {
    RubyHash<String, Integer> rh = newRubyHash();
    rh.compareByIdentity();
    rh.put("a", 1);
    rh.put(new String("a"), 1);
    assertEquals(2, rh.count());
  }

  @Test
  public void testCompareByIdentityʔ() {
    rh = newRubyHash();
    rh.compareByIdentity();
    assertTrue(rh.comparedByIdentityʔ());
  }

  @Test
  public void testDelete() {
    assertEquals(Integer.valueOf(4), rh.delete(3));
    assertNull(rh.delete(0));
    rh.setDefault(10);
    assertEquals(rh.getDefault(), rh.delete(3));
  }

  @Test
  public void testDeleteIf() {
    assertTrue(rh.deleteIf() instanceof RubyEnumerator);
    assertEquals(ra(hp(1, 2), hp(3, 4), hp(5, 6)), rh.deleteIf().toA());
  }

  @Test
  public void testDeleteIfWithBlock() {
    assertEquals(rh(3, 4, 5, 6), rh.deleteIf((key, value) -> key + value < 7));
    assertEquals(rh(3, 4, 5, 6), rh);
  }

  @Test
  public void testEach() {
    assertTrue(rh.each() instanceof RubyEnumerator);
    assertEquals(ra(hp(1, 2), hp(3, 4), hp(5, 6)), rh.each().toA());
  }

  @Test
  public void testEachWithBlock() {
    final RubyArray<Integer> ints = ra();
    assertEquals(rh, rh.each((BiConsumer<Integer, Integer>) (key, value) -> {
      ints.push(key);
      ints.push(value);
    }));
    assertEquals(ra(1, 2, 3, 4, 5, 6), ints);
  }

  @Test
  public void testEachKey() {
    assertTrue(rh.each() instanceof RubyEnumerator);
    assertEquals(ra(1, 3, 5), rh.eachKey().toA());
  }

  @Test
  public void testEachKeyWithBlock() {
    final RubyArray<Integer> ints = ra();
    assertEquals(rh, rh.eachKey(key -> ints.push(key)));
    assertEquals(ra(1, 3, 5), ints);
  }

  @Test
  public void testEachPair() {
    assertTrue(rh.eachPair() instanceof RubyEnumerator);
    assertEquals(ra(hp(1, 2), hp(3, 4), hp(5, 6)), rh.eachPair().toA());
  }

  @Test
  public void testEachPairWithBlock() {
    final RubyArray<Integer> ints = ra();
    assertEquals(rh, rh.eachPair((key, value) -> {
      ints.push(key);
      ints.push(value);
    }));
    assertEquals(ra(1, 2, 3, 4, 5, 6), ints);
  }

  @Test
  public void testEachValue() {
    assertTrue(rh.eachValue() instanceof RubyEnumerator);
    assertEquals(ra(2, 4, 6), rh.eachValue().toA());
  }

  @Test
  public void testEachValueWithBlock() {
    final RubyArray<Integer> ints = ra();
    assertEquals(rh, rh.eachValue(key -> ints.push(key)));
    assertEquals(ra(2, 4, 6), ints);
  }

  @Test
  public void testEmptyʔ() {
    assertFalse(rh.emptyʔ());
    rh = rh();
    assertTrue(rh.emptyʔ());
    rh = rh(null, null);
    assertFalse(rh.emptyʔ());
  }

  @Test
  public void testEqlʔ() {
    assertTrue(rh.eqlʔ(rh(1, 2, 3, 4, 5, 6)));
    rh = rh(3, 4, 1, 2, 5, 6);
    assertTrue(rh.eqlʔ(rh(1, 2, 3, 4, 5, 6)));
    rh = rh(1, 2, 3, 4);
    assertFalse(rh.eqlʔ(rh(1, 2, 3, 4, 5, 6)));
    assertFalse(rh.eqlʔ(new Object()));
  }

  @Test
  public void testFetch() {
    assertEquals(Integer.valueOf(6), rh.fetch(5));
  }

  @Test
  public void testFetchException() {
    assertThrows(NoSuchElementException.class, () -> {
      rh.fetch(7);
    });
  }

  @Test
  public void testFetchWithDefaultValue() {
    assertEquals(Integer.valueOf(6), rh.fetch(5, 10));
    assertEquals(Integer.valueOf(10), rh.fetch(7, 10));
  }

  @Test
  public void testFetchValues() {
    assertEquals(Ruby.Array.of(2, 4, 6), rh.fetchValues(1, 3, 5));
    assertEquals(Ruby.Array.of(2, 4, 6), rh.fetchValues(Arrays.asList(1, 3, 5)));
  }

  @Test
  public void testFetchValuesException() {
    assertThrows(NoSuchElementException.class, () -> {
      rh.fetchValues(5, 7, 9);
    });
  }

  @Test
  public void testFlatten() {
    assertEquals(ra(hp(1, 2), hp(3, 4), hp(5, 6)), rh.flatten());
  }

  @Test
  public void testFreeze() {
    rh.freeze();
    assertSame(rh, rh.freeze());
    assertTrue(rh.frozenʔ());
    frozenRh.freeze();
    assertSame(frozenRh, frozenRh.freeze());
    assertTrue(frozenRh.frozenʔ());
  }

  @Test
  public void testFrozenʔ() {
    assertFalse(rh.frozenʔ());
    assertTrue(frozenRh.frozenʔ());
  }

  @Test
  public void testFrozenException() {
    assertThrows(UnsupportedOperationException.class, () -> {
      frozenRh.shift();
    });
  }

  @Test
  public void testHash() {
    assertEquals(rh.hashCode(), rh.hash());
  }

  @Test
  public void testInspect() {
    assertEquals("{1=2, 3=4, 5=6}", rh.inspect());
    assertEquals(rh.toString(), rh.inspect());
  }

  @Test
  public void testInvert() {
    assertEquals(rh(2, 1, 4, 3, 6, 5), rh.invert());
    rh = rh(1, 2, 3, 5, 4, 5);
    assertEquals(rh(2, 1, 5, 4), rh.invert());
  }

  @Test
  public void testKeepIf() {
    assertTrue(rh.keepIf() instanceof RubyEnumerator);
    assertEquals(ra(hp(1, 2), hp(3, 4), hp(5, 6)), rh.keepIf().toA());
  }

  @Test
  public void testKeepIfWithBlock() {
    assertEquals(rh(1, 2), rh.keepIf((key, value) -> key + value < 7));
    assertEquals(rh(1, 2), rh);
  }

  @Test
  public void testKey() {
    assertEquals(Integer.valueOf(1), rh.key(2));
    assertNull(rh.key(8));
    rh = rh(1, 2, 3, null);
    assertEquals(Integer.valueOf(3), rh.key(null));
  }

  @Test
  public void testKeys() {
    assertEquals(ra(1, 3, 5), rh.keys());
  }

  @Test
  public void testKeyʔ() {
    assertTrue(rh.keyʔ(1));
    assertFalse(rh.keyʔ(2));
  }

  @Test
  public void testLength() {
    assertEquals(3, rh.length());
    assertEquals(rh.size(), rh.length());
  }

  @Test
  public void testMerge() {
    assertEquals(rh(1, 2, 3, 8, 5, 9, 7, 7), rh.merge(rh(3, 8, 5, 9, 7, 7)));
  }

  @Test
  public void testMergeWithBlock() {
    assertEquals(rh(1, 3, 3, 4, 5, 6, 0, 1),
        rh.merge(rh(0, 1, 1, 3, 3, 2), (key, oldval, newval) -> Math.max(oldval, newval)));
  }

  @Test
  public void testMergeǃ() {
    assertEquals(rh(1, 2, 3, 8, 5, 9, 7, 7), rh.mergeǃ(rh(3, 8, 5, 9, 7, 7)));
    assertEquals(rh(1, 2, 3, 8, 5, 9, 7, 7), rh);
  }

  @Test
  public void testMergeǃWithBlock() {
    assertEquals(rh(1, 3, 3, 4, 5, 6, 0, 1),
        rh.mergeǃ(rh(0, 1, 1, 3, 3, 2), (key, oldval, newval) -> Math.max(oldval, newval)));
    assertEquals(rh(1, 3, 3, 4, 5, 6, 0, 1), rh);
  }

  @Test
  public void testPut() {
    rh = newRubyHash();
    assertEquals(rh(1, 2), rh.put(hp(1, 2)));
    assertEquals(rh(1, 2, 3, 4, 5, 6), rh.put(hp(3, 4), hp(5, 6)));
  }

  @Test
  public void testRassoc() {
    rh = rh(1, 2, 3, 6, 5, 6);
    assertEquals(hp(3, 6), rh.rassoc(6));
    assertNull(rh.rassoc(5));
    rh = rh(1, 2, 3, null);
    assertEquals(hp(3, null), rh.rassoc(null));
  }

  @Test
  public void testRejectǃ() {
    assertTrue(rh.rejectǃ() instanceof RubyEnumerator);
    assertEquals(ra(hp(1, 2), hp(3, 4), hp(5, 6)), rh.rejectǃ().toA());
  }

  @Test
  public void testRejectǃWithBlock() {
    BiPredicate<Integer, Integer> block = (key, value) -> key + value == 7;
    assertEquals(rh(1, 2, 5, 6), rh.rejectǃ(block));
    assertEquals(rh(1, 2, 5, 6), rh);
    assertNull(rh.rejectǃ(block));
  }

  @Test
  public void testReplace() {
    rh = rh(1, 2, 3, 4);
    assertEquals(rh(5, 6, 7, 8), rh.replace(rh(5, 6, 7, 8)));
    assertEquals(rh(5, 6, 7, 8), rh);
  }

  @Test
  public void testShift() {
    rh = rh(1, 2);
    assertEquals(hp(1, 2), rh.shift());
    assertEquals(rh(), rh);
    assertNull(rh.shift());
  }

  @Test
  public void testStore() {
    rh = rh(1, 2);
    assertEquals(Integer.valueOf(4), rh.store(3, 4));
    assertEquals(Integer.valueOf(6), rh.store(5, 6));
    assertEquals(rh(1, 2, 3, 4, 5, 6), rh);
  }

  @Test
  public void testToH() {
    assertEquals(rh, rh.toH(e -> e));
    assertEquals(rh(hp(1, 2), hp(3, 4), hp(5, 6), null), rh.toH((e1, e2) -> hp(e1, e2)));
  }

  @Test
  public void testToHash() {
    assertSame(rh, rh.toHash());
  }

  @Test
  public void testToMap() {
    Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
    map.put(1, 2);
    map.put(3, 4);
    map.put(5, 6);
    assertEquals(map, rh.toMap());
    assertTrue(rh.toMap() instanceof LinkedHashMap);
  }

  @Test
  public void testToS() {
    assertEquals("{1=2, 3=4, 5=6}", rh.toS());
    assertEquals(rh.toString(), rh.toS());
  }

  @Test
  public void testTransformValues() {
    assertEquals(ra(2, 4, 6), rh.transformValues().toA());
    assertEquals(rh(1, 4, 3, 16, 5, 36), rh.transformValues(v -> v * v));
    assertNotSame(rh(1, 4, 3, 16, 5, 36), rh.transformValues(v -> v * v));
  }

  @Test
  public void testTransformValuesǃ() {
    assertEquals(ra(2, 4, 6), rh.transformValuesǃ().toA());
    assertEquals(rh(1, 4, 3, 16, 5, 36), rh.transformValuesǃ(v -> v * v));
    assertSame(rh, rh.transformValuesǃ(v -> v * v));
  }

  @Test
  public void testUpdate() {
    assertEquals(rh(1, 2, 3, 8, 5, 9, 7, 7), rh.update(rh(3, 8, 5, 9, 7, 7)));
    assertEquals(rh(1, 2, 3, 8, 5, 9, 7, 7), rh);
  }

  @Test
  public void testUpdateWithBlock() {
    assertEquals(rh(1, 3, 3, 4, 5, 6, 0, 1),
        rh.update(rh(0, 1, 1, 3, 3, 2), (key, oldval, newval) -> Math.max(oldval, newval)));
    assertEquals(rh(1, 3, 3, 4, 5, 6, 0, 1), rh);
  }

  @Test
  public void testValues() {
    assertTrue(rh.values() instanceof RubyArray);
    assertEquals(ra(2, 4, 6), rh.values());
  }

  @Test
  public void testValuesAt() {
    assertEquals(ra(2, 6, 4, 2, null), rh.valuesAt(1, 5, 3, 1, 7));
    assertEquals(ra(2, 6, 4, 2, null), rh.valuesAt(Arrays.asList(1, 5, 3, 1, 7)));
  }

  @Test
  public void testValueʔ() {
    assertTrue(rh.valueʔ(4));
    assertFalse(rh.valueʔ(8));
  }

  // Tests for entry blocks of RubyEnumerable methods
  @Test
  public void testAllʔ() {
    BiPredicate<Integer, Integer> block = (key, value) -> key > 0;
    assertTrue(rh.allʔ(block));
    rh = rh(0, 1, 2, 3);
    assertFalse(rh.allʔ(block));
  }

  @Test
  public void testAnyʔ() {
    BiPredicate<Integer, Integer> block = (key, value) -> key > 0;
    assertTrue(rh.anyʔ(block));
    rh = rh(0, -1);
    assertFalse(rh.anyʔ(block));
  }

  @Test
  public void testChunk() {
    assertEquals(ra(hp(3L, ra(hp(1, 2))), hp(7L, ra(hp(3, 4))), hp(11L, ra(hp(5, 6)))),
        rh.chunk((BiFunction<Integer, Integer, Long>) (key, value) -> Long.valueOf(key + value))
            .toA());
  }

  @Test
  public void testCollect() {
    assertEquals(ra(3L, 7L, 11L),
        rh.collect((BiFunction<Integer, Integer, Long>) (key, value) -> Long.valueOf(key + value)));
  }

  @Test
  public void testCollectConcat() {
    assertEquals(ra(3L, 7L, 11L),
        rh.collectConcat((BiFunction<Integer, Integer, RubyArray<Long>>) (key,
            value) -> ra(Long.valueOf(key + value))).toA());
  }

  @Test
  public void testCount() {
    assertEquals(1, rh.count((BiPredicate<Integer, Integer>) (key, value) -> key == 1));
  }

  @Test
  public void testCycle() {
    final RubyArray<Integer> ints = ra();
    assertThrows(IllegalStateException.class, () -> {
      rh.cycle((BiConsumer<Integer, Integer>) (key, value) -> {
        ints.add(key);
        ints.add(value);
        if (ints.size() > 1000) {
          throw new IllegalStateException();
        }
      });
    });
  }

  @Test
  public void testCycleWithN() {
    final RubyArray<Integer> ints = ra();
    rh.cycle(2, (BiConsumer<Integer, Integer>) (key, value) -> {
      ints.add(key);
      ints.add(value);
    });
    assertEquals(ra(1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6), ints);
  }

  @Test
  public void testDetect() {
    assertEquals(hp(3, 4), rh.detect((BiPredicate<Integer, Integer>) (key, value) -> value == 4));
  }

  @Test
  public void testDropWhile() {
    assertEquals(ra(hp(3, 4), hp(5, 6)),
        rh.dropWhile((BiPredicate<Integer, Integer>) (key, value) -> key + value <= 4));
  }

  @Test
  public void testEachEntry() {
    final RubyArray<Integer> ints = ra();
    rh.eachEntry((BiConsumer<Integer, Integer>) (key, value) -> ints.push(value));
    assertEquals(ra(2, 4, 6), ints);
  }

  @Test
  public void testFind() {
    assertEquals(hp(3, 4), rh.find((BiPredicate<Integer, Integer>) (key, value) -> value == 4));
  }

  @Test
  public void testFindAll() {
    assertEquals(ra(hp(1, 2), hp(3, 4)),
        rh.findAll((BiPredicate<Integer, Integer>) (key, value) -> key < 4));
  }

  @Test
  public void testFindIndex() {
    BiPredicate<Integer, Integer> block = (key, value) -> key < 4;
    assertEquals(Integer.valueOf(0), rh.findIndex(block));
    rh = rh(5, 6);
    assertNull(rh.findIndex(block));
  }

  @Test
  public void testFlatMap() {
    assertEquals(ra(3L, 7L, 11L), rh.flatMap((BiFunction<Integer, Integer, RubyArray<Long>>) (key,
        value) -> ra(Long.valueOf(key + value))).toA());
  }

  @Test
  public void testGrep() {
    assertEquals(ra(7L), rh.grep("4",
        (BiFunction<Integer, Integer, Long>) (key, value) -> Long.valueOf(key + value)));
  }

  @Test
  public void testGroupBy() {
    assertEquals(rh(true, ra(hp(1, 2), hp(3, 4)), false, ra(hp(5, 6))),
        rh.groupBy((BiFunction<Integer, Integer, Boolean>) (key, value) -> key + value < 10));
  }

  @Test
  public void testMap() {
    assertEquals(ra(3L, 7L, 11L),
        rh.map((BiFunction<Integer, Integer, Long>) (key, value) -> Long.valueOf(key + value)));
  }

  @Test
  public void testMaxByWithComparator() {
    assertEquals(hp(3, 4),
        rh(1, 6, 2, 5, 3, 4).maxBy((Comparator<Long>) (o1, o2) -> (int) (o2 - o1),
            (BiFunction<Integer, Integer, Long>) (key, value) -> Long.valueOf(value - key)));
  }

  @Test
  public void testMaxBy() {
    assertEquals(hp(1, 6), rh(1, 6, 2, 5, 3, 4)
        .maxBy((BiFunction<Integer, Integer, Long>) (key, value) -> Long.valueOf(value - key)));
  }

  @Test
  public void testMinByWithComparator() {
    assertEquals(hp(1, 6),
        rh(1, 6, 2, 5, 3, 4).minBy((Comparator<Long>) (o1, o2) -> (int) (o2 - o1),
            (BiFunction<Integer, Integer, Long>) (key, value) -> Long.valueOf(value - key)));
  }

  @Test
  public void testMinBy() {
    assertEquals(hp(3, 4), rh(1, 6, 2, 5, 3, 4)
        .minBy((BiFunction<Integer, Integer, Long>) (key, value) -> Long.valueOf(value - key)));
  }

  @Test
  public void testMinmaxByWithComparator() {
    assertEquals(ra(hp(1, 6), hp(3, 4)),
        rh(1, 6, 2, 5, 3, 4).minmaxBy((Comparator<Long>) (o1, o2) -> (int) (o2 - o1),
            (BiFunction<Integer, Integer, Long>) (key, value) -> Long.valueOf(value - key)));
  }

  @Test
  public void testMinmaxBy() {
    assertEquals(ra(hp(3, 4), hp(1, 6)), rh(1, 6, 2, 5, 3, 4)
        .minmaxBy((BiFunction<Integer, Integer, Long>) (key, value) -> Long.valueOf(value - key)));
  }

  @Test
  public void testNoneʔ() {
    BiPredicate<Integer, Integer> block = (key, value) -> value >= 10;
    assertTrue(rh.noneʔ(block));
    rh = rh(9, 10);
    assertFalse(rh.noneʔ(block));
  }

  @Test
  public void testOneʔ() {
    assertFalse(rh.oneʔ((BiPredicate<Integer, Integer>) (key, value) -> value > 3));
  }

  @Test
  public void testPartition() {
    assertEquals(ra(ra(hp(1, 2)), ra(hp(3, 4), hp(5, 6))),
        rh.partition((BiPredicate<Integer, Integer>) (key, value) -> key == 1));
  }

  @Test
  public void testReject() {
    assertEquals(ra(hp(3, 4), hp(5, 6)),
        rh.reject((BiPredicate<Integer, Integer>) (key, value) -> key == 1));
  }

  @Test
  public void testReverseEach() {
    final RubyArray<Integer> ints = ra();
    rh.reverseEach((BiConsumer<Integer, Integer>) (key, value) -> ints.push(key));
    assertEquals(ra(5, 3, 1), ints);
  }

  @Test
  public void testSelect() {
    assertEquals(ra(hp(1, 2), hp(3, 4)),
        rh.select((BiPredicate<Integer, Integer>) (key, value) -> key < 4));
  }

  @Test
  public void testSliceBefore() {
    assertEquals(ra(ra(hp(1, 2), hp(3, 4)), ra(hp(5, 6))),
        rh.sliceBefore((BiPredicate<Integer, Integer>) (key, value) -> key > 4).toA());
  }

  @Test
  public void testSortByWithComparator() {
    assertEquals(ra(hp(3, 4), hp(2, 5), hp(1, 6)),
        rh(1, 6, 2, 5, 3, 4).sortBy((Comparator<Long>) (o1, o2) -> (int) (o2 - o1),
            (BiFunction<Integer, Integer, Long>) (key, value) -> Long.valueOf(key)));
  }

  @Test
  public void testSortByBy() {
    assertEquals(ra(hp(1, 6), hp(2, 5), hp(3, 4)), rh(1, 6, 2, 5, 3, 4)
        .sortBy((BiFunction<Integer, Integer, Long>) (key, value) -> Long.valueOf(key)));
  }

  @Test
  public void testTakeWhile() {
    assertEquals(ra(hp(1, 2), hp(3, 4)),
        rh.takeWhile((BiPredicate<Integer, Integer>) (key, value) -> key < 5));
  }

  @Test
  public void testSize() {
    Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
    map.put(1, 2);
    map.put(3, 4);
    rh = rh(1, 2, 3, 4);
    assertEquals(map.size(), rh.size());
    map.clear();
    rh.clear();
    assertEquals(map.size(), rh.size());
  }

  @Test
  public void testIsEmpty() {
    Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
    map.put(1, 2);
    map.put(3, 4);
    rh = rh(1, 2, 3, 4);
    assertEquals(map.isEmpty(), rh.isEmpty());
    map.clear();
    rh.clear();
    assertEquals(map.isEmpty(), rh.isEmpty());
  }

  @Test
  public void testContainsKey() {
    Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
    map.put(1, 2);
    map.put(3, 4);
    rh = rh(1, 2, 3, 4);
    assertEquals(map.containsKey(1), rh.containsKey(1));
    assertEquals(map.containsKey(2), rh.containsKey(2));
  }

  @Test
  public void testContainsValue() {
    Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
    map.put(1, 2);
    map.put(3, 4);
    rh = rh(1, 2, 3, 4);
    assertEquals(map.containsValue(1), rh.containsValue(1));
    assertEquals(map.containsValue(2), rh.containsValue(2));
  }

  @Test
  public void testGet() {
    Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
    map.put(1, 2);
    map.put(3, 4);
    rh = rh(1, 2, 3, 4);
    assertEquals(map.get(1), rh.get(1));
  }

  @Test
  public void testRemove() {
    Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
    map.put(1, 2);
    map.put(3, 4);
    rh = rh(1, 2, 3, 4);
    map.remove(1);
    rh.remove(1);
    assertEquals(map, rh);
  }

  @Test
  public void testPutAll() {
    Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
    map.put(1, 2);
    map.put(3, 4);
    rh = rh(1, 2, 3, 4);
    Map<Integer, Integer> other = new LinkedHashMap<Integer, Integer>();
    other.put(5, 6);
    other.put(7, 8);
    map.putAll(other);
    rh.putAll(other);
    assertEquals(map, rh);
  }

  @Test
  public void testClear() {
    Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
    map.put(1, 2);
    map.put(3, 4);
    rh = rh(1, 2, 3, 4);
    map.clear();
    rh.clear();
    assertEquals(map, rh);
  }

  @Test
  public void testKeySet() {
    Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
    map.put(1, 2);
    map.put(3, 4);
    rh = rh(1, 2, 3, 4);
    assertEquals(map.keySet(), rh.keySet());
  }

  @Test
  public void testEntrySet() {
    Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
    map.put(1, 2);
    map.put(3, 4);
    rh = rh(1, 2, 3, 4);
    assertEquals(map.entrySet(), rh.entrySet());
  }

  @Test
  public void testEquals() {
    Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
    map.put(1, 2);
    map.put(3, 4);
    rh = rh(1, 2, 3, 4);
    Map<Integer, Integer> other = new LinkedHashMap<Integer, Integer>();
    other.put(1, 2);
    other.put(3, 4);
    assertEquals(map.equals(other), rh.equals(other));
  }

  @Test
  public void testHashCode() {
    Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
    map.put(1, 2);
    map.put(3, 4);
    rh = rh(1, 2, 3, 4);
    assertEquals(map.hashCode(), rh.hashCode());
  }

  @Test
  public void testToString() {
    Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
    map.put(1, 2);
    map.put(3, 4);
    rh = rh(1, 2, 3, 4);
    assertEquals(map.toString(), rh.toString());
  }

  @Test
  public void testComparableEntry() {
    assertEquals(rh(1, 2, 3, 4, 5, 6).toA(), rh.sort());
    assertEquals(hp(1, 2), rh.min());
    assertEquals(hp(5, 6), rh.max());
    assertTrue(rh.iterator().next() instanceof ComparableEntry);
    assertTrue(rh.assoc(1) instanceof ComparableEntry);
    assertTrue(rh.rassoc(6) instanceof ComparableEntry);
    assertTrue(rh.deleteIf().iterator().next() instanceof ComparableEntry);
    assertTrue(rh.each().iterator().next() instanceof ComparableEntry);
    assertTrue(rh.eachPair().iterator().next() instanceof ComparableEntry);
    assertTrue(rh.flatten().iterator().next() instanceof ComparableEntry);
    assertTrue(rh.keepIf().iterator().next() instanceof ComparableEntry);
    assertTrue(rh.rejectǃ().iterator().next() instanceof ComparableEntry);
    assertTrue(rh.shift() instanceof ComparableEntry);
  }

}
