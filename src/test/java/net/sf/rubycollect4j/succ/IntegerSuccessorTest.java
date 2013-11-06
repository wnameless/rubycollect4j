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
package net.sf.rubycollect4j.succ;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class IntegerSuccessorTest {

  private IntegerSuccessor successor = IntegerSuccessor.getInstance();

  @Test
  public void testSingleton() {
    assertSame(IntegerSuccessor.getInstance(), successor);
  }

  @Test
  public void testSucc() {
    assertEquals(Integer.valueOf(-1), successor.succ(-2));
    assertEquals(Integer.valueOf(0), successor.succ(-1));
    assertEquals(Integer.valueOf(1), successor.succ(0));
    assertEquals(Integer.valueOf(2), successor.succ(1));
  }

  @Test
  public void testCompareTo() {
    assertEquals(-1, successor.compare(-2, -1));
    assertEquals(0, successor.compare(0, 0));
    assertEquals(1, successor.compare(2, 1));
  }

  @Test
  public void testToString() {
    assertEquals("IntegerSuccessor", successor.toString());
  }

}
