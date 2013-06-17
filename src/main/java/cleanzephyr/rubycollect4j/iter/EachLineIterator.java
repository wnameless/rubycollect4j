package cleanzephyr.rubycollect4j.iter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class EachLineIterator implements Iterator<String> {
  private BufferedReader reader;
  private String line;

  public EachLineIterator(BufferedReader reader) {
    this.reader = reader;
    nextLine();
  }

  private void nextLine() {
    try {
      line = reader.readLine();
    } catch (IOException ex) {
      Logger.getLogger(EachLineIterator.class.getName()).log(Level.SEVERE,
          null, ex);
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
