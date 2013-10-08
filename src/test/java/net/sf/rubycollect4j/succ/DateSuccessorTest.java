package net.sf.rubycollect4j.succ;

import static net.sf.rubycollect4j.RubyCollections.date;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DateSuccessorTest {

  private DateSuccessor successor = DateSuccessor.getInstance();

  @Test
  public void testSingleton() {
    assertTrue(successor == DateSuccessor.getInstance());
  }

  @Test
  public void testSucc() {
    assertEquals(date(2013, 1, 1), successor.succ(date(2012, 12, 31)));
    assertEquals(date(2013, 1, 2), successor.succ(date(2013, 1, 1)));
  }

  @Test
  public void testCompareTo() {
    assertEquals(-1, successor.compare(date(2012, 12, 31), date(2013, 1, 1)));
    assertEquals(0, successor.compare(date(2013, 1, 1), date(2013, 1, 1)));
    assertEquals(1, successor.compare(date(2013, 1, 2), date(2013, 1, 1)));
  }

  @Test
  public void testToString() {
    assertEquals("DateSuccessor", successor.toString());
  }

}
