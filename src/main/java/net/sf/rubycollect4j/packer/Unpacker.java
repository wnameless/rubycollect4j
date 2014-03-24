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

import java.util.List;

import net.sf.rubycollect4j.RubyArray;

public final class Unpacker {

  public static RubyArray<String> unpack(String format, String str) {
    if (!Directive.verify(format))
      throw new IllegalArgumentException("Invalid template string");

    RubyArray<String> strs = newRubyArray();
    RubyArray<String> chars = rs(str).chars();
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
        strs.add(unpack);

        chars = rs(byteList2Str(bytes)).chars();
        break;

      case A:
        unpack = d.pack(bytes.shift(count));
        strs.add(unpack.replaceFirst("[\\s\0]+$", ""));

        chars = rs(byteList2Str(bytes)).chars();
        break;

      case Z:
        if (count == Integer.MAX_VALUE) {
          int index = bytes.indexOf((byte) '\0');
          unpack = index == -1 ? d.pack(bytes) : d.pack(bytes.shift(index + 1));
        } else {
          unpack = d.pack(bytes.shift(count));
        }

        int end = unpack.indexOf('\0');
        if (end != -1)
          strs.add(unpack.substring(0, unpack.indexOf('\0')));
        else
          strs.add(unpack);

        chars = rs(byteList2Str(bytes)).chars();
        break;

      case U:
        if (chars.isEmpty())
          continue;

        while (count > 0 && chars.anyʔ()) {
          strs.add(d.unpack(chars.shift()));
          bytes = rs(chars.join()).bytes();
          count--;
        }
        continue;

      case c:
        if (bytes.isEmpty()) {
          strs.add(null);
          break;
        }

        while (count > 0 && bytes.anyʔ()) {
          strs.add(d.unpack(bytes.shift(1)));
          count--;
        }

        chars = rs(byteList2Str(bytes)).chars();
        break;

      case s:
      case sb:
      case sl:
        if (bytes.isEmpty()) {
          strs.add(null);
          break;
        }

        while (count > 0 && bytes.anyʔ()) {
          strs.add(d.unpack(bytes.shift(2)));
          count--;
        }

        chars = rs(byteList2Str(bytes)).chars();
        break;

      case l:
      case lb:
      case ll:
      case G:
      case F:
      case f:
      case e:
      case g:
        if (bytes.isEmpty()) {
          strs.add(null);
          break;
        }

        while (count > 0 && bytes.anyʔ()) {
          strs.add(d.unpack(bytes.shift(4)));
          count--;
        }

        chars = rs(byteList2Str(bytes)).chars();
        break;

      case q:
      case qb:
      case ql:
      case D:
      case d:
      case E:
        if (bytes.isEmpty()) {
          strs.add(null);
          break;
        }

        while (count > 0 && bytes.anyʔ()) {
          strs.add(d.unpack(bytes.shift(8)));
          count--;
        }

        chars = rs(byteList2Str(bytes)).chars();
        break;

      }

    }

    return strs;
  }

  private static String byteList2Str(List<Byte> bytes) {
    byte[] byteAry = new byte[bytes.size()];
    for (int i = 0; i < bytes.size(); i++) {
      byteAry[0] = bytes.get(i);
    }
    return new String(byteAry);
  }

}
