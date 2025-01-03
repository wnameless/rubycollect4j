/*
 *
 * Copyright 2013 Wei-Ming Wu
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
package net.sf.rubycollect4j;

import static java.lang.Character.*;
import static net.sf.rubycollect4j.RubyObject.*;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.rubycollect4j.packer.Unpacker;
import net.sf.rubycollect4j.succ.StringSuccessor;
import net.sf.rubycollect4j.util.ByteUtils;

/**
 * 
 * {@link RubyString} implements all methods refer to the String class of Ruby language.
 * <p>
 * {@link RubyString} is also a Java CharSequence.
 * <P>
 * To avoid the conflict of Java 8 CharSequence#chars(), {@link RubyString} doesn't implement the
 * chars() method.
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class RubyString implements CharSequence, Comparable<CharSequence>, Serializable {

  private static final long serialVersionUID = 1L;

  private String str;

  /**
   * Returns a {@link RubyString} with an empty String as default value.
   */
  public RubyString() {
    str = "";
  }

  /**
   * Returns a {@link RubyString} with calling {@link Object#toString()} on given Object as default
   * value.
   * 
   * @param o any Object
   * @throws TypeConstraintException if o is null
   */
  public RubyString(Object o) {
    str = stringify(o);
  }

  private String stringify(Object o) {
    if (o == null)
      throw new ClassCastException("TypeError: no implicit conversion of null into String");

    if (o instanceof String) return (String) o;

    if (o instanceof CharSequence) {
      CharSequence cs = (CharSequence) o;
      return new StringBuilder(cs.length()).append(cs).toString();
    }

    return o.toString();
  }

  private RubyString inPlace(RubyString rs) {
    if (this.equals(rs)) return null;

    str = rs.toS();
    return this;
  }

  /**
   * Checks if {@link RubyString} only contains ascii.
   * 
   * @return true if this {@link RubyString} only contains ascii, false otherwise
   */
  public boolean asciiOnlyʔ() {
    return str.matches("^[\\u0000-\\u007F]*$");
  }

  /**
   * Returns a copied string whose encoding is ASCII-8BIT.
   * 
   * @return new {@link RubyString}
   */
  public RubyString b() {
    return Ruby.String.of(new String(str.getBytes(), Charset.forName("ISO-8859-1")));
  }

  /**
   * Returns an {@link RubyArray} of Byte in {@link RubyString}.
   * 
   * @return {@link RubyArray} of Byte
   */
  public RubyArray<Byte> bytes() {
    return ByteUtils.toList(str.getBytes());
  }

  /**
   * Returns the length of {@link RubyString} in bytes.
   * 
   * @return the length of bytes
   */
  public int bytesize() {
    return str.getBytes().length;
  }

  /**
   * Returns a substring of one byte at given index.
   * 
   * @param index of target byte
   * @return s {@link RubyString} of sliced byte
   */
  public RubyString byteslice(int index) {
    Byte ch = bytes().at(index);
    if (ch == null) return null;

    return Ruby.String.of(new String(new byte[] {ch}, Charset.forName("ISO-8859-1")));
  }

  /**
   * Returns a substring starting at the offset given by the first, and a length given by the
   * second.
   * 
   * @param offset begin index
   * @param length of the bytes
   * @return new {@link RubyString}
   */
  public RubyString byteslice(int offset, int length) {
    RubyArray<Byte> bytes = bytes().slice(offset, length);
    if (bytes == null) return null;

    byte[] byteAry = new byte[bytes.size()];
    for (int i = 0; i < byteAry.length; i++) {
      byteAry[i] = bytes.get(i);
    }
    return Ruby.String.of(new String(byteAry));
  }

  /**
   * Returns a copy of {@link RubyString} with the first character converted to uppercase and the
   * remainder to lowercase.
   * 
   * @return new {@link RubyString}
   */
  public RubyString capitalize() {
    if (str.isEmpty()) return this;

    return Ruby.String.of(str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase());
  }

  /**
   * Modifies {@link RubyString} by converting the first character to uppercase and the remainder to
   * lowercase.
   * 
   * @return this {@link RubyString} if capitalized, null otherwise
   */
  public RubyString capitalizeǃ() {
    return inPlace(capitalize());
  }

  /**
   * Case-insensitive version of {@link #compareTo(CharSequence)}.
   * 
   * @param charSeq any CharSequence
   * @return negative integer, zero, or a positive integer as the specified CharSequence is greater
   *         than, equal to, or less than this String, ignoring case considerations.
   */
  public int casecmp(CharSequence charSeq) {
    return str.compareToIgnoreCase(charSeq.toString());
  }

  /**
   * Returns a new {@link RubyString} of length width with str centered and padded with spaces.
   * 
   * @param width of result
   * @return new {@link RubyString}
   */
  public RubyString center(int width) {
    return center(width, " ");
  }

  /**
   * Returns a new {@link RubyString} of length width with str centered and padded with padstr.
   * 
   * @param width of result
   * @param padstr used to pad in front and after the str
   * @return new {@link RubyString}
   */
  public RubyString center(int width, String padstr) {
    if (stringify(padstr).isEmpty())
      throw new IllegalArgumentException("ArgumentError: zero width padding");

    if (width <= str.length()) return Ruby.String.of(str);

    RubyArray<String> centeredStr = Ruby.Array.create();
    int start = (width - str.length()) / 2;
    RubyLazyEnumerator<String> padStr = Ruby.String.of(padstr).eachChar().lazy().cycle();
    for (int i = 0; i < width; i++) {
      if (i < start) {
        centeredStr.add(padStr.next());
      } else if (i == start) {
        padStr.rewind();
        if (str.isEmpty()) {
          centeredStr.add(padStr.next());
          continue;
        }
        centeredStr = centeredStr.plus(eachChar().toA());
        i += str.length() - 1;
      } else {
        centeredStr.add(padStr.next());
      }
    }
    return Ruby.String.of(centeredStr.join());
  }

  /**
   * Returns a new {@link RubyString} with the line separator removed from the end of str.
   * 
   * @return new {@link RubyString}
   */
  public RubyString chomp() {
    String result = str.replaceFirst("\r\n$", "");
    if (result.length() < str.length()) return Ruby.String.of(result);

    result = result.replaceFirst("\r$", "");
    if (result.length() < str.length()) return Ruby.String.of(result);

    return Ruby.String.of(result.replaceFirst("\n$", ""));
  }

  /**
   * Modifies str in place as described for {@link #chomp()}, returning str, or null if no
   * modifications were made.
   * 
   * @return this {@link RubyString} or null
   */
  public RubyString chompǃ() {
    return inPlace(chomp());
  }

  /**
   * Returns a new {@link RubyString} with the given line separator removed from the end of str.
   * 
   * @param separator a line separator
   * @return new {@link RubyString}
   */
  public RubyString chomp(String separator) {
    separator = separator == null ? "" : separator;
    if (str.endsWith(separator))
      return Ruby.String.of(str.substring(0, str.lastIndexOf(separator)));

    return Ruby.String.of(str);
  }

  /**
   * Modifies str in place as described for {@link #chomp()}, returning str, or null if no
   * modifications were made.
   * 
   * @return this {@link RubyString} or null
   */
  public RubyString chompǃ(String separator) {
    return inPlace(chomp(separator));
  }

  /**
   * Returns a new {@link RubyString} with the last character removed. If the string ends with \r\n,
   * both characters are removed.
   * 
   * @return new {@link RubyString}
   */
  public RubyString chop() {
    if (str.isEmpty()) return Ruby.String.of(str);

    if (str.endsWith("\r\n"))
      return Ruby.String.of(str.substring(0, str.length() - 2));
    else
      return Ruby.String.of(str.substring(0, str.length() - 1));
  }

  /**
   * Processes str as for {@link #chop}, returning str, or null if str is the empty string.
   * 
   * @return new {@link RubyString} or null
   */
  public RubyString chopǃ() {
    return inPlace(chop());
  }

  /**
   * Returns a one-character string at the beginning of the string.
   * 
   * @return new {@link RubyString}
   */
  public RubyString chr() {
    if (str.isEmpty()) return this;

    return Ruby.String.of(str.substring(0, 1));
  }

  /**
   * Makes string empty.
   * 
   * @return this {@link RubyString}
   */
  public RubyString clear() {
    str = "";
    return this;
  }

  /**
   * Returns a {@link RubyArray} of the Integer ordinals of the characters in str.
   * 
   * @return {@link RubyArray}
   */
  public RubyArray<Integer> codepoints() {
    return Ruby.Range.of(0, str.length()).closedOpen().map(i -> str.codePointAt(i));
  }

  /**
   * Appends the given codepoint as a character to str.
   * 
   * @param codepoint of a character
   * @return this {@link RubyString}
   */
  public RubyString concat(int codepoint) {
    str += (char) codepoint;
    return this;
  }

  /**
   * Appends the given object to str.
   * 
   * @param o any Object
   * @return this {@link RubyString}
   */
  public RubyString concat(Object o) {
    str += o;
    return this;
  }

  /**
   * Each charSet parameter defines a set of characters to count. The intersection of these sets
   * defines the characters to count in str. Any charSet that starts with a caret ^ is negated. The
   * sequence c1-c2 means all characters between c1 and c2. The backslash character can be used to
   * escape ^ or -.
   * 
   * @param charSet a set of characters
   * @param charSets sets of characters
   * @return the total count
   */
  public int count(final String charSet, final String... charSets) {
    return eachChar().count(item -> {
      if (!isCharSetMatched(item, stringify(charSet))) return false;

      if (charSets != null) {
        for (String cs : charSets) {
          if (!isCharSetMatched(item, stringify(cs))) return false;
        }
      }

      return true;
    });
  }

  private boolean isCharSetMatched(String ch, String charSet) {
    if (charSet.startsWith("^") && !ch.matches("[" + charSet + "]")) return false;

    if (!charSet.startsWith("^") && !Ruby.String.of(charSet2Str(charSet)).includeʔ(ch))
      return false;

    return true;
  }

  private String charSet2Str(String charSet) {
    charSet = charSet.replaceAll("^\\\\([^-^])", "$1");
    Matcher matcher = Pattern.compile("[^\\\\]-.").matcher(charSet);
    while (matcher.find()) {
      String charRg = matcher.group();
      RubyRange<Character> rr = Ruby.Range.of(charRg.charAt(0), charRg.charAt(2));
      if (rr.noneʔ()) throw new IllegalArgumentException(
          "ArgumentError: invalid range \"" + charSet + "\" in string transliteration");
      charSet = charSet.replace(charRg, rr.toA().join());
    }
    return charSet;
  }

  /**
   * Applies a one-way cryptographic hash to str by invoking the MessageDigest(SHA-256) with the
   * given salt string.
   * 
   * @param salt a secret string @return new {@link RubyString}
   */
  public RubyString crypt(String salt) {
    String encrypt = str + stringify(salt);
    String sha = null;
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      digest.update(encrypt.getBytes("UTF-8"), 0, encrypt.length());
      sha = new BigInteger(1, digest.digest()).toString(16);
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {}
    return Ruby.String.of(sha);
  }

  /**
   * Returns a copy of str with all characters in the intersection of its arguments deleted. Uses
   * the same rules for building the set of characters as {@link #count()}.
   * 
   * @param charSet a set of characters
   * @return new {@link RubyString}
   */
  public RubyString delete(final String charSet) {
    stringify(charSet);
    return Ruby.String.of(eachChar().toA().deleteIf((item -> {
      if (!isCharSetMatched(item, charSet)) return false;

      return true;
    })).join());
  }

  /**
   * Performs a delete operation in place, returning str, or null if str was not modified.
   * 
   * @param charSet a set of characters
   * @return new {@link RubyString} or null
   */
  public RubyString deleteǃ(String charSet) {
    return inPlace(delete(charSet));
  }

  /**
   * Returns a copy of str with all uppercase letters replaced with their lowercase.
   * 
   * @return new {@link RubyString}
   */
  public RubyString downcase() {
    return Ruby.String.of(str.toLowerCase());
  }

  /**
   * Downcases the contents of str, returning null if no changes were made.
   * 
   * @return this {@link RubyString} or null
   */
  public RubyString downcaseǃ() {
    return inPlace(downcase());
  }

  /**
   * Produces a version of str with all non-printing or non-ASCII 8bit characters replaced by \\nnn
   * or \\unnnn notation and all special characters escaped.
   * 
   * @return new {@link RubyString}
   */
  public RubyString dump() {
    String printable = eachChar().map(item -> {
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
      else if (codepoint < 256 && !item.matches("\\p{C}"))
        return item;
      else if (codepoint < 256)
        return "\\" + Integer.toOctalString(codepoint);
      else
        return "\\u" + Integer.toHexString(codepoint);
    }).join();
    return Ruby.String.of("\"" + printable + "\"");
  }

  /**
   * Returns a {@link RubyEnumerator} of each byte in str.
   * 
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<Byte> eachByte() {
    return bytes().each();
  }

  /**
   * Passes each byte in str to the given block.
   * 
   * @param block to yield byte
   * @return this {@link RubyString}
   */
  public RubyString eachByte(Consumer<? super Byte> block) {
    bytes().each(block);
    return this;
  }

  /**
   * Returns a {@link RubyEnumerator} of each character in str.
   * 
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<String> eachChar() {
    return Ruby.Enumerator.of(Ruby.Range.of(0, str.length()).closedOpen().lazy()
        .map(i -> Character.toString(str.charAt(i))));
  }

  /**
   * Passes each character in str to the given block.
   * 
   * @param block to yield character
   * @return this {@link RubyString}
   */
  public RubyString eachChar(Consumer<? super String> block) {
    eachChar().each(block);
    return this;
  }

  /**
   * Returns a {@link RubyEnumerator} of each codepoint in str.
   * 
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<Integer> eachCodepoint() {
    return Ruby.Enumerator.of(eachChar().lazy().map(item -> item.codePointAt(0)));
  }

  /**
   * Passes each codepoint in str to the given block.
   * 
   * @param block to yield character
   * @return this {@link RubyString}
   */
  public RubyString eachCodepoint(Consumer<? super Integer> block) {
    eachCodepoint().each(block);
    return this;
  }

  /**
   * Returns a {@link RubyEnumerator} of each line in str.
   * 
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<String> eachLine() {
    return Ruby.Array.copyOf(str.split(System.getProperty("line.separator"))).each();
  }

  /**
   * Passes each line in str to the given block.
   * 
   * @param block to yield line
   * @return this {@link RubyString}
   */
  public RubyString eachLine(Consumer<? super String> block) {
    eachLine().each(block);
    return this;
  }

  /**
   * Returns a {@link RubyEnumerator} of each line by given separator in str.
   * 
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<String> eachLine(String separator) {
    return Ruby.Array.copyOf(str.split(stringify(separator))).each();
  }

  /**
   * Passes each line by given separator in str to the given block.
   * 
   * @param block to yield line
   * @return this {@link RubyString}
   */
  public RubyString eachLine(String separator, Consumer<? super String> block) {
    Ruby.Array.copyOf(str.split(stringify(separator))).each(block);
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
   * @param encoding name of encoding
   * @return new {@link RubyString}
   */
  public RubyString encode(String encoding) {
    stringify(encoding);
    return Ruby.String.of(new String(str.getBytes(), Charset.forName(encoding)));
  }

  /**
   * Returns a copy of str transcoded from srcEncoding to dstEncoding.
   * 
   * @param dstEncoding name of destination encoding
   * @param srcEncoding name of source encoding
   * @return new {@link RubyString}
   */
  public RubyString encode(String dstEncoding, String srcEncoding) {
    stringify(srcEncoding);
    stringify(dstEncoding);
    return Ruby.String
        .of(new String(str.getBytes(Charset.forName(srcEncoding)), Charset.forName(dstEncoding)));
  }

  /**
   * Returns the str transcoded to encoding encoding.
   * 
   * @param encoding name of encoding
   * @return this {@link RubyString}
   */
  public RubyString encodeǃ(String encoding) {
    str = encode(encoding).toS();
    return this;
  }

  /**
   * Returns the str transcoded from srcEncoding to dstEncoding.
   * 
   * @param dstEncoding name of destination encoding
   * @param srcEncoding name of source encoding
   * @return this {@link RubyString}
   */
  public RubyString encodeǃ(String dstEncoding, String srcEncoding) {
    str = encode(dstEncoding, srcEncoding).toS();
    return this;
  }

  /**
   * Returns UTF-8 Charset.
   * 
   * @return Charset
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
    if (str.endsWith(stringify(suffix))) return true;

    if (otherSuffix != null) {
      for (String s : otherSuffix) {
        if (str.endsWith(stringify(s))) return true;
      }
    }

    return false;
  }

  /**
   * Two RubyString are equal if they have the same length and content.
   * 
   * @param o any Object
   * @return true if two objects are equal, false otherwise
   */
  public boolean eqlʔ(Object o) {
    return equals(o);
  }

  /**
   * Changes the encoding to encoding and returns self.
   * 
   * @param encoding name of encoding
   * @return this {@link RubyString}
   */
  public RubyString forceEncoding(String encoding) {
    str = encode(encoding).toS();
    return this;
  }

  /**
   * Returns the indexth byte, or null if index was out of range.
   * 
   * @param index of a byte
   * @return Byte or null
   */
  public Byte getbyte(int index) {
    return bytes().at(index);
  }

  /**
   * Returns a copy of str with the all occurrences of pattern substituted for the second argument.
   * 
   * @param regex regular expression
   * @param replacement any String
   * @return new {@link RubyString}
   */
  public RubyString gsub(String regex, String replacement) {
    return Ruby.String.of(str.replaceAll(stringify(regex), stringify(replacement)));
  }

  /**
   * Returns a copy of str with the all occurrences of pattern substituted for the second argument.
   * The second argument is a Map, and the matched text is one of its keys, the corresponding value
   * is the replacement string.
   * 
   * @param regex regular expression
   * @param map any Map
   * @return new {@link RubyString}
   */
  public RubyString gsub(String regex, Map<String, ?> map) {
    if (isBlank(map)) return Ruby.String.of(str);

    String result = str;
    Matcher matcher = Pattern.compile(stringify(regex)).matcher(str);
    while (matcher.find()) {
      String target = matcher.group();
      if (map.containsKey(target)) result = result.replace(target, map.get(target).toString());
    }
    return Ruby.String.of(result);
  }

  /**
   * Returns a copy of str with the all occurrences of pattern substituted for the second argument.
   * The second argument is a TransformBlock, and the value returned by the block will be
   * substituted for the match on each call.
   * 
   * @param regex regular expression
   * @param block to do the replacement
   * @return new {@link RubyString}
   */
  public RubyString gsub(String regex, Function<? super String, ? extends CharSequence> block) {
    String result = str;
    Matcher matcher = Pattern.compile(stringify(regex)).matcher(str);
    while (matcher.find()) {
      String target = matcher.group();
      result = result.replace(target, block.apply(target));
    }
    return Ruby.String.of(result);
  }

  /**
   * Returns a {@link RubyEnumerator} of all matched str.
   * 
   * @param regex regular expression
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<String> gsub(String regex) {
    RubyArray<String> matches = Ruby.Array.create();
    Matcher matcher = Pattern.compile(stringify(regex)).matcher(str);
    while (matcher.find()) {
      matches.add(matcher.group());
    }
    return matches.each();
  }

  /**
   * Performs the substitutions of {@link #gsub(String, String)} in place, returning this
   * {@link RubyString}, or null if no substitutions were performed.
   * 
   * @param regex regular expression
   * @param replacement any String
   * @return this {@link RubyString} or null
   */
  public RubyString gsubǃ(String regex, String replacement) {
    return inPlace(gsub(regex, replacement));
  }

  /**
   * Performs the substitutions of {@link #gsub(String, Function)} in place, returning this
   * {@link RubyString}, or null if no substitutions were performed.
   * 
   * @param regex regular expression
   * @param block to do the replacement
   * @return this {@link RubyString} or null
   */
  public RubyString gsubǃ(String regex, Function<? super String, ? extends CharSequence> block) {
    return inPlace(gsub(regex, block));
  }

  /**
   * Returns a {@link RubyEnumerator} of all matched str.
   * 
   * @param regex regular expression
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<String> gsubǃ(String regex) {
    return gsub(regex);
  }

  /**
   * Return a hash based on the string’s length and content.
   * 
   * @return hashcode
   */
  public int hash() {
    return hashCode();
  }

  /**
   * Treats leading characters from str as a string of hexadecimal digits (with an optional sign and
   * an optional 0x) and returns the corresponding number. Zero is returned on error.
   * 
   * @return long
   */
  public long hex() {
    RubyString hex = slice(Pattern.compile("^\\s*[+|-]?\\s*(0x)?[0-9a-f]+"));
    if (hex == null)
      return 0;
    else
      return Long.parseLong(hex.toS().replaceAll("\\s+", "").replace("0x", ""), 16);
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
   * Returns the index of the first occurrence of the given substring in str. Returns null if not
   * found
   * 
   * @param substring any String
   * @return Integer or null
   */
  public Integer index(String substring) {
    return index(stringify(substring), 0);
  }

  /**
   * Returns the index of the first occurrence of the given substring in str. Returns null if not
   * found. The second parameter specifies the position in the string to begin the search.
   * 
   * @param substring any String
   * @param offset position to begin the search
   * @return Integer or null
   */
  public Integer index(String substring, int offset) {
    int index = str.indexOf(stringify(substring), offset);
    return index == -1 ? null : index;
  }

  /**
   * Returns the index of the first occurrence of the given Pattern in str. Returns null if not
   * found.
   * 
   * @param regex a Pattern
   * @return Integer or null
   */
  public Integer index(Pattern regex) {
    if (regex == null) throw new ClassCastException("TypeError: type mismatch: null given");

    return index(regex, 0);
  }

  /**
   * Returns the index of the first occurrence of the given Pattern in str by an offset. Returns
   * null if not found.
   * 
   * @param regex a Pattern
   * @param offset place to start
   * @return Integer or null
   */
  public Integer index(Pattern regex, int offset) {
    if (regex == null) throw new ClassCastException("TypeError: type mismatch: null given");

    if (offset < 0 || offset > str.length()) return null;

    Matcher matcher = regex.matcher(str);
    if (matcher.find(offset))
      return matcher.start();
    else
      return null;
  }

  /**
   * Inserts otherStr before the character at the given index, modifying str. Negative indices count
   * from the end of the string, and insert after the given character. The intent is insert aString
   * so that it starts at the given index.
   * 
   * @param index position to begin insertion
   * @param otherStr any String
   * @return this {@link RubyString}
   */
  public RubyString insert(int index, String otherStr) {
    if (index < -str.length() - 1 || index > str.length())
      throw new IndexOutOfBoundsException("IndexError: index " + index + " out of string");

    str = eachChar().toA().insert(index, stringify(otherStr)).join();
    return this;
  }

  /**
   * Returns a printable version of str, surrounded by quote marks, with special characters escaped.
   * 
   * @return new {@link RubyString}
   */
  public RubyString inspect() {
    String printable = eachChar().map(item -> {
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
        int codepoint = item.codePointAt(0);
        if (codepoint < 256)
          return "\\" + Integer.toOctalString(codepoint);
        else
          return "\\u" + Integer.toHexString(codepoint);
      }
      return item;
    }).join();
    return Ruby.String.of("\"" + printable + "\"");
  }

  /**
   * Returns a {@link RubyArray} of lines in str split using the supplied record separator
   * (System.getProperty("line.separator") by default).
   * 
   * @return {@link RubyArray}
   */
  public RubyArray<String> lines() {
    return eachLine().toA();
  }

  /**
   * Returns a {@link RubyArray} of lines in str split using given record separator.
   * 
   * @return {@link RubyArray}
   */
  public RubyArray<String> lines(String separator) {
    return eachLine(separator).toA();
  }

  /**
   * If integer is greater than the length of str, returns a new {@link RubyString} of length
   * integer with str left justified and padded with whitesapce; otherwise, returns str.
   * 
   * @param width of new {@link RubyString}
   * @return new {@link RubyString}
   */
  public RubyString ljust(int width) {
    return ljust(width, " ");
  }

  /**
   * If integer is greater than the length of str, returns a new {@link RubyString} of length
   * integer with str left justified and padded with padstr; otherwise, returns str.
   * 
   * @param width of new {@link RubyString}
   * @param padstr used to pad on the right of new {@link RubyString}
   * @return new {@link RubyString}
   */
  public RubyString ljust(int width, String padstr) {
    RubyLazyEnumerator<String> padStr = Ruby.String.of(stringify(padstr)).eachChar().lazy().cycle();

    int extra = width - str.length();
    if (extra > 0) {
      StringBuilder sb = new StringBuilder();
      while (extra > 0) {
        sb.append(padStr.next());
        extra--;
      }
      return Ruby.String.of(str + sb.toString());
    }

    return Ruby.String.of(str);
  }

  /**
   * Returns a copy of str with leading whitespace removed.
   * 
   * @return new {@link RubyString}
   */
  public RubyString lstrip() {
    return Ruby.String.of(str.replaceFirst("^\\s+", ""));
  }

  /**
   * Removes leading whitespace from str, returning null if no change was made.
   * 
   * @return this {@link RubyString} or null
   */
  public RubyString lstripǃ() {
    return inPlace(lstrip());
  }

  /**
   * Converts regex to a Pattern, then invokes its match method on str. Returns null if no match
   * found.
   * 
   * @param regex regular expression
   * @return Matcher or null
   */
  public Matcher match(String regex) {
    Matcher matcher = Pattern.compile(stringify(regex)).matcher(str);
    return matcher.find() ? matcher.reset() : null;
  }

  /**
   * Converts regex to a Pattern, then invokes its match method on str. The second parameter
   * specifies the position in the string to begin the search. Returns null if no match found.
   * 
   * @param regex regular expression
   * @param pos position begin to search
   * @return Matcher or null
   */
  public Matcher match(String regex, int pos) {
    if (pos < 0) pos = pos + str.length();
    if (pos >= str.length() || pos < 0) return null;

    Matcher matcher = Pattern.compile(stringify(regex)).matcher(str);
    matcher.region(pos, str.length());
    return matcher.find() ? matcher.reset().region(pos, str.length()) : null;
  }

  /**
   * Returns the successor to str.
   * 
   * @return new {@link RubyString}
   */
  public RubyString next() {
    return Ruby.String.of(StringSuccessor.getInstance().succ(str));
  }

  /**
   * Equivalent to {@link #next()}, but modifies the receiver in place.
   * 
   * @return new {@link RubyString}
   */
  public RubyString nextǃ() {
    str = StringSuccessor.getInstance().succ(str);
    return this;
  }

  /**
   * Treats leading characters of str as a string of octal digits (with an optional sign) and
   * returns the corresponding number. Returns 0 if the conversion fails.
   * 
   * @return int
   */
  public int oct() {
    return toI(8);
  }

  /**
   * Return the Integer ordinal of a one-character string.
   * 
   * @return int
   */
  public int ord() {
    if (str.isEmpty()) throw new IllegalArgumentException("ArgumentError: empty string");

    return str.codePointAt(0);
  }

  /**
   * Searches sep in the string and returns the part before it, the match, and the part after it. If
   * it is not found, returns two empty strings and str.
   * 
   * @param sep
   * @return {@link RubyArray}
   */
  public RubyArray<String> partition(String sep) {
    if (sep == null) throw new ClassCastException("TypeError: type mismatch: null given");

    int sepIndex = str.indexOf(sep);
    if (sepIndex == -1) return Ruby.Array.of(str, "", "");

    return Ruby.Array.of(str.substring(0, sepIndex), sep, str.substring(sepIndex + sep.length()));
  }

  /**
   * Searches pattern in the string and returns the part before it, the match, and the part after
   * it. If it is not found, returns two empty strings and str.
   * 
   * @param pattern a Pattern
   * @return {@link RubyArray}
   */
  public RubyArray<String> partition(Pattern pattern) {
    if (pattern == null) throw new ClassCastException("TypeError: type mismatch: null given");

    Matcher matcher = pattern.matcher(str);
    if (matcher.find()) {
      String sep = matcher.group();
      int sepIndex = str.indexOf(sep);
      return Ruby.Array.of(str.substring(0, sepIndex), sep, str.substring(sepIndex + sep.length()));
    } else {
      return Ruby.Array.of(str, "", "");
    }
  }

  /**
   * Prepends the given string to str.
   * 
   * @param otherStr any String
   * @return new {@link RubyString}
   */
  public RubyString prepend(String otherStr) {
    str = stringify(otherStr) + str;
    return this;
  }

  /**
   * Replaces the contents and taintedness of str with the corresponding values in otherStr.
   * 
   * @param otherStr
   * @return this {@link RubyString}
   */
  public RubyString replace(String otherStr) {
    str = stringify(otherStr);
    return this;
  }

  /**
   * Returns a new string with the characters from str in reverse order.
   * 
   * @return new {@link RubyString}
   */
  public RubyString reverse() {
    return Ruby.String.of(new StringBuilder(str).reverse().toString());
  }

  /**
   * Reverses str in place.
   * 
   * @return this {@link RubyString}
   */
  public RubyString reverseǃ() {
    str = reverse().toS();
    return this;
  }

  /**
   * Returns the index of the last occurrence of the given substring in str. Returns null if not
   * found.
   * 
   * @param substring any String
   * @return Ineger or null
   */
  public Integer rindex(String substring) {
    return rindex(stringify(substring), str.length() - 1);
  }

  /**
   * Returns the index of the last occurrence of the given Pattern in str. Returns null if not
   * found. The second parameter specifies the position in the string to end the search—characters
   * beyond this point will not be considered.
   * 
   * @param substring any String
   * @param stopAt position to stop search
   * @return Ineger or null
   */
  public Integer rindex(String substring, int stopAt) {
    stringify(substring);

    if (stopAt < 0) stopAt += str.length();
    if (stopAt < 0) return null;

    String revStr = new StringBuilder(str).reverse().toString();
    int index = revStr.indexOf(substring, str.length() - stopAt - 1);
    return index == -1 ? null : str.length() - index - 1;
  }

  /**
   * Returns the index of the last occurrence of the given Pattern in str. Returns null if not
   * found.
   * 
   * @param pattern a Pattern
   * @return Integer or null
   */
  public Integer rindex(Pattern pattern) {
    if (pattern == null) throw new ClassCastException("TypeError: type mismatch: null given");

    return rindex(pattern, str.length() - 1);
  }

  /**
   * Returns the index of the last occurrence of the given Pattern in str. Returns null if not
   * found. The second parameter specifies the position in the string to end the search—characters
   * beyond this point will not be considered.
   * 
   * @param pattern a Pattern
   * @param stopAt position to stop search
   * @return Integer or null
   */
  public Integer rindex(Pattern pattern, int stopAt) {
    if (pattern == null) throw new ClassCastException("TypeError: type mismatch: null given");

    if (stopAt < 0) stopAt += str.length();
    if (stopAt < 0) return null;

    Matcher matcher = pattern.matcher(str);
    int index = -1;
    while (matcher.find()) {
      int found = matcher.start();
      if (found <= stopAt) index = found;
    }
    return index == -1 ? null : index;
  }

  /**
   * If width is greater than the length of str, returns a new String of length integer with str
   * right justified and padded with whitespace; otherwise, returns str.
   * 
   * @param width of new {@link RubyString}
   * @return new {@link RubyString}
   */
  public RubyString rjust(int width) {
    return rjust(width, " ");
  }

  /**
   * If width is greater than the length of str, returns a new String of length integer with str
   * right justified and padded with padstr; otherwise, returns str.
   * 
   * @param width of new {@link RubyString}
   * @param padstr used to pad on the left of new RubyString
   * @return new {@link RubyString}
   */
  public RubyString rjust(int width, String padstr) {
    RubyLazyEnumerator<String> padStr = Ruby.String.of(padstr).eachChar().lazy().cycle();

    int extra = width - str.length();
    if (extra > 0) {
      StringBuilder sb = new StringBuilder();
      while (extra > 0) {
        sb.append(padStr.next());
        extra--;
      }
      return Ruby.String.of(sb.toString() + str);
    }

    return this;
  }

  /**
   * Searches sep in the string from the end of the string, and returns the part before it, the
   * match, and the part after it. If it is not found, returns two empty strings and str.
   * 
   * @param sep separator
   * @return {@link RubyArray}
   */
  public RubyArray<String> rpartition(String sep) {
    if (sep == null) throw new ClassCastException("TypeError: type mismatch: null given");

    int sepIndex = str.lastIndexOf(sep);
    if (sepIndex == -1) return Ruby.Array.of("", "", str);

    return Ruby.Array.of(str.substring(0, sepIndex), sep, str.substring(sepIndex + sep.length()));
  }

  /**
   * Searches Pattern in the string from the end of the string, and returns the part before it, the
   * match, and the part after it. If it is not found, returns two empty strings and str.
   * 
   * @param pattern a Pattern
   * @return {@link RubyArray}
   */
  public RubyArray<String> rpartition(Pattern pattern) {
    if (pattern == null) throw new ClassCastException("TypeError: type mismatch: null given");

    Matcher matcher = pattern.matcher(str);
    if (matcher.find()) {
      String sep = matcher.group();
      matcher.region(matcher.start() + 1, str.length());
      while (matcher.find()) {
        sep = matcher.group();
        matcher.region(matcher.start() + 1, str.length());
      }
      int sepIndex = str.lastIndexOf(sep);
      return Ruby.Array.of(str.substring(0, sepIndex), sep, str.substring(sepIndex + sep.length()));
    }

    return Ruby.Array.of("", "", str);
  }

  /**
   * Returns a copy of str with trailing whitespace removed.
   * 
   * @return new {@link RubyString}
   * @see #lstrip()
   * @see #strip()
   */
  public RubyString rstrip() {
    return Ruby.String.of(str.replaceFirst("\\s+$", ""));
  }

  /**
   * Removes trailing whitespace from str, returning nil if no change was made.
   * 
   * @return this {@link RubyString}
   * @see #lstripǃ()
   * @see #stripǃ()
   */
  public RubyString rstripǃ() {
    return inPlace(rstrip());
  }

  /**
   * Both forms iterate through str, matching the regexp. For each match, a result is generated and
   * added to the result {@link RubyArray}.
   * 
   * @param regex regular expression
   * @return {@link RubyArray}
   */
  public RubyArray<String> scan(String regex) {
    Matcher matcher = Pattern.compile(stringify(regex)).matcher(str);
    RubyArray<String> matches = Ruby.Array.create();
    while (matcher.find()) {
      matches.add(matcher.group());
    }
    return matches;
  }

  /**
   * Both forms iterate through str, matching the regexp. For each match, a result is generated and
   * passed to the block.
   * 
   * @param regex regular expression
   * @param block to do the replacement
   * @return this {@link RubyString}
   */
  public RubyString scan(String regex, Consumer<? super String> block) {
    scan(regex).each(block);
    return this;
  }

  /**
   * Both forms iterate through str, matching the regexp. For each match, a result is generated and
   * added to the result array. If the pattern contains no groups, each individual result consists
   * of the matched string. If the pattern contains groups, each individual result is itself an
   * array containing one entry per group.
   * 
   * @param regex regular expression
   * @return {@link RubyArray}
   */
  public RubyArray<RubyArray<String>> scanGroups(String regex) {
    Matcher matcher = Pattern.compile(stringify(regex)).matcher(str);
    RubyArray<RubyArray<String>> groups = Ruby.Array.create();
    while (matcher.find()) {
      RubyArray<String> group = Ruby.Array.create();
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
   * Both forms iterate through str, matching the regexp. For each match, a result is generated and
   * passed to the block. If the pattern contains no groups, each individual result consists of the
   * matched string. If the pattern contains groups, each individual result is itself an array
   * containing one entry per group.
   * 
   * @param regex regular expression
   * @param block to yield matched groups
   * @return this {@link RubyString}
   */
  public RubyString scanGroups(String regex, Consumer<? super RubyArray<String>> block) {
    scanGroups(stringify(regex)).each(block);
    return this;
  }

  /**
   * If the string is invalid byte sequence then replace invalid bytes with �.
   * 
   * @return new {@link RubyString}
   */
  public RubyString scrub() {
    return Ruby.String.of(str.replaceAll("\\p{C}", "\uFFFD"));
  }

  /**
   * If the string is invalid byte sequence then replace invalid bytes with given replacement
   * character.
   * 
   * @return new {@link RubyString}
   */
  public RubyString scrub(String repl) {
    if (repl == null) return scrub();

    return Ruby.String.of(str.replaceAll("\\p{C}", repl));
  }

  /**
   * If the string is invalid byte sequence then replace invalid bytes with returned value of the
   * block.
   * 
   * @return new {@link RubyString}
   */
  public RubyString scrub(Function<? super RubyArray<Byte>, ? extends CharSequence> block) {
    return Ruby.String.of(eachChar().map(item -> {
      if (item.matches("\\p{C}"))
        return block.apply(Ruby.String.of(item).bytes());
      else
        return item;
    }).join());
  }

  /**
   * If the string is invalid byte sequence then replace invalid bytes with �.
   * 
   * @return this {@link RubyString}
   */
  public RubyString scrubǃ() {
    return inPlace(scrub());
  }

  /**
   * If the string is invalid byte sequence then replace invalid bytes with given replacement
   * character.
   * 
   * @return this {@link RubyString}
   */
  public RubyString scrubǃ(String repl) {
    return inPlace(scrub(repl));
  }

  /**
   * If the string is invalid byte sequence then replace invalid bytes with returned value of the
   * block.
   * 
   * @return this {@link RubyString}
   */
  public RubyString scrubǃ(Function<? super RubyArray<Byte>, ? extends CharSequence> block) {
    return inPlace(scrub(block));
  }

  /**
   * Modifies the indexth byte.
   * 
   * @param index position to modify
   * @param b a byte
   * @return byte
   */
  public byte setbyte(int index, byte b) {
    byte[] bytes = str.getBytes();
    if (index < 0) index += bytes.length;
    if (index < 0 || index >= bytes.length)
      throw new IndexOutOfBoundsException("IndexError: index " + index + " out of string");

    bytes[index] = b;
    str = new String(bytes);
    return b;
  }

  /**
   * Returns the character length of str.
   * 
   * @return int
   */
  public int size() {
    return str.length();
  }

  /**
   * Returns a substring of one character at that index.
   * 
   * @param index of the character
   * @return new {@link RubyString} or null
   */
  public RubyString slice(int index) {
    String slicedStr = eachChar().toA().slice(index);
    return slicedStr == null ? null : Ruby.String.of(slicedStr);
  }

  /**
   * Returns a substring containing length characters starting at the index.
   * 
   * @param index position to begin slice
   * @param length of new RubyString
   * @return new {@link RubyString} or null
   */
  public RubyString slice(int index, int length) {
    RubyArray<String> slicedStr = eachChar().toA().slice(index, length);
    return slicedStr == null ? null : Ruby.String.of(slicedStr.join());
  }

  /**
   * Returns the matching portion of the string.
   * 
   * @param pattern a Pattern
   * @return new {@link RubyString} or null
   */
  public RubyString slice(Pattern pattern) {
    if (pattern == null) throw new ClassCastException("TypeError: type mismatch: null given");

    Matcher matcher = pattern.matcher(str);
    if (matcher.find())
      return Ruby.String.of(matcher.group());
    else
      return null;
  }

  /**
   * Returns the target group of matching portion of the string.
   * 
   * @param pattern a Pattern
   * @param group number pf matched group
   * @return new {@link RubyString} or null
   */
  public RubyString slice(Pattern pattern, int group) {
    Matcher matcher = pattern.matcher(str);
    if (matcher.find())
      if (group < 1 || group > matcher.groupCount())
        return null;
      else
        return Ruby.String.of(matcher.group(group));
    else
      return null;
  }

  /**
   * Returns if matchStr occurs in the string.
   * 
   * @param matchStr matched string
   * @return new {@link RubyString} or null
   */
  public RubyString slice(String matchStr) {
    return str.contains(matchStr) ? Ruby.String.of(matchStr) : null;
  }

  /**
   * Deletes the specified portion from str, and returns the portion deleted.
   * 
   * @param index of the character
   * @return new {@link RubyString} or null
   */
  public RubyString sliceǃ(int index) {
    RubyArray<String> chars = eachChar().toA();
    String slicedStr = chars.sliceǃ(index);
    if (slicedStr == null) return null;

    str = chars.join();
    return Ruby.String.of(slicedStr);
  }

  /**
   * Deletes the specified portion from str, and returns the portion deleted.
   * 
   * @param index position to begin slice
   * @param length of new {@link RubyString}
   * @return new {@link RubyString} or null
   */
  public RubyString sliceǃ(int index, int length) {
    RubyArray<String> chars = eachChar().toA();
    RubyArray<String> slicedStr = chars.sliceǃ(index, length);
    if (slicedStr == null) return null;

    str = chars.join();
    return Ruby.String.of(slicedStr.join());
  }

  /**
   * Deletes the specified portion from str, and returns the portion deleted.
   * 
   * @param pattern a Pattern
   * @return new {@link RubyString} or null
   */
  public RubyString sliceǃ(Pattern pattern) {
    RubyString slicedStr = slice(pattern);
    if (slicedStr == null) return null;

    str = str.replace(slicedStr, "");
    return slicedStr;
  }

  /**
   * Deletes the specified portion from str, and returns the portion deleted.
   * 
   * @param pattern a Pattern
   * @param group number pf matched group
   * @return new {@link RubyString} or null
   */
  public RubyString sliceǃ(Pattern pattern, int group) {
    RubyString slicedStr = slice(pattern, group);
    if (slicedStr == null) return null;

    str = str.replace(slicedStr, "");
    return slicedStr;
  }

  /**
   * Deletes the specified portion from str, and returns the portion deleted.
   * 
   * @param matchStr matched string
   * @return new {@link RubyString} or null
   */
  public RubyString sliceǃ(String matchStr) {
    RubyString slicedStr = slice(matchStr);
    if (slicedStr == null) return null;

    str = str.replace(slicedStr, "");
    return slicedStr;
  }

  /**
   * Divides str into substrings based on a whitespaces, returning a {@link RubyArray} of these
   * substrings.
   * 
   * @return {@link RubyArray}
   */
  public RubyArray<String> split() {
    return Ruby.Array.copyOf(str.trim().split(" +"));
  }

  /**
   * Divides str into substrings based on a delimiter, returning a {@link RubyArray} of these
   * substrings.
   * 
   * @param delimiter used to split str
   * @return {@link RubyArray}
   */
  public RubyArray<String> split(final String delimiter) {
    if (delimiter == null || delimiter.equals(" ")) return split();

    return Ruby.Array.copyOf(str.split(Pattern.quote(delimiter)));
  }

  /**
   * Divides str into substrings based on a delimiter, returning a {@link RubyArray} of these
   * substrings, at most that limit number of fields will be returned.
   * 
   * @param delimiter used to split str
   * @param limit max number of fields
   * @return {@link RubyArray}
   */
  public RubyArray<String> split(String delimiter, int limit) {
    if (delimiter == null || delimiter.equals(" ")) {
      if (limit <= 0)
        return Ruby.Array.copyOf(str.trim().split(" +"));
      else if (limit == 1)
        return Ruby.Array.of(str);
      else
        return Ruby.Array.copyOf(Ruby.String.of(str).lstrip().toS().split(" +", limit));
    }

    if (limit <= 0)
      return Ruby.Array.copyOf(str.split(Pattern.quote(delimiter)));
    else
      return Ruby.Array.copyOf(str.split(Pattern.quote(delimiter), limit));
  }

  /**
   * Divides str into substrings based on a Pattern, returning a {@link RubyArray} of these
   * substrings.
   * 
   * @param pattern a Pattern
   * @return {@link RubyArray}
   */
  public RubyArray<String> split(Pattern pattern) {
    if (pattern == null) return split();

    return Ruby.Array.copyOf(str.split(pattern.pattern()));
  }

  /**
   * Divides str into substrings based on a Pattern, returning a {@link RubyArray} of these
   * substrings, at most that limit number of fields will be returned.
   * 
   * @param pattern a Pattern
   * @param limit max number of fields
   * @return {@link RubyArray}
   */
  public RubyArray<String> split(Pattern pattern, int limit) {
    if (pattern == null) return split((String) null, limit);

    if (limit <= 0)
      return Ruby.Array.copyOf(str.split(pattern.pattern()));
    else
      return Ruby.Array.copyOf(str.split(pattern.pattern(), limit));
  }

  /**
   * Builds a set of characters using the procedure described for {@link #count()}.
   * 
   * @return new {@link RubyString}
   */
  public RubyString squeeze() {
    return Ruby.String.of(str.replaceAll("(.)\\1+", "$1"));
  }

  /**
   * Builds a set of characters from the charSet using the procedure described for {@link #count()}.
   * 
   * @param charSet a set of characters
   * @return new {@link RubyString}
   */
  public RubyString squeeze(String charSet) {
    stringify(charSet);
    return Ruby.String.of(str.replaceAll("([" + charSet2Str(charSet) + "])\\1+", "$1"));
  }

  /**
   * Squeezes str in place, returning either str, or nil if no changes were made.
   * 
   * @return this {@link RubyString} or null
   */
  public RubyString squeezeǃ() {
    return inPlace(squeeze());
  }

  /**
   * Squeezes str in place, returning either str, or nil if no changes were made.
   * 
   * @param charSet a set of characters
   * @return this {@link RubyString} or null
   */
  public RubyString squeezeǃ(String charSet) {
    return inPlace(squeeze(charSet));
  }

  /**
   * Returns true if str starts with one of the prefixes given.
   * 
   * @param prefix first prefix
   * @param otherPrefix otner prefixes
   * @return true if str starts with one of the prefixes given, flase otherwise
   */
  public boolean startWithʔ(String prefix, String... otherPrefix) {
    if (str.startsWith(stringify(prefix))) return true;

    if (otherPrefix != null) {
      for (String p : otherPrefix) {
        if (str.startsWith(p)) return true;
      }
    }

    return false;
  }

  /**
   * Returns a copy of str with leading and trailing whitespace removed.
   * 
   * @return new {@link RubyString}
   */
  public RubyString strip() {
    return Ruby.String.of(str.trim());
  }

  /**
   * Removes leading and trailing whitespace from str. Returns nil if str was not altered.
   * 
   * @return this {@link RubyString}
   */
  public RubyString stripǃ() {
    return inPlace(strip());
  }

  /**
   * Returns a copy of str with the first occurrence of pattern replaced by the second argument.
   * 
   * @param regex regular expression
   * @param replacement used to replace matched string
   * @return new {@link RubyString}
   */
  public RubyString sub(String regex, String replacement) {
    return Ruby.String.of(str.replaceFirst(stringify(regex), stringify(replacement)));
  }

  /**
   * Returns a copy of str with the first occurrence of pattern replaced by the second argument. The
   * second argument is a Map, and the matched text is one of its keys, the corresponding value is
   * the replacement string.
   * 
   * @param regex regular expression
   * @param map any Map
   * @return new {@link RubyString}
   */
  public RubyString sub(String regex, Map<String, ?> map) {
    String result = str;
    Matcher matcher = Pattern.compile(stringify(regex)).matcher(str);
    if (matcher.find()) {
      String target = matcher.group();
      if (isPresent(map) && map.containsKey(target))
        result = result.replace(target, map.get(target).toString());
    }
    return Ruby.String.of(result);
  }

  /**
   * Returns a copy of str with the first occurrence of pattern replaced by the value returned by
   * the block will be substituted for the match on each call.
   * 
   * @param regex regular expression
   * @param block to do the replacement
   * @return new {@link RubyString}
   */
  public RubyString sub(String regex, Function<? super String, ? extends CharSequence> block) {
    Matcher matcher = Pattern.compile(stringify(regex)).matcher(str);
    if (matcher.find()) {
      String match = matcher.group();
      return Ruby.String.of(str.replace(match, block.apply(match)));
    }
    return this;
  }

  /**
   * Performs the same substitution as {@link #sub(String, String)} in-place. Returns str if a
   * substitution was performed or nil if no substitution was performed.
   * 
   * @param regex regular expression
   * @param replacement used to replace matched string
   * @return this {@link RubyString} or null
   */
  public RubyString subǃ(String regex, String replacement) {
    return inPlace(sub(regex, replacement));
  }

  /**
   * Performs the same substitution as {@link #sub(String, Function)} in-place. Returns str if a
   * substitution was performed or nil if no substitution was performed.
   * 
   * @param regex regular expression
   * @param block to do the replacement
   * @return this {@link RubyString} or null
   */
  public RubyString subǃ(String regex, Function<? super String, ? extends CharSequence> block) {
    return inPlace(sub(regex, block));
  }

  /**
   * Returns the successor to str. The successor is calculated by incrementing characters starting
   * from the rightmost alphanumeric (or the rightmost character if there are no alphanumerics) in
   * the string. Incrementing a digit always results in another digit, and incrementing a letter
   * results in another letter of the same case. Incrementing nonalphanumerics uses the underlying
   * character set’s collating sequence.
   * 
   * @return new {@link RubyString}
   */
  public RubyString succ() {
    return Ruby.String.of(StringSuccessor.getInstance().succ(str));
  }

  /**
   * Equivalent to {@link #succ()}, but modifies the receiver in place.
   * 
   * @return this {@link RubyString}
   */
  public RubyString succǃ() {
    str = succ().toS();
    return this;
  }

  /**
   * Returns a basic n-bit checksum of the characters in str, where n is 16. The result is simply
   * the sum of the binary value of each character in str modulo 2**n - 1. This is not a
   * particularly good checksum.
   * 
   * @return int
   */
  public int sum() {
    return sum(16);
  }

  /**
   * Returns a basic n-bit checksum of the characters in str, where n is the optional Fixnum
   * parameter. The result is simply the sum of the binary value of each character in str modulo
   * 2**n - 1. This is not a particularly good checksum.
   * 
   * @param n any int
   * @return int
   */
  public int sum(int n) {
    int sum = 0;
    for (byte b : str.getBytes()) {
      sum += b & 0xFF;
    }
    return sum % (int) Math.pow(2, n - 1);
  }

  /**
   * Returns a copy of str with uppercase alphabetic characters converted to lowercase and lowercase
   * characters converted to uppercase. Note: case conversion is effective only in ASCII region.
   * 
   * @return new {@link RubyString}
   */
  public RubyString swapcase() {
    final Pattern upperCase = Pattern.compile("[A-Z]");
    return Ruby.String.of(eachChar()
        .map(item -> upperCase.matcher(item).matches() ? item.toLowerCase() : item.toUpperCase())
        .join());
  }

  /**
   * Equivalent to {@link #swapcase()}, but modifies the receiver in place, returning str, or null
   * if no changes were made. Note: case conversion is effective only in ASCII region.
   * 
   * @return this {@link RubyString} or null
   */
  public RubyString swapcaseǃ() {
    return inPlace(swapcase());
  }

  /**
   * Returns the result of interpreting leading characters in str as a floating point number.
   * Extraneous characters past the end of a valid number are ignored. If there is not a valid
   * number at the start of str, 0.0 is returned. This method never raises an exception.
   * 
   * @return double
   */
  public double toF() {
    Matcher intMatcher =
        Pattern.compile("^\\s*[-+]?\\s*[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?").matcher(str);
    if (intMatcher.find())
      return Double.valueOf(intMatcher.group().trim().replaceAll("\\s+", ""));
    else
      return 0.0;
  }

  /**
   * Returns the result of interpreting leading characters in str as a integer number. Extraneous
   * characters past the end of a valid number are ignored. If there is not a valid number at the
   * start of str, 0 is returned. This method never raises an exception.
   * 
   * @return int
   */
  public int toI() {
    Matcher intMatcher = Pattern.compile("^\\s*[-+]?\\s*\\d+").matcher(str);
    if (intMatcher.find())
      return Integer.valueOf(intMatcher.group().trim().replaceAll("[\\s\\+]+", ""));
    else
      return 0;
  }

  /**
   * Returns the result of interpreting leading characters in str as a integer number. Extraneous
   * characters past the end of a valid number are ignored. If there is not a valid number at the
   * start of str, 0 is returned. This method never raises an exception.
   * 
   * @param radix the number of unique digits
   * @return int
   */
  public int toI(int radix) {
    if (radix < MIN_RADIX || radix > MAX_RADIX)
      throw new IllegalArgumentException("ArgumentError: invalid radix " + radix);

    String digits = "0123456789abcdefghijklmnopqrstuvwxyz";
    Matcher intMatcher =
        Pattern.compile("(?i)^\\s*[-+]?\\s*[" + digits.substring(0, radix) + "]+").matcher(str);
    if (intMatcher.find()) {
      try {
        return Integer.parseInt(intMatcher.group().trim().replaceAll("[\\s\\+]+", ""), radix);
      } catch (NumberFormatException e) {
        return 0;
      }
    }
    return 0;
  }

  /**
   * Returns the receiver.
   * 
   * @return String
   */
  public String toS() {
    return str;
  }

  /**
   * Returns the receiver.
   * 
   * @return String
   */
  public String toStr() {
    return str;
  }

  /**
   * Returns a copy of str with the characters in fromStr replaced by the corresponding characters
   * in toStr. If toStr is shorter than fromStr, it is padded with its last character in order to
   * maintain the correspondence.
   * 
   * @param fromStr a list of characters
   * @param toStr a list of characters
   * @return new {@link RubyString}
   */
  public RubyString tr(String fromStr, String toStr) {
    fromStr = charSet2Str(stringify(fromStr));
    toStr = charSet2Str(stringify(toStr));
    if (fromStr.startsWith("^")) return Ruby.String.of(str.replaceAll("[" + fromStr + "]",
        toStr.isEmpty() ? "" : Ruby.String.of(toStr).eachChar().toA().last()));

    RubyArray<String> fromStrAry =
        Ruby.String.of(fromStr.replace("\\^", "^").replace("\\-", "-")).eachChar().toA();
    RubyArray<String> toStrAry = Ruby.String.of(toStr).eachChar().toA();
    if (toStrAry.isEmpty())
      toStrAry.fill("", 0, fromStrAry.length());
    else if (toStrAry.length() < fromStrAry.length())
      toStrAry.fill(toStrAry.last(), toStrAry.size(), fromStrAry.length() - toStrAry.length());

    RubyHash<String, String> rh =
        fromStrAry.zip(Ruby.Array.of(toStrAry)).toH(ra -> Ruby.Entry.of(ra.at(0), ra.at(1)));
    if (fromStr.contains("\\^")) {
      fromStr = fromStr.replaceAll("\\\\^", "");
      fromStr += "^";
    }
    if (fromStr.contains("\\-")) {
      fromStr = fromStr.replaceAll("\\\\-", "");
      fromStr += "-";
    }
    return gsub("[" + fromStr.replace("\\", "\\\\").replace("[", "\\[").replace("]", "\\]") + "]",
        rh);
  }

  /**
   * Translates str in place, using the same rules as {@link #tr(String, String)}. Returns str, or
   * nil if no changes were made.
   * 
   * @param fromStr a list of characters
   * @param toStr a list of characters
   * @return this {@link RubyString} or null
   */
  public RubyString trǃ(String fromStr, String toStr) {
    return inPlace(tr(fromStr, toStr));
  }

  /**
   * Processes a copy of str as described under {@link #tr(String, String)}, then removes duplicate
   * characters in regions that were affected by the translation.
   * 
   * @param fromStr a list of characters
   * @param toStr a list of characters
   * @return new {@link RubyString}
   */
  public RubyString trS(String fromStr, String toStr) {
    final String fromString = charSet2Str(stringify(fromStr));
    String trStr = tr(fromString, toStr).toS();

    RubyArray<Boolean> matchIndice = eachChar().map(item -> item.matches("[" + fromString + "]"));

    final Boolean[] prev = {matchIndice.get(0)};
    RubyArray<Integer> matchCounts = matchIndice.sliceBefore(item -> {
      if (item == prev[0]) {
        return false;
      } else {
        prev[0] = item;
        return true;
      }
    }).map(item -> item.count());

    if (toStr.isEmpty()) return Ruby.String.of(trStr);

    String trSqueezed = "";
    int i = 0;
    for (Integer mc : matchCounts) {
      if (matchIndice.get(i)) {
        trSqueezed += Ruby.String.of(trStr.substring(i, i + mc)).squeeze();
        i += mc;
      } else {
        trSqueezed += trStr.substring(i, i + mc);
        i += mc;
      }
    }
    return Ruby.String.of(trSqueezed);
  }

  /**
   * Performs {@link #trS(String, String)} processing on str in place, returning str, or null if no
   * changes were made.
   * 
   * @param fromStr a list of characters
   * @param toStr a list of characters
   * @return this {@link RubyString} or null
   */
  public RubyString trSǃ(String fromStr, String toStr) {
    return inPlace(trS(fromStr, toStr));
  }

  /**
   * Decodes str (which may contain binary data) according to the format string, returning an array
   * of each value extracted. The format string consists of a sequence of single-character
   * directives, summarized in the table at the end of this entry. Each directive may be followed by
   * a number, indicating the number of times to repeat with this directive. An asterisk (“*”) will
   * use up all remaining elements.
   * 
   * @param format format string
   * @return {@link RubyArray}
   * @see RubyArray#pack(String)
   */
  public RubyArray<Object> unpack(String format) {
    return Unpacker.unpack(stringify(format), str);
  }

  /**
   * Returns a copy of str with all lowercase letters replaced with their uppercase counterparts.
   * The operation is locale insensitive—only characters “a” to “z” are affected.
   * 
   * @return new {@link RubyString}
   */
  public RubyString upcase() {
    return Ruby.String.of(str.toUpperCase());
  }

  /**
   * Upcases the contents of str, returning nil if no changes were made.
   * 
   * @return new {@link RubyString}
   */
  public RubyString upcaseǃ() {
    return inPlace(upcase());
  }

  /**
   * Iterates through successive values, starting at str and ending at otherStr inclusive, passing
   * each value in turn to the block. The {@link #succ()} method is used to generate each value.
   * 
   * @param otherStr any String
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<String> upto(String otherStr) {
    return upto(stringify(otherStr), false);
  }

  /**
   * Iterates through successive values, starting at str and ending at otherStr inclusive, passing
   * each value in turn to the block. The {@link #succ()} method is used to generate each value. The
   * second argument exclusive is omitted or is false, the last value will be included; otherwise it
   * will be excluded.
   * 
   * @param otherStr any String
   * @param exclusive true if the last value is ommitted, false otherwise
   * @return {@link RubyEnumerator}
   */
  public RubyEnumerator<String> upto(final String otherStr, boolean exclusive) {
    stringify(otherStr);
    if (exclusive)
      return Ruby.Range.of(str, otherStr).closedOpen().each();
    else
      return Ruby.Range.of(str, otherStr).each();
  }

  /**
   * Iterates through successive values, starting at str and ending at otherStr inclusive, passing
   * each value in turn to the block.
   * 
   * @param otherStr any String
   * @param block to yield successive value
   * @return this {@link RubyString}
   */
  public RubyString upto(String otherStr, Consumer<? super String> block) {
    upto(stringify(otherStr), false).each(block);
    return this;
  }

  /**
   * Iterates through successive values, starting at str and ending at otherStr, passing each value
   * in turn to the block. The second argument exclusive is omitted or is false, the last value will
   * be included.
   * 
   * @param otherStr any String
   * @param exclusive true if the last value is omitted, false otherwise
   * @param block to yield successive value
   * @return this {@link RubyString}
   */
  public RubyString upto(String otherStr, boolean exclusive, Consumer<? super String> block) {
    upto(stringify(otherStr), exclusive).each(block);
    return this;
  }

  /**
   * Returns true for a string which encoded correctly.
   * 
   * @param encoding name of encoding
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
