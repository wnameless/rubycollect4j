package cleanzephyr.rubycollect4j.iter;

import java.io.RandomAccessFile;
import java.util.Iterator;

public final class EachLineIterable implements Iterable<String> {

  private final Iterator<String> iter;

  public EachLineIterable(RandomAccessFile raFile) {
    this.iter = new EachLineIterator(raFile);
  }

  @Override
  public Iterator<String> iterator() {
    return iter;
  }

}
