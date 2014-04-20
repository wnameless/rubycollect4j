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
package net.sf.rubycollect4j.util;

import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.rs;

import java.nio.ByteBuffer;
import java.util.List;

import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.RubyString;

/**
 * 
 * {@link ASCII8BitUTF} traverse a String byte by byte. It uses ACII8Bit to
 * encode String characters which have codepoints between 0-255 into single byte
 * and remains other String characters as UTF8 encoding.<br>
 * <br>
 * It is designed for {@link RubyString#unpack} to use.
 *
 */
public final class ASCII8BitUTF {

  private final String str;
  private final RubyArray<String> chars = newRubyArray();
  private final RubyArray<Byte> currentBytes = newRubyArray();
  private final int totalByteNumber;
  private int remainingByteNumber;
  private String currentChar;

  /**
   * Returns a {@link ASCII8BitUTF}.
   * 
   * @param str
   *          any String
   */
  public ASCII8BitUTF(String str) {
    if (str == null)
      throw new NullPointerException();

    this.str = str;
    chars.replace(rs(str).toA());
    totalByteNumber = remainingByteNumber = countByteNumber();
    reset();
  }

  private int countByteNumber() {
    advanceChar();
    int total = 0;
    while (hasByte()) {
      nextByte();
      total++;
    }
    return total;
  }

  private void advanceChar() {
    currentChar = chars.shift();
    for (@SuppressWarnings("unused")
    Byte b : currentBytes) {
      remainingByteNumber--;
    }
    currentBytes.replace(ch2Bytes(currentChar));
  }

  private RubyArray<Byte> ch2Bytes(String ch) {
    if (ch == null)
      return ra();

    if (ch.codePointAt(0) < 256)
      return ByteUtil.toList(
          ByteBuffer.allocate(2).putShort((short) ch.codePointAt(0)).array())
          .last(1);
    else
      return ByteUtil.toList(ch.getBytes());
  }

  /**
   * Returns the total number of bytes.
   * 
   * @return the total number of bytes
   */
  public int totalByteNumber() {
    return totalByteNumber;
  }

  /**
   * Returns the remaining number of bytes.
   * 
   * @return the remaining number of bytes
   */
  public int remainingByteNumber() {
    return remainingByteNumber;
  }

  /**
   * Resets this {@link ASCII8BitUTF}.
   */
  public void reset() {
    chars.replace(rs(str).toA());
    advanceChar();
    remainingByteNumber = totalByteNumber;
  }

  /**
   * Checks if there are remaining characters.
   * 
   * @return true if there are remaining characters, false otherwise
   */
  public boolean hasChar() {
    return (currentChar != null && currentChar.getBytes().length == currentBytes
        .size()) || chars.anyʔ();
  }

  /**
   * Checks if there are remaining bytes.
   * 
   * @return true if there are remaining bytes, false otherwise
   */
  public boolean hasByte() {
    return currentBytes.anyʔ() || hasChar();
  }

  /**
   * Returns next character.
   * 
   * @return a character as String
   * @throws IllegalStateException
   *           if no more character is left
   */
  public String nextChar() {
    if (!hasChar())
      throw new IllegalStateException("No more character");

    String ch;
    if (ch2Bytes(currentChar).size() == currentBytes.size()) {
      ch = currentChar;
      advanceChar();
    } else {
      advanceChar();
      ch = currentChar;
      advanceChar();
    }
    return ch;
  }

  /**
   * Returns next byte.
   * 
   * @return a byte
   * @throws IllegalStateException
   *           if no more byte is left
   */
  public byte nextByte() {
    if (currentBytes.anyʔ()) {
      remainingByteNumber--;
      return currentBytes.shift();
    } else if (hasChar()) {
      advanceChar();
      remainingByteNumber--;
      return currentBytes.shift();
    }
    throw new IllegalStateException("No more byte");
  }

  /**
   * Returns next n as maximum bytes .
   * 
   * @param n
   *          maximum number of bytes
   * @return a byte array
   */
  public byte[] nextByte(int n) {
    List<Byte> bytes = newRubyArray();
    while (n > 0 && hasByte()) {
      bytes.add(nextByte());
      n--;
    }
    return ByteUtil.toArray(bytes);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof ASCII8BitUTF) {
      ASCII8BitUTF a8u = (ASCII8BitUTF) o;
      return str.equals(a8u.str);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return str.hashCode();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{" + str + "}";
  }

}