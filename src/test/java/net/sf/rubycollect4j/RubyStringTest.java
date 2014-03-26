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

import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.rs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;

import javax.xml.bind.TypeConstraintException;

import net.sf.rubycollect4j.block.Block;

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
    assertEquals(rs("el"), rs("hello").byteslice(1, 2));
    assertNull(rs("hello").byteslice(5, -1));
  }

  @Test
  public void testCapitalize() {
    assertEquals(rs("Abcde"), rs("abCde").capitalize());
    assertEquals(rs(), rs("").capitalize());
  }

  @Test
  public void testCapitalizeǃ() {
    assertSame(rs, rs.capitalizeǃ());
    assertEquals("Abc", rs.toS());
    assertNull(rs("Abc").capitalizeǃ());
  }

  @Test
  public void testCasecmp() {
    assertEquals("abc".compareToIgnoreCase("ABC"), rs.casecmp("ABC"));
  }

  @Test
  public void testCenter() {
    assertEquals("hello", rs("hello").center(4).toS());
    assertEquals("       hello        ", rs("hello").center(20).toS());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCenterException() {
    rs("hello").center(4, "");
  }

  @Test
  public void testCenterWithPadstr() {
    assertEquals("1231231hello12312312", rs("hello").center(20, "123").toS());
    assertEquals("12312312311231231231", rs().center(20, "123").toS());
  }

  @Test
  public void testChars() {
    assertEquals(ra("a", "b", "c"), rs.chars());
  }

  @Test
  public void testChomp() {
    assertEquals("hello", rs("hello").chomp().toS());
    assertEquals("hello", rs("hello\n").chomp().toS());
    assertEquals("hello", rs("hello\r\n").chomp().toS());
    assertEquals("hello\n", rs("hello\n\r").chomp().toS());
    assertEquals("hello", rs("hello\r").chomp().toS());
    assertEquals("hello \n there", rs("hello \n there").chomp().toS());
    assertEquals("he", rs("hello").chomp("llo").toS());
  }

  @Test
  public void testChompǃ() {
    assertNull(rs.chompǃ());
    rs = rs("abc\n");
    assertSame(rs, rs.chompǃ());
    assertEquals("abc", rs.toS());
  }

  @Test
  public void testChompWithSeparator() {
    assertEquals("abc", rs("abc.").chomp(".").toS());
    assertEquals("abc", rs("abc").chomp(".").toS());
    assertEquals("abc", rs("abc").chomp(null).toS());
  }

  @Test
  public void testChompǃWithSeparator() {
    assertNull(rs.chompǃ("."));
    rs = rs("abc.");
    assertSame(rs, rs.chompǃ("."));
    assertEquals("abc", rs.toS());
  }

  @Test
  public void testChop() {
    assertEquals(rs(), rs("").chop());
    assertEquals(rs("abc"), rs("abc\r\n").chop());
    assertEquals(rs("abc\r\n"), rs("abc\r\n\r").chop());
    assertEquals(rs("ab"), rs("abc").chop());
  }

  @Test
  public void testChopǃ() {
    assertNull(rs().chopǃ());
    assertSame(rs, rs.chopǃ());
    assertEquals("ab", rs.toS());
  }

  @Test
  public void testChr() {
    assertEquals(rs("a"), rs.chr());
    assertEquals(rs(), rs("").chr());
  }

  @Test
  public void testCodepoints() {
    assertEquals(ra(97, 98, 99, 25105), rs("abc我").codepoints());
  }

  @Test
  public void testConcat() {
    assertEquals(rs("abc我"), rs.concat(25105));
    assertEquals(rs("null"), rs().concat(null));
  }

  @Test
  public void testConcatWithObject() {
    assertEquals(rs("abc[1, 2, 3]"), rs.concat(ra(1, 2, 3)));
  }

  @Test
  public void testDelete() {
    assertEquals(rs("b"), rs("abc").delete("ac"));
    assertEquals(rs("a"), rs("abc").delete("^a"));
    assertEquals(rs("abc"), rs("abc^").delete("\\^"));
    assertEquals(rs("d"), rs("abcd").delete("a-c"));
  }

  @Test(expected = TypeConstraintException.class)
  public void testDeleteException() {
    rs("abc").delete(null);
  }

  @Test
  public void testDeleteǃ() {
    assertNull(rs("abc").deleteǃ("d"));
    assertSame(rs, rs.deleteǃ("a"));
    assertEquals("bc", rs.toS());
  }

  @Test
  public void testDowncase() {
    assertEquals(rs("abc"), rs("ABC").downcase());
  }

  @Test
  public void testDowncaseǃ() {
    assertNull(rs("abc").downcaseǃ());
    rs = rs("ABC");
    assertSame(rs, rs.downcaseǃ());
    assertEquals("abc", rs.toS());
  }

  @Test
  public void testDump() {
    assertEquals("\"\\u6211\\b\\f\\n\\r\\t\\0abc]\\n\\177\\uea60\"",
        rs("我\b\f\n\r\t\0abc]\n\177\uea60").dump().toS());
  }

  @Test
  public void testEachBytes() {
    assertEquals(
        ra((byte) 'a', (byte) 'b', (byte) 'c', (byte) '\n', "我".getBytes()[0],
            "我".getBytes()[1], "我".getBytes()[2]), rs("abc\n我").eachByte()
            .toA());
  }

  @Test
  public void testEachBytesWithBlock() {
    final RubyArray<Integer> ints = newRubyArray();
    assertSame(rs, rs.eachByte(new Block<Byte>() {

      @Override
      public void yield(Byte item) {
        ints.add((int) item);
      }

    }));
    assertEquals(ra(97, 98, 99), ints);
  }

  @Test
  public void testEachChars() {
    assertEquals(ra("a", "b", "c", "\n", "我"), rs("abc\n我").eachChar().toA());
  }

  @Test
  public void testEachCharsWithBlock() {
    final RubyArray<String> chars = newRubyArray();
    assertSame(rs, rs.eachChar(new Block<String>() {

      @Override
      public void yield(String item) {
        chars.add(item);
      }

    }));
    assertEquals(ra("a", "b", "c"), chars);
  }

  @Test
  public void testEachCodepoint() {
    assertEquals(ra(97, 98, 99, 25105), rs("abc我").eachCodepoint().toA());
  }

  @Test
  public void testEachCodepointWithBlock() {
    final RubyArray<Integer> ints = newRubyArray();
    assertSame(rs, rs.eachCodepoint(new Block<Integer>() {

      @Override
      public void yield(Integer item) {
        ints.add(item);
      }

    }));
    assertEquals(ra(97, 98, 99), ints);
  }

  @Test
  public void testEachLine() {
    assertEquals(ra("a", "\r", "bc"), rs("a\n\r\nbc\n").eachLine().toA());
  }

  @Test
  public void testEachLineWithBlock() {
    rs = rs("a\n\r\nbc\n");
    final RubyArray<String> lines = newRubyArray();
    assertSame(rs, rs.eachLine(new Block<String>() {

      @Override
      public void yield(String item) {
        lines.add(item);
      }

    }));
    assertEquals(ra("a", "\r", "bc"), lines);
  }

  @Test
  public void testEachLineWithSeparator() {
    assertEquals(ra("a\n", "\nbc\n"), rs("a\n\r\nbc\n").eachLine("\r").toA());
  }

  @Test(expected = TypeConstraintException.class)
  public void testEachLineWithSeparatorException() {
    rs.eachLine((String) null);
  }

  @Test
  public void testEachLineWithSeparatorAndBlock() {
    rs = rs("a\n\r\nbc\n");
    final RubyArray<String> lines = newRubyArray();
    assertSame(rs, rs.eachLine("\r", new Block<String>() {

      @Override
      public void yield(String item) {
        lines.add(item);
      }

    }));
    assertEquals(ra("a\n", "\nbc\n"), lines);
  }

  @Test(expected = TypeConstraintException.class)
  public void testEachLineWithSeparatorAndBlockException() {
    rs.eachLine((String) null, new Block<String>() {

      @Override
      public void yield(String item) {}

    });
  }

  @Test
  public void testEmptyʔ() {
    assertFalse(rs.emptyʔ());
    rs = rs("");
    assertTrue(rs.emptyʔ());
  }

  @Test
  public void testEncode() {
    assertEquals(rs("æ"), rs("我").encode("ISO-8859-1"));
  }

  @Test(expected = TypeConstraintException.class)
  public void testEncodeException() {
    rs.encode(null);
  }

  @Test
  public void testEncodeWithDestinationAndSourceEncoding() {
    assertEquals(rs("æ"), rs("我").encode("ISO-8859-1", "UTF-8"));
  }

  @Test
  public void testEncodeǃ() {
    assertEquals(rs("æ"), rs("我").encodeǃ("ISO-8859-1"));
    assertSame(rs, rs.encodeǃ("ISO-8859-1"));
  }

  @Test
  public void testEncodeǃWithDestinationAndSourceEncoding() {
    assertEquals(rs("æ"), rs("我").encodeǃ("ISO-8859-1", "UTF-8"));
    assertSame(rs, rs.encodeǃ("ISO-8859-1", "UTF-8"));
  }

  @Test(expected = TypeConstraintException.class)
  public void testEncodeExceptionWithNullDestinationEncoding() {
    rs.encode(null, "UTF-8");
  }

  @Test(expected = TypeConstraintException.class)
  public void testEncodeExceptionWithNullSourceEncoding() {
    rs.encode("ISO-8859-1", null);
  }

  @Test
  public void testEncoding() {
    assertEquals(Charset.forName("UTF-8"), rs.encoding());
  }

  @Test
  public void testEndWithʔ() {
    assertTrue(rs.endWithʔ("c"));
    assertTrue(rs.endWithʔ("b", "a", "c"));
    assertFalse(rs.endWithʔ("a"));
  }

  @Test(expected = TypeConstraintException.class)
  public void testEndWithʔException1() {
    rs.endWithʔ(null);
  }

  @Test(expected = TypeConstraintException.class)
  public void testEndWithʔException2() {
    rs.endWithʔ("c", (String[]) null);
  }

  @Test
  public void testEqlʔ() {
    assertTrue(rs("a").eqlʔ(rs("a")));
    assertFalse(rs("a").eqlʔ("a"));
  }

  @Test
  public void testForceEncoding() {
    assertEquals(rs("æ"), rs("我").forceEncoding("ISO-8859-1"));
    assertSame(rs, rs.forceEncoding("ISO-8859-1"));
  }

  @Test
  public void testGetbyte() {
    assertEquals((Byte) "我".getBytes()[0], rs("我").getbyte(0));
    assertEquals((Byte) "我".getBytes()[1], rs("我").getbyte(1));
    assertEquals((Byte) "我".getBytes()[2], rs("我").getbyte(2));
    assertNull(rs("我").getbyte(3));
    assertEquals((Byte) "我".getBytes()[2], rs("我").getbyte(-1));
  }

  @Test
  public void testInspect() {
    assertEquals("\"我\\b\\f\\n\\r\\t\\0abc]\\n\\177\\uea60\"",
        rs("我\b\f\n\r\t\0abc]\n\177\uea60").inspect().toS());
  }

  @Test
  public void testClear() {
    assertEquals(rs(), rs.clear());
    assertSame(rs, rs.clear());
  }

  @Test
  public void testLines() {
    assertEquals(ra("a", "b", "", "c"), rs("a\nb\n\nc").lines());
  }

}
