package cleanzephyr.util.rubycollections;

import com.google.common.collect.ArrayListMultimap;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.reverse;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import static java.util.AbstractMap.SimpleEntry;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static com.google.common.collect.Maps.newHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Collections;
import java.util.Comparator;

public final class RubyEnumerable {

  public static interface EntryVisitor<K, V> {

    public void visit(K key, V value);
  }

  public static interface EntryBooleanVisitor<K, V> {

    public boolean visit(K key, V value);
  }

  public static interface EntryTransformVisitor<K, V, S> {

    public S visit(K key, V value);
  }

  public static interface EntryListVisitor<K, V, S> {

    public List<S> visit(K key, V value);
  }

  public static interface EntryInjectWithInitVisitor<K, V, S> {

    public S visit(S memo, Entry<K, V> item);
  }

  public static interface EntryMergeVisitor<K, V> {

    public V visit(K key, V oldval, V newval);
  }

  public static interface ItemVisitor<E> {

    public void visit(E item);
  }

  public static interface ItemWithReturnVisitor<E> {

    public E visit(E item);
  }

  public static interface ItemWithIndexVisitor<E> {

    public void visit(E item, int index);
  }

  public static interface IndexVisitor<E> {

    public void visit(int index);
  }

  public static interface BooleanVisitor<E> {

    public boolean visit(E item);
  }

  public static interface TransformVisitor<E, S> {

    public S visit(E item);
  }

  public static interface ListVisitor<E, S> {

    public List<S> visit(E item);
  }

  public static interface ItemBlockVisitor<E> {

    public void visit(List<E> block);
  }

  public static interface ItemWithObject<E> {

    public void visit(E item, Object o);
  }

  public static interface InjectVisitor<E> {

    public E visit(E memo, E item);
  }

  public static interface InjectWithInitVisitor<E, S> {

    public S visit(S memo, E item);
  }

  public static interface ReturnBlockVisitor<S> {

    public S visit();
  }

  public static <E> boolean hasAll(Iterable<E> iter) {
    boolean bool = true;
    for (E item : iter) {
      if (item == null) {
        bool = false;
      }
    }
    return bool;
  }

  public static <E> boolean hasAll(Iterable<E> iter, BooleanVisitor<E> visitor) {
    boolean bool = true;
    for (E item : iter) {
      if (visitor.visit(item) == false) {
        bool = false;
      }
    }
    return bool;
  }

  public static <E> boolean hasAny(Iterable<E> iter) {
    boolean bool = false;
    for (E item : iter) {
      if (item != null) {
        bool = true;
      }
    }
    return bool;
  }

  public static <E> boolean hasAny(Iterable<E> iter, BooleanVisitor<E> visitor) {
    boolean bool = false;
    for (E item : iter) {
      if (visitor.visit(item)) {
        bool = true;
      }
    }
    return bool;
  }

  public static <E, K> List<Entry<K, List<E>>> chunk(Iterable<E> iter, TransformVisitor<E, K> visitor) {
    Multimap<K, E> multimap = ArrayListMultimap.create();
    for (E item : iter) {
      K key = visitor.visit(item);
      multimap.put(key, item);
    }
    List<Entry<K, List<E>>> list = newArrayList();
    for (K key : multimap.keySet()) {
      list.add(new SimpleEntry<>(key, newArrayList(multimap.get(key))));
    }
    return list;
  }

  public static <E, S> List<S> collect(Iterable<E> iter, TransformVisitor<E, S> visitor) {
    List<S> list = newArrayList();
    for (E item : iter) {
      list.add(visitor.visit(item));
    }
    return list;
  }

  public static <E, S> List<S> collectConcat(Iterable<E> iter, ListVisitor<E, S> visitor) {
    List<S> list = newArrayList();
    for (E item : iter) {
      list.addAll(visitor.visit(item));
    }
    return list;
  }

  public static <E> int count(Iterable<E> iter) {
    int count = 0;
    for (E item : iter) {
      count++;
    }
    return count;
  }

  public static <E> int count(Iterable<E> iter, BooleanVisitor visitor) {
    int count = 0;
    for (E item : iter) {
      if (visitor.visit(item)) {
        count++;
      }
    }
    return count;
  }

