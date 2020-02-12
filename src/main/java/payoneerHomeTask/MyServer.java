package payoneerHomeTask;

import ratpack.handling.Context;
import ratpack.path.PathTokens;
import ratpack.server.RatpackServer;

import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServer implements Runnable {
  private static DBWrapper dbWrapper = DBWrapper.getInstance();

  public static void main(String[] args) {
    int coreCount = Runtime.getRuntime().availableProcessors();
    ExecutorService pools = Executors.newFixedThreadPool(coreCount);
    Runnable server = new MyServer();

    TimerTask process = new payoneerHomeTask.Process();
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(process, new Date(),1000);

    pools.execute(server);
  }

  private static void handleMsg(Context ctx) {
    PathTokens pathTokens = ctx.getPathTokens();
    String msgId = pathTokens.get("msgId");
    String data = pathTokens.get("data");
    DBWrapper.Message msg = new DBWrapper.Message(msgId, data, DBWrapper.Message.Status.Accepted);
    dbWrapper.insert(msgId, msg);
    ctx.getResponse().send("thank you for your message");
  }

  private static void getStatus(Context ctx) {
    PathTokens pathTokens = ctx.getPathTokens();
    String msgId = pathTokens.get("msgId");
    DBWrapper.Message.Status status = Objects.isNull(dbWrapper.get(msgId)) ?
        DBWrapper.Message.Status.NotFound : dbWrapper.get(msgId).status;
    ctx.getResponse().send(status.name());
  }

  @Override
  public void run() {
    try {
      RatpackServer.start(server -> server
          .handlers(chain -> chain
              .get(ctx -> ctx.render("Hey, which message would you like to process?"))
              .get("ingestMsg/:msgId/:data", ctx -> ctx.byMethod(m -> m.get(() -> handleMsg(ctx))))
              .get("getStatus/:msgId", ctx -> ctx.byMethod(m -> m.get(() -> getStatus(ctx)))
              )
          ));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
