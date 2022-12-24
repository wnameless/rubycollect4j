/*
 *
 * Copyright 2013 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package net.sf.rubycollect4j.iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EachLineIterableTest {

  static final String BASE_DIR = "src/test/resources/";
  EachLineIterable iter;

  @BeforeEach
  public void setUp() throws Exception {
    iter = new EachLineIterable(new File(BASE_DIR + "ruby_io_read_only_mode.txt"), false);
    iter = new EachLineIterable(
        new FileInputStream(new File(BASE_DIR + "ruby_io_read_only_mode.txt")), false);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof EachLineIterable);
  }

  @Test
  public void testConstructorException1() {
    assertThrows(NullPointerException.class, () -> {
      new EachLineIterable((File) null, false);
    });
  }

  @Test
  public void testConstructorException2() {
    assertThrows(NullPointerException.class, () -> {
      new EachLineIterable((InputStream) null, false);
    });
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
