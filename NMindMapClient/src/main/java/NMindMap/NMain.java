package NMindMap;

import javax.json.Json;
import javax.swing.*;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

public class NMain {
    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        int frameWidth = 1000;
        int frameHeight = 500;

        NCommandReceiver receiver = new NCommandReceiver();
        NCommandSender sender = new NCommandSender();
        NFrame mainFrame = new NFrame(frameWidth, frameHeight);
        mainFrame.listenSetIp(str -> {
            try {
                System.out.print("Connecting to " + str);
                NConnectionManager.connectToServer(new InetSocketAddress(str, 8080), data -> {
                    System.out.print(data.toString());
                    if (!data.getString("type").equals("error")) {
                        receiver.processCommand(mainFrame, data);
                    }
                    else {
                        sender.commandRefresh();
                    }
                });
                sender.commandRefresh();
            } catch (IOException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        mainFrame.setMain(sender);
        mainFrame.setSize(frameWidth, frameHeight);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

}