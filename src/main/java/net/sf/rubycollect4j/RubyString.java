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

import static net.sf.rubycollect4j.RubyCollections.Hash;
import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.newRubyEnumerator;
import static net.sf.rubycollect4j.RubyCollections.qr;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.range;
import static net.sf.rubycollect4j.RubyCollections.rs;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.TypeConstraintException;

import net.sf.rubycollect4j.block.Block;
import net.sf.rubycollect4j.block.BooleanBlock;
import net.sf.rubycollect4j.block.TransformBlock;
import net.sf.rubycollect4j.packer.Unpacker;
import net.sf.rubycollect4j.succ.StringSuccessor;

/**
 * 
 * RubyString implements all methods refer to the String of Ruby language.
 * RubyString is also a Java CharSequence.
 * 
 */
public final class RubyString extends RubyEnumerable<String> implements
    CharSequence {

  private String str;

  public RubyString() {
    str = "";
  }

  public RubyString(String str) {
    if (str == null)
      throw new NullPointerException();

    this.str = str;
  }

  public static boolean blankʔ(String str) {
    return str == null || str.trim().isEmpty();
  }

  @Override
  protected Iterable<String> getIterable() {
    final String iterStr = str;
    return range(0, iterStr.length() - 1).lazy().map(
        new TransformBlock<Integer, String>() {

          @Override
          public String yield(Integer item) {
            return String.valueOf(iterStr.charAt(item));
          }

        });
  }

  private String checkNotNull(String str) {
    if (str == null)
      throw new TypeConstraintException(
          "TypeError: no implicit conversion of null into String");

    return str;
  }

  public boolean asciiOnlyʔ() {
    return str.matches("^[\\u0000-\\u007F]*$");
  }

  public String b() {
    return new String(str.getBytes(), Charset.forName("ISO-8859-1"));
  }

  public RubyArray<Byte> bytes() {
    RubyArray<Byte> bytes = newRubyArray();
    for (Byte b : str.getBytes()) {
      bytes.add(b);
    }
    return bytes;
  }

  public int bytesize() {
    return str.getBytes().length;
  }

  public String byteslice(int fixnum) {
    Byte ch = bytes().at(fixnum);
    return ch == null ? null : new String(new byte[] { ch });
  }

  public String byteslice(int index, int length) {
    RubyArray<Byte> bytes = bytes().slice(index, length);
    if (bytes == null)
      return null;

    byte[] byteAry = new byte[bytes.size()];
    for (int i = 0; i < byteAry.length; i++) {
      byteAry[i] = bytes.get(i);
    }
    return new String(byteAry);
  }

  public String capitalize() {
    if (str.isEmpty())
      return str;

    return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
  }

  public RubyString capitalizeǃ() {
    str = capitalize();
    return this;
  }

  public int casecmp(String str) {
    return this.str.compareToIgnoreCase(str);
  }

  public String center(int width) {
    return center(width, " ");
  }

  public String center(int width, String padstr) {
    checkNotNull(padstr);
    if (padstr.isEmpty())
      throw new IllegalArgumentException("ArgumentError: zero width padding");

    if (width <= str.length())
      return str;

    RubyArray<String> centeredStr = newRubyArray();
    int start = (width - str.length()) / 2;
    RubyLazyEnumerator<String> padStr = rs(padstr).chars().lazy().cycle();
    for (int i = 0; i < width; i++) {
      if (i < start) {
        centeredStr.add(padStr.next());
      } else if (i == start) {
        padStr.rewind();
        if (str.isEmpty()) {
          centeredStr.add(padStr.next());
          continue;
        }
        centeredStr = centeredStr.plus(chars());
        i += str.length() - 1;
      } else {
        centeredStr.add(padStr.next());
      }
    }
    return centeredStr.join();
  }

  public RubyArray<String> chars() {
    if (str.isEmpty())
      return newRubyArray();

    return newRubyArray(str.split("(?!^)"));
  }

  public String chomp() {
    String result = str.replaceAll("\\r\\n$", "");
    if (result.length() < str.length())
      return result;

    result = str.replaceAll("\\n$", "");
    if (result.length() < str.length())
      return result;

    return result.replaceAll("\\r$", "");
  }

  public RubyString chompǃ() {
    str = chomp();
    return this;
  }

  public String chomp(String separator) {
    separator = separator == null ? "" : separator;
    return str.replaceAll(separator + "$", "");
  }

  public RubyString chompǃ(String separator) {
    str = chomp(separator);
    return this;
  }

  public String chop() {
    if (str.isEmpty())
      return str;

    String result = str.replaceAll("\\r\\n$", "");
    if (result.length() < str.length())
      return result;

    return result.substring(0, result.length() - 1);
  }

  public RubyString chopǃ() {
    str = chop();
    return this;
  }

  public String chr() {
    if (str.isEmpty())
      return str;

    return str.substring(0, 1);
  }

  public RubyString clear() {
    str = "";
    return this;
  }

  public RubyArray<Integer> codepoints() {
    return range(0, str.length() - 1).map(
        new TransformBlock<Integer, Integer>() {

          @Override
          public Integer yield(Integer item) {
            return str.codePointAt(item);
          }

        });
  }

  public RubyString concat(int codepoint) {
    str += (char) codepoint;
    return this;
  }

  public RubyString concat(Object o) {
    str += o;
    return this;
  }

  public int count(final String charSet, final String... charSets) {
    checkNotNull(charSet);

    return chars().count(new BooleanBlock<String>() {

      @Override
      public boolean yield(String item) {
        if (!isCharSetMatched(item, charSet))
          return false;

        for (String cs : charSets) {
          if (!isCharSetMatched(item, cs))
            return false;
        }

        return true;
      }

    });
  }

  private boolean isCharSetMatched(String ch, String charSet) {
    if (charSet.startsWith("^") && !ch.matches("[" + charSet + "]"))
      return false;

    if (!charSet.startsWith("^")
        && !rs(charSet2Str(charSet)).chars().includeʔ(ch))
      return false;

    return true;
  }

  private String charSet2Str(String charSet) {
    Matcher matcher = qr("\\\\?[^\\\\]-\\\\?[^\\\\]").matcher(charSet);
    while (matcher.find()) {
      String charRg = matcher.group();
      RubyRange<Character> rr =
          range(charRg.replace("\\", "").charAt(0), charRg.replace("\\", "")
              .charAt(2));
      if (rr.noneʔ())
        throw new IllegalArgumentException("ArgumentError: invalid range \""
            + charSet + "\" in string transliteration");
      charSet = charSet.replace(charRg, rr.toA().join());
    }
    return charSet;
  }

  public String crypt(String salt) {
    String md5 = null;
    MessageDigest digest;
    try {
      digest = MessageDigest.getInstance("MD5");
      digest.update(salt.getBytes(), 0, salt.length());
      md5 = new BigInteger(1, digest.digest()).toString(16);
    } catch (NoSuchAlgorithmException e) {}
    return md5;
  }

  public String delete(String otherStr) {
    checkNotNull(otherStr);

    return chars().minus(rs(otherStr).chars()).join();
  }

  public RubyString deleteǃ(String otherStr) {
    str = delete(otherStr);
    return this;
  }

  public String downcase() {
    return str.toLowerCase();
  }

  public RubyString downcaseǃ() {
    str = str.toLowerCase();
    return this;
  }

  public String dump() {
    return str.replaceAll("\b", "\\\\b").replaceAll("\t", "\\\\t")
        .replaceAll("\t", "\\\\t").replaceAll("\n", "\\\\n")
        .replaceAll("\f", "\\\\f").replaceAll("\r", "\\\\r")
        .replaceAll("\n", "\\\\n").replaceAll("\\p{C}", "");
  }

  public RubyString eachByte(Block<Byte> block) {
    bytes().each(block);
    return this;
  }

  public RubyEnumerator<Byte> eachByte() {
    return bytes().each();
  }

  public RubyString eachChar(Block<String> block) {
    each(block);
    return this;
  }

  public RubyEnumerator<String> eachChar() {
    return each();
  }

  public RubyString eachCodepoint(Block<Integer> block) {
    codepoints().each(block);
    return this;
  }

  public RubyEnumerator<Integer> eachCodepoint() {
    return codepoints().each();
  }

  public RubyString eachLine(Block<String> block) {
    ra(str.split("$")).each(block);
    return this;
  }

  public RubyEnumerator<String> eachLine() {
    return ra(str.split("$")).each();
  }

  public RubyString eachLine(String separator, Block<String> block) {
    ra(str.split(separator)).each(block);
    return this;
  }

  public RubyEnumerator<String> eachLine(String separator) {
    return ra(str.split(separator)).each();
  }

  public boolean emptyʔ() {
    return str.isEmpty();
  }

  public String encode(String encoding) {
    return new String(str.getBytes(), Charset.forName(encoding));
  }

  public String encode(String dstEncoding, String srcEncoding) {
    return new String(str.getBytes(Charset.forName(srcEncoding)),
        Charset.forName(dstEncoding));
  }

  public RubyString encodeǃ(String encoding) {
    str = encode(encoding);
    return this;
  }

  public RubyString encodeǃ(String dstEncoding, String srcEncoding) {
    str = encode(dstEncoding, srcEncoding);
    return this;
  }

  public Charset encoding() {
    return Charset.forName("UTF-8");
  }

  public boolean endWithʔ(String suffix, String... otherSuffix) {
    if (str.endsWith(suffix))
      return true;

    for (String s : otherSuffix) {
      if (str.endsWith(s))
        return true;
    }

    return false;
  }

  public boolean eqlʔ(Object o) {
    return equals(o);
  }

  public String forceEncoding(String encoding) {
    return encode(encoding);
  }

  public Byte getbyte(int index) {
    return bytes().at(index);
  }

  public String gsub(String pattern, String replacement) {
    return str.replaceAll(pattern, replacement);
  }

  public String gsub(String pattern, Map<String, ?> map) {
    String result = str;
    Matcher matcher = qr(pattern).matcher(str);
    while (matcher.find()) {
      String target = matcher.group();
      if (map.containsKey(target))
        result = result.replace(target, map.get(target).toString());
    }
    return result;
  }

  public RubyEnumerator<String> gsub(String pattern) {
    RubyArray<String> matches = newRubyArray();
    Matcher matcher = qr(pattern).matcher(str);
    while (matcher.find()) {
      matches.add(matcher.group());
    }
    return matches.each();
  }

  public RubyString gsubǃ(String pattern, String replacement) {
    str = gsub(pattern, replacement);
    return this;
  }

  public RubyString gsubǃ(String pattern, Map<String, ?> map) {
    str = gsub(pattern, map);
    return this;
  }

  public RubyEnumerator<String> gsubǃ(String pattern) {
    return gsub(pattern);
  }

  public int hash() {
    return hashCode();
  }

  public long hex() {
    return Long.parseLong(str, 16);
  }

  public boolean includeʔ(String otherStr) {
    return str.contains(otherStr);
  }

  public Integer index(String substring) {
    return index(substring, 0);
  }

  public Integer index(String substring, int offset) {
    int index = str.indexOf(substring, offset);
    return index == -1 ? null : index;
  }

  public Integer index(Pattern regex) {
    return index(regex, 0);
  }

  public Integer index(Pattern regex, int offset) {
    Matcher matcher = regex.matcher(str);
    if (matcher.find(offset))
      return matcher.start();
    else
      return null;
  }

  public RubyString insert(int index, String otherStr) {
    str = chars().insert(index, otherStr).join();
    return this;
  }

  public String inspect() {
    String printable = str.replaceAll("\b", "\\\\b");
    printable = printable.replaceAll("\f", "\\\\f");
    printable = printable.replaceAll("\n", "\\\\n");
    printable = printable.replaceAll("\r", "\\\\r");
    printable = printable.replaceAll("\t", "\\\\t");
    return "\"" + printable + "\"";
  }

  public RubyArray<String> lines() {
    return eachLine().toA();
  }

  public RubyArray<String> lines(String separator) {
    return eachLine(separator).toA();
  }

  public String ljust(int width) {
    return ljust(width, " ");
  }

  public String ljust(int width, String padstr) {
    RubyLazyEnumerator<String> padStr = rs(padstr).chars().lazy().cycle();

    int extra = width - str.length();
    if (extra > 0) {
      StringBuilder sb = new StringBuilder();
      while (extra > 0) {
        sb.append(padStr.next());
        extra--;
      }
      return str + sb.toString();
    }

    return str;
  }

  public String lstrip() {
    return str.replaceFirst("^\\s+", "");
  }

  public RubyString lstripǃ() {
    str = lstrip();
    return this;
  }

  public Matcher match(String pattern) {
    return qr(pattern).matcher(str);
  }

  public Matcher match(String pattern, int pos) {
    return qr(pattern).matcher(str.substring(pos));
  }

  public RubyString next() {
    return rs(StringSuccessor.getInstance().succ(str));
  }

  public RubyString nextǃ() {
    str = StringSuccessor.getInstance().succ(str);
    return this;
  }

  public int oct() {
    return toI(8);
  }

  public int ord() {
    if (str == null)
      throw new IllegalArgumentException("ArgumentError: empty string");

    return str.charAt(0);
  }

  public RubyArray<String> partition(String sep) {
    if (sep == null)
      throw new TypeConstraintException("TypeError: type mismatch: null given");

    int sepIndex = str.indexOf(sep);
    if (sepIndex == -1)
      return newRubyArray("", "", str);

    return newRubyArray(str.substring(0, sepIndex), sep,
        str.substring(sepIndex + sep.length()));
  }

  public RubyArray<String> partition(Pattern regex) {
    if (regex == null)
      throw new TypeConstraintException("TypeError: type mismatch: null given");

    Matcher matcher = regex.matcher(str);
    if (matcher.find()) {
      String sep = matcher.group();
      while (matcher.find()) {
        sep = matcher.group();
      }
      int sepIndex = str.indexOf(sep);
      return newRubyArray(str.substring(0, sepIndex), sep,
          str.substring(sepIndex + sep.length()));
    } else
      return newRubyArray("", "", str);
  }

  public RubyString prepend(String otherStr) {
    str = checkNotNull(otherStr) + str;
    return this;
  }

  public RubyString replace(String otherStr) {
    str = checkNotNull(otherStr);
    return this;
  }

  public String reverse() {
    return new StringBuilder(str).reverse().toString();
  }

  public RubyString reverseǃ() {
    str = reverse();
    return this;
  }

  public Integer rindex(String substring) {
    return rindex(substring, 0);
  }

  public Integer rindex(String substring, int offset) {
    int index = str.lastIndexOf(substring, str.length() - 1);
    return index == -1 ? null : index;
  }

  public Integer rindex(Pattern regex) {
    return rindex(regex, str.length() - 1);
  }

  public Integer rindex(Pattern regex, int stopAt) {
    Matcher matcher = regex.matcher(str);
    int index = -1;
    while (matcher.find()) {
      int found = matcher.start();
      if (found <= stopAt)
        index = found;
    }

    if (index != -1)
      return index;
    else
      return null;
  }

  public String rjust(int width) {
    return rjust(width, " ");
  }

  public String rjust(int width, String padstr) {
    RubyLazyEnumerator<String> padStr = rs(padstr).chars().lazy().cycle();

    int extra = width - str.length();
    if (extra > 0) {
      StringBuilder sb = new StringBuilder();
      while (extra > 0) {
        sb.append(padStr.next());
        extra--;
      }
      return sb.toString() + str;
    }

    return str;
  }

  public RubyArray<String> rpartition(String sep) {
    if (sep == null)
      throw new TypeConstraintException("TypeError: type mismatch: null given");

    int sepIndex = str.lastIndexOf(sep);
    if (sepIndex == -1)
      return newRubyArray("", "", str);

    return newRubyArray(str.substring(0, sepIndex), sep,
        str.substring(sepIndex + sep.length()));
  }

  public RubyArray<String> rpartition(Pattern regex) {
    if (regex == null)
      throw new TypeConstraintException("TypeError: type mismatch: null given");

    Matcher matcher = regex.matcher(str);
    if (matcher.find()) {
      String sep = matcher.group();
      while (matcher.find()) {
        sep = matcher.group();
      }
      int sepIndex = str.lastIndexOf(sep);
      return newRubyArray(str.substring(0, sepIndex), sep,
          str.substring(sepIndex + sep.length()));
    } else
      return newRubyArray("", "", str);
  }

  public String rstrip() {
    return str.replaceFirst("\\s+$", "");
  }

  public RubyString rstripǃ() {
    str = rstrip();
    return this;
  }

  public RubyArray<String> scan(String regex) {
    if (regex == null)
      throw new TypeConstraintException(
          "TypeError: wrong argument type null (expected Regexp)");

    RubyArray<String> matches = newRubyArray();
    Matcher matcher = qr(regex).matcher(str);
    while (matcher.find()) {
      matches.add(matcher.group());
    }
    return matches;
  }

  public RubyString scan(String regex, Block<String> block) {
    scan(regex).each(block);
    return this;
  }

  public RubyArray<RubyArray<String>> scanGroups(String regex) {
    if (regex == null)
      throw new TypeConstraintException(
          "TypeError: wrong argument type null (expected Regexp)");

    RubyArray<RubyArray<String>> groups = newRubyArray();
    Matcher matcher = qr(regex).matcher(str);
    while (matcher.find()) {
      RubyArray<String> group = newRubyArray();
      for (int i = 1; i <= matcher.groupCount(); i++) {
        group.add(matcher.group(i));
      }
      groups.add(group);
    }
    return groups;
  }

  public RubyString scanGroups(String regex, Block<RubyArray<String>> block) {
    scanGroups(regex).each(block);
    return this;
  }

  public String scrub() {
    return str.replaceAll("\\p{C}", "\uFFFD");
  }

  public String scrub(String repl) {
    if (repl == null)
      return scrub();

    return str.replaceAll("\\p{C}", repl);
  }

  public String scrub(final TransformBlock<RubyArray<Byte>, String> block) {
    return chars().map(new TransformBlock<String, String>() {

      @Override
      public String yield(String item) {
        if (item.matches("\\p{C}"))
          return block.yield(rs(item).bytes());
        else
          return item;
      }

    }).join();
  }

  public RubyString scrubǃ() {
    str = scrub();
    return this;
  }

  public RubyString scrubǃ(String repl) {
    str = scrub(repl);
    return this;
  }

  public RubyString scrubǃ(final TransformBlock<RubyArray<Byte>, String> block) {
    str = scrub(block);
    return this;
  }

  public int setbyte(int index, int i) {
    byte[] bytes = str.getBytes();
    bytes[index] = (byte) i;
    str = new String(bytes);
    return i;
  }

  public int size() {
    return str.length();
  }

  public String slice(int index) {
    return chars().slice(index);
  }

  public String slice(int index, int length) {
    RubyArray<String> slicedStr = chars().slice(index, length);
    return slicedStr == null ? null : slicedStr.join();
  }

  public String slice(Pattern regex) {
    Matcher matcher = regex.matcher(str);
    if (matcher.find())
      return matcher.group();
    else
      return null;
  }

  public String slice(Pattern regex, int group) {
    Matcher matcher = regex.matcher(str);
    if (matcher.find())
      if (group < 1 || group > matcher.groupCount())
        return null;
      else
        return matcher.group(group);
    else
      return null;
  }

  public String slice(String matchStr) {
    return str.contains(matchStr) ? matchStr : null;
  }

  public Integer sliceǃ(int index) {
    RubyArray<String> chars = chars();
    String slicedStr = chars.sliceǃ(index);
    if (slicedStr == null)
      return null;

    str = chars().join();
    return index;
  }

  public String sliceǃ(int index, int length) {
    RubyArray<String> chars = chars();
    RubyArray<String> slicedStr = chars.sliceǃ(index, length);
    if (slicedStr == null)
      return null;

    str = chars().join();
    return slicedStr.join();
  }

  public String sliceǃ(Pattern regex) {
    String slicedStr = slice(regex);
    if (slicedStr == null)
      return null;

    str = str.replace(slicedStr, "");
    return slicedStr;
  }

  public String sliceǃ(Pattern regex, int group) {
    String slicedStr = slice(regex, group);
    if (slicedStr == null)
      return null;

    str = str.replace(slicedStr, "");
    return slicedStr;
  }

  public String sliceǃ(String matchStr) {
    String slicedStr = slice(matchStr);
    if (slicedStr == null)
      return null;

    str = str.replace(slicedStr, "");
    return slicedStr;
  }

  public RubyArray<String> split() {
    return ra(str.trim().replaceAll("  ", " ").split(" "));
  }

  public RubyArray<String> split(final String pattern) {
    if (pattern == null || pattern.equals(" "))
      return split();

    return newRubyArray(str.split(Pattern.quote(pattern)));
  }

  public RubyArray<String> split(String pattern, int limit) {
    if (pattern == null)
      return split();

    if (limit < 0)
      return newRubyArray(str.split(Pattern.quote(pattern)));
    else
      return newRubyArray(str.split(Pattern.quote(pattern), limit));
  }

  public RubyArray<String> split(Pattern pattern) {
    if (pattern == null)
      return split();

    return newRubyArray(str.split(pattern.pattern()));
  }

  public RubyArray<String> split(Pattern pattern, int limit) {
    if (pattern == null)
      return split();

    if (limit < 0)
      return newRubyArray(str.split(pattern.pattern()));
    else
      return newRubyArray(str.split(pattern.pattern(), limit));
  }

  public String squeeze() {
    return squeeze(".");
  }

  public String squeeze(String regex) {
    checkNotNull(regex);

    return str.replaceAll("(" + regex + ")\\1+", "$1");
  }

  public String squeezeǃ(String regex) {
    String squeezedStr = squeeze(regex);
    if (squeezedStr.equals(str))
      return null;

    return str = squeezedStr;
  }

  public boolean startWithʔ(String prefix, String... otherPrefix) {
    if (str.startsWith(prefix))
      return true;

    for (String p : otherPrefix) {
      if (str.startsWith(p))
        return true;
    }

    return false;
  }

  public String strip() {
    return str.trim();
  }

  public String stripǃ() {
    String strippedStr = str.trim();
    if (strippedStr.equals(str))
      return null;

    return str = strippedStr;
  }

  public String sub(String pattern, String replacement) {
    return str.replaceFirst(pattern, replacement);
  }

  public String sub(String pattern, Map<String, ?> map) {
    String result = str;
    Matcher matcher = qr(pattern).matcher(str);
    if (matcher.find()) {
      String target = matcher.group();
      if (map.containsKey(target))
        result = result.replace(target, map.get(target).toString());
    }
    return result;
  }

  public String sub(String pattern, TransformBlock<String, String> block) {
    Matcher matcher = qr(pattern).matcher(str);
    if (matcher.find()) {
      String match = matcher.group();
      return str.replace(match, block.yield(match));
    }
    return str;
  }

  public String subǃ(String pattern, String replacement) {
    if (qr(pattern).matcher(str).find())
      return str.replaceFirst(pattern, replacement);
    else
      return null;
  }

  public String subǃ(String pattern, TransformBlock<String, String> block) {
    Matcher matcher = qr(pattern).matcher(str);
    if (matcher.find()) {
      String match = matcher.group();
      return str.replace(match, block.yield(match));
    }
    return null;
  }

  public String succ() {
    return StringSuccessor.getInstance().succ(str);
  }

  public String succǃ() {
    return str = StringSuccessor.getInstance().succ(str);
  }

  public int sum() {
    return sum(16);
  }

  public int sum(int n) {
    int sum = 0;
    for (byte b : str.getBytes()) {
      sum += (int) b;
    }
    return sum % (int) Math.pow(2, n - 1);
  }

  public String swapcase() {
    final Pattern upperCase = qr("[A-Z]");
    return chars().map(new TransformBlock<String, String>() {

      @Override
      public String yield(String item) {
        return upperCase.matcher(item).matches() ? item.toLowerCase() : item
            .toUpperCase();
      }

    }).join();
  }

  public RubyString swapcaseǃ() {
    String swappedStr = swapcase();
    if (swappedStr.equals(str))
      return null;

    str = swappedStr;
    return this;
  }

  public double toF() {
    Matcher intMatcher =
        qr("^\\s*[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?").matcher(str);
    if (intMatcher.find())
      return Double.valueOf(intMatcher.group().trim());
    else
      return 0.0;
  }

  public int toI() {
    Matcher intMatcher = qr("^\\s*[-+]?\\d+").matcher(str);
    if (intMatcher.find())
      return Integer.valueOf(intMatcher.group().trim());
    else
      return 0;
  }

  public int toI(int base) {
    if (base < 2 || base > 32)
      throw new IllegalArgumentException("ArgumentError: invalid radix " + base);

    int i = 0;
    Matcher intMatcher = qr("^\\s*[-+]?\\d+").matcher(str);
    if (intMatcher.find()) {
      int multiplier = 1;
      RubyArray<String> digits = rs(intMatcher.group().trim()).chars();
      while (digits.anyʔ()) {
        i += Integer.valueOf(digits.pop()) * multiplier;
        multiplier = multiplier * base;
      }
    }

    return i;
  }

  public String toS() {
    return str;
  }

  public String toStr() {
    return str;
  }

  public String tr(String fromStr, String toStr) {
    fromStr = charSet2Str(checkNotNull(fromStr));
    toStr = charSet2Str(checkNotNull(toStr));
    if (fromStr.startsWith("^")) {
      return str.replaceAll("[" + fromStr + "]",
          toStr.isEmpty() ? "" : rs(toStr).chars().last());
    } else {
      RubyArray<String> fromStrAry =
          rs(fromStr.replace("\\^", "^").replace("\\-", "-")).chars();
      RubyArray<String> toStrAry = rs(toStr).chars();
      if (toStrAry.isEmpty())
        toStrAry.fill("", 0, fromStrAry.length());
      else if (toStrAry.length() < fromStrAry.length())
        toStrAry.fill(toStrAry.last(), toStrAry.size(), fromStrAry.length()
            - toStrAry.length());

      @SuppressWarnings("unchecked")
      RubyHash<String, String> rh = Hash(fromStrAry.zip(toStrAry));
      return gsub(
          "["
              + fromStr.replace("\\", "\\\\").replace("[", "\\[")
                  .replace("]", "\\]") + "]", rh);
    }
  }

  public RubyString trǃ(String fromStr, String toStr) {
    String trimmedStr = tr(fromStr, toStr);
    if (trimmedStr.equals(str))
      return null;

    str = trimmedStr;
    return this;
  }

  public String trS(String fromStr, String toStr) {
    final String fromString = charSet2Str(checkNotNull(fromStr));
    toStr = charSet2Str(checkNotNull(toStr));
    String trStr = "";
    if (fromString.startsWith("^")) {
      trStr =
          str.replaceAll("[" + fromString + "]",
              toStr.isEmpty() ? "" : rs(toStr).chars().last());
    } else {
      RubyArray<String> fromStrAry =
          rs(fromString.replace("\\^", "^").replace("\\-", "-")).chars();
      RubyArray<String> toStrAry = rs(toStr).chars();
      if (toStrAry.isEmpty())
        toStrAry.fill("", 0, fromStrAry.length());
      else if (toStrAry.length() < fromStrAry.length())
        toStrAry.fill(toStrAry.last(), toStrAry.size(), fromStrAry.length()
            - toStrAry.length());

      @SuppressWarnings("unchecked")
      RubyHash<String, String> rh = Hash(fromStrAry.zip(toStrAry));
      trStr =
          gsub(
              "["
                  + fromString.replace("\\", "\\\\").replace("[", "\\[")
                      .replace("]", "\\]") + "]", rh);
    }

    RubyArray<Boolean> matchIndice =
        chars().map(new TransformBlock<String, Boolean>() {

          @Override
          public Boolean yield(String item) {
            return item.matches("[" + fromString + "]");
          }

        });

    final Boolean[] prev = { matchIndice.get(0) };
    RubyArray<Integer> matchCounts =
        matchIndice.sliceBefore(new BooleanBlock<Boolean>() {

          @Override
          public boolean yield(Boolean item) {
            if (item == prev[0]) {
              return false;
            } else {
              prev[0] = item;
              return true;
            }
          }

        }).map("count");

    String trSqueezed = "";
    int i = 0;
    for (Integer mc : matchCounts) {
      if (matchIndice.get(i)) {
        trSqueezed += rs(trStr.substring(i, i + mc)).squeeze();
        i += mc;
      } else {
        trSqueezed += trStr.substring(i, i + mc);
        i += mc;
      }
    }
    return trSqueezed;
  }

  public RubyString trSǃ(String fromStr, String toStr) {
    String trimmedStr = trS(fromStr, toStr);
    if (trimmedStr.equals(str))
      return null;

    str = trimmedStr;
    return this;
  }

  public RubyArray<String> unpack(String format) {
    return Unpacker.unpack(format, str);
  }

  public String upcase() {
    return str.toUpperCase();
  }

  public String upcaseǃ() {
    String upcasedStr = str.toUpperCase();
    if (upcasedStr.equals(str))
      return null;

    return str = upcasedStr;
  }

  public RubyEnumerator<String> upto(String otherStr) {
    return upto(otherStr, false);
  }

  public RubyEnumerator<String> upto(final String otherStr, boolean exclusive) {
    if (exclusive) {
      return newRubyEnumerator((Iterable<String>) range(str, otherStr).lazy()
          .takeWhile(new BooleanBlock<String>() {

            @Override
            public boolean yield(String item) {
              return !item.equals(otherStr);
            }

          }));
    } else
      return range(str, otherStr).each();
  }

  public boolean validEncodingʔ(Charset encoding) {
    CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
    CharsetEncoder encoder = encoding.newEncoder();
    ByteBuffer tmp;
    try {
      tmp = encoder.encode(CharBuffer.wrap(str));
    } catch (CharacterCodingException e) {
      return false;
    }
    try {
      decoder.decode(tmp);
      return true;
    } catch (CharacterCodingException e) {
      return false;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof RubyString) {
      RubyString rs = (RubyString) o;
      return str.equals(rs.str);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return str.hashCode();
  }

  @Override
  public String toString() {
    return str;
  }

  @Override
  public int length() {
    return str.length();
  }

  @Override
  public char charAt(int index) {
    return str.charAt(index);
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    return str.subSequence(start, end);
  }

}
