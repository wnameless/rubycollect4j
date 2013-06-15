package cleanzephyr.rubycollect4j;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cleanzephyr.rubycollect4j.block.ItemBlock;

import static cleanzephyr.rubycollect4j.RubyEnumerator.newRubyEnumerator;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RubyEnumeratorTest {

  private RubyEnumerator<Integer> re;
  private List<Integer> list;

  @Before
  public void setUp() throws Exception {
    list = newArrayList(1, 2, 3);
    re = newRubyEnumerator(list);
  }

  @Test
  public void testConstructor() {
    assertEquals(RubyEnumerator.class, re.getClass());
    re = newRubyEnumerator(list.iterator());
    assertEquals(RubyEnumerator.class, re.getClass());
  }

  @Test
  public void testEach() {
    assertTrue(re == re.each());
  }

  @Test
  public void testEachWithBlock() {
    final List<Integer> ints = newArrayList();
    assertTrue(re == re.each(new ItemBlock<Integer>() {

      @Override
      public void yield(Integer item) {
        ints.add(item);
      }

    }));
    assertEquals(newArrayList(1, 2, 3), ints);
  }

  @Test
  public void testIterator() {
    assertTrue(re.iterator() instanceof Iterator);
  }

}
