/*
 *
 * Copyright 2017 Wei-Ming Wu
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.Test;

public class RegexUtilsTest {

  @Test
  public void testPrivateConstructor() throws Exception {
    Constructor<RegexUtils> c = RegexUtils.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c.getModifiers()));
    c.setAccessible(true);
    c.newInstance();
  }

  @Test
  public void testConvertGlobToRegex() {
    assertEquals("/ab.{1}", RegexUtils.convertGlobToRegex("/ab?"));
    assertEquals("(.+/)?abc", RegexUtils.convertGlobToRegex("**/abc"));
    assertEquals("abc/d[^/]*f", RegexUtils.convertGlobToRegex("abc/d*f"));
    assertEquals("abc/(de|fg)", RegexUtils.convertGlobToRegex("abc/{de,fg}"));
    assertEquals("ab/(.+/)?(cd|e.{1})/[^/]*",
        RegexUtils.convertGlobToRegex("ab/**/{cd,e?}/*"));
  }

}
