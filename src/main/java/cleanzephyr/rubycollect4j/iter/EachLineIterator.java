package cleanzephyr.rubycollect4j.iter;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class EachLineIterator implements Iterator<String> {
  private RandomAccessFile raFile;
  private String line;

  public EachLineIterator(RandomAccessFile raFile) {
    this.raFile = raFile;
    nextLine();
  }

  private void nextLine() {
    try {
      line = raFile.readLine();
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
