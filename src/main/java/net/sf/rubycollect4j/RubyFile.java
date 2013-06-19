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
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
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

  private final File file;

  /**
   * Create a RubyFile by given file. Set the mode to read-only.
   * 
   * @param file
   *          a File
   * @return a RubyFile
   */
  public static RubyFile open(String path, String mode) {
    RubyFile rf = null;
    try {
      rf = new RubyFile(new File(path), Mode.fromString(mode));
    } catch (FileNotFoundException ex) {
      Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
    }
    return rf;
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
  public static RubyFile open(String path) {
    return open(path, R.getMode());
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
    this.file = file;
  }

  /**
   * Find the absolute path of the file.
   * 
   * @param path
   *          of a file
   * @return the absolute path of the file
   */
  public static String absolutePath(String path) {
    return new File(path).getAbsolutePath();
  }

  /**
   * Find the name of the file.
   * 
   * @param path
   *          of a file
   * @return the basename of the path
   */
  public static String basename(String path) {
    return new File(path).getName();
  }

  /**
   * Find the name of a file.
   * 
   * @param path
   *          of a file
   * @param suffix
   *          to be removed if it's at the end of the name
   * @return the name of the file
   */
  public static String basename(String path, String suffix) {
    String name = new File(path).getName();
    if (name.lastIndexOf(suffix) + suffix.length() == name.length()) {
      return name.substring(0, name.length() - suffix.length());
    } else {
      return name;
    }
  }

  /**
   * Change the permission mode of files.
   * 
   * @param modeInt
   *          permission
   * @param files
   *          to be changed mode
   * @return number of files chenged
   */
  public static int chmod(int modeInt, String... files) {
    for (String f : files) {
      File file = new File(f);
      try {
        Class<?> fspClass =
            Class.forName("java.util.prefs.FileSystemPreferences");
        Method chmodMethod =
            fspClass.getDeclaredMethod("chmod", String.class, Integer.TYPE);
        chmodMethod.setAccessible(true);
        chmodMethod.invoke(null, file.getPath(), modeInt);
      } catch (ClassNotFoundException ex) {
        Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
      } catch (SecurityException ex) {
        Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
      } catch (NoSuchMethodException ex) {
        Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IllegalArgumentException ex) {
        Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IllegalAccessException ex) {
        Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
      } catch (InvocationTargetException ex) {
        Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return files.length;
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
   * Check if a file is a directory.
   * 
   * @param path
   *          of a file
   * @return true if file is a directory, false otherwise
   */
  public static boolean directoryʔ(String path) {
    return new File(path).isDirectory();
  }

  /**
   * Find the parent folder of the file.
   * 
   * @param path
   *          of a file
   * @return the parent folder of the file
   */
  public static String dirname(String path) {
    return new File(path).getParent();
  }

  /**
   * Check if a file is executable.
   * 
   * @param path
   *          of a file
   * @return true if the file is executable, false otherwise
   */
  public static boolean executableʔ(String path) {
    return new File(path).canExecute();
  }

  /**
   * Check if a file existed.
   * 
   * @param path
   *          of a file
   * @return true if file existed, false otherwise
   */
  public static boolean existʔ(String path) {
    return new File(path).exists();
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
   * Find the absolute path of the file.
   * 
   * @param path
   *          of a file
   * @return the absolute path of the file
   */
  public static String expandPath(String path) {
    return new File(path).getAbsolutePath();
  }

  /**
   * Find the extension name of the file.
   * 
   * @param path
   *          of a file
   * @return the extension name of the file
   */
  public static String extname(String path) {
    String name = basename(path);
    int lastDot = name.lastIndexOf('.');
    return lastDot == -1 ? "" : name.substring(name.lastIndexOf('.'));
  }

  /**
   * Check if path is a file.
   * 
   * @param path
   *          of a file
   * @return true if path is a file, false otherwise
   */
  public static boolean fileʔ(String path) {
    return new File(path).isFile();
  }

  /**
   * Check if a file is readable.
   * 
   * @param path
   *          of a file
   * @return true if the file is readable, false otherwise
   */
  public static boolean readableʔ(String path) {
    return new File(path).canRead();
  }

  /**
   * Returns null if filedoesn’t exist or has 0 in length, the size of the file
   * otherwise.
   * 
   * @param path
   *          of a file
   * @return a Long or null
   */
  public static Long sizeʔ(String path) {
    File file = new File(path);
    if (!(file.exists())) {
      return null;
    }
    RandomAccessFile raf;
    Long size = 0L;
    try {
      raf = new RandomAccessFile(file, "r");
      size = raf.length();
      raf.close();
    } catch (FileNotFoundException ex) {
      Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
    }
    return size == 0L ? null : size;
  }

  /**
   * Check if a file is writable.
   * 
   * @param path
   *          of a file
   * @return true if the file is writable, false otherwise
   */
  public static boolean writableʔ(String path) {
    return new File(path).canWrite();
  }

  /**
   * Check if the file gets 0 in length.
   * 
   * @return true if file existed and gets 0 in length, otherwise false
   */
  public static boolean zeroʔ(String path) {
    if (!(existʔ(path))) {
      return false;
    }
    return sizeʔ(path) == null;
  }

  /**
   * Find the last modified Data of the file.
   * 
   * @return the last modified Data of the file
   */
  public Date mtime() {
    return new Date(file.lastModified());
  }

  /**
   * Find the path of the fiel.
   * 
   * @return the path of the file.
   */
  public String path() {
    return file.getPath();
  }

  /**
   * Find the size of the file in bytes.
   * 
   * @return the size of the file in bytes
   */
  public long size() {
    long size = 0L;
    try {
      size = raFile.length();
    } catch (IOException ex) {
      Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
    }
    return size;
  }

  /**
   * Find the path of the file.
   * 
   * @return the path of the file.
   */
  public String toPath() {
    return file.getPath();
  }

}
