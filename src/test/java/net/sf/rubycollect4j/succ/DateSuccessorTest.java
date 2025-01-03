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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import net.sf.rubycollect4j.Ruby;

public class DateSuccessorTest {

  DateSuccessor successor = DateSuccessor.getInstance();

  @Test
  public void testSingleton() {
    assertSame(DateSuccessor.getInstance(), successor);
  }

  @Test
  public void testSucc() {
    assertEquals(Ruby.Date.of(2013, 1, 1), successor.succ(Ruby.Date.of(2012, 12, 31)));
    assertEquals(Ruby.Date.of(2013, 1, 2), successor.succ(Ruby.Date.of(2013, 1, 1)));
  }

  @Test
  public void testCompareTo() {
    assertEquals(-1, successor.compare(Ruby.Date.of(2012, 12, 31), Ruby.Date.of(2013, 1, 1)));
    assertEquals(0, successor.compare(Ruby.Date.of(2013, 1, 1), Ruby.Date.of(2013, 1, 1)));
    assertEquals(1, successor.compare(Ruby.Date.of(2013, 1, 2), Ruby.Date.of(2013, 1, 1)));
  }

  @Test
  public void testToString() {
    assertEquals("DateSuccessor", successor.toString());
  }

}
