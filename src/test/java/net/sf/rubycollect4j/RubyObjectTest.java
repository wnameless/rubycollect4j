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
package net.sf.rubycollect4j;

import org.junit.Before;
import org.junit.Test;

public class RubyObjectTest {

  private SendTester tester;

  @Before
  public void setUp() throws Exception {
    tester = new SendTester();
  }

  @Test
  public void testSend() {
    RubyObject.send(tester, "setBytes", Byte.MAX_VALUE, (byte) 0x00);
    RubyObject.send(tester, "setShorts", Short.MAX_VALUE, (short) 0x00);
    RubyObject.send(tester, "setIntegers", Integer.MAX_VALUE, (int) 0x00);
    RubyObject.send(tester, "setLongs", Long.MAX_VALUE, (long) 0x00);
    RubyObject.send(tester, "setFloats", Float.MAX_VALUE, (float) 0x00);
    RubyObject.send(tester, "setDoubles", Double.MAX_VALUE, (double) 0x00);
    RubyObject.send(tester, "setBooleans", Boolean.TRUE, false);
    RubyObject.send(tester, "setCharacters", Character.valueOf(' '),
        (char) 0x00);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSendException1() {
    RubyObject.send(tester, "set");
  }

  @Test(expected = RuntimeException.class)
  public void testSendException2() {
    RubyObject.send(tester, "setBytes", null, Byte.MAX_VALUE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSendException3() {
    RubyObject.send(tester, "setBytes", "", "");
  }

  private class SendTester {

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
