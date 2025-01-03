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

import static net.sf.rubycollect4j.RubyCollections.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import net.sf.rubycollect4j.RubyHash;

public class ComparableEntryIteratorTest {

  ComparableEntryIterator<Integer, Integer> iter;
  RubyHash<Integer, Integer> rubyHash;

  @BeforeEach
  public void setUp() throws Exception {
    rubyHash = rh(1, 2, 3, 4);
    iter = new ComparableEntryIterator<>(rubyHash.entrySet().iterator());
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof ComparableEntryIterator);
  }

  @Test
  public void testHasNext() {
    assertTrue(iter.hasNext());
    while (iter.hasNext()) {
      iter.next();
    }
    assertFalse(iter.hasNext());
  }

  @Test
  public void testNext() {
    assertEquals(hp(1, 2), iter.next());
    assertEquals(hp(3, 4), iter.next());
    assertFalse(iter.hasNext());
  }

  @Test
  public void testRemove() {
    iter.next();
    iter.remove();
    assertEquals(rh(3, 4), rubyHash);
  }

}
