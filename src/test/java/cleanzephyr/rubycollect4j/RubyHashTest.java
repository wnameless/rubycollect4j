package cleanzephyr.rubycollect4j;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import cleanzephyr.rubycollect4j.block.EntryBooleanBlock;

import static cleanzephyr.rubycollect4j.RubyCollections.hp;
import static cleanzephyr.rubycollect4j.RubyCollections.ra;
import static cleanzephyr.rubycollect4j.RubyCollections.rh;
import static cleanzephyr.rubycollect4j.RubyHash.newRubyHash;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

  @Test
  public void testAssoc() {
    rh = rh(1, 2, 3, 4, 5, 6);
    assertEquals(hp(3, 4), rh.assoc(3));
    assertNull(rh.assoc(7));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCompareByIdentity() {
    rh = newRubyHash();
    rh.compareByIdentity();
  }

  @Test
  public void testCompareByIdentityʔ() {
    rh = newRubyHash();
    assertFalse(rh.comparedByIdentityʔ());
  }

  @Test
  public void testDelete() {
    rh = rh(1, 2, 3, 4, 5, 6);
    assertEquals(Integer.valueOf(4), rh.delete(3));
    assertNull(rh.delete(0));
    rh.setDefault(10);
    assertEquals(rh.getDefault(), rh.delete(3));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testDeleteIf() {
    rh = rh(1, 2, 3, 4, 5, 6);
    assertTrue(rh.deleteIf() instanceof RubyEnumerator);
    assertEquals(ra(hp(1, 2), hp(3, 4), hp(5, 6)), rh.deleteIf().toA());
  }

  @Test
  public void testDeleteIfWithBlock() {
    rh = rh(1, 2, 3, 4, 5, 6);
    assertEquals(rh(3, 4, 5, 6),
        rh.deleteIf(new EntryBooleanBlock<Integer, Integer>() {

          @Override
          public boolean yield(Integer key, Integer value) {
            return key + value < 7;
          }

        }));
    assertEquals(rh(3, 4, 5, 6), rh);
  }

}
