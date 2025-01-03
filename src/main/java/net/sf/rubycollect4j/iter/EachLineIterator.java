/*
 *
 * Copyright 2013 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package net.sf.rubycollect4j.iter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.rubycollect4j.util.WholeLineReader;

/**
 * 
 * {@link EachLineIterator} iterates a File line by line.
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class EachLineIterator implements Iterator<String> {

  private static final Logger logger = Logger.getLogger(EachLineIterator.class.getName());

  private final boolean keepNewLine;

  private File file;
  private InputStream inputStream;
  private WholeLineReader wlReader;
  private BufferedReader reader;
  private String line;

  /**
   * Creates an {@link EachLineIterator}.
   * 
   * @param file a File
   * @throws NullPointerException if file is null
   */
  public EachLineIterator(File file, boolean keepNewLine) {
    Objects.requireNonNull(file);

    this.keepNewLine = keepNewLine;
    this.file = file;
  }

  /**
   * Creates an {@link EachLineIterator}.
   * 
   * @param inputStream an {@link InputStream}
   * @throws NullPointerException if file is null
   */
  public EachLineIterator(InputStream inputStream, boolean keepNewLine) {
    Objects.requireNonNull(inputStream);

    this.keepNewLine = keepNewLine;
    this.inputStream = inputStream;
  }

  private void init() {
    try {
      if (file != null) {
        if (keepNewLine) {
          wlReader = new WholeLineReader(new FileReader(file));
        } else {
          reader = new BufferedReader(new FileReader(file));
        }
      } else {
        if (keepNewLine) {
          wlReader = new WholeLineReader(new InputStreamReader(inputStream));
        } else {
          reader = new BufferedReader(new InputStreamReader(inputStream));
        }
      }
    } catch (FileNotFoundException e) {
      logger.log(Level.SEVERE, null, e);
      throw new RuntimeException("Errno::ENOENT: No such file or directory - " + file.getName());
    }
    nextLine();
  }

  private String nextLine() {
    String next = line;
    try {
      if (keepNewLine) {
        line = wlReader.readLine();
      } else {
        line = reader.readLine();
      }
      if (line == null) {
        if (keepNewLine) {
          wlReader.close();
        } else {
          reader.close();
        }
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, null, e);
      throw new RuntimeException(e);
    }
    return next;
  }

  @Override
  public boolean hasNext() {
    if (reader == null && wlReader == null) init();

    return line != null;
  }

  @Override
  public String next() {
    if (!hasNext()) throw new NoSuchElementException();

    return nextLine();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
