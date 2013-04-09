package cleanzephyr.util.rubycollections;

import com.google.common.collect.ArrayListMultimap;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;
import com.google.common.collect.Multimap;
import static java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public final class RubyHash<K, V> implements Map<K, V> {

  private final Map<K, V> map;
  private V defaultValue;

  public RubyHash() {
    map = newLinkedHashMap();
  }

  public RubyHash(Map<K, V> map) {
    this.map = newLinkedHashMap(map);
  }

  public RubyHash<K, V> put(Entry<K, V> entry) {
    map.put(entry.getKey(), entry.getValue());
    return this;
  }

  public RubyHash<K, V> put(Entry<K, V>... entries) {
    for (Entry<K, V> entry : entries) {
      map.put(entry.getKey(), entry.getValue());
    }
    return this;
  }

  // Ruby Hash methods
  public Entry<K, V> assoc(K key) {
    if (map.containsKey(key)) {
      return new SimpleEntry(key, map.get(key));
    } else {
      return null;
    }
  }

  public V setDefault(V defaultValue) {
    this.defaultValue = defaultValue;
    return this.defaultValue;
  }

  public V getDefault() {
    return defaultValue;
  }

  public V delete(K key) {
    V removedItem = map.remove(key);
    if (removedItem == null && defaultValue != null) {
      return defaultValue;
    } else {
      return removedItem;
    }
  }

  public RubyHash<K, V> deleteIf(RubyEnumerable.EntryBooleanVisitor<K, V> visitor) {
    for (Entry<K, V> item : map.entrySet()) {
      if (visitor.visit(item.getKey(), item.getValue())) {
        map.remove(item.getKey());
      }
    }
    return this;
  }

  public RubyHash<K, V> each(RubyEnumerable.EntryVisitor<K, V> visitor) {
    for (Entry<K, V> item : map.entrySet()) {
      visitor.visit(item.getKey(), item.getValue());
    }
    return this;
  }

  public RubyHash<K, V> eachPair(RubyEnumerable.EntryVisitor<K, V> visitor) {
    return each(visitor);
  }

  public RubyHash<K, V> eachKey(RubyEnumerable.ItemVisitor<K> visitor) {
    for (K item : map.keySet()) {
      visitor.visit(item);
    }
    return this;
  }

  public RubyHash<K, V> eachValue(RubyEnumerable.ItemVisitor<V> visitor) {
    for (V item : map.values()) {
      visitor.visit(item);
    }
    return this;
  }

  public boolean eql(RubyHash other) {
    return this.equals(other);
  }

  public V fetch(K key) {
    if (!map.containsKey(key)) {
      throw new IllegalArgumentException("key not found: " + key);
    }
    return map.get(key);
  }

  public V fetch(K key, V defaultValue) {
    if (!map.containsKey(key)) {
      return defaultValue;
    }
    return map.get(key);
  }

  public RubyArray<Entry<K, V>> flatten() {
    return new RubyArray(RubyEnumerable.toA(map.entrySet()));
  }

  public boolean hasKey(K key) {
    return map.containsKey(key);
  }

  public boolean hasValue(V value) {
    return map.containsValue(value);
  }

  public int hash() {
    return map.hashCode();
  }

  public RubyHash<K, V> toH() {
    return this;
  }

  public RubyHash<K, V> toHash() {
    return this;
  }

  public RubyArray<Entry<K, V>> toA() {
    return new RubyArray(RubyEnumerable.toA(map.entrySet()));
  }

  public String toS() {
    return map.toString();
  }

  public String inspect() {
    return map.toString();
  }

  public RubyHash<V, K> invert() {
    RubyHash<V, K> invertHash = new RubyHash();
    for (Entry<K, V> item : map.entrySet()) {
      invertHash.put(item.getValue(), item.getKey());
    }
    return invertHash;
  }

  public RubyHash<K, V> keepIf(RubyEnumerable.EntryBooleanVisitor<K, V> visitor) {
    for (Entry<K, V> item : map.entrySet()) {
      if (!visitor.visit(item.getKey(), item.getValue())) {
        map.remove(item.getKey());
      }
    }
    return this;
  }

  public K key(V value) {
    for (Entry<K, V> item : map.entrySet()) {
      if (item.getValue().equals(value)) {
        return item.getKey();
      }
    }
    return null;
  }

  public RubyArray<K> keys() {
    return new RubyArray(map.keySet());
  }

  public int length() {
    return map.size();
  }

  public RubyHash<K, V> merge(Map<K, V> otherHash) {
    RubyHash<K, V> newHash = new RubyHash<>();
    for (Entry<K, V> item : map.entrySet()) {
      newHash.put(item);
    }
    for (Entry<K, V> item : otherHash.entrySet()) {
      newHash.put(item);
    }
    return newHash;
  }

  public RubyHash<K, V> mergeEx(Map<K, V> otherHash) {
    for (Entry<K, V> item : otherHash.entrySet()) {
      map.put(item.getKey(), item.getValue());
    }
    return this;
  }

  public RubyHash<K, V> update(Map<K, V> otherHash) {
    return mergeEx(otherHash);
  }

  public RubyHash<K, V> merge(Map<K, V> otherHash, RubyEnumerable.EntryMergeVisitor<K, V> visitor) {
    RubyHash<K, V> newHash = new RubyHash<>();
    for (Entry<K, V> item : map.entrySet()) {
      if (map.containsKey(item.getKey()) && otherHash.containsKey(item.getKey())) {
        newHash.put(item.getKey(), visitor.visit(item.getKey(), item.getValue(), otherHash.get(item.getKey())));
      } else {
        newHash.put(item);
      }
    }
    for (Entry<K, V> item : otherHash.entrySet()) {
      if (!newHash.containsKey(item.getKey())) {
        newHash.put(item);
      }
    }
    return newHash;
  }

  public RubyHash<K, V> mergeEx(Map<K, V> otherHash, RubyEnumerable.EntryMergeVisitor<K, V> visitor) {
    for (Entry<K, V> item : map.entrySet()) {
      if (map.containsKey(item.getKey()) && otherHash.containsKey(item.getKey())) {
        map.put(item.getKey(), visitor.visit(item.getKey(), item.getValue(), otherHash.get(item.getKey())));
      } else {
        map.put(item.getKey(), item.getValue());
      }
    }
    for (Entry<K, V> item : otherHash.entrySet()) {
      if (!map.containsKey(item.getKey())) {
        map.put(item.getKey(), item.getValue());
      }
    }
    return this;
  }

  public RubyHash<K, V> update(Map<K, V> otherHash, RubyEnumerable.EntryMergeVisitor<K, V> visitor) {
    return mergeEx(otherHash, visitor);
  }

  public Entry<K, V> rassoc(V value) {
    for (Entry<K, V> item : map.entrySet()) {
      if (item.getValue().equals(item)) {
        return item;
      }
    }
    return null;
  }

  public RubyHash<K, V> rejectEx(RubyEnumerable.EntryBooleanVisitor<K, V> visitor) {
    int beforeSize = map.size();
    deleteIf(visitor);
    if (map.size() == beforeSize) {
      return null;
    } else {
      return this;
    }
  }

  public RubyHash<K, V> replace(Map<K, V> otherHash) {
    map.clear();
    map.putAll(otherHash);
    return this;
  }

  public Entry<K, V> shift() {
    if (map.isEmpty()) {
      return null;
    } else {
      return map.entrySet().iterator().next();
    }
  }

  public V store(K key, V value) {
    map.put(key, value);
    return value;
  }

  @Override
  public RubyArray<V> values() {
    return new RubyArray(map.values());
  }

  public RubyArray<V> valuesAt(K... keys) {
    List<V> values = newArrayList();
    for (K key : keys) {
      values.add(map.get(key));
    }
    return new RubyArray(values);
  }

  // Ruby Enumerable methods
  public boolean hasAll() {
    return true;
  }

  public boolean hasAll(RubyEnumerable.EntryBooleanVisitor<K, V> visitor) {
    boolean bool = true;
    for (Entry<K, V> item : map.entrySet()) {
      if (visitor.visit(item.getKey(), item.getValue()) == false) {
        bool = false;
      }
    }
    return bool;
  }

  public boolean hasAny() {
    return RubyEnumerable.hasAny(map.entrySet());
  }

  public boolean hasAny(RubyEnumerable.EntryBooleanVisitor<K, V> visitor) {
    boolean bool = false;
    for (Entry<K, V> item : map.entrySet()) {
      if (visitor.visit(item.getKey(), item.getValue())) {
        bool = true;
      }
    }
    return bool;
  }

  public <S> RubyArray<Entry<S, RubyArray<Entry<K, V>>>> chunk(RubyEnumerable.EntryTransformVisitor<K, V, S> visitor) {
    Multimap<S, Entry<K, V>> multimap = ArrayListMultimap.create();
    for (Entry<K, V> item : map.entrySet()) {
      S key = visitor.visit(item.getKey(), item.getValue());
      multimap.put(key, item);
    }
    RubyArray<Entry<S, RubyArray<Entry<K, V>>>> list = new RubyArray();
    for (S key : multimap.keySet()) {
      list.add(new SimpleEntry<>(key, new RubyArray(multimap.get(key))));
    }
    return list;
  }

  public <S> RubyArray<S> collect(RubyEnumerable.EntryTransformVisitor<K, V, S> visitor) {
    List<S> list = newArrayList();
    for (Entry<K, V> item : map.entrySet()) {
      list.add(visitor.visit(item.getKey(), item.getValue()));
    }
    return new RubyArray(list);
  }

  public <S> RubyArray<S> collectConcat(RubyEnumerable.EntryListVisitor<K, V, S> visitor) {
    List<S> list = newArrayList();
    for (Entry<K, V> item : map.entrySet()) {
      list.addAll(visitor.visit(item.getKey(), item.getValue()));
    }
    return new RubyArray(list);
  }

  public int count() {
    return RubyEnumerable.count(map.entrySet());
  }

  public int count(RubyEnumerable.EntryBooleanVisitor<K, V> visitor) {
    int count = 0;
    for (Entry<K, V> item : map.entrySet()) {
      if (visitor.visit(item.getKey(), item.getValue())) {
        count++;
      }
    }
    return count;
  }

  public void cycle(RubyEnumerable.EntryVisitor<K, V> visitor) {
    while (true) {
      for (Entry<K, V> item : map.entrySet()) {
        visitor.visit(item.getKey(), item.getValue());
      }
    }
  }

  public void cycle(int cycles, RubyEnumerable.EntryVisitor<K, V> visitor) {
    for (int i = 0; i < cycles; i++) {
      for (Entry<K, V> item : map.entrySet()) {
        visitor.visit(item.getKey(), item.getValue());
      }
    }
  }

  public Entry<K, V> detect(RubyEnumerable.EntryBooleanVisitor<K, V> visitor) {
    for (Entry<K, V> item : map.entrySet()) {
      if (visitor.visit(item.getKey(), item.getValue())) {
        return item;
      }
    }
    return null;
  }

  public RubyArray<Entry<K, V>> drop(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("attempt to drop negative size");
    }
    List<Entry<K, V>> list = newArrayList();
    int i = 0;
    for (Entry<K, V> item : map.entrySet()) {
      if (i >= n) {
        list.add(item);
      }
      i++;
    }
    return new RubyArray(list);
  }

  public RubyArray<Entry<K, V>> dropWhile(RubyEnumerable.EntryBooleanVisitor<K, V> visitor) {
    List<Entry<K, V>> list = newArrayList();
    boolean cutPoint = false;
    for (Entry<K, V> item : map.entrySet()) {
      if (visitor.visit(item.getKey(), item.getValue()) || cutPoint) {
        cutPoint = true;
        list.add(item);
      }
    }
    return new RubyArray(list);
  }

  public void eachCons(int n, RubyEnumerable.ItemBlockVisitor<Entry<K, V>> visitor) {
    RubyEnumerable.eachCons(map.entrySet(), n, visitor);
  }

  public void eachSlice(int n, RubyEnumerable.ItemBlockVisitor<Entry<K, V>> visitor) {
    RubyEnumerable.eachSlice(map.entrySet(), n, visitor);
  }

  public void eachWithIndex(RubyEnumerable.ItemWithIndexVisitor<Entry<K, V>> visitor) {
    RubyEnumerable.eachWithIndex(map.entrySet(), visitor);
  }

  public void eachWithObject(Object o, RubyEnumerable.ItemWithObject<Entry<K, V>> visitor) {
    RubyEnumerable.eachWithObject(map.entrySet(), o, visitor);
  }

  public RubyArray<Entry<K, V>> entries() {
    return new RubyArray(RubyEnumerable.entries(map.entrySet()));
  }

  public Entry<K, V> find(RubyEnumerable.EntryBooleanVisitor<K, V> visitor) {
    return detect(visitor);
  }

  public RubyArray<Entry<K, V>> findAll(RubyEnumerable.EntryBooleanVisitor<K, V> visitor) {
    List<Entry<K, V>> list = newArrayList();
    for (Entry<K, V> item : map.entrySet()) {
      if (visitor.visit(item.getKey(), item.getValue())) {
        list.add(item);
      }
    }
    return new RubyArray(list);
  }

  public Entry<K, V> first() {
    return RubyEnumerable.first(map.entrySet());
  }

  public RubyArray<Entry<K, V>> first(int n) {
    return new RubyArray(RubyEnumerable.first(map.entrySet(), n));
  }

  public Integer findIndex(Entry<K, V> target) {
    return RubyEnumerable.findIndex(map.entrySet(), target);
  }

  public Integer findIndex(RubyEnumerable.EntryBooleanVisitor<K, V> visitor) {
    int index = 0;
    for (Entry<K, V> item : map.entrySet()) {
      if (visitor.visit(item.getKey(), item.getValue())) {
        return index;
      }
      index++;
    }
    return null;
  }

  public <S> RubyArray<S> flatMap(RubyEnumerable.EntryListVisitor<K, V, S> visitor) {
    return collectConcat(visitor);
  }

  public <S> RubyHash<S, RubyArray<Entry<K, V>>> groupBy(RubyEnumerable.EntryTransformVisitor<K, V, S> visitor) {
    Multimap<S, Entry<K, V>> multimap = ArrayListMultimap.create();
    for (Entry<K, V> item : map.entrySet()) {
      S key = visitor.visit(item.getKey(), item.getValue());
      multimap.put(key, item);
    }
    RubyHash<S, RubyArray<Entry<K, V>>> hash = new RubyHash();
    for (S key : multimap.keySet()) {
      hash.put(key, new RubyArray(multimap.get(key)));
    }
    return hash;
  }

  public boolean include(K key) {
    return map.containsKey(key);
  }

  public boolean hasMember(K key) {
    return map.containsKey(key);
  }

  public Entry<K, V> inject(RubyEnumerable.InjectVisitor<Entry<K, V>> visitor) {
    return RubyEnumerable.inject(map.entrySet(), visitor);
  }

  public <S> S inject(S init, RubyEnumerable.EntryInjectWithInitVisitor<K, V, S> visitor) {
    for (Entry<K, V> item : map.entrySet()) {
      init = visitor.visit(init, item);
    }
    return init;
  }

  public <S> RubyArray<S> map(RubyEnumerable.EntryTransformVisitor<K, V, S> visitor) {
    return collect(visitor);
  }

  public Entry<K, V> max(Comparator<? super K> comp) {
    K maxKey = RubyEnumerable.max(map.keySet(), comp);
    return find((k, v) -> {
      return k.equals(maxKey);
    });
  }

  public <S> Entry<K, V> maxBy(Comparator<? super S> comp, RubyEnumerable.EntryTransformVisitor<K, V, S> visitor) {
    List<Entry<K, V>> src = newArrayList();
    List<S> dst = newArrayList();
    for (Entry<K, V> item : map.entrySet()) {
      src.add(item);
      dst.add(visitor.visit(item.getKey(), item.getValue()));
    }
    S maxDst = Collections.max(dst, comp);
    return src.get(dst.indexOf(maxDst));
  }

  public Entry<K, V> min(Comparator<? super K> comp) {
    K minKey = RubyEnumerable.min(map.keySet(), comp);
    return find((k, v) -> {
      return k.equals(minKey);
    });
  }

  public <S> Entry<K, V> minBy(Comparator<? super S> comp, RubyEnumerable.EntryTransformVisitor<K, V, S> visitor) {
    List<Entry<K, V>> src = newArrayList();
    List<S> dst = newArrayList();
    for (Entry<K, V> item : map.entrySet()) {
      src.add(item);
      dst.add(visitor.visit(item.getKey(), item.getValue()));
    }
    S minDst = Collections.min(dst, comp);
    return src.get(dst.indexOf(minDst));
  }

  public RubyArray<Entry<K, V>> minmax(Comparator<? super K> comp) {
    K minKey = RubyEnumerable.min(map.keySet(), comp);
    K maxKey = RubyEnumerable.max(map.keySet(), comp);
    Entry<K, V> min = new SimpleEntry(minKey, map.get(minKey));
    Entry<K, V> max = new SimpleEntry(maxKey, map.get(maxKey));
    return new RubyArray(min, max);
  }

  public <S> RubyArray<Entry<K, V>> minmaxBy(Comparator<? super S> comp, RubyEnumerable.EntryTransformVisitor<K, V, S> visitor) {
    List<Entry<K, V>> src = newArrayList();
    List<S> dst = newArrayList();
    for (Entry<K, V> item : map.entrySet()) {
      src.add(item);
      dst.add(visitor.visit(item.getKey(), item.getValue()));
    }
    S minDst = Collections.min(dst, comp);
    S maxDst = Collections.max(dst, comp);
    return new RubyArray(src.get(dst.indexOf(minDst)), src.get(dst.indexOf(maxDst)));
  }

  public RubyArray<RubyArray<Entry<K, V>>> partition(RubyEnumerable.EntryBooleanVisitor<K, V> visitor) {
    RubyArray<Entry<K, V>> trueList = new RubyArray();
    RubyArray<Entry<K, V>> falseList = new RubyArray();
    for (Entry<K, V> item : map.entrySet()) {
      if (visitor.visit(item.getKey(), item.getValue())) {
        trueList.add(item);
      } else {
        falseList.add(item);
      }
    }
    return new RubyArray(trueList, falseList);
  }

  public boolean hasNone() {
    return RubyEnumerable.hasNone(map.entrySet());
  }

  public boolean hasNone(RubyEnumerable.EntryBooleanVisitor<K, V> visitor) {
    boolean bool = true;
    for (Entry<K, V> item : map.entrySet()) {
      if (visitor.visit(item.getKey(), item.getValue())) {
        bool = false;
      }
    }
    return bool;
  }

  public boolean hasOne() {
    return RubyEnumerable.hasOne(map.entrySet());
  }

  public boolean hasOne(RubyEnumerable.EntryBooleanVisitor<K, V> visitor) {
    int count = 0;
    for (Entry<K, V> item : map.entrySet()) {
      if (visitor.visit(item.getKey(), item.getValue())) {
        count++;
        if (count > 1) {
          return false;
        }
      }
    }
    return count == 1;
  }

  public Entry<K, V> reduce(RubyEnumerable.InjectVisitor<Entry<K, V>> visitor) {
    return inject(visitor);
  }

  public <S> S reduce(S init, RubyEnumerable.EntryInjectWithInitVisitor<K, V, S> visitor) {
    return inject(init, visitor);
  }

  public RubyHash<K, V> reject(RubyEnumerable.EntryBooleanVisitor<K, V> visitor) {
    RubyHash<K, V> hash = new RubyHash();
    for (Entry<K, V> item : map.entrySet()) {
      if (visitor.visit(item.getKey(), item.getValue())) {
        hash.put(item);
      }
    }
    return hash;
  }

  public void reverseEach(RubyEnumerable.EntryVisitor visitor) {
    List<Entry<K, V>> reversedEntries = newArrayList();
    for (Entry<K, V> entry : map.entrySet()) {
      reversedEntries.add(0, entry);
    }
    for (Entry<K, V> entry : reversedEntries) {
      visitor.visit(entry.getKey(), entry.getValue());
    }
  }

  public RubyArray<Entry<K, V>> select(RubyEnumerable.EntryBooleanVisitor<K, V> visitor) {
    return findAll(visitor);
  }

  public RubyArray<RubyArray<Entry<K, V>>> sliceBefore(String regex) {
    RubyArray<RubyArray<Entry<K, V>>> list = new RubyArray();
    Pattern pattern = Pattern.compile(regex);
    RubyArray<Entry<K, V>> group = null;
    for (Entry<K, V> item : map.entrySet()) {
      if (group == null) {
        group = new RubyArray();
        group.add(item);
      } else if (pattern.matcher(item.toString()).find()) {
        list.add(group);
        group = new RubyArray();
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

  public RubyArray<RubyArray<Entry<K, V>>> sliceBefore(RubyEnumerable.EntryBooleanVisitor<K, V> visitor) {
    RubyArray<RubyArray<Entry<K, V>>> list = new RubyArray();
    RubyArray<Entry<K, V>> group = null;
    for (Entry<K, V> item : map.entrySet()) {
      if (group == null) {
        group = new RubyArray();
        group.add(item);
      } else if (visitor.visit(item.getKey(), item.getValue())) {
        list.add(group);
        group = new RubyArray();
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

  public RubyHash<K, V> sort(Comparator<? super K> comp) {
    List<K> sortedKeys = RubyEnumerable.sort(map.keySet(), comp);
    Map<K, V> sortedMap = newLinkedHashMap();
    for (K key : sortedKeys) {
      sortedMap.put(key, map.get(key));
    }
    return new RubyHash(sortedMap);
  }

  public <S> RubyHash<K, V> sortBy(Comparator<? super S> comp, RubyEnumerable.EntryTransformVisitor<K, V, S> visitor) {
    List<Entry<S, K>> references = newArrayList();
    for (Entry<K, V> item : map.entrySet()) {
      Entry<S, K> ref = new SimpleEntry<S, K>(visitor.visit(item.getKey(), item.getValue()), item.getKey());
      references.add(ref);
    }
    references = RubyEnumerable.sortBy(references, comp, (e) -> {
      return e.getKey();
    });

    Map<K, V> sortedMap = newLinkedHashMap();
    for (Entry<S, K> ref : references) {
      sortedMap.put(ref.getValue(), map.get(ref.getValue()));
    }
    return new RubyHash(sortedMap);
  }

  public RubyArray<Entry<K, V>> take(int n) {
    return new RubyArray(RubyEnumerable.take(map.entrySet(), n));
  }

  public RubyArray<Entry<K, V>> takeWhile(RubyEnumerable.EntryBooleanVisitor visitor) {
    List<Entry<K, V>> list = newArrayList();
    for (Entry<K, V> item : map.entrySet()) {
      if (visitor.visit(item.getKey(), item.getValue())) {
        list.add(item);
      } else {
        return new RubyArray(list);
      }
    }
    return new RubyArray(list);
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return map.containsValue(value);
  }

  @Override
  public V get(Object key) {
    if (defaultValue != null && !map.containsKey(key)) {
      return defaultValue;
    }
    return map.get(key);
  }

  @Override
  public V put(K key, V value) {
    return map.put(key, value);
  }

  @Override
  public V remove(Object key) {
    return map.remove(key);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    map.putAll(m);
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public Set<K> keySet() {
    return map.keySet();
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    return map.entrySet();
  }
}
