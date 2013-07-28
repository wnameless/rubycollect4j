package net.sf.rubycollect4j.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IterableMapTest {

  private IterableMap<Integer, Integer> map;

  @Test
  public void testConstructor() {
    map = new IterableMap<Integer, Integer>(new HashMap<Integer, Integer>());
    assertTrue(map instanceof IterableMap);
    assertTrue(map instanceof Map);
    assertTrue(map instanceof Iterable);
  }

  @Test
  public void testDelegate() {
    Map<Integer, Integer> delegate = newLinkedHashMap();
    map = new IterableMap<Integer, Integer>(delegate);
    assertTrue(delegate == map.delegate());
  }

  @Test
  public void testIterator() {
    Map<Integer, Integer> delegate = newLinkedHashMap();
    map = new IterableMap<Integer, Integer>(delegate);
    delegate.put(1, 2);
    delegate.put(3, 4);
    delegate.put(5, 6);
    assertEquals(ra(delegate.entrySet().iterator()), ra(map.iterator()));
  }

}
