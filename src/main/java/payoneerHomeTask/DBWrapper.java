package payoneerHomeTask;

import java.util.List;

public interface DBWrapper {

  /**
   * This method will execute the query
   * "SELECT * FROM messages WHERE Status = "Accepted" limit" + bulkNum
   */
  List<Message> queryBulkToProcess(int bulkNum);

  /**
   * This method will execute the insert query
   * "INSERT OR REPLACE into messages values (id, msg.status, msg.data);"
   */
  void insert(String id, Message msg);

  /**
   * This method will execute the query
   * "SELECT status FROM messages WHERE msgId = " + id;"
   */
  Message get(String id);

}
