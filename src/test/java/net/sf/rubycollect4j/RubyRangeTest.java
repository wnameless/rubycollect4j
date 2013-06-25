package net.sf.rubycollect4j;

import org.junit.Test;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyRange.range;
import static org.junit.Assert.assertEquals;

public class RubyRangeTest {

  @Test
  public void testRangeWithInteger() {
    assertEquals(ra(-1, 0, 1, 2, 3, 4, 5, 6), range(-1, 6).toA());
  }

  @Test
  public void testRangeWithString() {
    assertEquals(ra("a", "b", "c", "d", "e"), range("a", "e").toA());
    assertEquals(ra("ay", "az", "ba"), range("ay", "ba").toA());
    assertEquals(ra("aY", "aZ", "bA"), range("aY", "bA").toA());
    assertEquals(ra(), range("999--", "1001--").toA());
    assertEquals(ra("999", "1000", "1001"), range("999", "1001").toA());
    assertEquals(ra("1.2", "1.3", "1.4", "1.5"), range("1.2", "1.5").toA());
    assertEquals(ra("1.48", "1.49", "1.50"), range("1.48", "1.5").toA());
    assertEquals(ra("***", "**+"), range("***", "**+").toA());
    assertEquals(ra(), range("zzz", "aaa").toA());
  }

  @Test
  public void testRangeWithLong() {
    assertEquals(ra(-1L, 0L, 1L, 2L, 3L, 4L, 5L, 6L), range(-1L, 6L).toA());
    assertEquals(ra(), range(-1L, -2L).toA());
  }

  @Test
  public void testRangeWithDouble() {
    assertEquals(ra(1.08, 1.09, 1.10), range(1.08, 1.1).toA());
    assertEquals(ra(), range(-1.0, -2.0).toA());
  }

}
