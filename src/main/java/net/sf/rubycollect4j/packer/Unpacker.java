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
package net.sf.rubycollect4j.packer;

import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyCollections.rs;

import java.nio.ByteBuffer;
import java.util.List;

import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.RubyString;
import net.sf.rubycollect4j.util.ByteUtil;

/**
 * 
 * Unpacker is designed to implement the RubyString#unpack.
 * 
 */
public final class Unpacker {

  private Unpacker() {}

  /**
   * Unpacks a String into a RubyArray of String.
   * 
   * @param format
   *          a String made by Directive
   * @param str
   *          target for unpacking
   * @return a RubyArray
   */
  public static RubyArray<Object> unpack(String format, String str) {
    if (!Directive.verify(format))
      throw new IllegalArgumentException("Invalid template string");

    RubyArray<Object> objs = newRubyArray();
    RubyArray<String> chars = rs(str).toA();
    RubyArray<Byte> bytes = rs(str).bytes();

    for (String template : Packer.parseTemplate(format)) {
      Directive d = Packer.parseDirective(template);
      String countStr = template.replaceFirst("^" + d, "");
      int count =
          countStr.isEmpty() ? 1 : countStr.equals("*") ? Integer.MAX_VALUE
              : rs(countStr).toI();

      String unpack;
      switch (d) {

      default: // a
        unpack = d.pack(bytes.shift(count));
        objs.add(unpack);

        chars = rs(byteList2Str(bytes)).toA();
        break;

      case A:
        unpack = d.pack(bytes.shift(count));
        objs.add(unpack.replaceFirst("[\\s\0]+$", ""));

        chars = rs(byteList2Str(bytes)).toA();
        break;

      case Z:
        if (count == Integer.MAX_VALUE) {
          int index = bytes.indexOf((byte) '\0');
          unpack =
              index == -1 ? d.pack(bytes.shift(bytes.size())) : d.pack(bytes
                  .shift(index + 1));
        } else {
          unpack = d.pack(bytes.shift(count));
        }

        int end = unpack.indexOf('\0');
        if (end != -1)
          objs.add(unpack.substring(0, unpack.indexOf('\0')));
        else
          objs.add(unpack);

        chars = rs(byteList2Str(bytes)).toA();
        break;

      case U:
        while (count > 0 && chars.anyʔ()) {
          objs.add(d.unpack(chars.shift().getBytes()));
          bytes = rs(chars.join()).bytes();
          count--;
        }
        continue;

      case c:
        boolean isInfinite = count == Integer.MAX_VALUE;
        if (chars.noneʔ()) {
          if (!isInfinite)
            objs.add(null);
          break;
        }

        RubyArray<Byte> strBytes;
        if (chars.first().codePointAt(0) < 256) {
          bytes.shift(chars.first().getBytes().length);
          strBytes =
              ra(ByteBuffer.allocate(2)
                  .putShort((short) chars.shift().codePointAt(0)).array()[1]);
        } else {
          strBytes = bytes.shift(chars.shift().getBytes().length);
        }

        while (count > 0) {
          if (strBytes.anyʔ()) {
            objs.add(strBytes.shift());
          } else if (bytes.anyʔ()) {
            if (chars.first().codePointAt(0) < 256) {
              bytes.shift(chars.first().getBytes().length);
              strBytes =
                  ra(ByteBuffer.allocate(2)
                      .putShort((short) chars.shift().codePointAt(0)).array()[1]);
            } else {
              strBytes = bytes.shift(chars.shift().getBytes().length);
            }
            objs.add(strBytes.shift());
          } else if (!isInfinite) {
            objs.add(null);
          } else {
            break;
          }
          count--;
        }
        if (strBytes.anyʔ())
          bytes.addAll(strBytes);

        chars = rs(byteList2Str(bytes)).toA();
        break;

      case B:
      case b:
        if (bytes.noneʔ()) {
          objs.add("");
          break;
        }

        int charByteNum = chars.first().getBytes().length;
        RubyString strMorLSB;
        if (chars.first().codePointAt(0) < 256) {
          charByteNum = 1;
          bytes.shift(chars.first().getBytes().length);
          strMorLSB =
              rs(
                  ByteUtil.toBinaryString(
                      ByteBuffer.allocate(2)
                          .putShort((short) chars.first().codePointAt(0))
                          .array(), d == Directive.B)).slice(8, 8);
        } else {
          strMorLSB =
              rs(ByteUtil.toBinaryString(new byte[] { bytes.shift() },
                  d == Directive.B));
        }

        RubyArray<String> unpackedMorLSB = newRubyArray();
        while (count > 0) {
          if (strMorLSB.anyʔ()) {
            unpackedMorLSB.add(strMorLSB.sliceǃ(0).toS());
            count--;
          } else if (bytes.anyʔ()) {
            charByteNum--;
            if (charByteNum == 0) {
              chars.shift();
              if (chars.first().codePointAt(0) < 256)
                charByteNum = 1;
              else
                charByteNum = chars.first().getBytes().length;
            }

            if (charByteNum == 1 && chars.first().codePointAt(0) < 256) {
              bytes.shift(chars.first().getBytes().length);
              strMorLSB =
                  rs(
                      ByteUtil.toBinaryString(
                          ByteBuffer.allocate(2)
                              .putShort((short) chars.first().codePointAt(0))
                              .array(), d == Directive.B)).slice(8, 8);
            } else {
              strMorLSB =
                  rs(ByteUtil.toBinaryString(new byte[] { bytes.shift() },
                      d == Directive.B));
            }
            unpackedMorLSB.add(strMorLSB.sliceǃ(0).toS());
            count--;
          } else {
            break;
          }
        }
        objs.add(unpackedMorLSB.join());

        chars = rs(byteList2Str(bytes)).toA();
        break;

      case H:
      case h:
        if (bytes.noneʔ()) {
          objs.add("");
          break;
        }

        charByteNum = chars.first().getBytes().length;
        RubyString strHorLNF;
        if (chars.first().codePointAt(0) < 256) {
          charByteNum = 1;
          bytes.shift(chars.first().getBytes().length);
          strHorLNF =
              rs(
                  ByteUtil.toHexString(
                      ByteBuffer.allocate(2)
                          .putShort((short) chars.first().codePointAt(0))
                          .array(), d == Directive.H)).slice(2, 2);
        } else {
          strHorLNF =
              rs(ByteUtil.toHexString(new byte[] { bytes.shift() },
                  d == Directive.H));
        }

        RubyArray<String> unpackedHorLNF = newRubyArray();
        while (count > 0) {
          if (strHorLNF.anyʔ()) {
            unpackedHorLNF.add(strHorLNF.sliceǃ(0).toS());
            count--;
          } else if (bytes.anyʔ()) {
            charByteNum--;
            if (charByteNum == 0) {
              chars.shift();
              if (chars.first().codePointAt(0) < 256)
                charByteNum = 1;
              else
                charByteNum = chars.first().getBytes().length;
            }

            if (charByteNum == 1 && chars.first().codePointAt(0) < 256) {
              bytes.shift(chars.first().getBytes().length);
              strHorLNF =
                  rs(
                      ByteUtil.toHexString(
                          ByteBuffer.allocate(2)
                              .putShort((short) chars.first().codePointAt(0))
                              .array(), d == Directive.H)).slice(2, 2);
            } else {
              strHorLNF =
                  rs(ByteUtil.toHexString(new byte[] { bytes.shift() },
                      d == Directive.H));
            }
            unpackedHorLNF.add(strHorLNF.sliceǃ(0).toS());
            count--;
          } else {
            break;
          }
        }
        objs.add(unpackedHorLNF.join());

        chars = rs(byteList2Str(bytes)).toA();
        break;

      case s:
      case sb:
      case sl:
        while (count > 0) {
          if (count == Integer.MAX_VALUE)
            count = bytes.size() / 2;

          if (bytes.size() >= 2)
            objs.add(d.unpack(bytes.shift(2)));
          else
            objs.add(null);

          count--;
        }

        chars = rs(byteList2Str(bytes)).toA();
        break;

      case l:
      case lb:
      case ll:
      case F:
      case f:
      case e:
      case g:
        while (count > 0) {
          if (count == Integer.MAX_VALUE)
            count = bytes.size() / 4;

          if (bytes.size() >= 4)
            objs.add(d.unpack(bytes.shift(4)));
          else
            objs.add(null);

          count--;
        }

        chars = rs(byteList2Str(bytes)).toA();
        break;

      case q:
      case qb:
      case ql:
      case D:
      case d:
      case E:
      case G:
        while (count > 0) {
          if (count == Integer.MAX_VALUE)
            count = bytes.size() / 8;

          if (bytes.size() >= 8)
            objs.add(d.unpack(bytes.shift(8)));
          else
            objs.add(null);

          count--;
        }

        chars = rs(byteList2Str(bytes)).toA();
        break;

      }
    }

    return objs;
  }

  private static String byteList2Str(List<Byte> bytes) {
    byte[] byteAry = new byte[bytes.size()];
    for (int i = 0; i < bytes.size(); i++) {
      byteAry[0] = bytes.get(i);
    }
    return new String(byteAry);
  }

}
