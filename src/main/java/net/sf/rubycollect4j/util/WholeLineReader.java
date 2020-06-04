/*
 *
 * Copyright 2020 Wei-Ming Wu
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
package net.sf.rubycollect4j.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * 
 * The major difference between a {@link WholeLineReader} and a
 * {@link BufferedReader} is that {@link WholeLineReader} read lines with its
 * line-termination characters, such as a line feed ({@code
 * '\n'}), a carriage return ({@code '\r'}), or a carriage return followed by a
 * linefeed ({@code "\r\n"}).
 * 
 * @author Wei-Ming Wu
 *
 */
public final class WholeLineReader implements Closeable {

  private final Readable readable;
  private final Reader reader;
  private final CharBuffer charBuff = CharBuffer.allocate(0x800);
  private final char[] buff = charBuff.array();
  private final Queue<String> lines = new LinkedList<>();

  private StringBuilder line = new StringBuilder();
  private boolean peekReturn = false;

  /**
   * Creates a {@link WholeLineReader} that will read lines with its
   * line-termination characters from the given {@code Readable}.
   */
  public WholeLineReader(Readable readable) {
    Objects.requireNonNull(readable);

    this.readable = readable;
    this.reader = (readable instanceof Reader) ? (Reader) readable : null;
  }

  /**
   * Reads a line of text. A line is considered to be terminated by any one of a
   * line feed ('\n'), a carriage return ('\r'), a carriage return followed
   * immediately by a line feed, or by reaching the end-of-file (EOF).
   *
   * @return A String containing the contents of the line, including any
   *         line-termination characters, or null if the end of the stream has
   *         been reached without reading any characters
   * @throws IOException
   *           If an I/O error occurs
   */
  public String readLine() throws IOException {
    while (lines.peek() == null) {
      charBuff.clear();
      int read = (reader != null) ? reader.read(buff, 0, buff.length)
          : readable.read(charBuff);
      if (read == -1) {
        finish();
        break;
      }
      add(buff, read);
    }
    return lines.poll();
  }

  @Override
  public void close() throws IOException {
    if (reader != null) reader.close();
  }

  private void add(char[] cbuf, int len) {
    int pos = 0;
    if (peekReturn && len > 0) {
      if (finishLine(cbuf[pos] == '\n')) {
        pos++;
      }
    }

    int start = pos;
    for (int end = len; pos < end; pos++) {
      switch (cbuf[pos]) {
        case '\r':
          line.append(cbuf, start, pos - start);
          peekReturn = true;
          if (pos + 1 < end) {
            if (finishLine(cbuf[pos + 1] == '\n')) {
              pos++;
            }
          }
          start = pos + 1;
          break;

        case '\n':
          line.append(cbuf, start, pos - start);
          finishLine(true);
          start = pos + 1;
          break;

        default:
      }
    }
    line.append(cbuf, start, len - start);
  }

  private boolean finishLine(boolean peekNewline) {
    String separator =
        peekReturn ? (peekNewline ? "\r\n" : "\r") : (peekNewline ? "\n" : "");
    lines.add(line.toString() + separator);
    line = new StringBuilder();
    peekReturn = false;
    return peekNewline;
  }

  private void finish() {
    if (peekReturn || line.length() > 0) {
      finishLine(false);
    }
  }

}
