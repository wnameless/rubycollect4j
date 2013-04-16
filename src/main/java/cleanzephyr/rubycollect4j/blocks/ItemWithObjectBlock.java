package cleanzephyr.rubycollect4j.blocks;

public interface ItemWithObjectBlock<E, S> {

  public void yield(E item, S o);
}
