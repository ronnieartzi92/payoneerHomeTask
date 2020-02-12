package payoneerHomeTask;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


class DBWrapper implements DB {
  static ConcurrentHashMap<String, Message> dataStruc;
  static DBWrapper dbWrapper;

  static class Message {
    String id;
    String data;
    Status status;

    Message(String id, String data, Status status) {
      this.id = id;
      this.data = data;
      this.status = status;
    }

    enum Status {
      Accepted,
      Processing,
      Error,
      Complete,
      NotFound
    }

    String getId() {
      return id;
    }

    String getData() {
      return data;
    }

    Status getStatus() {
      return status;
    }

    void setId(String id) {
      this.id = id;
    }

    void setData(String data) {
      this.data = data;
    }

    void setStatus(Status status) {
      this.status = status;
    }
  }

  private DBWrapper() {
    dataStruc = new ConcurrentHashMap<>();
  }

  static {
    dbWrapper = new DBWrapper();
  }

  static DBWrapper getInstance() {
    return dbWrapper;
  }

  public Message get(String id) {
    return dataStruc.get(id);
  }

  public ArrayList<Message> queryBulkToProcess(Integer bulkNum) {
    ArrayList<Message> msgsToProcess = new ArrayList<>();
    for (Message msg : dataStruc.values()) {
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
    dataStruc.put(id, msg);
  }

}
