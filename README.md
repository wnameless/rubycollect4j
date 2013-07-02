rubycollect4j
=============
Ruby Collections for Java.

The rubycollect4j implements all methods refer to Ruby Array, Hash, Enumerable and Range.
It also implements parts of Ruby Dir, File and Date methods.

For further information, please visit http://ruby-doc.org website.

For more documentation, please read JavaDoc on http://rubycollect4j.sf.net website.

Installation with Maven:
``` xml
<dependency>
  <groupId>net.sf.rubycollect4j</groupId>
	<artifactId>rubycollect4j</artifactId>
	<version>1.2.0</version>
</dependency>
```

Java 8 with rubycollect4j:
``` java
// Sort the characters by its frequency based on the word 'Mississippi' case-insensitively
RubyArray<String> word = ra("Mississippi".split("(?!^)"));

String result = word.map((c) -> {
  return c.toLowerCase();
}).sortBy((c) -> {
  return word.count(c);
}).uniq().join();

p(result);
// Output: "mpis"
```

Java 6 with rubycollect4j:
``` java
// Sort the characters by its frequency based on the word 'Mississippi' case-insensitively
final RubyArray<String> word = newRubyArray("Mississippi".split("(?!^)"));

String result = word.map(new TransformBlock<String, String>() {
  public String yield(String c) {
    return c.toLowerCase();
  }
}).sortBy(new TransformBlock<String, Integer>() {
  public Integer yield(String c) {
    return word.count(c);
  }
}).uniq().join();

p(result);
// Output: "mpis"
```

Please add following lines before running examples:
```java
import static net.sf.rubycollect4j.RubyCollections.*;
import static net.sf.rubycollect4j.RubyKernel.p;
```

Demo ra():
```java
p( ra(1, 2, 3, 4) );              // Output: [1, 2, 3, 4]
p( ra(ra(1, 2)) );                // Output: [[1, 2]]
List<Integer> list = new ArrayList<Integer>();
list.add(1);
p( ra(list) );                    // Output: [1]
Map<Integer, String> map = new LinkedHashMap<Integer, String>();
map.put(1, "a");
map.put(1, "b");
// Any Iterable or Iterator object can be converted into RubyArray.
p( ra(map.values) );              // Output: [a, b]
// RubyArray is also an List.
p( ra(1, 2, 3) instanceof List ); // Output: true
```

```java
// By default, RubyArray is just a wrapper to an existed List.
// You can make a defensive copy by following codes.
import static net.sf.rubycollect4j.RubyArray.newRubyArray;

List<Integer> list = new ArrayList<Integer>();
list.add(1);
RubyArray<Integer> ra = newRubyArray(list, true);
```

Demo rh(), hp() & Hash():
```java
p( rh("a", 1, "b" ,2) );               // Output: {a=1, b=2}
// hp() simply creates an Entry and the word 'hp' means hash pair
p( rh(hp("a", 1), hp("b" ,2)) );       // Output: {a=1, b=2}
p( Hash(ra(hp("a", 1), hp("b" ,2))) ); // Output: {a=1, b=2}
// RubyHash is also a Map.
p( rh(1, 2, 3, 4) instanceof Map );    // Output: true
```

```java
// By default, RubyHash makes a copy of input Map.
// You can only wrap a LinkedHashMap up by following codes, because the keys of RubyHash need to be ordered.
import static net.sf.rubycollect4j.RubyHash.newRubyHash;

LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
map.put(1, "a");
RubyHash<Integer, String> rh = newRubyHash(map, false);
```

Demo newRubyEnumerable():
```java
import static net.sf.rubycollect4j.RubyEnumerable.newRubyEnumerable;

Map<String, Long> map = new LinkedHashMap<String, Long>() {{ put("a", 1L); put("b", 2L); put("c", 3L); }};
// Any Iterable object can be wrapped up into RubyEnumerable.
RubyEnumerable<Entry<String, Long>> re = newRubyEnumerable(map.entrySet());
p( re.drop(1).toA() );       // Output: [b=2, c=3]
// RubyEnumerable is also an Iterable.
p( re instanceof Iterable ); // Output: true
```

