package payoneerHomeTask;

import ratpack.handling.Context;
import ratpack.path.PathTokens;
import ratpack.server.RatpackServer;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

class MyServer implements Runnable {
  private static final InMemoryDB inMemoryDb = InMemoryDB.getInstance();
  private static final MyCache cache = MyCache.getInstance();
  private static final Logger logger = Logger.getLogger(Process.class.getName());

  private void ingest(Context ctx) {
    PathTokens pathTokens = ctx.getPathTokens();
    String msgId = pathTokens.get("msgId");
    String data = pathTokens.get("data");
    Message msg = new Message(msgId, data, Message.Status.Accepted);
    inMemoryDb.insert(msgId, msg);
    cache.insertStatus(msgId, msg.getStatus());
    ctx.getResponse().send("thank you for your message");
  }

  private void getStatus(Context ctx) throws ExecutionException {
    PathTokens pathTokens = ctx.getPathTokens();
    String msgId = pathTokens.get("msgId");
    Message.Status status = cache.getStatus(msgId);
    ctx.getResponse().send(status.name());
  }

  @Override
  public void run() {
    try {
      RatpackServer.start(server -> server
          .handlers(chain -> chain
              .get(ctx -> ctx.render("Hey, which message would you like to process?"))
              .get("ingestMsg/:msgId/:data", ctx -> ctx.byMethod(m -> m.get(() -> ingest(ctx))))
              .get("getStatus/:msgId", ctx -> ctx.byMethod(m -> m.get(() -> getStatus(ctx)))
              )
          ));
    } catch (Exception e) {
      logger.info("Exception " + e + "running ratPackServer");
      e.printStackTrace();
    }
  }
}
