package payoneerHomeTask;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

class MyCache {
  private static final InMemoryDB inMemoryDb = InMemoryDB.getInstance();
  private static final MyCache cache = new MyCache();
  final private LoadingCache<String, Message.Status> statusCache;

  private MyCache() {
    statusCache = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(21, TimeUnit.MINUTES)
        .build(
            new CacheLoader<String, Message.Status>() {
              public Message.Status load(String id) { //after 21 minutes pull from db
                Message msgFromMemory = inMemoryDb.get(id);
                final Message.Status status = Objects.isNull(msgFromMemory) ?
                    Message.Status.NotFound : msgFromMemory.getStatus();
                return status;
              }
            }
        );
  }

  public static MyCache getInstance() {
    return cache;
  }

  Message.Status getStatus(String msgId) throws ExecutionException {
    return statusCache.get(msgId);
  }

  void insertStatus(String msgId, Message.Status status) {
    statusCache.put(msgId, status);
  }
}