```java
// You can simply make your class inherit all the methods by extending RubyEnumerable.
public class YourIterableClass<E> extends RubyEnumerable<E> {
  public YourIterableClass(Iterable<E> iter) { super(iter); }
}
```

Demo newRubyEnumerator():
```java
import static net.sf.rubycollect4j.RubyEnumerator.newRubyEnumerator;

Map<String, Long> map = new LinkedHashMap<String, Long>() {{ put("a", 1L); put("b", 2L); put("c", 3L); }};
// Any Iterable or Iterator object can be converted into RubyEnumerator.
RubyEnumerator<Entry<String, Long>> re = newRubyEnumerator(map.entrySet());
re = newRubyEnumerator(map.entrySet().iterator());
// RubyEnumerator is much like RubyEnumerable, but it is both an Iterator and an Iterable.
p( re instanceof Iterator ); // Output: true
p( re instanceof Iterable ); // Output: true
// It can 'peek' and 'rewind'.
p( re.peek() );              // Output: a=1
p( re.next() );              // Output: a=1
p( re.next() );              // Output: b=2
p( re.peek() );              // Output: c=3
re.rewind();
p( re.next() );              // Output: a=1
```

Demo range():
```java
p( range(1, 5).toA() );                                   // Output: [1, 2, 3, 4, 5]
p( range("Z", "AB").toA() );                              // Output: [Z, AA, AB]
p( range(1.08, 1.1).toA() );                              // Output: [1.08, 1.09, 1.10]
p( range("1.08", "1.1").toA() );                          // Output: [1.08, 1.09, 1.10]
p( range(date(2013, 06, 30), date(2013, 07, 01)).toA() ); // Output: [Sun Jun 30 00:00:00 CST 2013, Mon Jul 01 00:00:00 CST 2013]
```

Demo date():
```java
p( date(2013, 7, 1).add(10).days() );          // Output: Thu Jul 11 00:00:00 CST 2013
p( date(2013, 7, 1, 21).minus(30).minutes() ); // Output: Mon Jul 01 20:30:00 CST 2013
p( date(2013, 7, 7, 16, 15, 14).endOfDay() );  // Output: Sun Jul 07 23:59:59 CST 2013
p( date(2013, 7, 11).beginningOfWeek() );        // Output: Sun Jul 07 00:00:00 CST 2013
p( RubyDate.today() );                         // Output: date of today
p( RubyDate.yesterday() );                     // Output: date of yesterday
Calendar c = Calendar.getInstance();
c.clear();
p( date(c.getTime()) );                          // Output: Thu Jan 01 00:00:00 CST 1970
```

Demo qw():
```java
p( qw("a bc def") ); // Output: [a, bc, def]
```

Demo qx():
```java
p( qx("echo", "hello") ); // Output: hello
```

Demo qr():
```java
p( qr("^\\d+$").matcher("123").find() ); // Output: true
```

Demo RubyDir:
```text
folder1
       /folder1-1
                 /file1-1-1
                 /file1-1-2
       /folder1-2
                 /file1-2-1
                 /file1-2-2
                 /file1-2-3
       /file1-1
       /file1-2
       /file1-3
       /file1-4
```

```java
p( RubyDir.glob("folder1/*").sort() );                 // Output: ["file1-1", "file1-2", "file1-3", "file1-4", "folder1-1", "folder1-2"]
p( RubyDir.glob(GLOB_DIR + "**/*1-*-1*").sort() );     // Output: ["folder1/folder1-1/file1-1-1", "folder1/folder1-2/file1-2-1"]
p( RubyDir.glob(GLOB_DIR + "folder1/*[2,3]").sort() ); // Output: ["file1-2", "file1-3", "folder1-2"]
```

Demo RubyFile:
```java
RubyFile rf = RubyFile.open("test.txt", "r+");
rf.puts("1");
rf.puts("2");
rf.puts("3");
rf.close();
p( RubyFile.foreach("test.txt").toA() ); // Output: [1, 2, 3]
```
