package NMindMapServer;

import javax.json.JsonObject;
import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

/**
 * Created by sasch on 5/7/2016.
 */
public class NServerMain {
    public static NServerDataManager dataManager = new NServerDataManager();
    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        NServerConnectionManager.acceptConnection((AsynchronousSocketChannel channel, JsonObject json) -> {
            System.out.print(json.toString());
            JsonObject result = dataManager.processCommand(json);
            if (result != null) {
                NServerConnectionManager.sendJson(channel, result);
            }
        });
    }
}
