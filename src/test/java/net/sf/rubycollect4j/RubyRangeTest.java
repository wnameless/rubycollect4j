package net.sf.rubycollect4j;

import org.junit.Test;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyRange.range;
import static org.junit.Assert.assertEquals;

public class RubyRangeTest {

  @Test
  public void testRangeWithInteger() {
    assertEquals(ra(1, 2, 3, 4, 5, 6), range(1, 6).toA());
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
  }

}
