package cleanzephyr.rubycollect4j;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import static cleanzephyr.rubycollect4j.RubyCollections.hp;
import static cleanzephyr.rubycollect4j.RubyCollections.rh;
import static cleanzephyr.rubycollect4j.RubyHash.newRubyHash;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static org.junit.Assert.assertEquals;

public class RubyHashTest {
  private RubyHash<Integer, Integer> rh;

  @Test
  public void testFactory() {
    rh = newRubyHash();
    assertEquals(RubyHash.class, rh.getClass());
    Map<Integer, Integer> map = newHashMap();
    rh = newRubyHash(map);
    assertEquals(RubyHash.class, rh.getClass());
    LinkedHashMap<Integer, Integer> lhm = newLinkedHashMap();
    rh = newRubyHash(lhm, true);
    assertEquals(RubyHash.class, rh.getClass());
    rh = newRubyHash(lhm, false);
    assertEquals(RubyHash.class, rh.getClass());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testPut() {
    rh = newRubyHash();
    assertEquals(rh(1, 2), rh.put(hp(1, 2)));
    assertEquals(rh(1, 2, 3, 4, 5, 6), rh.put(hp(3, 4), hp(5, 6)));
  }

}
