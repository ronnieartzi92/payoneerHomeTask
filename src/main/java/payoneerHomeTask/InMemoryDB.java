package payoneerHomeTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryDB implements DBWrapper {
  private final ConcurrentHashMap<String, Message> messagesTable;
  private static final InMemoryDB inMemoryDb;

  private InMemoryDB() {
    messagesTable = new ConcurrentHashMap<>();
  }

  static {
    inMemoryDb = new InMemoryDB();
  }

  public static InMemoryDB getInstance() {
    return inMemoryDb;
  }

  public Message get(String id) {
    return messagesTable.get(id);
  }

  public List<Message> queryBulkToProcess(int bulkSize) {
    ArrayList<Message> msgsToProcess = new ArrayList<>();
    for (Message msg : messagesTable.values()) {
      if (bulkSize == 0) {
        return msgsToProcess;
      }
      if (msg.getStatus().equals(Message.Status.Accepted)) {
        msgsToProcess.add(msg);
        bulkSize--;
      }
    } return msgsToProcess;
  }

  public void insert(String id, Message msg) {
    messagesTable.put(id, msg);
  }

}
