package cleanzephyr.rubycollect4j;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import cleanzephyr.rubycollect4j.block.BooleanBlock;

import static junit.framework.Assert.assertEquals;

public class RubyEnumerableTest {
  private RubyEnumerable<Integer> re;

  @Before
  public void setUp() throws Exception {}

  @Test
  public void testConstructor() {
    re = new RubyEnumerable<Integer>(Arrays.asList(1, 2));
    assertEquals(RubyEnumerable.class, re.getClass());
    re = new RubyEnumerable<Integer>(new Integer[] { 1 });
    assertEquals(RubyEnumerable.class, re.getClass());
    re = new RubyEnumerable<Integer>(1, 2, 3);
    assertEquals(RubyEnumerable.class, re.getClass());
  }

  @Test
  public void testAllʔ() {
    re = new RubyEnumerable<Integer>(Arrays.asList(1, 2));
    assertEquals(true, re.allʔ());
    re = new RubyEnumerable<Integer>(new Integer[0]);
    assertEquals(true, re.allʔ());
    re = new RubyEnumerable<Integer>(1, 2, null);
    assertEquals(false, re.allʔ());
  }

  @Test
  public void testAllʔWithBlock() {
    re = new RubyEnumerable<Integer>(Arrays.asList(1, 2, 3));
    assertEquals(false, re.allʔ(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item % 2 == 0;
      }

    }));
    assertEquals(true, re.allʔ(new BooleanBlock<Integer>() {

      @Override
      public boolean yield(Integer item) {
        return item < 5;
      }

    }));
  }

}
