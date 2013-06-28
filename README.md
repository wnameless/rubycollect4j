rubycollect4j
=============
Ruby Collections for Java.

The rubycollect4j implements all methods refer to Ruby Array, Hash, Enumerable and Range.
It also implements parts of Ruby Dir & File methods.

For further information, please visit http://ruby-doc.org website.

For more documentation, please read JavaDoc on http://rubycollect4j.sf.net website.

Installation with Maven:
``` xml
<dependency>
  <groupId>net.sf.rubycollect4j</groupId>
	<artifactId>rubycollect4j</artifactId>
	<version>1.0.1</version>
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

Please add following line before running examples:
```java
import static net.sf.rubycollect4j.RubyCollections.*;
import static net.sf.rubycollect4j.RubyKernel.p;
```

Demo ra():
```java
p( ra(1, 2, 3, 4) ); // Output: [1, 2, 3, 4]
p( ra(ra(1, 2)) );   // Output: [[1, 2]]
List<Integer> list = new ArrayList<Integer>();
list.add(1);
p( ra(list) );       // Output: [1]
```

Demo rh() & Hash():
```java
p( rh("a", 1, "b" ,2) );               // Output: {a=1, b=2}
p( rh(hp("a", 1), hp("b" ,2)) );       // Output: {a=1, b=2}
p( Hash(ra(hp("a", 1), hp("b" ,2))) ); // Output: {a=1, b=2}
```

Demo range():
```java
p( range(1, 5).to_a );                                   // Output: [1, 2, 3, 4, 5]
p( range("Z", "AB").to_a );                              // Output: [Z, AA, AB]
p( range(1.08, 1.1).to_a );                              // Output: [1.08, 1.09, 1.10]
p( range("1.08", "1.1").to_a );                          // Output: [1.08, 1.09, 1.10]
p( range(date(2013, 06, 30), date(2013, 07, 01)).to_a ); // Output: [Sun Jun 30 00:00:00 CST 2013, Mon Jul 01 00:00:00 CST 2013]
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
