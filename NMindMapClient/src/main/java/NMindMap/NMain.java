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
    static NFrame mainFrame = new NFrame(frameWidth, frameHeight);

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        NCommandReceiver receiver = new NCommandReceiver();

        NConnectionManager.connectToServer(new InetSocketAddress("localhost", 8080), data -> {
            System.out.print(data.toString());
            receiver.processCommand(mainFrame, data);
        });

        NCommandSender sender = new NCommandSender();

        mainFrame.setMain(sender);
        mainFrame.setSize(frameWidth, frameHeight);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);

        NConnectionManager.sendJson(Json.createReader(new StringReader("{\"type\": \"refresh\"}")).readObject());
    }

}