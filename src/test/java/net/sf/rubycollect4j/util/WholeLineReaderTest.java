/*
 *
 * Copyright 2020 Wei-Ming Wu
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
package net.sf.rubycollect4j.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WholeLineReaderTest {

  WholeLineReader wlr;
  StringReader sr;

  @BeforeEach
  public void setUp() throws Exception {
    sr = new StringReader("a\nb\rc\r\nd\n\r");
    wlr = new WholeLineReader(sr);
  }

  @Test
  public void testReadLine() throws IOException {
    assertEquals("a\n", wlr.readLine());
    assertEquals("b\r", wlr.readLine());
    assertEquals("c\r\n", wlr.readLine());
    assertEquals("d\n", wlr.readLine());
    assertEquals("\r", wlr.readLine());
    assertNull(wlr.readLine());
  }

}
