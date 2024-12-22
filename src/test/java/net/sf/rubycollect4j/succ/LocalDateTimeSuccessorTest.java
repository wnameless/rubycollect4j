/*
 *
 * Copyright 2017 Wei-Ming Wu
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
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class LocalDateTimeSuccessorTest {

  LocalDateTimeSuccessor successor = LocalDateTimeSuccessor.getInstance();

  @Test
  public void testSingleton() {
    assertSame(LocalDateTimeSuccessor.getInstance(), successor);
  }

  @Test
  public void testSucc() {
    LocalDateTime now = LocalDateTime.now();
    assertEquals(now.plusDays(1), successor.succ(now));
  }

  @Test
  public void testCompareTo() {
    LocalDateTime now = LocalDateTime.now();
    assertEquals(-1, successor.compare(now, now.plusMinutes(1)));
    assertEquals(0, successor.compare(now, now));
    assertEquals(1, successor.compare(now.plusMinutes(1), now));
  }

  @Test
  public void testToString() {
    assertEquals("LocalDateTimeSuccessor", successor.toString());
  }

}