  public static <E> void cycle(Iterable<E> iter, ItemVisitor visitor) {
    while (true) {
      for (E item : iter) {
        visitor.visit(item);
      }
    }
  }

  public static <E> void cycle(Iterable<E> iter, int cycles, ItemVisitor visitor) {
    for (int i = 0; i < cycles; i++) {
      for (E item : iter) {
        visitor.visit(item);
      }
    }
  }

  public static <E> E detect(Iterable<E> iter, E target) {
    for (E item : iter) {
      if (item.equals(target)) {
        return item;
      }
    }
    return null;
  }

  public static <E> E detect(Iterable<E> iter, BooleanVisitor visitor) {
    for (E item : iter) {
      if (visitor.visit(item)) {
        return item;
      }
    }
    return null;
  }

  public static <E> List<E> drop(Iterable<E> iter, int n) {
    if (n < 0) {
      throw new IllegalArgumentException("attempt to drop negative size");
    }
    List<E> list = newArrayList();
    int i = 0;
    for (E item : iter) {
      if (i >= n) {
        list.add(item);
      }
      i++;
    }
    return list;
  }

  public static <E> List<E> dropWhile(Iterable<E> iter, BooleanVisitor visitor) {
    List<E> list = newArrayList();
    boolean cutPoint = false;
    for (E item : iter) {
      if (visitor.visit(item) || cutPoint) {
        cutPoint = true;
        list.add(item);
      }
    }
    return list;
  }

  public static <E> void eachCons(Iterable<E> iter, int n, ItemBlockVisitor<E> visitor) {
    if (n <= 0) {
      throw new IllegalArgumentException("invalid size");
    }
    List<E> list = newArrayList(iter);
    for (int i = 0; i < list.size(); i++) {
      if (i + n <= list.size()) {
        List<E> cons = newArrayList();
        for (int j = i; j < i + n; j++) {
          cons.add(list.get(j));
        }
        visitor.visit(cons);
      }
    }
  }

  public static <E> void eachSlice(Iterable<E> iter, int n, ItemBlockVisitor<E> visitor) {
    List<E> list = newArrayList(iter);
    int blocks = list.size() % n == 0 ? list.size() / n : list.size() / n + 1;
    for (int i = 0; i < blocks; i++) {
      List<E> cons = newArrayList();
      for (int j = i * n; j < list.size() ? j < i * n + n : false; j++) {
        cons.add(list.get(j));
      }
      visitor.visit(cons);
    }
  }

  public static <E> void eachWithIndex(Iterable<E> iter, ItemWithIndexVisitor<E> visitor) {
    int i = 0;
    for (E item : iter) {
      visitor.visit(item, i);
      i++;
    }
  }

  public static <E> void eachWithObject(Iterable<E> iter, Object o, ItemWithObject<E> visitor) {
    for (E item : iter) {
      visitor.visit(item, o);
    }
  }

  public static <E> List<E> entries(Iterable<E> iter) {
    return newArrayList(iter);
  }

  public static <E> E find(Iterable<E> iter, E target) {
    for (E item : iter) {
      if (item.equals(target)) {
        return item;
      }
    }
    return null;
  }

  public static <E> E find(Iterable<E> iter, BooleanVisitor visitor) {
    for (E item : iter) {
      if (visitor.visit(item)) {
        return item;
      }
    }
    return null;
  }

  public static <E> List<E> findAll(Iterable<E> iter, BooleanVisitor visitor) {
    List<E> list = newArrayList();
    for (E item : iter) {
      if (visitor.visit(item)) {
        list.add(item);
      }
    }
    return list;
  }

  public static <E> E first(Iterable<E> iter) {
    Iterator<E> iterator = iter.iterator();
    if (iterator.hasNext()) {
      return iterator.next();
    } else {
      return null;
    }
  }

  public static <E> List<E> first(Iterable<E> iter, int n) {
    if (n < 0) {
      throw new IllegalArgumentException("attempt to take negative size");
    }
    List<E> list = newArrayList(iter);
    for (int i = 0; i < n && i < list.size(); i++) {
      list.add(list.get(i));
    }
    return list;
  }

