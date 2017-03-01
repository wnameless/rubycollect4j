/*
 *
 * Copyright 2017 Wei-Ming Wu
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

import static net.sf.rubycollect4j.RubyCollections.newRubyArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * 
 * {@link RubyLiterals} implements operators refer to the Literals of Ruby
 * language.
 * 
 * @author Wei-Ming Wu
 *
 */
public class RubyLiterals {

  private static final Logger logger =
      Logger.getLogger(RubyLiterals.class.getName());

  RubyLiterals() {}

  /**
   * Creates a regular expression Pattern.
   * 
   * @param regex
   *          regular expression
   * @return Pattern
   */
  public static Pattern qr(String regex) {
    return Pattern.compile(regex);
  }

  /**
   * Creates a {@link RubyArray} of Strings.
   * 
   * @param str
   *          words separated by spaces
   * @return {@link RubyArray}
   */
  public static RubyArray<String> qw(String str) {
    return newRubyArray(str.trim().split("\\s+"));
  }

  /**
   * Executes a system command and returns its result.
   * 
   * @param cmd
   *          to be executed
   * @return String
   * @throws RuntimeException
   *           if command is not found
   */
  public static String qx(String... cmd) {
    StringBuilder sb = new StringBuilder();

    try {
      Process proc = Runtime.getRuntime().exec(cmd);
      BufferedReader stdInput =
          new BufferedReader(new InputStreamReader(proc.getInputStream()));
      BufferedReader stdError =
          new BufferedReader(new InputStreamReader(proc.getErrorStream()));

      String s;
      while ((s = stdInput.readLine()) != null) {
        sb.append(s).append(System.getProperty("line.separator"));
      }
      while ((s = stdError.readLine()) != null) {
        sb.append(s).append(System.getProperty("line.separator"));
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, null, e);
      throw new RuntimeException(e);
    }

    return sb.toString();
  }

}
