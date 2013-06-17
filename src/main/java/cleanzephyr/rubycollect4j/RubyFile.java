package cleanzephyr.rubycollect4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class RubyFile extends RubyIO {

  public static RubyFile open(String path) {
    RubyFile rf = null;
    try {
      rf = new RubyFile(new File(path), Mode.R);
    } catch (FileNotFoundException ex) {
      Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
    }
    return rf;
  }

  public static RubyFile open(String path, String mode) {
    RubyFile rf = null;
    try {
      rf = new RubyFile(new File(path), mode);
    } catch (FileNotFoundException ex) {
      Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
    }
    return rf;
  }

  private RubyFile(File file, Mode mode) throws FileNotFoundException,
      IOException {
    super(file, mode);
  }

  private RubyFile(File file, String mode) throws FileNotFoundException,
      IOException {
    super(file, Mode.getMode(mode));
  }

  public static void main(String[] args) {
    RubyFile rf = RubyFile.open("/Users/WMW/Desktop/test", "a+");
    rf.puts("hahaha");
    rf.puts("hsfaha");
    System.out.println(rf.read());
  }

}
