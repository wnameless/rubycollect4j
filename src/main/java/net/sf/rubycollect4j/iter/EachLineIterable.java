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

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Objects;

/**
 * 
 * {@link EachLineIterable} iterates a File line by line.
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class EachLineIterable implements Iterable<String> {

  private final boolean keepNewLine;

  private File file;
  private InputStream inputStream;

  /**
   * Creates an {@link EachLineIterable}.
   * 
   * @param file a File
   * @throws NullPointerException if file is null
   */
  public EachLineIterable(File file, boolean keepNewLine) {
    Objects.requireNonNull(file);

    this.keepNewLine = keepNewLine;
    this.file = file;
  }

  /**
   * Creates an {@link EachLineIterable}.
   * 
   * @param inputStream an {@link InputStream}
   * @throws NullPointerException if file is null
   */
  public EachLineIterable(InputStream inputStream, boolean keepNewLine) {
    Objects.requireNonNull(inputStream);

    this.keepNewLine = keepNewLine;
    this.inputStream = inputStream;
  }

  @Override
  public Iterator<String> iterator() {
    if (file != null)
      return new EachLineIterator(file, keepNewLine);
    else
      return new EachLineIterator(inputStream, keepNewLine);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    int index = 0;
    for (String item : this) {
      if (index == 0)
        sb.append(item);
      else
        sb.append(", " + item);
      index++;
    }
    sb.append("]");
    return sb.toString();
  }

}
