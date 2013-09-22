package net.sf.rubycollect4j.packer;

import static com.google.common.collect.Lists.newArrayList;

import org.junit.Test;

public class PackerTest {

  @Test(expected = IllegalArgumentException.class)
  public void testPackWithInvalidSymbol() {
    Packer.pack(newArrayList(), "c0");
  }

}
