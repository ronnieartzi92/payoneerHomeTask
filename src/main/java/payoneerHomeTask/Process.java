package payoneerHomeTask;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Process extends TimerTask {
  private static DBWrapper dbWrapper;
  private static Integer BULK_TO_PROCESS = 3;
  private static final Logger logger = Logger.getLogger(Process.class.getName());

  Process() {
    dbWrapper = DBWrapper.getInstance();
  }

  public void run() {
    ArrayList<DBWrapper.Message> msgsToProcess = dbWrapper.queryBulkToProcess(BULK_TO_PROCESS);
    if (msgsToProcess.isEmpty()) {
      logger.info("there is no messages to process");
    }
    for (DBWrapper.Message msgToProcess : msgsToProcess) {
      try {
        TimeUnit.SECONDS.sleep(3);
        msgToProcess.setStatus(DBWrapper.Message.Status.Complete);
        logger.info("just processed message id " + msgToProcess.getId());
      } catch (InterruptedException e) {
        msgToProcess.setStatus(DBWrapper.Message.Status.Error);
      }
      dbWrapper.insert(msgToProcess.id, msgToProcess);
    }
  }
}
