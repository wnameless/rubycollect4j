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
    CharSequence, Comparable<CharSequence> {

  private String str;

  /**
   * Returns a RubyString with an empty String as default value.
   */
  public RubyString() {
    str = "";
  }

  /**
   * Returns a RubyString with calling {@link Object#toString()} on given Object
   * as default value.
   * 
   * @param o
   *          any Object
   * @throws TypeConstraintException
   *           if o is null
   */
  public RubyString(Object o) {
    str = checkNotNull(o);
  }

  @Override
  protected Iterable<String> getIterable() {
    final String iterStr = str;
    return range(0, iterStr.length() - 1).lazy().map(
        new TransformBlock<Integer, String>() {

          @Override
          public String yield(Integer item) {
            return iterStr.substring(item, item + 1);
          }

        });
  }

  private String checkNotNull(Object o) {
    if (o == null)
      throw new TypeConstraintException(
          "TypeError: no implicit conversion of null into String");

    return o.toString();
  }

  /**
   * Checks if RubyString only contains ascii.
   * 
   * @return true if this RubyString only contains ascii, false otherwise
   */
  public boolean asciiOnlyʔ() {
    return str.matches("^[\\u0000-\\u007F]*$");
  }

  /**
   * Returns a copied string whose encoding is ASCII-8BIT.
   * 
   * @return a new RubyString
   */
  public RubyString b() {
    return rs(new String(str.getBytes(), Charset.forName("ISO-8859-1")));
  }

  /**
   * Returns an RubyArray of Byte in RubyString.
   * 
   * @return a RubyArray of Byte
   */
  public RubyArray<Byte> bytes() {
    RubyArray<Byte> bytes = newRubyArray();
    for (Byte b : str.getBytes()) {
      bytes.add(b);
    }
    return bytes;
  }

  /**
   * Returns the length of RubyString in bytes.
   * 
   * @return the length of bytes
   */
  public int bytesize() {
    return str.getBytes().length;
  }

  /**
   * Returns a substring of one byte at given index.
   * 
   * @param index
   *          of target byte
   * @return s RubyString of sliced byte
   */
  public RubyString byteslice(int index) {
    Byte ch = bytes().at(index);
    if (ch == null)
      return null;

    return rs(new String(new byte[] { ch }, Charset.forName("ISO-8859-1")));
  }

  /**
   * Returns a substring starting at the offset given by the first, and a length
   * given by the second.
   * 
   * @param offset
   *          begin index
   * @param length
   *          of the bytes
   * @return a new RubyString
   */
  public RubyString byteslice(int offset, int length) {
    RubyArray<Byte> bytes = bytes().slice(offset, length);
    if (bytes == null)
      return null;

    byte[] byteAry = new byte[bytes.size()];
    for (int i = 0; i < byteAry.length; i++) {
      byteAry[i] = bytes.get(i);
    }
    return rs(new String(byteAry));
  }

  /**
   * Returns a copy of RubyString with the first character converted to
   * uppercase and the remainder to lowercase.
   * 
   * @return a new RubyString
   */
  public RubyString capitalize() {
    if (str.isEmpty())
      return this;

    return rs(str.substring(0, 1).toUpperCase()
        + str.substring(1).toLowerCase());
  }

  /**
   * Modifies RubyString by converting the first character to uppercase and the
   * remainder to lowercase.
   * 
   * @return this RubyString if capitalized, null otherwise
   */
  public RubyString capitalizeǃ() {
    RubyString capitals = capitalize();
    if (capitals.equals(this))
      return null;

    str = capitals.toS();
    return this;
  }

  /**
   * Case-insensitive version of String#compareTo.
   * 
   * @param charSeq
   *          any CharSequence
   * @return a negative integer, zero, or a positive integer as the specified
   *         CharSequence is greater than, equal to, or less than this String,
   *         ignoring case considerations.
   */
  public int casecmp(CharSequence charSeq) {
    return this.str.compareToIgnoreCase(str.toString());
  }

  public RubyString center(int width) {
    return center(width, " ");
  }

  /**
   * Returns a new RubyString of length width with str centered and padded with
   * padstr; otherwise, returns str.
   * 
   * @param width
   *          of result
   * @param padstr
   *          used to pad in front and after the str
   * @return a new RubyString
   */
  public RubyString center(int width, String padstr) {
    checkNotNull(padstr);
    if (padstr.isEmpty())
      throw new IllegalArgumentException("ArgumentError: zero width padding");

    if (width <= str.length())
      return rs(str);

    RubyArray<String> centeredStr = newRubyArray();
    int start = (width - str.length()) / 2;
    RubyLazyEnumerator<String> padStr = rs(padstr).lazy().cycle();
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
    return rs(centeredStr.join());
  }

  /**
   * Returns a RubyArray of characters in str.
   * 
   * @return a RubyArray
   */
  public RubyArray<String> chars() {
    return newRubyArray(str.split("(?!^)"));
  }

  /**
   * Returns a new RubyString with the line separator removed from the end of
   * str.
   * 
   * @return a new RubyString
   */
  public RubyString chomp() {
    String result = str.replaceFirst("\r\n$", "");
    if (result.length() < str.length())
      return rs(result);

    result = result.replaceFirst("\r$", "");
    if (result.length() < str.length())
      return rs(result);

    return rs(result.replaceFirst("\n$", ""));
  }

  /**
   * Modifies str in place as described for RubyString#chomp, returning str, or
   * null if no modifications were made.
   * 
   * @return this RubyString or null
   */
  public RubyString chompǃ() {
    RubyString chomppedStr = chomp();
    if (chomppedStr.equals(this))
      return null;

    str = chomppedStr.toS();
    return this;
  }

  /**
   * Returns a new RubyString with the given line separator removed from the end
   * of str.
   * 
   * @param separator
   *          a line separator
   * @return a new RubyString
   */
  public RubyString chomp(String separator) {
    separator = separator == null ? "" : separator;
    if (str.endsWith(separator))
      return rs(str.substring(0, str.lastIndexOf(separator)));

    return rs(str);
  }

  /**
   * Modifies str in place as described for RubyString#chomp, returning str, or
   * null if no modifications were made.
   * 
   * @return this RubyString or null
   */
  public RubyString chompǃ(String separator) {
    RubyString chomppedStr = chomp(separator);
    if (chomppedStr.equals(this))
      return null;

    str = chomppedStr.toS();
    return this;
  }

  /**
   * Returns a new RubyString with the last character removed. If the string
   * ends with \r\n, both characters are removed.
   * 
   * @return a new RubyString
   */
  public RubyString chop() {
    if (str.isEmpty())
      return rs(str);

    if (str.endsWith("\r\n"))
      return rs(str.substring(0, str.length() - 2));
    else
      return rs(str.substring(0, str.length() - 1));
  }

  /**
   * Processes str as for RubyString#chop, returning str, or null if str is the
   * empty string.
   * 
   * @return a new RubyString or null
   */
  public RubyString chopǃ() {
    RubyString choppedStr = chop();
    if (choppedStr.equals(this))
      return null;

    str = choppedStr.toS();
    return this;
  }

  /**
   * Returns a one-character string at the beginning of the string.
   * 
   * @return a new RubyString
   */
  public RubyString chr() {
    if (str.isEmpty())
      return this;

    return rs(str.substring(0, 1));
  }

  /**
   * Makes string empty.
   * 
   * @return this RubyString
   */
  public RubyString clear() {
    str = "";
    return this;
  }

  /**
   * Returns a RubyArray of the Integer ordinals of the characters in str.
   * 
   * @return a RubyArray
   */
  public RubyArray<Integer> codepoints() {
    RubyArray<Integer> codepoints = newRubyArray();
    for (int i = 0; i < str.length(); i++) {
      codepoints.add(str.codePointAt(i));
    }
    return codepoints;
  }

  /**
   * Appends the given codepoint as a character to str.
   * 
   * @param codepoint
   *          of a character
   * @return this RubyString
   */
  public RubyString concat(int codepoint) {
    str += (char) codepoint;
    return this;
  }

  /**
   * Appends the given object to str.
   * 
   * @param o
   *          any Object
   * @return this RubyString
   */
  public RubyString concat(Object o) {
    str += o;
    return this;
  }

  /**
   * Each charSet parameter defines a set of characters to count. The
   * intersection of these sets defines the characters to count in str. Any
   * charSet that starts with a caret ^ is negated. The sequence c1-c2 means all
   * characters between c1 and c2. The backslash character can be used to escape
   * ^ or -.
   * 
   * @param charSet
   *          a set of characters
   * @param charSets
   *          sets of characters
   * @return the total count
   */
  public int count(final String charSet, final String... charSets) {
    checkNotNull(charSet);
    checkNotNull(charSets);

    return count(new BooleanBlock<String>() {

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

    if (!charSet.startsWith("^") && !rs(charSet2Str(charSet)).includeʔ(ch))
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

  /**
   * Applies a one-way cryptographic hash to str by invoking the
   * MessageDigest(MD5) with the given salt string.
   * 
   * @param salt
   *          a secret string
   * @return a new RubyString
   */
  public RubyString crypt(String salt) {
    checkNotNull(salt);

    String md5 = null;
    MessageDigest digest;
    try {
      digest = MessageDigest.getInstance("MD5");
      digest.update(salt.getBytes(), 0, salt.length());
      md5 = new BigInteger(1, digest.digest()).toString(16);
    } catch (NoSuchAlgorithmException e) {}
    return rs(md5);
  }

  /**
   * Returns a copy of str with all characters in the intersection of its
   * arguments deleted. Uses the same rules for building the set of characters
   * as RubyString#count.
   * 
   * @param charSet
   *          a set of characters
   * @return a new RubyString
   */
  public RubyString delete(final String charSet) {
    checkNotNull(charSet);

    return rs(chars().deleteIf((new BooleanBlock<String>() {

      @Override
      public boolean yield(String item) {
        if (!isCharSetMatched(item, charSet))
          return false;

        return true;
      }

    })).join());
  }

  /**
   * Performs a delete operation in place, returning str, or null if str was not
   * modified.
   * 
   * @param charSet
   *          a set of characters
   * @return a new RubyString or null
   */
  public RubyString deleteǃ(String charSet) {
    RubyString deletedStr = delete(charSet);
    if (deletedStr.equals(this))
      return null;

    str = deletedStr.toS();
    return this;
  }

  /**
   * Returns a copy of str with all uppercase letters replaced with their
   * lowercase.
   * 
   * @return a new RubyString
   */
  public RubyString downcase() {
    return rs(str.toLowerCase());
  }

  public RubyString downcaseǃ() {
    RubyString downcasedStr = downcase();
    if (downcasedStr.equals(this))
      return null;

    str = downcasedStr.toS();
    return this;
  }

  /**
   * Produces a version of str with all non-printing or non-ASCII 8bit
   * characters replaced by \\nnn or \\unnnn notation and all special characters
   * escaped.
   * 
   * @return a new RubyString
   */
  public RubyString dump() {
    String printable = chars().map(new TransformBlock<String, String>() {

      @Override
      public String yield(String item) {
        Integer codepoint = item.codePointAt(0);

        if (item.matches("\b"))
          return "\\b";
        else if (item.matches("\f"))
          return "\\f";
        else if (item.matches("\n"))
          return "\\n";
        else if (item.matches("\r"))
          return "\\r";
        else if (item.matches("\t"))
          return "\\t";
        else if (codepoint < 256 && !item.matches("\\p{C}")) {
          return item;
        } else {
          if (codepoint < 256)
            return "\\" + Integer.toOctalString(codepoint);
          else
            return "\\u" + Integer.toHexString(codepoint);
        }
      }

    }).join();
    return rs("\"" + printable + "\"");
  }

  /**
   * Returns a RubyEnumerator of each byte in str.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<Byte> eachByte() {
    return bytes().each();
  }

  /**
   * Passes each byte in str to the given block.
   * 
   * @param block
   *          to yield byte
   * @return this RubyString
   */
  public RubyString eachByte(Block<Byte> block) {
    bytes().each(block);
    return this;
  }

  /**
   * Returns a RubyEnumerator of each character in str.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<String> eachChar() {
    return each();
  }

  /**
   * Passes each character in str to the given block.
   * 
   * @param block
   *          to yield character
   * @return this RubyString
   */
  public RubyString eachChar(Block<String> block) {
    each(block);
    return this;
  }

  /**
   * Returns a RubyEnumerator of each codepoint in str.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<Integer> eachCodepoint() {
    return newRubyEnumerator((Iterable<Integer>) each().lazy().map(
        new TransformBlock<String, Integer>() {

          @Override
          public Integer yield(String item) {
            return item.codePointAt(0);
          }

        }));
  }

  /**
   * Passes each codepoint in str to the given block.
   * 
   * @param block
   *          to yield character
   * @return this RubyString
   */
  public RubyString eachCodepoint(Block<Integer> block) {
    eachCodepoint().each(block);
    return this;
  }

  public RubyEnumerator<String> eachLine() {
    return ra(str.split(System.getProperty("line.separator"))).each();
  }

  public RubyString eachLine(Block<String> block) {
    eachLine().each(block);
    return this;
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

  public RubyString encode(String encoding) {
    return rs(new String(str.getBytes(), Charset.forName(encoding)));
  }

  public RubyString encode(String dstEncoding, String srcEncoding) {
    return rs(new String(str.getBytes(Charset.forName(srcEncoding)),
        Charset.forName(dstEncoding)));
  }

  public RubyString encodeǃ(String encoding) {
    str = encode(encoding).toS();
    return this;
  }

  public RubyString encodeǃ(String dstEncoding, String srcEncoding) {
    str = encode(dstEncoding, srcEncoding).toS();
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

  public RubyString forceEncoding(String encoding) {
    str = encode(encoding).toS();
    return this;
  }

  public Byte getbyte(int index) {
    return bytes().at(index);
  }

  public RubyString gsub(String pattern, String replacement) {
    return rs(str.replaceAll(pattern, replacement));
  }

  public RubyString gsub(String pattern, Map<String, ?> map) {
    String result = str;
    Matcher matcher = qr(pattern).matcher(str);
    while (matcher.find()) {
      String target = matcher.group();
      if (map.containsKey(target))
        result = result.replace(target, map.get(target).toString());
    }
    return rs(result);
  }

  public RubyString gsub(String pattern, TransformBlock<String, String> block) {
    String result = str;
    Matcher matcher = qr(pattern).matcher(str);
    while (matcher.find()) {
      String target = matcher.group();
      result = result.replace(target, block.yield(target));
    }
    return rs(result);
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
    RubyString gsubedStr = gsub(pattern, replacement);
    if (gsubedStr.equals(this))
      return null;

    str = gsubedStr.toS();
    return this;
  }

  public RubyString gsubǃ(String pattern, TransformBlock<String, String> block) {
    RubyString gsubedStr = gsub(pattern, block);
    if (gsubedStr.equals(this))
      return null;

    str = gsubedStr.toS();
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

  public RubyString inspect() {
    String printable = chars().map(new TransformBlock<String, String>() {

      @Override
      public String yield(String item) {
        if (item.matches("\b"))
          return "\\b";
        else if (item.matches("\f"))
          return "\\f";
        else if (item.matches("\n"))
          return "\\n";
        else if (item.matches("\r"))
          return "\\r";
        else if (item.matches("\t"))
          return "\\t";
        else if (item.matches("\\p{C}")) {
          Integer codepoint = item.codePointAt(0);
          if (codepoint < 256)
            return "\\" + Integer.toOctalString(codepoint);
          else
            return "\\u" + Integer.toHexString(codepoint);
        }
        return item;
      }

    }).join();
    return rs("\"" + printable + "\"");
  }

  public RubyArray<String> lines() {
    return eachLine().toA();
  }

  public RubyArray<String> lines(String separator) {
    return eachLine(separator).toA();
  }

  public RubyString ljust(int width) {
    return ljust(width, " ");
  }

  public RubyString ljust(int width, String padstr) {
    RubyLazyEnumerator<String> padStr = rs(padstr).lazy().cycle();

    int extra = width - str.length();
    if (extra > 0) {
      StringBuilder sb = new StringBuilder();
      while (extra > 0) {
        sb.append(padStr.next());
        extra--;
      }
      return rs(str + sb.toString());
    }

    return rs(str);
  }

  public RubyString lstrip() {
    return rs(str.replaceFirst("^\\s+", ""));
  }

  public RubyString lstripǃ() {
    RubyString lstrippedStr = lstrip();
    if (lstrippedStr.equals(this))
      return null;

    str = lstrippedStr.toS();
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

  public RubyString reverse() {
    return rs(new StringBuilder(str).reverse().toString());
  }

  public RubyString reverseǃ() {
    RubyString reversedStr = reverse();
    if (reversedStr.equals(this))
      return null;

    str = reversedStr.toS();
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

  public RubyString rjust(int width) {
    return rjust(width, " ");
  }

  public RubyString rjust(int width, String padstr) {
    RubyLazyEnumerator<String> padStr = rs(padstr).lazy().cycle();

    int extra = width - str.length();
    if (extra > 0) {
      StringBuilder sb = new StringBuilder();
      while (extra > 0) {
        sb.append(padStr.next());
        extra--;
      }
      return rs(sb.toString() + str);
    }

    return this;
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

  public RubyString rstrip() {
    return rs(str.replaceFirst("\\s+$", ""));
  }

  public RubyString rstripǃ() {
    RubyString rstrippedStr = rstrip();
    if (rstrippedStr.equals(this))
      return null;

    str = rstrippedStr.toS();
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

  public RubyString scrub() {
    return rs(str.replaceAll("\\p{C}", "\uFFFD"));
  }

  public RubyString scrub(String repl) {
    if (repl == null)
      return scrub();

    return rs(str.replaceAll("\\p{C}", repl));
  }

  public RubyString scrub(final TransformBlock<RubyArray<Byte>, String> block) {
    return rs(map(new TransformBlock<String, String>() {

      @Override
      public String yield(String item) {
        if (item.matches("\\p{C}"))
          return block.yield(rs(item).bytes());
        else
          return item;
      }

    }).join());
  }

  public RubyString scrubǃ() {
    RubyString scrubbedStr = scrub();
    if (scrubbedStr.equals(this))
      return null;

    str = scrubbedStr.toS();
    return this;
  }

  public RubyString scrubǃ(String repl) {
    RubyString scrubbedStr = scrub(repl);
    if (scrubbedStr.equals(this))
      return null;

    str = scrubbedStr.toS();
    return this;
  }

  public RubyString scrubǃ(final TransformBlock<RubyArray<Byte>, String> block) {
    RubyString scrubbedStr = scrub(block);
    if (scrubbedStr.equals(this))
      return null;

    str = scrubbedStr.toS();
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

  public RubyString slice(int index) {
    return rs(chars().slice(index));
  }

  public RubyString slice(int index, int length) {
    RubyArray<String> slicedStr = chars().slice(index, length);
    return slicedStr == null ? null : rs(slicedStr.join());
  }

  public RubyString slice(Pattern regex) {
    Matcher matcher = regex.matcher(str);
    if (matcher.find())
      return rs(matcher.group());
    else
      return null;
  }

  public RubyString slice(Pattern regex, int group) {
    Matcher matcher = regex.matcher(str);
    if (matcher.find())
      if (group < 1 || group > matcher.groupCount())
        return null;
      else
        return rs(matcher.group(group));
    else
      return null;
  }

  public RubyString slice(String matchStr) {
    return str.contains(matchStr) ? rs(matchStr) : null;
  }

  public Integer sliceǃ(int index) {
    RubyArray<String> chars = chars();
    String slicedStr = chars.sliceǃ(index);
    if (slicedStr == null)
      return null;

    str = chars.join();
    return index;
  }

  public RubyString sliceǃ(int index, int length) {
    RubyArray<String> chars = chars();
    RubyArray<String> slicedStr = chars.sliceǃ(index, length);
    if (slicedStr == null)
      return null;

    str = chars.join();
    return rs(slicedStr.join());
  }

  public RubyString sliceǃ(Pattern regex) {
    RubyString slicedStr = slice(regex);
    if (slicedStr == null)
      return null;

    str = str.replace(slicedStr, "");
    return slicedStr;
  }

  public RubyString sliceǃ(Pattern regex, int group) {
    RubyString slicedStr = slice(regex, group);
    if (slicedStr == null)
      return null;

    str = str.replace(slicedStr, "");
    return slicedStr;
  }

  public RubyString sliceǃ(String matchStr) {
    RubyString slicedStr = slice(matchStr);
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

  public RubyString squeeze() {
    return squeeze(".");
  }

  public RubyString squeeze(String regex) {
    checkNotNull(regex);

    return rs(str.replaceAll("(" + regex + ")\\1+", "$1"));
  }

  public RubyString squeezeǃ(String regex) {
    RubyString squeezedStr = squeeze(regex);
    if (squeezedStr.equals(str))
      return null;

    str = squeezedStr.toS();
    return this;
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

  public RubyString strip() {
    return rs(str.trim());
  }

  public RubyString stripǃ() {
    RubyString strippedStr = strip();
    if (strippedStr.equals(this))
      return null;

    str = strippedStr.toS();
    return this;
  }

  public RubyString sub(String pattern, String replacement) {
    return rs(str.replaceFirst(pattern, replacement));
  }

  public RubyString sub(String pattern, Map<String, ?> map) {
    String result = str;
    Matcher matcher = qr(pattern).matcher(str);
    if (matcher.find()) {
      String target = matcher.group();
      if (map.containsKey(target))
        result = result.replace(target, map.get(target).toString());
    }
    return rs(result);
  }

  public RubyString sub(String pattern, TransformBlock<String, String> block) {
    Matcher matcher = qr(pattern).matcher(str);
    if (matcher.find()) {
      String match = matcher.group();
      return rs(str.replace(match, block.yield(match)));
    }
    return this;
  }

  public RubyString subǃ(String pattern, String replacement) {
    if (qr(pattern).matcher(str).find())
      return rs(str.replaceFirst(pattern, replacement));
    else
      return null;
  }

  public RubyString subǃ(String pattern, TransformBlock<String, String> block) {
    Matcher matcher = qr(pattern).matcher(str);
    if (matcher.find()) {
      String match = matcher.group();
      return rs(str.replace(match, block.yield(match)));
    }
    return null;
  }

  public RubyString succ() {
    return rs(StringSuccessor.getInstance().succ(str));
  }

  public RubyString succǃ() {
    str = succ().toS();
    return this;
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

  public RubyString swapcase() {
    final Pattern upperCase = qr("[A-Z]");
    return rs(map(new TransformBlock<String, String>() {

      @Override
      public String yield(String item) {
        return upperCase.matcher(item).matches() ? item.toLowerCase() : item
            .toUpperCase();
      }

    }).join());
  }

  public RubyString swapcaseǃ() {
    RubyString swappedStr = swapcase();
    if (swappedStr.equals(this))
      return null;

    str = swappedStr.toS();
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

  public RubyString tr(String fromStr, String toStr) {
    fromStr = charSet2Str(checkNotNull(fromStr));
    toStr = charSet2Str(checkNotNull(toStr));
    if (fromStr.startsWith("^")) {
      return rs(str.replaceAll("[" + fromStr + "]",
          toStr.isEmpty() ? "" : rs(toStr).chars().last()));
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
    RubyString trimmedStr = tr(fromStr, toStr);
    if (trimmedStr.equals(this))
      return null;

    str = trimmedStr.toS();
    return this;
  }

  public RubyString trS(String fromStr, String toStr) {
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
                      .replace("]", "\\]") + "]", rh).toS();
    }

    RubyArray<Boolean> matchIndice = map(new TransformBlock<String, Boolean>() {

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
    return rs(trSqueezed);
  }

  public RubyString trSǃ(String fromStr, String toStr) {
    RubyString trimmedStr = trS(fromStr, toStr);
    if (trimmedStr.equals(this))
      return null;

    str = trimmedStr.toS();
    return this;
  }

  public RubyArray<String> unpack(String format) {
    return Unpacker.unpack(format, str);
  }

  public RubyString upcase() {
    return rs(str.toUpperCase());
  }

  public RubyString upcaseǃ() {
    RubyString upcasedStr = upcase();
    if (upcasedStr.equals(this))
      return null;

    str = upcasedStr.toS();
    return this;
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

  @Override
  public int compareTo(CharSequence o) {
    return str.compareTo(o.toString());
  }

}
