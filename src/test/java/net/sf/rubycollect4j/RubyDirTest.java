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

import org.junit.Test;

import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.junit.Assert.assertEquals;

public class RubyDirTest {

  @Test
  public void testGlob() {
    assertEquals(ra(), RubyDir.glob(""));
    assertEquals(ra("pom.xml", "README.md", "src", "target"), RubyDir.glob("*"));
    assertEquals(ra("pom.xml", "README.md", "src", "target"),
        RubyDir.glob("**"));
    assertEquals(ra("src/", "target/"), RubyDir.glob("*/"));
    assertEquals(ra("pom.xml"), RubyDir.glob("*.xml"));
    assertEquals(ra("ruby_file_exist_test.txt"),
        RubyDir.glob("src/test/resources/*exist*.txt"));
    assertEquals(12, RubyDir.glob("src/test/resources/*.txt").count());
    assertEquals(ra("src", "target"), RubyDir.glob("*[c,t]"));
  }

}
