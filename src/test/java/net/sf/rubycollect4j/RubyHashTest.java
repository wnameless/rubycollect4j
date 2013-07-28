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

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;

import net.sf.rubycollect4j.block.Block;
import net.sf.rubycollect4j.block.EntryBlock;
import net.sf.rubycollect4j.block.EntryBooleanBlock;
import net.sf.rubycollect4j.block.EntryMergeBlock;
import net.sf.rubycollect4j.block.EntryTransformBlock;

import org.junit.Before;
import org.junit.Test;

import static net.sf.rubycollect4j.RubyCollections.hp;
import static net.sf.rubycollect4j.RubyCollections.newRubyHash;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.rh;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RubyHashTest {

  private RubyHash<Integer, Integer> rh;

  @Before
  public void setUp() {
    rh = new RubyHash<Integer, Integer>();
    rh.put(1, 2);
    rh.put(3, 4);
    rh.put(5, 6);
  }

  @Test
  public void testConstructor() {
    assertTrue(rh instanceof RubyHash);
    rh = new RubyHash<Integer, Integer>(new LinkedHashMap<Integer, Integer>());
    assertTrue(rh instanceof RubyHash);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException() {
    rh = new RubyHash<Integer, Integer>(null);
  }

  @Test
  public void testAssoc() {
    assertEquals(hp(3, 4), rh.assoc(3));
    assertNull(rh.assoc(7));
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

  @SuppressWarnings("unchecked")
  @Test
  public void testDeleteIf() {
    assertTrue(rh.deleteIf() instanceof RubyEnumerator);
    assertEquals(ra(hp(1, 2), hp(3, 4), hp(5, 6)), rh.deleteIf().toA());
  }

  @Test
  public void testDeleteIfWithBlock() {
    assertEquals(rh(3, 4, 5, 6),
        rh.deleteIf(new EntryBooleanBlock<Integer, Integer>() {

          @Override
          public boolean yield(Integer key, Integer value) {
            return key + value < 7;
          }

        }));
    assertEquals(rh(3, 4, 5, 6), rh);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testEach() {
    assertTrue(rh.each() instanceof RubyEnumerator);
    assertEquals(ra(hp(1, 2), hp(3, 4), hp(5, 6)), rh.each().toA());
  }

  @Test
  public void testEachWithBlock() {
    final RubyArray<Integer> ints = ra();
    assertEquals(rh, rh.each(new EntryBlock<Integer, Integer>() {

      @Override
      public void yield(Integer key, Integer value) {
        ints.push(key);
        ints.push(value);
      }

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
    assertEquals(rh, rh.eachKey(new Block<Integer>() {

      @Override
      public void yield(Integer key) {
        ints.push(key);
      }

    }));
    assertEquals(ra(1, 3, 5), ints);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testEachPair() {
    assertTrue(rh.eachPair() instanceof RubyEnumerator);
    assertEquals(ra(hp(1, 2), hp(3, 4), hp(5, 6)), rh.eachPair().toA());
  }

  @Test
  public void testEachPairWithBlock() {
    final RubyArray<Integer> ints = ra();
    assertEquals(rh, rh.eachPair(new EntryBlock<Integer, Integer>() {

      @Override
      public void yield(Integer key, Integer value) {
        ints.push(key);
        ints.push(value);
      }

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
    assertEquals(rh, rh.eachValue(new Block<Integer>() {

      @Override
      public void yield(Integer key) {
        ints.push(key);
      }

    }));
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

  @Test(expected = NoSuchElementException.class)
  public void testFetchException() {
    assertNull(rh.fetch(7));
  }

  @Test
  public void testFetchWithDefaultValue() {
    assertEquals(Integer.valueOf(6), rh.fetch(5, 10));
    assertEquals(Integer.valueOf(10), rh.fetch(7, 10));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testFlatten() {
    assertEquals(ra(hp(1, 2), hp(3, 4), hp(5, 6)), rh.flatten());
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

  @SuppressWarnings("unchecked")
  @Test
  public void testKeepIf() {
    assertTrue(rh.keepIf() instanceof RubyEnumerator);
    assertEquals(ra(hp(1, 2), hp(3, 4), hp(5, 6)), rh.keepIf().toA());
  }

  @Test
  public void testKeepIfWithBlock() {
    assertEquals(rh(1, 2), rh.keepIf(new EntryBooleanBlock<Integer, Integer>() {

      @Override
      public boolean yield(Integer key, Integer value) {
        return key + value < 7;
      }

    }));
    assertEquals(rh(1, 2), rh);
  }

  @Test
  public void testKey() {
    assertEquals(Integer.valueOf(1), rh.key(2));
    assertNull(rh.key(8));
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
        rh.merge(rh(0, 1, 1, 3, 3, 2), new EntryMergeBlock<Integer, Integer>() {

          @Override
          public Integer yield(Integer key, Integer oldval, Integer newval) {
            return Math.max(oldval, newval);
          }

        }));
  }

  @Test
  public void testMergeǃ() {
    assertEquals(rh(1, 2, 3, 8, 5, 9, 7, 7), rh.mergeǃ(rh(3, 8, 5, 9, 7, 7)));
    assertEquals(rh(1, 2, 3, 8, 5, 9, 7, 7), rh);
  }

  @Test
  public void testMergeǃWithBlock() {
    assertEquals(rh(1, 3, 3, 4, 5, 6, 0, 1), rh.mergeǃ(rh(0, 1, 1, 3, 3, 2),
        new EntryMergeBlock<Integer, Integer>() {

          @Override
          public Integer yield(Integer key, Integer oldval, Integer newval) {
            return Math.max(oldval, newval);
          }

        }));
    assertEquals(rh(1, 3, 3, 4, 5, 6, 0, 1), rh);
  }

  @SuppressWarnings("unchecked")
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
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testRejectǃ() {
    assertTrue(rh.rejectǃ() instanceof RubyEnumerator);
    assertEquals(ra(hp(1, 2), hp(3, 4), hp(5, 6)), rh.rejectǃ().toA());
  }

  @Test
  public void testRejectǃWithBlock() {
    assertEquals(rh(1, 2, 5, 6),
        rh.rejectǃ(new EntryBooleanBlock<Integer, Integer>() {

          @Override
          public boolean yield(Integer key, Integer value) {
            return key + value == 7;
          }

        }));
    assertEquals(rh(1, 2, 5, 6), rh);
    assertNull(rh.rejectǃ(new EntryBooleanBlock<Integer, Integer>() {

      @Override
      public boolean yield(Integer key, Integer value) {
        return key + value == 7;
      }

    }));
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
    assertTrue(rh == rh.toH());
  }

  @Test
  public void testToHash() {
    assertTrue(rh == rh.toHash());
  }

  @Test
  public void testToS() {
    assertEquals("{1=2, 3=4, 5=6}", rh.toS());
    assertEquals(rh.toString(), rh.toS());
  }

  @Test
  public void testUpdate() {
    assertEquals(rh(1, 2, 3, 8, 5, 9, 7, 7), rh.update(rh(3, 8, 5, 9, 7, 7)));
    assertEquals(rh(1, 2, 3, 8, 5, 9, 7, 7), rh);
  }

  @Test
  public void testUpdateWithBlock() {
    assertEquals(rh(1, 3, 3, 4, 5, 6, 0, 1), rh.update(rh(0, 1, 1, 3, 3, 2),
        new EntryMergeBlock<Integer, Integer>() {

          @Override
          public Integer yield(Integer key, Integer oldval, Integer newval) {
            return Math.max(oldval, newval);
          }

        }));
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
  }

  @Test
  public void testValueʔ() {
    assertTrue(rh.valueʔ(4));
    assertFalse(rh.valueʔ(8));
  }

  // Tests for entry blocks of RubyEnumerable methods
  @Test
  public void testAllʔ() {
    assertTrue(rh.allʔ(new EntryBooleanBlock<Integer, Integer>() {

      @Override
      public boolean yield(Integer key, Integer value) {
        return key > 0 && value > 0;
      }

    }));
  }

  @Test
  public void testAnyʔ() {
    assertTrue(rh.anyʔ(new EntryBooleanBlock<Integer, Integer>() {

      @Override
      public boolean yield(Integer key, Integer value) {
        return key > 0 && value > 0;
      }

    }));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testChunk() {
    assertEquals(
        ra(hp(3L, ra(hp(1, 2))), hp(7L, ra(hp(3, 4))), hp(11L, ra(hp(5, 6)))),
        rh.chunk(new EntryTransformBlock<Integer, Integer, Long>() {

          @Override
          public Long yield(Integer key, Integer value) {
            return Long.valueOf(key + value);
          }

        }).toA());
  }

  @Test
  public void testCollect() {
    assertEquals(ra(3L, 7L, 11L),
        rh.collect(new EntryTransformBlock<Integer, Integer, Long>() {

          @Override
          public Long yield(Integer key, Integer value) {
            return Long.valueOf(key + value);
          }

        }));
  }

  @Test
  public void testCollectConcat() {
    assertEquals(
        ra(3L, 7L, 11L),
        rh.collectConcat(
            new EntryTransformBlock<Integer, Integer, RubyArray<Long>>() {

              @Override
              public RubyArray<Long> yield(Integer key, Integer value) {
                return ra(Long.valueOf(key + value));
              }

            }).toA());
  }

  @Test
  public void testCount() {
    assertEquals(1, rh.count(new EntryBooleanBlock<Integer, Integer>() {

      @Override
      public boolean yield(Integer key, Integer value) {
        return key == 1;
      }

    }));
  }

  @Test(expected = IllegalStateException.class)
  public void testCycle() {
    final RubyArray<Integer> ints = ra();
    rh.cycle(new EntryBlock<Integer, Integer>() {

      @Override
      public void yield(Integer key, Integer value) {
        ints.add(key);
        ints.add(value);
        if (ints.size() > 1000) {
          throw new IllegalStateException();
        }
      }

    });
  }

  @Test
  public void testCycleWithN() {
    final RubyArray<Integer> ints = ra();
    rh.cycle(2, new EntryBlock<Integer, Integer>() {

      @Override
      public void yield(Integer key, Integer value) {
        ints.add(key);
        ints.add(value);
      }

    });
    assertEquals(ra(1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6), ints);
  }

  @Test
  public void testDetect() {
    assertEquals(hp(3, 4), rh.detect(new EntryBooleanBlock<Integer, Integer>() {

      @Override
      public boolean yield(Integer key, Integer value) {
        return value == 4;
      }

    }));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testDropWhile() {
    assertEquals(ra(hp(3, 4), hp(5, 6)),
        rh.dropWhile(new EntryBooleanBlock<Integer, Integer>() {

          @Override
          public boolean yield(Integer key, Integer value) {
            return key + value <= 4;
          }

        }));
  }

  @Test
  public void testEachEntry() {
    final RubyArray<Integer> ints = ra();
    rh.eachEntry(new EntryBlock<Integer, Integer>() {

      @Override
      public void yield(Integer key, Integer value) {
        ints.push(value);
      }

    });
    assertEquals(ra(2, 4, 6), ints);
  }

  @Test
  public void testFind() {
    assertEquals(hp(3, 4), rh.find(new EntryBooleanBlock<Integer, Integer>() {

      @Override
      public boolean yield(Integer key, Integer value) {
        return value == 4;
      }

    }));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testFindAll() {
    assertEquals(ra(hp(1, 2), hp(3, 4)),
        rh.findAll(new EntryBooleanBlock<Integer, Integer>() {

          @Override
          public boolean yield(Integer key, Integer value) {
            return key < 4;
          }

        }));
  }

  @Test
  public void testFindIndex() {
    assertEquals(Integer.valueOf(0),
        rh.findIndex(new EntryBooleanBlock<Integer, Integer>() {

          @Override
          public boolean yield(Integer key, Integer value) {
            return key < 4;
          }

        }));
  }

  @Test
  public void testFlatMap() {
    assertEquals(
        ra(3L, 7L, 11L),
        rh.flatMap(
            new EntryTransformBlock<Integer, Integer, RubyArray<Long>>() {

              @Override
              public RubyArray<Long> yield(Integer key, Integer value) {
                return ra(Long.valueOf(key + value));
              }

            }).toA());
  }

  @Test
  public void testGrep() {
    assertEquals(ra(7L),
        rh.grep("4", new EntryTransformBlock<Integer, Integer, Long>() {

          @Override
          public Long yield(Integer key, Integer value) {
            return Long.valueOf(key + value);
          }

        }));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testGroupBy() {
    assertEquals(rh(true, ra(hp(1, 2), hp(3, 4)), false, ra(hp(5, 6))),
        rh.groupBy(new EntryTransformBlock<Integer, Integer, Boolean>() {

          @Override
          public Boolean yield(Integer key, Integer value) {
            return key + value < 10;
          }

        }));
  }

  @Test
  public void testMap() {
    assertEquals(ra(3L, 7L, 11L),
        rh.map(new EntryTransformBlock<Integer, Integer, Long>() {

          @Override
          public Long yield(Integer key, Integer value) {
            return Long.valueOf(key + value);
          }

        }));
  }

  @Test
  public void testMaxByWithComparator() {
    assertEquals(hp(3, 4), rh(1, 6, 2, 5, 3, 4).maxBy(new Comparator<Long>() {

      @Override
      public int compare(Long o1, Long o2) {
        return (int) (o2 - o1);
      }

    }, new EntryTransformBlock<Integer, Integer, Long>() {

      @Override
      public Long yield(Integer key, Integer value) {
        return Long.valueOf(value - key);
      }

    }));
  }

  @Test
  public void testMaxBy() {
    assertEquals(
        hp(1, 6),
        rh(1, 6, 2, 5, 3, 4).maxBy(
            new EntryTransformBlock<Integer, Integer, Long>() {

              @Override
              public Long yield(Integer key, Integer value) {
                return Long.valueOf(value - key);
              }

            }));
  }

  @Test
  public void testMinByWithComparator() {
    assertEquals(hp(1, 6), rh(1, 6, 2, 5, 3, 4).minBy(new Comparator<Long>() {

      @Override
      public int compare(Long o1, Long o2) {
        return (int) (o2 - o1);
      }

    }, new EntryTransformBlock<Integer, Integer, Long>() {

      @Override
      public Long yield(Integer key, Integer value) {
        return Long.valueOf(value - key);
      }

    }));
  }

  @Test
  public void testMinBy() {
    assertEquals(
        hp(3, 4),
        rh(1, 6, 2, 5, 3, 4).minBy(
            new EntryTransformBlock<Integer, Integer, Long>() {

              @Override
              public Long yield(Integer key, Integer value) {
                return Long.valueOf(value - key);
              }

            }));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testMinmaxByWithComparator() {
    assertEquals(ra(hp(1, 6), hp(3, 4)),
        rh(1, 6, 2, 5, 3, 4).minmaxBy(new Comparator<Long>() {

          @Override
          public int compare(Long o1, Long o2) {
            return (int) (o2 - o1);
          }

        }, new EntryTransformBlock<Integer, Integer, Long>() {

          @Override
          public Long yield(Integer key, Integer value) {
            return Long.valueOf(value - key);
          }

        }));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testMinmaxBy() {
    assertEquals(
        ra(hp(3, 4), hp(1, 6)),
        rh(1, 6, 2, 5, 3, 4).minmaxBy(
            new EntryTransformBlock<Integer, Integer, Long>() {

              @Override
              public Long yield(Integer key, Integer value) {
                return Long.valueOf(value - key);
              }

            }));
  }

  @Test
  public void testNoneʔ() {
    assertTrue(rh.noneʔ(new EntryBooleanBlock<Integer, Integer>() {

      @Override
      public boolean yield(Integer key, Integer value) {
        return value > 10;
      }

    }));
  }

  @Test
  public void testOneʔ() {
    assertFalse(rh.oneʔ(new EntryBooleanBlock<Integer, Integer>() {

      @Override
      public boolean yield(Integer key, Integer value) {
        return value > 3;
      }

    }));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testPartition() {
    assertEquals(ra(ra(hp(1, 2)), ra(hp(3, 4), hp(5, 6))),
        rh.partition(new EntryBooleanBlock<Integer, Integer>() {

          @Override
          public boolean yield(Integer key, Integer value) {
            return key == 1;
          }

        }));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testReject() {
    assertEquals(ra(hp(3, 4), hp(5, 6)),
        rh.reject(new EntryBooleanBlock<Integer, Integer>() {

          @Override
          public boolean yield(Integer key, Integer value) {
            return key == 1;
          }

        }));
  }

  @Test
  public void testReverseEach() {
    final RubyArray<Integer> ints = ra();
    rh.reverseEach(new EntryBlock<Integer, Integer>() {

      @Override
      public void yield(Integer key, Integer value) {
        ints.push(key);
      }

    });
    assertEquals(ra(5, 3, 1), ints);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSelect() {
    assertEquals(ra(hp(1, 2), hp(3, 4)),
        rh.select(new EntryBooleanBlock<Integer, Integer>() {

          @Override
          public boolean yield(Integer key, Integer value) {
            return key < 4;
          }

        }));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSliceBefore() {
    assertEquals(ra(ra(hp(1, 2), hp(3, 4)), ra(hp(5, 6))),
        rh.sliceBefore(new EntryBooleanBlock<Integer, Integer>() {

          @Override
          public boolean yield(Integer key, Integer value) {
            return key > 4;
          }

        }).toA());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSortByWithComparator() {
    assertEquals(ra(hp(3, 4), hp(2, 5), hp(1, 6)),
        rh(1, 6, 2, 5, 3, 4).sortBy(new Comparator<Long>() {

          @Override
          public int compare(Long o1, Long o2) {
            return (int) (o2 - o1);
          }

        }, new EntryTransformBlock<Integer, Integer, Long>() {

          @Override
          public Long yield(Integer key, Integer value) {
            return Long.valueOf(key);
          }

        }));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSortByBy() {
    assertEquals(
        ra(hp(1, 6), hp(2, 5), hp(3, 4)),
        rh(1, 6, 2, 5, 3, 4).sortBy(
            new EntryTransformBlock<Integer, Integer, Long>() {

              @Override
              public Long yield(Integer key, Integer value) {
                return Long.valueOf(key);
              }

            }));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testTakeWhile() {
    assertEquals(ra(hp(1, 2), hp(3, 4)),
        rh.takeWhile(new EntryBooleanBlock<Integer, Integer>() {

          @Override
          public boolean yield(Integer key, Integer value) {
            return key < 5;
          }

        }));
  }

}
