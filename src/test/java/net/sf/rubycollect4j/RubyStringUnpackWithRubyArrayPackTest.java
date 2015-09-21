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
package net.sf.rubycollect4j;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.rs;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RubyStringUnpackWithRubyArrayPackTest {

  @Test
  public void testDirective_c() {
    assertEquals(ra((byte) 97, (byte) 98, (byte) 99), rs("abc").unpack("c*"));
    assertEquals("abc", rs("abc").unpack("c*").pack("c*"));
    assertEquals(ra((byte) 97, (byte) 98, (byte) 99, null),
        rs("abc").unpack("c3c"));
  }

  @Test
  public void testDirective_s() {
    assertEquals(ra((short) 25185), rs("abc").unpack("s*"));
    assertEquals("ab", rs("abc").unpack("s*").pack("s*"));
    assertEquals(ra((short) 25185), rs("abc").unpack("s<*"));
    assertEquals("ab", rs("abc").unpack("s<*").pack("s<*"));
    assertEquals("ba", rs("abc").unpack("s<*").pack("s>*"));
    assertEquals(ra((short) 24930), rs("abc").unpack("s>*"));
    assertEquals("ab", rs("abc").unpack("s>*").pack("s>*"));
    assertEquals("ba", rs("abc").unpack("s>*").pack("s<*"));
    assertEquals(ra((short) 25185, null), rs("abc").unpack("ss"));
  }

  @Test
  public void testDirective_l() {
    assertEquals(ra(1684234849), rs("abcdf").unpack("l*"));
    assertEquals("abcd", rs("abcdf").unpack("l*").pack("l*"));
    assertEquals(ra(1684234849), rs("abcdf").unpack("l<*"));
    assertEquals("abcd", rs("abcdf").unpack("l<*").pack("l<*"));
    assertEquals("dcba", rs("abcdf").unpack("l<*").pack("l>*"));
    assertEquals(ra(1633837924), rs("abcdf").unpack("l>*"));
    assertEquals("abcd", rs("abcdf").unpack("l>*").pack("l>*"));
    assertEquals("dcba", rs("abcdf").unpack("l>*").pack("l<*"));
    assertEquals(ra(1684234849, null), rs("abcd").unpack("ll"));
  }

  @Test
  public void testDirective_q() {
    assertEquals(ra(7523094288207667809L), rs("abcdefghi").unpack("q*"));
    assertEquals("abcdefgh", rs("abcdefghi").unpack("q*").pack("q*"));
    assertEquals(ra(7523094288207667809L), rs("abcdefghi").unpack("q<*"));
    assertEquals("abcdefgh", rs("abcdefghi").unpack("q<*").pack("q<*"));
    assertEquals("hgfedcba", rs("abcdefghi").unpack("q<*").pack("q>*"));
    assertEquals(ra(7017280452245743464L), rs("abcdefghi").unpack("q>*"));
    assertEquals("abcdefgh", rs("abcdefghi").unpack("q>*").pack("q>*"));
    assertEquals("hgfedcba", rs("abcdefghi").unpack("q>*").pack("q<*"));
    assertEquals(ra(7523094288207667809L, null), rs("abcdefgh").unpack("qq"));
  }

  @Test
  public void testDirective_F_f() {
    assertEquals(Float.valueOf("1.6777999E22").toString(),
        rs("abcde").unpack("f*").get(0).toString());
    assertEquals("abcd", rs("abcde").unpack("f*").pack("f*"));
    assertEquals(Float.valueOf("1.6777999E22").toString(),
        rs("abcde").unpack("F*").get(0).toString());
    assertEquals("abcd", rs("abcde").unpack("F*").pack("F*"));
    assertEquals(ra(Float.valueOf("1.6777999E22"), null),
        rs("abcd").unpack("FF"));
  }

  @Test
  public void testDirective_e() {
    assertEquals(Float.valueOf("1.6777999E22").toString(),
        rs("abcde").unpack("e*").get(0).toString());
    assertEquals("abcd", rs("abcde").unpack("e*").pack("e*"));
    assertEquals(ra(Float.valueOf("1.6777999E22"), null),
        rs("abcd").unpack("ee"));
  }

  @Test
  public void testDirective_g() {
    assertEquals(Float.valueOf("2.6100788E20").toString(),
        rs("abcde").unpack("g*").get(0).toString());
    assertEquals("abcd", rs("abcde").unpack("g*").pack("g*"));
    assertEquals(ra(Float.valueOf("2.6100788E20"), null),
        rs("abcd").unpack("gg"));
  }

  @Test
  public void testDirective_D_d() {
    assertEquals(Double.valueOf("8.540883223036124E194").toString(),
        rs("abcdefghi").unpack("D*").get(0).toString());
    assertEquals("abcdefgh", rs("abcdefghi").unpack("D*").pack("D*"));
    assertEquals(Double.valueOf("8.540883223036124E194").toString(),
        rs("abcdefghi").unpack("d*").get(0).toString());
    assertEquals("abcdefgh", rs("abcdefghi").unpack("d*").pack("d*"));
    assertEquals(ra(Double.valueOf("8.540883223036124E194"), null),
        rs("abcdefgh").unpack("dd"));
  }

  @Test
  public void testDirective_E() {
    assertEquals(Double.valueOf("8.540883223036124E194").toString(),
        rs("abcdefghi").unpack("E*").get(0).toString());
    assertEquals("abcdefgh", rs("abcdefghi").unpack("E*").pack("E*"));
    assertEquals(ra(Double.valueOf("8.540883223036124E194"), null),
        rs("abcdefgh").unpack("EE"));
  }

  @Test
  public void testDirective_G() {
    assertEquals(Double.valueOf("1.2926117907728089E161").toString(),
        rs("abcdefghi").unpack("G*").get(0).toString());
    assertEquals("abcdefgh", rs("abcdefghi").unpack("G*").pack("G*"));
    assertEquals(ra(Double.valueOf("1.2926117907728089E161"), null),
        rs("abcdefgh").unpack("GG"));
  }

  @Test
  public void testDirective_U() {
    assertEquals(ra(97, 98, 99, 19968, 20108, 19977),
        rs("abc一二三").unpack("U*"));
    assertEquals("abc一二三", rs("abc一二三").unpack("U*").pack("U*"));
    assertEquals(ra(Double.valueOf("1.2926117907728089E161"), null),
        rs("abcdefgh").unpack("GG"));
    assertEquals(ra(97, 98, 99, 19968, 20108, 19977),
        rs("abc一二三").unpack("U*U"));
  }

  @Test
  public void testDirective_B() {
    assertEquals(ra("011000010110001001100011111001101000100010010001"),
        rs("abc我").unpack("B*"));
    assertEquals("abc\346\210\221", rs("abc我").unpack("B*").pack("B*"));
    assertEquals(ra("01", "011", "0110", "11100", "100010", "1001000", ""),
        rs("abc我").unpack("B2B3B4B5B6B7B*"));
  }

  @Test
  public void testDirective_b() {
    assertEquals(ra("100001100100011011000110011001110001000110001001"),
        rs("abc我").unpack("b*"));
    assertEquals("abc\346\210\221", rs("abc我").unpack("b*").pack("b*"));
    assertEquals(ra("10", "010", "1100", "01100", "000100", "1000100", ""),
        rs("abc我").unpack("b2b3b4b5b6b7b*"));
  }

  @Test
  public void testDirective_H() {
    assertEquals(ra("616263e68891"), rs("abc我").unpack("H*"));
    assertEquals("abc\346\210\221", rs("abc我").unpack("H*").pack("H*"));
    assertEquals(ra("46", "45", ""), rs("FE").unpack("H2H3H*"));
  }

  @Test
  public void testDirective_h() {
    assertEquals(ra("1626366e8819"), rs("abc我").unpack("h*"));
    assertEquals("abc\346\210\221", rs("abc我").unpack("h*").pack("h*"));
    assertEquals(ra("64", "54", ""), rs("FE").unpack("h2h3h*"));
  }

  @Test
  public void testDirective_H_c() {
    assertEquals(ra((byte) 0x00, (byte) 0xff, (byte) 0xa0),
        rs(ra("00FFa0").pack("H*")).unpack("c*"));
  }

}
