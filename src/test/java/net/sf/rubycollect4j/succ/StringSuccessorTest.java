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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringSuccessorTest {

  private StringSuccessor successor = StringSuccessor.getInstance();

  @Test
  public void testSingleton() {
    assertTrue(successor == StringSuccessor.getInstance());
  }

  @Test
  public void testSucc() {
    assertEquals("", successor.succ(""));
    assertEquals(String.valueOf((char) 1) + String.valueOf((char) 1),
        successor.succ(String.valueOf((char) 65535)));
    assertEquals("10", successor.succ("9"));
    assertEquals("aa", successor.succ("z"));
    assertEquals("AA", successor.succ("Z"));
    assertEquals("AAA-000", successor.succ("ZZ-999"));
    assertEquals("98.77", successor.succ("98.76"));
    assertEquals("Hello worle!", successor.succ("Hello world!"));
    assertEquals("$$$%", successor.succ("$$$$"));
  }

  @Test
  public void testCompareTo() {
    assertEquals(-1, successor.compare("1", "2"));
    assertEquals(1, successor.compare("2.12", "1.1"));
    assertEquals(-1, successor.compare("a", "1.1"));
    assertEquals(1, successor.compare("2.22", "abc"));
    assertEquals(0, successor.compare("AAA", "AAA"));
  }

  @Test
  public void testToString() {
    assertEquals("StringSuccessor", successor.toString());
  }

}
