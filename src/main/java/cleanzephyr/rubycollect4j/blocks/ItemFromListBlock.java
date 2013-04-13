package cleanzephyr.rubycollect4j.blocks;

import java.util.List;

public interface ItemFromListBlock<E> {

  public void yield(List<E> block);
}
