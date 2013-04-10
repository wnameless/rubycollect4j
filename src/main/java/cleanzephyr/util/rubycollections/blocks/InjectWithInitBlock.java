package cleanzephyr.util.rubycollections.blocks;

public interface InjectWithInitBlock<E, S> {

  public S yield(S memo, E item);
}
