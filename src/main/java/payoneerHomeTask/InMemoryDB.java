package payoneerHomeTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryDB implements DBWrapper {
  private ConcurrentHashMap<String, Message> messagesTable;
  private static InMemoryDB inMemoryDb;

  private InMemoryDB() {
    messagesTable = new ConcurrentHashMap<>();
  }

  static {
    inMemoryDb = new InMemoryDB();
  }

  static InMemoryDB getInstance() {
    return inMemoryDb;
  }

  public Message get(String id) {
    return messagesTable.get(id);
  }

  public List<Message> queryBulkToProcess(int bulkNum) {
    ArrayList<Message> msgsToProcess = new ArrayList<>();
    for (Message msg : messagesTable.values()) {
      if (bulkNum == 0) {
        return msgsToProcess;
      }
      if (msg.status.equals(Message.Status.Accepted)) {
        msgsToProcess.add(msg);
        bulkNum--;
      }
    } return msgsToProcess;
  }

  public void insert(String id, Message msg) {
    messagesTable.put(id, msg);
  }

}
