package cleanzephyr.ruby.collections.blocks;

import java.util.List;

public interface ToListBlock<E, S> {

  public List<S> yield(E item);
}