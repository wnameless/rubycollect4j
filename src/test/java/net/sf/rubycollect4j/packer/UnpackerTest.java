/**
 *
 * @author Wei-Ming Wu
 *
 *
 * Copyright 2014 Wei-Ming Wu
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

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.Test;

public class UnpackerTest {

  @Test
  public void testPrivateConstructor() throws Exception {
    Constructor<Unpacker> c = Unpacker.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c.getModifiers()));
    c.setAccessible(true);
    c.newInstance();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUnpackWithInvalidDirective() {
    Unpacker.unpack("Y1", "");
  }

  @Test
  public void testUnpackWith_a() {
    assertEquals(ra("abc", " \0\0"), Unpacker.unpack("a3a3", "abc \0\0"));
  }

  @Test
  public void testUnpackWith_AZ() {
    assertEquals(ra("abc", "abc "), Unpacker.unpack("A6Z6", "abc \0\0abc \0\0"));
  }

  @Test
  public void testUnpackWith_c() {
    assertEquals(ra("0", "26", "0", "26", null),
        Unpacker.unpack("c4c", "\0\32\0\32"));
  }

  @Test
  public void testUnpackWith_Z() {
    assertEquals(ra("abc ", "abc "), Unpacker.unpack("Z*Z*", "abc \0abc \0"));
  }

  @Test
  public void testUnpackWith_U() {
    assertEquals(ra("97", "98", "99", "25105"), Unpacker.unpack("U*", "abc我"));
  }

  @Test
  public void testUnpackWith_s() {
    assertEquals(ra("-15426", "-15425"), Unpacker.unpack("ss", "\376\377"));
  }

  @Test
  public void testUnpackWith_l() {
    assertEquals(ra("-1010908225"), Unpacker.unpack("l", "\376\377"));
  }

  @Test
  public void testUnpackWith_q() {
    assertEquals(ra("-4341817762348350529"),
        Unpacker.unpack("q", "\376\377\376\377"));
  }

}
