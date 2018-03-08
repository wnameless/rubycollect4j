[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.sf.rubycollect4j/rubycollect4j/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.sf.rubycollect4j/rubycollect4j)
[![codecov](https://codecov.io/gh/wnameless/rubycollect4j/branch/master/graph/badge.svg)](https://codecov.io/gh/wnameless/rubycollect4j)


rubycollect4j
=============
Ruby Collections for Java.

The rubycollect4j implements all methods refer to Ruby Array, Hash, Set, Enumerable, Enumerator, Range and String.
It also implements parts of Ruby Dir, File and Date methods.

For further information, please visit http://ruby-doc.org website.

Install v2.0.0(Java 1.8 required):
``` xml
<dependency>
  <groupId>net.sf.rubycollect4j</groupId>
  <artifactId>rubycollect4j</artifactId>
  <version>2.0.1</version>
</dependency>
```

Install v1.9.0(Java 1.6+):
``` xml
<dependency>
  <groupId>net.sf.rubycollect4j</groupId>
  <artifactId>rubycollect4j</artifactId>
  <version>1.9.0</version>
</dependency>
```

Java 8 with rubycollect4j v2.0.0+:
```java
// Sorts the characters by its frequency based on the word 'Mississippi' case-insensitively.
RubyString word = Ruby.String.of("Mississippi"); // Equivalent to new RubyString("Mississippi")
String result = word.map(String::toLowerCase).sortBy(c -> word.count(c)).uniq().join();

Ruby.Kernel.p(result);
// Output: "mpis"
```

Java 6 with rubycollect4j v1.9.x:
```java
// Finds 2 words which get the least and the most unique letters in upper case.
RubyArray<String> words =
  RubyFile.foreach("/usr/share/dict/web2") // Dictionary of Mac OS
    .minmaxBy(new TransformBlock<String, Integer>() {
      public Integer yield(String item) {
        return new RubyString(item).toA().uniq().count();
      }
    }).mapǃ("toUpperCase");

p(words);
// Output: [A, BLEPHAROCONJUNCTIVITIS]
```

## Examples for v2.0.x:

Since v2.0.0, Java 8 is required.

Class Ruby has been introduced.<br/>
It provides a super convenient way to access the numerous features of RubyCollect4J.<br/>
All static classes and methods under Ruby class are well documented, feel free to try them by yourself.

Demo Ruby:
```java
Ruby.Array.copyOf(Arrays.asList(1, 2, 3, 4)).minmax().join();
// "14"

Ruby.Hash.of("a", 1, "b", 2, "c", 3).transformValues(v -> v * 2);
// {"a"=2, "b"=4, "c"=6}

Ruby.Entry.of("xyz", 321);
// xyz=321

Ruby.Set.of(1, 2, 3, 4).divide(i -> i % 2 == 0);
// [[1, 3], [2, 4]]

Ruby.Enumerator.of(Arrays.asList(1, 2, 3, 4)).inject((x, y) -> x * y);
// 24

Ruby.LazyEnumerator.of(Arrays.asList(1,2,3,4)).cycle().first(10);
// [1, 2, 3, 4, 1, 2, 3, 4, 1, 2]

Ruby.String.of("abcde").tr("a-c", "z");
// "zzzde"

Ruby.Range.of(1, 100).sum();
// 5050

Ruby.Dir.glob("./**/*");
// [file_1, dir_1, ...]

Ruby.File.foreach("/usr/share/dict/web2").drop(99).first();
// "Abbadide"

Ruby.Date.today().onWeekdayʔ();
// true or false

Ruby.Object.isBlank(Collections.emptyList());
// true

Ruby.Kernel.p(new char[] { 'a', 'b', 'c' });
// ['a', 'b', 'c']
```

## v2.0.0 Changelog
Most of the functions of v1.9.x are also avaliable in v2.0.x.<br/>
Here is the comparision list of static method names:

|v2.0.x                     |v1.9.x                                   |
|---------------------------|-----------------------------------------|
|                           |(net.sf.rubycollect4j.RubyCollections.*) |
|Ruby.Array.create          |                                         |
|Ruby.Array.of              |ra                                       |
|Ruby.Array.copyOf          |newRubyArray                             |
|                           |                                         |
|Ruby.Hash.create           |Hash                                     |
|Ruby.Hash.of               |rh                                       |
|Ruby.Hash.copyOf           |newRubyHash                              |
|                           |                                         |
|Ruby.Entry.of              |hp                                       |
|                           |                                         |
|Ruby.Set.create            |                                         |
|Ruby.Set.of                |                                         |
|Ruby.Set.copyOf            |newRubySet                               |
|                           |                                         |
|Ruby.Enumerator.of         |                                         |
|Ruby.Enumerator.copyOf     |newRubyEnumerator                        |
|                           |                                         |
|Ruby.LazyEnumerator.of     |                                         |
|Ruby.LazyEnumerator.copyOf |newRubyLazyEnumerator                    |
|                           |                                         |
|Ruby.String.create         |                                         |
|Ruby.String.of             |rs                                       |
|                           |                                         |
|Ruby.Range.of              |range                                    |
|                           |                                         |
|Ruby.Date.of               |date                                     |
|                           |                                         |
|Ruby.Object.isBlank        |isBlank                                  |
|Ruby.Object.isPresent      |isNotBlank                               |
|                           |(end of RubyCollections.*)               |
|Ruby.File.open             |RubyFile.open                            |
|                           |                                         |
|Ruby.Dir.open              |RubyDir.open                             |
|                           |                                         |
|Ruby.Kernel.p              |RubyKernel.p                             |

RubyEnumerable has been changed from an abstract class into an interface due to the default methods feature of Java 8.

## Examples:

Since v1.9.0, RubyIterables & RubyStrings are added, they are simply utility classes just like java.util.Arrays.<br/>
They provide the Ruby style ways to manipulate any Iterable and String (or CharSequence).

Highly recommended to use with the @ExtensionMethod of [Project Lombok](https://projectlombok.org/).
```java
Iterable<Integer> ints = Arrays.asList(3, 6, 7, 2);
int max = RubyIterables.max(ints);
System.out.println(max);   // Output: 7

String trStr = RubyStrings.tr("Mississippi", "i", "I");
System.out.println(trStr); // Output: MIssIssIppI
```

Please add following lines before running examples:
```java
import static net.sf.rubycollect4j.RubyKernel.p;
```

Demo RubyArray:
```java
p( Ruby.Array.of(1, 2, 3, 4) );                         // Output: [1, 2, 3, 4]
p( Ruby.Array.of(Ruby.Array.of(1, 2)) );                           // Output: [[1, 2]]
List<Integer> list = new ArrayList<Integer>();
list.add(1);
p( Ruby.Array.of(list) );                               // Output: [1]
Map<Integer, String> map = new LinkedHashMap<Integer, String>();
map.put(1, "a");
map.put(2, "b");
// Any Iterable or Iterator object can be converted into RubyArray.
p( Ruby.Array.of(map.values) );                         // Output: [a, b]
// RubyArray is Comparable if the elements are Comparable.
p( Ruby.Array.of(Ruby.Array.of(3, 4), Ruby.Array.of(1, 2)).sort() );          // Output: [[1, 2], [3, 4]]
// RubyArray is both a List and a Ruby.Enumerable.
p( Ruby.Array.of(1, 2, 3) instanceof List );            // Output: true
p( Ruby.Array.of(1, 2, 3) instanceof Ruby.Enumerable ); // Output: true
```

```java
// By default, Ruby.Array.of() is just a wrapper to an existed List.
// You can make a defensive copy by following codes.
List<Integer> list = new ArrayList<Integer>();
list.add(1);
RubyArray<Integer> ra = Ruby.Array.copyOf(list);
```

Demo RubyHash:
```java
p(  Ruby.Hash.of("a", 1, "b" ,2) );                        // Output: {a=1, b=2}
Map<String, Long> map = new HashMap<String, Long>();
map.put("abc", 123L);
// Any Map can be converted into RubyHash.
p(  Ruby.Hash.copyOf(map) );                                   // Output: {abc=123}
// hp() simply creates an Entry and the word 'hp' means hash pair
p( Hash(Ruby.Array.of(Ruby.Entry.of("a", 1), Ruby.Entry.of("b" ,2))) );          // Output: {a=1, b=2}
p( Hash(Ruby.Hash.of("a", 1, "b", 2).toA()) );            // Output: {a=1, b=2}
// The Entry of RubyHash is Comparable if the type of the key elements is Comparable.
p(  Ruby.Hash.of(4, 3, 2, 1).sort() );                     // Output: [2=1, 4=3]
// RubyHash is both a Map and a Ruby.Enumerable.
p(  Ruby.Hash.of(1, 2, 3, 4) instanceof Map );             // Output: true
p(  Ruby.Hash.of(1, 2, 3, 4) instanceof Ruby.Enumerable ); // Output: true
```

```java
// By default, Ruby.Hash.copyOf() makes a copy of input Map.
// You can only wrap a LinkedHashMap up by following codes, because the keys of RubyHash need to be ordered.
LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
map.put(1, "a");
RubyHash<Integer, String> rh = Ruby.Hash.of(map);
```

Demo RubySet:
```java
p( Ruby.Set.of(1, 2, 3, 3) );                                // Output: [1, 2, 3]
List<Integer> list = new ArrayList<Integer>();
list.add(1);
list.add(1);
// Any Iterable object can be converted into RubySet
p( Ruby.Set.copyOf(list) );                                      // Output: [1]
// RubySet is Comparable if the elements are Comparable.
p( Ruby.Set.of(newRubySet(3, 4), newRubySet(1, 2)).sort() ); // Output: [[1, 2], [3, 4]]
// RubySet is both a Set and a Ruby.Enumerable.
p( Ruby.Set.of(1, 2, 3, 3) instanceof Set );                 // Output: true
p( Ruby.Set.of(1, 2, 3, 3) instanceof Ruby.Enumerable );     // Output: true
```

```java
// You can only wrap a LinkedHashSet up by following codes, because the elements of RubySet need to be ordered.
LinkedHashSet<Integer> set = new LinkedHashSet<Integer>();
set.add(1);
RubySet<Integer> rubySet = Ruby.Set.of(set);
```

Demo RubyString:
```java
// RubyString can be created from any Object.
p( Ruby.String.of(1000L).count("0") );                  // Output: 3
// RubyString is also a RubyEnumerable of each character(as String).
p( Ruby.String.of("abc").map("codePointAt", 0) );                   // Output: [97, 98, 99]
// RubyString implements fluent interface.
// After multiple actions, you can turn it to a Java String by calling toS().
p( Ruby.String.of("ABC").chop().capitalize().concat("001").toS() ); // Output: Ab001
// RubyString is both a CharSequence and a Ruby.Enumerable.
p( Ruby.String.of("abc") instanceof CharSequence );                 // Output: true
p( Ruby.String.of("abc") instanceof Ruby.Enumerable );              // Output: true
```

Demo abstract RubyEnumerable class:
```java
// You can simply make your class inherit all the RubyEnumerable methods by extending it.
public class YourIterableClass<E> extends RubyEnumerable<E> {
  private final Iterable<E> iter;
  
  public YourIterableClass(Iterable<E> iter) { this.iter = iter; }
  
  @Override
  protected Iterable<E> getIterable() { return iter; }
}

// The class which extends RubyEnumerable becomes a Ruby.Enumerable.
p( new YourIterableClass(iter) instanceof Ruby.Enumerable ); // Output: true
```

Demo RubyEnumerator:
```java
Map<String, Long> map = new LinkedHashMap<String, Long>() {{ put("a", 1L); put("b", 2L); put("c", 3L); }};
// Any Iterable object can be wrapped into RubyEnumerator.
RubyEnumerator<Entry<String, Long>> re = Ruby.Enumerator.of(map.entrySet());
// RubyEnumerator is much like RubyEnumerable, but it is both an Iterator and an Iterable.
p( re instanceof Ruby.Enumerator ); // Output: true
p( re instanceof Ruby.Enumerable ); // Output: true
p( re instanceof Iterator );        // Output: true
p( re instanceof Iterable );        // Output: true
// It can 'peek' and 'rewind'.
p( re.peek() );                     // Output: a=1
p( re.next() );                     // Output: a=1
p( re.next() );                     // Output: b=2
p( re.peek() );                     // Output: c=3
re.rewind();
p( re.next() );                     // Output: a=1
```

```java
// By default, RubyEnumerator.of() is just a wrapper to an existed Iterable.
// You can make a defensive copy by following codes.
Map<String, Long> map = new LinkedHashMap<String, Long>() {{ put("a", 1L); put("b", 2L); put("c", 3L); }};
RubyEnumerator<Entry<String, Long>> re = Ruby.Enumerator.copyOf(map.entrySet());
```

Demo RubyLazyEnumerator:
```java
RubyLazyEnumerator rle = Ruby.LazyEnumerator.of(Arrays.asList(1, 2, 3, 4));
// RubyLazyEnumerator is not a Ruby.Enumerable, but it is a Ruby.LazyEnumerator.
p( rle.drop(1) instanceof Ruby.LazyEnumerator );    // Output: true
p( rle instanceof Iterator );                       // Output: true
p( rle instanceof Iterable );                       // Output: true
p( rle.drop(1).toA() );                             // Output: [2, 3, 4]
// A RubyLazyEnumerator can also be created by RubyArray#lazy.
p( ra(1, 2, 3, 4).lazy().cycle().drop(6).first() ); // Output: 3
```

```java
// By default, RubyLazyEnumerator.of() is just a wrapper to an existed Iterable.
// You can make a defensive copy by following codes.
Map<String, Long> map = new LinkedHashMap<String, Long>() {{ put("a", 1L); put("b", 2L); put("c", 3L); }};
RubyLazyEnumerator<Entry<String, Long>> re = Ruby.LazyEnumerator.copyOf(map.entrySet());
```

Demo RubyRange:
```java
p( Ruby.Range.of(1, 5).toA() );                                   // Output: [1, 2, 3, 4, 5]
p( Ruby.Range.of("Z", "AB").toA() );                              // Output: [Z, AA, AB]
p( Ruby.Range.of("ZZ-999", "AAA-001").toA() );                    // Output: [ZZ-999, AAA-000, AAA-001]
p( Ruby.Range.of(1.08, 1.1).toA() );                              // Output: [1.08, 1.09, 1.10]
p( Ruby.Range.of("1.08", "1.1").toA() );                          // Output: [1.08, 1.09, 1.10]
p( Ruby.Range.of(Ruby.Date.of(2013, 06, 30), Ruby.Date.of(2013, 07, 01)).toA() ); // Output: [Sun Jun 30 00:00:00 CST 2013, Mon Jul 01 00:00:00 CST 2013]

// RubyRange accepts any Temporal objects and TemporalUnit(ChronoUnit) of Java 8
p( Ruby.Range.of(LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChronoUnit.DAYS).toA().size() )    // Output: 8
p( Ruby.Range.of(LocalDateTime.now(), LocalDateTime.now().plusDays(7), ChronoUnit.SECONDS).toA().size() ) // Output: 604801
```

Demo RubyDate:
```java
p( Ruby.Date.of(2013, 7, 1).add(10).days() );          // Output: Thu Jul 11 00:00:00 CST 2013
p( Ruby.Date.of(2013, 7, 1, 21).minus(30).minutes() ); // Output: Mon Jul 01 20:30:00 CST 2013
p( Ruby.Date.of(2013, 7, 7, 16, 15, 14).endOfDay() );  // Output: Sun Jul 07 23:59:59 CST 2013
p( Ruby.Date.of(2013, 7, 11).beginningOfWeek() );      // Output: Sun Jul 07 00:00:00 CST 2013
p( RubyDate.today() );                         // Output: date of today
p( RubyDate.yesterday() );                     // Output: date of yesterday
Calendar c = Calendar.getInstance();
c.clear();
p( Ruby.Date.of(c.getTime()) );                        // Output: Thu Jan 01 00:00:00 CST 1970
p( Ruby.Date.of(2014, 5, 1).beginningOfQuarter() );    // Output: Tue Apr 01 00:00:00 CST 2014
```

Demo RubyObject:
```java
RubyHash<String, String> profile = Ruby.Hash.of("Name", "John Doe", "Gender", "Male", "Birthday", "2001/10/01");
// Assumes Object person has 3 setters: setName, setGender, setBirthday.
profile.each((key, value) -> Ruby.Object.send(person, "set" + key, value));
// As you can see, Ruby.Object.send() is a wrapper to Java Reflection.
```

Demo RubyLiterals:
```java
p( Ruby.Literals.qw("a bc def") ); // Output: [a, bc, def]
p( Ruby.Literals.qx("echo", "hello") ); // Output: hello
p( Ruby.Literals.qr("^\\d+$").matcher("123").find() ); // Output: true
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
p( Ruby.Dir.glob("folder1/*").sort() );                 // Output: [file1-1, file1-2, file1-3, file1-4, folder1-1, folder1-2]
p( Ruby.Dir.glob(GLOB_DIR + "**/*1-*-1*").sort() );     // Output: [folder1/folder1-1/file1-1-1, folder1/folder1-2/file1-2-1]
p( Ruby.Dir.glob(GLOB_DIR + "folder1/*[2,3]").sort() ); // Output: [file1-2, file1-3, folder1-2]
```

Demo RubyFile:
```java
RubyFile rf = Ruby.File.open("test.txt", "r+");
rf.puts("a");
rf.puts("bc");
rf.puts("def");
rf.close();
rf = RubyFile.open("test.txt", "r");
p( rf.eachLine().toA() );                         // Output: [a, bc, def]
rf.close();
// The method foreach() closes the stream automatically.
Ruby.File.foreach("test.txt", new Block<String>() {
  public void yield(String item) {
    System.out.print( item );
  }
});                                               // Output: abcdef
p( Ruby.File.join("/home/", "/ruby", "collect") ); // Output: "/home/ruby/collect"
```
