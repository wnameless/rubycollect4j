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

import static net.sf.rubycollect4j.RubyCollections.Hash;
import static net.sf.rubycollect4j.RubyCollections.hp;
import static net.sf.rubycollect4j.RubyCollections.newRubyEnumerator;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static net.sf.rubycollect4j.RubyIO.Mode.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.rubycollect4j.block.Block;
import net.sf.rubycollect4j.block.TransformBlock;
import net.sf.rubycollect4j.iter.EachLineIterable;

/**
 * 
 * RubyIO implements part of the methods refer to the IO class of Ruby.
 * 
 */
public class RubyIO {

  /**
   * 
   * This Mode enum contains all open modes of RubyIO.<br>
   * r : Read.<br>
   * r+ : Read and Write.<br>
   * w : Write.<br>
   * w+ : Write and Read.<br>
   * a : Append.<br>
   * a+ : Append and Read.
   * 
   */
  public enum Mode {

    /**
     * Read.
     */
    R("r", true, false),

    /**
     * Read and Write.
     */
    RW("r+", true, true),

    /**
     * Write.
     */
    W("w", false, true),

    /**
     * Write and Read.
     */
    WR("w+", true, true),

    /**
     * Append.
     */
    A("a", false, true),

    /**
     * Append and Read.
     */
    AR("a+", true, true);

    private static final Map<String, Mode> modeMap = Hash(
        ra(values()).map(new TransformBlock<Mode, Entry<String, Mode>>() {

          @Override
          public Entry<String, Mode> yield(Mode item) {
            return hp(item.toString(), item);
          }

        })).freeze();

    private final String mode;
    private final boolean isReadable;
    private final boolean isWritable;

    private Mode(String mode, boolean isReadable, boolean isWritable) {
      this.mode = mode;
      this.isReadable = isReadable;
      this.isWritable = isWritable;
    }

    /**
     * Checks if it is readable.
     * 
     * @return true if it allows to be read, false otherwise
     */
    public boolean isReadable() {
      return isReadable;
    }

    /**
     * Checks if it is Writable.
     * 
     * @return true if it allows to be written, false otherwise
     */
    public boolean isWritable() {
      return isWritable;
    }

    /**
     * Retrieves a Mode from a String.
     * 
     * @param mode
     *          mode in String form
     * @return a Mode
     * @throws IllegalArgumentException
     *           if the permission is not matched any of the Mode
     */
    public static Mode fromString(String mode) {
      if (modeMap.containsKey(mode))
        return modeMap.get(mode);
      else
        throw new IllegalArgumentException(
            "ArgumentError: invalid access mode " + mode);
    }

    @Override
    public String toString() {
      return mode;
    }

  }

  private static final Logger logger = Logger.getLogger(RubyIO.class.getName());

  protected final File file;
  protected final RandomAccessFile raFile;
  protected final Mode mode;

  /**
   * Creates a RubyIO by given path and mode.
   * 
   * @param path
   *          of a File
   * @param mode
   *          r, rw, w, w+, a, a+
   * @see Mode
   * @return a RubyIO
   */
  public static RubyIO open(String path, String mode) {
    RubyIO io = null;
    try {
      io = new RubyIO(new File(path), Mode.fromString(mode));
    } catch (IOException e) {
      logger.log(Level.SEVERE, null, e);
      throw new RuntimeException(e);
    }
    return io;
  }

  /**
   * Creates a RubyIO by given file. Sets the mode to read-only.
   * 
   * @param path
   *          of a file
   * @return a RubyIO
   */
  public static RubyIO open(String path) {
    return open(path, R.toString());
  }

