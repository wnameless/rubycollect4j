/*
 *
 * Copyright 2016 Wei-Ming Wu
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
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GrepVIterableTest {

  GrepVIterable<Integer> iter;
  List<Integer> list;
  String regex;

  @BeforeEach
  public void setUp() throws Exception {
    list = ra(1, 2, 3);
    regex = "2|3";
    iter = new GrepVIterable<Integer>(list, regex);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof GrepVIterable);
  }

  @Test
  public void testConstructorException1() {
    assertThrows(NullPointerException.class, () -> {
      new GrepVIterable<Integer>(list, null);
    });
  }

  @Test
  public void testConstructorException2() {
    assertThrows(NullPointerException.class, () -> {
      new GrepVIterable<Integer>(null, regex);
    });
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof GrepVIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[1]", iter.toString());
  }

}
