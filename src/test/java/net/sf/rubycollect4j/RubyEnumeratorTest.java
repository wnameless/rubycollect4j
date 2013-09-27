package net.sf.rubycollect4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sf.rubycollect4j.block.Block;

import org.junit.Before;
import org.junit.Test;

public class RubyEnumeratorTest {

  private RubyEnumerator<Integer> re;
  private List<Integer> list;

  @Before
  public void setUp() throws Exception {
    list = Arrays.asList(1, 2, 3);
    re = new RubyEnumerator<Integer>(list);
  }

  @Test
  public void testConstructor() {
    assertTrue(re instanceof RubyEnumerator);
    re = new RubyEnumerator<Integer>(list.iterator());
    assertTrue(re instanceof RubyEnumerator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    Iterable<Integer> iter = null;
    re = new RubyEnumerator<Integer>(iter);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException2() {
    Iterator<Integer> iter = null;
    re = new RubyEnumerator<Integer>(iter);
  }

  @Test
  public void testEach() {
    assertTrue(re == re.each());
  }

  @Test
  public void testEachWithBlock() {
    final List<Integer> ints = new ArrayList<Integer>();
    assertTrue(re == re.each(new Block<Integer>() {

      @Override
      public void yield(Integer item) {
        ints.add(item);
      }

    }));
    assertEquals(Arrays.asList(1, 2, 3), ints);
  }

  @Test
  public void testRewind() {
    while (re.hasNext()) {
      re.next();
    }
    re.rewind();
    assertTrue(re.hasNext());
  }

  @Test
  public void testPeek() {
    while (re.hasNext()) {
      Integer peeking = re.peek();
      assertEquals(peeking, re.next());
    }
  }

  @Test
  public void testIterator() {
    assertTrue(re.iterator() instanceof Iterator);
  }

  @Test
  public void testToString() {
    assertEquals("RubyEnumerator{[1, 2, 3]}", re.toString());
  }

}
