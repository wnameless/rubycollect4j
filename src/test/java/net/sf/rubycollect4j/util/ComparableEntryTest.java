package net.sf.rubycollect4j.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

public class ComparableEntryTest {

  private ComparableEntry<Integer, Integer> entry;

  @Before
  public void setUp() throws Exception {
    entry = new ComparableEntry<Integer, Integer>(0, 1);
  }

  @Test
  public void testConstructor() {
    assertTrue(entry instanceof ComparableEntry);
    entry =
        new ComparableEntry<Integer, Integer>(
            new SimpleEntry<Integer, Integer>(0, 1));
    assertTrue(entry instanceof ComparableEntry);
  }

  @Test
  public void testInterfaces() {
    assertTrue(entry instanceof Entry);
    assertTrue(entry instanceof Comparable);
  }

  @Test
  public void testGetKey() {
    assertEquals(Integer.valueOf(0), entry.getKey());
  }

  @Test
  public void testGetValue() {
    assertEquals(Integer.valueOf(1), entry.getValue());
  }

  @Test
  public void testSetValue() {
    assertEquals(Integer.valueOf(1), entry.setValue(2));
    assertEquals(Integer.valueOf(2), entry.getValue());
  }

  @Test
  public void testEquals() {
    assertTrue(entry.equals(new SimpleEntry<Integer, Integer>(0, 1)));
    assertFalse(entry.equals(null));
  }

  @Test
  public void testHashCode() {
    assertEquals(new SimpleEntry<Integer, Integer>(0, 1).hashCode(),
        entry.hashCode());
  }

  @Test
  public void testToString() {
    assertEquals(new SimpleEntry<Integer, Integer>(0, 1).toString(),
        entry.toString());
  }

  @Test
  public void testCompareTo() {
    assertTrue(entry.compareTo(new SimpleEntry<Integer, Integer>(0, 1)) == 0);
    assertTrue(entry.compareTo(new SimpleEntry<Integer, Integer>(2, 3)) < 0);
    assertTrue(entry.compareTo(new SimpleEntry<Integer, Integer>(0, 0)) > 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCompareToException() {
    ComparableEntry<Point, Integer> entry =
        new ComparableEntry<Point, Integer>(new Point(1, 1), 1);
    entry.compareTo(new ComparableEntry<Point, Integer>(new Point(1, 1), 0));
  }

}
