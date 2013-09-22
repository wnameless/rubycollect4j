/**
 *
 * @author Wei-Ming Wu
 *
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

import static com.google.common.base.Preconditions.checkArgument;
import static net.sf.rubycollect4j.RubyCollections.qr;
import static net.sf.rubycollect4j.RubyCollections.ra;

import java.util.List;
import java.util.regex.Matcher;

import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.block.TransformBlock;

/**
 * 
 * Packer is designed to implement the RubyArray#pack.
 * 
 */
public final class Packer {

  private Packer() {};

  public static String pack(@SuppressWarnings("rawtypes") List objs,
      String aTemplateString) {
    checkArgument(Directive.verify(aTemplateString), "Invalid template string.");
    StringBuilder sb = new StringBuilder();
    @SuppressWarnings("unchecked")
    byte[][] bytesArray = ByteUtil.toBytesArray(objs);
    List<String> templateList = parseTemplate(aTemplateString);
    int i = 0;
    for (String template : templateList) {
      Directive d = Directive.valueOf(String.valueOf(template.charAt(0)));
      if (template.length() == 1) {
        sb.append(d.pack(bytesArray[i]));
        i++;
      } else if (String.valueOf(template.charAt(1)) == "*") {
        while (i < bytesArray.length) {
          sb.append(d.pack(bytesArray[i]));
          i++;
        }
      } else {
        Matcher matcher = qr("\\d+").matcher(template);
        matcher.find();
        int count = Integer.valueOf(matcher.group());
        if (d.isWidthAdjustable()) {
          String str = d.pack(bytesArray[i]);
          if (str.length() >= count) {
            sb.append(str.substring(0, count + 1));
          } else {
            sb.append(str);
            count -= str.length();
            while (count > 0) {
              if (d == Directive.A)
                sb.append(" ");
              else
                sb.append("\\x00");
              count--;
            }
          }
          i++;
        } else {
          while (count > 0) {
            sb.append(d.pack(bytesArray[i]));
            count--;
            i++;
          }
        }
      }
    }
    return sb.toString();
  }

  private static List<String> parseTemplate(String template) {
    return ra(template.split("(?!^)")).sliceBefore(
        ra(Directive.values()).map(new TransformBlock<Directive, String>() {

          @Override
          public String yield(Directive item) {
            return item.toString();
          }

        }).join("|")).map(new TransformBlock<RubyArray<String>, String>() {

      @Override
      public String yield(RubyArray<String> item) {
        return item.join();
      }

    });
  }

  public static void main(String[] args) {
    System.out.println(pack(ra("a", "b"), "a3A"));
  }

}
