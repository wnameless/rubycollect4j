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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EachLineIteratorTest {

  static final String BASE_DIR = "src/test/resources/";
  EachLineIterator iter;
  EachLineIterator noFileIter;

  @BeforeEach
  public void setUp() throws Exception {
    iter = new EachLineIterator(new File(BASE_DIR + "ruby_io_read_only_mode.txt"), false);
    iter = new EachLineIterator(
        new FileInputStream(new File(BASE_DIR + "ruby_io_read_only_mode.txt")), false);
    noFileIter = new EachLineIterator(new File("No such file"), false);
  }

  @Test
  public void testConstructor() {
    assertTrue(iter instanceof EachLineIterator);
  }

  @Test
  public void testConstructorException1() {
    assertThrows(NullPointerException.class, () -> {
      new EachLineIterator((File) null, false);
    });
  }

  @Test
  public void testConstructorException2() {
    assertThrows(NullPointerException.class, () -> {
      new EachLineIterator((InputStream) null, false);
    });
  }

  @Test
  public void testConstructorException3() {
    assertThrows(RuntimeException.class, () -> {
      noFileIter.next();
    });
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

  @Test
  public void testNextException() {
    assertThrows(NoSuchElementException.class, () -> {
      while (iter.hasNext()) {
        iter.next();
      }
      iter.next();
    });
  }

  @Test
  public void testRemove() {
    assertThrows(UnsupportedOperationException.class, () -> {
      iter.remove();
    });
  }

}
