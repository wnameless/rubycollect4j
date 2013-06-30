package net.sf.rubycollect4j.succ;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class DoubleSuccessorTest {

  private DoubleSuccessor successor;

  @Before
  public void setUp() throws Exception {
    successor = new DoubleSuccessor(2);
  }

  @Test
  public void testConstructor() {
    assertTrue(successor instanceof DoubleSuccessor);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException() {
    new DoubleSuccessor(-1);
  }

  @Test
  public void testSucc() {
    assertEquals(Double.valueOf(1.5), successor.succ(1.49));
    successor = new DoubleSuccessor(3);
    assertEquals(Double.valueOf(1.601), successor.succ(1.6));
    successor = new DoubleSuccessor(0);
    assertEquals(Double.valueOf(2.7), successor.succ(1.7));
  }

  @Test
  public void testCompareTo() {
    assertEquals(0, successor.compare(1.500, 1.5));
    assertEquals(-1, successor.compare(1.499, 1.5));
    assertEquals(1, successor.compare(1.501, 1.5));
  }

  @Test
  public void testEquals() {
    assertTrue(new DoubleSuccessor(2).equals(successor));
    assertFalse(new DoubleSuccessor(3).equals(successor));
    assertFalse(successor.equals(null));
  }

  @Test
  public void testHashCode() {
    assertEquals(new DoubleSuccessor(2).hashCode(), successor.hashCode());
    assertNotEquals(new DoubleSuccessor(3).hashCode(), successor.hashCode());
    assertEquals(Objects.hashCode(2), successor.hashCode());
  }

  @Test
  public void testToString() {
    assertEquals("DoubleSuccessor{precision=2}", successor.toString());
  }

}
