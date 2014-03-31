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
  }

  @Test
  public void testDirective_F_f() {
    assertEquals(Float.valueOf("1.6777999E22").toString(),
        rs("abcde").unpack("f*").get(0).toString());
    assertEquals("abcd", rs("abcde").unpack("f*").pack("f*"));
    assertEquals(Float.valueOf("1.6777999E22").toString(),
        rs("abcde").unpack("F*").get(0).toString());
    assertEquals("abcd", rs("abcde").unpack("F*").pack("F*"));
  }

  @Test
  public void testDirective_e() {
    assertEquals(Float.valueOf("1.6777999E22").toString(),
        rs("abcde").unpack("e*").get(0).toString());
    assertEquals("abcd", rs("abcde").unpack("e*").pack("e*"));
  }

  @Test
  public void testDirective_g() {
    assertEquals(Float.valueOf("2.6100788E20").toString(),
        rs("abcde").unpack("g*").get(0).toString());
    assertEquals("abcd", rs("abcde").unpack("g*").pack("g*"));
  }

  @Test
  public void testDirective_D_d() {
    assertEquals(Double.valueOf("8.540883223036124E194").toString(),
        rs("abcdefghi").unpack("D*").get(0).toString());
    assertEquals("abcdefgh", rs("abcdefghi").unpack("D*").pack("D*"));
    assertEquals(Double.valueOf("8.540883223036124E194").toString(),
        rs("abcdefghi").unpack("d*").get(0).toString());
    assertEquals("abcdefgh", rs("abcdefghi").unpack("d*").pack("d*"));
  }

  @Test
  public void testDirective_E() {
    assertEquals(Double.valueOf("8.540883223036124E194").toString(),
        rs("abcdefghi").unpack("E*").get(0).toString());
    assertEquals("abcdefgh", rs("abcdefghi").unpack("E*").pack("E*"));
  }

  @Test
  public void testDirective_G() {
    assertEquals(Double.valueOf("1.2926117907728089E161").toString(),
        rs("abcdefghi").unpack("G*").get(0).toString());
    assertEquals("abcdefgh", rs("abcdefghi").unpack("G*").pack("G*"));
  }

  @Test
  public void testDirective_U() {
    assertEquals(ra(97, 98, 99, 19968, 20108, 19977), rs("abc一二三").unpack("U*"));
    assertEquals("abc一二三", rs("abc一二三").unpack("U*").pack("U*"));
  }

  @Test
  public void testDirective_B() {
    assertEquals(ra("011000010110001001100011111001101000100010010001"),
        rs("abc我").unpack("B*"));
    assertEquals("abc我", rs("abc我").unpack("B*").pack("B*"));
  }

  @Test
  public void testDirective_b() {
    assertEquals(ra("100001100100011011000110011001110001000110001001"),
        rs("abc我").unpack("b*"));
    assertEquals(rs("abc我"), rs(rs("abc我").unpack("b*").pack("b*")));
  }

}
