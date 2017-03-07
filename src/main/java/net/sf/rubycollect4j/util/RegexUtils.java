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
package net.sf.rubycollect4j.util;

import net.sf.rubycollect4j.Ruby;

/**
 * 
 * {@link RegexUtils} provides functions to construct Regex strings.
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class RegexUtils {

  private RegexUtils() {}

  /**
   * Converts a glob pattern to Regex string.
   * 
   * @param pattern
   *          a glob pattern
   * @return a Regex string
   */
  public static String convertGlobToRegex(String pattern) {
    pattern = pattern.replaceAll("[\\\\\\.\\(\\)\\+\\|\\^\\$]", "\\\\$0");
    pattern = pattern.replaceAll("\\[\\\\\\^", "[^");

    pattern = Ruby.String.of(pattern).gsub("\\{[^\\}]+\\}", m -> {
      m = m.replaceAll(",", "|");
      m = "(" + m.substring(1, m.lastIndexOf('}')) + ")";
      return m;
    }).toS();
    pattern = pattern.replaceAll("\\?", ".{1}");
    pattern = pattern.replaceAll("\\*\\*/", "(.+/)?");
    pattern = pattern.replaceAll("\\*", "[^/]*");
    return pattern;
  }

}
