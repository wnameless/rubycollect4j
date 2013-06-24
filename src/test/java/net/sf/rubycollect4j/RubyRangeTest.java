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

}
