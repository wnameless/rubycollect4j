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

import static net.sf.rubycollect4j.RubyCollections.qr;
import static net.sf.rubycollect4j.RubyCollections.ra;
import net.sf.rubycollect4j.block.TransformBlock;

/**
 * 
 * Directive defines available symbols for RubyArray#pack.
 * 
 */
public enum Directive {

  c(false), s(false), l(false), q(false), D(false), d(false), F(false),
  f(false), A(true), a(true), Z(true);

  private final boolean widthAdjustable;

  private Directive(boolean widthAdjustable) {
    this.widthAdjustable = widthAdjustable;
  }

  public boolean isWidthAdjustable() {
    return widthAdjustable;
  }

  public String pack(byte[] bytes) {
    switch (this) {
    case c:
      return ByteUtil.toASCII(bytes, 1);
    case s:
      return ByteUtil.toASCII(bytes, 2);
    case l:
      return ByteUtil.toASCII(bytes, 4);
    case q:
      return ByteUtil.toASCII(bytes, 8);
    case D:
      return ByteUtil.toASCII(bytes, 8);
    case d:
      return ByteUtil.toASCII(bytes, 8);
    case F:
      return ByteUtil.toASCII(bytes, 4);
    case f:
      return ByteUtil.toASCII(bytes, 4);
    case A:
      return new String(bytes);
    case a:
      return new String(bytes);
    case Z:
      return new String(bytes);
    }
    return "";
  }

  /**
   * Verifies a template string.
   * 
   * @param template
   *          a template string
   * @return true if template is valid, otherwise false
   */
  public static boolean verify(String template) {
    return qr(
        "(["
            + ra(Directive.values()).map(
                new TransformBlock<Directive, String>() {

                  @Override
                  public String yield(Directive item) {
                    return item.toString();
                  }

                }).join() + "](\\*|[1-9]|[1-9]\\d+)?)+").matcher(template)
        .matches();
  }

  public static void main(String[] args) {
    System.out.println(c.pack(ByteUtil.toBytesArray(66, 67, 127)[0]));
  }

}
