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

import static net.sf.rubycollect4j.RubyCollections.rs;

public class UnpackerTest {

  public static void main(String[] args) {
    System.out.println(rs("\0\32\0\32").unpack("c4c"));
    // System.out.println(rs("aaa").unpack("h2H2c"));
    // System.out.println("ABC \0".matches(".*\\p{C}"));
    // System.out.println(rs("abc \0\0abc \0").unpack("Z*Z*Z*"));
    // System.out.println(rs("hello").trS("l", "r"));
    // System.out.println(rs("hello^world").tr("\\^aeiou", "*"));
    // System.out.println(rs("hello\r\nworld").tr("\r", "*"));
    // System.out.println(rs("hello\r\nworld").tr("\\r", "*"));
    // System.out.println(rs("hello\r\nworld").tr("\\\r", "*"));
    // System.out.println(rs("X['\\b']").tr("X\\", ""));
    // System.out.println(rs("X['\\b']").tr("X-\\]", ""));
  }

}
