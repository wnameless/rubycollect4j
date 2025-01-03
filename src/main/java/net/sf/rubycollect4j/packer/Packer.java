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
package net.sf.rubycollect4j.packer;

import static java.nio.ByteOrder.nativeOrder;
import static net.sf.rubycollect4j.util.ByteUtils.toByteArray;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import net.sf.rubycollect4j.Ruby;
import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.RubyString;
import net.sf.rubycollect4j.util.ByteUtils;

/**
 * 
 * {@link Packer} is designed to implement the {@link RubyArray#pack(String)}.
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class Packer {

  private Packer() {};

  /**
   * Packs an array of Object into a binary String.
   * 
   * @param aTemplateString a String made by Directive
   * @param objs an array of Object
   * @return binary String
   */
  public static String pack(String aTemplateString, Object... objs) {
    return pack(aTemplateString, Arrays.asList(objs));
  }

  /**
   * Packs a List of Objects into a binary String.
   * 
   * @param aTemplateString a String made by Directive
   * @param objs a List of Objects
   * @return binary String
   * @throws IllegalArgumentException if template string is invalid
   * @throws IllegalArgumentException if too few arguments
   */
  public static String pack(String aTemplateString, List<?> objs) {
    if (!Directive.verify(aTemplateString))
      throw new IllegalArgumentException("Invalid template string");

    StringBuilder sb = new StringBuilder();
    List<String> templateList = parseTemplate(aTemplateString);
    if (templateList.size() > objs.size())
      throw new IllegalArgumentException("ArgumentError: too few arguments");

    RubyArray<?> items = RubyArray.copyOf(objs);
    for (String template : templateList) {
      Directive d = parseDirective(template);
      template = template.replace(d.toString(), "");
      int count = template.isEmpty() ? 1
          : template.equals("*") ? Integer.MAX_VALUE : Ruby.String.of(template).toI();

      switch (d) {

        case B:
        case b:
          RubyString binaryStr =
              Ruby.String.of(items.shift().toString()).slice(Pattern.compile("^[01]+"));
          if (binaryStr == null)
            sb.append("");
          else
            sb.append(d.pack(ByteUtils.fromBinaryString(binaryStr.toS())));

          break;

        case H:
        case h:
          RubyString hexStr =
              Ruby.String.of(items.shift().toString()).slice(Pattern.compile("^[0-9A-Fa-f]+"));
          if (hexStr == null)
            sb.append("");
          else
            sb.append(d.pack(ByteUtils.fromHexString(hexStr.toS())));

          break;

        default:
          ByteOrder bo = nativeOrder();
          if (template.isEmpty()) {
            sb.append(d.pack(toByteArray(d.cast(items.shift()), bo)));
          } else if (d == Directive.Z && template.charAt(0) == '*') {
            sb.append(d.pack(toByteArray(d.cast(items.shift()), bo)));
            sb.append("\0");
          } else if (template.charAt(0) == '*') {
            while (items.anyʔ()) {
              sb.append(d.pack(toByteArray(d.cast(items.shift()), bo)));
            }
          } else {
            if (d.isWidthAdjustable()) {
              String str = d.pack(toByteArray(d.cast(items.shift()), bo));
              if (str.length() > count) {
                sb.append(str.substring(0, count));
              } else {
                sb.append(str);
                count -= str.length();
                while (count > 0) {
                  if (d == Directive.A)
                    sb.append(" ");
                  else
                    sb.append("\0");

                  count--;
                }
              }
            } else {
              while (count > 0) {
                sb.append(d.pack(toByteArray(d.cast(items.shift()), bo)));
                count--;
              }
            }
          }

      }
    }

    return sb.toString();
  }

  static Directive parseDirective(String template) {
    if (template.length() >= 2) {
      if (Directive.lookup.get(template.substring(0, 2)) != null)
        return Directive.lookup.get(template.substring(0, 2));
    }
    return Directive.lookup.get(template.substring(0, 1));
  }

  static List<String> parseTemplate(String template) {
    return Ruby.Array.copyOf(template.split("(?!^)"))
        .sliceBefore(Ruby.Array.copyOf(Directive.values()).map(item -> item.toString()).join("|"))
        .map(item -> item.join());
  }

}
