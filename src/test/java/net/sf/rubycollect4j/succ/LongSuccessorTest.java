package net.sf.rubycollect4j.succ;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LongSuccessorTest {

  private LongSuccessor successor = LongSuccessor.getInstance();

  @Test
  public void testSingleton() {
    assertTrue(successor == LongSuccessor.getInstance());
  }

  @Test
  public void testSucc() {
    assertEquals(Long.valueOf(-1L), successor.succ(-2L));
    assertEquals(Long.valueOf(0L), successor.succ(-1L));
    assertEquals(Long.valueOf(1L), successor.succ(0L));
    assertEquals(Long.valueOf(2L), successor.succ(1L));
  }

  @Test
  public void testCompareTo() {
    assertEquals(-1, successor.compare(-2L, -1L));
    assertEquals(0, successor.compare(0L, 0L));
    assertEquals(1, successor.compare(2L, 1L));
  }

  @Test
  public void testToString() {
    assertEquals("LongSuccessor", successor.toString());
  }

}
