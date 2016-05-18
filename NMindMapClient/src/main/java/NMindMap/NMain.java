package NMindMap;

import javax.json.Json;
import javax.swing.*;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

public class NMain {
    static int frameWidth = 1000;
    static int frameHeight = 500;

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        NCommandReceiver receiver = new NCommandReceiver();
        NCommandSender sender = new NCommandSender();
        NFrame mainFrame = new NFrame(frameWidth, frameHeight);
        mainFrame.listenSetIp(str -> {
            try {
                System.out.print("Connecting to " + str);
                NConnectionManager.connectToServer(new InetSocketAddress(str, 8080), data -> {
                    System.out.print(data.toString());
                    receiver.processCommand(mainFrame, data);
                });
                NConnectionManager.sendJson(Json.createReader(new StringReader("{\"type\": \"refresh\"}")).readObject());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        mainFrame.setMain(sender);
        mainFrame.setSize(frameWidth, frameHeight);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

}