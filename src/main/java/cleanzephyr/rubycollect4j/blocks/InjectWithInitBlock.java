package cleanzephyr.rubycollect4j.blocks;

public interface InjectWithInitBlock<E, S> {

  public S yield(S memo, E item);
}
