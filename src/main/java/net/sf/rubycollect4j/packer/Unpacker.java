/*
 *
 * Copyright 2013 Wei-Ming Wu
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
import static net.sf.rubycollect4j.RubyCollections.rh;
import static net.sf.rubycollect4j.RubyCollections.rs;
import static net.sf.rubycollect4j.packer.Directive.B;
import static net.sf.rubycollect4j.packer.Directive.D;
import static net.sf.rubycollect4j.packer.Directive.E;
import static net.sf.rubycollect4j.packer.Directive.F;
import static net.sf.rubycollect4j.packer.Directive.G;
import static net.sf.rubycollect4j.packer.Directive.H;
import static net.sf.rubycollect4j.packer.Directive.b;
import static net.sf.rubycollect4j.packer.Directive.d;
import static net.sf.rubycollect4j.packer.Directive.e;
import static net.sf.rubycollect4j.packer.Directive.f;
import static net.sf.rubycollect4j.packer.Directive.g;
import static net.sf.rubycollect4j.packer.Directive.l;
import static net.sf.rubycollect4j.packer.Directive.lb;
import static net.sf.rubycollect4j.packer.Directive.ll;
import static net.sf.rubycollect4j.packer.Directive.q;
import static net.sf.rubycollect4j.packer.Directive.qb;
import static net.sf.rubycollect4j.packer.Directive.ql;
import static net.sf.rubycollect4j.packer.Directive.s;
import static net.sf.rubycollect4j.packer.Directive.sb;
import static net.sf.rubycollect4j.packer.Directive.sl;
import static net.sf.rubycollect4j.util.ByteUtils.toBinaryString;
import static net.sf.rubycollect4j.util.ByteUtils.toHexString;

import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.RubyHash;
import net.sf.rubycollect4j.RubyString;
import net.sf.rubycollect4j.util.ASCII8BitUTF;

/**
 * 
 * {@link Unpacker} is designed to implement the
 * {@link RubyString#unpack(String)}.
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class Unpacker {

  private static final int ж = Integer.MAX_VALUE;
  private static final Byte NUL = Byte.valueOf((byte) '\0');
  private static final RubyHash<Directive, Integer> NUMBER_LENGTH_IN_BYTE =
      rh(s, 2, sb, 2, sl, 2, l, 4, lb, 4, ll, 4, F, 4, f, 4, e, 4, g, 4, q, 8,
          qb, 8, ql, 8, D, 8, d, 8, E, 8, G, 8).freeze();

  private Unpacker() {}

  /**
   * Unpacks a String into a {@link RubyArray} of String.
   * 
   * @param format
   *          a String made by Directive
   * @param str
   *          target for unpacking
   * @return {@link RubyArray}
   */
  public static RubyArray<Object> unpack(String format, String str) {
    if (!Directive.verify(format))
      throw new IllegalArgumentException("Invalid template string");

    ASCII8BitUTF a8u = new ASCII8BitUTF(str);
    RubyArray<Object> objs = newRubyArray();

    for (String template : Packer.parseTemplate(format)) {
      Directive d = Packer.parseDirective(template);
      String countStr = template.replaceFirst("^" + d, "");
      int count = countStr.isEmpty() ? 1 : //
          countStr.equals("*") ? ж : rs(countStr).toI();

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
          if (count == ж) {
            RubyArray<Byte> bytes = newRubyArray();
            while (a8u.hasNextByte() && !NUL.equals(bytes.last())) {
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
          while (count > 0 && a8u.hasNextChar()) {
            objs.add(d.unpack(a8u.nextChar().getBytes()));
            count--;
          }
          continue;

        case c:
          if (!a8u.hasNextByte()) {
            if (count != ж) objs.add(null);

            break;
          }

          while (count > 0) {
            if (a8u.hasNextByte())
              objs.add(a8u.nextByte());
            else if (count != ж)
              objs.add(null);
            else
              break;

            if (count != ж) count--;
          }
          break;

        case B:
        case b:
        case H:
        case h:
          if (!a8u.hasNextByte()) {
            objs.add("");
            break;
          }

          RubyString unpackRS = null;
          RubyArray<String> unpackRA = newRubyArray();
          while (count > 0) {
            if (unpackRS != null && unpackRS.eachChar().anyʔ()) {
              unpackRA.add(unpackRS.sliceǃ(0).toS());
              count--;
            } else if (a8u.hasNextByte()) {
              if (d == B || d == b)
                unpackRS =
                    rs(toBinaryString(new byte[] { a8u.nextByte() }, d == B));
              else
                unpackRS =
                    rs(toHexString(new byte[] { a8u.nextByte() }, d == H));

              unpackRA.add(unpackRS.sliceǃ(0).toS());
              count--;
            } else {
              break;
            }
          }
          objs.add(unpackRA.join());
          break;

        case s:
        case sb:
        case sl:
        case l:
        case lb:
        case ll:
        case F:
        case f:
        case e:
        case g:
        case q:
        case qb:
        case ql:
        case D:
        case d:
        case E:
        case G:
          int lengthInByte = NUMBER_LENGTH_IN_BYTE.get(d);
          while (count > 0) {
            if (count == ж) count = a8u.remainingByteNumber() / lengthInByte;

            if (a8u.remainingByteNumber() >= lengthInByte)
              objs.add(d.unpack(a8u.nextByte(lengthInByte)));
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
