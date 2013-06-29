package net.sf.rubycollect4j;

import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("serial")
public final class RubyDate extends Date {

  public RubyDate(Date date) {
    setTime(date.getTime());
  }

  public DateShifter add(int ymd) {
    return new DateShifter(this).add(ymd);
  }

  public class DateShifter {

    private final Date date;
    private int ymdhsm;

    public DateShifter(Date date) {
      this.date = date;
    }

    public DateShifter add(int ymdhsm) {
      this.ymdhsm = ymdhsm;
      return this;
    }

    public RubyDate millisecs() {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(Calendar.MILLISECOND, ymdhsm);
      return new RubyDate(c.getTime());
    }

    public RubyDate secs() {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(Calendar.SECOND, ymdhsm);
      return new RubyDate(c.getTime());
    }

    public RubyDate mins() {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(Calendar.MINUTE, ymdhsm);
      return new RubyDate(c.getTime());
    }

    public RubyDate hours() {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(Calendar.HOUR_OF_DAY, ymdhsm);
      return new RubyDate(c.getTime());
    }

    public RubyDate days() {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(Calendar.DAY_OF_MONTH, ymdhsm);
      return new RubyDate(c.getTime());
    }

    public RubyDate months() {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(Calendar.MONTH, ymdhsm);
      return new RubyDate(c.getTime());
    }

    public RubyDate years() {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(Calendar.YEAR, ymdhsm);
      return new RubyDate(c.getTime());
    }

  }

}
