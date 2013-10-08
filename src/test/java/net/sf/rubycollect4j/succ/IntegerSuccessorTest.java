package net.sf.rubycollect4j.succ;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IntegerSuccessorTest {

  private IntegerSuccessor successor = IntegerSuccessor.getInstance();

  @Test
  public void testSingleton() {
    assertTrue(successor == IntegerSuccessor.getInstance());
  }

  @Test
  public void testSucc() {
    assertEquals(Integer.valueOf(-1), successor.succ(-2));
    assertEquals(Integer.valueOf(0), successor.succ(-1));
    assertEquals(Integer.valueOf(1), successor.succ(0));
    assertEquals(Integer.valueOf(2), successor.succ(1));
  }

  @Test
  public void testCompareTo() {
    assertEquals(-1, successor.compare(-2, -1));
    assertEquals(0, successor.compare(0, 0));
    assertEquals(1, successor.compare(2, 1));
  }

  @Test
  public void testToString() {
    assertEquals("IntegerSuccessor", successor.toString());
  }

}
