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
package net.sf.rubycollect4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.Before;
import org.junit.Test;

public class RubyObjectTest {

  SendTester tester;

  @Before
  public void setUp() throws Exception {
    tester = new SendTester();
  }

  @Test
  public void testPrivateConstructor() throws Exception {
    Constructor<RubyObject> c = RubyObject.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(c.getModifiers()));
    c.setAccessible(true);
    c.newInstance();
  }

  @Test
  public void testSend() {
    RubyObject.send(tester, "setBytes", Byte.MAX_VALUE, (byte) 0x00);
    RubyObject.send(tester, "setShorts", Short.MAX_VALUE, (short) 0x00);
    RubyObject.send(tester, "setIntegers", Integer.MAX_VALUE, 0x00);
    RubyObject.send(tester, "setLongs", Long.MAX_VALUE, (long) 0x00);
    RubyObject.send(tester, "setFloats", Float.MAX_VALUE, (float) 0x00);
    RubyObject.send(tester, "setDoubles", Double.MAX_VALUE, (double) 0x00);
    RubyObject.send(tester, "setBooleans", Boolean.TRUE, false);
    RubyObject.send(tester, "setCharacters", Character.valueOf(' '),
        (char) 0x00);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSendException1() {
    RubyObject.send(Integer.valueOf(1), "toString", 1, 2, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSendException2() {
    RubyObject.send(Integer.valueOf(1), "compareTo");
  }

  @Test(expected = RuntimeException.class)
  public void testSendException3() {
    RubyObject.send(tester, "setBytes", null, Byte.MAX_VALUE);
  }

  @Test
  public void testIsConvertable() {
    try {
      RubyObject.send(tester, "setBytes", "", "");
      fail();
    } catch (IllegalArgumentException e) {}
    try {
      RubyObject.send(tester, "setShorts", "", "");
      fail();
    } catch (IllegalArgumentException e) {}
    try {
      RubyObject.send(tester, "setIntegers", "", "");
      fail();
    } catch (IllegalArgumentException e) {}
    try {
      RubyObject.send(tester, "setLongs", "", "");
      fail();
    } catch (IllegalArgumentException e) {}
    try {
      RubyObject.send(tester, "setFloats", "", "");
      fail();
    } catch (IllegalArgumentException e) {}
    try {
      RubyObject.send(tester, "setDoubles", "", "");
      fail();
    } catch (IllegalArgumentException e) {}
    try {
      RubyObject.send(tester, "setBooleans", "", "");
      fail();
    } catch (IllegalArgumentException e) {}
    try {
      RubyObject.send(tester, "setCharacters", "", "");
      fail();
    } catch (IllegalArgumentException e) {}
  }

  @Test
  public void testSendWithPrimitiveAndObjectByte() {
    assertEquals("byteMethod", RubyObject.send(tester, "byteMethod", (byte) 1));
    assertEquals("ByteMethod", RubyObject.send(tester, "ByteMethod", (byte) 1));
    assertEquals("ByteMethod",
        RubyObject.send(tester, "ByteMethod", Byte.valueOf((byte) 1)));
  }

  @Test
  public void testSendWithPrimitiveAndObjectShort() {
    assertEquals("shortMethod",
        RubyObject.send(tester, "shortMethod", (short) 1));
    assertEquals("ShortMethod",
        RubyObject.send(tester, "ShortMethod", (short) 1));
    assertEquals("ShortMethod",
        RubyObject.send(tester, "ShortMethod", Short.valueOf((byte) 1)));
  }

  @Test
  public void testSendWithPrimitiveAndObjectInteger() {
    assertEquals("intMethod", RubyObject.send(tester, "intMethod", 1));
    assertEquals("IntegerMethod", RubyObject.send(tester, "IntegerMethod", 1));
    assertEquals("IntegerMethod",
        RubyObject.send(tester, "IntegerMethod", Integer.valueOf((byte) 1)));
  }

  @Test
  public void testSendWithPrimitiveAndObjectLong() {
    assertEquals("longMethod", RubyObject.send(tester, "longMethod", (long) 1));
    assertEquals("LongMethod", RubyObject.send(tester, "LongMethod", (long) 1));
    assertEquals("LongMethod",
        RubyObject.send(tester, "LongMethod", Long.valueOf((byte) 1)));
  }

  @Test
  public void testSendWithPrimitiveAndObjectFloat() {
    assertEquals("floatMethod",
        RubyObject.send(tester, "floatMethod", (float) 1));
    assertEquals("FloatMethod",
        RubyObject.send(tester, "FloatMethod", (float) 1));
    assertEquals("FloatMethod",
        RubyObject.send(tester, "FloatMethod", Float.valueOf((byte) 1)));
  }

  @Test
  public void testSendWithPrimitiveAndObjectDouble() {
    assertEquals("doubleMethod",
        RubyObject.send(tester, "doubleMethod", (double) 1));
    assertEquals("DoubleMethod",
        RubyObject.send(tester, "DoubleMethod", (double) 1));
    assertEquals("DoubleMethod",
        RubyObject.send(tester, "DoubleMethod", Double.valueOf((byte) 1)));
  }

  @Test
  public void testSendWithPrimitiveAndObjectBoolean() {
    assertEquals("booleanMethod",
        RubyObject.send(tester, "booleanMethod", false));
    assertEquals("BooleanMethod",
        RubyObject.send(tester, "BooleanMethod", false));
    assertEquals("BooleanMethod",
        RubyObject.send(tester, "BooleanMethod", Boolean.FALSE));
  }

  @Test
  public void testSendWithPrimitiveAndObjectCharacter() {
    assertEquals("charMethod", RubyObject.send(tester, "charMethod", (char) 1));
    assertEquals("CharacterMethod",
        RubyObject.send(tester, "CharacterMethod", (char) 1));
    assertEquals("CharacterMethod",
        RubyObject.send(tester, "CharacterMethod", Character.valueOf('a')));
  }

  private class SendTester {

    @SuppressWarnings("unused")
    public String byteMethod(byte arg) {
      return "byteMethod";
    }

    @SuppressWarnings("unused")
    public String ByteMethod(Byte arg) {
      return "ByteMethod";
    }

    @SuppressWarnings("unused")
    public String shortMethod(short arg) {
      return "shortMethod";
    }

    @SuppressWarnings("unused")
    public String ShortMethod(Short arg) {
      return "ShortMethod";
    }

    @SuppressWarnings("unused")
    public String intMethod(int arg) {
      return "intMethod";
    }

    @SuppressWarnings("unused")
    public String IntegerMethod(Integer arg) {
      return "IntegerMethod";
    }

    @SuppressWarnings("unused")
    public String longMethod(long arg) {
      return "longMethod";
    }

    @SuppressWarnings("unused")
    public String LongMethod(Long arg) {
      return "LongMethod";
    }

    @SuppressWarnings("unused")
    public String floatMethod(float arg) {
      return "floatMethod";
    }

    @SuppressWarnings("unused")
    public String FloatMethod(Float arg) {
      return "FloatMethod";
    }

    @SuppressWarnings("unused")
    public String doubleMethod(double arg) {
      return "doubleMethod";
    }

    @SuppressWarnings("unused")
    public String DoubleMethod(Double arg) {
      return "DoubleMethod";
    }

    @SuppressWarnings("unused")
    public String booleanMethod(boolean arg) {
      return "booleanMethod";
    }

    @SuppressWarnings("unused")
    public String BooleanMethod(Boolean arg) {
      return "BooleanMethod";
    }

    @SuppressWarnings("unused")
    public String charMethod(char arg) {
      return "charMethod";
    }

    @SuppressWarnings("unused")
    public String CharacterMethod(Character arg) {
      return "CharacterMethod";
    }

    @SuppressWarnings("unused")
    public void setBytes(byte v1, Byte v2) {}

    @SuppressWarnings("unused")
    public void setShorts(short v1, Short v2) {}

    @SuppressWarnings("unused")
    public void setIntegers(int v1, Integer v2) {}

    @SuppressWarnings("unused")
    public void setLongs(long v1, Long v2) {}

    @SuppressWarnings("unused")
    public void setFloats(float v1, Float v2) {}

    @SuppressWarnings("unused")
    public void setDoubles(double v1, Double v2) {}

    @SuppressWarnings("unused")
    public void setBooleans(boolean v1, Boolean v2) {}

    @SuppressWarnings("unused")
    public void setCharacters(char v1, Character v2) {}

  }

}
