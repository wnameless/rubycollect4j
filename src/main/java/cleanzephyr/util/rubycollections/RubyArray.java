package cleanzephyr.util.rubycollections;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import static com.google.common.collect.Lists.newArrayList;
import com.google.common.collect.Multimap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.regex.Pattern;
import org.uncommons.maths.combinatorics.CombinationGenerator;
import org.uncommons.maths.combinatorics.PermutationGenerator;

public final class RubyArray<E> implements List<E> {

  private final List<E> list;
  private final Random rand = new Random();

  public RubyArray() {
    list = newArrayList();
  }

  public RubyArray(E... args) {
    list = newArrayList(args);
  }

  public RubyArray(List<E> list) {
    this.list = list;
  }

  public RubyArray(List<E> list, boolean defensiveCopy) {
    if (defensiveCopy) {
      this.list = newArrayList(list);
    } else {
      this.list = list;
    }
  }

  public RubyArray(Collection<E> coll) {
    list = newArrayList(coll);
  }

  public RubyArray(Iterator<E> iter) {
    list = newArrayList(iter);
  }

  // Ruby Array methods
  public RubyArray<E> and(RubyArray<E> other) {
    List<E> andList = newArrayList();
    for (E item : this) {
      if (!andList.contains(item) && list.contains(item) && other.contains(item)) {
        andList.add(item);
      }
    }
    return new RubyArray(andList);
  }

