/*
 *
 * Copyright 2013-2015 Wei-Ming Wu
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
package net.sf.rubycollect4j.util;

import static net.sf.rubycollect4j.RubyCollections.hp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import net.sf.rubycollect4j.util.LinkedIdentityMap.IdentityEntry;

public class LinkedIdentityMap_IdentityEntryTest {

  IdentityEntry<String, Integer> entry;
  String key;
  Integer value;

  @Before
  public void setUp() throws Exception {
    key = "a";
    value = Integer.valueOf(1);
    entry = new IdentityEntry<String, Integer>(key, value);
  }

  @Test
  public void testConstructor() {
    assertTrue(entry instanceof LinkedIdentityMap.IdentityEntry);
    entry = new IdentityEntry<String, Integer>(hp(key, value));
    assertTrue(entry instanceof LinkedIdentityMap.IdentityEntry);
  }

  @Test
  public void testGetKey() {
    assertSame(key, entry.getKey());
  }

  @Test
  public void testGetValue() {
    assertSame(value, entry.getValue());
  }

  @Test
  public void testSetValue() {
    assertSame(value, entry.setValue(Integer.valueOf(2)));
    assertSame(Integer.valueOf(2), entry.getValue());
  }

  @Test
  public void testEquals() {
    assertTrue(entry
        .equals(new IdentityEntry<String, Integer>(key, Integer.valueOf(1))));
    assertFalse(entry.equals(new IdentityEntry<String, Integer>(key, null)));
    assertTrue(entry.equals(hp(new String("a"), value)));
    assertFalse(entry.equals(null));
    entry.setValue(null);
    assertTrue(entry.equals(new IdentityEntry<String, Integer>(key, null)));
    assertFalse(entry
        .equals(new IdentityEntry<String, Integer>(key, Integer.valueOf(1))));
    assertFalse(entry.equals(new IdentityEntry<String, Integer>(null, null)));
  }

  @Test
  public void testHashCode() {
    assertEquals(
        new IdentityEntry<String, Integer>(new String("a"), value).hashCode(),
        entry.hashCode());
    assertEquals(hp(new String("a"), value).hashCode(), entry.hashCode());
    assertNotEquals(
        new IdentityEntry<String, Integer>(new String("a"), Integer.valueOf(2))
            .hashCode(),
        entry.hashCode());
  }

  @Test
  public void teString() {
    assertEquals("a=1", entry.toString());
  }

}
