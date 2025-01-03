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
package net.sf.rubycollect4j.util;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TryComparatorTest {

  TryComparator<Integer> intComp;
  TryComparator<Integer> revIntComp;
  TryComparator<Set<Integer>> setComp;
  Set<Integer> emptySet;
  Set<Integer> singleSet;

  @BeforeEach
  public void setUp() throws Exception {
    intComp = new TryComparator<Integer>();
    revIntComp = new TryComparator<Integer>((o1, o2) -> o2 - o1);
    setComp = new TryComparator<Set<Integer>>();
    emptySet = Collections.emptySet();
    singleSet = Collections.singleton(1);
  }

  @Test
  public void testConstructor() {
    assertTrue(intComp instanceof TryComparator);
    assertTrue(revIntComp instanceof TryComparator);
  }

  @Test
  public void testCompare() {
    assertEquals(1, intComp.compare(1, 0));
    assertEquals(-1, revIntComp.compare(1, 0));
    assertEquals(0, intComp.compare(null, null));
    assertEquals(0, setComp.compare(emptySet, new HashSet<Integer>()));
  }

  @Test
  public void testCompareException1() {
    assertThrows(IllegalArgumentException.class, () -> {
      setComp.compare(null, emptySet);
    });
  }

  @Test
  public void testCompareException2() {
    assertThrows(IllegalArgumentException.class, () -> {
      setComp.compare(emptySet, null);
    });
  }

  @Test
  public void testCompareException3() {
    assertThrows(IllegalArgumentException.class, () -> {
      setComp.compare(emptySet, singleSet);
    });
  }

}
