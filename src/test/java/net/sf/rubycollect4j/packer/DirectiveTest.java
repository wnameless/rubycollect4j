/*
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

import static java.nio.ByteOrder.BIG_ENDIAN;
import static java.nio.ByteOrder.nativeOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import net.sf.rubycollect4j.util.ByteUtil;

public class DirectiveTest {

  static final boolean IS_BIG_ENDIAN = nativeOrder() == BIG_ENDIAN;

  byte[] ABC;
  byte[] ABCDEFGH;

  @Before
  public void setUp() throws Exception {
    ABC = new byte[] { (byte) 65, (byte) 66, (byte) 67 };
    ABCDEFGH = new byte[] { (byte) 65, (byte) 66, (byte) 67, (byte) 68,
        (byte) 69, (byte) 70, (byte) 71, (byte) 72 };
  }

  private byte[] int4407873() {
    return ByteUtil.toByteArray(4407873, nativeOrder());
  }

  @Test
  public void testVerify() {
    assertTrue(Directive.verify("c*"));
    assertFalse(Directive.verify("3c"));
  }

  @Test
  public void testIsWidthAdjustable() {
    assertFalse(Directive.c.isWidthAdjustable());
    assertTrue(Directive.a.isWidthAdjustable());
  }

  @Test
  public void testPackWithList() {
    assertEquals(IS_BIG_ENDIAN ? "C" : "A",
        Directive.c.pack(ByteUtil.toList(ABC)));
  }

  @Test
  public void testPack() {
    assertEquals(IS_BIG_ENDIAN ? "C" : "A", Directive.c.pack(ABC));

    assertEquals(IS_BIG_ENDIAN ? "BC" : "AB", Directive.s.pack(ABC));
    assertEquals("BA", Directive.sb.pack(int4407873()));
    assertEquals("AB", Directive.sl.pack(int4407873()));

    assertEquals(IS_BIG_ENDIAN ? "\0ABC" : "ABC\0", Directive.l.pack(ABC));
    assertEquals("\0CBA", Directive.lb.pack(int4407873()));
    assertEquals("ABC\0", Directive.ll.pack(int4407873()));

    assertEquals(IS_BIG_ENDIAN ? "\0\0\0\0\0ABC" : "ABC\0\0\0\0\0",
        Directive.q.pack(ABC));
    assertEquals("\0\0\0\0\0CBA", Directive.qb.pack(int4407873()));
    assertEquals("ABC\0\0\0\0\0", Directive.ql.pack(int4407873()));

    assertEquals(IS_BIG_ENDIAN ? "\0\0\0\0\0ABC" : "ABC\0\0\0\0\0",
        Directive.D.pack(ABC));
    assertEquals(IS_BIG_ENDIAN ? "\0\0\0\0\0ABC" : "ABC\0\0\0\0\0",
        Directive.d.pack(ABC));
    assertEquals("ABC\0\0\0\0\0", Directive.E.pack(int4407873()));
    assertEquals("\0\0\0\0\0CBA", Directive.G.pack(int4407873()));

    assertEquals(IS_BIG_ENDIAN ? "\0ABC" : "ABC\0", Directive.F.pack(ABC));
    assertEquals(IS_BIG_ENDIAN ? "\0ABC" : "ABC\0", Directive.f.pack(ABC));
    assertEquals("ABC\0", Directive.e.pack(int4407873()));
    assertEquals("\0CBA", Directive.g.pack(int4407873()));

    assertEquals("〹",
        Directive.U.pack(ByteUtil.toByteArray(12345, nativeOrder())));
    assertEquals("ABC", Directive.A.pack(ABC));
    assertEquals("ABC", Directive.a.pack(ABC));
    assertEquals("ABC", Directive.Z.pack(ABC));

    assertEquals("\346\210\221", Directive.B.pack("我".getBytes()));
    assertEquals("\147\21\211", Directive.b.pack("我".getBytes()));

    assertEquals("\346\210\221", Directive.H.pack("我".getBytes()));
    assertEquals("\156\210\31", Directive.h.pack("我".getBytes()));
  }

  @Test
  public void testUnpackWithList() {
    assertEquals((byte) 65, Directive.a.unpack(ByteUtil.toList(ABCDEFGH)));
  }

  @Test
  public void testUnpack() {
    assertEquals((byte) 65, Directive.a.unpack(ABCDEFGH));
    assertEquals((byte) -26, Directive.a.unpack("我".getBytes()));
    assertEquals(25105, Directive.U.unpack("我".getBytes()));
    assertEquals((short) 16961, Directive.s.unpack(ABCDEFGH));
    assertEquals((short) 16961, Directive.sl.unpack(ABCDEFGH));
    assertEquals((short) 16706, Directive.sb.unpack(ABCDEFGH));
    assertEquals(1145258561, Directive.l.unpack(ABCDEFGH));
    assertEquals(1145258561, Directive.ll.unpack(ABCDEFGH));
    assertEquals(1094861636, Directive.lb.unpack(ABCDEFGH));
    assertEquals(5208208757389214273L, Directive.q.unpack(ABCDEFGH));
    assertEquals(5208208757389214273L, Directive.ql.unpack(ABCDEFGH));
    assertEquals(4702394921427289928L, Directive.qb.unpack(ABCDEFGH));
    assertEquals(1.5839800103804824E40, Directive.D.unpack(ABCDEFGH));
    assertEquals(1.5839800103804824E40, Directive.d.unpack(ABCDEFGH));
    assertEquals(1.5839800103804824E40, Directive.E.unpack(ABCDEFGH));
    assertEquals(2393736.541207228, Directive.G.unpack(ABCDEFGH));
    assertEquals(781.0352f, Directive.F.unpack(ABCDEFGH));
    assertEquals(781.0352f, Directive.f.unpack(ABCDEFGH));
    assertEquals(781.0352f, Directive.e.unpack(ABCDEFGH));
    assertEquals(12.141422f, Directive.g.unpack(ABCDEFGH));
    assertEquals("111001101000100010010001",
        Directive.B.unpack("我".getBytes()));
    assertEquals("011001110001000110001001",
        Directive.b.unpack("我".getBytes()));
    assertEquals("e68891", Directive.H.unpack("我".getBytes()));
    assertEquals("6e8819", Directive.h.unpack("我".getBytes()));
  }

  @Test
  public void testCast() {
    assertEquals((byte) 123, Directive.c.cast(123));
    assertEquals((byte) 0, Directive.c.cast(false));
    assertEquals((byte) 1, Directive.c.cast(true));
    assertEquals((byte) 123, Directive.c.cast((char) 123));
    assertEquals("123", Directive.c.cast("123"));

    assertEquals((short) 123, Directive.s.cast(123));
    assertEquals((short) 0, Directive.s.cast(false));
    assertEquals((short) 1, Directive.s.cast(true));
    assertEquals((short) 123, Directive.s.cast((char) 123));
    assertEquals("123", Directive.s.cast("123"));

    assertEquals((short) 123, Directive.sb.cast(123));
    assertEquals((short) 0, Directive.sb.cast(false));
    assertEquals((short) 1, Directive.sb.cast(true));
    assertEquals((short) 123, Directive.sb.cast((char) 123));
    assertEquals("123", Directive.sb.cast("123"));

    assertEquals((short) 123, Directive.sl.cast(123));
    assertEquals((short) 0, Directive.sl.cast(false));
    assertEquals((short) 1, Directive.sl.cast(true));
    assertEquals((short) 123, Directive.sl.cast((char) 123));
    assertEquals("123", Directive.sl.cast("123"));

    assertEquals(123, Directive.l.cast(123));
    assertEquals(0, Directive.l.cast(false));
    assertEquals(1, Directive.l.cast(true));
    assertEquals(123, Directive.l.cast((char) 123));
    assertEquals("123", Directive.l.cast("123"));

    assertEquals(123, Directive.lb.cast(123));
    assertEquals(0, Directive.lb.cast(false));
    assertEquals(1, Directive.lb.cast(true));
    assertEquals(123, Directive.lb.cast((char) 123));
    assertEquals("123", Directive.lb.cast("123"));

    assertEquals(123, Directive.ll.cast(123));
    assertEquals(0, Directive.ll.cast(false));
    assertEquals(1, Directive.ll.cast(true));
    assertEquals(123, Directive.ll.cast((char) 123));
    assertEquals("123", Directive.ll.cast("123"));

    assertEquals((long) 123, Directive.q.cast(123));
    assertEquals((long) 0, Directive.q.cast(false));
    assertEquals((long) 1, Directive.q.cast(true));
    assertEquals((long) 123, Directive.q.cast((char) 123));
    assertEquals("123", Directive.q.cast("123"));

    assertEquals((long) 123, Directive.qb.cast(123));
    assertEquals((long) 0, Directive.qb.cast(false));
    assertEquals((long) 1, Directive.qb.cast(true));
    assertEquals((long) 123, Directive.qb.cast((char) 123));
    assertEquals("123", Directive.qb.cast("123"));

    assertEquals((long) 123, Directive.ql.cast(123));
    assertEquals((long) 0, Directive.ql.cast(false));
    assertEquals((long) 1, Directive.ql.cast(true));
    assertEquals((long) 123, Directive.ql.cast((char) 123));
    assertEquals("123", Directive.ql.cast("123"));

    assertEquals((double) 123, Directive.D.cast(123));
    assertEquals((double) 0, Directive.D.cast(false));
    assertEquals((double) 1, Directive.D.cast(true));
    assertEquals((double) 123, Directive.D.cast((char) 123));
    assertEquals("123", Directive.D.cast("123"));

    assertEquals((double) 123, Directive.d.cast(123));
    assertEquals((double) 0, Directive.d.cast(false));
    assertEquals((double) 1, Directive.d.cast(true));
    assertEquals((double) 123, Directive.d.cast((char) 123));
    assertEquals("123", Directive.d.cast("123"));

    assertEquals((double) 123, Directive.E.cast(123));
    assertEquals((double) 0, Directive.E.cast(false));
    assertEquals((double) 1, Directive.E.cast(true));
    assertEquals((double) 123, Directive.E.cast((char) 123));
    assertEquals("123", Directive.E.cast("123"));

    assertEquals((double) 123, Directive.G.cast(123));
    assertEquals((double) 0, Directive.G.cast(false));
    assertEquals((double) 1, Directive.G.cast(true));
    assertEquals((double) 123, Directive.G.cast((char) 123));
    assertEquals("123", Directive.G.cast("123"));

    assertEquals((float) 123, Directive.F.cast(123));
    assertEquals((float) 0, Directive.F.cast(false));
    assertEquals((float) 1, Directive.F.cast(true));
    assertEquals((float) 123, Directive.F.cast((char) 123));
    assertEquals("123", Directive.F.cast("123"));

    assertEquals((float) 123, Directive.f.cast(123));
    assertEquals((float) 0, Directive.f.cast(false));
    assertEquals((float) 1, Directive.f.cast(true));
    assertEquals((float) 123, Directive.f.cast((char) 123));
    assertEquals("123", Directive.f.cast("123"));

    assertEquals((float) 123, Directive.e.cast(123));
    assertEquals((float) 0, Directive.e.cast(false));
    assertEquals((float) 1, Directive.e.cast(true));
    assertEquals((float) 123, Directive.e.cast((char) 123));
    assertEquals("123", Directive.e.cast("123"));

    assertEquals((float) 123, Directive.g.cast(123));
    assertEquals((float) 0, Directive.g.cast(false));
    assertEquals((float) 1, Directive.g.cast(true));
    assertEquals((float) 123, Directive.g.cast((char) 123));
    assertEquals("123", Directive.g.cast("123"));

    assertEquals(123, Directive.U.cast(123));
    assertEquals(0, Directive.U.cast(false));
    assertEquals(1, Directive.U.cast(true));
    assertEquals(123, Directive.U.cast((char) 123));
    assertEquals("123", Directive.U.cast("123"));
  }

}
