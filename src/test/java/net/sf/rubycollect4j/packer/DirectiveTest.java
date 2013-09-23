package net.sf.rubycollect4j.packer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.ByteOrder;

import org.junit.Test;

public class DirectiveTest {

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
    assertEquals("A", Directive.c.pack(new byte[] { (byte) 65 }));
    assertEquals(ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN ? "\\x00A"
        : "A\\x00", Directive.s.pack(new byte[] { (byte) 65 }));
    assertEquals(ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN
        ? "\\x00\\x00\\x00A" : "A\\x00\\x00\\x00",
        Directive.l.pack(new byte[] { (byte) 65 }));
    assertEquals(ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN
        ? "\\x00\\x00\\x00\\x00\\x00\\x00\\x00A"
        : "A\\x00\\x00\\x00\\x00\\x00\\x00\\x00",
        Directive.q.pack(new byte[] { (byte) 65 }));
    assertEquals(ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN
        ? "\\x00\\x00\\x00\\x00\\x00\\x00\\x00A"
        : "A\\x00\\x00\\x00\\x00\\x00\\x00\\x00",
        Directive.D.pack(new byte[] { (byte) 65 }));
    assertEquals(ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN
        ? "\\x00\\x00\\x00\\x00\\x00\\x00\\x00A"
        : "A\\x00\\x00\\x00\\x00\\x00\\x00\\x00",
        Directive.d.pack(new byte[] { (byte) 65 }));
    assertEquals(ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN
        ? "\\x00\\x00\\x00A" : "A\\x00\\x00\\x00",
        Directive.F.pack(new byte[] { (byte) 65 }));
    assertEquals(ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN
        ? "\\x00\\x00\\x00A" : "A\\x00\\x00\\x00",
        Directive.f.pack(new byte[] { (byte) 65 }));
    assertEquals("〹", Directive.U.pack(ByteUtil.toByteArray(12345)));
    assertEquals("A", Directive.A.pack(ByteUtil.toByteArray("A")));
    assertEquals("A", Directive.a.pack(ByteUtil.toByteArray("A")));
    assertEquals("A", Directive.Z.pack(ByteUtil.toByteArray("A")));
  }

}
