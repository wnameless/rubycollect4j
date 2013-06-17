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
package cleanzephyr.rubycollect4j;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import cleanzephyr.rubycollect4j.block.TransformBlock;
import cleanzephyr.rubycollect4j.iter.EachLineIterable;

import static cleanzephyr.rubycollect4j.RubyCollections.Hash;
import static cleanzephyr.rubycollect4j.RubyCollections.hp;
import static cleanzephyr.rubycollect4j.RubyCollections.ra;
import static cleanzephyr.rubycollect4j.RubyEnumerator.newRubyEnumerator;

public class RubyIO {

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

    public String getMode() {
      return mode;
    }

    public boolean isReadable() {
      return isReadable;
    }

    public boolean isWritable() {
      return isWritable;
    }

    public static Mode fromString(String permission) {
      RubyHash<String, Mode> modeHash =
          Hash(ra(values()).map(
              new TransformBlock<Mode, Entry<String, Mode>>() {

                @Override
                public Entry<String, Mode> yield(Mode item) {
                  return hp(item.getMode(), item);
                }

              }));
      if (modeHash.keyʔ(permission)) {
        return modeHash.get(permission);
      } else {
        throw new NoSuchElementException();
      }
    }

  }

  private final RandomAccessFile raFile;
  private final Mode mode;

  public RubyIO(File file, Mode mode) throws IOException {
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

  public void close() {
    try {
      raFile.close();
    } catch (IOException ex) {
      Logger.getLogger(RubyIO.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public RubyEnumerator<String> eachLine() {
    if (mode.isReadable() == false) {
      throw new UnsupportedOperationException("IOError: not opened for reading");
    }
    return newRubyEnumerator(new EachLineIterable(raFile));
  }

  public void puts(String words) {
    if (mode.isWritable() == false) {
      throw new UnsupportedOperationException("IOError: not opened for writing");
    }
    try {
      raFile.writeBytes(words + "\n");
    } catch (IOException ex) {
      Logger.getLogger(RubyIO.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public String read() {
    if (mode.isReadable() == false) {
      throw new UnsupportedOperationException("IOError: not opened for reading");
    }
    StringBuilder sb = new StringBuilder();
    String line;
    try {
      while ((line = raFile.readLine()) != null) {
        sb.append(line + "\n");
      }
    } catch (IOException ex) {
      Logger.getLogger(RubyIO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return sb.toString();
  }

  public void seek(long pos) {
    try {
      raFile.seek(pos);
    } catch (IOException ex) {
      Logger.getLogger(RubyIO.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public int write(String words) {
    if (mode.isWritable() == false) {
      throw new UnsupportedOperationException("IOError: not opened for writing");
    }
    try {
      raFile.writeBytes(words);
    } catch (IOException ex) {
      Logger.getLogger(RubyIO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return words.getBytes().length;
  }

}
