package payoneerHomeTask;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Process extends TimerTask {
  private static InMemoryDB inMemoryDb;
  private static Integer BULK_SIZE_TO_PROCESS = 3;
  private static final Logger logger = Logger.getLogger(Process.class.getName());

  public Process() {
    inMemoryDb = InMemoryDB.getInstance();
  }

  public void run() {
    List<Message> msgsToProcess = inMemoryDb.queryBulkToProcess(BULK_SIZE_TO_PROCESS);
    if (msgsToProcess.isEmpty()) {
      logger.info("there are no messages to process");
    }
    for (Message msgToProcess : msgsToProcess) {
      try {
        TimeUnit.SECONDS.sleep(3);
        msgToProcess.setStatus(Message.Status.Complete);
        logger.info("just processed message id " + msgToProcess.getId());
      } catch (InterruptedException e) {
        msgToProcess.setStatus(Message.Status.Error);
      }
      inMemoryDb.insert(msgToProcess.getId(), msgToProcess);
    }
  }
}
