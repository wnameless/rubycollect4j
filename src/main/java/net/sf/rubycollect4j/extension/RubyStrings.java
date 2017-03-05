/*
 *
 * Copyright 2016 Wei-Ming Wu
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
package net.sf.rubycollect4j.extension;

import static net.sf.rubycollect4j.RubyCollections.rs;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.RubyString;

/**
 * 
 * {@link RubyStrings} is simply a utility class. It provides the Ruby style way
 * to manipulate any CharSequence.
 * 
 * @author Wei-Ming Wu
 *
 */
public final class RubyStrings {

  private RubyStrings() {}

  /**
   * @see net.sf.rubycollect4j.RubyString#asciiOnlyʔ()
   */
  public static boolean asciiOnlyʔ(CharSequence in) {
    return rs(in).asciiOnlyʔ();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#b()
   */
  public static String b(CharSequence in) {
    return rs(in).b().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#bytes()
   */
  public static List<Byte> bytes(CharSequence in) {
    return rs(in).bytes();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#bytesize()
   */
  public static int bytesize(CharSequence in) {
    return rs(in).bytesize();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#byteslice(int)
   */
  public static String byteslice(CharSequence in, int index) {
    RubyString rs = rs(in).byteslice(index);
    return rs == null ? null : rs.toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#byteslice(int, int)
   */
  public static String byteslice(CharSequence in, int offset, int length) {
    RubyString rs = rs(in).byteslice(offset, length);
    return rs == null ? null : rs.toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#capitalize()
   */
  public static String capitalize(CharSequence in) {
    return rs(in).capitalize().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#casecmp(CharSequence)
   */
  public static int casecmp(CharSequence in, CharSequence charSeq) {
    return rs(in).casecmp(charSeq);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#center(int)
   */
  public static String center(CharSequence in, int width) {
    return rs(in).center(width).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#center(int, String)
   */
  public static String center(CharSequence in, int width, String padstr) {
    return rs(in).center(width, padstr).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#chomp()
   */
  public static String chomp(CharSequence in) {
    return rs(in).chomp().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#chomp(String)
   */
  public static String chomp(CharSequence in, String separator) {
    return rs(in).chomp(separator).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#chop()
   */
  public static String chop(CharSequence in) {
    return rs(in).chop().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#chr()
   */
  public static String chr(CharSequence in) {
    return rs(in).chr().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#clear()
   */
  public static String clear(CharSequence in) {
    return rs(in).clear().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#codepoints()
   */
  public static List<Integer> codepoints(CharSequence in) {
    return rs(in).codepoints();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#concat(int)
   */
  public static String concat(CharSequence in, int codepoint) {
    return rs(in).concat(codepoint).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#concat(Object)
   */
  public static String concat(CharSequence in, Object o) {
    return rs(in).concat(o).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#count(String, String...)
   */
  public static int count(CharSequence in, String charSet, String... charSets) {
    return rs(in).count(charSet, charSets);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#crypt(String)
   */
  public static String crypt(CharSequence in, String salt) {
    return rs(in).crypt(salt).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#delete(String)
   */
  public static String delete(CharSequence in, String charSet) {
    return rs(in).delete(charSet).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#downcase()
   */
  public static String downcase(CharSequence in) {
    return rs(in).downcase().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#dump()
   */
  public static String dump(CharSequence in) {
    return rs(in).dump().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#eachByte()
   */
  public static Iterable<Byte> eachByte(CharSequence in) {
    return rs(in).eachByte();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#eachByte(Consumer)
   */
  public static String eachByte(CharSequence in, Consumer<Byte> block) {
    return rs(in).eachByte(block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#eachChar()
   */
  public static Iterable<String> eachChar(CharSequence in) {
    return rs(in).eachChar();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#eachChar(Consumer)
   */
  public static String eachChar(CharSequence in, Consumer<String> block) {
    return rs(in).eachChar(block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#eachCodepoint()
   */
  public static Iterable<Integer> eachCodepoint(CharSequence in) {
    return rs(in).eachCodepoint();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#eachCodepoint(Consumer)
   */
  public static String eachCodepoint(CharSequence in, Consumer<Integer> block) {
    return rs(in).eachCodepoint(block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#eachLine()
   */
  public static Iterable<String> eachLine(CharSequence in) {
    return rs(in).eachLine();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#eachLine(Consumer)
   */
  public static String eachLine(CharSequence in, Consumer<String> block) {
    return rs(in).eachLine(block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#eachLine(String)
   */
  public static Iterable<String> eachLine(CharSequence in, String separator) {
    return rs(in).eachLine(separator);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#eachLine(String, Consumer)
   */
  public static String eachLine(CharSequence in, String separator,
      Consumer<String> block) {
    return rs(in).eachLine(separator, block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#emptyʔ()
   */
  public static boolean emptyʔ(CharSequence in) {
    return rs(in).emptyʔ();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#encode(String)
   */
  public static String encode(CharSequence in, String encoding) {
    return rs(in).encode(encoding).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#encode(String, String)
   */
  public static String encode(CharSequence in, String dstEncoding,
      String srcEncoding) {
    return rs(in).encode(dstEncoding, srcEncoding).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#endWithʔ(String, String...)
   */
  public static boolean endWithʔ(CharSequence in, String suffix,
      String... otherSuffix) {
    return rs(in).endWithʔ(suffix, otherSuffix);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#forceEncoding(String)
   */
  public static String forceEncoding(CharSequence in, String encoding) {
    return rs(in).forceEncoding(encoding).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#getbyte(int)
   */
  public static Byte getbyte(CharSequence in, int index) {
    return rs(in).getbyte(index);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#gsub(String)
   */
  public static Iterable<String> gsub(CharSequence in, String regex) {
    return rs(in).gsub(regex);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#gsub(String, Map)
   */
  public static String gsub(CharSequence in, String regex, Map<String, ?> map) {
    return rs(in).gsub(regex, map).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#gsub(String, String)
   */
  public static String gsub(CharSequence in, String regex, String replacement) {
    return rs(in).gsub(regex, replacement).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#gsub(String, Function)
   */
  public static String gsub(CharSequence in, String regex,
      Function<String, String> block) {
    return rs(in).gsub(regex, block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#hex()
   */
  public static long hex(CharSequence in) {
    return rs(in).hex();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#includeʔ(String)
   */
  public boolean includeʔ(CharSequence in, String otherStr) {
    return rs(in).includeʔ(otherStr);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#index(Pattern)
   */
  public static Integer index(CharSequence in, Pattern regex) {
    return rs(in).index(regex);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#index(String)
   */
  public static Integer index(CharSequence in, String substring) {
    return rs(in).index(substring);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#index(Pattern, int)
   */
  public static Integer index(CharSequence in, Pattern regex, int offset) {
    return rs(in).index(regex, offset);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#index(String, int)
   */
  public static Integer index(CharSequence in, String substring, int offset) {
    return rs(in).index(substring, offset);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#insert(int, String)
   */
  public static String insert(CharSequence in, int index, String otherStr) {
    return rs(in).insert(index, otherStr).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#inspect()
   */
  public static String inspect(CharSequence in) {
    return rs(in).inspect().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#lines()
   */
  public static List<String> lines(CharSequence in) {
    return rs(in).lines();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#lines(String)
   */
  public static List<String> lines(CharSequence in, String separator) {
    return rs(in).lines(separator);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#ljust(int)
   */
  public static String ljust(CharSequence in, int width) {
    return rs(in).ljust(width).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#ljust(int, String)
   */
  public static String ljust(CharSequence in, int width, String padstr) {
    return rs(in).ljust(width, padstr).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#lstrip()
   */
  public static String lstrip(CharSequence in) {
    return rs(in).lstrip().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#match(String)
   */
  public static Matcher match(CharSequence in, String regex) {
    return rs(in).match(regex);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#match(String, int)
   */
  public static Matcher match(CharSequence in, String regex, int pos) {
    return rs(in).match(regex, pos);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#next()
   */
  public static String next(CharSequence in) {
    return rs(in).next().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#oct()
   */
  public static int oct(CharSequence in) {
    return rs(in).oct();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#ord()
   */
  public static int ord(CharSequence in) {
    return rs(in).ord();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#partition(Pattern)
   */
  public static List<String> partition(CharSequence in, Pattern pattern) {
    return rs(in).partition(pattern);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#partition(String)
   */
  public static List<String> partition(CharSequence in, String sep) {
    return rs(in).partition(sep);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#prepend(String)
   */
  public static String prepend(CharSequence in, String otherStr) {
    return rs(in).prepend(otherStr).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#reverse()
   */
  public static String reverse(CharSequence in) {
    return rs(in).reverse().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#rindex(Pattern)
   */
  public static Integer rindex(CharSequence in, Pattern pattern) {
    return rs(in).rindex(pattern);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#rindex(String)
   */
  public static Integer rindex(CharSequence in, String substring) {
    return rs(in).rindex(substring);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#rindex(Pattern, int)
   */
  public static Integer rindex(CharSequence in, Pattern pattern, int stopAt) {
    return rs(in).rindex(pattern, stopAt);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#rindex(String, int)
   */
  public static Integer rindex(CharSequence in, String substring, int stopAt) {
    return rs(in).rindex(substring, stopAt);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#rjust(int)
   */
  public static String rjust(CharSequence in, int width) {
    return rs(in).rjust(width).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#rjust(int, String)
   */
  public static String rjust(CharSequence in, int width, String padstr) {
    return rs(in).rjust(width, padstr).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#rpartition(Pattern)
   */
  public static List<String> rpartition(CharSequence in, Pattern pattern) {
    return rs(in).rpartition(pattern);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#rpartition(String)
   */
  public static List<String> rpartition(CharSequence in, String sep) {
    return rs(in).rpartition(sep);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#rstrip()
   */
  public static String rstrip(CharSequence in) {
    return rs(in).rstrip().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#scan(String)
   */
  public static List<String> scan(CharSequence in, String regex) {
    return rs(in).scan(regex);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#scan(String, Consumer)
   */
  public static String scan(CharSequence in, String regex,
      Consumer<String> block) {
    return rs(in).scan(regex, block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#scanGroups(String)
   */
  public static List<? extends List<String>> scanGroups(CharSequence in,
      String regex) {
    return rs(in).scanGroups(regex);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#scanGroups(String, Consumer)
   */
  public static String scanGroups(CharSequence in, String regex,
      Consumer<RubyArray<String>> block) {
    return rs(in).scanGroups(regex, block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#scrub()
   */
  public static String scrub(CharSequence in) {
    return rs(in).scrub().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#scrub(String)
   */
  public static String scrub(CharSequence in, String repl) {
    return rs(in).scrub(repl).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#scrub(Function)
   */
  public static String scrub(CharSequence in,
      Function<RubyArray<Byte>, String> block) {
    return rs(in).scrub(block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#setbyte(int, byte)
   */
  public static String setbyte(CharSequence in, int index, byte b) {
    RubyString rs = rs(in);
    rs.setbyte(index, b);
    return rs.toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#slice(int)
   */
  public static String slice(CharSequence in, int index) {
    RubyString rs = rs(in).slice(index);
    return rs == null ? null : rs.toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#slice(Pattern)
   */
  public static String slice(CharSequence in, Pattern pattern) {
    RubyString rs = rs(in).slice(pattern);
    return rs == null ? null : rs.toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#slice(String)
   */
  public static String slice(CharSequence in, String matchStr) {
    RubyString rs = rs(in).slice(matchStr);
    return rs == null ? null : rs.toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#slice(int, int)
   */
  public static String slice(CharSequence in, int index, int length) {
    RubyString rs = rs(in).slice(index, length);
    return rs == null ? null : rs.toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#slice(Pattern, int)
   */
  public static String slice(CharSequence in, Pattern pattern, int group) {
    RubyString rs = rs(in).slice(pattern, group);
    return rs == null ? null : rs.toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#split()
   */
  public static List<String> split(CharSequence in) {
    return rs(in).split();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#split(Pattern)
   */
  public static List<String> split(CharSequence in, Pattern pattern) {
    return rs(in).split(pattern);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#split(String)
   */
  public static List<String> split(CharSequence in, String delimiter) {
    return rs(in).split(delimiter);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#split(Pattern, int)
   */
  public static List<String> split(CharSequence in, Pattern pattern,
      int limit) {
    return rs(in).split(pattern, limit);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#split(String, int)
   */
  public static List<String> split(CharSequence in, String delimiter,
      int limit) {
    return rs(in).split(delimiter, limit);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#squeeze()
   */
  public static String squeeze(CharSequence in) {
    return rs(in).squeeze().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#squeeze(String)
   */
  public static String squeeze(CharSequence in, String charSet) {
    return rs(in).squeeze(charSet).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#startWithʔ(String, String...)
   */
  public static boolean startWithʔ(CharSequence in, String prefix,
      String... otherPrefix) {
    return rs(in).startWithʔ(prefix, otherPrefix);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#strip()
   */
  public static String strip(CharSequence in) {
    return rs(in).strip().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#sub(String, Map)
   */
  public static String sub(CharSequence in, String regex, Map<String, ?> map) {
    return rs(in).sub(regex, map).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#sub(String, String)
   */
  public static String sub(CharSequence in, String regex, String replacement) {
    return rs(in).sub(regex, replacement).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#sub(String, Function)
   */
  public static String sub(CharSequence in, String regex,
      Function<String, String> block) {
    return rs(in).sub(regex, block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#succ()
   */
  public static String succ(CharSequence in) {
    return rs(in).succ().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#sum()
   */
  public static int sum(CharSequence in) {
    return rs(in).sum();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#sum(int)
   */
  public static int sum(CharSequence in, int n) {
    return rs(in).sum(n);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#swapcase()
   */
  public static String swapcase(CharSequence in) {
    return rs(in).swapcase().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#toF()
   */
  public static double toF(CharSequence in) {
    return rs(in).toF();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#toI()
   */
  public static int toI(CharSequence in) {
    return rs(in).toI();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#toI(int)
   */
  public static int toI(CharSequence in, int radix) {
    return rs(in).toI(radix);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#toS()
   */
  public static String toS(CharSequence in) {
    return rs(in).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#toStr()
   */
  public static String toStr(CharSequence in) {
    return rs(in).toStr();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#tr(String, String)
   */
  public static String tr(CharSequence in, String fromStr, String toStr) {
    return rs(in).tr(fromStr, toStr).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#trS(String, String)
   */
  public static String trS(CharSequence in, String fromStr, String toStr) {
    return rs(in).trS(fromStr, toStr).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#unpack(String)
   */
  public static List<Object> unpack(CharSequence in, String format) {
    return rs(in).unpack(format);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#upcase()
   */
  public static String upcase(CharSequence in) {
    return rs(in).upcase().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#upto(String)
   */
  public static Iterable<String> upto(CharSequence in, String otherStr) {
    return rs(in).upto(otherStr);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#upto(String, Consumer)
   */
  public static String upto(CharSequence in, String otherStr,
      Consumer<String> block) {
    return rs(in).upto(otherStr, block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#upto(String, boolean)
   */
  public static Iterable<String> upto(CharSequence in, String otherStr,
      boolean exclusive) {
    return rs(in).upto(otherStr, exclusive);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#upto(String, boolean, Consumer)
   */
  public static String upto(CharSequence in, String otherStr, boolean exclusive,
      Consumer<String> block) {
    return rs(in).upto(otherStr, exclusive, block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#validEncodingʔ(String)
   */
  public static boolean validEncodingʔ(CharSequence in, String encoding) {
    return rs(in).validEncodingʔ(encoding);
  }

}
