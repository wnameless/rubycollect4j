package cleanzephyr.rubycollect4j.blocks;

public interface ItemTransformBlock<E, S> {

  public S yield(E item);
}
