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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    R("r"), RW("r+"), W("w"), WR("w+"), A("a"), AR("a+");

    private final String mode;

    private Mode(String mode) {
      this.mode = mode;
    }

    public String getMode() {
      return mode;
    }

    public static Mode getMode(String permission) {
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

  private final BufferedReader reader;
  private final BufferedWriter writer;

  public RubyIO(File file, Mode mode) throws IOException {
    switch (mode) {
    case R:
      reader = new BufferedReader(new FileReader(file));
      writer = null;
      break;
    case RW:
      reader = new BufferedReader(new FileReader(file));
      writer = new BufferedWriter(new FileWriter(file));
      break;
    case W:
      reader = null;
      writer = new BufferedWriter(new FileWriter(file));
      break;
    case WR:
      reader = new BufferedReader(new FileReader(file));
      writer = new BufferedWriter(new FileWriter(file));
      break;
    case A:
      reader = null;
      writer = new BufferedWriter(new FileWriter(file, true));
      break;
    case AR:
      reader = new BufferedReader(new FileReader(file));
      writer = new BufferedWriter(new FileWriter(file, true));
      break;
    default:
      reader = new BufferedReader(new FileReader(file));
      writer = null;
      break;
    }
  }

  public RubyEnumerator<String> eachLine() {
    if (reader == null) {
      throw new UnsupportedOperationException("IOError: not opened for reading");
    }
    return newRubyEnumerator(new EachLineIterable(reader));
  }

  public void puts(String words) {
    if (writer == null) {
      throw new UnsupportedOperationException("IOError: not opened for writing");
    }
    try {
      writer.append(words);
      writer.newLine();
      writer.flush();
    } catch (IOException ex) {
      Logger.getLogger(RubyIO.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public String read() {
    StringBuilder sb = new StringBuilder();
    String line;
    try {
      while ((line = reader.readLine()) != null) {
        sb.append(line + "\n");
      }
    } catch (IOException ex) {
      Logger.getLogger(RubyIO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return sb.toString();
  }

  public int write(String words) {
    if (writer == null) {
      throw new UnsupportedOperationException("IOError: not opened for writing");
    }
    try {
      writer.append(words);
      writer.flush();
    } catch (IOException ex) {
      Logger.getLogger(RubyIO.class.getName()).log(Level.SEVERE, null, ex);
    }
    int byteLen = 0;
    try {
      byteLen = words.getBytes("UTF-8").length;
    } catch (UnsupportedEncodingException ex) {
      Logger.getLogger(RubyIO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return byteLen;
  }

}