  public static <E> Integer findIndex(Iterable<E> iter, E target) {
    int index = 0;
    for (E item : iter) {
      if (item.equals(target)) {
        return index;
      }
      index++;
    }
    return null;
  }

  public static <E> Integer findIndex(Iterable<E> iter, BooleanVisitor<E> visitor) {
    int index = 0;
    for (E item : iter) {
      if (visitor.visit(item)) {
        return index;
      }
      index++;
    }
    return null;
  }

  public static <E, S> List<S> flatMap(Iterable<E> iter, ListVisitor<E, S> visitor) {
    return collectConcat(iter, visitor);
  }

  public static <E> List<E> grep(Iterable<E> iter, String regex) {
    Pattern pattern = Pattern.compile(regex);
    List<E> list = newArrayList();
    for (E item : iter) {
      Matcher matcher = pattern.matcher(item.toString());
      if (matcher.find()) {
        list.add(item);
      }
    }
    return list;
  }

  public static <E, S> List<S> grep(Iterable<E> iter, String regex, TransformVisitor<E, S> visitor) {
    Pattern pattern = Pattern.compile(regex);
    List<S> list = newArrayList();
    for (E item : iter) {
      Matcher matcher = pattern.matcher(item.toString());
      if (matcher.find()) {
        list.add(visitor.visit(item));
      }
    }
    return list;
  }

  public static <E, K> Map<K, List<E>> groupBy(Iterable<E> iter, TransformVisitor<E, K> visitor) {
    Multimap<K, E> multimap = ArrayListMultimap.create();
    for (E item : iter) {
      K key = visitor.visit(item);
      multimap.put(key, item);
    }
    Map<K, List<E>> map = Maps.newLinkedHashMap();
    for (K key : multimap.keySet()) {
      map.put(key, newArrayList(multimap.get(key)));
    }
    return map;
  }

  public static <E> boolean include(Iterable<E> iter, E target) {
    for (E item : iter) {
      if (item.equals(target)) {
        return true;
      }
    }
    return false;
  }

  public static <E> boolean hasMember(Iterable<E> iter, E target) {
    return include(iter, target);
  }

