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

public final class ASCII8BitUTF {

  private final String str;
  private final int totalByteNum;
  private int remainByteNum;
  private RubyArray<String> chars;
  private String currentChar;
  private RubyArray<Byte> currentBytes = newRubyArray();

  public ASCII8BitUTF(String str) {
    this.str = str;
    chars = rs(str).toA();
    advanceChar();
    int total = 0;
    while (hasByte()) {
      nextByte();
      total++;
    }
    totalByteNum = total;
    remainByteNum = totalByteNum;
    reset();
  }

  public int totalByteNum() {
    return totalByteNum;
  }

  public int remainByteNum() {
    return remainByteNum;
  }

  public void reset() {
    chars = rs(str).toA();
    remainByteNum = totalByteNum;
    advanceChar();
  }

  public void advanceChar() {
    currentChar = chars.shift();
    for (@SuppressWarnings("unused")
    Byte b : currentBytes) {
      remainByteNum--;
    }
    currentBytes = ch2Bytes(currentChar);
  }

  private RubyArray<Byte> ch2Bytes(String ch) {
    if (ch == null)
      return ra();

    if (ch.codePointAt(0) < 256)
      return ra(
          ByteUtil.toList(ByteBuffer.allocate(2)
              .putShort((short) ch.codePointAt(0)).array())).last(1);
    else
      return ra(ByteUtil.toList(ch.getBytes()));
  }

  public boolean hasChar() {
    return (currentChar != null && currentChar.getBytes().length == currentBytes
        .size()) || chars.anyʔ();
  }

  public boolean hasByte() {
    return currentBytes.anyʔ() || hasChar();
  }

  public String nextChar() {
    if (!hasChar())
      throw new IllegalStateException();

    String ch;
    if (currentChar.getBytes().length == currentBytes.size()) {
      ch = currentChar;
      advanceChar();
    } else {
      advanceChar();
      ch = currentChar;
      advanceChar();
    }
    return ch;
  }

  public List<Byte> nextByte(int n) {
    List<Byte> bytes = newRubyArray();
    while (n > 0 && hasByte()) {
      bytes.add(nextByte());
      n--;
    }
    return bytes;
  }

  public byte nextByte() {
    if (currentBytes.anyʔ()) {
      remainByteNum--;
      return currentBytes.shift();
    } else if (hasChar()) {
      advanceChar();
      remainByteNum--;
      return currentBytes.shift();
    }
    throw new IllegalStateException();
  }

}
