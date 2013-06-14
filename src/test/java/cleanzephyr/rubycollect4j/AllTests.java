package cleanzephyr.rubycollect4j;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ RubyArrayTest.class, RubyCollectionsTest.class,
    RubyEnumerableTest.class, RubyHashTest.class, RubyIOTest.class })
public class AllTests {}
