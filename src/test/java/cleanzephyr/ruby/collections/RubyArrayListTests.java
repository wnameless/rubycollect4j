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

import static cleanzephyr.ruby.collections.RubyCollections.ra;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RubyArrayListTests {

  public RubyArrayListTests() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test of and method, of class RubyArrayList.
   */
  @Test
  public void testAnd() {
    assertEquals(ra(1, 2, 3).and(ra(3, 4, 5)), ra(3));
  }

  /**
   * Test of multiply method, of class RubyArrayList.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMultiply() {
    assertEquals(ra(1, 2).multiply(2), ra(1, 2, 1, 2));
    assertEquals(ra(1, 2).multiply(", "), "1, 2");
    assertEquals(ra(1, 2).multiply(0), ra());
    ra(1, 2).multiply(-1);
  }

  /**
   * Test of add method, of class RubyArrayList.
   */
  @Test
  public void testAdd() {
    assertEquals(ra(1).add(ra(2, 3)), ra(1, 2, 3));
  }

  /**
   * Test of minus method, of class RubyArrayList.
   */
  @Test
  public void testMinus() {
    assertEquals(ra(1).minus(ra(2, 3)), ra(1));
    assertEquals(ra(1, 2, 3).minus(ra(2, 3)), ra(1));
    assertEquals(ra(1).minus(ra(1, 2, 3)), ra());
  }

  /**
   * Test of assoc method, of class RubyArrayList.
   */
  @Test
  public void testAssoc() {
    assertNull(ra(1, 2, 3).assoc(1));
    assertNull(ra(ra(2, 3)).assoc(1));
    assertEquals(ra(ra(1, 2, 3), ra(4, 5, 6)).assoc(4), ra(ra(4, 5, 6)));
  }
}
