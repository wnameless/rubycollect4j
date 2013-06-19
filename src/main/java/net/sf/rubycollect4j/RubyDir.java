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

import java.io.File;
import java.util.Arrays;
import java.util.List;

import net.sf.rubycollect4j.block.BooleanBlock;

import static com.google.common.collect.Lists.newArrayList;
import static net.sf.rubycollect4j.RubyCollections.ra;

/**
 * 
 * RubyDir is a directory utility which implements parts of the methods refer to
 * the Dir class of Ruby.
 * 
 */
public final class RubyDir {

  /**
   * Retrieve all paths of files of given url.
   * 
   * @param pattern
   *          of target files
   * @return a RubyArray
   */
  public static RubyArray<String> glob(String pattern) {
    if (pattern.isEmpty()) {
      return ra();
    }
    boolean recursive = pattern.contains("/**/") || pattern.startsWith("**/");
    boolean emptyRoot = false;

    String rootPath =
        ra(pattern.split("/")).takeWhile(new BooleanBlock<String>() {

          @Override
          public boolean yield(String item) {
            return !item.contains("*");
          }

        }).join("/");

    if (rootPath.isEmpty()) {
      rootPath = "./";
      emptyRoot = true;
    } else {
      rootPath += "/";
      pattern = pattern.replace(rootPath, "");
    }

    pattern = pattern.replaceAll("\\?", ".{1}");
    pattern = pattern.replaceAll("\\*\\*/", ".+/");
    pattern = pattern.replaceAll("\\*", "[^/]+");

    RubyArray<File> files = ra(traverseFolder(new File(rootPath), recursive));

    RubyArray<String> paths = ra();
    for (File f : files) {
      String path = f.getPath();
      String fPath = f.isDirectory() ? f.getPath() + "/" : f.getPath();
      if (emptyRoot) {
        path = path.replace("./", "");
        fPath = fPath.replace("./", "");
      } else {
        path = path.replace(rootPath, "");
        fPath = fPath.replace(rootPath, "");
      }
      if (path.matches("(\\.[^/]+.*|.*/\\.[^/]+.*)")) {
        continue;
      }
      if (path.matches(pattern)) {
        paths.add(path);
      } else if (fPath.matches(pattern)) {
        paths.add(fPath);
      }
    }

    return paths;
  }

  private static List<File> traverseFolder(File file, boolean recursive) {
    List<File> files = newArrayList();
    List<File> tempFiles = newArrayList(Arrays.asList(file.listFiles()));

    while (!(tempFiles.isEmpty())) {
      File item = tempFiles.remove(0);
      if (item.isDirectory() && recursive) {
        files.add(item);
        tempFiles.addAll(Arrays.asList(item.listFiles()));
      } else {
        files.add(item);
      }
    }

    return files;
  }

}
