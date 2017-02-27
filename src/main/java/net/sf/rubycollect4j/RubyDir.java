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

import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.newRubyEnumerator;
import static net.sf.rubycollect4j.RubyCollections.ra;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * 
 * {@link RubyDir} implements parts of the methods refer to the Dir class of
 * Ruby.
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class RubyDir extends RubyEnumerable<String> {

  private final File directory;
  private final RubyArray<String> entries;
  private int position = 0;

  @Override
  protected Iterable<String> getIterable() {
    return entries;
  }

  /**
   * Creates a {@link RubyDir} by given path.
   * 
   * @param path
   *          of a file
   * @return {@link RubyDir}
   * @throws IllegalArgumentException
   *           if no such file or directory
   */
  public static RubyDir open(String path) {
    File dir = new File(path);
    if (!dir.exists() || !dir.isDirectory()) throw new IllegalArgumentException(
        "Errno::ENOENT: No such file or directory - " + path);

    return new RubyDir(dir);
  }

  private RubyDir(File directory) {
    this.directory = directory;
    entries = entries(directory.getPath());
  }

  /**
   * Deletes a directory.
   * 
   * @param path
   *          of a file
   * @return true if the directory is deleted,false otherwise
   */
  public static boolean delete(String path) {
    return new File(path).delete();
  }

  /**
   * Puts entries of a folder into a {@link RubyArray}.
   * 
   * @param path
   *          of a file
   * @return {@link RubyArray}
   */
  public static RubyArray<String> entries(String path) {
    File file = new File(path);
    return ra(file.listFiles()).map(item -> item.getName()).unshift("..")
        .unshift(".");
  }

  /**
   * Checks if the file is a directory.
   * 
   * @param path
   *          of a file
   * @return true if the file is a directory, false otherwise
   */
  public static boolean existsʔ(String path) {
    return new File(path).isDirectory();
  }

  /**
   * Checks if the file is a directory.
   * 
   * @param path
   *          of a file
   * @return true if the file is a directory, false otherwise
   */
  public static boolean existʔ(String path) {
    return new File(path).isDirectory();
  }

  /**
   * Creates a {@link RubyEnumerator} of entries of the given path.
   * 
   * @param path
   *          of a file
   * @return {@link RubyEnumerator}
   */
  public static RubyEnumerator<String> foreach(String path) {
    return newRubyEnumerator(entries(path));
  }

  /**
   * Retrieves all paths of files of given url pattern. The glob pattern is NOT
   * fully implemented yet. Be careful! <br>
   * 
   * {@literal *} Matches any file.<br>
   * {@literal **} Matches directories recursively.<br>
   * {@literal ?} Matches any one character. Equivalent to /.{1}/ in regexp.<br>
   * {@literal [set]} Matches any one character in set.
   * 
   * @param pattern
   *          of target files
   * @return {@link RubyArray}
   */
  public static RubyArray<String> glob(String pattern) {
    if (pattern.isEmpty()) return newRubyArray();

    pattern = convertWindowsPathToLinuxPath(pattern);
    boolean recursive = pattern.contains("/**/") || pattern.startsWith("**/");
    boolean emptyRoot = false;

    String rootPath =
        ra(pattern.split("/")).takeWhile(item -> !item.contains("*")).join("/");

    if (rootPath.isEmpty()) {
      rootPath = "./";
      emptyRoot = true;
    } else {
      rootPath += "/";
      pattern = pattern.replace(rootPath, "");
    }

    pattern = pattern.replaceAll("\\?", ".{1}");
    pattern = pattern.replaceAll("\\*\\*/", "(.+/)?");
    pattern = pattern.replaceAll("\\*", "[^/]*");

    RubyArray<File> files = ra(traverseFolder(new File(rootPath), recursive));

    RubyArray<String> paths = newRubyArray();
    for (File f : files) {
      String path = convertWindowsPathToLinuxPath(f.getPath());
      String fPath = f.isDirectory() ? f.getPath() + "/" : f.getPath();
      fPath = convertWindowsPathToLinuxPath(fPath);
      if (path.matches("(\\.[^/]+.*|.*/\\.[^/]+.*)")) continue;

      if (emptyRoot) {
        path = path.replace("./", "");
        fPath = fPath.replace("./", "");
      } else {
        path = path.replace(rootPath, "");
        fPath = fPath.replace(rootPath, "");
      }
      if (path.matches(pattern)) {
        paths.add(normalizePath(path));
      } else if (fPath.matches(pattern)) {
        paths.add(normalizePath(fPath));
      }
    }

    return paths;
  }

  private static String normalizePath(String path) {
    String os = System.getProperty("os.name");
    if (os.startsWith("Windows"))
      return path.replaceAll("/", "\\\\");
    else
      return path;
  }

  private static String convertWindowsPathToLinuxPath(String path) {
    String os = System.getProperty("os.name");
    if (os.startsWith("Windows"))
      return path.replaceAll("\\\\", "/");
    else
      return path;
  }

  private static List<File> traverseFolder(File file, boolean recursive) {
    List<File> files = new ArrayList<File>();
    List<File> tempFiles = new ArrayList<File>(Arrays.asList(file.listFiles()));

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
   * Finds the home directory of current user.
   * 
   * @return the home directory of current user
   */
  public static String home() {
    return System.getProperty("user.home");
  }

  /**
   * Creates a directory.
   * 
   * @param path
   *          of a file
   * @return true if the directory is created, false otherwise
   */
  public static boolean mkdir(String path) {
    return new File(path).mkdir();
  }

  /**
   * Returns the present working directory.
   * 
   * @return the present working directory
   */
  public static String pwd() {
    String pwd = new File("./").getAbsolutePath();
    pwd = pwd.substring(0, pwd.length() - 2);
    return pwd;
  }

  /**
   * Creates a {@link RubyEnumerator} of entries of this {@link RubyDir}.
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<String> each() {
    return eachEntry();
  }

  /**
   * Yields each entry to the given block.
   * 
   * @param block
   *          to yield entries
   * @return this {@link RubyDir}
   */
  @Override
  public RubyDir each(Consumer<? super String> block) {
    eachEntry(block);
    return this;
  }

  /**
   * Returns the path of this {@link RubyDir}.
   * 
   * @return the path of this {@link RubyDir}
   */
  public String path() {
    return directory.getPath();
  }

  /**
   * Returns the current position of entries.
   * 
   * @return the current position of entries
   */
  public int pos() {
    return position;
  }

  /**
   * Sets the current position of entries.
   * 
   * @param position
   *          the current index of entries to be set
   * @return the current position of entries
   */
  public int pos(int position) {
    this.position = position;
    return this.position;
  }

  /**
   * Returns the next of entries.
   * 
   * @return entry of file
   */
  public String read() {
    if (position < 0 || position >= entries.size()) return null;

    return entries.get(position++);
  }

  /**
   * Sets 0 to the position of entries.
   * 
   * @return this RubyDir
   */
  public RubyDir rewind() {
    position = 0;
    return this;
  }

  /**
   * Returns a {@link RubyDir} that entries is set to the given position.
   * 
   * @param position
   *          to be searched
   * @return {@link RubyDir} that entries is set to the given position
   */
  public RubyDir seek(int position) {
    RubyDir dir = RubyDir.open(directory.getPath());
    dir.pos(position);
    return dir;
  }

  /**
   * Returns the current position of entries.
   * 
   * @return the current position of entries
   */
  public int tell() {
    return pos();
  }

  @Override
  public String toString() {
    return "RubyDir{path=" + directory.getPath() + "}";
  }

}
