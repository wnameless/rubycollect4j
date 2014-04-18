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
import static net.sf.rubycollect4j.RubyCollections.rs;
import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.RubyString;
import net.sf.rubycollect4j.util.ASCII8BitUTF;
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

    ASCII8BitUTF a8u = new ASCII8BitUTF(str);
    RubyArray<Object> objs = newRubyArray();

    for (String template : Packer.parseTemplate(format)) {
      Directive d = Packer.parseDirective(template);
      String countStr = template.replaceFirst("^" + d, "");
      int count =
          countStr.isEmpty() ? 1 : countStr.equals("*") ? Integer.MAX_VALUE
              : rs(countStr).toI();

      String unpack;
      switch (d) {

      default: // a
        unpack = d.pack(a8u.nextByte(count));
        objs.add(unpack);
        break;

      case A:
        unpack = d.pack(a8u.nextByte(count));
        objs.add(unpack.replaceFirst("[\\s\0]+$", ""));
        break;

      case Z:
        if (count == Integer.MAX_VALUE) {
          RubyArray<Byte> bytes = newRubyArray();
          while (a8u.hasByte() && bytes.last() != Byte.valueOf((byte) '\0')) {
            bytes.add(a8u.nextByte());
          }
          unpack = d.pack(bytes);
        } else {
          unpack = d.pack(a8u.nextByte(count));
        }

        int end = unpack.indexOf('\0');
        if (end != -1)
          objs.add(unpack.substring(0, unpack.indexOf('\0')));
        else
          objs.add(unpack);
        break;

      case U:
        while (count > 0 && a8u.hasChar()) {
          objs.add(d.unpack(a8u.nextChar().getBytes()));
          count--;
        }
        continue;

      case c:
        boolean isInfinite = count == Integer.MAX_VALUE;
        if (!a8u.hasByte()) {
          if (!isInfinite)
            objs.add(null);
          break;
        }

        while (count > 0) {
          if (a8u.hasByte()) {
            objs.add(a8u.nextByte());
          } else if (!isInfinite) {
            objs.add(null);
          } else {
            break;
          }
          count--;
        }
        break;

      case B:
      case b:
        if (!a8u.hasByte()) {
          objs.add("");
          break;
        }

        RubyString strMorLSB =
            rs(ByteUtil.toBinaryString(new byte[] { a8u.nextByte() },
                d == Directive.B));

        RubyArray<String> unpackedMorLSB = newRubyArray();
        while (count > 0) {
          if (strMorLSB.anyʔ()) {
            unpackedMorLSB.add(strMorLSB.sliceǃ(0).toS());
            count--;
          } else if (a8u.hasByte()) {
            strMorLSB =
                rs(ByteUtil.toBinaryString(new byte[] { a8u.nextByte() },
                    d == Directive.B));
            unpackedMorLSB.add(strMorLSB.sliceǃ(0).toS());
            count--;
          } else {
            break;
          }
        }
        objs.add(unpackedMorLSB.join());
        break;

      case H:
      case h:
        if (!a8u.hasByte()) {
          objs.add("");
          break;
        }

        RubyString strHorLNF =
            rs(ByteUtil.toHexString(new byte[] { a8u.nextByte() },
                d == Directive.H));

        RubyArray<String> unpackedHorLNF = newRubyArray();
        while (count > 0) {
          if (strHorLNF.anyʔ()) {
            unpackedHorLNF.add(strHorLNF.sliceǃ(0).toS());
            count--;
          } else if (a8u.hasByte()) {
            strHorLNF =
                rs(ByteUtil.toHexString(new byte[] { a8u.nextByte() },
                    d == Directive.H));
            unpackedHorLNF.add(strHorLNF.sliceǃ(0).toS());
            count--;
          } else {
            break;
          }
        }
        objs.add(unpackedHorLNF.join());
        break;

      case s:
      case sb:
      case sl:
        while (count > 0) {
          if (count == Integer.MAX_VALUE)
            count = a8u.remainingByteNumber() / 2;

          if (a8u.remainingByteNumber() >= 2)
            objs.add(d.unpack(a8u.nextByte(2)));
          else
            objs.add(null);

          count--;
        }
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
            count = a8u.remainingByteNumber() / 4;

          if (a8u.remainingByteNumber() >= 4)
            objs.add(d.unpack(a8u.nextByte(4)));
          else
            objs.add(null);

          count--;
        }
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
            count = a8u.remainingByteNumber() / 8;

          if (a8u.remainingByteNumber() >= 8)
            objs.add(d.unpack(a8u.nextByte(8)));
          else
            objs.add(null);

          count--;
        }
        break;

      }
    }

    return objs;
  }

}
