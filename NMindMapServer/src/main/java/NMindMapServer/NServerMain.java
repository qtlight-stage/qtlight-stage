package NMindMapServer;

import javax.json.JsonObject;
import java.io.File;
import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by sasch on 5/7/2016.
 */
public class NServerMain {
    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        String path = "data.json";

        File file = new File(path);
        final NServerData data;
        if (file.exists()) {
            System.out.print("Loaded the saved file.\r\n");
            JsonObject dataJson = NFileManager.loadJson(file);
            data = NServerData.fromJson(dataJson);
        } else {
            System.out.print("Created a new data.\r\n");
            data = new NServerData();
        }
        NServerDataManager dataManager = new NServerDataManager(data);

        List<AsynchronousSocketChannel> list = new LinkedList<>();
        NServerConnectionManager.acceptConnection((AsynchronousSocketChannel channel, JsonObject json) -> {
            if (!list.contains(channel)) {
                list.add(channel);
            }
            System.out.print(json.toString());
            JsonObject result = dataManager.processCommand(json);
            if (result != null) {
                for (AsynchronousSocketChannel ch : list) {
                    if (ch.isOpen()) {
                        NServerConnectionManager.sendJson(ch, result);
                    }
                }
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.print("Saving file...");
                try {
                    NFileManager.saveJson(data.toJson(), path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
