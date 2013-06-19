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
import net.sf.rubycollect4j.block.TransformBlock;

import static com.google.common.collect.Lists.newArrayList;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyEnumerator.newRubyEnumerator;

/**
 * 
 * RubyDir is a directory utility which implements parts of the methods refer to
 * the Dir class of Ruby.
 * 
 */
public final class RubyDir {

  /**
   * Delete a directory.
   * 
   * @param path
   *          of a file
   * @return true if the directory is deleted,false otherwise
   */
  public static boolean delete(String path) {
    return new File(path).delete();
  }

  /**
   * Store a entries of a folder into a RubyArray.
   * 
   * @param path
   *          of a file
   * @return a RubyArray
   */
  public static RubyArray<String> entries(String path) {
    File file = new File(path);
    return ra(file.listFiles()).map(new TransformBlock<File, String>() {

      @Override
      public String yield(File item) {
        return item.getName();
      }

    }).unshift("..").unshift(".");
  }

  /**
   * Check if a file is a directory.
   * 
   * @param path
   *          of a file
   * @return true if the file is a directory, false otherwise
   */
  public static boolean existsʔ(String path) {
    return new File(path).isDirectory();
  }

  /**
   * Check if a file is a directory.
   * 
   * @param path
   *          of a file
   * @return true if the file is a directory, false otherwise
   */
  public static boolean existʔ(String path) {
    return new File(path).isDirectory();
  }

  /**
   * Create a RubyEnumerator of entries of a file of the path.
   * 
   * @param path
   *          of a file
   * @return a RubyEnumerator
   */
  public static RubyEnumerator<String> foreach(String path) {
    return newRubyEnumerator(entries(path));
  }

  /**
   * Retrieve all paths of files of given url pattern. The glob pattern is NOT
   * fully implemented yet. Be careful!
   * 
   * * Matches any file. <br/>
   * ** Matches directories recursively.<br/>
   * ? Matches any one character. Equivalent to /.{1}/ in regexp. <br/>
   * [set] Matches any one character in set.<br/>
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
    pattern = pattern.replaceAll("\\*", "[^/]*");

    System.out.println(pattern);
    RubyArray<File> files = ra(traverseFolder(new File(rootPath), recursive));

    RubyArray<String> paths = ra();
    for (File f : files) {
      String path = f.getPath();
      String fPath = f.isDirectory() ? f.getPath() + "/" : f.getPath();
      if (path.matches("(\\.[^/]+.*|.*/\\.[^/]+.*)")) {
        continue;
      }
      if (emptyRoot) {
        path = path.replace("./", "");
        fPath = fPath.replace("./", "");
      } else {
        path = path.replace(rootPath, "");
        fPath = fPath.replace(rootPath, "");
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

  /**
   * Find the home directory of current user.
   * 
   * @return the home directory of current user
   */
  public static String home() {
    return System.getProperty("user.home");
  }

  /**
   * Make a directory.
   * 
   * @param path
   *          of a file
   * @return true if the directory is created, false otherwise
   */
  public static boolean mkdir(String path) {
    return new File(path).mkdir();
  }

  /**
   * Find the present working directory.
   * 
   * @return the present working directory
   */
  public static String pwd() {
    String pwd = new File("./").getAbsolutePath();
    pwd = pwd.substring(0, pwd.length() - 2);
    return pwd;
  }

}
