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

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 
 * EachLineIterator iterates a RandomAccessFile line by line.
 * 
 */
public final class EachLineIterator implements Iterator<String> {
  private RandomAccessFile raFile;
  private String line;

  /**
   * The constructor of the EachLineIterator.
   * 
   * @param raFile
   *          a RandomAccessFile
   */
  public EachLineIterator(RandomAccessFile raFile) {
    this.raFile = checkNotNull(raFile);
    try {
      this.raFile.seek(0L);
    } catch (IOException ex) {
      Logger.getLogger(EachLineIterator.class.getName()).log(Level.SEVERE,
          null, ex);
      throw new RuntimeException(ex);
    }
    nextLine();
  }

  private void nextLine() {
    try {
      line = raFile.readLine();
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
    String next = line;
    nextLine();
    return next;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
