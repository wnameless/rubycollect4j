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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static net.sf.rubycollect4j.RubyIO.Mode.R;

/**
 * 
 * RubyFile is a file utility which implements parts of the methods refer to the
 * File class of Ruby.
 * 
 */
public final class RubyFile extends RubyIO {

  /**
   * Create a RubyFile by given file. Set the mode to read-only.
   * 
   * @param file
   *          a File
   * @return a RubyFile
   */
  public static RubyFile open(File file) {
    RubyFile rf = null;
    try {
      rf = new RubyFile(file, R);
    } catch (FileNotFoundException ex) {
      Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
    }
    return rf;
  }

  /**
   * Create a RubyFile by given file and mode.
   * 
   * @param file
   *          a File
   * @param mode
   *          r, rw, w, w+, a, a+
   * @return a RubyFile
   */
  public static RubyFile open(File file, String mode) {
    RubyFile rf = null;
    try {
      rf = new RubyFile(file, mode);
    } catch (FileNotFoundException ex) {
      Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
    }
    return rf;
  }

  /**
   * Create a RubyFile by given path. Set the mode to read-only.
   * 
   * @param path
   *          of a file
   * @return a RubyFile
   */
  public static RubyFile open(String path) {
    return open(new File(path));
  }

  /**
   * Create a RubyFile by given path and mode.
   * 
   * @param path
   *          of a file
   * @param mode
   *          r, rw, w, w+, a, a+
   * @return a RubyFile
   */
  public static RubyFile open(String path, String mode) {
    return open(new File(path), mode);
  }

  /**
   * Private constructor to enhance static factory methods and prevent from
   * inheritance.
   * 
   * @param file
   *          a File
   * @param mode
   *          a Mode
   * @throws FileNotFoundException
   * @throws IOException
   */
  private RubyFile(File file, Mode mode) throws FileNotFoundException,
      IOException {
    super(file, mode);
  }

  /**
   * Private constructor to enhance static factory methods and prevent from
   * inheritance.
   * 
   * @param file
   * @param mode
   * @throws FileNotFoundException
   * @throws IOException
   */
  private RubyFile(File file, String mode) throws FileNotFoundException,
      IOException {
    super(file, Mode.fromString(mode));
  }

  /**
   * Delete all given files.
   * 
   * @param files
   *          names of files
   * @return the number of deleted files
   */
  public static int delete(String... files) {
    for (String f : files) {
      File file = new File(f);
      file.delete();
    }
    return files.length;
  }

  /**
   * Check if a file existed.
   * 
   * @param path
   *          of a file
   * @return true if file existed, false otherwise
   */
  public static boolean existʔ(String path) {
    File file = new File(path);
    return file.exists();
  }

  /**
   * Equivalent to existʔ().
   * 
   * @param path
   *          of a file
   * @return true if file existed, false otherwise
   */
  public static boolean existsʔ(String path) {
    return existʔ(path);
  }

  /**
   * Generator a RubyEnumerator of lines in the file.
   * 
   * @param file
   *          a File
   * @return a RubyEnumerator
   */
  public static RubyEnumerator<String> foreach(File file) {
    return open(file).eachLine();
  }

  /**
   * Generator a RubyEnumerator of lines in the file.
   * 
   * @param path
   *          of a file
   * @return a RubyEnumerator
   */
  public static RubyEnumerator<String> foreach(String path) {
    return foreach(new File(path));
  }

}
