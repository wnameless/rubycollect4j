/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cleanzephyr.rubycollect4j;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author WMW
 */
public class RubyHsh<K, V> extends RubyEnum<Entry<K, V>> implements Map<K, V> {

  private LinkedHashMap<K, V> lhm;

  public RubyHsh(LinkedHashMap<K, V> lhm) {
    super(lhm.entrySet());
    this.lhm = lhm;
  }

  @Override
  public int size() {
    return lhm.size();
  }

  @Override
  public boolean isEmpty() {
    return lhm.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return lhm.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return lhm.containsValue(value);
  }

  @Override
  public V get(Object key) {
    return lhm.get(key);
  }

  @Override
  public V put(K key, V value) {
    return lhm.put(key, value);
  }

  @Override
  public V remove(Object key) {
    return lhm.remove(key);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    lhm.putAll(m);
  }

  @Override
  public void clear() {
    lhm.clear();
  }

  @Override
  public Set<K> keySet() {
    return lhm.keySet();
  }

  @Override
  public Collection<V> values() {
    return lhm.values();
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    return lhm.entrySet();
  }

}