  public RubyArray<E> multiply(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("negative argument");
    }
    List<E> multiplyList = newArrayList();
    for (int i = 0; i < n; i++) {
      for (E item : list) {
        multiplyList.add(item);
      }
    }
    return new RubyArray(multiplyList);
  }

  public String multiply(String separator) {
    return this.join(separator);
  }

  public RubyArray<E> add(RubyArray<E> other) {
    List<E> addList = newArrayList();
    for (E item : list) {
      addList.add(item);
    }
    for (E item : other) {
      addList.add(item);
    }
    return new RubyArray(addList);
  }

  public RubyArray<E> minus(RubyArray<E> other) {
    List<E> minusList = newArrayList();
    for (E item : list) {
      minusList.add(item);
    }
    for (E item : other) {
      minusList.remove(item);
    }
    return new RubyArray(minusList);
  }

  public <S> RubyArray<S> assoc(S target) {
    for (E item : list) {
      if (item instanceof List) {
        List itemList = (List) item;
        if (itemList.size() > 0 && itemList.get(0).equals(target)) {
          return new RubyArray(itemList, true);
        }
      }
    }
    return null;
  }

  public E at(int index) {
    if (list.isEmpty()) {
      return null;
    } else if (index >= 0 && index < list.size()) {
      return list.get(index);
    } else if (index <= -1 && index >= -list.size()) {
      return list.get(index + list.size());
    } else {
      return null;
    }
  }

  public E bsearch(E target, Comparator<? super E> comp) {
    int index = Collections.binarySearch(list, target, comp);
    return index < 0 ? null : list.get(index);
  }

  public RubyArray<RubyArray<E>> combination(int n) {
    RubyArray<RubyArray<E>> comb = new RubyArray();
    if (n < 0) {
      return comb;
    } else if (n == 0) {
      comb.add(new RubyArray<E>());
      return comb;
    } else if (n > list.size()) {
      return comb;
    } else {
      CombinationGenerator<E> cg = new CombinationGenerator<>(list, n);
      for (List<E> combination : cg) {
        comb.add(new RubyArray<E>(combination));
      }
    }
    return comb;
  }

  public RubyArray<RubyArray<E>> combination(int n, RubyEnumerable.ItemBlock<RubyArray<E>> block) {
    RubyArray<RubyArray<E>> comb = combination(n);
    for (RubyArray<E> item : comb) {
      block.yield(item);
    }
    return comb;
  }

  public RubyArray<RubyArray<E>> repeatedCombination(int n) {
    return combination(n);
  }

  public RubyArray<RubyArray<E>> repeatedCombination(int n, RubyEnumerable.ItemBlock<RubyArray<E>> block) {
    return combination(n, block);
  }

  public RubyArray<E> compact() {
    RubyArray<E> rubyArray = new RubyArray();
    for (E item : list) {
      if (item != null) {
        rubyArray.add(item);
      }
    }
    return rubyArray;
  }

  public RubyArray<E> compactEx() {
    boolean isCompacted = false;
    ListIterator<E> li = list.listIterator();
    while (li.hasNext()) {
      E item = li.next();
      if (item == null) {
        li.remove();
        isCompacted = true;
      }
    }
    return isCompacted ? this : null;
  }

  public RubyArray<E> concat(RubyArray<E> other) {
    list.addAll(other);
    return this;
  }

  public int count(E target) {
    int count = 0;
    for (E item : list) {
      if (item.equals(target)) {
        count++;
      }
    }
    return count;
  }

  public E delete(E target) {
    boolean isDeleted = false;
    ListIterator<E> li = list.listIterator();
    while (li.hasNext()) {
      E item = li.next();
      if (item.equals(target)) {
        li.remove();
        isDeleted = true;
      }
    }
    return isDeleted ? target : null;
  }

  public E delete(E target, RubyEnumerable.Block<E> block) {
    boolean isDeleted = false;
    ListIterator<E> li = list.listIterator();
    while (li.hasNext()) {
      E item = li.next();
      if (item.equals(target)) {
        li.remove();
        isDeleted = true;
      }
    }
    return isDeleted ? target : block.yield();
  }

  public E deleteAt(int index) {
    if (list.isEmpty()) {
      return null;
    } else if (index >= 0 && index < list.size()) {
      return list.remove(index);
    } else if (index <= -1 && index >= -list.size()) {
      return list.remove(index + list.size());
    } else {
      return null;
    }
  }

  public RubyArray<E> deleteIf(RubyEnumerable.BooleanBlock<E> block) {
    ListIterator<E> li = list.listIterator();
    while (li.hasNext()) {
      E item = li.next();
      if (block.yield(item)) {
        li.remove();
      }
    }
    return this;
  }

  public void each(RubyEnumerable.ItemBlock<E> block) {
    for (E item : list) {
      block.yield(item);
    }
  }

  public void eachIndex(RubyEnumerable.IndexBlock<E> block) {
    for (int i = 0; i < list.size(); i++) {
      block.yield(i);
    }
  }

  public boolean eql(RubyArray<E> other) {
    return this.equals(other);
  }

  public E fetch(int index) {
    return at(index);
  }

  public E fetch(int index, E defaultValue) {
    E target = at(index);
    if (target == null) {
      return defaultValue;
    } else {
      return target;
    }
  }

  public E fetch(int index, RubyEnumerable.ItemBlock<E> block) {
    E target = at(index);
    if (target == null) {
      block.yield(target);
      return null;
    } else {
      return target;
    }
  }

  public RubyArray<E> fill(E item) {
    for (int i = 0; i < list.size(); i++) {
      list.set(i, item);
    }
    return this;
  }

  public RubyArray<E> fill(E item, int start) {
    for (int i = start; i < list.size(); i++) {
      list.set(i, item);
    }
    return this;
  }

  public RubyArray<E> fill(E item, int start, int length) {
    for (int i = start; i < start + length && i < list.size(); i++) {
      list.set(i, item);
    }
    return this;
  }

  public RubyArray<E> fill(RubyEnumerable.ItemWithReturnBlock<E> block) {
    for (int i = 0; i < list.size(); i++) {
      list.set(i, block.yield(list.get(i)));
    }
    return this;
  }

  public RubyArray<E> fill(int start, RubyEnumerable.ItemWithReturnBlock<E> block) {
    for (int i = start; i < list.size(); i++) {
      list.set(i, block.yield(list.get(i)));
    }
    return this;
  }

  public RubyArray<E> fill(int start, int length, RubyEnumerable.ItemWithReturnBlock<E> block) {
    for (int i = start; i < start + length && i < list.size(); i++) {
      list.set(i, block.yield(list.get(i)));
    }
    return this;
  }

  public Integer index(E target) {
    Integer index = null;
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).equals(target)) {
        return i;
      }
    }
    return index;
  }

  public Integer index(RubyEnumerable.BooleanBlock<E> block) {
    Integer index = null;
    for (int i = 0; i < list.size(); i++) {
      if (block.yield(list.get(i))) {
        return i;
      }
    }
    return index;
  }

  public <S> RubyArray<S> flatten() {
    RubyArray<S> rubyArray = new RubyArray();
    List subLists = newArrayList();
    for (E item : list) {
      if (item instanceof List) {
        subLists.add(item);
      } else {
        rubyArray.add((S) item);
      }
    }
    while (!subLists.isEmpty()) {
      if (subLists.get(0) instanceof List) {
        List subList = (List) subLists.remove(0);
        for (Object item : subList) {
          if (item instanceof List) {
            subLists.add(item);
          } else {
            rubyArray.add((S) item);
          }
        }
      } else {
        rubyArray.add((S) subLists.remove(0));
      }
    }
    return rubyArray;
  }

  public RubyArray<E> replace(List<E> other) {
    list.clear();
    list.addAll(other);
    return this;
  }

  public RubyArray<E> insert(int index, E... args) {
    if (index < -(list.size() + 1)) {
      return null;
    } else if (index < 0) {
      int relIndex = list.size() + index + 1;
      for (int i = args.length - 1; i >= 0; i--) {
        list.add(relIndex, args[i]);
      }
    } else if (index <= list.size()) {
      for (int i = args.length - 1; i >= 0; i--) {
        list.add(index, args[i]);
      }
    } else {
      while (index > list.size()) {
        list.add(null);
      }
      System.out.println(list);
      for (int i = args.length - 1; i >= 0; i--) {
        list.add(index - args.length + 2, args[i]);
      }
    }
    return this;
  }

  public String inspect() {
    return list.toString();
  }

  public String join() {
    StringBuilder sb = new StringBuilder();
    for (E item : list) {
      sb.append(item.toString());
    }
    return sb.toString();
  }

  public String join(String separator) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < list.size(); i++) {
      if (i > 0 && i != list.size()) {
        sb.append(separator);
      }
      sb.append(list.get(i).toString());
    }
    return sb.toString();
  }

  public RubyArray<E> keepIf(RubyEnumerable.BooleanBlock<E> block) {
    ListIterator<E> li = list.listIterator();
    while (li.hasNext()) {
      E item = li.next();
      if (!(block.yield(item))) {
        li.remove();
      }
    }
    return this;
  }

  public E last() {
    List<E> reverseList = Lists.reverse(list);
    if (reverseList.isEmpty()) {
      return null;
    } else {
      return reverseList.get(0);
    }
  }

  public RubyArray<E> last(int n) {
    List<E> reverseList = Lists.reverse(list);
    RubyArray<E> rubyArray = new RubyArray();
    for (int i = 0; i < n && i < reverseList.size(); i++) {
      rubyArray.add(reverseList.get(i));
    }
    return rubyArray;
  }

  public E pop() {
    if (list.isEmpty()) {
      return null;
    } else {
      return list.remove(list.size() - 1);
    }
  }

  public RubyArray<E> pop(int n) {
    RubyArray<E> rubyArray = new RubyArray();
    for (int i = 0; i < n; i++) {
      rubyArray.add(0, pop());
    }
    rubyArray.keepIf((val) -> {
      return val != null;
    });
    return rubyArray;
  }

  public RubyArray<RubyArray<E>> product(RubyArray<E>... arys) {
    RubyArray<RubyArray<E>> rubyArray = new RubyArray();
    RubyArray<E>[] others = new RubyArray[arys.length + 1];
    others[0] = this;
    System.arraycopy(arys, 0, others, 1, arys.length);
    int[] counters = new int[others.length];
    while (isLooping(counters, others)) {
      RubyArray<E> combination = new RubyArray();
      for (int i = 0; i < counters.length; i++) {
        combination.add(others[i].get(counters[i]));
      }
      rubyArray.add(combination);
      increaseCounters(counters, others);
    }
    return rubyArray;
  }

  private void increaseCounters(int[] counters, RubyArray<E>[] others) {
    for (int i = counters.length - 1; i >= 0; i--) {
      if (counters[i] < others[i].size() - 1) {
        counters[i]++;
        for (int j = i + 1; j < counters.length; j++) {
          counters[j] = 0;
        }
        return;
      } else {
        counters[i] = -1;
      }
    }
  }

  private boolean isLooping(int[] counters, RubyArray<E>[] others) {
    for (int i = 0; i < counters.length; i++) {
      if (others[i].isEmpty()) {
        return false;
      }
    }
    return Arrays.binarySearch(counters, -1) == -1;
  }

  public void product(RubyArray<RubyArray<E>> arys, RubyEnumerable.ItemBlock<RubyArray<E>> block) {
    RubyArray<RubyArray<E>> combinations = product(arys.toArray(new RubyArray[arys.length()]));
    for (RubyArray<E> comb : combinations) {
      block.yield(comb);
    }
  }

  public RubyArray<E> push(E item) {
    list.add(item);
    return this;
  }

  public <S> RubyArray<S> rassoc(S target) {
    for (E item : list) {
      if (item instanceof List) {
        List itemList = (List) item;
        if (itemList.size() > 0 && itemList.get(itemList.size() - 1).equals(target)) {
          return new RubyArray(itemList, true);
        }
      }
    }
    return null;
  }

  public RubyArray<E> rejectEx(RubyEnumerable.BooleanBlock<E> block) {
    int beforeLength = list.size();
    RubyArray<E> rubyArray = deleteIf(block);
    if (rubyArray.size() != beforeLength) {
      return rubyArray;
    } else {
      return null;
    }
  }

  public RubyArray<E> replace(RubyArray<E> other) {
    list.clear();
    list.addAll(other);
    return this;
  }

  public RubyArray<E> reverse() {
    return new RubyArray(Lists.reverse(list));
  }

  public RubyArray<E> reverseEx() {
    List<E> reversedList = Lists.reverse(list);
    list.clear();
    list.addAll(reversedList);
    return this;
  }

  public Integer rindex(E target) {
    Integer index = null;
    for (int i = list.size() - 1; i >= 0; i--) {
      if (list.get(i).equals(target)) {
        return i;
      }
    }
    return index;
  }

  public Integer rindex(RubyEnumerable.BooleanBlock<E> block) {
    Integer index = null;
    for (int i = list.size() - 1; i >= 0; i--) {
      if (block.yield(list.get(i))) {
        return i;
      }
    }
    return index;
  }

  public RubyArray<E> rotate() {
    RubyArray<E> rubyArray = new RubyArray<>(list, true);
    if (rubyArray.size() > 0) {
      rubyArray.add(rubyArray.remove(0));
    }
    return rubyArray;
  }

  public RubyArray<E> rotateEx() {
    if (list.size() > 0) {
      list.add(list.remove(0));
    }
    return this;
  }

  public RubyArray<E> rotate(int count) {
    List<E> rotatedList = newArrayList(list);
    if (rotatedList.size() > 0) {
      while (count != 0) {
        if (count > 0) {
          rotatedList.add(rotatedList.remove(0));
          count--;
        } else if (count < 0) {
          rotatedList.add(0, rotatedList.remove(rotatedList.size() - 1));
          count++;
        }
      }
    }
    return new RubyArray<>(rotatedList);
  }

  public RubyArray<E> rotateEx(int count) {
    if (list.size() > 0) {
      while (count != 0) {
        if (count > 0) {
          list.add(list.remove(0));
          count--;
        } else if (count < 0) {
          list.add(0, list.remove(list.size() - 1));
          count++;
        }
      }
    }
    return this;
  }

  public E sample() {
    if (list.size() > 0) {
      return list.get(rand.nextInt(list.size()));
    } else {
      return null;
    }
  }

  public RubyArray<E> sample(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("negative sample number");
    }
    List<E> copyList = newArrayList(list);
    List<E> samples = newArrayList();
    while (samples.size() < list.size() && samples.size() < n) {
      samples.add(copyList.remove(rand.nextInt(copyList.size())));
    }
    return new RubyArray(samples);
  }

  public RubyArray<E> selectEx(RubyEnumerable.BooleanBlock block) {
    RubyArray<E> rubyArray = select(block);
    if (rubyArray.size() == list.size()) {
      return null;
    } else {
      list.clear();
      list.addAll(rubyArray);
      return this;
    }
  }

  public E shift() {
    if (list.isEmpty()) {
      return null;
    } else {
      return list.remove(0);
    }
  }

  public RubyArray<E> shift(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("negative array size");
    }
    List<E> shiftedList = newArrayList();
    while (!list.isEmpty() && shiftedList.size() < n) {
      shiftedList.add(list.remove(0));
    }
    return new RubyArray(shiftedList);
  }

  public RubyArray<E> shuffle() {
    List<E> shuffledList = newArrayList(list);
    Collections.shuffle(shuffledList);
    return new RubyArray(shuffledList);
  }

  public RubyArray<E> shuffleEx() {
    Collections.shuffle(list);
    return this;
  }

  public E slice(int index) {
    return at(index);
  }

  public RubyArray<E> slice(int index, int length) {
    List<E> slicedList = newArrayList();
    if (index < -list.size()) {
      return null;
    } else if (index >= list.size()) {
      return null;
    } else {
      if (index < 0) {
        index += list.size();
      }
      for (int i = index; i < list.size() && i < index + length; i++) {
        slicedList.add(list.get(i));
      }
    }
    return new RubyArray(slicedList);
  }

  public E sliceEx(int index) {
    if (list.isEmpty()) {
      return null;
    } else if (index >= 0 && index < list.size()) {
      return list.remove(index);
    } else if (index <= -1 && index >= -list.size()) {
      return list.remove(index + list.size());
    } else {
      return null;
    }
  }

  public RubyArray<E> sliceEx(int index, int length) {
    List<E> slicedList = newArrayList();
    if (index < -list.size()) {
      return null;
    } else if (index >= list.size()) {
      return null;
    } else {
      if (index < 0) {
        index += list.size();
      }
      for (int i = index; i < list.size() && length > 0;) {
        slicedList.add(list.remove(i));
        length--;
      }
    }
    return new RubyArray(slicedList);
  }

  public int length() {
    return list.size();
  }

  public RubyArray<E> uniq() {
    List<E> uniqList = newArrayList();
    for (E item : list) {
      if (!uniqList.contains(item)) {
        uniqList.add(item);
      }
    }
    return new RubyArray(uniqList);
  }

  public RubyArray<E> uniqEx() {
    RubyArray<E> uniqList = uniq();
    list.clear();
    list.addAll(uniqList);
    return this;
  }

  public <S> RubyArray<E> uniq(RubyEnumerable.TransformBlock<E, S> block) {
    List<E> uniqList = newArrayList();
    List<S> uniqByList = newArrayList();
    for (E item : list) {
      S trans = block.yield(item);
      if (!uniqByList.contains(trans)) {
        uniqByList.add(trans);
        uniqList.add(item);
      }
    }
    return new RubyArray(uniqList);
  }

  public RubyArray<E> union(RubyArray<E> other) {
    List<E> unionList = newArrayList();
    for (E item : list) {
      if (!unionList.contains(item)) {
        unionList.add(item);
      }
    }
    for (E item : other) {
      if (!unionList.contains(item)) {
        unionList.add(item);
      }
    }
    return new RubyArray(unionList);
  }

  public RubyArray<E> unshift(E item) {
    list.add(0, item);
    return this;
  }

  public RubyArray<E> valuesAt(int... indice) {
    List<E> values = newArrayList();
    for (int index : indice) {
      values.add(this.at(index));
    }
    return new RubyArray(values);
  }

  public RubyArray<RubyArray<E>> zip(RubyArray<E>... others) {
    RubyArray<RubyArray<E>> zippedRubyArray = new RubyArray<>();
    for (int i = 0; i < list.size(); i++) {
      RubyArray<E> zip = new RubyArray();
      zip.add(this.at(i));
      for (int j = 0; j < others.length; j++) {
        zip.add(others[j].at(i));
      }
      zippedRubyArray.add(zip);
    }
    return zippedRubyArray;
  }

  public void zip(RubyArray<RubyArray<E>> others, RubyEnumerable.ItemBlock<RubyArray<E>> block) {
    RubyArray<RubyArray<E>> zippedRubyArray = zip(others.toArray(new RubyArray[others.length()]));
    for (RubyArray<E> item : zippedRubyArray) {
      block.yield(item);
    }
  }

  // Ruby Enumerable methods
  public boolean hasAll() {
    return RubyEnumerable.hasAll(list);
  }

  public boolean hasAll(RubyEnumerable.BooleanBlock block) {
    return RubyEnumerable.hasAll(list, block);
  }

  public <K> RubyArray<Entry<K, RubyArray<E>>> chunk(RubyEnumerable.TransformBlock<E, K> block) {
    Multimap<K, E> multimap = ArrayListMultimap.create();
    for (E item : list) {
      K key = block.yield(item);
      multimap.put(key, item);
    }
    RubyArray<Entry<K, RubyArray<E>>> rubyArray = new RubyArray();
    for (K key : multimap.keySet()) {
      rubyArray.add(new SimpleEntry<>(key, new RubyArray(multimap.get(key))));
    }
    return rubyArray;
  }

  public <S> RubyArray<E> collect(RubyEnumerable.TransformBlock<E, S> block) {
    return new RubyArray(RubyEnumerable.collect(list, block));
  }

  public <S> RubyArray<S> collectConcat(RubyEnumerable.ToListBlock<E, S> block) {
    return new RubyArray(RubyEnumerable.collectConcat(list, block));
  }

  public int count() {
    return RubyEnumerable.count(list);
  }

  public int count(RubyEnumerable.BooleanBlock<E> block) {
    return RubyEnumerable.count(list, block);
  }

  public void cycle(RubyEnumerable.ItemBlock<E> block) {
    RubyEnumerable.cycle(list, block);
  }

  public void cycle(int cycles, RubyEnumerable.ItemBlock<E> block) {
    RubyEnumerable.cycle(list, cycles, block);
  }

  public E detect(E target) {
    return RubyEnumerable.detect(list, target);
  }

  public E detect(RubyEnumerable.BooleanBlock<E> block) {
    return RubyEnumerable.detect(list, block);
  }

  public RubyArray<E> drop(int n) {
    return new RubyArray(RubyEnumerable.drop(list, n));
  }

  public RubyArray<E> dropWhile(RubyEnumerable.BooleanBlock block) {
    return new RubyArray(RubyEnumerable.dropWhile(list, block));
  }

  public void eachCons(int n, RubyEnumerable.ItemFromListBlock<E> block) {
    RubyEnumerable.eachCons(list, n, block);
  }

  public void eachSlice(int n, RubyEnumerable.ItemFromListBlock<E> block) {
    RubyEnumerable.eachSlice(list, n, block);
  }

  public void eachWithIndex(RubyEnumerable.ItemWithIndexBlock<E> vistor) {
    RubyEnumerable.eachWithIndex(list, vistor);
  }

  public void eachWithObject(Object o, RubyEnumerable.ItemWithObjectBlock<E> block) {
    RubyEnumerable.eachWithObject(list, o, block);
  }

  public RubyArray<E> entries() {
    return new RubyArray(RubyEnumerable.entries(list));
  }

  public E find(E target) {
    return RubyEnumerable.find(list, target);
  }

  public E find(RubyEnumerable.BooleanBlock<E> block) {
    return RubyEnumerable.find(list, block);
  }

  public E first() {
    return RubyEnumerable.first(list);
  }

  public RubyArray<E> first(int n) {
    return new RubyArray(RubyEnumerable.first(list, n));
  }

  public Integer findIndex(E target) {
    return RubyEnumerable.findIndex(list, target);
  }

  public Integer findIndex(RubyEnumerable.BooleanBlock<E> block) {
    return RubyEnumerable.findIndex(list, block);
  }

  public RubyArray<E> findAll(RubyEnumerable.BooleanBlock<E> block) {
    return new RubyArray(RubyEnumerable.findAll(list, block));
  }

  public <S> RubyArray<S> flatMap(RubyEnumerable.ToListBlock<E, S> block) {
    return new RubyArray(RubyEnumerable.flatMap(list, block));
  }

  public RubyArray<E> grep(String regex) {
    return new RubyArray(RubyEnumerable.grep(list, regex));
  }

  public <S> RubyArray<S> grep(String regex, RubyEnumerable.TransformBlock<E, S> block) {
    return new RubyArray(RubyEnumerable.grep(list, regex, block));
  }

  public <K> RubyHash<K, RubyArray<E>> groupBy(RubyEnumerable.TransformBlock<E, K> block) {
    Multimap<K, E> multimap = ArrayListMultimap.create();
    for (E item : list) {
      K key = block.yield(item);
      multimap.put(key, item);
    }
    RubyHash<K, RubyArray<E>> hash = new RubyHash();
    for (K key : multimap.keySet()) {
      hash.put(key, new RubyArray(multimap.get(key)));
    }
    return hash;
  }

  public boolean include(E target) {
    return RubyEnumerable.include(list, target);
  }

  public boolean hasMember(E target) {
    return RubyEnumerable.hasMember(list, target);
  }

  public E inject(String methodName) {
    return RubyEnumerable.inject(list, methodName);
  }

  public E inject(E init, String methodName) {
    return RubyEnumerable.inject(list, init, methodName);
  }

  public E inject(RubyEnumerable.InjectBlock<E> block) {
    return RubyEnumerable.inject(list, block);
  }

  public <S> S inject(S init, RubyEnumerable.InjectWithInitBlock<E, S> block) {
    return RubyEnumerable.inject(this, init, block);
  }

  public <S> RubyArray<S> map(RubyEnumerable.TransformBlock<E, S> block) {
    return new RubyArray(RubyEnumerable.map(list, block));
  }

  public E max(Comparator<? super E> comp) {
    return RubyEnumerable.max(list, comp);
  }

  public <S> E maxBy(Comparator<? super S> comp, RubyEnumerable.TransformBlock<E, S> block) {
    return RubyEnumerable.maxBy(list, comp, block);
  }

  public E min(Comparator<? super E> comp) {
    return RubyEnumerable.min(list, comp);
  }

  public <S> E minBy(Comparator<? super S> comp, RubyEnumerable.TransformBlock<E, S> block) {
    return RubyEnumerable.minBy(list, comp, block);
  }

  public RubyArray<E> minmax(Comparator<? super E> comp) {
    return new RubyArray(RubyEnumerable.minmax(list, comp));
  }

  public <S> RubyArray<E> minmaxBy(Comparator<? super S> comp, RubyEnumerable.TransformBlock<E, S> block) {
    return new RubyArray(RubyEnumerable.minmaxBy(list, comp, block));
  }

  public RubyArray<RubyArray<E>> partition(RubyEnumerable.BooleanBlock<E> block) {
    RubyArray<E> trueList = new RubyArray();
    RubyArray<E> falseList = new RubyArray();
    for (E item : list) {
      if (block.yield(item)) {
        trueList.add(item);
      } else {
        falseList.add(item);
      }
    }
    RubyArray<RubyArray<E>> rubyArray = new RubyArray();
    rubyArray.add(trueList);
    rubyArray.add(falseList);
    return rubyArray;
  }

  public RubyArray<RubyArray<E>> permutation() {
    RubyArray<RubyArray<E>> perms = new RubyArray();
    PermutationGenerator<E> pg = new PermutationGenerator<>(list);
    while (pg.hasMore()) {
      perms.add(new RubyArray<>(pg.nextPermutationAsList()));
    }
    return perms;
  }

  public RubyArray<RubyArray<E>> permutation(int n) {
    RubyArray<RubyArray<E>> perms = new RubyArray();
    if (n < 0) {
      return perms;
    } else if (n == 0) {
      perms.add(new RubyArray<E>());
      return perms;
    } else if (n > list.size()) {
      return perms;
    } else {
      RubyArray<RubyArray<E>> combs = combination(n);
      for (RubyArray<E> comb : combs) {
        PermutationGenerator<E> pg = new PermutationGenerator<>(comb);
        while (pg.hasMore()) {
          perms.add(new RubyArray<E>(pg.nextPermutationAsList()));
        }
      }
    }
    return perms.uniq();
  }

  public RubyArray<RubyArray<E>> permutation(int n, RubyEnumerable.ItemBlock<RubyArray<E>> block) {
    RubyArray<RubyArray<E>> perms = permutation(n);
    for (RubyArray<E> item : perms) {
      block.yield(item);
    }
    return perms;
  }

  public RubyArray<RubyArray<E>> repeatedPermutation(int n) {
    return permutation(n);
  }

  public RubyArray<RubyArray<E>> repeatedPermutation(int n, RubyEnumerable.ItemBlock<RubyArray<E>> block) {
    return permutation(n, block);
  }

  public boolean hasNone() {
    return RubyEnumerable.hasNone(list);
  }

  public boolean hasNone(RubyEnumerable.BooleanBlock<E> block) {
    return RubyEnumerable.hasNone(list, block);
  }

  public boolean hasOne() {
    return RubyEnumerable.hasOne(list);
  }

  public boolean hasOne(RubyEnumerable.BooleanBlock<E> block) {
    return RubyEnumerable.hasOne(list, block);
  }

  public E reduce(String methodName) {
    return inject(methodName);
  }

  public E reduce(E init, String methodName) {
    return inject(init, methodName);
  }

  public E reduce(RubyEnumerable.InjectBlock<E> block) {
    return inject(block);
  }

  public <S> S reduce(S init, RubyEnumerable.InjectWithInitBlock<E, S> block) {
    return inject(init, block);
  }

  public RubyArray<E> reject(RubyEnumerable.BooleanBlock block) {
    return new RubyArray(RubyEnumerable.reject(list, block));
  }

  public void reverseEach(RubyEnumerable.ItemBlock block) {
    RubyEnumerable.reverseEach(list, block);
  }

  public RubyArray<E> select(RubyEnumerable.BooleanBlock block) {
    return findAll(block);
  }

  public RubyArray<E> sort(Comparator<? super E> comp) {
    return new RubyArray(RubyEnumerable.sort(list, comp));
  }

  public <S> RubyArray<E> sortBy(Comparator<? super S> comp, RubyEnumerable.TransformBlock<E, S> block) {
    return new RubyArray(RubyEnumerable.sortBy(list, comp, block));
  }

  public RubyArray<RubyArray<E>> sliceBefore(String regex) {
    RubyArray<RubyArray<E>> rubyArray = new RubyArray();
    Pattern pattern = Pattern.compile(regex);
    RubyArray<E> group = null;
    for (E item : list) {
      if (group == null) {
        group = new RubyArray();
        group.add(item);
      } else if (pattern.matcher(item.toString()).find()) {
        rubyArray.add(group);
        group = new RubyArray();
        group.add(item);
      } else {
        group.add(item);
      }
    }
    if (group != null && !group.isEmpty()) {
      rubyArray.add(group);
    }
    return rubyArray;
  }

  public RubyArray<RubyArray<E>> sliceBefore(RubyEnumerable.BooleanBlock block) {
    RubyArray<RubyArray<E>> rubyArray = new RubyArray();
    RubyArray<E> group = null;
    for (E item : list) {
      if (group == null) {
        group = new RubyArray();
        group.add(item);
      } else if (block.yield(item)) {
        rubyArray.add(group);
        group = new RubyArray();
        group.add(item);
      } else {
        group.add(item);
      }
    }
    if (group != null && !group.isEmpty()) {
      rubyArray.add(group);
    }
    return rubyArray;
  }

  public RubyArray<E> take(int n) {
    return new RubyArray(RubyEnumerable.take(list, n));
  }

  public RubyArray<E> takeWhile(RubyEnumerable.BooleanBlock block) {
    return new RubyArray(RubyEnumerable.takeWhile(list, block));
  }

  public RubyArray<E> toA() {
    return this;
  }

  public RubyArray<E> toAry() {
    return this;
  }

  public String toS() {
    return list.toString();
  }

  @Override
  public int size() {
    return list.size();
  }

  @Override
  public boolean isEmpty() {
    return list.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return list.contains(o);
  }

  @Override
  public Iterator<E> iterator() {
    return list.iterator();
  }

  @Override
  public Object[] toArray() {
    return list.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return list.toArray(a);
  }

  @Override
  public boolean add(E e) {
    return list.add(e);
  }

  @Override
  public boolean remove(Object o) {
    return list.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return list.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    return list.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends E> c) {
    return list.addAll(c);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return list.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return list.retainAll(c);
  }

  @Override
  public void clear() {
    list.clear();
  }

  @Override
  public E get(int index) {
    return list.get(index);
  }

  @Override
  public E set(int index, E element) {
    return list.set(index, element);
  }

  @Override
  public void add(int index, E element) {
    list.add(index, element);
  }

  @Override
  public E remove(int index) {
    return list.remove(index);
  }

  @Override
  public int indexOf(Object o) {
    return list.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return list.lastIndexOf(o);
  }

  @Override
  public ListIterator<E> listIterator() {
    return list.listIterator();
  }

  @Override
  public ListIterator<E> listIterator(int index) {
    return list.listIterator(index);
  }

  @Override
  public List<E> subList(int fromIndex, int toIndex) {
    return list.subList(fromIndex, toIndex);
  }

  @Override
  public String toString() {
    return list.toString();
  }
}
