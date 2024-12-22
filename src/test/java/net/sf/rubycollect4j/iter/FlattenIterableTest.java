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
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import net.sf.rubycollect4j.RubyArray;

public class FlattenIterableTest {

  FlattenIterable<Integer, Double> iter;
  List<Integer> list;
  Function<Integer, RubyArray<Double>> block;

  @BeforeEach
  public void setUp() throws Exception {
    list = ra(1, 2, 3);
    block = item -> ra(item.doubleValue(), item.doubleValue());
    iter = new FlattenIterable<Integer, Double>(list, block);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof FlattenIterable);
  }

  @Test
  public void testConstructorException1() {
    assertThrows(NullPointerException.class, () -> {
      new FlattenIterable<Integer, Double>(null, block);
    });
  }

  @Test
  public void testConstructorException2() {
    assertThrows(NullPointerException.class, () -> {
      new FlattenIterable<Integer, Double>(list, null);
    });
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof FlattenIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[1.0, 1.0, 2.0, 2.0, 3.0, 3.0]", iter.toString());
  }

}
