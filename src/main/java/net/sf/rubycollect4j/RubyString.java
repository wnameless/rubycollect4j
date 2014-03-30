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

import static java.lang.Character.MAX_RADIX;
import static java.lang.Character.MIN_RADIX;
import static net.sf.rubycollect4j.RubyCollections.Hash;
import static net.sf.rubycollect4j.RubyCollections.isBlank;
import static net.sf.rubycollect4j.RubyCollections.isNotBlank;
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
    str = stringify(o);
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

  private String stringify(Object o) {
    if (o == null)
      throw new TypeConstraintException(
          "TypeError: no implicit conversion of null into String");

    return o.toString();
  }

  private RubyString inPlace(RubyString rs) {
    if (this.equals(rs))
      return null;

    str = rs.toS();
    return this;
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
    return inPlace(capitalize());
  }

  /**
   * Case-insensitive version of RubyString#compareTo.
   * 
   * @param charSeq
   *          any CharSequence
   * @return a negative integer, zero, or a positive integer as the specified
   *         CharSequence is greater than, equal to, or less than this String,
   *         ignoring case considerations.
   */
  public int casecmp(CharSequence charSeq) {
    return str.compareToIgnoreCase(charSeq.toString());
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
    if (stringify(padstr).isEmpty())
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
    return inPlace(chomp());
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
    return inPlace(chomp(separator));
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
    return inPlace(chop());
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
    return count(new BooleanBlock<String>() {

      @Override
      public boolean yield(String item) {
        if (!isCharSetMatched(item, stringify(charSet)))
          return false;

        if (charSets != null) {
          for (String cs : charSets) {
            if (!isCharSetMatched(item, stringify(cs)))
              return false;
          }
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
    charSet = charSet.replaceAll("^\\\\([^-^])", "$1");
    Matcher matcher = qr("[^\\\\]-.").matcher(charSet);
    while (matcher.find()) {
      String charRg = matcher.group();
      RubyRange<Character> rr = range(charRg.charAt(0), charRg.charAt(2));
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
    String encrypt = str + stringify(salt);

    String md5 = null;
    MessageDigest digest;
    try {
      digest = MessageDigest.getInstance("MD5");
      digest.update(encrypt.getBytes(), 0, encrypt.length());
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
    stringify(charSet);

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
    return inPlace(delete(charSet));
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

  /**
   * Downcases the contents of str, returning null if no changes were made.
   * 
   * @return this RubyString or null
   */
  public RubyString downcaseǃ() {
    return inPlace(downcase());
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

  /**
   * Returns a RubyEnumerator of each line in str.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<String> eachLine() {
    return ra(str.split(System.getProperty("line.separator"))).each();
  }

  /**
   * Passes each line in str to the given block.
   * 
   * @param block
   *          to yield line
   * @return this RubyString
   */
  public RubyString eachLine(Block<String> block) {
    eachLine().each(block);
    return this;
  }

  /**
   * Returns a RubyEnumerator of each line by given separator in str.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<String> eachLine(String separator) {
    return ra(str.split(stringify(separator))).each();
  }

  /**
   * Passes each line by given separator in str to the given block.
   * 
   * @param block
   *          to yield line
   * @return this RubyString
   */
  public RubyString eachLine(String separator, Block<String> block) {
    ra(str.split(stringify(separator))).each(block);
    return this;
  }

  /**
   * Returns true if str has a length of zero.
   * 
   * @return true if str has a length of zero, false otherwise
   */
  public boolean emptyʔ() {
    return str.isEmpty();
  }

  /**
   * Returns a copy of str transcoded to encoding encoding.
   * 
   * @param encoding
   *          name of encoding
   * @return a new RubyString
   */
  public RubyString encode(String encoding) {
    stringify(encoding);

    return rs(new String(str.getBytes(), Charset.forName(encoding)));
  }

  /**
   * Returns a copy of str transcoded from srcEncoding to dstEncoding.
   * 
   * @param dstEncoding
   *          name of destination encoding
   * @param srcEncoding
   *          name of source encoding
   * @return a new RubyString
   */
  public RubyString encode(String dstEncoding, String srcEncoding) {
    stringify(srcEncoding);
    stringify(dstEncoding);

    return rs(new String(str.getBytes(Charset.forName(srcEncoding)),
        Charset.forName(dstEncoding)));
  }

  /**
   * Returns the str transcoded to encoding encoding.
   * 
   * @param encoding
   *          name of encoding
   * @return this RubyString
   */
  public RubyString encodeǃ(String encoding) {
    str = encode(encoding).toS();
    return this;
  }

  /**
   * Returns the str transcoded from srcEncoding to dstEncoding.
   * 
   * @param dstEncoding
   *          name of destination encoding
   * @param srcEncoding
   *          name of source encoding
   * @return this RubyString
   */
  public RubyString encodeǃ(String dstEncoding, String srcEncoding) {
    str = encode(dstEncoding, srcEncoding).toS();
    return this;
  }

  /**
   * Returns UTF-8 Charset.
   * 
   * @return a Charset
   */
  public Charset encoding() {
    return Charset.forName("UTF-8");
  }

  /**
   * Returns true if str ends with one of the suffixes given.
   * 
   * @param suffix
   * @param otherSuffix
   * @return true if str ends with one of the suffixes given, false otherwise
   */
  public boolean endWithʔ(String suffix, String... otherSuffix) {
    if (str.endsWith(stringify(suffix)))
      return true;

    if (otherSuffix != null) {
      for (String s : otherSuffix) {
        if (str.endsWith(stringify(s)))
          return true;
      }
    }

    return false;
  }

  /**
   * Two RubyString are equal if they have the same length and content.
   * 
   * @param o
   *          any Object
   * @return true if two objects are equal, false otherwise
   */
  public boolean eqlʔ(Object o) {
    return equals(o);
  }

  /**
   * Changes the encoding to encoding and returns self.
   * 
   * @param encoding
   *          name of encoding
   * @return this RubyString
   */
  public RubyString forceEncoding(String encoding) {
    str = encode(encoding).toS();
    return this;
  }

  /**
   * Returns the indexth byte, or null if index was out of range.
   * 
   * @param index
   *          of a byte
   * @return a Byte or null
   */
  public Byte getbyte(int index) {
    return bytes().at(index);
  }

  /**
   * Returns a copy of str with the all occurrences of pattern substituted for
   * the second argument.
   * 
   * @param regex
   *          regular expression
   * @param replacement
   *          any String
   * @return a new RubyString
   */
  public RubyString gsub(String regex, String replacement) {
    return rs(str.replaceAll(stringify(regex), stringify(replacement)));
  }

  /**
   * Returns a copy of str with the all occurrences of pattern substituted for
   * the second argument. The second argument is a Map, and the matched text is
   * one of its keys, the corresponding value is the replacement string.
   * 
   * @param regex
   *          regular expression
   * @param map
   *          any Map
   * @return a new RubyString
   */
  public RubyString gsub(String regex, Map<String, ?> map) {
    if (isBlank(map))
      return rs(str);

    String result = str;
    Matcher matcher = qr(stringify(regex)).matcher(str);
    while (matcher.find()) {
      String target = matcher.group();
      if (map.containsKey(target))
        result = result.replace(target, map.get(target).toString());
    }
    return rs(result);
  }

  /**
   * Returns a copy of str with the all occurrences of pattern substituted for
   * the second argument. The second argument is a TransformBlock, and the value
   * returned by the block will be substituted for the match on each call.
   * 
   * @param regex
   *          regular expression
   * @param block
   *          to do the replacement
   * @return a new RubyString
   */
  public RubyString gsub(String regex, TransformBlock<String, String> block) {
    String result = str;
    Matcher matcher = qr(stringify(regex)).matcher(str);
    while (matcher.find()) {
      String target = matcher.group();
      result = result.replace(target, block.yield(target));
    }
    return rs(result);
  }

  /**
   * Returns a RubyEnumerator of all matched str.
   * 
   * @param regex
   *          regular expression
   * @return a RubyEnumerator
   */
  public RubyEnumerator<String> gsub(String regex) {
    RubyArray<String> matches = newRubyArray();
    Matcher matcher = qr(stringify(regex)).matcher(str);
    while (matcher.find()) {
      matches.add(matcher.group());
    }
    return matches.each();
  }

  /**
   * Performs the substitutions of RubyString#gsub in place, returning this
   * RubyString, or null if no substitutions were performed.
   * 
   * @param regex
   *          regular expression
   * @param replacement
   *          any String
   * @return this RubyString or null
   */
  public RubyString gsubǃ(String regex, String replacement) {
    return inPlace(gsub(regex, replacement));
  }

  /**
   * Performs the substitutions of RubyString#gsub in place, returning this
   * RubyString, or null if no substitutions were performed.
   * 
   * @param regex
   *          regular expression
   * @param block
   *          to do the replacement
   * @return this RubyString or null
   */
  public RubyString gsubǃ(String regex, TransformBlock<String, String> block) {
    return inPlace(gsub(regex, block));
  }

  /**
   * Returns a RubyEnumerator of all matched str.
   * 
   * @param regex
   *          regular expression
   * @return a RubyEnumerator
   */
  public RubyEnumerator<String> gsubǃ(String regex) {
    return gsub(regex);
  }

  /**
   * Return a hash based on the string’s length and content.
   * 
   * @return a hashcode
   */
  public int hash() {
    return hashCode();
  }

  /**
   * Treats leading characters from str as a string of hexadecimal digits (with
   * an optional sign and an optional 0x) and returns the corresponding number.
   * Zero is returned on error.
   * 
   * @return a long
   */
  public long hex() {
    RubyString hex = slice(qr("^\\s*[+|-]?\\s*(0x)?[0-9a-f]+"));
    if (hex == null)
      return 0;
    else
      return Long.parseLong(hex.toS().replaceAll("\\s+", "").replace("0x", ""),
          16);
  }

  /**
   * Returns true if str contains the given string.
   * 
   * @return true if str contains the given string, false otherwise
   */
  public boolean includeʔ(String otherStr) {
    return str.contains(stringify(otherStr));
  }

  /**
   * Returns the index of the first occurrence of the given substring in str.
   * Returns null if not found
   * 
   * @param substring
   *          any String
   * @return an Integer or null
   */
  public Integer index(String substring) {
    return index(stringify(substring), 0);
  }

  /**
   * Returns the index of the first occurrence of the given substring in str.
   * Returns null if not found. The second parameter specifies the position in
   * the string to begin the search.
   * 
   * @param substring
   *          any String
   * @param offset
   *          position to begin the search
   * @return an Integer or null
   */
  public Integer index(String substring, int offset) {
    int index = str.indexOf(stringify(substring), offset);
    return index == -1 ? null : index;
  }

  /**
   * Returns the index of the first occurrence of the given Pattern in str.
   * Returns null if not found.
   * 
   * @param regex
   *          a Pattern
   * @return an Integer or null
   */
  public Integer index(Pattern regex) {
    if (regex == null)
      throw new TypeConstraintException("TypeError: type mismatch: null given");

    return index(regex, 0);
  }

  public Integer index(Pattern regex, int offset) {
    if (regex == null)
      throw new TypeConstraintException("TypeError: type mismatch: null given");

    if (offset < 0 || offset > str.length())
      return null;

    Matcher matcher = regex.matcher(str);
    if (matcher.find(offset))
      return matcher.start();
    else
      return null;
  }

  /**
   * Inserts otherStr before the character at the given index, modifying str.
   * Negative indices count from the end of the string, and insert after the
   * given character. The intent is insert aString so that it starts at the
   * given index.
   * 
   * @param index
   *          position to begin insertion
   * @param otherStr
   *          any String
   * @return this RubyString
   */
  public RubyString insert(int index, String otherStr) {
    if (index < -str.length() - 1 || index > str.length())
      throw new IndexOutOfBoundsException("IndexError: index " + index
          + " out of string");

    str = chars().insert(index, stringify(otherStr)).join();
    return this;
  }

  /**
   * Returns a printable version of str, surrounded by quote marks, with special
   * characters escaped.
   * 
   * @return a new RubyString
   */
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

  /**
   * Returns a RubyArray of lines in str split using the supplied record
   * separator (System.getProperty("line.separator") by default).
   * 
   * @return a RubyArray
   */
  public RubyArray<String> lines() {
    return eachLine().toA();
  }

  /**
   * Returns a RubyArray of lines in str split using given record separator.
   * 
   * @return a RubyArray
   */
  public RubyArray<String> lines(String separator) {
    return eachLine(separator).toA();
  }

  /**
   * If integer is greater than the length of str, returns a new RubyString of
   * length integer with str left justified and padded with whitesapce;
   * otherwise, returns str.
   * 
   * @param width
   *          of new RubyString
   * @return a new RubyString
   */
  public RubyString ljust(int width) {
    return ljust(width, " ");
  }

  /**
   * If integer is greater than the length of str, returns a new RubyString of
   * length integer with str left justified and padded with padstr; otherwise,
   * returns str.
   * 
   * @param width
   *          of new RubyString
   * @param padstr
   *          used to pad on the right of new RubyString
   * @return a new RubyString
   */
  public RubyString ljust(int width, String padstr) {
    RubyLazyEnumerator<String> padStr = rs(stringify(padstr)).lazy().cycle();

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

  /**
   * Returns a copy of str with leading whitespace removed.
   * 
   * @return a new RubyString
   */
  public RubyString lstrip() {
    return rs(str.replaceFirst("^\\s+", ""));
  }

  /**
   * Removes leading whitespace from str, returning null if no change was made.
   * 
   * @return
   */
  public RubyString lstripǃ() {
    return inPlace(lstrip());
  }

  /**
   * Converts regex to a Pattern, then invokes its match method on str. Returns
   * null if no match found.
   * 
   * @param regex
   *          regular expression
   * @return a Matcher or null
   */
  public Matcher match(String regex) {
    Matcher matcher = qr(stringify(regex)).matcher(str);
    return matcher.find() ? matcher.reset() : null;
  }

  /**
   * Converts regex to a Pattern, then invokes its match method on str. The
   * second parameter specifies the position in the string to begin the search.
   * Returns null if no match found.
   * 
   * @param regex
   *          regular expression
   * @param pos
   *          position begin to search
   * @return a Matcher or null
   */
  public Matcher match(String regex, int pos) {
    if (pos < 0)
      pos = pos + str.length();
    if (pos >= str.length() || pos < 0)
      return null;

    Matcher matcher = qr(stringify(regex)).matcher(str);
    matcher.region(pos, str.length());
    return matcher.find() ? matcher.reset().region(pos, str.length()) : null;
  }

  /**
   * Returns the successor to str.
   * 
   * @return a new RubyString
   */
  public RubyString next() {
    return rs(StringSuccessor.getInstance().succ(str));
  }

  /**
   * Equivalent to RubyString#next, but modifies the receiver in place.
   * 
   * @return a new RubyString
   */
  public RubyString nextǃ() {
    str = StringSuccessor.getInstance().succ(str);
    return this;
  }

  /**
   * Treats leading characters of str as a string of octal digits (with an
   * optional sign) and returns the corresponding number. Returns 0 if the
   * conversion fails.
   * 
   * @return an int
   */
  public int oct() {
    return toI(8);
  }

  /**
   * Return the Integer ordinal of a one-character string.
   * 
   * @return an int
   */
  public int ord() {
    if (str.isEmpty())
      throw new IllegalArgumentException("ArgumentError: empty string");

    return str.codePointAt(0);
  }

  /**
   * Searches sep in the string and returns the part before it, the match, and
   * the part after it. If it is not found, returns two empty strings and str.
   * 
   * @param sep
   * @return a RubyArray
   */
  public RubyArray<String> partition(String sep) {
    if (sep == null)
      throw new TypeConstraintException("TypeError: type mismatch: null given");

    int sepIndex = str.indexOf(sep);
    if (sepIndex == -1)
      return newRubyArray(str, "", "");

    return newRubyArray(str.substring(0, sepIndex), sep,
        str.substring(sepIndex + sep.length()));
  }

  /**
   * Searches pattern in the string and returns the part before it, the match,
   * and the part after it. If it is not found, returns two empty strings and
   * str.
   * 
   * @param pattern
   *          a Pattern
   * @return a RubyArray
   */
  public RubyArray<String> partition(Pattern pattern) {
    if (pattern == null)
      throw new TypeConstraintException("TypeError: type mismatch: null given");

    Matcher matcher = pattern.matcher(str);
    if (matcher.find()) {
      String sep = matcher.group();
      int sepIndex = str.indexOf(sep);
      return newRubyArray(str.substring(0, sepIndex), sep,
          str.substring(sepIndex + sep.length()));
    } else {
      return newRubyArray(str, "", "");
    }
  }

  /**
   * Prepends the given string to str.
   * 
   * @param otherStr
   *          any String
   * @return a new RubyString
   */
  public RubyString prepend(String otherStr) {
    str = stringify(otherStr) + str;
    return this;
  }

  /**
   * Replaces the contents and taintedness of str with the corresponding values
   * in otherStr.
   * 
   * @param otherStr
   * @return
   */
  public RubyString replace(String otherStr) {
    str = stringify(otherStr);
    return this;
  }

  /**
   * Returns a new string with the characters from str in reverse order.
   * 
   * @return a new RubyString
   */
  public RubyString reverse() {
    return rs(new StringBuilder(str).reverse().toString());
  }

  /**
   * Reverses str in place.
   * 
   * @return this RubyString
   */
  public RubyString reverseǃ() {
    str = reverse().toS();
    return this;
  }

  /**
   * Returns the index of the last occurrence of the given substring in str.
   * Returns null if not found.
   * 
   * @param substring
   *          any String
   * @return an Ineger or null
   */
  public Integer rindex(String substring) {
    return rindex(stringify(substring), str.length() - 1);
  }

  /**
   * Returns the index of the last occurrence of the given Pattern in str.
   * Returns null if not found. The second parameter specifies the position in
   * the string to end the search—characters beyond this point will not be
   * considered.
   * 
   * @param substring
   *          any String
   * @param stopAt
   *          position to stop search
   * @return an Ineger or null
   */
  public Integer rindex(String substring, int stopAt) {
    stringify(substring);

    if (stopAt < 0)
      stopAt += str.length();
    if (stopAt < 0)
      return null;

    String revStr = new StringBuilder(str).reverse().toString();
    int index = revStr.indexOf(substring, str.length() - stopAt - 1);
    return index == -1 ? null : str.length() - index - 1;
  }

  /**
   * Returns the index of the last occurrence of the given Pattern in str.
   * Returns null if not found.
   * 
   * @param pattern
   *          a Pattern
   * @return an Integer or null
   */
  public Integer rindex(Pattern pattern) {
    if (pattern == null)
      throw new TypeConstraintException("TypeError: type mismatch: null given");

    return rindex(pattern, str.length() - 1);
  }

  /**
   * Returns the index of the last occurrence of the given Pattern in str.
   * Returns null if not found. The second parameter specifies the position in
   * the string to end the search—characters beyond this point will not be
   * considered.
   * 
   * @param pattern
   *          a Pattern
   * @param stopAt
   *          position to stop search
   * @return an Integer or null
   */
  public Integer rindex(Pattern pattern, int stopAt) {
    if (pattern == null)
      throw new TypeConstraintException("TypeError: type mismatch: null given");

    if (stopAt < 0)
      stopAt += str.length();
    if (stopAt < 0)
      return null;

    Matcher matcher = pattern.matcher(str);
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

  /**
   * If width is greater than the length of str, returns a new String of length
   * integer with str right justified and padded with whitespace; otherwise,
   * returns str.
   * 
   * @param width
   *          of new RubyString
   * @return a new RubyString
   */
  public RubyString rjust(int width) {
    return rjust(width, " ");
  }

  /**
   * If width is greater than the length of str, returns a new String of length
   * integer with str right justified and padded with padstr; otherwise, returns
   * str.
   * 
   * @param width
   *          of new RubyString
   * @param padstr
   *          used to pad on the left of new RubyString
   * @return a new RubyString
   */
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

  /**
   * Searches sep in the string from the end of the string, and returns the part
   * before it, the match, and the part after it. If it is not found, returns
   * two empty strings and str.
   * 
   * @param sep
   *          separator
   * @return a RubyArray
   */
  public RubyArray<String> rpartition(String sep) {
    if (sep == null)
      throw new TypeConstraintException("TypeError: type mismatch: null given");

    int sepIndex = str.lastIndexOf(sep);
    if (sepIndex == -1)
      return newRubyArray("", "", str);

    return newRubyArray(str.substring(0, sepIndex), sep,
        str.substring(sepIndex + sep.length()));
  }

  /**
   * Searches Pattern in the string from the end of the string, and returns the
   * part before it, the match, and the part after it. If it is not found,
   * returns two empty strings and str.
   * 
   * @param pattern
   *          a Pattern
   * @return a RubyArray
   */
  public RubyArray<String> rpartition(Pattern pattern) {
    if (pattern == null)
      throw new TypeConstraintException("TypeError: type mismatch: null given");

    Matcher matcher = pattern.matcher(str);
    if (matcher.find()) {
      String sep = matcher.group();
      matcher.region(matcher.start() + 1, str.length());
      while (matcher.find()) {
        sep = matcher.group();
        matcher.region(matcher.start() + 1, str.length());
      }
      int sepIndex = str.lastIndexOf(sep);
      return newRubyArray(str.substring(0, sepIndex), sep,
          str.substring(sepIndex + sep.length()));
    } else {
      return newRubyArray("", "", str);
    }
  }

  /**
   * Returns a copy of str with trailing whitespace removed. See also
   * RubyString#lstrip and RubyString#strip.
   * 
   * @return a new RubyString
   */
  public RubyString rstrip() {
    return rs(str.replaceFirst("\\s+$", ""));
  }

  /**
   * Removes trailing whitespace from str, returning nil if no change was made.
   * See also RubyString#lstrip! and RubyString#strip!.
   * 
   * @return this RubyString
   */
  public RubyString rstripǃ() {
    return inPlace(rstrip());
  }

  /**
   * Both forms iterate through str, matching the regexp. For each match, a
   * result is generated and added to the result RubyArray.
   * 
   * @param regex
   *          regular expression
   * @return a RubyArray
   */
  public RubyArray<String> scan(String regex) {
    Matcher matcher = qr(stringify(regex)).matcher(str);
    RubyArray<String> matches = newRubyArray();
    while (matcher.find()) {
      matches.add(matcher.group());
    }
    return matches;
  }

  /**
   * Both forms iterate through str, matching the regexp. For each match, a
   * result is generated and passed to the block.
   * 
   * @param regex
   *          regular expression
   * @param block
   *          to do the replacement
   * @return this RubyString
   */
  public RubyString scan(String regex, Block<String> block) {
    scan(regex).each(block);
    return this;
  }

  /**
   * Both forms iterate through str, matching the regexp. For each match, a
   * result is generated and added to the result array. If the pattern contains
   * no groups, each individual result consists of the matched string. If the
   * pattern contains groups, each individual result is itself an array
   * containing one entry per group.
   * 
   * @param regex
   *          regular expression
   * @return a RubyArray
   */
  public RubyArray<RubyArray<String>> scanGroups(String regex) {
    Matcher matcher = qr(stringify(regex)).matcher(str);
    RubyArray<RubyArray<String>> groups = newRubyArray();
    while (matcher.find()) {
      RubyArray<String> group = newRubyArray();
      if (matcher.groupCount() == 0) {
        group.add(matcher.group());
      } else {
        for (int i = 1; i <= matcher.groupCount(); i++) {
          group.add(matcher.group(i));
        }
      }
      groups.add(group);
    }
    return groups;
  }

  /**
   * Both forms iterate through str, matching the regexp. For each match, a
   * result is generated and passed to the block. If the pattern contains no
   * groups, each individual result consists of the matched string. If the
   * pattern contains groups, each individual result is itself an array
   * containing one entry per group.
   * 
   * @param regex
   *          regular expression
   * @param block
   *          to yield matched groups
   * @return this RubyString
   */
  public RubyString scanGroups(String regex, Block<RubyArray<String>> block) {
    scanGroups(stringify(regex)).each(block);
    return this;
  }

  /**
   * If the string is invalid byte sequence then replace invalid bytes with �.
   * 
   * @return a new RubyString
   */
  public RubyString scrub() {
    return rs(str.replaceAll("\\p{C}", "\uFFFD"));
  }

  /**
   * If the string is invalid byte sequence then replace invalid bytes with
   * given replacement character.
   * 
   * @return a new RubyString
   */
  public RubyString scrub(String repl) {
    if (repl == null)
      return scrub();

    return rs(str.replaceAll("\\p{C}", repl));
  }

  /**
   * If the string is invalid byte sequence then replace invalid bytes with
   * returned value of the block.
   * 
   * @return a new RubyString
   */
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

  /**
   * If the string is invalid byte sequence then replace invalid bytes with �.
   * 
   * @return this RubyString
   */
  public RubyString scrubǃ() {
    return inPlace(scrub());
  }

  /**
   * If the string is invalid byte sequence then replace invalid bytes with
   * given replacement character.
   * 
   * @return this RubyString
   */
  public RubyString scrubǃ(String repl) {
    return inPlace(scrub(repl));
  }

  /**
   * If the string is invalid byte sequence then replace invalid bytes with
   * returned value of the block.
   * 
   * @return this RubyString
   */
  public RubyString scrubǃ(final TransformBlock<RubyArray<Byte>, String> block) {
    return inPlace(scrub(block));
  }

  /**
   * Modifies the indexth byte.
   * 
   * @param index
   *          position to modify
   * @param b
   *          a byte
   * @return a byte
   */
  public byte setbyte(int index, byte b) {
    byte[] bytes = str.getBytes();
    if (index < 0)
      index += bytes.length;
    if (index < 0 || index >= bytes.length)
      throw new IndexOutOfBoundsException("IndexError: index " + index
          + " out of string");

    bytes[index] = b;
    str = new String(bytes);
    return b;
  }

  /**
   * Returns the character length of str.
   * 
   * @return an int
   */
  public int size() {
    return str.length();
  }

  /**
   * Returns a substring of one character at that index.
   * 
   * @param index
   *          of the character
   * @return a new RubyString or null
   */
  public RubyString slice(int index) {
    String slicedStr = chars().slice(index);
    return slicedStr == null ? null : rs(slicedStr);
  }

  /**
   * Returns a substring containing length characters starting at the index.
   * 
   * @param index
   *          position to begin slice
   * @param length
   *          of new RubyString
   * @return a new RubyString or null
   */
  public RubyString slice(int index, int length) {
    RubyArray<String> slicedStr = chars().slice(index, length);
    return slicedStr == null ? null : rs(slicedStr.join());
  }

  /**
   * Returns the matching portion of the string.
   * 
   * @param pattern
   *          a Pattern
   * @return a new RubyString or null
   */
  public RubyString slice(Pattern pattern) {
    if (pattern == null)
      throw new TypeConstraintException("TypeError: type mismatch: null given");

    Matcher matcher = pattern.matcher(str);
    if (matcher.find())
      return rs(matcher.group());
    else
      return null;
  }

  /**
   * Returns the target group of matching portion of the string.
   * 
   * @param pattern
   *          a Pattern
   * @param group
   *          number pf matched group
   * @return a new RubyString or null
   */
  public RubyString slice(Pattern pattern, int group) {
    Matcher matcher = pattern.matcher(str);
    if (matcher.find())
      if (group < 1 || group > matcher.groupCount())
        return null;
      else
        return rs(matcher.group(group));
    else
      return null;
  }

  /**
   * Returns if matchStr occurs in the string.
   * 
   * @param matchStr
   *          matched string
   * @return a new RubyString or null
   */
  public RubyString slice(String matchStr) {
    return str.contains(matchStr) ? rs(matchStr) : null;
  }

  /**
   * Deletes the specified portion from str, and returns the portion deleted.
   * 
   * @param index
   *          of the character
   * @return a new RubyString or null
   */
  public RubyString sliceǃ(int index) {
    RubyArray<String> chars = chars();
    String slicedStr = chars.sliceǃ(index);
    if (slicedStr == null)
      return null;

    str = chars.join();
    return rs(slicedStr);
  }

  /**
   * Deletes the specified portion from str, and returns the portion deleted.
   * 
   * @param index
   *          position to begin slice
   * @param length
   *          of new RubyString
   * @return a new RubyString or null
   */
  public RubyString sliceǃ(int index, int length) {
    RubyArray<String> chars = chars();
    RubyArray<String> slicedStr = chars.sliceǃ(index, length);
    if (slicedStr == null)
      return null;

    str = chars.join();
    return rs(slicedStr.join());
  }

  /**
   * Deletes the specified portion from str, and returns the portion deleted.
   * 
   * @param pattern
   *          a Pattern
   * @return a new RubyString or null
   */
  public RubyString sliceǃ(Pattern pattern) {
    RubyString slicedStr = slice(pattern);
    if (slicedStr == null)
      return null;

    str = str.replace(slicedStr, "");
    return slicedStr;
  }

  /**
   * Deletes the specified portion from str, and returns the portion deleted.
   * 
   * @param pattern
   *          a Pattern
   * @param group
   *          number pf matched group
   * @return a new RubyString or null
   */
  public RubyString sliceǃ(Pattern pattern, int group) {
    RubyString slicedStr = slice(pattern, group);
    if (slicedStr == null)
      return null;

    str = str.replace(slicedStr, "");
    return slicedStr;
  }

  /**
   * Deletes the specified portion from str, and returns the portion deleted.
   * 
   * @param matchStr
   *          matched string
   * @return a new RubyString or null
   */
  public RubyString sliceǃ(String matchStr) {
    RubyString slicedStr = slice(matchStr);
    if (slicedStr == null)
      return null;

    str = str.replace(slicedStr, "");
    return slicedStr;
  }

  /**
   * Divides str into substrings based on a whitespaces, returning a RubyArray
   * of these substrings.
   * 
   * @return a RubyArray
   */
  public RubyArray<String> split() {
    return ra(str.trim().split(" +"));
  }

  /**
   * Divides str into substrings based on a delimiter, returning a RubyArray of
   * these substrings.
   * 
   * @param delimiter
   *          used to split str
   * @return a RubyArray
   */
  public RubyArray<String> split(final String delimiter) {
    if (delimiter == null || delimiter.equals(" "))
      return split();

    return newRubyArray(str.split(Pattern.quote(delimiter)));
  }

  /**
   * Divides str into substrings based on a delimiter, returning a RubyArray of
   * these substrings, at most that limit number of fields will be returned.
   * 
   * @param delimiter
   *          used to split str
   * @param limit
   *          max number of fields
   * @return a RubyArray
   */
  public RubyArray<String> split(String delimiter, int limit) {
    if (delimiter == null || delimiter.equals(" ")) {
      if (limit <= 0)
        return newRubyArray(str.trim().split(" +"));
      else if (limit == 1)
        return newRubyArray(str);
      else
        return newRubyArray(rs(str).lstrip().toS().split(" +", limit));
    }

    if (limit <= 0)
      return newRubyArray(str.split(Pattern.quote(delimiter)));
    else
      return newRubyArray(str.split(Pattern.quote(delimiter), limit));
  }

  /**
   * Divides str into substrings based on a Pattern, returning a RubyArray of
   * these substrings.
   * 
   * @param pattern
   *          a Pattern
   * @return a RubyArray
   */
  public RubyArray<String> split(Pattern pattern) {
    if (pattern == null)
      return split();

    return newRubyArray(str.split(pattern.pattern()));
  }

  /**
   * Divides str into substrings based on a Pattern, returning a RubyArray of
   * these substrings, at most that limit number of fields will be returned.
   * 
   * @param pattern
   *          a Pattern
   * @param limit
   *          max number of fields
   * @return a RubyArray
   */
  public RubyArray<String> split(Pattern pattern, int limit) {
    if (pattern == null)
      return split((String) null, limit);

    if (limit <= 0)
      return newRubyArray(str.split(pattern.pattern()));
    else
      return newRubyArray(str.split(pattern.pattern(), limit));
  }

  /**
   * Builds a set of characters using the procedure described for
   * RubyString#count.
   * 
   * @return a new RubyString
   */
  public RubyString squeeze() {
    return rs(str.replaceAll("(.)\\1+", "$1"));
  }

  /**
   * Builds a set of characters from the charSet using the procedure described
   * for RubyString#count.
   * 
   * @param charSet
   *          a set of characters
   * @return a new RubyString
   */
  public RubyString squeeze(String charSet) {
    stringify(charSet);

    return rs(str.replaceAll("([" + charSet2Str(charSet) + "])\\1+", "$1"));
  }

  /**
   * Squeezes str in place, returning either str, or nil if no changes were
   * made.
   * 
   * @return this RubyString or null
   */
  public RubyString squeezeǃ() {
    return inPlace(squeeze());
  }

  /**
   * Squeezes str in place, returning either str, or nil if no changes were
   * made.
   * 
   * @param charSet
   *          a set of characters
   * @return this RubyString or null
   */
  public RubyString squeezeǃ(String charSet) {
    return inPlace(squeeze(charSet));
  }

  /**
   * Returns true if str starts with one of the prefixes given.
   * 
   * @param prefix
   *          first prefix
   * @param otherPrefix
   *          otner prefixes
   * @return true if str starts with one of the prefixes given, flase otherwise
   */
  public boolean startWithʔ(String prefix, String... otherPrefix) {
    if (str.startsWith(stringify(prefix)))
      return true;

    if (otherPrefix != null) {
      for (String p : otherPrefix) {
        if (str.startsWith(p))
          return true;
      }
    }

    return false;
  }

  /**
   * Returns a copy of str with leading and trailing whitespace removed.
   * 
   * @return a new RubyString
   */
  public RubyString strip() {
    return rs(str.trim());
  }

  /**
   * Removes leading and trailing whitespace from str. Returns nil if str was
   * not altered.
   * 
   * @return this RubyString
   */
  public RubyString stripǃ() {
    return inPlace(strip());
  }

  /**
   * Returns a copy of str with the first occurrence of pattern replaced by the
   * second argument.
   * 
   * @param regex
   *          regular expression
   * @param replacement
   *          used to replace matched string
   * @return a new RubyString
   */
  public RubyString sub(String regex, String replacement) {
    return rs(str.replaceFirst(stringify(regex), stringify(replacement)));
  }

  /**
   * Returns a copy of str with the first occurrence of pattern replaced by the
   * second argument. The second argument is a Map, and the matched text is one
   * of its keys, the corresponding value is the replacement string.
   * 
   * @param regex
   *          regular expression
   * @param map
   *          any Map
   * @return a new RubyString
   */
  public RubyString sub(String regex, Map<String, ?> map) {
    String result = str;
    Matcher matcher = qr(stringify(regex)).matcher(str);
    if (matcher.find()) {
      String target = matcher.group();
      if (isNotBlank(map) && map.containsKey(target))
        result = result.replace(target, map.get(target).toString());
    }
    return rs(result);
  }

  /**
   * Returns a copy of str with the first occurrence of pattern replaced by the
   * value returned by the block will be substituted for the match on each call.
   * 
   * @param regex
   *          regular expression
   * @param block
   *          to do the replacement
   * @return a new RubyString
   */
  public RubyString sub(String regex, TransformBlock<String, String> block) {
    Matcher matcher = qr(stringify(regex)).matcher(str);
    if (matcher.find()) {
      String match = matcher.group();
      return rs(str.replace(match, block.yield(match)));
    }
    return this;
  }

  /**
   * Performs the same substitution as #sub in-place. Returns str if a
   * substitution was performed or nil if no substitution was performed.
   * 
   * @param regex
   *          regular expression
   * @param replacement
   *          used to replace matched string
   * @return this RubyString or null
   */
  public RubyString subǃ(String regex, String replacement) {
    return inPlace(sub(regex, replacement));
  }

  /**
   * Performs the same substitution as #sub in-place. Returns str if a
   * substitution was performed or nil if no substitution was performed.
   * 
   * @param regex
   *          regular expression
   * @param block
   *          to do the replacement
   * @return this RubyString or null
   */
  public RubyString subǃ(String regex, TransformBlock<String, String> block) {
    return inPlace(sub(regex, block));
  }

  /**
   * Returns the successor to str. The successor is calculated by incrementing
   * characters starting from the rightmost alphanumeric (or the rightmost
   * character if there are no alphanumerics) in the string. Incrementing a
   * digit always results in another digit, and incrementing a letter results in
   * another letter of the same case. Incrementing nonalphanumerics uses the
   * underlying character set’s collating sequence.
   * 
   * @return a new RubyString
   */
  public RubyString succ() {
    return rs(StringSuccessor.getInstance().succ(str));
  }

  /**
   * Equivalent to RubyString#succ, but modifies the receiver in place.
   * 
   * @return this RubyString
   */
  public RubyString succǃ() {
    str = succ().toS();
    return this;
  }

  /**
   * Returns a basic n-bit checksum of the characters in str, where n is 16. The
   * result is simply the sum of the binary value of each character in str
   * modulo 2**n - 1. This is not a particularly good checksum.
   * 
   * @return an int
   */
  public int sum() {
    return sum(16);
  }

  /**
   * Returns a basic n-bit checksum of the characters in str, where n is the
   * optional Fixnum parameter. The result is simply the sum of the binary value
   * of each character in str modulo 2**n - 1. This is not a particularly good
   * checksum.
   * 
   * @param n
   *          any int
   * @return an int
   */
  public int sum(int n) {
    int sum = 0;
    for (byte b : str.getBytes()) {
      sum += (int) b & 0xFF;
    }
    return sum % (int) Math.pow(2, n - 1);
  }

  /**
   * Returns a copy of str with uppercase alphabetic characters converted to
   * lowercase and lowercase characters converted to uppercase. Note: case
   * conversion is effective only in ASCII region.
   * 
   * @return a new RubyString
   */
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

  /**
   * Equivalent to String#swapcase, but modifies the receiver in place,
   * returning str, or null if no changes were made. Note: case conversion is
   * effective only in ASCII region.
   * 
   * @return
   */
  public RubyString swapcaseǃ() {
    return inPlace(swapcase());
  }

  /**
   * Returns the result of interpreting leading characters in str as a floating
   * point number. Extraneous characters past the end of a valid number are
   * ignored. If there is not a valid number at the start of str, 0.0 is
   * returned. This method never raises an exception.
   * 
   * @return a double
   */
  public double toF() {
    Matcher intMatcher =
        qr("^\\s*[-+]?\\s*[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?").matcher(str);
    if (intMatcher.find())
      return Double.valueOf(intMatcher.group().trim().replaceAll("\\s+", ""));
    else
      return 0.0;
  }

  public int toI() {
    Matcher intMatcher = qr("^\\s*[-+]?\\s*\\d+").matcher(str);
    if (intMatcher.find())
      return Integer.valueOf(intMatcher.group().trim().replaceAll("\\s+", ""));
    else
      return 0;
  }

  public int toI(int radix) {
    if (radix < MIN_RADIX || radix > MAX_RADIX)
      throw new IllegalArgumentException("ArgumentError: invalid radix "
          + radix);

    String digits = "0123456789abcdefghijklmnopqrstuvwxyz";
    Matcher intMatcher =
        qr("(?i)^\\s*[-+]?\\s*[" + digits.substring(0, radix) + "]+").matcher(
            str);
    if (intMatcher.find()) {
      try {
        return Integer.parseInt(intMatcher.group().trim(), radix);
      } catch (NumberFormatException e) {
        return 0;
      }
    }
    return 0;
  }

  /**
   * Returns the receiver.
   * 
   * @return a String
   */
  public String toS() {
    return str;
  }

  /**
   * Returns the receiver.
   * 
   * @return a String
   */
  public String toStr() {
    return str;
  }

  /**
   * Returns a copy of str with the characters in from_str replaced by the
   * corresponding characters in toStr. If to_str is shorter than fromStr, it is
   * padded with its last character in order to maintain the correspondence.
   * 
   * @param fromStr
   *          a list of characters
   * @param toStr
   *          a list of characters
   * @return a new RubyString
   */
  public RubyString tr(String fromStr, String toStr) {
    fromStr = charSet2Str(stringify(fromStr));
    toStr = charSet2Str(stringify(toStr));
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
      if (fromStr.contains("\\^")) {
        fromStr = fromStr.replaceAll("\\\\^", "");
        fromStr += "^";
      }
      if (fromStr.contains("\\-")) {
        fromStr = fromStr.replaceAll("\\\\-", "");
        fromStr += "-";
      }
      return gsub(
          "["
              + fromStr.replace("\\", "\\\\").replace("[", "\\[")
                  .replace("]", "\\]") + "]", rh);
    }
  }

  /**
   * Translates str in place, using the same rules as String#tr. Returns str, or
   * nil if no changes were made.
   * 
   * @param fromStr
   *          a list of characters
   * @param toStr
   *          a list of characters
   * @return this RubyString or null
   */
  public RubyString trǃ(String fromStr, String toStr) {
    return inPlace(tr(fromStr, toStr));
  }

  /**
   * Processes a copy of str as described under RubyString#tr, then removes
   * duplicate characters in regions that were affected by the translation.
   * 
   * @param fromStr
   *          a list of characters
   * @param toStr
   *          a list of characters
   * @return a new RubyString
   */
  public RubyString trS(String fromStr, String toStr) {
    final String fromString = charSet2Str(stringify(fromStr));
    toStr = charSet2Str(stringify(toStr));
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
      if (fromStr.contains("\\^")) {
        fromStr = fromStr.replaceAll("\\\\^", "");
        fromStr += "^";
      }
      if (fromStr.contains("\\-")) {
        fromStr = fromStr.replaceAll("\\\\-", "");
        fromStr += "-";
      }
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

  /**
   * Performs RubyString#trS processing on str in place, returning str, or null
   * if no changes were made.
   * 
   * @param fromStr
   *          a list of characters
   * @param toStr
   *          a list of characters
   * @return this RubyString or null
   */
  public RubyString trSǃ(String fromStr, String toStr) {
    return inPlace(trS(fromStr, toStr));
  }

  /**
   * Decodes str (which may contain binary data) according to the format string,
   * returning an array of each value extracted. The format string consists of a
   * sequence of single-character directives, summarized in the table at the end
   * of this entry. Each directive may be followed by a number, indicating the
   * number of times to repeat with this directive. An asterisk (“*”) will use
   * up all remaining elements. See also RubyArray#pack.
   * 
   * @param format
   *          format string
   * @return a RubyArray
   */
  public RubyArray<Object> unpack(String format) {
    return Unpacker.unpack(stringify(format), str);
  }

  /**
   * Returns a copy of str with all lowercase letters replaced with their
   * uppercase counterparts. The operation is locale insensitive—only characters
   * “a” to “z” are affected.
   * 
   * @return a new RubyString
   */
  public RubyString upcase() {
    return rs(str.toUpperCase());
  }

  /**
   * Upcases the contents of str, returning nil if no changes were made.
   * 
   * @return a new RubyString
   */
  public RubyString upcaseǃ() {
    return inPlace(upcase());
  }

  /**
   * Iterates through successive values, starting at str and ending at otherStr
   * inclusive, passing each value in turn to the block. The RubyString#succ
   * method is used to generate each value.
   * 
   * @param otherStr
   *          any String
   * @return a RubyEnumerator
   */
  public RubyEnumerator<String> upto(String otherStr) {
    return upto(stringify(otherStr), false);
  }

  /**
   * Iterates through successive values, starting at str and ending at otherStr
   * inclusive, passing each value in turn to the block. The RubyString#succ
   * method is used to generate each value. The second argument exclusive is
   * omitted or is false, the last value will be included; otherwise it will be
   * excluded.
   * 
   * @param otherStr
   *          any String
   * @param exclusive
   *          true if the last value is ommitted, false otherwise
   * @return a RubyEnumerator
   */
  public RubyEnumerator<String> upto(final String otherStr, boolean exclusive) {
    stringify(otherStr);

    if (exclusive) {
      return newRubyEnumerator((Iterable<String>) range(str, otherStr).lazy()
          .takeWhile(new BooleanBlock<String>() {

            @Override
            public boolean yield(String item) {
              return !item.equals(otherStr);
            }

          }));
    } else {
      return range(str, otherStr).each();
    }
  }

  /**
   * Iterates through successive values, starting at str and ending at otherStr
   * inclusive, passing each value in turn to the block.
   * 
   * @param otherStr
   *          any String
   * @param block
   *          to yield successive value
   * @return this RubyString
   */
  public RubyString upto(String otherStr, Block<String> block) {
    upto(stringify(otherStr), false).each(block);
    return this;
  }

  /**
   * Iterates through successive values, starting at str and ending at otherStr,
   * passing each value in turn to the block. The second argument exclusive is
   * omitted or is false, the last value will be included.
   * 
   * @param otherStr
   *          any String
   * @param exclusive
   *          true if the last value is ommitted, false otherwise
   * @param block
   *          to yield successive value
   * @return this RubyString
   */
  public RubyString
      upto(String otherStr, boolean exclusive, Block<String> block) {
    upto(stringify(otherStr), exclusive).each(block);
    return this;
  }

  /**
   * Returns true for a string which encoded correctly.
   * 
   * @param encoding
   *          name of encoding
   * @return true if a string encoded correctly, flase otherwise
   */
  public boolean validEncodingʔ(String encoding) {
    CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
    CharsetEncoder encoder = Charset.forName(stringify(encoding)).newEncoder();
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
