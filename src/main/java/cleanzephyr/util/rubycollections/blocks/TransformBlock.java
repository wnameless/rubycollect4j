package cleanzephyr.util.rubycollections.blocks;

public interface TransformBlock<E, S> {

  public S yield(E item);
}
