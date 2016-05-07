package NMindMap;

import javax.json.Json;
import javax.swing.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.ExecutionException;

public class NMain {
    static int frameWidth = 1000;
    static int frameHeight = 500;
    static NFrame mainFrame = new NFrame(frameWidth, frameHeight);

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        NCommandReceiver receiver = new NCommandReceiver();

        NConnectionManager.connectToServer(data -> {
            System.out.print(data.toString());
            receiver.processCommand(mainFrame, data);
        });

        NCommandSender sender = new NCommandSender();

        mainFrame.setMain(sender);
        mainFrame.setSize(frameWidth, frameHeight);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);

        NConnectionManager.sendJson(Json.createReader(new StringReader("{\"type\": \"refresh\"}")).readObject());
        // 아래부분에 네트워크 통신 부분 필요
        /*long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();
		int x = 100;
		while (true) {
			end = System.currentTimeMillis();
			if (end - start > 5000) {
				C.commandAddNode(mainFrame, "test", x, x, 120, 25);
				System.out.println("hello");
				start = end;
				x = x + 20;
			}

		}*/
    }

}