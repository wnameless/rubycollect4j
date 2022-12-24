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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DoubleSuccessorTest {

  DoubleSuccessor successor;

  @BeforeEach
  public void setUp() throws Exception {
    successor = new DoubleSuccessor(2);
  }

  @Test
  public void testConstructor() {
    assertTrue(successor instanceof DoubleSuccessor);
  }

  @Test
  public void testConstructorException() {
    assertThrows(IllegalArgumentException.class, () -> {
      new DoubleSuccessor(-1);
    });
  }

  @Test
  public void testSucc() {
    assertEquals(Double.valueOf(1.5), successor.succ(1.49));
    successor = new DoubleSuccessor(3);
    assertEquals(Double.valueOf(1.601), successor.succ(1.6));
    successor = new DoubleSuccessor(0);
    assertEquals(Double.valueOf(2.7), successor.succ(1.7));
  }

  @Test
  public void testCompareTo() {
    assertEquals(0, successor.compare(1.500, 1.5));
    assertEquals(-1, successor.compare(1.499, 1.5));
    assertEquals(1, successor.compare(1.501, 1.5));
  }

  @Test
  public void testEquals() {
    assertTrue(new DoubleSuccessor(2).equals(successor));
    assertFalse(new DoubleSuccessor(3).equals(successor));
    assertFalse(successor.equals(null));
  }

  @Test
  public void testHashCode() {
    assertEquals(new DoubleSuccessor(2).hashCode(), successor.hashCode());
    assertNotEquals(new DoubleSuccessor(3).hashCode(), successor.hashCode());
    assertEquals(2, successor.hashCode());
  }

  @Test
  public void testToString() {
    assertEquals("DoubleSuccessor{precision=2}", successor.toString());
  }

}
