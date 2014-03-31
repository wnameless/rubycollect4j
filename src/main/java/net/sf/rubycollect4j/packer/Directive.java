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

import static java.nio.ByteOrder.BIG_ENDIAN;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static java.nio.ByteOrder.nativeOrder;
import static net.sf.rubycollect4j.RubyCollections.Hash;
import static net.sf.rubycollect4j.RubyCollections.hp;
import static net.sf.rubycollect4j.RubyCollections.qr;
import static net.sf.rubycollect4j.RubyCollections.ra;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.rubycollect4j.block.TransformBlock;

/**
 * 
 * Directive defines available directives for RubyArray#pack.
 * 
 */
public enum Directive {

  /**
   * 8-bit char.
   */
  c(false),

  /**
   * 16-bit short, native endian.
   */
  s(false),

  /**
   * 16-bit short, big-endian. It should be used as s&gt; in a template string.
   */
  sb(false),

  /**
   * 16-bit short, little-endian. It should be used as s&lt; in a template
   * string.
   */
  sl(false),

  /**
   * 32-bit int, native endian.
   */
  l(false),

  /**
   * 32-bit int, big-endian. It should be used as l&gt; in a template string.
   */
  lb(false),

  /**
   * 32-bit int, little-endian. It should be used as l&lt; in a template string.
   */
  ll(false),

  /**
   * 64-bit long, native endian.
   */
  q(false),

  /**
   * 64-bit long, big-endian. It should be used as q&gt; in a template string.
   */
  qb(false),

  /**
   * 64-bit long, little-endian. It should be used as q&lt; in a template
   * string.
   */
  ql(false),

  /**
   * 64-bit double, native endian.
   */
  D(false),

  /**
   * 64-bit double, native endian.
   */
  d(false),

  /**
   * 64-bit double, little-endian.
   */
  E(false),

  /**
   * 64-bit double, big-endian.
   */
  G(false),

  /**
   * 32-bit float, native endian.
   */
  F(false),

  /**
   * 32-bit float, native endian.
   */
  f(false),

  /**
   * 32-bit float, little-endian.
   */
  e(false),

  /**
   * 32-bit float, big-endian.
   */
  g(false),

  /**
   * UTF-16 character.
   */
  U(false),

  /**
   * String, space padded with a count.
   */
  A(true),

  /**
   * String, null padded with a count.
   */
  a(true),

  /**
   * String, null padded with a count and null is added with *.
   */
  Z(true),

  /**
   * String, bit string (MSB first)
   */
  B(false),

  /**
   * String, bit string (LSB first)
   */
  b(false),

  /**
   * String, hex string (high nibble first)
   */
  H(false),

  /**
   * String, hex string (low nibble first)
   */
  h(false);

  public static final Map<String, Directive> lookup = Hash(
      ra(Directive.values()).map(
          new TransformBlock<Directive, Entry<String, Directive>>() {

            @Override
            public Entry<String, Directive> yield(Directive item) {
              return hp(item.toString(), item);
            }

          })).freeze();

  private final boolean widthAdjustable;

  private Directive(boolean widthAdjustable) {
    this.widthAdjustable = widthAdjustable;
  }

  /**
   * Returns true if the length of packed String is adjustable, false otherwise.
   * 
   * @return true if the length of packed String is adjustable, false otherwise
   */
  public boolean isWidthAdjustable() {
    return widthAdjustable;
  }

  public String pack(List<Byte> bytes) {
    byte[] byteAry = new byte[bytes.size()];
    for (int i = 0; i < bytes.size(); i++) {
      byteAry[i] = bytes.get(i);
    }
    return pack(byteAry);
  }

