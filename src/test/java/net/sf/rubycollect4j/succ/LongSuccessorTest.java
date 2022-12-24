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
package net.sf.rubycollect4j.succ;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class LongSuccessorTest {

  LongSuccessor successor = LongSuccessor.getInstance();

  @Test
  public void testSingleton() {
    assertSame(LongSuccessor.getInstance(), successor);
  }

  @Test
  public void testSucc() {
    assertEquals(Long.valueOf(-1L), successor.succ(-2L));
    assertEquals(Long.valueOf(0L), successor.succ(-1L));
    assertEquals(Long.valueOf(1L), successor.succ(0L));
    assertEquals(Long.valueOf(2L), successor.succ(1L));
  }

  @Test
  public void testCompareTo() {
    assertEquals(-1, successor.compare(-2L, -1L));
    assertEquals(0, successor.compare(0L, 0L));
    assertEquals(1, successor.compare(2L, 1L));
  }

  @Test
  public void testToString() {
    assertEquals("LongSuccessor", successor.toString());
  }

}
