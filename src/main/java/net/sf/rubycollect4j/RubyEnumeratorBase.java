package net.sf.rubycollect4j;

import java.util.Iterator;

public interface RubyEnumeratorBase<E, I extends RubyEnumeratorBase<?, ?, ?>, Z extends RubyEnumerableBase<?, ?, ?>>
    extends RubyEnumerableBase<E, I, Z>, Iterable<E>, Iterator<E> {}
