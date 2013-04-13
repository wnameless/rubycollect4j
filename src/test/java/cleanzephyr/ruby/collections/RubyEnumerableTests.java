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
package cleanzephyr.ruby.collections;

import cleanzephyr.rubycollect4j.RubyEnumerable;
import static com.google.common.collect.Lists.newArrayList;
import java.util.List;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertFalse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RubyEnumerableTests {

  private List<String> list;

  public RubyEnumerableTests() {
  }

  @Before
  public void setUp() {
    list = newArrayList();
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testHasAll() {
    assertTrue(RubyEnumerable.allʔ(list));
    list.add("item1");
    assertTrue(RubyEnumerable.allʔ(list));
    list.add(null);
    assertFalse(RubyEnumerable.allʔ(list));
  }

  @Test
  public void testHasAllWithBlock() {
    list.add("item1");
    assertTrue(RubyEnumerable.allʔ(list, (i) -> {
      return i instanceof String;
    }));
    assertFalse(RubyEnumerable.allʔ(list, (i) -> {
      return i.equals("");
    }));
  }
}