package cleanzephyr.rubycollect4j.iter;

import java.io.BufferedReader;
import java.util.Iterator;

public final class EachLineIterable implements Iterable<String> {

  private final Iterator<String> iter;

  public EachLineIterable(BufferedReader reader) {
    this.iter = new EachLineIterator(reader);
  }

  @Override
  public Iterator<String> iterator() {
    return iter;
  }

}
