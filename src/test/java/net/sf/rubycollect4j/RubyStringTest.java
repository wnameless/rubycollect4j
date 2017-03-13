/*
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
package net.sf.rubycollect4j;

import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.rh;
import static net.sf.rubycollect4j.RubyCollections.rs;
import static net.sf.rubycollect4j.RubyLiterals.qr;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.TypeConstraintException;

import org.junit.Before;
import org.junit.Test;

import net.sf.rubycollect4j.succ.StringSuccessor;

public class RubyStringTest {

  RubyString rs;
  CharSequence cs;

  @Before
  public void setUp() throws Exception {
    rs = new RubyString("abc");
    cs = new CharSequence() {

      private String str = "testCharSequence";

      @Override
      public char charAt(int index) {
        return str.charAt(index);
      }

      @Override
      public int length() {
        return str.length();
      }

      @Override
      public CharSequence subSequence(int beginIndex, int endIndex) {
        return str.subSequence(beginIndex, endIndex);
      }

    };
  }

  @Test
  public void testInterfaces() {
    assertTrue(rs instanceof CharSequence);
    assertTrue(rs instanceof Comparable);
    assertTrue(rs instanceof Serializable);
  }

  @Test
  public void testConstructor() {
    assertEquals("abc", rs.toS());
    assertEquals("", rs().toS());
    assertEquals("1", rs(1).toS());
    assertEquals("testCharSequence", rs(cs).toS());
  }

  @Test(expected = TypeConstraintException.class)
  public void testConstructException() {
    rs(null);
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
    assertNull(rs("hello").byteslice(5));
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
    assertEquals(rs.toS().compareToIgnoreCase("ABC"), rs.casecmp("ABC"));
    assertEquals(rs.toS().compareToIgnoreCase("def"), rs.casecmp("def"));
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

  // @Test
  // public void testChars() {
  // assertEquals(ra("a", "b", "c"), rs.toA());
  // }

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
  public void testClear() {
    assertEquals(rs(), rs.clear());
    assertSame(rs, rs.clear());
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
  public void testCount() {
    rs = rs("hello world");
    assertEquals(5, rs.count("lo"));
    assertEquals(2, rs.count("lo", "o"));
    assertEquals(5, rs.count("lo", (String[]) null));
    assertEquals(4, rs.count("hello", "^l"));
    assertEquals(4, rs.count("ej-m"));
    assertEquals(4, rs("hello^world").count("\\^aeiou"));
    assertEquals(4, rs("hello-world").count("a\\-eo"));
    rs = rs("hello world\\r\\n");
    assertEquals(2, rs.count("\\"));
    assertEquals(0, rs.count("\\A"));
    assertEquals(3, rs.count("X-\\w"));
  }

  @Test(expected = TypeConstraintException.class)
  public void testCountException1() {
    rs.count((String) null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCountException2() {
    rs.count("x-a");
  }

  @Test
  public void testCrypt() {
    String encrypt = rs.toS() + "secret";
    String md5 = null;
    MessageDigest digest;
    try {
      digest = MessageDigest.getInstance("SHA-256");
      digest.update(encrypt.getBytes("UTF-8"), 0, encrypt.length());
      md5 = new BigInteger(1, digest.digest()).toString(16);
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {}
    assertEquals(md5, rs.crypt("secret").toS());
  }

  @Test(expected = TypeConstraintException.class)
  public void testCryptException() {
    rs.crypt(null);
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
    rs.delete(null);
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
            "我".getBytes()[1], "我".getBytes()[2]),
        rs("abc\n我").eachByte().toA());
  }

  @Test
  public void testEachBytesWithBlock() {
    final RubyArray<Integer> ints = newRubyArray();
    assertSame(rs, rs.eachByte(item -> ints.add((int) item)));
    assertEquals(ra(97, 98, 99), ints);
  }

  @Test
  public void testEachChars() {
    assertEquals(ra("a", "b", "c", "\n", "我"), rs("abc\n我").eachChar().toA());
  }

  @Test
  public void testEachCharsWithBlock() {
    final RubyArray<String> chars = newRubyArray();
    assertSame(rs, rs.eachChar(item -> chars.add(item)));
    assertEquals(ra("a", "b", "c"), chars);
  }

  @Test
  public void testEachCodepoint() {
    assertEquals(ra(97, 98, 99, 25105), rs("abc我").eachCodepoint().toA());
  }

  @Test
  public void testEachCodepointWithBlock() {
    final RubyArray<Integer> ints = newRubyArray();
    assertSame(rs, rs.eachCodepoint(item -> ints.add(item)));
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
    assertSame(rs, rs.eachLine(item -> lines.add(item)));
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
    assertSame(rs, rs.eachLine("\r", item -> lines.add(item)));
    assertEquals(ra("a\n", "\nbc\n"), lines);
  }

  @Test(expected = TypeConstraintException.class)
  public void testEachLineWithSeparatorAndBlockException() {
    rs.eachLine((String) null, item -> {});
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
    assertTrue(rs.endWithʔ("c", (String[]) null));
  }

  @Test(expected = TypeConstraintException.class)
  public void testEndWithʔException() {
    rs.endWithʔ(null);
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
  public void testGsub() {
    assertEquals(rs("ab77c77"), rs("ab4c56").gsub("\\d+", "77"));
  }

  @Test(expected = TypeConstraintException.class)
  public void testGsubException1() {
    rs("ab4c56").gsub(null, "77");
  }

  @Test(expected = TypeConstraintException.class)
  public void testGsubException2() {
    rs("ab4c56").gsub("\\d+", (String) null);
  }

  @Test
  public void testGsubWithMap() {
    assertEquals(rs("0ab88c99"),
        rs("0ab4c56").gsub("\\d+", rh("4", "88", "56", "99")));
    assertEquals(rs, rs.gsub("\\d+", (Map<String, ?>) null));
    assertNotSame(rs, rs.gsub("\\d+", (Map<String, ?>) null));
  }

  @Test(expected = TypeConstraintException.class)
  public void testGsubWithMapException() {
    rs("0ab4c56").gsub(null, rh("4", "88", "56", "99"));
  }

  @Test
  public void testGsubWithBlock() {
    assertEquals(rs("ab40c560"), rs("ab4c56").gsub("\\d+", item -> item + "0"));
  }

  @Test(expected = TypeConstraintException.class)
  public void testGsubWithBlockException() {
    rs("ab4c56").gsub(null, item -> item + "0");
  }

  @Test
  public void testGsubWithoutReplacement() {
    assertEquals(ra("4", "56"), rs("ab4c56").gsub("\\d+").toA());
  }

  @Test(expected = TypeConstraintException.class)
  public void testGsubWithoutReplacementException() {
    rs("ab4c56").gsub(null);
  }

  @Test
  public void testGsubǃ() {
    rs = rs("ab4c56");
    assertSame(rs, rs.gsubǃ("\\d+", "77"));
    assertEquals(rs("ab77c77"), rs);
    assertNull(rs.gsubǃ("\\d+", "77"));
  }

  @Test
  public void testGsubǃWithBlock() {
    rs = rs("ab4c56");
    assertSame(rs, rs.gsubǃ("\\d+", item -> "?"));
    assertEquals(rs("ab?c?"), rs);
    assertNull(rs.gsubǃ("\\d+", item -> item + "?"));
  }

  @Test
  public void testGsubǃWithoutReplacement() {
    assertEquals(ra("4", "56"), rs("ab4c56").gsubǃ("\\d+").toA());
  }

  @Test
  public void testHash() {
    assertEquals(rs.hashCode(), rs.hash());
  }

  @Test
  public void testHex() {
    assertEquals(10, rs("0x0a").hex());
    assertEquals(-4660, rs("-1234").hex());
    assertEquals(0, rs("0").hex());
    assertEquals(0, rs("haha").hex());
  }

  @Test
  public void testIncludeʔ() {
    assertTrue(rs("abc").includeʔ("ab"));
    assertFalse(rs("abc").includeʔ("ac"));
  }

  @Test(expected = TypeConstraintException.class)
  public void testIncludeʔtException() {
    rs.includeʔ(null);
  }

  @Test
  public void testIndex() {
    assertEquals((Integer) 1, rs.index("bc"));
    assertNull(rs.index("def"));
  }

  @Test(expected = TypeConstraintException.class)
  public void testIndexException() {
    rs.index((String) null);
  }

  @Test
  public void testIndexWithOffset() {
    assertEquals((Integer) 1, rs.index("bc", 1));
    assertNull(rs.index("ab", 100));
  }

  @Test(expected = TypeConstraintException.class)
  public void testIndexWithOffsetException() {
    rs.index((String) null, 100);
  }

  @Test
  public void testIndexWithPattern() {
    assertEquals((Integer) 2, rs.index(qr("[c-z]+")));
    assertNull(rs.index(qr("[d-z]+")));
  }

  @Test(expected = TypeConstraintException.class)
  public void testIndexWithPatternException() {
    rs.index((Pattern) null);
  }

  @Test
  public void testIndexWithPatternAndOffset() {
    assertEquals((Integer) 2, rs.index(qr("[c-z]+"), 1));
    assertNull(rs.index(qr("[c-z]+"), rs.length()));
    assertNull(rs.index(qr("[c-z]+"), -100));
    assertNull(rs.index(qr("[c-z]+"), 100));
  }

  @Test(expected = TypeConstraintException.class)
  public void testIndexWithPatternAndOffsetException() {
    rs.index((Pattern) null, 1);
  }

  @Test
  public void testInsert() {
    assertEquals(rs("X1234"), rs("1234").insert(0, "X"));
    assertEquals(rs("X1234"), rs("1234").insert(-5, "X"));
    assertEquals(rs("123X4"), rs("1234").insert(3, "X"));
    assertEquals(rs("1234X"), rs("1234").insert(4, "X"));
    assertEquals(rs("12X34"), rs("1234").insert(-3, "X"));
    assertEquals(rs("1234X"), rs("1234").insert(0 - 1, "X"));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testInsertIndexException1() {
    rs("1234").insert(-6, "X");
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testInsertIndexException2() {
    rs("1234").insert(5, "X");
  }

  @Test(expected = TypeConstraintException.class)
  public void testInsertException() {
    rs.insert(0, null);
  }

  @Test
  public void testInspect() {
    assertEquals("\"我\\b\\f\\n\\r\\t\\0abc]\\n\\177\\uea60\"",
        rs("我\b\f\n\r\t\0abc]\n\177\uea60").inspect().toS());
  }

  @Test
  public void testLines() {
    assertEquals(ra("a", "b", "", "c"), rs("a\nb\n\nc").lines());
  }

  @Test
  public void testLinesWithSeparator() {
    assertEquals(ra("a\n", "\nc"), rs("a\nb\n\nc").lines("b\n"));
  }

  @Test(expected = TypeConstraintException.class)
  public void testLinesWithSeparatorException() {
    rs("a\nb\n\nc").lines(null);
  }

  @Test
  public void testLjust() {
    assertEquals(rs("hello"), rs("hello").ljust(4));
    assertEquals(rs("hello               "), rs("hello").ljust(20));
  }

  @Test
  public void testLjustWithPadstr() {
    assertEquals(rs("hello123412341234123"), rs("hello").ljust(20, "1234"));
  }

  @Test(expected = TypeConstraintException.class)
  public void testLjustWithPadstrException() {
    rs("hello").ljust(20, null);
  }

  @Test
  public void testLstrip() {
    assertEquals(rs("hello  "), rs("  hello  ").lstrip());
  }

  @Test
  public void testLstripǃ() {
    rs = rs("  hello  ");
    assertSame(rs, rs.lstripǃ());
    assertEquals(rs("hello  "), rs);
    assertNull(rs.lstripǃ());
  }

  @Test
  public void testMatch() {
    Matcher matcher = rs.match("[a-z]");
    assertTrue(matcher instanceof Matcher);
    RubyArray<String> chars = rs.eachChar().toA();
    while (matcher.find()) {
      assertEquals(chars.shift(), matcher.group());
    }
    assertNull(rs.match("\\d"));
  }

  @Test(expected = TypeConstraintException.class)
  public void testMatchException() {
    rs.match(null);
  }

  @Test
  public void testMatchWithPosition() {
    Matcher matcher = rs.match("[a-z]", 1);
    assertTrue(matcher instanceof Matcher);
    RubyArray<String> chars = rs.eachChar().toA();
    chars.shift(1);
    while (matcher.find()) {
      assertEquals(chars.shift(), matcher.group());
    }
    assertNull(rs.match("\\d", 1));
    assertNull(rs.match("[a-z]", -rs.length() - 1));
    assertNull(rs.match("[a-z]", rs.length()));
  }

  @Test(expected = TypeConstraintException.class)
  public void testMatchWithPositionException() {
    rs.match(null, 1);
  }

  @Test
  public void testNext() {
    assertEquals(StringSuccessor.getInstance().succ(rs.toS()), rs.next().toS());
  }

  @Test
  public void testNextǃ() {
    RubyString succ = rs.next();
    assertSame(rs, rs.nextǃ());
    assertEquals(succ, rs);
  }

  @Test
  public void testOct() {
    assertEquals(83, rs("123").oct());
    assertEquals(-255, rs("-377").oct());
    assertEquals(0, rs("bad").oct());
    assertEquals(255, rs("0377bad").oct());
  }

  @Test
  public void testOrd() {
    assertEquals(97, rs("abc").ord());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOrdException() {
    rs("").ord();
  }

  @Test
  public void testPartition() {
    assertEquals(ra("he", "l", "lo"), rs("hello").partition("l"));
    assertEquals(ra("hello", "", ""), rs("hello").partition("x"));
  }

  @Test(expected = TypeConstraintException.class)
  public void testPartitionException() {
    rs.partition((String) null);
  }

  @Test
  public void testPartitionWithPattern() {
    assertEquals(ra("h", "el", "lo"), rs("hello").partition(qr(".l")));
    assertEquals(ra("hello", "", ""), rs("hello").partition(qr(".x")));
  }

  @Test(expected = TypeConstraintException.class)
  public void testPartitionWithPatternException() {
    rs.partition((Pattern) null);
  }

  @Test
  public void testPrepend() {
    assertSame(rs, rs.prepend("def"));
    assertEquals(rs("defabc"), rs);
  }

  @Test(expected = TypeConstraintException.class)
  public void testPrependException() {
    rs.prepend(null);
  }

  @Test
  public void testReplace() {
    assertSame(rs, rs.replace("def"));
    assertEquals(rs("def"), rs);
  }

  @Test(expected = TypeConstraintException.class)
  public void testReplaceException() {
    rs.replace(null);
  }

  @Test
  public void testReverse() {
    assertEquals(rs("cba"), rs.reverse());
  }

  @Test
  public void testReverseǃ() {
    assertSame(rs, rs.reverseǃ());
    assertEquals(rs("cba"), rs);
    assertEquals(rs("abc"), rs.reverseǃ());
  }

  @Test
  public void testLindex() {
    assertEquals((Integer) 1, rs("hello").rindex("e"));
    assertEquals((Integer) 4, rs("hello").rindex("o"));
    assertEquals(null, rs("hello").rindex("a"));
  }

  @Test(expected = TypeConstraintException.class)
  public void testLindexException() {
    rs.rindex((String) null);
  }

  @Test
  public void testLindexWithEnd() {
    assertEquals((Integer) 3, rs("hello").rindex("l", -2));
    assertEquals((Integer) 4, rs("hello").rindex("o", -1));
    assertEquals((Integer) 2, rs("hello").rindex("l", -3));
    assertEquals((Integer) 3, rs("hello").rindex("l", 100));
    assertNull(rs("hello").rindex("l", -100));
    assertEquals((Integer) 0, rs("hello").rindex("h", -5));
  }

  @Test(expected = TypeConstraintException.class)
  public void testLindexWithEndException() {
    rs.rindex((String) null, -1);
  }

  @Test
  public void testLindexWithPattern() {
    assertEquals((Integer) 1, rs("hello").rindex(qr("e")));
    assertEquals((Integer) 4, rs("hello").rindex(qr("o")));
    assertEquals(null, rs("hello").rindex(qr("a")));
  }

  @Test(expected = TypeConstraintException.class)
  public void testLindexWithPatternException() {
    rs.rindex((Pattern) null);
  }

  @Test
  public void testLindexWithPatternAndStopAt() {
    assertEquals((Integer) 3, rs("hello").rindex(qr("l"), -2));
    assertEquals((Integer) 4, rs("hello").rindex(qr("o"), -1));
    assertEquals((Integer) 2, rs("hello").rindex(qr("l"), -3));
    assertEquals((Integer) 3, rs("hello").rindex(qr("l"), 100));
    assertNull(rs("hello").rindex(qr("l"), -100));
    assertEquals((Integer) 0, rs("hello").rindex(qr("h"), -5));
  }

  @Test(expected = TypeConstraintException.class)
  public void testLindexWithPatternAndStopAtException() {
    rs.rindex((Pattern) null, -1);
  }

  @Test
  public void testRjust() {
    assertEquals(rs("hello"), rs("hello").rjust(4));
    assertEquals(rs("               hello"), rs("hello").rjust(20));
  }

  @Test
  public void testRjustWithPadstr() {
    assertEquals(rs("hello"), rs("hello").rjust(-1, "1234"));
    assertEquals(rs("123412341234123hello"), rs("hello").rjust(20, "1234"));
  }

  @Test
  public void testRpartition() {
    assertEquals(ra("hel", "l", "o"), rs("hello").rpartition("l"));
    assertEquals(ra("", "", "hello"), rs("hello").rpartition("x"));
  }

  @Test(expected = TypeConstraintException.class)
  public void testRpartitionException() {
    rs.rpartition((String) null);
  }

  @Test
  public void testRpartitionWithPattern() {
    assertEquals(ra("he", "ll", "o"), rs("hello").rpartition(qr(".l")));
    assertEquals(ra("", "", "hello"), rs("hello").rpartition(qr(".x")));
  }

  @Test(expected = TypeConstraintException.class)
  public void testRpartitionWithPatternException() {
    rs.rpartition((Pattern) null);
  }

  @Test
  public void testRstrip() {
    assertEquals(rs("  hello"), rs("  hello  ").rstrip());
    assertEquals(rs("hello"), rs("hello").rstrip());
  }

  @Test
  public void testRstripǃ() {
    rs = rs("  hello  ");
    assertSame(rs, rs.rstripǃ());
    assertEquals(rs("  hello"), rs);
    assertNull(rs.rstripǃ());
  }

  @Test
  public void testScan() {
    assertEquals(ra("cruel", "world"), rs("cruel world").scan("\\w+"));
    assertEquals(ra("cru", "el ", "wor"), rs("cruel world").scan("..."));
  }

  @Test(expected = TypeConstraintException.class)
  public void testScanException() {
    rs.scan(null);
  }

  @Test
  public void testScanWithBlock() {
    final RubyArray<String> strs = newRubyArray();
    rs = rs("cruel world");
    assertSame(rs, rs.scan("\\w+", item -> strs.add(item)));
    assertEquals(ra("cruel", "world"), strs);
  }

  @Test(expected = TypeConstraintException.class)
  public void testScanWithBlockException() {
    rs.scan(null, null);
  }

  @Test
  public void testScanGroups() {
    assertEquals(ra(ra("cru"), ra("el "), ra("wor")),
        rs("cruel world").scanGroups("(...)"));
    assertEquals(ra(ra("cr", "ue"), ra("l ", "wo")),
        rs("cruel world").scanGroups("(..)(..)"));
    assertEquals(ra(ra("cru"), ra("el "), ra("wor")),
        rs("cruel world").scanGroups("..."));
  }

  @Test(expected = TypeConstraintException.class)
  public void testScanGroupsException() {
    rs.scanGroups(null);
  }

  @Test
  public void testScanGroupsWithBlock() {
    final RubyArray<String> strs = newRubyArray();
    rs = rs("cruel world");
    assertSame(rs, rs.scanGroups("(...)", item -> strs.concat(item)));
    assertEquals(ra("cru", "el ", "wor"), strs);
  }

  @Test(expected = TypeConstraintException.class)
  public void testScanGroupsWithBlockException() {
    rs.scanGroups(null, null);
  }

  @Test
  public void testScrub() {
    assertEquals(rs("abcあ�"), rs("abc\u3042\0").scrub());
    assertEquals(rs("abcあ\uFFFD"), rs("abc\u3042\0").scrub());
  }

  @Test
  public void testScrubWithReplacement() {
    assertEquals(rs("abcあ!"), rs("abc\u3042\0").scrub("!"));
    assertEquals(rs("abc\u3042\0").scrub(),
        rs("abc\u3042\0").scrub((String) null));
  }

  @Test
  public void testScrubWithBlock() {
    assertEquals(rs("abcあ?"), rs("abc\u3042\0").scrub(item -> {
      assertEquals(1, item.size());
      assertEquals(new Byte((byte) 0), item.get(0));
      return "?";
    }));
  }

  @Test
  public void testScrubǃ() {
    rs = rs("abc\u3042\0");
    assertSame(rs, rs.scrubǃ());
    assertEquals(rs("abcあ�"), rs);
    assertNull(rs.scrubǃ());
  }

  @Test
  public void testScrubǃWithReplacement() {
    rs = rs("abc\u3042\0");
    assertSame(rs, rs.scrubǃ("!"));
    assertEquals(rs("abcあ!"), rs);
    assertNull(rs.scrubǃ("!"));
  }

  @Test
  public void testScrubǃWithBlock() {
    Function<RubyArray<Byte>, String> block = item -> {
      assertEquals(1, item.size());
      assertEquals(new Byte((byte) 0), item.get(0));
      return "?";
    };
    rs = rs("abc\u3042\0");
    assertSame(rs, rs.scrubǃ(block));
    assertEquals(rs("abcあ?"), rs);
    assertNull(rs.scrubǃ("?"));
  }

  @Test
  public void testSetbyte() {
    assertEquals((byte) 97, rs.setbyte(2, (byte) 97));
    assertEquals(rs("aba"), rs);
    assertEquals((byte) 97, rs.setbyte(-2, (byte) 97));
    assertEquals(rs("aaa"), rs);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testSetbyteException1() {
    rs.setbyte(3, (byte) 97);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testSetbyteException2() {
    rs.setbyte(-4, (byte) 97);
  }

  @Test
  public void testSize() {
    assertEquals(rs.length(), rs.size());
  }

  @Test
  public void testSlice() {
    assertNull(rs.slice(3));
    assertNull(rs.slice(-4));
    assertEquals(rs("a"), rs.slice(0));
    assertEquals(rs("c"), rs.slice(-1));
  }

  @Test
  public void testSliceWithLength() {
    assertNull(rs.slice(3, 2));
    assertNull(rs.slice(-4, 2));
    assertEquals(rs("ab"), rs.slice(0, 2));
    assertEquals(rs("c"), rs.slice(-1, 2));
  }

  @Test
  public void testSliceWithPattern() {
    assertNull(rs.slice(qr("d")));
    assertEquals(rs("ab"), rs.slice(qr("[a-b]+")));
  }

  @Test(expected = TypeConstraintException.class)
  public void testSliceWithPatternException() {
    rs.slice((Pattern) null);
  }

  @Test
  public void testSliceWithPatternAndGroupNumber() {
    assertNull(rs.slice(qr("d"), 1));
    assertNull(rs.slice(qr("a"), 1));
    assertNull(rs.slice(qr("a"), 0));
    assertEquals(rs("ab"), rs.slice(qr("([a-b]+)"), 1));
  }

  @Test
  public void testSliceWithMatchStr() {
    assertNull(rs.slice("d"));
    assertEquals(rs("bc"), rs.slice("bc"));
  }

  @Test
  public void testSliceǃ() {
    assertNull(rs.sliceǃ(3));
    assertNull(rs.sliceǃ(-4));
    assertEquals(rs("a"), rs.sliceǃ(0));
    assertEquals(rs("bc"), rs);
  }

  @Test
  public void testSliceǃWithLength() {
    assertNull(rs.sliceǃ(3, 2));
    assertNull(rs.sliceǃ(-4, 2));
    assertEquals(rs("ab"), rs.sliceǃ(0, 2));
    assertEquals(rs("c"), rs);
  }

  @Test
  public void testSliceǃWithPattern() {
    assertNull(rs.sliceǃ(qr("d")));
    assertEquals(rs("ab"), rs.sliceǃ(qr("[a-b]+")));
    assertEquals(rs("c"), rs);
  }

  @Test(expected = TypeConstraintException.class)
  public void testSliceǃWithPatternException() {
    rs.sliceǃ((Pattern) null);
  }

  @Test
  public void testSliceǃWithPatternAndGroupNumber() {
    assertNull(rs.sliceǃ(qr("d"), 1));
    assertNull(rs.sliceǃ(qr("a"), 1));
    assertNull(rs.sliceǃ(qr("a"), 0));
    assertEquals(rs("ab"), rs.sliceǃ(qr("([a-b]+)"), 1));
    assertEquals(rs("c"), rs);
  }

  @Test
  public void testSliceǃWithMatchStr() {
    assertNull(rs.sliceǃ("d"));
    assertEquals(rs("bc"), rs.sliceǃ("bc"));
    assertEquals(rs("a"), rs);
  }

  @Test
  public void testSplit() {
    assertEquals(ra("a", "bc", "def"), rs("  a   bc   def ").split());
  }

  @Test
  public void testSplitWithDelimiter() {
    assertEquals(ra("a", "bc", "def"), rs("  a   bc   def ").split(" "));
    assertEquals(ra("a", "bc", "def"),
        rs("  a   bc   def ").split((String) null));
    assertEquals(ra("", "a", " bc", " def "),
        rs("  a   bc   def ").split("  "));
  }

  @Test
  public void testSplitWithDelimiterAndLimit() {
    assertEquals(ra("a", "bc", "def"), rs("  a   bc   def ").split(" ", 0));
    assertEquals(ra("a", "bc", "def"),
        rs("  a   bc   def ").split((String) null, 0));
    assertEquals(ra("a", "bc", "def"), rs("  a   bc   def ").split(" ", -1));
    assertEquals(ra("  a   bc   def "), rs("  a   bc   def ").split(" ", 1));
    assertEquals(ra("a", "bc   def "), rs("  a   bc   def ").split(" ", 2));
    assertEquals(ra("", "a   bc   def "), rs("  a   bc   def ").split("  ", 2));
    assertEquals(ra("", "a", " bc", " def "),
        rs("  a   bc   def ").split("  ", 0));
  }

  @Test
  public void testSplitWithPatternAndDelimiter() {
    assertEquals(ra("", "a", "bc", "def"),
        rs("  a   bc   def ").split(qr(" +")));
    assertEquals(ra("a", "bc", "def"),
        rs("  a   bc   def ").split((Pattern) null));
    assertEquals(ra("", "a", "bc", "def"),
        rs("  a   bc   def ").split(qr(" +")));
  }

  @Test
  public void testSplitWithPatternAndDelimiterAndLimit() {
    assertEquals(ra("", "a", "bc", "def"),
        rs("  a   bc   def ").split(qr(" +"), 0));
    assertEquals(ra("a", "bc", "def"),
        rs("  a   bc   def ").split((Pattern) null, 0));
    assertEquals(ra("", "a", "bc", "def"),
        rs("  a   bc   def ").split(qr(" +"), -1));
    assertEquals(ra("  a   bc   def "),
        rs("  a   bc   def ").split(qr(" +"), 1));
    assertEquals(ra("", "a   bc   def "),
        rs("  a   bc   def ").split(qr(" +"), 2));
    assertEquals(ra("", "a   bc   def "),
        rs("  a   bc   def ").split(qr(" {2,2}"), 2));
    assertEquals(ra("", "a", " bc", " def "),
        rs("  a   bc   def ").split(qr(" {2,2}"), 0));
  }

  @Test
  public void testSqueeze() {
    assertEquals(rs("yelow mon"), rs("yellow moon").squeeze());
  }

  @Test
  public void testSqueezeWithCharSet() {
    assertEquals(rs(" now is the"), rs("  now   is  the").squeeze(" "));
    assertEquals(rs("puters shot balls"),
        rs("putters shoot balls").squeeze("m-z"));
  }

  @Test(expected = TypeConstraintException.class)
  public void testSqueezeWithCharSetException() {
    rs("  now   is  the").squeeze(null);
  }

  @Test
  public void testSqueezeǃ() {
    rs = rs("yellow moon");
    assertSame(rs, rs.squeezeǃ());
    assertEquals(rs("yelow mon"), rs);
    assertNull(rs.squeezeǃ());
  }

  @Test
  public void testSqueezeǃWithCharSetException() {
    rs = rs("  now   is  the");
    assertSame(rs, rs.squeezeǃ(" "));
    assertEquals(rs(" now is the"), rs);
    assertNull(rs.squeezeǃ(" "));
  }

  @Test
  public void testStartWithʔ() {
    assertTrue(rs("hello").startWithʔ("hell"));
    assertTrue(rs("hello").startWithʔ("heaven", "hell"));
    assertFalse(rs("hello").startWithʔ("heaven", "paradise"));
    assertTrue(rs("hello").startWithʔ("hell", (String[]) null));
  }

  @Test(expected = TypeConstraintException.class)
  public void testStartWithʔException() {
    rs("hello").startWithʔ(null);
  }

  @Test
  public void testStrip() {
    assertEquals(rs("goodbye"), rs("\tgoodbye\r\n").strip());
  }

  @Test
  public void testStripǃ() {
    rs = rs("\tgoodbye\r\n");
    assertSame(rs, rs.stripǃ());
    assertEquals(rs("goodbye"), rs);
    assertNull(rs.stripǃ());
  }

  @Test
  public void testSub() {
    assertEquals(rs("h*llo"), rs("hello").sub("[aeiou]", "*"));
    assertEquals(rs("h<e>llo"), rs("hello").sub("([aeiou])", "<$1>"));
  }

  @Test(expected = TypeConstraintException.class)
  public void testSubException1() {
    rs("hello").sub(null, "*");
  }

  @Test(expected = TypeConstraintException.class)
  public void testSubException2() {
    rs("hello").sub("[aeiou]", (String) null);
  }

  @Test
  public void testSubWithMap() {
    assertEquals(rs("h0llo"), rs("hello").sub("[aeiou]", rh("e", 0)));
    assertEquals(rs("hello"), rs("hello").sub("[aeiou]", rh("a", 1)));
    assertEquals(rs("hello"), rs("hello").sub("x", rh("a", 1)));
    assertEquals(rs("hello"),
        rs("hello").sub("e", new HashMap<String, Object>()));
  }

  @Test(expected = TypeConstraintException.class)
  public void testSubWithMapException() {
    rs.sub(null, rh("a", 1));
  }

  @Test
  public void testSubWithBlock() {
    Function<String, String> block = item -> "0";
    assertEquals(rs("h0llo"), rs("hello").sub("[aeiou]", block));
    assertEquals(rs("hello"), rs("hello").sub("x", block));
  }

  @Test(expected = TypeConstraintException.class)
  public void testSubWithBlockException() {
    rs.sub(null, (Function<String, String>) null);
  }

  @Test
  public void testSubǃ() {
    rs = rs("hello");
    assertSame(rs, rs.subǃ("e", "*"));
    assertEquals(rs("h*llo"), rs);
    assertNull(rs.subǃ("e", "*"));
  }

  @Test(expected = TypeConstraintException.class)
  public void testSubǃException1() {
    rs("hello").subǃ(null, "*");
  }

  @Test(expected = TypeConstraintException.class)
  public void testSubǃException2() {
    rs("hello").subǃ("[aeiou]", (String) null);
  }

  @Test
  public void testSubǃWithBlock() {
    Function<String, String> block = item -> "0";
    rs = rs("hello");
    assertSame(rs, rs.subǃ("e", block));
    assertEquals(rs("h0llo"), rs);
    assertNull(rs.subǃ("e", block));
  }

  @Test(expected = TypeConstraintException.class)
  public void testSubǃWithBlockException() {
    rs.subǃ(null, (Function<String, String>) null);
  }

  @Test
  public void testSucc() {
    assertEquals(rs("abce"), rs("abcd").succ());
    assertEquals(rs("THX1139"), rs("THX1138").succ());
    assertEquals(rs("<<koalb>>"), rs("<<koala>>").succ());
    assertEquals(rs("2000aaa"), rs("1999zzz").succ());
    assertEquals(rs("AAAA0000"), rs("ZZZ9999").succ());
    assertEquals(rs("**+"), rs("***").succ());
  }

  @Test
  public void testSuccǃ() {
    assertSame(rs, rs.succǃ());
    assertEquals(rs("abd"), rs);
  }

  @Test
  public void testSum() {
    assertEquals(805, rs("abc我").sum());
  }

  @Test
  public void testSwapcase() {
    assertEquals(rs("hELLO"), rs("Hello").swapcase());
    assertEquals(rs("CyBeR_pUnK11"), rs("cYbEr_PuNk11").swapcase());
  }

  @Test
  public void testSwapcaseǃ() {
    rs = rs("Hello");
    assertSame(rs, rs.swapcaseǃ());
    assertEquals(rs("hELLO"), rs);
    assertEquals(rs("Hello"), rs.swapcaseǃ());
    assertNull(rs("!").swapcaseǃ());
  }

  @Test
  public void testToF() {
    assertEquals((Double) 0d, (Double) rs("dhd").toF());
    assertEquals((Double) (-0.0253), (Double) rs("  -  .253e-1 ").toF());
    assertEquals((Double) (0.0253), (Double) rs("  +  .253E-1 ").toF());
  }

  @Test
  public void testToI() {
    assertEquals((Integer) 0, (Integer) rs("fbdh").toI());
    assertEquals((Integer) (-123), (Integer) rs("  -  123 ").toI());
    assertEquals((Integer) 456, (Integer) rs("  +  456 ").toI());
  }

  @Test
  public void testToIWithRadix() {
    assertEquals((Integer) 12345, (Integer) rs("12345").toI());
    assertEquals((Integer) 99, (Integer) rs("99 red balloons").toI());
    assertEquals((Integer) 0, (Integer) rs("0a").toI());
    assertEquals((Integer) 10, (Integer) rs("0a").toI(16));
    assertEquals((Integer) 0, (Integer) rs("hello").toI());
    assertEquals((Integer) 101, (Integer) rs(" + 1100101").toI(2));
    assertEquals((Integer) 294977, (Integer) rs("1100101").toI(8));
    assertEquals((Integer) rs("1100101").toI(8),
        (Integer) rs("11001019").toI(8));
    assertEquals((Integer) rs("11001").toI(8), (Integer) rs("110019").toI(8));
    assertEquals((Integer) 1100101, (Integer) rs("1100101").toI(10));
    assertEquals((Integer) 17826049, (Integer) rs("1100101").toI(16));
    assertEquals((Integer) rs("1100101F").toI(16),
        (Integer) rs("1100101fg").toI(16));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToIWithRadixException1() {
    rs.toI(1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToIWithRadixException2() {
    rs.toI(37);
  }

  @Test
  public void testToIWithRadixException3() {
    assertEquals((Integer) 0,
        (Integer) rs("fffffffffffffffffffffffffffffffffffff").toI(16));
  }

  @Test
  public void testToS() {
    assertEquals("abc", rs.toS());
  }

  @Test
  public void testToStr() {
    assertEquals("abc", rs.toStr());
  }

  @Test
  public void testTr() {
    assertEquals(rs("hippo"), rs("hello").tr("el", "ip"));
    assertEquals(rs("h*ll*"), rs("hello").tr("aeiou", "*"));
    assertEquals(rs("hAll*"), rs("hello").tr("aeiou", "AA*"));
    assertEquals(rs("ifmmp"), rs("hello").tr("a-y", "b-z"));
    assertEquals(rs("*e**o"), rs("hello").tr("^aeiou", "*"));
    assertEquals(rs("h*ll**w*rld"), rs("hello^world").tr("\\^aeiou", "*"));
    assertEquals(rs("h*ll**w*rld"), rs("hello-world").tr("a\\-eo", "*"));
    assertEquals(rs("hello\nworld"), rs("hello\r\nworld").tr("\r", ""));
    assertEquals(rs("hello\r\nwold").inspect(),
        rs("hello\r\nworld").tr("\\r", "").inspect());
    assertEquals(rs("hello\nworld"), rs("hello\r\nworld").tr("\\\r", ""));
    assertEquals(rs("['b']"), rs("X['\\b']").tr("X\\", ""));
    assertEquals(rs("'b'"), rs("X['\\b']").tr("X-\\]", ""));
    assertEquals(rs("ell"), rs("hello").tr("^el", ""));
  }

  @Test(expected = TypeConstraintException.class)
  public void testTrException1() {
    rs.tr(null, "");
  }

  @Test(expected = TypeConstraintException.class)
  public void testTrException2() {
    rs.tr("", null);
  }

  @Test
  public void testTrǃ() {
    rs = rs("hello");
    assertSame(rs, rs.trǃ("el", "ip"));
    assertEquals(rs("hippo"), rs);
    assertNull(rs.trǃ("el", "ip"));
  }

  @Test
  public void testTrS() {
    assertEquals(rs("hero"), rs("hello").trS("l", "r"));
    assertEquals(rs("h*o"), rs("hello").trS("el", "*"));
    assertEquals(rs("hhxo"), rs("hello").trS("el", "hx"));
    assertEquals(rs("xellx"), rs("hello").trS("^el", "hx"));
    assertEquals(rs("ell"), rs("hello").trS("^el", ""));
  }

  @Test(expected = TypeConstraintException.class)
  public void testTrSException1() {
    rs.trS(null, "");
  }

  @Test(expected = TypeConstraintException.class)
  public void testTrSException2() {
    rs.trS("", null);
  }

  @Test
  public void testTrSǃ() {
    rs = rs("hello");
    assertSame(rs, rs.trSǃ("l", "r"));
    assertEquals(rs("hero"), rs);
    assertNull(rs.trSǃ("l", "r"));
  }

  @Test
  public void testUnpack() {
    assertEquals(ra("abc", "abc "), rs("abc \0\0abc \0\0").unpack("A6Z6"));
    assertEquals(ra("abc", " \000\000"), rs("abc \0\0").unpack("a3a3"));
    assertEquals(ra("abc ", "abc "), rs("abc \0abc \0").unpack("Z*Z*"));
    assertEquals(ra("10000110", "01100001"), rs("aa").unpack("b8B8"));
    assertEquals(ra("16", "61", (byte) 97), rs("aaa").unpack("h2H2c"));
  }

  @Test(expected = TypeConstraintException.class)
  public void testUnpackException() {
    rs.unpack(null);
  }

  @Test
  public void testUpcase() {
    assertEquals(rs("ABC"), rs.upcase());
  }

  @Test
  public void testUpcaseǃ() {
    assertSame(rs, rs.upcaseǃ());
    assertEquals(rs("ABC"), rs);
    assertNull(rs.upcaseǃ());
  }

  @Test
  public void testUpto() {
    assertEquals(ra("9", "10", "11"), rs("9").upto("11").toA());
    assertEquals(ra(), rs("25").upto("5").toA());
    assertEquals(ra("07", "08", "09", "10", "11"), rs("07").upto("11").toA());
  }

  @Test(expected = TypeConstraintException.class)
  public void testUptoException() {
    rs.upto(null);
  }

  @Test
  public void testUptoWithExclusive() {
    assertEquals(ra("9", "10", "11"), rs("9").upto("11", false).toA());
    assertEquals(ra("9", "10"), rs("9").upto("11", true).toA());
  }

  @Test(expected = TypeConstraintException.class)
  public void testUptoWithExclusiveException() {
    rs.upto(null, false);
  }

  @Test
  public void testUptoWithBlock() {
    final RubyArray<String> strs = newRubyArray();
    assertSame(rs, rs.upto("abe", item -> strs.add(item)));
    assertEquals(ra("abc", "abd", "abe"), strs);
  }

  @Test(expected = TypeConstraintException.class)
  public void testUptoWithBlockException() {
    rs.upto(null, (Consumer<String>) null);
  }

  @Test
  public void testUptoWithExclusiveAndBlock() {
    final RubyArray<String> strs = newRubyArray();
    assertSame(rs, rs.upto("abe", true, item -> strs.add(item)));
    assertEquals(ra("abc", "abd"), strs);
  }

  @Test(expected = TypeConstraintException.class)
  public void testUptoWithExclusiveAndBlockException() {
    rs.upto(null, true, (Consumer<String>) null);
  }

  @Test
  public void testValidEncodingʔ() {
    String test1 = "Itâ€™s a string with â€˜unknownâ€˜).";
    String test2 = "It’s a string with ‘unknown‘).";
    assertTrue(rs(test1).validEncodingʔ("Windows-1252"));
    assertFalse(rs(test2).validEncodingʔ("Windows-1252"));
  }

  @Test(expected = TypeConstraintException.class)
  public void testValidEncodingʔException() {
    rs.validEncodingʔ(null);
  }

  @Test
  public void testCharSequence() {
    assertEquals(rs.toS().length(), rs.length());
    assertEquals(rs.toS().charAt(0), rs.charAt(0));
    assertEquals(rs.toS().subSequence(0, 1), rs.subSequence(0, 1));
  }

  @Test
  public void testComparable() {
    assertEquals(rs.toS().compareTo("def"), rs.compareTo("def"));
    assertEquals(ra(rs("a"), rs("b"), rs("c")),
        ra(rs("c"), rs("b"), rs("a")).sort());
  }

}
