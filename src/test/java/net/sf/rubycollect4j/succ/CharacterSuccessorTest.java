/*
 *
 * Copyright 2013-2015 Wei-Ming Wu
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

public class CharacterSuccessorTest {

  CharacterSuccessor successor = CharacterSuccessor.getInstance();

  @Test
  public void testSingleton() {
    assertSame(CharacterSuccessor.getInstance(), successor);
  }

  @Test
  public void testSucc() {
    assertEquals(Character.valueOf('B'), successor.succ('A'));
    assertEquals(Character.valueOf('{'), successor.succ('z'));
  }

  @Test
  public void testCompareTo() {
    assertEquals(-1, successor.compare('1', '2'));
    assertEquals(1, successor.compare('2', '1'));
    assertEquals(0, successor.compare('A', 'A'));
  }

  @Test
  public void testToString() {
    assertEquals("CharacterSuccessor", successor.toString());
  }

}
