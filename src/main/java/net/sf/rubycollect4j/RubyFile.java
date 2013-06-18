package net.sf.rubycollect4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static net.sf.rubycollect4j.RubyIO.Mode.R;


public final class RubyFile extends RubyIO {

  public static RubyFile open(File file) {
    RubyFile rf = null;
    try {
      rf = new RubyFile(file, R);
    } catch (FileNotFoundException ex) {
      Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
    }
    return rf;
  }

  public static RubyFile open(File file, String mode) {
    RubyFile rf = null;
    try {
      rf = new RubyFile(file, mode);
    } catch (FileNotFoundException ex) {
      Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(RubyFile.class.getName()).log(Level.SEVERE, null, ex);
    }
    return rf;
  }

  public static RubyFile open(String path) {
    return open(new File(path));
  }

  public static RubyFile open(String path, String mode) {
    return open(new File(path), mode);
  }

  private RubyFile(File file, Mode mode) throws FileNotFoundException,
      IOException {
    super(file, mode);
  }

  private RubyFile(File file, String mode) throws FileNotFoundException,
      IOException {
    super(file, Mode.fromString(mode));
  }

  public static RubyEnumerator<String> foreach(File file) {
    return open(file).eachLine();
  }

  public static RubyEnumerator<String> foreach(String path) {
    return foreach(new File(path));
  }

}
