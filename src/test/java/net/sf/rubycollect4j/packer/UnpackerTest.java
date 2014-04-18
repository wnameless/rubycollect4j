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
    assertEquals(ra((byte) 0, (byte) 26, (byte) 0, (byte) 26, null),
        Unpacker.unpack("c4c", "\0\32\0\32"));
    assertEquals(ra((byte) 97, null, null, null), Unpacker.unpack("c4", "a"));
    assertEquals(ra((byte) 97, (byte) 98, (byte) 99),
        Unpacker.unpack("c*", "abc"));
  }

  @Test
  public void testUnpackWith_Z() {
    assertEquals(ra("abc ", "abc "), Unpacker.unpack("Z*Z*", "abc \0abc \0"));
    assertEquals(ra("abc abc", ""), Unpacker.unpack("Z*Z*", "abc abc"));
  }

  @Test
  public void testUnpackWith_U() {
    assertEquals(ra(97, 98, 99, 25105), Unpacker.unpack("U*", "abc我"));
    assertEquals(ra(), Unpacker.unpack("U6", ""));
    assertEquals(ra(97), Unpacker.unpack("U1", "abc"));
  }

  @Test
  public void testUnpackWith_s() {
    assertEquals(ra((short) -16701, (short) -16445),
        Unpacker.unpack("ss", "\376\377"));
    assertEquals(ra((short) 25185, null), Unpacker.unpack("ss", "abc"));
    assertEquals(ra((short) 25185, (short) 25699),
        Unpacker.unpack("s*", "abcde"));
  }

  @Test
  public void testUnpackWith_l() {
    assertEquals(ra(-1077690685), Unpacker.unpack("l", "\376\377"));
    assertEquals(ra(1684234849, null), Unpacker.unpack("ll", "abcde"));
    assertEquals(ra(1684234849, 1751606885), Unpacker.unpack("l*", "abcdefghi"));
  }

  @Test
  public void testUnpackWith_q() {
    assertEquals(ra(-4628646244061561149L),
        Unpacker.unpack("q", "\376\377\376\377"));
    assertEquals(ra(7523094288207667809L, null),
        Unpacker.unpack("qq", "abcdefghi"));
    assertEquals(ra(7523094288207667809L, 8101815670912281193L),
        Unpacker.unpack("q*", "abcdefghijklmnopqr"));
  }

  @Test
  public void testUnpackWith_B() {
    assertEquals(ra("01100001"), Unpacker.unpack("B*", "a"));
    assertEquals(ra("0110"), Unpacker.unpack("B4", "a"));
    assertEquals(
        ra("11111111011000010110001001100011111001101000100010010001", ""),
        Unpacker.unpack("B100B", "\377abc我"));
    assertEquals(
        ra("11100110100010001001000111111111011000010110001001100011", ""),
        Unpacker.unpack("B100B", "我\377abc"));
  }

  @Test
  public void testUnpackWith_b() {
    assertEquals(ra("10000110"), Unpacker.unpack("b*", "a"));
    assertEquals(ra("1000"), Unpacker.unpack("b4", "a"));
    assertEquals(ra("10000110", ""), Unpacker.unpack("b100b", "a"));
    assertEquals(
        ra("11111111100001100100011011000110011001110001000110001001", ""),
        Unpacker.unpack("b100b", "\377abc我"));
    assertEquals(
        ra("01100111000100011000100111111111100001100100011011000110", ""),
        Unpacker.unpack("b100b", "我\377abc"));
  }

  @Test
  public void testUnpackWith_H() {
    assertEquals(ra("616263"), Unpacker.unpack("H*", "abc"));
    assertEquals(ra("6"), Unpacker.unpack("H1", "abc"));
    assertEquals(ra("6162"), Unpacker.unpack("H4", "abc"));
    assertEquals(ra("ff616263e68891", ""), Unpacker.unpack("H100H", "\377abc我"));
    assertEquals(ra("e68891ff616263", ""), Unpacker.unpack("H100H", "我\377abc"));
  }

  @Test
  public void testUnpackWith_h() {
    assertEquals(ra("162636"), Unpacker.unpack("h*", "abc"));
    assertEquals(ra("1"), Unpacker.unpack("h1", "abc"));
    assertEquals(ra("1626"), Unpacker.unpack("h4", "abc"));
    assertEquals(ra("ff1626366e8819", ""), Unpacker.unpack("h100h", "\377abc我"));
    assertEquals(ra("6e8819ff162636", ""), Unpacker.unpack("h100h", "我\377abc"));
  }

}
