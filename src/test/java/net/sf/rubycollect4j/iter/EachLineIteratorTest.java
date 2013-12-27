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
package net.sf.rubycollect4j.iter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class EachLineIteratorTest {

  private static final String BASE_DIR = "src/test/resources/";
  private EachLineIterator iter;

  @Before
  public void setUp() throws Exception {
    iter =
        new EachLineIterator(new File(BASE_DIR + "ruby_io_read_only_mode.txt"));
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof EachLineIterator);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException1() {
    new EachLineIterator(null);
  }

  @Test(expected = RuntimeException.class)
  public void testConstructorException2() {
    iter = new EachLineIterator(new File("No such file"));
    iter.hasNext();
  }

  @Test
  public void testHasNext() {
    assertTrue(iter.hasNext());
    while (iter.hasNext()) {
      iter.next();
    }
    assertFalse(iter.hasNext());
  }

  @Test
  public void testNext() {
    assertEquals("a", iter.next());
    assertEquals("bc", iter.next());
    assertEquals("def", iter.next());
    assertFalse(iter.hasNext());
  }

  @Test(expected = NoSuchElementException.class)
  public void testNextException() {
    while (iter.hasNext()) {
      iter.next();
    }
    iter.next();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemove() {
    iter.remove();
  }

}
