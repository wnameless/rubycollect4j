/**
 *
 * @author Wei-Ming Wu
 *
 *
 * Copyright 2013 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package cleanzephyr.rebycollect4j.util;

import java.util.Map.Entry;

import com.google.common.base.Objects;

public final class Pair<K, V> implements Entry<K, V> {

  private K key;
  private V value;

  public static <K, V> Pair<K, V> newPair(K key, V value) {
    return new Pair<K, V>(key, value);
  }

  private Pair(K key, V value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public K getKey() {
    return key;
  }

  @Override
  public V getValue() {
    return value;
  }

  @Override
  public V setValue(V value) {
    this.value = value;
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Entry) {
      @SuppressWarnings("rawtypes")
      Entry entry = (Entry) o;
      return Objects.equal(key, entry.getKey())
          && Objects.equal(value, entry.getValue());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(key, value);
  }

  @Override
  public String toString() {
    String keyStr = key == null ? "null" : key.toString();
    String valueStr = value == null ? "null" : value.toString();
    return keyStr + "=" + valueStr;
  }

}
