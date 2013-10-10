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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.RandomAccessFile;

import org.junit.Before;
import org.junit.Test;

public class EachLineIterableTest {

  private static final String BASE_DIR = "src/test/resources/";
  private EachLineIterable iter;

  @Before
  public void setUp() throws Exception {
    iter =
        new EachLineIterable(new RandomAccessFile(new File(BASE_DIR
            + "ruby_io_read_only_mode.txt"), "r"));
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof EachLineIterable);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorException() {
    new EachLineIterable(null);
  }

  @Test
  public void testIterator() {
    assertTrue(iter.iterator() instanceof EachLineIterator);
  }

  @Test
  public void testToString() {
    assertEquals("[a, bc, def]", iter.toString());
  }

}
