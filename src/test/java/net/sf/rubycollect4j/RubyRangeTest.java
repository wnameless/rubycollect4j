package net.sf.rubycollect4j;

import org.junit.Test;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.rr;
import static org.junit.Assert.assertEquals;

public class RubyRangeTest {

  @Test
  public void testRangeWithInteger() {
    assertEquals(ra(-1, 0, 1, 2, 3, 4, 5, 6), rr(-1, 6).toA());
  }

  @Test
  public void testRangeWithString() {
    assertEquals(ra("abcd", "abce"), rr("abcd", "abce").toA());
    assertEquals(ra("THX1138", "THX1139"), rr("THX1138", "THX1139").toA());
    assertEquals(ra("<<koala>>", "<<koalb>>"), rr("<<koala>>", "<<koalb>>")
        .toA());
    assertEquals(ra("1999zzz", "2000aaa"), rr("1999zzz", "2000aaa").toA());
    assertEquals(ra("zzz9999", "aaaa0000"), rr("zzz9999", "aaaa0000").toA());
    assertEquals(ra("***", "**+"), rr("***", "**+").toA());
    assertEquals(ra("a", "b", "c", "d", "e"), rr("a", "e").toA());
    assertEquals(ra("ay", "az", "ba"), rr("ay", "ba").toA());
    assertEquals(ra("aY", "aZ", "bA"), rr("aY", "bA").toA());
    assertEquals(ra("999--", "1000--", "1001--"), rr("999--", "1001--").toA());
    assertEquals(ra("999", "1000", "1001"), rr("999", "1001").toA());
    assertEquals(ra("1.2", "1.3", "1.4", "1.5"), rr("1.2", "1.5").toA());
    assertEquals(ra("1.48", "1.49", "1.50"), rr("1.48", "1.5").toA());
    assertEquals(ra(), rr("zzz", "aaa").toA());
  }

  @Test
  public void testRangeWithLong() {
    assertEquals(ra(-1L, 0L, 1L, 2L, 3L, 4L, 5L, 6L), rr(-1L, 6L).toA());
    assertEquals(ra(), rr(-1L, -2L).toA());
  }

  @Test
  public void testRangeWithDouble() {
    assertEquals(ra(1.08, 1.09, 1.10), rr(1.08, 1.1).toA());
    assertEquals(ra(), rr(-1.0, -2.0).toA());
  }

}
