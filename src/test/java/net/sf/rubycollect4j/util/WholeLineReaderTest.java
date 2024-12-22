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

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import net.sf.rubycollect4j.Ruby;

public class WholeLineReaderTest {

  WholeLineReader wlr;
  StringReader sr;

  @BeforeEach
  public void setUp() throws Exception {
    sr = new StringReader("a\nb\rc\r\nd\n\r\r\n\n1234567890\n");
    wlr = new WholeLineReader(sr);
  }

  @Test
  public void testReadLine1() throws IOException {
    assertEquals("a\n", wlr.readLine());
    assertEquals("b\r", wlr.readLine());
    assertEquals("c\r\n", wlr.readLine());
    assertEquals("d\n", wlr.readLine());
    assertEquals("\r", wlr.readLine());
    assertEquals("\r\n", wlr.readLine());
    assertEquals("\n", wlr.readLine());
    assertEquals("1234567890\n", wlr.readLine());
    assertNull(wlr.readLine());
  }

  @Test
  public void testReadLine2() throws IOException {
    String str = Ruby.String.of("a").ljust(2047, "a").toS();
    sr = new StringReader(str + "\r\n");
    wlr = new WholeLineReader(sr);

    assertEquals(str + "\r\n", wlr.readLine());
    assertNull(wlr.readLine());
  }

  @Test
  public void testReadLine3() throws IOException {
    String str = Ruby.String.of("a").ljust(2047, "a").toS();
    sr = new StringReader(str + "\r");
    wlr = new WholeLineReader(sr);

    assertEquals(str + "\r", wlr.readLine());
    assertNull(wlr.readLine());
  }

  @Test
  public void testReadLine4() throws IOException {
    String str = Ruby.String.of("a").ljust(2048, "a").toS();
    sr = new StringReader(str);
    wlr = new WholeLineReader(sr);

    assertEquals(str, wlr.readLine());
    assertNull(wlr.readLine());
  }

  @Test
  public void testClose() throws IOException {
    wlr.close();
  }

}
