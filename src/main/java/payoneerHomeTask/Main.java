package payoneerHomeTask;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

  public static void main(String[] args) {
    int coreCount = Runtime.getRuntime().availableProcessors();
    ExecutorService pools = Executors.newFixedThreadPool(coreCount);
    Runnable server = new MyServer();

    TimerTask process = new payoneerHomeTask.Process();
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(process, new Date(),1000);

    pools.execute(server);
  }
}