  /**
   * Creates an RubyIO by given File and Mode.
   * 
   * @param file
   *          a File
   * @param mode
   *          a Mode
   * @see Mode
   * @throws NullPointerException
   *           if file is null
   * @throws IOException
   *           if file can't not open
   */
  public RubyIO(File file, Mode mode) throws IOException {
    if (file == null)
      throw new NullPointerException();

    this.file = file;
    switch (mode) {
    case RW:
      raFile = new RandomAccessFile(file, "rws");
      this.mode = mode;
      raFile.seek(0);
      break;
    case W:
      raFile = new RandomAccessFile(file, "rws");
      this.mode = mode;
      raFile.setLength(0);
      raFile.seek(0);
      break;
    case WR:
      raFile = new RandomAccessFile(file, "rws");
      this.mode = mode;
      raFile.setLength(0);
      raFile.seek(0);
      break;
    case A:
      raFile = new RandomAccessFile(file, "rws");
      this.mode = mode;
      raFile.seek(raFile.length());
      break;
    case AR:
      raFile = new RandomAccessFile(file, "rws");
      this.mode = mode;
      raFile.seek(raFile.length());
      break;
    default:
      raFile = new RandomAccessFile(file, "r");
      this.mode = Mode.R;
      break;
    }
  }

  /**
   * Returns a RubyEnumerator of lines in given file.
   * 
   * @param path
   *          of a File
   * @return a RubyEnumerator
   */
  public static RubyEnumerator<String> foreach(String path) {
    return newRubyEnumerator(new EachLineIterable(new File(path)));
  }

  /**
   * Iterates a file line by line.
   * 
   * @param path
   *          of a File
   * @param block
   *          to process each line
   */
  public static void foreach(String path, Block<String> block) {
    newRubyEnumerator(new EachLineIterable(new File(path))).each(block);
  }

  /**
   * Closes this IO.
   */
  public void close() {
    try {
      raFile.close();
    } catch (IOException e) {
      logger.log(Level.SEVERE, null, e);
      throw new RuntimeException(e);
    }
  }

  /**
   * Returns a RubyEnumerator of lines in the file.
   * 
   * @return a RubyEnumerator
   * @throws IllegalStateException
   *           if file is not readable
   */
  public RubyEnumerator<String> eachLine() {
    if (mode.isReadable() == false)
      throw new IllegalStateException("IOError: not opened for reading");

    return newRubyEnumerator(new EachLineIterable(file));
  }

  /**
   * Writes a line in the file.
   * 
   * @param words
   *          to write a line
   * @throws IllegalStateException
   *           if file is not writable
   */
  public void puts(String words) {
    if (mode.isWritable() == false)
      throw new IllegalStateException("IOError: not opened for writing");

    try {
      raFile.write(words.getBytes());
      raFile.writeBytes(System.getProperty("line.separator"));
    } catch (IOException e) {
      logger.log(Level.SEVERE, null, e);
      throw new RuntimeException(e);
    }
  }

  /**
   * Reads the content of a file.
   * 
   * @return a String
   * @throws IllegalStateException
   *           if file is not readable
   */
  public String read() {
    if (mode.isReadable() == false)
      throw new IllegalStateException("IOError: not opened for reading");

    StringBuilder sb = new StringBuilder();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      char[] buf = new char[1024];
      int numOfChars = 0;
      while ((numOfChars = reader.read(buf)) != -1) {
        sb.append(String.valueOf(buf, 0, numOfChars));
      }
      reader.close();
    } catch (IOException e) {
      logger.log(Level.SEVERE, null, e);
      throw new RuntimeException(e);
    }
    return sb.toString();
  }

  /**
   * Moves the cursor to certain position.
   * 
   * @param pos
   *          of the file
   */
  public void seek(long pos) {
    try {
      raFile.seek(pos);
    } catch (IOException e) {
      logger.log(Level.SEVERE, null, e);
      throw new RuntimeException(e);
    }
  }

  /**
   * Writes to the file.
   * 
   * @param words
   *          to write
   * @return the number of written bytes.
   * @throws IllegalStateException
   *           if file is not writable
   */
  public int write(String words) {
    if (mode.isWritable() == false)
      throw new IllegalStateException("IOError: not opened for writing");

    try {
      raFile.write(words.getBytes());
    } catch (IOException e) {
      logger.log(Level.SEVERE, null, e);
      throw new RuntimeException(e);
    }
    return words.getBytes().length;
  }

  @Override
  public String toString() {
    return "RubyIO{path=" + file.getPath() + ", mode=" + mode + "}";
  }

}
