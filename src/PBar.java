public class PBar {

  private int calls; // number of calls to determine if refresh is called the first time
  private int width; // the width of the progress bar inclusive percent

  private int current; // the current value of the progress bar
  private int last;    // the value of the progress bar during the last refresh
  private int total;   // the maximum value that represents 100%

  private long prevtime; // the last time (in millis) refresh was called

  private static final long MIN_DELAY = 100; // the minimum time between two refresh to prevent screen flicker


  // short demo program
  public static void main(String[] args) {
    PBar p;
    
    System.out.println("Test 01: 10.000 Items in 30 sec");
    p= new PBar(10000);
    for(int i=0; i<=10000; i++) {
      p.setCurrent(i);
      p.refresh();
      try {
        Thread.sleep(5);
      }
      catch(java.lang.InterruptedException e) {
      }
    }

    System.out.println("Test 02: 300 Items in 6 sec");
    p= new PBar(300);
    for(int i=0; i<=300; i++) {
      p.setCurrent(i);
      p.refresh();
      try {
        Thread.sleep(20);
      }
      catch(java.lang.InterruptedException e) {
      }
    }
  }

  public PBar(int total) {
    this.total = total;
    this.current = 0;
    this.last = -1;
    this.calls = 0;
    this.prevtime = 0;

    String columns = System.getenv("COLUMNS");
    if(columns == null) {
      this.width = 79;
    }
    else {
      try {
        this.width = Integer.parseInt(columns) - 1;
      }
      catch (NumberFormatException e) {
        this.width = 79;
      }
    }
  }

  private void setCurrent(int current) {
    if(current > total) {
      this.current = this.total;
    }
    else {
      this.current = current;
    }
  }

  private void refresh() {
    double frac = (double)current / (double)total;
    int totalpoints = width - 9;
    int points = (int)(frac * totalpoints);
    if (calls > 0 && ((System.currentTimeMillis() - prevtime) < MIN_DELAY) && current < total) {
      return;
    }

    calls++;

    if(current != last) {
      if (totalpoints < 1) {
        System.out.print(String.format("\r%5.1f%%", frac * 100f));
      }
      else {
        String bar;
        if(points == totalpoints) {
          bar = new String(new char[totalpoints]).replace('\0', '=');
        }
        else if(points == 1) {
          bar = ">";
        }
        else if(points == 0) {
          bar = "";
        }
        else {
          bar = new String(new char[points-1]).replace("\0", "=") + ">";
        }

        String format = String.format("\r[%%-%ds] %%5.1f%%%%", totalpoints);
        System.out.print(String.format(format, bar, frac * 100f));
      }
    }
    if(current == total) {
      System.out.println("");
    }
    prevtime = System.currentTimeMillis();
    last = current;
  }
}
