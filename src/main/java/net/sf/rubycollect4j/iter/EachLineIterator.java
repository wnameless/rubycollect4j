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
package net.sf.rubycollect4j.iter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * EachLineIterator iterates a File line by line.
 * 
 */
public final class EachLineIterator implements Iterator<String> {

  private final BufferedReader reader;
  private String line;

  /**
   * Creates an EachLineIterator.
   * 
   * @param file
   *          a File
   * @throws NullPointerException
   *           if file is null
   */
  public EachLineIterator(File file) {
    if (file == null)
      throw new NullPointerException();

    try {
      reader = new BufferedReader(new FileReader(file));
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Errno::ENOENT: No such file or directory - "
          + file.getName());
    }
    nextLine();
  }

  private void nextLine() {
    try {
      line = reader.readLine();
      if (line == null)
        reader.close();
    } catch (IOException ex) {
      Logger.getLogger(EachLineIterator.class.getName()).log(Level.SEVERE,
          null, ex);
      throw new RuntimeException(ex);
    }
  }

  @Override
  public boolean hasNext() {
    return line != null;
  }

  @Override
  public String next() {
    if (!hasNext())
      throw new NoSuchElementException();

    String next = line;
    nextLine();
    return next;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
