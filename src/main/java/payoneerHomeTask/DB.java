package payoneerHomeTask;

import java.util.ArrayList;

public interface DB {

  /**
   * This method will execute the query
   * "SELECT * FROM messages WHERE Status = "Accepted" limit" + bulkNum
   */
  ArrayList<DBWrapper.Message> queryBulkToProcess(Integer bulkNum);

  /**
   * This method will execute the insert query
   * "INSERT OR REPLACE into messages values (id, msg.status, msg.data);"
   */
  void insert(String id, DBWrapper.Message msg);

  /**
   * This method will execute the query
   * "SELECT status FROM messages WHERE msgId = " + id;"
   */
  DBWrapper.Message get(String id);

}
