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
package net.sf.rubycollect4j.iter;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyLiterals.qr;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SliceBeforeIterableTest {

  SliceBeforeIterable<Integer> iter;
  List<Integer> list;
  Predicate<Integer> block;
  Pattern pattern;

  @BeforeEach
  public void setUp() throws Exception {
    list = ra(1, 2, 3, 4, 5);
    block = item -> item % 2 == 0;
    pattern = qr("3");
    iter = new SliceBeforeIterable<Integer>(list, block);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof SliceBeforeIterable);
    iter = new SliceBeforeIterable<Integer>(list, pattern);
    assertTrue(iter instanceof SliceBeforeIterable);
  }

  @Test
  public void testConstructorException1() {
    assertThrows(NullPointerException.class, () -> {
      new SliceBeforeIterable<Integer>(null, block);
    });
  }

  @Test
  public void testConstructorException2() {
    assertThrows(NullPointerException.class, () -> {
      new SliceBeforeIterable<Integer>(list, (Predicate<Integer>) null);
    });
  }

  @Test
  public void testConstructorException3() {
    assertThrows(NullPointerException.class, () -> {
      new SliceBeforeIterable<Integer>(null, pattern);
    });
  }

  @Test
  public void testConstructorException4() {
    assertThrows(NullPointerException.class, () -> {
      new SliceBeforeIterable<Integer>(list, (Pattern) null);
    });
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof SliceBeforeIterator);
    iter = new SliceBeforeIterable<Integer>(list, pattern);
    assertTrue(iter.iterator() instanceof SliceBeforeIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[[1], [2, 3], [4, 5]]", iter.toString());
  }

}
