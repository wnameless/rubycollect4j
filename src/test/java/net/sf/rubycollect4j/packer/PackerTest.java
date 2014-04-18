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
package net.sf.rubycollect4j.packer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class PackerTest {

  @Test
  public void testPrivateConstructor() throws Exception {
    Constructor<Packer> c = Packer.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c.getModifiers()));
    c.setAccessible(true);
    c.newInstance();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPackWithInvalidDirective() {
    Packer.pack("X1", new ArrayList<Object>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPackWithShortArguments1() {
    Packer.pack("ccc", Arrays.asList(1, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPackWithShortArguments2() {
    Packer.pack("X6", Arrays.asList(1, 2));
  }

  @Test
  public void testPackWith_c() {
    assertEquals("ABC", Packer.pack("ccc", 65, 66, 67));
    assertEquals("ABC", Packer.pack("c3", 65, 66, 67));
  }

  @Test
  public void testPackWith_A() {
    assertEquals("a  b  c  ", Packer.pack("A3A3A3", "a", "b", "c"));
    assertEquals("a", Packer.pack("A*", "a"));
    assertEquals("ab", Packer.pack("A1A1", "abc", "bc"));
  }

  @Test
  public void testPackWith_a() {
    assertEquals("a\0\0b\0\0c\0\0", Packer.pack("a3a3a3", "a", "b", "c"));
  }

  @Test
  public void testPackWith_Z() {
    assertEquals("a\0", Packer.pack("Z*", "a"));
    assertEquals("a\0\0", Packer.pack("Z3", "a"));
  }

  @Test
  public void testPackWith_sb() {
    assertEquals("\0{", Packer.pack("s>", 123));
  }

  @Test
  public void testPackWith_sl() {
    assertEquals("{\0", Packer.pack("s<", 123));
  }

  @Test
  public void testPackWith_B() {
    assertEquals("abc\346\210\221",
        Packer.pack("B*", "011000010110001001100011111001101000100010010001"));
    assertEquals("", Packer.pack("B*", "!!!"));
    assertEquals("b", Packer.pack("B*", "0110001"));
  }

  @Test
  public void testPackWith_b() {
    assertEquals("abc\346\210\221",
        Packer.pack("b*", "100001100100011011000110011001110001000110001001"));
    assertEquals("F", Packer.pack("b*", "0110001"));
  }

  @Test
  public void testPackWith_H() {
    assertEquals("abc\346\210\221", Packer.pack("H*", "616263e68891"));
    assertEquals("", Packer.pack("H*", "!!!"));
    assertEquals("`", Packer.pack("H*", "6"));
  }

  @Test
  public void testPackWith_h() {
    assertEquals("abc\346\210\221", Packer.pack("h*", "1626366e8819"));
    assertEquals("\1", Packer.pack("h*", "1"));
  }

}
