/*
 *
 * Copyright 2016 Wei-Ming Wu
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
package net.sf.rubycollect4j.extension;

import static net.sf.rubycollect4j.RubyCollections.*;
import static net.sf.rubycollect4j.RubyLiterals.qr;
import static org.junit.jupiter.api.Assertions.*;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.succ.StringSuccessor;

public class RubyStringsTest {

  String rs;

  @BeforeEach
  public void setUp() throws Exception {
    rs = "abc";
  }

  @Test
  public void testPrivateConstructor() throws Exception {
    Constructor<RubyStrings> c = RubyStrings.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c.getModifiers()));
    c.setAccessible(true);
    c.newInstance();
  }

  @Test
  public void testAsciiOnlyʔ() {
    assertTrue(RubyStrings.asciiOnlyʔ("abc"));
    assertFalse(RubyStrings.asciiOnlyʔ("我abc"));
  }

  @Test
  public void testB() {
    assertEquals("æabc", RubyStrings.toS(RubyStrings.b("我abc")));
  }

  @Test
  public void testBytes() {
    assertEquals(ra((byte) 97, (byte) 98, (byte) 99), RubyStrings.bytes("abc"));
  }

  @Test
  public void testBytesize() {
    assertEquals(3, RubyStrings.bytesize("我"));
  }

  @Test
  public void testByteslice() {
    assertEquals("\346", RubyStrings.byteslice("我", 0));
    assertEquals("\210", RubyStrings.byteslice("我", 1));
    assertEquals("\221", RubyStrings.byteslice("我", 2));
    assertEquals("el", RubyStrings.byteslice("hello", 1, 2));
    assertNull(RubyStrings.byteslice("hello", 5));
    assertNull(RubyStrings.byteslice("hello", 5, -1));
  }

  @Test
  public void testCapitalize() {
    assertEquals("Abcde", RubyStrings.capitalize("abCde"));
    assertEquals("", RubyStrings.capitalize(""));
  }

  @Test
  public void testCasecmp() {
    assertEquals(rs.compareToIgnoreCase("ABC"), RubyStrings.casecmp(rs, "ABC"));
    assertEquals(rs.compareToIgnoreCase("def"), RubyStrings.casecmp(rs, "def"));
  }

  @Test
  public void testCenter() {
    assertEquals("hello", RubyStrings.center("hello", 4));
    assertEquals("       hello        ", RubyStrings.center("hello", 20));
  }

  @Test
  public void testCenterException() {
    assertThrows(IllegalArgumentException.class, () -> {
      RubyStrings.center("hello", 4, "");
    });
  }

  @Test
  public void testCenterWithPadstr() {
    assertEquals("1231231hello12312312", RubyStrings.center("hello", 20, "123"));
    assertEquals("12312312311231231231", RubyStrings.center("", 20, "123"));
  }

  @Test
  public void testChomp() {
    assertEquals("hello", RubyStrings.chomp("hello"));
    assertEquals("hello", RubyStrings.chomp("hello\n"));
    assertEquals("hello", RubyStrings.chomp("hello\r\n"));
    assertEquals("hello\n", RubyStrings.chomp("hello\n\r"));
    assertEquals("hello", RubyStrings.chomp("hello\r"));
    assertEquals("hello \n there", RubyStrings.chomp("hello \n there"));
    assertEquals("he", RubyStrings.chomp("hello", "llo"));
  }

  @Test
  public void testChompWithSeparator() {
    assertEquals("abc", RubyStrings.chomp("abc.", "."));
    assertEquals("abc", RubyStrings.chomp("abc", "."));
    assertEquals("abc", RubyStrings.chomp("abc", null));
  }

  @Test
  public void testChop() {
    assertEquals("", RubyStrings.chop(""));
    assertEquals("abc", RubyStrings.chop("abc\r\n"));
    assertEquals("abc\r\n", RubyStrings.chop("abc\r\n\r"));
    assertEquals("ab", RubyStrings.chop("abc"));
  }

  @Test
  public void testChr() {
    assertEquals("a", RubyStrings.chr(rs));
    assertEquals("", RubyStrings.chr(""));
  }

  @Test
  public void testClear() {
    assertEquals("", RubyStrings.clear(rs));
  }

  @Test
  public void testCodepoints() {
    assertEquals(ra(97, 98, 99, 25105), RubyStrings.codepoints("abc我"));
  }

  @Test
  public void testConcat() {
    assertEquals("abc我", RubyStrings.concat(rs, 25105));
    assertEquals("null", RubyStrings.concat("", null));
  }

  @Test
  public void testConcatWithObject() {
    assertEquals("abc[1, 2, 3]", RubyStrings.concat(rs, ra(1, 2, 3)));
  }

  @Test
  public void testCount() {
    rs = "hello world";
    assertEquals(5, RubyStrings.count(rs, "lo"));
    assertEquals(2, RubyStrings.count(rs, "lo", "o"));
    assertEquals(5, RubyStrings.count(rs, "lo", (String[]) null));
    assertEquals(4, RubyStrings.count(rs, "hello", "^l"));
    assertEquals(4, RubyStrings.count(rs, "ej-m"));
    assertEquals(4, RubyStrings.count("hello^world", "\\^aeiou"));
    assertEquals(4, RubyStrings.count("hello-world", "a\\-eo"));
    rs = "hello world\\r\\n";
    assertEquals(2, RubyStrings.count(rs, "\\"));
    assertEquals(0, RubyStrings.count(rs, "\\A"));
    assertEquals(3, RubyStrings.count(rs, "X-\\w"));
  }

  @Test
  public void testCountException1() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.count(rs, (String) null);
    });
  }

  @Test
  public void testCountException2() {
    assertThrows(IllegalArgumentException.class, () -> {
      RubyStrings.count(rs, "x-a");
    });
  }

  @Test
  public void testCrypt() {
    String encrypt = rs + "secret";
    String md5 = null;
    MessageDigest digest;
    try {
      digest = MessageDigest.getInstance("SHA-256");
      digest.update(encrypt.getBytes("UTF-8"), 0, encrypt.length());
      md5 = new BigInteger(1, digest.digest()).toString(16);
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {}
    assertEquals(md5, RubyStrings.crypt(rs, "secret"));
  }

  @Test
  public void testCryptException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.crypt(rs, null);
    });
  }

  @Test
  public void testDelete() {
    assertEquals("b", RubyStrings.delete("abc", "ac"));
    assertEquals("a", RubyStrings.delete("abc", "^a"));
    assertEquals("abc", RubyStrings.delete("abc^", "\\^"));
    assertEquals("d", RubyStrings.delete("abcd", "a-c"));
  }

  @Test
  public void testDeleteException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.delete(rs, null);
    });
  }

  @Test
  public void testDowncase() {
    assertEquals("abc", RubyStrings.downcase("ABC"));
  }

  @Test
  public void testDump() {
    assertEquals("\"\\u6211\\b\\f\\n\\r\\t\\0abc]\\n\\177\\uea60\"",
        RubyStrings.dump("我\b\f\n\r\t\0abc]\n\177\uea60"));
  }

  @Test
  public void testEachBytes() {
    assertEquals(ra((byte) 'a', (byte) 'b', (byte) 'c', (byte) '\n', "我".getBytes()[0],
        "我".getBytes()[1], "我".getBytes()[2]), RubyIterables.toA(RubyStrings.eachByte("abc\n我")));
  }

  @Test
  public void testEachBytesWithBlock() {
    final RubyArray<Integer> ints = newRubyArray();
    assertSame(rs, RubyStrings.eachByte(rs, item -> ints.add((int) item)));
    assertEquals(ra(97, 98, 99), ints);
  }

  @Test
  public void testEachChars() {
    assertEquals(ra("a", "b", "c", "\n", "我"), RubyIterables.toA(RubyStrings.eachChar("abc\n我")));
  }

  @Test
  public void testEachCharsWithBlock() {
    final RubyArray<String> chars = newRubyArray();
    assertSame(rs, RubyStrings.eachChar(rs, item -> chars.add(item)));
    assertEquals(ra("a", "b", "c"), chars);
  }

  @Test
  public void testEachCodepoint() {
    assertEquals(ra(97, 98, 99, 25105), RubyIterables.toA(RubyStrings.eachCodepoint("abc我")));
  }

  @Test
  public void testEachCodepointWithBlock() {
    final RubyArray<Integer> ints = newRubyArray();
    assertSame(rs, RubyStrings.eachCodepoint(rs, item -> ints.add(item)));
    assertEquals(ra(97, 98, 99), ints);
  }

  @Test
  public void testEachLine() {
    assertEquals(ra("a", "\r", "bc"), RubyIterables.toA(RubyStrings.eachLine("a\n\r\nbc\n")));
  }

  @Test
  public void testEachLineWithBlock() {
    rs = "a\n\r\nbc\n";
    final RubyArray<String> lines = newRubyArray();
    assertSame(rs, RubyStrings.eachLine(rs, item -> lines.add(item)));
    assertEquals(ra("a", "\r", "bc"), lines);
  }

  @Test
  public void testEachLineWithSeparator() {
    assertEquals(ra("a\n", "\nbc\n"), RubyIterables.toA(RubyStrings.eachLine("a\n\r\nbc\n", "\r")));
  }

  @Test
  public void testEachLineWithSeparatorException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.eachLine(rs, (String) null);
    });
  }

  @Test
  public void testEachLineWithSeparatorAndBlock() {
    rs = "a\n\r\nbc\n";
    final RubyArray<String> lines = newRubyArray();
    assertSame(rs, RubyStrings.eachLine(rs, "\r", item -> lines.add(item)));
    assertEquals(ra("a\n", "\nbc\n"), lines);
  }

  @Test
  public void testEachLineWithSeparatorAndBlockException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.eachLine(rs, (String) null, item -> {});
    });
  }

  @Test
  public void testEmptyʔ() {
    assertFalse(RubyStrings.emptyʔ(rs));
    rs = "";
    assertTrue(RubyStrings.emptyʔ(rs));
  }

  @Test
  public void testEncode() {
    assertEquals("æ", RubyStrings.encode("我", "ISO-8859-1"));
  }

  @Test
  public void testEncodeException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.encode(rs, null);
    });
  }

  @Test
  public void testEncodeWithDestinationAndSourceEncoding() {
    assertEquals("æ", RubyStrings.encode("我", "ISO-8859-1", "UTF-8"));
  }

  @Test
  public void testEncodeExceptionWithNullDestinationEncoding() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.encode(rs, null, "UTF-8");
    });
  }

  @Test
  public void testEncodeExceptionWithNullSourceEncoding() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.encode(rs, "ISO-8859-1", null);
    });
  }

  @Test
  public void testEndWithʔ() {
    assertTrue(RubyStrings.endWithʔ(rs, "c"));
    assertTrue(RubyStrings.endWithʔ(rs, "b", "a", "c"));
    assertFalse(RubyStrings.endWithʔ(rs, "a"));
    assertTrue(RubyStrings.endWithʔ(rs, "c", (String[]) null));
  }

  @Test
  public void testEndWithʔException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.endWithʔ(rs, null);
    });
  }

  @Test
  public void testForceEncoding() {
    assertEquals("æ", RubyStrings.forceEncoding("我", "ISO-8859-1"));
    assertEquals(rs, RubyStrings.forceEncoding(rs, "ISO-8859-1"));
  }

  @Test
  public void testGetbyte() {
    assertEquals((Byte) "我".getBytes()[0], RubyStrings.getbyte("我", 0));
    assertEquals((Byte) "我".getBytes()[1], RubyStrings.getbyte("我", 1));
    assertEquals((Byte) "我".getBytes()[2], RubyStrings.getbyte("我", 2));
    assertNull(RubyStrings.getbyte("我", 3));
    assertEquals((Byte) "我".getBytes()[2], RubyStrings.getbyte("我", -1));
  }

  @Test
  public void testGsub() {
    assertEquals("ab77c77", RubyStrings.gsub("ab4c56", "\\d+", "77"));
  }

  @Test
  public void testGsubException1() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.gsub("ab4c56", null, "77");
    });
  }

  @Test
  public void testGsubException2() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.gsub("ab4c56", "\\d+", (String) null);
    });
  }

  @Test
  public void testGsubWithMap() {
    assertEquals("0ab88c99", RubyStrings.gsub("0ab4c56", "\\d+", rh("4", "88", "56", "99")));
    assertEquals(rs, RubyStrings.gsub(rs, "\\d+", (Map<String, ?>) null));
    // assertNotSame(rs, RubyStrings.gsub(rs, "\\d+", (Map<String, ?>) null));
  }

  @Test
  public void testGsubWithMapException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.gsub("0ab4c56", null, rh("4", "88", "56", "99"));
    });
  }

  @Test
  public void testGsubWithBlock() {
    assertEquals("ab40c560", RubyStrings.gsub("ab4c56", "\\d+", item -> item + "0"));
  }

  @Test
  public void testGsubWithBlockException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.gsub("ab4c56", null, item -> item + "0");
    });
  }

  @Test
  public void testGsubWithoutReplacement() {
    assertEquals(ra("4", "56"), RubyIterables.toA(RubyStrings.gsub("ab4c56", "\\d+")));
  }

  @Test
  public void testGsubWithoutReplacementException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.gsub("ab4c56", null);
    });
  }

  @Test
  public void testHex() {
    assertEquals(10, RubyStrings.hex("0x0a"));
    assertEquals(-4660, RubyStrings.hex("-1234"));
    assertEquals(0, RubyStrings.hex("0"));
    assertEquals(0, RubyStrings.hex("haha"));
  }

  @Test
  public void testIndex() {
    assertEquals((Integer) 1, RubyStrings.index(rs, "bc"));
    assertNull(RubyStrings.index(rs, "def"));
  }

  @Test
  public void testIndexException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.index(rs, (String) null);
    });
  }

  @Test
  public void testIndexWithOffset() {
    assertEquals((Integer) 1, RubyStrings.index(rs, "bc", 1));
    assertNull(RubyStrings.index(rs, "ab", 100));
  }

  @Test
  public void testIndexWithOffsetException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.index(rs, (String) null, 100);
    });
  }

  @Test
  public void testIndexWithPattern() {
    assertEquals((Integer) 2, RubyStrings.index(rs, qr("[c-z]+")));
    assertNull(RubyStrings.index(rs, qr("[d-z]+")));
  }

  @Test
  public void testIndexWithPatternException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.index(rs, (Pattern) null);
    });
  }

  @Test
  public void testIndexWithPatternAndOffset() {
    assertEquals((Integer) 2, RubyStrings.index(rs, qr("[c-z]+"), 1));
    assertNull(RubyStrings.index(rs, qr("[c-z]+"), rs.length()));
    assertNull(RubyStrings.index(rs, qr("[c-z]+"), -100));
    assertNull(RubyStrings.index(rs, qr("[c-z]+"), 100));
  }

  @Test
  public void testIndexWithPatternAndOffsetException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.index(rs, (Pattern) null, 1);
    });
  }

  @Test
  public void testInsert() {
    assertEquals("X1234", RubyStrings.insert("1234", 0, "X"));
    assertEquals("X1234", RubyStrings.insert("1234", -5, "X"));
    assertEquals("123X4", RubyStrings.insert("1234", 3, "X"));
    assertEquals("1234X", RubyStrings.insert("1234", 4, "X"));
    assertEquals("12X34", RubyStrings.insert("1234", -3, "X"));
    assertEquals("1234X", RubyStrings.insert("1234", 0 - 1, "X"));
  }

  @Test
  public void testInsertIndexException1() {
    assertThrows(IndexOutOfBoundsException.class, () -> {
      RubyStrings.insert("1234", -6, "X");
    });
  }

  @Test
  public void testInsertIndexException2() {
    assertThrows(IndexOutOfBoundsException.class, () -> {
      RubyStrings.insert("1234", 5, "X");
    });
  }

  @Test
  public void testInsertException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.insert(rs, 0, null);
    });
  }

  @Test
  public void testInspect() {
    assertEquals("\"我\\b\\f\\n\\r\\t\\0abc]\\n\\177\\uea60\"",
        RubyStrings.inspect("我\b\f\n\r\t\0abc]\n\177\uea60"));
  }

  @Test
  public void testLines() {
    assertEquals(ra("a", "b", "", "c"), RubyStrings.lines("a\nb\n\nc"));
  }

  @Test
  public void testLinesWithSeparator() {
    assertEquals(ra("a\n", "\nc"), RubyStrings.lines("a\nb\n\nc", "b\n"));
  }

  @Test
  public void testLinesWithSeparatorException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.lines("a\nb\n\nc", null);
    });
  }

  @Test
  public void testLjust() {
    assertEquals("hello", RubyStrings.ljust("hello", 4));
    assertEquals("hello               ", RubyStrings.ljust("hello", 20));
  }

  @Test
  public void testLjustWithPadstr() {
    assertEquals("hello123412341234123", RubyStrings.ljust("hello", 20, "1234"));
  }

  @Test
  public void testLjustWithPadstrException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.ljust("hello", 20, null);
    });
  }

  @Test
  public void testLstrip() {
    assertEquals("hello  ", RubyStrings.lstrip("  hello  "));
  }

  @Test
  public void testMatch() {
    Matcher matcher = RubyStrings.match(rs, "[a-z]");
    assertTrue(matcher instanceof Matcher);
    RubyArray<String> chars = rs(rs).eachChar().toA();
    while (matcher.find()) {
      assertEquals(chars.shift(), matcher.group());
    }
    assertNull(RubyStrings.match(rs, "\\d"));
  }

  @Test
  public void testMatchException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.match(rs, null);
    });
  }

  @Test
  public void testMatchWithPosition() {
    Matcher matcher = RubyStrings.match(rs, "[a-z]", 1);
    assertTrue(matcher instanceof Matcher);
    RubyArray<String> chars = rs(rs).eachChar().toA();
    chars.shift(1);
    while (matcher.find()) {
      assertEquals(chars.shift(), matcher.group());
    }
    assertNull(RubyStrings.match(rs, "\\d", 1));
    assertNull(RubyStrings.match(rs, "[a-z]", -rs.length() - 1));
    assertNull(RubyStrings.match(rs, "[a-z]", rs.length()));
  }

  @Test
  public void testMatchWithPositionException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.match(rs, null, 1);
    });
  }

  @Test
  public void testNext() {
    assertEquals(StringSuccessor.getInstance().succ(rs), RubyStrings.next(rs));
  }

  @Test
  public void testOct() {
    assertEquals(83, RubyStrings.oct("123"));
    assertEquals(-255, RubyStrings.oct("-377"));
    assertEquals(0, RubyStrings.oct("bad"));
    assertEquals(255, RubyStrings.oct("0377bad"));
  }

  @Test
  public void testOrd() {
    assertEquals(97, RubyStrings.ord("abc"));
  }

  @Test
  public void testOrdException() {
    assertThrows(IllegalArgumentException.class, () -> {
      RubyStrings.ord("");
    });
  }

  @Test
  public void testPartition() {
    assertEquals(ra("he", "l", "lo"), RubyStrings.partition("hello", "l"));
    assertEquals(ra("hello", "", ""), RubyStrings.partition("hello", "x"));
  }

  @Test
  public void testPartitionException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.partition(rs, (String) null);
    });
  }

  @Test
  public void testPartitionWithPattern() {
    assertEquals(ra("h", "el", "lo"), RubyStrings.partition("hello", qr(".l")));
    assertEquals(ra("hello", "", ""), RubyStrings.partition("hello", qr(".x")));
  }

  @Test
  public void testPartitionWithPatternException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.partition(rs, (Pattern) null);
    });
  }

  @Test
  public void testPrepend() {
    assertEquals("defabc", RubyStrings.prepend(rs, "def"));
  }

  @Test
  public void testPrependException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.prepend(rs, null);
    });
  }

  @Test
  public void testReverse() {
    assertEquals("cba", RubyStrings.reverse(rs));
  }

  @Test
  public void testLindex() {
    assertEquals((Integer) 1, RubyStrings.rindex("hello", "e"));
    assertEquals((Integer) 4, RubyStrings.rindex("hello", "o"));
    assertEquals(null, RubyStrings.rindex("hello", "a"));
  }

  @Test
  public void testLindexException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.rindex(rs, (String) null);
    });
  }

  @Test
  public void testLindexWithEnd() {
    assertEquals((Integer) 3, RubyStrings.rindex("hello", "l", -2));
    assertEquals((Integer) 4, RubyStrings.rindex("hello", "o", -1));
    assertEquals((Integer) 2, RubyStrings.rindex("hello", "l", -3));
    assertEquals((Integer) 3, RubyStrings.rindex("hello", "l", 100));
    assertNull(RubyStrings.rindex("hello", "l", -100));
    assertEquals((Integer) 0, RubyStrings.rindex("hello", "h", -5));
  }

  @Test
  public void testLindexWithEndException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.rindex(rs, (String) null, -1);
    });
  }

  @Test
  public void testLindexWithPattern() {
    assertEquals((Integer) 1, RubyStrings.rindex("hello", qr("e")));
    assertEquals((Integer) 4, RubyStrings.rindex("hello", qr("o")));
    assertEquals(null, RubyStrings.rindex("hello", qr("a")));
  }

  @Test
  public void testLindexWithPatternException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.rindex(rs, (Pattern) null);
    });
  }

  @Test
  public void testLindexWithPatternAndStopAt() {
    assertEquals((Integer) 3, RubyStrings.rindex("hello", qr("l"), -2));
    assertEquals((Integer) 4, RubyStrings.rindex("hello", qr("o"), -1));
    assertEquals((Integer) 2, RubyStrings.rindex("hello", qr("l"), -3));
    assertEquals((Integer) 3, RubyStrings.rindex("hello", qr("l"), 100));
    assertNull(RubyStrings.rindex("hello", qr("l"), -100));
    assertEquals((Integer) 0, RubyStrings.rindex("hello", qr("h"), -5));
  }

  @Test
  public void testLindexWithPatternAndStopAtException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.rindex(rs, (Pattern) null, -1);
    });
  }

  @Test
  public void testRjust() {
    assertEquals("hello", RubyStrings.rjust("hello", 4));
    assertEquals("               hello", RubyStrings.rjust("hello", 20));
  }

  @Test
  public void testRjustWithPadstr() {
    assertEquals("hello", RubyStrings.rjust("hello", -1, "1234"));
    assertEquals("123412341234123hello", RubyStrings.rjust("hello", 20, "1234"));
  }

  @Test
  public void testRpartition() {
    assertEquals(ra("hel", "l", "o"), RubyStrings.rpartition("hello", "l"));
    assertEquals(ra("", "", "hello"), RubyStrings.rpartition("hello", "x"));
  }

  @Test
  public void testRpartitionException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.rpartition(rs, (String) null);
    });
  }

  @Test
  public void testRpartitionWithPattern() {
    assertEquals(ra("he", "ll", "o"), RubyStrings.rpartition("hello", qr(".l")));
    assertEquals(ra("", "", "hello"), RubyStrings.rpartition("hello", qr(".x")));
  }

  @Test
  public void testRpartitionWithPatternException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.rpartition(rs, (Pattern) null);
    });
  }

  @Test
  public void testRstrip() {
    assertEquals("  hello", RubyStrings.rstrip("  hello  "));
    assertEquals("hello", RubyStrings.rstrip("hello"));
  }

  @Test
  public void testScan() {
    assertEquals(ra("cruel", "world"), RubyStrings.scan("cruel world", "\\w+"));
    assertEquals(ra("cru", "el ", "wor"), RubyStrings.scan("cruel world", "..."));
  }

  @Test
  public void testScanException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.scan(rs, null);
    });
  }

  @Test
  public void testScanWithBlock() {
    final RubyArray<String> strs = newRubyArray();
    rs = "cruel world";
    assertSame(rs, RubyStrings.scan(rs, "\\w+", item -> strs.add(item)));
    assertEquals(ra("cruel", "world"), strs);
  }

  @Test
  public void testScanWithBlockException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.scan(rs, null, null);
    });
  }

  @Test
  public void testScanGroups() {
    assertEquals(ra(ra("cru"), ra("el "), ra("wor")),
        RubyStrings.scanGroups("cruel world", "(...)"));
    assertEquals(ra(ra("cr", "ue"), ra("l ", "wo")),
        RubyStrings.scanGroups("cruel world", "(..)(..)"));
    assertEquals(ra(ra("cru"), ra("el "), ra("wor")), RubyStrings.scanGroups("cruel world", "..."));
  }

  @Test
  public void testScanGroupsException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.scanGroups(rs, null);
    });
  }

  @Test
  public void testScanGroupsWithBlock() {
    final RubyArray<String> strs = newRubyArray();
    rs = "cruel world";
    assertSame(rs, RubyStrings.scanGroups(rs, "(...)", item -> strs.concat(item)));
    assertEquals(ra("cru", "el ", "wor"), strs);
  }

  @Test
  public void testScanGroupsWithBlockException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.scanGroups(rs, null, null);
    });
  }

  @Test
  public void testScrub() {
    assertEquals("abcあ�", RubyStrings.scrub("abc\u3042\0"));
    assertEquals("abcあ\uFFFD", RubyStrings.scrub("abc\u3042\0"));
  }

  @Test
  public void testScrubWithReplacement() {
    assertEquals("abcあ!", RubyStrings.scrub("abc\u3042\0", "!"));
    assertEquals(rs("abc\u3042\0").scrub().toS(), RubyStrings.scrub("abc\u3042\0", (String) null));
  }

  @Test
  public void testScrubWithBlock() {
    assertEquals("abcあ?", RubyStrings.scrub("abc\u3042\0", item -> {
      assertEquals(1, item.size());
      assertEquals(Byte.valueOf((byte) 0), item.get(0));
      return "?";
    }));
  }

  @Test
  public void testSetbyte() {
    assertEquals("aba", RubyStrings.setbyte(rs, 2, (byte) 97));
    assertEquals("aac", RubyStrings.setbyte(rs, -2, (byte) 97));
  }

  @Test
  public void testSlice() {
    assertNull(RubyStrings.slice(rs, 3));
    assertNull(RubyStrings.slice(rs, -4));
    assertEquals("a", RubyStrings.slice(rs, 0));
    assertEquals("c", RubyStrings.slice(rs, -1));
  }

  @Test
  public void testSliceWithLength() {
    assertNull(RubyStrings.slice(rs, 3, 2));
    assertNull(RubyStrings.slice(rs, -4, 2));
    assertEquals("ab", RubyStrings.slice(rs, 0, 2));
    assertEquals("c", RubyStrings.slice(rs, -1, 2));
  }

  @Test
  public void testSliceWithPattern() {
    assertNull(RubyStrings.slice(rs, qr("d")));
    assertEquals("ab", RubyStrings.slice(rs, qr("[a-b]+")));
  }

  @Test
  public void testSliceWithPatternException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.slice(rs, (Pattern) null);
    });
  }

  @Test
  public void testSliceWithPatternAndGroupNumber() {
    assertNull(RubyStrings.slice(rs, qr("d"), 1));
    assertNull(RubyStrings.slice(rs, qr("a"), 1));
    assertNull(RubyStrings.slice(rs, qr("a"), 0));
    assertEquals("ab", RubyStrings.slice(rs, qr("([a-b]+)"), 1));
  }

  @Test
  public void testSliceWithMatchStr() {
    assertNull(RubyStrings.slice(rs, "d"));
    assertEquals("bc", RubyStrings.slice(rs, "bc"));
  }

  @Test
  public void testSplit() {
    assertEquals(ra("a", "bc", "def"), RubyStrings.split("  a   bc   def "));
  }

  @Test
  public void testSplitWithDelimiter() {
    assertEquals(ra("a", "bc", "def"), RubyStrings.split("  a   bc   def ", " "));
    assertEquals(ra("a", "bc", "def"), RubyStrings.split("  a   bc   def ", (String) null));
    assertEquals(ra("", "a", " bc", " def "), RubyStrings.split("  a   bc   def ", "  "));
  }

  @Test
  public void testSplitWithDelimiterAndLimit() {
    assertEquals(ra("a", "bc", "def"), RubyStrings.split("  a   bc   def ", " ", 0));
    assertEquals(ra("a", "bc", "def"), rs("  a   bc   def ").split((String) null, 0));
    assertEquals(ra("a", "bc", "def"), RubyStrings.split("  a   bc   def ", " ", -1));
    assertEquals(ra("  a   bc   def "), RubyStrings.split("  a   bc   def ", " ", 1));
    assertEquals(ra("a", "bc   def "), RubyStrings.split("  a   bc   def ", " ", 2));
    assertEquals(ra("", "a   bc   def "), RubyStrings.split("  a   bc   def ", "  ", 2));
    assertEquals(ra("", "a", " bc", " def "), RubyStrings.split("  a   bc   def ", "  ", 0));
  }

  @Test
  public void testSplitWithPatternAndDelimiter() {
    assertEquals(ra("", "a", "bc", "def"), RubyStrings.split("  a   bc   def ", qr(" +")));
    assertEquals(ra("a", "bc", "def"), RubyStrings.split("  a   bc   def ", (Pattern) null));
    assertEquals(ra("", "a", "bc", "def"), RubyStrings.split("  a   bc   def ", qr(" +")));
  }

  @Test
  public void testSplitWithPatternAndDelimiterAndLimit() {
    assertEquals(ra("", "a", "bc", "def"), RubyStrings.split("  a   bc   def ", qr(" +"), 0));
    assertEquals(ra("a", "bc", "def"), RubyStrings.split("  a   bc   def ", (Pattern) null, 0));
    assertEquals(ra("", "a", "bc", "def"), RubyStrings.split("  a   bc   def ", qr(" +"), -1));
    assertEquals(ra("  a   bc   def "), RubyStrings.split("  a   bc   def ", qr(" +"), 1));
    assertEquals(ra("", "a   bc   def "), RubyStrings.split("  a   bc   def ", qr(" +"), 2));
    assertEquals(ra("", "a   bc   def "), RubyStrings.split("  a   bc   def ", qr(" {2,2}"), 2));
    assertEquals(ra("", "a", " bc", " def "),
        RubyStrings.split("  a   bc   def ", qr(" {2,2}"), 0));
  }

  @Test
  public void testSqueeze() {
    assertEquals("yelow mon", RubyStrings.squeeze("yellow moon"));
  }

  @Test
  public void testSqueezeWithCharSet() {
    assertEquals(" now is the", RubyStrings.squeeze("  now   is  the", " "));
    assertEquals("puters shot balls", RubyStrings.squeeze("putters shoot balls", "m-z"));
  }

  @Test
  public void testSqueezeWithCharSetException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.squeeze("  now   is  the", null);
    });
  }

  @Test
  public void testStartWithʔ() {
    assertTrue(RubyStrings.startWithʔ("hello", "hell"));
    assertTrue(RubyStrings.startWithʔ("hello", "heaven", "hell"));
    assertFalse(RubyStrings.startWithʔ("hello", "heaven", "paradise"));
    assertTrue(RubyStrings.startWithʔ("hello", "hell", (String[]) null));
  }

  @Test
  public void testStartWithʔException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.startWithʔ("hello", null);
    });
  }

  @Test
  public void testStrip() {
    assertEquals("goodbye", RubyStrings.strip("\tgoodbye\r\n"));
  }

  @Test
  public void testSub() {
    assertEquals("h*llo", RubyStrings.sub("hello", "[aeiou]", "*"));
    assertEquals("h<e>llo", RubyStrings.sub("hello", "([aeiou])", "<$1>"));
  }

  @Test
  public void testSubException1() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.sub("hello", null, "*");
    });
  }

  @Test
  public void testSubException2() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.sub("hello", "[aeiou]", (String) null);
    });
  }

  @Test
  public void testSubWithMap() {
    assertEquals("h0llo", RubyStrings.sub("hello", "[aeiou]", rh("e", 0)));
    assertEquals("hello", RubyStrings.sub("hello", "[aeiou]", rh("a", 1)));
    assertEquals("hello", RubyStrings.sub("hello", "x", rh("a", 1)));
    assertEquals("hello", RubyStrings.sub("hello", "e", new HashMap<String, Object>()));
  }

  @Test
  public void testSubWithMapException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.sub(rs, null, rh("a", 1));
    });
  }

  @Test
  public void testSubWithBlock() {
    Function<String, String> block = item -> "0";
    assertEquals("h0llo", RubyStrings.sub("hello", "[aeiou]", block));
    assertEquals("hello", RubyStrings.sub("hello", "x", block));
  }

  @Test
  public void testSubWithBlockException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.sub(rs, null, (Function<String, String>) null);
    });
  }

  @Test
  public void testSucc() {
    assertEquals("abce", RubyStrings.succ("abcd"));
    assertEquals("THX1139", RubyStrings.succ("THX1138"));
    assertEquals("<<koalb>>", RubyStrings.succ("<<koala>>"));
    assertEquals("2000aaa", RubyStrings.succ("1999zzz"));
    assertEquals("AAAA0000", RubyStrings.succ("ZZZ9999"));
    assertEquals("**+", RubyStrings.succ("***"));
  }

  @Test
  public void testSum() {
    assertEquals(RubyStrings.sum("abc我", 16), RubyStrings.sum("abc我"));
  }

  @Test
  public void testSwapcase() {
    assertEquals("hELLO", RubyStrings.swapcase("Hello"));
    assertEquals("CyBeR_pUnK11", RubyStrings.swapcase("cYbEr_PuNk11"));
  }

  @Test
  public void testToF() {
    assertEquals((Double) 0d, (Double) RubyStrings.toF("dhd"));
    assertEquals((Double) (-0.0253), (Double) RubyStrings.toF("  -  .253e-1 "));
    assertEquals((Double) (0.0253), (Double) RubyStrings.toF("  +  .253E-1 "));
  }

  @Test
  public void testToI() {
    assertEquals((Integer) 0, (Integer) RubyStrings.toI("fbdh"));
    assertEquals((Integer) (-123), (Integer) RubyStrings.toI("  -  123 "));
    assertEquals((Integer) 456, (Integer) RubyStrings.toI("  +  456 "));
  }

  @Test
  public void testToIWithRadix() {
    assertEquals((Integer) 12345, (Integer) RubyStrings.toI("12345"));
    assertEquals((Integer) 99, (Integer) RubyStrings.toI("99 red balloons"));
    assertEquals((Integer) 0, (Integer) RubyStrings.toI("0a"));
    assertEquals((Integer) 10, (Integer) RubyStrings.toI("0a", 16));
    assertEquals((Integer) 0, (Integer) RubyStrings.toI("hello"));
    assertEquals((Integer) 101, (Integer) RubyStrings.toI(" + 1100101", 2));
    assertEquals((Integer) 294977, (Integer) RubyStrings.toI("1100101", 8));
    assertEquals((Integer) rs("1100101").toI(8), (Integer) RubyStrings.toI("11001019", 8));
    assertEquals((Integer) rs("11001").toI(8), (Integer) RubyStrings.toI("110019", 8));
    assertEquals((Integer) 1100101, (Integer) RubyStrings.toI("1100101", 10));
    assertEquals((Integer) 17826049, (Integer) RubyStrings.toI("1100101", 16));
    assertEquals((Integer) rs("1100101F").toI(16), (Integer) RubyStrings.toI("1100101fg", 16));
  }

  @Test
  public void testToIWithRadixException1() {
    assertThrows(IllegalArgumentException.class, () -> {
      RubyStrings.toI(rs, 1);
    });
  }

  @Test
  public void testToIWithRadixException2() {
    assertThrows(IllegalArgumentException.class, () -> {
      RubyStrings.toI(rs, 37);
    });
  }

  @Test
  public void testToIWithRadixException3() {
    assertEquals((Integer) 0,
        (Integer) RubyStrings.toI("fffffffffffffffffffffffffffffffffffff", 16));
  }

  @Test
  public void testToS() {
    assertEquals("abc", RubyStrings.toS(rs));
  }

  @Test
  public void testToStr() {
    assertEquals("abc", RubyStrings.toStr(rs));
  }

  @Test
  public void testTr() {
    assertEquals("hippo", RubyStrings.tr("hello", "el", "ip"));
    assertEquals("h*ll*", RubyStrings.tr("hello", "aeiou", "*"));
    assertEquals("hAll*", RubyStrings.tr("hello", "aeiou", "AA*"));
    assertEquals("ifmmp", RubyStrings.tr("hello", "a-y", "b-z"));
    assertEquals("*e**o", RubyStrings.tr("hello", "^aeiou", "*"));
    assertEquals("h*ll**w*rld", RubyStrings.tr("hello^world", "\\^aeiou", "*"));
    assertEquals("h*ll**w*rld", RubyStrings.tr("hello-world", "a\\-eo", "*"));
    assertEquals("hello\nworld", RubyStrings.tr("hello\r\nworld", "\r", ""));
    assertEquals(rs("hello\r\nwold").inspect().toS(),
        RubyStrings.inspect(RubyStrings.tr("hello\r\nworld", "\\r", "")));
    assertEquals("hello\nworld", RubyStrings.tr("hello\r\nworld", "\\\r", ""));
    assertEquals("['b']", RubyStrings.tr("X['\\b']", "X\\", ""));
    assertEquals("'b'", RubyStrings.tr("X['\\b']", "X-\\]", ""));
    assertEquals("ell", RubyStrings.tr("hello", "^el", ""));
  }

  @Test
  public void testTrException1() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.tr(rs, null, "");
    });
  }

  @Test
  public void testTrException2() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.tr(rs, "", null);
    });
  }

  @Test
  public void testTrS() {
    assertEquals("hero", RubyStrings.trS("hello", "l", "r"));
    assertEquals("h*o", RubyStrings.trS("hello", "el", "*"));
    assertEquals("hhxo", RubyStrings.trS("hello", "el", "hx"));
    assertEquals("xellx", RubyStrings.trS("hello", "^el", "hx"));
    assertEquals("ell", RubyStrings.trS("hello", "^el", ""));
  }

  @Test
  public void testTrSException1() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.trS(rs, null, "");
    });
  }

  @Test
  public void testTrSException2() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.trS(rs, "", null);
    });
  }

  @Test
  public void testUnpack() {
    assertEquals(ra("abc", "abc "), RubyStrings.unpack("abc \0\0abc \0\0", "A6Z6"));
    assertEquals(ra("abc", " \000\000"), RubyStrings.unpack("abc \0\0", "a3a3"));
    assertEquals(ra("abc ", "abc "), RubyStrings.unpack("abc \0abc \0", "Z*Z*"));
    assertEquals(ra("10000110", "01100001"), RubyStrings.unpack("aa", "b8B8"));
    assertEquals(ra("16", "61", (byte) 97), RubyStrings.unpack("aaa", "h2H2c"));
  }

  @Test
  public void testUnpackException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.unpack(rs, null);
    });
  }

  @Test
  public void testUpcase() {
    assertEquals("ABC", RubyStrings.upcase(rs));
  }

  @Test
  public void testUpto() {
    assertEquals(ra("9", "10", "11"), RubyIterables.toA(RubyStrings.upto("9", "11")));
    assertEquals(ra(), RubyIterables.toA(RubyStrings.upto("25", "5")));
    assertEquals(ra("07", "08", "09", "10", "11"), RubyIterables.toA(RubyStrings.upto("07", "11")));
  }

  @Test
  public void testUptoException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.upto(rs, null);
    });
  }

  @Test
  public void testUptoWithExclusive() {
    assertEquals(ra("9", "10", "11"), RubyIterables.toA(RubyStrings.upto("9", "11", false)));
    assertEquals(ra("9", "10"), RubyIterables.toA(RubyStrings.upto("9", "11", true)));
  }

  @Test
  public void testUptoWithExclusiveException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.upto(rs, null, false);
    });
  }

  @Test
  public void testUptoWithBlock() {
    final RubyArray<String> strs = newRubyArray();
    assertSame(rs, RubyStrings.upto(rs, "abe", item -> strs.add(item)));
    assertEquals(ra("abc", "abd", "abe"), strs);
  }

  @Test
  public void testUptoWithBlockException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.upto(rs, null, (Consumer<String>) null);
    });
  }

  @Test
  public void testUptoWithExclusiveAndBlock() {
    final RubyArray<String> strs = newRubyArray();
    assertSame(rs, RubyStrings.upto(rs, "abe", true, item -> strs.add(item)));
    assertEquals(ra("abc", "abd"), strs);
  }

  @Test
  public void testUptoWithExclusiveAndBlockException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.upto(rs, null, true, (Consumer<String>) null);
    });
  }

  @Test
  public void testValidEncodingʔ() {
    String test1 = "Itâ€™s a string with â€˜unknownâ€˜).";
    String test2 = "It’s a string with ‘unknown‘).";
    assertTrue(RubyStrings.validEncodingʔ(test1, "Windows-1252"));
    assertFalse(RubyStrings.validEncodingʔ(test2, "Windows-1252"));
  }

  @Test
  public void testValidEncodingʔException() {
    assertThrows(ClassCastException.class, () -> {
      RubyStrings.validEncodingʔ(rs, null);
    });
  }

}
