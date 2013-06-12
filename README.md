rubycollect4j
=============
Ruby Collections for Java 8.

The rubycollect4j implements all methods refer to Ruby Array, Hash and Enumerable.

For further information, please visit http://ruby-doc.org website.

For more documentation, please read JavaDoc.

With Java 8:
``` java
// Sort the characters by its frequency based on the word 'Mississippi' case-insensitively
RubyArray<String> word = ra("Mississippi".split("(?!^)"));

String result = word.map((c) -> {
  return c.toLowerCase();
}).sortBy((c) -> {
  return word.count(c);
}).uniq().join();

puts(result);
// Output: "mpis"
```

With Java 6:
``` java
// Sort the characters by its frequency based on the word 'Mississippi' case-insensitively
RubyArray<String> word = newRubyArray("Mississippi".split("(?!^)"));

String result = word.map(new ItemTransformBlock<String, String>() {
  public String yield(String c) {
    return c.toLowerCase();
  }
}).sortBy(new ItemTransformBlock<String, Integer>() {
  public Integer yield(String c) {
    return word.count(c);
  }
}).uniq().join();

puts(result);
// Output: "mpis"
```
