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
package net.sf.rubycollect4j;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyLiterals.qr;
import static net.sf.rubycollect4j.RubyLiterals.qw;
import static net.sf.rubycollect4j.RubyLiterals.qx;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

public class RubyLiteralsTest {

  @Test
  public void testQr() {
    assertTrue(qr("\\d+") instanceof Pattern);
    assertTrue(qr("\\d+").matcher("asf324ds").find());
  }

  @Test
  public void testQw() {
    assertTrue(qw("a b c") instanceof RubyArray);
    assertEquals(ra("a", "b", "c"), qw("a b c"));
  }

  @Test
  public void testQx() {
    if (System.getProperty("os.name").startsWith("Windows")) {
      assertEquals("Hello world!" + System.getProperty("line.separator"),
          qx("cmd", "/C", "echo Hello world!"));
    } else {
      assertEquals("Hello world!" + System.getProperty("line.separator"),
          qx("echo", "Hello world!"));
      assertEquals("Hello world!" + System.getProperty("line.separator"),
          qx(new String[] { "sh", "-c", "echo Hello world! 1>&2" }));
    }
  }

  @Test
  public void testQxException() {
    assertThrows(RuntimeException.class, () -> {
      qx("lls");
    });
  }

}
