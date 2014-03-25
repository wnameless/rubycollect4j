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
    Packer.pack("c1", new ArrayList<Object>());
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
  public void testPack() {
    assertEquals("a  b  c  ", Packer.pack("A3A3A3", "a", "b", "c"));
    assertEquals("a\\x00\\x00b\\x00\\x00c\\x00\\x00",
        Packer.pack("a3a3a3", "a", "b", "c"));
    assertEquals("ABC", Packer.pack("ccc", 65, 66, 67));
    assertEquals("ABC", Packer.pack("c3", 65, 66, 67));
    assertEquals("\\x00{", Packer.pack("s>", 123));
    assertEquals("{\\x00", Packer.pack("s<", 123));
    assertEquals("a", Packer.pack("A*", "a"));
    assertEquals("ab", Packer.pack("A1A1", "abc", "bc"));
    assertEquals("a\\x00", Packer.pack("Z*", "a"));
    assertEquals("a\\x00\\x00", Packer.pack("Z3", "a"));
  }

}
