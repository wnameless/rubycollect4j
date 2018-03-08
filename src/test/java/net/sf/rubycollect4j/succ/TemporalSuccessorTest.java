/*
 *
 * Copyright 2018 Wei-Ming Wu
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.Test;

public class TemporalSuccessorTest {

  TemporalSuccessor<LocalDateTime> successor =
      new TemporalSuccessor<>(ChronoUnit.SECONDS);

  @Test(expected = NullPointerException.class)
  public void testConstructorException() {
    new TemporalSuccessor<LocalDateTime>(null);
  }

  @Test
  public void testSucc() {
    LocalDateTime now = LocalDateTime.now();
    assertEquals(now.plusSeconds(1), successor.succ(now));
  }

  @Test
  public void testCompareTo() {
    LocalDateTime now = LocalDateTime.now();
    assertEquals(-1, successor.compare(now, now.plusSeconds(1)));
    assertEquals(1, successor.compare(now, now.minusSeconds(1)));
    assertEquals(0, successor.compare(now, now));
  }

  @Test
  public void testToString() {
    assertEquals("TemporalSuccessor{temporalUnit=" + ChronoUnit.SECONDS + "}",
        successor.toString());
  }

}
