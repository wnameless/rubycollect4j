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

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.rubycollect4j.Ruby;
import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.RubyString;

/**
 * 
 * {@link RubyStrings} is simply a utility class. It provides the Ruby style way to manipulate any
 * CharSequence.
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
    return Ruby.String.of(in).asciiOnlyʔ();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#b()
   */
  public static String b(CharSequence in) {
    return Ruby.String.of(in).b().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#bytes()
   */
  public static List<Byte> bytes(CharSequence in) {
    return Ruby.String.of(in).bytes();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#bytesize()
   */
  public static int bytesize(CharSequence in) {
    return Ruby.String.of(in).bytesize();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#byteslice(int)
   */
  public static String byteslice(CharSequence in, int index) {
    RubyString rs = Ruby.String.of(in).byteslice(index);
    return rs == null ? null : rs.toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#byteslice(int, int)
   */
  public static String byteslice(CharSequence in, int offset, int length) {
    RubyString rs = Ruby.String.of(in).byteslice(offset, length);
    return rs == null ? null : rs.toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#capitalize()
   */
  public static String capitalize(CharSequence in) {
    return Ruby.String.of(in).capitalize().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#casecmp(CharSequence)
   */
  public static int casecmp(CharSequence in, CharSequence charSeq) {
    return Ruby.String.of(in).casecmp(charSeq);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#center(int)
   */
  public static String center(CharSequence in, int width) {
    return Ruby.String.of(in).center(width).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#center(int, String)
   */
  public static String center(CharSequence in, int width, String padstr) {
    return Ruby.String.of(in).center(width, padstr).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#chomp()
   */
  public static String chomp(CharSequence in) {
    return Ruby.String.of(in).chomp().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#chomp(String)
   */
  public static String chomp(CharSequence in, String separator) {
    return Ruby.String.of(in).chomp(separator).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#chop()
   */
  public static String chop(CharSequence in) {
    return Ruby.String.of(in).chop().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#chr()
   */
  public static String chr(CharSequence in) {
    return Ruby.String.of(in).chr().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#clear()
   */
  public static String clear(CharSequence in) {
    return Ruby.String.of(in).clear().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#codepoints()
   */
  public static List<Integer> codepoints(CharSequence in) {
    return Ruby.String.of(in).codepoints();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#concat(int)
   */
  public static String concat(CharSequence in, int codepoint) {
    return Ruby.String.of(in).concat(codepoint).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#concat(Object)
   */
  public static String concat(CharSequence in, Object o) {
    return Ruby.String.of(in).concat(o).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#count(String, String...)
   */
  public static int count(CharSequence in, String charSet, String... charSets) {
    return Ruby.String.of(in).count(charSet, charSets);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#crypt(String)
   */
  public static String crypt(CharSequence in, String salt) {
    return Ruby.String.of(in).crypt(salt).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#delete(String)
   */
  public static String delete(CharSequence in, String charSet) {
    return Ruby.String.of(in).delete(charSet).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#downcase()
   */
  public static String downcase(CharSequence in) {
    return Ruby.String.of(in).downcase().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#dump()
   */
  public static String dump(CharSequence in) {
    return Ruby.String.of(in).dump().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#eachByte()
   */
  public static Iterable<Byte> eachByte(CharSequence in) {
    return Ruby.String.of(in).eachByte();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#eachByte(Consumer)
   */
  public static String eachByte(CharSequence in, Consumer<? super Byte> block) {
    return Ruby.String.of(in).eachByte(block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#eachChar()
   */
  public static Iterable<String> eachChar(CharSequence in) {
    return Ruby.String.of(in).eachChar();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#eachChar(Consumer)
   */
  public static String eachChar(CharSequence in, Consumer<? super String> block) {
    return Ruby.String.of(in).eachChar(block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#eachCodepoint()
   */
  public static Iterable<Integer> eachCodepoint(CharSequence in) {
    return Ruby.String.of(in).eachCodepoint();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#eachCodepoint(Consumer)
   */
  public static String eachCodepoint(CharSequence in, Consumer<? super Integer> block) {
    return Ruby.String.of(in).eachCodepoint(block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#eachLine()
   */
  public static Iterable<String> eachLine(CharSequence in) {
    return Ruby.String.of(in).eachLine();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#eachLine(Consumer)
   */
  public static String eachLine(CharSequence in, Consumer<? super String> block) {
    return Ruby.String.of(in).eachLine(block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#eachLine(String)
   */
  public static Iterable<String> eachLine(CharSequence in, String separator) {
    return Ruby.String.of(in).eachLine(separator);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#eachLine(String, Consumer)
   */
  public static String eachLine(CharSequence in, String separator, Consumer<? super String> block) {
    return Ruby.String.of(in).eachLine(separator, block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#emptyʔ()
   */
  public static boolean emptyʔ(CharSequence in) {
    return Ruby.String.of(in).emptyʔ();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#encode(String)
   */
  public static String encode(CharSequence in, String encoding) {
    return Ruby.String.of(in).encode(encoding).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#encode(String, String)
   */
  public static String encode(CharSequence in, String dstEncoding, String srcEncoding) {
    return Ruby.String.of(in).encode(dstEncoding, srcEncoding).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#endWithʔ(String, String...)
   */
  public static boolean endWithʔ(CharSequence in, String suffix, String... otherSuffix) {
    return Ruby.String.of(in).endWithʔ(suffix, otherSuffix);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#forceEncoding(String)
   */
  public static String forceEncoding(CharSequence in, String encoding) {
    return Ruby.String.of(in).forceEncoding(encoding).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#getbyte(int)
   */
  public static Byte getbyte(CharSequence in, int index) {
    return Ruby.String.of(in).getbyte(index);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#gsub(String)
   */
  public static Iterable<String> gsub(CharSequence in, String regex) {
    return Ruby.String.of(in).gsub(regex);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#gsub(String, Map)
   */
  public static String gsub(CharSequence in, String regex, Map<String, ?> map) {
    return Ruby.String.of(in).gsub(regex, map).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#gsub(String, String)
   */
  public static String gsub(CharSequence in, String regex, String replacement) {
    return Ruby.String.of(in).gsub(regex, replacement).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#gsub(String, Function)
   */
  public static String gsub(CharSequence in, String regex,
      Function<? super String, ? extends CharSequence> block) {
    return Ruby.String.of(in).gsub(regex, block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#hex()
   */
  public static long hex(CharSequence in) {
    return Ruby.String.of(in).hex();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#includeʔ(String)
   */
  public boolean includeʔ(CharSequence in, String otherStr) {
    return Ruby.String.of(in).includeʔ(otherStr);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#index(Pattern)
   */
  public static Integer index(CharSequence in, Pattern regex) {
    return Ruby.String.of(in).index(regex);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#index(String)
   */
  public static Integer index(CharSequence in, String substring) {
    return Ruby.String.of(in).index(substring);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#index(Pattern, int)
   */
  public static Integer index(CharSequence in, Pattern regex, int offset) {
    return Ruby.String.of(in).index(regex, offset);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#index(String, int)
   */
  public static Integer index(CharSequence in, String substring, int offset) {
    return Ruby.String.of(in).index(substring, offset);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#insert(int, String)
   */
  public static String insert(CharSequence in, int index, String otherStr) {
    return Ruby.String.of(in).insert(index, otherStr).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#inspect()
   */
  public static String inspect(CharSequence in) {
    return Ruby.String.of(in).inspect().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#lines()
   */
  public static List<String> lines(CharSequence in) {
    return Ruby.String.of(in).lines();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#lines(String)
   */
  public static List<String> lines(CharSequence in, String separator) {
    return Ruby.String.of(in).lines(separator);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#ljust(int)
   */
  public static String ljust(CharSequence in, int width) {
    return Ruby.String.of(in).ljust(width).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#ljust(int, String)
   */
  public static String ljust(CharSequence in, int width, String padstr) {
    return Ruby.String.of(in).ljust(width, padstr).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#lstrip()
   */
  public static String lstrip(CharSequence in) {
    return Ruby.String.of(in).lstrip().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#match(String)
   */
  public static Matcher match(CharSequence in, String regex) {
    return Ruby.String.of(in).match(regex);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#match(String, int)
   */
  public static Matcher match(CharSequence in, String regex, int pos) {
    return Ruby.String.of(in).match(regex, pos);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#next()
   */
  public static String next(CharSequence in) {
    return Ruby.String.of(in).next().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#oct()
   */
  public static int oct(CharSequence in) {
    return Ruby.String.of(in).oct();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#ord()
   */
  public static int ord(CharSequence in) {
    return Ruby.String.of(in).ord();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#partition(Pattern)
   */
  public static List<String> partition(CharSequence in, Pattern pattern) {
    return Ruby.String.of(in).partition(pattern);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#partition(String)
   */
  public static List<String> partition(CharSequence in, String sep) {
    return Ruby.String.of(in).partition(sep);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#prepend(String)
   */
  public static String prepend(CharSequence in, String otherStr) {
    return Ruby.String.of(in).prepend(otherStr).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#reverse()
   */
  public static String reverse(CharSequence in) {
    return Ruby.String.of(in).reverse().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#rindex(Pattern)
   */
  public static Integer rindex(CharSequence in, Pattern pattern) {
    return Ruby.String.of(in).rindex(pattern);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#rindex(String)
   */
  public static Integer rindex(CharSequence in, String substring) {
    return Ruby.String.of(in).rindex(substring);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#rindex(Pattern, int)
   */
  public static Integer rindex(CharSequence in, Pattern pattern, int stopAt) {
    return Ruby.String.of(in).rindex(pattern, stopAt);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#rindex(String, int)
   */
  public static Integer rindex(CharSequence in, String substring, int stopAt) {
    return Ruby.String.of(in).rindex(substring, stopAt);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#rjust(int)
   */
  public static String rjust(CharSequence in, int width) {
    return Ruby.String.of(in).rjust(width).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#rjust(int, String)
   */
  public static String rjust(CharSequence in, int width, String padstr) {
    return Ruby.String.of(in).rjust(width, padstr).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#rpartition(Pattern)
   */
  public static List<String> rpartition(CharSequence in, Pattern pattern) {
    return Ruby.String.of(in).rpartition(pattern);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#rpartition(String)
   */
  public static List<String> rpartition(CharSequence in, String sep) {
    return Ruby.String.of(in).rpartition(sep);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#rstrip()
   */
  public static String rstrip(CharSequence in) {
    return Ruby.String.of(in).rstrip().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#scan(String)
   */
  public static List<String> scan(CharSequence in, String regex) {
    return Ruby.String.of(in).scan(regex);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#scan(String, Consumer)
   */
  public static String scan(CharSequence in, String regex, Consumer<? super String> block) {
    return Ruby.String.of(in).scan(regex, block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#scanGroups(String)
   */
  public static List<? extends List<String>> scanGroups(CharSequence in, String regex) {
    return Ruby.String.of(in).scanGroups(regex);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#scanGroups(String, Consumer)
   */
  public static String scanGroups(CharSequence in, String regex,
      Consumer<? super RubyArray<String>> block) {
    return Ruby.String.of(in).scanGroups(regex, block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#scrub()
   */
  public static String scrub(CharSequence in) {
    return Ruby.String.of(in).scrub().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#scrub(String)
   */
  public static String scrub(CharSequence in, String repl) {
    return Ruby.String.of(in).scrub(repl).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#scrub(Function)
   */
  public static String scrub(CharSequence in,
      Function<? super RubyArray<? super Byte>, ? extends String> block) {
    return Ruby.String.of(in).scrub(block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#setbyte(int, byte)
   */
  public static String setbyte(CharSequence in, int index, byte b) {
    RubyString rs = Ruby.String.of(in);
    rs.setbyte(index, b);
    return rs.toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#slice(int)
   */
  public static String slice(CharSequence in, int index) {
    RubyString rs = Ruby.String.of(in).slice(index);
    return rs == null ? null : rs.toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#slice(Pattern)
   */
  public static String slice(CharSequence in, Pattern pattern) {
    RubyString rs = Ruby.String.of(in).slice(pattern);
    return rs == null ? null : rs.toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#slice(String)
   */
  public static String slice(CharSequence in, String matchStr) {
    RubyString rs = Ruby.String.of(in).slice(matchStr);
    return rs == null ? null : rs.toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#slice(int, int)
   */
  public static String slice(CharSequence in, int index, int length) {
    RubyString rs = Ruby.String.of(in).slice(index, length);
    return rs == null ? null : rs.toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#slice(Pattern, int)
   */
  public static String slice(CharSequence in, Pattern pattern, int group) {
    RubyString rs = Ruby.String.of(in).slice(pattern, group);
    return rs == null ? null : rs.toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#split()
   */
  public static List<String> split(CharSequence in) {
    return Ruby.String.of(in).split();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#split(Pattern)
   */
  public static List<String> split(CharSequence in, Pattern pattern) {
    return Ruby.String.of(in).split(pattern);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#split(String)
   */
  public static List<String> split(CharSequence in, String delimiter) {
    return Ruby.String.of(in).split(delimiter);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#split(Pattern, int)
   */
  public static List<String> split(CharSequence in, Pattern pattern, int limit) {
    return Ruby.String.of(in).split(pattern, limit);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#split(String, int)
   */
  public static List<String> split(CharSequence in, String delimiter, int limit) {
    return Ruby.String.of(in).split(delimiter, limit);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#squeeze()
   */
  public static String squeeze(CharSequence in) {
    return Ruby.String.of(in).squeeze().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#squeeze(String)
   */
  public static String squeeze(CharSequence in, String charSet) {
    return Ruby.String.of(in).squeeze(charSet).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#startWithʔ(String, String...)
   */
  public static boolean startWithʔ(CharSequence in, String prefix, String... otherPrefix) {
    return Ruby.String.of(in).startWithʔ(prefix, otherPrefix);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#strip()
   */
  public static String strip(CharSequence in) {
    return Ruby.String.of(in).strip().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#sub(String, Map)
   */
  public static String sub(CharSequence in, String regex, Map<String, ?> map) {
    return Ruby.String.of(in).sub(regex, map).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#sub(String, String)
   */
  public static String sub(CharSequence in, String regex, String replacement) {
    return Ruby.String.of(in).sub(regex, replacement).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#sub(String, Function)
   */
  public static String sub(CharSequence in, String regex,
      Function<? super String, ? extends CharSequence> block) {
    return Ruby.String.of(in).sub(regex, block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#succ()
   */
  public static String succ(CharSequence in) {
    return Ruby.String.of(in).succ().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#sum()
   */
  public static int sum(CharSequence in) {
    return Ruby.String.of(in).sum();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#sum(int)
   */
  public static int sum(CharSequence in, int n) {
    return Ruby.String.of(in).sum(n);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#swapcase()
   */
  public static String swapcase(CharSequence in) {
    return Ruby.String.of(in).swapcase().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#toF()
   */
  public static double toF(CharSequence in) {
    return Ruby.String.of(in).toF();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#toI()
   */
  public static int toI(CharSequence in) {
    return Ruby.String.of(in).toI();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#toI(int)
   */
  public static int toI(CharSequence in, int radix) {
    return Ruby.String.of(in).toI(radix);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#toS()
   */
  public static String toS(CharSequence in) {
    return Ruby.String.of(in).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#toStr()
   */
  public static String toStr(CharSequence in) {
    return Ruby.String.of(in).toStr();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#tr(String, String)
   */
  public static String tr(CharSequence in, String fromStr, String toStr) {
    return Ruby.String.of(in).tr(fromStr, toStr).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#trS(String, String)
   */
  public static String trS(CharSequence in, String fromStr, String toStr) {
    return Ruby.String.of(in).trS(fromStr, toStr).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#unpack(String)
   */
  public static List<Object> unpack(CharSequence in, String format) {
    return Ruby.String.of(in).unpack(format);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#upcase()
   */
  public static String upcase(CharSequence in) {
    return Ruby.String.of(in).upcase().toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#upto(String)
   */
  public static Iterable<String> upto(CharSequence in, String otherStr) {
    return Ruby.String.of(in).upto(otherStr);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#upto(String, Consumer)
   */
  public static String upto(CharSequence in, String otherStr, Consumer<? super String> block) {
    return Ruby.String.of(in).upto(otherStr, block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#upto(String, boolean)
   */
  public static Iterable<String> upto(CharSequence in, String otherStr, boolean exclusive) {
    return Ruby.String.of(in).upto(otherStr, exclusive);
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#upto(String, boolean, Consumer)
   */
  public static String upto(CharSequence in, String otherStr, boolean exclusive,
      Consumer<? super String> block) {
    return Ruby.String.of(in).upto(otherStr, exclusive, block).toS();
  }

  /**
   * @see net.sf.rubycollect4j.RubyString#validEncodingʔ(String)
   */
  public static boolean validEncodingʔ(CharSequence in, String encoding) {
    return Ruby.String.of(in).validEncodingʔ(encoding);
  }

}
