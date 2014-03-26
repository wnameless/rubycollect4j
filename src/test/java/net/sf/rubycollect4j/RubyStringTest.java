/**
 *
 * @author Wei-Ming Wu
 *
 *
 * Copyright 2014 Wei-Ming Wu
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
import static net.sf.rubycollect4j.RubyCollections.rs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.TypeConstraintException;

import org.junit.Before;
import org.junit.Test;

public class RubyStringTest {

  RubyString rs;

  @Before
  public void setUp() throws Exception {
    rs = new RubyString("abc");
  }

  @Test
  public void testInterface() {
    assertTrue(rs instanceof RubyEnumerable);
    assertTrue(rs instanceof CharSequence);
    assertTrue(rs instanceof Comparable);
  }

  @Test
  public void testConstruct() {
    assertEquals("abc", rs.toS());
    assertEquals("", rs().toS());
    assertEquals("1", rs(1).toS());
  }

  @Test(expected = TypeConstraintException.class)
  public void testConstructException() {
    rs(null);
  }

  @Test
  public void testGetIterable() {
    assertEquals(ra("a", "b", "c"), ra(rs.getIterable()));
  }

  @Test
  public void testAsciiOnlyʔ() {
    assertTrue(rs("abc").asciiOnlyʔ());
    assertFalse(rs("我abc").asciiOnlyʔ());
  }

  @Test
  public void testB() {
    assertEquals("æabc", rs("我abc").b().toS());
  }

  @Test
  public void testBytes() {
    assertEquals(ra((byte) 97, (byte) 98, (byte) 99), rs("abc").bytes());
  }

  @Test
  public void testBytesize() {
    assertEquals(3, rs("我").bytesize());
  }

  @Test
  public void testByteslice() {
    assertEquals(rs('\346'), rs("我").byteslice(0));
    assertEquals(rs('\210'), rs("我").byteslice(1));
    assertEquals(rs('\221'), rs("我").byteslice(2));
  }

  @Test
  public void testInspect() {
    assertEquals("\"\\0abc]\\n\"", rs("\0abc]\n").inspect().toS());
  }

}