  /**
   * Packs array of byte into a binary String.
   * 
   * @param bytes
   *          array of byte
   * @return a binary String represented in hex or ASCII format
   */
  public String pack(byte[] bytes) {
    switch (this) {
    default: // c
      return ByteUtil.toASCIIs(bytes, 1);
    case s:
      return ByteUtil.toASCIIs(bytes, 2, nativeOrder());
    case sb:
      if (ByteOrder.nativeOrder() != BIG_ENDIAN)
        reverseBytes(bytes);
      return ByteUtil.toASCIIs(bytes, 2, BIG_ENDIAN);
    case sl:
      if (ByteOrder.nativeOrder() != LITTLE_ENDIAN)
        reverseBytes(bytes);
      return ByteUtil.toASCIIs(bytes, 2, LITTLE_ENDIAN);
    case l:
      return ByteUtil.toASCIIs(bytes, 4, nativeOrder());
    case lb:
      if (ByteOrder.nativeOrder() != BIG_ENDIAN)
        reverseBytes(bytes);
      return ByteUtil.toASCIIs(bytes, 4, BIG_ENDIAN);
    case ll:
      if (ByteOrder.nativeOrder() != LITTLE_ENDIAN)
        reverseBytes(bytes);
      return ByteUtil.toASCIIs(bytes, 4, LITTLE_ENDIAN);
    case q:
      return ByteUtil.toASCIIs(bytes, 8, nativeOrder());
    case qb:
      if (ByteOrder.nativeOrder() != BIG_ENDIAN)
        reverseBytes(bytes);
      return ByteUtil.toASCIIs(bytes, 8, BIG_ENDIAN);
    case ql:
      if (ByteOrder.nativeOrder() != LITTLE_ENDIAN)
        reverseBytes(bytes);
      return ByteUtil.toASCIIs(bytes, 8, LITTLE_ENDIAN);
    case D:
      return ByteUtil.toASCIIs(bytes, 8, nativeOrder());
    case d:
      return ByteUtil.toASCIIs(bytes, 8, nativeOrder());
    case E:
      if (ByteOrder.nativeOrder() != LITTLE_ENDIAN)
        reverseBytes(bytes);
      return ByteUtil.toASCIIs(bytes, 8, LITTLE_ENDIAN);
    case G:
      if (ByteOrder.nativeOrder() != BIG_ENDIAN)
        reverseBytes(bytes);
      return ByteUtil.toASCIIs(bytes, 8, BIG_ENDIAN);
    case F:
      return ByteUtil.toASCIIs(bytes, 4, nativeOrder());
    case f:
      return ByteUtil.toASCIIs(bytes, 4, nativeOrder());
    case e:
      if (ByteOrder.nativeOrder() != LITTLE_ENDIAN)
        reverseBytes(bytes);
      return ByteUtil.toASCIIs(bytes, 4, LITTLE_ENDIAN);
    case g:
      if (ByteOrder.nativeOrder() != BIG_ENDIAN)
        reverseBytes(bytes);
      return ByteUtil.toASCIIs(bytes, 4, BIG_ENDIAN);
    case U:
      if (ByteOrder.nativeOrder() != BIG_ENDIAN)
        reverseBytes(bytes);
      return ByteUtil.toUTF(bytes);
    case A:
      return new String(bytes);
    case a:
      return new String(bytes);
    case Z:
      return new String(bytes);
    case B:
      return new String(bytes);
    case b:
      byte[] msb = new byte[bytes.length];
      for (int i = 0; i < msb.length; i++) {
        msb[i] =
            (byte) Integer.parseInt(
                ByteUtil.toBinaryString(new byte[] { bytes[i] }, false), 2);
      }
      return new String(msb);
    case H:
      return new String(bytes);
    case h:
      byte[] lnf = new byte[bytes.length];
      for (int i = 0; i < lnf.length; i++) {
        lnf[i] =
            (byte) Integer.parseInt(
                ByteUtil.toHexString(new byte[] { bytes[i] }, false), 16);
      }
      return new String(lnf);
    }
  }

  /**
   * Adaptor for #unpack(byte[] bytes).
   * 
   * @param bytes
   *          a list of byte
   * @return a String
   */
  public Object unpack(List<Byte> bytes) {
    byte[] byteAry = new byte[bytes.size()];
    for (int i = 0; i < bytes.size(); i++) {
      byteAry[i] = bytes.get(i);
    }
    return unpack(byteAry);
  }