  public static <E> E inject(Iterable<E> iter, String methodName) {
    E result = null;
    Iterator<E> iterator = iter.iterator();
    int i = 0;
    while (iterator.hasNext()) {
      if (i == 0) {
        result = iterator.next();
      } else {
        E curr = iterator.next();
        try {
          Method m = result.getClass().getDeclaredMethod(methodName, result.getClass());
          result = (E) m.invoke(result, curr);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
          Logger.getLogger(RubyEnumerable.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      i++;
    }
    return result;
  }

  public static <E> E inject(Iterable<E> iter, E init, String methodName) {
    E result = init;
    Iterator<E> iterator = iter.iterator();
    while (iterator.hasNext()) {
      E curr = iterator.next();
      try {
        Method m = result.getClass().getDeclaredMethod(methodName, result.getClass());
        result = (E) m.invoke(result, curr);
      } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        Logger.getLogger(RubyEnumerable.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return result;
  }

  public static <E> E inject(Iterable<E> iter, InjectVisitor<E> visitor) {
    E result = null;
    int i = 0;
    for (E item : iter) {
      if (i == 0) {
        result = item;
      } else {
        result = visitor.visit(result, item);
      }
      i++;
    }
    return result;
  }

  public static <E, S> S inject(Iterable<E> iter, S init, InjectWithInitVisitor<E, S> visitor) {
    for (E item : iter) {
      init = visitor.visit(init, item);
    }
    return init;
  }

  public static <E, S> List<S> map(Iterable<E> iter, TransformVisitor<E, S> visitor) {
    return collect(iter, visitor);
  }

  public static <E extends Comparable<E>> E max(Iterable<E> iter) {
    List<E> list = newArrayList(iter);
    return Collections.max(list);
  }

  public static <E> E max(Iterable<E> iter, Comparator<? super E> comp) {
    List<E> list = newArrayList(iter);
    return Collections.max(list, comp);
  }

  public static <E, S extends Comparable<S>> E maxBy(Iterable<E> iter, TransformVisitor<E, S> visitor) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(visitor.visit(item));
    }
    S maxDst = Collections.max(dst);
    return src.get(dst.indexOf(maxDst));
  }

  public static <E, S> E maxBy(Iterable<E> iter, Comparator<? super S> comp, TransformVisitor<E, S> visitor) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(visitor.visit(item));
    }
    S maxDst = Collections.max(dst, comp);
    return src.get(dst.indexOf(maxDst));
  }

  public static <E extends Comparable<E>> E min(Iterable<E> iter) {
    List<E> list = newArrayList(iter);
    return Collections.min(list);
  }

  public static <E> E min(Iterable<E> iter, Comparator<? super E> comp) {
    List<E> list = newArrayList(iter);
    return Collections.min(list, comp);
  }

  public static <E, S extends Comparable<S>> E minBy(Iterable<E> iter, TransformVisitor<E, S> visitor) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(visitor.visit(item));
    }
    S minDst = Collections.min(dst);
    return src.get(dst.indexOf(minDst));
  }

  public static <E, S> E minBy(Iterable<E> iter, Comparator<? super S> comp, TransformVisitor<E, S> visitor) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(visitor.visit(item));
    }
    S minDst = Collections.min(dst, comp);
    return src.get(dst.indexOf(minDst));
  }

  public static <E extends Comparable<E>> List<E> minmax(Iterable<E> iter) {
    List<E> list = newArrayList(iter);
    return newArrayList(Collections.min(list), Collections.max(list));
  }

  public static <E> List<E> minmax(Iterable<E> iter, Comparator<? super E> comp) {
    List<E> list = newArrayList(iter);
    return newArrayList(Collections.min(list, comp), Collections.max(list, comp));
  }

  public static <E, S extends Comparable<S>> List<E> minmaxBy(Iterable<E> iter, TransformVisitor<E, S> visitor) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(visitor.visit(item));
    }
    S minDst = Collections.min(dst);
    S maxDst = Collections.max(dst);
    return newArrayList(src.get(dst.indexOf(minDst)), src.get(dst.indexOf(maxDst)));
  }

  public static <E, S> List<E> minmaxBy(Iterable<E> iter, Comparator<? super S> comp, TransformVisitor<E, S> visitor) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(visitor.visit(item));
    }
    S minDst = Collections.min(dst, comp);
    S maxDst = Collections.max(dst, comp);
    return newArrayList(src.get(dst.indexOf(minDst)), src.get(dst.indexOf(maxDst)));
  }

  public static <E> List<List<E>> partition(Iterable<E> iter, BooleanVisitor visitor) {
    List<E> trueList = newArrayList();
    List<E> falseList = newArrayList();
    for (E item : iter) {
      if (visitor.visit(item)) {
        trueList.add(item);
      } else {
        falseList.add(item);
      }
    }
    return newArrayList(trueList, falseList);
  }

  public static <E> boolean hasNone(Iterable<E> iter) {
    boolean bool = true;
    for (E item : iter) {
      if (item != null) {
        bool = false;
      }
    }
    return bool;
  }

  public static <E> boolean hasNone(Iterable<E> iter, BooleanVisitor<E> visitor) {
    boolean bool = true;
    for (E item : iter) {
      if (visitor.visit(item)) {
        bool = false;
      }
    }
    return bool;
  }

  public static <E> boolean hasOne(Iterable<E> iter) {
    int count = 0;
    for (E item : iter) {
      if (item != null) {
        count++;
        if (count > 1) {
          return false;
        }
      }
    }
    return count == 1;
  }

  public static <E> boolean hasOne(Iterable<E> iter, BooleanVisitor<E> visitor) {
    int count = 0;
    for (E item : iter) {
      if (visitor.visit(item)) {
        count++;
        if (count > 1) {
          return false;
        }
      }
    }
    return count == 1;
  }

  public static <E> E reduce(Iterable<E> iter, String methodName) {
    return inject(iter, methodName);
  }

  public static <E> E reduce(Iterable<E> iter, E init, String methodName) {
    return inject(iter, init, methodName);
  }

  public static <E> E reduce(Iterable<E> iter, InjectVisitor<E> visitor) {
    return inject(iter, visitor);
  }

  public static <E, S> S reduce(Iterable<E> iter, S init, InjectWithInitVisitor<E, S> visitor) {
    return inject(iter, init, visitor);
  }

  public static <E> List<E> reject(Iterable<E> iter, BooleanVisitor visitor) {
    List<E> list = newArrayList();
    for (E item : iter) {
      if (!(visitor.visit(item))) {
        list.add(item);
      }
    }
    return list;
  }

  public static <E> void reverseEach(Iterable<E> iter, ItemVisitor visitor) {
    List<E> list = newArrayList(iter);
    for (E item : reverse(list)) {
      visitor.visit(item);
    }
  }

  public static <E> List<E> select(Iterable<E> iter, BooleanVisitor visitor) {
    return findAll(iter, visitor);
  }

  public static <E> List<List<E>> sliceBefore(Iterable<E> iter, String regex) {
    List<List<E>> list = newArrayList();
    Pattern pattern = Pattern.compile(regex);
    List<E> group = null;
    for (E item : iter) {
      if (group == null) {
        group = newArrayList();
        group.add(item);
      } else if (pattern.matcher(item.toString()).find()) {
        list.add(group);
        group = newArrayList();
        group.add(item);
      } else {
        group.add(item);
      }
    }
    if (group != null && !group.isEmpty()) {
      list.add(group);
    }
    return list;
  }

  public static <E> List<List<E>> sliceBefore(Iterable<E> iter, BooleanVisitor visitor) {
    List<List<E>> list = newArrayList();
    List<E> group = null;
    for (E item : iter) {
      if (group == null) {
        group = newArrayList();
        group.add(item);
      } else if (visitor.visit(item)) {
        list.add(group);
        group = newArrayList();
        group.add(item);
      } else {
        group.add(item);
      }
    }
    if (group != null && !group.isEmpty()) {
      list.add(group);
    }
    return list;
  }

  public static <E extends Comparable<E>> List<E> sort(Iterable<E> iter) {
    List<E> list = newArrayList(iter);
    Collections.sort(list);
    return list;
  }

  public static <E> List<E> sort(Iterable<E> iter, Comparator<? super E> comp) {
    List<E> list = newArrayList(iter);
    Collections.sort(list, comp);
    return list;
  }

  public static <E, S extends Comparable<S>> List<E> sortBy(Iterable<E> iter, TransformVisitor<E, S> visitor) {
    Map<S, E> map = newHashMap();
    List<E> list = newArrayList();
    for (E item : iter) {
      map.put(visitor.visit(item), item);
    }
    List<S> keys = newArrayList(map.keySet());
    Collections.sort(keys);
    for (S key : keys) {
      list.add(map.get(key));
    }
    return list;
  }

  public static <E, S> List<E> sortBy(Iterable<E> iter, Comparator<? super S> comp, TransformVisitor<E, S> visitor) {
    Map<S, E> map = newHashMap();
    List<E> list = newArrayList();
    for (E item : iter) {
      map.put(visitor.visit(item), item);
    }
    List<S> keys = newArrayList(map.keySet());
    Collections.sort(keys, comp);
    for (S key : keys) {
      list.add(map.get(key));
    }
    return list;
  }

  public static <E> List<E> take(Iterable<E> iter, int n) {
    if (n < 0) {
      throw new IllegalArgumentException("attempt to take negative size");
    }
    List<E> list = newArrayList();
    int i = 0;
    for (E item : iter) {
      if (i < n) {
        list.add(item);
      } else {
        return list;
      }
      i++;
    }
    return list;
  }

  public static <E> List<E> takeWhile(Iterable<E> iter, BooleanVisitor visitor) {
    List<E> list = newArrayList();
    for (E item : iter) {
      if (visitor.visit(item)) {
        list.add(item);
      } else {
        return list;
      }
    }
    return list;
  }

  public static <E> List<E> toA(Iterable<E> iter) {
    return newArrayList(iter);
  }
}
