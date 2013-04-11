package cleanzephyr.ruby.collections.blocks;

import java.util.List;

public interface ItemToListBlock<E, S> {

  public List<S> yield(E item);
}