  /**
   * Unpacks array of byte into a String.
   * 
   * @param bytes
   *          array of byte
   * @return a String
   */
  public Object unpack(byte[] bytes) {
    switch (this) {
    default: // c
      return bytes[0];
    case U:
      return new String(bytes).codePointAt(0);
    case s:
      return ByteBuffer.wrap(bytes).order(nativeOrder()).getShort();
    case sb:
      return ByteBuffer.wrap(bytes).order(BIG_ENDIAN).getShort();
    case sl:
      return ByteBuffer.wrap(bytes).order(LITTLE_ENDIAN).getShort();
    case l:
      return ByteBuffer.wrap(bytes).order(nativeOrder()).getInt();
    case lb:
      return ByteBuffer.wrap(bytes).order(BIG_ENDIAN).getInt();
    case ll:
      return ByteBuffer.wrap(bytes).order(LITTLE_ENDIAN).getInt();
    case q:
      return ByteBuffer.wrap(bytes).order(nativeOrder()).getLong();
    case qb:
      return ByteBuffer.wrap(bytes).order(BIG_ENDIAN).getLong();
    case ql:
      return ByteBuffer.wrap(bytes).order(LITTLE_ENDIAN).getLong();
    case D:
      return ByteBuffer.wrap(bytes).order(nativeOrder()).getDouble();
    case d:
      return ByteBuffer.wrap(bytes).order(nativeOrder()).getDouble();
    case E:
      return ByteBuffer.wrap(bytes).order(LITTLE_ENDIAN).getDouble();
    case G:
      return ByteBuffer.wrap(bytes).order(BIG_ENDIAN).getDouble();
    case F:
      return ByteBuffer.wrap(bytes).order(nativeOrder()).getFloat();
    case f:
      return ByteBuffer.wrap(bytes).order(nativeOrder()).getFloat();
    case e:
      return ByteBuffer.wrap(bytes).order(LITTLE_ENDIAN).getFloat();
    case g:
      return ByteBuffer.wrap(bytes).order(BIG_ENDIAN).getFloat();
    case B:
      return ByteUtil.toBinaryString(bytes, true);
    case b:
      return ByteUtil.toBinaryString(bytes, false);
    case H:
      return ByteUtil.toHexString(bytes, true);
    case h:
      return ByteUtil.toHexString(bytes, false);
    }
  }

  private void reverseBytes(byte[] bytes) {
    for (int i = 0; i < bytes.length / 2; i++) {
      byte temp = bytes[i];
      bytes[i] = bytes[bytes.length - 1 - i];
      bytes[bytes.length - 1 - i] = temp;
    }
  }

  /**
   * Casts an Object to corresponding Byte, Short, Integer or Long if the Object
   * is a Number, Boolean or Character.
   * 
   * @param o
   *          any Object
   * @return a cast Object
   */
  @SuppressWarnings("incomplete-switch")
  public Object cast(Object o) {
    switch (this) {
    case c:
      if (o instanceof Number)
        return ((Number) o).byteValue();
      if (o instanceof Boolean)
        return ((Boolean) o) ? (byte) 1 : (byte) 0;
      if (o instanceof Character)
        return (byte) ((Character) o).charValue();
      break;
    case s:
    case sb:
    case sl:
      if (o instanceof Number)
        return ((Number) o).shortValue();
      if (o instanceof Boolean)
        return ((Boolean) o) ? (short) 1 : (short) 0;
      if (o instanceof Character)
        return (short) ((Character) o).charValue();
      break;
    case l:
    case lb:
    case ll:
    case U:
      if (o instanceof Number)
        return ((Number) o).intValue();
      if (o instanceof Boolean)
        return ((Boolean) o) ? 1 : 0;
      if (o instanceof Character)
        return (int) ((Character) o).charValue();
      break;
    case q:
    case qb:
    case ql:
      if (o instanceof Number)
        return ((Number) o).longValue();
      if (o instanceof Boolean)
        return ((Boolean) o) ? 1L : 0L;
      if (o instanceof Character)
        return (long) ((Character) o).charValue();
      break;
    case D:
    case d:
    case E:
    case G:
      if (o instanceof Number)
        return ((Number) o).doubleValue();
      if (o instanceof Boolean)
        return ((Boolean) o) ? 1.0 : 0.0;
      if (o instanceof Character)
        return (double) ((Character) o).charValue();
      break;
    case F:
    case f:
    case e:
    case g:
      if (o instanceof Number)
        return ((Number) o).floatValue();
      if (o instanceof Boolean)
        return ((Boolean) o) ? 1.0f : 0.0f;
      if (o instanceof Character)
        return (float) ((Character) o).charValue();
      break;
    }
    return o;
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
        "(("
            + ra(Directive.values()).map(
                new TransformBlock<Directive, String>() {

                  @Override
                  public String yield(Directive item) {
                    return item.toString();
                  }

                }).join("|") + ")(([1-9]\\d*)?\\*?)?)+").matcher(template)
        .matches();
  }

  @Override
  public String toString() {
    switch (this) {
    case sb:
      return "s>";
    case sl:
      return "s<";
    case lb:
      return "l>";
    case ll:
      return "l<";
    case qb:
      return "q>";
    case ql:
      return "q<";
    default:
      return super.toString();
    }
  }

}