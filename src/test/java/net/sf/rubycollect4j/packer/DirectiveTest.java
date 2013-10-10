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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.ByteOrder;

import org.junit.Before;
import org.junit.Test;

public class DirectiveTest {

  private static final boolean IS_BIG_ENDIAN =
      ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;

  private byte[] ABC;

  @Before
  public void setUp() throws Exception {
    ABC = new byte[] { (byte) 65, (byte) 66, (byte) 67 };
  }

  private byte[] int4407873() {
    return ByteUtil.toByteArray(4407873);
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
  public void testPack() {
    assertEquals(IS_BIG_ENDIAN ? "C" : "A", Directive.c.pack(ABC));

    assertEquals(IS_BIG_ENDIAN ? "BC" : "AB", Directive.s.pack(ABC));
    assertEquals("BA", Directive.sb.pack(int4407873()));
    assertEquals("AB", Directive.sl.pack(int4407873()));

    assertEquals(IS_BIG_ENDIAN ? "\\x00ABC" : "ABC\\x00", Directive.l.pack(ABC));
    assertEquals("\\x00CBA", Directive.lb.pack(int4407873()));
    assertEquals("ABC\\x00", Directive.ll.pack(int4407873()));

    assertEquals(IS_BIG_ENDIAN ? "\\x00\\x00\\x00\\x00\\x00ABC"
        : "ABC\\x00\\x00\\x00\\x00\\x00", Directive.q.pack(ABC));
    assertEquals("\\x00\\x00\\x00\\x00\\x00CBA",
        Directive.qb.pack(int4407873()));
    assertEquals("ABC\\x00\\x00\\x00\\x00\\x00",
        Directive.ql.pack(int4407873()));

    assertEquals(IS_BIG_ENDIAN ? "\\x00\\x00\\x00\\x00\\x00ABC"
        : "ABC\\x00\\x00\\x00\\x00\\x00", Directive.D.pack(ABC));
    assertEquals(IS_BIG_ENDIAN ? "\\x00\\x00\\x00\\x00\\x00ABC"
        : "ABC\\x00\\x00\\x00\\x00\\x00", Directive.d.pack(ABC));
    assertEquals("ABC\\x00\\x00\\x00\\x00\\x00", Directive.E.pack(int4407873()));
    assertEquals("\\x00\\x00\\x00\\x00\\x00CBA", Directive.G.pack(int4407873()));

    assertEquals(IS_BIG_ENDIAN ? "\\x00ABC" : "ABC\\x00", Directive.F.pack(ABC));
    assertEquals(IS_BIG_ENDIAN ? "\\x00ABC" : "ABC\\x00", Directive.f.pack(ABC));
    assertEquals("ABC\\x00", Directive.e.pack(int4407873()));
    assertEquals("\\x00CBA", Directive.g.pack(int4407873()));

    assertEquals("〹", Directive.U.pack(ByteUtil.toByteArray(12345)));
    assertEquals("ABC", Directive.A.pack(ABC));
    assertEquals("ABC", Directive.a.pack(ABC));
    assertEquals("ABC", Directive.Z.pack(ABC));
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

    assertEquals((int) 123, Directive.l.cast(123));
    assertEquals((int) 0, Directive.l.cast(false));
    assertEquals((int) 1, Directive.l.cast(true));
    assertEquals((int) 123, Directive.l.cast((char) 123));
    assertEquals("123", Directive.l.cast("123"));

    assertEquals((int) 123, Directive.lb.cast(123));
    assertEquals((int) 0, Directive.lb.cast(false));
    assertEquals((int) 1, Directive.lb.cast(true));
    assertEquals((int) 123, Directive.lb.cast((char) 123));
    assertEquals("123", Directive.lb.cast("123"));

    assertEquals((int) 123, Directive.ll.cast(123));
    assertEquals((int) 0, Directive.ll.cast(false));
    assertEquals((int) 1, Directive.ll.cast(true));
    assertEquals((int) 123, Directive.ll.cast((char) 123));
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

    assertEquals((int) 123, Directive.U.cast(123));
    assertEquals((int) 0, Directive.U.cast(false));
    assertEquals((int) 1, Directive.U.cast(true));
    assertEquals((int) 123, Directive.U.cast((char) 123));
    assertEquals("123", Directive.U.cast("123"));
  }

}
