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
package cleanzephyr.rubycollect4j;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static cleanzephyr.rubycollect4j.RubyArray.newRubyArray;
import static cleanzephyr.rubycollect4j.RubyCollections.ra;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

public class RubyArrayTest {
  private RubyArray<Integer> ra;

  @Test
  public void testFactory() {
    ra = newRubyArray(Arrays.asList(1, 2));
    assertEquals(RubyArray.class, ra.getClass());
    ra = newRubyArray(new Integer[] { 1 });
    assertEquals(RubyArray.class, ra.getClass());
    ra = newRubyArray(1, 2, 3);
    assertEquals(RubyArray.class, ra.getClass());
    ra = newRubyArray(ra.iterator());
    assertEquals(RubyArray.class, ra.getClass());
    List<Integer> ints = newArrayList(1, 2, 3);
    ra = newRubyArray(ints);
    ints.set(0, 4);
    assertEquals(ra(4, 2, 3), ra);
    ints = newArrayList(1, 2, 3);
    ra = newRubyArray(ints, true);
    ints.set(0, 4);
    assertEquals(ra(1, 2, 3), ra);
  }

}
