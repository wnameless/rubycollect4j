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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
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
   * This Mode enum contains all kinds of open modes of RubyIO.
   * 
   */
  public enum Mode {

    R("r", true, false), RW("r+", true, true), W("w", false, true), WR("w+",
        true, true), A("a", false, true), AR("a+", true, true);

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
     * @throws NoSuchElementException
     *           if the permission is not matched any of the Mode
     */
    public static Mode fromString(String mode) {
      RubyHash<String, Mode> modeHash =
          Hash(ra(values()).map(
              new TransformBlock<Mode, Entry<String, Mode>>() {

                @Override
                public Entry<String, Mode> yield(Mode item) {
                  return hp(item.toString(), item);
                }

              }));

      if (modeHash.keyʔ(mode))
        return modeHash.get(mode);
      else
        throw new IllegalArgumentException(
            "ArgumentError: invalid access mode " + mode);
    }

    @Override
    public String toString() {
      return mode;
    }

  }

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
   * @return a RubyIO
   */
  public static RubyIO open(String path, String mode) {
    RubyIO io = null;
    try {
      io = new RubyIO(new File(path), Mode.fromString(mode));
    } catch (IOException ex) {
      Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
      throw new RuntimeException(ex);
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
   * Iterates a File line by line.
   * 
   * @param path
   *          of a File
   * @param block
   *          to process each line
   */
  public static void foreach(String path, Block<String> block) {
    RubyIO io = null;
    try {
      io = new RubyIO(new File(path), Mode.R);
    } catch (IOException ex) {
      Logger.getLogger(RubyIO.class.getName()).log(Level.SEVERE, null, ex);
      throw new RuntimeException(ex);
    }
    io.eachLine().each(block);
    io.close();
  }

  /**
   * Closes this IO.
   */
  public void close() {
    try {
      raFile.close();
    } catch (IOException ex) {
      Logger.getLogger(RubyIO.class.getName()).log(Level.SEVERE, null, ex);
      throw new RuntimeException(ex);
    }
  }

  /**
   * Generators a RubyEnumerator of lines in the file.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<String> eachLine() {
    if (mode.isReadable() == false) {
      close();
      throw new IllegalStateException("IOError: not opened for reading");
    }
    return newRubyEnumerator(new EachLineIterable(raFile));
  }

  /**
   * Writes a line in the file.
   * 
   * @param words
   *          to write a line
   */
  public void puts(String words) {
    if (mode.isWritable() == false) {
      close();
      throw new IllegalStateException("IOError: not opened for writing");
    }
    try {
      raFile.writeBytes(words + "\n");
    } catch (IOException ex) {
      close();
      Logger.getLogger(RubyIO.class.getName()).log(Level.SEVERE, null, ex);
      throw new RuntimeException(ex);
    }
  }

  /**
   * Reads the content of a file.
   * 
   * @return a String
   */
  public String read() {
    if (mode.isReadable() == false) {
      close();
      throw new UnsupportedOperationException("IOError: not opened for reading");
    }
    StringBuilder sb = new StringBuilder();
    String line;
    try {
      while ((line = raFile.readLine()) != null) {
        sb.append(line + "\n");
      }
    } catch (IOException ex) {
      close();
      Logger.getLogger(RubyIO.class.getName()).log(Level.SEVERE, null, ex);
      throw new RuntimeException(ex);
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
    } catch (IOException ex) {
      close();
      Logger.getLogger(RubyIO.class.getName()).log(Level.SEVERE, null, ex);
      throw new RuntimeException(ex);
    }
  }

  /**
   * Writes to the file.
   * 
   * @param words
   *          to write
   * @return the number of written bytes.
   */
  public int write(String words) {
    if (mode.isWritable() == false) {
      close();
      throw new IllegalStateException("IOError: not opened for writing");
    }
    try {
      raFile.writeBytes(words);
    } catch (IOException ex) {
      close();
      Logger.getLogger(RubyIO.class.getName()).log(Level.SEVERE, null, ex);
      throw new RuntimeException(ex);
    }
    return words.getBytes().length;
  }

  @Override
  public String toString() {
    return "RubyIO{path=" + file.getPath() + ", mode=" + mode + "}";
  }

}
